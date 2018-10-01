package com.kendy.util;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author linzt
 * @date
 */
public class HtmlUtil {

  public static void main(String args[]) {

    System.out.println(
        "转成Unicode编码：" + StringEscapeUtils.escapeJava("陈磊兴")); //转义成Unicode编码                    
    System.out
        .println("转义XML：" + StringEscapeUtils.escapeXml("<name>陈磊兴</name>")); //转义xml          
    System.out.println(
        "反转义XML：" + StringEscapeUtils.unescapeXml("<name>陈磊兴</name>")); //转义xml                
  }

}
