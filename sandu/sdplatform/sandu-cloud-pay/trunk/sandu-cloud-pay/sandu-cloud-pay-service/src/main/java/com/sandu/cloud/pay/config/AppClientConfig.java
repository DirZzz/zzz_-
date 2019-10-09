package com.sandu.cloud.pay.config;

import java.util.HashMap;
import java.util.Map;

import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.AliPayConfig;
import com.sandu.cloud.pay.model.ClientInfo;
import com.sandu.cloud.pay.model.WxPayConfig;

public class AppClientConfig {

	
	private static Map<String,ClientInfo> CLIENT_INFO_MAP = new HashMap<String,ClientInfo>();
	static {
		CLIENT_INFO_MAP.put("001", new ClientInfo("001","活动服务","123456"));
	}
	
	private static Map<String,WxPayConfig> WX_PAY_CONFIG_MAP = new HashMap<String,WxPayConfig>();
	static {
		WX_PAY_CONFIG_MAP.put("wx17c320f6c1feb057", new WxPayConfig("左右旗舰总店","wx17c320f6c1feb057","b7126f2a4dc242be3a34c9fe5dfac3a1","1394367302","wxb3048NorKad782765143df7NorKcb2",null,null));
		WX_PAY_CONFIG_MAP.put("wxe24ed743feb9c17f", new WxPayConfig("诺克照明","wxe24ed743feb9c17f","e7b41561564c9934b054f6bd9d62ad17","1378786102","wxb3048NorKad782765143df7NorKcb2","/data001/data/pay_certificate/norklighting/apiclient_cert.p12","1378786102"));
		WX_PAY_CONFIG_MAP.put("wx0c11b729a27ec96a", new WxPayConfig("巴洛克木业（生活家）","wx0c11b729a27ec96a","3e6125ef3c4767ddb64bc1e4d837563b","1226164102","abc123456Aabc123456Aabc123456AAA",null,null));
		WX_PAY_CONFIG_MAP.put("wxb13c586ba00424d4", new WxPayConfig("方内生活","wxb13c586ba00424d4","af76ea5f44ccb5f0c2a031ba92671e00","1505588411","wxb13c586ba00424d4yfjp1234567com",null,null));
		WX_PAY_CONFIG_MAP.put("wx0d37f598e1028825", new WxPayConfig("三度空间总店","wx0d37f598e1028825","803e47542a23f5da15e50c567f702419","1394367302","wxb3048NorKad782765143df7NorKcb2",null,null));
		WX_PAY_CONFIG_MAP.put("wx42e6b214e6cdaed3", new WxPayConfig("随选网","wx42e6b214e6cdaed3","db38a182abfaaa2204f16375588f399c","1394367302","wxb3048NorKad782765143df7NorKcb2","C:\\Users\\Administrator\\Desktop\\certs\\suixuanwang\\apiclient_cert.p12","1394367302"));
		WX_PAY_CONFIG_MAP.put("wxab644357fca9a0ce", new WxPayConfig("TJ天基电气","wxab644357fca9a0ce","380dc9186265926ba18ca8408c09859c","1509648501","52tianjidianqizhongguo8009993819",null,null));
		WX_PAY_CONFIG_MAP.put("wxe8455e63bb9f7c25", new WxPayConfig("nvc雷士家居照明","wxe8455e63bb9f7c25","d6dfc5771f371f4f8afb462c5ad76102","1521865871","Heatonseazion213555888666533leis","/data001/data/pay_certificate/leisiNVC/apiclient_cert.p12","1521865871"));
		WX_PAY_CONFIG_MAP.put("wx6e369e0fac7d3273", new WxPayConfig("三度装修公司","wx6e369e0fac7d3273","468772af53426b4d54fed86fb476ef77","1394367302","wxb3048NorKad782765143df7NorKcb2",null,null));
		WX_PAY_CONFIG_MAP.put("wxc168f92a18e5c196", new WxPayConfig("三度云享家","wxc168f92a18e5c196","19e9668bc7a920dffbf327c03f86ff3b","1394367302","wxb3048NorKad782765143df7NorKcb2",null,null));
		
		//微信扫码
		WX_PAY_CONFIG_MAP.put("wxd4934d0dab14d276", new WxPayConfig("三度通用版","wxd4934d0dab14d276","test","1509195691","wxd4934d0dab14d276167ff8cSanDucb","C:\\Users\\Administrator\\Desktop\\certs\\sanduyunapp\\apiclient_cert.p12","1509195691"));
		//微信h5
		WX_PAY_CONFIG_MAP.put("wx9bbed019f8b2f4fb", new WxPayConfig("三度空间","wx9bbed019f8b2f4fb","test","1394367302","wxb3048NorKad782765143df7NorKcb2","C:\\Users\\Administrator\\Desktop\\certs\\sanduyunapp\\apiclient_cert.p12","1394367302"));

	}
	
	private static Map<String,AliPayConfig> AL_PAY_CONFIG_MAP = new HashMap<String,AliPayConfig>();
	static {
		AL_PAY_CONFIG_MAP.put("2016092301955254", new AliPayConfig("2016092301955254",
    			"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",
    			"10001","2088421876638111"));
	}
	
	
	public static ClientInfo getClientInfo(String clientId) {
		ClientInfo clientInfo = CLIENT_INFO_MAP.get(clientId);
		if(clientInfo==null) {
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_APP_CLIENT_NOT_FOUND);
		}
		return clientInfo;
	}

	
	public static WxPayConfig getWxPayConfig(String appId) {
		WxPayConfig wxPayConfig = WX_PAY_CONFIG_MAP.get(appId);
		if(wxPayConfig==null) {
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_PAY_CONFIG_NOT_FOUND);
		}
		return wxPayConfig;
	}
	
	public static AliPayConfig getAliPayConfig(String appId) {
		AliPayConfig aliPayConfig = AL_PAY_CONFIG_MAP.get(appId);
		if(aliPayConfig==null) {
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_WX_PAY_CONFIG_NOT_FOUND);
		}
		return aliPayConfig;
	}
	
	

}


