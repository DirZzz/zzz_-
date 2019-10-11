package com.sandu.analysis.biz.tob.plan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanAnalysisResultDto implements Serializable {

    private static final long serialVersionUID = 5704777121927377928L;

    /**
     * bigdata_plan_analysis_result_day.id
     */
    private Long id;

    /**
     * 开始时间<p>
     * bigdata_plan_analysis_result_day.start_time
     */
    private Date startTime;

    /**
     * 结束时间<p>
     * bigdata_plan_analysis_result_day.end_time
     */
    private Date endTime ;

    /**
     * 新增方案数<p>
     * bigdata_plan_analysis_result_day.new_plan_count
     */
    private Integer newPlanCount;

    /**
     * 方案使用数<p>
     * bigdata_plan_analysis_result_day.plan_use_count
     */
    private Integer usePlanCount;

    /**
     * 方案类型 0:普通设计方案;1:一件装修设计方案<p>
     * bigdata_plan_analysis_result_day.recommended_type
     */
    private Integer planType;

    /**
     * 方案风格<p>
     * bigdata_plan_analysis_result_day.design_recommended_style_id
     */
    private Integer designStyleId;

    /**
     * 空间类型<p>
     * bigdata_plan_analysis_result_day.space_function_id
     */
    private Integer spaceCommonType;

    /**
     * 所属企业id<p>
     * bigdata_plan_analysis_result_day.company_id
     */
    private Integer companyId;

    /**
     * 方案来源<p>
     * bigdata_plan_analysis_result_day.plan_source
     */
    private String planSource;

    /**
     * 是否删除<p>
     * bigdata_plan_analysis_result_day.is_deleted
     */
    private Integer isDeleted;

    /**
     *  创建者
     */
    private String creator;

    /**
     *  修改时间
     */
    private Date gmtCreate;

    /**
     *  修改人
     */
    private String modifier;

    /**
     *  修改时间
     */
    private Date gmtModified;

    /**
     *  备注
     */
    private String remark;
}
