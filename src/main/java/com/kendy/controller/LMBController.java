package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.kendy.constant.DataConstans;
import com.kendy.customize.MyTable;
import com.kendy.db.DBUtil;
import com.kendy.db.entity.GameRecord;
import com.kendy.db.entity.pk.GameRecordPK;
import com.kendy.db.service.GameRecordService;
import com.kendy.entity.Club;
import com.kendy.entity.GameInfo;
import com.kendy.entity.GlbInfo;
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
public class LMBController extends BaseController implements Initializable {


  @Autowired
  MyController myController;

  @Autowired
  DataConstans dataConstans;

  @Resource
  GameRecordService gameRecordService;

  @Autowired
  ChangciController changciController;

  @Autowired
  DBUtil dbUtil;


  //======================================================俱乐部合计表
  @FXML
  public TableView<GlbInfo> tableGlb;
  @FXML
  private TableColumn<GlbInfo, String> glbClubId;
  @FXML
  private TableColumn<GlbInfo, String> glbClubName;
  @FXML
  private TableColumn<GlbInfo, String> glbBaoxianChouqu;
  @FXML
  private TableColumn<GlbInfo, String> glbZhanjiChouqu;
  @FXML
  private TableColumn<GlbInfo, String> glbChouquHeji;
  @FXML
  private TableColumn<GlbInfo, String> glbLianmengDaiShoushui;
  @FXML
  private TableColumn<GlbInfo, String> glbLianmengFanshui;
  @FXML
  private TableColumn<GlbInfo, String> glbLianmengBXJiaoshou;
  @FXML
  private TableColumn<GlbInfo, String> glbLianmengBXZhancheng;
  @FXML
  private TableColumn<GlbInfo, String> glbClubBaoxian;
  @FXML
  private TableColumn<GlbInfo, String> glbClubHeji;

  //===================================================================小游戏合计表
  @FXML
  public TableView<GameInfo> tableGame;
  @FXML
  private TableColumn<GameInfo, String> gameClubId;
  @FXML
  private TableColumn<GameInfo, String> gameClubName;
  @FXML
  private TableColumn<GameInfo, String> gameLianmengFencheng;
  @FXML
  private TableColumn<GameInfo, String> gameClubFencheng;
  @FXML
  private TableColumn<GameInfo, String> gameClubHeji;
  @FXML
  private JFXTextField currentClubNameField; // 当前俱乐部名称
  @FXML
  private JFXTextField currentClubIdField; // 当前俱乐部ID

  // ===========================其它属性
  @FXML
  private StackPane stackPane;
  @FXML
  private StackPane stackPaneClub;
  @FXML
  private JFXTextField gameTypeField;
  @FXML
  private JFXTextField tuoIdField;
  @FXML
  private JFXTextField jlbLianmengFenchengRate; // 加勒比联盟分成比例
  @FXML
  private JFXTextField jlbClubFenchengRate; // 加勒比俱乐部分成比例
  @FXML
  private JFXTextField dezhouNiuzaiField; // 德州牛仔输入框（输入的ID视为庄家）

  private static final String JIA_LE_BI_HAI = ExcelAutoDownType.JIA_LE_BI.getName();
  private static final String DE_ZHOU_NIU_ZAI = ExcelAutoDownType.DE_ZHOU_NIU_ZAI.getName();
  private static final String ZHUANG_WEI = "1";
  public static final String HE_JI = "合计";

  private static final String LMB_NAME = "lian_meng_bi";
  private Collection<GameRecordModel> todayDatas = new ArrayList<>(500);
  private LmbCache lmbCache ; // 缓存联盟币界面数据
  private Stage stage; // 查看视图


  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 绑定列值属性及颜色
    bindCellValueByTable(new GlbInfo(), tableGlb);
    tableGlb.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        GlbInfo item = getSelectedRow(tableGlb);
        if (item != null && item.getDetailList() != null) {
          openGlbInfoDetailView(item);
        }
      }
    });
    bindCellValueByTable(new GameInfo(), tableGame);
    tableGame.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        GameInfo item = getSelectedRow(tableGame);
        if (item != null && item.getDetailList() != null) {
          // 展示小游戏详情
          openGameInfoDetailView(item);
        }
      }
    });

    // 初始化配置项
    initConfig();

    // 监听俱乐部变化
    initSingClubListen();

  }

  /**
   * 示例：将一个普通装饰成带有导出功能的示例代码
   * @param table
   */
  private void initExport(TableView<GlbInfo> table) {
    MyTable<GlbInfo> myTable = new MyTable<>(table, "俱乐部信息");
    myTable.setEntityClass(GlbInfo.class);
    JFXButton exportBtn = ButtonUtil.getDownloadButnByTable(myTable);

    stackPaneClub.getChildren().remove(0,stackPane.getChildren().size());
    stackPaneClub.getChildren().addAll(table, exportBtn);
    stackPaneClub.setAlignment(Pos.BOTTOM_CENTER);
  }

  @FXML
  public void exportTableGlbAction() {
    new MyTable<GlbInfo>(tableGlb, "俱乐部联盟币").exportByTable();
  }


  @FXML
  public void exportTableGameAction(){
    new MyTable<GameInfo>(tableGame, "小游戏联盟币").exportByTable();
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

    // 修改额外数据
    set_game_etra_data(todayDatas);
  }


  /**
   * 修改加勒比海的联盟分成和俱乐部分成
   * @param todayDatas
   */
  private void set_game_etra_data(Collection<GameRecordModel> todayDatas){

    if (CollectionUtils.isNotEmpty(todayDatas)) {

      // 处理俱乐部
      mergeClubInformation();

      // 桌号Map={桌号，该桌玩家记录列表}
      Map<String, List<GameRecordModel>> tableIdMap = todayDatas.stream()
          .collect(Collectors.groupingBy(GameRecordModel::getTableid));

      for (GameRecordModel model : todayDatas) {
        if (isGameType(model.getJutype())) {
          // 针对德州牛仔，根据设置的人员ID设置是否庄位
          if (StringUtils.equals(DE_ZHOU_NIU_ZAI, model.getJutype())) {
            if (lmbCache.getDezhouZhuangweiIds().contains(model.getPlayerid())
                && !StringUtils.equals(ZHUANG_WEI, model.getIsZhuangwei())) { // 非庄位，则设置庄位，并更新到数据库
              setDezhouZhuangwei(model);
            }
          }

          // 针对加勒比海，根据设置占比，设置俱乐部分成、联盟分成、联盟返水
          if (StringUtils.equals(JIA_LE_BI_HAI, model.getJutype())) {
            ClubInfomation clubInfomation = getClubInmationByClubId(model.getClubid());
            // 设置小游戏联盟分成
            model.setLianmengFencheng(computeFencheng(clubInfomation, model, true));
            // 设置小游戏俱乐部分成
            model.setClubFencheng(computeFencheng(clubInfomation, model, false));
          }

          // 设置小游戏其他字段
          boolean isZhuangWei = StringUtils.equals(ZHUANG_WEI, model.getIsZhuangwei());
          if (isZhuangWei) {
            // 第一个表合计小游戏战绩抽取=是庄位
            // 设置小游戏战绩抽取(Q列彩池合计)
            model.setSingleinsurance(computeGameZJChouqu(model, tableIdMap.get(model.getTableid())));
            model.setClubZaifenpei("0");
          } else {
            // 第一个表合计小游戏联盟返水=不是庄位=>取详细表的俱乐部分成
            // 设置小游戏联盟返水(Y列 = 俱乐部再分配=战绩抽取)
            model.setClubZaifenpei(model.getClubFencheng());
          }

        }
      }
    }
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


  /**
   * 针对德州牛仔，根据设置的人员ID设置是否庄位
   * @param model
   */
  private void setDezhouZhuangwei(GameRecordModel model) {
    model.setIsZhuangwei(ZHUANG_WEI);
    // 更新到数据库
    GameRecord entity = gameRecordService.get(
        new GameRecordPK(model.getSoftTime(), model.getClubid(), model.getTableid(),
            model.getPlayerid()));
    entity.setIsZhuangwei(ZHUANG_WEI);
    gameRecordService.updateNotNull(entity);
    logger.info("德州牛仔使用人工设置的庄位ID:{}", model.getPlayerid());
  }

  /**
   * 计算加勒比海分成：俱乐部分成和联盟分成 = (yszj - baoxiao) * 对应比例
   * @param model
   * @param isLMFencheng
   * @return
   */
  private String computeFencheng(ClubInfomation clubInfomation, GameRecordModel model, boolean isLMFencheng){
    String yszj = model.getYszj();
    String clubFenchengRate = clubInfomation.getClubFenchengRate();
    String lianmengFenchengRate = clubInfomation.getLianmengFenchengRate();

    double value = NumUtil.getNum(yszj) - NumUtil.getNum(model.getSingleinsurance());
    if (isLMFencheng) {
      return NumUtil.digit(value * (-1) * NumUtil.getNum(lianmengFenchengRate));
    }
    return NumUtil.digit(value * (-1) * NumUtil.getNum(clubFenchengRate));
  }

  /**
   * 获取后台的战绩抽取<br/>
   * 本桌若有托，且是加勒比海，则战绩抽取公式：（庄位的原始战绩 + 所有托的原始战绩 + 所有托的保险相反值） * 2
   * 本桌若无托，则用旧公式：则战绩抽取公式：庄位的原始战绩 * 2
   * @param model
   * @param tableIdRecors
   * @return
   */
  private String computeGameZJChouqu(GameRecordModel model,
      List<GameRecordModel> tableIdRecors){
    boolean tableHasTuo = isTableHasTuo(tableIdRecors);
    boolean jlbh = isJLBH(model);
    if (jlbh && tableHasTuo) {
      // 有托情况下使用新公式
      return getGameZJChouquWithTuo(model, tableIdRecors);
    }
    // 使用旧公式
    String yszj = model.getYszj();
    return NumUtil.digit(NumUtil.getNum(yszj) * 2);
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
   * 判断一桌记录里是否有托
   * @param tableIdRecors
   * @return
   */
  private boolean isTableHasTuo(List<GameRecordModel> tableIdRecors) {
    if (CollectionUtils.isNotEmpty(tableIdRecors)) {
      for (GameRecordModel record : tableIdRecors) {
        if (lmbCache.getTuoIds().contains(record.getPlayerid())) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * 监听俱乐部变化
   *
   */
  public void initSingClubListen() {

    // ListView变化时自动更新右边的信息
    tableGame.getSelectionModel().selectedItemProperty()
        .addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {

          GameInfo currentItem = (GameInfo) newValue;
          if (currentItem == null || currentItem.getGameClubId() == null) {
            return;// 屏蔽空指针异常
          }
          // 根据当前俱乐部信息，设置相应比例
          String clubId = currentItem.getGameClubId();
          ClubInfomation clubInfomation = getClubInmationByClubId(clubId);
          currentClubNameField.setText(clubInfomation.getClubName());
          currentClubIdField.setText(clubInfomation.getClubId());
          jlbClubFenchengRate.setText(clubInfomation.getClubFenchengRate());
          jlbLianmengFenchengRate.setText(clubInfomation.getLianmengFenchengRate());
        });
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
      String cacheJson = dbUtil.getValueByKey(LMB_NAME);
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
    // 设置小游戏类型
    lmbCache.setGameTypes(TextFieldUtil.defaultSplit(gameTypeField));

    // 设置加勒比海
    setLmbcaheOfJLBRate();

    // 设置德州牛仔
    lmbCache.setDezhouZhuangweiIds(TextFieldUtil.defaultSplit(dezhouNiuzaiField));

    // 托ID
    lmbCache.setTuoIds(TextFieldUtil.defaultSplit(tuoIdField));
  }

  /**
   * 设置加勒比海比例
   */
  private void setLmbcaheOfJLBRate() {
    for (ClubInfomation clubInfomation : lmbCache.getClubInfomations()) {
      String clubId = clubInfomation.getClubId();
      if (StringUtils.equals(clubId, currentClubIdField.getText())) {
        String clubFenchengRate = getDefaultString(jlbClubFenchengRate);
        String lmFenchengRate = getDefaultString(jlbLianmengFenchengRate);
        clubInfomation.setClubFenchengRate(clubFenchengRate);
        clubInfomation.setLianmengFenchengRate(lmFenchengRate);
      }
    }
  }

  private String getDefaultString(TextField textField) {
    String content = StringUtils.defaultString(textField.getText()).trim();
    if (content.contains("%")) {
      return content;
    }
    return "0%";
  }


  /**
   * 从缓存中设置界面内容
   */
  private void setUIText(){
    // 设置小游戏类型
    TextFieldUtil.setDefaultContent(gameTypeField, lmbCache.getGameTypes());
    // 设置德州牛仔
    TextFieldUtil.setDefaultContent(dezhouNiuzaiField, lmbCache.getDezhouZhuangweiIds());
    // 设置加勒比海
    setFisrtClubInfomation();
    // 设置托ID
    TextFieldUtil.setDefaultContent(tuoIdField, lmbCache.getTuoIds());

  }

  private void setFisrtClubInfomation() {
    jlbClubFenchengRate.setText("0%");
    jlbLianmengFenchengRate.setText("0%");
  }


  /**
   * 初始化配置项
   */
  private void initConfig() {

    // 从数据库加载
    loadLmbCacheFromDB();

    // 设置界面选项值
    setUIText();

    // 初始化数据
    getData();
  }


  @FXML
  private void saveConfigActioin() {
    // 刷新缓存
    refreshLmbCache();

    // 保存到数据库
    dbUtil.saveOrUpdateOthers(LMB_NAME, JSON.toJSONString(lmbCache));

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
   */
  private void openGlbInfoDetailView(GlbInfo item) {
    List<GlbInfo> detailList = item.getDetailList();
    addSumLine(detailList, item);
    String gameClubName = item.getGlbClubName();
    String TITLE = gameClubName + "俱乐部的统计";
    MyTable<GlbInfo> table = new MyTable<>();

    setClubColumns(table);
    table.setEditable(false);

    // 获取值
    table.getItems().addAll(detailList);
    table.getSelectionModel().clearSelection();

    // 导出按钮
    table.setEntityClass(GlbInfo.class);
    table.setExcelName(TITLE + TimeUtil.getDateTime());
    JFXButton exportBtn = ButtonUtil.getDownloadButn(table);

    StackPane stackPane = new StackPane();
    stackPane.getChildren().addAll(table, exportBtn);
    stackPane.setAlignment(Pos.BOTTOM_CENTER);

    if (stage == null) { // 共享一个舞台
      stage = new Stage();
    }
    // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
    ShowUtil.setIcon(stage);
    stage.setTitle(TITLE);
    stage.setWidth(1550);
    stage.setHeight(450);

    Scene scene = new Scene(stackPane);
    stage.setScene(scene);
    stage.show();
  }

  private void addSumLine(List<GlbInfo> detailList, GlbInfo item) {
    if (item != null) {
      if (detailList == null) {
        return;
      }
      boolean isContaisHeji = detailList.stream().map(GlbInfo::getGlbType)
          .anyMatch(e -> StringUtils.equals(HE_JI, e));
      if (isContaisHeji) {
        return; // 若已存在合计，则不再添加
      } else {
        // 计算总和
        GlbInfo sumInfo = new GlbInfo();
        BeanUtils.copyProperties(item, sumInfo);
        sumInfo.setGlbPlayerId("-");
        sumInfo.setGlbPlayerName("-");
        sumInfo.setGlbPaiju("-");
        sumInfo.setGlbType(HE_JI);
        detailList.add(sumInfo);
      }
    }
  }

  private void setClubColumns(TableView table) {
    List<TableColumn<GlbInfo, String>> cols = Arrays.asList(
        getTableColumn("俱乐部ID", "glbClubId", 2),
        getTableColumn("俱乐部名称", "glbClubName", 2),
        getTableColumn("玩家ID", "glbPlayerId", 2),
        getTableColumn("玩家名称", "glbPlayerName", 2),
        getTableColumn("牌局", "glbPaiju", 2),
        getTableColumn("类型", "glbType", 2),
        getTableColumn("俱乐部保险抽取", "glbBaoxianChouqu", 2),
        getTableColumn("俱乐部战绩抽取", "glbZhanjiChouqu", 2),
        getTableColumn("俱乐部抽取合计", "glbChouquHeji", 2),
        getTableColumn("俱乐部联盟代收水", "glbLianmengDaiShoushui", 2),
        getTableColumn("俱乐部联盟返水", "glbLianmengFanshui", 2),
        getTableColumn("俱乐部联盟保险交收", "glbLianmengBXJiaoshou", 2),
        getTableColumn("俱乐部联盟保险占成", "glbLianmengBXZhancheng", 2),
        getTableColumn("俱乐部保险", "glbClubBaoxian", 2),
        getTableColumn("俱乐部合计", "glbClubHeji", 2)
    );
    table.getColumns().addAll(cols);
  }


  /**
   * 设置俱乐部表数据
   */
  private void setTableDataGlb(Map<String, List<GameRecordModel>> glbMap) {
    ObservableList<GlbInfo> obList = FXCollections.observableArrayList();
    glbMap.forEach((clubId, clubDatas) -> {

      List<GlbInfo> detailGameInfos = new ArrayList<>();
      for (GameRecordModel model : clubDatas) {
        GlbInfo detail = new GlbInfo();
        detail.setGlbClubId(clubId);
        detail.setGlbClubName(model.getClubName());
        detail.setGlbPlayerId(model.getPlayerid());
        detail.setGlbPlayerName(model.getBeginplayername());
        detail.setGlbPaiju(model.getTableid());

        detail.setGlbType(model.getJutype());
        detail.setGlbIsZhuangWei(model.getIsZhuangwei());
        detail.setGlbYszj(model.getYszj());
        detail.setGlbClubFencheng(model.getClubFencheng());
        detail.setDetailList(null);

        if (isGameType(model.getJutype())) {
          detail
              .setGlbBaoxianChouqu("0");
          detail.setGlbZhanjiChouqu(digit(model.getSingleinsurance())); // see 方法set_game_etra_data()
          detail.setGlbChouquHeji("0");
          detail.setGlbLianmengDaiShoushui("0");
          detail.setGlbLianmengFanshui(digit(model.getClubZaifenpei())); // see 方法set_game_etra_data()
          detail.setGlbLianmengBXJiaoshou("0");
          detail.setGlbLianmengBXZhancheng("0");
          detail.setGlbClubBaoxian("0");
          detail.setGlbClubHeji(getClubHeji(detail));
        } else {
          detail
              .setGlbBaoxianChouqu(digit(NumUtil.getNum(model.getSingleinsurance()) * (-1))); // Q列相反值
          detail.setGlbZhanjiChouqu(getZhanjiChouqu(model.getYszj()));
          detail.setGlbChouquHeji(
              digit(NumUtil.getSum(detail.getGlbZhanjiChouqu(), detail.getGlbBaoxianChouqu())));
          detail.setGlbLianmengDaiShoushui(model.getLmbKoujian()); // 联盟代收水 = Excel的W列 = 即联盟币扣减
          detail.setGlbLianmengFanshui(model.getClubZaifenpei()); // 联盟返水= Y列 = 俱乐部再分配
          detail.setGlbLianmengBXJiaoshou(
              digit(NumUtil.getNum(model.getSingleinsurance()) * (-1) * 0.05)); // Q列所属玩家保险*0.05
          detail.setGlbLianmengBXZhancheng(digit(
              NumUtil.getNum(model.getSingleinsurance()) * (-1) * 0.95
                  * 0.1)); // 联盟收取后台保险占成=(Q列所属玩家保险*0.95*0.1)
          detail.setGlbClubBaoxian(model.getBaoxianZaifenpei());
          detail.setGlbClubHeji(getClubHeji(detail));
        }


        detailGameInfos.add(detail);
      }
      // 获取俱乐部单条记录
      GlbInfo clubGameInfo = getSumGlbInfo(detailGameInfos, 1);
      obList.add(clubGameInfo);
    });
    // 添加汇总记录
    GlbInfo clubGameInfo = getSumGlbInfo(obList, 2);
    obList.add(clubGameInfo);
    tableGlb.setItems(obList);
    tableGlb.refresh();
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


  /**
   * 获取俱乐部汇总记录
   *
   * @param details 俱乐部的详细数据
   * @param type 1计算单个俱乐部表记录  2计算所有俱乐部的合计记录
   */
  private GlbInfo getSumGlbInfo(List<GlbInfo> details, int type) {
    double baoxianChouqu = 0;
    double zhanjiChouqu = 0;
    double daiShoushui = 0;
    double fanshui = 0;
    double baoxianJiaoshou = 0;
    double baoxianZhancheng = 0;
    double clubBaoxian = 0;
    double clubHeji = 0;
    for (GlbInfo detail : details) {
      if (isGameType(detail.getGlbType())) { // 累加小游戏，目前只修改战绩抽取和联盟返水
        fanshui += NumUtil.getNum(detail.getGlbLianmengFanshui()); // getGameLianmengFanshui(detail);
        zhanjiChouqu += NumUtil.getNum(detail.getGlbZhanjiChouqu());
        clubHeji += NumUtil.getNum(detail.getGlbClubHeji());

      } else {
        baoxianChouqu += NumUtil.getNum(detail.getGlbBaoxianChouqu());
        zhanjiChouqu += NumUtil.getNum(detail.getGlbZhanjiChouqu());
        daiShoushui += NumUtil.getNum(detail.getGlbLianmengDaiShoushui());
        fanshui += NumUtil.getNum(detail.getGlbLianmengFanshui());
        baoxianJiaoshou += NumUtil.getNum(detail.getGlbLianmengBXJiaoshou());
        baoxianZhancheng += NumUtil.getNum(detail.getGlbLianmengBXZhancheng());
        clubBaoxian += NumUtil.getNum(detail.getGlbClubBaoxian());
        clubHeji += NumUtil.getNum(detail.getGlbClubHeji());
      }
    }
    // 设置抽取合计
    for (GlbInfo detail : details) {
      detail.setGlbChouquHeji(digit(NumUtil.getSum(detail.getGlbBaoxianChouqu(), detail.getGlbZhanjiChouqu())));
    }
    GlbInfo clubInfo = new GlbInfo();
    clubInfo.setGlbBaoxianChouqu(digit(baoxianChouqu));
    clubInfo.setGlbZhanjiChouqu(digit(zhanjiChouqu));
    clubInfo.setGlbChouquHeji(digit(baoxianChouqu + zhanjiChouqu));
    clubInfo.setGlbLianmengDaiShoushui(digit(daiShoushui));
    clubInfo.setGlbLianmengFanshui(digit(fanshui));
    clubInfo.setGlbLianmengBXJiaoshou(digit(baoxianJiaoshou));
    clubInfo.setGlbLianmengBXZhancheng(digit(baoxianZhancheng));
    clubInfo.setGlbClubBaoxian(digit(clubBaoxian));
    clubInfo.setGlbClubHeji(digit(clubHeji));

    if (type == 1) {
      clubInfo.setDetailList(details);
      clubInfo.setGlbClubId(details.get(0).getGlbClubId());
      clubInfo.setGlbClubName(details.get(0).getGlbClubName());
    } else {
      clubInfo.setDetailList(null);
      clubInfo.setGlbClubName("合计");
      clubInfo.setGlbClubId("-");
    }
    return clubInfo;
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
   * 展示小游戏详情
   */
  private void openGameInfoDetailView(GameInfo item) {
    List<GameInfo> detailList = item.getDetailList();
    addGameSumLine(detailList, item);
    String gameClubName = item.getGameClubName();
    String TITLE = gameClubName + "的小游戏统计";
    MyTable<GameInfo> table = new MyTable<>();

    setColumns(table);

    table.setEditable(false);

    // 获取值
    table.getItems().addAll(detailList);
    table.getSelectionModel().clearSelection();

    // 导出按钮
    table.setEntityClass(GameInfo.class);
    table.setExcelName(TITLE + TimeUtil.getDateTime());
    JFXButton exportBtn = ButtonUtil.getDownloadButn(table);

    StackPane stackPane = new StackPane();
    stackPane.getChildren().addAll(table, exportBtn);
    stackPane.setAlignment(Pos.BOTTOM_CENTER);

    if (stage == null) { // 共享一个舞台
      stage = new Stage();
    }
    // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
    ShowUtil.setIcon(stage);
    stage.setTitle(TITLE);
    stage.setWidth(720);
    stage.setHeight(400);

    Scene scene = new Scene(stackPane);
    stage.setScene(scene);
    stage.show();
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

  private void setColumns(TableView table) {
    List<TableColumn<GameInfo, String>> cols = Arrays.asList(
        getTableColumn("俱乐部ID", "gameClubId", 1),
        getTableColumn("俱乐部名称", "gameClubName", 1),
        getTableColumn("玩家ID", "gamePlayerId", 1),
        getTableColumn("玩家名称", "gamePlayerName", 1),
        getTableColumn("牌局", "gamePaiju", 1),
        getTableColumn("类型", "gameType", 1),
        getTableColumn("联盟分成", "gameLianmengFencheng", 1),
        getTableColumn("俱乐部分成", "gameClubFencheng", 1)
    );
    table.getColumns().addAll(cols);
  }


  /**
   * 获取动态数据表的列
   */
  private <T> TableColumn<T, String> getTableColumn(String colName,
      String colVal, int type) {
    T t = (T) (type == 1 ? new GameInfo() : new GlbInfo());
    double width = (type == 1 ? 85.0 : 110.0);
    return ColumnUtil.getTableRedColumn(colName, colVal, width, t);
  }


  /**
   * 设置小游戏表数据
   */
  private void setTableDataGame(Map<String, List<GameRecordModel>> gameMap) {
    ObservableList<GameInfo> obList = FXCollections.observableArrayList();
    gameMap.forEach((clubId, clubDatas) -> {

      List<GameInfo> detailGameInfos = new ArrayList<>();
      for (GameRecordModel model : clubDatas) {
        GameInfo detail = new GameInfo();
        detail.setGameType(model.getJutype());
        detail.setGameClubId(clubId);
        detail.setGameClubName(model.getClubName());
        detail.setGamePlayerId(model.getPlayerid());
        detail.setGamePlayerName(model.getBeginplayername());
        detail.setGamePaiju(model.getTableid());
        detail.setGameLianmengFencheng(model.getLianmengFencheng());
        detail.setGameClubFencheng(model.getClubFencheng());
        detail.setGameClubHeji(
            NumUtil.digit2(NumUtil.getSum(model.getLianmengFencheng(), model.getClubFencheng())));
        detailGameInfos.add(detail);
      }
      // 获取小游戏单条记录
      GameInfo clubGameInfo = getSumGameInfo(detailGameInfos, 1);
      obList.add(clubGameInfo);
    });
    // 添加汇总记录
    GameInfo clubGameInfo = getSumGameInfo(obList, 2);
    obList.add(clubGameInfo);
    tableGame.setItems(obList);
    tableGame.refresh();
  }


  /**
   * 获取俱乐部汇总记录
   *
   * @param type 1计算单个俱乐部的小游戏  2计算所有俱乐部的小游戏
   */
  private GameInfo getSumGameInfo(List<GameInfo> details, int type) {
    double sumLianmengFencheng = 0;
    double sumClubFencheng = 0;
    for (GameInfo detail : details) {
      sumLianmengFencheng += NumUtil.getNum(detail.getGameLianmengFencheng());
      sumClubFencheng += NumUtil.getNum(detail.getGameClubFencheng());
    }
    GameInfo clubInfo = new GameInfo();
    clubInfo.setGameLianmengFencheng(NumUtil.digit2(sumLianmengFencheng + ""));
    clubInfo.setGameClubFencheng(NumUtil.digit2(sumClubFencheng + ""));
    // 小游戏俱乐部合计 = 俱乐部分成
    clubInfo.setGameClubHeji(NumUtil.digit2(sumClubFencheng + ""));
    if (type == 1) {
      clubInfo.setDetailList(details);
      clubInfo.setGameClubId(details.get(0).getGameClubId());
      clubInfo.setGameClubName(details.get(0).getGameClubName());
    } else {
      clubInfo.setDetailList(null);
      clubInfo.setGameClubName(HE_JI);
      clubInfo.setGameClubId("-");
    }
    return clubInfo;
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


  /*******************************************************************************
   *
   *  对外常用方法
   *
   ******************************************************************************/

  public boolean isLittleGame(GameRecordModel recordModel) {
    try {
      return lmbCache.getGameTypes().contains(recordModel.getJutype());

    } catch (Exception e) {
      logger.error("缓存中不存在小游戏类型");
      return false;
    }
  }

  public boolean isJLBH(GameRecordModel gameRecordModel) {
    return StringUtils.equals(JIA_LE_BI_HAI, gameRecordModel.getJutype());
  }

  public boolean isDeZhou(GameRecordModel gameRecordModel) {
    return StringUtils.equals(DE_ZHOU_NIU_ZAI, gameRecordModel.getJutype());
  }

  public boolean isLittleGame(String juType) {
    return lmbCache.getGameTypes().contains(juType);
  }




}