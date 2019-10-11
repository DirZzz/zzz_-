package com.sandu.analysis.biz.page.offline;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTCODE_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTNAME_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.page.dao.PageViewResultDao;
import com.sandu.analysis.biz.page.model.PageViewResultDO;
import com.sandu.analysis.biz.util.DBUtils;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.UserUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;

import scala.Tuple2;

public class PageAnalysis {

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
				System.out.println("warn, function = PageAnalysis.main, message = hdfs中对应目录不存在, dir = " + hdfsFileDir);
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
			// update by huangsongbo 2019.07.17 需要统计退出率, 所以要获取登录事件
			if(StringUtils.equals(strs[3], EVENTNAME_ENUM.pageview.getName())
					// 或者是登录事件
					|| (
							StringUtils.equals(strs[3], EVENTNAME_ENUM.pageevent.getName()) &&
							StringUtils.equals(EVENTCODE_ENUM.SIGN_IN.getValue(), Utils.getMap(strs[4]).get(EVENTPROPERTY_ENUM.event.getName()))
							)
					) {
				return true;
			} else {
				return false;
			}
		})
		// 0		1			2			3				4		5
		// uuid	event		appId	channel		time	eventType
		// 到这一步, rdd中的eventType = pageevent 就是登录事件
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			Map<String, String> eventMap = Utils.getMap(strs[4]);
			String event = eventMap.get(EVENTPROPERTY_ENUM.curpage.getName());
			String channel = channelInfoMap.containsKey(strs[0]) ? channelInfoMap.get(strs[0]) : CommonConstants.DEFAULT_CHANNEL;
			Long time = LocalDateTime.parse(strs[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toInstant(ZoneOffset.of("+8")).toEpochMilli();
			return strs[0] 
					+ AnalysisConstants.SPLIT_REGEX + event 
					+ AnalysisConstants.SPLIT_REGEX + strs[5] 
					+ AnalysisConstants.SPLIT_REGEX + channel
					+ AnalysisConstants.SPLIT_REGEX + time
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		// 准备计算timeOnPage
		//                                        0		1			2			3				4		5
		// key = uuid	appId, value = uuid	event		appId	channel		time	eventType
		.mapToPair(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return new Tuple2<String, String>(strs[0] + AnalysisConstants.SPLIT_REGEX + strs[3], str);
		})
		.groupByKey()
		// 	                                       0		1			2			3				4					5
		// key = uuid	appId, value = [uuid	event		appId	channel		timeOnPage	bounceStatus]
		// bounceStatus: 是否是退出前最后的页面, 1 = true; 0 = false;
		.mapValues(strIterable -> {
			List<String> resultList = new ArrayList<String>();
			@SuppressWarnings("unchecked")
			List<String> list = IteratorUtils.toList(strIterable.iterator());
			// 排序
			list.sort((o1, o2) -> {
				long time1 = Long.parseLong(o1.split(AnalysisConstants.SPLIT_REGEX)[4]);
				long time2 = Long.parseLong(o2.split(AnalysisConstants.SPLIT_REGEX)[4]);
				return (int) (time1 - time2);
			});
			
			String previousStr = null;
			for(String str : list) {
				// 如果是登录事件, 则上一个事件是退出前页面 add by huangsongbo 2019.07.17
				// 是否是登录事件
				boolean isSignIn = false;
				Integer bounceStatus = 0;
				
				if (previousStr == null) {
					
				} else {
					String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
					
					if (StringUtils.equals(EVENTNAME_ENUM.pageevent.getName(), strs[5])) {
						isSignIn = true;
						bounceStatus = 1;
					}
					
					String[] previousStrs = previousStr.split(AnalysisConstants.SPLIT_REGEX);
					// 停留时间, 大于10分钟则标记为"从该页面退出", 停留时间记做0
					long timeOnPage = Long.valueOf(strs[4]) - Long.valueOf(previousStrs[4]);
					if(timeOnPage > AnalysisConstants.TIME_OVERTIME) {
						timeOnPage = 0L;
					}
					String resultStr = previousStrs[0] 
							+ AnalysisConstants.SPLIT_REGEX + previousStrs[1] 
							+ AnalysisConstants.SPLIT_REGEX + previousStrs[2] 
							+ AnalysisConstants.SPLIT_REGEX + previousStrs[3] 
							+ AnalysisConstants.SPLIT_REGEX + timeOnPage
							+ AnalysisConstants.SPLIT_REGEX + bounceStatus;
					
					resultList.add(resultStr);
				}
				if (isSignIn) {
					previousStr = null;
				} else {
					previousStr = str;
				}
			}
			
			// 最后一条记录也要存, 只不过time = null
			
			String[] strs = list.get(list.size() - 1).split(AnalysisConstants.SPLIT_REGEX);
			String lastOneStr = strs[0] 
					+ AnalysisConstants.SPLIT_REGEX + strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2] 
					+ AnalysisConstants.SPLIT_REGEX + strs[3] 
					+ AnalysisConstants.SPLIT_REGEX + 0
					// 该用户的最后一条记录, 视为退出事件
					+ AnalysisConstants.SPLIT_REGEX + 1;
			resultList.add(lastOneStr);
			return resultList;
		})
		// 0		1			2			3				4					5
		// uuid	event		appId	channel		timeOnPage	bounceStatus
		/*.flatMap(f -> f._2.iterator())*/
		.flatMap(f -> {
			return f._2.stream().collect(Collectors.toList());
		})
		// ==========识别新用户, add by huangsongbo 2019.07.11, 新用户标记 = 1, else = 0 ->start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
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
		
		// javaRDD 为所有用户日志
		// newUserJavaRDD 为新用户日志
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		JavaRDD<String> newUserJavaRDD = javaRDD.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (StringUtils.equals(strs[6], "1")) {
				return true;
			}
			return false;
		}).cache();
		
		// ==========pv -> start
		/*Map<String, Long> pvResult = javaRDDForPvUv.countByValue();*/
		
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> pvResultMap = javaRDD
		// 0				1			2
		// event			appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		/*System.out.println("pvResult = " + pvResult);*/
		// ==========pv -> end
		
		// ==========new user pv (add by huangsongbo 2019.07.11) ->start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> newUserPvResultMap = newUserJavaRDD
		// 0				1			2
		// event			appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		// ==========new user pv (add by huangsongbo 2019.07.11)->end
		
		// ==========bounce_pv(当前页面退出次数) ->start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> bouncePvResultMap = javaRDD
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (StringUtils.equals("1", strs[5])) {
				/*System.out.println("str = " + str);*/
				return true;
			}
			return false;
		})
		// 0				1			2
		// event			appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		/*System.out.println("bouncePvResultMap = " + bouncePvResultMap);*/
		// ==========bounce_pv(当前页面退出次数) ->end
		
		// ==========bounce_npv(当前页面退出次数) ->start
		Map<String, Long> bounceNpvResultMap = newUserJavaRDD
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (StringUtils.equals("1", strs[5])) {
				return true;
			}
			return false;
		})
		// 0				1			2
		// event			appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		// ==========bounce_npv(当前页面退出次数) ->end
		
		// ==========uv -> start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> uvResultMap = javaRDD
		// 0		1			2			3					
		// uuid	event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[0] 
					+ AnalysisConstants.SPLIT_REGEX + strs[1]
					+ AnalysisConstants.SPLIT_REGEX + strs[2] 
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.distinct()
		// 0			1			2
		// event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		/*System.out.println("uvResult = " + uvResult);*/
		// ==========uv -> end
		
		// ==========newUser uv (add by huangsongbo 2019.07.11) -> start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> newUserUvResultMap = newUserJavaRDD
		// 0		1			2			3					
		// uuid	event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[0] 
					+ AnalysisConstants.SPLIT_REGEX + strs[1]
					+ AnalysisConstants.SPLIT_REGEX + strs[2] 
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.distinct()
		// 0			1			2
		// event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		/*System.out.println("uvResult = " + uvResult);*/
		// ==========newUser uv (add by huangsongbo 2019.07.11) -> end
		
		// ==========bounce_uv ->start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> bounceUvResultMap = javaRDD
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (StringUtils.equals("1", strs[5])) {
				return true;
			}
			return false;
		})
		// 0		1			2			3					
		// uuid	event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[0] 
					+ AnalysisConstants.SPLIT_REGEX + strs[1]
					+ AnalysisConstants.SPLIT_REGEX + strs[2] 
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.distinct()
		// 0			1			2
		// event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		// ==========bounce_uv ->end
		
		// ==========bounce_nuv ->start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> bounceNuvResultMap = newUserJavaRDD
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (StringUtils.equals("1", strs[5])) {
				return true;
			}
			return false;
		})
		// 0		1			2			3					
		// uuid	event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[0] 
					+ AnalysisConstants.SPLIT_REGEX + strs[1]
					+ AnalysisConstants.SPLIT_REGEX + strs[2] 
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.distinct()
		// 0			1			2
		// event		appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		// ==========bounce_nuv ->end
		
		// ==========平均页面停留时间 ->start
		// 0		1			2			3				4					5						6
		// uuid	event		appId	channel		timeOnPage	bounceStatus 	isNewUser
		Map<String, Long> timeOnPageResultMap = javaRDD
		// key = event		appId	channel, value = timeOnPage
		.mapToPair(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return new Tuple2<String, Long>(
					strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2] 
					+ AnalysisConstants.SPLIT_REGEX + strs[3]
					, 
					Long.parseLong(strs[4]));
		})
		.mapValues(timeOnPage -> new Tuple2<Long, Long>(timeOnPage, 1L))
		.reduceByKey((t1, t2) -> new Tuple2<>(t1._1 + t2._1, t1._2 + t2._2))
		.mapValues(t -> t._1/(t._2 * 1000))
		.collectAsMap();
		/*System.out.println("timeOnPageMap = " + timeOnPageMap);*/
		// ==========平均页面停留时间 ->end
		
		// ==========result insert into mysql -> start
		InsertIntoDB(
				pvResultMap, uvResultMap, newUserPvResultMap, newUserUvResultMap, timeOnPageResultMap, 
				bouncePvResultMap, bounceNpvResultMap, bounceUvResultMap, bounceNuvResultMap,
				localDate);
		// ==========result insert into mysql -> end
	}

	private static void InsertIntoDB(
			Map<String, Long> pvResultMap, 
			Map<String, Long> uvResultMap, 
			Map<String, Long> newUserPvResultMap, 
			Map<String, Long> newUserUvResultMap, 
			Map<String, Long> timeOnPageResultMap, 
			Map<String, Long> bouncePvResultMap, 
			Map<String, Long> bounceNpvResultMap, 
			Map<String, Long> bounceUvResultMap, 
			Map<String, Long> bounceNuvResultMap, 
			LocalDate localDate) {
		// ==========整合结果集 ->start
		//          0			1			2                       0		1		2					3					4					5				6					7				8
		// key = event	appId	channel, value = pv	uv		timeOnPage	newUserPv		newUserUv	bouncePv	bounceNpv	bounceUv	bounceNuv
		Map<String, String> pvUvResult = getPvUvResult(
				pvResultMap, uvResultMap, newUserPvResultMap, newUserUvResultMap, timeOnPageResultMap,
				bouncePvResultMap, bounceNpvResultMap, bounceUvResultMap, bounceNuvResultMap
				);
		// ==========整合结果集 ->end
		
		if(pvUvResult == null || pvUvResult.size() == 0) {
			System.out.println("warn, function = PageAnalysis.InsertIntoDB, message = (pvUvResult == null || pvUvResult.size() == 0) = true");
			return;
		}
		
		List<PageViewResultDO> pageViewResultDOList = new ArrayList<PageViewResultDO>();
		
		Date now = new Date();
		Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		for(String key : pvUvResult.keySet()) {
			String value = pvUvResult.get(key);
			String[] strs = key.split(AnalysisConstants.SPLIT_REGEX);
			String[] valueStrs = value.split(AnalysisConstants.SPLIT_REGEX);
			String eventProperty = strs[0];
			String appId = strs[1];
			String channel = strs[2];
			Integer pv = Integer.valueOf(valueStrs[0]);
			Integer uv = Integer.valueOf(valueStrs[1]);
			Integer timeOnPage = Integer.valueOf(valueStrs[2]);
			Integer newUserPv = Integer.valueOf(valueStrs[3]);
			Integer newUserUv = Integer.valueOf(valueStrs[4]);
			Integer bouncePv = Integer.valueOf(valueStrs[5]);
			Integer bounceNpv = Integer.valueOf(valueStrs[6]);
			Integer bounceUv = Integer.valueOf(valueStrs[7]);
			Integer bounceNuv = Integer.valueOf(valueStrs[8]);
			
			PageViewResultDO pageViewResultDO = new PageViewResultDO();
			pageViewResultDO.setAppId(appId);
			pageViewResultDO.setChannel(channel);
			pageViewResultDO.setCreator(CommonConstants.CREATOR_SYSTEM);
			pageViewResultDO.setEndTime(endTime);
			pageViewResultDO.setGmtCreate(now);
			pageViewResultDO.setGmtModified(now);
			pageViewResultDO.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
			pageViewResultDO.setModifier(CommonConstants.CREATOR_SYSTEM);
			pageViewResultDO.setPageProperty(eventProperty);
			pageViewResultDO.setPv(pv);
			pageViewResultDO.setRemark("");
			pageViewResultDO.setStartTime(startTime);
			pageViewResultDO.setTimeOnPage(timeOnPage);
			pageViewResultDO.setUv(uv);
			pageViewResultDO.setNpv(newUserPv);
			pageViewResultDO.setNuv(newUserUv);
			pageViewResultDO.setBounceNpv(bounceNpv);
			pageViewResultDO.setBounceNuv(bounceNuv);
			pageViewResultDO.setBouncePv(bouncePv);
			pageViewResultDO.setBounceUv(bounceUv);
			pageViewResultDOList.add(pageViewResultDO);
		}
		
		PageViewResultDao pageViewResultDao = DaoFactory.getPageViewResultDao();
		pageViewResultDao.insertBeforeDelete(pageViewResultDOList);
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param pvResultMap
	 * @param uvResultMap
	 * @param timeOnPageResultMap
	 * @param bounceNuvResultMap 
	 * @param bounceUvResultMap 
	 * @param bounceNpvResultMap 
	 * @param bouncePvResultMap 
	 * @return key = event	appId	channel, value = pv	uv		timeOnPage	newUserPv		newUserUv	bouncePv	bounceNpv	bounceUv	bounceNuv
	 */
	private static Map<String, String> getPvUvResult(
			Map<String, Long> pvResultMap, 
			Map<String, Long> uvResultMap,
			Map<String, Long> newUserPvResultMap, 
			Map<String, Long> newUserUvResultMap, 
			Map<String, Long> timeOnPageResultMap, 
			Map<String, Long> bouncePvResultMap, 
			Map<String, Long> bounceNpvResultMap, 
			Map<String, Long> bounceUvResultMap, 
			Map<String, Long> bounceNuvResultMap
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
			Long timeOnPage = timeOnPageResultMap.get(key);
			Long bouncePv = bouncePvResultMap.get(key);
			Long bounceNpv = bounceNpvResultMap.get(key);
			Long bounceUv = bounceUvResultMap.get(key);
			Long bounceNuv = bounceNuvResultMap.get(key);
			
			resultMap.put(key, 
					(pv == null ? 0 : pv) +
					AnalysisConstants.SPLIT_REGEX + (uv == null ? 0 : uv) + 
					AnalysisConstants.SPLIT_REGEX + (timeOnPage == null ? 0 : timeOnPage) + 
					AnalysisConstants.SPLIT_REGEX + (newUserPv == null ? 0 : newUserPv) + 
					AnalysisConstants.SPLIT_REGEX + (newUserUv == null ? 0 : newUserUv) + 
					AnalysisConstants.SPLIT_REGEX + (bouncePv == null ? 0 : bouncePv) + 
					AnalysisConstants.SPLIT_REGEX + (bounceNpv == null ? 0 : bounceNpv) + 
					AnalysisConstants.SPLIT_REGEX + (bounceUv == null ? 0 : bounceUv) + 
					AnalysisConstants.SPLIT_REGEX + (bounceNuv == null ? 0 : bounceNuv)
					);
		}
		
		return resultMap;
	}
	
}