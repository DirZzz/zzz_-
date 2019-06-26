package com.sandu.analysis.biz.usersRetention.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.sandu.analysis.biz.usersRetention.model.UserRetentionResultDO;
import com.sandu.analysis.biz.usersRetention.model.UserRetentionResultForUpdateDO;

public interface UserRetentionResultDao {

	/**
	 * 第一步, 删除startTime和endTime相同的数据(逻辑删除)
	 * 第二步, insert对应数据
	 * 
	 * @param userRetentionResultDO
	 */
	void insertBeforeDelete(UserRetentionResultDO userRetentionResultDO);

	List<UserRetentionResultDO> select(LocalDateTime startTime, LocalDateTime endTime);

	void update(UserRetentionResultForUpdateDO userRetentionResultForUpdateDO);

}
