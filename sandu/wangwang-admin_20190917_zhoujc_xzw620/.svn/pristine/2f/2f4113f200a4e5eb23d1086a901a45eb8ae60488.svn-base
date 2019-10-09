package com.sandu.api.basesupplydemand.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述:user_reviews表的实体类
 *
 * @author: Sandu
 * @创建时间: 2019-02-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReviews implements Serializable {
	/**
	 *
	 */
	private Integer id;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * 业务ID
	 */
	private Integer businessId;

	/**
	 * 评论详情
	 */
	private String reviewsMsg;

	/**
	 * 创建者
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 是否删除（0:否，1:是）
	 */
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 评论的目标用户ID
	 */
	private Integer fid;

	/**
	 * 评论图片
	 */
	private String picIds;

	/**
	 * 评论方案ID
	 */
	private Integer planId;

	/**
	 * 评论户型ID
	 */
	private Integer houseId;

	/**
	 * 需求信息发布用户ID
	 */
	private Integer supplyDemandPublisherId;

	/**
	 * 方案类型,1:单空间推荐方案.2:全屋单推荐方案,3:单空间我的设计方案,4:全屋我的设计方案
	 */
	private Integer planType;

	/**
	 * 0:未读,1:已读
	 */
	private Integer isRead;

	/**
	 * 置顶排序
	 */
	private Integer isTop;


	/******************** biz field	********************/
	private List<Integer> updateIds;

	private String planName;

	private String houseName;

	private Map picMap;
	/******************** biz field	********************/


	private Integer likeCount;
	private Integer likeVirtualCount;
}

