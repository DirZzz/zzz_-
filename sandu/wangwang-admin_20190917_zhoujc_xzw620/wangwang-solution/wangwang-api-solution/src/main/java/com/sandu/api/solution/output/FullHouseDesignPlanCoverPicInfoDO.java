package com.sandu.api.solution.output;

import java.io.Serializable;

import lombok.Data;

@Data
public class FullHouseDesignPlanCoverPicInfoDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * res_render_pic.id
	 */
	private Long picId;
	
	/**
	 * res_render_pic.pic_path
	 */
	private String picPath;
	
	/**
	 * full_house_design_plan_detail.space_type
	 */
	private Integer spaceTypeValue;
	
}
