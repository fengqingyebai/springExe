package com.kendy.entity;

/**
 * 俱乐部实体类
 *
 * @author 林泽涛
 * @time 2017年11月22日 下午8:13:04
 */
public class Club {

  //俱乐部ID
  private String clubId;
  //俱乐部名称
  private String name;
  //俱乐部额度
  private String edu;
  //桌费
  private String zhuoFei = "0";
  //就结算
  private String yiJieSuan = "0";

  //附加的联盟2联盟3的桌费、已结算、和额度
  private String zhuoFei2 = "0";
  private String zhuoFei3 = "0";
  private String yiJieSuan2 = "0";
  private String yiJieSuan3 = "0";
  private String edu2 = "0";
  private String edu3 = "0";

  private String gudong = "";


  public Club() {
    super();
  }


  /**
   * @param clubId
   * @param name
   * @param edu
   */
  public Club(String clubId, String name, String edu) {
    super();
    this.clubId = clubId;
    this.name = name;
    this.edu = edu;
  }


  public String getClubId() {
    return clubId;
  }

  public void setClubId(String clubId) {
    this.clubId = clubId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEdu() {
    return edu;
  }

  public void setEdu(String edu) {
    this.edu = edu;
  }


  public String getZhuoFei() {
    return zhuoFei;
  }


  public void setZhuoFei(String zhuoFei) {
    this.zhuoFei = zhuoFei;
  }


  public String getYiJieSuan() {
    return yiJieSuan;
  }


  public void setYiJieSuan(String yiJieSuan) {
    this.yiJieSuan = yiJieSuan;
  }


  public String getZhuoFei2() {
    return zhuoFei2;
  }


  public void setZhuoFei2(String zhuoFei2) {
    this.zhuoFei2 = zhuoFei2;
  }


  public String getZhuoFei3() {
    return zhuoFei3;
  }


  public void setZhuoFei3(String zhuoFei3) {
    this.zhuoFei3 = zhuoFei3;
  }


  public String getYiJieSuan2() {
    return yiJieSuan2;
  }


  public void setYiJieSuan2(String yiJieSuan2) {
    this.yiJieSuan2 = yiJieSuan2;
  }


  public String getYiJieSuan3() {
    return yiJieSuan3;
  }


  public void setYiJieSuan3(String yiJieSuan3) {
    this.yiJieSuan3 = yiJieSuan3;
  }


  public String getEdu2() {
    return edu2;
  }


  public void setEdu2(String edu2) {
    this.edu2 = edu2;
  }


  public String getEdu3() {
    return edu3;
  }


  public void setEdu3(String edu3) {
    this.edu3 = edu3;
  }


  public String getGudong() {
    return gudong;
  }


  public void setGudong(String gudong) {
    this.gudong = gudong;
  }


  @Override
  public String toString() {
    return "Club [clubId=" + clubId + ", name=" + name + ", edu=" + edu + ", zhuoFei=" + zhuoFei
        + ", yiJieSuan="
        + yiJieSuan + ", zhuoFei2=" + zhuoFei2 + ", zhuoFei3=" + zhuoFei3 + ", yiJieSuan2="
        + yiJieSuan2
        + ", yiJieSuan3=" + yiJieSuan3 + ", edu2=" + edu2 + ", edu3=" + edu3 + ", gudong=" + gudong
        + "]";
  }


}
