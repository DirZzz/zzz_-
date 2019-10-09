package com.sandu.cloud.pay.service.impl.forward.ali;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.jpay.alipay.AliPayApi;
import com.jpay.alipay.AliPayApiConfig;
import com.jpay.alipay.AliPayApiConfigKit;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.AliPayConfig;
import com.sandu.cloud.pay.model.TradeRefundRecord;
import com.sandu.cloud.pay.model.TradeTransfersRecord;
import com.sandu.cloud.pay.service.TradeRefundRecordService;
import com.sandu.cloud.pay.service.impl.forward.BasePayServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AliPayServiceImpl extends  BasePayServiceImpl{

	//@Autowired
	//private AsyncQueryAliRefundResult asyncQueryAliRefundResult;
    @Resource
    private TradeRefundRecordService payTradeRefundRecordService;
	
	protected void setAliPayApiConfig(String appId) {
		AliPayConfig aliPayConfig = AppClientConfig.getAliPayConfig(appId);
		AliPayApiConfig aliPayApiConfig = AliPayApiConfig.New()
	        		.setAppId(appId)
	        		.setAlipayPublicKey(aliPayConfig.getPublicKey())
	        		.setCharset("UTF-8")
	        		.setPrivateKey(aliPayConfig.getPrivateKey())
	        		.setServiceUrl(AliPayConfig.SERVICE_URL)
	        		.setSignType("RSA2")
	        		.build();
		AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
		AliPayApiConfigKit.setThreadLocalAppId(appId);
    }
	
	protected Object buildRefundParameter(RefundParamDto refundParam) {
		log.info("支付宝退款父类封装参数方法");
		return null;
	}

	protected String executeRefund(Object params) {
		try {
			return AliPayApi.tradeRefund((AlipayTradeRefundModel)params);
		} catch (AlipayApiException e) {
			log.error("阿里退款异常:",e);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ALI_REFUND_ERROR);
		}
	}
	
	protected Object buildTransfersParameter(TransfersParamDto transfersParam) {
		return null;
	}
	
	protected String executeTransfers(Object params){
		return null;
	}
	
	@Override
	protected String updateAndPackageRefundResult(TradeRefundRecord tradeRefundRecord,String jsonResult) {
		Map resultMap = JsonUtils.fromJson(jsonResult, Map.class);
		Map<String, Object> resultParams = new HashMap<>();
		Map responseMap = (Map)resultMap.get("alipay_trade_refund_response");
		if (Objects.equals(responseMap.get("code"),"10000") && Objects.equals("Success",responseMap.get("msg"))){
			if ("N".equals(responseMap.get("fund_change"))){
				log.error("订单已全额退款:{}",JsonUtils.toJson(responseMap));
				throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ALI_REFUND_REPEAT);
	        }
			this.tradeRefundSuccess(tradeRefundRecord.getId(),responseMap);
			resultParams.put("originInternalTradeNo", tradeRefundRecord.getOriginInternalTradeNo());
			resultParams.put("originPayTradeNo", tradeRefundRecord.getOriginPayTradeNo());
			resultParams.put("internalRefundNo", tradeRefundRecord.getInternalRefundNo());
			resultParams.put("payRefundNo", tradeRefundRecord.getPayRefundNo());
			resultParams.put("refundFee", responseMap.get("refund_fee"));
			resultParams.put("successTime", responseMap.get("gmt_refund_pay"));
			return JsonUtils.toJson(resultParams);
		}
		this.tradeRefundFailure(tradeRefundRecord.getId());
		log.error("支付宝退款异常 =>{}",jsonResult);
		throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ALI_REFUND_ERROR);
				
    }
	
	private void tradeRefundSuccess(Long id,Map<String, String> responseMap) {
		TradeRefundRecord obj = new TradeRefundRecord();
		obj.setId(id);
		obj.setRefundSuccessTime(responseMap.get("gmt_refund_pay"));
		obj.setExternalRefundNo(responseMap.get("trade_no"));
		obj.setRefundStatus("SUCCESS");
		obj.setStatus(TradeRefundRecord.STATUS_SUCCESS);
		payTradeRefundRecordService.modify(obj);
	}
	
	private void tradeRefundFailure(Long id) {
		TradeRefundRecord obj = new TradeRefundRecord();
		obj.setId(id);
		obj.setRefundStatus("FAILURE");
		obj.setStatus(TradeRefundRecord.STATUS_FAILURE);
		payTradeRefundRecordService.modify(obj);
	}
	
	@Override
	protected String updateAndPackageTransfersResult(TradeTransfersRecord tradeTransfersRecord, String xmlOrJsonResult) {
		return null;
	}
	
	
	
}
