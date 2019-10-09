package com.sandu.service.basesupplydemand.dao;

import com.sandu.api.basesupplydemand.input.UserReviewsQuery;
import com.sandu.api.basesupplydemand.model.UserReviews;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserReviewsMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(UserReviews record);

	int insertSelective(UserReviews record);

	UserReviews selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(UserReviews record);

	int updateByPrimaryKey(UserReviews record);

	List<UserReviews> queryByOptions(UserReviews userReviews);

	List<UserReviews> queryBiz(UserReviewsQuery query);

	UserReviews getHeadReviews(@Param("id") Integer id);

    List<UserReviews> selectListBySupplyDemandId(@Param("businessId")Integer id);

    List<UserReviews> findBySupplyDemandIdList(Integer id);
}