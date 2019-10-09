package com.sandu.cloud.activity.bargain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class BargainInviteRecordDto {

	
	
    public BargainInviteRecordDto(String nickname, String headPic, Double cutPrice,String cutTime) {
		super();
		this.nickname = nickname;
		this.headPic = headPic;
		this.cutPrice = cutPrice;
		this.cutTime = cutTime;
	}


	@ApiModelProperty(value = "砍价人昵称")
    private String nickname;
        
        
    @ApiModelProperty(value = "活动参与人头像")
    private String headPic;
        
        
    @ApiModelProperty(value = "砍掉价格")
    private Double cutPrice;
    
    @ApiModelProperty(value = "砍价时间")
    private String cutTime;
        
}
