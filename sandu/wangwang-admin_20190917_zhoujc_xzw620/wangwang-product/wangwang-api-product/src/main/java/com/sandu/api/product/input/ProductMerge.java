package com.sandu.api.product.input;

import com.sandu.api.user.model.LoginUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Sandu
 * @ClassName ProductMerge
 * @date 2019/7/11-14:11$
 */

@Data
public class ProductMerge implements Serializable {

	@ApiModelProperty("产品名称 ")
	private String productName;

	@ApiModelProperty(value = "产品系列")
//	@Min(value = 1, message = "请输入正确的产品系列ID")
	private Integer seriesId;

	@ApiModelProperty(value = "产品缩略图ID")
//	@NotNull(message = "请选择产品缩略图")
	private String defaultPicId;

	@ApiModelProperty(value = "产品所有图片ID,以逗号分隔")
//	@Pattern(regexp = "^[1-9]\\d{0,11}(,[1-9]\\d{0,11})*$", message = "请输入有效的图片集合")
	private String picIds;

	@ApiModelProperty(value = "产品型号", required = true)
//	@NotNull(message = "产品型号不能为空")
	private String modelNumber = "";

	@ApiModelProperty(value = "产品款式")
//	@Min(value = 1, message = "请输入正确的产品款式ID")
	private Integer modelingId;

	@ApiModelProperty("所有产品ID")
	@NotEmpty
	private List<Integer> productIds;

	@ApiModelProperty("之前的主产品ID")
	private Integer preMainProductId;

	@ApiModelProperty("当前主产品ID")
	@NotNull
	private Integer curMainProductId;

	@ApiModelProperty(hidden = true)
	private Integer productBatchType;

	@ApiModelProperty(hidden = true)
	private LoginUser loginUser;
}
