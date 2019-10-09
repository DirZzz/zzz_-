package com.sandu.api.statistics.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OtherResourceStatistics implements Serializable {
    private static final long serialVersionUID = 2384082512364448500L;

    /**
     *
     */
    private Integer id;

    /**
     * 方案渲染资源
     */
    private String planRender;

    /**
     * 方案配置资源
     */
    private String planConfig;

    /**
     * 互动区
     */
    private String interactiveZone;

    /**
     * 时间
     */
    private Integer date;

    /**
     * 天
     */
    private Integer day;

    /**
     * 周
     */
    private Integer week;

    /**
     * 月
     */
    private Integer month;

    /**
     * 年
     */
    private Integer year;

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