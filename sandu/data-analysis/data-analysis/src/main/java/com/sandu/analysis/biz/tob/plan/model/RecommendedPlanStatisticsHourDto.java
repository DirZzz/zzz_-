package com.sandu.analysis.biz.tob.plan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-15 19:11
 * Desc:b端方案数据统计结果表(每小时)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendedPlanStatisticsHourDto implements Serializable{

    private static final long serialVersionUID = 8405367958403410866L;

    /** id **/
    private Long id;
    /** 开始时间 **/
    private String startTime;
    /**结束时间  **/
    private String endTime;
    /** 方案新增量 **/
    private Integer newPlanCount;
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
    /** 每小时(eg:2019-06-05 01、2019-06-05 02) */
    private String perHour;

}
