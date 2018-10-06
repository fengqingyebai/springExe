package com.kendy.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author 林泽涛
 * @time 2018年1月1日 下午10:56:51
 */
public class TimeUtil {

  public static void main(String[] args) {
    System.out.println(getTime());

  }

  public static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static DateTimeFormatter sdf = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);

  public static String getTime() {
    String timeStr = "";
    timeStr = LocalDateTime.now().format(sdf);
    return timeStr;
  }


  /**
   * 获取时间 注意这个方法是线程不安全的 若考虑线程安全，建议直接采用JDK8自带的时间API
   *
   * @time 2017年11月30日
   */
  public static String getDateTime() {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    String timeStr = "";
    timeStr = format.format(new Date());
    return timeStr;
  }

  public static String getDateTime2() {
    SimpleDateFormat format = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
    String timeStr = "";
    timeStr = format.format(new Date());
    return timeStr;
  }


  /**
   * 获取当前日期
   *
   * @time 2018年2月11日
   */
  public static String getDateString() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String timeStr = "";
    timeStr = format.format(new Date());
    return timeStr;
  }


  public static String getTimeString() {
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    String timeStr = "";
    timeStr = format.format(new Date());
    return timeStr;
  }

  /*******************************************************************/

  /**
   * 根据当天起始时间点获取范围
   *
   * @time 2018年4月14日
   */
  public static long[] getTimeRange() {
    LocalDate now = LocalDate.now();
    LocalDateTime atStartOfDay = now.atStartOfDay();

    LocalDateTime todayTime24 = atStartOfDay.plusHours(24L);

    long start = atStartOfDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    long end = todayTime24.toInstant(ZoneOffset.of("+8")).toEpochMilli();

    long[] timeRange = new long[]{start, end};
    return timeRange;
  }

}
