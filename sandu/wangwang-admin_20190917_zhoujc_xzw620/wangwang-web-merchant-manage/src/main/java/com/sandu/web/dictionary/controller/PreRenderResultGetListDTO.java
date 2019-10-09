package com.sandu.web.dictionary.controller;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PreRenderResultGetListDTO implements Serializable {

	private static final long serialVersionUID = 1003413010704271652L;

	/**
	 * 全屋方案id(full_house_design_plan.id)
	 */
	private Long fullHouseId;


	private Integer fullHouseRecommendPlanId;
	/**
	 * 渲染效果图资源uuid(design_plan_store_release.uuid/design_pre_render_result.uuid)
	 */
	private String uuid;

	/**
	 * 户型id(base_house.id/design_pre_render_result.house_id)
	 */
	private Long houseId;

	/**
	 * 主任务id(auto_render_task.id/design_pre_render_result.main_task_id)
	 */
	private Long mainTaskId;

	/**
	 * 方案风格(全屋推荐方案的方案风格)
	 */
	private String planStyle;

	/**
	 * 方案名称(全屋推荐方案的名称)
	 */
	private String planName;

	/**
	 * 封面图path
	 */
	private String coverImagePath;

	/**
	 * 装修总价
	 */
	/*private String totalPrice;*/

	/**
	 * 小区名称
	 */
	private String livingName;

	/**
	 * 几室几厅信息
	 */
	private String houseInfo;

	/**
	 * 面积信息
	 */
	private String areaInfo;

	/**
	 * 装修进度
	 */
	private Double decorationRate;

	private Date gmtCreate;

	private Object fullPlanCost;


}
