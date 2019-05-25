package com.kendy.model.lmbcontribute.lmb;

/**
 * 俱乐部信息类
 */
public class ClubContributeInfomation {
  private String clubId;
  private String clubName;
  private String glbUserRate;
  private String gameUserRate;
  private String gudong;

  /**
   * 类型：1俱乐部 2游戏
   */
  private String type;

  public ClubContributeInfomation() {
    clubId="";
    clubName="";
    glbUserRate="0%";
    gameUserRate="0%";
    type = "1";
    gudong = "";
  }

  public String getClubId() {
    return clubId;
  }

  public ClubContributeInfomation setClubId(String clubId) {
    this.clubId = clubId;
    return this;
  }

  public String getClubName() {
    return clubName;
  }

  public ClubContributeInfomation setClubName(String clubName) {
    this.clubName = clubName;
    return this;
  }

  public String getGlbUserRate() {
    return glbUserRate;
  }

  public ClubContributeInfomation setGlbUserRate(String glbUserRate) {
    this.glbUserRate = glbUserRate;
    return this;
  }

  public String getGameUserRate() {
    return gameUserRate;
  }

  public ClubContributeInfomation setGameUserRate(String gameUserRate) {
    this.gameUserRate = gameUserRate;
    return this;
  }

  public String getGudong() {
    return gudong;
  }

  public ClubContributeInfomation setGudong(String gudong) {
    this.gudong = gudong;
    return this;
  }

  public String getType() {
    return type;
  }

  public ClubContributeInfomation setType(String type) {
    this.type = type;
    return this;
  }
}
