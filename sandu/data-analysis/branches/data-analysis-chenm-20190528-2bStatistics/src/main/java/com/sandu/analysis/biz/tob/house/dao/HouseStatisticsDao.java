package com.sandu.analysis.biz.tob.house.dao;

import com.sandu.analysis.biz.tob.house.model.HouseInfoDto;
import com.sandu.analysis.biz.tob.house.model.HouseStatisticsDayDto;
import com.sandu.analysis.biz.tob.house.model.HouseStatisticsHourDto;
import com.sandu.analysis.biz.tob.house.model.HouseUsageAmountStatisticsDayDto;

import java.util.List;

/**
 * 户型统计数据层
 * @author xiaoxc
 * @data 2019/6/14 0014.
 */
public interface HouseStatisticsDao {

    List<HouseInfoDto> selectHouseArea(List<String> houseIdList);

    List<HouseInfoDto> selectNewHouseCount(String startTime, String endTime, String type);

    void insertByDay(List<HouseStatisticsDayDto> dtoList, String startTime, String endTime);

    void insertByHour(List<HouseStatisticsHourDto> dtoList, String startTime, String endTime);

    void insertHouseUsageAmountByDay(List<HouseUsageAmountStatisticsDayDto> dtoList, String startTime, String endTime);

    void delete(String startTime, String endTime, String tableName);

}
