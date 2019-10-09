package com.sandu.service.statisticsUser.dao;

import com.sandu.api.statisticsUser.model.UserStatistics;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatisticsHourMapper {
    List<UserStatistics> selectHourUserList(UserStatistics query);

    UserStatistics selectHourUserTotal(UserStatistics query);

    List<UserStatistics> selectHourUserChart(UserStatistics query);
}
