package com.sandu.cloud.user.api.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandu.cloud.user.model.User;

@RequestMapping("/api/v1/user")
public interface UserServiceApiClient {
	
	@GetMapping("getByOpenId")
	User getByOpenId(@RequestParam(required=false,name="openId") String openId);
	
	
	@GetMapping("testGet")
	User testGet(@RequestParam("userId") Long userId);
	
	@PostMapping("/testUserSysException")
    public void testUserSysException();
	
	@PostMapping("/testUserBizException")
    public void testUserBizException();
}
