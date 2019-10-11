package com.sandu.analysis.biz.tob.plan.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecommendedPlanDto implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 方案类型（推荐方案、普通方案）
     */
    private Integer recommendedType;
    /**
     * 方案风格
     */
    private Integer styleId;
    /**
     * 空间类型
     */
    private Integer spaceType;
    /**
     * 企业Id
     */
    private Integer companyId;
    /**
     * 方案来源
     */
    private String planSource;
    /**
     * 新增方案数
     */
    private Integer newPlanCount;

}
