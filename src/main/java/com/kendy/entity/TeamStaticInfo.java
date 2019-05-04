package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 团队历史统计对象
 */
public class TeamStaticInfo implements Entity {

  @MyExcelField(title = "俱乐部ID", colWidth = 12)
  private SimpleStringProperty teamClubId = new SimpleStringProperty();//俱乐部ID

  @MyExcelField(title = "团队ID")
  private SimpleStringProperty teamId = new SimpleStringProperty();//团家ID

  @MyExcelField(title = "统计时间", colWidth = 15)
  private SimpleStringProperty staticTime = new SimpleStringProperty();// 开始统计时间

  @MyExcelField(title = "总战绩")
  private SimpleStringProperty sumZJ = new SimpleStringProperty();//总战绩

  @MyExcelField(title = "总回水")
  private SimpleStringProperty sumChuhuishui = new SimpleStringProperty();//总出回水

  @MyExcelField(title = "总回保")
  private SimpleStringProperty sumHuibao = new SimpleStringProperty();//总回保


  @MyExcelField(title = "总人数")
  private SimpleStringProperty sumPerson = new SimpleStringProperty();//总人数

  @MyExcelField(title = "服务费")
  private SimpleStringProperty teamFWF = new SimpleStringProperty();// 团队服务费

  @MyExcelField(title = "总输赢")
  private SimpleStringProperty sumProfit = new SimpleStringProperty();//总输赢 = 总战绩 + 总出回水 + 总回保 - 团队服务费

  @MyExcelField(title = "团保比例")
  private SimpleStringProperty teamProxyHBRate = new SimpleStringProperty();// 团队代理回保比例

  @MyExcelField(title = "团水比例")
  private SimpleStringProperty teamProxyHSRate = new SimpleStringProperty();// 团队代理回水比例



  public TeamStaticInfo() {
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

  public String getStaticTime() {
    return staticTime.get();
  }

  public SimpleStringProperty staticTimeProperty() {
    return staticTime;
  }

  public void setStaticTime(String staticTime) {
    this.staticTime.set(staticTime);
  }

  public String getSumZJ() {
    return sumZJ.get();
  }

  public SimpleStringProperty sumZJProperty() {
    return sumZJ;
  }

  public void setSumZJ(String sumZJ) {
    this.sumZJ.set(sumZJ);
  }

  public String getSumHuibao() {
    return sumHuibao.get();
  }

  public SimpleStringProperty sumHuibaoProperty() {
    return sumHuibao;
  }

  public void setSumHuibao(String sumHuibao) {
    this.sumHuibao.set(sumHuibao);
  }

  public String getSumChuhuishui() {
    return sumChuhuishui.get();
  }

  public SimpleStringProperty sumChuhuishuiProperty() {
    return sumChuhuishui;
  }

  public void setSumChuhuishui(String sumChuhuishui) {
    this.sumChuhuishui.set(sumChuhuishui);
  }

  public String getSumPerson() {
    return sumPerson.get();
  }

  public SimpleStringProperty sumPersonProperty() {
    return sumPerson;
  }

  public void setSumPerson(String sumPerson) {
    this.sumPerson.set(sumPerson);
  }

  public String getSumProfit() {
    return sumProfit.get();
  }

  public SimpleStringProperty sumProfitProperty() {
    return sumProfit;
  }

  public void setSumProfit(String sumProfit) {
    this.sumProfit.set(sumProfit);
  }

  public String getTeamFWF() {
    return teamFWF.get();
  }

  public SimpleStringProperty teamFWFProperty() {
    return teamFWF;
  }

  public void setTeamFWF(String teamFWF) {
    this.teamFWF.set(teamFWF);
  }

  public String getTeamProxyHBRate() {
    return teamProxyHBRate.get();
  }

  public SimpleStringProperty teamProxyHBRateProperty() {
    return teamProxyHBRate;
  }

  public void setTeamProxyHBRate(String teamProxyHBRate) {
    this.teamProxyHBRate.set(teamProxyHBRate);
  }

  public String getTeamProxyHSRate() {
    return teamProxyHSRate.get();
  }

  public SimpleStringProperty teamProxyHSRateProperty() {
    return teamProxyHSRate;
  }

  public void setTeamProxyHSRate(String teamProxyHSRate) {
    this.teamProxyHSRate.set(teamProxyHSRate);
  }
}
