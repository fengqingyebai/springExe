package com.kendy.util;

import java.io.File;

/**
 * 系统工具类
 * 
 * @author kendy
 */
public class SystemUtil {
  
  public static File getUserFile() {
    return new File(System.getProperty("user.home"));
  }

}
