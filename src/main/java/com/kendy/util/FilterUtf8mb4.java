package com.kendy.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterUtf8mb4 {

  public static void main(String[] args) {
    System.out.println("美国队长🇺🇸");
    System.out.println("叼鱼🐟");
    System.out.println(filterUtf8mb4("美国队长🇺🇸"));
    System.out.println(filterUtf8mb4("叼鱼🐟"));
    System.out.println(filterUtf8mb4("叼鱼佛挡杀佛e"));
    System.out.println(filterUtf8mb4("叼___==33杀佛e"));
  }

  /**
   * 过滤掉字符串中的utf8mb4表情
   * 
   * @time 2018年4月4日
   * @param str
   * @return
   */
  public static String filterUtf8mb4(String str) {

    if (str.trim().isEmpty()) {
      return str;
    }
    String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    String reStr = "";
    Pattern emoji = Pattern.compile(pattern);
    Matcher emojiMatcher = emoji.matcher(str);
    str = emojiMatcher.replaceAll(reStr);
    return str;
  }
}
