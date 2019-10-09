package com.sandu.cloud.pay.service.impl.forward.wx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

@Service("wxH5Pay")
@Slf4j
public class WxH5PayServiceImpl extends WxPayServiceImpl {
    private static final ThreadLocal<String> TL = new ThreadLocal<String>();
    @Override
    protected Map<String, String> buildPayParameter(PayParamDto payParam) {
        WxPayApiConfig apiConfig = super.getWxPayApiConfig(payParam.getAppId());
        Map<String, String> params = apiConfig
                .setBody(payParam.getTradeDesc())
                .setOutTradeNo(payParam.getPayTradeNo())
                .setTotalFee(payParam.getTotalFee().toString())
                .setSpbillCreateIp(payParam.getIp())
				.setTradeType(WxPayApi.TradeType.MWEB)		//setSceneInfo(h5_info.toString()) 文档规定必填,但是好像可以不填
                .setSceneInfo("1324648") //直接写死,可以不传
                .build();
        if (payParam.getPassbackParams() != null){
            apiConfig.setAttach(payParam.getPassbackParams());
        }
        TL.set(payParam.getRedirectUrl());
        return params;	
    }

   
    @Override
	protected Integer getPayMethod() {
		return TradeRecord.PAY_METHOD_WX_H5PAY;
	}


	@Override
	protected String packageResult(String payTradeNo,String xmlResult) {
		
	 	Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(returnCode)) {
            log.error("微信H5支付下单异常->msg:{},info:{}",returnMsg,xmlResult);
            throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_H5_TRADE_ERROR);
        }
        String resultCode = result.get("result_code");
        String errCodeDes = result.get("err_code_des");
        if (!PaymentKit.codeIsOK(resultCode)) {
        	log.error("微信H5支付下单结果异常->msg:{},info:{}",returnMsg,errCodeDes);
            throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_H5_TRADE_RESULT_ERROR);
        }
 
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
 		String mwebUrl = result.get("mweb_url");
 		String redirectUrl = TL.get();
 		try {
 			if(StringUtils.isNotBlank(redirectUrl)) {
 				mwebUrl = mwebUrl+"&redirect_url=" +URLEncoder.encode(redirectUrl, "utf-8");
 			}
		} catch (UnsupportedEncodingException e) { 
			log.error("Encode Error:"+redirectUrl,e);
		}
        Map<String, String> packageParams = new HashMap<String, String>();
 		packageParams.put("payTradeNo", payTradeNo);
 		packageParams.put("mwebUrl", mwebUrl);
		return JsonUtils.toJson(packageParams);
	}

	
}
