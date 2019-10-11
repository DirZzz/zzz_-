package com.sandu.analysis.biz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	/**
	 * 日期格式yyyy-MM-dd
	 */
	public final static String DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * eg:
	 * str = refpage:shoes,curpage:pages/home/A
	 * 
	 * @author huangsongbo
	 * @param str
	 * @return
	 */
	public static Map<String, String> getMap(String str) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(StringUtils.isEmpty(str)) {
			return resultMap;
		}
		
		for(String strItem : str.split(",")) {
			String[] strItems = strItem.split(":");
			if(strItems.length != 2) {
				continue;
			}
			resultMap.put(strItems[0], strItems[1]);
		}
		
		return resultMap;
	}
	
	/**
	 * 取出args[0], 格式为: "20190430", 转化为LocalDate
	 * 
	 * @author huangsongbo
	 * @param args
	 * @return
	 */
	public static LocalDate getLocalDate(String[] args) {
		LocalDate localDate = null;
		if(args != null && args.length > 0) {
			// args[0] 格式: 20190422
			localDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyyMMdd"));
		} else {
			// 默认统计昨天的数据
			localDate = LocalDate.now().minusDays(1);
		}
		
		return localDate;
	}

	/**
	 * 获取localDate最近上一周的周日期区间/上个月的月日期区间 列表
	 * @param localDate
	 * @param type
	 * @return
	 */

	public static List<LocalDate> getWeekOrMonthDateRangeList(LocalDate localDate, String type){
		List<LocalDate> resultList = new ArrayList<>();
		if (StringUtils.isEmpty(type)){
			return resultList;
		}
		//LocalDate localDate = LocalDate.parse(nowDate);
		if(type.equals("1")){
			LocalDate yesterday = localDate.minusDays(1);
			resultList.add(yesterday);
		}else if(type.equals("2")){
			//获取上个礼拜周一
			LocalDate startDate = localDate.minusWeeks(1l).with(DayOfWeek.MONDAY);
			//获取上个礼拜周日
			LocalDate endDate = localDate.minusWeeks(1l).with(DayOfWeek.SUNDAY);
			List<LocalDate> weekBetweenDate = getBetweenDate(startDate, endDate);
			resultList.addAll(weekBetweenDate);
		}else if(type.equals("3")){
			LocalDate lastMonthDate= localDate.withMonth(localDate.getMonthValue() - 1);
			// 取上个月第1天 2019-04-01
			LocalDate startDate = lastMonthDate.with(TemporalAdjusters.firstDayOfMonth());
			// 取上个月最后一天 2019-04-30
			LocalDate endDate = lastMonthDate.with(TemporalAdjusters.lastDayOfMonth());
			List<LocalDate> monthBetweenDate = getBetweenDate(startDate, endDate);
			resultList.addAll(monthBetweenDate);
		}
		return resultList;
	}

	/**
	 * 获取日期区间列表
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<LocalDate> getBetweenDate(LocalDate startDate, LocalDate endDate){
		List<LocalDate> list = new ArrayList<>();
		long distance = ChronoUnit.DAYS.between(startDate, endDate);
		if (distance < 1) {
			return list;
		}
		Stream.iterate(startDate, d -> {
			return d.plusDays(1);
		}).limit(distance + 1).forEach(f -> {
			list.add(f);
		});
		return list;
	}

	public static Map<String, LocalDate> getDateRangeMap(LocalDate localDate, String type){
		Map<String, LocalDate> resultMap = new HashMap<String, LocalDate>();
		if (null == localDate || null == type){
			return resultMap;
		}
		LocalDate startDate = null;
		LocalDate endDate = null;
		if(type.equals("1")){
			startDate = localDate.minusDays(1);
			endDate = localDate;
		}else if(type.equals("2")){
			//获取上个礼拜周一
			startDate = localDate.minusWeeks(1l).with(DayOfWeek.MONDAY);
			//获取上个礼拜周日
			endDate = localDate.minusWeeks(1l).with(DayOfWeek.SUNDAY);
		}else if(type.equals("3")){
			LocalDate lastMonthDate= localDate.withMonth(localDate.getMonthValue() - 1);
			// 取上个月第1天 2019-04-01
			startDate = lastMonthDate.with(TemporalAdjusters.firstDayOfMonth());
			// 取上个月最后一天 2019-04-30
			endDate = lastMonthDate.with(TemporalAdjusters.lastDayOfMonth());
		}
		resultMap.put("startDate",startDate);
		resultMap.put("endDate",endDate);
		return resultMap;

	}

	/**
	 * 获取某天的开始时间
	 * @return
	 */
	public static String getIntradayStartTime(String date){
		return getIntradayStartTime(date,DATE_TIME_PATTERN);
	}


	public static String getIntradayStartTime(String date,String format){
		if(StringUtils.isBlank(format)){
			format = DATE_TIME_PATTERN;
		}
		DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
		LocalDate localDate = LocalDate.parse(date,df);
		LocalDateTime newLocalTime = LocalDateTime.of(localDate, LocalTime.MIN);
		return formatDateTime(newLocalTime);
	}

	public static String getDateAfter(String date){
		return getDateAfter(date,DATE_TIME_PATTERN,1);
	}

	public static String getDateAfter(String date,String format,int day){
		if(StringUtils.isBlank(format)){
			format = DATE_TIME_PATTERN;
		}
		DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
		LocalDateTime dateTime = LocalDateTime.parse(date,df);
		LocalDateTime newTime = dateTime.plusDays(day);
		String time = df.format(newTime);
		return time;
	}

	/***
	 * 时间格式转换
	 * @param date 字符串日期
	 * @return
	 */
	public static Date str2date(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 时间格式转换
	 * @param dateStr 字符串日期
	 * @return
	 */
	public static String dateStrFormat(String dateStr, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date date = dateFormat.parse(dateStr);
			return dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 时间格式转换
	 * @param date 字符串日期
	 * @return
	 */
	public static String date2str(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	/**
	 * 格式化时间-默认yyyy-MM-dd HH:mm:ss格式
	 *
	 * @param dateTime  LocalDateTime对象
	 * @return
	 */
	public static String formatDateTime(LocalDateTime dateTime) {
		return formatDateTime(dateTime, DATE_TIME_PATTERN);
	}

	/**
	 * 获取指定时间的指定格式-默认yyyy-MM-dd HH:mm:ss格式
	 *
	 * @param dateTime  LocalDateTime对象
	 * @param pattern   要格式化的字符串
	 * @return
	 */
	public static String formatDateTime(LocalDateTime dateTime, String pattern) {
		if (dateTime == null) {
			return null;
		}
		if (pattern == null || pattern.isEmpty()) {
			pattern = DATE_TIME_PATTERN;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return dateTime.format(formatter);
	}

	/**
	 * 获取小时区间值
	 */
	public static Map<String, String> getBetweenHour(String time) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(time, df);
		LocalDate localDate = localDateTime.toLocalDate();
		LocalTime localTime = localDateTime.toLocalTime();

		LocalTime startTime = localTime.withMinute(0).withSecond(0);
		LocalDateTime startLocalTime = LocalDateTime.of(localDate,startTime);
		LocalTime endTime = startTime.plusHours(1);

		//处理零点
		LocalTime zero = LocalTime.of(0, 0, 0); // 00:00:00
		if (Objects.equals(endTime, zero)) {
			localDate = localDate.plusDays(1);
		}
		LocalDateTime endLocalTime = LocalDateTime.of(localDate,endTime);

		Map<String, String> hourMap = new HashMap<>(3);
		hourMap.put("startTime", df.format(startLocalTime));
		hourMap.put("endTime", df.format(endLocalTime));
		return hourMap;
	}

	/**
	 * 获取当前时间
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime time = LocalDateTime.now();
		return df.format(time);
	}

	/**
	 * 获取小时时间 eg:2019-06-15 01
	 * @param time
	 * @return
	 */
	public static String getPerHour(String time) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
		LocalDateTime localDateTime = LocalDateTime.parse(time.substring(0, time.length()-6), df);
		return df.format(localDateTime);
	}


}
