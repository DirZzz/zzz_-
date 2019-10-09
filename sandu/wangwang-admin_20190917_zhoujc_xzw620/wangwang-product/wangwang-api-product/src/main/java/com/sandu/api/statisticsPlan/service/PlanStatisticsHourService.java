package com.sandu.api.statisticsPlan.service;

import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsPlan.model.PlanStatistics;
import org.springframework.stereotype.Component;

import java.util.List;

public interface PlanStatisticsHourService {

    PageInfo<PlanStatistics> selectList(PlanStatistics query);
    PlanStatistics selectPlanTotal(PlanStatistics planStatistics);
    List<PlanStatistics> selectHourPlanChart(PlanStatistics query);

}
