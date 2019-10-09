package com.sandu.api.solution.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: YuXingchi
 * @Description:
 * @Date: Created in 17:26 2018/5/17
 */
@Data
public class DesignRenderRoam implements Serializable{

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 缩略图
     */
    private Integer screenShotId;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 配置文件ID
     */
    private Integer roamConfig;

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
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 字符备用1
     */
    private String att1;

    /**
     * 字符备用2
     */
    private String att2;

    /**
     * 整数备用1
     */
    private Integer numa1;

    /**
     * 整数备用2
     */
    private Integer numa2;

    /**
     * 备注
     */
    private String remark;
}
