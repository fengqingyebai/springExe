package com.kendy.db;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.Constants;
import com.kendy.db.entity.GameRecord;
import com.kendy.db.service.GameRecordService;
import com.kendy.db.service.PlayerService;
import com.kendy.entity.Club;
import com.kendy.entity.ClubBankModel;
import com.kendy.entity.ClubStaticInfo;
import com.kendy.entity.ClubZhuofei;
import com.kendy.entity.Huishui;
import com.kendy.entity.JifenInfo;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.ShangmaNextday;
import com.kendy.entity.TGCommentInfo;
import com.kendy.entity.TGCompanyModel;
import com.kendy.entity.TGKaixiaoInfo;
import com.kendy.entity.TGLirunInfo;
import com.kendy.entity.TGTeamModel;
import com.kendy.entity.TeamStaticInfo;
import com.kendy.entity.TotalInfo2;
import com.kendy.entity.ZjClubStaticDetailInfo;
import com.kendy.entity.ZjClubStaticInfo;
import com.kendy.entity.ZjTeamStaticDetailInfo;
import com.kendy.entity.ZjTeamStaticInfo;
import com.kendy.model.BankFlowModel;
import com.kendy.model.GameRecordModel;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TimeUtil;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 数据库操作类
 *
 * @author 林泽涛
 * @time 2018年1月1日 下午10:54:17
 */
@Component
public class DBService {

  private Logger loger = LoggerFactory.getLogger(DBService.class);

  @Resource
  private GameRecordService gameRecordService;

  @Resource
  private PlayerService playerService;

  @Autowired
  private DataSource dataSource;


  private Connection con = null;
  private PreparedStatement ps = null;
  private String sql;

  /**
   * 构造方法
   */
  public DBService() {
    super();
    loger.info("正在初始化DBUtil构造方法");
  }

  @PostConstruct
  public void inits() {
    loger.info("正在初始化DBUtil构造方法后的初始化");
  }

  public Connection getConnection(){
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      loger.error("数据库连接失败", e);
    }
    return null;
  }

  /**
   * 积分查询
   */
  public List<JifenInfo> getJifenQuery(String clubId, String jifenValue, String teamId,
      String startTime,
      String endTime, boolean isCheckTeamProfitBox, String limit) {
    List<JifenInfo> list = new LinkedList<>();
    try {
      con = dataSource.getConnection();
      String subSql =
          isCheckTeamProfitBox ? "sum(shouHuishui) + sum(chuHuishui)" : "sum(shouHuishui)";
      String sql = new StringBuilder()
          .append(
              "SELECT 	(@i :=@i + 1) AS jfRankNo, 	hh.* FROM ( SELECT DISTINCT playerName,floor(("
                  + subSql + ") / ")
          .append(jifenValue).append(") AS jifenValue FROM 	( ")
          .append(GAME_RECORD_SQL).append("	WHERE 	m.teamId = '").append(teamId)
          .append("' AND soft_time >= '").append(startTime)
          .append("' AND soft_time <= '").append(endTime)
          .append("' AND r.clubId = '").append(clubId)
          .append(
              "' )h 	GROUP BY 	playerId 	ORDER BY jifenValue DESC ) hh, 	(SELECT @i := 0) b LIMIT ")
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


  // 删除一条团队
  public void delHuishui(final String teamId) {
    try {
      con = dataSource.getConnection();
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


  // 导入昨日留底数据（仅在导入和锁定最后一场时用到）
  public void insertPreData(String dataTime, String preData) {
    try {
      con = dataSource.getConnection();
      // 先删除所有昨日留底数据
      String sql = "delete from yesterday_data";
      ps = con.prepareStatement(sql);
      ps.execute();
      // 执行插入操作
      sql = "replace into yesterday_data values(?,?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, dataTime);
      ps.setString(2, preData);
      ps.execute();
      loger.info("================昨日留底插入进数据库...finishes");
    } catch (Exception e) {
      ErrorUtil.err("昨日留底插入失败:" + e.getMessage());
      e.printStackTrace();
    } finally {
      close(con, ps);
    }
  }

  public void _insertPreData(String dataTime, String preData) {
    try {
      con = dataSource.getConnection();
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
      e.printStackTrace();
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
      con = dataSource.getConnection();
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
      map = JSON.parseObject(res, new TypeReference<Map<String, String>>() {
      });
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
   */
  public Map<String, String> getLastLockedData() {
    Map<String, String> map = new HashMap<>();
    try {
      // 获取数据
      con = dataSource.getConnection();
      String sql;
      sql = "select * from last_locked_data l where l.ju = (select MAX(ju) from last_locked_data)";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      String res = "";
      int recordCount = 0; // 当前最大记录数
      while (rs.next()) {
        recordCount = rs.getInt(1);
        loger.info("中途加载的最大局是：" + recordCount);
        res = rs.getString(2);
        break;
      }
      // 封闭数据
      if (StringUtil.isBlank(res)) {
        ShowUtil.show("没有可供中途需要加载的锁定数据！");
        return map;
      }
      map = JSON.parseObject(res, new TypeReference<Map<String, String>>() {
      });

    } catch (SQLException e) {
      ErrorUtil.err("查找最新的锁定数据失败", e);
      return map;
    } finally {
      close(con, ps);
    }
    return map;
  }

  /**
   * 锁定的详细数据单独加进来<br/> 避免内存溢出<br> 只在中途继续时调用 ， 其他情况不调用
   */
  public Map<String, Map<String, String>> getAllLockedRecords() {
    // ======================================================把锁定的详细数据单独加进来
    Map<String, Map<String, String>> locked_data_detail_map = new HashMap<>();
    try {
      con = dataSource.getConnection();
      String sql = "select * from last_locked_data_detail ";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      rs = ps.executeQuery();
      while (rs.next()) {
        locked_data_detail_map.put(rs.getString(1),
            JSON.parseObject(rs.getString(2), new TypeReference<Map<String, String>>() {
            }));
      }
      loger.info("中途加载当天详细锁定数据,总局数：" + locked_data_detail_map.size());
    } catch (Exception e) {
      loger.error("中途加载当天详细锁定数据失败，原因" + e.getMessage());
    } finally {
      close(con, ps);
    }
    return locked_data_detail_map;
    // ======================================================
  }

  /**
   * 添加新团队回水
   *
   * @time 2017年11月12日
   */
  public boolean addTeamHS(final Huishui hs) {
    boolean isOK = true;
    try {
      con = dataSource.getConnection();
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
   */
  public boolean updateTeamHsRate(String teamId, String teamHsRate) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public boolean updateTeamHsBaoxianRate(String teamId, String teamHsRate) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public boolean updateTeamHsGudong(String teamId, String teamGD) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public boolean updateTeamYajinAndEdu(String teamId, String teamYajin, String teamEdu,
      String teamAvailabel) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public boolean updateTeamHsShowInsure(String teamId, String showInsure) {
    teamId = teamId.toUpperCase();
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public void updateTeamHS(final Huishui hs) {
    try {
      con = dataSource.getConnection();
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
   */
  public void insertTeamHS(final Map<String, Huishui> map) {
    if (map != null && map.size() > 0) {
      try {
        con = dataSource.getConnection();
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
      con = dataSource.getConnection();

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
   * 获取所有的团队回水
   */
  public List<Huishui> getAllTeamHS() {
    List<Huishui> result = new ArrayList<Huishui>();
    try {
      con = dataSource.getConnection();
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
    return "2017-01-01".equals(Load_Date);
  }

  /**
   * 清空并恢复到最开始的数据
   */
  public boolean clearAllData() {
    boolean isOK = true;
    try {
      con = dataSource.getConnection();
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
   */
  public boolean handle_last_locked_data() {
    boolean isOK = true;
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public Map<String, Set<String>> getCombineData() {
    Map<String, Set<String>> combineMap = new HashMap<>();
    try {
      con = dataSource.getConnection();
      String sql = "select parentId,subIdJson,update_time from combine_ids";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      String parentId = "";
      String subIdJson = "";
      while (rs.next()) {
        parentId = rs.getString(1);
        subIdJson = rs.getString(2);
        Set<String> subIdSet = JSON.parseObject(subIdJson, new TypeReference<Set<String>>() {
        });
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
   * @param parentId 父ID
   * @time 2017年11月4日
   */
  public boolean saveOrUpdateCombineId(String parentId, Set<String> subIdSet) {
    boolean hasCombineRelation = isHasCombineId(parentId);
    if (subIdSet.size() == 0 && hasCombineRelation) {// 针对没有子ID集合的更新关系应该是删除此合并关系
      cancelCombineId(parentId);
      return true;
    }
    String subIdJson = JSON.toJSONString(subIdSet);
    String time = TimeUtil.getTime();
    if (StringUtil.isBlank(parentId) || StringUtil.isBlank(subIdJson)) {
      return false;
    }

    if (hasCombineRelation) {
      return updateCombineId(parentId, subIdJson, time);
    } else {
      return addNewCombineId(parentId, subIdJson, time);
    }
  }

  /**
   * 更新合并ID关系
   *
   * @param superId 父ID
   * @param subIdJson 子IDJSON值
   * @param time 更新时间
   * @time 2017年11月4日
   */
  public boolean updateCombineId(String superId, String subIdJson, String time) {
    boolean isOK = true;
    try {
      con = dataSource.getConnection();
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
   * @param subIdJson 子IDJSON值
   * @param time 更新时间
   * @time 2017年11月4日
   */
  public boolean addNewCombineId(String parentId, String subIdJson, String time) {
    boolean isOK = true;
    try {
      con = dataSource.getConnection();
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
   * @param parentId 玩家ID
   * @time 2017年10月31日
   */
  public boolean isHasCombineId(String parentId) {
    boolean hasCombineId = false;
    try {
      // 获取数据
      con = dataSource.getConnection();
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
   */
  public void cancelCombineId(String parentId) {
    if (isHasCombineId(parentId)) {
      try {
        con = dataSource.getConnection();
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
   * @return 1:开始新一天按钮 2:中途继续按钮
   * @time 2017年11月12日
   */
  public int newStaticOrContinue() {
    int buttonCode = 1;
    try {
      // 获取数据
      con = dataSource.getConnection();
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
   */
  public Map<String, Club> getAllClub() {
    Map<String, Club> map = new HashMap<>();
    try {
      con = dataSource.getConnection();
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
   * @param id 俱乐部ID
   * @time 2017年11月22日
   */
  public void delClub(final String id) {
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public void addClub(final Club club) {
    try {
      // 数据库中没有则添加
      con = dataSource.getConnection();
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
   * @param id 俱乐部ID
   * @time 2017年10月31日
   */
  public boolean isHasClub(String id) throws Exception {
    if (StringUtil.isBlank(id)) {
      return false;
    }

    boolean hsRecord = false;
    try {
      // 获取数据
      con = dataSource.getConnection();
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
   */
  public boolean updateClub(final Club club) {
    boolean isOK = true;
    try {
      con = dataSource.getConnection();
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
   */
  public void clearAllClub_ZF_YiJiSuan() {
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public Map<String, ClubBankModel> getAllClubBanks() {
    Map<String, ClubBankModel> map = new HashMap<>();
    try {
      con = dataSource.getConnection();
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
   */
  public String getValueByKey(final String key) {
    String value = "{}";
    value = getValueString(key, value);
    return value;
  }

  private String getValueString(String key, String value) {
    try {
      con = dataSource.getConnection();
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
    value = getValueString(key, value);
    return value;
  }

  /**
   * 根据key删除value
   *
   * @time 2018年1月29日
   */
  public void delValueByKey(final String key) {
    try {
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdateOthers(final String key, final String value) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public void addValue(final String key, final String value) {
    try {
      // 数据库中没有则添加
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_SM_nextday(final ShangmaNextday nextday) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public List<ShangmaNextday> getAllSM_nextday() {
    List<ShangmaNextday> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
   */
  public boolean setNextDayLoaded() {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_club_zhuofei(final ClubZhuofei zhuofei) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public List<ClubZhuofei> get_LM1_all_club_zhuofei() {
    List<ClubZhuofei> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_gudong_kaixiao(final KaixiaoInfo kaixiao) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public List<KaixiaoInfo> get_all_gudong_kaixiao() {
    List<KaixiaoInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_tg_kaixiao(final TGKaixiaoInfo kaixiao) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public List<TGKaixiaoInfo> get_all_tg_kaixiao() {
    List<TGKaixiaoInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
      String sql = "delete from tg_kaixiao ";
      ps = con.prepareStatement(sql);
      ps.execute();
    } catch (Exception e) {
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
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_tg_comment(final TGCommentInfo comment) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public List<TGCommentInfo> get_all_tg_comment() {
    List<TGCommentInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_tg_company(final TGCompanyModel company) {
    boolean isOK = false;

    try {
      con = dataSource.getConnection();
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
   */
  public List<TGCompanyModel> get_all_tg_company() {
    List<TGCompanyModel> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
   */
  public List<TGCompanyModel> get_all_tg_company_By_clubId(String clubId) {
    List<TGCompanyModel> list = new ArrayList<>();
    if (StringUtil.isBlank(clubId)) {
      ShowUtil.show("当前俱乐部ID为空，请检查！", 2);
      return list;
    }
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_tg_team(final TGTeamModel team) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public List<TGTeamModel> get_all_tg_team() {
    List<TGTeamModel> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public boolean saveOrUpdate_tg_lirun(final TGLirunInfo lirun) {
    boolean isOK = false;
    try {
      con = dataSource.getConnection();
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
   */
  public List<TGLirunInfo> get_all_tg_lirun(String tgCompany) {
    List<TGLirunInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
      con = dataSource.getConnection();
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
   */
  public List<BankFlowModel> getAllHistoryBankMoney() {
    List<BankFlowModel> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
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
   */
  public void delBankFlowByType(final String bankName) {
    try {
      con = dataSource.getConnection();
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
   * GameRecord表 注意：<p>
   * 1、本表不保存teamId和clubName, 查询时自动去关联members表和club表，
   * 2、但是缓存中是有这两个数据的，更新玩家名称、俱乐部名称和团队时请更新相应数据表和缓存，不修改本表
   *
   ***********************************************************************************************/

  private final String GAME_RECORD_SQL =
      "select r.*, m.playerName, m.teamId, c.name from  game_record r left join members m on r.playerId = m.playerId left join club c on r.clubId = c.clubId ";


  /**
   * 更改记录的已结算字段
   * <P>
   * 用于场次信息中的团队累积
   */
  public void updateRecordJiesuan(String softTime, String clubId, String teamId) {
    try {
      con = dataSource.getConnection();
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
   */
  public void addGameRecordList(final List<GameRecordModel> recordList) throws Exception {
    if (CollectUtil.isHaveValue(recordList)) {
      long start = System.currentTimeMillis();
      List<GameRecord> entityData = new ArrayList<>();
      for (GameRecordModel record : recordList) {
        //addGameRecord(record);
        GameRecord entity = new GameRecord();
        BeanUtils.copyProperties(record, entity);
        entityData.add(entity);
      }
      gameRecordService.save(entityData);
      long end = System.currentTimeMillis();
      loger.info("导入{}条白名单记录进数据库，耗时：{}毫秒", recordList.size(), (end - start));
    }
  }

  /**
   * 获取已锁定的战绩记录中最大的时间
   *
   * @time 2017年11月25日
   */
  public String getMaxGameRecordTime() {
    String maxRecordTime = "";
    try {
      con = dataSource.getConnection();
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
   * 获取有效桌数统计
   */
  public Map<String, String> getValidLevelAndCount(String currentLMType) {
    Map<String, String> map = new HashMap<>();
    try {
      con = dataSource.getConnection();
      String sql =
          "SELECT max(LEVEL), count(DISTINCT tableId) FROM ( SELECT tableId,LEVEL, sumHandsCount, soft_time FROM game_record WHERE sumHandsCount > '0' and lmType = '"
              + currentLMType
              + "' and soft_time = ( SELECT max(soft_time) AS softTime FROM game_record )) a GROUP BY LEVEL";
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
   * 获取已锁定的战绩记录中最大的时间
   *
   * @time 2017年11月25日
   */
  public String getMaxBankFlowTime() {
    String maxRecordTime = "";
    try {
      con = dataSource.getConnection();
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


  /**
   * 获取某俱乐部下的团队统计数据
   */
  public List<TeamStaticInfo> getStaticRecordsByClub(String clubId, String teamId) {
    return getStaticRecords(clubId, null);
  }

  /**
   * 获取某团队统计数据
   */
  public List<TeamStaticInfo> getStaticRecordsByTeam(String clubId, String teamId) {
    return getStaticRecords(clubId, teamId);
  }

  private List<TeamStaticInfo> getStaticRecords(String clubId, String teamId) {
    List<TeamStaticInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
      String aTeamBeginSQL =
          "SELECT b.softTime, b.teamId, b.sumZJ, b.sumHS, b.sumHB, b.sumFWF, b.sumPerson, round(b.sumZJ + b.sumHS + b.sumHB - b.sumFWF, 1) sumProfit, b.HBRate, b.HSRate FROM ( SELECT a.teamId, a.sumZJ, a.sumHS, a.sumHB, a.sumPerson, a.softTime, a.HBRate, a.HSRate, CASE WHEN ( HSRate >= 0 AND HBRate >= 0 AND (sumHS + sumHB) > FWFValid ) THEN TRUNCATE ( sumHS * HSRate + sumHB * HBRate, 1 ) ELSE 0 END AS sumFWF FROM ( SELECT m.teamId, sum(r.shishou) sumZJ, ROUND(sum(r.chuHuishui), 1) * (- 1) sumHS, ROUND(sum(r.huiBao), 1) sumHB, count(1) + '' sumPerson, ROUND(sum(r.heLirun), 0) sumProfit, min(r.soft_time) softTime, min(t.proxyHBRate) * 0.01 HBRate, min(t.proxyHSRate) * 0.01 HSRate, min(t.proxyFWF) FWFValid FROM game_record r LEFT JOIN members m ON r.playerId = m.playerId LEFT JOIN teamhs t ON m.teamId = t.teamId "
              + " WHERE  r.isCleared = '0' and r.clubId = '" + clubId + "' ";
      String aTeamEndSQL = "GROUP BY r.soft_time, t.teamId ) a ) b ORDER BY b.softTime, b.teamId ASC ";

      String allTeamBeginSQL = "";
      String aTeamConditionSQL = "";
      String allTeamEndSQL = "";

      if (StringUtils.isNotBlank(teamId)) {
        aTeamConditionSQL = " AND t.teamId = '" + teamId + "' ";
      } else {
        allTeamBeginSQL = "SELECT min(c.softTime) 统计时间, min(c.teamId) 团队ID, sum(c.sumZJ) 总战绩, sum(c.sumHS) 总回水, sum(c.sumHB) 总回保, sum(c.sumFWF) 总服务费, sum(c.sumPerson) 总人数, sum(c.sumProfit) 总输赢, min(c.HBRate) 代理回保比例, min(c.HSRate) 代理回水比例 FROM( ";
        allTeamEndSQL = ")c GROUP BY c.teamId ORDER BY 总战绩 desc";
      }
      String sql =
          allTeamBeginSQL + aTeamBeginSQL + aTeamConditionSQL + aTeamEndSQL + allTeamEndSQL;
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TeamStaticInfo info = new TeamStaticInfo();
        info.setTeamClubId(clubId);
        info.setStaticTime(rs.getString(1));
        info.setTeamId(rs.getString(2));
        info.setSumZJ(rs.getString(3));
        info.setSumChuhuishui(rs.getString(4));
        info.setSumHuibao(rs.getString(5));
        info.setTeamFWF(rs.getString(6));
        info.setSumPerson(rs.getString(7));
        info.setSumProfit(rs.getString(8));
        info.setTeamProxyHBRate(NumUtil.getPercentStr(rs.getString(9)));
        info.setTeamProxyHSRate(NumUtil.getPercentStr(rs.getString(10)));
        list.add(info);
      }
    } catch (Exception e) {
      ErrorUtil.err("获取团队统计数据失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }


  public List<TotalInfo2> getStaticDetailRecords(String clubId, String teamId, String softTime) {
    List<TotalInfo2> finalList = new ArrayList<>();
    try {
      List<GameRecordModel> list = gameRecordService
          .getStaticDetailRecords(clubId, teamId, softTime);
      finalList = list.stream().map(r -> {
        TotalInfo2 info = new TotalInfo2();
        info.setTuan(r.getTeamId());
        info.setWanjiaId(r.getPlayerid());
        info.setWanjia(r.getPlayerName());
        info.setJifen(r.getYszj());
        info.setShishou(r.getShishou());
        info.setShouHuishui(r.getShouhuishui());
        info.setChuHuishui(r.getChuhuishui());
        info.setShuihouxian(r.getShuihouxian());
        info.setBaohui(r.getHuibao());
        info.setBaoxian(r.getSingleinsurance()); // 待查看
        info.setHeLirun(r.getHelirun());
        info.setTableId(r.getTableid());

        return info;
      }).collect(Collectors.toList());
    } catch (Exception e) {
      ErrorUtil.err("获取当天历史记录失败", e);
    } finally {
      close(con, ps);
    }
    return finalList;
  }


  // 清空团队的记录，只是操作标志位
  public int clearTeamGameRecord(String clubId, String teamId) {
    int i = 0;
    try {
      loger.info("清空团队的记录，俱乐部是：{}, 团队是：{}", clubId, teamId);
      con = dataSource.getConnection();
      String sql;
      sql =
          "update game_record r LEFT JOIN members m on r.playerId = m.playerId  set r.isCleared = '1'  where r.clubId = '"
              + clubId + "' and  m.teamId = '" + teamId + "'";
      ps = con.prepareStatement(sql);
      i = ps.executeUpdate();
    } catch (SQLException e) {
      ErrorUtil.err("清空团队的记录失败", e);
    } finally {
      close(con, ps);
    }
    return i;
  }

  // 清空俱乐部的记录，只是操作标志位
  public int clearClubGameRecord(String lmType, String clubId) {
    int i = 0;
    try {
      loger.info("清空俱乐部的记录，俱乐部是：{}, 所属联盟是：{}", clubId, lmType);
      con = dataSource.getConnection();
      String sql;
      sql =
          "update game_record r LEFT JOIN club c on r.clubId = c.clubId  set r.isCleared = '1'  where r.isCleared = '0' and r.clubId = '"
              + clubId + "' and  r.lmType = '" + lmType + "'";
      ps = con.prepareStatement(sql);
      i = ps.executeUpdate();
    } catch (SQLException e) {
      ErrorUtil.err("清空俱乐部的记录失败", e);
    } finally {
      close(con, ps);
    }
    return i;
  }

  // ============================

  /**
   * 获取俱乐部统计数据
   */
  public List<ClubStaticInfo> getClubTotalStatic(String lmType) {
    return getClubStaticRecords(lmType, null, false);
  }

  /**
   * 获取某个俱乐部每天的统计数据
   */
  public List<ClubStaticInfo> getClubEveryDayStatic(String lmType, String clubId) {
    return getClubStaticRecords(lmType, clubId, false);
  }

  /**
   * 导出所有俱乐部的统计数据
   */
  public List<ClubStaticInfo> getClubsExcelStatic(String lmType) {
    return getClubStaticRecords(lmType, null, true);
  }


  private List<ClubStaticInfo> getClubStaticRecords(String lmType, String clubId,
      boolean isExportAllClubExcels) {
    List<ClubStaticInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();
      String baseSql =
          "SELECT r.lmType, c. NAME, r.clubId, ROUND(sum(r.yszj) * " + Constants.FINAL_HS_RATE_095
              + ", 0) 总战绩 , ROUND( sum(r.shuihouxian), 0 ) 总保险, count(1) 总人数,  min(r.soft_time) FROM game_record r LEFT JOIN club c ON r.clubId = c.clubId "
              + " where r.isCleared = '0' and r.lmType = '" + lmType + "'";
      String sql = null;
      if (isExportAllClubExcels) {
        sql = baseSql
            + " GROUP BY r.clubId, r.soft_time ORDER BY r.clubId, r.soft_time ASC"; // 导出Excel所需

      } else {

        if (StringUtils.isBlank(clubId)) {
          sql = baseSql + " GROUP BY r.clubId ORDER BY 总战绩 desc"; // 查看所有俱乐部的汇总统计

        } else {
          sql = baseSql + "and r.clubId = '" + clubId
              + "' GROUP BY r.soft_time ORDER BY r.soft_time ASC"; // 查看一个俱乐部的每天统计
        }
      }

      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ClubStaticInfo info = new ClubStaticInfo();
        info.setClubLmType(rs.getString(1));
        info.setClubName(rs.getString(2));
        info.setClubId(rs.getString(3));
        info.setClubSumZJ(rs.getString(4));
        info.setClubSumBaoxian(rs.getString(5));
        info.setClubSumPerson(rs.getString(6));
        info.setClubStaticTime(rs.getString(7));
        info.setClubSumProfit(
            NumUtil.digit0(NumUtil.getSum(info.getClubSumZJ(), info.getClubSumBaoxian())));
        list.add(info);
      }
    } catch (Exception e) {
      ErrorUtil.err("获取俱乐部统计数据失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }


  /**
   * 获取软件时间的月日字符串
   * <p>
   * 如把2018-10-14转为10月14日
   */
  private String handleTime(String softTime) {
    if (StringUtil.isNotBlank(softTime) && softTime.contains("-")) {
      String[] timeArr = softTime.split("-");
      softTime = Integer.valueOf(timeArr[1]) + "月" + Integer.valueOf(timeArr[2]) + "号";
    }
    return softTime;
  }


  /**
   * 战绩统计插入最新数据
   *
   * @param clubId 当前俱乐部
   */
  public void insertZjStaticData(String clubId) {
    try {
      String sql = "";
      // 先删除
      con = dataSource.getConnection();
      sql = "delete from game_record_zj";
      ps = con.prepareStatement(sql);
      ps.execute();

      // 再插入
      long start = System.currentTimeMillis();
      //sql = "INSERT INTO game_record_zj SELECT b.player_id, r.importTime finished_time,  r.beginPlayerName, r.clubId, m.teamId, r.tableId, r.yszj, r.soft_time, r.singleInsurance, c. NAME FROM ( SELECT a.player_id FROM ( SELECT r.playerId player_id, sum(r.yszj) AS total_yszj, sum(r.singleInsurance) total_insurance FROM game_record r where r.juType = '普通保险局' or r.juType = '奥马哈保险局' GROUP BY r.playerId ) a WHERE a.total_insurance = 0 AND a.total_yszj > 0 ) b LEFT JOIN members m ON b.player_id = m.playerId LEFT JOIN game_record r ON b.player_id = r.playerId and r.juType in('普通保险局','奥马哈保险局') LEFT JOIN club c ON c.clubId = r.clubId";
      sql = "INSERT INTO game_record_zj SELECT r.playerId, r.importTime finished_time, r.beginPlayerName, r.clubId, m.teamId, r.tableId, r.yszj, r.soft_time, r.singleInsurance, c. NAME FROM ( SELECT a.player_id, a.clubId FROM ( SELECT r.playerId player_id, sum(r.yszj) AS total_yszj, sum(r.singleInsurance) total_insurance, r.clubId FROM game_record r WHERE r.juType = '普通保险局' OR r.juType = '奥马哈保险局' GROUP BY r.clubId, r.playerId ) a WHERE a.total_insurance = 0 AND a.total_yszj > 0 ) rr INNER JOIN game_record r ON rr.player_id = r.playerId AND rr.clubId = r.clubId AND r.juType IN ( '普通保险局', '奥马哈保险局' ) LEFT JOIN members m ON r.playerId = m.playerId LEFT JOIN club c ON c.clubId = rr.clubId";
      ps = con.prepareStatement(sql);
      ps.execute();
      loger.info("战绩统计原始数据入库耗时：{}毫秒", (System.currentTimeMillis() - start));

    } catch (SQLException e) {
      ErrorUtil.err("战绩统计原始数据入库失败", e);
    } finally {
      close(con, ps);
    }
  }

  /**
   * 战绩统计获取团队汇总信息
   */
  public List<ZjTeamStaticInfo> getZjTeamStaticsRecordsByClub(String clubId) {
    // 更新最新数据到战绩表game_record_zj
    List<ZjTeamStaticInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();

      long start = System.currentTimeMillis();
      String sql = "SELECT rr.clubId 所属俱乐部, rr.teamId 团队ID, sum(rr.personCount) 人次, min(rr.soft_time) 最早统计时间 FROM ( SELECT * FROM ( SELECT r.clubId, r.teamId, r.playerId, r.playerName, count(1) personCount, sum(r.yszj) totalYszj, min(r.soft_time) soft_time FROM game_record_zj r WHERE r.clubId = ? GROUP BY r.playerId ) a WHERE a.totalYszj > 0 ) rr GROUP BY rr.teamId";
      ps = con.prepareStatement(sql);
      ps.setString(1, clubId);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ZjTeamStaticInfo info = new ZjTeamStaticInfo();
        info.setTeamClubId(rs.getString(1));
        info.setTeamId(StringUtils.defaultString(rs.getString(2)));
        info.setTeamPersonCount(rs.getString(3));
        info.setTeamBeginStaticTime(rs.getString(4));
        list.add(info);
      }
      loger.info("战绩统计每个团队结果耗时：{}毫秒", (System.currentTimeMillis() - start));

    } catch (SQLException e) {
      ErrorUtil.err("战绩统计每个团队结果失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }


  public List<ZjTeamStaticDetailInfo> getZjTeamStaticsByTeamId(String clubId, String teamId) {
    // 更新最新数据到战绩表game_record_zj
    List<ZjTeamStaticDetailInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();

      long start = System.currentTimeMillis();
      String sql = "SELECT * FROM ( SELECT r.clubId 所属俱乐部, r.teamId 团队ID, r.playerId 玩家ID, r.playerName 玩家名称, count(1) 人次, sum(r.yszj) 累计战绩 FROM game_record_zj r WHERE r.clubId = ? AND r.teamId = ? GROUP BY r.playerId ) a WHERE a.累计战绩 > 0";
      ps = con.prepareStatement(sql);
      ps.setString(1, clubId);
      ps.setString(2, teamId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ZjTeamStaticDetailInfo info = new ZjTeamStaticDetailInfo();
        info.setDetailClubId(clubId);
        info.setDetailTeamId(rs.getString(2));
        info.setDetailPlayerId(rs.getString(3));
        info.setDetailPlayerName(rs.getString(4));
        info.setDetailPersonCount(rs.getString(5));
        info.setDetailPersonSumYszj(rs.getString(6)); // TODO 重写SQL设置个人的累计战绩
        list.add(info);
      }
      if (loger.isDebugEnabled()) {
        loger.info("战绩统计{}团队结果耗时：{}毫秒", teamId, (System.currentTimeMillis() - start));
      }
    } catch (SQLException e) {
      ErrorUtil.err("战绩统计每个团队结果失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }


  /**
   * 战绩统计：获取所有俱乐部的统计数据中
   */
  public List<ZjClubStaticInfo> getZjClubTotalStatic() {
    // 更新最新数据到战绩表game_record_zj
    List<ZjClubStaticInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();

      long start = System.currentTimeMillis();
      String sql = "SELECT r.club_name 俱乐部名称, r.clubId 俱乐部ID, min(r.soft_time) 最早统计时间, count(1) 人次 FROM game_record_zj r GROUP BY r.clubId";
      ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ZjClubStaticInfo info = new ZjClubStaticInfo();
        info.setClubName(rs.getString(1));
        info.setClubId(rs.getString(2));
        info.setClubBeginStaticTime(rs.getString(3));
        info.setClubPersonCount(rs.getString(4));
        list.add(info);
      }
      loger.info("战绩统计统计所有俱乐部结果耗时：{}毫秒", (System.currentTimeMillis() - start));
    } catch (SQLException e) {
      ErrorUtil.err("战绩统计每个团队结果失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }


  /**
   * 战绩统计：右边单个俱乐部统计
   */
  public List<ZjClubStaticDetailInfo> getZjClubStaticDetail(String clubId) {
    // 更新最新数据到战绩表game_record_zj
    List<ZjClubStaticDetailInfo> list = new ArrayList<>();
    try {
      con = dataSource.getConnection();

      // 再插入
      long start = System.currentTimeMillis();
      String sql = "SELECT r.club_name 俱乐部名称, r.clubId 所属俱乐部, r.playerId, r.playerName 玩家名称, count(1) 人次, sum(r.yszj) 累计战绩, min(r.soft_time) 开始统计时间 FROM game_record_zj r WHERE r.clubId = ? GROUP BY r.playerId";
      ps = con.prepareStatement(sql);
      ps.setString(1, clubId);
      ResultSet rs = ps.executeQuery();
      AtomicInteger indexObj = new AtomicInteger(1);
      while (rs.next()) {
        ZjClubStaticDetailInfo info = new ZjClubStaticDetailInfo();
        info.setDetailClubIndex(indexObj.getAndIncrement() + "");
        info.setDetailClubName(rs.getString(1));
        info.setDetailClubId(rs.getString(2));
        info.setDetailClubPlayerId(rs.getString(3));
        info.setDetailClubPlayerName(rs.getString(4));
        info.setDetailClubPersonCount(rs.getString(5));
        info.setDetailClubTotalZJ(rs.getString(6));
        info.setDetailClubBeginStaticTime(rs.getString(7));
        list.add(info);
      }
      loger.info("战绩统计右边单个俱乐部统计结果耗时：{}毫秒", (System.currentTimeMillis() - start));
    } catch (SQLException e) {
      ErrorUtil.err("战绩统计右边单个俱乐部统计结果失败", e);
    } finally {
      close(con, ps);
    }
    return list;
  }
}
