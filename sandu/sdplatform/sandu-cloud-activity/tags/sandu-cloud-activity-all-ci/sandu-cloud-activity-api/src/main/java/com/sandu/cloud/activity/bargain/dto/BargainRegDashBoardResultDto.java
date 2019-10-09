package com.sandu.cloud.activity.bargain.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BargainRegDashBoardResultDto {

        
    @ApiModelProperty(value = "参与人数")
    private List<BargainRegCountResultDto> regList;
        
    @ApiModelProperty(value = "砍价成功人数")
    private List<BargainRegCountResultDto> regSuccessList;
    
    @ApiModelProperty(value = "所有参与砍价的人数(包括自己,好友,装进我家)")
    private List<BargainRegCountResultDto> cutList;
    
    
}
