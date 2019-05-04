package com.kendy.model.lmb;

/**
 * 俱乐部信息类
 */
public class ClubInfomation{
  private String clubId;
  private String clubName;
  private String clubFenchengRate;
  private String lianmengFenchengRate;

  public ClubInfomation() {
    clubId="";
    clubName="";
    clubFenchengRate="0%";
    lianmengFenchengRate="0%";
  }

  public String getClubId() {
    return clubId;
  }

  public void setClubId(String clubId) {
    this.clubId = clubId;
  }

  public String getClubName() {
    return clubName;
  }

  public void setClubName(String clubName) {
    this.clubName = clubName;
  }

  public String getClubFenchengRate() {
    return clubFenchengRate;
  }

  public void setClubFenchengRate(String clubFenchengRate) {
    this.clubFenchengRate = clubFenchengRate;
  }

  public String getLianmengFenchengRate() {
    return lianmengFenchengRate;
  }

  public void setLianmengFenchengRate(String lianmengFenchengRate) {
    this.lianmengFenchengRate = lianmengFenchengRate;
  }
}
