package com.sandu.cloud.notification.sms.service.impl;

import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sandu.cloud.notification.sms.exception.SmsBizException;
import com.sandu.cloud.notification.sms.exception.SmsBizExceptionCode;
import com.sandu.cloud.notification.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RefreshScope
public class SmsServiceImpl  implements SmsService{

	@Value("${sms.url}")
	private String url ;
	
	@Value("${sms.signKey}")
	private String signKey;
	
	@Value("${sms.password}")
	private String password;
	
	@Value("${sms.enable}")
	private boolean enable;

    @Autowired
    private RestTemplate restTemplate;

	@Override
	public void send(String mobile,String message) {
        if (enable) {
        	String result = null; 
        	try {
        		String reqUrl = url + "?cdkey={1}&password={2}&phone={3}&message={4}&seqid={5}";
	        	message = URLEncoder.encode(message, "UTF-8");
	            String responseString = restTemplate.getForObject(reqUrl, String.class,signKey,"067195",mobile,message,System.currentTimeMillis());
	        	//String responseString ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><error>0</error><message></message></response>";
	        	if (StringUtils.isNotBlank(responseString)) {
	        		result = xmlMt(responseString);
	            }
        	}catch(Exception e) {
        		log.error("短信发送异常:",e);
        		throw new RuntimeException(e);
        	}
        	if (!"0".equals(result)) {
            	throw new SmsBizException(SmsBizExceptionCode.SD_ERR_NOTIFY_SMS_SEND_ERROR);
            }
        } else {
            log.warn("短信通知服务已关闭!");
        }
	}
	
 
    private String xmlMt(String response) throws DocumentException {
        Document document = null;
        document = DocumentHelper.parseText(response);
        Element root = document.getRootElement();
        String result = root.elementText("error");
        return result;
    }
	
	
	
	

	


	
}
