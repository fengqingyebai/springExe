package com.kendy.model;

import com.kendy.entity.CurrentMoneyInfo;

/**
 * @author linzt
 * @date
 */
public class RangeResult extends Result {

  String playerId;
  String playerName;
  String teamId;
  /*
   * 联盟币购买数量
   */
  String buyStack;
  /*
   * 实时金额
   * 非合并ID直接取最实时金额表中的实时金额
   * 合并ID则取所有节点的实时金额之和
   */
  String currentMoney;
  /*
   * 可用额度
   * 非合并ID直接取最实时金额表中的实可用额度
   * 合并ID则取所有节点的可用额度之最大值
   */
  String availableEdu;

  /*
   * 实时金额表中待修改的记录
   */
  CurrentMoneyInfo cmi;

  public RangeResult(boolean ok, String description, String currentMoney,
      String availableEdu) {
    super(ok, description);
    this.currentMoney = currentMoney;
    this.availableEdu = availableEdu;
  }


  public RangeResult(boolean ok, int code, String description, String playerId,
      String playerName, String teamId, String buyStack, String currentMoney,
      String availableEdu, CurrentMoneyInfo cmi) {
    super(ok, code, description);
    this.playerId = playerId;
    this.playerName = playerName;
    this.teamId = teamId;
    this.buyStack = buyStack;
    this.currentMoney = currentMoney;
    this.availableEdu = availableEdu;
    this.cmi = cmi;
  }

  public String getCurrentMoney() {
    return currentMoney;
  }

  public void setCurrentMoney(String currentMoney) {
    this.currentMoney = currentMoney;
  }

  public String getAvailableEdu() {
    return availableEdu;
  }

  public void setAvailableEdu(String availableEdu) {
    this.availableEdu = availableEdu;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getBuyStack() {
    return buyStack;
  }

  public void setBuyStack(String buyStack) {
    this.buyStack = buyStack;
  }

  public CurrentMoneyInfo getCmi() {
    return cmi;
  }

  public void setCmi(CurrentMoneyInfo cmi) {
    this.cmi = cmi;
  }
}
