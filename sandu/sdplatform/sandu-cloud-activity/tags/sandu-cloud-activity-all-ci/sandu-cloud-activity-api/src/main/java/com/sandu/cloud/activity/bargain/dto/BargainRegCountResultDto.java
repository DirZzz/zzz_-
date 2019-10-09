package com.sandu.cloud.activity.bargain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BargainRegCountResultDto {

        
    @ApiModelProperty(value = "时间点")
    private String time;
        
    @ApiModelProperty(value = "数量")
    private Integer num;
    
    
}
