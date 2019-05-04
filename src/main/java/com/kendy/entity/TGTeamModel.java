package com.kendy.entity;

public class TGTeamModel {

  private String tgTeamId;
  private String tgHuishui;
  private String tgHuiBao;
  private String tgFwfRate;
  private String tgTeamProxy = "0"; //托管团队是否代理，1：是 0：否（默认）


  /**
   *
   */
  public TGTeamModel() {
    super();
    // TODO Auto-generated constructor stub
  }


  /**
   * @param tgTeamId
   * @param tgHuishui
   * @param tgHuiBao
   * @param tgFwfRate
   */
  public TGTeamModel(java.lang.String tgTeamId, java.lang.String tgHuishui,
      java.lang.String tgHuiBao,
      java.lang.String tgFwfRate, String tgTeamProxy) {
    super();
    this.tgTeamId = tgTeamId;
    this.tgHuishui = tgHuishui;
    this.tgHuiBao = tgHuiBao;
    this.tgFwfRate = tgFwfRate;
    this.tgTeamProxy = tgTeamProxy;
  }


  public String getTgTeamId() {
    return tgTeamId;
  }

  public void setTgTeamId(String tgTeamId) {
    this.tgTeamId = tgTeamId;
  }

  public String getTgHuishui() {
    return tgHuishui;
  }

  public void setTgHuishui(String tgHuishui) {
    this.tgHuishui = tgHuishui;
  }

  public String getTgHuiBao() {
    return tgHuiBao;
  }

  public void setTgHuiBao(String tgHuiBao) {
    this.tgHuiBao = tgHuiBao;
  }

  public String getTgFwfRate() {
    return tgFwfRate;
  }

  public void setTgFwfRate(String tgFwfRate) {
    this.tgFwfRate = tgFwfRate;
  }


  public String getTgTeamProxy() {
    return tgTeamProxy;
  }


  public void setTgTeamProxy(String tgTeamProxy) {
    this.tgTeamProxy = tgTeamProxy;
  }


  @Override
  public String toString() {
    return "TGTeamModel [tgTeamId=" + tgTeamId + ", tgHuishui=" + tgHuishui + ", tgHuiBao="
        + tgHuiBao + ", tgFwfRate="
        + tgFwfRate + "]";
  }


}
