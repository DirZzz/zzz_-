package com.sandu.api.statisticsPlan.service;

import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsPlan.model.PlanUsageAmountDay;
import org.springframework.stereotype.Component;

import java.util.List;

public interface PlanUsageAmountDayService {

    PageInfo<PlanUsageAmountDay> selectDetailList(PlanUsageAmountDay query);

    List<PlanUsageAmountDay> selectHotPlan(PlanUsageAmountDay query);
}
