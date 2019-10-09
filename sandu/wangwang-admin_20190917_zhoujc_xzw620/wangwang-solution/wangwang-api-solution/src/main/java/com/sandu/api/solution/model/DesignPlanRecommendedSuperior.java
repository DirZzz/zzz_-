package com.sandu.api.solution.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:design_plan_recommended_superior表的实体类
 *
 * @author: Sandu
 * @创建时间: 2019-02-25
 */
@Data
public class DesignPlanRecommendedSuperior {
	/**
	 *
	 */
	private Long id;

	/**
	 * 方案编码
	 */
	private String planCode;

	/**
	 * 方案名称
	 */
	private String planName;

	/**
	 * 方案ID
	 */
	private Integer designPlanRecommendedId;

	/**
	 * 排序
	 */
	private Integer ordering;

	/**
	 * 空间类型
	 */
	private Integer spaceType;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 创建者
	 */
	private String creator;

	/**
	 *
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
	 * 是否删除
	 */
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 方案类型:0.单空间,1.全屋
	 */
	private Integer planType;

}