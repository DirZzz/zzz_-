package com.sandu.api.statisticsHouse.model;

import com.sandu.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseStatistics extends BaseQuery implements Serializable{

    private static final long serialVersionUID = 8405367958403410866L;

    private int id;
    /** 开始时间 **/
    private String startTime="";
    /** 结束时间 **/
    private String endTime;
    /** 户型新增数量 **/
    private int newHouseCount;
    /** 使用户型数量 **/
    private int useHouseCount;
    /** 省编码 **/
    private String provinceCode;
    /** 省名称 **/
    private String provinceName;
    /** 市编码 **/
    private String cityCode;
    /** 市名称 **/
    private String cityName;
    /** 创建者 **/
    private String creator;
    /** 创建时间 **/
    private String gmtCreate;
    /** 修改人 **/
    private String modifier;
    /** 修改时间 **/
    private String gmtModified;
    /** 逻辑删除字段(0:正常 1:已删除) **/
    private Integer isDeleted;
    /** 备注 **/
    private String remark;
    //新增户型总数
    private int newHouseTotal;
    //使用户型总数
    private int useHouseTotal;
    //筛选条件 时间
    private Integer time;
    //户型总数
    private int houseCount;
    //类型：1新增 0使用
    private Integer type;

    private Integer day;

}
