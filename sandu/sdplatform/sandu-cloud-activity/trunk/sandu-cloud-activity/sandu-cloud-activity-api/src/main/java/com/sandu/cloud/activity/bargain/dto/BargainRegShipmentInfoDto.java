package com.sandu.cloud.activity.bargain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class BargainRegShipmentInfoDto {

	@ApiModelProperty(value = "发货单号")
	private String carrier;
        
    @ApiModelProperty(value = "发货单号")
    private String shipmentNo;
        
}
