package com.kendy.db.service;

import com.kendy.db.entity.Player;
import java.util.List;

public interface PlayerService extends GenericService<Player, String> {


  public int insertMembers(List<Player> players);

  public int  deleteByTeamId(String teamId);
}
