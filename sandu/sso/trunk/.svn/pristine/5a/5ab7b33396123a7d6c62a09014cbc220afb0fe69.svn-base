package com.sandu.api.user.input;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Api(value = "用户子账号搜索列表")
public class UserMasterSonSearch implements Serializable {

    @ApiModelProperty(value = "主账号id")
    private Integer masterUserId;

    @ApiModelProperty(value = "登录名")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "昵称")
    private String userName;

    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    @ApiModelProperty(value = "0：普通账号、1：子账号、2：主账号")
    private Integer masterSonType;

    @ApiModelProperty(value = "当前页码")
    @NotNull(message = "当前页不能为空")
    private Integer page;

    @ApiModelProperty(value = "每页显示条数")
    @NotNull(message = "每页显示条数不能为空")
    private Integer limit;

    private Integer companyId;
}
