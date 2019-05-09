package com.kendy.model.lmb;

import java.util.ArrayList;
import java.util.List;

/**
 * 联盟币缓存类
 * @author linzt
 * @date
 */
public class LmbCache {

  /**
   * 小游戏类型
   */
  private List<String> gameTypes = new ArrayList<>();
  /**
   * 托玩家ID
   */
  private List<String> tuoIds = new ArrayList<>();

  /**
   * 德州牛仔庄位玩家ID
   */
  private List<String> dezhouZhuangweiIds = new ArrayList<>();

  /**
   * 俱乐部信息类
   */
  private List<ClubInfomation> clubInfomations = new ArrayList<>();


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

  public List<ClubInfomation> getClubInfomations() {
    return clubInfomations;
  }

  public void setClubInfomations(List<ClubInfomation> clubInfomations) {
    this.clubInfomations = clubInfomations;
  }

  public List<String> getTuoIds() {
    return tuoIds;
  }

  public void setTuoIds(List<String> tuoIds) {
    this.tuoIds = tuoIds;
  }
}

