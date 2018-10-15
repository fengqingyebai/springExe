package com.kendy.entity;

import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

/**
 * 俱乐部银行卡信息
 *
 * @author 林泽涛
 * @time 2017年12月18日 下午9:13:22
 */
public class ClubStaticInfo implements Entity {

  private SimpleStringProperty clubName = new SimpleStringProperty();//所属联盟名称
  private SimpleStringProperty clubId = new SimpleStringProperty();//总战绩
  private SimpleStringProperty clubSumZJ = new SimpleStringProperty();//总人数
  private SimpleStringProperty clubSumBaoxian = new SimpleStringProperty();//总回保
  private SimpleStringProperty clubSumPerson = new SimpleStringProperty();//团家ID
  private SimpleStringProperty clubSumProfit = new SimpleStringProperty();// 开始统计时间
  private SimpleStringProperty clubLmType = new SimpleStringProperty();// 开始统计时间

  public String getClubLmType() {
    return clubLmType.get();
  }

  public SimpleStringProperty clubLmTypeProperty() {
    return clubLmType;
  }

  public void setClubLmType(String clubLmType) {
    this.clubLmType.set(clubLmType);
  }

  public String getClubName() {
    return clubName.get();
  }

  public SimpleStringProperty clubNameProperty() {
    return clubName;
  }

  public void setClubName(String clubName) {
    this.clubName.set(clubName);
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

  public String getClubSumZJ() {
    return clubSumZJ.get();
  }

  public SimpleStringProperty clubSumZJProperty() {
    return clubSumZJ;
  }

  public void setClubSumZJ(String clubSumZJ) {
    this.clubSumZJ.set(clubSumZJ);
  }

  public String getClubSumBaoxian() {
    return clubSumBaoxian.get();
  }

  public SimpleStringProperty clubSumBaoxianProperty() {
    return clubSumBaoxian;
  }

  public void setClubSumBaoxian(String clubSumBaoxian) {
    this.clubSumBaoxian.set(clubSumBaoxian);
  }

  public String getClubSumPerson() {
    return clubSumPerson.get();
  }

  public SimpleStringProperty clubSumPersonProperty() {
    return clubSumPerson;
  }

  public void setClubSumPerson(String clubSumPerson) {
    this.clubSumPerson.set(clubSumPerson);
  }

  public String getClubSumProfit() {
    return clubSumProfit.get();
  }

  public SimpleStringProperty clubSumProfitProperty() {
    return clubSumProfit;
  }

  public void setClubSumProfit(String clubSumProfit) {
    this.clubSumProfit.set(clubSumProfit);
  }
}
