package com.sandu.cloud.pay.service.impl.callback.wx;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jpay.ext.kit.PaymentKit;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.model.WxPayConfig;
import com.sandu.cloud.pay.service.TradeRecordLogService;
import com.sandu.cloud.pay.service.TradeRecordService;
import com.sandu.cloud.pay.service.impl.callback.BaseRefundCallbackServiceImpl;
import com.sandu.cloud.pay.util.AESTool;

import lombok.extern.slf4j.Slf4j;

@Service("wxRefundCallbackService")
@Slf4j
public class WxRefundCallbackServiceImpl extends BaseRefundCallbackServiceImpl{

	
	@Resource
    private TradeRecordService payTradeRecordService ;
    
    @Resource
    private TradeRecordLogService payTradeRecordLogService ;
    
    @Override
    public boolean verifySignature(Object notifyBody) {
    	Map<String, String> params = PaymentKit.xmlToMap(notifyBody.toString());
    	return PaymentKit.verifyNotify(params, AppClientConfig.getWxPayConfig(params.get("appid")).getMchKey());
		//return PaymentKit.verifyNotify(params, PayConfig.WX_MCH_KEY);
    }
    
    @Override
    public Map<String, String> getNotifyParams(Object notifyBody){
    	Map<String,String> params = new HashMap<String, String>();
    	Map<String, String> notifyParams = PaymentKit.xmlToMap(notifyBody.toString());
    	params.put("returnCode", notifyParams.get("return_code"));
		String reqInfo = notifyParams.get("req_info");
		WxPayConfig appClient = AppClientConfig.getWxPayConfig(notifyParams.get("appid"));
		String mchKey = appClient.getMchKey();
		try {
			String xmlData = new AESTool(mchKey).decryptData(reqInfo);
			Map<String, String> retParamsMap = PaymentKit.xmlToMap(xmlData);
			params.put("extenalTradeNo", retParamsMap.get("transaction_id"));
			params.put("payTradeNo",retParamsMap.get("out_trade_no"));
			params.put("externalRefundNo",retParamsMap.get("refund_id"));
			params.put("payRefundNo",retParamsMap.get("out_refund_no"));
			params.put("totalFee",retParamsMap.get("total_fee"));
			params.put("refundFee",retParamsMap.get("refund_fee"));
			params.put("refundStatus",retParamsMap.get("refund_status"));
			params.put("successTime",retParamsMap.get("success_time"));
		} catch (Exception e) {
			log.error("解密错误:", e);
			throw new RuntimeException(e);
		}
    	return params;
    }

	

}
