package com.sandu.service.statisticsUser.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsUser.model.UserStatistics;
import com.sandu.api.statisticsUser.service.UserStatisticsDayService;
import com.sandu.api.statisticsUser.service.UserStatisticsHourService;
import com.sandu.service.statisticsUser.dao.UserStatisticsDayMapper;
import com.sandu.service.statisticsUser.dao.UserStatisticsHourMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userStatisticsHourService")
public class UserStatisticsHourServiceImpl implements UserStatisticsHourService {
    @Autowired
    private UserStatisticsHourMapper userStatisticsHourMapper;

    @Override
    public PageInfo<UserStatistics> selectList(UserStatistics userStatistics) {
        PageHelper.startPage(userStatistics.getPage(), userStatistics.getLimit());
        List<UserStatistics> userStatisticsList = userStatisticsHourMapper.selectHourUserList(userStatistics);
        return new PageInfo<>(userStatisticsList);
    }

    @Override
    public UserStatistics selectUserTotal(UserStatistics userStatistics) {
        return userStatisticsHourMapper.selectHourUserTotal(userStatistics);
    }

    @Override
    public List<UserStatistics> selectHourUserChart(UserStatistics userStatistics) {
        return userStatisticsHourMapper.selectHourUserChart(userStatistics);
    }

}
