package com.sandu.cloud.pay.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.pay.config.AppClientConfig;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;
import com.sandu.cloud.pay.exception.PayBizException;
import com.sandu.cloud.pay.exception.PayExceptionCode;
import com.sandu.cloud.pay.model.ClientInfo;
import com.sandu.cloud.pay.service.forward.PayService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/pay")
@Slf4j
public class PayServiceApi {

	@Autowired
	private Map<String, PayService> payStrategy = new ConcurrentHashMap<>();
    
    @RequestMapping(value = "/doPay",method = RequestMethod.POST)
	public String doPay(@Valid @RequestBody(required=false) PayParamDto payParam) {
    	// 验证签名
    	verifySign(payParam);
		PayService payService = payStrategy.get(payParam.getPayMethod());
		if(payService==null) {
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_METHOD_ERROR);
		}
		String strResult = payService.doPay(payParam);
		return strResult;
	}
    
    private void verifySign(PayParamDto payParam) {
    	ClientInfo clientInfo = AppClientConfig.getClientInfo(payParam.getClientId());
    	String sign = payParam.createSign(clientInfo.getClientSecret());
    	if(!sign.equals(payParam.getSign())) {
    		log.error("签名错误,正确签名为:{}",sign);
    		throw new PayBizException(PayExceptionCode.SD_ERR_PAY_SIGN_ERROR);
    	}
    }
    
    

	@RequestMapping(value = "/doRefund", method = RequestMethod.POST)
	@ResponseBody
	public Object doRefund(@Valid @RequestBody(required=false) RefundParamDto refundParam) {
		// 验证签名
		verifySign(refundParam);
		PayService payService = payStrategy.get(refundParam.getPayMethod());
		if(payService==null) {
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_METHOD_ERROR);
		}
		String obj = payService.doRefund(refundParam);
		log.info("退款接口完成 =>{}", obj);
		return obj;
	}
	
	private void verifySign(RefundParamDto refundParam) {
		ClientInfo clientInfo = AppClientConfig.getClientInfo(refundParam.getClientId());
		String sign = refundParam.createSign(clientInfo.getClientSecret());
		if (!sign.equals(refundParam.getSign())) {
			log.error("签名错误,正确签名为:{}", sign);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_SIGN_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/doTransfer", method = RequestMethod.POST)
	@ResponseBody
	public Object doTransfer(@Valid @RequestBody(required=false) TransfersParamDto transfersParamDto) {
		// 验证签名
		verifySign(transfersParamDto);
		PayService payService = payStrategy.get(transfersParamDto.getPayMethod());
		if(payService==null) {
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_METHOD_ERROR);
		}
		String obj = payService.doTransfers(transfersParamDto);
		log.info("企业付款接口完成 =>{}", obj);
		return obj;
	}
	
	private void verifySign(TransfersParamDto transfersParamDto) {
		ClientInfo clientInfo = AppClientConfig.getClientInfo(transfersParamDto.getClientId());
		String sign = transfersParamDto.createSign(clientInfo.getClientSecret());
		if (!sign.equals(transfersParamDto.getSign())) {
			log.error("签名错误,正确签名为:{}", sign);
			throw new PayBizException(PayExceptionCode.SD_ERR_PAY_SIGN_ERROR);
		}
	}

   
    public static void main(String[] args) {
    	System.out.println(System.currentTimeMillis());
    	System.out.println(DigestUtils.md5Hex("clientId=000001&timestamp=1558072768514&key=test&refundFee=1").toUpperCase());
	}

}
