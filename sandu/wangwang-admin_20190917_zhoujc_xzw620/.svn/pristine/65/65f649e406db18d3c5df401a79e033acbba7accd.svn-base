package com.sandu.service.statisticsPlan.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsPlan.model.PlanUsageAmountDay;
import com.sandu.api.statisticsPlan.service.PlanUsageAmountDayService;
import com.sandu.service.statisticsPlan.dao.PlanUsageAmountDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("planUsageAmountDayService")
public class PlanUsageAmountDayServiceImpl implements PlanUsageAmountDayService {
    @Autowired
    private PlanUsageAmountDayMapper planUsageAmountDayMapper;

    @Override
    public PageInfo<PlanUsageAmountDay> selectDetailList(PlanUsageAmountDay planUsageAmountDay) {
        PageHelper.startPage(planUsageAmountDay.getPage(), planUsageAmountDay.getLimit());
        List<PlanUsageAmountDay> planUsageAmountDayList = planUsageAmountDayMapper.selectDetailList(planUsageAmountDay);
        return new PageInfo<>(planUsageAmountDayList);
    }

    @Override
    public List<PlanUsageAmountDay> selectHotPlan(PlanUsageAmountDay planUsageAmountDay) {
        return planUsageAmountDayMapper.selectHotPlan(planUsageAmountDay);
    }
}
