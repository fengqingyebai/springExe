package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 战绩统计中的单个俱乐部对象
 */
public class ZjClubStaticDetailInfo implements Entity {

  @MyExcelField(title = "序号")
  private SimpleStringProperty detailClubIndex = new SimpleStringProperty();

  @MyExcelField(title = "俱乐部名称", colWidth = 12)
  private SimpleStringProperty detailClubName = new SimpleStringProperty();

  @MyExcelField(title = "俱乐部ID", colWidth = 12)
  private SimpleStringProperty detailClubId = new SimpleStringProperty();

  @MyExcelField(title = "玩家名称")
  private SimpleStringProperty detailClubPlayerName = new SimpleStringProperty();

  @MyExcelField(title = "玩家ID")
  private SimpleStringProperty detailClubPlayerId = new SimpleStringProperty();

  @MyExcelField(title = "开始统计时间", colWidth = 15)
  private SimpleStringProperty detailClubBeginStaticTime = new SimpleStringProperty();

  @MyExcelField(title = "累计战绩")
  private SimpleStringProperty detailClubTotalZJ = new SimpleStringProperty();

  @MyExcelField(title = "人次")
  private SimpleStringProperty detailClubPersonCount = new SimpleStringProperty();


  public ZjClubStaticDetailInfo() {
  }

  public String getDetailClubIndex() {
    return detailClubIndex.get();
  }

  public SimpleStringProperty detailClubIndexProperty() {
    return detailClubIndex;
  }

  public void setDetailClubIndex(String detailClubIndex) {
    this.detailClubIndex.set(detailClubIndex);
  }

  public String getDetailClubName() {
    return detailClubName.get();
  }

  public SimpleStringProperty detailClubNameProperty() {
    return detailClubName;
  }

  public void setDetailClubName(String detailClubName) {
    this.detailClubName.set(detailClubName);
  }

  public String getDetailClubId() {
    return detailClubId.get();
  }

  public SimpleStringProperty detailClubIdProperty() {
    return detailClubId;
  }

  public void setDetailClubId(String detailClubId) {
    this.detailClubId.set(detailClubId);
  }

  public String getDetailClubPlayerName() {
    return detailClubPlayerName.get();
  }

  public SimpleStringProperty detailClubPlayerNameProperty() {
    return detailClubPlayerName;
  }

  public void setDetailClubPlayerName(String detailClubPlayerName) {
    this.detailClubPlayerName.set(detailClubPlayerName);
  }

  public String getDetailClubPlayerId() {
    return detailClubPlayerId.get();
  }

  public SimpleStringProperty detailClubPlayerIdProperty() {
    return detailClubPlayerId;
  }

  public void setDetailClubPlayerId(String detailClubPlayerId) {
    this.detailClubPlayerId.set(detailClubPlayerId);
  }

  public String getDetailClubBeginStaticTime() {
    return detailClubBeginStaticTime.get();
  }

  public SimpleStringProperty detailClubBeginStaticTimeProperty() {
    return detailClubBeginStaticTime;
  }

  public void setDetailClubBeginStaticTime(String detailClubBeginStaticTime) {
    this.detailClubBeginStaticTime.set(detailClubBeginStaticTime);
  }

  public String getDetailClubPersonCount() {
    return detailClubPersonCount.get();
  }

  public SimpleStringProperty detailClubPersonCountProperty() {
    return detailClubPersonCount;
  }

  public void setDetailClubPersonCount(String detailClubPersonCount) {
    this.detailClubPersonCount.set(detailClubPersonCount);
  }

  public String getDetailClubTotalZJ() {
    return detailClubTotalZJ.get();
  }

  public SimpleStringProperty detailClubTotalZJProperty() {
    return detailClubTotalZJ;
  }

  public void setDetailClubTotalZJ(String detailClubTotalZJ) {
    this.detailClubTotalZJ.set(detailClubTotalZJ);
  }
}
