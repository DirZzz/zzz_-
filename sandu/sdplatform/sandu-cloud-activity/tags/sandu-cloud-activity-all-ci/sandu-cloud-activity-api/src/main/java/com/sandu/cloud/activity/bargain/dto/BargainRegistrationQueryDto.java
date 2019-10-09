package com.sandu.cloud.activity.bargain.dto;
import lombok.Data;

@Data
public class BargainRegistrationQueryDto {

	private Integer pageNum = 1;
	private Integer pageSize = 20;
	private String openId;
	private String nickname;
	private Integer status;
	private Integer decorateStatus;
	private String actId;

}
