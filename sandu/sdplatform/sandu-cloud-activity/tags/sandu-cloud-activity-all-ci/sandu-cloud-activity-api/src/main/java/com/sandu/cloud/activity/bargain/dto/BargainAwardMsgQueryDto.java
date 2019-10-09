package com.sandu.cloud.activity.bargain.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BargainAwardMsgQueryDto {

	private Integer pageNum = 1;
	private Integer pageSize = 10;
	
    @ApiModelProperty(value = "活动id",required = true)
    @NotNull(message = "砍价活动Id不能为空")
    private String actId;

}
