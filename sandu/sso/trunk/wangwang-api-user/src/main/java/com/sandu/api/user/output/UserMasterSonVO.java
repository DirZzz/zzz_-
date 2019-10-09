package com.sandu.api.user.output;

import lombok.Data;
import java.io.Serializable;

/**
 * 列表
 * @author: chenqiang
 * @create: 2019-07-02 16:18
 */
@Data
public class UserMasterSonVO implements Serializable {
    private static final long serialVersionUID = 7704094062857311670L;

    /**
     * 主键id
     **/
    private Long id;
    /**
     * 用户名
     **/
    private String userName;
    /**
     * 昵称
     **/
    private String nickName;

    /**
     * 电话
     **/
    private String mobile;

    /**
     * 用户类型:(1:内部用户,2:厂商,3:经销商,4:设计公司,5:设计师,6:装修公司,7:学校(培训机构),8:普通用户)
     **/
    private Integer userType;

    private String  userTypeName;

    /**
     * 主子账号类型（0：普通账号、1：子账号、2：主账号）
     */
    private Integer masterSonType;

}
