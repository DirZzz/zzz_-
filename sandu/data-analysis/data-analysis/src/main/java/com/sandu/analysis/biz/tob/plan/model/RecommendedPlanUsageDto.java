package com.sandu.analysis.biz.tob.plan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-14 15:14
 * Desc:b端单个方案统计结果表(每天)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendedPlanUsageDto implements Serializable{

    private static final long serialVersionUID = 7370343050055770821L;

    private long id;
    /** 开始时间 **/
    private String startTime;
    /** 结束时间 **/
    private String endTime;
    /** 推荐方案id **/
    private Integer planId;
    /** 推荐方案名称 **/
    private String planName;
    /** 推荐方案编码 **/
    private String planCode;
    /** 方案类型(1:普通;2:一键) **/
    private Integer planType;
    /** 方案来源 **/
    private String planSource;
    /** 方案风格 **/
    private Integer designStyleId;
    /** 企业id **/
    private Integer companyId;
    /** 空间ID **/
    private Integer spaceCommonId;
    /** 空间类型 **/
    private Integer spaceCommonType;
    /** 方案PC端使用总次数 **/
    private Integer planUsageAmountPc;
    /** 方案移动B端使用总次数 **/
    private Integer planUsageAmountMobile2b;
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

}
