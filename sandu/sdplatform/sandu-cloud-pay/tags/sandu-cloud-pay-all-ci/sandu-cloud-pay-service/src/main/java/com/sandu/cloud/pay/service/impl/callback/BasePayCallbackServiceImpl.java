package com.sandu.cloud.pay.service.impl.callback;

import java.util.HashMap;
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

import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.TradeRecord;
import com.sandu.cloud.pay.service.TradeRecordLogService;
import com.sandu.cloud.pay.service.TradeRecordService;
import com.sandu.cloud.pay.service.callback.PayCallbackService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePayCallbackServiceImpl implements PayCallbackService{
	
	@Resource
    private TradeRecordService payTradeRecordService ;
    
    @Resource
    private TradeRecordLogService payTradeRecordLogService ;
    
    @Autowired
    private RestTemplate restTemplate;
    
  //  @Transactional
    @Override
	public boolean callback(Object notifyBody) {
    	boolean success = false;
    	//step0.验证签名
    	boolean signFlag = verifySignature(notifyBody);
    //	boolean signFlag = true;
    	if(signFlag) {
			Map<String,String> params = getNotifyParams(notifyBody);
			if(params==null) return false;
			log.info("notifyBody:{}",notifyBody);
    		String resultCode = params.get("resultCode");
    		String payTradeNo = params.get("payTradeNo");//支付网关交易号
    		String extenalTradeNo = params.get("extenalTradeNo");
    		String totalFee = params.get("totalFee"); //交易金额
			String passbackParams = params.get("passbackParams");//回调调起支付系统所需参数

			log.info("回调调起支付系统所需参数 =>{}"+passbackParams);

    		//step1.验证交易金额
    		TradeRecord trade = getTrade(payTradeNo,JsonUtils.toJson(params));
    		if(trade.getTotalFee().longValue()!=Long.valueOf(totalFee).longValue()) {
    			log.error("交易金额不正确!原金额{},接收金额:{}",trade.getTotalFee().longValue(),Long.valueOf(totalFee).longValue());
    			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_TOTAL_FEE_ERROR);
    		}
        	if(lockRecord(payTradeNo)) {
        		//step2.记录外部系统(微信,支付宝)通知日志
    			saveExternalNotifyBody(trade.getId(), (notifyBody instanceof String)?(String)notifyBody:JsonUtils.toJson(notifyBody));
    			
    			//step3.保存外部系统(微信,支付宝)交易号及更新交易状态为成功
    			modifyExtenalTradeNoAndStatus(trade.getId(),extenalTradeNo,"SUCCESS".equals(resultCode)?TradeRecord.STATUS_SUCCESS:TradeRecord.STATUS_FAILURE);

    			//step4.获取通知内部系统所需参数
    			Map<String,String> internalNotifyParams = this.getInternalSystemNotifyParams("SUCCESS".equals(resultCode), trade,passbackParams);
    			
    			//step5.通知内部系统(订单,充值等)
    			String retCode = notifyInternalSystem(trade.getNotifyUrl(), internalNotifyParams);
    			
    			//step6.记录内部系统通知日志
    			saveInternalSystemNotifyLog(trade.getId(), JsonUtils.toJson(internalNotifyParams),retCode);
        		
    			//step7.更新内部系统通知结果
    			modifyInternalNotifyResult(trade.getId(),"SUCCESS".equalsIgnoreCase(retCode)?TradeRecord.NOTIFY_RESULT_SUCCESS:TradeRecord.NOTIFY_RESULT_FAILURE);
    			success = true;
    		}else {
    			log.error("重复处理:"+payTradeNo);
    			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_TRADE_REPEAT_PROCESS_ERROR);
    		}
    	}else {
    		log.error("签名错误:",(notifyBody instanceof String)?(String)notifyBody:JsonUtils.toJson(notifyBody)); 
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_SIGN_ERROR);
		}
    	return success;
    }
    
    private boolean lockRecord(String payTradeNo) {
    	int count = payTradeRecordService.changeToProcessStatus(payTradeNo);
		return count>0?true:false;
	}

	    
    private Map<String,String> getInternalSystemNotifyParams(boolean success, TradeRecord trade, String passbackParams) {
    	Map<String,String> notifyParams = new HashMap<String, String>();
    	if (success) {
			success = true;
			notifyParams.put("resultCode", "SUCCESS");
			notifyParams.put("resultMsg", "OK");
		}else {
			notifyParams.put("resultCode", "FAIL");
			notifyParams.put("resultMsg", "FAIL");
		}
    	notifyParams.put("payTradeNo", trade.getPayTradeNo());
		notifyParams.put("intenalTradeNo", trade.getIntenalTradeNo());
		notifyParams.put("totalFee", trade.getTotalFee().toString());
		notifyParams.put("tradeDesc", trade.getTradeDesc());
		notifyParams.put("appId", trade.getAppId());
		notifyParams.put("passbackParams",passbackParams);
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

	public TradeRecord getTrade(String payTradeNo,String notifyBody) {
		if(StringUtils.isNotBlank(payTradeNo)) {
			TradeRecord trade = payTradeRecordService.getTradeRecord(payTradeNo);
			if(trade == null) {
				log.error("交易不存在:{}",notifyBody);
				throw new PayBizException(PayExceptionCode.SD_ERR_PAY_TRADE_NOT_EXIST);
			}
			return trade;
		}else {
			log.error("交易不存在:{}",notifyBody);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_TRADE_NOT_EXIST);
		}
		
	}
	
	public void saveExternalNotifyBody(Long tradeId, String notifyBody) {
		log.info("记录外部系统(微信,支付宝)通知日志:{}",notifyBody);
		payTradeRecordLogService.saveExternalNotifyBody(tradeId,notifyBody);
	}
	
	public void modifyExtenalTradeNoAndStatus(Long tradeId, String extenalTradeNo,Integer status) {
		log.info("保存外部系统(微信,支付宝)交易号及更新交易状态为成功:{}",extenalTradeNo);
		payTradeRecordService.modifyExtenalTradeNoAndStatus(tradeId,extenalTradeNo,status);
	}
	
	public void saveInternalSystemNotifyLog(Long tradeId, String request, String response) {
		log.info("记录内部系统通知日志:{}",tradeId);
		payTradeRecordLogService.saveInternalSystemNotifyLog(tradeId, request,response);
	}
	
	public void modifyInternalNotifyResult(Long tradeId, Integer notifyResult) {
		log.info("更新内部系统通知结果:{}",tradeId);
		payTradeRecordService.modifyInternalNotifyResult(tradeId,notifyResult);
	}
	
}
