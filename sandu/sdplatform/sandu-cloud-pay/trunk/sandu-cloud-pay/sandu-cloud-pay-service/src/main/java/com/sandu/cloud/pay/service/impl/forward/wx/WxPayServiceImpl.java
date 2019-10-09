package com.sandu.cloud.pay.service.impl.forward.wx;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jpay.ext.kit.PaymentKit;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApiConfig;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.TradeRefundRecord;
import com.sandu.cloud.pay.model.TradeTransfersRecord;
import com.sandu.cloud.pay.model.WxPayConfig;
import com.sandu.cloud.pay.service.TradeRefundRecordService;
import com.sandu.cloud.pay.service.impl.forward.BasePayServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class WxPayServiceImpl extends BasePayServiceImpl {

    @Autowired
    private TradeRefundRecordService payTradeRefundRecordService;
    
    protected Object buildRefundParameter(RefundParamDto refundParam) {
		return null;
	}
    
    protected Object buildTransfersParameter(TransfersParamDto transfersParam) {
		return null;
	}
     
    protected String executePay(Object params) {
        String xmlResult = WxPayApi.pushOrder(false,  (Map<String, String>)params);
        return xmlResult;
    }

    protected WxPayApiConfig getWxPayApiConfig(String appId) {
    	WxPayConfig appClient = AppClientConfig.getWxPayConfig(appId);
        WxPayApiConfig apiConfig = WxPayApiConfig.New()
                .setAppId(appId)
                .setMchId(appClient.getMchId())
                .setPaternerKey(appClient.getMchKey())
                .setNotifyUrl(WxPayConfig.WX_NOTIFY_URL)
                .setPayModel(WxPayApiConfig.PayModel.BUSINESSMODEL);
        return apiConfig;
    }
    
    protected String executeRefund(Object params) {
    	Map<String, String> paramsMap = (Map<String, String>)params;
    	String apiCertPath = AppClientConfig.getWxPayConfig(paramsMap.get("appid")).getApiCertPath();
    	String certPass = AppClientConfig.getWxPayConfig(paramsMap.get("appid")).getCertPass();
    	String xmlResult = WxPayApi.orderRefund(false, paramsMap , apiCertPath, certPass);
        return xmlResult;
    }

    protected String executeTransfers(Object params) {
    	Map<String, String> paramsMap = (Map<String, String>)params;
        log.info("准备开始企业付款参数: =>{}" , paramsMap);
        WxPayConfig wxPayConfig = AppClientConfig.getWxPayConfig(paramsMap.get("mch_appid"));
    	String apiCertPath = wxPayConfig.getApiCertPath();
    	String certPass = wxPayConfig.getCertPass();
    	log.info("开始调用微信企业付款到零钱接口");
    	String xmlResult = WxPayApi.transfers(paramsMap , apiCertPath,certPass);
    	log.info("发红包:{}",xmlResult);
        return xmlResult;
	}
    
    @Override
	protected String updateAndPackageRefundResult(TradeRefundRecord tradeRefundRecord,String xmlResult) {
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(return_code)) {
        	log.error("微信退款异常:{}"+xmlResult);
            throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_SCAN_CODE_REFUND_ERROR);
        }
        String result_code = result.get("result_code");
        if (!PaymentKit.codeIsOK(result_code)) {
        	log.error("微信退款结果异常:{}"+xmlResult);
        	throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_SCAN_CODE_REFUND_RESULT_ERROR);
        }
        this.modifyTradeRefundInfo(tradeRefundRecord.getId(),result);
        Map<String, String> resultParams = new HashMap<String, String>();
        resultParams.put("originInternalTradeNo", tradeRefundRecord.getOriginInternalTradeNo());
        resultParams.put("originPayTradeNo", tradeRefundRecord.getOriginPayTradeNo());
        resultParams.put("internalRefundNo", tradeRefundRecord.getInternalRefundNo());
        resultParams.put("payRefundNo", tradeRefundRecord.getPayRefundNo());
        resultParams.put("refundFee", result.get("refund_fee"));
        return JsonUtils.toJson(resultParams);
    }


    private void modifyTradeRefundInfo(Long id,Map<String, String> result) {
        TradeRefundRecord obj = new TradeRefundRecord();
        obj.setId(id);
        obj.setRefundFee(Long.valueOf(result.get("refund_fee").toString()));
        obj.setExternalRefundNo(result.get("refund_id"));
        payTradeRefundRecordService.modify(obj);
    }

    @Override
	protected String updateAndPackageTransfersResult(TradeTransfersRecord tradeTransfersRecord, String xmlOrJsonResult) {
		return null;
	}
    	
}
