package com.kendy.entity.lmbcontribute;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 * 联盟币贡献值实体类
 * @author linzt
 * @date
 */
public class GlbContributeInfo implements Entity {

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
   * 俱乐部用户比例
   */
  @MyExcelField(title = "俱乐部用户比例", colWidth = 15)
  private SimpleStringProperty glbUserRate = new SimpleStringProperty();
  /**
   * 俱乐部股东贡献值
   */
  @MyExcelField(title = "俱乐部股东贡献值", colWidth = 15)
  private SimpleStringProperty glbContribute = new SimpleStringProperty();


  public GlbContributeInfo() {
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

  public String getGlbUserRate() {
    return glbUserRate.get();
  }

  public SimpleStringProperty glbUserRateProperty() {
    return glbUserRate;
  }

  public void setGlbUserRate(String glbUserRate) {
    this.glbUserRate.set(glbUserRate);
  }

  public String getGlbContribute() {
    return glbContribute.get();
  }

  public SimpleStringProperty glbContributeProperty() {
    return glbContribute;
  }

  public void setGlbContribute(String glbContribute) {
    this.glbContribute.set(glbContribute);
  }
}
