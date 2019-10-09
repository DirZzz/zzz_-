package com.sandu.cloud.activity.bargain.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BargainAwardMsgAddDto {

    @ApiModelProperty(value = "活动id",required = true)
    @NotNull(message = "活动id不能为空")
    private String actId;

    @ApiModelProperty(value = "消息内容",required = true)
    @NotNull(message = "消息内容不能为空")
    private String message;

    @ApiModelProperty(value = "appId",required = true)
    @NotNull(message = "appId不能为空")
    private String appId;

}
