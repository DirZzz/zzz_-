package com.sandu.cloud.activity.bargain.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BargainAddDto {

    @ApiModelProperty(value = "ID")
    private String id;

    @NotNull(message = "活动名称不能为空")
    @ApiModelProperty(value = "砍价活动名称")
    private String actName;
        
            
    @ApiModelProperty(value = "活动描述")
    private String actDetail;
        
            
    @ApiModelProperty(value = "活动规则")
    private String actRule;

    @ApiModelProperty(value = "转发图片")
    private  String shareImg;
        
    @NotNull(message = "活动开始时间不能为空")
    @ApiModelProperty(value = "活动开始时间")
    private Date begainTime;

    @NotNull(message = "活动结束时间不能为空")
    @ApiModelProperty(value = " 活动结束时间")
    private Date endTime;

    @NotNull(message = "产品名称不能为空")
    @ApiModelProperty(value = "产品名称")
    private String productName;

    @NotNull(message = "产品原价不能不空")
    @ApiModelProperty(value = "原价")
    private Double productOriginalPrice;

    @NotNull(message = "产品优惠价不能为空")
    @ApiModelProperty(value = "优惠价")
    private Double productDiscountPrice;

    @NotNull(message = "产品底价不能为空")
    @ApiModelProperty(value = "底价")
    private Double productMinPrice;
        
            
    @ApiModelProperty(value = "产品图片")
    private String productImg;

    @NotNull(message = "商品实际库存不能为空")
    @ApiModelProperty(value = "商品实际库存")
    private Integer productCount;
    
    @NotNull(message = "商品显示库存不能为空")
    @ApiModelProperty(value = "商品显示库存")
    private Integer productDisplayCount;

    @ApiModelProperty(value = "产品剩余数量")
    private Integer productRemainCount;
        
            
    @ApiModelProperty(value = "只记录虚拟扣除数量,不做逻辑处理(定时任务定时扣除)")
    private Integer productVitualCount;

    //@NotNull(message = "参与人数不能为空")
    @ApiModelProperty(value = "参与人数")
    private Integer registrationCount;
        
            
    @ApiModelProperty(value = "系统每小时扣减数量:(定时任务定时扣除,可当作参与人数与减少库存数)")
    private Integer sysReduceNum;

    @NotNull(message = "自己砍价最低金额不能为空")
    @ApiModelProperty(value = "自己砍价最低金额")
    private Double myCutPriceMin;

    @NotNull(message = "自己砍价最高金额不能为空")
    @ApiModelProperty(value = "自己砍价最高金额")
    private Double myCutPriceMax;

    @NotNull(message = "好友砍价最低金额不能为空")
    @ApiModelProperty(value = "好友砍价最低金额")
    private Double cutMethodPriceMin;

    @NotNull(message = "好友砍价最高金额不能为空")
    @ApiModelProperty(value = "好友砍价最高金额")
    private Double cutMethodPriceMax;
        
            
    @ApiModelProperty(value = "最少邀请好友数")
    private Integer cutMethodInviteNumMin;
        
            
    @ApiModelProperty(value = "最多邀请好友数")
    private Integer cutMethodInviteNumMax;
    
    @NotNull(message = "请选择新老用户限制规则")
    @ApiModelProperty(value = "1:只允许新用户;0:所有用户都可参与")
    private Integer onlyAllowNew;
    
    @ApiModelProperty(value = "only_allow_new=0时,每天可帮砍好友数")
    private Integer helpCutPerDay;

    @ApiModelProperty(value = "only_allow_new=0时,活动期间可帮砍好友数")
    private Integer helpCutPerAct;
            
    @ApiModelProperty(value = "是否有效0:无效,1:有效")
    private Integer isEnable;
        
    @NotNull(message = "微信appid不能为空")
    @ApiModelProperty(value = "微信appid")
    private String appId;
        
            
    @ApiModelProperty(value = "小程序名称")
    private String appName;
        
            
    @ApiModelProperty(value = "小程序所属企业id")
    private Integer companyId;
        
            
    @ApiModelProperty(value = "小程序所属企业名称")
    private String companyName;
        
            
    @ApiModelProperty(value = "创建人")
    private String creator;
        
            
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
        
            
    @ApiModelProperty(value = "修改人")
    private String modifier;
        
            
    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;
        
            
    @ApiModelProperty(value = "是否删除：0未删除、1已删除")
    private Integer isDeleted;

}
