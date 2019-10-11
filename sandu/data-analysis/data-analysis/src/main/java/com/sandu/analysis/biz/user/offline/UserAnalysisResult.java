package com.sandu.analysis.biz.user.offline;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.user.dao.UserAnalysisResultDao;
import com.sandu.analysis.biz.user.model.UserAnalysisResultDto;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class UserAnalysisResult {
    /**
     * 获取用户活跃信息
     * @param args #{nowDate}, #{type}
     * type = 1:日...
     */
    public static void main(String[] args) {

    	System.out.println("args = " + args.toString());
    	
        // ==========spark上下文初始化 ->start
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("PageAnalysis");
        sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
        @SuppressWarnings("resource")
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        // ==========spark上下文初始化 ->end

        LocalDate nowDate = null;
        if(args != null && args.length > 1) {
            nowDate = LocalDate.parse(args[1]);
        }else {
            nowDate = LocalDate.now();
        }
        // 获取时间区间列表
        List<LocalDate> rangeList = Utils.getWeekOrMonthDateRangeList(nowDate, args[0]);
        //List<LocalDate> rangeList = Utils.getWeekOrMonthDateRangeList("2019-05-13", "1");

        //获取所有日期区间所有用户活跃文件union后的RDD
        JavaRDD<String> javaRDDUnionnAllUser = getDataRDD(javaSparkContext,rangeList,AnalysisConstants.HDFS_USERIDENTITY_DIR);
        //获取所有日期区间新增用户活跃文件union后的RDD
        JavaRDD<String> javaRDDUnionNewUser = getDataRDD(javaSparkContext,rangeList,AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR);


        // 活跃用户的resultMap
        System.out.println("==========javaRDDUnionnAllUser.size = " + javaRDDUnionnAllUser.count());
        Map<String, Long> mapAllUser = getUserInfoMap(javaRDDUnionnAllUser);
        // 新增用户的resultMap
        System.out.println("==========javaRDDUnionNewUser.size = " + javaRDDUnionNewUser.count());
        Map<String, Long> mapNewUser = getUserInfoMap(javaRDDUnionNewUser);

        //根据appid	channel将活跃用户统计和新增用户
        Map<String, String> resultMap = getUserInfoResult(mapAllUser, mapNewUser);
        System.out.println("==========resultMap = " + resultMap);
        insertInfoToDB(resultMap, nowDate, args[0]);
        System.out.println("==========程序执行完毕");

    }

    private static void insertInfoToDB(Map<String, String> resultMap, LocalDate localDate, String type){
    	if(resultMap == null || resultMap.size() == 0) {
    		System.out.println("warn, resultMap = " + resultMap + ", 识别没有检测到用户信息, 程序终止");
    		return;
    	}
        List<UserAnalysisResultDto> dtoList = new ArrayList<>();
        Date now = new Date();
        Map<String, LocalDate> dateRangeMap = Utils.getDateRangeMap(localDate, type);
        LocalDate startDate = dateRangeMap.get("startDate");
        LocalDate endDate = dateRangeMap.get("endDate");
        Date startTime = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        for(String key : resultMap.keySet()) {
            String value = resultMap.get(key);
            String[] keyStrs = key.split(AnalysisConstants.SPLIT_REGEX);
            String[] valueStrs = value.split(AnalysisConstants.SPLIT_REGEX);
            String appId = keyStrs[0];
            String channel = keyStrs[1];
            Integer activeUserCount = Integer.valueOf(valueStrs[0]);
            Integer newUserCount = Integer.valueOf(valueStrs[1]);

            UserAnalysisResultDto dto = new UserAnalysisResultDto();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setNewUserCount(newUserCount);
            dto.setActiveUserCount(activeUserCount);
            dto.setType(Integer.parseInt(type));
            dto.setAppId(appId);
            dto.setChannel(channel);
            dto.setCreator(CommonConstants.CREATOR_SYSTEM);
            dto.setGmtCreate(now);
            dto.setModifier(CommonConstants.CREATOR_SYSTEM);
            dto.setGmtModified(now);
            dto.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
            dto.setRemark("");
            dtoList.add(dto);
        }

        UserAnalysisResultDao userAnalysisResultDao = DaoFactory.getUserAnalysisResultDao();
        userAnalysisResultDao.insertBeforeDelete(dtoList);
    }


    public static Map<String, String> getUserInfoResult(Map<String, Long> mapAllUser, Map<String, Long> mapNewUser){
        if(mapAllUser == null || mapAllUser.size() == 0) {
            System.out.println("warn, function = UserAnalysisResult.getUserInfoResult, message = (mapAllUser == null || mapAllUser.size() == 0) = true");
            return null;
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        for(String key : mapAllUser.keySet()) {
            Long allUser = mapAllUser.get(key);
            Long newUser = mapNewUser.get(key);
            resultMap.put(key, (allUser == null ? 0 : allUser) + AnalysisConstants.SPLIT_REGEX + (newUser == null ? 0 : newUser));
        }

        return resultMap;
    }

    public static Map<String, Long> getUserInfoMap(JavaRDD<String> javaRDDUnionUser){
        // key = appid	channel, value = sum
        Map<String, Long> map =
        		// 0		1			2				3			4
                // uuid	appid	channel		time		time
                javaRDDUnionUser
                .map(str -> {
                	String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
                	return strs[0] + AnalysisConstants.SPLIT_REGEX + strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2];
                })
                .distinct()
                        // key = appid	channel
                        .map(str -> {
                            String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
                            return strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2];
                        }).countByValue();
        return map;
    }


    public static JavaRDD<String> getDataRDD(JavaSparkContext javaSparkContext, List<LocalDate> rangeList,String dir){
        //List<JavaRDD<String>> javaRDDList = new ArrayList<>();
        /*JavaRDD<String> javaRDDUnion = null;*/
    	JavaRDD<String> javaRDDUnion = javaSparkContext.parallelize(new ArrayList<String>());
        for (int i=0;i<rangeList.size();i++){
            // 目录 ->start
            String hdfsFileDir = null;
            String dataDirInfo = rangeList.get(i).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
            if(ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV)) {
                hdfsFileDir = "D:/devil/hdfs/user";
            } else {
                hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + dir + dataDirInfo;
                System.out.println("==========hdfsFileDir = " + hdfsFileDir);
                if(!HadoopUtils.getIsExist(hdfsFileDir)) {
                	System.out.println("==========该目录不存在, hdfsFileDir = " + hdfsFileDir);
                	continue;
                }
            }
            // 目录 ->end
            
            JavaRDD<String> javaRDD = javaSparkContext.textFile(hdfsFileDir);
            /*if (i==0){
                javaRDDUnion = javaRDD;
                continue;
            }*/
            javaRDDUnion = javaRDDUnion.union(javaRDD);
            //javaRDDList.add(javaRDD);
        }
        return javaRDDUnion;
       /* for(int i=0;i<javaRDDList.size();i++){
            javaRDDUn.union(javaRDDList.get(i));
        }*/
    }

}