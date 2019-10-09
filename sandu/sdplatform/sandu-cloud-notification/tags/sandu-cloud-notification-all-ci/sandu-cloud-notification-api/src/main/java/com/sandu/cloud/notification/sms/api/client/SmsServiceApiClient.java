package com.sandu.cloud.notification.sms.api.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/notify/sms")
public interface SmsServiceApiClient {
	
	@RequestMapping(path = "/send")
	void send(@RequestParam(name="mobile",required=false) String mobile,
			  @RequestParam(name="message",required=false) String message);
	
	@RequestMapping(path = "/send2")
	void send2(@RequestBody(required=false) TestVo testVo);
	
}
