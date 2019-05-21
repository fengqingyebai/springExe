package com.kendy.model.lmbcontribute.lmb;

/**
 * 俱乐部信息类
 */
public class ClubContributeInfomation {
  private String clubId;
  private String clubName;
  private String glbUserRate;
  private String gameUserRate;

  public ClubContributeInfomation() {
    clubId="";
    clubName="";
    glbUserRate="0%";
    gameUserRate="0%";
  }

  public String getClubId() {
    return clubId;
  }

  public void setClubId(String clubId) {
    this.clubId = clubId;
  }

  public String getClubName() {
    return clubName;
  }

  public void setClubName(String clubName) {
    this.clubName = clubName;
  }

  public String getGlbUserRate() {
    return glbUserRate;
  }

  public void setGlbUserRate(String glbUserRate) {
    this.glbUserRate = glbUserRate;
  }

  public String getGameUserRate() {
    return gameUserRate;
  }

  public void setGameUserRate(String gameUserRate) {
    this.gameUserRate = gameUserRate;
  }
}
