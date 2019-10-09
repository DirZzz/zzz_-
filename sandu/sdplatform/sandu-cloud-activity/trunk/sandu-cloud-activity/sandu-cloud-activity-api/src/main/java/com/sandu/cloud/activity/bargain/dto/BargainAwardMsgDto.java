package com.sandu.cloud.activity.bargain.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BargainAwardMsgDto {

    @ApiModelProperty(value = "领取消息Id")
    private String id;
        
        
        
    @ApiModelProperty(value = "活动id")
    private String actId;
        
        
    @ApiModelProperty(value = "报名id")
    private String registrationId;
        
        
    @ApiModelProperty(value = "兑奖人open_id")
    private String openId;
        
        
    @ApiModelProperty(value = "消息内容")
    private String message;
        
        
    @ApiModelProperty(value = "微信appid")
    private String appId;
        
        
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
