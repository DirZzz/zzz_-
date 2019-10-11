package com.sandu.analysis.biz.tob.user.dao;

import com.sandu.analysis.biz.tob.user.model.AreaInfoDto;
import com.sandu.analysis.biz.tob.user.model.AreaStatistics2bDayDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface AreaStatisticsDao {

    void insertAfterDelete(List<AreaStatistics2bDayDto> dtos, LocalDate localDate);

    List<AreaInfoDto>  selectProvinceInfoByAreaNames(Set<String> areaNameSet);

    List<AreaInfoDto>  selectCityInfoByAreaNames(Set<String> areaNameSet);
}
