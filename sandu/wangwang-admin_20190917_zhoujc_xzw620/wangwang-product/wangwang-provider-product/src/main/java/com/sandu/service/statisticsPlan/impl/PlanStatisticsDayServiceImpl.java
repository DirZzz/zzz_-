package com.sandu.service.statisticsPlan.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsPlan.model.PlanStatistics;
import com.sandu.api.statisticsPlan.service.PlanStatisticsDayService;
import com.sandu.service.statisticsPlan.dao.PlanStatisticsDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("planStatisticsDayService")
public class PlanStatisticsDayServiceImpl implements PlanStatisticsDayService {
    @Autowired
    private PlanStatisticsDayMapper planStatisticsDayMapper;

    @Override
    public PageInfo<PlanStatistics> selectList(PlanStatistics planStatistics) {
        PageHelper.startPage(planStatistics.getPage(), planStatistics.getLimit());
        List<PlanStatistics> planStatisticsList = planStatisticsDayMapper.selectDayPlanList(planStatistics);
        return new PageInfo<>(planStatisticsList);
    }

    @Override
    public PlanStatistics selectPlanTotal(PlanStatistics planStatistics) {
        return planStatisticsDayMapper.selectDayPlanTotal(planStatistics);
    }

    @Override
    public List<PlanStatistics> selectDayPlanChart(PlanStatistics planStatistics) {
        return planStatisticsDayMapper.selectDayPlanChart(planStatistics);
    }

}
