package com.sandu.cloud.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.user.client.PayServiceClient;
import com.sandu.cloud.user.exception.UserBizException;
import com.sandu.cloud.user.exception.UserBizExceptionCode;
import com.sandu.cloud.user.model.User;
import com.sandu.cloud.user.service.UserService;

@RestController
@RefreshScope
@RequestMapping("v1/user")
public class UserController {

	@Value("${custom.property.jdbc.url}")
	private String testProperty;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PayServiceClient payServiceClient;
	

	@GetMapping(path = "/test")
	public String getCurrentAccount() {
		return testProperty;
	}
	
	@RequestMapping(path = "/queryById", method = RequestMethod.GET)
	public User queryById() {
		return null;
	}
	
	@PostMapping("/testException")
    public void testException() {
		//int a = 1/0;
		throw new UserBizException(UserBizExceptionCode.SD_ERR_USER_NOT_EXIST);
    }
	
	
	
}
