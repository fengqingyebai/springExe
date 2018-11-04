package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 俱乐部历史统计对象
 *
 */
public class ClubStaticInfo implements Entity {


  @MyExcelField(title = "统计时间", colWidth = 15)
  private SimpleStringProperty clubStaticTime = new SimpleStringProperty();

  @MyExcelField(title = "所属联盟", colWidth = 15)
  private SimpleStringProperty clubLmType = new SimpleStringProperty();

  @MyExcelField(title = "俱乐部名称", colWidth = 15)
  private SimpleStringProperty clubName = new SimpleStringProperty();

  @MyExcelField(title = "俱乐部ID", colWidth = 15)
  private SimpleStringProperty clubId = new SimpleStringProperty();

  @MyExcelField(title = "总战绩")
  private SimpleStringProperty clubSumZJ = new SimpleStringProperty();

  @MyExcelField(title = "总保险")
  private SimpleStringProperty clubSumBaoxian = new SimpleStringProperty();

  @MyExcelField(title = "总人数")
  private SimpleStringProperty clubSumPerson = new SimpleStringProperty();

  @MyExcelField(title = "总交收")
  private SimpleStringProperty clubSumProfit = new SimpleStringProperty();




  public String getClubLmType() {
    return clubLmType.get();
  }

  public String getClubStaticTime() {
    return clubStaticTime.get();
  }

  public SimpleStringProperty clubStaticTimeProperty() {
    return clubStaticTime;
  }

  public void setClubStaticTime(String clubStaticTime) {
    this.clubStaticTime.set(clubStaticTime);
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
