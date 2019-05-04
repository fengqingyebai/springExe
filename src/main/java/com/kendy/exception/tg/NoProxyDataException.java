package com.kendy.exception.tg;

import com.kendy.exception.FinancialException;

/**
 * @author linzt
 * @date
 */
public class NoProxyDataException extends FinancialException {

  public NoProxyDataException(String message) {
    super(message);
  }

  public NoProxyDataException(String message, Throwable cause) {
    super(message, cause);
  }
}
