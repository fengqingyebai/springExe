package com.kendy.exception;

/**
 * 绑定列值异常
 *
 * @author linzt
 * @time 2018年7月6日
 */
public class BindColumnException extends Exception {

  private static final long serialVersionUID = 6775179545328979398L;

  public BindColumnException() {
    super();
  }

  /**
   * @param arg0
   */
  public BindColumnException(String arg0) {
    super(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public BindColumnException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public BindColumnException(Throwable arg0) {
    super(arg0);
  }

}
