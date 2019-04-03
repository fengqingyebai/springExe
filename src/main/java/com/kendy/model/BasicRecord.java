package com.kendy.model;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;

/**
 * 新版本战绩记录（统一） 备注：保险是整个牌局的保险合计的总和 而保险合计只是单个玩家单条记录的保险
 *
 * @author 林泽涛
 * @time 2018年7月7日 下午9:10:43
 */
public class BasicRecord {

  @MyExcelField(title = "牌局类型")
  private String juType;

  @MyExcelField(title = "总手数")
  private String sumHandsCount;

  @MyExcelField(title = "玩家ID")
  private String playerId;

  @MyExcelField(title = "玩家昵称")
  private String playerName;

  @MyExcelField(title = "俱乐部ID")
  private String clubId;

  @MyExcelField(title = "俱乐部")
  private String clubName;

  @MyExcelField(title = "保险合计#彩池合计")
  private String sinegleInsurance;

  @MyExcelField(title = "俱乐部保险#俱乐部彩池")
  private String clubInsurance;

  @MyExcelField(title = "保险#总彩池")
  private String currentTableInsurance;

  //@MyExcelField(title = "战绩")
  @MyExcelField(title = "原始战绩")
  private String yszj;

  @MyExcelField(title = "结束时间")
  private String finisedTime;


  public BasicRecord() {
    super();
  }


  public String getSumHandsCount() {
    return sumHandsCount;
  }


  public void setSumHandsCount(String sumHandsCount) {
    this.sumHandsCount = sumHandsCount;
  }


  public String getPlayerId() {
    return playerId;
  }


  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }


  public String getPlayerName() {
    return playerName;
  }


  public void setPlayerName(String playerName) {
    this.playerName = playerName;
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


  public String getSinegleInsurance() {
    return sinegleInsurance;
  }


  public void setSinegleInsurance(String sinegleInsurance) {
    this.sinegleInsurance = sinegleInsurance;
  }


  public String getClubInsurance() {
    return clubInsurance;
  }


  public void setClubInsurance(String clubInsurance) {
    this.clubInsurance = clubInsurance;
  }


  public String getCurrentTableInsurance() {
    return currentTableInsurance;
  }


  public void setCurrentTableInsurance(String currentTableInsurance) {
    this.currentTableInsurance = currentTableInsurance;
  }


  public String getYszj() {
    return yszj;
  }


  public void setYszj(String yszj) {
    this.yszj = yszj;
  }


  public String getFinisedTime() {
    return finisedTime;
  }


  public void setFinisedTime(String finisedTime) {
    this.finisedTime = finisedTime;
  }

  public String getJuType() {
    return juType;
  }

  public void setJuType(String juType) {
    this.juType = juType;
  }
}
