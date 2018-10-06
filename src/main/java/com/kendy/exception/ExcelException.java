package com.kendy.exception;

/**
 * Excel报错抛出此异常
 *
 * @author 林泽涛
 * @time 2018年4月9日 下午8:12:12
 */
public class ExcelException extends Exception {

  private static final long serialVersionUID = 6775179545328979398L;

  public ExcelException() {
    super();
  }

  /**
   * @param arg0
   */
  public ExcelException(String arg0) {
    super(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public ExcelException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public ExcelException(Throwable arg0) {
    super(arg0);
  }

}
