package com.kendy.db.entity.pk;

import java.io.Serializable;

/**
 * @author linzt
 * @date
 */
public class GameRecordPK implements Serializable {

  private static final long serialVersionUID = 1L;

  private String softTime;

  private String clubid;

  private String tableid;

  private String playerid;

  public GameRecordPK() {
  }

  public GameRecordPK(String softTime, String clubid, String tableid, String playerid) {
    this.softTime = softTime;
    this.clubid = clubid;
    this.tableid = tableid;
    this.playerid = playerid;
  }

  public String getSoftTime() {
    return softTime;
  }

  public void setSoftTime(String softTime) {
    this.softTime = softTime;
  }

  public String getClubid() {
    return clubid;
  }

  public void setClubid(String clubid) {
    this.clubid = clubid;
  }

  public String getTableid() {
    return tableid;
  }

  public void setTableid(String tableid) {
    this.tableid = tableid;
  }

  public String getPlayerid() {
    return playerid;
  }

  public void setPlayerid(String playerid) {
    this.playerid = playerid;
  }
}
