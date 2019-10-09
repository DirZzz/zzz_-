package com.sandu.api.solution.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Sandu
 * @ClassName ManagerSXWPlanUpdate
 * @date 2019/2/23-15:58$
 */
@Data
public class ManagerSXWPlanUpdate implements Serializable {

	private Integer planId;

	private String planName;

	@ApiModelProperty("是否展示到随选网首页，true：是，false：否 ")
	private Boolean showInSXWIndexFlag;

	@ApiModelProperty("是否展示到随选网，true：是，false：否")
	private Boolean showInSXWFlag;

	@ApiModelProperty("随选网首页排序")
	private Integer sortInSXWIndex;

	@ApiModelProperty("店铺id")
	private Integer shopIn720Page;
}
