package com.sandu.service.basesupplydemand.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.basesupplydemand.input.UserReviewsQuery;
import com.sandu.api.basesupplydemand.model.NodeDetails;
import com.sandu.api.basesupplydemand.model.NodeInfoConstance;
import com.sandu.api.basesupplydemand.model.UserReviews;
import com.sandu.api.basesupplydemand.service.NodeInfoService;
import com.sandu.api.basesupplydemand.service.UserReviewsService;
import com.sandu.service.basesupplydemand.dao.UserReviewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sandu
 * @ClassName UserReviewServiceImpl
 * @date 2018/11/6
 */
@Service
public class UserReviewServiceImpl implements UserReviewsService {

	@Autowired
	private UserReviewsMapper userReviewsMapper;

	@Autowired
	private NodeInfoService nodeInfoService;

	@Override
	public List<UserReviews> queryByOptions(UserReviews userReviews) {
		return userReviewsMapper.queryByOptions(userReviews);
	}

	@Override
	public int updateByOptions(UserReviews build) {
		return userReviewsMapper.updateByPrimaryKeySelective(build);
	}

	@Override
	public PageInfo<UserReviews> queryBiz(UserReviewsQuery query) {
		PageHelper.startPage(query.getPage(), query.getLimit());
		PageInfo<UserReviews> pageInfo = new PageInfo<>(userReviewsMapper.queryBiz(query));

		List<UserReviews> userReviews = pageInfo.getList();
		//处理点赞数
		Map<Integer, Integer> id2Count = nodeInfoService.listNodeDetails(
				userReviews.stream().map(UserReviews::getId).collect(Collectors.toList()),
				NodeInfoConstance.NODE_TYPE_USER_REVIEWS,
				Collections.singletonList(NodeInfoConstance.DETAIL_TYPE_LIKE)
		).stream().collect(Collectors.toMap(NodeDetails::getContentId, NodeDetails::getValue));

		for (UserReviews it : userReviews) {
			it.setLikeCount(id2Count.get(it.getId()));
		}

		return pageInfo;
	}

	@Override
	public UserReviews getHeadReviews(Integer id) {
		return userReviewsMapper.getHeadReviews(id);
	}

	@Override
	public List<UserReviews> findBySupplyDemandIdList(Integer id) {
		return userReviewsMapper.findBySupplyDemandIdList(id);
	}
}
