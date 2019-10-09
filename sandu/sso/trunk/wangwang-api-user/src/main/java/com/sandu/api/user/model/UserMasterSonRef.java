package com.sandu.api.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserMasterSonRef implements Serializable {
    private static final long serialVersionUID = 2211185107820112112L;

    /**
     *
     */
    private Integer id;

    /**
     * 主账号id
     */
    private Integer masterId;

    /**
     * 子账号id
     */
    private Integer sonId;

    /**
     * 企业id
     */
    private Integer companyId;

    /**
     * 系统编码
     */
    private String sysCode;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 是否删除(0:未删除、1:删除)
     */
    private Integer isDeleted;

    /**
     * 备注
     */
    private String remark;

}