package com.sandu.cloud.user.service;

import com.sandu.cloud.user.model.User;

public interface UserService {
	
	User getByOpenId(String openId);
	
}
