package com.sandu.cloud.pay.service.impl.forward.ali;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.google.gson.Gson;
import com.jpay.alipay.AliPayApi;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.AliPayConfig;
import com.sandu.cloud.pay.model.TradeRecord;

import lombok.extern.slf4j.Slf4j;

@Service("aliAppPay")
@Slf4j
public class AliAppPayServiceImpl extends AliPayServiceImpl {

    @Override
	protected Object buildPayParameter(PayParamDto payParam) {
    	super.setAliPayApiConfig(payParam.getAppId());
    	AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(payParam.getTradeDesc());
		model.setSubject(payParam.getTradeDesc());
		model.setOutTradeNo(payParam.getPayTradeNo());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(String.valueOf((payParam.getTotalFee().doubleValue()/100)));
		model.setProductCode("QUICK_MSECURITY_PAY");
		if (payParam.getPassbackParams() != null){
			model.setPassbackParams(payParam.getPassbackParams());
		}
		return model;
	}
    
    @Override
	protected String executePay(Object params) {
		try {
			String result = AliPayApi.startAppPay((AlipayTradeAppPayModel)params, AliPayConfig.PAY_NOTIFY_URL);
			return result;
		} catch (AlipayApiException e) {
			log.error("阿里app支付异常:",e);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_APP_TRADE_RESULT_ERROR);
		}
	}
    
    @Override
   	protected Integer getPayMethod() {
   		return TradeRecord.PAY_METHOD_AL_APPPAY;
   	}
    
	@Override
	protected String packageResult(String payTradeNo,String jsonResult) {
		Map<String,String> map= new HashMap<>();
		map.put("form",jsonResult);
		map.put("payTradeNo",payTradeNo);
    	return new Gson().toJson(map);
	}

	
	
	
}
