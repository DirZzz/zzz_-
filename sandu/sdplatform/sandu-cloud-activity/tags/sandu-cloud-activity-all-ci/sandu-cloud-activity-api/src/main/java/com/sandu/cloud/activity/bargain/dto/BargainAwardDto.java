package com.sandu.cloud.activity.bargain.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BargainAwardDto { 

    
        
    @ApiModelProperty(value = "")
        
    private String wxactbargainawardId;
        
        
        
    @ApiModelProperty(value = "活动id")
        
        
    private String actId;
        
        
    @ApiModelProperty(value = "报名id")
        
        
    private String registrationId;
        
        
    @ApiModelProperty(value = "兑奖人open_id")
        
        
    private String openId;
        
        
    @ApiModelProperty(value = "兑奖人昵称")
        
        
    private String nickname;
        
        
    @ApiModelProperty(value = "收货人")
        
        
    private String receiver;
        
        
    @ApiModelProperty(value = "手机号")
        
        
    private String mobile;
        
        
    @ApiModelProperty(value = "详细地址")
        
        
    private String address;
        
        
    @ApiModelProperty(value = "创建时间")
        
        
    private Date gmtCreate;
        
        
    @ApiModelProperty(value = "微信appid")
        
        
    private String appId;
        
    
}
