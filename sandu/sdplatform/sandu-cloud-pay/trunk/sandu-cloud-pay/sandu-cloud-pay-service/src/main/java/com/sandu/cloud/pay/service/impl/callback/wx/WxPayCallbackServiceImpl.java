package com.sandu.cloud.pay.service.impl.callback.wx;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.jpay.ext.kit.PaymentKit;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.service.TradeRecordLogService;
import com.sandu.cloud.pay.service.TradeRecordService;
import com.sandu.cloud.pay.service.impl.callback.BasePayCallbackServiceImpl;

@Service("wxPayCallbackService")
public class WxPayCallbackServiceImpl extends BasePayCallbackServiceImpl{

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
    	params.put("resultCode", notifyParams.get("result_code"));
		params.put("payTradeNo", notifyParams.get("out_trade_no"));//支付网关交易号
		params.put("extenalTradeNo", notifyParams.get("transaction_id"));// 微信支付订单号
		params.put("totalFee", notifyParams.get("total_fee")); //交易金额
		params.put("passbackParams",notifyParams.get("attach") == null?"0":notifyParams.get("attach"));
    	return params;
    }
}
