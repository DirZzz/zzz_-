package com.sandu.analysis.biz.tob.plan.offline;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.tob.plan.dao.PlanAnalysisResultDao;
import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanDao;
import com.sandu.analysis.biz.tob.plan.model.PlanAnalysisResultDto;
import com.sandu.analysis.biz.tob.plan.model.PlanAnalysisResultQuery;
import com.sandu.analysis.biz.tob.plan.model.RecommendedPlanDto;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class PlanAnalysis {
    private static String className = "PlanAnalysis";

    public static void main(String[] args) {

        // ==========spark上下文初始化 ->start
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("PlanAnalysis");
        sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
        System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);

        @SuppressWarnings("resource")
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        // ==========spark上下文初始化 ->end

        // ==========获取与要处理的hdfs文件路径 ->start
        LocalDate localDate = Utils.getLocalDate(args);
        String dataDirInfo = localDate.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));

        String hdfsFileDir = null;
        if (ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV)) {
            hdfsFileDir = "C:/Users/Sandu/Desktop/hdfs/log.txt";
        } else {
            hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_EVENTS_DIR + dataDirInfo;
            // 检测hdfs中有没有这个目录, 如果没有则创建目录
            boolean isExist = HadoopUtils.getIsExist(hdfsFileDir);
            if (!isExist) {
                System.out.println("warn, function = PlanAnalysis.main, message = hdfs中对应目录不存在, dir = " + hdfsFileDir);
                return;
            }
        }
        System.out.println("------hdfsFileDir = " + hdfsFileDir);
        // ==========获取与要处理的hdfs文件路径 ->end

//           0           1                 2                    3              4                                                          5       6       7          8                9          10       11      12
//        234101	2019-06-13 19:21:51	2019-06-13 19:19:26	serviceEvent	event:recommendedPlanUse,planId:199030  	pc2b	java	1.0.0	192.168.1.225	192.168.3.124	中国	广东省	深圳市
        JavaRDD<String> javaRDD = javaSparkContext.textFile(hdfsFileDir)
                .filter(str -> {
                    String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
                    // log 格式过滤
                    if (AnalysisConstants.LOG_SPLIT_LENGTH != strs.length) {
                        return false;
                    }
                    // 内容过滤
                    if (!str.contains("event:recommendedPlanUse")) {
                        return false;
                    }
                    return true;
                })
                .map(str -> {
                    String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
                    String[] planId = strs[4].split("event:recommendedPlanUse,planId:");
                    return planId[1] + AnalysisConstants.SPLIT_REGEX + strs[1];
                })
                .cache();

        System.out.println("javaRDD======" + javaRDD.collect());

        disposePlanDataByDay(javaRDD, localDate);
//        disposePlanDataByHour(javaRDD, localDate);
    }


    private static void disposePlanDataByDay(JavaRDD<String> javaRDD, LocalDate localDate) {

        //planId集合
        List<String> planIdList = javaRDD.map(strs -> {
            String[] planIdArr = strs.split(AnalysisConstants.SPLIT_REGEX);
            return planIdArr[0];
        }).filter(str -> StringUtils.isNotBlank(str)).collect();

        //获取过滤信息
        RecommendedPlanDao resultDao = DaoFactory.getRecommendedPlanDao();
        List<RecommendedPlanDto> planList = resultDao.selectRecommendedPlanList(planIdList);

        //转换map key=planId,value=RecommendedPlanDto
        Map<Long, RecommendedPlanDto> planInfoDtoMap = planList.stream().collect(Collectors.toMap(RecommendedPlanDto::getId, a -> a, (k1, k2) -> k1));
        if (null == planInfoDtoMap || planInfoDtoMap.size() == 0) {
            System.out.println("planIdList : " + planIdList);
            return;
        }
        //同一个类型使用多次就需要记录多次
        javaRDD = javaRDD.map(str -> {
            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
            Long planId = Long.valueOf(strs[0]);
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


//    private static void disposePlanDataByHour(JavaRDD<String> javaRDD, LocalDate localDate) throws ParseException {
//        //planId集合
//        List<String> planIdList = javaRDD.map(strs -> {
//            String[] planIdArr = strs.split(AnalysisConstants.SPLIT_REGEX);
//            return planIdArr[0];
//        }).filter(str -> StringUtils.isNotBlank(str)).collect();
//
//        //获取过滤信息
//        RecommendedPlanDao resultDao = DaoFactory.getRecommendedPlanDao();
//        List<RecommendedPlanDto> planList = resultDao.selectRecommendedPlanList(planIdList);
//        //转换map key=planId,value=RecommendedPlanDto
//        Map<Long, RecommendedPlanDto> planInfoDtoMap = planList.stream().collect(Collectors.toMap(RecommendedPlanDto::getId, a -> a, (k1, k2) -> k1));
//        if (null == planInfoDtoMap || planInfoDtoMap.size() == 0) {
//            System.out.println("planIdList : " + planIdList);
//            return;
//        }
//        //同一个类型使用多次就需要记录多次
//        javaRDD = javaRDD.map(str -> {
//            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
//            Long planId = Long.valueOf(strs[0]);
//            //获取过滤信息  /除去planId便于统计
//            StringBuilder builder = new StringBuilder("");
//            if (planInfoDtoMap.containsKey(planId)) {
//                RecommendedPlanDto planDto = planInfoDtoMap.get(planId);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
//                String time = strs[1];
//                Date date = sdf.parse(time);
//                String times = sdf.format(date) + ":00:00";
//                builder.append(planDto.getRecommendedType()).append(AnalysisConstants.SPLIT_REGEX);
//                builder.append(planDto.getStyleId()).append(AnalysisConstants.SPLIT_REGEX);
//                builder.append(planDto.getSpaceType()).append(AnalysisConstants.SPLIT_REGEX);
//                builder.append(planDto.getCompanyId()).append(AnalysisConstants.SPLIT_REGEX);
//                builder.append(planDto.getPlanSource()).append(AnalysisConstants.SPLIT_REGEX);
//                builder.append(times);
//            }
//            if (StringUtils.isBlank(builder.toString())) {
//                return null;
//            }
//            return builder.toString();
//        }).cache();
//
//        System.out.println("javaRDD : " + javaRDD.collect());
//
//        //根据过滤信息分组获取使用量
//        Map<String, Long> planDayResultMap = javaRDD
//                .mapToPair(str -> {
//                    return new Tuple2<String, Long>(str, 1L);
//                }).reduceByKey((s1, s2) -> s1 + s2)
//                .collectAsMap();
//        System.out.println("planDayResultMap = " + planDayResultMap.size());
//
//        //插入数据
//        insertByHour(planDayResultMap, localDate);
//
//    }
//
//
//    private static void insertByHour(Map<String, Long> planDayResultMap, LocalDate localDate) throws ParseException {
//        PlanAnalysisResultDao resultDao = DaoFactory.getPlanAnalysisResultDao();
//
//        if (planDayResultMap == null || planDayResultMap.size() == 0) {
//            System.out.println("warn, function = PlanAnalysis.insertByDay, message: planDayResultMap is empty");
//            return;
//        }
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Date nowTime = new Date();
//
//        List<PlanAnalysisResultDto> list = new ArrayList<PlanAnalysisResultDto>();
//        PlanAnalysisResultQuery query = new PlanAnalysisResultQuery();
//
//        for (String key : planDayResultMap.keySet()) {
//            Integer value = planDayResultMap.get(key).intValue();
//            String[] keyArr = key.split(AnalysisConstants.SPLIT_REGEX);
//            Integer recommendedType = Integer.valueOf(keyArr[0]);
//            Integer styleId = Integer.valueOf(keyArr[1]);
//            Integer spaceType = Integer.valueOf(keyArr[2]);
//            Integer companyId = Integer.valueOf(keyArr[3]);
//            String planSource = keyArr[4];
//            String times = keyArr[5];
//
//            LocalDateTime localDateTime = LocalDateTime.parse(times, df);
//
//            LocalDate localDates = localDateTime.toLocalDate();
//            LocalTime time = localDateTime.toLocalTime();
//            LocalTime startTimes = time.withMinute(0).withSecond(0);
//            LocalDateTime startLocalTime = LocalDateTime.of(localDates, startTimes);
//            LocalTime endTimes = startTimes.plusHours(1);
//            LocalDateTime endLocalTime = LocalDateTime.of(localDates, endTimes);
//            String startTimeStr = df.format(startLocalTime);
//            String endTimeStr = df.format(endLocalTime);
//
//            Date startTime = dateFormat.parse(endTimeStr);
//            Date endTime = dateFormat.parse(startTimeStr);
//
//            PlanAnalysisResultDto planAnalysisResultDO = new PlanAnalysisResultDto();
//            planAnalysisResultDO.setPlanType(recommendedType);
//            planAnalysisResultDO.setDesignStyleId(styleId);
//            planAnalysisResultDO.setSpaceCommonType(spaceType);
//            planAnalysisResultDO.setCompanyId(companyId);
//            planAnalysisResultDO.setPlanSource(planSource);
//            planAnalysisResultDO.setNewPlanCount(value);
//            planAnalysisResultDO.setUsePlanCount(value);
//            planAnalysisResultDO.setStartTime(endTime);
//            planAnalysisResultDO.setEndTime(startTime);
//            planAnalysisResultDO.setIsDeleted(0);
//            planAnalysisResultDO.setGmtCreate(nowTime);
//            planAnalysisResultDO.setGmtModified(nowTime);
//            planAnalysisResultDO.setCreator(CommonConstants.CREATOR_SYSTEM);
//            planAnalysisResultDO.setModifier(CommonConstants.CREATOR_SYSTEM);
//            planAnalysisResultDO.setRemark("");
//            list.add(planAnalysisResultDO);
//
//            query.setStartTime(endTime);
//            query.setEndTime(startTime);
//
//            //添加数据之前先删除
//            resultDao.deleteByHour(query);
//        }
//
//
//        resultDao.insertByHour(list);
//    }
}
