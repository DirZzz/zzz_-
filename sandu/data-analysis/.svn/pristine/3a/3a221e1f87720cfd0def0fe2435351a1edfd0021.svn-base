package com.sandu.analysis.biz.tob.plan.offline;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.tob.plan.dao.PlanAnalysisResultDao;
import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanDao;
import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanStatisticsHourDao;
import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanUsageDao;
import com.sandu.analysis.biz.tob.plan.model.*;
import com.sandu.analysis.biz.util.DBUtils;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-14 10:37
 * Desc:
 */
public class RecommendedPlanAnalysisResult {

    public static void main(String[] args) {
        // ==========spark上下文初始化 ->start
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("PageAnalysis");
        sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
        System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);
        @SuppressWarnings("resource")
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        // ==========spark上下文初始化 ->end

        // ==========获取与要处理的hdfs文件路径 ->start
        LocalDate localDate = Utils.getLocalDate(args);
        String dataDirInfo = localDate.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));

        String hdfsFileDir = null;
        if(ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV)) {
            hdfsFileDir = "C:/Users/Administrator/Desktop/hdfs/1";
        } else {
            hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_EVENTS_DIR + dataDirInfo;
            // 检测hdfs中有没有这个目录, 如果没有则创建目录
            boolean isExist = HadoopUtils.getIsExist(hdfsFileDir);
            if(!isExist) {
                System.out.println("warn, function = BtnClickAnalysis.main, message = hdfs中对应目录不存在, dir = " + hdfsFileDir);
                return;
            }
        }
        System.out.println("------hdfsFileDir = " + hdfsFileDir);
        // ==========获取与要处理的hdfs文件路径 ->end

        // ==========获取用户渠道信息 ->start
        // key = uuid, value = channel
        Map<String, String> channelInfoMap = DBUtils.getChannelInfoMap();
        // ==========获取用户渠道信息 ->end

        JavaRDD<String> javaRDD = javaSparkContext.textFile(hdfsFileDir);
        //0		1	                    2	                3				4						                5
        //54	2019-06-09 22:07:32	2019-06-09 22:05:03	serviceEvent	event:recommendedPlanUse,planId:485629	    pc2b
        //uuid	?	                    ?	                事件类型	      事件：方案使用,方案ID：11	                平台
        // 过了格式不正确/不需要的log
        javaRDD = javaRDD.filter(str -> {
            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
            // log 格式过滤
            if(AnalysisConstants.LOG_SPLIT_LENGTH != strs.length) {
                return false;
            }
            // 事件类型过滤
            if( !"serviceEvent".equals(strs[3]) ){
                return false;
            }
            // 事件过滤
            Map<String, String> eventMap = Utils.getMap(strs[4]);
            if( !"recommendedPlanUse".equals(eventMap.get(AnalysisConstants.EVENTPROPERTY_ENUM.event.getName())) ){
                return false;
            }
            // 平台过滤
            if( !AnalysisConstants.PLATFORM_CODE_2B_MAP.containsKey(strs[5]) ){
                return false;
            }
            return true;
        })
        .map(str -> {
            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
            Map<String, String> eventMap = Utils.getMap(strs[4]);
            //          0        1       2
            // newKey:dateTime plaId platformCode
            return strs[1] + AnalysisConstants.SPLIT_REGEX + eventMap.get("planId") + AnalysisConstants.SPLIT_REGEX + strs[5];
        })
        .cache();

        // 热门方案统计
        recommendedPlanAnalysisResult(javaRDD, localDate);

        // 方案数据统计(每小时)
        planAnalysisResultForHour(javaRDD, localDate);

        // 方案数据统计(每天)
        disposePlanDataByDay(javaRDD, localDate);

    }

    /**
     * 方案数据统计(每小时)
     */
    public static void planAnalysisResultForHour(JavaRDD<String> javaRDD, LocalDate localDate){
        // 0        1       2
        // dateTime plaId platformCode
        List<Integer> ids = javaRDD.map(str -> {
            String[] arr = str.split(AnalysisConstants.SPLIT_REGEX);
            return Integer.valueOf(arr[1]);
        }).collect();

        RecommendedPlanUsageDao dao = DaoFactory.getRecommendedPlanUsageDao();
        // 查询方案其他的数据
        List<RecommendedPlanUsageDto> planInfos = dao.selectPlanInfoByIds(ids);
        Map<String, RecommendedPlanUsageDto> planInfoMap = new HashMap<>();
        List<Integer> spaceIds = new ArrayList<>();
        if( CollectionUtils.isNotEmpty(planInfos) ) {
            for (RecommendedPlanUsageDto planInfo : planInfos) {
                planInfoMap.put(planInfo.getPlanId().toString(), planInfo);
                spaceIds.add(planInfo.getSpaceCommonId());
            }
        }

        // 查询空间类型
        List<SpaceCommonTypeDto> spaceCommonTypeDtos = dao.selectSpaceTypeByIds(spaceIds);
        Map<Integer, Integer> spaceCommonTypeMap = new HashMap<>();
        if( CollectionUtils.isNotEmpty(spaceCommonTypeDtos) ) {
            for (SpaceCommonTypeDto dto : spaceCommonTypeDtos) {
                spaceCommonTypeMap.put(dto.getSpaceCommonId(), dto.getSpaceCommonType());
            }
        }

        // 查询新增方案数量
        RecommendedPlanStatisticsHourDao recommendedPlanStatisticsHourDao = DaoFactory.getRecommendedPlanStatisticsHourDao();
        List<RecommendedPlanStatisticsHourDto> recommendedPlanStatisticsHourDtos = recommendedPlanStatisticsHourDao.selectNewPlanCount(localDate);
        Map<String, RecommendedPlanStatisticsHourDto> newPlanCountMap = new HashMap<>();
        if( recommendedPlanStatisticsHourDtos != null ) {
            recommendedPlanStatisticsHourDtos.stream().forEach(dto -> {
                // key:perTime planType designStyleId spaceCommonType companyId planSource
                newPlanCountMap.put(dto.getPerHour() + AnalysisConstants.SPLIT_REGEX +
                                dto.getPlanType() + AnalysisConstants.SPLIT_REGEX +
                                dto.getDesignStyleId() + AnalysisConstants.SPLIT_REGEX +
                                dto.getSpaceCommonType() + AnalysisConstants.SPLIT_REGEX +
                                dto.getCompanyId() + AnalysisConstants.SPLIT_REGEX +
                                dto.getPlanSource()
                        , dto);
            });
        }


        Map<String, String> resultMap = javaRDD.mapToPair(str -> {
            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
            String planId = strs[1];
            RecommendedPlanUsageDto dto = planInfoMap.get(planId);
            Map<String, String> timeRange = Utils.getBetweenHour(strs[0]);
            // pcCount,mobileCount
            String value = "0,0";
            if( AnalysisConstants.PLATFORM_CODE_ENUM.pc2b.name().equals(strs[2]) ){
                value = "1,0";
            }else if( AnalysisConstants.PLATFORM_CODE_ENUM.mobile2b.name().equals(strs[2]) ){
                value = "0,1";
            }
            //       0          1       2       3               4               5       6           7
            // key:startTime endTime planType designStyleId spaceCommonType companyId planSource platform
            return new Tuple2<String, String>(timeRange.get("startTime") + AnalysisConstants.SPLIT_REGEX + timeRange.get("endTime") + AnalysisConstants.SPLIT_REGEX
                    + dto.getPlanType() + AnalysisConstants.SPLIT_REGEX + dto.getDesignStyleId() + AnalysisConstants.SPLIT_REGEX
                    + spaceCommonTypeMap.get(dto.getSpaceCommonId()) + AnalysisConstants.SPLIT_REGEX + dto.getCompanyId() + AnalysisConstants.SPLIT_REGEX + dto.getPlanSource()
                    + AnalysisConstants.SPLIT_REGEX + strs[2], value);
        }).reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String s, String s2) throws Exception {
                String[] arr1 = s.split(AnalysisConstants.SPLIT_COMMA);
                String[] arr2 = s2.split(AnalysisConstants.SPLIT_COMMA);
                Integer pcCount =  Integer.valueOf(arr1[0]) + Integer.valueOf(arr2[0]);
                Integer mobileCount =  Integer.valueOf(arr1[1]) + Integer.valueOf(arr2[1]);
                return pcCount + AnalysisConstants.SPLIT_COMMA + mobileCount;
            }
        }).collectAsMap();

        // 将日志数据转换成list
        List<RecommendedPlanStatisticsHourDto> dtos = new ArrayList<>();
        for( String key : resultMap.keySet() ){
            String[] arrs = key.split(AnalysisConstants.SPLIT_REGEX);
            String[] values = resultMap.get(key).split(AnalysisConstants.SPLIT_COMMA);
            // 新增方案数量 perTime planType designStyleId spaceCommonType companyId planSource
            Integer newPlanCount = 0;
            String newPlanCountKey = Utils.dateStrFormat(arrs[0], "yyyy-MM-dd HH") + AnalysisConstants.SPLIT_REGEX + arrs[2] + AnalysisConstants.SPLIT_REGEX
                    + arrs[3] + AnalysisConstants.SPLIT_REGEX + arrs[4] + AnalysisConstants.SPLIT_REGEX
                    + arrs[5] + AnalysisConstants.SPLIT_REGEX + arrs[6] + AnalysisConstants.SPLIT_REGEX;
            if( newPlanCountMap.containsKey(newPlanCountKey) ){
                newPlanCount = newPlanCountMap.get(newPlanCountKey).getNewPlanCount();
                newPlanCountMap.remove(newPlanCountKey);
            }

            RecommendedPlanStatisticsHourDto newDto = RecommendedPlanStatisticsHourDto.builder().startTime(arrs[0]).endTime(arrs[1]).newPlanCount(newPlanCount)
                    .planType(arrs[2]==null?null:Integer.valueOf(arrs[2])).designStyleId(arrs[3]==null?null:Integer.valueOf(arrs[3]))
                    .spaceCommonType(arrs[4]==null?null:Integer.valueOf(arrs[4])).companyId(arrs[5]==null?null:Integer.valueOf(arrs[5]))
                    .planSource(arrs[6]).pcUsePlanCount(Integer.valueOf(values[0])).mobileUsePlanCount(Integer.valueOf(values[1]))
                    .creator("sys").gmtCreate(Utils.date2str(new Date())).modifier("sys").gmtModified(Utils.date2str(new Date())).isDeleted(0)
                    .build();
            dtos.add(newDto);
        }

        // 把剩余还没有被使用的新增方案单独加上
        if( newPlanCountMap != null ){
            RecommendedPlanStatisticsHourDto planCountDto = null;
            for( String key : newPlanCountMap.keySet() ){
                planCountDto = newPlanCountMap.get(key);
                Map<String, String> timeRange = Utils.getBetweenHour(planCountDto.getGmtCreate());
                RecommendedPlanStatisticsHourDto newDto = RecommendedPlanStatisticsHourDto.builder().startTime(timeRange.get("startTime")).endTime(timeRange.get("endTime"))
                        .newPlanCount(planCountDto.getNewPlanCount()).planType(planCountDto.getPlanType()).spaceCommonType(planCountDto.getSpaceCommonType())
                        .planSource(planCountDto.getPlanSource()).designStyleId(planCountDto.getDesignStyleId()).companyId(planCountDto.getCompanyId())
                        .pcUsePlanCount(0).mobileUsePlanCount(0)
                        .creator("sys").gmtCreate(Utils.date2str(new Date())).modifier("sys").gmtModified(Utils.date2str(new Date())).isDeleted(0)
                        .build();
                dtos.add(newDto);
            }
        }

        //先删除，再插入
        recommendedPlanStatisticsHourDao.insertAfterDelete(dtos, localDate);

    }

    /**
     * 热门方案统计
     * @param javaRDD
     * @param args
     */
    public static void recommendedPlanAnalysisResult(JavaRDD<String> javaRDD, LocalDate localDate){
        // 0        1       2
        // dateTime plaId platformCode
        Map<String, String> resultMap = javaRDD.mapToPair(str -> {
            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
            // pcCount,mobileCount
            String value = "0,0";
            if( AnalysisConstants.PLATFORM_CODE_ENUM.pc2b.name().equals(strs[2]) ){
                value = "1,0";
            }else if( AnalysisConstants.PLATFORM_CODE_ENUM.mobile2b.name().equals(strs[2]) ){
                value = "0,1";
            }
            return new Tuple2<String, String>(strs[0] + AnalysisConstants.SPLIT_REGEX + strs[1], value);
        }).reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String s, String s2) throws Exception {
                String[] arr1 = s.split(AnalysisConstants.SPLIT_COMMA);
                String[] arr2 = s2.split(AnalysisConstants.SPLIT_COMMA);
                Integer pcCount =  Integer.valueOf(arr1[0]) + Integer.valueOf(arr2[0]);
                Integer mobileCount =  Integer.valueOf(arr1[1]) + Integer.valueOf(arr2[1]);
                return pcCount + AnalysisConstants.SPLIT_COMMA + mobileCount;
            }
        }).collectAsMap();

        List<Integer> ids = resultMap.keySet().stream().map(str -> {
            String[] arr = str.split(AnalysisConstants.SPLIT_REGEX);
            return Integer.valueOf(arr[1]);
        }).collect(Collectors.toList());

        RecommendedPlanUsageDao dao = DaoFactory.getRecommendedPlanUsageDao();

        // 查询方案其他的数据
        List<RecommendedPlanUsageDto> planInfos = dao.selectPlanInfoByIds(ids);
        Map<String, RecommendedPlanUsageDto> planInfoMap = new HashMap<>();
        List<Integer> spaceIds = new ArrayList<>();
        if( CollectionUtils.isNotEmpty(planInfos) ) {
            for (RecommendedPlanUsageDto planInfo : planInfos) {
                planInfoMap.put(planInfo.getPlanId().toString(), planInfo);
                spaceIds.add(planInfo.getSpaceCommonId());
            }
        }

        // 查询空间类型
        List<SpaceCommonTypeDto> spaceCommonTypeDtos = dao.selectSpaceTypeByIds(spaceIds);
        Map<Integer, Integer> spaceCommonTypeMap = new HashMap<>();
        if( CollectionUtils.isNotEmpty(spaceCommonTypeDtos) ) {
            for (SpaceCommonTypeDto dto : spaceCommonTypeDtos) {
                spaceCommonTypeMap.put(dto.getSpaceCommonId(), dto.getSpaceCommonType());
            }
        }

        // 将日志数据转成list
        List<RecommendedPlanUsageDto> dtos = new ArrayList<>();
        RecommendedPlanUsageDto dto = null;
        for( String key : resultMap.keySet() ){
            String value = resultMap.get(key);
            String[] keyArr = key.split(AnalysisConstants.SPLIT_REGEX);
            String startTime = Utils.getIntradayStartTime(keyArr[0], null);
            String endTime = Utils.getDateAfter(startTime, null, 1);
            String[] valueArr = value.split(AnalysisConstants.SPLIT_COMMA);
            dto = planInfoMap.get(keyArr[1]);
            if( dto == null ){
                continue;
            }
            Integer spaceCommonType = spaceCommonTypeMap.get(dto.getSpaceCommonId());
            RecommendedPlanUsageDto newDto = RecommendedPlanUsageDto.builder().startTime(startTime).endTime(endTime)
                    .planId(dto.getPlanId()).planName(dto.getPlanName()).planCode(dto.getPlanCode()).planType(dto.getPlanType()).planSource(dto.getPlanSource())
                    .designStyleId(dto.getDesignStyleId()).companyId(dto.getCompanyId()).spaceCommonType(spaceCommonType)
                    .planUsageAmountPc(Integer.valueOf(valueArr[0])).planUsageAmountMobile2b(Integer.valueOf(valueArr[1]))
                    .creator("sys").gmtCreate(Utils.date2str(new Date())).modifier("sys").gmtModified(Utils.date2str(new Date())).isDeleted(0).build();
            dtos.add(newDto);
        }

        //先删除，再插入
        dao.insertAfterDelete(dtos, localDate);
    }

    private static void disposePlanDataByDay(JavaRDD<String> javaRDD, LocalDate localDate) {

        //planId集合
        List<String> planIdList = javaRDD.map(strs -> {
            String[] planIdArr = strs.split(AnalysisConstants.SPLIT_REGEX);
            return planIdArr[1];
        }).filter(str -> StringUtils.isNotBlank(str)).collect();

        //获取过滤信息
        RecommendedPlanDao resultDao = DaoFactory.getRecommendedPlanDao();
        List<RecommendedPlanDto> planList = resultDao.selectRecommendedPlanList(planIdList);
        if( planList == null ){
            return;
        }

        //转换map key=planId,value=RecommendedPlanDto
        Map<Long, RecommendedPlanDto> planInfoDtoMap = planList.stream().collect(Collectors.toMap(RecommendedPlanDto::getId, a -> a, (k1, k2) -> k1));
        if (null == planInfoDtoMap || planInfoDtoMap.size() == 0) {
            System.out.println("planIdList : " + planIdList);
            return;
        }
        //同一个类型使用多次就需要记录多次
        javaRDD = javaRDD.map(str -> {
            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
            Long planId = Long.valueOf(strs[1]);
            //获取过滤信息  /除去planId便于统计
            StringBuilder builder = new StringBuilder("");
            if (planInfoDtoMap.containsKey(planId)) {
                RecommendedPlanDto planDto = planInfoDtoMap.get(planId);
                builder.append(planDto.getRecommendedType()).append(AnalysisConstants.SPLIT_REGEX);
                builder.append(planDto.getStyleId()).append(AnalysisConstants.SPLIT_REGEX);
                builder.append(planDto.getSpaceType()).append(AnalysisConstants.SPLIT_REGEX);
                builder.append(planDto.getCompanyId()).append(AnalysisConstants.SPLIT_REGEX);
                builder.append(planDto.getPlanSource());

            }
            if (StringUtils.isBlank(builder.toString())) {
                return null;
            }
            return builder.toString();
        }).cache();

        System.out.println("javaRDD : " + javaRDD.collect());

        //根据过滤信息分组获取使用量
        Map<String, Long> planDayResultMap = javaRDD
                .mapToPair(str -> {
                    return new Tuple2<String, Long>(str, 1L);
                }).reduceByKey((s1, s2) -> s1 + s2)
                .collectAsMap();
        System.out.println("planDayResultMap = " + planDayResultMap.size());

        //插入数据
        insertByDay(planDayResultMap, localDate);
    }

    private static void insertByDay(Map<String, Long> planDayResultMap, LocalDate localDate) {

        if (planDayResultMap == null || planDayResultMap.size() == 0) {
            System.out.println("warn, function = PlanAnalysis.insertByDay, message: planDayResultMap is empty");
            return;
        }

        Date nowTime = new Date();
        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        // ==========从db查询新增方案数据数 ->start
        RecommendedPlanDao recommendedPlanDao = DaoFactory.getRecommendedPlanDao();
        List<RecommendedPlanDto> newPlanList = recommendedPlanDao.selectNewRecommendedPlan(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());

        Map<String, RecommendedPlanDto> map = new HashMap<>(newPlanList.size());
        for (RecommendedPlanDto planDto : newPlanList) {
            StringBuilder builder = new StringBuilder("");
            builder.append(planDto.getRecommendedType()).append(AnalysisConstants.SPLIT_REGEX);
            builder.append(planDto.getStyleId()).append(AnalysisConstants.SPLIT_REGEX);
            builder.append(planDto.getSpaceType()).append(AnalysisConstants.SPLIT_REGEX);
            builder.append(planDto.getCompanyId()).append(AnalysisConstants.SPLIT_REGEX);
            builder.append(planDto.getPlanSource());

            map.put(builder.toString(), planDto);
        }
        // ==========从db查询新增方案数据数 ->end

        List<PlanAnalysisResultDto> list = new ArrayList<PlanAnalysisResultDto>();
        for (String key : planDayResultMap.keySet()) {
            PlanAnalysisResultDto planAnalysisResultDto = new PlanAnalysisResultDto();

            Integer newPlanCount = 0;

            if (map != null && map.size() != 0 && map.containsKey(key)) {
                RecommendedPlanDto planDto = map.get(key);
                newPlanCount = planDto.getNewPlanCount();
                map.remove(key);
            }

            Integer value = planDayResultMap.get(key).intValue();
            String[] keyArr = key.split(AnalysisConstants.SPLIT_REGEX);
            Integer recommendedType = Integer.valueOf(keyArr[0]);
            Integer styleId = Integer.valueOf(keyArr[1]);
            Integer spaceType = Integer.valueOf(keyArr[2]);
            Integer companyId = Integer.valueOf(keyArr[3]);
            String planSource = keyArr[4];

            planAnalysisResultDto.setPlanType(recommendedType);
            planAnalysisResultDto.setDesignStyleId(styleId);
            planAnalysisResultDto.setSpaceCommonType(spaceType);
            planAnalysisResultDto.setCompanyId(companyId);
            planAnalysisResultDto.setPlanSource(planSource);
            planAnalysisResultDto.setNewPlanCount(newPlanCount);
            planAnalysisResultDto.setUsePlanCount(value);
            planAnalysisResultDto.setStartTime(startTime);
            planAnalysisResultDto.setEndTime(endTime);
            planAnalysisResultDto.setIsDeleted(0);
            planAnalysisResultDto.setGmtCreate(nowTime);
            planAnalysisResultDto.setGmtModified(nowTime);
            planAnalysisResultDto.setCreator(CommonConstants.CREATOR_SYSTEM);
            planAnalysisResultDto.setModifier(CommonConstants.CREATOR_SYSTEM);
            planAnalysisResultDto.setRemark("");

            list.add(planAnalysisResultDto);
        }
        if (map.size() != 0 && map != null) {
            for (String key : map.keySet()) {
                Integer newPlanCount = 0;

                RecommendedPlanDto planDto = map.get(key);
                newPlanCount=planDto.getNewPlanCount();
                PlanAnalysisResultDto planAnalysisResultDto = new PlanAnalysisResultDto();
                planAnalysisResultDto.setPlanType(planDto.getRecommendedType());
                planAnalysisResultDto.setDesignStyleId(planDto.getStyleId());
                planAnalysisResultDto.setSpaceCommonType(planDto.getStyleId());
                planAnalysisResultDto.setCompanyId(planDto.getCompanyId());
                planAnalysisResultDto.setPlanSource(planDto.getPlanSource());
                planAnalysisResultDto.setNewPlanCount(newPlanCount);
                planAnalysisResultDto.setUsePlanCount(0);
                planAnalysisResultDto.setStartTime(startTime);
                planAnalysisResultDto.setEndTime(endTime);
                planAnalysisResultDto.setIsDeleted(0);
                planAnalysisResultDto.setGmtCreate(nowTime);
                planAnalysisResultDto.setGmtModified(nowTime);
                planAnalysisResultDto.setCreator(CommonConstants.CREATOR_SYSTEM);
                planAnalysisResultDto.setModifier(CommonConstants.CREATOR_SYSTEM);
                planAnalysisResultDto.setRemark("");
                list.add(planAnalysisResultDto);
            }
        }

        PlanAnalysisResultDao resultDao = DaoFactory.getPlanAnalysisResultDao();
        PlanAnalysisResultQuery query = new PlanAnalysisResultQuery();
        query.setStartTime(startTime);
        query.setEndTime(endTime);
        //添加数据之前先删除
        resultDao.deleteByDay(query);
        resultDao.insertByDay(list);
    }
}
