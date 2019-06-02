package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * 主机表第一个TableView所绑定的实体
 *
 * @author 林泽涛
 */
public class TotalInfo implements Entity {

  @MyExcelField(title = "团ID")
  private SimpleStringProperty tuan = new SimpleStringProperty();//团

  @MyExcelField(title = "玩家ID", colWidth = 15)
  private SimpleStringProperty wanjiaId = new SimpleStringProperty();//ID

  @MyExcelField(title = "玩家名称", colWidth = 15)
  private SimpleStringProperty wanjia = new SimpleStringProperty();//玩家

  @MyExcelField(title = "计分")
  private SimpleStringProperty jifen = new SimpleStringProperty();//计分

  @MyExcelField(title = "实收")
  private SimpleStringProperty shishou = new SimpleStringProperty();//实收

  @MyExcelField(title = "保险")
  private SimpleStringProperty baoxian = new SimpleStringProperty();//保险

  @MyExcelField(title = "出回水")
  private SimpleStringProperty chuHuishui = new SimpleStringProperty();//出回水

  @MyExcelField(title = "保回")
  private SimpleStringProperty baohui = new SimpleStringProperty();//保回

  @MyExcelField(title = "水后险")
  private SimpleStringProperty shuihouxian = new SimpleStringProperty();//水后险

  @MyExcelField(title = "收回水")
  private SimpleStringProperty shouHuishui = new SimpleStringProperty();//收回水

  @MyExcelField(title = "小游戏返利")
  private SimpleStringProperty gameFL = new SimpleStringProperty();//收回水

  @MyExcelField(title = "合利润")
  private SimpleStringProperty heLirun = new SimpleStringProperty();//合利润

  private SimpleStringProperty isLittleGame = new SimpleStringProperty("0"); // 是否为小游戏


  public TotalInfo() {
    super();
  }


  public TotalInfo(String tuan, String wanjiaId, String wanjia) {
    super();
    this.tuan = new SimpleStringProperty(tuan);
    this.wanjiaId = new SimpleStringProperty(wanjiaId);
    this.wanjia = new SimpleStringProperty(wanjia);
  }


  public SimpleStringProperty tuanProperty() {
    return this.tuan;
  }


  public String getTuan() {
    return this.tuanProperty().get();
  }


  public void setTuan(final String tuan) {
    this.tuanProperty().set(tuan);
  }


  public SimpleStringProperty wanjiaIdProperty() {
    return this.wanjiaId;
  }


  public String getWanjiaId() {
    return this.wanjiaIdProperty().get();
  }


  public void setWanjiaId(final String wanjiaId) {
    this.wanjiaIdProperty().set(wanjiaId);
  }


  public SimpleStringProperty wanjiaProperty() {
    return this.wanjia;
  }


  public String getWanjia() {
    return this.wanjiaProperty().get();
  }


  public void setWanjia(final String wanjia) {
    this.wanjiaProperty().set(wanjia);
  }


  public SimpleStringProperty jifenProperty() {
    return this.jifen;
  }


  public String getJifen() {
    return this.jifenProperty().get();
  }


  public void setJifen(final String jifen) {
    this.jifenProperty().set(jifen);
  }


  public SimpleStringProperty shishouProperty() {
    return this.shishou;
  }


  public String getShishou() {
    return this.shishouProperty().get();
  }


  public void setShishou(final String shishou) {
    this.shishouProperty().set(shishou);
  }


  public SimpleStringProperty baoxianProperty() {
    return this.baoxian;
  }


  public String getBaoxian() {
    return this.baoxianProperty().get();
  }


  public void setBaoxian(final String baoxian) {
    this.baoxianProperty().set(baoxian);
  }


  public SimpleStringProperty chuHuishuiProperty() {
    return this.chuHuishui;
  }


  public String getChuHuishui() {
    return this.chuHuishuiProperty().get();
  }


  public void setChuHuishui(final String chuHuishui) {
    this.chuHuishuiProperty().set(chuHuishui);
  }


  public SimpleStringProperty baohuiProperty() {
    return this.baohui;
  }


  public String getBaohui() {
    return this.baohuiProperty().get();
  }


  public void setBaohui(final String baohui) {
    this.baohuiProperty().set(baohui);
  }


  public SimpleStringProperty shuihouxianProperty() {
    return this.shuihouxian;
  }


  public String getShuihouxian() {
    return this.shuihouxianProperty().get();
  }


  public void setShuihouxian(final String shuihouxian) {
    this.shuihouxianProperty().set(shuihouxian);
  }


  public SimpleStringProperty shouHuishuiProperty() {
    return this.shouHuishui;
  }


  public String getShouHuishui() {
    return this.shouHuishuiProperty().get();
  }


  public void setShouHuishui(final String shouHuishui) {
    this.shouHuishuiProperty().set(shouHuishui);
  }


  public SimpleStringProperty heLirunProperty() {
    return this.heLirun;
  }


  public String getHeLirun() {
    return this.heLirunProperty().get();
  }


  public void setHeLirun(final String heLirun) {
    this.heLirunProperty().set(heLirun);
  }

  public String getIsLittleGame() {
    return isLittleGame.get();
  }

  public SimpleStringProperty isLittleGameProperty() {
    return isLittleGame;
  }

  public void setIsLittleGame(String isLittleGame) {
    this.isLittleGame.set(isLittleGame);
  }

  public String getGameFL() {
    return gameFL.get();
  }

  public SimpleStringProperty gameFLProperty() {
    return gameFL;
  }

  public void setGameFL(String gameFL) {
    this.gameFL.set(gameFL);
  }
}
