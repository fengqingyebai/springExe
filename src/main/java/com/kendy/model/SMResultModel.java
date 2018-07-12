package com.kendy.model;

import java.util.ArrayList;
import java.util.List;
import com.kendy.entity.ShangmaInfo;

/**
 * 根据玩家ID
 * 
 * @author 林泽涛
 * @time 2018年3月31日 下午11:58:50
 */
public class SMResultModel {

  /*
   * 团队可上码
   */
  private String teamTotalAvailabel = "0";
  /*
   * 主表上码列表
   */
  private List<ShangmaInfo> smList = new ArrayList<>();

  private String teamId = "";

  private String playerId = "";

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public ShangmaInfo getSelectedSMInfo() {
    return selectedSMInfo;
  }

  public void setSelectedSMInfo(ShangmaInfo selectedSMInfo) {
    this.selectedSMInfo = selectedSMInfo;
  }

  private ShangmaInfo selectedSMInfo;



  public String getTeamTotalAvailabel() {
    return teamTotalAvailabel;
  }

  public void setTeamTotalAvailabel(String teamTotalAvailabel) {
    this.teamTotalAvailabel = teamTotalAvailabel;
  }

  public List<ShangmaInfo> getSmList() {
    return smList;
  }

  public void setSmList(List<ShangmaInfo> smList) {
    this.smList = smList;
  }


}
