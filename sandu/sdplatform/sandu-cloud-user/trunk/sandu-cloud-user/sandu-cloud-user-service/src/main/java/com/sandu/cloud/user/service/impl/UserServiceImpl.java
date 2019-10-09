package com.sandu.cloud.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sandu.cloud.user.dao.UserDao;
import com.sandu.cloud.user.model.User;
import com.sandu.cloud.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User getByOpenId(String openId) {
		User user = new User();
		user.setOpenId(openId);
		user.setIsDeleted(0);
		return userDao.selectOne(user);
	}

}
