package com.kendy.db.dao;

import com.kendy.db.entity.Player;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface PlayerDao extends GenericDao<Player>, Mapper<Player> {

  @Delete("delete from members where teamId = #{teamId}")
  int deleteByTeamId(@Param("teamId") String teamId);


  void insertBatch(List<Player> players);
}