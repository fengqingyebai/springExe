package com.kendy.enums;

/**
 * other表中的key
 * 
 * @author linzt
 * @time 2018年7月11日 下午6:01:23
 */
public enum KeyEnum {
  GU_DONG("gudong");


  private KeyEnum(String keyName) {
    this.keyName = keyName;
  }

  private String keyName;

  public String getKeyName() {
    return keyName;
  }

  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }


}
