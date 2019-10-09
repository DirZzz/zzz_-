package com.sandu.cloud.pay.service.impl.forward.ali;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.jpay.alipay.AliPayApi;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.AliPayConfig;
import com.sandu.cloud.pay.model.TradeRecord;
import com.sandu.cloud.pay.service.TradeRecordService;
import lombok.extern.slf4j.Slf4j;

@Service("aliScanCodePay")
@Slf4j
public class AliScanCodePayServiceImpl extends AliPayServiceImpl {

	@Resource
	private TradeRecordService payTradeRecordService;

    @Override
	protected Object buildPayParameter(PayParamDto payParam) {
    	super.setAliPayApiConfig(payParam.getAppId());
    	AliPayConfig aliPayConfig = AppClientConfig.getAliPayConfig(payParam.getAppId());
		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		model.setOutTradeNo(payParam.getPayTradeNo());
		model.setSubject(payParam.getTradeDesc());
		model.setTotalAmount(String.valueOf((payParam.getTotalFee().doubleValue()/100)));
		model.setStoreId(aliPayConfig.getStoreId());
		model.setTimeoutExpress("30m");
		model.setSellerId(aliPayConfig.getSellerId());
		return model;
	}

	@Override
	protected Object buildRefundParameter(RefundParamDto refundParam) {
		super.setAliPayApiConfig(refundParam.getAppId());
		AlipayTradeRefundModel model  = new AlipayTradeRefundModel();
//		model.setOperatorId(refundParam.getOperator().toString());
		model.setRefundAmount(String.valueOf((refundParam.getRefundFee().doubleValue()/100)));
		if (StringUtils.isNotBlank(refundParam.getOriginInternalTradeNo())) {
			//如果是通过系统内部交易号,则取支付网关的交易号 =>{} 商户订单号
			String intenalTradeNo = payTradeRecordService.getTradeRecordByIntenalTradeNo(refundParam.getOriginInternalTradeNo());
			if (StringUtils.isNotBlank(intenalTradeNo)) {
				model.setOutTradeNo(intenalTradeNo);
			} else {
				log.error("原交易不存在:",refundParam.getOriginInternalTradeNo());
				throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ORIGIN_TRADE_NOT_EXIST);
	 
			}
		} else {
			//如果是通过支付网关的交易号,则取微信号 =>{} 支付宝交易号
			String payTradeNo = payTradeRecordService.getTradeRecordByPayTradeNo(refundParam.getOriginPayTradeNo());
			if (StringUtils.isNotBlank(payTradeNo)) {
				model.setTradeNo(payTradeNo);
			} else {
				log.error("原交易不存在:",refundParam.getOriginInternalTradeNo());
				throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ORIGIN_TRADE_NOT_EXIST);
			}
		}
		log.info("支付宝退款封装参数按成 =>{}",model);
		return model;
	}

    @Override
	protected String executePay(Object params) {
		try {
			String result = AliPayApi.tradePrecreatePay((AlipayTradePrecreateModel)params, AliPayConfig.PAY_NOTIFY_URL);
			return result;
		} catch (AlipayApiException e) {
			log.error("阿里扫码支付异常:",e);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_ALI_SCAN_CODE_ERROR);
		}
	}

    @Override
   	protected Integer getPayMethod() {
   		return TradeRecord.PAY_METHOD_AL_SCANCODE;
   	}
    
	@Override
	protected String packageResult(String payTradeNo,String jsonResult) {
		Map resultMap = JsonUtils.fromJson(jsonResult, Map.class);
		Map responseMap = (Map)resultMap.get("alipay_trade_precreate_response");
		Map<String, String> packageRetParams = new HashMap<String, String>();
		packageRetParams.put("payTradeNo", payTradeNo);
        packageRetParams.put("qrCodeUrl", responseMap.get("qr_code").toString());
		return JsonUtils.toJson(packageRetParams);
	}

}
