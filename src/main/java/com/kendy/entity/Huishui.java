package com.kendy.entity;

/**
 * 团队回水
 * TODO 将重构为实体类
 */
public class Huishui {

  //团id
  public String teamId;

  //团名
  public String teamName;

  //团队回水rate
  public String huishuiRate;

  //保险比例
  public String insuranceRate;

  //股东
  public String gudong;

  //战绩是否代管理
  public String zjManaged;

  //备注
  public String beizhu;

  //回水比例--回水的回水
  public String proxyHSRate = "0%";

  //回保比例
  public String proxyHBRate = "0%";

  //服务费有效值，比如大如1000，合计才会去计算服务费
  public String proxyFWF = "0";

  //代理查询导出时是否显示团队保险，默认不显示
  public String showInsure = "0";

  //实时上码中的团队押金
  public String teamYajin = "0";

  //实时上码中的团队额度
  public String teamEdu = "0";

  //打勾：团队可上码
  public String teamAvailabel = "0";

  // 团队小游戏返利比例
  public String teamGameFLRate = "0%";

  public Huishui() {
    super();
  }


  public String getTeamId() {
    return teamId;
  }

  public Huishui(String teamId, String teamName, String huishuiRate, String insuranceRate,
      String gudong,
      String zjManaged, String beizhu, String proxyHSRate, String proxyHBRate, String proxyFWF, String teamGameFLRate) {
    super();
    this.teamId = teamId;
    this.teamName = teamName;
    this.huishuiRate = huishuiRate;
    this.insuranceRate = insuranceRate;
    this.gudong = gudong;
    this.zjManaged = zjManaged;
    this.beizhu = beizhu;
    this.proxyHSRate = proxyHSRate;
    this.proxyHBRate = proxyHBRate;
    this.proxyFWF = proxyFWF;
    this.teamGameFLRate = teamGameFLRate;
  }


  /**
   * 所有字段的构造方法
   */
  public Huishui(String teamId, String teamName, String huishuiRate, String insuranceRate,
      String gudong,
      String zjManaged, String beizhu, String proxyHSRate, String proxyHBRate, String proxyFWF,
      String showInsure,
      String teamYajin, String teamEdu, String teamAvailabel, String teamGameFLRate) {
    super();
    this.teamId = teamId;
    this.teamName = teamName;
    this.huishuiRate = huishuiRate;
    this.insuranceRate = insuranceRate;
    this.gudong = gudong;
    this.zjManaged = zjManaged;
    this.beizhu = beizhu;
    this.proxyHSRate = proxyHSRate;
    this.proxyHBRate = proxyHBRate;
    this.proxyFWF = proxyFWF;
    this.showInsure = showInsure;
    this.teamYajin = teamYajin;
    this.teamEdu = teamEdu;
    this.teamAvailabel = teamAvailabel;
    this.teamGameFLRate = teamGameFLRate;
  }


  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public String getGudong() {
    return gudong;
  }

  public void setGudong(String gudong) {
    this.gudong = gudong;
  }

  public String getHuishuiRate() {
    return huishuiRate;
  }

  public void setHuishuiRate(String huishuiRate) {
    this.huishuiRate = huishuiRate;
  }

  public String getBeizhu() {
    return beizhu;
  }

  public void setBeizhu(String beizhu) {
    this.beizhu = beizhu;
  }

  public String getInsuranceRate() {
    return insuranceRate;
  }

  public void setInsuranceRate(String insuranceRate) {
    this.insuranceRate = insuranceRate;
  }

  public String getZjManaged() {
    return zjManaged;
  }

  public void setZjManaged(String zjManaged) {
    this.zjManaged = zjManaged;
  }


  public String getProxyHBRate() {
    return proxyHBRate;
  }


  public void setProxyHBRate(String proxyHBRate) {
    this.proxyHBRate = proxyHBRate;
  }


  public String getProxyHSRate() {
    return proxyHSRate;
  }


  public void setProxyHSRate(String proxyHSRate) {
    this.proxyHSRate = proxyHSRate;
  }


  public String getProxyFWF() {
    return proxyFWF;
  }


  public void setProxyFWF(String proxyFWF) {
    this.proxyFWF = proxyFWF;
  }


  public String getShowInsure() {
    return showInsure;
  }


  public void setShowInsure(String showInsure) {
    this.showInsure = showInsure;
  }


  public String getTeamYajin() {
    return teamYajin;
  }


  public void setTeamYajin(String teamYajin) {
    this.teamYajin = teamYajin;
  }


  public String getTeamEdu() {
    return teamEdu;
  }


  public void setTeamEdu(String teamEdu) {
    this.teamEdu = teamEdu;
  }


  public String getTeamAvailabel() {
    return teamAvailabel;
  }


  public void setTeamAvailabel(String teamAvailabel) {
    this.teamAvailabel = teamAvailabel;
  }

  public String getTeamGameFLRate() {
    return teamGameFLRate;
  }

  public Huishui setTeamGameFLRate(String teamGameFLRate) {
    this.teamGameFLRate = teamGameFLRate;
    return this;
  }
}
