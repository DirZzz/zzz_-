package com.sandu.cloud.pay.service.impl.forward.wx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.jpay.ext.kit.PaymentKit;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApiConfig;
import com.sandu.cloud.common.exception.BizException;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TradeQueryDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.TradeRecord;
import com.sandu.cloud.pay.model.TradeRefundRecord;
import com.sandu.cloud.pay.model.TradeTransfersRecord;
import com.sandu.cloud.pay.model.WxPayConfig;
import com.sandu.cloud.pay.service.TradeRecordService;
import com.sandu.cloud.pay.service.TradeRefundRecordService;
import com.sandu.cloud.pay.service.TradeTransfersRecordService;

import lombok.extern.slf4j.Slf4j;

@Service("wxMiniPay")
@Slf4j
public class WxMiniPayServiceImpl extends WxPayServiceImpl {

	private static final ThreadLocal<String> TL = new ThreadLocal<String>();
	private static final Map<String, WxPayApiConfig> CFG_MAP = new ConcurrentHashMap<String, WxPayApiConfig>();
    
    @Resource
    private TradeRecordService payTradeRecordService ;

    @Resource
    private TradeRefundRecordService payTradeRefundRecordService ;
    
    @Resource
    private TradeTransfersRecordService payTradeTransfersRecordService;
    
	@Override
	protected Integer getPayMethod() {
		return TradeRecord.PAY_METHOD_WX_MINIPRO;
	}

	@Override
	protected Map<String, String> buildPayParameter(PayParamDto payParam) {
		WxPayConfig client = AppClientConfig.getWxPayConfig(payParam.getAppId());
		
		WxPayApiConfig apiConfig = WxPayApiConfig.New();
		Map<String, String> params = apiConfig
				.setBody(payParam.getTradeDesc())
				.setOutTradeNo(payParam.getPayTradeNo())
				.setTotalFee(payParam.getTotalFee().toString())
                .setMchId(client.getMchId())
                .setOpenId(payParam.getOpenId())
                .setPaternerKey(client.getMchKey())
                .setPayModel(WxPayApiConfig.PayModel.BUSINESSMODEL)
                .setNonceStr(RandomStringUtils.randomAlphanumeric(32))
                .setSpbillCreateIp("127.0.0.1")
                .setTradeType(WxPayApi.TradeType.JSAPI)
                .setAppId(client.getAppId())
                .setNotifyUrl(WxPayConfig.WX_NOTIFY_URL)
				.build();
		CFG_MAP.put(apiConfig.getAppId(), apiConfig);
		TL.set(client.getAppId());
		return params;
	}

	@Override
	protected String packageResult(String payTradeNo,String xmlResult) {
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.error("微信小程序下单异常:{}",xmlResult);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_MINI_TRADE_ERROR);
		}
		String result_code = result.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.error("微信小程序结果异常:{}",xmlResult);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_MINI_TRADE_RESULT_ERROR);
		}
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
		String prepay_id = result.get("prepay_id");

		//封装调起微信支付的参数https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
		Map<String, String> packageParams = new HashMap<String, String>();
		WxPayApiConfig apiConfig = CFG_MAP.get(TL.get());
		packageParams.put("appId", apiConfig.getAppId());
		packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		packageParams.put("nonceStr", System.currentTimeMillis() + "");
		packageParams.put("package", "prepay_id=" + prepay_id);
		packageParams.put("signType", "MD5");
		String packageSign = PaymentKit.createSign(packageParams, apiConfig.getPaternerKey());
		packageParams.put("paySign", packageSign);
		return JsonUtils.toJson(packageParams);
	}
	
	@Override
    protected Map<String, String> buildRefundParameter(RefundParamDto refundParam) {
		WxPayConfig client = AppClientConfig.getWxPayConfig(refundParam.getAppId());
        Map<String, String> params = new HashMap<String, String>();
		params.put("appid", client.getAppId());
		params.put("mch_id",client.getMchId());
		params.put("nonce_str", System.currentTimeMillis()+"");
		
		if (StringUtils.isNotBlank(refundParam.getOriginInternalTradeNo())) {
			//如果是通过系统内部交易号,则取支付网关的交易号
			TradeRecord trade = this.getTradeRecordByIntenalTradeNo(refundParam.getOriginInternalTradeNo());
			if(trade!=null) {
				params.put("out_trade_no", trade.getPayTradeNo());
			}else {
				log.error("交易不存在:{}",refundParam.getOriginInternalTradeNo());
				throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_MINI_TRADE_RESULT_ERROR);
			}
			
		}else {
			//如果是通过支付网关的交易号,则取微信号
			TradeRecord trade = this.getTradeRecordByPayTradeNo(refundParam.getOriginPayTradeNo());
			if(trade!=null) {
				params.put("transaction_id", trade.getExtenalTradeNo());
			}else {
				log.error("交易不存在:{}",refundParam.getOriginPayTradeNo());
				throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_MINI_TRADE_RESULT_ERROR);
			}
		}
		params.put("out_refund_no", refundParam.getPayRefundNo());
		params.put("total_fee", refundParam.getTotalFee().toString());
		params.put("refund_fee", refundParam.getRefundFee().toString());
		params.put("notify_url", WxPayConfig.WX_REFUND_NOTIFY_URL);
		params.put("sign", PaymentKit.createSign(params, client.getMchKey()));
		params.put("api_cert_path", client.getApiCertPath());
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

	
	@Override
	protected String updateAndPackageRefundResult(TradeRefundRecord tradeRefundRecord,String xmlResult) {
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.error("微信小程序退款异常:{}",xmlResult);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_MINI_REFUND_ERROR);
		}
		String result_code = result.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.error("微信小程序退款结果异常:{}",xmlResult);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_MINI_REFUND_RESULT_ERROR);
			
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
	protected Object buildTransfersParameter(TransfersParamDto transfersParam) {
		
		WxPayConfig client = AppClientConfig.getWxPayConfig(transfersParam.getAppId());
		Map<String, String> params = new HashMap<String, String>();
		params.put("mch_appid", client.getAppId());
		params.put("mchid", client.getMchId());
		String nonceStr = String.valueOf(System.currentTimeMillis());
		params.put("nonce_str", nonceStr);
		params.put("partner_trade_no", transfersParam.getPayTradeNo());
		params.put("openid", transfersParam.getOpenId());
		params.put("check_name", "NO_CHECK");
		params.put("amount", transfersParam.getAmount().toString());
		params.put("desc", transfersParam.getTradeDesc());
		params.put("spbill_create_ip", transfersParam.getIp());
		params.put("sign", PaymentKit.createSign(params, client.getMchKey()));
		params.put("api_cert_path", client.getApiCertPath());
		return params;
	}

	@Override
	protected String updateAndPackageTransfersResult(TradeTransfersRecord tradeTransfersRecord, String xmlResult) {
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
		this.modifyTradeTransfersInfo(tradeTransfersRecord.getId(),result);
		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.error(xmlResult);
			throw new BizException("微信小程序企业付款异常:" + return_msg);
		}
		String result_code = result.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.error(xmlResult);
			throw new BizException("微信小程序企业付款结果异常:" + result.get("err_code_des"));
		}
		Map<String, String> resultParams = new HashMap<String, String>();
		resultParams.put("internalTradeNo", tradeTransfersRecord.getIntenalTradeNo());
		resultParams.put("payTradeNo", tradeTransfersRecord.getPayTradeNo());
		resultParams.put("paymentTime", result.get("payment_time"));
		return JsonUtils.toJson(resultParams);
	}
	
	private void modifyTradeTransfersInfo(Long id,Map<String, String> result) {
		TradeTransfersRecord obj = new TradeTransfersRecord();
		obj.setId(id);
		if (PaymentKit.codeIsOK(result.get("return_code"))) {
			obj.setStatus(TradeTransfersRecord.STATUS_SUCCESS);
		}else {
			obj.setStatus(TradeTransfersRecord.STATUS_FAILURE);
		}
		obj.setExtenalTradeNo(result.get("payment_no"));
		obj.setPaymentTime(result.get("payment_time"));
		payTradeTransfersRecordService.modify(obj);
	}
    
}
