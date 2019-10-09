package com.sandu.service.statisticsArea.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsArea.model.AreaStatisticsDay;
import com.sandu.api.statisticsArea.service.AreaStatisticsDayService;
import com.sandu.service.statisticsArea.dao.AreaStatisticsDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("areaStatisticsDayService")
public class AreaStatisticsDayServiceImpl implements AreaStatisticsDayService {
    @Autowired
    private AreaStatisticsDayMapper areaStatisticsDayMapper;

    @Override
    public PageInfo<AreaStatisticsDay> selectNewDetailList(AreaStatisticsDay areaStatisticsDay) {
        PageHelper.startPage(areaStatisticsDay.getPage(), areaStatisticsDay.getLimit());
        List<AreaStatisticsDay> areaStatisticsDayList = areaStatisticsDayMapper.selectNewDetailList(areaStatisticsDay);
        return new PageInfo<>(areaStatisticsDayList);
    }

    @Override
    public PageInfo<AreaStatisticsDay> selectActiveDetailList(AreaStatisticsDay areaStatisticsDay) {
        PageHelper.startPage(areaStatisticsDay.getPage(), areaStatisticsDay.getLimit());
        List<AreaStatisticsDay> areaStatisticsDayList = areaStatisticsDayMapper.selectActiveDetailList(areaStatisticsDay);
        return new PageInfo<>(areaStatisticsDayList);
    }

    @Override
    public List<AreaStatisticsDay> selectNewHotArea(AreaStatisticsDay areaStatisticsDay) {
        return areaStatisticsDayMapper.selectNewHotArea(areaStatisticsDay);
    }

    @Override
    public List<AreaStatisticsDay> selectActiveHotArea(AreaStatisticsDay areaStatisticsDay) {
        return areaStatisticsDayMapper.selectActiveHotArea(areaStatisticsDay);
    }

}
