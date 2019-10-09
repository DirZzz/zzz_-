package com.sandu.service.statistics.impl;

import com.sandu.api.statistics.dto.ResourceStatisticsDto;
import com.sandu.api.statistics.dto.ShopResourceDto;
import com.sandu.api.statistics.dto.UserDto;
import com.sandu.api.statistics.model.ResourceStatisticsDeviceLog;
import com.sandu.api.statistics.model.UserResourceStatistics;
import com.sandu.api.statistics.service.ResourceStatisticsService;
import com.sandu.api.statistics.service.ResourceStatisticsServiceEnum;
import com.sandu.common.util.StringUtils;
import com.sandu.common.util.date.DateUtil;
import com.sandu.service.statistics.dao.ResourceStatisticsDao;
import com.sandu.service.statistics.dao.ResourceStatisticsDeviceLogDao;
import com.sandu.service.statistics.dao.UserResourceStatisticsDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 用户资源统计
 * @author: chenqiang
 * @create: 2019-05-25 16:30
 */
@Service(value = "userResourceStatisticsServiceImpl")
@Slf4j
public class UserResourceStatisticsServiceImpl implements ResourceStatisticsService {

    @Autowired
    private ResourceStatisticsDao resourceStatisticsDao;

    @Autowired
    private UserResourceStatisticsDao userResourceStatisticsDao;

    @Autowired
    private ResourceStatisticsDeviceLogDao resourceStatisticsDeviceLogDao;

    @Override
    public void resourceStatistics() throws Exception{

        log.info("用户资源统计开始");

        // 查看是否有强制更新计划
        ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
        logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.user.name());
        logSearch.setDeviceType(ResourceStatisticsDeviceLog.deviceTypeEnum.force.name());
        logSearch.setStatusList(Arrays.stream(new Integer[]{0, 1}).collect(Collectors.toList()));

        List<ResourceStatisticsDeviceLog> resourceStatisticsDeviceLogList = resourceStatisticsDeviceLogDao.selectList(logSearch);

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Integer differenceDay = 0;
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
                    resourceStatisticsTask(localDateTime,ResourceStatisticsDeviceLog.deviceTypeEnum.force.name());

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
                        resourceStatisticsTask(dateTime,ResourceStatisticsDeviceLog.deviceTypeEnum.time.name());

                    } catch (Exception e){

                        log.error("调用用户实现类统计资源 异常",e);
                        e.printStackTrace();
                        System.out.println(e);
                    }
                }
            });

            // 下一天
            localDateTime = DateUtil.plus(localDateTime,1,ChronoUnit.DAYS);
        }

    }


    private void resourceStatisticsTask (final LocalDateTime dateTime,String deviceType) throws Exception{

        LocalDateTime localDateTime = dateTime;
        int date = Integer.parseInt(DateUtil.formatDateTime(localDateTime,DateUtil.DATE_PATTERN).replaceAll("-",""));
        ResourceStatisticsDto dtoSerach = new ResourceStatisticsDto();

        // 获取所有用户
        List<UserDto> userDtoList = resourceStatisticsDao.listUser(dtoSerach);

        if (null != userDtoList && userDtoList.size() > 0) {

            int sum = userDtoList.size();
            int count = 1;

            if (sum > ResourceStatisticsDto.LIMIT_SIZE) {

                count = ((int)Math.ceil((double)sum/(double)ResourceStatisticsDto.LIMIT_SIZE));
            }

            // 查询是否存在当前时间计划日志
            ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
            logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.user.name());
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
                resourceStatisticsDeviceLog.setGmtModified(new Date());
                resourceStatisticsDeviceLogDao.updateByPrimaryKeySelective(resourceStatisticsDeviceLog);
            } else {

                resourceStatisticsDeviceLog = new ResourceStatisticsDeviceLog();
                resourceStatisticsDeviceLog.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.user.name());
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

            CountDownLatch countDownLatchCount = new CountDownLatch(count);
            int start = 0;
            for (int i = 0 ; i < count ; i++){

                start = i * ResourceStatisticsDto.LIMIT_SIZE;
                dtoSerach.setStart(start);
                dtoSerach.setSize(ResourceStatisticsDto.LIMIT_SIZE);

                userDtoList = resourceStatisticsDao.listUser(dtoSerach);

                final List<UserDto> userDtoListParam = userDtoList;

                pool.submit(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            // 统计当天资源信息
                            resourceStatisticsDetails(dateTime,ResourceStatisticsDeviceLog.deviceTypeEnum.time.name(),userDtoListParam);

                            countDownLatchCount.countDown();

                        } catch (Exception e){

                            log.error("调用企业实现类统计资源详情 异常",e);
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                });

            }

            countDownLatchCount.await();

            // 日志成功
            resourceStatisticsDeviceLog.setStatus(2);
            resourceStatisticsDeviceLog.setGmtModified(new Date());
            resourceStatisticsDeviceLogDao.updateByPrimaryKeySelective(resourceStatisticsDeviceLog);
        }
    }

    private void resourceStatisticsDetails(final LocalDateTime dateTime,String deviceType,final List<UserDto> userDtoList) throws Exception{

        LocalDateTime localDateTime = dateTime;
        UserResourceStatistics userResourceStatistics = null;

        ResourceStatisticsDto dto = new ResourceStatisticsDto();
        dto.setStartDate(DateUtil.getDayStart(localDateTime));
        dto.setEndDate(DateUtil.getDayEnd(localDateTime));

        if (null != userDtoList && userDtoList.size() > 0) {

            Integer date = Integer.parseInt(DateUtil.formatDateTime(localDateTime,DateUtil.DATE_PATTERN).replaceAll("-",""));
            Integer year = localDateTime.getYear();
            Integer month = localDateTime.getMonthValue();
            Integer week = DateUtil.getWeekByTime(localDateTime);
            Integer day = localDateTime.getDayOfMonth();

            log.info("用户资源统计开始-时间="+date);

            for (UserDto userDto : userDtoList) {

                // 参数设置
                dto.setUserId(userDto.getId());
                userResourceStatistics = new UserResourceStatistics();

                // 获取用户方案配置资源
                Long userPlanConfigSize = resourceStatisticsDao.selectUserPlanConfigSource(dto);

                // 获取用户方案渲染资源
                Long userPlanRenderSize = resourceStatisticsDao.selectUserPlanRenderSource(dto);

                // 获取用户效果图方案配置资源
                Long userScenePlanConfigSize = resourceStatisticsDao.selectUserScenePlanRenderSource(dto);

                // 获取用户效果图方案渲染资源
                Long userScenePlanRenderSize = resourceStatisticsDao.selectUserScenePlanRenderSource(dto);

                // 获取用户通用版上传材质资源
                Long userTextureSize = resourceStatisticsDao.selectUserTextureSource(dto);

                // 获取店铺资源
                Long userShopSize = this.getUserShopSize(dto);

                // 获取店铺博文资源
                Long userShopArticleSize = this.getUserShopArticleSize(dto);

                // 获取店铺工程案例资源
                Long userShopCaseSize = this.getUserShopCaseSize(dto);

                // 构造用户资源信息
                userResourceStatistics.setUserId(userDto.getId());
                userResourceStatistics.setPlanRender(userPlanRenderSize + "");
                userResourceStatistics.setPlanConfig(userPlanConfigSize + "");
                userResourceStatistics.setScenePlanRender(userScenePlanRenderSize + "");
                userResourceStatistics.setScenePlanConfig(userScenePlanConfigSize + "");
                userResourceStatistics.setTexture(userTextureSize + "");
                userResourceStatistics.setShop(userShopSize + "");
                userResourceStatistics.setShopBowen(userShopArticleSize + "");
                userResourceStatistics.setShopEngineering(userShopCaseSize + "");
                userResourceStatistics.setDate(date);
                userResourceStatistics.setDay(day);
                userResourceStatistics.setWeek(week);
                userResourceStatistics.setMonth(month);
                userResourceStatistics.setYear(year);
                userResourceStatistics.setSysCode(UUID.randomUUID().toString().replace("-",""));
                userResourceStatistics.setCreator("system");
                userResourceStatistics.setGmtCreate(new Date());
                userResourceStatistics.setModifier("system");
                userResourceStatistics.setGmtModified(new Date());
                userResourceStatistics.setIsDeleted(0);

                // 查询是否存在资源信息
                UserResourceStatistics userSearch = new UserResourceStatistics();
                userSearch.setUserId(userDto.getId());
                userSearch.setDate(date);
                List<UserResourceStatistics> userResourceStatisticsList = userResourceStatisticsDao.selectList(userSearch);
                if (null != userResourceStatisticsList && userResourceStatisticsList.size() > 0) {

                    userResourceStatistics.setId(userResourceStatisticsList.get(0).getId());
                    userResourceStatisticsDao.updateByPrimaryKeySelective(userResourceStatistics);
                } else {

                    userResourceStatisticsDao.insertSelective(userResourceStatistics);
                }

            }
        }
    }


    /** 获取店铺资源 */
    private Long getUserShopSize(ResourceStatisticsDto dto){
        long size = 0;

        ResourceStatisticsDto searchDto = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,searchDto);
        searchDto.setShopType(2);

        // 获取店铺封面、logo、介绍 资源id集合
        List<ShopResourceDto> shopResourceDtoList = resourceStatisticsDao.selectShop(searchDto);

        if (null != shopResourceDtoList && shopResourceDtoList.size() > 0) {

            List<Integer> picIdList = new ArrayList<>();
            List<Integer> fileIdList = new ArrayList<>();

            shopResourceDtoList.stream().forEach(o -> {

                if (null != o.getCoverPicId() && o.getCoverPicId() > 0) {

                    picIdList.add(o.getCoverPicId());
                }

                if (StringUtils.isNotBlank(o.getCoverResIds())) {

                    picIdList.addAll(Arrays.asList(o.getCoverResIds().split(",")).stream().filter(s -> StringUtils.isNotBlank(s) && Integer.parseInt(s) > 0).map(s -> Integer.parseInt(s)).collect(Collectors.toList()));
                }

                if (null != o.getIntroducedFileId() && o.getIntroducedFileId() > 0) {

                    fileIdList.add(o.getIntroducedFileId());
                }

            });

            long userShopPicSize = 0;
            if (null != picIdList && picIdList.size() > 0) {

                searchDto.setPicIdList(picIdList);
                userShopPicSize = resourceStatisticsDao.selectShopPicSource(searchDto);
            }

            long userShopFileSize = 0;
            if (null != fileIdList && fileIdList.size() > 0) {

                searchDto.setFileIdList(fileIdList);
                userShopFileSize = resourceStatisticsDao.selectShopIntroducedSource(searchDto);
            }

            size = size + userShopPicSize + userShopFileSize;
        }

        return size;
    }

    /** 获取店铺博文资源 */
    private Long getUserShopArticleSize(ResourceStatisticsDto dto){
        long size = 0;

        ResourceStatisticsDto searchDto = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,searchDto);
        searchDto.setShopType(2);

        // 获取博文封面资源id
        List<Integer> coverIdList = resourceStatisticsDao.selectShopArticleCover(searchDto);

        // 获取博文封面资源
        if (null != coverIdList && coverIdList.size() > 0) {

            searchDto.setPicIdList(coverIdList);
            size = resourceStatisticsDao.selectShopPicSource(searchDto);

        }

        return size;
    }

    /** 获取店铺工程案例资源 */
    private long getUserShopCaseSize(ResourceStatisticsDto dto){
        long size = 0;

        ResourceStatisticsDto searchDto = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,searchDto);
        searchDto.setShopType(2);

        // 获取店铺工程案例 封面、logo、介绍 资源id 集合
        List<ShopResourceDto> shopResourceDtoList = resourceStatisticsDao.selectShopCase(searchDto);

        if (null != shopResourceDtoList && shopResourceDtoList.size() > 0) {

            List<Integer> picIdList = new ArrayList<>();
            List<Integer> fileIdList = new ArrayList<>();

            shopResourceDtoList.stream().forEach(o -> {

                if (null != o.getCoverPicId() && o.getCoverPicId() > 0) {

                    picIdList.add(o.getCoverPicId());
                }

                if (null != o.getHouseTypePicId() && o.getHouseTypePicId() > 0) {

                    picIdList.add(o.getHouseTypePicId());
                }

                if (StringUtils.isNotBlank(o.getPicIds())) {

                    picIdList.addAll(Arrays.asList(o.getPicIds().split(",")).stream().filter(s -> StringUtils.isNotBlank(s) && Integer.parseInt(s) > 0).map(s -> Integer.parseInt(s)).collect(Collectors.toList()));
                }

                if (null != o.getFileId() && o.getFileId() > 0) {

                    fileIdList.add(o.getFileId());
                }
            });

            long userShopCasePicSize = 0;
            if (null != picIdList && picIdList.size() > 0 ) {

                searchDto.setPicIdList(picIdList);
                userShopCasePicSize = resourceStatisticsDao.selectShopPicSource(searchDto);
            }

            long userShopCaseFileSize = 0;
            if (null != fileIdList && fileIdList.size() > 0) {

                searchDto.setFileIdList(fileIdList);
                userShopCaseFileSize = resourceStatisticsDao.selectShopIntroducedSource(searchDto);
            }

            size = size + userShopCasePicSize + userShopCaseFileSize;
        }

        return size;
    }

    @Override
    public ResourceStatisticsServiceEnum getServiceImplKey() {
        return ResourceStatisticsServiceEnum.userResourceStatisticsServiceImpl;
    }
}
