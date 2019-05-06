package com.kendy.db.service;

import com.kendy.db.entity.Player;
import java.util.List;

public interface PlayerService extends GenericService<Player, String> {


  public int insertMembers(List<Player> players);

  public int  deleteByTeamId(String teamId);

  /**
   * 自定义批量插入
   * 详见https://blog.csdn.net/m0_37981235/article/details/79131493
   * @param players
   */
  void insertBatch(List<Player> players);
}
