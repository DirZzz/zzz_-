package com.sandu.cloud.notification.wechat.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.sandu.cloud.notification.wechat.dto.TemplateMsgReqParam;
import com.sandu.cloud.notification.wechat.service.TemplateMsgService;

@Component
public class AsyncTemplateMsgService {

	@Autowired
	private TemplateMsgService templateMsgService;
    
    @Async
    public void send(TemplateMsgReqParam templateMsgReqParam) {
		templateMsgService.sendTemplateMsg(templateMsgReqParam);
	}
}
