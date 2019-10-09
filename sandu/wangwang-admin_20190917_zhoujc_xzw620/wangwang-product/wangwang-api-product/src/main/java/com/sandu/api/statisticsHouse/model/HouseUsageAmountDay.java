package com.sandu.api.statisticsHouse.model;

import com.sandu.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseUsageAmountDay extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 8405367958403410866L;

    private int id;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     *  结束时间
     */
    private String endTime;
    /**
     *  户型Id
     */
    private Integer houseId;
    /**
     *  户型名称
     */
    private String houseName="";
    /**
     *  户型编码
     */
    private String houseCode="";
    /**
     *  小区名称
     */
    private String livingName="";
    /**
     *  户型使用数量
     */
    private int houseUsageAmount;
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
    //使用户型总数
    private int useHouseTotal;
    //筛选条件 时间
    private Integer time;

}
