package com.sandu.analysis.biz.user.dao;

import java.time.LocalDateTime;
import java.util.List;

public interface UserDao {

	List<String> selectUuid(LocalDateTime startTime, LocalDateTime endTime);

	
}
