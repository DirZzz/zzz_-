package com.sandu.analysis.biz.usersRetention.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserRetentionResultQuery {

	private Date startTime;
	
	private Date endTime;
	
	private String appId;
	
}
