package com.sandu.cloud.pay.service.impl.forward.wx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jpay.ext.kit.PaymentKit;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApiConfig;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TradeQueryDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.TradeRecord;
import com.sandu.cloud.pay.model.WxPayConfig;
import com.sandu.cloud.pay.service.TradeRecordService;
import com.sandu.cloud.pay.service.TradeRefundRecordService;
import com.sandu.cloud.pay.service.TradeTransfersRecordService;

import lombok.extern.slf4j.Slf4j;

@Service("wxScanCodePay")
@Slf4j
public class WxScanCodePayServiceImpl extends WxPayServiceImpl {

    @Resource
    private TradeRecordService payTradeRecordService ;

    @Resource
    private TradeRefundRecordService payTradeRefundRecordService ;

    @Resource
    private TradeTransfersRecordService payTradeTransfersRecordService;

    @Override
    protected Map<String, String> buildPayParameter(PayParamDto payParam) {
        WxPayApiConfig apiConfig = super.getWxPayApiConfig(payParam.getAppId());
        Map<String, String> params = apiConfig
                .setBody(payParam.getTradeDesc())
                .setOutTradeNo(payParam.getPayTradeNo())
                .setTotalFee(payParam.getTotalFee().toString())
                .setSpbillCreateIp(payParam.getIp())
				.setTradeType(WxPayApi.TradeType.NATIVE)
                .build();
        return params;
    }
    
    @Override
	protected Integer getPayMethod() {
		return TradeRecord.PAY_METHOD_WX_SCANCODE;
	}


	@Override
	protected String packageResult(String payTradeNo,String xmlResult) {
	 	Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(returnCode)) {
            log.error("微信下单异常:{},msg:{}",returnMsg,xmlResult);
            throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_APP_TRADE_ERROR);
        }
        String resultCode = result.get("result_code");
        String errCodeDes = result.get("err_code_des");
        if (!PaymentKit.codeIsOK(resultCode)) {
        	log.error("微信下单结果异常:{},msg:{}",errCodeDes,xmlResult);
        	throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_APP_TRADE_RESULT_ERROR);
        }
        Map<String, String> packageRetParams = new HashMap<String, String>();
        String qrCodeUrl = result.get("code_url");
        packageRetParams.put("payTradeNo", payTradeNo);
        packageRetParams.put("qrCodeUrl", qrCodeUrl);
		return JsonUtils.toJson(packageRetParams);
	}

	public Object buildRefundParameter(RefundParamDto refundParam){
		WxPayConfig appClient = AppClientConfig.getWxPayConfig(refundParam.getAppId());
		Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appClient.getAppId());
        params.put("mch_id",appClient.getMchId());
        params.put("nonce_str", System.currentTimeMillis()+"");

        if (StringUtils.isNotBlank(refundParam.getOriginInternalTradeNo())) {
            //如果是通过系统内部交易号,则取支付网关的交易号
            TradeRecord trade = this.getTradeRecordByIntenalTradeNo(refundParam.getOriginInternalTradeNo());
            if(trade!=null) {
                params.put("out_trade_no", trade.getPayTradeNo());
            }else {
            	log.error("交易不存在:{}",refundParam.getOriginInternalTradeNo());
            	throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ORIGIN_TRADE_NOT_EXIST);
            }

        }else {
            //如果是通过支付网关的交易号,则取微信号
            TradeRecord trade = this.getTradeRecordByPayTradeNo(refundParam.getOriginPayTradeNo());
            if(trade!=null) {
                params.put("transaction_id", trade.getExtenalTradeNo());
            }else {
            	log.error("交易不存在:{}",refundParam.getOriginInternalTradeNo());
            	throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ORIGIN_TRADE_NOT_EXIST);
            }
        }
        params.put("out_refund_no", refundParam.getPayRefundNo());
        params.put("total_fee", refundParam.getTotalFee().toString());
        params.put("refund_fee", refundParam.getRefundFee().toString());
        params.put("notify_url", WxPayConfig.WX_REFUND_NOTIFY_URL);
        params.put("sign", PaymentKit.createSign(params, appClient.getMchKey()));
        //params.put("api_cert_path", "C:\\Users\\Sandu\\Desktop\\apiclient_cert.p12");
       // params.put("api_cert_path", PayConfig.CERTIFICATE_PATH);
        return params;
    }

    private TradeRecord getTradeRecordByIntenalTradeNo(String intenalTradeNo) {
        if(StringUtils.isBlank(intenalTradeNo)) {
            return null;
        }
        TradeQueryDto queryVo = new TradeQueryDto();
        queryVo.setIntenalTradeNo(intenalTradeNo);
        List<TradeRecord> list = payTradeRecordService.getList(queryVo);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private TradeRecord getTradeRecordByPayTradeNo(String payTradeNo) {
        if(StringUtils.isBlank(payTradeNo)) {
            return null;
        }
        TradeQueryDto queryVo = new TradeQueryDto();
        queryVo.setPayTradeNo(payTradeNo);
        List<TradeRecord> list = payTradeRecordService.getList(queryVo);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


}
