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
import lombok.ToString;


@Data
@ToString
public class PayParamDto{
	
	
	/**
	 * 小程序支付需要用到
	 */
	private String openId;
	
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
	 * 交易描述
	 */
	@NotEmpty(message = "交易详情不能为空")
	private String tradeDesc;
	/**
	 * 费用,单位为分
	 */
	@NotNull(message = "交易金额不能为空")
	@Min(value=1,message = "交易金额必须大于0")
	private Long totalFee;
	
	/**
	 * 支付方式
	 */
	@NotEmpty(message = "支付方式不能为空")
	private String payMethod;
	
	/**
	 * 调用方ip
	 */
	@NotEmpty(message = "ip地址不能为空")
	private String ip;
	
	
	/**
	 * 重定向url H5支付使用
	 */
	private String redirectUrl;
	
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
	
	/**
	 * clientId
	 */
	@NotEmpty(message = "clientId不能为空")
	private String clientId;
	
	
	/**
	 * appId
	 */
	@NotEmpty(message = "appId不能为空")
	private String appId;

	/**
	 * 公用回传参数
	 */
	private String passbackParams;
	
	
	public String createSign(String clientSecret) {
		Map<String, String> map = new HashMap<String, String>();
		if(!StringUtils.isBlank(openId)) {
			map.put("openId", openId);
		}
		if(!StringUtils.isBlank(payTradeNo)) {
			map.put("payTradeNo", payTradeNo);
		}
		if(!StringUtils.isBlank(redirectUrl)) {
			map.put("redirectUrl", redirectUrl);
		}
		if(!StringUtils.isBlank(passbackParams)) {
			map.put("passbackParams", passbackParams);
		}
		
		map.put("intenalTradeNo", intenalTradeNo);
		map.put("tradeDesc", tradeDesc);
		map.put("totalFee", totalFee.toString());
		map.put("payMethod", payMethod);
		map.put("ip", ip);
		map.put("notifyUrl", notifyUrl);
		map.put("clientId", clientId);
		map.put("appId", appId);
		
		return PaymentKit.createSign(map, clientSecret);
	}
	
	public static void main(String[] args) {
		PayParamDto payParamDto = new PayParamDto();
		//应用系统交易号
		payParamDto.setIntenalTradeNo("123456");
		//支付描述
		payParamDto.setTradeDesc("tradeDesc");
		//支付金额
		payParamDto.setTotalFee(1L);
		//支付方式
		payParamDto.setPayMethod(PayMethodContants.WX_SCANCODE_PAY);
		//客户端ip
		payParamDto.setIp("127.0.0.1");
		//支付成功回调地址
		payParamDto.setNotifyUrl("http://127.0.0.1:8089/v1/pay/callback/test/notify");
		//客户端id
		payParamDto.setClientId("001");
		//收款应用id
		payParamDto.setAppId("wxd4934d0dab14d276");
		//生成签名
		payParamDto.setSign(payParamDto.createSign("123456"));
		
		System.out.println(payParamDto.getSign());
		
	}
	

}
