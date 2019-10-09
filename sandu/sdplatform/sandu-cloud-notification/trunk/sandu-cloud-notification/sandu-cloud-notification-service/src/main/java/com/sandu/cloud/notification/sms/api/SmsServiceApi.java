package com.sandu.cloud.notification.sms.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.notification.sms.api.client.TestVo;
import com.sandu.cloud.notification.sms.service.SmsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/notify/sms")
@Validated
@Slf4j
public class SmsServiceApi {
	
	@Autowired
	private SmsService smsService;

	@RequestMapping(path = "/send")
	public void send(
	@Pattern(regexp = "^((17[0-9])|(2[0-9][0-9])|(13[0-9])|(15[012356789])|(18[0-9])|(14[57])|(16[0-9])|(19[0-9]))[0-9]{8}$", message = "手机号码不正确！")
	@NotEmpty(message="手机号码不能为空!") @RequestParam(required=false,name="mobile") String mobile,
	@NotEmpty(message="短信内容不能为空!") @RequestParam(required=false,name="message") String message) {
		log.info(mobile+"----"+message);
		smsService.send(mobile,message);
	}
	
	@RequestMapping(path = "/send2")
	public void send2(@RequestBody(required=false) TestVo testVo) {
		log.info(JsonUtils.toJson(testVo));
	}
	
}
