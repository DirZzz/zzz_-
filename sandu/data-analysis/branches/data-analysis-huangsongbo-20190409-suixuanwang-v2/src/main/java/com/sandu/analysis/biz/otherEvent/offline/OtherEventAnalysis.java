package com.sandu.analysis.biz.otherEvent.offline;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.otherEvent.constant.OtherEventConstants;
import com.sandu.analysis.biz.otherEvent.dao.OtherEventAnalysisResultDao;
import com.sandu.analysis.biz.otherEvent.model.OtherEventAnalysisResultDO;
import com.sandu.analysis.biz.util.DBUtils;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;

/**
 * 其他行为事件统计
 * 
 * @author huangsongbo
 *
 */
public class OtherEventAnalysis {

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
				return;
			}
		}
		System.out.println("------hdfsFileDir = " + hdfsFileDir);
		// ==========获取与要处理的hdfs文件路径 ->end
		
		// ==========获取用户渠道信息 ->start
		// key = uuid, value = channel
		Map<String, String> channelInfoMap = DBUtils.getChannelInfoMap();
		// ==========获取用户渠道信息 ->end
		
		// 0		1								2								3					4								5				6		7		8						9					10		11			12
		// uuid	2019-04-26 11:07:15	2019-04-26 11:07:57	serviceEvent	event:replaceProduct	appId		wx		1.0.0	192.168.1.225		192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> javaRDD = javaSparkContext.textFile(hdfsFileDir)
		// 过了格式不正确/不需要的log
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			// log 格式过滤
			if(AnalysisConstants.LOG_SPLIT_LENGTH != strs.length) {
				return false;
			}
			// 内容过滤
			Map<String, String> eventMap = Utils.getMap(strs[4]);
			String code = strs[3] + AnalysisConstants.SPLIT_REGEX + eventMap.get(EVENTPROPERTY_ENUM.event.getName()) + AnalysisConstants.SPLIT_REGEX + strs[5];
			/*System.out.println(code);*/
			if(!OtherEventConstants.OTHER_EVENT_FLAG_LIST.contains(code)) {
				return false;
			}
			
			return true;
		})
		// 0		1					2			3
		// uuid	eventCode		appid	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			Map<String, String> eventMap = Utils.getMap(strs[4]);
			String code = strs[3] + AnalysisConstants.SPLIT_REGEX + eventMap.get(EVENTPROPERTY_ENUM.event.getName()) + AnalysisConstants.SPLIT_REGEX + strs[5];
			String eventCode = OtherEventConstants.OTHER_EVENT_FLAG_MAP.get(code);
			String channel = channelInfoMap.containsKey(strs[0]) ? channelInfoMap.get(strs[0]) : CommonConstants.DEFAULT_CHANNEL;
			return strs[0] + AnalysisConstants.SPLIT_REGEX + eventCode + AnalysisConstants.SPLIT_REGEX + strs[5] + AnalysisConstants.SPLIT_REGEX + channel;
		})
		.cache();
		
		// ==========pv ->start
		// 0		1					2			3
		// uuid	eventCode		appid	channel
		Map<String, Long> pvResultMap = javaRDD
		// 0					1			2
		// eventCode		appid	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		System.out.println("pvResultMap = " + pvResultMap);
		// ==========pv ->end
		
		// ==========uv ->start
		// 0		1					2			3
		// uuid	eventCode		appid	channel
		Map<String, Long> uvResultMap = javaRDD
		.distinct()
		// 0					1			2
		// eventCode		appid	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] 
					+ AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX + strs[3];
		})
		.countByValue();
		System.out.println("uvResultMap = " + uvResultMap);
		// ==========uv ->end
		
		// ==========result insert into mysql -> start
		InsertIntoDB(pvResultMap, uvResultMap, localDate);
		// ==========result insert into mysql -> end
	}

	private static void InsertIntoDB(Map<String, Long> pvResultMap, Map<String, Long> uvResultMap,
			LocalDate localDate) {
		// ==========整合结果集 ->start
		//          0					1			2                       0		1
		// key = eventCode	appId	channel, value = pv	uv
		Map<String, String> pvUvResult = getPvUvResult(pvResultMap, uvResultMap);
		// ==========整合结果集 ->end
		
		if(pvUvResult == null || pvUvResult.size() == 0) {
			System.out.println("warn, function = OtherEventAnalysis.InsertIntoDB, message = (pvUvResult == null || pvUvResult.size() == 0) = true");
			return;
		}
		
		Date now = new Date();
		Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		List<OtherEventAnalysisResultDO> list = new ArrayList<OtherEventAnalysisResultDO>();
		for(String key : pvUvResult.keySet()) {
			String value = pvUvResult.get(key);
			String[] strs = key.split(AnalysisConstants.SPLIT_REGEX);
			String[] valueStrs = value.split(AnalysisConstants.SPLIT_REGEX);
			String appId = strs[1];
			String channel = strs[2];
			String eventCode = strs[0];
			Integer pv = Integer.valueOf(valueStrs[0]);
			Integer uv = Integer.valueOf(valueStrs[1]);
			
			OtherEventAnalysisResultDO otherEventAnalysisResultDO = new OtherEventAnalysisResultDO();
			otherEventAnalysisResultDO.setAppId(appId);
			otherEventAnalysisResultDO.setChannel(channel);
			otherEventAnalysisResultDO.setCreator(CommonConstants.CREATOR_SYSTEM);
			otherEventAnalysisResultDO.setEndTime(endTime);
			otherEventAnalysisResultDO.setEventCode(eventCode);
			otherEventAnalysisResultDO.setGmtCreate(now);
			otherEventAnalysisResultDO.setGmtModified(now);
			otherEventAnalysisResultDO.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
			otherEventAnalysisResultDO.setModifier(CommonConstants.CREATOR_SYSTEM);
			otherEventAnalysisResultDO.setPv(pv);
			otherEventAnalysisResultDO.setRemark("");
			otherEventAnalysisResultDO.setStartTime(startTime);
			otherEventAnalysisResultDO.setUv(uv);
			list.add(otherEventAnalysisResultDO);
		}
		
		OtherEventAnalysisResultDao otherEventAnalysisResultDao = DaoFactory.getOtherEventAnalysisResultDao();
		otherEventAnalysisResultDao.insertBeforeDelete(list);
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param pvResultMap
	 * @param uvResultMap
	 * @return key = eventCode	appId	channel, value = pv	uv
	 */
	private static Map<String, String> getPvUvResult(Map<String, Long> pvResultMap, Map<String, Long> uvResultMap) {
		if(pvResultMap == null || pvResultMap.size() == 0) {
			System.out.println("warn, function = PageAnalysis.getPvUvResult, message = (pvResultMap == null || pvResultMap.size() == 0) = true");
			return null;
		}
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		for(String key : pvResultMap.keySet()) {
			Long pv = pvResultMap.get(key);
			Long uv = uvResultMap.get(key);
			resultMap.put(key, (pv == null ? 0 : pv) + AnalysisConstants.SPLIT_REGEX + (uv == null ? 0 : uv));
		}
		
		return resultMap;
	}
	
}
