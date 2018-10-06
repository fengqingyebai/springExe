package com.kendy.entity;

import com.kendy.enums.BankEnum;
import com.kendy.interfaces.Entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * 银行类
 *
 * @author linzt
 * @time 2018年6月28日
 */
public class BankFlowInfo implements Entity {


  private SimpleStringProperty index = new SimpleStringProperty(""); // 序号
  private SimpleStringProperty dateString = new SimpleStringProperty(""); // 时间

  private SimpleStringProperty yuEBao = new SimpleStringProperty(""); // 余额宝
  private SimpleStringProperty huaXia = new SimpleStringProperty(""); // 华夏
  private SimpleStringProperty pingAn = new SimpleStringProperty(""); // 平安
  private SimpleStringProperty zhaoShang = new SimpleStringProperty(""); // 招商
  private SimpleStringProperty zhiFuBao = new SimpleStringProperty(""); // 支付宝
  private SimpleStringProperty puFa = new SimpleStringProperty(""); // 浦发
  private SimpleStringProperty xingYe = new SimpleStringProperty(""); // 兴业
  //中国，中信，民生，光大，建设，工商
  private SimpleStringProperty zhongGuo = new SimpleStringProperty("");//类型
  private SimpleStringProperty zhongXin = new SimpleStringProperty("");//值
  private SimpleStringProperty minSheng = new SimpleStringProperty("");//描述（备选项）
  private SimpleStringProperty guangDa = new SimpleStringProperty("");//类型
  private SimpleStringProperty jianShe = new SimpleStringProperty("");//金额
  public SimpleStringProperty gongShang = new SimpleStringProperty("");//俱乐部ID


  public BankFlowInfo() {
    super();
  }

  public BankFlowInfo(String yuEBao, String huaXia,
      String pingAn, String zhaoShang, String zhiFuBao, String puFa) {
    super();
    this.yuEBao = new SimpleStringProperty(yuEBao);
    this.huaXia = new SimpleStringProperty(huaXia);
    this.pingAn = new SimpleStringProperty(pingAn);
    this.zhaoShang = new SimpleStringProperty(zhaoShang);
    this.zhiFuBao = new SimpleStringProperty(zhiFuBao);
    this.puFa = new SimpleStringProperty(puFa);
  }

  //==========================================
  public SimpleStringProperty indexProperty() {
    return this.index;
  }

  public String getIndex() {
    return this.indexProperty().get();
  }

  public void setIndex(final String index) {
    this.indexProperty().set(index);
  }

  //==========================================
  public SimpleStringProperty dateStringProperty() {
    return this.dateString;
  }

  public String getDateString() {
    return this.dateStringProperty().get();
  }

  public void setDateString(final String index) {
    this.dateStringProperty().set(index);
  }


  //=======================
  public SimpleStringProperty yuEBaoProperty() {
    return this.yuEBao;
  }

  public String getYuEBao() {
    return this.yuEBaoProperty().get();
  }

  public void setYuEBao(final String yuEBao) {
    this.yuEBaoProperty().set(yuEBao);
  }

  //=======================
  public SimpleStringProperty zhiFuBaoProperty() {
    return this.zhiFuBao;
  }

  public String getZhiFuBao() {
    return this.zhiFuBaoProperty().get();
  }

  public void setZhiFuBao(final String zhiFuBao) {
    this.zhiFuBaoProperty().set(zhiFuBao);
  }

  //=======================
  public SimpleStringProperty zhaoShangProperty() {
    return this.zhaoShang;
  }

  public String getZhaoShang() {
    return this.zhaoShangProperty().get();
  }

  public void setZhaoShang(final String zhaoShang) {
    this.zhaoShangProperty().set(zhaoShang);
  }


  //=======================
  public SimpleStringProperty huaXiaProperty() {
    return this.huaXia;
  }

  public String getHuaXia() {
    return this.huaXiaProperty().get();
  }

  public void setHuaXia(final String huaXia) {
    this.huaXiaProperty().set(huaXia);
  }


  //=======================
  public SimpleStringProperty pingAnProperty() {
    return this.pingAn;
  }

  public String getPingAn() {
    return this.pingAnProperty().get();
  }

  public void setPingAn(final String pingAn) {
    this.pingAnProperty().set(pingAn);
  }

  //=======================
  public SimpleStringProperty puFaProperty() {
    return this.puFa;
  }

  public String getPuFa() {
    return this.puFaProperty().get();
  }

  public void setPuFa(final String puFa) {
    this.puFaProperty().set(puFa);
  }

  //=======================
  public SimpleStringProperty xingYeProperty() {
    return this.xingYe;
  }

  public String getXingYe() {
    return this.xingYeProperty().get();
  }

  public void setXingYe(final String xingYe) {
    this.xingYeProperty().set(xingYe);
  }


  //=======================
  public SimpleStringProperty zhongGuoProperty() {
    return this.zhongGuo;
  }

  public String getZhongGuo() {
    return this.zhongGuoProperty().get();
  }

  public void setZhongGuo(final String zhongGuo) {
    this.zhongGuoProperty().set(zhongGuo);
  }

  //============================
  public SimpleStringProperty zhongXinProperty() {
    return this.zhongXin;
  }

  public String getZhongXin() {
    return this.zhongXinProperty().get();
  }

  public void setZhongXin(final String zhongXin) {
    this.zhongXinProperty().set(zhongXin);
  }

  //============================
  public SimpleStringProperty gongShangProperty() {
    return this.gongShang;
  }

  public String getGongShang() {
    return this.gongShangProperty().get();
  }

  public void setGongShang(final String gongShang) {
    this.gongShangProperty().set(gongShang);
  }

  //=======================
  public SimpleStringProperty minShengProperty() {
    return this.minSheng;
  }

  public String getMinSheng() {
    return this.minShengProperty().get();
  }

  public void setMinSheng(final String minSheng) {
    this.minShengProperty().set(minSheng);
  }

  //=======================
  public SimpleStringProperty guangDaProperty() {
    return this.guangDa;
  }

  public String getGuangDa() {
    return this.guangDaProperty().get();
  }

  public void setGuangDa(final String guangDa) {
    this.guangDaProperty().set(guangDa);
  }


  //=======================
  public SimpleStringProperty jianSheProperty() {
    return this.jianShe;
  }

  public String getJianShe() {
    return this.jianSheProperty().get();
  }

  public void setJianShe(final String jianShe) {
    this.jianSheProperty().set(jianShe);
  }


  @Override
  public String toString() {
    return "KaixiaoInfo [huaXia=" + huaXia.get() + ", pingAn=" + pingAn.get() + "]";
  }


}
