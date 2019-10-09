package com.sandu.web.basesupplydemand.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.sandu.api.basesupplydemand.input.UserReviewsQuery;
import com.sandu.api.basesupplydemand.input.UserReviewsUpdate;
import com.sandu.api.basesupplydemand.model.NodeInfoConstance;
import com.sandu.api.basesupplydemand.model.UserReviews;
import com.sandu.api.basesupplydemand.service.NodeInfoService;
import com.sandu.api.basesupplydemand.service.UserReviewsService;
import com.sandu.api.basesupplydemand.service.biz.BaseSupplydemandBizService;
import com.sandu.api.goods.model.ResPic;
import com.sandu.api.storage.service.ResPicService;
import com.sandu.common.BaseController;
import com.sandu.common.ReturnData;
import com.sandu.constant.Punctuation;
import com.sandu.constant.ResponseEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sandu.api.basesupplydemand.model.NodeInfoConstance.DETAIL_TYPE_LIKE;
import static com.sandu.api.basesupplydemand.model.NodeInfoConstance.DETAIL_TYPE_VIRTUAL_LIKE;

/**
 * @author Sandu
 * @ClassName UserReviewController
 * @date 2018/11/6
 */
@Slf4j
@RestController
@RequestMapping("reviews")
public class UserReviewController extends BaseController {


	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyDDmmSSs");

	@Autowired
	private UserReviewsService userReviewsService;

	@Autowired
	private BaseSupplydemandBizService baseSupplydemandBizService;

	@Autowired
	private ResPicService resPicService;

	@Autowired
	private NodeInfoService nodeInfoService;


	@ApiOperation("(批量)删除评论")
	@DeleteMapping()
	public ReturnData delete(String ids) {

		ReturnData result = ReturnData.builder();
		try {
			userReviewsService.updateByOptions(UserReviews.builder()
					.updateIds(
							Stream.of(Strings.nullToEmpty(ids).split(Punctuation.COMMA))
									.map(Integer::valueOf)
									.collect(Collectors.toList())
					)
					.isDeleted(1)
					.build());
			result.success(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.message("系统错误").code(ResponseEnum.ERROR).success(false);
		}
		return result;
	}


	@ApiOperation("更新评论")
	@PutMapping()
	public ReturnData update(@RequestBody UserReviewsUpdate update) {
		ReturnData result = ReturnData.builder();
		try {
			userReviewsService.updateByOptions(UserReviews.builder()
					.id(update.getReviewsId())
					.reviewsMsg(update.getContent())
					.picIds(update.getPicIds())
					.planId(update.getPlanId())
					.houseId(update.getHouseId())
					.build());

			// 更新点赞数
			nodeInfoService.updateNodeDetailsValue(
					update.getReviewsId(),
					NodeInfoConstance.NODE_TYPE_USER_REVIEWS,
					DETAIL_TYPE_VIRTUAL_LIKE,
					update.getVirtualLikeCount() == null ? 0 : update.getVirtualLikeCount()
			);


			result.success(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.message("系统错误").code(ResponseEnum.ERROR).success(false);
		}
		return result;
	}


	@ApiOperation("评论详情")
	@GetMapping("/{id}")
	public ReturnData details(@PathVariable("id") Integer id) {
		ReturnData result = ReturnData.builder();
		try {
			List<UserReviews> userReviews = userReviewsService.queryByOptions(UserReviews.builder()
					.id(id)
					.isDeleted(0)
					.build());
			if (userReviews.isEmpty()) {
				result.message("没有数据了...").success(true);
				return result;
			}
			UserReviews reviews = userReviews.get(0);
			String planName = baseSupplydemandBizService.fetchPlanNameByIdAndType(reviews.getPlanId(), reviews.getPlanType());

			//方案名称
			reviews.setPlanName(planName);

			//户型名称
			reviews.setHouseName(baseSupplydemandBizService.fetchHouseNameDetails(reviews.getHouseId()));

			//处理点赞数
			nodeInfoService.listNodeDetails(
					userReviews.stream().map(UserReviews::getId).collect(Collectors.toList()),
					NodeInfoConstance.NODE_TYPE_USER_REVIEWS,
					Arrays.asList(DETAIL_TYPE_VIRTUAL_LIKE, DETAIL_TYPE_LIKE)
			)
					.forEach(node -> {
						for (UserReviews it : userReviews) {
							if (node.getContentId().equals(it.getId())) {
								if (node.getDetailsType().equals(DETAIL_TYPE_VIRTUAL_LIKE)) {
									it.setLikeVirtualCount(node.getValue());
								}
								if (node.getDetailsType().equals(DETAIL_TYPE_LIKE)) {
									it.setLikeCount(node.getValue());
								}
							}
						}
					});




			//处理图片
			Map<Long, String> picMap = Stream.of(Strings.nullToEmpty(reviews.getPicIds()).split(Punctuation.COMMA))
					.filter(StringUtils::isNotEmpty)
					.map(Long::valueOf)
					.collect(Collectors.collectingAndThen(Collectors.toList(), baseSupplydemandBizService::getResPicByIds))
					.stream()
					.collect(Collectors.toMap(ResPic::getId, ResPic::getPicPath));

			reviews.setPicMap(picMap);


			result.success(true).data(userReviews.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			result.message("系统错误").code(ResponseEnum.ERROR).success(false);
		}

		return result;
	}


	@ApiOperation("根据供求信息查询评论列表")
	@GetMapping("/demand")
	public ReturnData<UserReviews> listUserReviews(UserReviewsQuery query) {
		ReturnData result = ReturnData.builder();
		PageInfo<UserReviews> userReviews = null;
		try {
			userReviews = userReviewsService.queryBiz(query);


			result.success(true).total(userReviews.getTotal()).list(userReviews.getList());
		} catch (Exception e) {
			e.printStackTrace();
			result.message("系统错误").code(ResponseEnum.ERROR).success(false);
		}
		return result;
	}

	@ApiOperation("置顶")
	@GetMapping("/top/{id}/{action}")
	public ReturnData putTop(@PathVariable("id") Integer id, @PathVariable("action") Integer action) {
		ReturnData result = ReturnData.builder();

		UserReviews userReviews = userReviewsService.getHeadReviews(id);

		int topNum = 0;
		if (userReviews != null) {
			topNum = userReviews.getIsTop() + 1;
		}
		try {
			userReviewsService.updateByOptions(
					UserReviews.builder()
							.id(id)
							.isTop(action > 0 ? topNum : 0)
							.build()
			);
			result.success(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.message("系统错误").code(ResponseEnum.ERROR).success(false);
		}
		return result;

	}


	public static void main(String[] args) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");

	}

}
