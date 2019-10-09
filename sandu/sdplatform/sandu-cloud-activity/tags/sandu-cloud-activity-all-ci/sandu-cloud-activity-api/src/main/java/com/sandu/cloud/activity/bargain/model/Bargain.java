package com.sandu.cloud.activity.bargain.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Table(name="wx_act_bargain")
public class Bargain{

	/**
	 * 未开始
	 */
	public static Integer STATUS_UNBEGIN = 0;
    /**
     * 进行中
     */
	public static Integer STATUS_ONGOING = 10;
    /**
     * 已结束
     */
	public static Integer STATUS_ENDED = 20;
   
    
    /** ID */
	@Id
    private String id;
    /** 砍价活动名称 */
    private String actName;
    /** 活动规则 */
    private String actRule;
    /** 转发图片 */
    private  String shareImg;
    /** 活动开始时间 */
    private Date begainTime;
    /**  活动结束时间 */
    private Date endTime;
    /** 产品名称 */
    private String productName;
    /** 原价 */
    private Double productOriginalPrice;
    /** 优惠价 */
    private Double productDiscountPrice;
    /** 底价 */
    private Double productMinPrice;
    /** 产品图片 */
    private String productImg;
    /** 产品数量 */
    private Integer productCount;
    /** 产品显示库存 */
    private Integer productDisplayCount;
    /** 产品剩余数量 */
    private Integer productRemainCount;
    /** 只记录虚拟扣除数量,不做逻辑处理(定时任务定时扣除) */
    private Integer productVitualCount;
    /** 参与人数 */
    private Integer registrationCount;
    /** 系统每小时扣减数量:(定时任务定时扣除,可当作参与人数与减少库存数) */
    private Integer sysReduceNum;
    /** 自己砍价最低金额 */
    private Double myCutPriceMin;
    /** 自己砍价最高金额 */
    private Double myCutPriceMax;
    /** 好友砍价最低金额 */
    private Double cutMethodPriceMin;
    /** 好友砍价最高金额 */
    private Double cutMethodPriceMax;
    /**1:只允许新用户;0:所有用户都可参与*/
    private Integer onlyAllowNew;
    /**only_allow_new=0时,每天可帮砍好友数*/
    private Integer helpCutPerDay;
    /**only_allow_new=0时,活动期间可帮砍好友数*/
    private Integer helpCutPerAct;
    
    /** 是否有效0:无效,1:有效 */
    private Integer isEnable;
    /** 微信appid */
    private String appId;
    /** 小程序名称 */
    private String appName;
    /** 小程序所属企业id */
    private Integer companyId;
    /** 小程序所属企业名称 */
    private String companyName;
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
    /** 复制链接  **/
    @Transient
    private String copyUrl;

    /** 活动转态 **/
    @Transient
    private Integer actStatus;
    
    public Bargain(){
    
    }
}