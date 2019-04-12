package com.kendy.db.service.Impl;

import com.kendy.db.dao.PlayerDao;
import com.kendy.db.entity.Player;
import com.kendy.db.service.PlayerService;
import com.kendy.util.ErrorUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author linzt
 * @date
 */
@Service("playerService")
public class PlayerServiceImpl extends GenericServiceImpl<PlayerDao, Player, String>
    implements PlayerService {

  /**
   * 根据玩家ID查询玩家信息
   *
   * @time 2017年10月26日
   */
//  public PlayerModel getMemberById(String playerId) {
//    PlayerModel playerModel = new PlayerModel();
//    if (StringUtil.isBlank(playerId)) {
//      return playerModel;
//    }
//    try {
//      con = DBConnection.getConnection();
//      String sql;
//      sql =
//          "select m.playerId,m.playerName,m.teamId,m.gudong,m.edu,m.isParent from members m where playerId = ? ";
//      ps = con.prepareStatement(sql);
//      ps.setString(1, playerId);
//      ResultSet rs = ps.executeQuery();
//      while (rs.next()) {
//        playerModel.setGameId(playerId);
//        playerModel.setPlayerName(rs.getString(2));
//        playerModel.setTeamName(rs.getString(3));
//        playerModel.setGudong(rs.getString(4));
//        playerModel.setEdu(rs.getString(5));
//        break;
//      }
//      return playerModel;
//    } catch (SQLException e) {
//      ErrorUtil.err("根据玩家ID查询玩家信息失败", e);
//      return playerModel;
//    } finally {
//      close(con, ps);
//    }
//  }


  // 插入一条人员名单---未测试
//  public void addMember(final PlayerModel playerModel) {
//    try {
//      con = DBConnection.getConnection();
//      String sql;
//      sql = "insert into members values(?,?,?,?,?,?,?,?)";
//      ps = con.prepareStatement(sql);
//      ps.setString(1, playerModel.getgameId());
//      ps.setString(2, playerModel.getPlayerName());
//      ps.setString(3, playerModel.getGudong());
//      ps.setString(4, playerModel.getTeamName());
//      ps.setString(5, playerModel.getEdu());
//      ps.setString(6, "0");
//      ps.setString(7, playerModel.getHuibao());
//      ps.setString(8, playerModel.getHuishui());
//      ps.execute();
//    } catch (SQLException e) {
//      ErrorUtil.err(playerModel.toString() + ",插入一条人员名单失败", e);
//    } finally {
//      close(con, ps);
//    }
//  }






  /**
   * 保存或者修改玩家信息
   *
   * @param playerModel 玩家信息
   * @time 2017年10月31日
   */
//  public void saveOrUpdate(final PlayerModel playerModel) {
//
//    if (isHasMember(playerModel.getgameId())) {
//      updateMember(playerModel);
//    } else {
//      addMember(playerModel);
//    }
//  }




  /**
   * 玩家是否存在
   *
   * @param playerId 玩家ID
   * @time 2017年10月31日
   */
//  public boolean isHasMember(String playerId) {
//    boolean hasMember = false;
//    try {
//      // 获取数据
//      con = DBConnection.getConnection();
//      String sql = "select count(*) from members  where playerName = ?";
//      ps = con.prepareStatement(sql);
//      ps.setString(1, playerId);
//      ResultSet rs = ps.executeQuery();
//      while (rs.next()) {
//        if (rs.getInt(1) == 1) {
//          hasMember = true;
//          break;
//        }
//      }
//    } catch (Exception e) {
//
//    } finally {
//      close(con, ps);
//    }
//    return hasMember;
//  }





  // 修改人员名单---未测试
//  public void updateMember(final PlayerModel playerModel) {
//    try {
//      con = DBConnection.getConnection();
//      String sql;
//      sql = "update members set playerName=?,gudong=?,teamId=?,edu=? where playerId =?";
//      ps = con.prepareStatement(sql);
//      ps.setString(1, playerModel.getPlayerName());
//      ps.setString(2, playerModel.getGudong());
//      ps.setString(3, playerModel.getTeamName());
//      ps.setString(4, playerModel.getEdu());
//      ps.setString(5, playerModel.getgameId());
//      // ps.setString(6, "0");//是否是父类
//      ps.executeUpdate();
//      loger.info("================修改人员...finishes");
//    } catch (SQLException e) {
//      ErrorUtil.err("修改人员名单失败", e);
//    } finally {
//      close(con, ps);
//    }
//  }


  /**
   * 导入人员名单
   *
   * @time 2017年11月19日
   */
  @Transactional
  @Override
  public int insertMembers(final List<Player> players) {
    if (CollectionUtils.isNotEmpty(players)) {
      String incorrectPlayerName = "";
      long start = System.currentTimeMillis();
      try {
        // TODO 先清空人员表
        List<Player> all = getAll();
        if (all != null) {
          try {
            remove(all.stream().map(e->e.getPlayerid()).collect(Collectors.toList()));
          } catch (Exception e) {
            logger.error("删除所有人员失败",e);
            throw e;
          }
        }

        int saveCount = save(players);
        long end = System.currentTimeMillis();
        logger.info("导入人员名单完成，耗时：" + (end - start) + "毫秒");
        return saveCount;
      } catch (Exception e) {
        ErrorUtil.err(incorrectPlayerName + "=导入人员名单进数据库失败", e);
      }
    }
    return 0;
  }



  // 删除一条人员名单---未测试
//  public void delMember(final String playerId) {
//    try {
//      con = DBConnection.getConnection();
//      String sql;
//      if (!StringUtil.isBlank(playerId)) {
//        sql = "delete from members where playerId = '" + playerId + "'";
//        ps = con.prepareStatement(sql);
//        ps.execute();
//      }
//    } catch (SQLException e) {
//      ErrorUtil.err("根据ID(" + playerId + ")删除人员失败", e);
//    } finally {
//      close(con, ps);
//    }
//  }

  /**
   * 删除团队时顺带删除所有该团队的人
   *
   * @time 2017年11月14日
   */
  @Override
  public int  deleteByTeamId(String teamId) {
    if (StringUtils.isBlank(teamId)) {
      return 0;
    }
    teamId = teamId.toUpperCase();
    return getDao().deleteByTeamId(teamId);
  }




  /**
   * 获取所有的人员名单
   */
//  public List<PlayerModel> getAllMembers() {
//    List<PlayerModel> result = new ArrayList<PlayerModel>();
//    try {
//      con = DBConnection.getConnection();
//      String sql = "select * from members";
//      ps = con.prepareStatement(sql);
//      ResultSet rs = ps.executeQuery();
//      PlayerModel p;
//      while (rs.next()) {
//        p = new PlayerModel();
//        p.setGameId(rs.getString(1));
//        p.setPlayerName(rs.getString(2));
//        p.setGudong(rs.getString(3));
//        p.setTeamName(rs.getString(4));
//        p.setEdu(rs.getString(5));
//        p.setIsParent(rs.getString(6));
//        p.setHuibao(rs.getString(7));
//        p.setHuishui(rs.getString(8));
//        result.add(p);
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } finally {
//      close(con, ps);
//    }
//    return result;
//  }


}
