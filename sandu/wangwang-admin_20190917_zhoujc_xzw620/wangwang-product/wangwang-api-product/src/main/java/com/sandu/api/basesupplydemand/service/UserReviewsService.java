package com.sandu.api.basesupplydemand.service;

import com.github.pagehelper.PageInfo;
import com.sandu.api.basesupplydemand.input.UserReviewsQuery;
import com.sandu.api.basesupplydemand.model.UserReviews;

import java.util.List;

public interface

UserReviewsService {
	List<UserReviews> queryByOptions(UserReviews userReviews);

	int updateByOptions(UserReviews build);

	PageInfo<UserReviews> queryBiz(UserReviewsQuery query);

	UserReviews getHeadReviews(Integer id);

	List<UserReviews> findBySupplyDemandIdList(Integer id);
}
