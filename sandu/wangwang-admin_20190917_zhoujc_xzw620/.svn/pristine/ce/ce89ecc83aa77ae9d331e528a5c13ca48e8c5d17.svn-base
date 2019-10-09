package com.sandu.service.statisticsHouse.dao;

import com.sandu.api.statisticsHouse.model.HouseStatistics;
import com.sandu.api.statisticsPlan.model.PlanStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseStatisticsDayMapper {
    List<HouseStatistics> selectDayHouseList(HouseStatistics query);
    HouseStatistics selectDayHouseTotal(HouseStatistics query);
    List<HouseStatistics> selectDayHouseChart(HouseStatistics query);

}
