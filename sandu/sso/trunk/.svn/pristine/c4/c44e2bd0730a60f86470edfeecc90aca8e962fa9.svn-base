package com.sandu.api.user.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 用户设置主子类型入参类
 * @author: chenqiang
 * @create: 2019-07-02 14:42
 */
@Data
public class UserMSTypeSet implements Serializable{
    private static final long serialVersionUID = 6149722302706249673L;

    @ApiModelProperty(value = "用户id集合")
    @NotEmpty(message = "用户id不能为空")
    private List<Integer> userIdList;

    @ApiModelProperty(value = "操作类型（普通：ordinary、子：son、主：master、添加子账号：addSon、移除子账号：removeSon）")
    @NotNull(message = "操作类型不能为空")
    private String msType;

    @ApiModelProperty(value = "主账号id")
    private Integer masterUserId;

    public enum msTypeEnum{
        ordinary,son,master,addSon,removeSon
    }
}
