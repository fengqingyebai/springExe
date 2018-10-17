package com.kendy.entity;

import com.kendy.excel.excel4j.annotation.ExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * 团队历史统计对象
 */
public class TeamStaticInfo implements Entity {

  private SimpleStringProperty lmName = new SimpleStringProperty();//所属联盟名称
  private SimpleStringProperty teamId = new SimpleStringProperty();//团家ID
  private SimpleStringProperty staticTime = new SimpleStringProperty();// 开始统计时间
  private SimpleStringProperty sumZJ = new SimpleStringProperty();//总战绩
  private SimpleStringProperty sumHuibao = new SimpleStringProperty();//总回保
  private SimpleStringProperty sumChuhuishui = new SimpleStringProperty();//总出回水
  private SimpleStringProperty sumPerson = new SimpleStringProperty();//总人数
  private SimpleStringProperty sumProfit = new SimpleStringProperty();//总输赢
  private SimpleStringProperty clubId = new SimpleStringProperty();//总输赢

  public TeamStaticInfo() {
  }


  public String getClubId() {
    return clubId.get();
  }

  public SimpleStringProperty clubIdProperty() {
    return clubId;
  }

  public void setClubId(String clubId) {
    this.clubId.set(clubId);
  }

  public String getLmName() {
    return lmName.get();
  }

  public SimpleStringProperty lmNameProperty() {
    return lmName;
  }

  public void setLmName(String lmName) {
    this.lmName.set(lmName);
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
}
