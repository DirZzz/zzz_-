package com.sandu.api.statisticsPlan.model;

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
public class PlanStatistics extends BaseQuery implements Serializable{

    private static final long serialVersionUID = 8405367958403410866L;

    /** id **/
    private int id;
    /** 开始时间 **/
    private String startTime="";
    /**结束时间  **/
    private String endTime;
    /** 方案新增量 **/
    private int newPlanCount;
    /** 方案使用次数(每天) **/
    private int usePlanCount;
    /** PC端方案使用次数 **/
    private Integer pcUsePlanCount;
    /** 移动端方案使用次数 **/
    private Integer mobileUsePlanCount;
    /** 方案类型(1:普通;2:一键) **/
    private Integer planType;
    /** 方案风格 **/
    private Integer designStyleId;
    /** 空间类型 **/
    private Integer spaceCommonType;
    /** 企业id **/
    private Integer companyId;
    /** 方案来源 **/
    private String planSource;
    /** 创建者 **/
    private String creator;
    /** 修改时间 **/
    private String gmtCreate;
    /** 修改人 **/
    private String modifier;
    /** 修改时间 **/
    private String gmtModified;
    /** 逻辑删除字段(0:正常;1:已删除) **/
    private Integer isDeleted;
    /** 备注 **/
    private String remark;
    //新增方案总数
    private int newPlanTotal;
    //使用方案总数
    private int usePlanTotal;
    //筛选条件 时间
    private Integer time;
    private int shelfPlanCount;
    private int planCount;

    private String planGroupStyleId;
    private List<Integer> listGroupStyleId;
    //类型：1新增 0使用
    private Integer type;

    private Integer day;

}
