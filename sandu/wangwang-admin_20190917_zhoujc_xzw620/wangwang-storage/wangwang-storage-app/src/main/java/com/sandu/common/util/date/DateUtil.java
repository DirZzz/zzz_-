package com.sandu.common.util.date;

import org.apache.commons.lang3.time.DateUtils;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.Date;

/**
 * 时间工具类
 * @author: chenqiang
 * @create: 2019-05-27 09:54
 */
public class DateUtil extends DateUtils {

    /**
     * 日期格式yyyy-MM-dd
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * String转LocalDateTime
     *
     * @param dateTimeStr  Date字符串
     * @param pattern      转换格式
     * @return
     */
    public static LocalDateTime coverStrToLDT(String dateTimeStr, String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, df);
        return dateTime;
    }

    /**
     * Date转LocalDateTime
     *
     * @param date  Date对象
     * @return
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param dateTime  LocalDateTime对象
     * @return
     */
    public static Date convertLDTToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
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
     * 获取当前时间的指定格式
     *
     * @param pattern   要格式化的字符串
     * @return
     */
    public static String formatNow(String pattern) {
        return  formatDateTime(LocalDateTime.now(), pattern);
    }

    /**
     * 计算两个时间的时间差---天
     *
     * @param starTime  开始时间
     * @param endTime   结束时间
     * @return
     */
    public static Long timeDifference(LocalDateTime starTime,LocalDateTime endTime){
        long days = endTime.toLocalDate().toEpochDay() - starTime.toLocalDate().toEpochDay();
        return days;
    }

    /**
     * 获取指定日期的毫秒
     *
     * @param time  LocalDateTime对象
     * @return
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     *
     * @param time  LocalDateTime对象
     * @return
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取某天的最小时间
     *
     * @param dateTime
     * @return
     */
    public static String getDayStart(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(LocalTime.MIN));
    }

    /**
     * 获取某天最大时间
     *
     * @param dateTime
     * @return
     */
    public static String getDayEnd(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(LocalTime.MAX));
    }

    /**
     * 获取某月第一天最小时间
     *
     * @param dateTime  LocalDateTime对象
     * @return
     */
    public static String getFirstDayOfMonth(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN));
    }

    /**
     * 获取某月最后一天的最大时间
     *
     * @param dateTime  LocalDateTime对象
     * @return
     */
    public static String getLastDayOfMonth(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX));
    }

    /**
     * 获取时间在当年的第几周
     *
     * @param dateTime  LocalDateTime对象
     * @return
     */
    public static Integer getWeekByTime(LocalDateTime dateTime){
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,1);
        return dateTime.get(weekFields.weekOfYear());
    }

    /**
     * 获取之后的时间  根据unit不同加不同值
     *
     * @param time          LocalDateTime对象
     * @param amountToAdd   添加的值
     * @param unit          ChronoUnit.*
     * @return
     */
    public static LocalDateTime plus(LocalDateTime time, long amountToAdd, TemporalUnit unit) {
        return time.plus(amountToAdd, unit);
    }


    /**
     * 获取之前的时间  根据unit不同减不同值
     *
     * @param time              LocalDateTime对象
     * @param amountToSubtract  减去的值
     * @param unit              ChronoUnit.*
     * @return
     */
    public static LocalDateTime minus(LocalDateTime time, long amountToSubtract, TemporalUnit unit) {
        return time.minus(amountToSubtract,unit);
    }

    /**
     * 获取两个日期的差 根据unit不同返回不同的类型值
     *
     * @param startTime LocalDateTime对象
     * @param endTime   LocalDateTime对象
     * @param unit      ChronoUnit.*
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit unit) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (unit == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (unit == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return unit.between(startTime, endTime);
    }


    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(DateUtil.getDayStart(LocalDateTime.now()));
        System.out.println(DateUtil.getDayEnd(LocalDateTime.now()));
        System.out.println(DateUtil.getDayStart(DateUtil.minus(LocalDateTime.now(),1,ChronoUnit.DAYS)));
        System.out.println(DateUtil.getWeekByTime(LocalDateTime.now()));
    }

}
