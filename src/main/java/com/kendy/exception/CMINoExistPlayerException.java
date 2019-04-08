package com.kendy.exception;

/**
 * 实时金额表找不到该实时金额信息异常
 */
public class CMINoExistPlayerException extends Exception {

  private static final long serialVersionUID = 6775179545328979398L;

  public CMINoExistPlayerException() {
    super();
  }

  /**
   * @param arg0
   */
  public CMINoExistPlayerException(String arg0) {
    super(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public CMINoExistPlayerException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public CMINoExistPlayerException(Throwable arg0) {
    super(arg0);
  }

}
