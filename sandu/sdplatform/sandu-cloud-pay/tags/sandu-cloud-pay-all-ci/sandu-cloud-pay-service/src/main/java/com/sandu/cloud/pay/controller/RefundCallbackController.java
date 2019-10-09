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

import com.jpay.ext.kit.HttpKit;
import com.jpay.ext.kit.PaymentKit;
import com.sandu.cloud.common.exception.BizException;
import com.sandu.cloud.pay.service.callback.RefundCallbackService;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/v1/refund/callback")
@Slf4j
public class RefundCallbackController {

	@Resource(name="wxRefundCallbackService")
	private RefundCallbackService refundCallbackService;
	
    @RequestMapping(value = "/wx/notify")
    public void wxPayCallback(HttpServletRequest request, HttpServletResponse response) {
    	log.info("wx退款回调");
        //获取xml回调报文
    	String xmlResult = HttpKit.readData(request);
 //   	String xmlResult = "<xml><return_code>SUCCESS</return_code><appid><![CDATA[wxd4934d0dab14d276]]></appid><mch_id><![CDATA[1509195691]]></mch_id><nonce_str><![CDATA[79b17bf51e079be780b4ae160c5f4f0e]]></nonce_str><req_info><![CDATA[QBDWSVrqQcOr3zMJvGWeFG0DFkeqceB/kuaOc37/KngmjfuzVv9LdtWwWeXN7nrH2Rf6qDslDixUPb6HPrnSTN4jukyuee2pS0SdvDHJuwDN4B4tfiQhQPysJoTPgkOYJL9Vl7ENw+k4pJKMiZXIIiDi2m2QmVH1iBg431OWqeOhnQPvCC9eJzuDzlYS6PLt4btB0ZF8JD0gsexOt1YfWnJOThdJo554fKl3tYQ/lfnVbujuAGGRE0/9ApJgxY14QeDCBNmVmLpe80dPvF41xt7A/f1lPvpZ6yXGXHZorxQOzEGhUu8lM3vtCNNGMLK/DiPkfVhM2bGmwVjCDCMqFh7HuL5ql0RXZ936RUiVWg5OB6LEp4xFWdAYeDhMdFZ7AS8h8SIBKgAB6nYkIUPNjjISYOkeDY7Lk4c5I4XmhWNYzWYrpHDxjnXGPT3dU1Bsy0j/y/DSwNL515J2cWuXZmMgVKL3RB/ek0J4Z6nhpP++F4thPTg+CA99CDszOtyeeiA6KG2uma07iBBzZdqpeTN4nrP9pzDLnx4CYxAVKz7Cg74wXEzl8bm6buHh2uxGKVqVCbxpEULtZGSo2bBxFhVX33MnOf2kbH2Upshn9X4VdqElkj2a6i4ONrfZ1E61auQgTvuc5NsXBePn5Bfjqb/gQBnipQRohowaHNOxInqW3QwOdbKcMvcx6Kv2XlU1mxptg1HoAunQoTBxj/TTNmWsorDDhYG6U5jS/km1tVXzUao43o3bnJBq6oJPMpGEIOR1jKhknq+nndZT/TfbRH/z6mLDNZt8BU3Lazd2nq9Rwl9k3cO4xkEWU0VUGbT2Vy3kjVY5zVPNZMwyNUR6nFVh5psi2pXR7colAfWMCkoG9/EwwoJv1tbuhYrQ+VNWLEuGBSQN2LaRI/NMapo0/9oD3F4FJIDHZcnDE/UIObxVJ3inuvM7XWxOToI6abv8XiicnErGHL4RS8MTpmnM5b2Oo/sPPCkNG6fTkier8q7N79ac0cQhXiXa7pqoZ9nseZnh/4wvdX/k15eWkYxjhhG9ZHUdblLzI4BWp35NPN+WbGNcWjIo/Fs9cn5Uq7gm2Jyu3H6uSiN0cVA1J1CMxQ==]]></req_info></xml>";
    	boolean success = false;
    	String errMsg = "";
    	try {
    		log.info("wx退款回调结果为 =>{}"+xmlResult);
    		success = refundCallbackService.callback(xmlResult);
    		//响应微信服务器
        	responseToWxServer(success,errMsg,response);
    	}catch(BizException ex) {
    		errMsg = ex.getMessage();
    	}catch(Exception ex) {
    		log.error("系统错误:", ex);
    		errMsg = "未知错误";
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
    		log.info("退款后,开始通知微信服务器:{}",xml);
    		PrintWriter writer = response.getWriter();
    		writer.write(PaymentKit.toXml(xml));
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
