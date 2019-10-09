package com.sandu.cloud.pay.service.impl.forward.ali;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.jpay.alipay.AliPayApi;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.AliPayConfig;
import com.sandu.cloud.pay.model.TradeRecord;

import lombok.extern.slf4j.Slf4j;

@Service("aliH5Pay")
@Slf4j
public class AliH5PayServiceImpl extends AliPayServiceImpl {
	
    private static final ThreadLocal<String> TL = new ThreadLocal<String>();
    @Override
	protected Object buildPayParameter(PayParamDto payParam) {
    	super.setAliPayApiConfig(payParam.getAppId());
    	AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody(payParam.getTradeDesc());
		model.setSubject(payParam.getTradeDesc());
		model.setOutTradeNo(payParam.getPayTradeNo());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(String.valueOf((payParam.getTotalFee().doubleValue()/100)));
		model.setQuitUrl(payParam.getRedirectUrl());
		model.setProductCode("QUICK_WAP_PAY");
		if (payParam.getPassbackParams() != null){
			model.setPassbackParams(payParam.getPassbackParams());
		}
		TL.set(payParam.getRedirectUrl());
		return model;
	}
    
    @Override
	protected String executePay(Object params) {
		try {
			String redirectUrl = TL.get();
			String result = AliPayApi.wapPayStr(null, (AlipayTradeWapPayModel)params, redirectUrl,AliPayConfig.PAY_NOTIFY_URL);
			return result;
		} catch (AlipayApiException | IOException e) {
			log.error("阿里H5支付异常:",e);			 
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ALI_H5_ERROR);
		}
	}
    
    @Override
   	protected Integer getPayMethod() {
   		return TradeRecord.PAY_METHOD_AL_H5PAY;
   	}
    
	@Override
	protected String packageResult(String payTradeNo,String jsonResult) {
		Map<String, String> packageParams = new HashMap<String, String>();
	 	packageParams.put("payTradeNo", payTradeNo);
	 	packageParams.put("mwebUrl", jsonResult);
		return JsonUtils.toJson(packageParams);
	}

	
	
	
}
