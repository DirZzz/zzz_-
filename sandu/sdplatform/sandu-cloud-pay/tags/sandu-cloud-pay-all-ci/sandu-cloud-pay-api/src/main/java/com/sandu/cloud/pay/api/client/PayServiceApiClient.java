package com.sandu.cloud.pay.api.client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;

@RequestMapping("/api/v1/pay")
public interface PayServiceApiClient {

	@PostMapping(value = "/doPay")
	public String doPay(@RequestBody(required = false) PayParamDto payParam);
	
	@PostMapping(value = "/doRefund")
	public String doRefund(@RequestBody(required = false) RefundParamDto refundParam);

	@PostMapping(value = "/doTransfer")
	public String doTransfer(@RequestBody(required = false) TransfersParamDto transfersParamDto);
}
