package com.sandu.analysis.biz.tob.user.dao;


import com.sandu.analysis.biz.tob.user.model.CompanyInfoDto;
import com.sandu.analysis.biz.tob.user.model.CompanyStatistics2bDayDto;

import java.time.LocalDate;
import java.util.List;

public interface CompanyStatisticsDao {

    List<CompanyInfoDto> selectCompanyInfoListByIdList(List<Integer> idList);

    void insertAfterDelete(List<CompanyStatistics2bDayDto> dtos, LocalDate localDate);
}
