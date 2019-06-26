package com.sandu.analysis.biz.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class Utils {

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
			localDate = LocalDate.now().plusDays(-1);
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

	
}
