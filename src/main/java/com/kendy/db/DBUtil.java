package com.kendy.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.DataConstans;
import com.kendy.entity.Club;
import com.kendy.entity.ClubBankModel;
import com.kendy.entity.ClubZhuofei;
import com.kendy.entity.Huishui;
import com.kendy.entity.JifenInfo;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.Player;
import com.kendy.entity.ShangmaNextday;
import com.kendy.entity.TGCommentInfo;
import com.kendy.entity.TGCompanyModel;
import com.kendy.entity.TGKaixiaoInfo;
import com.kendy.entity.TGLirunInfo;
import com.kendy.entity.TGTeamModel;
import com.kendy.model.BankFlowModel;
import com.kendy.model.GameRecord;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TimeUtil;



/**
 * 数据库操作类
 * 
 * @author 林泽涛
 * @time 2018年1月1日 下午10:54:17
 */
@Component
public class DBUtil {
  private Logger loger = LoggerFactory.getLogger(DBUtil.class);

  private Connection con = null;
  private PreparedStatement ps = null;
  private String sql;

  /**
   * 构造方法
   */
  public DBUtil() {
    super();
    loger.info("正在初始化DBUtil构造方法");
  }

  @PostConstruct
  public void inits() {
    loger.info("正在初始化DBUtil构造方法后的初始化");
  }

  /**
   * 积分查询
   */
  public List<JifenInfo> getJifenQuery(String clubId, String jifenValue, String teamId, String startTime,
      String endTime, String limit) {
    List<JifenInfo> list = new LinkedList<>();
    try {
      con = DBConnection.getConnection();
      String sql = new StringBuilder()
          .append("SELECT 	(@i :=@i + 1) AS jfRankNo, 	hh.* FROM ( SELECT DISTINCT playerName,floor((sum(shouHuishui) - sum(chuHuishui)) / ")
          .append(jifenValue).append(") AS jifenValue FROM 	( ")
          .append(GAME_RECORD_SQL).append("	WHERE 	m.teamId = '").append(teamId)
          .append("' AND finished_time >= '").append(startTime).append(" 00:00:00' ")
          .append(" AND finished_time <= '").append(endTime).append(" 23:59:59' AND r.clubId = '")
          .append(clubId).append("'	) h 	GROUP BY 	playerId 	ORDER BY jifenValue DESC ) hh, 	(SELECT @i := 0) b LIMIT ")
          .append(limit)
          .toString();
      loger.info("积分：" + sql);
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        list.add(new JifenInfo(rs.getString(1), rs.getString(2), rs.getString(3)));
      }
      return list;
    } catch (Exception e) {
      ErrorUtil.err("积分查询失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 会员历史战绩查询
   * 
   * @param playerId
   * @return
   */
  public String getTotalZJByPId(String playerId) {
    try {
      con = DBConnection.getConnection();
      String sql = "SELECT DISTINCT hr.playerName,sum(hr.shishou) as sum from (" + GAME_RECORD_SQL
          + ") hr where hr.playerId = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, playerId);
      ResultSet rs = ps.executeQuery();
      String res = "";
      while (rs.next()) {
        res = rs.getString(2);
        break;
      }
      return StringUtil.isBlank(res) ? "0.0" : NumUtil.digit0(res);
    } catch (Exception e) {
      ErrorUtil.err("会员历史战绩查询失败", e);
    } finally {
      close(con, ps);
    }
    return "0.0";
  }



  // 删除一条人员名单---未测试
  public void delMember(final String playerId) {
    try {
      con = DBConnection.getConnection();
      String sql;
      if (!StringUtil.isBlank(playerId)) {
        sql = "delete from members where playerId = '" + playerId + "'";
        ps = con.prepareStatement(sql);
        ps.execute();
      }
    } catch (SQLException e) {
      ErrorUtil.err("根据ID(" + playerId + ")删除人员失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 删除团队时顺带删除所有该团队的人
   * 
   * @time 2017年11月14日
   * @param teamId
   */
  public void delMembers_after_delTeam(final String teamId) {
    try {
      con = DBConnection.getConnection();
      String sql;
      if (!StringUtil.isBlank(teamId)) {
        sql = "delete from members where teamId = '" + teamId.toUpperCase() + "'";
        ps = con.prepareStatement(sql);
        ps.execute();
      }
    } catch (SQLException e) {
      ErrorUtil.err(teamId + ",删除此团队时顺带删除所有该团队的人失败", e);
    } finally {
      close(con, ps);
    }
  }

  // 删除一条团队
  public void delHuishui(final String teamId) {
    try {
      con = DBConnection.getConnection();
      String sql;
      if (!StringUtil.isBlank(teamId)) {
        sql = "delete from teamhs where teamId = '" + teamId.toUpperCase() + "'";
        ps = con.prepareStatement(sql);
        ps.execute();
      }
    } catch (SQLException e) {
      ErrorUtil.err("根据团队ID(" + teamId + ")删除回水失败", e);
    } finally {
      close(con, ps);
    }
  }


  // 插入一条人员名单---未测试
  public void addMember(final Player player) {
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "insert into members values(?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, player.getgameId());
      ps.setString(2, player.getPlayerName());
      ps.setString(3, player.getGudong());
      ps.setString(4, player.getTeamName());
      ps.setString(5, player.getEdu());
      ps.setString(6, "0");
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err(player.toString() + ",插入一条人员名单失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 根据玩家ID查询玩家信息
   * 
   * @time 2017年10月26日
   * @param playerId
   * @return
   */
  public Player getMemberById(String playerId) {
    Player player = new Player();
    if (StringUtil.isBlank(playerId))
      return player;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "select m.playerId,m.playerName,m.teamId,m.gudong,m.edu,m.isParent from members m where playerId = ? ";
      ps = con.prepareStatement(sql);
      ps.setString(1, playerId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        player.setGameId(playerId);
        player.setPlayerName(rs.getString(2));
        player.setTeamName(rs.getString(3));
        player.setGudong(rs.getString(4));
        player.setEdu(rs.getString(5));
        break;
      }
      loger.error("玩家ID(" + playerId + ")不存在！");
      return player;
    } catch (SQLException e) {
      ErrorUtil.err("根据玩家ID查询玩家信息失败", e);
      return player;
    } finally {
      close(con, ps);
    }
  }

  /**
   * 保存或者修改玩家信息
   * 
   * @time 2017年10月31日
   * @param player 玩家信息
   */
  public void saveOrUpdate(final Player player) {

    if (isHasMember(player.getgameId())) {
      updateMember(player);
    } else {
      addMember(player);
    }
  }


  /**
   * 玩家是否存在
   * 
   * @time 2017年10月31日
   * @param playerId 玩家ID
   * @return
   */
  public boolean isHasMember(String playerId) {
    boolean hasMember = false;
    try {
      // 获取数据
      con = DBConnection.getConnection();
      String sql = "select count(*) from members  where playerName = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, playerId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        if (rs.getInt(1) == 1) {
          hasMember = true;
          break;
        }
      }
    } catch (Exception e) {

    } finally {
      close(con, ps);
    }
    return hasMember;
  }

  // 修改人员名单---未测试
  public void updateMember(final Player player) {
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "update members set playerName=?,gudong=?,teamId=?,edu=? where playerId =?";
      ps = con.prepareStatement(sql);
      ps.setString(1, player.getPlayerName());
      ps.setString(2, player.getGudong());
      ps.setString(3, player.getTeamName());
      ps.setString(4, player.getEdu());
      ps.setString(5, player.getgameId());
      // ps.setString(6, "0");//是否是父类
      ps.executeUpdate();
      loger.info("================修改人员...finishes");
    } catch (SQLException e) {
      ErrorUtil.err("修改人员名单失败", e);
    } finally {
      close(con, ps);
    }
  }


  /**
   * 导入人员名单
   * 
   * @time 2017年11月19日
   * @param map
   */
  public void insertMembers(final Map<String, Player> map) {
    if (map != null && map.size() > 0) {
      String incorrectPlayerName = "";
      long start = System.currentTimeMillis();
      try {
        con = DBConnection.getConnection();
        String sql;
        if (map != null && map.size() > 0) {
          sql = "delete from members";
          ps = con.prepareStatement(sql);
          ps.execute();
        }

        Player player;
        sql = "insert into members values(?,?,?,?,?,?)";
        ps = con.prepareStatement(sql);
        int count = 0;
        con.setAutoCommit(false);
        for (Map.Entry<String, Player> entry : map.entrySet()) {
          player = entry.getValue();
          incorrectPlayerName = player.getPlayerName();
          ps.setString(1, player.getgameId());
          ps.setString(2, player.getPlayerName());
          ps.setString(3, player.getGudong());
          ps.setString(4, player.getTeamName());
          ps.setString(5, player.getEdu());
          ps.setString(6, "0");
          ps.addBatch();
          // ps.execute();//批量插入应该用ps.executeBatch()
          if ((++count) % map.size() == 0) { // 每500条刷新并写入数据库
            ps.executeBatch();
            con.commit();
            // 清空stmt中积攒的sql
            ps.clearBatch();
          }
        }
        ps.executeBatch();
        ps.clearBatch();
        long end = System.currentTimeMillis();
        loger.info("导入人员名单完成，耗时：" + (end - start) + "毫秒");
      } catch (SQLException e) {
        ErrorUtil.err(incorrectPlayerName + "=导入人员名单进数据库失败", e);
      } finally {
        close(con, ps);
      }
    }
  }

  // 导入昨日留底数据（仅在导入和锁定最后一场时用到）
  public void insertPreData(String dataTime, String preData) {
    try {
      con = DBConnection.getConnection();
      String sql;
      if ("2017-01-01".equals(dataTime)) {
        // 查看数据库是不是有2017-01-01的数据
        sql = "select * from yesterday_data where dateTime = '2017-01-01'";
        ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
          sql = "insert into yesterday_data values(?,?)";
          ps = con.prepareStatement(sql);
          ps.setString(1, dataTime);
          ps.setString(2, preData);
          ps.execute();
          return;
        }
        // 若无
        sql = "update yesterday_data set preData = ? where dateTime = '2017-01-01'";
        ps = con.prepareStatement(sql);
        ps.setString(1, preData);
        ps.execute();
        return;
      }


      sql = "insert into yesterday_data values(?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, dataTime);
      ps.setString(2, preData);
      ps.execute();
      loger.info("================昨日留底插入进数据库...finishes");
    } catch (SQLException e) {
      ErrorUtil.err("昨日留底插入失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 查找最新的昨日留底数据
   * 
   * @author 小林
   */
  public String Load_Date = "";

  public Map<String, String> getLastPreData() {
    Map<String, String> map = new HashMap<>();
    try {
      // 获取数据
      con = DBConnection.getConnection();
      String sql;
      sql =
          "select * from yesterday_data yd where yd.dateTime = (select MAX(dateTime) from yesterday_data )";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      String res = "";
      while (rs.next()) {
        loger.info("加载的昨日留底时间是：" + rs.getString(1));
        Load_Date = rs.getString(1);
        res = rs.getString(2);
        break;
      }
      // 封闭数据
      if (StringUtil.isBlank(res)) {
        ShowUtil.show("获取昨日留底数据失败！原因：数据库中没有昨日留底数据！");
        return map;
      }
      map = JSON.parseObject(res, new TypeReference<Map<String, String>>() {});
      // //资金
      // Map<String,String> zijinMap = _map.get("资金");
      // //实时开销(可以实时金额那里拿)
      // Map<String,String> presentPayoutMap = _map.get("实时开销");
      // //时实金额
      // Map<String,String> presentMoneyMap = _map.get("时实金额");
      // loger.info(JSON.toJSONString(presentMoneyMap));
      // //昨日利润
      // Map<String,String> yesterdayProfitMap = _map.get("昨日利润");
      // //联盟对帐
      // Map<String,String> LMMap = _map.get("联盟对帐");
    } catch (SQLException e) {
      ErrorUtil.err("查找最新的昨日留底数据失败", e);
    } finally {
      close(con, ps);
    }
    return map;
  }

  /**
   * 查找最新的锁定数据
   * 
   */
  public Map<String, String> getLastLockedData() {
    Map<String, String> map = new HashMap<>();
    try {
      // 获取数据
      con = DBConnection.getConnection();
      String sql;
      sql = "select * from last_locked_data l where l.ju = (select MAX(ju) from last_locked_data)";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      String res = "";
      while (rs.next()) {
        loger.info("中途加载的最大局是：" + rs.getString(1));
        res = rs.getString(2);
        break;
      }
      // 封闭数据
      if (StringUtil.isBlank(res)) {
        ShowUtil.show("没有可供中途需要加载的锁定数据！");
        return map;
      }
      map = JSON.parseObject(res, new TypeReference<Map<String, String>>() {});
      // ======================================================把锁定的详细数据单独加进来
      try {
        sql = "select * from last_locked_data_detail ";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        Map<String, Map<String, String>> locked_data_detail_map = new HashMap<>();
        while (rs.next()) {
          locked_data_detail_map.put(rs.getString(1),
              JSON.parseObject(rs.getString(2), new TypeReference<Map<String, String>>() {}));
        }
        map.put("All_Locked_Data_Map", JSON.toJSONString(locked_data_detail_map));
        loger.info("中途加载当天详细锁定数据,总局数：" + locked_data_detail_map.size());
      } catch (Exception e) {
        loger.error("中途加载当天详细锁定数据失败，原因" + e.getMessage());
      }
      // ======================================================
      // //资金
      // Map<String,String> zijinMap = _map.get("资金");
      // //实时开销(可以实时金额那里拿)
      // Map<String,String> presentPayoutMap = _map.get("实时开销");
      // //时实金额
      // Map<String,String> presentMoneyMap = _map.get("时实金额");
      // loger.info(JSON.toJSONString(presentMoneyMap));
      // //昨日利润
      // Map<String,String> yesterdayProfitMap = _map.get("昨日利润");
      // //联盟对帐
      // Map<String,String> LMMap = _map.get("联盟对帐");

    } catch (SQLException e) {
      ErrorUtil.err("查找最新的锁定数据失败", e);
      return map;
    } finally {
      close(con, ps);
    }
    return map;
  }

  /**
   * 添加新团队回水
   * 
   * @time 2017年11月12日
   * @param hs
   * @return
   */
  public boolean addTeamHS(final Huishui hs) {
    boolean isOK = true;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "insert into teamhs values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";// 13列
      ps = con.prepareStatement(sql);
      ps.setString(1, hs.getTeamId().toUpperCase());
      ps.setString(2, hs.getTeamName());
      ps.setString(3, hs.getHuishuiRate());
      ps.setString(4, hs.getInsuranceRate());
      ps.setString(5, hs.getGudong());
      ps.setString(6, hs.getZjManaged());
      ps.setString(7, hs.getBeizhu());
      ps.setString(8, hs.getProxyHSRate());
      ps.setString(9, hs.getProxyHBRate());
      ps.setString(10, hs.getProxyFWF());
      ps.setString(11, hs.getShowInsure());
      ps.setString(12, hs.getTeamYajin());
      ps.setString(13, hs.getTeamEdu());
      ps.setString(14, hs.getTeamAvailabel());
      ps.execute();
    } catch (SQLException e) {
      isOK = false;
      ErrorUtil.err("插入一条团队回水进数据库失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 修改团队回水比例
   * 
   * @time 2017年11月17日
   * @param teamId
   * @param teamHsRate
   * @return
   */
  public boolean updateTeamHsRate(String teamId, String teamHsRate) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "update teamhs set huishuiRate=? where teamId =?";
      ps = con.prepareStatement(sql);
      ps.setString(1, teamHsRate);
      ps.setString(2, teamId);
      ps.executeUpdate();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("修改团队回水比例失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }


  /**
   * 修改团队保险比例
   * 
   * @time 2018年2月8日
   * @param teamId
   * @param teamHsRate
   * @return
   */
  public boolean updateTeamHsBaoxianRate(String teamId, String teamHsRate) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "update teamhs set insuranceRate=? where teamId =?";
      ps = con.prepareStatement(sql);
      ps.setString(1, teamHsRate);
      ps.setString(2, teamId);
      ps.executeUpdate();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("修改团队保险比例失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 修改团队股东
   * 
   * @param teamId
   * @param teamGD
   * @return
   */
  public boolean updateTeamHsGudong(String teamId, String teamGD) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "update teamhs set gudong=? where teamId =?";
      ps = con.prepareStatement(sql);
      ps.setString(1, teamGD);
      ps.setString(2, teamId);
      ps.executeUpdate();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("修改团队股东失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 修改团队押金与额度
   * 
   * @param teamId
   * @param teamYajin
   * @param teamEdu
   * @return
   */
  public boolean updateTeamYajinAndEdu(String teamId, String teamYajin, String teamEdu,
      String teamAvailabel) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "update teamhs set teamYajin=?, teamEdu=?, teamAvailabel=?  where teamId =?";
      ps = con.prepareStatement(sql);
      ps.setString(1, teamYajin);
      ps.setString(2, teamEdu);
      ps.setString(3, teamAvailabel);
      ps.setString(4, teamId);
      ps.executeUpdate();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("修改团队押金与额度失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 修改代理查询中导出是否显示团队保险
   *
   * @param teamId
   * @param showInsure
   * @return
   */
  public boolean updateTeamHsShowInsure(String teamId, String showInsure) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "update teamhs set showInsure=? where teamId =?";
      ps = con.prepareStatement(sql);
      ps.setString(1, showInsure);
      ps.setString(2, teamId);
      ps.executeUpdate();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("修改代理查询中导出是否显示团队保险失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 团队回水修改
   * 
   * @time 2018年1月5日
   * @param hs
   */
  public void updateTeamHS(final Huishui hs) {
    try {
      con = DBConnection.getConnection();
      String sql = "update teamhs set teamName=?,huishuiRate=?,insuranceRate=?,"
          + "gudong=?,zjManaged=?,beizhu=?,proxyHSRate=?,proxyHBRate=?,proxyFWF=?,showInsure=?,teamYajin=?,teamEdu=?,teamAvailabel=?  where teamId = ?";// 11列
      ps = con.prepareStatement(sql);
      ps.setString(1, hs.getTeamName());
      ps.setString(2, hs.getHuishuiRate());
      ps.setString(3, hs.getInsuranceRate());
      ps.setString(4, hs.getGudong());
      ps.setString(5, hs.getZjManaged());
      ps.setString(6, hs.getBeizhu());
      ps.setString(7, hs.getProxyHSRate());
      ps.setString(8, hs.getProxyHBRate());
      ps.setString(9, hs.getProxyFWF());
      ps.setString(10, hs.getShowInsure());
      ps.setString(11, hs.getTeamYajin());
      ps.setString(12, hs.getTeamEdu());
      ps.setString(13, hs.getTeamAvailabel());
      ps.setString(14, hs.getTeamId().toUpperCase());
      ps.execute();
      loger.info("团队回水修改");
    } catch (SQLException e) {
      ErrorUtil.err("团队回水修改失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 团队回水入库
   * 
   * @time 2017年11月19日
   * @param map
   */
  public void insertTeamHS(final Map<String, Huishui> map) {
    if (map != null && map.size() > 0) {
      try {
        con = DBConnection.getConnection();
        if (map != null && map.size() > 0) {
          sql = "delete from teamhs";
          ps = con.prepareStatement(sql);
          ps.execute();
        }

        Huishui hs;
        String sql;
        loger.info("团队回水进数据库开始...");
        for (Map.Entry<String, Huishui> entry : map.entrySet()) {
          hs = entry.getValue();
          sql = "insert into teamhs values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";// 10列
          ps = con.prepareStatement(sql);
          ps.setString(1, hs.getTeamId().toUpperCase());
          ps.setString(2, hs.getTeamName());
          ps.setString(3, hs.getHuishuiRate());
          ps.setString(4, hs.getInsuranceRate());
          ps.setString(5, hs.getGudong());
          ps.setString(6, hs.getZjManaged());
          ps.setString(7, hs.getBeizhu());
          ps.setString(8, hs.getProxyHSRate());
          ps.setString(9, hs.getProxyHBRate());
          ps.setString(10, hs.getProxyFWF());
          ps.setString(11, hs.getShowInsure());
          ps.setString(12, hs.getTeamYajin());
          ps.setString(13, hs.getTeamEdu());
          ps.setString(14, hs.getTeamAvailabel());
          ps.execute();
        }
        loger.info("团队回水进数据库结束！size:" + map.size());
      } catch (SQLException e) {
        ErrorUtil.err("批量团队回水进数据库失败", e);
      } finally {
        close(con, ps);
      }
    }
  }


  /**
   * 锁定时保存所有缓存数据 备注：如果插入到一半失败了呢，后期考虑引入事务
   */
  public int saveLastLockedData(int ju_size, String json_all_locked_data, int ju_size2,
      String lastDataDetailJson) {
    int lockedIndex = 0;
    // Map<String, String> lastLockedDataMap = dataConstants.getLockedDataMap();// 这里没有场次信息的数据了
    // String json_all_locked_data = JSON.toJSONString(lastLockedDataMap);
    // int ju_size = dataConstants.Index_Table_Id_Map.size();
    try {
      loger.info("================插入锁定数据进数据库...开始");
      con = DBConnection.getConnection();

      // 插入最新锁定数据
      long start = System.currentTimeMillis();
      sql = "insert into last_locked_data values(?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, ju_size + "");
      ps.setString(2, json_all_locked_data);
      ps.execute();
      long end = System.currentTimeMillis();
      loger.info("================插入锁定数据进数据库...结束...局数：" + ju_size + "，耗时：" + (end - start) + "毫秒");

      // 删除原先数据
      String sql = "DELETE from last_locked_data where ju < " + (ju_size);// ju_size-2
      ps = con.prepareStatement(sql);
      ps.execute();

      lockedIndex = ju_size;

      // 单独保存最后一场的详细数据
      saveLastLockedDataDetail(ju_size2, lastDataDetailJson);
      return lockedIndex;
    } catch (SQLException e) {
      if (e.getMessage().contains("Incorrect")) {
        ErrorUtil.err("锁定时保存所有缓存数据失败,特殊字符串引起", e);
      } else {
        ErrorUtil.err("锁定时保存所有缓存数据失败", e);
      }
    } finally {
      close(con, ps);
    }
    return lockedIndex;
  }

  /**
   * 锁定时保存最后一场的详细数据
   * 
   * @time 2018年4月30日
   */
  private void saveLastLockedDataDetail(int ju_size, String lastDataDetailJson) {
    try {
      // int ju_size = dataConstants.Paiju_Index.get() - 1;
      // loger.info("----------------------执行锁定时保存最后一场的详细数据：" + ju_size);
      // Map<String, String> lastDataDetailMap = dataConstants.All_Locked_Data_Map.get(ju_size +
      // "");
      // String lastDataDetailJson = JSON.toJSONString(lastDataDetailMap);
      sql = "replace into last_locked_data_detail values(?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, ju_size + "");
      ps.setString(2, lastDataDetailJson);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("锁定时保存最后一场的详细数据失败", e);
    } finally {
      close(con, ps);
    }

  }

  public <K, V> Entry<K, V> getTail(LinkedHashMap<K, V> map) {
    Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
    Entry<K, V> tail = null;
    while (iterator.hasNext()) {
      tail = iterator.next();
    }
    return tail;
  }


  /**
   * 获取所有的人员名单
   * 
   * @return
   */
  public List<Player> getAllMembers() {
    List<Player> result = new ArrayList<Player>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from members";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      Player p;
      while (rs.next()) {
        p = new Player();
        p.setGameId(rs.getString(1));
        p.setPlayerName(rs.getString(2));
        p.setGudong(rs.getString(3));
        p.setTeamName(rs.getString(4));
        p.setEdu(rs.getString(5));
        result.add(p);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(con, ps);
    }
    return result;
  }



  /**
   * 获取所有的团队回水
   * 
   * @return
   */
  public List<Huishui> getAllTeamHS() {
    List<Huishui> result = new ArrayList<Huishui>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from teamhs";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      Huishui hs;
      while (rs.next()) {
        hs = new Huishui();
        hs.setTeamId(rs.getString(1));
        hs.setTeamName(rs.getString(2));
        hs.setHuishuiRate(rs.getString(3));
        hs.setInsuranceRate(rs.getString(4));
        hs.setGudong(rs.getString(5));
        hs.setZjManaged(rs.getString(6));
        hs.setBeizhu(rs.getString(7));
        hs.setProxyHSRate(rs.getString(8));
        hs.setProxyHBRate(rs.getString(9));
        hs.setProxyFWF(rs.getString(10));
        hs.setShowInsure(rs.getString(11));
        hs.setTeamYajin(rs.getString(12));
        hs.setTeamEdu(rs.getString(13));
        hs.setTeamAvailabel(rs.getString(14));
        result.add(hs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(con, ps);
    }
    return result;
  }



  /******************************************************** 关闭流 ******/
  public void close(Connection c, Statement s) {
    close(c);
    close(s);

  }

  public void close(Connection c) {
    if (c != null) {
      try {
        c.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public void close(Statement s) {
    if (s != null) {
      try {
        s.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }


  public Set<String> daySet = new LinkedHashSet<>();

  public boolean isPreData2017VeryFirst() {
    return "2017-01-01".equals(Load_Date) ? true : false;
  }

  /**
   * 清空并恢复到最开始的数据
   * 
   * @return
   */
  public boolean clearAllData() {
    boolean isOK = true;
    try {
      con = DBConnection.getConnection();
      String sql;


      List<String> delTables =
          Arrays.asList("club_zhuofei", "game_record", "gudong_kaixiao", "last_locked_data",
              "last_locked_data_detail", "tg_lirun", "history_bank_money", "shangma_nextday");

      for (String tableName : delTables) {
        sql = "DELETE from " + tableName;
        ps = con.prepareStatement(sql);
        ps.execute();
      }

      sql = "DELETE from yesterday_data where dateTime != '2017-01-01'";
      ps = con.prepareStatement(sql);
      ps.execute();

      // 俱乐部的桌费和已结处归0
      reset_clubZhuofei_to_0();

    } catch (SQLException e) {
      isOK = false;
      ErrorUtil.err("清空并恢复到最开始的数据", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 结束今天统计时把last_locked_data设置ju = 0
   * 
   * @return
   */
  public boolean handle_last_locked_data() {
    boolean isOK = true;
    try {
      con = DBConnection.getConnection();
      String sql = "select count(*) from last_locked_data";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      int count = 0;
      while (rs.next()) {
        count = Integer.valueOf(rs.getString(1));
      }
      if (count > 0) {
        // 将最大值设置为0(最大值就是
        int maxJu = getMaxJu();
        // 更新已锁定数据
        update_last_locked_data(maxJu);
      }
    } catch (SQLException e) {
      isOK = false;
      ErrorUtil.err("结束今天统计时把last_locked_data设置ju = 0失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  // 获取已锁定数据最大的牌局
  private int getMaxJu() {
    int maxJu = 0;
    try {
      con = DBConnection.getConnection();
      String sql = "select max(ju) from last_locked_data";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        maxJu = Integer.valueOf(rs.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(con, ps);
    }
    return maxJu;
  }

  // 结束今天统计时更新已锁定数据
  private void update_last_locked_data(int maxJu) {
    try {
      con = DBConnection.getConnection();
      String sql = "update last_locked_data set ju = 0 where ju = ?";
      ps = con.prepareStatement(sql);
      ps.setInt(1, maxJu);
      ps.executeUpdate();

      // 删除ju != 0的数据
      sql = "DELETE from last_locked_data where ju > 0";
      ps = con.prepareStatement(sql);
      ps.execute();

    } catch (SQLException e) {
      ErrorUtil.err("结束今天统计时更新已锁定数据", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 打开软件时获取合并ID的数据
   * 
   * @time 2017年11月4日
   * @return
   */
  public Map<String, Set<String>> getCombineData() {
    Map<String, Set<String>> combineMap = new HashMap<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select parentId,subIdJson,update_time from combine_ids";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      String parentId = "";
      String subIdJson = "";
      while (rs.next()) {
        parentId = rs.getString(1);
        subIdJson = rs.getString(2);
        Set<String> subIdSet = JSON.parseObject(subIdJson, new TypeReference<Set<String>>() {});
        combineMap.put(parentId, subIdSet);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(con, ps);
    }
    return combineMap;
  }

  /**
   * 保存或更新合并ID关系
   * <P>
   * 后面提供批量插入功能
   * 
   * @time 2017年11月4日
   * @param parentId 父ID
   * @return
   */
  public boolean saveOrUpdateCombineId(String parentId, Set<String> subIdSet) {
    boolean hasCombineRelation = isHasCombineId(parentId);
    if (subIdSet.size() == 0 && hasCombineRelation) {// 针对没有子ID集合的更新关系应该是删除此合并关系
      cancelCombineId(parentId);
      return true;
    }
    String subIdJson = JSON.toJSONString(subIdSet);
    String time = TimeUtil.getTime();
    if (StringUtil.isBlank(parentId) || StringUtil.isBlank(subIdJson))
      return false;

    if (hasCombineRelation) {
      return updateCombineId(parentId, subIdJson, time);
    } else {
      return addNewCombineId(parentId, subIdJson, time);
    }
  }

  /**
   * 更新合并ID关系
   * 
   * @time 2017年11月4日
   * @param superId 父ID
   * @param subIdJson 子IDJSON值
   * @param time 更新时间
   * @return
   */
  public boolean updateCombineId(String superId, String subIdJson, String time) {
    boolean isOK = true;
    try {
      con = DBConnection.getConnection();
      String sql = "update combine_ids set subIdJson=?,update_time=? where parentId = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, subIdJson);
      ps.setString(2, time);
      ps.setString(3, superId);

      ps.executeUpdate();

    } catch (SQLException e) {
      isOK = false;
      ErrorUtil.err("更新一条合并ID进数据库失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }


  /**
   * 添加新合并ID关系
   * 
   * @time 2017年11月4日
   * @param subIdJson 子IDJSON值
   * @param time 更新时间
   * @return
   */
  public boolean addNewCombineId(String parentId, String subIdJson, String time) {
    boolean isOK = true;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "insert into combine_ids values(?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, parentId);
      ps.setString(2, subIdJson);
      ps.setString(3, time);
      ps.execute();
    } catch (SQLException e) {
      isOK = false;
      ErrorUtil.err("插入一条合并ID进数据库失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }


  /**
   * 合并ID是否存在 根据父ID查询数据库中是否记录
   * 
   * @time 2017年10月31日
   * @param parentId 玩家ID
   * @return
   */
  public boolean isHasCombineId(String parentId) {
    boolean hasCombineId = false;
    try {
      // 获取数据
      con = DBConnection.getConnection();
      String sql = "select count(*) from combine_ids  where parentId = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, parentId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        if (rs.getInt(1) == 1) {
          hasCombineId = true;
          break;
        }
      }
    } catch (Exception e) {

    } finally {
      close(con, ps);
    }
    return hasCombineId;
  }

  /**
   * 解除合并ID关系
   * 
   * @time 2017年11月4日
   * @param parentId
   */
  public void cancelCombineId(String parentId) {
    if (isHasCombineId(parentId)) {
      try {
        con = DBConnection.getConnection();
        String sql = "delete from combine_ids where parentId = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, parentId);
        ps.execute();
      } catch (SQLException e) {
        ErrorUtil.err("数据库解除合关ID关系失败", e);
      } finally {
        close(con, ps);
      }
    }
  }

  /**
   * 是否应该点击开始新一天统计按钮 场景：上一场已经结束今日统计后，下一场应该只能开始新一天按钮，而不应该是中途继续
   * 
   * @time 2017年11月12日
   * @return 1:开始新一天按钮 2:中途继续按钮
   */
  public int newStaticOrContinue() {
    int buttonCode = 1;
    try {
      // 获取数据
      con = DBConnection.getConnection();
      String sql = "select count(*),min(ju) from last_locked_data";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int count = rs.getInt(1);
        String ju = rs.getString(2);
        if (count >= 1 && !"0".equals(ju)) {
          buttonCode = 2;
        }
      }
    } catch (Exception e) {
      buttonCode = 1;
    } finally {
      close(con, ps);
    }
    return buttonCode;
  }


  /***********************************************************************************
   * 
   * 小工具CRUD
   * 
   **********************************************************************************/
  /**
   * 从数据库获取所有俱乐部信息
   * 
   * @time 2017年11月24日
   * @return
   */
  public Map<String, Club> getAllClub() {
    Map<String, Club> map = new HashMap<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from club";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Club club = new Club();
        club.setClubId(rs.getString(1));
        club.setName(rs.getString(2));
        club.setEdu(rs.getString(3));
        club.setZhuoFei(rs.getString(4));
        club.setYiJieSuan(rs.getString(5));
        club.setZhuoFei2(rs.getString(6));
        club.setZhuoFei3(rs.getString(7));
        club.setYiJieSuan2(rs.getString(8));
        club.setYiJieSuan3(rs.getString(9));
        club.setEdu2(rs.getString(10));
        club.setEdu3(rs.getString(11));
        club.setGudong(rs.getString(12));
        map.put(club.getClubId(), club);
      }
    } catch (SQLException e) {
      ErrorUtil.err("从数据库获取所有俱乐部信息失败", e);
    } finally {
      close(con, ps);
    }
    return map;
  }

  /**
   * 删除俱乐部
   * 
   * @time 2017年11月22日
   * @param id 俱乐部ID
   */
  public void delClub(final String id) {
    try {
      con = DBConnection.getConnection();
      String sql;
      if (!StringUtil.isBlank(id)) {
        sql = "delete from club where clubId = '" + id + "'";
        ps = con.prepareStatement(sql);
        ps.execute();
        // 是否需要删除对应的人员？


      }
    } catch (SQLException e) {
      ErrorUtil.err("根据ID(" + id + ")删除俱乐部失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 到新的一天重置俱乐部桌费为0
   * 
   * @time 2018年2月21日
   */
  public void reset_clubZhuofei_to_0() {
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "update club c set c.zhuoFei='0',c.zhuoFei2='0',c.zhuoFei3='0',c.yiJieSuan='0',c.yiJieSuan2='0',c.yiJieSuan3='0'";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("到新的一天重置俱乐部桌费为0失败", e);
    } finally {
      close(con, ps);
    }
  }


  /**
   * 添加新俱乐部
   * 
   * @time 2017年11月22日
   * @param club
   */
  public void addClub(final Club club) {
    try {
      // 数据库中没有则添加
      con = DBConnection.getConnection();
      String sql;
      sql = "insert into club values(?,?,?,?,?,?,?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, club.getClubId());
      ps.setString(2, club.getName());
      ps.setString(3, club.getEdu());
      ps.setString(4, club.getZhuoFei());
      ps.setString(5, club.getYiJieSuan());
      ps.setString(6, club.getZhuoFei2());
      ps.setString(7, club.getZhuoFei3());
      ps.setString(8, club.getYiJieSuan2());
      ps.setString(9, club.getYiJieSuan3());
      ps.setString(10, club.getEdu2());
      ps.setString(11, club.getEdu3());
      ps.setString(12, club.getGudong());
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("添加新俱乐部失败", e);
    } finally {
      close(con, ps);
    }
  }



  /**
   * 俱乐部是否存在
   * 
   * @time 2017年10月31日
   * @param id 俱乐部ID
   * @return
   * @throws Exception
   */
  public boolean isHasClub(String id) throws Exception {
    if (StringUtil.isBlank(id))
      return false;

    boolean hsRecord = false;
    try {
      // 获取数据
      con = DBConnection.getConnection();
      String sql = "select count(*) from club  where clubId = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        if (rs.getInt(1) == 1) {
          hsRecord = true;
          break;
        }
      }
    } catch (Exception e) {
      loger.error(id + "查询俱乐部是否存在失败", e);
      throw e;
    } finally {
      close(con, ps);
    }
    return hsRecord;
  }

  /**
   * 更新俱乐部额度
   * 
   * @time 2017年11月22日
   * @param club
   * @return
   */
  public boolean updateClub(final Club club) {
    boolean isOK = true;
    try {
      con = DBConnection.getConnection();
      String sql =
          "update club set name=?,edu=?,zhuoFei=?,yiJieSuan=?,zhuoFei2=?,zhuoFei3=?,yiJieSuan2=?,yiJieSuan3=?,edu2=?,edu3=?,gudong=?  where clubId = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, club.getName());
      ps.setString(2, club.getEdu());
      ps.setString(3, club.getZhuoFei());
      ps.setString(4, club.getYiJieSuan());
      ps.setString(5, club.getZhuoFei2());
      ps.setString(6, club.getZhuoFei3());
      ps.setString(7, club.getYiJieSuan2());
      ps.setString(8, club.getYiJieSuan3());
      ps.setString(9, club.getEdu2());
      ps.setString(10, club.getEdu3());
      ps.setString(11, club.getGudong());
      ps.setString(12, club.getClubId());
      ps.executeUpdate();
    } catch (SQLException e) {
      isOK = false;
      ErrorUtil.err("更新俱乐部额度失败", e);
    } finally {
      close(con, ps);
    }
    return isOK;
  }


  /**
   * 新增或修改俱乐部信息
   * 
   * @time 2017年11月22日
   * @param club
   * @throws Exception
   */
  public void saveOrUpdateClub(final Club club) throws Exception {
    if (isHasClub(club.getClubId())) {
      updateClub(club);
    } else {
      addClub(club);
    }
  }

  /**
   * 清空所有俱乐部桌费和已结算
   * 
   * @time 2017年11月26日
   * @throws SQLException
   */
  public void clearAllClub_ZF_YiJiSuan() {
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "update club set zhuoFei='0',zhuoFei2='0',zhuoFei3='0',yiJieSuan='0',yiJieSuan2='0',yiJieSuan3='0'";
      ps = con.prepareStatement(sql);
      ps.executeUpdate();
      loger.info("数据库：清空所有俱乐部桌费和已结算OK！");
    } catch (SQLException e) {
      ErrorUtil.err("清空所有俱乐部桌费和已结算失败", e);
    } finally {
      close(con, ps);
    }
  }


  /**
   * 清空所有统计信息(record)
   * 
   * @time 2017年11月14日
   */
  public void del_all_record() {
    try {
      con = DBConnection.getConnection();
      String sql;

      sql = "delete from record ";
      ps = con.prepareStatement(sql);
      ps.execute();

    } catch (SQLException e) {
      ErrorUtil.err("清空所有统计信息失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 清空所有统计信息(record zhuofei kaixiao)
   * 
   * @time 2017年11月14日
   */
  public void del_all_record_and_zhuofei_and_kaixiao() {
    try {
      con = DBConnection.getConnection();
      String sql;

      sql = "delete from game_record ";
      ps = con.prepareStatement(sql);
      ps.execute();

      // add 2018 - 3 -17
      this.del_all_club_zhuofei();
      this.del_all_gudong_kaixiao();
    } catch (SQLException e) {
      ErrorUtil.err("清空所有统计信息失败", e);
    } finally {
      close(con, ps);
    }
  }

  /****************************************************************
   * 
   * 俱乐部银行卡信息表操作 数据表：clubBank 数据模型：ClubBankModel
   * 
   ****************************************************************/
  /**
   * 增加或修改俱乐部银行卡信息
   */
  public boolean addOrUpdateClubBank(final ClubBankModel bank) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "replace into clubBank values(?,?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, bank.getClubId());
      ps.setString(2, bank.getClubName());
      ps.setString(3, bank.getMobilePayType());
      ps.setString(4, bank.getPersonName());
      ps.setString(5, bank.getPhoneNumber());
      ps.setString(6, bank.getBankType());
      ps.setString(7, bank.getBankAccountInfo());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("增加俱乐部银行卡信息记录失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有俱乐部银行卡信息
   * 
   * @time 2017年12月18日
   * @return
   */
  public Map<String, ClubBankModel> getAllClubBanks() {
    Map<String, ClubBankModel> map = new HashMap<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from  clubBank";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ClubBankModel bank = new ClubBankModel();
        bank.setClubId(rs.getString(1));
        bank.setClubName(rs.getString(2));
        bank.setMobilePayType(rs.getString(3));
        bank.setPersonName(rs.getString(4));
        bank.setPhoneNumber(rs.getString(5));
        bank.setBankType(rs.getString(6));
        bank.setBankAccountInfo(rs.getString(7));
        map.put(rs.getString(1), bank);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有俱乐部银行卡信息失败", e);
    } finally {
      close(con, ps);
    }
    return map;
  }

  /***************************************************************************
   * 
   * others表
   * 
   **************************************************************************/

  /**
   * 获取others表记录，根据key
   * 
   * @time 2018年2月3日
   * @return
   */
  public String getValueByKey(final String key) {
    String value = "{}";
    try {
      con = DBConnection.getConnection();
      String sql = "select value from  others o where o.key = '" + key + "'";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        value = rs.getString(1);
      }
    } catch (SQLException e) {
      ErrorUtil.err("根据key(" + key + ")获取others表记录失败", e);
    } finally {
      close(con, ps);
    }
    return value;
  }


  public String getValueByKeyWithoutJson(final String key) {
    String value = "";
    try {
      con = DBConnection.getConnection();
      String sql = "select value from  others o where o.key = '" + key + "'";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        value = rs.getString(1);
      }
    } catch (SQLException e) {
      ErrorUtil.err("根据key(" + key + ")获取others表记录失败", e);
    } finally {
      close(con, ps);
    }
    return value;
  }

  /**
   * 根据key删除value
   * 
   * @time 2018年1月29日
   * @param key
   */
  public void delValueByKey(final String key) {
    try {
      con = DBConnection.getConnection();
      String sql;
      if (!StringUtil.isBlank(key)) {
        sql = "delete from others where key = '" + key + "'";
        ps = con.prepareStatement(sql);
        ps.execute();
      }
    } catch (SQLException e) {
      ErrorUtil.err("others表根据key(" + key + ")删除失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 保存others记录 如股东贡献值中的客服记录
   * 
   * @time 2018年2月3日
   * @param key
   * @param value
   * @return
   */
  public boolean saveOrUpdateOthers(final String key, final String value) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "replace into others values(?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, key);
      ps.setString(2, value);
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(key + ":" + value + ",保存others记录失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * others表添加记录
   * 
   * @time 2018年1月30日
   * @param key
   * @param value
   */
  public void addValue(final String key, final String value) {
    try {
      // 数据库中没有则添加
      con = DBConnection.getConnection();
      String sql;
      sql = "insert into others values(?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, key);
      ps.setString(2, value);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("others表添加记录（key:" + key + ",value:" + value + "）失败", e);
    } finally {
      close(con, ps);
    }
  }


  /***************************************************************************
   * 
   * 次日上码表
   * 
   **************************************************************************/
  /**
   * 保存次日上码
   * 
   * @time 2018年2月5日
   * @param nextday
   * @return
   */
  public boolean saveOrUpdate_SM_nextday(final ShangmaNextday nextday) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "replace into shangma_nextday(playerId,playerName,changci,shangma,time,type) values(?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, nextday.getPlayerId());
      ps.setString(2, nextday.getPlayerName());
      ps.setString(3, nextday.getChangci());
      ps.setString(4, nextday.getShangma());
      ps.setString(5, nextday.getTime());
      ps.setString(6, nextday.getType());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(nextday.getPlayerId() + ":" + nextday.getChangci() + ",保存次日上码记录失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取玩家的次日数据
   * 
   * @time 2018年2月5日
   * @return
   */
  public List<ShangmaNextday> getAllSM_nextday() {
    List<ShangmaNextday> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from  shangma_nextday n where n.type = '0' ";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ShangmaNextday nextday = new ShangmaNextday();
        nextday.setPlayerId(rs.getString(1));
        nextday.setPlayerName(rs.getString(2));
        nextday.setChangci(rs.getString(3));
        nextday.setShangma(rs.getString(4));
        nextday.setTime(rs.getString(5));
        nextday.setType(rs.getString(6));
        list.add(nextday);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取玩家的次日数据失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 用户自行加载完次日数据后，将数据表中的type设置为1，表示已经加载过
   * 
   * @time 2018年2月5日
   * @return
   */
  public boolean setNextDayLoaded() {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "update shangma_nextday n  set n.type = '1' where n.type = '0' ";
      ps = con.prepareStatement(sql);
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("修改次日数据记录（为加载）失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /***************************************************************************
   * 
   * 桌费表
   * 
   **************************************************************************/
  /**
   * 保存或修改历史桌费
   * 
   * @time 2018年2月11日
   * @param zhuofei
   * @return
   */
  public boolean saveOrUpdate_club_zhuofei(final ClubZhuofei zhuofei) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "replace into club_zhuofei(time,clubId,zhuofei,lmType) values(?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, zhuofei.getTime());
      ps.setString(2, zhuofei.getClubId());
      ps.setString(3, zhuofei.getZhuofei());
      ps.setString(4, zhuofei.getLmType());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(zhuofei.getTime() + ":" + zhuofei.getClubId() + ",保存或修改历史桌费失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取联盟1的所有历史桌费
   * 
   * @time 2018年2月11日
   * @return
   */
  public List<ClubZhuofei> get_LM1_all_club_zhuofei() {
    List<ClubZhuofei> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql =
          "select cz.time, cz.clubId, cz.zhuofei, cz.lmType, c.name, c.gudong from  club_zhuofei cz  "
              + "LEFT JOIN club c on cz.clubId = c.clubId where cz.lmType='联盟1'";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ClubZhuofei zhuofei = new ClubZhuofei(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getString(6));
        list.add(zhuofei);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有历史桌费失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 删除所有的历史联盟桌费
   * 
   * @time 2018年2月11日
   */
  public void del_all_club_zhuofei() {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from club_zhuofei ";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("删除所有的历史联盟桌费失败", e);
    } finally {
      close(con, ps);
    }
  }

  /***************************************************************************
   * 
   * 股东开销表
   * 
   **************************************************************************/
  /**
   * 保存或修改股东开销
   * 
   */
  public boolean saveOrUpdate_gudong_kaixiao(final KaixiaoInfo kaixiao) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "replace into gudong_kaixiao(kaixiaoID, kaixiaoType, kaixiaoMoney, kaixiaoGudong, kaixiaoTime) values(?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, kaixiao.getKaixiaoID());
      ps.setString(2, kaixiao.getKaixiaoType());
      ps.setString(3, kaixiao.getKaixiaoMoney());
      ps.setString(4, kaixiao.getKaixiaoGudong());
      ps.setString(5, kaixiao.getKaixiaoTime());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(kaixiao.toString() + ",保存或修改股东开销失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有股东开销
   * 
   * @time 2018年2月21日
   * @return
   */
  public List<KaixiaoInfo> get_all_gudong_kaixiao() {
    List<KaixiaoInfo> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from gudong_kaixiao";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        KaixiaoInfo kaixiao = new KaixiaoInfo(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5));
        list.add(kaixiao);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有股东开销失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 删除所有的股东开销
   * 
   * @time 2018年2月21日
   */
  public void del_all_gudong_kaixiao() {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from gudong_kaixiao ";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("删除所有的股东开销失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 根据ID删除股东开销
   * 
   * @time 2018年2月21日
   */
  public void del_gudong_kaixiao_by_id(String kaixiaoID) {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from gudong_kaixiao where kaixiaoID like '" + kaixiaoID + "%'";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("根据ID删除股东开销失败", e);
    } finally {
      close(con, ps);
    }
  }



  /***************************************************************************
   * 
   * 托管开销表
   * 
   **************************************************************************/
  /**
   * 保存或修改托管开销表
   * 
   */
  public boolean saveOrUpdate_tg_kaixiao(final TGKaixiaoInfo kaixiao) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "replace into tg_kaixiao(tg_id, tg_date, tg_player_name, tg_pay_items, tg_money, tg_company) values(?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, kaixiao.getTgKaixiaoEntityId());
      ps.setString(2, kaixiao.getTgKaixiaoDate());
      ps.setString(3, kaixiao.getTgKaixiaoPlayerName());
      ps.setString(4, kaixiao.getTgKaixiaoPayItem());
      ps.setString(5, kaixiao.getTgKaixiaoMoney());
      ps.setString(6, kaixiao.getTgKaixiaoCompany());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(kaixiao.toString() + ",保存或修改托管开销失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有托管开销
   * 
   * @time 2018年3月4日
   * @return
   */
  public List<TGKaixiaoInfo> get_all_tg_kaixiao() {
    List<TGKaixiaoInfo> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_kaixiao";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TGKaixiaoInfo kaixiao = new TGKaixiaoInfo(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getString(6));
        list.add(kaixiao);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有托管开销失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 删除所有的托管开销
   * 
   * @time 2018年3月4日
   */
  public void del_all_tg_kaixiao() {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_kaixiao ";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("删除所有的托管开销失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 根据ID删除托管开销
   * 
   * @time 2018年3月4日
   */
  public void del_tg_kaixiao_by_id(String kaixiaoID) {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_kaixiao where tg_id = '" + kaixiaoID + "'";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("根据ID删除托管开销失败", e);
    } finally {
      close(con, ps);
    }
  }

  /***************************************************************************
   * 
   * 托管玩家备注表
   * 
   **************************************************************************/
  /**
   * 保存或修改玩家备注表
   * 
   */
  public boolean saveOrUpdate_tg_comment(final TGCommentInfo comment) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "replace into tg_comment(id, tg_date, tg_player_id, tg_player_name, tg_type, tg_id, tg_name, tg_beizhu, tg_company) values(?,?,?,?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, comment.getTgCommentEntityId());
      ps.setString(2, comment.getTgCommentDate());
      ps.setString(3, comment.getTgCommentPlayerId());
      ps.setString(4, comment.getTgCommentPlayerName());
      ps.setString(5, comment.getTgCommentType());
      ps.setString(6, comment.getTgCommentId());
      ps.setString(7, comment.getTgCommentName());
      ps.setString(8, comment.getTgCommentBeizhu());
      ps.setString(9, comment.getTgCommentCompany());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(comment.toString() + ",保存或修改玩家备注失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有玩家备注
   * 
   * @time 2018年3月4日
   * @return
   */
  public List<TGCommentInfo> get_all_tg_comment() {
    List<TGCommentInfo> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_comment";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        // id, tg_date, tg_player_id, tg_player_name, tg_type, tg_id, tg_name, tg_beizhu
        TGCommentInfo comment = new TGCommentInfo(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
            rs.getString(9));
        list.add(comment);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有玩家备注失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 删除所有的玩家备注
   * 
   * @time 2018年3月4日
   */
  public void del_all_tg_comment() {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_comment ";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("删除所有的玩家备注失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 根据ID删除玩家备注
   * 
   * @time 2018年3月4日
   */
  public void del_tg_comment_by_id(String commentID) {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_comment where id = '" + commentID + "'";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("根据ID删除玩家备注失败", e);
    } finally {
      close(con, ps);
    }
  }

  /***************************************************************************
   * 
   * 托管公司表
   * 
   **************************************************************************/
  /**
   * 保存或修改托管公司表
   * 
   */
  public boolean saveOrUpdate_tg_company(final TGCompanyModel company) {
    boolean isOK = false;

    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "replace into tg_company(tg_company_name, company_rate, tg_company_rate"
          + ", yajin, edu, tg_teams_str, beizhu,club_id, yifenhong) values(?,?,?,?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, company.getTgCompanyName());
      ps.setString(2, company.getCompanyRate());
      ps.setString(3, company.getTgCompanyRate());
      ps.setString(4, company.getYajin());
      ps.setString(5, company.getEdu());
      ps.setString(6, company.getTgTeamsStr());
      ps.setString(7, company.getBeizhu());
      ps.setString(8, company.getClubId());
      ps.setString(9, company.getYifenhong());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(company.toString() + ",保存或修改托管公司失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有托管公司
   * 
   * @time 2018年3月4日
   * @return
   */
  public List<TGCompanyModel> get_all_tg_company() {
    List<TGCompanyModel> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_company";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        // tgCompanyName, companyRate, tgCompanyRate, yajin, edu, tgTeamsStr
        TGCompanyModel company =
            new TGCompanyModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                rs.getString(5), rs.getString(6), rs.getString(8), rs.getString(9));
        company.setBeizhu(rs.getString(7));
        if (StringUtil.isNotBlank(company.getTgTeamsStr())) {
          String[] teamArr = company.getTgTeamsStr().split("#");
          company.setTgTeamList(new ArrayList<String>(Arrays.asList(teamArr)));
        }
        list.add(company);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有托管公司失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 根据俱乐部ID获取托管公司
   * 
   * @time 2018年3月24日
   * @param clubId
   * @return
   */
  public List<TGCompanyModel> get_all_tg_company_By_clubId(String clubId) {
    List<TGCompanyModel> list = new ArrayList<>();
    if (StringUtil.isBlank(clubId)) {
      ShowUtil.show("当前俱乐部ID为空，请检查！", 2);
      return list;
    }
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_company where club_id = '" + clubId + "'";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        // tgCompanyName, companyRate, tgCompanyRate, yajin, edu, tgTeamsStr
        TGCompanyModel company =
            new TGCompanyModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                rs.getString(5), rs.getString(6), rs.getString(8), rs.getString(9));
        company.setBeizhu(rs.getString(7));
        if (StringUtil.isNotBlank(company.getTgTeamsStr())) {
          String[] teamArr = company.getTgTeamsStr().split("#");
          company.setTgTeamList(new ArrayList<String>(Arrays.asList(teamArr)));
        }
        list.add(company);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有托管公司失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 删除所有的托管公司
   * 
   * @time 2018年3月4日
   */
  public void del_all_tg_company() {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_company ";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("删除所有的托管公司失败", e);
    } finally {
      close(con, ps);
    }
  }


  public TGCompanyModel get_tg_company_by_id(String company) {
    TGCompanyModel model = null;
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_company where tg_company_name = '" + company + "'";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        model = new TGCompanyModel(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(8), rs.getString(9));
        model.setBeizhu(rs.getString(7));
      }
    } catch (SQLException e) {
      ErrorUtil.err(company + ", 获有托管公司（根据公司名）失败", e);
    } finally {
      close(con, ps);
    }
    return model;
  }

  /**
   * 根据ID删除托管公司
   * 
   * @time 2018年3月4日
   */
  public void del_tg_company_by_id(String companyName) {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_company where tg_company_name = '" + companyName + "'";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("根据托管公司名称删除托管公司失败", e);
    } finally {
      close(con, ps);
    }
  }



  /***************************************************************************
   * 
   * 托管团队比例表
   * 
   **************************************************************************/
  /**
   * 保存或修改团队比例表
   * 
   */
  public boolean saveOrUpdate_tg_team(final TGTeamModel team) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "replace into tg_team(tg_team_id, tg_hs_rate, tg_hb_rate, tg_fwf_rate,tg_team_proxy) values(?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, team.getTgTeamId());
      ps.setString(2, team.getTgHuishui());
      ps.setString(3, team.getTgHuiBao());
      ps.setString(4, team.getTgFwfRate());
      ps.setString(5, team.getTgTeamProxy());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(team.toString() + ",保存或修改团队比例失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有团队比例
   * 
   * @time 2018年3月4日
   * @return
   */
  public List<TGTeamModel> get_all_tg_team() {
    List<TGTeamModel> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_team";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        // id, tg_date, tg_player_id, tg_player_name, tg_type, tg_id, tg_name, tg_beizhu
        TGTeamModel team = new TGTeamModel(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5));
        list.add(team);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有团队比例失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  public TGTeamModel get_tg_team_by_id(String teamId) {
    TGTeamModel model = null;
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_team where tg_team_id = '" + teamId + "'";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        // id, tg_date, tg_player_id, tg_player_name, tg_type, tg_id, tg_name, tg_beizhu
        model = new TGTeamModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
            rs.getString(5));
      }
    } catch (SQLException e) {
      ErrorUtil.err(teamId + ", 获有团队比例失败", e);
    } finally {
      close(con, ps);
    }
    return model;
  }

  /**
   * 删除所有的团队比例
   * 
   * @time 2018年3月4日
   */
  public void del_all_tg_team() {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_team ";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("删除所有的团队比例失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 根据ID删除团队比例
   * 
   * @time 2018年3月4日
   */
  public void del_tg_team_by_id(String teamId) {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_comment where tg_team_id = '" + teamId + "'";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (SQLException e) {
      ErrorUtil.err("根据ID删除团队比例失败", e);
    } finally {
      close(con, ps);
    }
  }

  /***************************************************************************
   * 
   * 托管日利润表
   * 
   **************************************************************************/
  /**
   * 保存或修改托管日利润表
   * 
   */
  public boolean saveOrUpdate_tg_lirun(final TGLirunInfo lirun) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "replace into tg_lirun(tg_lirun_date, tg_lirun_total_profit, tg_lirun_total_kaixiao, tg_lirun_atm_company,"
              + "tg_lirun_tg_company,tg_lirun_team_profit,tg_lirun_tg_heji,tg_lirun_rest_heji,tg_lirun_company_name) "
              + "values(?,?,?,?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, lirun.getTgLirunDate());
      ps.setString(2, lirun.getTgLirunTotalProfit());
      ps.setString(3, lirun.getTgLirunTotalKaixiao());
      ps.setString(4, lirun.getTgLirunATMCompany());
      ps.setString(5, lirun.getTgLirunTGCompany());
      ps.setString(6, lirun.getTgLirunTeamProfit());
      ps.setString(7, lirun.getTgLirunHeji());
      ps.setString(8, lirun.getTgLirunRestHeji());
      ps.setString(9, lirun.getTgLirunCompanyName());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(lirun.toString() + ",保存或修改托管日利润失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有托管日利润
   * 
   * @time 2018年3月4日
   * @return
   */
  public List<TGLirunInfo> get_all_tg_lirun(String tgCompany) {
    List<TGLirunInfo> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from tg_lirun where tg_lirun_company_name='" + tgCompany + "'";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TGLirunInfo lirun = new TGLirunInfo(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
            rs.getString(9));
        list.add(lirun);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取所有托管日利润失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 删除所有的托管日利润
   * 
   * @time 2018年3月4日
   */
  public boolean del_all_tg_lirun() {
    boolean delOK = false;
    try {
      con = DBConnection.getConnection();
      String sql = "delete from tg_lirun ";
      ps = con.prepareStatement(sql);
      ps.execute();
      delOK = true;
    } catch (SQLException e) {
      ErrorUtil.err("删除所有的托管日利润失败", e);
      delOK = false;
    } finally {
      close(con, ps);
    }
    return delOK;
  }

  /**
   * 清空所有详细的锁定数据
   * 
   * @time 2018年4月30日
   */
  public void del_all_locked_data_details() {
    try {
      con = DBConnection.getConnection();
      String sql = "delete from last_locked_data_detail ";
      ps = con.prepareStatement(sql);
      ps.execute();

    } catch (SQLException e) {
      ErrorUtil.err("清空所有详细的锁定数据失败", e);
    } finally {
      close(con, ps);
    }
  }


  /***************************************************************************
   * 
   * 银行流水表
   * 
   **************************************************************************/
  /**
   * 保存银行流水表
   */
  public boolean saveHistoryBankMoney(final BankFlowModel moneyModel) {
    boolean isOK = false;
    try {
      con = DBConnection.getConnection();
      String sql;
      sql =
          "insert into history_bank_money(bank_name, money, update_time, soft_time) values(?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, moneyModel.getBankName());
      ps.setInt(2, moneyModel.getMoney());
      ps.setString(3, moneyModel.getUpdateTime());
      ps.setString(4, moneyModel.getSoftTime());
      ps.execute();
      isOK = true;
    } catch (SQLException e) {
      ErrorUtil.err(moneyModel.toString() + ",保存银行流水失败", e);
      isOK = false;
    } finally {
      close(con, ps);
    }
    return isOK;
  }

  /**
   * 获取所有银行流水
   * 
   */
  public List<BankFlowModel> getAllHistoryBankMoney() {
    List<BankFlowModel> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = "select * from history_bank_money order by soft_time, update_time";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        BankFlowModel money =
            new BankFlowModel(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4));
        list.add(money);
      }
    } catch (SQLException e) {
      ErrorUtil.err("获获取所有银行流水失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * 删除对应的银行流水
   * 
   * @param bankName
   */
  public void delBankFlowByType(final String bankName) {
    try {
      con = DBConnection.getConnection();
      String sql;
      if (StringUtil.isNotBlank(bankName)) {
        sql = "delete from history_bank_money where bank_name = '" + bankName + "'";
        ps = con.prepareStatement(sql);
        ps.execute();
      }
    } catch (SQLException e) {
      ErrorUtil.err("(" + bankName + ")删除对应银行流水失败", e);
    } finally {
      close(con, ps);
    }
  }


  /************************************************************************************************
   * 
   * GameRecord表 注意： 1、本表不保存teamId和clubName, 查询时自动去关联members表和club表，
   * 2、但是缓存中是有这两个数据的，更新玩家名称、俱乐部名称和团队时请更新相应数据表和缓存，不修改本表
   * 
   ***********************************************************************************************/

  private final String GAME_RECORD_SQL =
      "select r.*, m.playerName, m.teamId, c.name from  game_record r left join members m on r.playerId = m.playerId left join club c on r.clubId = c.clubId ";

  /**
   * 插入记录 注意：批量插入有性能问题，需要后期优化 不插入团队ID, 玩家名称， 和俱乐部名称，需要时自动去关联查询
   * 
   * @throws Exception
   */
  public void addGameRecord(final GameRecord record) throws Exception {
    try {
      con = DBConnection.getConnection();
      String sql;
      sql = "insert into game_record values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, record.getSoftDate());
      ps.setString(2, record.getClubId());
      ps.setString(3, record.getTableId());
      ps.setString(4, record.getPlayerId());
      ps.setString(5, record.getYszj());
      ps.setString(6, record.getSinegleInsurance());
      ps.setString(7, record.getClubInsurance());
      ps.setString(8, record.getCurrentTableInsurance());
      ps.setString(9, record.getShishou());
      ps.setString(10, record.getChuHuishui());
      ps.setString(11, record.getShouHuishui());
      ps.setString(12, record.getShuihouxian());
      ps.setString(13, record.getHuiBao());
      ps.setString(14, record.getHeLirun());
      ps.setString(15, record.getLmType());
      ps.setString(16, record.getFinisedTime());
      ps.setString(17, record.getIsJiesuaned());
      ps.setString(18, record.getLevel());
      ps.setString(19, record.getSumHandsCount());
      ps.execute();
    } catch (SQLException e) {
      throw new Exception("添加战绩记录失败", e);
    } finally {
      close(con, ps);
    }
  }
  
  /**
   * 更改记录的已结算字段
   * <P>
   * 用于场次信息中的团队累积
   * 
   * @param softTime
   * @param clubId
   * @param teamId
   */
  public void updateRecordJiesuan(String softTime, String clubId, String teamId) {
    try {
      con = DBConnection.getConnection();
      String sql = "update game_record r " 
      + "left join members m on r.playerId = m.playerId " 
      + "set isJiesuaned = '1' " 
      + "where soft_time = ? and clubId= ? and m.teamId = ? and isJiesuaned = '0'";
      ps = con.prepareStatement(sql);
      ps.setString(1, softTime);
      ps.setString(2, clubId);
      ps.setString(3, teamId);
      ps.execute();
      loger.info("更改记录的已结算字段");
    } catch (SQLException e) {
      ErrorUtil.err("更改记录的已结算字段失败", e);
    } finally {
      close(con, ps);
    }
  }
  

  /**
   * 联盟对帐批量插入战绩记录
   * 
   * @time 2017年11月19日
   * @param recordList
   * @throws Exception
   */
  public void addGameRecordList(final List<GameRecord> recordList) throws Exception {
    if (CollectUtil.isHaveValue(recordList)) {
      long start = System.currentTimeMillis();
      for (GameRecord record : recordList) {
        addGameRecord(record);
      }
      long end = System.currentTimeMillis();
      loger.info("导入{}条白名单记录进数据库，耗时：{}毫秒", recordList.size(), (end - start));
    }
  }

  /**
   * 获取已锁定的战绩记录中最大的时间
   * 
   * @time 2017年11月25日
   * @return
   */
  public String getMaxGameRecordTime() {
    String maxRecordTime = "";
    try {
      con = DBConnection.getConnection();
      String sql = "select max(soft_time) from game_record";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        maxRecordTime = rs.getString(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(con, ps);
    }
    return maxRecordTime;
  }

  /**
   * 获取最新的战绩记录列表（单位：天）
   * 
   * @time 2018年7月9日
   * @param maxRecordTime
   * @return
   */
  public List<GameRecord> getGameRecordsByMaxTime(String maxRecordTime) {
    List<GameRecord> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = GAME_RECORD_SQL + " where soft_time =  ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, maxRecordTime);
      ResultSet rs = ps.executeQuery();
      list = getGameRecordResult(rs);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录（单位：天）失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  private List<GameRecord> getGameRecordResult(ResultSet rs) throws Exception {
    List<GameRecord> list = new ArrayList<>();
    while (rs.next()) {
      GameRecord record = new GameRecord();
      record.setSoftDate(rs.getString(1));
      record.setClubId(rs.getString(2));
      record.setTableId(rs.getString(3));
      record.setPlayerId(rs.getString(4));
      record.setYszj(rs.getString(5));
      record.setSinegleInsurance(rs.getString(6));
      record.setClubInsurance(rs.getString(7));
      record.setCurrentTableInsurance(rs.getString(8));
      record.setShishou(rs.getString(9));
      record.setChuHuishui(rs.getString(10));
      record.setShouHuishui(rs.getString(11));
      record.setShuihouxian(rs.getString(12));
      record.setHuiBao(rs.getString(13));
      record.setHeLirun(rs.getString(14));
      record.setLmType(rs.getString(15));
      record.setFinisedTime(rs.getString(16));
      record.setIsJiesuaned(rs.getString(17));
      record.setLevel(rs.getString(18));
      record.setSumHandsCount(rs.getString(19));
      // 单独设置团队ID和俱乐部名称
      record.setPlayerName(rs.getString(20));
      record.setTeamId(StringUtil.nvl(rs.getString(21), "")); // 可能关联不到该人员
      record.setClubName(rs.getString(22));
      list.add(record);
    }
    return list;
  }

  /**
   * 获取有效桌数统计
   */
  public Map<String, String> getValidLevelAndCount(String currentLMType) {
    Map<String, String> map = new HashMap<>();
    try {
      con = DBConnection.getConnection();
      String sql =
          "SELECT max(LEVEL), count(DISTINCT tableId) FROM ( SELECT tableId,LEVEL, sumHandsCount, soft_time FROM game_record WHERE sumHandsCount > '0' and lmType = '"+currentLMType+"' and soft_time = ( SELECT max(soft_time) AS softTime FROM game_record )) a GROUP BY LEVEL";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        map.put(rs.getString(1), rs.getString(2));
      }
    } catch (SQLException e) {
      ErrorUtil.err("获取有效桌数统计失败", e);
    } finally {
      close(con, ps);
    }
    return map;
  }

  /**
   * 获取最新的战绩记录列表（单位：当天某俱乐部）
   * 
   * @time 2018年7月9日
   * @param maxRecordTime
   * @param clubId
   * @return
   */
  public List<GameRecord> getGameRecordsByMaxTimeAndClub(String maxRecordTime, String clubId) {
    List<GameRecord> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = GAME_RECORD_SQL + " where r.soft_time =  ? and r.clubId = ? ";
      ps = con.prepareStatement(sql);
      ps.setString(1, maxRecordTime);
      ps.setString(2, clubId);
      ResultSet rs = ps.executeQuery();
      list = getGameRecordResult(rs);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录（单位：当天俱乐部）失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }

  /**
   * TODO 关联部分未设值
   * 
   * @time 2018年7月9日
   * @param clubId
   * @return
   */
  public List<GameRecord> getGameRecordsByClubId(String clubId) {
    List<GameRecord> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = GAME_RECORD_SQL + " where  r.clubId = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, clubId);
      ResultSet rs = ps.executeQuery();
      list = getGameRecordResult(rs);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录（单位：当天俱乐部）失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }


  /**
   * TODO 是否只获取当天的 获取最新的所有战绩记录列表（单位：天） 由于前面的会被删掉，帮只取最最后一天的数据
   * 
   * @time 2017年11月25日
   */
  public List<GameRecord> getAllGameRecords() {
    List<GameRecord> list = new ArrayList<>();
    try {
      con = DBConnection.getConnection();
      String sql = GAME_RECORD_SQL + "where 1=1";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      list = getGameRecordResult(rs);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录（单位：天）失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }



  /**
   * 获取已锁定的战绩记录中最大的时间
   * 
   * @time 2017年11月25日
   * @return
   */
  public String getMaxBankFlowTime() {
    String maxRecordTime = "";
    try {
      con = DBConnection.getConnection();
      String sql = "select max(soft_time) from history_bank_money";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        maxRecordTime = rs.getString(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(con, ps);
    }
    return maxRecordTime;
  }


}
