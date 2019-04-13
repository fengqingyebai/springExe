package com.kendy.entity;

import com.kendy.interfaces.Entity;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author linzt
 * @date
 */
public class GameInfo implements Entity {

  /**
   * 小游戏俱乐部ID
   */
  private SimpleStringProperty gameClubId = new SimpleStringProperty();
  /**
   * 小游戏俱乐部名称
   */
  private SimpleStringProperty gameClubName = new SimpleStringProperty();
  /**
   * 小游戏联盟分成
   */
  private SimpleStringProperty gameLianmengFencheng = new SimpleStringProperty();
  /**
   * 小游戏俱乐部分成
   */
  private SimpleStringProperty gameClubFencheng = new SimpleStringProperty();
  /**
   * 小游戏合计
   */
  private SimpleStringProperty gameClubHeji = new SimpleStringProperty();
  private SimpleStringProperty gamePlayerId = new SimpleStringProperty();
  private SimpleStringProperty gamePlayerName = new SimpleStringProperty();
  private SimpleStringProperty gamePaiju = new SimpleStringProperty();

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
}
