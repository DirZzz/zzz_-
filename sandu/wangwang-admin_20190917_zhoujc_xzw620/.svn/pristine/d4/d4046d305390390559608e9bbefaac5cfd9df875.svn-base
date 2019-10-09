package com.sandu.service.statisticsPlan.dao;

import com.sandu.api.statisticsPlan.model.PlanStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanStatisticsDayMapper {
    List<PlanStatistics> selectDayPlanList(PlanStatistics query);
    PlanStatistics selectDayPlanTotal(PlanStatistics planStatistics);
    List<PlanStatistics> selectDayPlanChart(PlanStatistics query);

}
