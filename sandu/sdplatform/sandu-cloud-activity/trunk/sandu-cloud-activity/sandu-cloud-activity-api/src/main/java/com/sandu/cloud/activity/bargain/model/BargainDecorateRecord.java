package com.sandu.cloud.activity.bargain.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Table(name="wx_act_bargain_decorate_record")
public class BargainDecorateRecord {

    
    /** 记录id */
	@Id
    private String id;
    /** 报名id */
    private String registrationId;
    /** 砍价人open_id */
    private String openId;
    /** 活动参与人头像 */
    private String headPic;
    /** 户型id */
    private Long houseId;
    /** 户型名称 */
    private String houseName;
    /** 砍掉价格 */
    private Double cutPrice;
    /** 砍后价格 */
    private Double remainPrice;
    /** 微信appid */
    private String appId;
    /** 创建人 */
    private String creator;
    /** 创建时间 */
    private Date gmtCreate;
    /** 是否删除：0未删除、1已删除 */
    private Integer isDeleted;
    
}
