package com.sandu.api.product.model.bo;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 3:55 2018/7/7 0007
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author weisheng
 * @Title: 天花截面数据
 * @Package
 * @Description:
 * @date 2018/7/7 0007PM 3:55
 */

@Data
public class CeilingCrossSectionDataDTO implements Serializable {


	@ApiModelProperty("适用面积")
	private String applyArea;
	private String applyAreaValue;
	@ApiModelProperty("截面数据")
	private String ceilingInfo;
	@ApiModelProperty("灯带数据")
	private String lightInfo;


}
