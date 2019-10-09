package com.sandu.cloud.common.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sandu.cloud.common.vo.LoginUser;

public class LoginContext {

	public static LoginUser getLoginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null) {
			return (LoginUser)authentication.getPrincipal();
		}
		return null;
	}
}
