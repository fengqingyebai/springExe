package com.kendy.exception;

/**
 * 登录异常
 */
public class LoginException extends Exception {

  private static final long serialVersionUID = 6775179545328979398L;

  public LoginException() {
    super();
  }

  /**
   * @param arg0
   */
  public LoginException(String arg0) {
    super(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public LoginException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public LoginException(Throwable arg0) {
    super(arg0);
  }

}
