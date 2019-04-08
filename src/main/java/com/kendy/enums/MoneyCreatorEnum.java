package com.kendy.enums;

/**
 * 实时金额创建人
 */
public enum MoneyCreatorEnum {
  LIAN_MENG_BI("lmb"),
  DEFAULT("system"),
  USER("user");
  String creatorName;

  MoneyCreatorEnum(String creatorName) {
    this.creatorName = creatorName;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }
}
