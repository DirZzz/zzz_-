package com.sandu.service.statisticsPlan.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsPlan.model.PlanStatistics;
import com.sandu.api.statisticsPlan.service.PlanStatisticsHourService;
import com.sandu.service.statisticsPlan.dao.PlanStatisticsHourMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("planStatisticsHourService")
public class PlanStatisticsHourServiceImpl implements PlanStatisticsHourService {
    @Autowired
    private PlanStatisticsHourMapper planStatisticsHourMapper;

    @Override
    public PageInfo<PlanStatistics> selectList(PlanStatistics planStatistics) {
        PageHelper.startPage(planStatistics.getPage(), planStatistics.getLimit());
        List<PlanStatistics> planStatisticsList = planStatisticsHourMapper.selectHourPlanList(planStatistics);
        return new PageInfo<>(planStatisticsList);
    }

    @Override
    public PlanStatistics selectPlanTotal(PlanStatistics planStatistics) {
        return planStatisticsHourMapper.selectHourPlanTotal(planStatistics);
    }

    @Override
    public List<PlanStatistics> selectHourPlanChart(PlanStatistics planStatistics) {
        return planStatisticsHourMapper.selectHourPlanChart(planStatistics);
    }
}
