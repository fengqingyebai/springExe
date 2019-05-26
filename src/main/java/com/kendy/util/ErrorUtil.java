package com.kendy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 操作错误工具类
 *
 * @author 林泽涛
 * @time 2017年11月12日 上午12:42:17
 */
public class ErrorUtil {

  private static Logger log = LoggerFactory.getLogger(ErrorUtil.class);

  /**
   * 错误提示及日志入库
   *
   * @time 2017年11月12日
   */
  public static void err(String msg, Throwable e) {
    msg += ",原因：" + e.getMessage();
    // 弹框提示
    ShowUtil.show(msg);
    // 日志入库
    log.error(msg, e);
  }

  /**
   * 错误简单提示及日志入库
   *
   * @time 2017年11月12日
   */
  public static void err(String msg) {
    // 弹框提示
    ShowUtil.show(msg);
    // 日志入库
    log.error(msg);
  }
}
