package com.sandu.cloud.pay.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WxPayConfig{

	public static String WX_NOTIFY_URL = "http://paydev.sanduspace.com:8089/v1/pay/callback/wx/notify";
	public static String WX_REFUND_NOTIFY_URL = "http://paydev.sanduspace.com:8089/v1/refund/callback/wx/notify";
	/**
	 * 应用名称
	 */
	private String appName;
	
	/**
	 * 应用id
	 */
	private String appId;
	
	/**
	 * 密码
	 */
	private String appSecret;
	
	/**
	 * 商户id
	 */
	private String mchId;
	
	/**
	 * 商户密钥
	 */
	private String mchKey;
	
	/**
	 * 证书路径 
	 */
	private String apiCertPath;
	
	private String certPass;
	
}
