package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 战绩统计中的俱乐部对象
 */
public class ZjClubStaticInfo implements Entity {

  @MyExcelField(title = "俱乐部名称", colWidth = 12)
  private SimpleStringProperty clubName = new SimpleStringProperty();//俱乐部名称

  @MyExcelField(title = "俱乐部ID", colWidth = 12)
  private SimpleStringProperty clubId = new SimpleStringProperty();//俱乐部ID

  @MyExcelField(title = "开始统计时间", colWidth = 15)
  private SimpleStringProperty clubBeginStaticTime = new SimpleStringProperty();// 开始统计时间

  @MyExcelField(title = "人次")
  private SimpleStringProperty clubPersonCount = new SimpleStringProperty();//总人次

  public ZjClubStaticInfo() {
  }

  public ZjClubStaticInfo(SimpleStringProperty clubName,
      SimpleStringProperty clubId, SimpleStringProperty clubBeginStaticTime,
      SimpleStringProperty clubPersonCount) {
    this.clubName = clubName;
    this.clubId = clubId;
    this.clubBeginStaticTime = clubBeginStaticTime;
    this.clubPersonCount = clubPersonCount;
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

  public String getClubBeginStaticTime() {
    return clubBeginStaticTime.get();
  }

  public SimpleStringProperty clubBeginStaticTimeProperty() {
    return clubBeginStaticTime;
  }

  public void setClubBeginStaticTime(String clubBeginStaticTime) {
    this.clubBeginStaticTime.set(clubBeginStaticTime);
  }

  public String getClubPersonCount() {
    return clubPersonCount.get();
  }

  public SimpleStringProperty clubPersonCountProperty() {
    return clubPersonCount;
  }

  public void setClubPersonCount(String clubPersonCount) {
    this.clubPersonCount.set(clubPersonCount);
  }
}
