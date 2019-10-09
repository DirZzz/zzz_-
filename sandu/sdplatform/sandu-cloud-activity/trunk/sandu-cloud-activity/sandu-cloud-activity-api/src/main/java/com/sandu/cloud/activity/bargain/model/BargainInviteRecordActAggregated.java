package com.sandu.cloud.activity.bargain.model;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="wx_act_bargain_invite_record_act_aggregated")
public class BargainInviteRecordActAggregated {
   
	@Id
	private String id;

    private String openId;

    private String actId;

    private Integer inviteCutCount;

    private String appId;

}