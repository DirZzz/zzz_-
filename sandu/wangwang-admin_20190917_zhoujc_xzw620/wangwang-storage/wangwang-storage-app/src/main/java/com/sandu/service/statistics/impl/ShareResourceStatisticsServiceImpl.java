package com.sandu.service.statistics.impl;

import com.sandu.api.statistics.dto.HouseDto;
import com.sandu.api.statistics.dto.PicDto;
import com.sandu.api.statistics.dto.ResourceStatisticsDto;
import com.sandu.api.statistics.model.CompanyResourceStatistics;
import com.sandu.api.statistics.model.ResourceStatisticsDeviceLog;
import com.sandu.api.statistics.model.ShareResourceStatistics;
import com.sandu.api.statistics.service.ResourceStatisticsService;
import com.sandu.api.statistics.service.ResourceStatisticsServiceEnum;
import com.sandu.common.util.ObjectUtils;
import com.sandu.common.util.date.DateUtil;
import com.sandu.service.statistics.dao.ResourceStatisticsDao;
import com.sandu.service.statistics.dao.ResourceStatisticsDeviceLogDao;
import com.sandu.service.statistics.dao.ShareResourceStatisticsDao;
import io.netty.util.internal.ObjectUtil;
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
 * 共有资源统计
 * @author: chenqiang
 * @create: 2019-06-05 16:45
 */
@Service(value = "shareResourceStatisticsServiceImpl")
@Slf4j
public class ShareResourceStatisticsServiceImpl implements ResourceStatisticsService {

    @Autowired
    private ResourceStatisticsDao resourceStatisticsDao;

    @Autowired
    private ShareResourceStatisticsDao shareResourceStatisticsDao;

    @Autowired
    private ResourceStatisticsDeviceLogDao resourceStatisticsDeviceLogDao;

    @Override
    public void resourceStatistics() throws Exception {

        log.info("共有资源统计开始");

        // 查看是否有强制更新计划
        ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
        logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.common.name());
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

                        log.error("调用共享实现类统计资源详情 异常",e);
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
        ShareResourceStatistics shareResourceStatistics = new ShareResourceStatistics();

        dto.setStartDate(DateUtil.getDayStart(localDateTime));
        dto.setEndDate(DateUtil.getDayEnd(localDateTime));
        Integer date = Integer.parseInt(DateUtil.formatDateTime(localDateTime,DateUtil.DATE_PATTERN).replaceAll("-",""));
        Integer year = localDateTime.getYear();
        Integer month = localDateTime.getMonthValue();
        Integer week = DateUtil.getWeekByTime(localDateTime);
        Integer day = localDateTime.getDayOfMonth();

        log.info("共有资源统计开始-时间="+date);

        // 查询是否存在当前时间计划日志
        ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
        logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.common.name());
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
            resourceStatisticsDeviceLog.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.common.name());
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

        // 获取户型资源
        Long houseSource = this.getHouseSource(dto);

        // 获取户型空间资源
        Long houseSpaceSource = this.getHouseSpaceSource(dto);

        // 获取户型空间对象样板房资源
        Long houseSpaceTempletSource = this.getHouseSpaceTempletSource(dto);

        // 获取供求信息
        Long supplySource = resourceStatisticsDao.selectSupplyDemandSource(dto);

        shareResourceStatistics.setHouse(houseSource + "");
        shareResourceStatistics.setSpace(houseSpaceSource + "");
        shareResourceStatistics.setTemplet(houseSpaceTempletSource + "");
        shareResourceStatistics.setSupplyInformation(supplySource + "");
        shareResourceStatistics.setDate(date);
        shareResourceStatistics.setDay(day);
        shareResourceStatistics.setWeek(week);
        shareResourceStatistics.setMonth(month);
        shareResourceStatistics.setYear(year);
        shareResourceStatistics.setSysCode(UUID.randomUUID().toString().replace("-",""));
        shareResourceStatistics.setCreator("system");
        shareResourceStatistics.setGmtCreate(new Date());
        shareResourceStatistics.setModifier("system");
        shareResourceStatistics.setGmtModified(new Date());
        shareResourceStatistics.setIsDeleted(0);

        // 查询是否存在资源信息
        ShareResourceStatistics shareSearch = new ShareResourceStatistics();
        shareSearch.setDate(date);
        List<ShareResourceStatistics> shareResourceStatisticsList = shareResourceStatisticsDao.selectList(shareSearch);
        if (null != shareResourceStatisticsList && shareResourceStatisticsList.size() > 0) {

            shareResourceStatistics.setId(shareResourceStatisticsList.get(0).getId());
            shareResourceStatisticsDao.updateByPrimaryKeySelective(shareResourceStatistics);
        } else {

            shareResourceStatisticsDao.insertSelective(shareResourceStatistics);
        }

        log.info("共有资源统计开始-时间="+date + "结束");

        // 日志成功
        resourceStatisticsDeviceLog.setStatus(2);
        resourceStatisticsDeviceLogDao.updateByPrimaryKeySelective(resourceStatisticsDeviceLog);
    }
    @Override
    public ResourceStatisticsServiceEnum getServiceImplKey() {
        return ResourceStatisticsServiceEnum.shareResourceStatisticsServiceImpl;
    }


    private Long getHouseSpaceTempletSource(ResourceStatisticsDto dto){
        long size = 0;
        ResourceStatisticsDto dtoSearch = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,dtoSearch);

        List<HouseDto> houseDtoList = resourceStatisticsDao.listHouseSpaceTemplet(dtoSearch);

        if (null != houseDtoList && houseDtoList.size() > 0) {

            List<Integer> picIdList = new ArrayList<>();
            List<Integer> modelIdList = new ArrayList<>();
            List<Integer> fileIdList = new ArrayList<>();

            houseDtoList.stream().forEach(houseDto -> {

                if (null != houseDto.getPicId() && houseDto.getPicId() > 0) {

                    picIdList.add(houseDto.getPicId());
                }

                if (null != houseDto.getEffectPic() && houseDto.getEffectPic() > 0) {

                    picIdList.add(houseDto.getEffectPic());
                }

                if (null != houseDto.getEffetPlanIds() && houseDto.getEffetPlanIds() > 0) {

                    picIdList.add(houseDto.getEffetPlanIds());
                }

                if (null != houseDto.getPcModelU3dId() && houseDto.getPcModelU3dId() > 0) {

                    modelIdList.add(houseDto.getPcModelU3dId());
                }

                if (null != houseDto.getConfigFileId() && houseDto.getConfigFileId() > 0) {

                    fileIdList.add(houseDto.getConfigFileId());
                }
            });

            long picSize = 0;
            if (null != picIdList && picIdList.size() > 0) {

                picSize = this.getPicSource(picIdList,dtoSearch);
            }

            long modelSize = 0;
            if (null != modelIdList && modelIdList.size() > 0) {

                dtoSearch.setModelIdList(modelIdList);
                modelSize = resourceStatisticsDao.selectModelSource(dtoSearch);
            }

            long fileSize = 0;
            if (null != fileIdList && fileIdList.size() > 0) {
                dtoSearch.setFileIdList(fileIdList);
                fileSize = resourceStatisticsDao.selectHouseFileSource(dtoSearch);
            }

            size = picSize + modelSize + fileSize;
        }

        return size;
    }


    private Long getHouseSpaceSource(ResourceStatisticsDto dto){
        long size = 0;
        ResourceStatisticsDto dtoSearch = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,dtoSearch);

        List<HouseDto> houseDtoList = resourceStatisticsDao.listHouseSpace(dtoSearch);

        if (null != houseDtoList && houseDtoList.size() > 0) {

            List<Integer> picIdList = new ArrayList<>();
            List<Integer> modelIdList = new ArrayList<>();

            houseDtoList.stream().forEach(houseDto -> {

                if (null != houseDto.getPicId() && houseDto.getPicId() > 0) {

                    picIdList.add(houseDto.getPicId());
                }

                if (null != houseDto.getDaylightU3dModelId() && houseDto.getDaylightU3dModelId() > 0) {

                    modelIdList.add(houseDto.getDaylightU3dModelId());
                }

                if (null != houseDto.getDusklightU3dModelId() && houseDto.getDusklightU3dModelId() > 0) {

                    modelIdList.add(houseDto.getDusklightU3dModelId());
                }

                if (null != houseDto.getNightlightU3dModelId() && houseDto.getNightlightU3dModelId() > 0) {

                    modelIdList.add(houseDto.getNightlightU3dModelId());
                }
            });

            long picSize = 0;
            if (null != picIdList && picIdList.size() > 0) {

                picSize = this.getPicSource(picIdList,dtoSearch);
            }

            long modelSize = 0;
            if (null != modelIdList && modelIdList.size() > 0) {

                dtoSearch.setModelIdList(modelIdList);
                modelSize = resourceStatisticsDao.selectModelSource(dtoSearch);
            }

            size = picSize + modelSize;
        }

        return size;
    }

    private Long getHouseSource(ResourceStatisticsDto dto){
        long size = 0;
        ResourceStatisticsDto dtoSearch = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,dtoSearch);

        List<HouseDto> houseDtoList = resourceStatisticsDao.listHouse(dtoSearch);

        if (null != houseDtoList && houseDtoList.size() > 0) {

            List<Integer> picIdList = new ArrayList<>();
            List<Integer> fileIdList = new ArrayList<>();

            houseDtoList.stream().forEach(houseDto -> {

                if (null != houseDto.getPicRes1Id() && houseDto.getPicRes1Id() > 0) {

                    picIdList.add(houseDto.getPicRes1Id());
                }

                if (null != houseDto.getPicRes2Id() && houseDto.getPicRes2Id() > 0) {

                    picIdList.add(houseDto.getPicRes2Id());
                }

                if (null != houseDto.getPicRes3Id() && houseDto.getPicRes3Id() > 0) {

                    picIdList.add(houseDto.getPicRes3Id());
                }

                if (null != houseDto.getPicRes4Id() && houseDto.getPicRes4Id() > 0) {

                    picIdList.add(houseDto.getPicRes4Id());
                }

                if (null != houseDto.getPicResId() && houseDto.getPicResId() > 0) {

                    picIdList.add(houseDto.getPicResId());
                }

                if (null != houseDto.getFullHouseObjId() && houseDto.getFullHouseObjId() > 0) {

                    fileIdList.add(houseDto.getFullHouseObjId());
                }

            });

            long picSize = 0;
            if (null != picIdList && picIdList.size() > 0) {
                picSize = this.getPicSource(picIdList,dtoSearch);
            }

            long fileSize = 0;
            if (null != fileIdList && fileIdList.size() > 0) {
                dtoSearch.setFileIdList(fileIdList);
                fileSize = resourceStatisticsDao.selectHouseFileSource(dtoSearch);
            }

            size = picSize + fileSize;
        }

        return size;
    }

    /** 计算图片主图、缩略图资源 */
    private Long getPicSource(List<Integer> picIdList,ResourceStatisticsDto dto){

        long size = 0;

        if (null != picIdList && picIdList.size() > 0) {

            List<Integer> picAllIdList = new ArrayList<>();
            List<PicDto> picDtoAllList = new ArrayList<>();
            List<Integer> idSearchList = new ArrayList<>();

            ResourceStatisticsDto dtoSearch = new ResourceStatisticsDto();
            BeanUtils.copyProperties(dto,dtoSearch);
            picAllIdList.addAll(picIdList);


            int sum = 0;
            int count = 0;

            while (sum < picIdList.size()) {

                if (count == 500 || sum == picIdList.size()) {

                    dtoSearch.setPicIdList(idSearchList);
                    // 查询图片主图
                    List<PicDto> picDtoList = resourceStatisticsDao.listPidPic(dtoSearch);

                    if (null != picDtoList && picDtoList.size() > 0) {
                        picDtoAllList.addAll(picDtoList);
                    }

                    count = 0;
                    idSearchList = new ArrayList<>();
                }

                idSearchList.add(picIdList.get(sum));
                count++;
                sum++;
            }

            if (null != picAllIdList && picAllIdList.size() > 0 ) {

                for (PicDto picDto : picDtoAllList) {

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
