package com.sandu.analysis.biz.tob.house.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 按天统计户型数据实体
 * @author xiaoxc
 * @data 2019/6/14 0014.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseStatisticsDayDto implements Serializable {

    private static final long serialVersionUID = -3657847416448216197L;

    private Integer id;
    /**
     * 开始时间
     */
    private String startTime;

    /**
     *  结束时间
     */
    private String endTime;

    /**
     *  户型新增数量
     */
    private Integer newHouseCount;

    /**
     *  使用户型数量
     */
    private Integer useHouseCount;

    /**
     *  省编码
     */
    private String provinceCode;

    /**
     *  省名称
     */
    private String provinceName;

    /**
     *  市编码
     */
    private String cityCode;

    /**
     *  市名称
     */
    private String cityName;

    /**
     *  创建者
     */
    private String creator;

    /**
     *  创建时间
     */
    private String gmtCreate;

    /**
     *  修改人
     */
    private String modifier;

    /**
     *  修改时间
     */
    private String gmtModified;

    /**
     *  逻辑删除字段(0:正常 1:已删除)
     */
    private Integer isDeleted;

    /**
     *  备注
     */
    private String remark;
}
