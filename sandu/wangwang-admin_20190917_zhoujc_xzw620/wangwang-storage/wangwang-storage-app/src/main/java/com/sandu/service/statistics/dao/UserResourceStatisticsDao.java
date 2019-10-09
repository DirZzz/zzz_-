package com.sandu.service.statistics.dao;

import com.sandu.api.statistics.model.UserResourceStatistics;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserResourceStatisticsDao {
    /**
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int insertSelective(UserResourceStatistics record);

    /**
     *
     * @mbggenerated
     */
    UserResourceStatistics selectByPrimaryKey(Integer id);


    List<UserResourceStatistics> selectList(UserResourceStatistics record);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserResourceStatistics record);

}