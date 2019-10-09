package com.sandu.cloud.pay.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sandu.cloud.pay.util.PaymentKit;
import com.sandu.cloud.pay.util.StringUtils;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class TransfersParamDto{
	
	/**
	 * 交易号
	 */
	@NotEmpty(message = "交易号不能为空")
	private String intenalTradeNo;
	
	/**
	 * 支付网关交易号
	 */
	private String payTradeNo;
	
	/**
	 * 收款方openId
	 */
	@NotEmpty(message = "收款方openId不能为空")
	private String openId;
	/**
	 * 费用,单位为分
	 */
	@NotNull(message = "付款金额不能为空")
	private Long amount;
	
	/**
	 *  企业付款描述
	 */
	@NotEmpty(message = "企业付款描述不能为空")
	private String tradeDesc;
	
	/**
	 * 调用方ip
	 */
	@NotEmpty(message = "ip地址不能为空")
	private String ip;
	
	/**
	 * 签名
	 */
	@NotEmpty(message = "签名不能为空")
	private String sign;
	
	@NotEmpty(message = "clientId不能为空")
	private String clientId;
	
	/**
	 * appId
	 */
	@NotEmpty(message = "appId不能为空")
	private String appId;
	
	/**
	 * 支付方式
	 */
	@NotEmpty(message = "付款方式不能为空")
	private String payMethod;
	
	
	public String createSign(String clientSecret) {
		Map<String, String> map = new HashMap<String, String>();
		if(!StringUtils.isBlank(payTradeNo)) {
			map.put("payTradeNo", payTradeNo);
		}
		
		map.put("intenalTradeNo", intenalTradeNo);
		map.put("openId", openId);
		map.put("payMethod", payMethod);
		map.put("amount", amount.toString());
		map.put("tradeDesc", tradeDesc);
		map.put("ip", ip);
		map.put("clientId", clientId);
		map.put("appId", appId);
		return PaymentKit.createSign(map, clientSecret);
	}
}
