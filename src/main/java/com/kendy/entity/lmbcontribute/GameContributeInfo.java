package com.kendy.entity.lmbcontribute;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 * 联盟币小游戏实体类
 * @author linzt
 */
public class GameContributeInfo implements Entity {

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
  /**
   * 小游戏联盟分成
   */
  @MyExcelField(title = "联盟分成", colWidth = 15)
  private SimpleStringProperty gameLianmengFencheng = new SimpleStringProperty();
  /**
   * 所属股东
   */
  @MyExcelField(title = "所属股东", colWidth = 15)
  private SimpleStringProperty gameGudong = new SimpleStringProperty();
  /**
   * 小游戏用户比例
   */
  @MyExcelField(title = "小游戏用户比例", colWidth = 15)
  private SimpleStringProperty gameUserRate = new SimpleStringProperty();
  /**
   * 小游戏股东贡献值
   */
  @MyExcelField(title = "小游戏股东贡献值", colWidth = 15)
  private SimpleStringProperty gameContribute = new SimpleStringProperty();

  public GameContributeInfo() {
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

  public String getGameGudong() {
    return gameGudong.get();
  }

  public SimpleStringProperty gameGudongProperty() {
    return gameGudong;
  }

  public void setGameGudong(String gameGudong) {
    this.gameGudong.set(gameGudong);
  }

  public String getGameUserRate() {
    return gameUserRate.get();
  }

  public SimpleStringProperty gameUserRateProperty() {
    return gameUserRate;
  }

  public void setGameUserRate(String gameUserRate) {
    this.gameUserRate.set(gameUserRate);
  }

  public String getGameContribute() {
    return gameContribute.get();
  }

  public SimpleStringProperty gameContributeProperty() {
    return gameContribute;
  }

  public void setGameContribute(String gameContribute) {
    this.gameContribute.set(gameContribute);
  }
}
