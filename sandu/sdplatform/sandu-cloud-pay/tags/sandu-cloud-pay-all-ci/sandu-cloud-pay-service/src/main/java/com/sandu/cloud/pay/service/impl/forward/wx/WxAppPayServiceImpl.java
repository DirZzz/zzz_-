package com.sandu.cloud.pay.service.impl.forward.wx;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.jpay.ext.kit.PaymentKit;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApiConfig;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.TradeRecord;

import lombok.extern.slf4j.Slf4j;

@Service("wxAppPay")
@Slf4j
public class WxAppPayServiceImpl extends WxPayServiceImpl {
	private static final ThreadLocal<String> TL = new ThreadLocal<String>();
	private static final Map<String, WxPayApiConfig> CFG_MAP = new ConcurrentHashMap<String, WxPayApiConfig>();
    @Override
    protected Map<String, String> buildPayParameter(PayParamDto payParam) {
        WxPayApiConfig apiConfig = super.getWxPayApiConfig(payParam.getAppId());
        Map<String, String> params = apiConfig
                .setBody(payParam.getTradeDesc())
                .setOutTradeNo(payParam.getPayTradeNo())
                .setTotalFee(payParam.getTotalFee().toString())
                .setSpbillCreateIp(payParam.getIp())
				.setTradeType(WxPayApi.TradeType.APP)
                .build();
        if (payParam.getPassbackParams() != null){
        	apiConfig.setAttach(payParam.getPassbackParams());
		}
        CFG_MAP.put(apiConfig.getAppId(), apiConfig);
		TL.set(apiConfig.getAppId());
        return params;	
    }
    
    @Override
	protected Integer getPayMethod() {
		return TradeRecord.PAY_METHOD_WX_APPPAY;
	}


	@Override
	protected String packageResult(String payTradeNo,String xmlResult) {
	 	Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(returnCode)) {
            log.error("微信app支付返回异常结果:{}",xmlResult);
            throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_APP_TRADE_ERROR);
        }
        String resultCode = result.get("result_code");
        String errCodeDes = result.get("err_code_des");
        if (!PaymentKit.codeIsOK(resultCode)) {
            log.error("微信app支付返回异常结果:",xmlResult);
            throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_APP_TRADE_RESULT_ERROR);
        }
        
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
 		String prepay_id = result.get("prepay_id");
 		//封装调起微信支付的参数 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12
 		Map<String, String> packageParams = new HashMap<String, String>();
 		WxPayApiConfig apiConfig = CFG_MAP.get(TL.get());
 		packageParams.put("appid", apiConfig.getAppId());
 		packageParams.put("partnerid", apiConfig.getMchId());
 		packageParams.put("prepayid", prepay_id);
 		packageParams.put("package", "Sign=WXPay");
 		packageParams.put("noncestr", System.currentTimeMillis() + "");
 		packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
 		String packageSign = PaymentKit.createSign(packageParams,apiConfig.getPaternerKey());
 		packageParams.put("sign", packageSign);
		return JsonUtils.toJson(packageParams);
	}

	
}
