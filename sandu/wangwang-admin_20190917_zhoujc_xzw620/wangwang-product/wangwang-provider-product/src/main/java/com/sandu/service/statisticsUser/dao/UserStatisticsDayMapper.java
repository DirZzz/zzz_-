package com.sandu.service.statisticsUser.dao;

import com.sandu.api.statisticsUser.model.UserStatistics;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatisticsDayMapper {
    List<UserStatistics> selectDayUserList(UserStatistics query);

    UserStatistics selectDayUserTotal(UserStatistics query);

    UserStatistics selectAccountAndNonactivatedTotal(UserStatistics query);

    List<UserStatistics> selectDayUserChart(UserStatistics query);

}
