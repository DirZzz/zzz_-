package com.sandu.cloud.activity.bargain.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BargainInviteRecordAddDto {

   
            
    @ApiModelProperty(value = "记录id")
    private String id;
        
            
    @ApiModelProperty(value = "报名id")
    private String registrationId;
        
            
    @ApiModelProperty(value = "砍价人open_id")
    private String openId;
        
            
    @ApiModelProperty(value = "砍价人昵称")
    private String nickname;
        
            
    @ApiModelProperty(value = "活动参与人头像")
    private String headPic;
        
            
    @ApiModelProperty(value = "砍掉价格")
    private String cutPrice;
        
            
    @ApiModelProperty(value = "砍后价格")
    private String remainPrice;
        
            
    @ApiModelProperty(value = "微信appid")
    private String appId;
        
            
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
        
            
    @ApiModelProperty(value = "是否删除：0未删除、1已删除")
    private Integer isDeleted;
        
    
}
