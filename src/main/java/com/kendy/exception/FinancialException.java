package com.kendy.exception;

/**
 * 财务软件异常类
 * @author linzt
 * @date
 */
public class FinancialException extends Exception {

  FinancialException() {
    super();
  }

  public FinancialException(String message) {
    super(message);
  }

  public FinancialException(String message, Throwable cause) {
    super(message, cause);
  }
}
