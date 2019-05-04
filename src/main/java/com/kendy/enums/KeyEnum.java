package com.kendy.enums;

/**
 * other表中的key
 *
 * @author linzt
 * @time 2018年7月11日 下午6:01:23
 */
public enum KeyEnum {
  GU_DONG("gudong"),
  CLUB_ID("clubId"),
  SM_AOTO_NEXT_DAY_DB_KEY("sm_aoto_next_day_db_key"),
  SM_AOTO_TOKEN_DB_KEY("sm_aoto_token_db_key"),
  SM_FILTER_TEAM_DB_KEY("sm_filter_team_db_key");


  KeyEnum(String keyName) {
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
