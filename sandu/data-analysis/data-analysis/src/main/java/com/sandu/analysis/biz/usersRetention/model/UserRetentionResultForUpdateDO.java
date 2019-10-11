package com.sandu.analysis.biz.usersRetention.model;

import lombok.Data;

@Data
public class UserRetentionResultForUpdateDO {

	private Long id;
	
	private Integer oneDayRetentionCount;
	
	private Integer threeDayRetentionCount;
	
	private Integer sevenDayRetentionCount;
	
	private Integer thirtyDayRetentionCount;
	
}
