package com.sandu.api.statisticsUser.service;

import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsCompany.model.CompanyStatisticsDay;
import com.sandu.api.statisticsUser.model.UserStatistics;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserStatisticsDayService {

    PageInfo<UserStatistics> selectList(UserStatistics query);

    UserStatistics selectUserTotal(UserStatistics query);

    UserStatistics selectAccountAndNonactivatedTotal(UserStatistics userStatistics);

    List<UserStatistics> selectDayUserChart(UserStatistics query);

}
