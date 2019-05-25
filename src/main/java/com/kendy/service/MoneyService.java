package com.kendy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.controller.BankFlowController;
import com.kendy.controller.ChangciController;
import com.kendy.controller.ChangciController.ZhanjiType;
import com.kendy.controller.GDController;
import com.kendy.controller.LMBController;
import com.kendy.controller.MyController;
import com.kendy.controller.SMAutoController;
import com.kendy.db.DBUtil;
import com.kendy.db.entity.CurrentMoney;
import com.kendy.db.entity.Player;
import com.kendy.db.entity.pk.CurrentMoneyPK;
import com.kendy.db.entity.GameRecord;
import com.kendy.db.service.CurrentMoneyService;
import com.kendy.db.service.GameRecordService;
import com.kendy.db.service.PlayerService;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.DangjuInfo;
import com.kendy.entity.Huishui;
import com.kendy.entity.JiaoshouInfo;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.KeyValue;
import com.kendy.entity.PersonalInfo;
import com.kendy.entity.PingzhangInfo;
import com.kendy.entity.ProfitInfo;
import com.kendy.entity.TeamInfo;
import com.kendy.entity.TotalInfo;
import com.kendy.entity.WanjiaInfo;
import com.kendy.entity.ZijinInfo;
import com.kendy.enums.ColumnColorType;
import com.kendy.enums.MoneyCreatorEnum;
import com.kendy.excel.ExportMembersExcel;
import com.kendy.excel.ExportTeamhsExcel;
import com.kendy.interfaces.Entity;
import com.kendy.model.BankFlowModel;
import com.kendy.model.GameRecordModel;
import com.kendy.util.AlertUtil;
import com.kendy.util.ClipBoardUtil;
import com.kendy.util.ColumnUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.Text2ImageUtil;
import com.kendy.util.TimeUtil;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoneyService {

  private Logger log = LoggerFactory.getLogger(MoneyService.class);

  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public DataConstans dataConstants; // 数据控制类
  @Autowired
  public MyController myController;
  @Autowired
  public SMAutoController smAutoController; // 托管控制类
  @Autowired
  public GDController gdController; // 股东控制类
  @Autowired
  public ShangmaService shangmaService; // 上码控制类
  @Autowired
  public BankFlowController bankFlowController; // 银行流水类
  @Autowired
  LMBController lmbController;

  @Autowired
  ChangciController changciController;

  @Autowired
  LittleGameService littleGameService; // 小游戏服务类

  @Resource
  GameRecordService gameRecordService;

  @Resource
  CurrentMoneyService currentMoneyService;

  @Resource
  PlayerService playerService;




  // {玩家ID=CurrentMoneyInfo}
  public Map<String, CurrentMoneyInfo> Table_CMI_Map = new HashMap<>();
  public TableView<CurrentMoneyInfo> tableCurrentMoneyInfo;

  public void iniitMoneyInfo(TableView<CurrentMoneyInfo> tableCurrentMoney) {
    tableCurrentMoneyInfo = tableCurrentMoney;
  }

  public DecimalFormat df = new DecimalFormat("#.00");


  public void fillTablerAfterImportZJ(TableView<TotalInfo> table,
      TableView<WanjiaInfo> tablePaiju, TableView<DangjuInfo> tableDangju,
      TableView<JiaoshouInfo> tableJiaoshou, TableView<TeamInfo> tableTeam,
      TableView<PersonalInfo> tablePersonal,
      List<GameRecordModel> gameRecordModels, String tableId) {

    if (dataConstants.Team_Huishui_Map == null) { // add 2018-08-04
      dataConstants.Team_Huishui_Map = new LinkedHashMap<>();
    }

    // 1清空 表数据
    table.setItems(null);
    // 2获取InfoList
    ObservableList<TotalInfo> tableTotalInfoList = FXCollections.observableArrayList();
    ObservableList<WanjiaInfo> tableWanjiaInfoList = FXCollections.observableArrayList();
    TotalInfo info = null;
    WanjiaInfo wanjia;
    List<GameRecordModel> teamHuishuiList = null;// 用于缓存和计算团累计团队回水
    Set<String> relatedTeamIdSet = new HashSet<>();// 用于只展示本次战绩的团队信息
    dataConstants.Dangju_Team_Huishui_List = new LinkedList<>();// 每次导入都去初始化当局团队战绩信息
    for (GameRecordModel r : gameRecordModels) {

      /*************************************************** 填充信息表 *****************/
      info = new TotalInfo();
      String playerId = r.getPlayerid();
      String teamId = r.getTeamId();
      String yszj = r.getYszj();
      String playerName = r.getPlayerName();
      String baoxian = r.getSingleinsurance();
      String shishou = r.getShishou();
      String chuHuishui = r.getChuhuishui();
      String shuihouxian = r.getShuihouxian();
      String shouHuishui = r.getShouhuishui();
      String huibao = r.getHuibao();
      String heLirun = r.getHelirun();

      // 团(团ID)
      info.setTuan(teamId);
      // ID
      info.setWanjiaId(playerId);
      // 玩家
      info.setWanjia(playerName);
      // 计分
      info.setJifen(yszj);
      // 实收
      info.setShishou(shishou);
      // 保险
      info.setBaoxian(baoxian);
      // 水后险
      info.setShuihouxian(shuihouxian);
      // 收回水
      info.setShouHuishui(shouHuishui);
      // 合利润
      info.setHeLirun(heLirun);
      // 新增小游戏标识
      if (ZhanjiType.getInstance().isLittleGame()) {
        info.setIsLittleGame("1");
      }

      //设置回水回保
      setHsHbOfTotalInfo(info, r);

      tableTotalInfoList.add(info);

      /*************************************************** 填充牌局表 *****************/
      wanjia = getWanjiaInfo(r, playerId, playerName, shishou);

      /*************************************************** 缓存各团队回水记录 *****************/
      teamId = teamId.toUpperCase();
      if (StringUtil.isNotBlank(teamId)) {
        // 缓存到当局团队战绩信息中
        dataConstants.Dangju_Team_Huishui_List.add(r);

        if (!"公司".equals(teamId)) {
          relatedTeamIdSet.add(teamId);
        }
        // 缓存到总团队回水中(结算按钮后从中减少)
        teamHuishuiList = dataConstants.Team_Huishui_Map.getOrDefault(teamId, new ArrayList<>());
        teamHuishuiList.add(r);
        dataConstants.Team_Huishui_Map.put(teamId, teamHuishuiList);
      } else {
        log.debug("检测到团队ID是空或为公司，玩家是：" + playerName);
      }

      // 是否显示支付按钮
      wanjia.setHasPayed(relatedTeamIdSet.contains(teamId) ? teamId : "");
      tableWanjiaInfoList.add(wanjia);
    }

    // 3填充
    table.setItems(tableTotalInfoList);
    table.refresh();
    fillTablePaiju(tablePaiju, tableWanjiaInfoList);// 添加需要支付的按钮靠前排

    // 缓存当局固定总和
    dataConstants.SumMap.putAll(getSums(tableTotalInfoList));
    // 填充当局表和交收表
    initTableDangjuAndTableJiaoshou(tableDangju, tableJiaoshou);
    // 填充团队表
    fillTableTeam(tableTeam, relatedTeamIdSet);
    // 填充个人表
    fillTablePersonal(tablePersonal, gameRecordModels);
    // 实时金额栏生成小游戏抬头
    fillTableSSJE_GAME();
    // 更新实时上码表的个人详情
    shangmaService.updateShangDetailMap(tablePaiju);
  }

  /**
   * 针对小游戏添加抬头
   */
  private void fillTableSSJE_GAME() {

    List<GameRecordModel> currentList = dataConstants.Dangju_Team_Huishui_List;
    if (CollectionUtils.isNotEmpty(currentList)) {
      // TODO 监视锁不能改变状态
      synchronized (changciController.ssjeMonitor){
        TableView<CurrentMoneyInfo> tableCurrentMoneyInfo = changciController.tableCurrentMoneyInfo;
        double littleGameHoutaiProfit = 0;
        double playerProfit = 0; // 当局客户利润，即客户输赢
        double gameBaoxian = 0; // 当局小游戏彩池合计
        for (GameRecordModel recordModel : currentList) {
          if (littleGameService.isLittleGame(recordModel)) {
            // 小游戏后台盈利：（原始战绩 - 保险） * 20 * （-1）
            double dif = NumUtil.getNum(recordModel.getYszj()) - NumUtil.getNum(recordModel.getSingleinsurance());
            littleGameHoutaiProfit +=
                dif * Constants.LITTLE_GAME_RATE_HOU_TAI * (-1);
            // 小游戏客户输赢
            playerProfit +=
                NumUtil.getNum(recordModel.getYszj());
            // 彩池合计 =
            gameBaoxian += NumUtil.getNum(recordModel.getSingleinsurance()) * (-1);
          }
        }
        String tableId = ZhanjiType.getInstance().getTableId();
        if (littleGameHoutaiProfit !=  0) {
          /************************************** 小游戏后台 **********************************************/
          String dangjuGameHoutaiProfit = NumUtil.digit(littleGameHoutaiProfit);
          String gameName = "小游戏后台";
          CurrentMoneyInfo infoByName = getInfoByName(gameName);
          if (infoByName == null) {
            infoByName = new CurrentMoneyInfo(gameName,
                NumUtil.digit(littleGameHoutaiProfit), "", "0",
                MoneyCreatorEnum.DEFAULT.getCreatorName(), "");
            // 若没有则新增一条
            log.info("实时金额栏不存在小游戏后台，新增记录，实时金额为：{}, 桌号是：{}", dangjuGameHoutaiProfit, tableId);
            changciController.tableCurrentMoneyInfo.getItems().add(infoByName);
          } else {
            String oldSSJE = infoByName.getShishiJine();
            String newSSJE = NumUtil.digit(NumUtil.getNum(oldSSJE) + littleGameHoutaiProfit);
            infoByName.setShishiJine(newSSJE);
            log.info("小游戏后台修改台=====旧金额：{}, 当局小游戏后台利润：{}，小游戏后台总利润：{}, 桌号是：{}",
                oldSSJE, dangjuGameHoutaiProfit, newSSJE, tableId);
          }
        }
        if (gameBaoxian !=  0) {
          /************************************** 小游戏彩池合计 **********************************************/
          String gameName = "小游戏彩池";
          CurrentMoneyInfo infoByName = getInfoByName(gameName);
          if (infoByName == null) {
            infoByName = new CurrentMoneyInfo(gameName,
                NumUtil.digit(gameBaoxian), "", "0",
                MoneyCreatorEnum.DEFAULT.getCreatorName(), "");
            // 若没有则新增一条
            log.info("实时金额栏不存在{}，新增记录，实时金额为：{}, 桌号是：{}", gameName, gameBaoxian, tableId);
            changciController.tableCurrentMoneyInfo.getItems().add(infoByName);
          } else {
            String oldSSJE = infoByName.getShishiJine();
            String newSSJE = NumUtil.digit(NumUtil.getNum(oldSSJE) + gameBaoxian);
            infoByName.setShishiJine(newSSJE);
            log.info("{}修改=====旧金额：{}, 当局小游戏后台利润：{}，小游戏后台总利润：{}, 桌号是：{}",
                gameName, oldSSJE, gameBaoxian, newSSJE, tableId);
          }
        }
        if (playerProfit != 0) {
          /************************************** 小游戏联盟对帐 **********************************************/
          String dangjuPlayerProfit = NumUtil.digit(playerProfit);
          String gameLMName = "小游戏联盟对帐";
          CurrentMoneyInfo playerSSJEInfo = getInfoByName(gameLMName);
          if (playerSSJEInfo == null) {
            playerSSJEInfo = new CurrentMoneyInfo(gameLMName,
                NumUtil.digit(dangjuPlayerProfit), "", "0",
                MoneyCreatorEnum.DEFAULT.getCreatorName(), "");
            // 若没有则新增一条
            log.info("实时金额栏不存在小游戏联盟对帐，新增记录，实时金额为：{}, 桌号是：{}", dangjuPlayerProfit, tableId);
            changciController.tableCurrentMoneyInfo.getItems().add(playerSSJEInfo);
          } else {
            String oldSSJE = playerSSJEInfo.getShishiJine();
            String newSSJE = NumUtil.digit(NumUtil.getNum(oldSSJE) + playerProfit);
            playerSSJEInfo.setShishiJine(newSSJE);
            log.info("游戏联盟对帐修改====旧金额：{}, 当局客户输赢：{}，小游戏总联盟对帐：{}, 桌号是：{}",
                oldSSJE, dangjuPlayerProfit, newSSJE, tableId);
          }
        }
        if (littleGameHoutaiProfit != 0 || playerProfit != 0 || gameBaoxian != 0) {
          // 刷新
          changciController.tableCurrentMoneyInfo.refresh();
        }
      }
    }



  }



  private WanjiaInfo getWanjiaInfo(GameRecordModel r, String playerId, String playerName,
      String shishou) {
    if (littleGameService.isLittleGame(r)) {
      return littleGameService.getWanjiaInfo0(r, playerId, playerName, shishou);
    }
    WanjiaInfo wanjia;
    wanjia = new WanjiaInfo();
    String lmb = getYicunJifen(playerId); // 联盟币代替已存积分
    String heji = digit0(NumUtil.getNum(lmb) + NumUtil.getNum(shishou) + "");
    wanjia.setPaiju(r.getTableid());
    wanjia.setWanjiaName(playerName);
    wanjia.setYicunJifen(lmb);
    wanjia.setZhangji(shishou);
    wanjia.setHeji(heji);
    wanjia.setWanjiaId(playerId);
    return wanjia;
  }

  private void setHsHbOfTotalInfo(TotalInfo info, GameRecordModel r) {
    String hshbType = r.getHshbType();
    if (StringUtils.equals(Constants.TEAM_OF_HSHB, hshbType)) {
      // 出回水
      info.setChuHuishui(r.getChuhuishui());
      // 回保
      info.setBaohui(r.getHuibao());
    } else {
      info.setChuHuishui(NumUtil.getNum(r.getPersonalHuishui()) * (-1) + "");
      info.setBaohui(r.getPersonalHuibao());
    }
  }

  private void fillTablePersonal(TableView<PersonalInfo> tablePersonal,
      List<GameRecordModel> gameRecordModels) {
    ObservableList<PersonalInfo> items = FXCollections.observableArrayList();
    // 从数据库获取当天未结算的个人记录
    List<GameRecord> dbPersonalRecords = gameRecordService
        .getPersonalRecords(changciController.getSoftDate(), myController.getClubId());
    for (GameRecordModel model : gameRecordModels) {
      if (StringUtils.equals(Constants.PERSONAL_OF_HSHB, model.getHshbType())
          && StringUtils.equals(Constants.PERSONAL_OF_UN_JIE_SUAN, model.getPersonalJiesuan())) {
        GameRecord entity = new GameRecord();
        BeanUtils.copyProperties(model, entity);
        dbPersonalRecords.add(entity);
      }
    }
    Map<String, List<GameRecord>> personalMap = dbPersonalRecords.stream()
        .collect(Collectors.groupingBy(GameRecord::getPlayerid));

    // 累计求和
    personalMap.forEach((playerId, personalList) -> {
      double sumOfZJ = 0.0;
      double sumOfHS = 0.0;
      double sumOfHB = 0.0;
      for (GameRecord entity : personalList) {
        sumOfZJ += NumUtil.getNum(entity.getShishou());
        sumOfHS += NumUtil.getNum(entity.getPersonalHuishui());
        sumOfHB += NumUtil.getNum(entity.getPersonalHuibao());
      }
      PersonalInfo info = new PersonalInfo(playerId,
          dataConstants.membersMap.get(playerId).getPlayername(),
          NumUtil.digit0(sumOfZJ),
          NumUtil.digit2(sumOfHS + ""),
          NumUtil.digit2(sumOfHB + ""),
          "");
      items.add(info);
    });

    // 刷新表
    tablePersonal.setItems(items);
    tablePersonal.refresh();

    // 缓存总和
    //dataConstants.SumMap.put("个人累计回水回保总和", getPersonHsHbSum());
  }

  private double getPersonHsHbSum() {
    double sumOfPersonalHsHb = 0d;
    for (PersonalInfo item : changciController.tablePersonal.getItems()) {
      sumOfPersonalHsHb += (NumUtil.getNum(item.getPersonalSumHS()) + NumUtil
          .getNum(item.getPersonalSumHB()));
    }
    return sumOfPersonalHsHb;
  }


  public void fillGameRecords(List<GameRecordModel> gameRecordModels, String tableId, String level,
      String LMType) {
    for (GameRecordModel r : gameRecordModels) {
      if (littleGameService.isLittleGame(r)) {
        // 小游戏战绩
        littleGameService.setSingleGameRecord0(r, tableId, level, LMType);

      } else {
        // 普通战绩
        setSingleGameRecord(r, tableId, level, LMType);
      }
    }
  }

  private void setSingleGameRecord(GameRecordModel r, String tableId, String level, String LMType) {

    String teamId = getTeamId(r.getPlayerid());
    // 计算收回险
    String yszj = r.getYszj();
    String baoxian = r.getSingleinsurance();
    String shishou = getShiShou(yszj);
    String chuHuishui = myController.getHuishuiByYSZJ(yszj, teamId, 1);
    String shuihouxian = getShuihouxian(baoxian);
    String shouHuishui = myController.getHuishuiByYSZJ(yszj, "", 2);
    String huiBao = NumUtil.digit1(getHuiBao(baoxian, teamId));

    // 初始名称
    r.setBeginplayername(r.getPlayerName());
    // 获取软件时间
    r.setSoftTime(dataConstants.Date_Str);
    // 设置桌号
    r.setTableid(tableId);
    // 设置联盟
    r.setLmtype(LMType);
    // 设置团队ID
    r.setTeamId(teamId);
    // 级别
    r.setLevel(level);
    // 导入时间
    r.setImporttime(TimeUtil.getDateTime());
    // 实收
    r.setShishou(shishou);
    // 出回水
    r.setChuhuishui(chuHuishui);
    // 回保
    r.setHuibao(huiBao);
    // 水后险
    r.setShuihouxian(shuihouxian);
    // 收回水
    r.setShouhuishui(shouHuishui);

    // 个人回水、回保
    setPersonalHsHb(r);

    // 合利润
    r.setHelirun(NumUtil.digit2(getHeLirun(r)));

    log.info("{}的保险是{}，计算出水后险是{}", r.getPlayerName(), baoxian, r.getShuihouxian());
  }


  public void setPersonalHsHb(GameRecordModel r) {
    r.setHshbType("0"); // 默认为团队类型
    r.setPersonalHuibao("0");
    r.setPersonalHuishui("0");
    String playerid = r.getPlayerid();
    Player player = dataConstants.membersMap.get(playerid);
    if (player != null) {
      double personalHBRate = getNumberFromUnknowStr(player.getHuibao());
      double personalHSRate = getNumberFromUnknowStr(player.getHuishui());
      // 其中一个大于0则进行个人回水回保的计算
      if (personalHBRate > 0 || personalHSRate > 0) {
        r.setHshbType("1"); // 个人类型
        r.setPersonalHuibao(getPersonalHB(r.getSingleinsurance(), personalHBRate));
        r.setPersonalHuishui(getPersonalHs(r.getShishou(), personalHSRate));
      }
    }
  }

  private String getPersonalHs(String shishou, double personalHSRate) {
    if (NumUtil.getNum(shishou) < 0) {
      return NumUtil.digit2(Math.abs(NumUtil.getNum(shishou)) * personalHSRate + "");
    }
    return "0";
  }

  private String getPersonalHB(String singleinsurance, double personalHBRate) {
    return NumUtil.digit2(NumUtil.getNum(singleinsurance) * personalHBRate * (-1) + "");
  }

  public double getNumberFromUnknowStr(String originVal) {
    if (StringUtils.isBlank(originVal)) {
      return 0d;
    }
    if (StringUtils.contains(originVal, "%")) {
      return NumUtil.getNumByPercent(originVal);
    }
    return NumUtil.getNum(originVal);
  }


  public String getHuiBao(String baoxian, String teamId) {
    if (StringUtil.isBlank(baoxian) || "0".equals(baoxian)) {
      return "0";
    }
    Huishui hs = dataConstants.huishuiMap.get(teamId);
    if (hs == null) {
      return "0";
    } else {
      String insuranceRate = hs.getInsuranceRate();
      insuranceRate = !StringUtil.isBlank(insuranceRate) ? insuranceRate : "0";
      return Double.valueOf(baoxian) * Double.valueOf(insuranceRate) * (-1) + "";
    }
  }

  public String getShuihouxian(String baoxian) {
    String shuiHouXian =
        NumUtil.digit1((-1) * Double.valueOf(baoxian) * Constants.CURRENT_HS_RATE + "");
    if ("-0.0".equals(shuiHouXian)) {
      shuiHouXian = "0";
    }
    return shuiHouXian;
  }

  public String getShiShou(String zhanji) {
    Double zj = Double.valueOf(zhanji);
    if (zj > 0) {
      String shishou = zj * Constants.CURRENT_HS_RATE + "";
      if (shishou.contains(".")) {
        shishou = shishou.substring(0, shishou.lastIndexOf("."));
      }
      return shishou;
    } else {
      return zhanji;
    }
  }

  public String getChuhuishui(String zhanji, String teamId) {
    Huishui hs = dataConstants.huishuiMap.get(teamId);
    if (hs == null) {
      return "";
    } else {
      Double hsRate = Double.valueOf(hs.getHuishuiRate());
      Double zj = Double.valueOf(zhanji);
      return Math.abs(zj * hsRate) * (-1) + "";
    }
  }

  public String getHeLirun(GameRecordModel r) {
    String shouhuishui = r.getShouhuishui();
    String shuihouxian = r.getShuihouxian();
    String chuhuishui = "";
    String huibao = "";
    // 团队类型
    boolean isHsHbTypeOfTeam = StringUtils.equals(Constants.TEAM_OF_HSHB, r.getHshbType());
    if (isHsHbTypeOfTeam) {
      chuhuishui = r.getChuhuishui();
      huibao = r.getHuibao();
    } else {
      chuhuishui = "-" + r.getPersonalHuishui(); // 取相反数
      huibao = r.getPersonalHuibao();
    }

    double _shouHushui = NumUtil.getNum(shouhuishui);
    if (_shouHushui >= 0) {
       String finalHelirun = _shouHushui + NumUtil.getNum(chuhuishui) + NumUtil.getNum(shuihouxian)
          - NumUtil.getNum(huibao) + "";
      return finalHelirun;
    } else {
      return "0";
    }
  }

  public String getTeamId(String playerId) {
    Player player = dataConstants.membersMap.get(playerId);
    String teamId = "";
    if (player != null) {
      teamId = player.getTeamid();
      teamId = StringUtil.isBlank(teamId) ? "" : teamId.toUpperCase();
    }
    // add 2017-10-26
    else {
      Player tempPlayer = playerService.get(playerId);
      if (tempPlayer != null && !StringUtil.isBlank(tempPlayer.getTeamid())) {
        dataConstants.membersMap.put(playerId, tempPlayer);
        teamId = tempPlayer.getTeamid();
        teamId = StringUtil.isBlank(teamId) ? "" : teamId.toUpperCase();
      }
    }
    return teamId;
  }


  public void fillTablePaiju(TableView<WanjiaInfo> tablePaiju,
      ObservableList<WanjiaInfo> tableWanjiaInfoList) {
    List<WanjiaInfo> linkedList = new LinkedList<>();
    List<WanjiaInfo> noNeedPayList = new ArrayList<>();

    if (tableWanjiaInfoList != null && tableWanjiaInfoList.size() > 0) {
      for (WanjiaInfo info : tableWanjiaInfoList) {
        linkedList.add(info);
      }

      ListIterator<WanjiaInfo> it = linkedList.listIterator();
      WanjiaInfo wj = null;
      while (it.hasNext()) {
        wj = it.next();
        // 在此处增加是否要显示该按钮(如果玩家从属于某个非空或非公司的团队，则无需显示按钮)
        String tempTeamId = wj.getHasPayed();// 这个tempTeamId是hasPayed的内容,这里没有公司的人
        if (!StringUtil.isBlank(tempTeamId) && !"0".equals(tempTeamId)) {
          // 获取团队信息
          Huishui hs = dataConstants.huishuiMap.get(tempTeamId);
          // 情况一：有从属团队的玩家，再分两种情况
          if (hs != null) {
            // A:若团队战绩要管理，需要显示支付按钮
            if ("是".equals(hs.getZjManaged())) {
              // log.info("====teamId为不为空，要显示，要战绩管理：是");
              // setGraphic(btn);
              // B:若团队战绩不要管理，无须显示支付按钮
            } else {
              noNeedPayList.add(wj);
              it.remove();
              // setGraphic(null);
            }
          }
        } else {
          // log.info("====teamId为空或为0，要显示+"+dataConstants.membersMap.get(wj.getWanjiaId()).getTeamName());
          // //情况二：对于没有从从属的团队的玩家或者团队是公司的玩家，一定需要需要显示支付按钮
          // setGraphic(btn);
        }
      }

      if (noNeedPayList.size() > 0) {
        for (WanjiaInfo info : noNeedPayList) {
          linkedList.add(info);
        }
      }

      if (noNeedPayList != null && noNeedPayList.size() > 0) {
        tableWanjiaInfoList.clear();
        for (WanjiaInfo info : linkedList) {
          tableWanjiaInfoList.add(info);
        }
      }
      // 刷新牌局表
      tablePaiju.setItems(tableWanjiaInfoList);
      tablePaiju.refresh();


    }
  }

  // 待返回的团队回水和团队保险总和
  // private double sum_teamHS_and_teamBS = 0.0;

  private void fillTableTeam(TableView<TeamInfo> table, Set<String> relatedTeamIdSet) {
    double sum_teamHS_and_teamBS = 0;
    // 准备数据
    ObservableList<TeamInfo> list = FXCollections.observableArrayList();
    relatedTeamIdSet = dataConstants.Team_Huishui_Map.keySet();// 这是后期增加：查看所有团队
    for (String relatedTeamId : relatedTeamIdSet) {

      // 非公司
      boolean noCompanyTeam = !StringUtils.equals(Constants.COMPANY, relatedTeamId);
      if (noCompanyTeam) {
        double sumOfZJ = 0.0;
        double sumOfHS = 0.0;
        double sumOfHB = 0.0;
        List<GameRecordModel> teamLS = dataConstants.Team_Huishui_Map
            .getOrDefault(relatedTeamId, new ArrayList<>());
        //只获取未结算的数据
        teamLS = teamLS.stream()
            .filter(e -> StringUtils.equals(Constants.TEAM_OF_HSHB, e.getHshbType()) &&
                StringUtils.equals(Constants.TEAM_OF_UN_JIE_SUAN, e.getIsjiesuaned())) // 新增条件
            .collect(Collectors.toList());
        // add 2019-04-12 过滤掉有个人回水的
        if (CollectionUtils.isEmpty(teamLS)) {
          continue;
        }
        for (GameRecordModel info : teamLS) {
          sumOfZJ += NumUtil.getNum(getTeamSumZhanji(info));
          sumOfHS += Math.abs(NumUtil.getNum(info.getChuhuishui()));
          if (!littleGameService.isJLBH(info)) { // 移除加勒比海回保累计
            sumOfHB += NumUtil.getNum(info.getSingleinsurance());// 就是回保
          }
        }
        double sum = 0d;
        if (sumOfHB != 0) {// 需要乘以团队保险比例的
          sumOfHB *= -getTeamInsuranceRate(relatedTeamId);
        }
        if (isZjManaged(relatedTeamId)) {// 战绩代管理,不加战绩(个人记录有支付按钮，个人处理战绩)
          sum = sumOfHS + sumOfHB;
        } else {
          sum = sumOfHS + sumOfHB + sumOfZJ;// 战绩不代管理,加战绩(个人记录没有支付按钮，团队处理战绩)
        }
        sum_teamHS_and_teamBS += sum;// 团队回水和团队保险的总和
        // 计算当局需要显示的团队记录
        list.add(new TeamInfo(relatedTeamId, digit0(sumOfZJ), NumUtil.digit1(sumOfHS + ""),
            NumUtil.digit1(sumOfHB + ""), digit0(sum)));
      }
    }
    // 缓存总和
    dataConstants.SumMap.put("团队回水及保险总和", sum_teamHS_and_teamBS);
    // 填充团队表
    table.setItems(list);
    table.refresh();
  }

  /**
   * 团队累计表中的团队总战绩
   * 一般返回实收
   * 小游戏返回原始战绩
   */
  private String getTeamSumZhanji(GameRecordModel model) {
    if (littleGameService.isLittleGame(model)) {
      return model.getYszj();
    }
    return model.getShishou();
  }

  // 获取团队表的总和
  public double getSumMapValue(ObservableList<TeamInfo> resultTeamList) {
    Double sum = 0.0;
    for (TeamInfo teamInfo : resultTeamList) {
      sum += NumUtil.getNum(teamInfo.getTeamSum());
    }
    return sum.intValue();
  }


  // 获取团队保险比例（回保）
  public double getTeamInsuranceRate(String teamId) {
    Huishui hs = dataConstants.huishuiMap.get(teamId);
    if (hs != null) {
      String rate = hs.getInsuranceRate();
      if (!StringUtil.isBlank(rate)) {
        return Double.valueOf(rate);
      }
    }
    return 1.0d;
  }

  private boolean isZjManaged(String teamId) {
    Huishui hs = dataConstants.huishuiMap.get(teamId);
    return hs != null && StringUtils.equals("是", hs.getZjManaged());
  }

  private void initTableDangjuAndTableJiaoshou(TableView<DangjuInfo> tableDangju,
      TableView<JiaoshouInfo> tableJiaoshou) {

    if (dataConstants.SumMap != null && dataConstants.SumMap.size() != 0) {
      // 初始化当局表
      ObservableList<DangjuInfo> list = FXCollections.observableArrayList();
      List<String> tableDangjuList = Arrays.asList("服务费", "保险", "团队回水", "团队回保", "小游戏当局利润");
      tableDangjuList.forEach(type -> {
        list.add(new DangjuInfo(type, dataConstants.SumMap.get(type) + ""));
      });
      tableDangju.setItems(list);
      // 初始化交收表
      ObservableList<JiaoshouInfo> list2 = FXCollections.observableArrayList();
      List<String> tableJiaoshouList = Arrays.asList("客户输赢", "收回水", "水后险");
      tableJiaoshouList.forEach(type -> {
        list2.add(new JiaoshouInfo(type, dataConstants.SumMap.get(type) + ""));
      });
      tableJiaoshou.setItems(list2);
    }
  }

  private String getShiShouDif(String playerId){
    for (GameRecordModel recordModel : dataConstants.Dangju_Team_Huishui_List) {
      if (StringUtils.equals(recordModel.getPlayerid(), playerId)) {
        if (littleGameService.isLittleGame(recordModel)) {
          return recordModel.getYszj(); // 小游戏返回原始战绩
        } else {
          return recordModel.getShishou();
        }
      }
    }
    return "0";
  }

  public Map<String, Double> getSums(ObservableList<TotalInfo> lists) {
    List<String> jifenList = new ArrayList<>();
    List<String> shishouList = new ArrayList<>();
    List<String> baoxianList = new ArrayList<>();
    List<String> chuhuishuiList = new ArrayList<>();
    List<String> baohuiList = new ArrayList<>();
    List<String> shuihouxianList = new ArrayList<>();
    List<String> shouHuishuiList = new ArrayList<>();
    List<String> heLirunList = new ArrayList<>();
    List<String> littleGameShishouList = new ArrayList<>();

    lists.forEach(info -> {
      jifenList.add(info.getJifen());
      //shishouList.add(info.getShishou());
      shishouList.add(getShiShouDif(info.getWanjiaId()));
      baoxianList.add(info.getBaoxian());
      chuhuishuiList.add(info.getChuHuishui());
      baohuiList.add(info.getBaohui());
      shuihouxianList.add(info.getShuihouxian());
      shouHuishuiList.add(info.getShouHuishui());
      heLirunList.add(info.getHeLirun());
      if (StringUtils.equals("1", info.getIsLittleGame())) {
        littleGameShishouList.add(info.getShishou());
      }
    });

    Map<String, Double> cacheMap = new HashMap<>();
    // 备份数据
    cacheMap.put("总计分", getSum(jifenList));
    cacheMap.put("总实收", getSum(shishouList));
    cacheMap.put("总保险", getSum(baoxianList));
    cacheMap.put("总出回水", getSum(chuhuishuiList));
    cacheMap.put("总保回", getSum(baohuiList));
    cacheMap.put("总水后险", getSum(shuihouxianList));
    cacheMap.put("总收回水", getSum(shouHuishuiList));
    cacheMap.put("总合利润", getSum(heLirunList));
    // 当局表
    cacheMap.put("服务费", cacheMap.get("总收回水"));
    cacheMap.put("保险", cacheMap.get("总水后险"));
    cacheMap.put("团队回水", cacheMap.get("总出回水"));
    cacheMap.put("团队回保", cacheMap.get("总保回") * (-1));
    cacheMap.put("小游戏当局利润", getSum(littleGameShishouList));
    double dj = Double.sum(cacheMap.get("服务费"), cacheMap.get("保险"))
        + Double.sum(cacheMap.get("团队回水"), cacheMap.get("团队回保"))
        + cacheMap.get("小游戏当局利润");
    cacheMap.put("当局", dj);// 总当局
    // 交收表
    cacheMap.put("客户输赢", cacheMap.get("总实收"));
    cacheMap.put("收回水", cacheMap.get("总收回水"));
    cacheMap.put("水后险", cacheMap.get("总水后险"));
    cacheMap.put("交收", cacheMap.get("客户输赢") + cacheMap.get("收回水") + cacheMap.get("水后险"));// 总

    return cacheMap;
  }

  public Double getSum(List<String> list) {
    double sum = 0.0;
    for (String value : list) {
      sum += NumUtil.getNum(value);
    }
    return Double.valueOf(df.format(sum));
  }


  public String getYicunJifen(String playerId) {
    // 从当前实时金额表中获取最新的已存积分（波哥要求）
    ObservableList<CurrentMoneyInfo> list = tableCurrentMoneyInfo.getItems();
    if (list != null) {
      for (CurrentMoneyInfo info : list) {
        if (StringUtils.equals(playerId, info.getWanjiaId())) {
          return StringUtil.nvl(info.getCmiLmb(), "0");
        }
      }
    }
    return "";
  }

  public String digit0(String str) {
    return NumUtil.digit0(str);
  }

  public String digit0(double num) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("#######0");
    String res = df.format(Double.valueOf(num));
    return res;
  }
  // *****************************************************************************

  public void fillTableCurrentMoneyInfo(TableView<CurrentMoneyInfo> table,
      TableView<ZijinInfo> tableZijin, TableView<ProfitInfo> tableProfit,
      TableView<KaixiaoInfo> tableKaixiao, Label LMLabel) {

    Map<String, String> map = dataConstants.preDataMap;
    /************************************************ 实时金额表 **************/
    // 1清空表数据
    table.setItems(null);
    // 2匹配ID和额度
    ObservableList<CurrentMoneyInfo> observableList1 = FXCollections.observableArrayList();
    String name = "实时金额";
    Map<String, String> jineMap =
        JSON.parseObject(map.get(name), new TypeReference<Map<String, String>>() {
        });
    final String FLAG = "###";
    jineMap.forEach((mingziAndID, cmiJson) -> { // mingziAndID 之前是mingzi
//      String mingzi = "";
//      String playerId = "";
//      if (mingziAndID.contains(FLAG)) {
//        mingzi = mingziAndID.split(FLAG)[0];
//        playerId = mingziAndID.split(FLAG)[1];
//        playerId = playerId.replace("#", "");// playerId中含有#号，则删除#号（不严谨）
//      } else {
//        mingzi = mingziAndID;
//      }
//      Player player = dataConstants.membersMap.get(playerId);// add 现在根据玩家去匹配
//      CurrentMoneyInfo cmi;
//      if (player == null || StringUtil.isBlank(player.getPlayerid())) {
//        // 实时金额中的人名找不到对应的玩家ID
//        cmi = new CurrentMoneyInfo(mingzi, cmiJson, "", "",
//            MoneyCreatorEnum.DEFAULT.getCreatorName(), "");
//      } else {
//        cmi = new CurrentMoneyInfo(mingzi, cmiJson, player.getPlayerid(), player.getEdu(),
//            MoneyCreatorEnum.DEFAULT.getCreatorName(), "");
//      }
      try {
        CurrentMoneyInfo cmi = JSON.parseObject(cmiJson, CurrentMoneyInfo.class);
        if (cmi != null) {
          observableList1.add(cmi);
        }else {
          log.info("导入昨日表后cmi为空！cmiJson是：" + cmiJson);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    table.setItems(observableList1);
    table.refresh();
    // 刷新合并ID操作
    flush_SSJE_table();

    // 加载其他几个表
    fillTables(tableZijin, tableProfit, tableKaixiao, LMLabel, map);
  }

  public void fillTables(TableView<ZijinInfo> tableZijin, TableView<ProfitInfo> tableProfit,
      TableView<KaixiaoInfo> tableKaixiao, Label LMLabel, Map<String, String> map) {
    /************************************************ 资金表 **************/
    // 1清空 表数据
    tableZijin.setItems(null);
    // 2获取InfoList
    ObservableList<ZijinInfo> observableList2 = FXCollections.observableArrayList();
    Map<String, String> zijinMap =
        JSON.parseObject(map.get("资金"), new TypeReference<Map<String, String>>() {
        });
    zijinMap.forEach((type, account) -> {
      observableList2.add(new ZijinInfo(type, account));
    });
    tableZijin.setItems(observableList2);
    /************************************************ 利润表 **************/
    // 1清空 表数据
    tableProfit.setItems(null);
    // 2获取InfoList
    ObservableList<ProfitInfo> observableList3 = FXCollections.observableArrayList();
    Map<String, String> profitMap =
        JSON.parseObject(map.get("昨日利润"), new TypeReference<Map<String, String>>() {
        });
    profitMap.forEach((type, account) -> {
      observableList3.add(new ProfitInfo(type, digit0(account)));
    });
    tableProfit.setItems(observableList3);
    /************************************************ 实时开销表 **************/
    tableKaixiao.setItems(null);
    // 2获取InfoList
    ObservableList<KaixiaoInfo> observableList4 = FXCollections.observableArrayList();
    Map<String, String> kaixiaoMap =
        JSON.parseObject(map.get("实时开销"), new TypeReference<Map<String, String>>() {
        });
    kaixiaoMap.forEach((type, account) -> {
      observableList4.add(new KaixiaoInfo(type, digit0(account)));
    });
    tableKaixiao.setItems(observableList4);
    // 缓存一一场的实时开销总和
    dataConstants.SumMap.put("上场开销", getSumOfTableKaixiao(tableKaixiao));
    /************************************************ 联盟对帐 **************/
    LMLabel.setText(LMLabel.getText());// 注意这里是{联盟对帐={联盟对帐=3000}}
  }

  // 模拟Oracle的nvl函数
  public String nvl(String condition, String ifNullStr) {
    if (!StringUtil.isBlank(condition)) {
      return condition.trim();
    } else {
      return ifNullStr;
    }
  }

  private void updatetTableProfitFirst(TableView<ProfitInfo> tableProfit) {
    /************************************************ 利润表 **************/
    // 1清空 表数据
    tableProfit.setItems(null);
    // 2获取InfoList
    ObservableList<ProfitInfo> observableList3 = FXCollections.observableArrayList();
    Map<String, String> profitMap = null;
    if (dataConstants.Index_Table_Id_Map.size() == 0) {
      log.info("=========================刷新（从昨天加载数据）");
      if (dbUtil.isPreData2017VeryFirst()) {
        profitMap = JSON.parseObject(dataConstants.preDataMap.get("昨日利润"),
            new TypeReference<Map<String, String>>() {
            });
        if (profitMap != null) {
          profitMap.forEach((type, account) -> {
            observableList3.add(new ProfitInfo(type, digit0(account)));
          });
        }
      } else {
        List<ProfitInfo> list = JSON.parseObject(dataConstants.preDataMap.get("利润"),
            new TypeReference<List<ProfitInfo>>() {
            });
        if (list != null) {
          list.forEach(info -> {
            observableList3
                .add(new ProfitInfo(info.getProfitType(), digit0(info.getProfitAccount())));
          });
        }
      }

    } else {
      log.info("=========================刷新（从上一场加载数据）");
      Map<String, String> map =
          dataConstants.All_Locked_Data_Map.get(dataConstants.Index_Table_Id_Map.size() + "");
      List<ProfitInfo> ProfitInfoList = JSON.parseObject(this.getJsonString(map, "利润"),
          new TypeReference<List<ProfitInfo>>() {
          });
      for (ProfitInfo infos : ProfitInfoList) {
        observableList3.add(infos);
      }

      // sumMap相关设值
      int size = dataConstants.Index_Table_Id_Map.size();
      if (size >= 1) {
        // 此情况下要从上一场加载==团队回水总和
        String shangchangKaixiao = this.getLockedInfo(size + "", "实时开销总和");
        dataConstants.SumMap.put("上场开销", Double.valueOf(shangchangKaixiao));// add 9-1
      }
    }
    tableProfit.setItems(observableList3);
    tableProfit.refresh();

  }


  private Double getSumOfTableCurrentMoney(TableView<CurrentMoneyInfo> table) {
    // 获取ObserableList
    ObservableList<CurrentMoneyInfo> list = table.getItems();
    Double sumOfTableCurrentMoney = 0.0;
    if (list == null) {
      return sumOfTableCurrentMoney;
    }
    String tempSingleVal = "";// 实时金额表中每一行的具体金额
    for (CurrentMoneyInfo moneyInfo : list) {
      tempSingleVal = moneyInfo.getShishiJine();
      if (StringUtil.isAllNotBlank(tempSingleVal, moneyInfo.getMingzi())) {
        sumOfTableCurrentMoney +=
            NumUtil.getNum(tempSingleVal) + NumUtil.getNum(moneyInfo.getCmiLmb());
      }
    }
    return sumOfTableCurrentMoney;
  }

  private Double getSumOfTableZijin(TableView<ZijinInfo> table) {
    // 获取ObserableList
    ObservableList<ZijinInfo> list = table.getItems();
    Double sumOfTable = 0.0;
    if (list == null) {
      return sumOfTable;
    }
    String tempSingleVal = "";// 表中每一行的具体金额
    for (ZijinInfo moneyInfo : list) {
      tempSingleVal = moneyInfo.getZijinAccount();
      sumOfTable += NumUtil.getNum(tempSingleVal);
    }
    return sumOfTable;
  }

  private Double getSumOfTableProfit(TableView<ProfitInfo> table) {
    // 获取ObserableList
    ObservableList<ProfitInfo> list = table.getItems();
    Double sumOfTable = 0.0;
    String tempSingleVal = "";// 表中每一行的具体金额
    for (ProfitInfo moneyInfo : list) {
      if (!"当日开销".endsWith(moneyInfo.getProfitType())) {
        tempSingleVal = moneyInfo.getProfitAccount();
        sumOfTable += NumUtil.getNum(tempSingleVal);
      }
    }
    return sumOfTable;
  }

  private Double getSumOfTableKaixiao(TableView<KaixiaoInfo> table) {
    Double sumOfTable = 0.0;
    // 获取ObserableList
    ObservableList<KaixiaoInfo> list = table.getItems();
    if (list == null) {
      return sumOfTable;
    }
    String tempSingleVal = "";// 表中每一行的具体金额
    for (KaixiaoInfo moneyInfo : list) {
      tempSingleVal = moneyInfo.getKaixiaoMoney();
      sumOfTable += NumUtil.getNum(tempSingleVal);
    }
    return sumOfTable;
  }

  private Double getSumOfTablePingzhang(TableView<PingzhangInfo> table) {
    // 获取ObserableList
    ObservableList<PingzhangInfo> list = table.getItems();
    Double sumOfTable = 0.0;
    if (list == null) {
      return sumOfTable;
    }
    if (list.size() == 2) {
      sumOfTable = NumUtil.getNum(list.get(1).getPingzhangMoney())
          - NumUtil.getNum(list.get(0).getPingzhangMoney());
    }
    return sumOfTable;
  }

  public void setTotalNumOnTable(TableView<? extends Entity> table, double sum) {
    table.getColumns().get(1).setText(digit0(sum + ""));
  }

  public void setTotalNumOnTable(TableView<? extends Entity> table, double sum,
      int sumCloumn) {
    table.getColumns().get(sumCloumn).setText(digit0(sum + ""));
  }


  public void add2AllTeamFWF_from_tableProfit(TableView<ProfitInfo> table, Double teamFWF) {
    try {
      ProfitInfo profitInfo =
          TableUtil.getItem(table).filtered(info -> "总团队服务费".equals(info.getProfitType())).get(0);
      String allTeamFWF = NumUtil.digit0(NumUtil.getNum(profitInfo.getProfitAccount()) + teamFWF);
      profitInfo.setProfitAccount(allTeamFWF);
      table.refresh();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void refreshSumPane(TableView<TeamInfo> tableTeam, TableView<ZijinInfo> tableZijin,
      TableView<KaixiaoInfo> tableKaixiao, TableView<ProfitInfo> tableProfit,
      TableView<CurrentMoneyInfo> tableCurrentMoney, TableView<PingzhangInfo> tablePingzhang,
      Label LMLabel) {
    // log.info(JSON.toJSONString(dataConstants.SumMap));
    // 0 重新初始化利润表(避免重复计算)
    // String allTeamFWF = getAllTeamFWF(tableProfit);//缓存总团队服务费

    updatetTableProfitFirst(tableProfit);
    add2AllTeamFWF_from_tableProfit(tableProfit,
        changciController.current_Jiesuaned_team_fwf_sum);// 修改总团队服务费表

    // 1 计算开销总和
    double sumOfKaixiao = getSumOfTableKaixiao(tableKaixiao);
    dataConstants.SumMap.put("开销", sumOfKaixiao);
    setTotalNumOnTable(tableKaixiao, sumOfKaixiao);

    // 2 更新利润表
    updateTableProfit(tableProfit, sumOfKaixiao);

    // add 2018-02-25
    clearTableProfit(tableProfit);
    // 3 计算利润表总和
    double sumOfProfit = getSumOfTableProfit(tableProfit);
    dataConstants.SumMap.put("利润", sumOfProfit);
    setTotalNumOnTable(tableProfit, sumOfProfit);

    // 4 计算资金表总和
    double sumOfZijin = getSumOfTableZijin(tableZijin);
    dataConstants.SumMap.put("资金", sumOfZijin);
    setTotalNumOnTable(tableZijin, sumOfZijin);

    // 5 计算实时金额表总和
    double sumOfCurrentMoney = getSumOfTableCurrentMoney(tableCurrentMoney);
    dataConstants.SumMap.put("实时金额", sumOfCurrentMoney);
    setTotalNumOnTable(tableCurrentMoney, sumOfCurrentMoney, 2);

    // 6 更新平帐表
    updateTablePingzhang(tablePingzhang, sumOfZijin, sumOfProfit, sumOfCurrentMoney, LMLabel);
    double sumOfPingzhang = getSumOfTablePingzhang(tablePingzhang);
    dataConstants.SumMap.put("平帐", sumOfPingzhang);
    setTotalNumOnTable(tablePingzhang, sumOfPingzhang);

    // 7更新团队回水表（若果之前有点击结算按钮）
    double sumOfTeamHS = getSumMapValue("团队回水及保险总和");// dataConstants.SumMap.get("团队回水及保险总和");
    setTotalNumOnTable(tableTeam, sumOfTeamHS, 4);

  }

  public void clearTableProfit(TableView<ProfitInfo> tableProfit) {
    // 清空利润表
    if (gdController.has_quotar_oneKey) {
      tableProfit.getItems().forEach(info -> info.setProfitAccount("0"));
      ShowUtil.show("已经点击过贡献值中的一键分配，清空利润栏！", 2);
    }
  }

  public void updateTableProfit(TableView<ProfitInfo> table, double todayKaixiao) {
    // 获取ObserableList
    try {
      ObservableList<ProfitInfo> list = table.getItems();
      for (ProfitInfo info : list) {
        String type = info.getProfitType();
        if ("总服务费".equals(type)) {
          info.setProfitAccount(
              digit0(NumUtil.getNum(info.getProfitAccount()) + getSumMapValue("服务费")));

        } else if ("总保险".endsWith(type)) {
          info.setProfitAccount(
              digit0(NumUtil.getNum(info.getProfitAccount()) + getSumMapValue("保险")));

        } else if ("总回水".endsWith(type)) {
          info.setProfitAccount(
              digit0(NumUtil.getNum(info.getProfitAccount()) + getSumMapValue("团队回水")));

        } else if ("总开销".endsWith(type)) {
          info.setProfitAccount(digit0(
              (NumUtil.getNum(info.getProfitAccount()) + todayKaixiao - getSumMapValue("上场开销"))
                  + ""));

        } else if ("总回保".endsWith(type)) {
          // 无公式 总保回
          info.setProfitAccount(
              digit0(
                  NumUtil.getNum(info.getProfitAccount()) + (getSumMapValue("总保回") * (-1)) + ""));

        } else if ("小游戏利润".endsWith(type)) {
          info.setProfitAccount(
              digit0(
                  NumUtil.getNum(info.getProfitAccount()) + getSumMapValue("小游戏当局利润")));
        }
      }
      table.setItems(list);
    } catch (Exception e) {
      log.error("检测到更新利润表出错，可能是没有导入数据就进行平帐和锁定");
    }
  }

  public void updateTablePingzhang(TableView<PingzhangInfo> table, Double sumOfZijin,
      Double sumOfProfit, Double sumOfCurrentMoney, Label LMVal) {
    // 获取ObserableList
    ObservableList<PingzhangInfo> list = FXCollections.observableArrayList();
    Double _LMVal = 0d;
    try {
      _LMVal = Double.valueOf(LMVal.getText());
    } catch (Exception e) {
      _LMVal = 0d;
    }
    // 获取个人累计回水回保
    double personHsHbSum = getPersonHsHbSum();
    double sumTeamHsHb = getSumMapValue("团队回水及保险总和");
    list.add(new PingzhangInfo("外债+利润+团队回水+联盟对帐+个人回水",
        digit0(sumOfProfit + sumOfCurrentMoney + sumTeamHsHb + _LMVal
            + personHsHbSum
            + "")));// Double.valueOf(LMVal.getText())
    list.add(new PingzhangInfo("资金", digit0(sumOfZijin + "")));
    table.setItems(list);
  }


  public double getSumMapValue(String name) {
    if (dataConstants.SumMap.get(name) == null) {
      return 0d;
    } else {
      return dataConstants.SumMap.get(name);
    }
  }

  private Integer getNextSelectedIndex(TableView<CurrentMoneyInfo> table,
      String searchText) {
    List<Integer> indexList = new LinkedList<>();
    if (!StringUtil.isBlank(searchText)) {
      if (table == null || table.getItems() == null) {
        return -1;
      }
      ObservableList<CurrentMoneyInfo> list = table.getItems();
      String tempName = "";
      String ssje = "";
      int selectedIndex = table.getSelectionModel().getSelectedIndex();
      for (CurrentMoneyInfo moneyInfo : list) {
        tempName = moneyInfo.getMingzi();
        ssje = moneyInfo.getShishiJine();
        if (StringUtil.isNotBlank(tempName)) {
          if (tempName.contains(searchText.trim())
              || tempName.toLowerCase().contains(searchText.trim().toLowerCase())
              || tempName.toUpperCase().contains(searchText.trim().toUpperCase())
              || ssje.equals(searchText)
          ) {
            int index = list.indexOf(moneyInfo);
            indexList.add(index);
          }
        }
      }
      int size = indexList.size();
      // 返回排序序号
      if (size == 0) {
        return -1;
      } else if (size == 1) {
        // 返回第一个
        return indexList.get(0);
      } else {
        if (indexList.contains(selectedIndex)) {
          int i = indexList.indexOf(selectedIndex);
          if (i == (size - 1)) {
            // 返回第一个
            return indexList.get(0);
          } else {
            // 返回下一个
            return indexList.get(i + 1);
          }
        } else {
          // 返回第一个
          return indexList.get(0);
        }
      }
    }
    return -1;
  }


  public void searchRowAction(TableView<CurrentMoneyInfo> table, String searchText) {
    int nextIndex = getNextSelectedIndex(table, searchText);
    if (-1 == nextIndex) {
      ShowUtil.show("找不到结果！", 1);
    } else {
      table.scrollTo(nextIndex);
      table.getSelectionModel().select(nextIndex);
    }
  }

  public CurrentMoneyInfo searchRowByPlayerId(String playerId) {
    if (StringUtils.isNotBlank(playerId)) {
      for (CurrentMoneyInfo item : changciController.tableCurrentMoneyInfo.getItems()) {
        if (StringUtils.equals(playerId, item.getWanjiaId())) {
          return item;
        }
      }
    }
    return null;
  }

  public boolean changeYicunJifen(TableView<WanjiaInfo> tablePaiju, String playerName,
      String newYicunJifen) {
    boolean isOK = false;
    if (!StringUtil.isBlank(newYicunJifen)) {
      try {
        Double.valueOf(newYicunJifen);
      } catch (Exception e) {
        ShowUtil.show(newYicunJifen + "不是数值！！");
      }
    }
    ObservableList<WanjiaInfo> wanjiaList = tablePaiju.getItems();
    if (wanjiaList != null && wanjiaList.size() > 0) {
      boolean isInPaiju = false;
      for (WanjiaInfo info : wanjiaList) {
        if (info.getWanjiaName().equals(playerName)) {
          if ("1".equals(info.getHasPayed())) {
            ShowUtil.show("抱歉，已支付过，请到下一场修改", 2);
          } else {
            String zj = info.getZhangji();
            info.setYicunJifen(newYicunJifen);
            info.setHeji(digit0(Double.sum(Double.valueOf(zj), Double.valueOf(newYicunJifen))));
            ShowUtil.show("已经更新到已存积分", 2);
            tablePaiju.setItems(wanjiaList);
            isOK = true;
            break;
          }
          isInPaiju = true;
        }
      }
      if (!isInPaiju) {// 如果没有找到供修改的记录，也要让其通过
        isOK = true;
      }
    } else {
      isOK = true;
    }
    return isOK;
  }


  public Map<String, String> lock10Tables(TableView<TotalInfo> tableTotal,
      TableView<WanjiaInfo> tableWanjia, TableView<TeamInfo> tableTeam,
      TableView<CurrentMoneyInfo> tableCurrentMoney, TableView<ZijinInfo> tableZijin,
      TableView<KaixiaoInfo> tableKaixiao, TableView<ProfitInfo> tableProfit,
      TableView<DangjuInfo> tableDangju, TableView<JiaoshouInfo> tableJiaoshou,
      TableView<PersonalInfo> tablePersonal,
      TableView<PingzhangInfo> tablePingzhang, Label LMLabel) {
    /************************************************ 战绩表 **************/
    ObservableList<TotalInfo> TotalInfoObservableList = tableTotal.getItems();
    List<TotalInfo> TotalInfoList = new LinkedList<>();
    for (TotalInfo info : TotalInfoObservableList) {
      TotalInfoList.add(info);
    }

    /************************************************ 玩家表 **************/
    ObservableList<WanjiaInfo> WanjiaInfoObservableList = tableWanjia.getItems();
    if (WanjiaInfoObservableList == null) {
      WanjiaInfoObservableList = FXCollections.observableArrayList();
    }
    List<WanjiaInfo> WanjiaInfoList = new LinkedList<>();
    for (WanjiaInfo info : WanjiaInfoObservableList) {
      WanjiaInfoList.add(info);
    }

    /************************************************ 团队回水表 **************/
    ObservableList<TeamInfo> TeamInfoInfoObservableList = tableTeam.getItems();
    List<TeamInfo> TeamInfoList = new LinkedList<>();
    for (TeamInfo info : TeamInfoInfoObservableList) {
      String hasJiesuaned = info.getHasJiesuaned();// 对于已结算过的不再记录
      if ("".equals(hasJiesuaned) || "0".equals(hasJiesuaned)) {
        TeamInfoList.add(info);
      }
    }
    String sumOfTeam = tableTeam.getColumns().get(4).getText();

    /************************************************ 个人累计表 **************/
    ObservableList<PersonalInfo> personalItems = tablePersonal.getItems();
    List<PersonalInfo> listPersonalInfo = new LinkedList<>();
    for (PersonalInfo info : personalItems) {
      listPersonalInfo.add(info);
    }

    /************************************************ 实时金额表 **************/
    ObservableList<CurrentMoneyInfo> ObservableList = tableCurrentMoney.getItems();
    List<CurrentMoneyInfo> list = new LinkedList<>();
    for (CurrentMoneyInfo info : ObservableList) {
      list.add(info);
    }
    String sumOfCurrentMoney = tableCurrentMoney.getColumns().get(2).getText();
    /************************************************ 资金表 **************/
    ObservableList<ZijinInfo> ZijinInfoObservableList = tableZijin.getItems();
    List<ZijinInfo> listZijinInfo = new LinkedList<>();
    for (ZijinInfo info : ZijinInfoObservableList) {
      listZijinInfo.add(info);
    }
    String sumOfZijin = tableZijin.getColumns().get(1).getText();
    /************************************************ 利润表 **************/
    ObservableList<ProfitInfo> ProfitInfoObservableList = tableProfit.getItems();
    List<ProfitInfo> listProfitInfo = new LinkedList<>();
    for (ProfitInfo info : ProfitInfoObservableList) {
      listProfitInfo.add(info);
    }
    String sumOfProfit = tableProfit.getColumns().get(1).getText();
    /************************************************ 实时开销表 **************/
    ObservableList<KaixiaoInfo> KaixiaoInfoObservableList = tableKaixiao.getItems();
    List<KaixiaoInfo> listKaixiaoInfo = new LinkedList<>();
    for (KaixiaoInfo info : KaixiaoInfoObservableList) {
      log.info(info.getKaixiaoType());
      listKaixiaoInfo.add(info);
    }
    String sumOfKaixiao = tableKaixiao.getColumns().get(1).getText();
    /************************************************ 当局表 **************/
    ObservableList<DangjuInfo> DangjuInfoObservableList = tableDangju.getItems();
    List<DangjuInfo> listDangjuInfo = new LinkedList<>();
    for (DangjuInfo info : DangjuInfoObservableList) {
      listDangjuInfo.add(info);
    }
    String sumOfDangjuInfo = tableDangju.getColumns().get(1).getText();
    /************************************************ 交收表 **************/
    ObservableList<JiaoshouInfo> JiaoshouInfoObservableList = tableJiaoshou.getItems();
    List<JiaoshouInfo> listJiaoshouInfo = new LinkedList<>();
    for (JiaoshouInfo info : JiaoshouInfoObservableList) {
      listJiaoshouInfo.add(info);
    }
    String sumOfJiaoshouInfo = tableJiaoshou.getColumns().get(1).getText();
    /************************************************ 平帐表 **************/
    ObservableList<PingzhangInfo> PingzhangInfoObservableList = tablePingzhang.getItems();
    List<PingzhangInfo> listPingzhangInfo = new LinkedList<>();
    for (PingzhangInfo info : PingzhangInfoObservableList) {
      listPingzhangInfo.add(info);
    }
    String sumOfPingzhangInfo = tablePingzhang.getColumns().get(1).getText();
    // 汇总信息
    Map<String, String> map = new HashMap<>();
    map.put("战绩", JSON.toJSONString(TotalInfoList));
    map.put("玩家", JSON.toJSONString(WanjiaInfoList));
    map.put("团队回水", JSON.toJSONString(TeamInfoList));
    map.put("团队回水总和", sumOfTeam);
    map.put("实时金额", JSON.toJSONString(list));
    map.put("实时金额总和", sumOfCurrentMoney);
    map.put("资金", JSON.toJSONString(listZijinInfo));
    map.put("资金总和", sumOfZijin);
    map.put("利润", JSON.toJSONString(listProfitInfo));
    map.put("利润总和", sumOfProfit);
    map.put("实时开销", JSON.toJSONString(listKaixiaoInfo));
    map.put("实时开销总和", sumOfKaixiao);
    map.put("当局", JSON.toJSONString(listDangjuInfo));
    map.put("当局总和", sumOfDangjuInfo);
    map.put("交收", JSON.toJSONString(listJiaoshouInfo));
    map.put("交收总和", sumOfJiaoshouInfo);
    map.put("平帐", JSON.toJSONString(listPingzhangInfo));
    map.put("平帐总和", sumOfPingzhangInfo);
    map.put("联盟对帐", getLMLabelText(LMLabel));
    map.put("个人累计", JSON.toJSONString(listPersonalInfo));

    return map;
  }

  public String getLMLabelText(Label LMLabel) {
    String LMVal = LMLabel.getText();
    if ("0.00".equals(LMVal)) {
      LMLabel.setText(Constants.ZERO);
      return Constants.ZERO;
    }
    return LMVal;

  }

  public String getLockedInfo(String keyOfJu, String subKey) {
    String value = "";// 可能是JSONString,也可能是普通字符串
    if (dataConstants.Index_Table_Id_Map.size() != 0) {
      Map<String, String> map = dataConstants.All_Locked_Data_Map.get(keyOfJu);
      if (map != null) {
        value = map.get(subKey);
        value = value == null ? "" : value;
      }
    }
    return value;
  }

  public void reCovery10TablesByPage(TableView<TotalInfo> tableTotal,
      TableView<WanjiaInfo> tableWanjia, TableView<TeamInfo> tableTeam,
      TableView<CurrentMoneyInfo> tableCurrentMoney, TableView<ZijinInfo> tableZijin,
      TableView<KaixiaoInfo> tableKaixiao, TableView<ProfitInfo> tableProfit,
      TableView<DangjuInfo> tableDangju, TableView<JiaoshouInfo> tableJiaoshou,
      TableView<PingzhangInfo> tablePingzhang,
      TableView<PersonalInfo> tablePersonal, Label LMLabel,
      int pageIndex) {

    // 获取该页所有数据
    Map<String, String> map = dataConstants.All_Locked_Data_Map.get(pageIndex + "");
    /********************************************************/
    // List<TotalInfo> TotalInfoList = JSON.parseObject(map.get("战绩"), new
    // TypeReference<List<TotalInfo>>() {});
    List<TotalInfo> TotalInfoList =
        JSON.parseObject(getJsonString(map, "战绩"), new TypeReference<List<TotalInfo>>() {
        });
    ObservableList<TotalInfo> obList = FXCollections.observableArrayList();
    for (TotalInfo infos : TotalInfoList) {
      obList.add(infos);
    }
    tableTotal.setItems(obList);
    tableTotal.refresh();
    /************************************************************/
    List<WanjiaInfo> WanjiaInfoList =
        JSON.parseObject(getJsonString(map, "玩家"), new TypeReference<List<WanjiaInfo>>() {
        });
    ObservableList<WanjiaInfo> WanjiaInfo_OB_List = FXCollections.observableArrayList();
    for (WanjiaInfo infos : WanjiaInfoList) {
      WanjiaInfo_OB_List.add(infos);
    }
    tableWanjia.setItems(WanjiaInfo_OB_List);
    tableWanjia.refresh();

    /****************************************************************************/
    List<TeamInfo> TeamInfoList =
        JSON.parseObject(getJsonString(map, "团队回水"), new TypeReference<List<TeamInfo>>() {
        });
    ObservableList<TeamInfo> TeamInfo_OB_List = FXCollections.observableArrayList();
    for (TeamInfo infos : TeamInfoList) {
      TeamInfo_OB_List.add(infos);
    }
    tableTeam.setItems(TeamInfo_OB_List);
    tableTeam.getColumns().get(4).setText(getJsonString(map, "团队回水总和"));
    tableTeam.refresh();

    /****************************************************************************/
    List<PersonalInfo> PersonalInfoList =
        JSON.parseObject(getJsonString(map, "个人累计"), new TypeReference<List<PersonalInfo>>() {
        });
    ObservableList<PersonalInfo> PersonalInfo_OB_List = FXCollections.observableArrayList();
    for (PersonalInfo infos : PersonalInfoList) {
      PersonalInfo_OB_List.add(infos);
    }
    tablePersonal.setItems(PersonalInfo_OB_List);
    tablePersonal.refresh();

    /****************************************************************************/
    List<CurrentMoneyInfo> CurrentMoneyInfoList = JSON.parseObject(getJsonString(map, "实时金额"),
        new TypeReference<List<CurrentMoneyInfo>>() {
        });
    ObservableList<CurrentMoneyInfo> CurrentMoneyInfo_OB_List = FXCollections
        .observableArrayList();
    for (CurrentMoneyInfo infos : CurrentMoneyInfoList) {
      CurrentMoneyInfo_OB_List.add(infos);
    }
    tableCurrentMoney.setItems(CurrentMoneyInfo_OB_List);
    String sumOfCurrentMoney = getJsonString(map, "实时金额总和");
    tableCurrentMoney.getColumns().get(2).setText(sumOfCurrentMoney);
    tableCurrentMoney.refresh();

    /*************************************************************/
    List<ZijinInfo> ZijinInfoList =
        JSON.parseObject(getJsonString(map, "资金"), new TypeReference<List<ZijinInfo>>() {
        });
    ObservableList<ZijinInfo> ZijinInfo_OB_List = FXCollections.observableArrayList();
    for (ZijinInfo infos : ZijinInfoList) {
      ZijinInfo_OB_List.add(infos);
    }
    tableZijin.getColumns().get(1).setText(getJsonString(map, "资金总和"));
    tableZijin.setItems(ZijinInfo_OB_List);
    tableZijin.refresh();

    /******************************************************************/
    List<ProfitInfo> ProfitInfoList =
        JSON.parseObject(getJsonString(map, "利润"), new TypeReference<List<ProfitInfo>>() {
        });
    ObservableList<ProfitInfo> ProfitInfo_OB_List = FXCollections.observableArrayList();
    for (ProfitInfo infos : ProfitInfoList) {
      ProfitInfo_OB_List.add(infos);
    }
    tableProfit.getColumns().get(1).setText(getJsonString(map, "利润总和"));
    tableProfit.setItems(ProfitInfo_OB_List);
    tableProfit.refresh();

    /*************************************************************/
    List<KaixiaoInfo> KaixiaoInfoList =
        JSON.parseObject(getJsonString(map, "实时开销"), new TypeReference<List<KaixiaoInfo>>() {
        });
    ObservableList<KaixiaoInfo> KaixiaoInfo_OB_List = FXCollections.observableArrayList();
    for (KaixiaoInfo infos : KaixiaoInfoList) {
      KaixiaoInfo_OB_List.add(infos);
    }
    tableKaixiao.getColumns().get(1).setText(getJsonString(map, "实时开销总和"));
    tableKaixiao.setItems(KaixiaoInfo_OB_List);
    tableKaixiao.refresh();

    /***************************************************************/
    List<DangjuInfo> DangjuInfoList =
        JSON.parseObject(getJsonString(map, "当局"), new TypeReference<List<DangjuInfo>>() {
        });
    ObservableList<DangjuInfo> DangjuInfo_OB_List = FXCollections.observableArrayList();
    for (DangjuInfo infos : DangjuInfoList) {
      DangjuInfo_OB_List.add(infos);
    }
    tableDangju.getColumns().get(1).setText(getJsonString(map, "当局总和"));
    tableDangju.setItems(DangjuInfo_OB_List);
    tableDangju.refresh();

    /****************************************************************/
    List<JiaoshouInfo> JiaoshouInfoList =
        JSON.parseObject(getJsonString(map, "交收"), new TypeReference<List<JiaoshouInfo>>() {
        });
    ObservableList<JiaoshouInfo> JiaoshouInfo_OB_List = FXCollections.observableArrayList();
    for (JiaoshouInfo infos : JiaoshouInfoList) {
      JiaoshouInfo_OB_List.add(infos);
    }
    tableJiaoshou.getColumns().get(1).setText(getJsonString(map, "交收总和"));
    tableJiaoshou.setItems(JiaoshouInfo_OB_List);
    tableJiaoshou.refresh();

    /******************************************************************/
    List<PingzhangInfo> PingzhangInfoList =
        JSON.parseObject(getJsonString(map, "平帐"), new TypeReference<List<PingzhangInfo>>() {
        });
    ObservableList<PingzhangInfo> PingzhangInfo_OB_List = FXCollections.observableArrayList();
    for (PingzhangInfo infos : PingzhangInfoList) {
      PingzhangInfo_OB_List.add(infos);
    }
    tablePingzhang.getColumns().get(1).setText(getJsonString(map, "平帐总和"));
    tablePingzhang.setItems(PingzhangInfo_OB_List);
    tablePingzhang.refresh();
    /******************************************************************/
    LMLabel.setText(getJsonString(map, "联盟对帐"));
  }

  public String getJsonString(Map<String, String> map, String key) {
    if (map != null && map.size() > 0) {
      return map.get(key);
    } else {
      if (key.contains("总和")) {
        return "0";
      }
      return JSON.toJSONString(Collections.EMPTY_LIST);
    }
  }


  public void updateOrAdd_SSJE_after_personal_Pay(PersonalInfo personalInfo) throws Exception {
    // 1判断玩家是否在该金额表中
    String playerId = personalInfo.getPersonalPlayerId();
    String playerName = personalInfo.getPersonalPlayerName();
    if (StringUtil.isBlank(playerId)) {
      ShowUtil.show("支付按钮时找不到玩家" + playerName + "的ID!!请确认!");
    }
    // 修改金额表中的玩家金额
    // 个人总和 = 回保累计 + 回水累计
    double personalSum = NumUtil.getNum(personalInfo.getPersonalSumHB()) + NumUtil
        .getNum(personalInfo.getPersonalSumHS());
    for (CurrentMoneyInfo moneyInfo : tableCurrentMoneyInfo.getItems()) {
      if (playerId.equals(moneyInfo.getWanjiaId())) {
        String ssje = personalSum + NumUtil.getNum(moneyInfo.getShishiJine()) + "";
        if (ssje.contains(".")) {
          ssje = NumUtil.digit2(ssje);
        }
        moneyInfo.setShishiJine(ssje);
        saveOrUpdate2DB(moneyInfo);
        return;
      }
    }
    // 2 到这里，说明玩家不存在于金额表中，需要新增记录
    Player p = dataConstants.membersMap.get(playerId);
    if (p == null) {
      throw new Exception("该玩家不存在于名单中，且匹配不到团ID!玩家名称：" + playerName + ",玩家ID:" + playerId);
    }

    CurrentMoneyInfo tempMoneyInfo = new CurrentMoneyInfo(playerName,
        NumUtil.digit1(personalSum + ""),
        playerId, dataConstants.membersMap.get(playerId).getEdu(),
        MoneyCreatorEnum.USER.getCreatorName(), "0");
    addInfo(tempMoneyInfo);
    // 保存到数据库
    saveOrUpdate2DB(tempMoneyInfo);
  }

  public void updateOrAdd_SSJE_after_Pay(WanjiaInfo wj) throws Exception {
    // 1判断玩家是否在该金额表中
    String playerId = wj.getWanjiaId();
    if (StringUtil.isBlank(playerId)) {
      ShowUtil.show("支付按钮时找不到玩家" + wj.getWanjiaName() + "的ID!!请确认!");
    }
    // 修改金额表中的玩家金额
    for (CurrentMoneyInfo moneyInfo : tableCurrentMoneyInfo.getItems()) {
      if (playerId.equals(moneyInfo.getWanjiaId())) {
        moneyInfo.setCmiLmb(wj.getHeji());
        saveOrUpdate2DB(moneyInfo);
        return;
      }
    }
    // 2 到这里，说明玩家不存在于金额表中，需要新增记录
    Player p = dataConstants.membersMap.get(playerId);
    if (p == null) {
      throw new Exception("该玩家不存在于名单中，且匹配不到团ID!玩家名称：" + wj.getWanjiaName() + ",玩家ID:" + playerId);
    }

    CurrentMoneyInfo tempMoneyInfo = new CurrentMoneyInfo(wj.getWanjiaName(), "0",
        playerId, dataConstants.membersMap.get(playerId).getEdu(),
        MoneyCreatorEnum.DEFAULT.getCreatorName(), wj.getHeji());
    addInfo(tempMoneyInfo);
    // 保存到数据库
    saveOrUpdate2DB(tempMoneyInfo);
  }

  public void update_Table_CMI_Map() {
    Table_CMI_Map.clear();
    if (tableCurrentMoneyInfo != null && tableCurrentMoneyInfo.getItems() != null) {
      for (CurrentMoneyInfo moneyInfo : tableCurrentMoneyInfo.getItems()) {
        if (!StringUtil.isBlank(moneyInfo.getWanjiaId())) {
          Table_CMI_Map.put(moneyInfo.getWanjiaId(), moneyInfo);
        }
      }
    }
  }

  public CurrentMoneyInfo get_CMI_byId(String playerId) {
    for (CurrentMoneyInfo item : tableCurrentMoneyInfo.getItems()) {
      if (StringUtils.equals(playerId, item.getWanjiaId())) {
        return item;
      }
    }
    return null;
  }

  public String get_SSJE_byId(String playerId) {
    update_Table_CMI_Map();
    CurrentMoneyInfo ssjeInfo = Table_CMI_Map.get(playerId);
    if (ssjeInfo == null) {
      return "0";
    }
    return StringUtil.isBlank(ssjeInfo.getShishiJine()) ? "0" : ssjeInfo.getShishiJine();
  }

  public void del_SSJE_byId(String playerId) {
    if (!StringUtil.isBlank(playerId) && tableCurrentMoneyInfo != null
        && tableCurrentMoneyInfo.getItems().size() > 0) {
      update_Table_CMI_Map();
      CurrentMoneyInfo ssjeInfo = Table_CMI_Map.get(playerId);
      if (ssjeInfo == null) {
        return;
      }
      CurrentMoneyInfo info = null;
      for (CurrentMoneyInfo moneyInfo : tableCurrentMoneyInfo.getItems()) {
        if (playerId.equals(moneyInfo.getWanjiaId())) {
          info = moneyInfo;
        }
      }
      if (info != null) {
        tableCurrentMoneyInfo.getItems().remove(info);
      }

    }
  }

  public void fillTableCurrentMoneyInfo2(TableView<TeamInfo> tableTeam,
      TableView<CurrentMoneyInfo> table, TableView<ZijinInfo> tableZijin,
      TableView<ProfitInfo> tableProfit, TableView<KaixiaoInfo> tableKaixiao, Label LMLabel) {

    /************************************************ 团队回水表 **************/
    tableTeam.setItems(null);
    ObservableList<TeamInfo> obList = FXCollections.observableArrayList();
    List<TeamInfo> teamList = JSON.parseObject(dataConstants.preDataMap.get("团队回水"),
        new TypeReference<List<TeamInfo>>() {
        });
    for (TeamInfo info : teamList) {
      obList.add(info);
    }
    tableTeam.setItems(obList);
    /************************************************ 实时金额表 **************/
    // 1清空 表数据
    table.setItems(null);
    // 2获取InfoList
    ObservableList<CurrentMoneyInfo> obListCurrentMoney = FXCollections.observableArrayList();
    List<CurrentMoneyInfo> cmList = JSON.parseObject(dataConstants.preDataMap.get("实时金额"),
        new TypeReference<List<CurrentMoneyInfo>>() {
        });
    for (CurrentMoneyInfo info : cmList) {
      obListCurrentMoney.add(info);
    }
    table.setItems(obListCurrentMoney);
    table.refresh();

    // 合并ID操作
    flush_SSJE_table();

    /************************************************ 资金表 **************/
    // 1清空 表数据
    tableZijin.setItems(null);
    // 2获取InfoList
    ObservableList<ZijinInfo> obListZijin = FXCollections.observableArrayList();
    List<ZijinInfo> zijinList = JSON.parseObject(dataConstants.preDataMap.get("资金"),
        new TypeReference<List<ZijinInfo>>() {
        });
    for (ZijinInfo info : zijinList) {
      obListZijin.add(info);
    }
    tableZijin.setItems(obListZijin);
    /************************************************ 利润表 **************/
    // 1清空 表数据
    tableProfit.setItems(null);
    // 2获取InfoList
    ObservableList<ProfitInfo> obListProfit = FXCollections.observableArrayList();
    List<ProfitInfo> profitList = JSON.parseObject(dataConstants.preDataMap.get("利润"),
        new TypeReference<List<ProfitInfo>>() {
        });
    for (ProfitInfo info : profitList) {
      obListProfit.add(info);
    }
    tableProfit.setItems(obListProfit);
    /************************************************ 实时开销表 **************/
    tableKaixiao.setItems(null);
    // 2获取InfoList
    ObservableList<KaixiaoInfo> obListKaixiao = FXCollections.observableArrayList();
    List<KaixiaoInfo> kaixiaoList = JSON.parseObject(dataConstants.preDataMap.get("实时开销"),
        new TypeReference<List<KaixiaoInfo>>() {
        });
    for (KaixiaoInfo info : kaixiaoList) {
      obListKaixiao.add(info);
    } // add 注释掉：新一天的统计中实时开销为空
    tableKaixiao.setItems(obListKaixiao);
    // 缓存一一场的实时开销总和
    dataConstants.SumMap.put("上场开销", getSumOfTableKaixiao(tableKaixiao));
    /************************************************ 联盟对帐 **************/
    String lm = dataConstants.preDataMap.get("联盟对帐");
    LMLabel.setText(lm);// 注意这里是{联盟对帐={联盟对帐=3000}}
  }


  // 右表：名称鼠标双击事件：打开对话框增加上码值
  public void openAddZijinDiag(TableView<ZijinInfo> tableZijin, ZijinInfo info) {
    if (info != null && info.getZijinType() != null) {
      String oddZijin = StringUtil.isBlank(info.getZijinAccount()) ? "0" : info.getZijinAccount();
      TextInputDialog dialog = new TextInputDialog();
      ShowUtil.setIcon(dialog);
      dialog.setTitle("添加");
      dialog.setHeaderText(null);
      dialog.setContentText("续增" + info.getZijinType() + "值(Enter):");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()) {
        int value = 0;
        try {
          value = Integer.valueOf(result.get().trim());
        } catch (Exception e) {
          ShowUtil.show("非法数据!");
          return;
        }

        String softDate = changciController.getSoftDate();
        if (StringUtil.isBlank(softDate)) {
          ShowUtil.show("请先在场次信息中填写当天时间!");
          return;
        }
        // 保存到数据库
        BankFlowModel bankMoney =
            new BankFlowModel(info.getZijinType(), value, TimeUtil.getDateTime2(), softDate);
        dbUtil.saveHistoryBankMoney(bankMoney);
        // 生成界面表记录
        info.setZijinAccount(
            NumUtil.digit0(NumUtil.getNum(oddZijin) + NumUtil.getNum(result.get().trim())));
        // 添加到银行流水中
        bankFlowController.totalBankFlowList.add(bankMoney);
      }
      if (tableZijin != null && tableZijin.getItems() != null) {
        for (ZijinInfo zijin : tableZijin.getItems()) {
          if (zijin.getZijinType().equals(info.getZijinType())) {
            zijin.setZijinAccount(info.getZijinAccount());
            break;
          }
        }
      }
    }
  }

  private Stage stage;

  public void showSumPersonSSJE(TableView<CurrentMoneyInfo> tableCurrentMoney, int rowIndex) {
    CurrentMoneyInfo item = tableCurrentMoneyInfo.getItems().get(rowIndex);
    TableView<KeyValue> table = new TableView<>();
    TableColumn<KeyValue, String> playerNameCol = getTableColumnCommon("玩家名称", "key",
        ColumnColorType.COLUMN_COMMON);
    TableColumn<KeyValue, String> SSJECol = getTableColumnCommon("资金", "value",
        ColumnColorType.COLUMN_RED);
    table.getColumns().addAll(playerNameCol, SSJECol);
    table.setEditable(false);

    // 获取值
    List<KeyValue> list = new ArrayList<>();
    int size = tableCurrentMoney.getItems().size();
    double sumMoney = 0.0;
    String playerName, ssje;
    for (int i = rowIndex; i < size; i++) {
      CurrentMoneyInfo info = tableCurrentMoneyInfo.getItems().get(i);
      playerName = info.getMingzi();
      ssje = info.getShishiJine();
      if (StringUtil.isAllNotBlank(info.getWanjiaId(), playerName)) {
        list.add(new KeyValue(playerName, ssje));
        sumMoney += NumUtil.getNum(ssje);
      } else {
        break;
      }
    }
    table.getItems().addAll(list);
    table.getSelectionModel().clearSelection();

    // 设置总金额
    Label sumLabel = new Label();
    String totalDesc = "合计：" + NumUtil.digit0(sumMoney);
    sumLabel.setText(totalDesc);
    String color = sumMoney > 0 ? Constants.ORANGE : Constants.RED;
    sumLabel.setTextFill(Color.web(color)); // 设置总金额颜色
    sumLabel.setScaleX(3);
    sumLabel.setScaleY(3);
    sumLabel.setPrefHeight(250);
    sumLabel.setPrefWidth(290);
    sumLabel.setAlignment(Pos.CENTER);

    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(2, 0, 0, 0));
    vbox.getChildren().addAll(sumLabel, table);

    if (stage == null) { // 共享一个舞台
      stage = new Stage();
    }
    // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
    ShowUtil.setIcon(stage);
    stage.setTitle(item.getMingzi());
    stage.setWidth(240);
    stage.setHeight(300);

    Scene scene = new Scene(vbox);
    stage.setScene(scene);
    stage.show();

    //截图功能与通知功能
//    try {
//      clipBord(list, totalDesc);
//      Platform.runLater(() -> {
//        Notifications.create().title("截图成功").darkStyle()
//            .text(item.getMingzi() + System.lineSeparator() + totalDesc)
//            .position(Pos.BOTTOM_LEFT).showInformation();
//      });
//    } catch (Exception e) {
//      Platform.runLater(() -> {
//        Notifications.create().title("截图失败").text(e.getMessage()).position(Pos.BOTTOM_LEFT)
//            .showError();
//      });
//      e.printStackTrace();
//    }
  }

  private void clipBord(List<KeyValue> list, String totalDesc) throws Exception {
    String code = myController.sysCode.getText();
    if (StringUtil.isBlank(code)) {
      code = "GBK";
    }
    String html = Text2ImageUtil.getHtml2(list, totalDesc);
    BufferedImage img = Text2ImageUtil.toImage(html, code, 400, 700);
    ClipBoardUtil.setClipboardImage(img);
  }

  private TableColumn<KeyValue, String> getTableColumnCommon(String colName, String colVal,
      ColumnColorType columnColorType) {
    TableColumn<KeyValue, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(110);
    col.setCellValueFactory(new PropertyValueFactory<KeyValue, String>(colVal));
    if (columnColorType == ColumnColorType.COLUMN_RED) {
      col.setCellFactory(ColumnUtil.getColorCellFactory(new KeyValue()));
    }
    col.setSortable(false);
    return col;
  }


  public void updatePlayerName(String playerId, String oldeName, String newName) {
    ObservableList<CurrentMoneyInfo> obList = tableCurrentMoneyInfo.getItems();
    boolean isExist = false;
    if (obList != null && obList.size() > 0) {
      for (CurrentMoneyInfo info : obList) {
        // 先通过ID进行匹配
        if (info != null && info.getMingzi() != null && info.getWanjiaId() != null
            && info.getWanjiaId().equals(playerId)) {
          info.setMingzi(newName);
          isExist = true;
          break;
        }
        // 再通过名称进行匹配
        if (info != null && info.getMingzi() != null && info.getMingzi().trim().equals(oldeName)) {
          info.setMingzi(newName);
          isExist = true;
          break;
        }
      }
      if (isExist) {
        tableCurrentMoneyInfo.refresh();
      }
    }

  }

  public void exportMemberExcel() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String title = "名单登记表-德扑圈" + sdf.format(new Date());
    log.info("导出人员表Excel:" + title);
    String[] rowsName = new String[]{"玩家ID", "玩家名称", "股东", "团队", "额度", "是否父ID", "回保", "回水"};
    List<Object[]> dataList = new ArrayList<>();
    Object[] objs = null;
    Map<String, Player> memberMap = dataConstants.membersMap;
    String pId;
    Player player;
    for (Map.Entry<String, Player> entry : memberMap.entrySet()) {
      pId = entry.getKey();
      player = entry.getValue();
      objs = new Object[rowsName.length];
      objs[0] = pId;
      objs[1] = player.getPlayername();
      objs[2] = player.getGudong();
      objs[3] = player.getTeamid();
      objs[4] = player.getEdu();
      objs[5] = player.getIsparent();
      objs[6] = player.getHuibao();
      objs[7] = player.getHuishui();
      dataList.add(objs);
    }
    String out = "D:/" + title;
    ExportMembersExcel ex = new ExportMembersExcel(title, rowsName, dataList, out);
    try {
      ex.export();
      log.info("导出人员表Excel成功");
    } catch (Exception e) {
      ErrorUtil.err("导出人员表Excel失败", e);
    }
  }


  public void exportTeamhsExcel() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String title = "回水表" + sdf.format(new Date());
    log.info("导出团队回水表Excel:" + title);
    String[] rowsName =
        new String[]{"团队ID", "团队名字", "比例", "保险比例", "股东", "战绩是否代管理", "备注", "回水比例", "回保比例", "服务费判定"};
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    Map<String, Huishui> huishuiMap = dataConstants.huishuiMap;
    String teamId;
    Huishui hs;
    for (Map.Entry<String, Huishui> entry : huishuiMap.entrySet()) {
      teamId = entry.getKey();
      hs = entry.getValue();
      objs = new Object[rowsName.length];
      objs[0] = teamId;
      objs[1] = hs.getTeamName();
      objs[2] = NumUtil.getPercentStr(NumUtil.getNum(NumUtil.digit4(hs.getHuishuiRate())));
      objs[3] = NumUtil.getPercentStr(NumUtil.getNum(NumUtil.digit4(hs.getInsuranceRate())));
      objs[4] = hs.getGudong();
      objs[5] = hs.getZjManaged();
      objs[6] = hs.getBeizhu();// "回水比例","回保比例","服务费判定","服务费判定"
      objs[7] = hs.getProxyHSRate();
      objs[8] = hs.getProxyHBRate();
      objs[9] = hs.getProxyFWF();
      dataList.add(objs);
    }
    String out = "D:/" + title;
    ExportTeamhsExcel ex = new ExportTeamhsExcel(title, rowsName, dataList, out);
    try {
      ex.export();
      log.debug("导出回水表Excel成功");
    } catch (Exception e) {
      ErrorUtil.err("导出回水表Excel失败", e);
    }
  }

  public void exportSSJEAction(TableView<CurrentMoneyInfo> tableCurrentMoneyInfo) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String title = "实时金额表" + sdf.format(new Date());
    log.info("导出实时金额表Excel:" + title);
    String[] rowsName = new String[]{"总和", "玩家ID", "名字", "实时金额", "联盟币余", "额度"};
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    ObservableList<CurrentMoneyInfo> currentInfoList = tableCurrentMoneyInfo.getItems();
    for (CurrentMoneyInfo info : currentInfoList) {
      objs = new Object[rowsName.length];
      objs[0] = info.getCmSuperIdSum();
      objs[1] = info.getWanjiaId();
      objs[2] = info.getMingzi();
      objs[3] = info.getShishiJine();
      objs[4] = info.getCmiLmb();
      objs[5] = info.getCmiEdu();
      dataList.add(objs);
    }
    String out = "D:/" + title;
    ExportMembersExcel ex = new ExportMembersExcel(title, rowsName, dataList, out);
    try {
      ex.export();
      log.debug("导出实时金额表Excel成功");
    } catch (Exception e) {
      ErrorUtil.err("导出实时金额表Excel失败", e);
    }
  }

  public void exportCombineIDAction() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String title = "数据库中的合并ID表" + sdf.format(new Date());
    String[] rowsName = new String[]{"父ID", "子ID列表(请用#隔开)"};
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    Map<String, Set<String>> combineIdMap = dbUtil.getCombineData();
    if (combineIdMap == null || combineIdMap.size() == 0) {
      ShowUtil.show("数据库中无合并ID数据");
      return;
    }
    StringBuffer sb = new StringBuffer();
    for (Map.Entry<String, Set<String>> entry : combineIdMap.entrySet()) {
      String parentId = entry.getKey();
      Set<String> subIdSet = entry.getValue();

      sb.delete(0, sb.length());
      for (String subId : subIdSet) {
        sb.append(subId).append("#");
      }
      String subIdString = sb.substring(0, sb.lastIndexOf("#"));
      objs = new Object[rowsName.length];
      if (!StringUtil.isBlank(parentId) && !StringUtil.isBlank(subIdString)) {
        objs[0] = parentId;
        objs[1] = subIdString;
        dataList.add(objs);
      }
    }
    String out = "D:/" + title;
    ExportMembersExcel ex = new ExportMembersExcel(title, rowsName, dataList, out);
    try {
      ex.export();
      log.info("导出合并ID表Excel成功");
    } catch (Exception e) {
      ErrorUtil.err("导出合并ID表Excel失败", e);
    }
  }

  public void saveOrUpdate2FX(CurrentMoneyInfo cmiInfo) {
    if (cmiInfo == null) {
      return;
    }
    String playerId = cmiInfo.getWanjiaId();
    CurrentMoneyInfo info = getInfoById(playerId);
    if (info == null) {
      addInfo(cmiInfo);// 添加一条实时金额信息
    } else {
      info = cmiInfo;// 修改
    }
    refreshRecord();
  }

  public CurrentMoneyInfo getInfoById(String playerId) {
    if (StringUtil.isBlank(playerId)) {
      return null;
    }
    if (tableCurrentMoneyInfo != null && tableCurrentMoneyInfo.getItems() != null) {
      ObservableList<CurrentMoneyInfo> obList = tableCurrentMoneyInfo.getItems();
      for (CurrentMoneyInfo entity : obList) {
        if (entity != null && !StringUtil.isBlank(entity.getWanjiaId())
            && playerId.equals(entity.getWanjiaId())) {
          return entity;
        }
      }
    }
    return null;
  }

  public CurrentMoneyInfo getInfoByName(String name) {
    if (StringUtil.isBlank(name)) {
      return null;
    }
    if (tableCurrentMoneyInfo != null && tableCurrentMoneyInfo.getItems() != null) {
      ObservableList<CurrentMoneyInfo> obList = tableCurrentMoneyInfo.getItems();
      for (CurrentMoneyInfo entity : obList) {
        if (entity != null && !StringUtil.isBlank(entity.getMingzi())
            && name.toUpperCase().equals(entity.getMingzi().toUpperCase())) {
          return entity;
        }
      }
    }
    return null;
  }

  public boolean isExistIn_SSJE_Table_byId(String playerId) {
    return getInfoById(playerId) != null;
  }

  public boolean isExistIn_SSJE_Table_byName(String name) {
    return getInfoByName(name) != null;
  }

  public void addInfo(CurrentMoneyInfo info) {
    if (tableCurrentMoneyInfo != null && tableCurrentMoneyInfo.getItems() != null) {
      ObservableList<CurrentMoneyInfo> obList = tableCurrentMoneyInfo.getItems();
      obList.add(info);
    }
  }

  public void refreshRecord() {
    if (tableCurrentMoneyInfo != null && tableCurrentMoneyInfo.getItems() != null) {
      tableCurrentMoneyInfo.refresh();
    }
  }

  private boolean isTableNotNull() {
    return tableCurrentMoneyInfo != null && tableCurrentMoneyInfo.getItems() != null;
  }

  public void flush_SSJE_table() {
    LinkedList<CurrentMoneyInfo> srcList = new LinkedList<>();
    if (!isTableNotNull()) {
      return;
    } else {
      for (CurrentMoneyInfo info : tableCurrentMoneyInfo.getItems()) {
        srcList.add(info);
      }
    }

    // 组装数据为空
    // 对组装的数据列表进行处理，空行直接删除，父ID删除，子ID删除后缓存
    ListIterator<CurrentMoneyInfo> it = srcList.listIterator();
    Map<String, CurrentMoneyInfo> superIdInfoMap = new HashMap<>();// 存放父ID那条记录的信息
    Map<String, List<CurrentMoneyInfo>> superIdSubListMap = new HashMap<>();// 存放父ID下的所有子记录信息
    String playerId = "";
    while (it.hasNext()) {
      CurrentMoneyInfo item = it.next();
      // 删除空行
      if (StringUtil.isBlank(item.getWanjiaId()) && StringUtil.isBlank(item.getMingzi())
          && StringUtil.isBlank(item.getShishiJine())) {
        it.remove();// 没有玩家ID的就是空行
        continue;
      }
      // 删除父ID
      playerId = item.getWanjiaId();
      if (dataConstants.Combine_Super_Id_Map.get(playerId) != null) {
        superIdInfoMap.put(playerId, item);
        it.remove();
        continue;
      }
      // 删除子ID
      if (dataConstants.Combine_Sub_Id_Map.get(playerId) != null) {
        // 是子ID节点
        String parentID = dataConstants.Combine_Sub_Id_Map.get(playerId);
        List<CurrentMoneyInfo> childList = superIdSubListMap.get(parentID);
        if (childList == null) {
          childList = new ArrayList<>();
        }
        childList.add(item);
        superIdSubListMap.put(parentID, childList);
        it.remove();
        continue;
      }
    }

    // 添加子ID不在实时金额表中的父ID记录（单条父记录）
    superIdInfoMap.forEach((superId, info) -> {
      if (!superIdSubListMap.containsKey(superId)) {
        info.setCmSuperIdSum("");
        srcList.add(info);// 添加父记录
      }
    });

    // 添加父ID不在实时金额表中的子ID记录
    Iterator<Map.Entry<String, List<CurrentMoneyInfo>>> ite =
        superIdSubListMap.entrySet().iterator();
    while (ite.hasNext()) {
      Map.Entry<String, List<CurrentMoneyInfo>> entry = ite.next();
      String superId = entry.getKey();
      CurrentMoneyInfo superInfo = superIdInfoMap.get(superId);
      if (superInfo == null) {
        for (CurrentMoneyInfo info : entry.getValue()) {
          srcList.add(info);// 添加子记录
        }
        ite.remove();// 删除
      }
    }

    // 计算总和并填充父子节点和空行
    String superId = "";
    srcList.add(new CurrentMoneyInfo());// 空开一行
    for (Map.Entry<String, List<CurrentMoneyInfo>> entry : superIdSubListMap.entrySet()) {
      superId = entry.getKey();
      CurrentMoneyInfo superInfo = superIdInfoMap.get(superId);
//      Player player = dataConstants.membersMap.get(superId);
//      String superEdu = NumUtil.digit(player.getEdu());

      // 计算父节点总和
      List<CurrentMoneyInfo> subInfoList = entry.getValue();
      Double superIdSum = 0d;// 合并ID节点总和
      for (CurrentMoneyInfo info : subInfoList) {
        superIdSum += NumUtil.getNum(info.getShishiJine()) + NumUtil.getNum(info.getCmiLmb());
      }
      String sumSuper = NumUtil.getSum(superInfo.getShishiJine(), superInfo.getCmiLmb(), superInfo.getCmiEdu());
      superInfo
          .setCmSuperIdSum(NumUtil.digit0(NumUtil.getSum(superIdSum+"", sumSuper)));

      // 添加空行和子节点
      srcList.add(superInfo);// 先添加父节点
      for (CurrentMoneyInfo info : subInfoList) {// 再添加子节点
        srcList.add(info);
      }
      srcList.add(new CurrentMoneyInfo());// 空开一行
    }
    // 更新
    ObservableList<CurrentMoneyInfo> obList = FXCollections.observableArrayList();
    for (CurrentMoneyInfo cmi : srcList) {
      obList.add(cmi);
    }
    tableCurrentMoneyInfo.setItems(obList);
    tableCurrentMoneyInfo.refresh();
  }

  public boolean scrolById(String playerId) {
    boolean isExist = false;// 检查该玩家是否存在
    if (isTableNotNull() && playerId != null) {
      playerId = playerId.trim();
      ObservableList<CurrentMoneyInfo> list = tableCurrentMoneyInfo.getItems();
      String tempId = "";
      for (CurrentMoneyInfo moneyInfo : list) {
        tempId = moneyInfo.getWanjiaId();
        if (StringUtil.isBlank(tempId)) {
          continue;
        } else {
          tempId = tempId.trim();
        }
        if (playerId.equals(moneyInfo.getShishiJine())) {
          int index = list.indexOf(moneyInfo);
          tableCurrentMoneyInfo.scrollTo(index);
          // table.getSelectionModel().focus(index);
          tableCurrentMoneyInfo.getSelectionModel().select(index);
          isExist = true;
          break;
        }
      }
    }
    return isExist;
  }

  public boolean scrolByName(String playerName) {
    boolean isExist = false;// 检查该玩家是否存在
    if (isTableNotNull() && playerName != null) {
      playerName = playerName.trim().toUpperCase();
      ObservableList<CurrentMoneyInfo> list = tableCurrentMoneyInfo.getItems();
      String tempName = "";
      for (CurrentMoneyInfo moneyInfo : list) {
        tempName = moneyInfo.getMingzi();
        if (StringUtil.isBlank(tempName)) {
          continue;
        } else {
          tempName = tempName.trim().toUpperCase();
        }
        if (playerName.equals(tempName)) {
          int index = list.indexOf(moneyInfo);
          tableCurrentMoneyInfo.scrollTo(index);
          // table.getSelectionModel().focus(index);
          tableCurrentMoneyInfo.getSelectionModel().select(index);
          isExist = true;
          break;
        }
      }
    }
    return isExist;
  }


  public void addBank() {
    TextInputDialog dialog = new TextInputDialog();
    ShowUtil.setIcon(dialog);
    dialog.setTitle("添加");
    dialog.setHeaderText(null);
    dialog.setContentText("请输入新的资金类型:");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      String newBank = result.get().trim();
      if (StringUtil.isBlank(newBank)) {
        ShowUtil.show("亲，你输入的资金类型为空，将不计入资金列表！");
        return;
      }
      ZijinInfo newBankInfo = new ZijinInfo(newBank, "0");
      TableView<ZijinInfo> tableZijin = changciController.tableZijin;
      if (tableZijin != null && tableZijin.getItems() != null) {
        tableZijin.getItems().add(newBankInfo);
        tableZijin.refresh();
      }
    }
  }

  public void delBank() {
    TableView<ZijinInfo> tableZijin = changciController.tableZijin;
    ZijinInfo selectedZijin = tableZijin.getSelectionModel().getSelectedItem();
    if (selectedZijin == null) {
      ShowUtil.show("亲，请先选择你要删除的资金类型!");
      return;
    }
    String bankName = selectedZijin.getZijinType();
    String confirmMsg = "将删除" + bankName + "的所有银行流水，确定要删除吗？";
    if (AlertUtil.confirm(confirmMsg)) {
      // 资金表中删除该资金类型
      tableZijin.getItems().remove(selectedZijin);
      tableZijin.refresh();

      // 删除该资金类型的银行流水
      dbUtil.delBankFlowByType(bankName);

      // TODO 修改银行流水TAB中的列

      FXUtil.info("删除成功,已将" + bankName + "从软件系统中删除！");
    }
  }

  public CurrentMoneyInfo copyCurrentMoneyInfo(CurrentMoneyInfo info) {
    CurrentMoneyInfo copyInfo = new CurrentMoneyInfo(info.getMingzi(), info.getShishiJine(),
        info.getWanjiaId(), info.getCmiEdu(), info.getCreator(), info.getCmiLmb());
    copyInfo.setColor(info.getColor());
    copyInfo.setCmSuperIdSum(info.getCmSuperIdSum());
    return copyInfo;
  }

  public CurrentMoney change2CurrentMoney(CurrentMoneyInfo cmi) {
    CurrentMoney entity = new CurrentMoney();
    entity.setId(cmi.getWanjiaId());
    entity.setName(cmi.getMingzi());
    entity.setMoney(cmi.getShishiJine());
    entity.setLmb(cmi.getCmiLmb());
    entity.setEdu(cmi.getCmiEdu());
    entity.setSum(cmi.getCmSuperIdSum());
    entity.setUpdateTime(new Date());
    entity.setDecription(MoneyCreatorEnum.LIAN_MENG_BI.getCreatorName());
    return entity;
  }

  public void saveOrUpdate2DB(CurrentMoneyInfo cmi) {
    try {
      // 保存到数据库
      currentMoneyService.saveOrUpdate(new CurrentMoneyPK(cmi.getWanjiaId(), cmi.getMingzi()),
          change2CurrentMoney(cmi));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
