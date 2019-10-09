package com.sandu.cloud.activity.bargain.dto;

import lombok.Data;

@Data
public class BargainInviteRecordQueryDto{
	
	/** 报名id */
    private String registrationId;
    /** 砍价人open_id */
    private String openId;
    /** 是否删除：0未删除、1已删除 */
    private Integer isDeleted;

}
