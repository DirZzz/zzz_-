package com.sandu.service.statisticsPlan.dao;

import com.sandu.api.statisticsPlan.model.PlanUsageAmountDay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanUsageAmountDayMapper {
    List<PlanUsageAmountDay> selectDetailList(PlanUsageAmountDay query);

    List<PlanUsageAmountDay> selectHotPlan(PlanUsageAmountDay query);

}
