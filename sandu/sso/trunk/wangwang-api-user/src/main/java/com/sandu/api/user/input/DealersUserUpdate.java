package com.sandu.api.user.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangHaiLin
 * @date 2018/6/5  16:52
 */
@Data
public class DealersUserUpdate implements Serializable {

    @ApiModelProperty(value = "Id")
    private Long userId;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "电话")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String eMail;

    @ApiModelProperty(value = "头像Id")
    private Long headPicId;

    @ApiModelProperty(value = "账号类型")
    private Integer userType;

    @ApiModelProperty(value = "经销商企业Id")
    private Long companyId;

    @ApiModelProperty(value = "激活时间(首次登录时间)")
    private Date firstLoginTime;

    @ApiModelProperty(value = "省地区编码")
    private String provinceCode;

    @ApiModelProperty(value = "市地区编码")
    private String cityCode;

    @ApiModelProperty(value = "区地区编码")
    private String areaCode;

    @ApiModelProperty(value = "街道地区编码")
    private String streetCode;

    @ApiModelProperty(value = "详细地址")
    private String address;

}