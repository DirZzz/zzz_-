package com.sandu.service.statisticsCompany.dao;

import com.sandu.api.statisticsCompany.model.CompanyStatisticsDay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyStatisticsDayMapper {
    List<CompanyStatisticsDay> selectDetailList(CompanyStatisticsDay query);

    List<CompanyStatisticsDay> selectHotCompany(CompanyStatisticsDay query);

}
