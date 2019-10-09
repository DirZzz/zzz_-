package com.sandu.service.statistics.impl;

import com.sandu.api.statistics.dto.*;
import com.sandu.api.statistics.model.CompanyResourceStatistics;
import com.sandu.api.statistics.model.ResourceStatisticsDeviceLog;
import com.sandu.api.statistics.model.UserResourceStatistics;
import com.sandu.api.statistics.service.ResourceStatisticsService;
import com.sandu.api.statistics.service.ResourceStatisticsServiceEnum;
import com.sandu.common.util.date.DateUtil;
import com.sandu.service.statistics.dao.CompanyResourceStatisticsDao;
import com.sandu.service.statistics.dao.ResourceStatisticsDao;
import com.sandu.service.statistics.dao.ResourceStatisticsDeviceLogDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 企业资源统计
 * @author: chenqiang
 * @create: 2019-05-27 14:45
 */
@Service(value = "companyResourceStatisticsServiceImpl")
@Slf4j
public class CompanyResourceStatisticsServiceImpl implements ResourceStatisticsService{

    @Autowired
    private ResourceStatisticsDao resourceStatisticsDao;

    @Autowired
    private CompanyResourceStatisticsDao companyResourceStatisticsDao;

    @Autowired
    private ResourceStatisticsDeviceLogDao resourceStatisticsDeviceLogDao;

    @Override
    public void resourceStatistics() throws Exception{

        log.info("企业资源统计开始");

        // 查看是否有强制更新计划
        ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
        logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.company.name());
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
                    resourceStatisticsTask(localDateTime,ResourceStatisticsDeviceLog.deviceTypeEnum.force.name());

                    // 下一天
                    localDateTime = DateUtil.plus(localDateTime,1,ChronoUnit.DAYS);
                }

                // 更改计划状态
                resourceStatisticsDeviceLog.setStatus(2);
                resourceStatisticsDeviceLog.setGmtModified(new Date());
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

                        log.error("调用企业实现类统计资源详情 异常",e);
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

        // 获取所有企业
        List<CompanyDto> companyDtoList = resourceStatisticsDao.listCompany(dtoSerach);

        if (null != companyDtoList && companyDtoList.size() > 0) {

            int sum = companyDtoList.size();
            int count = 1;

            if (sum > ResourceStatisticsDto.LIMIT_SIZE) {

                count = ((int)Math.ceil((double)sum/(double)ResourceStatisticsDto.LIMIT_SIZE));
            }

            // 查询是否存在当前时间计划日志
            ResourceStatisticsDeviceLog logSearch = new ResourceStatisticsDeviceLog();
            logSearch.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.company.name());
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
                resourceStatisticsDeviceLog.setLogType(ResourceStatisticsDeviceLog.logTypeEnum.company.name());
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

                companyDtoList = resourceStatisticsDao.listCompany(dtoSerach);

                final List<CompanyDto> companyDtoListParam = companyDtoList;

                pool.submit(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            // 统计当天资源信息
                            resourceStatisticsDetails(dateTime,ResourceStatisticsDeviceLog.deviceTypeEnum.time.name(),companyDtoListParam);

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

    private void resourceStatisticsDetails(final LocalDateTime dateTime,String deviceType,final List<CompanyDto> companyDtoList) throws Exception{

        LocalDateTime localDateTime = dateTime;
        CompanyResourceStatistics companyResourceStatistics = null;

        ResourceStatisticsDto dtoSerach = new ResourceStatisticsDto();
        dtoSerach.setStartDate(DateUtil.getDayStart(localDateTime));
        dtoSerach.setEndDate(DateUtil.getDayEnd(localDateTime));

        if (null != companyDtoList && companyDtoList.size() > 0) {

            int date = Integer.parseInt(DateUtil.formatDateTime(localDateTime,DateUtil.DATE_PATTERN).replaceAll("-",""));
            int year = localDateTime.getYear();
            int month = localDateTime.getMonthValue();
            int week = DateUtil.getWeekByTime(localDateTime);
            int day = localDateTime.getDayOfMonth();

            log.info("企业资源统计开始-时间="+date);

            for (CompanyDto companyDto : companyDtoList) {

                companyResourceStatistics = new CompanyResourceStatistics();
                List<Integer> brandIdList = null;
                dtoSerach.setCompanyId(companyDto.getId());
                if (StringUtils.isBlank(companyDto.getBrandIds())) {
                    brandIdList = resourceStatisticsDao.listBrand(companyDto.getId());
                } else {
                    brandIdList = Arrays.stream(companyDto.getBrandIds().split(",")).filter(s -> StringUtils.isNotBlank(s)).map(s -> Integer.parseInt(s)).collect(Collectors.toList());
                }
                dtoSerach.setBrandIdList(brandIdList);

                // 获取企业方案渲染资源
                Long planRenderSource = resourceStatisticsDao.selectCompanyPlanRenderSource(dtoSerach);

                // 获取企业方案配置资源
                Long planConfigSource = resourceStatisticsDao.selectCompanyPlanConfigSource(dtoSerach);

                // 获取企业共享方案渲染资源
                Long sharePlanRenderSource = resourceStatisticsDao.selectCompanySharePlanRenderSource(dtoSerach);

                // 获取企业共享方案配置资源
                Long sharePlanConfigSource = resourceStatisticsDao.selectCompanySharePlanConfigSource(dtoSerach);

                // 获取企业材质资源
                Long textrueSource = this.getCompanyTextureSource(dtoSerach);

                // 获取企业贴图产品资源
                Long stickChartSource = this.getCompanyProductPicSource(dtoSerach,1);

                // 获取企业模型产品 模型资源
                Long modelSource = this.getCompanyModelSource(dtoSerach);

                // 获取企业模型产品 图片资源
                Long productPicSource = this.getCompanyProductPicSource(dtoSerach,0);

                // 获取企业店铺资源
                Long companyShopSource = this.getCompanyShopSize(dtoSerach);

                // 获取企业店铺博文资源
                Long companyShopArticleSize = this.getCompanyShopArticleSize(dtoSerach);

                // 获取企业店铺工程案例资源
                Long companyShopCaseSize = this.getcompanyShopCaseSize(dtoSerach);

                // 获取企业账号资源
                Long companyUserSource = this.getPicSource(resourceStatisticsDao.listCompanyUserList(dtoSerach),dtoSerach);

                // 获取企业资源
                Long companySource = resourceStatisticsDao.selectCompanySource(dtoSerach);

                // 获取企业门店资源
                if (2 == companyDto.getBusinessType()) {
                    dtoSerach.setClaimCompanyId(companyDto.getId());
                    dtoSerach.setCompanyId(companyDto.getPid());
                }
                long companyShopOfflineSize = this.getCompanyShopOfflineSize(dtoSerach);

                // 新增资源信息数据
                companyResourceStatistics.setCompanyId(companyDto.getId());
                companyResourceStatistics.setPlanRender(planRenderSource + "");
                companyResourceStatistics.setPlanConfig(planConfigSource + "");
                companyResourceStatistics.setSharePlanConfig(sharePlanConfigSource + "");
                companyResourceStatistics.setSharePlanRender(sharePlanRenderSource + "");
                companyResourceStatistics.setTexture(textrueSource + "");
                companyResourceStatistics.setStickChartProduct(stickChartSource + "");
                companyResourceStatistics.setProductModel(modelSource + "");
                companyResourceStatistics.setProductPic(productPicSource + "");
                companyResourceStatistics.setShop(companyShopSource + "");
                companyResourceStatistics.setShopBowen(companyShopArticleSize + "");
                companyResourceStatistics.setShopEngineering(companyShopCaseSize + "");
                companyResourceStatistics.setUser(companyUserSource + "");
                companyResourceStatistics.setCompany(companySource + "");
                companyResourceStatistics.setStore(companyShopOfflineSize + "");
                companyResourceStatistics.setDate(date);
                companyResourceStatistics.setDay(day);
                companyResourceStatistics.setWeek(week);
                companyResourceStatistics.setMonth(month);
                companyResourceStatistics.setYear(year);
                companyResourceStatistics.setSysCode(UUID.randomUUID().toString().replace("-",""));
                companyResourceStatistics.setCreator("system");
                companyResourceStatistics.setGmtCreate(new Date());
                companyResourceStatistics.setModifier("system");
                companyResourceStatistics.setGmtModified(new Date());
                companyResourceStatistics.setIsDeleted(0);


                // 查询是否存在资源信息
                CompanyResourceStatistics companySearch = new CompanyResourceStatistics();
                companySearch.setCompanyId(companyDto.getId());
                companySearch.setDate(date);
                List<CompanyResourceStatistics> companyResourceStatisticsList = companyResourceStatisticsDao.selectList(companySearch);
                if (null != companyResourceStatisticsList && companyResourceStatisticsList.size() > 0) {

                    companyResourceStatistics.setId(companyResourceStatisticsList.get(0).getId());
                    companyResourceStatisticsDao.updateByPrimaryKeySelective(companyResourceStatistics);
                } else {

                    companyResourceStatisticsDao.insertSelective(companyResourceStatistics);
                }

            }

        }

    }


    @Override
    public ResourceStatisticsServiceEnum getServiceImplKey() {
        return ResourceStatisticsServiceEnum.companyResourceStatisticsServiceImpl;
    }

    /** 获取企业门店资源 */
    private Long getCompanyShopOfflineSize(ResourceStatisticsDto dto){
        long size = 0;

        ResourceStatisticsDto searchDto = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,searchDto);
        searchDto.setShopType(1);

        // 获取店铺封面、logo、介绍 资源id集合
        List<ShopResourceDto> shopResourceDtoList = resourceStatisticsDao.listCompanyOffline(searchDto);

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

            long companyShopPicSize = 0;
            if (null != picIdList && picIdList.size() > 0) {

                searchDto.setPicIdList(picIdList);
                companyShopPicSize = resourceStatisticsDao.selectShopPicSource(searchDto);
            }

            long companyShopFileSize = 0;
            if (null != fileIdList && fileIdList.size() > 0) {

                searchDto.setFileIdList(fileIdList);
                companyShopFileSize = resourceStatisticsDao.selectShopIntroducedSource(searchDto);
            }

            size = size + companyShopPicSize + companyShopFileSize;
        }

        return size;
    }

    /** 获取店铺资源 */
    private Long getCompanyShopSize(ResourceStatisticsDto dto){
        long size = 0;

        ResourceStatisticsDto searchDto = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,searchDto);
        searchDto.setShopType(1);

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

            long companyShopPicSize = 0;
            if (null != picIdList && picIdList.size() > 0) {

                searchDto.setPicIdList(picIdList);
                companyShopPicSize = resourceStatisticsDao.selectShopPicSource(searchDto);
            }

            long companyShopFileSize = 0;
            if (null != fileIdList && fileIdList.size() > 0) {

                searchDto.setFileIdList(fileIdList);
                companyShopFileSize = resourceStatisticsDao.selectShopIntroducedSource(searchDto);
            }

            size = size + companyShopPicSize + companyShopFileSize;
        }

        return size;
    }

    /** 获取店铺博文资源 */
    private Long getCompanyShopArticleSize(ResourceStatisticsDto dto){
        long size = 0;

        ResourceStatisticsDto searchDto = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,searchDto);
        searchDto.setShopType(1);

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
    private Long getcompanyShopCaseSize(ResourceStatisticsDto dto){
        long size = 0;

        ResourceStatisticsDto searchDto = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dto,searchDto);
        searchDto.setShopType(1);

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

            long companyShopCasePicSize = 0;
            if (null != picIdList && picIdList.size() > 0 ) {

                searchDto.setPicIdList(picIdList);
                companyShopCasePicSize = resourceStatisticsDao.selectShopPicSource(searchDto);
            }

            long companyShopCaseFileSize = 0;
            if (null != fileIdList && fileIdList.size() > 0) {

                searchDto.setFileIdList(fileIdList);
                companyShopCaseFileSize = resourceStatisticsDao.selectShopIntroducedSource(searchDto);
            }

            size = size + companyShopCasePicSize + companyShopCaseFileSize;
        }

        return size;
    }

    /** 获取产品图片资源 */
    private Long getCompanyProductPicSource(ResourceStatisticsDto dtoSerach,Integer isNotModel){

        long size = 0;
        ResourceStatisticsDto dtoProductPicSearch = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dtoSerach,dtoProductPicSearch);
        dtoProductPicSearch.setIsNotModel(isNotModel);

        // 查询产品图片信息
        List<ProductDto> productDtoList = resourceStatisticsDao.listProductPic(dtoProductPicSearch);


        if (null != productDtoList && productDtoList.size() > 0) {

            // 得到所有图片id
            List<Integer> picIdList = new ArrayList<>();

            productDtoList.stream().forEach(productDto -> {

                if (null != productDto.getPicId() && productDto.getPicId() > 0) {

                    picIdList.add(productDto.getPicId());
                }

                if (StringUtils.isNotBlank(productDto.getPicIds())) {

                    picIdList.addAll(Arrays.stream(productDto.getPicIds().split(",")).filter(s -> StringUtils.isNotBlank(s) && Integer.parseInt(s) > 0).map(s -> Integer.parseInt(s)).collect(Collectors.toList()));
                }

                if (StringUtils.isNotBlank(productDto.getMaterialPicIds())) {

                    picIdList.addAll(Arrays.stream(productDto.getMaterialPicIds().split(",")).filter(s -> StringUtils.isNotBlank(s) && Integer.parseInt(s) > 0).map(s -> Integer.parseInt(s)).collect(Collectors.toList()));
                }
            });

            // 计算图片资源
            size = this.getPicSource(picIdList,dtoSerach);
        }

        return size;
    }


    /** 获取模型资源 */
    private Long getCompanyModelSource(ResourceStatisticsDto dtoSerach){

        long size = 0;
        ResourceStatisticsDto dtoModelSearch = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dtoSerach,dtoModelSearch);

        // 获取模型资源
        size = resourceStatisticsDao.selectCompanyModelSource(dtoModelSearch);

        // 获取模型材质信息
        List<TextureDto> textureDtoList = resourceStatisticsDao.listCompanyModelTextrue(dtoModelSearch);

        size = size + this.getTextureSize(textureDtoList,dtoModelSearch);

        return size;
    }


    /** 获取企业材质资源 */
    private Long getCompanyTextureSource(ResourceStatisticsDto dtoSerach){
        long size = 0;
        ResourceStatisticsDto dtoTextureSearch = new ResourceStatisticsDto();
        BeanUtils.copyProperties(dtoSerach,dtoTextureSearch);

        size = resourceStatisticsDao.selectCompanyTextrueSource(dtoTextureSearch);

        // 获取企业材质信息
        List<TextureDto> textureDtoList = resourceStatisticsDao.listCompanyTextrue(dtoSerach);

        // 获取企业材质对应图片模型资源
        size = size + this.getTextureSize(textureDtoList,dtoTextureSearch);

        return size;
    }

    /** 计算材质图片模型资源 */
    private Long getTextureSize(List<TextureDto> textureDtoList,ResourceStatisticsDto dtoTextureSearch){

        long size = 0;

        if (null != textureDtoList && textureDtoList.size() > 0) {

            List<Integer> picIdList = null;
            List<Integer> modelIdList = null;

            for (TextureDto textureDto : textureDtoList) {

                picIdList = new ArrayList<>();
                modelIdList = new ArrayList<>();

                if (null != textureDto.getPicId() && textureDto.getPicId() > 0) {

                    picIdList.add(textureDto.getPicId());
                }

                if (null != textureDto.getNormalPicId() && textureDto.getNormalPicId() > 0) {

                    picIdList.add(textureDto.getNormalPicId());
                }

                if (null != textureDto.getTextureBallFileId() && textureDto.getTextureBallFileId() > 0) {

                    modelIdList.add(textureDto.getTextureBallFileId());
                }

                if (null != textureDto.getAndroidTextureBallFileId() && textureDto.getAndroidTextureBallFileId() > 0) {

                    modelIdList.add(textureDto.getAndroidTextureBallFileId());
                }

                if (null != textureDto.getIosTextureBallFileId() && textureDto.getIosTextureBallFileId() > 0) {

                    modelIdList.add(textureDto.getIosTextureBallFileId());
                }

                // 获取材质图片资源
                long picSource = this.getPicSource(picIdList,dtoTextureSearch);

                // 获取材质材质球资源
                long ballSource = 0;
                if (null != modelIdList && modelIdList.size() > 0) {

                    dtoTextureSearch.setModelIdList(modelIdList);
                    ballSource = resourceStatisticsDao.selectModelSource(dtoTextureSearch);
                }

                size = size + (null != textureDto.getFileSize() ? textureDto.getFileSize() : 0);
                size = size + picSource + ballSource;
            }
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
