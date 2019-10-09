package com.sandu.service.statisticsCompany.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsCompany.model.CompanyStatisticsDay;
import com.sandu.api.statisticsCompany.service.CompanyStatisticsDayService;
import com.sandu.service.statisticsCompany.dao.CompanyStatisticsDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("companyStatisticsDayService")
public class CompanyStatisticsDayServiceImpl implements CompanyStatisticsDayService {
    @Autowired
    private CompanyStatisticsDayMapper companyStatisticsDayMapper;

    @Override
    public PageInfo<CompanyStatisticsDay> selectDetailList(CompanyStatisticsDay companyStatisticsDay) {
        PageHelper.startPage(companyStatisticsDay.getPage(), companyStatisticsDay.getLimit());
        List<CompanyStatisticsDay> companyStatisticsDayList = companyStatisticsDayMapper.selectDetailList(companyStatisticsDay);
        return new PageInfo<>(companyStatisticsDayList);
    }

    @Override
    public List<CompanyStatisticsDay> selectHotCompany(CompanyStatisticsDay companyStatisticsDay) {
        return companyStatisticsDayMapper.selectHotCompany(companyStatisticsDay);
    }
}
