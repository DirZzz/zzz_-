package com.sandu.cloud.pay.service.impl.callback;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sandu.cloud.common.exception.BizException;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.common.vo.ResponseEnvelope;
import com.sandu.cloud.pay.dto.TradeRefundQueryDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.TradeRefundRecord;
import com.sandu.cloud.pay.service.TradeRefundRecordLogService;
import com.sandu.cloud.pay.service.TradeRefundRecordService;
import com.sandu.cloud.pay.service.callback.RefundCallbackService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseRefundCallbackServiceImpl implements RefundCallbackService{	
	
    @Autowired
    private RestTemplate restTemplate;
    
	@Resource
    private TradeRefundRecordService payTradeRefundRecordService ;
    
    @Resource
    private TradeRefundRecordLogService payTradeRefundRecordLogService ;
    
    @Transactional
    @Override
	public boolean callback(Object notifyBody) {
    	boolean success = false;
    	//step0.验证签名
  //  	boolean signFlag = verifySignature(notifyBody);
    	boolean signFlag = true; //无需验证签名.回调报文已加密,下面会有解密操作
    	if(signFlag) {
    		Map<String,String> params = getNotifyParams(notifyBody);
			String resultCode = params.get("refundStatus");
			String payRefundNo = params.get("payRefundNo");
        	if(lockRecord(payRefundNo)) {
        		
        		TradeRefundRecord tradeRefund = getTradeRefundRecord(payRefundNo);
        		
        		//step2.记录外部系统(微信,支付宝)通知日志
    			saveExternalNotifyBody(tradeRefund.getId(), (notifyBody instanceof String)?(String)notifyBody:JsonUtils.toJson(notifyBody));
    			
    			//step3.保存外部系统(微信,支付宝)退款号及更新交易状态为成功
    			modifyTradeRefundInfo(tradeRefund.getId(),params);

    			//step4.获取通知内部系统所需参数
    			Map<String,String> internalNotifyParams = this.getInternalSystemNotifyParams("SUCCESS".equals(resultCode), tradeRefund,params);
    			
    			//step5.通知内部系统(订单,充值等)
    			String retCode = notifyInternalSystem(tradeRefund.getNotifyUrl(), internalNotifyParams);
    			
    			//step6.记录内部系统通知日志
    			saveInternalSystemNotifyLog(tradeRefund.getId(), JsonUtils.toJson(internalNotifyParams),retCode);
        		
    			//step7.更新内部系统通知结果
    			modifyInternalNotifyResult(tradeRefund.getId(),"SUCCESS".equalsIgnoreCase(retCode)?TradeRefundRecord.NOTIFY_RESULT_SUCCESS:TradeRefundRecord.NOTIFY_RESULT_FAILURE);
    			success = true;
    		}else {
    			log.error("重复处理:{}",payRefundNo);
    			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_TRADE_REPEAT_PROCESS_ERROR);
    			
    		}
    	}else {
    		log.error("签名错误:",(notifyBody instanceof String)?(String)notifyBody:JsonUtils.toJson(notifyBody)); 
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_SIGN_ERROR);
		}
    	return success;
    }
    
    private TradeRefundRecord getTradeRefundRecord(String payRefundNo) {
    	if(StringUtils.isBlank(payRefundNo)) {
    		throw new BizException("交易不存在");
    	}
    	TradeRefundQueryDto queryVo = new TradeRefundQueryDto();
    	queryVo.setPayRefundNo(payRefundNo);
    	List<TradeRefundRecord> list = payTradeRefundRecordService.getList(queryVo);
    	if(list!=null && list.size()>0) {
    		return list.get(0);
    	}
    	throw new BizException("交易不存在");
    }
    
    private boolean lockRecord(String payRefundNo) {
    	int count = payTradeRefundRecordService.changeToProcessStatus(payRefundNo);
		return count>0?true:false;
	}

	private boolean getInternalSystemNotifyResult(ResponseEnvelope ret) {
    	String code = ret.getcode();
		if("SUCCESS".equalsIgnoreCase(code)) {
			 return true;
		}
		return false;
    }
	 
	
    
    private Map<String,String> getInternalSystemNotifyParams(boolean success, TradeRefundRecord tradeRefund,Map<String,String> params) {
    	Map<String,String> notifyParams = new HashMap<String, String>();
    	if (success) {
			success = true;
			notifyParams.put("resultCode", "SUCCESS");
			notifyParams.put("resultMsg", "OK");
		}else {
			notifyParams.put("resultCode", "FAIL");
			notifyParams.put("resultMsg", "FAIL");
		}
    	String refundStatus = params.get("refundStatus");
		String successTime = params.get("successTime");
    	notifyParams.put("intenalTradeNo", tradeRefund.getOriginInternalTradeNo()); 
    	notifyParams.put("internalRefundNo", tradeRefund.getInternalRefundNo());
    	notifyParams.put("payTradeNo", tradeRefund.getOriginPayTradeNo());
    	notifyParams.put("payRefundNo", tradeRefund.getPayRefundNo());
		notifyParams.put("refundFee", tradeRefund.getRefundFee().toString());
		notifyParams.put("refundDesc", tradeRefund.getRefundDesc());
		notifyParams.put("refundStatus", refundStatus);
		notifyParams.put("successTime", successTime);
		notifyParams.put("appId", tradeRefund.getAppId());
    	return notifyParams;
    }

    
    public abstract Map<String, String> getNotifyParams(Object notifyBody);

	public abstract boolean verifySignature(Object notifyBody);
    
	protected String notifyInternalSystem(String notifyUrl,Map<String,String> notifyParams) {
		log.info("开始通知内部系统:{}",notifyUrl);
		String result = "";
		try {
			HttpHeaders headers = new HttpHeaders();//header参数
			MultiValueMap<String,String> param = new LinkedMultiValueMap<String, String>();
			for (Map.Entry<String, String> entry : notifyParams.entrySet()) { 
				 param.add(entry.getKey(),entry.getValue());
			}
	        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<MultiValueMap<String,String>>(param,headers);//将参数和header组成一个请求
	        result = restTemplate.postForObject(notifyUrl,request,String.class);
			log.info("通知内部系统返回:{}",result);
		}catch(Exception ex) {
			result = ex.getMessage();
			log.error("通知内部系统异常:",ex);
		}
		return result;
		
	}
	
	public void saveExternalNotifyBody(Long tradeRefundId, String notifyBody) {
		log.info("记录外部系统(微信,支付宝)通知日志:"+notifyBody);
		payTradeRefundRecordLogService.saveExternalNotifyBody(tradeRefundId,notifyBody);
	}
	
	public void modifyTradeRefundInfo(Long tradeRefundId, Map<String,String> params) {
		String resultCode = params.get("refundStatus");
		TradeRefundRecord payTradeRefundRecord = new TradeRefundRecord();
		payTradeRefundRecord.setId(tradeRefundId);
		payTradeRefundRecord.setExternalRefundNo(params.get("externalRefundNo"));
		payTradeRefundRecord.setRefundStatus(params.get("refundStatus"));
		payTradeRefundRecord.setRefundSuccessTime(params.get("successTime"));
		payTradeRefundRecord.setStatus("SUCCESS".equals(resultCode)?TradeRefundRecord.STATUS_SUCCESS:TradeRefundRecord.STATUS_FAILURE);
		payTradeRefundRecord.setGmtModified(new Date());
		payTradeRefundRecordService.modify(payTradeRefundRecord);
	}
	
	public void saveInternalSystemNotifyLog(Long tradeRefundId, String request, String response) {
		log.info("记录内部系统通知日志:"+tradeRefundId);
		payTradeRefundRecordLogService.saveInternalSystemNotifyLog(tradeRefundId, request,response);
	}
	
	public void modifyInternalNotifyResult(Long tradeRefundId, Integer notifyResult) {
		log.info("更新内部系统通知结果:"+tradeRefundId);
		TradeRefundRecord payTradeRefundRecord = new TradeRefundRecord();
		payTradeRefundRecord.setId(tradeRefundId);
		payTradeRefundRecord.setNotifyResult(notifyResult);
		payTradeRefundRecord.setGmtModified(new Date());
		payTradeRefundRecordService.modify(payTradeRefundRecord);
	}
	
}
