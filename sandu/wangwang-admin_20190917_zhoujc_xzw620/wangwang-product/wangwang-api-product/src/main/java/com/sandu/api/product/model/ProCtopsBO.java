package com.sandu.api.product.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Sandu
 * @ClassName ProCtopsBO
 * @date 2019/3/4-14:19$
 */
@Data
public class ProCtopsBO implements Serializable {

	private Integer idForWall;
	@ApiModelProperty("0、啥都不是;1、靠墙;2、外")
	private Integer dataTypeForWall;
	@ApiModelProperty("材质ID")
	private Integer textureIdForWall;
	@ApiModelProperty("靠墙截面数据")
	private String crossSectionDataForWall;
	@ApiModelProperty(hidden = true)
	private String textureNameForWall;

	private String texturePicPathForWall;

	private Integer idForOut;
	@ApiModelProperty("0、啥都不是;1、靠墙;2、外")
	private Integer dataTypeForOut;
	@ApiModelProperty("材质ID")
	private Integer textureIdForOut;
	@ApiModelProperty("靠外截面数据")
	private String crossSectionDataForOut;

	@ApiModelProperty(hidden = true)
	private String textureNameForOut;

	private String texturePicPathForOut;
}