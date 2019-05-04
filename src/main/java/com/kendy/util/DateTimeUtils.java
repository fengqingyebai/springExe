/**
 * 
 */
package com.kendy.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 */
public class DateTimeUtils {

  /**
   * 基本日期时间格式{@value}
   */
  public static final String BASIC_DATE_TIME_PATTERN = "yyyyMMddHHmmss";

  /**
   * 基本日期格式{@value}
   */
  public static final String BASIC_DATE_PATTERN = "yyyyMMdd";

  /**
   * 基本时间格式{@value}
   */
  public static final String BASIC_TIME_PATTERN = "HHmmss";

  /**
   * ISO格式{@value}
   */
  public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

  /**
   * JDK8 DateTimeFormatter,线程安全
   */
  private static DateTimeFormatter BASIC_DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern(BASIC_DATE_TIME_PATTERN);

  private static DateTimeFormatter BASIC_DATE_FORMATTER =
      DateTimeFormatter.ofPattern(BASIC_DATE_PATTERN);

  /**
   * 传统Date,FastDateFormat自带实例缓存更高效
   */
  private static FastDateFormat BASIC_FAST_DATE_FORMAT =
      FastDateFormat.getInstance(BASIC_DATE_TIME_PATTERN);

  private static DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  /**
   * 按照传入格式格式化日期
   * 
   * @param date
   * @param pattern
   * @return
   */
  public static String format(Date date, String pattern) {
    return FastDateFormat.getInstance(pattern).format(date);
  }

  /**
   * 按照传入格式格式化日期
   * 
   * @param localDate
   * @return
   */
  public static String format(LocalDate localDate, String pattern) {
    return localDate.format(DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * 按照传入格式格式化日期
   * 
   * @param localDateTime
   * @param pattern
   * @return
   */
  public static String format(LocalDateTime localDateTime, String pattern) {
    return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
  }


  /**
   * 按照基本格式格式化当前日期时间
   * 
   * @return
   */
  public static String formatBasic() {
    return formatBasic(LocalDateTime.now());
  }

  /**
   * 按照基本格式格式化日期
   * 
   * @param date
   * @return
   * @see DateTimeUtils.DEFAULT_DATE_TIME_PATTERN
   */
  public static String formatBasic(Date date) {
    return BASIC_FAST_DATE_FORMAT.format(date);
  }

  /**
   * 按照基本格式格式化日期
   * 
   * @param localDate
   * @return
   * @see DateTimeUtils.DEFAULT_DATE_PATTERN
   */
  public static String formatBasic(LocalDate localDate) {
    return localDate.format(BASIC_DATE_FORMATTER);
  }

  /**
   * 按照基本格式格式化日期
   * 
   * @param localDateTime
   * @return
   * @see DateTimeUtils.DEFAULT_DATE_TIME_PATTERN
   */
  public static String formatBasic(LocalDateTime localDateTime) {
    return localDateTime.format(BASIC_DATE_TIME_FORMATTER);
  }

  /**
   * 月份第一秒
   * 
   * @return
   */
  public static LocalDateTime getFirstSecondOfMonth(LocalDate localDate) {
    return getFirstSecondOfMonth(localDate.atStartOfDay());

  }

  /**
   * 月份第一秒
   * 
   * @return
   */
  public static LocalDateTime getFirstSecondOfMonth(LocalDateTime localDateTime) {
    localDateTime = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).withHour(0)
        .withMinute(0).withSecond(0);
    return localDateTime;
  }

  /**
   * 月份最后一秒
   * 
   * @return
   */
  public static Date getLastSecondOfMonth(Date date) {
    LocalDateTime localDateTime = toLocalDateTime(date);
    return toDate(getLastSecondOfMonth(localDateTime));
  }

  /**
   * 月份最后一秒
   * 
   * @return
   */
  public static LocalDateTime getLastSecondOfMonth(LocalDate localDate) {
    return getLastSecondOfMonth(localDate.atStartOfDay());
  }

  /**
   * 月份最后一秒
   * 
   * @return
   */
  public static LocalDateTime getLastSecondOfMonth(LocalDateTime localDateTime) {
    localDateTime = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    return localDateTime;
  }

  public static void main(String[] str) throws ParseException {
    System.out
        .println(FastDateFormat.getInstance(ISO_DATE_TIME_PATTERN).parse("2018-04-28T15:41:31"));

    LocalDateTime time = LocalDateTime.now();
    System.out.println(time);

    System.out.println(parseIsoLocalDateTime("2018-04-28T15:41:31"));

    System.out.println(DateTimeUtils.getLastSecondOfMonth(time));


    // 开通月份
    YearMonth subMonth = YearMonth.of(2018, 12);

    // 上个月
    YearMonth lastMonth = YearMonth.now().minusMonths(1);

    // 本月
    YearMonth thisMonth = YearMonth.now();

    System.out.println("subMonth.isAfter(lastMonth): " + subMonth.isAfter(lastMonth));
    System.out.println("subMonth.equals(lastMonth): " + subMonth.equals(lastMonth));
    System.out.println("subMonth.equals(thisMonth): " + subMonth.equals(thisMonth));

    //System.out.println(YearMonth.from(null));

    YearMonth unsubMonth = YearMonth.of(2018, 12);

    boolean isDuplicate = false;
    // 判断是否为当月或上月已开通过并且在本月退订过,重复订购
    if (subMonth.equals(thisMonth)) {
      // 当月已开通过,认定为重复订购
      isDuplicate = true;
    } else if (subMonth.equals(lastMonth)) {
      // 上月开通本月已退订过,认定为重复订购
      if (unsubMonth != null) {
        if (unsubMonth.equals(thisMonth)) {
          isDuplicate = true;
        }
      }
    }
    System.out.println(isDuplicate);
  }

  /**
   * @param dateTimeString
   * @param pattern
   * @return
   */
  public static Date parseDate(String dateTimeString, String pattern) {
    try {
      return FastDateFormat.getInstance(pattern).parse(dateTimeString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * 将字符串按照基本格式转换为Date
   * 
   * @param dateTimeString
   * @return
   */
  public static Date parseBasicDate(String dateTimeString) {
    return toDate(parseBasicLocalDateTime(dateTimeString));
  }

  /**
   * 将字符串按照基本格式转换为LocalDateTime
   * 
   * @param dateTimeString
   * @return
   */
  public static LocalDateTime parseBasicLocalDateTime(String dateTimeString) {
    return LocalDateTime.parse(dateTimeString, BASIC_DATE_TIME_FORMATTER);
  }

  /**
   * 将字符串按照ISO格式转换为LocalDateTime
   * 
   * @param dateTimeString
   * @return
   * @see DateTimeFormatter
   */
  public static LocalDateTime parseIsoLocalDateTime(String dateTimeString) {
    return LocalDateTime.parse(dateTimeString, ISO_DATE_TIME_FORMATTER);
  }

  /**
   * @param dateString
   * @param pattern
   * @return
   */
  public static LocalDate parseLocalDate(String dateString, String pattern) {
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * @param dateTimeString
   * @param pattern
   * @return
   */
  public static LocalDateTime parseLocalDateTime(String dateTimeString, String pattern) {
    return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * @param localDate
   * @return
   */
  public static Date toDate(LocalDate localDate) {
    return toDate(localDate.atStartOfDay());
  }

  /**
   * @param localDate
   * @return
   */
  public static Date toDate(LocalDateTime localDate) {
    return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * @param date
   * @return
   */
  public static LocalDate toLocalDate(Date date) {
    return toLocalDateTime(date).toLocalDate();
  }

  /**
   * @param date
   * @return
   */
  public static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}
