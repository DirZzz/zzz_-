package com.sandu.analysis.biz.tob.house.offline;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.tob.house.constant.HouseEventConstants;
import com.sandu.analysis.biz.tob.house.dao.HouseStatisticsDao;
import com.sandu.analysis.biz.tob.house.model.HouseInfoDto;
import com.sandu.analysis.biz.tob.house.model.HouseStatisticsDayDto;
import com.sandu.analysis.biz.tob.house.model.HouseStatisticsHourDto;
import com.sandu.analysis.biz.tob.house.model.HouseUsageAmountStatisticsDayDto;
import com.sandu.analysis.biz.util.DBUtils;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 户型使用数据统计
 * 
 * @author xiaoxc
 *
 */
public class HouseStatisticsResult {

	private static Map<String, String> areaMap = new HashMap<>(200);

	@SuppressWarnings("All")
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
		
		// 0		1						2				3					4		 5			6		7		8						9					10		11			12
		// uuid	2019-04-26 11:07:15	2019-04-26 11:07:57	serviceEvent	event:useHouse	appId		wx		1.0.0	192.168.1.225		192.168.3.97	中国	广东省	深圳市
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
			if(!HouseEventConstants.HOUSE_EVENT_FLAG_LIST.contains(code)) {
				return false;
			}
			
			return true;
		})

		//  	0		1
		// serverTime houseId
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			Map<String, String> eventMap = Utils.getMap(strs[4]);
			String code = strs[3] + AnalysisConstants.SPLIT_REGEX + eventMap.get(EVENTPROPERTY_ENUM.event.getName()) + AnalysisConstants.SPLIT_REGEX + strs[5];
			String eventCode = HouseEventConstants.HOUSE_EVENT_FLAG_MAP.get(code);
			StringBuilder builder = new StringBuilder();
			builder.append(strs[1]).append(AnalysisConstants.SPLIT_REGEX);
			//String currentDay = Utils.getIntradayStartTime(strs[1], null);
			//builder.append(currentDay).append(AnalysisConstants.SPLIT_REGEX);
			//builder.append(eventCode).append(AnalysisConstants.SPLIT_REGEX);
			String houseId = eventMap.get("houseId");
			if (StringUtils.isBlank(houseId)) {
				return null;
			}
			builder.append(houseId).append(AnalysisConstants.SPLIT_REGEX);
			//
			return builder.toString();
		})
		.cache();
		System.out.println("javaRDD : " + javaRDD.collect());

		JavaRDD<String> dayJavaRDD = javaRDD;
		JavaRDD<String> hourJavaRDD = javaRDD;
		JavaRDD<String> hotHourJavaRDD = javaRDD;

		//获取户型省市code
		Map<Integer, HouseInfoDto> houseAreaMap = getHouseAreaMap(javaRDD);

		//统计户型数据(按天)
		statisticsHouseDataByDay(dayJavaRDD, houseAreaMap, localDate);

		//统计户型数据(按每小时)
		statisticsHouseDataByHour(hourJavaRDD, houseAreaMap, localDate);

		//统计热门户型数据
		statisticsHotHouseDataByDay(hotHourJavaRDD, houseAreaMap, localDate);



	}

	/**
	 * 按天统计户型数据
	 * @param javaRDD
	 * @param localDate
	 */
	private static void  statisticsHouseDataByDay(JavaRDD<String> javaRDD, Map<Integer, HouseInfoDto> houseAreaMap, LocalDate localDate) {
		//同一个户型使用多次就需要记录多次
		// 		0		  1
		//provinceCode cityCode
		javaRDD = javaRDD.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			Integer houseId = Integer.valueOf(strs[1]);
			//获取省市  /除去houseId便于统计
			StringBuilder builder = new StringBuilder("");
			if (houseAreaMap.containsKey(houseId)) {
				HouseInfoDto houseInfoDto = houseAreaMap.get(houseId);
				builder.append(houseInfoDto.getProvinceCode()).append(AnalysisConstants.SPLIT_REGEX);
				builder.append(houseInfoDto.getCityCode()).append(AnalysisConstants.SPLIT_REGEX);
			}
			if (StringUtils.isBlank(builder.toString())) {
				return null;
			}
			return builder.toString();
		}).filter(str -> {
			if (StringUtils.isBlank(str)) {
				return false;
			}
			return true;
		}).cache();

		System.out.println("javaRDD : " + javaRDD.collect());

		//根据省市分组获取使用量
		Map<String, Long> houseDayResultMap = javaRDD
				.mapToPair(str -> {
					return new Tuple2<String, Long>(str, 1L);
				}).reduceByKey((s1,s2) -> s1 + s2)
				.collectAsMap();
		System.out.println("houseDayResultMap = " + houseDayResultMap);

		//插入数据
		insertByDay(houseDayResultMap, localDate);
	}

	/**
	 * 当天按小时统计户型数据
	 * @param javaRDD
	 */
	private static void statisticsHouseDataByHour(JavaRDD<String> javaRDD, Map<Integer, HouseInfoDto> houseAreaMap, LocalDate localDate) {
		//同一个户型使用多次就需要记录多次
		// 	0			1		2		  3
		//startTime	endTime  provinceCode cityCode
		javaRDD = javaRDD.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			Integer houseId = Integer.valueOf(strs[1]);

			StringBuilder builder = new StringBuilder("");
			Map<String, String> hourMap = Utils.getBetweenHour(strs[0]);
			if (null != hourMap) {
				builder.append(hourMap.get("startTime")).append(AnalysisConstants.SPLIT_REGEX);
				builder.append(hourMap.get("endTime")).append(AnalysisConstants.SPLIT_REGEX);
			} else {
				System.out.println("disposeHouseDataByhour.hourMap is null!");
				return null;
			}
			if (houseAreaMap.containsKey(houseId)) {
				HouseInfoDto houseInfoDto = houseAreaMap.get(houseId);
				builder.append(houseInfoDto.getProvinceCode()).append(AnalysisConstants.SPLIT_REGEX);
				builder.append(houseInfoDto.getCityCode()).append(AnalysisConstants.SPLIT_REGEX);
			}
			if (StringUtils.isBlank(builder.toString())) {
				return null;
			}
			return builder.toString();
		}).filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (strs.length < 4) {
				return false;
			}
			return true;
		}).cache();

		System.out.println("hourJavaRDD : " + javaRDD.collect());

		//根据每小时省市分组获取使用量
		Map<String, Long> houseHourResultMap = javaRDD
				.mapToPair(str -> {
					return new Tuple2<String, Long>(str, 1L);
				}).reduceByKey((s1,s2) -> s1 + s2)
				.collectAsMap();
		System.out.println("houseHourResultMap = " + houseHourResultMap.size());

		//插入数据
		insertByHour(houseHourResultMap, localDate);
	}

	/**
	 * 统计户型使用次数
	 * @param javaRDD
	 */
	private static void statisticsHotHouseDataByDay(JavaRDD<String> javaRDD, Map<Integer, HouseInfoDto> houseAreaMap, LocalDate localDate) {

		Map<String, Long> houseUseResultMap = javaRDD
				.map(str -> {
					String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
					String houseId = strs[1];
					if (StringUtils.isBlank(houseId)) {
						return null;
					}
					return houseId;
				}).mapToPair(str -> {
					return new Tuple2<String, Long>(str, 1L);
				}).reduceByKey((s1,s2) -> s1 + s2)
				.collectAsMap();

		//System.out.println(houseUseResultMap.size());
		//插入数据
		insertByDayHotHouse(houseUseResultMap, houseAreaMap, localDate);

	}

	/**
	 * 获取户型省市编码及名称
	 * @param javaRDD
	 * @return
	 */
	private static Map<Integer, HouseInfoDto> getHouseAreaMap(JavaRDD<String> javaRDD) {
		//houseId集合
		List<String> houseIdList = javaRDD.map(strs ->{
			String [] houseIdArr = strs.split(AnalysisConstants.SPLIT_REGEX);
			return houseIdArr[1];
		}).filter(str -> StringUtils.isNotBlank(str)).collect();

		//获取过滤信息 省市编码
		HouseStatisticsDao houseDao = DaoFactory.getHouseStatisticsDao();
		List<HouseInfoDto> houseInfoDtoList = houseDao.selectHouseArea(houseIdList);
		if (null == houseInfoDtoList || houseInfoDtoList.size() == 0) {
			System.out.println("houseInfoDtoList : " + houseIdList);
			return null;
		}

		//存储省市Code对应的名称
		houseInfoDtoList.stream().forEach(houseInfoDto -> {
			String provinceCode = houseInfoDto.getProvinceCode();
			if (!areaMap.containsKey(provinceCode)) {
				areaMap.put(provinceCode, houseInfoDto.getProvinceName());
			}
			String cityCode = houseInfoDto.getCityCode();
			if (!areaMap.containsKey(cityCode)) {
				areaMap.put(cityCode, houseInfoDto.getCityName());
			}
		});

		//转换map key=houseId,value=HouseInfoDto
		Map<Integer, HouseInfoDto> houseAreaDtoMap  = houseInfoDtoList.stream().collect(Collectors.toMap(HouseInfoDto::getHouseId, a -> a,(k1, k2 )-> k1));
		if (null == houseAreaDtoMap || houseAreaDtoMap.size() == 0) {
			System.out.println("houseAreaDtoMap : " + houseIdList);
			return null;
		}

		return houseAreaDtoMap;
	}

	/**
	 * 插入每天统计的户型数据
	 * @param houseDayResultMap
	 * @param localDate
	 */
	private static void insertByDay(Map<String, Long> houseDayResultMap, LocalDate localDate) {
		//   		0			1
		//key = provinceCode cityCode; value = 1
		if(houseDayResultMap == null || houseDayResultMap.size() == 0) {
			System.out.println("warn, function = HouseStatisticsResult.InsertIntoDB, message: houseDayResultMap is empty");
			return;
		}

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String startTime = df.format(LocalDateTime.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault())));
		String endTime = df.format(LocalDateTime.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault())));

		//获取startTime-endTime这个时间段新增户型,并根据省市code分组统计数量
		Map<String, HouseInfoDto> houseInfoDtoMap  = getNewHouseMap(startTime, endTime, HouseEventConstants.HOUSE_DAY_TYPE);
		boolean flag = false;
		if (null != houseInfoDtoMap && houseInfoDtoMap.size() > 0) {
			flag = true;
		}

		//遍历数据
		List<HouseStatisticsDayDto> list = new ArrayList<HouseStatisticsDayDto>();
		for (String key : houseDayResultMap.keySet()) {
			Integer value = houseDayResultMap.get(key).intValue();
			String[] keyArr = key.split(AnalysisConstants.SPLIT_REGEX);
			String provinceCode = keyArr[0];
			String cityCode = keyArr[1];
			Integer newHouseCount = 0;

			//获取新增户型数据
			if (flag && houseInfoDtoMap.containsKey(cityCode)) {
				HouseInfoDto houseInfoDto = houseInfoDtoMap.get(cityCode);
				if (null != houseInfoDto && null != houseInfoDto.getNewHouseCount()) {
					newHouseCount = houseInfoDto.getNewHouseCount();
				}
				//houseInfoDtoMap匹配一个则删除一个,循环完剩下的数据需要插入到list中保存
				houseInfoDtoMap.remove(cityCode);
				if (null == houseInfoDtoMap || houseInfoDtoMap.size() <= 0) {
					flag = false;
				}
			}
			HouseStatisticsDayDto houseStatisticsDayDto = new HouseStatisticsDayDto();
			houseStatisticsDayDto.setProvinceCode(provinceCode);
			houseStatisticsDayDto.setProvinceName(areaMap.get(provinceCode));
			houseStatisticsDayDto.setCityCode(cityCode);
			houseStatisticsDayDto.setCityName(areaMap.get(cityCode));
			houseStatisticsDayDto.setNewHouseCount(newHouseCount);
			houseStatisticsDayDto.setUseHouseCount(value);
			houseStatisticsDayDto.setStartTime(startTime);
			houseStatisticsDayDto.setEndTime(endTime);
			sysSave(houseStatisticsDayDto);
			list.add(houseStatisticsDayDto);
		}

		//houseInfoDtoMap剩下的数据需要插入到表中，新增户型统计,使用次数为0
		if (flag) {
			for (String key : houseInfoDtoMap.keySet()) {
				HouseInfoDto houseInfoDto = houseInfoDtoMap.get(key);
				if (null != houseInfoDto) {
					HouseStatisticsDayDto houseStatisticsDayDto = new HouseStatisticsDayDto();
					houseStatisticsDayDto.setProvinceCode(houseInfoDto.getProvinceCode());
					houseStatisticsDayDto.setProvinceName(houseInfoDto.getProvinceName());
					houseStatisticsDayDto.setCityCode(houseInfoDto.getCityCode());
					houseStatisticsDayDto.setCityName(houseInfoDto.getCityName());
					houseStatisticsDayDto.setNewHouseCount(houseInfoDto.getNewHouseCount());
					houseStatisticsDayDto.setUseHouseCount(0);
					houseStatisticsDayDto.setStartTime(startTime);
					houseStatisticsDayDto.setEndTime(endTime);
					sysSave(houseStatisticsDayDto);
					list.add(houseStatisticsDayDto);
				}
			}
		}

		HouseStatisticsDao houseStatisticsDao = DaoFactory.getHouseStatisticsDao();
		houseStatisticsDao.insertByDay(list, startTime, endTime);
	}


	/**
	 * 插入每小时统计的户型数据
	 * @param houseHourResultMap
	 */
	private static void insertByHour(Map<String, Long> houseHourResultMap, LocalDate localDate) {
		//   		0		1		2			3
		//key = startTime,endTime,provinceCode cityCode; value = 1
		if(houseHourResultMap == null || houseHourResultMap.size() == 0) {
			System.out.println("warn, function = HouseStatisticsResult.InsertIntoDB, message: houseDayResultMap is empty");
			return;
		}

		//获取当天区间时间2019-06-14 00:00:00 到 2019-06-15 00:00:00
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String startTime = df.format(LocalDateTime.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault())));
		String endTime = df.format(LocalDateTime.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault())));

		//获取startTime-endTime这个时间段新增户型,并根据每小时、省、市code分组统计数量
		Map<String, HouseInfoDto> houseInfoDtoMap  = getNewHouseMap(startTime, endTime, HouseEventConstants.HOUSE_HOUR_TYPE);
		boolean flag = false;
		if (null != houseInfoDtoMap && houseInfoDtoMap.size() > 0) {
			flag = true;
		}

		List<HouseStatisticsHourDto> list = new ArrayList<HouseStatisticsHourDto>();
		for (String key : houseHourResultMap.keySet()) {
			Integer value = houseHourResultMap.get(key).intValue();
			String[] keyArr = key.split(AnalysisConstants.SPLIT_REGEX);

			String startHourTime = keyArr[0];
			String provinceCode = keyArr[2];
			String cityCode = keyArr[3];
			String perHour = Utils.getPerHour(startHourTime);
			Integer newHouseCount = 0;

			//获取新增户型数据
			String newHouseKey = perHour+AnalysisConstants.SPLIT_REGEX+cityCode;
			if (flag && houseInfoDtoMap.containsKey(newHouseKey)) {
				HouseInfoDto houseInfoDto = houseInfoDtoMap.get(newHouseKey);
				if (null != houseInfoDto && null != houseInfoDto.getNewHouseCount()) {
					newHouseCount = houseInfoDto.getNewHouseCount();
				}
				//houseInfoDtoMap匹配一个则删除一个,循环完剩下的数据需要插入到list中保存
				houseInfoDtoMap.remove(newHouseKey);
				if (null == houseInfoDtoMap || houseInfoDtoMap.size() <= 0) {
					flag = false;
				}
			}

			HouseStatisticsHourDto houseStatisticsHourDto = new HouseStatisticsHourDto();
			houseStatisticsHourDto.setProvinceCode(provinceCode);
			houseStatisticsHourDto.setProvinceName(areaMap.get(provinceCode));
			houseStatisticsHourDto.setCityCode(cityCode);
			houseStatisticsHourDto.setCityName(areaMap.get(cityCode));
			houseStatisticsHourDto.setNewHouseCount(newHouseCount);
			houseStatisticsHourDto.setUseHouseCount(value);
			houseStatisticsHourDto.setStartTime(startHourTime);
			houseStatisticsHourDto.setEndTime(keyArr[1]);
			sysSave(houseStatisticsHourDto);
			list.add(houseStatisticsHourDto);
		}

		//houseInfoDtoMap剩下的数据需要插入到表中，新增户型统计,使用次数为0
		if (flag) {
			for (String key : houseInfoDtoMap.keySet()) {
				HouseInfoDto houseInfoDto = houseInfoDtoMap.get(key);
				if (null != houseInfoDto) {
					HouseStatisticsHourDto houseStatisticsHourDto = new HouseStatisticsHourDto();
					houseStatisticsHourDto.setProvinceCode(houseInfoDto.getProvinceCode());
					houseStatisticsHourDto.setProvinceName(houseInfoDto.getProvinceName());
					houseStatisticsHourDto.setCityCode(houseInfoDto.getCityCode());
					houseStatisticsHourDto.setCityName(houseInfoDto.getCityName());
					houseStatisticsHourDto.setNewHouseCount(houseInfoDto.getNewHouseCount());
					houseStatisticsHourDto.setUseHouseCount(0);
					//处理时间
					Map<String, String> timeMap = Utils.getBetweenHour(houseInfoDto.getCreateTime());
					houseStatisticsHourDto.setStartTime(timeMap.get("startTime"));
					houseStatisticsHourDto.setEndTime(timeMap.get("endTime"));
					sysSave(houseStatisticsHourDto);
					list.add(houseStatisticsHourDto);
				}
			}
		}

		HouseStatisticsDao houseStatisticsDao = DaoFactory.getHouseStatisticsDao();
		houseStatisticsDao.insertByHour(list, startTime, endTime);
	}

	/**
	 * 插入户型使用数量
	 * @param houseHourResultMap
	 * @param houseAreaMap
	 * @param localDate
	 */
	private static void insertByDayHotHouse(Map<String, Long> houseHourResultMap, Map<Integer, HouseInfoDto> houseAreaMap, LocalDate localDate) {
		//key = houseId; value = 1
		if(houseHourResultMap == null || houseHourResultMap.size() == 0) {
			System.out.println("warn, function = HouseStatisticsResult.insertByDayHotHouse, message: houseHourResultMap is empty");
			return;
		}
		if (null == houseAreaMap || houseAreaMap.size() == 0) {
			System.out.println("warn, function = HouseStatisticsResult.insertByDayHotHouse, message: houseAreaMap is empty");
			return;
		}

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String startTime = df.format(LocalDateTime.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault())));
		String endTime = df.format(LocalDateTime.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault())));

		List<HouseUsageAmountStatisticsDayDto> list = new ArrayList<>();
		for (String key : houseHourResultMap.keySet()) {
			Integer value = houseHourResultMap.get(key).intValue();
			//获取户型信息
			Integer houseId = Integer.valueOf(key);
			HouseInfoDto houseInfoDto = houseAreaMap.get(houseId);
			if (null == houseInfoDto || null == houseInfoDto.getHouseId()) {
				System.out.println("HouseStatistics.insertByDayHotHouse houseInfoDto is null, houseId=" + houseId);
				continue;
			}

			HouseUsageAmountStatisticsDayDto dto = new HouseUsageAmountStatisticsDayDto();
			dto.setHouseId(houseId);
			dto.setHouseName(houseInfoDto.getHouseName());
			dto.setHouseCode(houseInfoDto.getHouseCode());
			dto.setLivingName(houseInfoDto.getLivingName());
			dto.setHouseUsageAmount(value);
			dto.setProvinceCode(houseInfoDto.getProvinceCode());
			dto.setProvinceName(houseInfoDto.getProvinceName());
			dto.setCityCode(houseInfoDto.getCityCode());
			dto.setCityName(houseInfoDto.getCityName());
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setCreator(CommonConstants.CREATOR_SYSTEM);
			dto.setGmtCreate(Utils.getCurrentTime());
			dto.setGmtModified(Utils.getCurrentTime());
			dto.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
			dto.setModifier(CommonConstants.CREATOR_SYSTEM);
			dto.setRemark("");
			list.add(dto);
		}

		HouseStatisticsDao houseStatisticsDao = DaoFactory.getHouseStatisticsDao();
		houseStatisticsDao.insertHouseUsageAmountByDay(list, startTime, endTime);
	}

	/**
	 * 获取时间段的新增户型
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private static Map<String, HouseInfoDto> getNewHouseMap(String startTime, String endTime, String type) {
		HouseStatisticsDao houseStatisticsDao = DaoFactory.getHouseStatisticsDao();
		//获取startTime-endTime这个时间段新增户型,并根据省市code分组统计数量
		List<HouseInfoDto> houseInfoDtoList = houseStatisticsDao.selectNewHouseCount(startTime, endTime, type);
		if (null != houseInfoDtoList && houseInfoDtoList.size() > 0) {
			if (Objects.equals(type, HouseEventConstants.HOUSE_DAY_TYPE)) {
				Map<String, HouseInfoDto> houseInfoDtoMap = houseInfoDtoList.stream().collect(Collectors.toMap(HouseInfoDto::getCityCode, a -> a,(k1, k2 )-> k1));
				return houseInfoDtoMap;

			} else if (Objects.equals(type, HouseEventConstants.HOUSE_HOUR_TYPE)) {
				Map<String, HouseInfoDto> houseInfoDtoMap = new HashMap<>();
				houseInfoDtoList.stream().forEach(houseInfoDto -> {
					String key = houseInfoDto.getPerHour()
							+ AnalysisConstants.SPLIT_REGEX
							+ houseInfoDto.getCityCode();
					houseInfoDtoMap.put(key, houseInfoDto);
				});
				return houseInfoDtoMap;
			}
		}
		return null;
	}

	/**
	 * 填充系统字段
	 * @param dto
	 */
	@SuppressWarnings("all")
	private static void sysSave(HouseStatisticsDayDto dto) {
		dto.setCreator(CommonConstants.CREATOR_SYSTEM);
		dto.setGmtCreate(Utils.getCurrentTime());
		dto.setGmtModified(Utils.getCurrentTime());
		dto.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
		dto.setModifier(CommonConstants.CREATOR_SYSTEM);
		dto.setRemark("");
	}
	/**
	 * 填充系统字段
	 * @param dto
	 */
	@SuppressWarnings("all")
	private static void sysSave(HouseStatisticsHourDto dto) {
		dto.setCreator(CommonConstants.CREATOR_SYSTEM);
		dto.setGmtCreate(Utils.getCurrentTime());
		dto.setGmtModified(Utils.getCurrentTime());
		dto.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
		dto.setModifier(CommonConstants.CREATOR_SYSTEM);
		dto.setRemark("");
	}


}
