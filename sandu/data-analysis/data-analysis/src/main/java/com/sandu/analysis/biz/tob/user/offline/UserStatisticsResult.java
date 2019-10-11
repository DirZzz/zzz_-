package com.sandu.analysis.biz.tob.user.offline;

import com.sandu.analysis.biz.tob.user.dao.AreaStatisticsDao;
import com.sandu.analysis.biz.tob.user.dao.CompanyStatisticsDao;
import com.sandu.analysis.biz.tob.user.dao.UserStatisticsDao;
import com.sandu.analysis.biz.tob.user.model.*;
import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.factory.DaoFactory;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName UserStatisticsResult
 * @Description B端用户数据统计
 * @Author chenm
 * @Date 2019/5/28 19:47
 * @Version 1.0
 **/
public class UserStatisticsResult {


    /**
     * B端用户、企业、区域信息统计
     */
    public static void main(String [] args){
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("PageAnalysis");

        System.out.println("###################################开始计算B端用户、企业、区域数据,Master:" + AnalysisConstants.SPARK_MASTER);
        sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        //FIXME:调试代码
     /*   args = new String[1];
        args[0] = "20190813";*/
        if(args != null && args.length > 0){
            System.out.println("################################### args:" + args[0]);
        }else{
            System.out.println("################################### args:" + null);
        }
        LocalDate localDate = Utils.getLocalDate(args);
        System.out.println("################################### localDate:" + localDate.toString());
        //新增用户数据
      /*  JavaRDD<String> newUserIdentityJavaRDD = getUserDataRdd(localDate,javaSparkContext,AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR);*/
        //活跃用户数据
        JavaRDD<String> userIdentityJavaRDD = getUserDataRdd(localDate,javaSparkContext,AnalysisConstants.HDFS_USERIDENTITY_DIR);
        if(userIdentityJavaRDD == null){
            System.out.println("###################################没有活跃用户数据,程序结束");
            return;
        }
        System.out.println(userIdentityJavaRDD.collect());
        //获取新增/活跃用户数据 (每天)
        //获取新增/活跃用户数据 (每小时)
        Map<String,Object> resultMap = getStatisticsDataList(userIdentityJavaRDD,localDate);
        if(resultMap == null || resultMap.size() < 0) {
            System.out.println("###################################warn-未得到有效结果,程序结束");
            return;
        }
        List<UserStatistics2bDayDto> dayDtos = (List<UserStatistics2bDayDto>) resultMap.get("userDayDtos");
        List<UserStatistics2bHourDto> hourDtos = (List<UserStatistics2bHourDto>) resultMap.get("userHourDtos");
        //用户登录信息
        UserStatisticsDao resultDao = DaoFactory.getUserStatisticsDao();
        resultDao.insertAfterDeleteWithDay(dayDtos,localDate);
        resultDao.insertAfterDeleteWithHour(hourDtos,localDate);
        List<CompanyStatistics2bDayDto> companyStatistics2BDayDtos = (List<CompanyStatistics2bDayDto>) resultMap.get("companyDayDtos");
        //企业统计信息
        CompanyStatisticsDao companyStatisticsDao = DaoFactory.getCompanyStatisticsDao();
        companyStatisticsDao.insertAfterDelete(companyStatistics2BDayDtos,localDate);
        System.out.println("###################################计算B端用户、企业数据完毕");
        /**
         * 读取区域用户数据
         * 因为要读取的是用户实际活动区域，
         * 经过过滤的用户日志去除了省市信息,
         * 所以需要直接读取埋点数据过滤和计算
         */
        System.out.println("###################################开始计算B端区域用户数据");
        //获取今日埋点日志
        JavaRDD<String> eventJavaRDD = getUserDataRdd(localDate,javaSparkContext,AnalysisConstants.HDFS_EVENTS_DIR);
        //区域统计信息
        List<AreaStatistics2bDayDto> areaStatistics2bDayDtos = getAreaStatisticsDataListByDay(eventJavaRDD,localDate);
        AreaStatisticsDao areaStatisticsDao = DaoFactory.getAreaStatisticsDao();
        areaStatisticsDao.insertAfterDelete(areaStatistics2bDayDtos,localDate);
        System.out.println("###################################计算B端区域用户数据完毕,得到结果条数:" + (areaStatistics2bDayDtos != null ? areaStatistics2bDayDtos.size() : 0));
    }

    public static JavaRDD<String> getUserDataRdd(LocalDate date,JavaSparkContext javaSparkContext,String dirPath){
        String userIdentityDataDirpath = null;
        //FIXME:调试代码
        /* if(ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV) ) {
            if (Objects.equals(AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR, dirPath)) {
                userIdentityDataDirpath = "G:/addUserData.txt";
            } else if (Objects.equals(AnalysisConstants.HDFS_EVENTS_DIR, dirPath)) {
                userIdentityDataDirpath = "G:/eventlog_201908_13.txt";
            } else {
                userIdentityDataDirpath = "G:/eventlog_201907_25.txt";
            }

        }else {*/
        userIdentityDataDirpath = AnalysisConstants.HDFS_DOMAIN + dirPath
                + date.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
        if (!HadoopUtils.getIsExist(userIdentityDataDirpath)) {
            System.out.println("###################################hdfs中对应目录数据不存在,path:" + userIdentityDataDirpath);
            return null;
        }
       /* }*/
        System.out.println("################################### 读取埋点数据path:" + userIdentityDataDirpath);
        JavaRDD<String> javaRDD = javaSparkContext.textFile(userIdentityDataDirpath);
        return javaRDD;
    }

    public static Map<String,Object> getStatisticsDataList(JavaRDD<String> userIdentityJavaRDD,LocalDate localDate){
        Map<String,Object> resultMapObjectMap = new HashMap<String,Object>(4);
        userIdentityJavaRDD = userIdentityJavaRDD.filter(str -> {
            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
            //只查找B端用户登录信息
            if(!AnalysisConstants.PLATFORM_CODE_2B_MAP.containsKey(strs[1])){
                return false;
            }
            return true;
        });

        List<String> userUUidList = userIdentityJavaRDD.map(strs ->{
            String [] strArr = strs.split(AnalysisConstants.SPLIT_REGEX);
            return strArr[0];
        }).filter(str -> StringUtils.isNotBlank(str) && !"null".equals(str)).distinct().collect();

        //查询用户信息

        UserStatisticsDao userDao = DaoFactory.getUserStatisticsDao();
        List<UserInfoDto> userInfoDtos = userDao.selectUserInfoByUUidList(userUUidList);
        Map<String,UserInfoDto> userInfoDtoMap  = new HashMap<>(userInfoDtos!= null ? userInfoDtos.size() : 0);
        if(CollectionUtils.isEmpty(userInfoDtos)){
            System.out.println("###################################error-没有有效用户信息");
        }else{
            userInfoDtoMap = userInfoDtos.stream().collect(Collectors.toMap(UserInfoDto::getUuid,a -> a,(k1,k2 )-> k1));
        }

        //分组并计算
        JavaPairRDD<String,String> pairRDD = userIdentityJavaRDD
            .mapToPair(t -> {
              /** uuid appid channel startTime endTime **/
              String[] arrs = t.split(AnalysisConstants.SPLIT_REGEX);
                /* newKey:uuid,newValue: appid startTime endTime */
                return new Tuple2<String,String>(arrs[0]
                        ,arrs[1] + AnalysisConstants.SPLIT_REGEX + arrs[3] + AnalysisConstants.SPLIT_REGEX + arrs[4]);
            }).cache();
        try {
            List<UserStatistics2bDayDto> dayDtos = getUserStatisticsDataListByDay(pairRDD,userInfoDtoMap,localDate);
            resultMapObjectMap.put("userDayDtos",dayDtos);
            System.out.println("###################################得到B端每天用户信息数据条数:" + (dayDtos != null ? dayDtos.size() : 0));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("计算每日用户登录数据时出现异常,时间:" + localDate +"异常信息:" + e.getMessage());
        }
        try {
            List<UserStatistics2bHourDto> hourDtos = getUserStatisticsDataListByHour(pairRDD,userInfoDtoMap,localDate);
            resultMapObjectMap.put("userHourDtos",hourDtos);
            System.out.println("###################################得到B端每小时用户信息数据条数:" + (hourDtos != null ? hourDtos.size() : 0));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("###################################计算每小时用户登录数据时出现异常,时间:" + localDate +"异常信息:" + e.getMessage());
        }
        try {
            List<CompanyStatistics2bDayDto> companyStatistics2BDayDtos = getCompanyStatistics2BDayDtoList(pairRDD,userInfoDtoMap,localDate);
            resultMapObjectMap.put("companyDayDtos",companyStatistics2BDayDtos);
            System.out.println("###################################得到B端企业信息数据条数:" + (companyStatistics2BDayDtos != null ? companyStatistics2BDayDtos.size() : 0));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("###################################计算每日企业用户数据时出现异常,时间:" + localDate +"异常信息:" + e.getMessage());
        }
        return resultMapObjectMap;
    }

    public static List<UserStatistics2bDayDto> getUserStatisticsDataListByDay(JavaPairRDD<String,String> pairRDD , Map<String,UserInfoDto> userInfoDtoMap,LocalDate localDate){

        //每天的登录用户统计数据
        JavaPairRDD<String,String> pairRDD1 =  pairRDD.filter(t -> {
                    String key = t._1;
                    if(!userInfoDtoMap.containsKey(key)){
                        return false;
                    }
                    return true;
                })
                .mapToPair(t ->{
                    String uuid = t._1;
                    String [] valueArrs = t._2.split(AnalysisConstants.SPLIT_REGEX);
                    String appId = valueArrs[0];
                    boolean hadLogin = AnalysisConstants.PLATFORM_CODE_2B_MAP.containsKey(appId);
                    boolean visitedPC = AnalysisConstants.PLATFORM_CODE_ENUM.pc2b.name().equals(appId);
                    boolean visitedMobiel2B = AnalysisConstants.PLATFORM_CODE_ENUM.mobile2b.name().equals(appId);
                    boolean visitedMerchantManage = AnalysisConstants.PLATFORM_CODE_ENUM.merchantManage.name().equals(appId);
                    UserInfoDto userInfoDto = userInfoDtoMap.get(uuid);
                    Integer userType = null;
                    Integer useType = null;
                    if(userInfoDto != null){
                        userType  = userInfoDto.getUserType();
                        useType = userInfoDto.getUseType();
                    }
                    //处理时间
                    //服务器时间
                    String time1 = valueArrs[1];
                    String startDate = Utils.getIntradayStartTime(time1);
                    String endDate = Utils.getDateAfter(startDate);
                    /*key : startTime endTime userType useType uuid*/
                    String newKey = startDate + AnalysisConstants.SPLIT_REGEX + endDate + AnalysisConstants.SPLIT_REGEX
                            + userType + AnalysisConstants.SPLIT_REGEX + useType + AnalysisConstants.SPLIT_REGEX + uuid;
                    /* value: activeUserCount loginUserCountPC2B loginUserCountMobile2B loginUserCountMerchantManage*/
                    String newValue = String.valueOf(hadLogin ? 1:0) + AnalysisConstants.SPLIT_REGEX
                            +  String.valueOf(visitedPC ? 1:0) + AnalysisConstants.SPLIT_REGEX +  String.valueOf(visitedMobiel2B ? 1:0) + AnalysisConstants.SPLIT_REGEX
                            +  String.valueOf(visitedMerchantManage ? 1:0);
                    return new Tuple2<String,String>(newKey,newValue);
                });
        Map<String,String> resultByDayMap =  pairRDD1 .reduceByKey(new Function2<String, String, String>() {
                    @Override
                    public String call(String v1, String v2) throws Exception {
                        //去除同一新增用户多次登录多个平台的影响
                        String[] value1 = v1.split(AnalysisConstants.SPLIT_REGEX);
                        String[] value2 = v2.split(AnalysisConstants.SPLIT_REGEX);
                        String activeUserCount = Integer.parseInt(value1[0]) > Integer.parseInt(value2[0]) ? value1[0] : value2[0];
                        String loginUserCountPC2B =  Integer.parseInt(value1[1]) > Integer.parseInt(value2[1]) ? value1[1] : value2[1];
                        String loginUserCountMobile2B = Integer.parseInt(value1[2]) > Integer.parseInt(value2[2]) ? value1[2] : value2[2];
                        String loginUserCountMerchantManage = Integer.parseInt(value1[3]) > Integer.parseInt(value2[3]) ? value1[3] : value2[3] ;
                        String newValue = activeUserCount + AnalysisConstants.SPLIT_REGEX + loginUserCountPC2B
                                +AnalysisConstants.SPLIT_REGEX  + loginUserCountMobile2B + AnalysisConstants.SPLIT_REGEX + loginUserCountMerchantManage;
                        return newValue;
                    }
                })
                .mapToPair(t -> {
                    String key = t._1;
                    //去除 uuid
                    /*newKey : startTime endTime userType useType*/
                    String newKey = key.substring(0,key.lastIndexOf(AnalysisConstants.SPLIT_REGEX));
                    return new Tuple2<String,String>(newKey,t._2);
                })
                .reduceByKey(new Function2<String, String, String>() {
                    @Override
                    public String call(String v1, String v2) throws Exception {
                        String[] value1 = v1.split(AnalysisConstants.SPLIT_REGEX);
                        String[] value2 = v2.split(AnalysisConstants.SPLIT_REGEX);
                        String activeUserCount = String.valueOf(Integer.parseInt(value1[0]) + Integer.parseInt(value2[0]));
                        String loginUserCountPC2B = String.valueOf(Integer.parseInt(value1[1]) + Integer.parseInt(value2[1]));
                        String loginUserCountMobile2B = String.valueOf(Integer.parseInt(value1[2]) + Integer.parseInt(value2[2]));
                        String loginUserCountMerchantManage = String.valueOf(Integer.parseInt(value1[3]) + Integer.parseInt(value2[3]));
                        String newValue = activeUserCount + AnalysisConstants.SPLIT_REGEX + loginUserCountPC2B
                                +AnalysisConstants.SPLIT_REGEX  + loginUserCountMobile2B + AnalysisConstants.SPLIT_REGEX + loginUserCountMerchantManage;
                        return newValue;
                    }
                }).collectAsMap();
        //获取查询数据时间区间
        String startTime = Utils.formatDateTime(LocalDateTime.of(localDate, LocalTime.MIN));
        String endTime = Utils.getDateAfter(startTime);
        UserStatisticsDao userStatisticsDao = DaoFactory.getUserStatisticsDao();
        //获取昨日新增用户数
        List<UserInfoDto> newUserCountDtos = userStatisticsDao.selectNewUserCountWithDay(startTime,endTime);
        Map<String,UserInfoDto> newUserCountMap = new HashMap<>(newUserCountDtos != null ? newUserCountDtos.size() : 0);
        newUserCountDtos.stream().forEach(userInfoDto -> {
            String key = userInfoDto.getUserType()
                    + AnalysisConstants.SPLIT_REGEX
                    + userInfoDto.getUseType();
            newUserCountMap.put(key,userInfoDto);
        });

        //获取昨日累积用户数
        List<UserInfoDto> userAccountDtos  = userStatisticsDao.selectUserCountByDay(endTime);
        Map<String, UserInfoDto> userAccountDtoMap = new HashMap<>(userAccountDtos != null ? userAccountDtos.size() : 0);
        userAccountDtos.stream().forEach(userInfoDto -> {
            String key = userInfoDto.getUserType()
                    + AnalysisConstants.SPLIT_REGEX
                    + userInfoDto.getUseType();
            userAccountDtoMap.put(key, userInfoDto);
        });
        //获取昨日未激活用户数
        List<UserInfoDto>  nonactivatedUserAccountDtos  = userStatisticsDao.selectNonactivatedUserCountByDay(endTime);
        Map<String, UserInfoDto> nonactivatedUserAccountDtoMap = new HashMap<>(nonactivatedUserAccountDtos != null ? nonactivatedUserAccountDtos.size() : 0);
        nonactivatedUserAccountDtos.stream().forEach(userInfoDto -> {
            String key = userInfoDto.getUserType()
                    + AnalysisConstants.SPLIT_REGEX
                    + userInfoDto.getUseType();
            nonactivatedUserAccountDtoMap.put(key, userInfoDto);
        });

        //转化
        List<UserStatistics2bDayDto> dayDtos = new ArrayList<>();
        UserStatistics2bDayDto dayDto = null;
        for (String key : resultByDayMap.keySet()){
            dayDto = new UserStatistics2bDayDto();
            /*key : startTime endTime userType useType*/
            String [] keyArrs = key.split(AnalysisConstants.SPLIT_REGEX);
            /* value:newUserCount activeUserCount loginUserCountPC2B loginUserCountMobile2B loginUserCountMerchantManage*/
            String [] valueArrs = resultByDayMap.get(key).split(AnalysisConstants.SPLIT_REGEX);
            String mapKey = keyArrs[2] + AnalysisConstants.SPLIT_REGEX + keyArrs[3];
            //累计账户数
            Integer accountCount = 0;
            if(userAccountDtoMap.containsKey(mapKey)){
                UserInfoDto userAccountDto = userAccountDtoMap.get(mapKey);
                accountCount = userAccountDto.getAccountCount();
                userAccountDtoMap.remove(mapKey);
            }
            //未激活账户数
            Integer nonactivatedUserCount = 0;
            if(nonactivatedUserAccountDtoMap.containsKey(mapKey)){
                UserInfoDto nonactivatedUserAccountDto = nonactivatedUserAccountDtoMap.get(mapKey);
                nonactivatedUserCount = nonactivatedUserAccountDto.getNonactivatedUserCount();
                nonactivatedUserAccountDtoMap.remove(mapKey);
            }
            //新增用户数
            Integer newUserCount = 0;
            if(newUserCountMap.containsKey(mapKey)){
                UserInfoDto infoDto = newUserCountMap.get(mapKey);
                newUserCount = infoDto.getNewUserCount();
                newUserCountMap.remove(mapKey);
            }

            dayDto = UserStatistics2bDayDto.builder().startTime(keyArrs[0]).endTime(keyArrs[1])
                    .userType("null".equals(keyArrs[2]) ? null : Integer.parseInt(keyArrs[2]))
                    .useType("null".equals(keyArrs[3]) ? null :Integer.parseInt(keyArrs[3]))
                    .newUserCount(newUserCount).activeUserCount(Integer.parseInt(valueArrs[0]))
                    .loginUserCountPC2B(Integer.parseInt(valueArrs[1])).loginUserCountMobile2B(Integer.parseInt(valueArrs[2])).accountCount(accountCount)
                    .nonactivatedUserCount(nonactivatedUserCount).loginUserCountMerchantManage(Integer.parseInt(valueArrs[3])).gmtCreate(new Date()).gmtModified(new Date())
                    .isDeleted(0).creator("sys").modifier("sys").build();
            dayDtos.add(dayDto);
        }
        //添加没有登录记录的其他类型数据
        if(userAccountDtoMap.size() > 0 || nonactivatedUserAccountDtoMap.size() > 0 ){

            if(userAccountDtoMap.size() > 0){
                for(String key : userAccountDtoMap.keySet()){
                    UserInfoDto infoDto =  userAccountDtoMap.get(key);
                    Integer accountCount = infoDto.getAccountCount() != null ? infoDto.getAccountCount() : 0;
                    //获取未激活用户数
                    Integer noLoginUserCount = 0;
                    if(nonactivatedUserAccountDtoMap.containsKey(key)){
                        noLoginUserCount = nonactivatedUserAccountDtoMap.get(key).getNonactivatedUserCount();
                        noLoginUserCount = noLoginUserCount != null ? noLoginUserCount : 0;
                        nonactivatedUserAccountDtoMap.remove(key);
                    }
                    //获取新增用户数
                    Integer newUserCount = 0;
                    if(newUserCountMap.containsKey(key)){
                        newUserCount = newUserCountMap.get(key).getNewUserCount();
                        newUserCount = newUserCount != null ? newUserCount : 0;
                        newUserCountMap.remove(key);
                    }
                    UserStatistics2bDayDto dto = UserStatistics2bDayDto.builder().startTime(startTime).endTime(endTime).userType(infoDto.getUserType()).useType(infoDto.getUseType())
                            .activeUserCount(0).newUserCount(newUserCount).accountCount(accountCount).nonactivatedUserCount(noLoginUserCount)
                            .gmtCreate(new Date()).gmtModified(new Date()).creator("sys").modifier("sys").isDeleted(0).remark("[自动添加]").build();
                    dayDtos.add(dto);
                }
            }
            if(nonactivatedUserAccountDtoMap.size() > 0){
                for(String key : nonactivatedUserAccountDtoMap.keySet()){
                    UserInfoDto infoDto =  nonactivatedUserAccountDtoMap.get(key);
                    Integer noLoginUserCount = infoDto.getNonactivatedUserCount();
                    noLoginUserCount = noLoginUserCount != null ? noLoginUserCount : 0;
                    //获取新增用户数
                    Integer newUserCount = 0;
                    if(newUserCountMap.containsKey(key)){
                        newUserCount = newUserCountMap.get(key).getNewUserCount();
                        newUserCount = newUserCount != null ? newUserCount : 0;
                        newUserCountMap.remove(key);
                    }
                    UserStatistics2bDayDto dto = UserStatistics2bDayDto.builder().startTime(startTime).endTime(endTime).userType(infoDto.getUserType()).useType(infoDto.getUseType())
                            .activeUserCount(0).newUserCount(newUserCount).accountCount(0).nonactivatedUserCount(noLoginUserCount)
                            .gmtCreate(new Date()).gmtModified(new Date()).creator("sys").isDeleted(0).modifier("sys").remark("[自动添加]").build();
                    dayDtos.add(dto);
                }
            }
            if(newUserCountMap.size()  > 0){
                for (String key : newUserCountMap.keySet()){
                    UserInfoDto infoDto = newUserCountMap.get(key);
                    Integer newUserCount = infoDto.getNewUserCount();
                    newUserCount = newUserCount != null ? newUserCount : 0;
                    UserStatistics2bDayDto dto = UserStatistics2bDayDto.builder().startTime(startTime).endTime(endTime).userType(infoDto.getUserType()).useType(infoDto.getUseType())
                            .activeUserCount(0).newUserCount(newUserCount).accountCount(0).nonactivatedUserCount(0)
                            .gmtCreate(new Date()).gmtModified(new Date()).creator("sys").isDeleted(0).modifier("sys").remark("[自动添加]").build();
                    dayDtos.add(dto);

                }
            }
        }

        return dayDtos;
    }


    public static List<UserStatistics2bHourDto> getUserStatisticsDataListByHour(JavaPairRDD<String,String> pairRDD, Map<String,UserInfoDto> userInfoDtoMap,LocalDate localDate){
        //每天的登录用户统计数据
        Map<String,String> resultByDayMap = pairRDD.filter(t -> {
                    String key = t._1;
                    if(!userInfoDtoMap.containsKey(key)){
                        return false;
                    }
                    return true;
                })
                .mapToPair(t ->{
                    String uuid = t._1;
                    String [] valueArrs = t._2.split(AnalysisConstants.SPLIT_REGEX);
                    String appId = valueArrs[0];
                    boolean hadLogin = AnalysisConstants.PLATFORM_CODE_2B_MAP.containsKey(appId);
                    boolean visitedPC = AnalysisConstants.PLATFORM_CODE_ENUM.pc2b.name().equals(appId);
                    boolean visitedMobiel2B = AnalysisConstants.PLATFORM_CODE_ENUM.mobile2b.name().equals(appId);
                    boolean visitedMerchantManage = AnalysisConstants.PLATFORM_CODE_ENUM.merchantManage.name().equals(appId);
                    UserInfoDto userInfoDto = userInfoDtoMap.get(uuid);
                    Integer userType = null;
                    Integer useType = null;
                    if(userInfoDto != null){
                        userType  = userInfoDto.getUserType();
                        useType = userInfoDto.getUseType();
                    }
                    //处理时间
                    String time1 = valueArrs[1];
                    Map<String,String> timeMap = Utils.getBetweenHour(time1);
                    String startTimeStr = timeMap.get("startTime");
                    String endTimeStr = timeMap.get("endTime");
                    /*key : startTime endTime userType useType uuid*/
                    String newKey = startTimeStr + AnalysisConstants.SPLIT_REGEX + endTimeStr + AnalysisConstants.SPLIT_REGEX
                            + userType + AnalysisConstants.SPLIT_REGEX + useType + AnalysisConstants.SPLIT_REGEX + uuid;
                    /* value:activeUserCount loginUserCountPC2B loginUserCountMobile2B loginUserCountMerchantManage*/
                    String newValue = String.valueOf(hadLogin ? 1:0) + AnalysisConstants.SPLIT_REGEX
                            +  String.valueOf(visitedPC ? 1:0) + AnalysisConstants.SPLIT_REGEX +  String.valueOf(visitedMobiel2B ? 1:0) + AnalysisConstants.SPLIT_REGEX
                            +  String.valueOf(visitedMerchantManage ? 1:0);
                    return new Tuple2<String,String>(newKey,newValue);
                })
                .reduceByKey(new Function2<String, String, String>() {
                    @Override
                    public String call(String v1, String v2) throws Exception {
                        //去除同一新增用户多次登录多个平台的影响
                        String[] value1 = v1.split(AnalysisConstants.SPLIT_REGEX);
                        String[] value2 = v2.split(AnalysisConstants.SPLIT_REGEX);
                        String activeUserCount = Integer.parseInt(value1[0]) > Integer.parseInt(value2[0]) ? value1[0] : value2[0];
                        String loginUserCountPC2B =  Integer.parseInt(value1[1]) > Integer.parseInt(value2[1]) ? value1[1] : value2[1];
                        String loginUserCountMobile2B = Integer.parseInt(value1[2]) > Integer.parseInt(value2[2]) ? value1[2] : value2[2];
                        String loginUserCountMerchantManage = Integer.parseInt(value1[3]) > Integer.parseInt(value2[3]) ? value1[3] : value2[3] ;
                        String newValue = activeUserCount + AnalysisConstants.SPLIT_REGEX + loginUserCountPC2B
                                +AnalysisConstants.SPLIT_REGEX  + loginUserCountMobile2B + AnalysisConstants.SPLIT_REGEX + loginUserCountMerchantManage;
                        return newValue;
                    }
                })
                .mapToPair(t -> {
                    String key = t._1;
                    //去除 uuid
                    /*newKey : startTime endTime userType useType*/
                    String newKey = key.substring(0,key.lastIndexOf(AnalysisConstants.SPLIT_REGEX));
                    return new Tuple2<String,String>(newKey,t._2);
                })
                .reduceByKey(new Function2<String, String, String>() {
                    @Override
                    public String call(String v1, String v2) throws Exception {
                        String[] value1 = v1.split(AnalysisConstants.SPLIT_REGEX);
                        String[] value2 = v2.split(AnalysisConstants.SPLIT_REGEX);
                        String activeUserCount = String.valueOf(Integer.parseInt(value1[0]) + Integer.parseInt(value2[0]));
                        String loginUserCountPC2B = String.valueOf(Integer.parseInt(value1[1]) + Integer.parseInt(value2[1]));
                        String loginUserCountMobile2B = String.valueOf(Integer.parseInt(value1[2]) + Integer.parseInt(value2[2]));
                        String loginUserCountMerchantManage = String.valueOf(Integer.parseInt(value1[3]) + Integer.parseInt(value2[3]));
                        String newValue = activeUserCount + AnalysisConstants.SPLIT_REGEX + loginUserCountPC2B
                                +AnalysisConstants.SPLIT_REGEX  + loginUserCountMobile2B + AnalysisConstants.SPLIT_REGEX + loginUserCountMerchantManage;
                        return newValue;
                    }
                })
                .collectAsMap();
        //转化
        List<UserStatistics2bHourDto> hourDtos = new ArrayList<>();
        UserStatistics2bHourDto hourDto = null;
        //获取某天的时间区间
        String startTime = Utils.formatDateTime(LocalDateTime.of(localDate, LocalTime.MIN));
        String endTime = Utils.getDateAfter(startTime);
        UserStatisticsDao userStatisticsDao = DaoFactory.getUserStatisticsDao();
        List<UserInfoDto> infoDtos = userStatisticsDao.selectNewUserCountWithHour(startTime,endTime);
        Map<String,UserInfoDto> infoDtoMap = new HashMap<>();
        infoDtos.stream().forEach(userInfoDto -> {
            String key = userInfoDto.getPerHour() + AnalysisConstants.SPLIT_REGEX
                    + userInfoDto.getUserType() + AnalysisConstants.SPLIT_REGEX + userInfoDto.getUseType();
            infoDtoMap.put(key, userInfoDto);
        });

        for (String key : resultByDayMap.keySet()){
            hourDto = new UserStatistics2bHourDto();
            /*key : startTime endTime userType useType*/
            String [] keyArrs = key.split(AnalysisConstants.SPLIT_REGEX);
            /* value:newUserCount activeUserCount loginUserCountPC2B loginUserCountMobile2B loginUserCountMerchantManage*/
            String [] valueArrs = resultByDayMap.get(key).split(AnalysisConstants.SPLIT_REGEX);
           /* String perHour = Utils.getPerHour(keyArrs[0]);*/
            String mapKey = keyArrs[0] + AnalysisConstants.SPLIT_REGEX + keyArrs[2] + AnalysisConstants.SPLIT_REGEX + keyArrs[3];
            Integer newUserCount = 0;
            if(infoDtoMap.containsKey(mapKey)){
                UserInfoDto infoDto = infoDtoMap.get(mapKey);
                if(infoDto != null && infoDto.getNewUserCount() != null){
                    newUserCount = infoDto.getNewUserCount();
                }
                infoDtoMap.remove(mapKey);
            }
            hourDto = UserStatistics2bHourDto.builder().startTime(keyArrs[0]).endTime(keyArrs[1])
                    .userType("null".equals(keyArrs[2]) ? null : Integer.parseInt(keyArrs[2]))
                    .useType("null".equals(keyArrs[3]) ? null :Integer.parseInt(keyArrs[3]))
                    .newUserCount(newUserCount).activeUserCount(Integer.parseInt(valueArrs[0]))
                    .loginUserCountPC2B(Integer.parseInt(valueArrs[1])).loginUserCountMobile2B(Integer.parseInt(valueArrs[2]))
                    .loginUserCountMerchantManage(Integer.parseInt(valueArrs[3])).gmtCreate(new Date()).isDeleted(0).gmtModified(new Date()).creator("sys").modifier("sys").build();
            hourDtos.add(hourDto);
        }
        //增加新增但未登录的数据记录
        if(infoDtoMap.size() > 0){
            for (String key : infoDtoMap.keySet()){
                UserInfoDto dto = infoDtoMap.get(key);
                Map<String, String> timeMap = Utils.getBetweenHour(dto.getPerHour());
                String beginDate =  timeMap.get("startTime");
                String endDate = timeMap.get("endTime");
                UserStatistics2bHourDto bHourDto = UserStatistics2bHourDto.builder().userType(dto.getUserType()).useType(dto.getUseType()).newUserCount(dto.getNewUserCount()).startTime(beginDate).endTime(endDate)
                        .loginUserCountMobile2B(0).loginUserCountPC2B(0).loginUserCountMerchantManage(0).activeUserCount(0)
                        .gmtCreate(new Date()).creator("sys").modifier("sys").gmtModified(new Date()).isDeleted(0).remark("[自动添加]").build();
                hourDtos.add(bHourDto);
            }
        }

        return hourDtos;
    }
    public static List<CompanyStatistics2bDayDto> getCompanyStatistics2BDayDtoList(JavaPairRDD<String,String> pairRDD, Map<String,UserInfoDto> userInfoDtoMap,LocalDate localDate) {

        List<CompanyStatistics2bDayDto> dtoList = new ArrayList<>();
        //得到所有登录用户的企业
        List<Integer> companyIds = userInfoDtoMap.entrySet().stream()
                .sorted(Comparator.comparing(v -> v.getKey()))
                .map(e -> e.getValue().getCompanyId()).filter(e -> e != null).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(companyIds)){
            System.out.println("###################################error - 没有有效企业信息");
            return null;
        }
        CompanyStatisticsDao companyStatisticsDao = DaoFactory.getCompanyStatisticsDao();
        List<CompanyInfoDto> companyInfoDtoList = companyStatisticsDao.selectCompanyInfoListByIdList(companyIds);
        Map<Integer,CompanyInfoDto> infoDtoMap = companyInfoDtoList.stream().collect(Collectors.toMap(CompanyInfoDto::getId,a -> a,(k1,k2 )-> k1));
        Map<String,String> pairRddMap = pairRDD.filter(t -> {
                String key = t._1;
                if(!userInfoDtoMap.containsKey(key)){
                    return false;
                }
                return true;
            })
            .mapToPair(t -> {
            String uuid = t._1;
            String value = t._2;
            String[] valueArrs = t._2.split(AnalysisConstants.SPLIT_REGEX);
            UserInfoDto infoDto = userInfoDtoMap.get(uuid);
            /*CompanyInfoDto companyInfoDto = infoDtoMap.get(infoDto.getCompanyId());*/
            //处理时间 得到今日最小时间和最大时间
            String time1 = valueArrs[1];
            String startTime = Utils.getIntradayStartTime(time1);
            String endTime = Utils.getDateAfter(startTime);
            /** newKsy: startTime endTime companyId uuid**/
            String newKey = startTime + AnalysisConstants.SPLIT_REGEX + endTime + AnalysisConstants.SPLIT_REGEX + infoDto.getCompanyId()
                    + AnalysisConstants.SPLIT_REGEX + uuid;
            /* newKey: startTime endTime companyId uuid
            newValue: loginUserCount*/
            String newValue = String.valueOf(1);
            return new Tuple2<String,String>(newKey,newValue);
        })
        .filter(s ->{
            //防止空值干扰
            String[] keyArrs = s._1.replace(AnalysisConstants.SPLIT_REGEX + "null","").split(AnalysisConstants.SPLIT_REGEX);
            if(keyArrs == null || keyArrs.length != 4){
                System.out.println("###################################企业数值有问题:" + s._1);
                return false;
            }
            return true;
        })
        .reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                //去除同一新增用户对数据的影响
                String activeUserCount = Integer.parseInt(v1) > Integer.parseInt(v2) ? v1 : v2;
                return activeUserCount;
            }
        })
        .mapToPair(t -> {
            String key = t._1;
            //去除 uuid
             /*newKey : startTime endTime companyId */
            String newKey = key.substring(0,key.lastIndexOf(AnalysisConstants.SPLIT_REGEX));
            return new Tuple2<String,String>(newKey,t._2);
        })
        .reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                String activeUserCount = String.valueOf(Integer.parseInt(v1) + Integer.parseInt(v2));
                return activeUserCount;
            }
        })
        .collectAsMap();
        //转换
        String startTime = Utils.formatDateTime(LocalDateTime.of(localDate, LocalTime.MIN));
        String endTime = Utils.getDateAfter(startTime);
        CompanyStatistics2bDayDto dayDto = null;
        UserStatisticsDao userStatisticsDao = DaoFactory.getUserStatisticsDao();
        for (String key : pairRddMap.keySet()){
            String[] keyArrs = key.split(AnalysisConstants.SPLIT_REGEX);
            //某日活跃用户
            Integer activeUserCount = Integer.parseInt(pairRddMap.get(key));
            Integer companyId = Integer.parseInt(keyArrs[2]);
            CompanyInfoDto infoDto = infoDtoMap.get(companyId);
            //企业开通人数
            Long userTotal = userStatisticsDao.selectUserTotalByCompanyId(companyId,endTime);

            //使用率(活跃用户/企业开通人数)
            DecimalFormat df = new DecimalFormat("0.00");
            String percent = new BigDecimal((double)activeUserCount*100/userTotal).setScale(2,BigDecimal.ROUND_HALF_UP)+"%";

             dayDto = CompanyStatistics2bDayDto.builder().startTime(keyArrs[0]).endTime(keyArrs[1]).companyId(companyId).userAccountCount(userTotal.intValue())
                    .companyName(infoDto.getCompanyName()).companyType(infoDto.getCompanyType()).brandName(infoDto.getBrandName()).userEffectiveRate(percent)
                    .activeUserCount(activeUserCount).gmtCreate(new Date()).gmtModified(new Date()).isDeleted(0)
                     .creator("sys").modifier("sys").build();
            dtoList.add(dayDto);
        }

        return dtoList;
    }


    public static List<AreaStatistics2bDayDto> getAreaStatisticsDataListByDay(JavaRDD<String> javaRDD,LocalDate localDate) {

        //过滤出用户登录日志
        //           0                               1                 2                    3              4      5       6       7                  8             9  10      11      12
        //c5297991bb3f11e8b1d5f403434d7108	2019-06-19 16:44:36	2019-06-19 16:44:36	serviceEvent	event:login	pc2b	java	1.0.0	192.168.1.225	10.10.30.164	中国	 广东省	深圳市
        JavaPairRDD<String,String> pairRDD = javaRDD.filter(e -> {
            String[] strArr = e.split(AnalysisConstants.SPLIT_REGEX);
            if (!Objects.equals("serviceEvent", strArr[3])) {
                return false;
            }
            Map<String, String> eventMap = Utils.getMap(strArr[4]);
            if (!Objects.equals("login", eventMap.get(AnalysisConstants.EVENTPROPERTY_ENUM.event.getName()))) {
                return false;
            }
            if (!AnalysisConstants.PLATFORM_CODE_2B_MAP.containsKey(strArr[5])) {
                return false;
            }
            if(StringUtils.isBlank(e)){
                return false;
            }
            return true;
        }).mapToPair(str ->{
            String[] strArr = str.split(AnalysisConstants.SPLIT_REGEX);
            //newKey: 省名称\t市名称\t用户uuid
            String newKey = strArr[11] + AnalysisConstants.SPLIT_REGEX + strArr[12] + AnalysisConstants.SPLIT_REGEX +  strArr[0];
            //newValue:用户的uuid
            String newValue = strArr[0];
            return new Tuple2<String,String>(newKey,newValue);
        }).distinct().cache();
        List<AreaStatistics2bDayDto> dtoList = null;
        try {
            dtoList = analysisAreaStatisticsDataListByDay(pairRDD,localDate);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("###################################计算每小时区域用户数据时出现异常,计算的时间:" + localDate +"异常信息:" + e.getMessage());
        }

        return dtoList;
    }



    public static List<AreaStatistics2bDayDto> analysisAreaStatisticsDataListByDay(JavaPairRDD<String,String> pairRDD,LocalDate localDate) {
        List<AreaStatistics2bDayDto> dtoList = new ArrayList<>();
        //获取查询数据时间区间
        String startTime = Utils.formatDateTime(LocalDateTime.of(localDate, LocalTime.MIN));
        String endTime = Utils.getDateAfter(startTime);
        //获取某日新增B端用户数据
        UserStatisticsDao userStatisticsDao = DaoFactory.getUserStatisticsDao();
        List<String> uuidList = userStatisticsDao.selectNewUserUuidWithDay(startTime,endTime);
        //某日新增用户uuid集合
        Set<String> uuidSet = uuidList.stream().collect(Collectors.toSet());
        //一级行政区名称集合
        Set<String> provinceNameSet = new HashSet<>();
        //二级行政区名称集合
        Set<String> cityNameSet = new HashSet<>();

        //转化数据
        Map<String,String> areaUserCountMap = pairRDD.mapValues(value ->{
            //是否是新增用户
            boolean isNewUser = false;
            if(uuidSet.contains(value)){
                isNewUser = true;
            }
            //newValue:登录用户数\t新增(已登录)用户数
            String newValue = 1 + AnalysisConstants.SPLIT_REGEX + (isNewUser ? 1 : 0);
            return newValue;
        }).distinct()
          .mapToPair(str -> {
                String oldKey = str._1;
                String newKey = oldKey.substring(0,oldKey.lastIndexOf(AnalysisConstants.SPLIT_REGEX));
                return new Tuple2<String,String>(newKey,str._2);
        }).reduceByKey((s, s2) -> {
            String[] valueArr1 = s.split(AnalysisConstants.SPLIT_REGEX);
            String[] valueArr2 = s2.split(AnalysisConstants.SPLIT_REGEX);
            Integer userCount = Integer.parseInt(valueArr1[0]) + Integer.parseInt(valueArr2[0]);
            Integer newUserCount = Integer.parseInt(valueArr1[1]) + Integer.parseInt(valueArr2[1]);
            //newValue:活跃用户数\t新增(登录)用户数
            String newValue = userCount.toString() + AnalysisConstants.SPLIT_REGEX + newUserCount.toString();
            return newValue;
        }).collectAsMap();
        if(areaUserCountMap == null || areaUserCountMap.size() <= 0){
            return null;
        }

        //根据省市的名称查询省市 code
        provinceNameSet = areaUserCountMap.keySet().stream().map(key -> {
            String provinceName = key.split(AnalysisConstants.SPLIT_REGEX)[0];
            return provinceName;
        }).filter(str -> StringUtils.isNotBlank(str)).collect(Collectors.toSet());
        cityNameSet = areaUserCountMap.keySet().stream().map(key -> {
            String provinceName = key.split(AnalysisConstants.SPLIT_REGEX)[1];
            return provinceName;
        }).filter(str -> StringUtils.isNotBlank(str)).collect(Collectors.toSet());

        AreaStatisticsDao areaStatisticsDao = DaoFactory.getAreaStatisticsDao();
        List<AreaInfoDto> provinceInfoList = areaStatisticsDao.selectProvinceInfoByAreaNames(provinceNameSet);
        List<AreaInfoDto> cityInfoList = areaStatisticsDao.selectCityInfoByAreaNames(cityNameSet);
        Map<String,AreaInfoDto> provinceMap = new HashMap<>(provinceInfoList != null ? provinceInfoList.size() : 0);
        if(CollectionUtils.isNotEmpty(provinceInfoList)){
            provinceMap = provinceInfoList.stream().collect(Collectors.toMap(AreaInfoDto::getAreaName,a -> a,(k1,k2) -> k2));
        }
        Map<String,AreaInfoDto> cityMap = new HashMap<>(cityInfoList != null ? cityInfoList.size() : 0);
        if(CollectionUtils.isNotEmpty(cityInfoList)){
            cityMap = cityInfoList.stream().collect(Collectors.toMap(AreaInfoDto::getAreaName,a -> a,(k1,k2) -> k2));
        }

        //组装数据
        for (String key : areaUserCountMap.keySet()){
            //key: provinceName\tcityName
            String[] keyArrs = key.split(AnalysisConstants.SPLIT_REGEX);
            //value:活跃用户数\t新增(登录)用户数
            String[] valueArrs = areaUserCountMap.get(key).split(AnalysisConstants.SPLIT_REGEX);
            String provinceCode = null;
            String cityCode = null;
            if(provinceMap.containsKey(keyArrs[0])){
                provinceCode = provinceMap.get(keyArrs[0]).getAreaCode();
            }
            if(cityMap.containsKey(keyArrs[1])){
                cityCode = cityMap.get(keyArrs[1]).getAreaCode();
            }
            AreaStatistics2bDayDto dto= AreaStatistics2bDayDto.builder().startTime(startTime).endTime(endTime)
                    .provinceCode(provinceCode).provinceName(keyArrs[0]).cityCode(cityCode).cityName(keyArrs[1])
                    .newUserCount(Integer.parseInt(valueArrs[1])).activeUserCount(Integer.parseInt(valueArrs[0]))
                    .gmtCreate(new Date()).gmtModified(new Date()).isDeleted(0).creator("sys").modifier("sys").build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    }
