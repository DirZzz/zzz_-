package com.sandu.cloud.pay.model;

import lombok.Data;

@Data
public class AliPayConfig {
	
    public static String SERVICE_URL = "https://openapi.alipay.com/gateway.do";
    //"https://zhifu.sanduspace.com/v1/gateway/pay/callback/ali/notify";						  
    public static String PAY_NOTIFY_URL = "http://paydev.sanduspace.com:8089/v1/pay/callback/ali/notify";

	//三度云享家通用版
    private String appId;
    
    private String privateKey;
    
    private String publicKey;
    
    private String storeId;
    
    private String sellerId;
    
    
    public AliPayConfig(String appId, String privateKey, String publicKey, String storeId, String sellerId) {
		super();
		this.appId = appId;
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.storeId = storeId;
		this.sellerId = sellerId;
	}
   
}
