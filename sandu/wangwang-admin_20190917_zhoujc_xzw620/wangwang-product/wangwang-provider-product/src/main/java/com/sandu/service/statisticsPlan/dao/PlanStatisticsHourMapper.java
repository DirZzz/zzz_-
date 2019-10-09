package com.sandu.service.statisticsPlan.dao;

import com.sandu.api.statisticsPlan.model.PlanStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanStatisticsHourMapper {
    List<PlanStatistics> selectHourPlanList(PlanStatistics query);
    PlanStatistics selectHourPlanTotal(PlanStatistics planStatistics);
    List<PlanStatistics> selectHourPlanChart(PlanStatistics query);
}
