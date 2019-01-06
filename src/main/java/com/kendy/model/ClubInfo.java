package com.kendy.model;

/**
 * 检查多个联盟的共享额度
 *
 * @author linzt
 * @date
 */
public class ClubInfo {

  private String clubId;

  private String clubName;

  private double sharedEdu; // 取多个联盟里，相同俱乐部的最大额度

  private double jieYu; // 取多个联盟里，相同俱乐部的结余累加

  public ClubInfo() {
  }

  public ClubInfo(String clubId, String clubName, double sharedEdu, double jieYu) {
    this.clubId = clubId;
    this.clubName = clubName;
    this.sharedEdu = sharedEdu;
    this.jieYu = jieYu;
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

  public double getSharedEdu() {
    return sharedEdu;
  }

  public void setSharedEdu(double sharedEdu) {
    this.sharedEdu = sharedEdu;
  }

  public double getJieYu() {
    return jieYu;
  }

  public void setJieYu(double jieYu) {
    this.jieYu = jieYu;
  }
}
