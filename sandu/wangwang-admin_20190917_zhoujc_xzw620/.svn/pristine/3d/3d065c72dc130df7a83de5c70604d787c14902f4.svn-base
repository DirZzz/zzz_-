package com.sandu.api.basesupplydemand.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sandu
 * @ClassName UserReviewsUpdate
 * @date 2019/2/2-14:24$
 */
@Data
public class UserReviewsUpdate implements Serializable {
	@ApiModelProperty("评论ID")
	@NotNull
	@Min(1)
	private Integer reviewsId;

	@ApiModelProperty("方案ID")
	@Min(0)
	private Integer planId;

	@ApiModelProperty("户型ID")
	@Min(0)
	private Integer houseId;

	@ApiModelProperty("评论内容")
	private String content;

	@ApiModelProperty("评论图片")
	private String picIds;

	@ApiModelProperty("点赞数")
	@NotNull
	@Min(0)
	private Integer likeCount;

	@ApiModelProperty("点赞数")
	@NotNull
	@Min(0)
	private Integer virtualLikeCount;

}
