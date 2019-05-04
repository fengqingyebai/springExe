package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 团队战绩统计对象
 */
public class ZjTeamStaticInfo implements Entity {

  @MyExcelField(title = "俱乐部ID", colWidth = 12)
  private SimpleStringProperty teamClubId = new SimpleStringProperty();//俱乐部ID

  @MyExcelField(title = "团队ID")
  private SimpleStringProperty teamId = new SimpleStringProperty();//团家ID

  @MyExcelField(title = "统计时间", colWidth = 15)
  private SimpleStringProperty teamBeginStaticTime = new SimpleStringProperty();// 开始统计时间

  @MyExcelField(title = "人次")
  private SimpleStringProperty teamPersonCount = new SimpleStringProperty();//总战绩

  public ZjTeamStaticInfo() {
  }

  public ZjTeamStaticInfo(SimpleStringProperty teamClubId,
      SimpleStringProperty teamId, SimpleStringProperty teamBeginStaticTime,
      SimpleStringProperty teamPersonCount) {
    this.teamClubId = teamClubId;
    this.teamId = teamId;
    this.teamBeginStaticTime = teamBeginStaticTime;
    this.teamPersonCount = teamPersonCount;
  }

  public String getTeamClubId() {
    return teamClubId.get();
  }

  public SimpleStringProperty teamClubIdProperty() {
    return teamClubId;
  }

  public void setTeamClubId(String teamClubId) {
    this.teamClubId.set(teamClubId);
  }

  public String getTeamId() {
    return teamId.get();
  }

  public SimpleStringProperty teamIdProperty() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId.set(teamId);
  }

  public String getTeamBeginStaticTime() {
    return teamBeginStaticTime.get();
  }

  public SimpleStringProperty teamBeginStaticTimeProperty() {
    return teamBeginStaticTime;
  }

  public void setTeamBeginStaticTime(String teamBeginStaticTime) {
    this.teamBeginStaticTime.set(teamBeginStaticTime);
  }

  public String getTeamPersonCount() {
    return teamPersonCount.get();
  }

  public SimpleStringProperty teamPersonCountProperty() {
    return teamPersonCount;
  }

  public void setTeamPersonCount(String teamPersonCount) {
    this.teamPersonCount.set(teamPersonCount);
  }
}
