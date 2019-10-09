package com.sandu.cloud.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jpay.alipay.AliPayApi;
import com.jpay.ext.kit.HttpKit;
import com.jpay.ext.kit.PaymentKit;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.pay.service.callback.PayCallbackService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/v1/pay/callback")
@Slf4j
public class PayCallbackController {

	@Resource(name="wxPayCallbackService")
	private PayCallbackService wxCallbackService;
	
	@Resource(name="aliPayCallbackService")
	private PayCallbackService aliPayCallbackService;
	
  
    @RequestMapping(value = "/wx/notify")
    public void wxPayCallback(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		log.info("wx支付回调");
        	String xmlResult = HttpKit.readData(request);  //获取xml回调报文
        //	String xmlResult="<xml><appid><![CDATA[wxd4934d0dab14d276]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1509195691]]></mch_id><nonce_str><![CDATA[1557986522326]]></nonce_str><openid><![CDATA[o0fAQ1aXYMSLAcYso-dtzOepkpxo]]></openid><out_trade_no><![CDATA[20190516140202320018115389719004]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[FB4C172C8B8D3AC350BBBC0523AC34C1]]></sign><time_end><![CDATA[20190516140342]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[NATIVE]]></trade_type><transaction_id><![CDATA[4200000304201905164848416375]]></transaction_id></xml>";
        	boolean success = false;
        	String errMsg = "";
    		log.info("wx回调结果为 =>{}"+xmlResult);
    		success = wxCallbackService.callback(xmlResult);
        	responseToWxServer(success,errMsg,response);//响应微信服务器
    	}catch(Exception ex) {
    		log.error("微信回调异常:", ex);
    	}
    	
    }
    
    private	void responseToWxServer(boolean success,String errMsg,HttpServletResponse response) {
    	Map<String, String> xml = new HashMap<String, String>();
    	if(success) {
    		xml.put("return_code","SUCCESS");
    		xml.put("return_msg", "OK");
    	}else {
    		xml.put("return_code","FAIL");
    		xml.put("return_msg", errMsg);
    	}
    	try {
    		log.info("付款后,通知微信服务器:{}",xml);
    		PrintWriter writer = response.getWriter();
    		writer.write(PaymentKit.toXml(xml));
			writer.flush();
		} catch (IOException e) {
			log.error("微信回调网络异常:", e);
		}
    }

    @RequestMapping(value = "/ali/notify")
    public void aliPayCallback(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		Map<String, String> map = AliPayApi.toMap(request);
        	log.info("支付宝通知参数:{}",map);
        	boolean success = false;
    		success = aliPayCallbackService.callback(map);
    		responseToAliServer(success,response); //响应支付宝服务器
    	}catch(Exception ex) {
    		log.error("系统错误:", ex);
    	}
    	
    }
    
    private	void responseToAliServer(boolean success,HttpServletResponse response) {
    	try {
    		log.info("付款后,开始通知支付宝服务器:{}",success);
    		PrintWriter writer = response.getWriter();			
    		if(success) {
    			writer.write("success");
    		}else {
    			writer.write("failure");
    		}
    		writer.flush();
		} catch (IOException e) {
			log.error("网络异常:", e);
		}
    }
    
    @RequestMapping(value = "/test/notify")
    public void test(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		log.error("返回参数:");
    		Map<String, String[]> requestParams = request.getParameterMap();
    		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
    			String name = (String) iter.next();
    			String[] values = (String[]) requestParams.get(name);
    			String valueStr = "";
    			for (int i = 0; i < values.length; i++) {
    				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
    			}
    			// 乱码解决，这段代码在出现乱码时使用。
    			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
    			log.error(name+" : "+valueStr);
    		}
//    		return ResponseEnvelope.ok();
			response.getWriter().print("SUCCESS");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
}
