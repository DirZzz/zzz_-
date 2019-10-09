package com.sandu.service.statistics.impl;

import com.sandu.api.statistics.dto.HouseDto;
import com.sandu.api.statistics.dto.PicDto;
import com.sandu.api.statistics.dto.ResourceStatisticsDto;
import com.sandu.api.statistics.model.OtherResourceStatistics;
import com.sandu.api.statistics.model.ResourceStatisticsDeviceLog;
import com.sandu.api.statistics.model.ShareResourceStatistics;
import com.sandu.api.statistics.service.ResourceStatisticsService;
import com.sandu.api.statistics.service.ResourceStatisticsServiceEnum;
import com.sandu.common.util.date.DateUtil;
import com.sandu.service.statistics.dao.OtherResourceStatisticsDao;
import com.sandu.service.statistics.dao.ResourceStatisticsDao;
import com.sandu.service.statistics.dao.ResourceStatisticsDeviceLogDao;
import com.sandu.service.statistics.dao.ShareResourceStatisticsDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 其他资源统计
 * @author: chenqiang
 * @create: 2019-06-05 16:45
 */
@Service(value = "otherResourceStatisticsServiceImpl")
@Slf4j
public class OtherResourceStatisticsServiceImpl implements ResourceStatisticsService {

    @Autowired
    private ResourceStatisticsDao resourceStatisticsDao;

    @Autowired
    private OtherResourceStatisticsDao otherResourceStatisticsDao;

    @Autowired
    private ResourceStatisticsDeviceLogDao resourceStatisticsDeviceLogDao;

    @Override
    public void resourceStatistics() throws Exception{

        log.info("其他资源统计开始");

        // 查看是否有强制更新计划
        ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
        logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.other.name());
        logSearch.setDeviceType(ResourceStatisticsDeviceLog.deviceTypeEnum.force.name());
        logSearch.setStatusList(Arrays.stream(new Integer[]{0, 1}).collect(Collectors.toList()));

        List<ResourceStatisticsDeviceLog> resourceStatisticsDeviceLogList = resourceStatisticsDeviceLogDao.selectList(logSearch);

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        int differenceDay = 0;
        LocalDateTime localDateTime = null;

        if (null != resourceStatisticsDeviceLogList && resourceStatisticsDeviceLogList.size() > 0) {

            for (ResourceStatisticsDeviceLog resourceStatisticsDeviceLog : resourceStatisticsDeviceLogList) {

                // 获取开始时间
                startTime = DateUtil.coverStrToLDT(resourceStatisticsDeviceLog.getStartDate(),DateUtil.DATE_TIME_PATTERN);

                // 获取结束时间
                endTime = DateUtil.coverStrToLDT(resourceStatisticsDeviceLog.getEndDate(),DateUtil.DATE_TIME_PATTERN);

                // 获取相差天数
                differenceDay = DateUtil.timeDifference(startTime,endTime).intValue() + 1;

                localDateTime = startTime;
                for (int i = 0 ; i < differenceDay; i++){

                    // 统计当天资源信息
                    resourceStatisticsDetails(localDateTime,ResourceStatisticsDeviceLog.deviceTypeEnum.force.name());

                    // 下一天
                    localDateTime = DateUtil.plus(localDateTime,1,ChronoUnit.DAYS);
                }

                // 更改计划状态
                resourceStatisticsDeviceLog.setStatus(2);
                resourceStatisticsDeviceLogDao.updateByPrimaryKeySelective(resourceStatisticsDeviceLog);
            }
        }

        // 定时执行计划,检查前5天信息
        startTime = DateUtil.minus(LocalDateTime.now(),5,ChronoUnit.DAYS);
        endTime = LocalDateTime.now();
        differenceDay = DateUtil.timeDifference(startTime,endTime).intValue();

        localDateTime = startTime;
        for (int i = 0 ; i < differenceDay; i++){

            final LocalDateTime dateTime = localDateTime;
            pool.submit(new Runnable() {
                @Override
                public void run() {

                    try {

                        // 统计当天资源信息
                        resourceStatisticsDetails(dateTime,ResourceStatisticsDeviceLog.deviceTypeEnum.time.name());

                    } catch (Exception e){

                        log.error("调用其他实现类统计资源详情 异常",e);
                        e.printStackTrace();
                        System.out.println(e);
                    }
                }
            });

            // 下一天
            localDateTime = DateUtil.plus(localDateTime,1,ChronoUnit.DAYS);
        }

    }


    private void resourceStatisticsDetails(final LocalDateTime dateTime,String deviceType) throws Exception{

        LocalDateTime localDateTime = dateTime;
        ResourceStatisticsDto dto = new ResourceStatisticsDto();
        OtherResourceStatistics otherResourceStatistics = new OtherResourceStatistics();

        dto.setStartDate(DateUtil.getDayStart(localDateTime));
        dto.setEndDate(DateUtil.getDayEnd(localDateTime));
        int date = Integer.parseInt(DateUtil.formatDateTime(localDateTime,DateUtil.DATE_PATTERN).replaceAll("-",""));
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int week = DateUtil.getWeekByTime(localDateTime);
        int day = localDateTime.getDayOfMonth();

        log.info("其他资源统计开始-时间="+date);

        // 查询是否存在当前时间计划日志
        ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
        logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.other.name());
        logSearch.setDeviceType(ResourceStatisticsDeviceLog.deviceTypeEnum.time.name());
        logSearch.setDate(date);
        List<ResourceStatisticsDeviceLog> resourceStatisticsDeviceLogList = resourceStatisticsDeviceLogDao.selectList(logSearch);

        // 计划日志
        ResourceStatisticsDeviceLog resourceStatisticsDeviceLog = null;
        if (null != resourceStatisticsDeviceLogList && resourceStatisticsDeviceLogList.size() > 0) {

            resourceStatisticsDeviceLog = resourceStatisticsDeviceLogList.get(0);

            // 判断计划日志状态
            if (ResourceStatisticsDeviceLog.deviceTypeEnum.time.name().equals(deviceType) && 2 == resourceStatisticsDeviceLog.getStatus()) {
                return;
            }

            resourceStatisticsDeviceLog.setStatus(0);
            resourceStatisticsDeviceLogDao.updateByPrimaryKeySelective(resourceStatisticsDeviceLog);
        } else {

            resourceStatisticsDeviceLog = new ResourceStatisticsDeviceLog();
            resourceStatisticsDeviceLog.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.other.name());
            resourceStatisticsDeviceLog.setDeviceType(ResourceStatisticsDeviceLog.deviceTypeEnum.time.name());
            resourceStatisticsDeviceLog.setStatus(0);
            resourceStatisticsDeviceLog.setStartDate(DateUtil.getDayStart(localDateTime));
            resourceStatisticsDeviceLog.setEndDate(DateUtil.getDayEnd(localDateTime));
            resourceStatisticsDeviceLog.setDate(date);
            resourceStatisticsDeviceLog.setSysCode(UUID.randomUUID().toString().replace("-",""));
            resourceStatisticsDeviceLog.setCreator("system");
            resourceStatisticsDeviceLog.setGmtCreate(new Date());
            resourceStatisticsDeviceLog.setModifier("system");
            resourceStatisticsDeviceLog.setGmtModified(new Date());
            resourceStatisticsDeviceLog.setIsDeleted(0);
            resourceStatisticsDeviceLogDao.insertSelective(resourceStatisticsDeviceLog);
        }


        // 获取互动区资源
        Long zoneSource = getZoneSource(dto);

        // 构建资源信息
        otherResourceStatistics.setInteractiveZone(zoneSource + "");
        otherResourceStatistics.setDate(date);
        otherResourceStatistics.setDay(day);
        otherResourceStatistics.setWeek(week);
        otherResourceStatistics.setMonth(month);
        otherResourceStatistics.setYear(year);
        otherResourceStatistics.setSysCode(UUID.randomUUID().toString().replace("-",""));
        otherResourceStatistics.setCreator("system");
        otherResourceStatistics.setGmtCreate(new Date());
        otherResourceStatistics.setModifier("system");
        otherResourceStatistics.setGmtModified(new Date());
        otherResourceStatistics.setIsDeleted(0);

        // 查询是否存在资源信息
        OtherResourceStatistics otherSearch = new OtherResourceStatistics();
        otherSearch.setDate(date);
        List<OtherResourceStatistics> otherResourceStatisticsList = otherResourceStatisticsDao.selectList(otherSearch);
        if (null != otherResourceStatisticsList && otherResourceStatisticsList.size() > 0) {

            otherResourceStatistics.setId(otherResourceStatisticsList.get(0).getId());
            otherResourceStatisticsDao.updateByPrimaryKeySelective(otherResourceStatistics);
        } else {

            otherResourceStatisticsDao.insertSelective(otherResourceStatistics);
        }

        // 日志成功
        resourceStatisticsDeviceLog.setStatus(2);
        resourceStatisticsDeviceLogDao.updateByPrimaryKeySelective(resourceStatisticsDeviceLog);
    }

    @Override
    public ResourceStatisticsServiceEnum getServiceImplKey() {
        return ResourceStatisticsServiceEnum.otherResourceStatisticsServiceImpl;
    }


    private Long getZoneSource(ResourceStatisticsDto dto){
        long size = 0;

        // 获取互动区
        long zoneSize = resourceStatisticsDao.selectZoneSource(dto);

        List<PicDto> picDtoList = resourceStatisticsDao.listArticleSourceOne(dto);

        long picSzie = 0;
        if (null != picDtoList && picDtoList.size() > 0) {
            
            List<Integer> picIdList = new ArrayList<>();

            picDtoList.stream().forEach(picDto -> {

                if (null != picDto.getHouseCoverId() && picDto.getHouseCoverId() > 0) {

                    picIdList.add(picDto.getHouseCoverId());
                }

                if (null != picDto.getCoverPicId() && picDto.getCoverPicId() > 0) {

                    picIdList.add(picDto.getCoverPicId());
                }

                if (StringUtils.isNotBlank(picDto.getPicIds())) {

                    picIdList.addAll(Arrays.stream(picDto.getPicIds().split(",")).filter(s -> StringUtils.isNotBlank(s) && Integer.parseInt(s) > 0).map(s -> Integer.parseInt(s)).collect(Collectors.toList()));
                }
            });


            picSzie = getPicSource(picIdList,dto);
        }

        size = zoneSize + picSzie;

        return size;
    }


    /** 计算图片主图、缩略图资源 */
    private Long getPicSource(List<Integer> picIdList,ResourceStatisticsDto dto){

        long size = 0;

        if (null != picIdList && picIdList.size() > 0) {

            List<Integer> picAllIdList = new ArrayList<>();
            ResourceStatisticsDto dtoSearch = new ResourceStatisticsDto();
            BeanUtils.copyProperties(dto,dtoSearch);
            dtoSearch.setPicIdList(picIdList);
            picAllIdList.addAll(picIdList);

            // 查询图片主图
            List<PicDto> picDtoList = resourceStatisticsDao.listPidPic(dtoSearch);

            if (null != picDtoList && picDtoList.size() > 0 ) {

                for (PicDto picDto : picDtoList) {

                    if (StringUtils.isNotBlank(picDto.getSmallPicInfo())) {

                        String[] smallIds = picDto.getSmallPicInfo().split(";");

                        if (null != smallIds && smallIds.length > 0) {

                            for (String smallId : smallIds) {

                                String[] smalls = smallId.split(":");

                                if (null != smalls && smalls.length >= 1) {

                                    try {
                                        picAllIdList.add(Integer.parseInt(smalls[1]));
                                    } catch (Exception e){

                                    }

                                }
                            }
                        }


                    }
                }

                picAllIdList = picAllIdList.stream().distinct().collect(Collectors.toList());
                dtoSearch.setPicIdList(picAllIdList);
                size = resourceStatisticsDao.selectPicSource(dtoSearch);
            }
        }

        return size;
    }
}
