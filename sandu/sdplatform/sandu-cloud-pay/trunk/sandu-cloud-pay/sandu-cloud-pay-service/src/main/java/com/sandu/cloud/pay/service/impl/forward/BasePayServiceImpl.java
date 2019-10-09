package com.sandu.cloud.pay.service.impl.forward;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.sandu.cloud.common.exception.BizException;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TradeQueryDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.TradeRecord;
import com.sandu.cloud.pay.model.TradeRecordLog;
import com.sandu.cloud.pay.model.TradeRefundRecord;
import com.sandu.cloud.pay.model.TradeRefundRecordLog;
import com.sandu.cloud.pay.model.TradeTransfersRecord;
import com.sandu.cloud.pay.model.TradeTransfersRecordLog;
import com.sandu.cloud.pay.service.TradeRecordLogService;
import com.sandu.cloud.pay.service.TradeRecordService;
import com.sandu.cloud.pay.service.TradeRefundRecordLogService;
import com.sandu.cloud.pay.service.TradeRefundRecordService;
import com.sandu.cloud.pay.service.TradeTransfersRecordLogService;
import com.sandu.cloud.pay.service.TradeTransfersRecordService;
import com.sandu.cloud.pay.service.forward.PayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePayServiceImpl implements PayService {
    
    @Resource
    private TradeRecordService payTradeRecordService ;
    
    @Resource
    private TradeRecordLogService payTradeRecordLogService ;
    
    @Resource
    private TradeRefundRecordService payTradeRefundRecordService ;
    
    @Resource
    private TradeRefundRecordLogService payTradeRefundRecordLogService ;
    
    @Resource
    private TradeTransfersRecordService payTradeTransfersRecordService;
    
    @Resource
    private TradeTransfersRecordLogService payTradeTransfersRecordLogService;
    
    private DateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public String doPay(@Valid PayParamDto payParam){
        //step2.构建接口参数
    	String payTradeNo = getPayTradeNo();
    	payParam.setPayTradeNo(payTradeNo);
    	Object params = this.buildPayParameter(payParam);
		log.info("公共回传参数 =>{}",payParam.getPassbackParams());
		log.info("支付参数 =>{}",params.toString());

        //step3.记录交易流水
        TradeRecord tradeRecord = addTradeRecord(payParam);
        //step4.调用第三方支付接口:微信是xml,支付宝是json
        String xmlOrJsonResult = this.executePay(params);
        
        //step5.保存支付返回结果
        saveExcuteLog(tradeRecord,JsonUtils.toJson(params),xmlOrJsonResult);
        
        //step6.封装执行结果返回给调用方
        String jsonResult = packageResult(payTradeNo,xmlOrJsonResult);
        
        return jsonResult;
    }

   
    
    protected abstract Integer getPayMethod();
    
    private String getPayTradeNo() {
    	String nowStr = df.format(new Date());
    	// source+nowStr+(10000000000L-userId);
    	return nowStr + RandomStringUtils.randomNumeric(15);
    }
    
    private String getRefundTradeNo() {
    	String nowStr = df.format(new Date());
    	//return "RF"+source+nowStr+(10000000000L-userId);
    	return "RF"+nowStr + RandomStringUtils.randomNumeric(15);
    }
    
    private TradeRecord addTradeRecord(PayParamDto payParam) {
    	Date now = new Date();
    	TradeRecord trade = new TradeRecord();
    	trade.setIntenalTradeNo(payParam.getIntenalTradeNo());
    	trade.setPayTradeNo(payParam.getPayTradeNo());
    	trade.setTradeDesc(payParam.getTradeDesc());
    	trade.setTotalFee(payParam.getTotalFee());
    	trade.setTradeDate(now);
    	trade.setTradeType(TradeRecord.TRADE_TYPE_PAY);
    	trade.setPayMethod(getPayMethod()); 
    	trade.setNotifyUrl(payParam.getNotifyUrl());
    	trade.setSign(payParam.getSign());
    	trade.setStatus(TradeRecord.STATUS_BEGIN);
    	trade.setAppId(payParam.getAppId());
    	trade.setGmtCreate(now);
    	trade.setGmtModified(now);
    	trade.setIsDeleted(0);
    	payTradeRecordService.addPayTradeRecord(trade);
    	return trade;
    }
    
    protected abstract Object buildPayParameter(PayParamDto payParam);

    protected abstract String executePay(Object params);

    private void saveExcuteLog(TradeRecord tradeRecord, String request, String response) {
    	Date now = new Date();
    	TradeRecordLog log = new TradeRecordLog();
    	log.setTradeId(tradeRecord.getId());
    	log.setExternalRequest(request);
    	log.setExternalResponse(response);
    	log.setGmtCreate(now);
    	log.setGmtModified(now);
    	log.setIsDeleted(0);
    	payTradeRecordLogService.addPayTradeRecordLog(log);
    }
    
    protected abstract String packageResult(String payTradeNo,String xmlResult);

    protected abstract String updateAndPackageRefundResult(TradeRefundRecord tradeRefundRecord,String xmlResult);
    
    
    public String doRefund(RefundParamDto refundParam){
    	//step1.参数验证
    	validateParameter(refundParam);
    	
    	//生成退款交易号
    	String refundNo = this.getRefundTradeNo();
       
    	//step2.构建接口参数
    	refundParam.setPayRefundNo(refundNo);
    	Object params = this.buildRefundParameter(refundParam);
    	
        //step3.记录交易流水
    	TradeRefundRecord tradeRefundRecord = addTradeRefundRecord(refundParam);
        
        //step4.调用第三方支付接口:微信是xml,支付宝是json
        String xmlOrJsonResult = this.executeRefund(params);
        
        //step5.保存支付返回结果
        saveExcuteRefundLog(tradeRefundRecord,JsonUtils.toJson(params),xmlOrJsonResult);
       
        //step6.更新返回信息及封装执行结果返回给调用方
        String jsonResult =  updateAndPackageRefundResult(tradeRefundRecord,xmlOrJsonResult);
        
        return jsonResult;
    }
    
    protected abstract Object buildRefundParameter(RefundParamDto refundParam);
    
    
    
    private void validateParameter(RefundParamDto refundParam) {
     	if(StringUtils.isBlank(refundParam.getOriginInternalTradeNo())
     			&& StringUtils.isBlank(refundParam.getOriginPayTradeNo())) {
       		throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ORIGIN_TRADE_NO_AND_PAY_NO_ARE_NULL);
       	}
     	
    }
    
    private TradeRefundRecord addTradeRefundRecord(RefundParamDto refundParam) {
    	Date now = new Date();
    	TradeRefundRecord tradeRefund = new TradeRefundRecord();
    	TradeRecord originTrade = this.getPayTradeRecord(refundParam);
    	if(originTrade==null) {
    		throw new BizException("原交易不存在!");
    	}
    	tradeRefund.setOriginPayTradeNo(originTrade.getPayTradeNo());
    	tradeRefund.setOriginInternalTradeNo(originTrade.getIntenalTradeNo());
    	tradeRefund.setOriginExternalTradeNo(originTrade.getExtenalTradeNo());
    	tradeRefund.setOriginTradeDate(originTrade.getTradeDate());
    	tradeRefund.setOriginTradeDesc(originTrade.getTradeDesc());
    	tradeRefund.setOriginTotalFee(originTrade.getTotalFee());
    	tradeRefund.setInternalRefundNo(refundParam.getInternalRefundNo());
    	tradeRefund.setPayRefundNo(refundParam.getPayRefundNo());
    	//tradeRefund.setExternalRefundNo();
    	if(refundParam.getTotalFee().longValue()!=originTrade.getTotalFee().longValue()) {
    		throw new BizException("原交易金额不正确!");
    	}
    	tradeRefund.setRefundFee(refundParam.getRefundFee());
    	tradeRefund.setRefundDesc(refundParam.getRefundDesc());
    	tradeRefund.setNotifyUrl(refundParam.getNotifyUrl());
    	tradeRefund.setSign(refundParam.getSign());
    	tradeRefund.setStatus(TradeRefundRecord.STATUS_BEGIN);
    	tradeRefund.setAppId(refundParam.getAppId());
    	tradeRefund.setGmtCreate(now);
    	tradeRefund.setGmtModified(now);
    	tradeRefund.setIsDeleted(0);
    	payTradeRefundRecordService.add(tradeRefund);
    	return tradeRefund;
    }
    
    private TradeRecord getPayTradeRecord(RefundParamDto refundParam) {
    	TradeQueryDto queryVo = new TradeQueryDto();
    	if(StringUtils.isNotBlank(refundParam.getOriginInternalTradeNo())) {
    		queryVo.setIntenalTradeNo(refundParam.getOriginInternalTradeNo());
    	}else if(StringUtils.isNotBlank(refundParam.getOriginPayTradeNo())) {
    		queryVo.setPayTradeNo(refundParam.getOriginPayTradeNo());
    	}else {
    		return null;
    	}
    	
    	List<TradeRecord> list = payTradeRecordService.getList(queryVo);
    	
    	if(list!=null && list.size()>0) {
    		return list.get(0);
    	}
    	return null;
    }
    
    protected abstract String executeRefund(Object params);
    
    private void saveExcuteRefundLog(TradeRefundRecord tradeRefundRecord, String request, String response) {
    	Date now = new Date();
    	TradeRefundRecordLog log = new TradeRefundRecordLog();
    	log.setTradeRefundId(tradeRefundRecord.getId());
    	log.setExternalRequest(request);
    	log.setExternalResponse(response);
    	log.setGmtCreate(now);
    	log.setGmtModified(now);
    	log.setIsDeleted(0);
    	payTradeRefundRecordLogService.add(log);
    }
  
    public String doTransfers(TransfersParamDto transfersParam){ 
        //step2.构建接口参数
    	String payTradeNo = getPayTradeNo();
    	transfersParam.setPayTradeNo(payTradeNo);
    	Object params = this.buildTransfersParameter(transfersParam);

        //step3.记录交易流水
        TradeTransfersRecord tradeTransfersRecord = addTradeTransfersRecord(transfersParam);
        
        //step4.调用第三方支付接口:微信是xml,支付宝是json
        String xmlOrJsonResult = this.executeTransfers(params);
        
        //step5.保存支付返回结果
        saveExcuteLog(tradeTransfersRecord,JsonUtils.toJson(params),xmlOrJsonResult);
        
        //step6.封装执行结果返回给调用方
        String jsonResult = updateAndPackageTransfersResult(tradeTransfersRecord,xmlOrJsonResult);
        
        return jsonResult;
    }
    
    protected abstract String updateAndPackageTransfersResult(TradeTransfersRecord tradeTransfersRecord, String xmlOrJsonResult);

	private void saveExcuteLog(TradeTransfersRecord transfersRecord, String request, String response) {
    	Date now = new Date();
    	TradeTransfersRecordLog log = new TradeTransfersRecordLog();
    	log.setTradeTransfersId(transfersRecord.getId());
    	log.setExternalRequest(request);
    	log.setExternalResponse(response);
    	log.setGmtCreate(now);
    	log.setGmtModified(now);
    	log.setIsDeleted(0);
    	payTradeTransfersRecordLogService.add(log);
    }
    
    protected abstract String executeTransfers(Object params);

	private TradeTransfersRecord addTradeTransfersRecord(TransfersParamDto transfersParam) {
    	Date now = new Date();
    	TradeTransfersRecord tansfersRecord = new TradeTransfersRecord();
    	tansfersRecord.setPayTradeNo(transfersParam.getPayTradeNo());
    	tansfersRecord.setIntenalTradeNo(transfersParam.getIntenalTradeNo());
    	//tansfersRecord.setExtenalTradeNo();
    	tansfersRecord.setPayMethod(getPayMethod());
    	tansfersRecord.setAmount(transfersParam.getAmount());
    	tansfersRecord.setTradeDate(now);
    	tansfersRecord.setTradeDesc(transfersParam.getTradeDesc());
    	tansfersRecord.setOpenId(transfersParam.getOpenId());
    	tansfersRecord.setSign(transfersParam.getSign());
    	tansfersRecord.setStatus(TradeTransfersRecord.STATUS_BEGIN);
    	//tansfersRecord.setPaymentTime(paymentTime);
    	tansfersRecord.setIp(transfersParam.getIp());
    	tansfersRecord.setClientId(transfersParam.getClientId());
    	tansfersRecord.setAppId(transfersParam.getAppId());
    	tansfersRecord.setGmtCreate(now);
    	tansfersRecord.setGmtModified(now);
    	tansfersRecord.setIsDeleted(0);
    	//tansfersRecord.setRemark(remark);
    	payTradeTransfersRecordService.add(tansfersRecord);
		return tansfersRecord;
	}

	protected abstract Object buildTransfersParameter(TransfersParamDto transfersParam);
    
}
