package com.kendy.entity ;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
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
  @MyExcelField(title = "俱乐部ID", colWidth = 15)
  private SimpleStringProperty glbClubId = new SimpleStringProperty();
  /**
   * 俱乐部名称
   */
  @MyExcelField(title = "俱乐部名称", colWidth = 15)
  private SimpleStringProperty  glbClubName = new SimpleStringProperty();
  /**
   * 玩家ID
   */
  @MyExcelField(title = "玩家ID", colWidth = 15)
  private SimpleStringProperty  glbPlayerId = new SimpleStringProperty();
  /**
   * 玩家名称
   */
  @MyExcelField(title = "玩家名称", colWidth = 15)
  private SimpleStringProperty glbPlayerName = new SimpleStringProperty();
  /**
   * 牌局
   */
  @MyExcelField(title = "牌局", colWidth = 15)
  private SimpleStringProperty glbPaiju = new SimpleStringProperty();
  /**
   * 类型
   */
  @MyExcelField(title = "类型", colWidth = 15)
  private SimpleStringProperty glbType = new SimpleStringProperty();
  /**
   * 俱乐部保险抽取
   */
  @MyExcelField(title = "保险抽取", colWidth = 15)
  private SimpleStringProperty  glbBaoxianChouqu = new SimpleStringProperty();
  /**
   * 俱乐部战绩抽取
   */
  @MyExcelField(title = "战绩抽取", colWidth = 15)
  private SimpleStringProperty glbZhanjiChouqu = new SimpleStringProperty();
  /**
   * 俱乐部抽取合计
   */
  @MyExcelField(title = "抽取合计", colWidth = 15)
  private SimpleStringProperty glbChouquHeji = new SimpleStringProperty();
  /**
   * 俱乐部联盟代收水
   */
  @MyExcelField(title = "联盟代收水", colWidth = 15)
  private SimpleStringProperty  glbLianmengDaiShoushui = new SimpleStringProperty();
  /**
   * 俱乐部联盟返水
   * 小游戏庄位为1时返水为0
   * 小游戏加勒比海联盟返水=战绩S列相反值 * 0.1
   * 小游戏德州牛仔读取N列，即俱乐部分成
   */
  @MyExcelField(title = "联盟返水", colWidth = 15)
  private SimpleStringProperty glbLianmengFanshui = new SimpleStringProperty();
  /**
   * 俱乐部联盟保险交收
   */
  @MyExcelField(title = "联盟保险交收", colWidth = 15)
  private SimpleStringProperty  glbLianmengBXJiaoshou = new SimpleStringProperty();
  /**
   * 俱乐部联盟保险占成
   */
  @MyExcelField(title = "联盟保险占成", colWidth = 15)
  private SimpleStringProperty  glbLianmengBXZhancheng = new SimpleStringProperty();
  /**
   * 俱乐部合计
   */
  @MyExcelField(title = "俱乐部合计", colWidth = 15)
  private SimpleStringProperty  glbClubHeji = new SimpleStringProperty();

  /**
   * 是否为庄位
   * 1是 0否
   */
  private SimpleStringProperty  glbIsZhuangWei = new SimpleStringProperty();
  /**
   * 原始战绩
   */
  private SimpleStringProperty  glbYszj = new SimpleStringProperty();
  /**
   * 俱乐部分成
   */
  private SimpleStringProperty  glbClubFencheng = new SimpleStringProperty();

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

  public String getGlbType() {
    return glbType.get();
  }

  public SimpleStringProperty glbTypeProperty() {
    return glbType;
  }

  public void setGlbType(String glbType) {
    this.glbType.set(glbType);
  }

  public String getGlbIsZhuangWei() {
    return glbIsZhuangWei.get();
  }

  public SimpleStringProperty glbIsZhuangWeiProperty() {
    return glbIsZhuangWei;
  }

  public void setGlbIsZhuangWei(String glbIsZhuangWei) {
    this.glbIsZhuangWei.set(glbIsZhuangWei);
  }

  public String getGlbYszj() {
    return glbYszj.get();
  }

  public SimpleStringProperty glbYszjProperty() {
    return glbYszj;
  }

  public void setGlbYszj(String glbYszj) {
    this.glbYszj.set(glbYszj);
  }

  public String getGlbClubFencheng() {
    return glbClubFencheng.get();
  }

  public SimpleStringProperty glbClubFenchengProperty() {
    return glbClubFencheng;
  }

  public void setGlbClubFencheng(String glbClubFencheng) {
    this.glbClubFencheng.set(glbClubFencheng);
  }
}
