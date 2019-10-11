package com.sandu.analysis.biz.tob.user.dao;

import com.sandu.analysis.biz.tob.user.model.UserStatistics2bDayDto;
import com.sandu.analysis.biz.tob.user.model.UserStatistics2bHourDto;
import com.sandu.analysis.biz.tob.user.model.UserInfoDto;

import java.time.LocalDate;
import java.util.List;

public interface UserStatisticsDao {

    List<UserInfoDto> selectUserInfoByUUidList(List<String> uuidList);

    void insertAfterDeleteWithDay(List<UserStatistics2bDayDto> dayDtos, LocalDate localDate);

    void insertAfterDeleteWithHour(List<UserStatistics2bHourDto> hourDtos, LocalDate localDate);

    /**
     * 获取某天已累计用户数
     */
    List<UserInfoDto> selectUserCountByDay(String endTime);

    /**
     * 获取某天未激活用户总数
     */
    List<UserInfoDto> selectNonactivatedUserCountByDay(String endTime);

    /**
     * 获取某企业下所有有效用户数量
     */
    Long selectUserTotalByCompanyId(Integer companyId,String endTime);

    /**
     * 获取某个时间段各类型的用户新增数量
     */
    List<UserInfoDto> selectNewUserCountWithHour(String beginTime,String endTime);

    /**
     * 获取某天各类型的用户新增数量
     */
    List<UserInfoDto> selectNewUserCountWithDay(String beginTime,String endTime);

    /**
     * 获取某天各城市的用户新增数量
     */
    List<UserInfoDto> selectNewUserCountByCity(String beginTime,String endTime);

    /**
     * 获取某天新增的用户 uuid 集合
     */
    List<String> selectNewUserUuidWithDay(String beginTime,String endTime);
}
