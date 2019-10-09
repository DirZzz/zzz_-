package com.sandu.api.solution.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 方案推荐
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanRecommendedQuery implements Serializable{

	private static final long serialVersionUID = 1L;

	/** id **/
	private Integer id;
	/** 方案推荐类型  1分享  2一键装修 **/
	private Integer recommendedType;
	/**  创建时间  **/
	private Date gmtCreate;
	/**  修改时间  **/
	private Date gmtModified;
	/**  是否删除  **/
	private Integer isDeleted;
	/**  方案来源  **/
	private String planSource;
	/** 方案推荐风格ID **/
	private Integer designRecommendedStyleId;
	/** 设计方案发布时间 **/
	private Date putawayTime;
	/** 空间类型 **/
	private Integer spaceFunctionId;
	/** 企业id **/
	private Integer companyId;
	//筛选条件 时间
	private Integer time;

	private String planGroupStyleId;
	private List<Integer> listGroupStyleId;

}
