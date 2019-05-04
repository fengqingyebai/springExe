package com.kendy.util;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 数据转换计算工具类
 *
 * @author kendy
 */
public class NumUtil {


  private static Logger log = Logger.getLogger(NumUtil.class);

  // 把字符串转化为小数
  public static Double getNum(String str) {
    if (StringUtil.isBlank(str)) {
      return 0d;
    } else {
      try {
        return Double.valueOf(str).doubleValue();
      } catch (Exception e) {
        return 0d;
      }
    }
  }

  public static boolean compare(String a, String b) {
    return NumUtil.getNum(a).compareTo(NumUtil.getNum(b)) >= 0;
  }

  // 保留两位小数
  public static String digit2(String str) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("#######0.00");
    if (!StringUtil.isBlank(str)) {
      String res = df.format(Double.valueOf(str));
      return res;
    }
    return "0";
  }

  // 保留四位小数
  public static String digit4(String str) {
    if (str != null && str.contains("%")) {
      str = getNumByPercent(str) + "";
    }
    java.text.DecimalFormat df = new java.text.DecimalFormat("#######0.0000");
    if (!StringUtil.isBlank(str)) {
      String res = df.format(Double.valueOf(str));
      return res;
    }
    return "0";
  }

  // 截取一位小数，后面不进行舍入
  public static String digit1(String str) {
    if (!(StringUtil.isBlank(str))) {
      try {
        if (str.contains(".")) {
          str = str.substring(0, str.lastIndexOf(".") + 2);
          return str;
        } else {
          return str;
        }
      } catch (Exception e) {
        str = String.format("%.4f", Float.valueOf(str));
        str = str.substring(0, str.lastIndexOf(".") + 2);
        return str;
      }
    }
    return "0.0";
  }

  // 保留整数
  public static String digit0(String str) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("#######0");
    if (!StringUtil.isBlank(str)) {
      String res = "0";
      try {
        res = df.format(Double.valueOf(str));
      } catch (NumberFormatException e) {
        res = "0";
        log.error("检测到问题：" + res + "==" + e.getMessage());
      }
      return res;
    }
    return "0";
  }

  // 保留整数
  public static String digit0(double num) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("#######0");
    String res = df.format(Double.valueOf(num));
    return res;

  }

  // 把百分比转化为小数
  public static Double getNumByPercent(String percentStr) {
    if (!StringUtil.isBlank(percentStr) && percentStr.contains("%")) {
      try {
        Double res = NumUtil
            .getNumDivide(new Double(percentStr.substring(0, percentStr.indexOf("%"))), 100d);
        return res;
      } catch (NumberFormatException e) {
        ErrorUtil.err(percentStr + "把百分比转化为小数失败", e);
        return 0d;
      }
    }
    return 0d;
  }

  /**
   * 根据小数转成百分比
   */
  public static String getPercentStr(Double number) {
    NumberFormat num = NumberFormat.getPercentInstance();
    num.setMaximumIntegerDigits(3);
    num.setMaximumFractionDigits(2);
    String percentString = num.format(number);
    return percentString;
  }

  /**
   * 根据小数字符串转成百分比
   */
  public static String getPercentStr(String numberStr) {
    return getPercentStr(getNum(numberStr));
  }

  /**
   * 判断是否为数字
   *
   * @time 2017年12月7日
   * @param str
   * @return
   */
  private static Pattern pattern = Pattern.compile("(-)?[0-9]*");

  public static boolean isNumeric(String str) {
    if (str == null || "".equals(str.trim())) {
      return false;
    }
    // str = str.trim();
    Matcher isNum = pattern.matcher(str);
    return isNum.matches();
  }

  public static void main(String... args) {
    String s1 = " -14";
    String s2 = "942";
    String s3 = "fdd942";
    System.out.println(isNumeric(s1));
    System.out.println(isNumeric(s2));
    System.out.println(isNumeric(s3));
  }

  /**
   * 获取总和（可变参数）
   *
   * @time 2018年1月9日
   */
  public static String getSum(String... strings) {
    Double sum = 0d;
    if (strings.length > 0) {
      for (String str : strings) {
        sum += NumUtil.getNum(str);
      }
    }

    return NumUtil.digit0(sum) + "";
  }

  /**
   * 获取乘积
   *
   * @time 2018年1月28日
   */
  public static Double getNumTimes(String str1, String str2) {
    Double a = 0.0;
    Double b = 0.0;
    if (str1.contains("%")) {
      a = NumUtil.getNumByPercent(str1);
    } else {
      a = NumUtil.getNum(str1);
    }
    if (str2.contains("%")) {
      b = NumUtil.getNumByPercent(str2);
    } else {
      b = NumUtil.getNum(str2);
    }
    return a * b;
  }

  /**
   * 两数相除
   *
   * @time 2018年2月20日
   */
  public static Double getNumDivide(Double d1, Double d2) {
    if (d1.compareTo(0d) == 0 || d2.compareTo(0d) == 0 || d1.compareTo(-0d) == 0
        || d2.compareTo(-0d) == 0) {
      return 0d;
    } else {
      return d1 / d2;
    }
  }

  public static String getNumDivide2(Double d1, Double d2) {
    return digit2(getNumDivide(d1, d2) + "");
  }

  public static String digit(double val){
    return digit(String.valueOf(val));
  }

  /**
   * 获取数据
   * 3.123 => 3.12
   * 3.1 => 3.1
   * -0.0 => 0
   * -0 => 0
   * -0.00 => 0
   * 3 => 3
   * 3.00 => 3
   * @param val
   * @return
   */
  public static String digit(String val){
    if (StringUtils.isNotBlank(val)) {
      if (!val.contains(".")) {
        return val.trim();
      }
      if (StringUtils.endsWith(val, ".00")) {
        return StringUtils.substringBeforeLast(val, ".");
      }
      if(val.contains(".") && StringUtils.substringAfterLast(val, ".").length() > 2){
        return NumUtil.digit2(val);
      }
      if("-0.0".equals(val) || "0.0".equals(val) || "0.00".equals(val) || "-0.00".equals(val) || "-0".equals(val)){
        return "0";
      }
      return val;
    }
    return "0";
  }
}
