package com.kendy.constant;

import com.kendy.db.entity.Player;
import com.kendy.db.service.GameRecordService;
import com.kendy.db.service.PlayerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.db.DBUtil;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.Huishui;
import com.kendy.entity.ShangmaDetailInfo;
import com.kendy.entity.TeamInfo;
import com.kendy.entity.UserInfos;
import com.kendy.enums.KeyEnum;
import com.kendy.model.GameRecordModel;
import com.kendy.util.CollectUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.stage.Stage;

/**
 * 模拟数据表的常量类 功能：缓存数据
 *
 * @author 林泽涛
 */
@Component
public class DataConstans {

  private Logger logger = LoggerFactory.getLogger(DataConstans.class);

  @Autowired
  private DBUtil dbUtil;

  @Resource
  GameRecordService gameRecordService;

  @Resource
  PlayerService playerService;


//  public static Map<String, String> permissions = new HashMap<>();

//  public void initPermission(){
//    Map<String, String> map = new HashMap<>();
//    map.put("基本信息", "");
//    map.put("场次信息", "");
//    map.put("总汇信息", "");
//    permissions = map;
//  }



  /**
   *
   */
  public DataConstans() {
    super();
    logger.info("DataConstans 构造方法");
  }

  /**************************************************************************************
   * List<GameRecord> Dangju_Team_Huishui_List ：当前俱乐部的记录集合
   * <p>
   * Map<String, List<GameRecord>> zjMap :当前俱乐部的记录，以场次GroupBy Map<String,List<GameRecord>>
   * <p>
   * Team_Huishui_Map : 当前俱乐部的记录，以TeamId进行GroupByMap<String,List<GameRecord>>
   * <p>
   * Total_Team_Huishui_Map : 当前俱乐部的记录，以TeamId进行GroupBy， 不过不删除撤销的数据？
   *
   **************************************************************************************/


  // 缓存人员名单登记Excel中的数据{玩家ID={}}
  public Map<String, Player> membersMap = new HashMap<>();// 撤销后不变
  // 团队ID=玩家ID列表
  // public Map<String, List<String>> teamWanjiaIdMap = new HashMap<>();//撤销后不变
  // 玩家ID=上码详情列表（正在使用的值）
  public Map<String, List<ShangmaDetailInfo>> SM_Detail_Map = new HashMap<>();
  // 玩家ID=上码详情列表（上一场所定的数据，用于撤销时恢复原数据）
  public Map<String, List<ShangmaDetailInfo>> SM_Detail_Map_Locked = new HashMap<>();

  // 缓存战绩文件夹中多份excel中的数据 {场次=infoList...}
  public Map<String, List<GameRecordModel>> zjMap = new LinkedHashMap<>();
  // 缓存当局回水
  public List<GameRecordModel> Dangju_Team_Huishui_List = new LinkedList<>();
  // 缓存战绩文件夹中多份excel中的数据 {团队ID=List<GameRecord>...}这个可能会被修改，用在展示每场的tableTeam信息
  public Map<String, List<GameRecordModel>> Team_Huishui_Map = new LinkedHashMap<>();
  // 这个不会被修改，是总的团队回水记录。用在团队回水当天查询
  public Map<String, List<GameRecordModel>> Total_Team_Huishui_Map = new LinkedHashMap<>();// 撤销后不变

  // 缓存120场次的所有锁定数据{页数第几局={...}}
  public Map<String, Map<String, String>> All_Locked_Data_Map = new LinkedHashMap<>();// 撤销后不变
  // 锁定后是第X局
  public AtomicInteger Paiju_Index = new AtomicInteger(1);// 撤销后不变
  // 缓存场次与局映射
  public Map<String, String> Index_Table_Id_Map = new HashMap<>();// 撤销后不变

  // 缓存父ID和子ID {父ID=List<子ID>}
  public Map<String, Set<String>> Combine_Super_Id_Map = new HashMap<>();
  // 缓存子ID和父ID {子ID=父ID}
  public Map<String, String> Combine_Sub_Id_Map = new HashMap<>();

  public String Root_Dir = System.getProperty("user.home");// 撤销后不变

  // 缓存团队回水
  public Map<String, Huishui> huishuiMap = new HashMap<>();// 撤销后不变

  // 缓存实时开销
  public Map<String, String> kaixiaoMap = new HashMap<>();

  // 缓存昨日留底
  public Map<String, String> preDataMap = new HashMap<>();

  // 缓存所有股东名称
  public List<String> gudongList = new ArrayList<>();// 撤销后不变

  // 缓存一些窗体的实例
  public Map<String, Stage> framesNameMap = new HashMap<>();// 撤销后不变

  // 导入战绩后缓存一些总和信息
  public Map<String, Double> SumMap = new HashMap<>();

  // 缓存上一场次已经锁定的团队记录{teamID=TeamInfo...},主要用于合并团队记录(好像也没必要了这个)
  public Map<String, TeamInfo> Team_Info_Pre_Map = new HashMap<>();// 撤销后不变

  // 缓存日期
  public String Date_Str = "";


  private final String KEY_GU_DONG = KeyEnum.GU_DONG.getKeyName();

  // 初始化股东列表
  public void initGudong() {
    String gudongs = dbUtil.getValueByKeyWithoutJson(KEY_GU_DONG);
    if (StringUtil.isBlank(gudongs)) {
      gudongs = "B,C,Q,银河"; // 如果客户删除这四个，想用其它的，这里的值就不对了
      dbUtil.saveOrUpdateOthers(KEY_GU_DONG, gudongs);
    }
    if (!StringUtil.isBlank(gudongs)) {
      for (String gudong : gudongs.split(",")) {
        gudongList.add(gudong);
      }
    }
  }

  @PostConstruct
  public void inits() {

    // 初始化权限
    // initPermission();

    // 股东
    initGudong();

    // 初始化人员表或团队回水表
    initMetaData();

    logger.info("初始化股东、人员表、团队回水、合并ID关系表完成。。。");
  }

  /**
   * 锁定时获取缓存数据 注意：人员，回水和合并ID不缓存，单独存储到数据
   *
   * @time 2017年11月4日
   */
  public Map<String, String> getLockedDataMap() {
    Map<String, String> lastLockedDataMap = new HashMap<>();

    // 玩家ID=上码详情列表（正在使用的值）
    // public Map<String,List<ShangmaDetailInfo>> SM_Detail_Map= new HashMap<>();
    lastLockedDataMap.put("SM_Detail_Map", JSON.toJSONString(this.SM_Detail_Map));

    // 玩家ID=上码详情列表（上一场所定的数据，用于撤销时恢复原数据）
    // public Map<String,List<ShangmaDetailInfo>> SM_Detail_Map_Locked= new HashMap<>();
    lastLockedDataMap.put("SM_Detail_Map_Locked", JSON.toJSONString(this.SM_Detail_Map_Locked));

    // **********************************************************以下四个不保存数据，中途开始时去数据表拿*********************************
    // 缓存战绩文件夹中多份excel中的数据 {场次=infoList...}
    // public Map<String,List<UserInfos>> zjMap = new LinkedHashMap<>();
    // lastLockedDataMap.put("zjMap", JSON.toJSONString(this.zjMap));

    // 缓存当局回水
    // public List<GameRecord> Dangju_Team_Huishui_List = new LinkedList<>();
    // lastLockedDataMap.put("Dangju_Team_Huishui_List",
    // JSON.toJSONString(this.Dangju_Team_Huishui_List));

    // 缓存战绩文件夹中多份excel中的数据 {团队ID=List<GameRecord>...}这个可能会被修改，用在展示每场的tableTeam信息
    // public Map<String,List<GameRecord>> Team_Huishui_Map = new LinkedHashMap<>();
    // lastLockedDataMap.put("Team_Huishui_Map", JSON.toJSONString(this.Team_Huishui_Map));

    // 这个不会被修改，是总的团队回水记录。用在团队回水当天查询
    // public Map<String,List<GameRecord>> Total_Team_Huishui_Map = new
    // LinkedHashMap<>();//撤销后不变
    // lastLockedDataMap.put("Total_Team_Huishui_Map",
    // JSON.toJSONString(this.Total_Team_Huishui_Map));

    // *******************************************************************************************

    // 锁定后是第X局
    // public AtomicInteger Paiju_Index = new AtomicInteger(1);//撤销后不变
    lastLockedDataMap.put("Paiju_Index", JSON.toJSONString(this.Paiju_Index));

    // 缓存场次与局映射
    // public Map<String,String> Index_Table_Id_Map = new HashMap<>();//撤销后不变
    lastLockedDataMap.put("Index_Table_Id_Map", JSON.toJSONString(this.Index_Table_Id_Map));

    // public String Root_Dir = System.getProperty("user.home");//撤销后不变
    lastLockedDataMap.put("Root_Dir", JSON.toJSONString(this.Root_Dir));

    // 缓存实时开销
    // public Map<String, String> kaixiaoMap = new HashMap<>();
    lastLockedDataMap.put("kaixiaoMap", JSON.toJSONString(this.kaixiaoMap));

    // 缓存昨日留底
    // public Map<String,Map<String,String>> preDataMap = new HashMap<>();;
    lastLockedDataMap.put("preDataMap", JSON.toJSONString(this.preDataMap));

    // 缓存所有股东名称
    // public List<String> gudongList = new ArrayList<>();//撤销后不变
    lastLockedDataMap.put("gudongList", JSON.toJSONString(this.gudongList));

    // 导入战绩后缓存一些总和信息
    // public Map<String,Double> SumMap = new HashMap<>();
    lastLockedDataMap.put("SumMap", JSON.toJSONString(this.SumMap));

    // 缓存上一场次已经锁定的团队记录{teamID=TeamInfo...},主要用于合并团队记录(好像也没必要了这个)
    // public Map<String,TeamInfo> Team_Info_Pre_Map = new HashMap<>();//撤销后不变
    lastLockedDataMap.put("Team_Info_Pre_Map", JSON.toJSONString(this.Team_Info_Pre_Map));

    // 缓存日期
    // public String Date_Str = "";
    lastLockedDataMap.put("Date_Str", JSON.toJSONString(this.Date_Str));

    return lastLockedDataMap;
  }


  /**
   * 初始化所有缓存
   */
  public void clearAllData() {
    // 缓存人员名单登记Excel中的数据{玩家ID={}}
    membersMap = new HashMap<>();// 撤销后不变
    // 玩家ID=上码详情列表（正在使用的值）
    SM_Detail_Map = new HashMap<>();
    // 玩家ID=上码详情列表（上一场所定的数据，用于撤销时恢复原数据）
    SM_Detail_Map_Locked = new HashMap<>();
    // 缓存战绩文件夹中多份excel中的数据 {场次=infoList...}
    zjMap = new LinkedHashMap<>();
    // 缓存当局回水
    Dangju_Team_Huishui_List = new LinkedList<>();
    // 缓存战绩文件夹中多份excel中的数据 {团队ID=List<GameRecord>...}这个可能会被修改，用在展示每场的tableTeam信息
    Team_Huishui_Map = new LinkedHashMap<>();
    // 这个不会被修改，是总的团队回水记录。用在团队回水当天查询
    Total_Team_Huishui_Map = new LinkedHashMap<>();// 撤销后不变
    // 缓存120场次的所有锁定数据{页数第几局={...}}
    All_Locked_Data_Map = new LinkedHashMap<>();// 撤销后不变
    // 锁定后是第X局
    Paiju_Index = new AtomicInteger(1);// 撤销后不变
    // 缓存场次与局映射
    Index_Table_Id_Map = new HashMap<>();// 撤销后不变
    // 缓存父ID和子ID {父ID=List<子ID>}
    Combine_Super_Id_Map = new HashMap<>();
    // 缓存子ID和父ID {子ID=父ID}
    Combine_Sub_Id_Map = new HashMap<>();
    Root_Dir = System.getProperty("user.home");// 撤销后不变
    // 缓存团队回水
    huishuiMap = new HashMap<>();// 撤销后不变
    // 缓存实时开销
    kaixiaoMap = new HashMap<>();
    // 缓存最新前一场数据，如果当前数据场次为1，则加载昨日数据
    preDataMap = new HashMap<>();
    // 缓存所有股东名称
    gudongList = new ArrayList<>();// 撤销后不变
    // 缓存一些窗体的实例
    framesNameMap = new HashMap<>();// 撤销后不变
    // 导入战绩后缓存一些总和信息
    SumMap = new HashMap<>();
    // 缓存上一场次已经锁定的团队记录{teamID=TeamInfo...},主要用于合并团队记录(好像也没必要了这个)
    Team_Info_Pre_Map = new HashMap<>();// 撤销后不变
    Date_Str = "";
  }

  /**
   * 初始化数据
   */
  public void initMetaData() {
    // 清空所有数据
    // clearAllData();

    try {
      // 初始化人员数据
      List<Player> memberList = playerService.getAll();
      membersMap = new HashMap<>();
      memberList.forEach(player -> {
        membersMap.put(player.getPlayerid(), player);
      });

      // 初始化团队回水
      List<Huishui> teamHSList = dbUtil.getAllTeamHS();
      huishuiMap = new HashMap<>();
      teamHSList.forEach(hs -> {
        huishuiMap.put(hs.getTeamId().toUpperCase(), hs);
      });

      // 初始化合并ID关系
      initCombineId();

    } catch (Exception e) {
      ShowUtil.show("警告：初始化人员表或团队回水表或合并ID表失败！原因：" + e.getMessage());
    }
  }


  /**
   * 初始化合并ID关系
   *
   * @time 2017年11月4日
   */
  public void initCombineId() {
    this.Combine_Super_Id_Map = dbUtil.getCombineData();
    if (this.Combine_Super_Id_Map == null) {
      this.Combine_Super_Id_Map = new HashMap<>();
    } else {
      this.Combine_Sub_Id_Map = new HashMap<>();
      this.Combine_Super_Id_Map.forEach((parentId, subIdSet) -> {
        for (String subId : subIdSet) {
          this.Combine_Sub_Id_Map.put(subId, parentId);
        }
      });
    }
  }


  /**
   * 加载昨日数据
   */
  public void loadPreData() {
    // 初始化昨日留底数据
    preDataMap = new HashMap<>();
    try {
      preDataMap = dbUtil.getLastPreData();
      // 从数据库中获取上一次保存的锁定数据
      if (!dbUtil.isPreData2017VeryFirst()) {
        Map<String, String> map = dbUtil.getLastLockedData();
        SumMap = JSON.parseObject(map.get("SumMap"), new TypeReference<Map<String, Double>>() {
        });
      }
    } catch (Exception e) {
      ShowUtil.show("警告：初始化昨日留底数据失败！原因：" + e.getMessage());
    }
  }


  public Map<String, Map<String, String>> getPreData() {

    return null;
  }

  /**
   * 中途恢复缓存数据
   *
   * （人员和回水以及合并ID不在此处，在调用此方法前已经设置了）
   */
  public void recoveryAllCache() {
    // 清空所有数据
    clearAllData();

    // 从数据库加载记录
    recoveryGameRecords();

    // 加载每一场的锁定数据
    All_Locked_Data_Map = dbUtil.getAllLockedRecords();
    logger.info("加载锁定数据：" + (All_Locked_Data_Map == null ? "为null!" : "不为空"));

    // 从数据库中获取上一次保存的锁定数据
    Map<String, String> map = dbUtil.getLastLockedData();

    // 玩家ID=上码详情列表（正在使用的值）
    SM_Detail_Map = JSON.parseObject(map.get("SM_Detail_Map"),
        new TypeReference<Map<String, List<ShangmaDetailInfo>>>() {
        });

    // 玩家ID=上码详情列表（上一场所定的数据，用于撤销时恢复原数据）
    SM_Detail_Map_Locked = JSON.parseObject(map.get("SM_Detail_Map_Locked"),
        new TypeReference<Map<String, List<ShangmaDetailInfo>>>() {
        });

    // 缓存120场次的所有锁定数据{页数第几局={...}}
//    All_Locked_Data_Map = JSON.parseObject(map.get("All_Locked_Data_Map"),
//        new TypeReference<Map<String, Map<String, String>>>() {});
//    logger.info("加载锁定数据：" + (All_Locked_Data_Map == null ? "为null!" : "不为空"));

    // 锁定后是第X局
    Paiju_Index = JSON.parseObject(map.get("Paiju_Index"), new TypeReference<AtomicInteger>() {
    });

    // 缓存场次与局映射
    Index_Table_Id_Map = JSON.parseObject(map.get("Index_Table_Id_Map"),
        new TypeReference<Map<String, String>>() {
        });

    Root_Dir = JSON.parseObject(map.get("Root_Dir"), new TypeReference<String>() {
    });
    // 缓存实时开销
    kaixiaoMap =
        JSON.parseObject(map.get("kaixiaoMap"), new TypeReference<Map<String, String>>() {
        });

    // 缓存最新前一场数据
    preDataMap =
        JSON.parseObject(map.get("preDataMap"), new TypeReference<Map<String, String>>() {
        });

    // 缓存所有股东名称
    gudongList = JSON.parseObject(map.get("gudongList"), new TypeReference<List<String>>() {
    });

    // 导入战绩后缓存一些总和信息
    SumMap = JSON.parseObject(map.get("SumMap"), new TypeReference<Map<String, Double>>() {
    });

    // 缓存上一场次已经锁定的团队记录{teamID=TeamInfo...},主要用于合并团队记录(好像也没必要了这个)
    Team_Info_Pre_Map = JSON.parseObject(map.get("Team_Info_Pre_Map"),
        new TypeReference<Map<String, TeamInfo>>() {
        });
    Date_Str = JSON.parseObject(map.get("Date_Str"), new TypeReference<String>() {
    });

    logger.info("==============中途恢复缓存数据。。。finishes");
  }

  /**************************************************************************************
   *
   * List<GameRecord> Dangju_Team_Huishui_List ：当前俱乐部的记录集合
   * <p>
   * Map<String, List<GameRecord>> zjMap : 当前俱乐部的记录，以场次GroupBy 
   * <p>
   * Map<String,List<GameRecord>> Team_Huishui_Map : 当前俱乐部的记录，以TeamId进行GroupBy
   * <p>
   * Map<String,List<GameRecord>> Total_Team_Huishui_Map : 当前俱乐部的记录，以TeamId进行GroupBy， 不过不删除撤销的数据？
   *
   **************************************************************************************/
  /**
   * 中途继续恢复记录信息 主要是恢复 Dangju_Team_Huishui_List， zjMap，Team_Huishui_Map，Total_Team_Huishui_Map
   */
  public void recoveryGameRecords() {
    String maxGameRecordTime = dbUtil.getMaxGameRecordTime();
    // String clubId = myController.currentClubId.getText();
    String clubId = dbUtil.getValueByKeyWithoutJson(KeyEnum.CLUB_ID.getKeyName());
    List<GameRecordModel> gameRecordModels = gameRecordService.getGameRecordsByMaxTimeAndClub(maxGameRecordTime, clubId);

    if (StringUtil.isAnyBlank(maxGameRecordTime, clubId) || CollectUtil.isEmpty(gameRecordModels)) {
      // 清空所有数据
      clearAllData();
    } else {
      // 缓存战绩文件夹中多份excel中的数据 {场次=infoList...}
      zjMap = gameRecordModels.stream().collect(Collectors.groupingBy(GameRecordModel::getTableid));

      // 缓存当局回水
      Dangju_Team_Huishui_List = gameRecordModels;

      // 缓存战绩文件夹中多份excel中的数据 {团队ID=List<GameRecord>...}这个可能会被修改，用在展示每场的tableTeam信息
      Team_Huishui_Map = gameRecordModels.stream()
          .filter(e -> "0".equals(e.getIsjiesuaned()))
          .collect(Collectors.groupingBy(GameRecordModel::getTeamId));

      // 这个不会被修改，是总的团队回水记录。用在团队回水当天查询
      Total_Team_Huishui_Map =
          gameRecordModels.stream().collect(Collectors.groupingBy(GameRecordModel::getTeamId));
    }

  }

  /******************************************************************************************
   *
   *
   *
   * ****************************************************************************************
   */
  /**
   * 获取LinkedHashMap的最后一个元素
   */
  public <K, V> Entry<K, V> getTail(LinkedHashMap<K, V> map) {
    Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
    Entry<K, V> tail = null;
    while (iterator.hasNext()) {
      tail = iterator.next();
    }
    return tail;
  }

  public Entry<String, List<UserInfos>> getTail(Map<String, List<UserInfos>> zjMap) {
    Iterator<Entry<String, List<UserInfos>>> iterator = zjMap.entrySet().iterator();
    Entry<String, List<UserInfos>> tail = null;
    while (iterator.hasNext()) {
      tail = iterator.next();
    }
    return tail;
  }

  // 同步SM_Detail_Map

  // 获取最新的SM_Detail_Map(上码表的个人详情）{玩家ID=List<ShangmaDetailInfo>}
  public void refresh_SM_Detail_Map() {
    if (this.membersMap == null) {
      return;
    }

    this.membersMap.forEach((playerId, player) -> {
      if (!StringUtil.isBlank(playerId) && this.SM_Detail_Map.get(playerId) == null) {
        this.SM_Detail_Map.put(playerId, new ArrayList<ShangmaDetailInfo>());
      }
    });
  }

  // 在实时金额那里临时添加
  public void refresh_SM_Detail_Map(CurrentMoneyInfo info) {
    String playerId = info.getWanjiaId();
    if (!StringUtil.isBlank(playerId)) {
      List<ShangmaDetailInfo> list = this.SM_Detail_Map.get(playerId);
      if (list == null) {
        list = new ArrayList<>();
        this.SM_Detail_Map.put(playerId, list);
      }

    }
  }

  /**
   * 根据名称找玩家ID
   */
  @SuppressWarnings("unused")
  public String getPlayerIdByName(String name) {
    final Map<String, Player> memberMap = this.membersMap;
    if (memberMap != null && memberMap.size() > 0) {
      String playerName = "";
      for (Map.Entry<String, Player> entry : memberMap.entrySet()) {
        Player wanjia = entry.getValue();
        playerName = wanjia.getPlayername();
        if (!StringUtil.isBlank(playerName) && playerName.equals(name)) {
          return entry.getKey();// playerId
        }
      }
    }
    return "";
  }

  public Player getPlayerByName(String name) {
    String pId = getPlayerIdByName(name);
    if (!StringUtil.isBlank(pId)) {
      return this.membersMap.get(pId);
    }
    return null;
  }

  // 锁定当局备份上码表的个人详情
  public void lock_SM_Detail_Map() {
    Map<String, List<ShangmaDetailInfo>> map = new HashMap<>();
    List<ShangmaDetailInfo> list = null;
    List<ShangmaDetailInfo> srcList = null;
    ShangmaDetailInfo info = null;
    for (Map.Entry<String, List<ShangmaDetailInfo>> entry : this.SM_Detail_Map.entrySet()) {
      srcList = entry.getValue();
      if (srcList == null || srcList.size() == 0) {
        list = new ArrayList<ShangmaDetailInfo>();
        map.put(entry.getKey(), list);
      } else {
        list = new ArrayList<ShangmaDetailInfo>();
        for (ShangmaDetailInfo detail : srcList) {
          info = new ShangmaDetailInfo();
          info.setShangmaDetailName(detail.getShangmaDetailName());
          info.setShangmaJu(detail.getShangmaJu());
          info.setShangmaPlayerId(detail.getShangmaPlayerId());
          info.setShangmaShishou(detail.getShangmaShishou());
          info.setShangmaSM(detail.getShangmaSM());
          info.setShangmaPreSM(detail.getShangmaPreSM());
          info.setShangmaHasPayed(detail.getShangmaHasPayed());
          list.add(info);
        }
        map.put(entry.getKey(), list);
      }
    }

    this.SM_Detail_Map_Locked = null;
    this.SM_Detail_Map_Locked = map;
  }

  // 撤销时当局恢复上码表的个人详情
  public void recovery_SM_Detail_Map() {
    Map<String, List<ShangmaDetailInfo>> map = new HashMap<>();
    List<ShangmaDetailInfo> list = null;
    List<ShangmaDetailInfo> srcList = null;
    ShangmaDetailInfo info = null;
    for (Map.Entry<String, List<ShangmaDetailInfo>> entry : this.SM_Detail_Map_Locked.entrySet()) {
      srcList = entry.getValue();
      if (srcList == null || srcList.size() == 0) {
        list = new ArrayList<ShangmaDetailInfo>();
        map.put(entry.getKey(), list);
      } else {
        list = new ArrayList<ShangmaDetailInfo>();
        for (ShangmaDetailInfo detail : srcList) {
          info = new ShangmaDetailInfo();
          info.setShangmaDetailName(detail.getShangmaDetailName());
          info.setShangmaJu(detail.getShangmaJu());
          info.setShangmaPlayerId(detail.getShangmaPlayerId());
          info.setShangmaShishou(detail.getShangmaShishou());
          info.setShangmaSM(detail.getShangmaSM());
          info.setShangmaPreSM(detail.getShangmaPreSM());
          info.setShangmaHasPayed(detail.getShangmaHasPayed());
          list.add(info);
        }
        map.put(entry.getKey(), list);
      }
    }

    this.SM_Detail_Map = null;
    this.SM_Detail_Map = map;
  }


}
