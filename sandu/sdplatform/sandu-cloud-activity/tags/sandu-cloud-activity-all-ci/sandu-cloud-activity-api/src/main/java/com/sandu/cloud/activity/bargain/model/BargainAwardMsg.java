package com.sandu.cloud.activity.bargain.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Table(name="wx_act_bargain_award_msg")
public class BargainAwardMsg {

    
	@Id
    private String id;
    /** 活动id */
    private String actId;
    /** 报名id */
    private String registrationId;
    /** 兑奖人open_id */
    private String openId;
    /** 消息内容 */
    private String message;
    /** 微信appid */
    private String appId;
    /** 创建人 */
    private String creator;
    /** 创建时间 */
    private Date gmtCreate;
    /** 修改人 */
    private String modifier;
    /** 修改时间 */
    private Date gmtModified;
    /** 是否删除：0未删除、1已删除 */
    private Integer isDeleted;


    
}
