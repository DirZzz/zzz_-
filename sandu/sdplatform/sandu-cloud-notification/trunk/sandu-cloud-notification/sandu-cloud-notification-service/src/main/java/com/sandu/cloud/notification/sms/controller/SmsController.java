package com.sandu.cloud.notification.sms.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/notify/sms")
public class SmsController {

	/*@Autowired
	private SmsService smsService;
	
	@GetMapping(path = "/send")
	public ResponseEnvelope send(
			@Pattern(regexp = "^((17[0-9])|(2[0-9][0-9])|(13[0-9])|(15[012356789])|(18[0-9])|(14[57])|(16[0-9])|(19[0-9]))[0-9]{8}$", message = "手机号码不正确！")
			@NotEmpty(message="手机号码不能为空!") String mobile,
			@NotEmpty(message="短信内容不能为空!") String message) {
		smsService.send(mobile, message);
		return ResponseEnvelope.ok();
	}*/
	
	
	
	
}
