package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author linzt
 */
public class GameInfo implements Entity {

  /**
   * 小游戏俱乐部从属股东
   */
  @MyExcelField(title = "股东", colWidth = 15)
  private SimpleStringProperty gameGudong = new SimpleStringProperty();
  /**
   * 小游戏俱乐部ID
   */
  @MyExcelField(title = "俱乐部ID", colWidth = 15)
  private SimpleStringProperty gameClubId = new SimpleStringProperty();
  /**
   * 小游戏俱乐部名称
   */
  @MyExcelField(title = "俱乐部名称", colWidth = 15)
  private SimpleStringProperty gameClubName = new SimpleStringProperty();

  @MyExcelField(title = "玩家ID", colWidth = 15)
  private SimpleStringProperty gamePlayerId = new SimpleStringProperty();

  @MyExcelField(title = "玩家名称", colWidth = 15)
  private SimpleStringProperty gamePlayerName = new SimpleStringProperty();

  @MyExcelField(title = "牌局", colWidth = 15)
  private SimpleStringProperty gamePaiju = new SimpleStringProperty();
  /**
   * 类型
   */
  @MyExcelField(title = "类型", colWidth = 15)
  private SimpleStringProperty gameType = new SimpleStringProperty();
  /**
   * 小游戏联盟分成
   */
  @MyExcelField(title = "联盟分成", colWidth = 15)
  private SimpleStringProperty gameLianmengFencheng = new SimpleStringProperty();
  /**
   * 小游戏俱乐部分成
   */
  @MyExcelField(title = "俱乐部分成", colWidth = 15)
  private SimpleStringProperty gameClubFencheng = new SimpleStringProperty();
  /**
   * 小游戏合计
   */
  @MyExcelField(title = "俱乐部合计", colWidth = 15)
  private SimpleStringProperty gameClubHeji = new SimpleStringProperty();

  // 自定义额外数据
  private List<GameInfo> detailList;

  public GameInfo() {
  }

  public String getGameClubId() {
    return gameClubId.get();
  }

  public SimpleStringProperty gameClubIdProperty() {
    return gameClubId;
  }

  public void setGameClubId(String gameClubId) {
    this.gameClubId.set(gameClubId);
  }

  public String getGameClubName() {
    return gameClubName.get();
  }

  public SimpleStringProperty gameClubNameProperty() {
    return gameClubName;
  }

  public void setGameClubName(String gameClubName) {
    this.gameClubName.set(gameClubName);
  }

  public String getGameLianmengFencheng() {
    return gameLianmengFencheng.get();
  }

  public SimpleStringProperty gameLianmengFenchengProperty() {
    return gameLianmengFencheng;
  }

  public void setGameLianmengFencheng(String gameLianmengFencheng) {
    this.gameLianmengFencheng.set(gameLianmengFencheng);
  }

  public String getGameClubFencheng() {
    return gameClubFencheng.get();
  }

  public SimpleStringProperty gameClubFenchengProperty() {
    return gameClubFencheng;
  }

  public void setGameClubFencheng(String gameClubFencheng) {
    this.gameClubFencheng.set(gameClubFencheng);
  }

  public String getGameClubHeji() {
    return gameClubHeji.get();
  }

  public SimpleStringProperty gameClubHejiProperty() {
    return gameClubHeji;
  }

  public void setGameClubHeji(String gameClubHeji) {
    this.gameClubHeji.set(gameClubHeji);
  }


  public List<GameInfo> getDetailList() {
    return detailList;
  }

  public void setDetailList(List<GameInfo> detailList) {
    this.detailList = detailList;
  }

  public String getGamePlayerId() {
    return gamePlayerId.get();
  }

  public SimpleStringProperty gamePlayerIdProperty() {
    return gamePlayerId;
  }

  public void setGamePlayerId(String gamePlayerId) {
    this.gamePlayerId.set(gamePlayerId);
  }

  public String getGamePlayerName() {
    return gamePlayerName.get();
  }

  public SimpleStringProperty gamePlayerNameProperty() {
    return gamePlayerName;
  }

  public void setGamePlayerName(String gamePlayerName) {
    this.gamePlayerName.set(gamePlayerName);
  }

  public String getGamePaiju() {
    return gamePaiju.get();
  }

  public SimpleStringProperty gamePaijuProperty() {
    return gamePaiju;
  }

  public void setGamePaiju(String gamePaiju) {
    this.gamePaiju.set(gamePaiju);
  }

  public String getGameType() {
    return gameType.get();
  }

  public SimpleStringProperty gameTypeProperty() {
    return gameType;
  }

  public void setGameType(String gameType) {
    this.gameType.set(gameType);
  }

  public String getGameGudong() {
    return gameGudong.get();
  }

  public SimpleStringProperty gameGudongProperty() {
    return gameGudong;
  }

  public void setGameGudong(String gameGudong) {
    this.gameGudong.set(gameGudong);
  }
}
