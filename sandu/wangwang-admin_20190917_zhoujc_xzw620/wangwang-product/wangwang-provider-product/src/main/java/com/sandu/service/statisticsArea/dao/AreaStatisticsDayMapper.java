package com.sandu.service.statisticsArea.dao;

import com.sandu.api.statisticsArea.model.AreaStatisticsDay;
import com.sandu.api.statisticsCompany.model.CompanyStatisticsDay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaStatisticsDayMapper {
    List<AreaStatisticsDay> selectNewDetailList(AreaStatisticsDay query);

    List<AreaStatisticsDay> selectActiveDetailList(AreaStatisticsDay query);

    List<AreaStatisticsDay> selectNewHotArea(AreaStatisticsDay query);

    List<AreaStatisticsDay> selectActiveHotArea(AreaStatisticsDay query);

}
