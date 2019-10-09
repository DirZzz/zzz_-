package com.sandu.service.statisticsHouse.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsHouse.model.HouseUsageAmountDay;
import com.sandu.api.statisticsHouse.service.HouseUsageAmountDayService;
import com.sandu.service.statisticsHouse.dao.HouseUsageAmountDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("houseUsageAmountDayService")
public class HouseUsageAmountDayServiceImpl implements HouseUsageAmountDayService {
    @Autowired
    private HouseUsageAmountDayMapper houseUsageAmountDayMapper;

    @Override
    public PageInfo<HouseUsageAmountDay> selectDetailList(HouseUsageAmountDay houseUsageAmountDay) {
        PageHelper.startPage(houseUsageAmountDay.getPage(), houseUsageAmountDay.getLimit());
        List<HouseUsageAmountDay> houseUsageAmountDayList = houseUsageAmountDayMapper.selectDetailList(houseUsageAmountDay);
        return new PageInfo<>(houseUsageAmountDayList);
    }

    @Override
    public List<HouseUsageAmountDay> selectHotHouse(HouseUsageAmountDay houseUsageAmountDay) {
        return houseUsageAmountDayMapper.selectHotHouse(houseUsageAmountDay);
    }
}
