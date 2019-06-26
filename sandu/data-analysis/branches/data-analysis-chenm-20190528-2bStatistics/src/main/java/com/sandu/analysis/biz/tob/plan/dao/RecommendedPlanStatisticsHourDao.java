package com.sandu.analysis.biz.tob.plan.dao;

import com.sandu.analysis.biz.tob.plan.model.RecommendedPlanStatisticsHourDto;

import java.time.LocalDate;
import java.util.List;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-15 19:55
 * Desc:
 */
public interface RecommendedPlanStatisticsHourDao {

    void insertAfterDelete(List<RecommendedPlanStatisticsHourDto> dtos, LocalDate localDate);

    List<RecommendedPlanStatisticsHourDto> selectNewPlanCount(LocalDate localDate);
}
