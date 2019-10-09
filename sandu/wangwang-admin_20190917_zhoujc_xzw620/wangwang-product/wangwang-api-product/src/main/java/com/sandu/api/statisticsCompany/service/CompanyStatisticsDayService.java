package com.sandu.api.statisticsCompany.service;

import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsCompany.model.CompanyStatisticsDay;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CompanyStatisticsDayService {
    PageInfo<CompanyStatisticsDay> selectDetailList(CompanyStatisticsDay query);

    List<CompanyStatisticsDay> selectHotCompany(CompanyStatisticsDay query);
}
