package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 团队历史统计对象
 */
public class ZjTeamStaticDetailInfo implements Entity {

  @MyExcelField(title = "俱乐部ID", colWidth = 12)
  private SimpleStringProperty detailClubId = new SimpleStringProperty();

  @MyExcelField(title = "团队ID")
  private SimpleStringProperty detailTeamId = new SimpleStringProperty();

  @MyExcelField(title = "玩家ID")
  private SimpleStringProperty detailPlayerId = new SimpleStringProperty();

  @MyExcelField(title = "玩家名称")
  private SimpleStringProperty detailPlayerName = new SimpleStringProperty();

  @MyExcelField(title = "累计战绩")
  private SimpleStringProperty detailPersonSumYszj = new SimpleStringProperty();

  @MyExcelField(title = "总人数")
  private SimpleStringProperty detailPersonCount = new SimpleStringProperty();

  public ZjTeamStaticDetailInfo() {
  }

  public ZjTeamStaticDetailInfo(SimpleStringProperty detailClubId,
      SimpleStringProperty detailTeamId, SimpleStringProperty detailPlayerId,
      SimpleStringProperty detailPlayerName,
      SimpleStringProperty detailPersonSumYszj,
      SimpleStringProperty detailPersonCount) {
    this.detailClubId = detailClubId;
    this.detailTeamId = detailTeamId;
    this.detailPlayerId = detailPlayerId;
    this.detailPlayerName = detailPlayerName;
    this.detailPersonSumYszj = detailPersonSumYszj;
    this.detailPersonCount = detailPersonCount;
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

  public String getDetailTeamId() {
    return detailTeamId.get();
  }

  public SimpleStringProperty detailTeamIdProperty() {
    return detailTeamId;
  }

  public void setDetailTeamId(String detailTeamId) {
    this.detailTeamId.set(detailTeamId);
  }

  public String getDetailPlayerId() {
    return detailPlayerId.get();
  }

  public SimpleStringProperty detailPlayerIdProperty() {
    return detailPlayerId;
  }

  public void setDetailPlayerId(String detailPlayerId) {
    this.detailPlayerId.set(detailPlayerId);
  }

  public String getDetailPlayerName() {
    return detailPlayerName.get();
  }

  public SimpleStringProperty detailPlayerNameProperty() {
    return detailPlayerName;
  }

  public void setDetailPlayerName(String detailPlayerName) {
    this.detailPlayerName.set(detailPlayerName);
  }

  public String getDetailPersonSumYszj() {
    return detailPersonSumYszj.get();
  }

  public SimpleStringProperty detailPersonSumYszjProperty() {
    return detailPersonSumYszj;
  }

  public void setDetailPersonSumYszj(String detailPersonSumYszj) {
    this.detailPersonSumYszj.set(detailPersonSumYszj);
  }

  public String getDetailPersonCount() {
    return detailPersonCount.get();
  }

  public SimpleStringProperty detailPersonCountProperty() {
    return detailPersonCount;
  }

  public void setDetailPersonCount(String detailPersonCount) {
    this.detailPersonCount.set(detailPersonCount);
  }
}
