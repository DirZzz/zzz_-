package com.sandu.cloud.activity.bargain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BargainAwardAddDto {


	@ApiModelProperty(value = "报名id")
    @NotNull(message = "任务id不能为空!")
    private String regId;
    
            
    @ApiModelProperty(value = "收货人")
    @NotNull(message = "收货人不能为空!")
    private String receiver;
        
            
    @ApiModelProperty(value = "手机号")
    @NotNull(message = "手机号不能为空!")
    @Pattern(regexp = "^((17[0-9])|(2[0-9][0-9])|(13[0-9])|(15[012356789])|(18[0-9])|(14[57])|(16[0-9])|(19[0-9]))[0-9]{8}$", message = "手机号码不正确！")
    private String mobile;
        
    @ApiModelProperty(value = "验证码")
    @NotNull(message = "验证码不能为空!")
    private String validationCode;
    
    @ApiModelProperty(value = "详细地址")
    @NotNull(message = "收货地址不能为空!")
    @Size(max = 100, message = "地址长度不超过100个字!")
    private String address;
    
        
}
