package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.jfoenix.controls.JFXButton;
import com.kendy.constant.DataConstans;
import com.kendy.customize.MyTable;
import com.kendy.db.DBUtil;
import com.kendy.db.entity.GameRecord;
import com.kendy.db.entity.pk.GameRecordPK;
import com.kendy.db.service.GameRecordService;
import com.kendy.entity.Club;
import com.kendy.entity.GameInfo;
import com.kendy.entity.GlbInfo;
import com.kendy.entity.lmbcontribute.GameContributeInfo;
import com.kendy.entity.lmbcontribute.GlbContributeInfo;
import com.kendy.enums.ExcelAutoDownType;
import com.kendy.model.GameRecordModel;
import com.kendy.model.lmb.ClubInfomation;
import com.kendy.model.lmb.LmbCache;
import com.kendy.util.ButtonUtil;
import com.kendy.util.ColumnUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.MaskerPaneUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.TextFieldUtil;
import com.kendy.util.TimeUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linzt
 * @date
 */
@Component
public class LMBContributeController extends BaseController implements Initializable {


  @Autowired
  MyController myController;

  @Autowired
  DataConstans dataConstans;

  @Resource
  GameRecordService gameRecordService;

  @Autowired
  ChangciController changciController;

  @Autowired
  LMBController lmbController;

  @Autowired
  DBUtil dbUtil;


  //======================================================俱乐部合计表
  @FXML
  public TableView<GlbContributeInfo> tableGlb;
  @FXML
  private TableColumn<GlbContributeInfo, String> glbClubId;
  @FXML
  private TableColumn<GlbContributeInfo, String> glbClubName;
  @FXML
  private TableColumn<GlbContributeInfo, String> glbBaoxianChouqu;
  @FXML
  private TableColumn<GlbContributeInfo, String> glbZhanjiChouqu;
  @FXML
  private TableColumn<GlbContributeInfo, String> glbChouquHeji;
  @FXML
  private TableColumn<GlbContributeInfo, String> glbUserRate;
  @FXML
  private TableColumn<GlbContributeInfo, String> glbContribute;

  //===================================================================小游戏合计表
  @FXML
  public TableView<GameContributeInfo> tableGame;
  @FXML
  private TableColumn<GameContributeInfo, String> gameClubId;
  @FXML
  private TableColumn<GameContributeInfo, String> gameClubName;
  @FXML
  private TableColumn<GameContributeInfo, String> gameLianmengFencheng;
  @FXML
  private TableColumn<GameContributeInfo, String> gameUserRate;
  @FXML
  private TableColumn<GameContributeInfo, String> gameContribute;

  //=================================
  @FXML
  private StackPane stackPane;

  private static final String JIA_LE_BI_HAI = ExcelAutoDownType.JIA_LE_BI.getName();
  private static final String DE_ZHOU_NIU_ZAI = ExcelAutoDownType.DE_ZHOU_NIU_ZAI.getName();
  private static final String ZHUANG_WEI = "1";
  public static final String HE_JI = "合计";

  private static final String LMB_CONTRIBUTE_NAME = "lmb_contribute";
  private Collection<GameRecordModel> todayDatas = new ArrayList<>(500);
  private LmbCache lmbCache ; // 缓存联盟币界面数据
  private Stage stage; // 查看视图


  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 绑定列值属性及颜色
    bindCellValueByTable(new GlbContributeInfo(), tableGlb);
    tableGlb.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        GlbContributeInfo item = getSelectedRow(tableGlb);
        if (item != null) {
          openGlbView(item);
        }
      }
    });
    bindCellValueByTable(new GameContributeInfo(), tableGame);
    tableGame.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        GameContributeInfo item = getSelectedRow(tableGame);
        if (item != null) {
          // 展示小游戏详情
          openGameView(item);
        }
      }
    });

    // 初始化配置项
    initConfig();

  }


  @FXML
  public void exportTableGlbAction() {
    new MyTable<GlbContributeInfo>(tableGlb, "俱乐部联盟币贡献值").exportByTable();
  }


  @FXML
  public void exportTableGameAction(){
    new MyTable<GameContributeInfo>(tableGame, "小游戏联盟币贡献值").exportByTable();
  }

  /**
   * 从数据库获取今日数据
   */
  private void getData() {
    if (CollectionUtils.isNotEmpty(todayDatas)) {
      todayDatas.clear();
    }
    todayDatas = gameRecordService
        .getGameRecordsByMaxTime(changciController.getSoftDate());

  }



  private ClubInfomation getClubInmationByClubId(String clubid) {
    for (ClubInfomation clubInfomation : lmbCache.getClubInfomations()) {
      if (StringUtils.equals(clubid, clubInfomation.getClubId())) {
        return clubInfomation;
      }
    }
    return saveClubInfomationIfNull(clubid);
  }

  private ClubInfomation saveClubInfomationIfNull(String clubId){
    Map<String, Club> allClub = dbUtil.getAllClub();
    ClubInfomation newClubInfo = new ClubInfomation();
    if (MapUtils.isNotEmpty(allClub) && allClub.containsKey(clubId)) {
      newClubInfo.setClubId(clubId);
      newClubInfo.setClubName(allClub.get(clubId).getName());
      List<ClubInfomation> clubInfomations = lmbCache.getClubInfomations();
      if (lmbCache.getClubInfomations() != null) {
        clubInfomations = new ArrayList<>();
      }
      clubInfomations.add(newClubInfo);
    } else {
      logger.error("俱乐部ID不存在：{}", clubId);
    }
    return newClubInfo;
  }


  private String getGameZJChouquWithTuo(GameRecordModel model, List<GameRecordModel> tableIdRecors) {
    double zhuangweiYSZJ = NumUtil.getNum(model.getYszj());
    double sumTuoYSZJ = 0d;
    double sumTuoBaoxian = 0d;
    for (GameRecordModel record : tableIdRecors) {
      if (lmbCache.getTuoIds().contains(record.getPlayerid())) {
        logger.info("是托：玩家名称：{}, 原始战绩{}, 保险{} ", record.getBeginplayername(), record.getYszj(),
            -1 * NumUtil.getNum(record.getSingleinsurance()));
        sumTuoYSZJ += NumUtil.getNum(record.getYszj());
        sumTuoBaoxian += NumUtil.getNum(record.getSingleinsurance()) * (-1);
      }
    }
    logger.info("是加勒比庄位：玩家ID是：{}, 名称是：{},其庄位原始战绩是：{}， 托总原始战绩：{}, 托总彩池合计：{}",
        model.getPlayerid(), model.getBeginplayername(), model.getYszj(),
        sumTuoYSZJ, sumTuoBaoxian);
    return NumUtil.digit((zhuangweiYSZJ + sumTuoYSZJ + sumTuoBaoxian) * 2);
  }


  /**
   * 点击刷新按钮
   */
  @FXML
  public void refreshAction() {
    MaskerPaneUtil.addMaskerPane(stackPane);
    Task task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        Thread.sleep(1000);
        Platform.runLater(() -> {

          getData();

          // 处理业务逻辑
          refresh();
        });
        return null;
      }

      @Override
      protected void succeeded() {
        super.succeeded();
        MaskerPaneUtil.hideMaskerPane(stackPane);
      }
    };
    new Thread(task).start();
  }

  /**
   * 合并俱乐部信息
   */
  private void mergeClubInformation() {
    Map<String, Club> allClub = dbUtil.getAllClub();
    List<ClubInfomation> clubInfomations = lmbCache.getClubInfomations();
    if (allClub != null) {
      if(allClub.size() == clubInfomations.size()){
        return;
      }
      allClub.forEach((clubId, clubInfo)->{
        boolean isContain = false;
        for (ClubInfomation clubInfomation : clubInfomations) {
          if (StringUtils.equals(clubId, clubInfomation.getClubId())) {
            isContain = true;
            break;
          }
        }
        if (!isContain) {
          ClubInfomation clubInfomation = new ClubInfomation();
          clubInfomation.setClubId(clubId);
          clubInfomation.setClubName(clubInfo.getName());
          clubInfomations.add(clubInfomation);
        }
      });
    }
  }

  /**
   * 刷新两个表数据
   */
  private void refresh() {
    if (CollectionUtils.isNotEmpty(todayDatas)) {
      Map<String, List<GameRecordModel>> glbMap = new HashMap<>();
      Map<String, List<GameRecordModel>> gameMap = new HashMap<>();
      String clubId;
      for (final GameRecordModel recordModel : todayDatas) {
        clubId = recordModel.getClubid();

        //小游戏
        boolean isGameType = isGameType(recordModel.getJutype());
        boolean notZhuangwei = !StringUtils.equals(ZHUANG_WEI, recordModel.getIsZhuangwei());
        if (isGameType && notZhuangwei) {
          // 属于小游戏且不是庄位
          List<GameRecordModel> gameList = gameMap.getOrDefault(clubId, new ArrayList<>());
          gameList.add(recordModel);
          gameMap.put(clubId, gameList);
        } else {
          // 非小游戏
          List<GameRecordModel> glbList = glbMap.getOrDefault(clubId, new ArrayList<>());
          glbList.add(recordModel);
          glbMap.put(clubId, glbList);
        }
      }
      // 设置俱乐部表数据
      setTableDataGlb(glbMap);
      // 设置小游戏表数据
      setTableDataGame(gameMap);
    }
  }

  /**
   * 从数据库加载
   */
  private void loadLmbCacheFromDB(){
    LmbCache _lmbCache = null;
    try {
      String cacheJson = dbUtil.getValueByKey(LMB_CONTRIBUTE_NAME);
      _lmbCache = JSON.parseObject(cacheJson, LmbCache.class);
      if (_lmbCache != null) {
        // 设置值

        lmbCache = _lmbCache;
      }
    } catch (Exception e) {
      ErrorUtil.err("加载配置项失败");
    }
  }

  /**
   * 点击保存时获取最新缓存
   */
  private void refreshLmbCache(){

  }


  private String getDefaultString(TextField textField) {
    String content = StringUtils.defaultString(textField.getText()).trim();
    if (content.contains("%")) {
      return content;
    }
    return "0%";
  }


  /**
   * 初始化配置项
   */
  private void initConfig() {

    // 从数据库加载
    loadLmbCacheFromDB();

    // 初始化数据
    getData();
  }


  @FXML
  private void saveConfigActioin() {
    // 刷新缓存
    refreshLmbCache();

    // 保存到数据库
    dbUtil.saveOrUpdateOthers(LMB_CONTRIBUTE_NAME, JSON.toJSONString(lmbCache));

    // 直接刷新
    this.refreshAction();
  }

  /**
   * 战绩记录是否为小游戏类型
   */
  private boolean isGameType(String gameType) {
    return lmbCache.getGameTypes().contains(gameType);
  }


  /**********************************************************************************************
   *
   *                                   第一个统计表 【俱乐部】
   *
   /**********************************************************************************************
   /**
   * 展示俱乐部关于联盟币的详情统计
   * @param item
   */
  private void openGlbView(GlbContributeInfo item) {

  }


  /**
   * 设置俱乐部表数据
   */
  private void setTableDataGlb(Map<String, List<GameRecordModel>> glbMap) {

  }

  /**
   * 第一个俱乐部合计 = 联盟返水 + 俱乐部保险
   */
  private String getClubHeji(GlbInfo detail) {
    String glbLianmengFanshui = detail.getGlbLianmengFanshui();
    String glbClubBaoxian = detail.getGlbClubBaoxian();
    String clubHeji = NumUtil
        .getSum(glbLianmengFanshui, glbClubBaoxian);
    return NumUtil.digit2(clubHeji);
  }

  private String getZhanjiChouqu(String yszj) {
    String zhanjieChouqu = StringUtils.contains(yszj, "-") ? "0" : NumUtil.getNum(yszj) * 0.05 + "";
    return NumUtil.digit2(zhanjieChouqu);
  }



  private String digit(double val) {
    return NumUtil.digit(val);
  }

  private String digit(String val) {
    return NumUtil.digit(val);
  }


  /**********************************************************************************************
   *
   *                                   第二个统计表【小游戏】
   *
   /**********************************************************************************************
   /**
   * 修改小游戏
   * @param item
   */
  private void openGameView(GameContributeInfo item) {
  }

  private void addGameSumLine(List<GameInfo> detailList, GameInfo item) {
    if (item != null) {
      if (detailList == null) {
        return;
      }
      boolean isContaisHeji = detailList.stream().map(GameInfo::getGameType)
          .anyMatch(e -> StringUtils.equals(HE_JI, e));
      if (isContaisHeji) {
        return; // 若已存在合计，则不再添加
      } else {
        // 计算总和
        GameInfo sumInfo = new GameInfo();
        BeanUtils.copyProperties(item, sumInfo);
        sumInfo.setGamePlayerId("-");
        sumInfo.setGamePlayerName("-");
        sumInfo.setGamePaiju("-");
        sumInfo.setGameType(HE_JI);
        detailList.add(sumInfo);
      }
    }
  }




  /**
   * 设置小游戏表数据
   */
  private void setTableDataGame(Map<String, List<GameRecordModel>> gameMap) {
    ObservableList<GameInfo> obList = FXCollections.observableArrayList();

  }



  // ===================================================================================

  /**
   * 获取加勒比海联盟分成比例
   * @return
   */
  private double get_fencheng_rate(String rate){
    if (StringUtils.contains(rate, "%")) {
      return NumUtil.getNumByPercent(rate);
    }
    return 0d;
  }



}
