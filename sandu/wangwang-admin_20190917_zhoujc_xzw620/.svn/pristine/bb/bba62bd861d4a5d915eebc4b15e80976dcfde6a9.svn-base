package com.sandu.api.statistics.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ResourceStatisticsDeviceLog implements Serializable {
    private static final long serialVersionUID = 4118555174649897521L;

    public enum  logTypeEnum {
        user,
        company,
        common,
        other
    }
    public enum  deviceTypeEnum {
        force,
        time
    }

    /**
     *
     */
    private Integer id;

    /**
     * 日志类型（user:用户、company:企业、common:共有、other:其他）
     */
    private String logType;

    /**
     * 计划类型（force:强制更新、time:定时执行）
     */
    private String deviceType;

    /**
     * 状态（0:未执行、1:失败、2:成功）
     */
    private Integer status;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 时间
     */
    private Integer date;

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

    private List<Integer> statusList;
}



