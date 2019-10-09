package com.sandu.cloud.activity.bargain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BargainAwardMsgWebDto {

    @ApiModelProperty(value = "领取消息Id")
    private String awardmsgId;

    @ApiModelProperty(value = "活动id")
    private String actId;


    @ApiModelProperty(value = "消息内容")
    private String message;

}
