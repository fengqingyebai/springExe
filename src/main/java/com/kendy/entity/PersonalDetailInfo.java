package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 个人回水
 *
 * @author 林泽涛
 */
public class PersonalDetailInfo implements Entity {

  @MyExcelField(title = "俱乐部ID")
  private SimpleStringProperty clubId = new SimpleStringProperty();

  @MyExcelField(title = "俱乐部名称")
  private SimpleStringProperty clubName = new SimpleStringProperty();

  @MyExcelField(title = "团队")
  private SimpleStringProperty teamId = new SimpleStringProperty();

  @MyExcelField(title = "玩家ID", colWidth = 15)
  private SimpleStringProperty playerId = new SimpleStringProperty();

  @MyExcelField(title = "玩家名称", colWidth = 25)
  private SimpleStringProperty playerName = new SimpleStringProperty();

  @MyExcelField(title = "类型", colWidth = 15)
  private SimpleStringProperty juType = new SimpleStringProperty();

  @MyExcelField(title = "牌局", colWidth = 15)
  private SimpleStringProperty tableId = new SimpleStringProperty();

  @MyExcelField(title = "计分")
  private SimpleStringProperty jifen = new SimpleStringProperty();

  @MyExcelField(title = "实收")
  private SimpleStringProperty shishou = new SimpleStringProperty();

  @MyExcelField(title = "保险")
  private SimpleStringProperty baoxian = new SimpleStringProperty();

  @MyExcelField(title = "出回水")
  private SimpleStringProperty chuHuishui = new SimpleStringProperty();

  @MyExcelField(title = "回保")
  private SimpleStringProperty huibao = new SimpleStringProperty();


  public PersonalDetailInfo() {
    super();
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

  public String getClubName() {
    return clubName.get();
  }

  public SimpleStringProperty clubNameProperty() {
    return clubName;
  }

  public void setClubName(String clubName) {
    this.clubName.set(clubName);
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

  public String getPlayerId() {
    return playerId.get();
  }

  public SimpleStringProperty playerIdProperty() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId.set(playerId);
  }

  public String getPlayerName() {
    return playerName.get();
  }

  public SimpleStringProperty playerNameProperty() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName.set(playerName);
  }

  public String getJifen() {
    return jifen.get();
  }

  public SimpleStringProperty jifenProperty() {
    return jifen;
  }

  public void setJifen(String jifen) {
    this.jifen.set(jifen);
  }

  public String getShishou() {
    return shishou.get();
  }

  public SimpleStringProperty shishouProperty() {
    return shishou;
  }

  public void setShishou(String shishou) {
    this.shishou.set(shishou);
  }

  public String getBaoxian() {
    return baoxian.get();
  }

  public SimpleStringProperty baoxianProperty() {
    return baoxian;
  }

  public void setBaoxian(String baoxian) {
    this.baoxian.set(baoxian);
  }

  public String getChuHuishui() {
    return chuHuishui.get();
  }

  public SimpleStringProperty chuHuishuiProperty() {
    return chuHuishui;
  }

  public void setChuHuishui(String chuHuishui) {
    this.chuHuishui.set(chuHuishui);
  }

  public String getHuibao() {
    return huibao.get();
  }

  public SimpleStringProperty huibaoProperty() {
    return huibao;
  }

  public void setHuibao(String huibao) {
    this.huibao.set(huibao);
  }

  public String getJuType() {
    return juType.get();
  }

  public SimpleStringProperty juTypeProperty() {
    return juType;
  }

  public void setJuType(String juType) {
    this.juType.set(juType);
  }

  public String getTableId() {
    return tableId.get();
  }

  public SimpleStringProperty tableIdProperty() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId.set(tableId);


  }
}
