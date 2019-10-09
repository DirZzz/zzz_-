package com.sandu.cloud.common.vo;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser extends User{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String mobile="123456789663";
	
	private String openId;
	
	private String nickName;
	
	private String appId;
	
	private String headPic;
	
	private Date gmtCreate;
	
	private Long companyId;
	
	
	public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
	}

}
