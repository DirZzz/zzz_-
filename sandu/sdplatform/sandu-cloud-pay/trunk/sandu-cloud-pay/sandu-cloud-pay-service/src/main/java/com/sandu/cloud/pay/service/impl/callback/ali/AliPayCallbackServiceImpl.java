package com.sandu.cloud.pay.service.impl.callback.ali;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jpay.util.StringUtils;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.AliPayConfig;
import com.sandu.cloud.pay.service.TradeRecordLogService;
import com.sandu.cloud.pay.service.TradeRecordService;
import com.sandu.cloud.pay.service.impl.callback.BasePayCallbackServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service("aliPayCallbackService")
@Slf4j
public class AliPayCallbackServiceImpl extends BasePayCallbackServiceImpl{

	@Resource
    private TradeRecordService payTradeRecordService ;
    
    @Resource
    private TradeRecordLogService payTradeRecordLogService ;

  
    
    @Override
    public boolean verifySignature(Object notifyBody) {
    	boolean signFlag = false;
		try {
			Map<String,String> params = (Map<String,String>) notifyBody;
			String appId = params.get("auth_app_id");
			AliPayConfig aliPayConfig = AppClientConfig.getAliPayConfig(appId);
			signFlag = AlipaySignature.rsaCheckV1(params, 
					aliPayConfig.getPublicKey(), 
					"UTF-8",
					"RSA2");
		} catch (AlipayApiException e) {
			log.error("支付宝验证签名异常:", e);
		}
		return signFlag;
    }
    
    @Override
    public Map<String, String> getNotifyParams(Object notifyBody){
    	Map<String,String> params = new HashMap<String, String>();
    	Map<String,String> notifyParams = (Map<String,String>) notifyBody;
    	String tradeStatus = notifyParams.get("trade_status");
    	if ("TRADE_SUCCESS".equals(tradeStatus)) {
    		params.put("resultCode", "SUCCESS");
    	}else if("WAIT_BUYER_PAY".equals(tradeStatus)) {
    		log.warn("等待支付状态不处理回调:{}",JsonUtils.toJson(notifyParams));
    		return null;//throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ALI_WAIT_BUYER_PAY_ERROR);
    	}else{
    		params.put("resultCode", "FAIL");
    	}
		params.put("payTradeNo", notifyParams.get("out_trade_no"));//支付网关交易号
		params.put("extenalTradeNo", notifyParams.get("trade_no"));// 支付宝交易号
		String strAmount = notifyParams.get("total_amount");
		if(StringUtils.isNotBlank(strAmount)) {
			Double d = Double.valueOf(strAmount)*100;
			strAmount = String.valueOf(d.longValue()); //金额用分表示
		}
		params.put("totalFee", strAmount); //交易金额
		params.put("passbackParams",notifyParams.get("passback_params"));
		
		return params;
    }

}
