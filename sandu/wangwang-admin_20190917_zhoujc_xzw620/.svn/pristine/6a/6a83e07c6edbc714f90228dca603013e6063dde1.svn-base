package com.sandu.service.statisticsHouse.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsHouse.model.HouseStatistics;
import com.sandu.api.statisticsHouse.service.HouseStatisticsDayService;
import com.sandu.service.statisticsHouse.dao.HouseStatisticsDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("houseStatisticsDayService")
public class HouseStatisticsDayServiceImpl implements HouseStatisticsDayService {
    @Autowired
    private HouseStatisticsDayMapper houseStatisticsDayMapper;

    @Override
    public PageInfo<HouseStatistics> selectList(HouseStatistics houseStatistics) {
        PageHelper.startPage(houseStatistics.getPage(), houseStatistics.getLimit());
        List<HouseStatistics> houseStatisticsList = houseStatisticsDayMapper.selectDayHouseList(houseStatistics);
        return new PageInfo<>(houseStatisticsList);
    }

    @Override
    public HouseStatistics selectHouseTotal(HouseStatistics houseStatistics) {
        return houseStatisticsDayMapper.selectDayHouseTotal(houseStatistics);
    }

    @Override
    public List<HouseStatistics> selectDayHouseChart(HouseStatistics houseStatistics) {
        return houseStatisticsDayMapper.selectDayHouseChart(houseStatistics);
    }

}
