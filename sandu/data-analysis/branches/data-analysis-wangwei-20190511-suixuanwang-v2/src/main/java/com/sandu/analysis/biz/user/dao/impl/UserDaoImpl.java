package com.sandu.analysis.biz.user.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sandu.analysis.biz.user.dao.UserDao;

public class UserDaoImpl implements UserDao {

	@Override
	public List<String> selectUuid(LocalDateTime startTime, LocalDateTime endTime) {
		/*return new ArrayList<String>();*/
		return new ArrayList<String>(Arrays.asList("c4fa6857bb3f11e8b1d5f403434d7108"));
	}

}
