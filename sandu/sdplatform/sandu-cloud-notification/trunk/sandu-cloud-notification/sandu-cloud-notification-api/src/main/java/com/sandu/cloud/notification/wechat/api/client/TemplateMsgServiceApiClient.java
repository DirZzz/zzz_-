package com.sandu.cloud.notification.wechat.api.client;

import org.springframework.web.bind.annotation.RequestMapping;

import com.sandu.cloud.notification.wechat.dto.TemplateMsgReqParam;

@RequestMapping("/api/v1/notify/wechat/templateMsg")
public interface TemplateMsgServiceApiClient {
	
	@RequestMapping(path = "/send")
	public void send(TemplateMsgReqParam templateMsgReqParam);
	
}
