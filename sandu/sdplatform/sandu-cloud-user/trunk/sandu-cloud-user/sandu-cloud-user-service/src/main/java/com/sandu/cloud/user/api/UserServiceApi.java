package com.sandu.cloud.user.api;


import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.user.exception.UserBizException;
import com.sandu.cloud.user.exception.UserBizExceptionCode;
import com.sandu.cloud.user.model.User;
import com.sandu.cloud.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RefreshScope
@RequestMapping("/api/v1/user")
@Slf4j
@Validated
public class UserServiceApi {

	@Value("${custom.property.jdbc.url}")
	private String testProperty;
	
	@Autowired
	private UserService userService;

	
	
	@GetMapping(path = "/getByOpenId")
	public User getByOpenId(@NotEmpty(message="openId不能为空") @RequestParam(required=false,name="openId") String openId) {
		return userService.getByOpenId(openId);
	}
	

	@GetMapping(path = "/test")
	public String getCurrentAccount() {
		return testProperty;
	}
	
	@GetMapping(path = "/testGet")
	public User testGet(@RequestParam("userId") Long userId) {
		log.info("执行user服务");
		return null;
	}
	
	@PostMapping("/testAdd")
    public void testAdd() {
		System.out.println("testUpdate");
    }
	
	@PostMapping("/testException")
    public void testException() {
		//int a = 1/0;
		throw new UserBizException(UserBizExceptionCode.SD_ERR_USER_NOT_EXIST);
    }
	
	@PostMapping("/testUserSysException")
    public void testUserSysException() {
		int a = 1/0;
	}
	
	@PostMapping("/testUserBizException")
    public void testUserBizException() {
		throw new UserBizException(UserBizExceptionCode.SD_ERR_USER_NOT_EXIST);
	}
	
}
