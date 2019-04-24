package com.kendy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linzt
 * @date
 */
public class LmbCache {

  /**
   * 小游戏类型
   */
  private List<String> gameTypes = new ArrayList<>();
  /**
   * 德州牛仔庄位玩家ID
   */
  private List<String> dezhouZhuangweiIds = new ArrayList<>();

  /**
   * 加勒比海联盟分成比例
   */
  private String jialebiLMFenchengRate = "90%";

  /**
   * 加勒比海俱乐部分成比例
   */
  private String jialebiClubFenchengRate = "10%";

  public LmbCache() {
  }

  public List<String> getGameTypes() {
    return gameTypes;
  }

  public void setGameTypes(List<String> gameTypes) {
    this.gameTypes = gameTypes;
  }

  public List<String> getDezhouZhuangweiIds() {
    return dezhouZhuangweiIds;
  }

  public void setDezhouZhuangweiIds(List<String> dezhouZhuangweiIds) {
    this.dezhouZhuangweiIds = dezhouZhuangweiIds;
  }

  public String getJialebiLMFenchengRate() {
    return jialebiLMFenchengRate;
  }

  public void setJialebiLMFenchengRate(String jialebiLMFenchengRate) {
    this.jialebiLMFenchengRate = jialebiLMFenchengRate;
  }

  public String getJialebiClubFenchengRate() {
    return jialebiClubFenchengRate;
  }

  public void setJialebiClubFenchengRate(String jialebiClubFenchengRate) {
    this.jialebiClubFenchengRate = jialebiClubFenchengRate;
  }
}
