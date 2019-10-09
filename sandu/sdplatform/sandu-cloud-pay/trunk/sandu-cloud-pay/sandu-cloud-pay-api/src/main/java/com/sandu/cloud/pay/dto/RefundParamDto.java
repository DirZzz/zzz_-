package com.sandu.cloud.pay.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sandu.cloud.pay.constants.PayMethodContants;
import com.sandu.cloud.pay.util.PaymentKit;
import com.sandu.cloud.pay.util.StringUtils;

import lombok.Data;

@Data
public class RefundParamDto{
	
	private String originInternalTradeNo;
	
	private String originPayTradeNo;
	
	@NotEmpty(message = "退款交易号不能为空")
	private String internalRefundNo;
	
	private String payRefundNo;
	
	@NotNull(message = "交易金额不能为空")
	private Long totalFee;
	
	@NotNull(message = "退款金额不能为空")
	@Min(value=1,message = "退款金额必须大于0")
	private Long refundFee;
	
	private String refundDesc;
	
	/**
	 * 调用方ip
	 */
	@NotEmpty(message = "ip地址不能为空")
	private String ip;
	
	/**
	 * 回调通知url
	 */
	@NotEmpty(message = "回调地址不能为空")
	private String notifyUrl;
	
	/**
	 * 签名
	 */
	@NotEmpty(message = "签名不能为空")
	private String sign;
	
	@NotEmpty(message = "clientId不能为空")
	private String clientId;
	
	@NotEmpty(message = "appId不能为空")
	private String appId;
	
	
	/**
	 * 支付方式
	 */
	@NotEmpty(message = "付款方式不能为空")
	private String payMethod;


	public String createSign(String clientSecret) {
		Map<String, String> map = new HashMap<String, String>();
		if(!StringUtils.isBlank(originInternalTradeNo)) {
			map.put("originInternalTradeNo", originInternalTradeNo);
		}
		if(!StringUtils.isBlank(originPayTradeNo)) {
			map.put("originPayTradeNo", originPayTradeNo);
		}
		if(!StringUtils.isBlank(payRefundNo)) {
			map.put("payRefundNo", payRefundNo);
		}
		if(!StringUtils.isBlank(refundDesc)) {
			map.put("refundDesc", refundDesc);
		}
		
		map.put("internalRefundNo", internalRefundNo);
		map.put("totalFee", totalFee.toString());
		map.put("refundFee", refundFee.toString());
		map.put("ip", ip);
		map.put("notifyUrl", notifyUrl);
		map.put("clientId", clientId);
		map.put("appId", appId);
		map.put("payMethod", payMethod);
		return PaymentKit.createSign(map, clientSecret);
	}
	
	
	public static void main(String[] args) {
		RefundParamDto refundParamDto = new RefundParamDto();
		refundParamDto.setOriginPayTradeNo("20190520161734717470953459680588");
		refundParamDto.setInternalRefundNo("654321");
		refundParamDto.setTotalFee(1L);
		refundParamDto.setRefundFee(1L);
		refundParamDto.setPayMethod(PayMethodContants.WX_SCANCODE_PAY);
		refundParamDto.setRefundDesc("refundDesc");
		refundParamDto.setIp("127.0.0.1");
		refundParamDto.setNotifyUrl("http://127.0.0.1:8089/v1/refund/callback/test/notify");
		refundParamDto.setClientId("001");
		refundParamDto.setAppId("wxd4934d0dab14d276");
		refundParamDto.setSign(refundParamDto.createSign("123456"));
		System.out.println(refundParamDto.getSign());
	}
}
