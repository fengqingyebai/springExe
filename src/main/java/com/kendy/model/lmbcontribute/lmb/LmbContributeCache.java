package com.kendy.model.lmbcontribute.lmb;

import java.util.ArrayList;
import java.util.List;

/**
 * 联盟币缓存类
 * @author linzt
 * @date
 */
public class LmbContributeCache {

  /**
   * 俱乐部信息类
   */
  private List<ClubContributeInfomation> clubContributeInfomations = new ArrayList<>();


  public LmbContributeCache() {
  }

  public List<ClubContributeInfomation> getClubContributeInfomations() {
    return clubContributeInfomations;
  }

  public void setClubContributeInfomations(
      List<ClubContributeInfomation> clubContributeInfomations) {
    this.clubContributeInfomations = clubContributeInfomations;
  }
}

