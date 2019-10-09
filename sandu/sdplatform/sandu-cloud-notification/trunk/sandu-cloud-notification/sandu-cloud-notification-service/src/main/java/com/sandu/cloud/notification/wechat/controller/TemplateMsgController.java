package com.sandu.cloud.notification.wechat.controller;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.common.login.LoginContext;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.common.vo.ResponseEnvelope;
import com.sandu.cloud.notification.wechat.service.TemplateMsgService;


@RestController
@RequestMapping("v1/notify/wechat/templateMsg")
public class TemplateMsgController {

	@Autowired
	private TemplateMsgService templateMsgService;
	
	@PostMapping(path = "/collectFormId")
	public ResponseEnvelope collectFormId(@NotNull(message="formIdList不能为空") @RequestBody(required=false) List<String> formIdList) {
		LoginUser user = LoginContext.getLoginUser();
		templateMsgService.collectMiniUserFormId(user.getOpenId(), formIdList);
		return ResponseEnvelope.ok();
	}
	
	
	
	
}
