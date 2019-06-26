package com.sandu.analysis.biz.usersRetention.offline;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.USERRETENTION_DAYS_ENUM;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.usersRetention.dao.UserRetentionResultDao;
import com.sandu.analysis.biz.usersRetention.model.UserRetentionResultDO;
import com.sandu.analysis.biz.usersRetention.model.UserRetentionResultForUpdateDO;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;

import scala.Tuple2;

public class UsersRetentionAnaysis {

	private static String className = "UsersRetentionAnaysis";
	
	/**
	 * 分析用户留存
	 * 大致逻辑:
	 * 如果要统计3月1号的新增用户次日留存
	 * 取出3月1号的newUserTagList与3月2号的allUserTagList做并集, 取size
	 * 
	 * 逻辑细节:
	 * 1. 算"昨天"的次日留存时, 需同时计算出"昨天"的新用户平均访问页面次数, 其他时间只要更新结果数据的对应天数的留存数就可以
	 * 
	 * 备注:
	 * 1. 此程序一定要在UserAnalysis.main执行完成之后
	 * 
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		/*String functionName = className + ".main";*/
		
		// ==========spark上下文初始化 ->start
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("UsersRetentionAnaysis");
		sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
		System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		// ==========spark上下文初始化 ->end
		
		LocalDate localDate = Utils.getLocalDate(args);
		
		// ==========获取所有我需要的hdfs路径 ->start
		// 今天的所有用户信息的路径
		String todayUserHdfsFileDir = null;
		// 1天前的新用户信息的路径
		String oneDayBeforeNewUserHdfsFileDir = null;
		// 3天前的新用户信息的路径
		String threeDayBeforeNewUserHdfsFileDir = null;
		// 7天前的新用户信息的路径
		String sevenDayBeforeNewUserHdfsFileDir = null;
		// 30天前的新用户信息的路径
		String thirtyDayBeforeNewUserHdfsFileDir = null;
		if (ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV)) {
			todayUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_USERIDENTITY_DIR + localDate.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			oneDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			threeDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-3).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			sevenDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-7).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			thirtyDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-30).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
		} else {
			todayUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_USERIDENTITY_DIR + localDate.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			oneDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			threeDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-3).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			sevenDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-7).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
			thirtyDayBeforeNewUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.plusDays(-30).format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
		}
		// ==========获取所有我需要的hdfs路径 ->end
		
		// ==========取出今天的allUserMap ->start
		Map<String, Iterable<String>> todayUserMap = new HashMap<String, Iterable<String>>();
		
		if (!ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV) && !HadoopUtils.getIsExist(todayUserHdfsFileDir)) {
			
		} else {
			// 0		1			2
			// uuid	appId	channel
			todayUserMap = javaSparkContext.textFile(todayUserHdfsFileDir)
			.mapToPair(str -> {
				String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
				return new Tuple2<String, String>(strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2], strs[0]);
			})
			.groupByKey()
			.collectAsMap();
		}
		// ==========取出今天的allUserMap ->end
		
		// ==========计算出一天前的次日留存 ->start
		Map<String, Integer> oneDayRetentionResultMap = getDayResultMap(javaSparkContext, oneDayBeforeNewUserHdfsFileDir, todayUserMap);
		// ==========计算出一天前的次日留存 ->end
		
		// ==========计算出3天前的3日留存 ->start
		Map<String, Integer> threeDayRetentionResultMap = getDayResultMap(javaSparkContext, threeDayBeforeNewUserHdfsFileDir, todayUserMap);
		// ==========计算出3天前的3日留存 ->end
		
		// ==========计算出7天前的7日留存 ->start
		Map<String, Integer> sevenDayRetentionResultMap = getDayResultMap(javaSparkContext, sevenDayBeforeNewUserHdfsFileDir, todayUserMap);
		// ==========计算出7天前的7日留存 ->end
		
		// ==========计算出30天前的30日留存 ->start
		Map<String, Integer> thirtyDayRetentionResultMap = getDayResultMap(javaSparkContext, thirtyDayBeforeNewUserHdfsFileDir, todayUserMap);
		// ==========计算出30天前的30日留存 ->end
		
		// ==========本地调试 ->start
		/*System.out.println(oneDayRetentionResultMap);
		System.out.println(threeDayRetentionResultMap);
		System.out.println(sevenDayRetentionResultMap);
		System.out.println(thirtyDayRetentionResultMap);*/
		// ==========本地调试 ->end
		
		// ==========update bigdata_user_retention_result ->start
		updateDBDate(oneDayRetentionResultMap, threeDayRetentionResultMap, sevenDayRetentionResultMap, thirtyDayRetentionResultMap, localDate);
		// ==========update bigdata_user_retention_result ->end
	}
	
	/**
	 * 第一步: 找出数据库, startTime/endTime相等的数据集, 待更新
	 * 第二步: 更新appId + channel相等的数据
	 * 
	 * @author huangsongbo
	 * @param oneDayRetentionResultMap
	 * @param threeDayRetentionResultMap
	 * @param sevenDayRetentionResultMap
	 * @param thirtyDayRetentionResultMap
	 */
	private static void updateDBDate(
			Map<String, Integer> oneDayRetentionResultMap,
			Map<String, Integer> threeDayRetentionResultMap, 
			Map<String, Integer> sevenDayRetentionResultMap,
			Map<String, Integer> thirtyDayRetentionResultMap,
			LocalDate localDate
			) {
		System.out.println("oneDayRetentionResultMap = " + oneDayRetentionResultMap);
		System.out.println("threeDayRetentionResultMap = " + threeDayRetentionResultMap);
		System.out.println("sevenDayRetentionResultMap = " + sevenDayRetentionResultMap);
		System.out.println("thirtyDayRetentionResultMap = " + thirtyDayRetentionResultMap);
		
		// ==========初始化 ->start
		UserRetentionResultDao userRetentionResultDao = DaoFactory.getUserRetentionResultDao();
		// ==========初始化 ->end
		
		// ==========找出1天前的数据集(from bigdata_user_retention_result) ->start
		LocalDateTime startTimeOneDayBefore = localDate.plusDays(-1).atStartOfDay();
		LocalDateTime endTimeOneDayBefore = startTimeOneDayBefore.plusDays(1);
		List<UserRetentionResultDO> userRetentionResultOneDayBeforeDOList = userRetentionResultDao.select(startTimeOneDayBefore, endTimeOneDayBefore);
		// ==========找出1天前的数据集(from bigdata_user_retention_result) ->end
		
		// ==========更新1天前的次日留存属性 ->start
		update(userRetentionResultOneDayBeforeDOList, oneDayRetentionResultMap, USERRETENTION_DAYS_ENUM.oneDay);
		// ==========更新1天前的次日留存属性 ->end
		
		// ==========找出3天前的数据集(from bigdata_user_retention_result) ->start
		LocalDateTime startTimeThreeDayBefore = localDate.plusDays(-3).atStartOfDay();
		LocalDateTime endTimeThreeDayBefore = startTimeThreeDayBefore.plusDays(1);
		List<UserRetentionResultDO> userRetentionResultThreeDayBeforeDOList = userRetentionResultDao.select(startTimeThreeDayBefore, endTimeThreeDayBefore);
		// ==========找出3天前的数据集(from bigdata_user_retention_result) ->end
		
		// ==========更新3天前的次日留存属性 ->start
		update(userRetentionResultThreeDayBeforeDOList, threeDayRetentionResultMap, USERRETENTION_DAYS_ENUM.threeDay);
		// ==========更新3天前的次日留存属性 ->end
		
		// ==========找出7天前的数据集(from bigdata_user_retention_result) ->start
		LocalDateTime startTimeSevenDayBefore = localDate.plusDays(-7).atStartOfDay();
		LocalDateTime endTimeSevenDayBefore = startTimeSevenDayBefore.plusDays(1);
		List<UserRetentionResultDO> userRetentionResultSevenDayBeforeDOList = userRetentionResultDao.select(startTimeSevenDayBefore, endTimeSevenDayBefore);
		// ==========找出7天前的数据集(from bigdata_user_retention_result) ->end
		
		// ==========更新7天前的次日留存属性 ->start
		update(userRetentionResultSevenDayBeforeDOList, sevenDayRetentionResultMap, USERRETENTION_DAYS_ENUM.sevenDay);
		// ==========更新7天前的次日留存属性 ->end
		
		// ==========找出30天前的数据集(from bigdata_user_retention_result) ->start
		LocalDateTime startTimeThirtyDayBefore = localDate.plusDays(-30).atStartOfDay();
		LocalDateTime endTimeThirtyDayBefore = startTimeThirtyDayBefore.plusDays(1);
		List<UserRetentionResultDO> userRetentionResultThirtyDayBeforeDOList = userRetentionResultDao.select(startTimeThirtyDayBefore, endTimeThirtyDayBefore);
		// ==========找出30天前的数据集(from bigdata_user_retention_result) ->end
		
		// ==========更新30天前的次日留存属性 ->start
		update(userRetentionResultThirtyDayBeforeDOList, thirtyDayRetentionResultMap, USERRETENTION_DAYS_ENUM.thirtyDay);
		// ==========更新30天前的次日留存属性 ->end
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param userRetentionResultDOList
	 * @param oneDayRetentionResultMap key = appId	channel, value = num
	 */
	private static void update(
			List<UserRetentionResultDO> userRetentionResultDOList,
			Map<String, Integer> oneDayRetentionResultMap,
			USERRETENTION_DAYS_ENUM typeEnum
			) {
		String functionName = className + "update";
		
		if(userRetentionResultDOList == null || userRetentionResultDOList.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (userRetentionResultDOList == null) =  true");
			return;
		}
		
		for(UserRetentionResultDO userRetentionResultDO : userRetentionResultDOList) {
			Integer retentionCount = 0;
			String key = userRetentionResultDO.getAppId() + AnalysisConstants.SPLIT_REGEX + userRetentionResultDO.getChannel();
			if(oneDayRetentionResultMap == null || !oneDayRetentionResultMap.containsKey(key)) {
				
			} else {
				retentionCount = oneDayRetentionResultMap.get(key);
			}
			
			// ==========init UserRetentionResultForUpdateDO ->start
			UserRetentionResultForUpdateDO userRetentionResultForUpdateDO = new UserRetentionResultForUpdateDO();
			userRetentionResultForUpdateDO.setId(userRetentionResultDO.getId());
			
			switch (typeEnum) {
			case oneDay:
				userRetentionResultForUpdateDO.setOneDayRetentionCount(retentionCount);
				break;
			case threeDay:
				userRetentionResultForUpdateDO.setThreeDayRetentionCount(retentionCount);
				break;
			case sevenDay:
				userRetentionResultForUpdateDO.setSevenDayRetentionCount(retentionCount);
				break;
			case thirtyDay:
				userRetentionResultForUpdateDO.setThirtyDayRetentionCount(retentionCount);
				break;
			default:
				break;
			}
			// ==========init UserRetentionResultForUpdateDO ->end
			
			// ==========update UserRetentionResultForUpdateDO ->start
			UserRetentionResultDao userRetentionResultDao = DaoFactory.getUserRetentionResultDao();
			userRetentionResultDao.update(userRetentionResultForUpdateDO);
			// ==========update UserRetentionResultForUpdateDO ->end
		}
		
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param javaSparkContext
	 * @param oneDayBeforeNewUserHdfsFileDir
	 * @param todayUserMap
	 * @return return Map<String, Integer>, key = appId	channel, value = num
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Integer> getDayResultMap(
			JavaSparkContext javaSparkContext, 
			String oneDayBeforeNewUserHdfsFileDir,
			Map<String, Iterable<String>> todayUserMap
			) {
		String functionName = className + ".getDayResultMap";
		
		Map<String, Iterable<String>> newUserMap = new HashMap<String, Iterable<String>>();
		
		// ==========获取指定日期的新增用户信息的map(newUserMap) ->start
		if (!ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV) && !HadoopUtils.getIsExist(oneDayBeforeNewUserHdfsFileDir)) {
			System.out.println("warn, function = " + functionName + ", message = 没有找到当天的数据(新增用户数据)(dir = " + oneDayBeforeNewUserHdfsFileDir + ")");
		} else {
			newUserMap = javaSparkContext.textFile(oneDayBeforeNewUserHdfsFileDir)
			.mapToPair(str -> {
				String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
				return new Tuple2<String, String>(strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2], strs[0]);
			})
			.groupByKey()
			.collectAsMap();
		}
		// ==========获取指定日期的新增用户信息的map(newUserMap) ->start
		
		// ==========计算出留存用户量(oneDayResultMap) ->start
		Map<String, Integer> oneDayResultMap = new HashMap<>();
		for(String key : newUserMap.keySet()) {
			List<String> oneDayBeforeNewUserList = IteratorUtils.toList(newUserMap.get(key).iterator());
			
			List<String> todayUserList = new ArrayList<String>();
			if(todayUserMap.containsKey(key)) {
				todayUserList = IteratorUtils.toList(todayUserMap.get(key).iterator());
			}
			
			todayUserList.retainAll(oneDayBeforeNewUserList);
			Integer size = todayUserList.size();
			
			oneDayResultMap.put(key, size);
		}
		// ==========计算出留存用户量(oneDayResultMap) ->start
		
		return oneDayResultMap;
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param avgPvMap key = appId	channel, value = avg
	 * @param localDate 
	 */
	public static void insertIntoDB(Map<String, Integer> avgPvMap, Map<String, Long> newUserMap, LocalDate localDate) {
		String functionName = className + ".insertIntoDB";
		
		if(avgPvMap == null || avgPvMap.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (avgPvMap == null || avgPvMap.size() == 0) = true");
			return;
		}
		
		Date now = new Date();
		Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		for(String key : avgPvMap.keySet()) {
			Integer value = avgPvMap.get(key);
			String keyStrs[] = key.split(AnalysisConstants.SPLIT_REGEX);
			String appId = keyStrs[0];
			String channel = keyStrs[1];
			Long newUserCount = newUserMap.get(key);
			
			UserRetentionResultDO userRetentionResultDO = new UserRetentionResultDO();
			userRetentionResultDO.setAppId(appId);
			userRetentionResultDO.setAveragePv(value);
			userRetentionResultDO.setChannel(channel);
			userRetentionResultDO.setCreator(CommonConstants.CREATOR_SYSTEM);
			userRetentionResultDO.setEndTime(endTime);
			userRetentionResultDO.setGmtCreate(now);
			userRetentionResultDO.setGmtModified(now);
			userRetentionResultDO.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
			userRetentionResultDO.setModifier(CommonConstants.CREATOR_SYSTEM);
			userRetentionResultDO.setNewUserCount(newUserCount == null ? 0 : newUserCount.intValue());
			userRetentionResultDO.setOneDayRetentionCount(CommonConstants.RETENTIONCOUNT_DEFAULT);
			userRetentionResultDO.setRemark("");
			userRetentionResultDO.setSevenDayRetentionCount(CommonConstants.RETENTIONCOUNT_DEFAULT);
			userRetentionResultDO.setStartTime(startTime);
			userRetentionResultDO.setThirtyDayRetentionCount(CommonConstants.RETENTIONCOUNT_DEFAULT);
			userRetentionResultDO.setThreeDayRetentionCount(CommonConstants.RETENTIONCOUNT_DEFAULT);
			
			// ==========insert to mysql ->start
			UserRetentionResultDao userRetentionResultDao = DaoFactory.getUserRetentionResultDao();
			userRetentionResultDao.insertBeforeDelete(userRetentionResultDO);
			// ==========insert to mysql ->end
		}
		
	}
	
}
