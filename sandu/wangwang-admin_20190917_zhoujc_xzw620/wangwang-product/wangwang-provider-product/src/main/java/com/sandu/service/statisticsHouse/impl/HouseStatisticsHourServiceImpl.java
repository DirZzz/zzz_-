package com.sandu.service.statisticsHouse.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsHouse.model.HouseStatistics;
import com.sandu.api.statisticsHouse.service.HouseStatisticsHourService;
import com.sandu.api.statisticsPlan.model.PlanStatistics;
import com.sandu.api.statisticsPlan.service.PlanStatisticsHourService;
import com.sandu.service.statisticsHouse.dao.HouseStatisticsDayMapper;
import com.sandu.service.statisticsHouse.dao.HouseStatisticsHourMapper;
import com.sandu.service.statisticsPlan.dao.PlanStatisticsHourMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("houseStatisticsHourService")
public class HouseStatisticsHourServiceImpl implements HouseStatisticsHourService {
    @Autowired
    private HouseStatisticsHourMapper houseStatisticsHourMapper;

    @Override
    public PageInfo<HouseStatistics> selectList(HouseStatistics houseStatistics) {
        PageHelper.startPage(houseStatistics.getPage(), houseStatistics.getLimit());
        List<HouseStatistics> houseStatisticsList = houseStatisticsHourMapper.selectHourHouseList(houseStatistics);
        return new PageInfo<>(houseStatisticsList);
    }

    @Override
    public HouseStatistics selectHouseTotal(HouseStatistics houseStatistics) {
        return houseStatisticsHourMapper.selectHourHouseTotal(houseStatistics);
    }

    @Override
    public List<HouseStatistics> selectHourHouseChart(HouseStatistics houseStatistics) {
        return houseStatisticsHourMapper.selectHourHouseChart(houseStatistics);
    }
}
