package com.kendy.entity ;

import com.kendy.interfaces.Entity;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author linzt
 * @date
 */
public class GlbInfo implements Entity {

  /**
   * 俱乐部ID
   */
  private SimpleStringProperty glbClubId = new SimpleStringProperty();
  /**
   * 俱乐部名称
   */
  private SimpleStringProperty  glbClubName = new SimpleStringProperty();
  /**
   * 玩家ID
   */
  private SimpleStringProperty  glbPlayerId = new SimpleStringProperty();
  /**
   * 玩家名称
   */
  private SimpleStringProperty glbPlayerName = new SimpleStringProperty();
  /**
   * 牌局
   */
  private SimpleStringProperty glbPaiju = new SimpleStringProperty();
  /**
   * 俱乐部保险抽取
   */
  private SimpleStringProperty  glbBaoxianChouqu = new SimpleStringProperty();
  /**
   * 俱乐部战绩抽取
   */
  private SimpleStringProperty glbZhanjiChouqu = new SimpleStringProperty();
  /**
   * 俱乐部抽取合计
   */
  private SimpleStringProperty glbChouquHeji = new SimpleStringProperty();
  /**
   * 俱乐部联盟代收水
   */
  private SimpleStringProperty  glbLianmengDaiShoushui = new SimpleStringProperty();
  /**
   * 俱乐部联盟返水
   */
  private SimpleStringProperty glbLianmengFanshui = new SimpleStringProperty();
  /**
   * 俱乐部联盟保险交收
   */
  private SimpleStringProperty  glbLianmengBXJiaoshou = new SimpleStringProperty();
  /**
   * 俱乐部联盟保险占成
   */
  private SimpleStringProperty  glbLianmengBXZhancheng = new SimpleStringProperty();
  /**
   * 俱乐部合计
   */
  private SimpleStringProperty  glbClubHeji = new SimpleStringProperty();

  public GlbInfo() {
  }

  public String getGlbClubId() {
    return glbClubId.get();
  }

  public SimpleStringProperty glbClubIdProperty() {
    return glbClubId;
  }

  public void setGlbClubId(String glbClubId) {
    this.glbClubId.set(glbClubId);
  }

  public String getGlbClubName() {
    return glbClubName.get();
  }

  public SimpleStringProperty glbClubNameProperty() {
    return glbClubName;
  }

  public void setGlbClubName(String glbClubName) {
    this.glbClubName.set(glbClubName);
  }

  public String getGlbPlayerId() {
    return glbPlayerId.get();
  }

  public SimpleStringProperty glbPlayerIdProperty() {
    return glbPlayerId;
  }

  public void setGlbPlayerId(String glbPlayerId) {
    this.glbPlayerId.set(glbPlayerId);
  }

  public String getGlbPlayerName() {
    return glbPlayerName.get();
  }

  public SimpleStringProperty glbPlayerNameProperty() {
    return glbPlayerName;
  }

  public void setGlbPlayerName(String glbPlayerName) {
    this.glbPlayerName.set(glbPlayerName);
  }

  public String getGlbPaiju() {
    return glbPaiju.get();
  }

  public SimpleStringProperty glbPaijuProperty() {
    return glbPaiju;
  }

  public void setGlbPaiju(String glbPaiju) {
    this.glbPaiju.set(glbPaiju);
  }

  public String getGlbBaoxianChouqu() {
    return glbBaoxianChouqu.get();
  }

  public SimpleStringProperty glbBaoxianChouquProperty() {
    return glbBaoxianChouqu;
  }

  public void setGlbBaoxianChouqu(String glbBaoxianChouqu) {
    this.glbBaoxianChouqu.set(glbBaoxianChouqu);
  }

  public String getGlbZhanjiChouqu() {
    return glbZhanjiChouqu.get();
  }

  public SimpleStringProperty glbZhanjiChouquProperty() {
    return glbZhanjiChouqu;
  }

  public void setGlbZhanjiChouqu(String glbZhanjiChouqu) {
    this.glbZhanjiChouqu.set(glbZhanjiChouqu);
  }

  public String getGlbChouquHeji() {
    return glbChouquHeji.get();
  }

  public SimpleStringProperty glbChouquHejiProperty() {
    return glbChouquHeji;
  }

  public void setGlbChouquHeji(String glbChouquHeji) {
    this.glbChouquHeji.set(glbChouquHeji);
  }

  public String getGlbLianmengDaiShoushui() {
    return glbLianmengDaiShoushui.get();
  }

  public SimpleStringProperty glbLianmengDaiShoushuiProperty() {
    return glbLianmengDaiShoushui;
  }

  public void setGlbLianmengDaiShoushui(String glbLianmengDaiShoushui) {
    this.glbLianmengDaiShoushui.set(glbLianmengDaiShoushui);
  }

  public String getGlbLianmengFanshui() {
    return glbLianmengFanshui.get();
  }

  public SimpleStringProperty glbLianmengFanshuiProperty() {
    return glbLianmengFanshui;
  }

  public void setGlbLianmengFanshui(String glbLianmengFanshui) {
    this.glbLianmengFanshui.set(glbLianmengFanshui);
  }

  public String getGlbLianmengBXJiaoshou() {
    return glbLianmengBXJiaoshou.get();
  }

  public SimpleStringProperty glbLianmengBXJiaoshouProperty() {
    return glbLianmengBXJiaoshou;
  }

  public void setGlbLianmengBXJiaoshou(String glbLianmengBXJiaoshou) {
    this.glbLianmengBXJiaoshou.set(glbLianmengBXJiaoshou);
  }

  public String getGlbLianmengBXZhancheng() {
    return glbLianmengBXZhancheng.get();
  }

  public SimpleStringProperty glbLianmengBXZhanchengProperty() {
    return glbLianmengBXZhancheng;
  }

  public void setGlbLianmengBXZhancheng(String glbLianmengBXZhancheng) {
    this.glbLianmengBXZhancheng.set(glbLianmengBXZhancheng);
  }

  public String getGlbClubHeji() {
    return glbClubHeji.get();
  }

  public SimpleStringProperty glbClubHejiProperty() {
    return glbClubHeji;
  }

  public void setGlbClubHeji(String glbClubHeji) {
    this.glbClubHeji.set(glbClubHeji);
  }

  // 自定义额外数据
  private List<GlbInfo> detailList;

  public List<GlbInfo> getDetailList() {
    return detailList;
  }

  public void setDetailList(List<GlbInfo> detailList) {
    this.detailList = detailList;
  }
}
