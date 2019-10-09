package com.sandu.cloud.activity.bargain.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="wx_act_bargain_invite_record_act_day_aggregated")
public class BargainInviteRecordActDayAggregated {
    
	@Id
	private String id;

    private String openId;

    private Date inviteDate;

    private String actId;

    private Integer inviteCutCount;

    private String appId;

}