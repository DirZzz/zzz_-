package com.sandu.service.statisticsUser.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsPlan.model.PlanStatistics;
import com.sandu.api.statisticsPlan.service.PlanStatisticsDayService;
import com.sandu.api.statisticsUser.model.UserStatistics;
import com.sandu.api.statisticsUser.service.UserStatisticsDayService;
import com.sandu.service.statisticsPlan.dao.PlanStatisticsDayMapper;
import com.sandu.service.statisticsUser.dao.UserStatisticsDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userStatisticsDayService")
public class UserStatisticsDayServiceImpl implements UserStatisticsDayService {
    @Autowired
    private UserStatisticsDayMapper userStatisticsDayMapper;

    @Override
    public PageInfo<UserStatistics> selectList(UserStatistics userStatistics) {
        PageHelper.startPage(userStatistics.getPage(), userStatistics.getLimit());
        List<UserStatistics> userStatisticsList = userStatisticsDayMapper.selectDayUserList(userStatistics);
        return new PageInfo<>(userStatisticsList);
    }

    @Override
    public UserStatistics selectUserTotal(UserStatistics userStatistics) {
        return userStatisticsDayMapper.selectDayUserTotal(userStatistics);
    }

    @Override
    public UserStatistics selectAccountAndNonactivatedTotal(UserStatistics userStatistics) {
        return userStatisticsDayMapper.selectAccountAndNonactivatedTotal(userStatistics);
    }

    @Override
    public List<UserStatistics> selectDayUserChart(UserStatistics userStatistics) {
        return userStatisticsDayMapper.selectDayUserChart(userStatistics);
    }
}
