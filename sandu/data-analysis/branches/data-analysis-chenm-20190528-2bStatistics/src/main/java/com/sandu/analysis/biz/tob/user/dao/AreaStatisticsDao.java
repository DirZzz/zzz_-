package com.sandu.analysis.biz.tob.user.dao;

import com.sandu.analysis.biz.tob.user.model.AreaStatistics2bDayDto;

import java.time.LocalDate;
import java.util.List;

public interface AreaStatisticsDao {

    void insertAfterDelete(List<AreaStatistics2bDayDto> dtos, LocalDate localDate);
}
