package com.sandu.analysis.biz.page.offline;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTNAME_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.page.dao.ButtonClickResultDao;
import com.sandu.analysis.biz.page.model.ButtonClickResultDO;
import com.sandu.analysis.biz.util.DBUtils;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.UserUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;

public class BtnClickAnalysis {

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
			hdfsFileDir = "C:/Users/Administrator/Desktop/hdfs/";
		} else {
			hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_EVENTS_DIR + dataDirInfo;
			// 检测hdfs中有没有这个目录, 如果没有则创建目录
			boolean isExist = HadoopUtils.getIsExist(hdfsFileDir);
			if(!isExist) {
				System.out.println("warn, function = BtnClickAnalysis.main, message = hdfs中对应目录不存在, dir = " + hdfsFileDir);
				javaSparkContext.close();
				return;
			}
		}
		System.out.println("------hdfsFileDir = " + hdfsFileDir);
		// ==========获取与要处理的hdfs文件路径 ->end
		
		// ==========获取用户渠道信息 ->start
		// key = uuid, value = channel
		Map<String, String> channelInfoMap = DBUtils.getChannelInfoMap();
		// ==========获取用户渠道信息 ->end
		
		// 获取新用户uuid list add by huangsongbo 2019.07.11
		List<String> newUserUuidList = UserUtils.getNewUserUuidList(localDate, javaSparkContext);
		
		// ==========读取并先处理一次日志格式 -> start
		// 0		1								2								3				4															5			6	7		8					9					10		11			12
		// uuid	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	refpage:shoes,curpage:pages/home/A	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> javaRDD = javaSparkContext.textFile(hdfsFileDir)
		// 过滤格式不正确的log
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			// log 格式过滤
			if(AnalysisConstants.LOG_SPLIT_LENGTH != strs.length) {
				return false;
			}
			// 只要pageview类型的事件
			if(!StringUtils.equals(strs[3], EVENTNAME_ENUM.btnclick.getName())) {
				return false;
			}
			return true;
		})
		// 0		1				2						3			4
		// uuid	curpage	buttonProperty	appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			Map<String, String> eventMap = Utils.getMap(strs[4]);
			String curpage = eventMap.get(EVENTPROPERTY_ENUM.curpage.getName());
			String buttonProperty = eventMap.get(EVENTPROPERTY_ENUM.btnid.getName());
			String channel = channelInfoMap.containsKey(strs[0]) ? channelInfoMap.get(strs[0]) : CommonConstants.DEFAULT_CHANNEL;
			return strs[0] 
					+ AnalysisConstants.SPLIT_REGEX + curpage 
					+ AnalysisConstants.SPLIT_REGEX + buttonProperty
					+ AnalysisConstants.SPLIT_REGEX + strs[5]
					+ AnalysisConstants.SPLIT_REGEX + channel;
		})
		// ==========识别新用户, add by huangsongbo 2019.07.11, 新用户标记 = 1, else = 0 ->start
		// 0		1				2						3			4				5
		// uuid	curpage	buttonProperty	appId	channel		isNewUser
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			String uuid = strs[0];
			
			if (newUserUuidList.contains(uuid)) {
				str += (AnalysisConstants.SPLIT_REGEX + "1");
			} else {
				str += (AnalysisConstants.SPLIT_REGEX + "0");
			}
			return str;
		})
		// ==========识别新用户, add by huangsongbo 2019.07.11, 新用户标记 = 1, else = 0 ->end
		.cache();
		
		/*System.out.println("javaRDD = " + javaRDD.collect());*/
		
		// ==========pv -> start
		// 0		1				2						3			4				5
		// uuid	curpage	buttonProperty	appId	channel		isNewUser
		Map<String, Long> pvResultMap = javaRDD
		// 0				1						2			4
		// curpage		buttonProperty	appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3]
					+ AnalysisConstants.SPLIT_REGEX + strs[4];
		})
		.countByValue();
		/*System.out.println("pvResultMap = " + pvResultMap);*/
		// ==========pv -> end
		
		// ==========newUser pv (add by huangsongbo 2019.07.11) ->start
		// 0		1				2						3			4				5
		// uuid	curpage	buttonProperty	appId	channel		isNewUser
		Map<String, Long> newUserPvResultMap = javaRDD
		// 只要新用户信息
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (StringUtils.equals(strs[5], "1")) {
				return true;
			}
			return false;
		})
		// 0				1						2			4
		// curpage		buttonProperty	appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3]
					+ AnalysisConstants.SPLIT_REGEX + strs[4];
		})
		.countByValue();
		// ==========newUser pv (add by huangsongbo 2019.07.11) ->end
		
		// ==========uv -> start
		// 0		1				2						3			4				5
		// uuid	curpage	buttonProperty	appId	channel		isNewUser
		Map<String, Long> uvResultMap =javaRDD
		.distinct()
		// 0				1						2			3			
		// curpage		buttonProperty	appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3]
					+ AnalysisConstants.SPLIT_REGEX + strs[4];
		})
		.countByValue();
		/*System.out.println("uvResultMap = " + uvResultMap);*/
		// ==========uv -> end
		
		// ==========newUser uv (add by huangsongbo 2019.07.11) ->start
		// 0		1				2						3			4				5
		// uuid	curpage	buttonProperty	appId	channel		isNewUser
		Map<String, Long> newUserUvResultMap =javaRDD
		// 只要新用户信息
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (StringUtils.equals(strs[5], "1")) {
				return true;
			}
			return false;
		})
		.distinct()
		// 0				1						2			3			
		// curpage		buttonProperty	appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3]
					+ AnalysisConstants.SPLIT_REGEX + strs[4];
		})
		.countByValue();
		/*System.out.println("uvResultMap = " + uvResultMap);*/
		// ==========newUser uv (add by huangsongbo 2019.07.11) ->end
		
		// ==========result insert into mysql -> start
		InsertIntoDB(pvResultMap, uvResultMap, newUserPvResultMap, newUserUvResultMap, localDate);
		// ==========result insert into mysql -> end
	}

	private static void InsertIntoDB(
			Map<String, Long> pvResultMap, 
			Map<String, Long> uvResultMap, 
			Map<String, Long> newUserPvResultMap, 
			Map<String, Long> newUserUvResultMap, 
			LocalDate localDate) {
		
		// ==========整合结果集 ->start
		//          0				1						2          3                       0		1		2					3
		// key = curpage	buttonProperty	appId	channel, value = pv	uv		newUserPv		newUserUv
		Map<String, String> pvUvResult = getPvUvResult(pvResultMap, uvResultMap, newUserPvResultMap, newUserUvResultMap);
		// ==========整合结果集 ->end
		
		if(pvUvResult == null || pvUvResult.size() == 0) {
			System.out.println("warn, function = BtnClickAnalysis.InsertIntoDB, message = (pvUvResult == null || pvUvResult.size() == 0) = true");
			return;
		}
		
		List<ButtonClickResultDO> buttonClickResultDOList = new ArrayList<ButtonClickResultDO>();
		
		Date now = new Date();
		Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		for(String key : pvUvResult.keySet()) {
			String value = pvUvResult.get(key);
			String[] strs = key.split(AnalysisConstants.SPLIT_REGEX);
			String[] valueStrs = value.split(AnalysisConstants.SPLIT_REGEX);
			String curpage = strs[0];
			String buttonProperty = strs[1];
			String appId = strs[2];
			String channel = strs[3];
			Integer pv = Integer.valueOf(valueStrs[0]);
			Integer uv = Integer.valueOf(valueStrs[1]);
			Integer newUserPv = Integer.valueOf(valueStrs[2]);
			Integer newUserUv = Integer.valueOf(valueStrs[3]);
			
			ButtonClickResultDO buttonClickResultDO = new ButtonClickResultDO();
			buttonClickResultDO.setAppId(appId);
			buttonClickResultDO.setButtonProperty(buttonProperty);
			buttonClickResultDO.setChannel(channel);
			buttonClickResultDO.setCreator(CommonConstants.CREATOR_SYSTEM);
			buttonClickResultDO.setCurrentPageProperty(curpage);
			buttonClickResultDO.setEndTime(endTime);
			buttonClickResultDO.setGmtCreate(now);
			buttonClickResultDO.setGmtModified(now);
			buttonClickResultDO.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
			buttonClickResultDO.setModifier(CommonConstants.CREATOR_SYSTEM);
			buttonClickResultDO.setPv(pv);
			buttonClickResultDO.setRemark("");
			buttonClickResultDO.setStartTime(startTime);
			buttonClickResultDO.setUv(uv);
			buttonClickResultDO.setNpv(newUserPv);
			buttonClickResultDO.setNuv(newUserUv);
			
			buttonClickResultDOList.add(buttonClickResultDO);
		}
		
		ButtonClickResultDao buttonClickResultDao = DaoFactory.getButtonClickResultDao();
		buttonClickResultDao.insertBeforeDelete(buttonClickResultDOList);
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param pvResultMap
	 * @param uvResultMap
	 * @param newUserUvResultMap 
	 * @param newUserPvResultMap 
	 * @param timeOnPageResultMap
	 * @return key = event	appId	channel, value = pv	uv		newUserPv		newUserUv
	 */
	private static Map<String, String> getPvUvResult(
			Map<String, Long> pvResultMap, 
			Map<String, Long> uvResultMap, 
			Map<String, Long> newUserPvResultMap, 
			Map<String, Long> newUserUvResultMap
			) {
		if(pvResultMap == null || pvResultMap.size() == 0) {
			System.out.println("warn, function = PageAnalysis.getPvUvResult, message = (pvResultMap == null || pvResultMap.size() == 0) = true");
			return null;
		}
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		for(String key : pvResultMap.keySet()) {
			Long pv = pvResultMap.get(key);
			Long uv = uvResultMap.get(key);
			Long newUserPv = newUserPvResultMap.get(key);
			Long newUserUv = newUserUvResultMap.get(key);
			resultMap.put(key, 
					(pv == null ? 0 : pv) + 
					AnalysisConstants.SPLIT_REGEX + (uv == null ? 0 : uv) +
					AnalysisConstants.SPLIT_REGEX + (newUserPv == null ? 0 : newUserPv) +
					AnalysisConstants.SPLIT_REGEX + (newUserUv == null ? 0 : newUserUv)
					);
		}
		
		return resultMap;
	}
	
}
