package com.sandu.cloud.notification.wechat.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.notification.wechat.async.AsyncTemplateMsgService;
import com.sandu.cloud.notification.wechat.dto.TemplateMsgReqParam;

@RestController
@RequestMapping("/api/v1/notify/wechat/templateMsg")
public class TemplateMsgServiceApi {

	@Autowired
	private AsyncTemplateMsgService asyncTemplateMsgService;

	@RequestMapping(path = "/send")
	public void send(@Valid TemplateMsgReqParam templateMsgReqParam) {
		asyncTemplateMsgService.send(templateMsgReqParam);
	}
	
}
