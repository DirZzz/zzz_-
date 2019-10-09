package com.sandu.cloud.activity.bargain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RegistrationStatusDto {

	@ApiModelProperty(value = "任务id")
    private String registrationId;
        
    @ApiModelProperty(value = "任务状态")
    private String statusCode;

	public RegistrationStatusDto(String statusCode,String registrationId) {
		super();
		this.statusCode = statusCode;
		this.registrationId = registrationId;
	}
    
    
    
}
