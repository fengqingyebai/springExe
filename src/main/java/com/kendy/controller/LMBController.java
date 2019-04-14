package com.kendy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.kendy.constant.DataConstans;
import com.kendy.customize.MyTable;
import com.kendy.db.service.GameRecordService;
import com.kendy.entity.GameInfo;
import com.kendy.entity.GlbInfo;
import com.kendy.model.GameRecordModel;
import com.kendy.util.ButtonUtil;
import com.kendy.util.ColumnUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.MaskerPaneUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.TimeUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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


  //======================================================俱乐部合计表
  @FXML
  public TableView<GlbInfo> tableGlb;
  @FXML
  private TableColumn<GlbInfo, String> glbClubId;
  @FXML
  private TableColumn<GlbInfo, String> glbClubName;
//  @FXML
//  private TableColumn<GlbInfo, String> glbPlayerId;
//  @FXML
//  private TableColumn<GlbInfo, String> glbPlayerName;
//  @FXML
//  private TableColumn<GlbInfo, String> glbPaiju;
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

  // ===========================其它属性
  @FXML
  private StackPane stackPane;
  @FXML
  private JFXTextField gameTypeField;


  private Collection<GameRecordModel> todayDatas = new ArrayList<>(500);
  private List<String> GAME_TYPES = new ArrayList<>();
  private Stage stage; // 查看视图


  @PostConstruct
  public void initData() {

  }

  private void getData() {
    if (CollectionUtils.isNotEmpty(todayDatas)) {
      todayDatas.clear();
    }
//    Map<String, List<GameRecordModel>> zjMap = dataConstans.zjMap;
//    if (zjMap != null) {
//      for (List<GameRecordModel> teamList : zjMap.values()) {
//        todayDatas.addAll(teamList);
//      }
//    }
    todayDatas = gameRecordService
        .getGameRecordsByMaxTime(changciController.getSoftDate());
  }


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

    // 初始化游戏类型
    GAME_TYPES = getGameType();

    // 初始化数据
    getData();
  }

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
   * 刷新两个表数据
   */
  private void refresh() {
    if (CollectionUtils.isNotEmpty(todayDatas)) {
      Map<String, List<GameRecordModel>> glbMap = new HashMap<>();
      Map<String, List<GameRecordModel>> gameMap = new HashMap<>();
      String clubId;
      for (final GameRecordModel recordModel : todayDatas) {
        clubId = recordModel.getClubid();
        if (isGameType(recordModel.getJutype())) {
          List<GameRecordModel> gameList = gameMap.getOrDefault(clubId, new ArrayList<>());
          gameList.add(recordModel);
          gameMap.put(clubId, gameList);

        } else {
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
   * 获取小游戏类型列表
   * @return
   */
  private List<String> getGameType() {
    List<String> _gameTypes = new ArrayList<>();
    String gameTypeText = gameTypeField.getText();
    if (StringUtils.isNotBlank(gameTypeText)) {
      for (String gameType : gameTypeText.trim().split("##")) {
        _gameTypes.add(gameType);
      }
    }
    return _gameTypes;
  }

  @FXML
  private void saveGameTypeActioin() {
    GAME_TYPES = getGameType();
    // TODO 保存到数据库
    FXUtil.info("小游戏类型保存成功, 已更新到软件系统，刷新生效哦！");
  }

  /**
   * 战绩记录是否为小游戏类型
   */
  private boolean isGameType(String gameType) {
    return GAME_TYPES.contains(gameType);
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
    stage.setWidth(1400);
    stage.setHeight(400);

    Scene scene = new Scene(stackPane);
    stage.setScene(scene);
    stage.show();
  }

  private void setClubColumns(TableView table) {
    List<TableColumn<GlbInfo, String>> cols = Arrays.asList(
        getTableColumn("俱乐部ID", "glbClubId", 2),
        getTableColumn("俱乐部名称", "glbClubName", 2),
        getTableColumn("玩家ID", "glbPlayerId", 2),
        getTableColumn("玩家名称", "glbPlayerName", 2),
        getTableColumn("牌局", "glbPaiju", 2),
        getTableColumn("俱乐部保险抽取", "glbBaoxianChouqu", 2),
        getTableColumn("俱乐部战绩抽取", "glbZhanjiChouqu", 2),
        getTableColumn("俱乐部抽取合计", "glbChouquHeji", 2),
        getTableColumn("俱乐部联盟代收水", "glbLianmengDaiShoushui", 2),
        getTableColumn("俱乐部联盟返水", "glbLianmengFanshui", 2),
        getTableColumn("俱乐部联盟保险交收", "glbLianmengBXJiaoshou", 2),
        getTableColumn("俱乐部联盟保险占成", "glbLianmengBXZhancheng", 2)
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
        detail.setGlbBaoxianChouqu(digit(NumUtil.getNum(model.getSingleinsurance()) * (-1))); // Q列相反值
        detail.setGlbZhanjiChouqu(getZhanjiChouqu(model.getYszj()));
        detail.setGlbChouquHeji(digit(NumUtil.getSum(detail.getGlbZhanjiChouqu(), detail.getGlbBaoxianChouqu())));
        detail.setGlbLianmengDaiShoushui(model.getLmbKoujian()); // 联盟代收水 = Excel的W列 = 即联盟币扣减
        detail.setGlbLianmengFanshui(model.getClubZaifenpei()); // 联盟返水= Y列 = 俱乐部再分配
        detail.setGlbLianmengBXJiaoshou(digit(NumUtil.getNum(model.getSingleinsurance()) * (-1) * 0.05 )); // Q列所属玩家保险*0.05
        detail.setGlbLianmengBXZhancheng(digit(NumUtil.getNum(model.getSingleinsurance()) * (-1) * 0.95 * 0.1 )); // 联盟收取后台保险占成=(Q列所属玩家保险*0.95*0.1)
        detail.setGlbClubHeji(getClubHeji(detail));
        detail.setDetailList(null);

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
   * 第一个俱乐部合计 = 联盟代收水 + 联盟返水 + 联盟保险交收 + 联盟收取后台保险占成
   * @param detail
   * @return
   */
  private String getClubHeji(GlbInfo detail) {
    String glbLianmengDaiShoushui = detail.getGlbLianmengDaiShoushui();
    String glbLianmengFanshui = detail.getGlbLianmengFanshui();
    String glbLianmengBXJiaoshou = detail.getGlbLianmengBXJiaoshou();
    String glbLianmengBXZhancheng = detail.getGlbLianmengBXZhancheng();
    String clubHeji = NumUtil.getSum(glbLianmengDaiShoushui, glbLianmengFanshui, glbLianmengBXJiaoshou
        ,glbLianmengBXZhancheng);
    return NumUtil.digit2(clubHeji);
  }

  private String getZhanjiChouqu(String yszj) {
    String zhanjieChouqu = StringUtils.contains(yszj, "-") ? "0" : NumUtil.getNum(yszj) * 0.05 + "";
    return NumUtil.digit2(zhanjieChouqu);
  }


  /**
   * 获取俱乐部汇总记录
   *
   * @param type 1计算单个俱乐部表记录  2计算所有俱乐部的合计记录
   */
  private GlbInfo getSumGlbInfo(List<GlbInfo> details, int type) {
    double baoxianChouqu = 0;
    double zhanjiChouqu = 0;
    double chouquHeji = 0;
    double daiShoushui = 0;
    double fanshui = 0;
    double baoxianJiaoshou = 0;
    double baoxianZhancheng = 0;
    double clubHeji = 0;
    for (GlbInfo detail : details) {
      baoxianChouqu += NumUtil.getNum(detail.getGlbBaoxianChouqu());
      zhanjiChouqu += NumUtil.getNum(detail.getGlbZhanjiChouqu());
      chouquHeji += (baoxianChouqu + zhanjiChouqu);
      daiShoushui += NumUtil.getNum(detail.getGlbLianmengDaiShoushui());
      fanshui += NumUtil.getNum(detail.getGlbLianmengFanshui());
      baoxianJiaoshou += NumUtil.getNum(detail.getGlbLianmengBXJiaoshou());
      baoxianZhancheng += NumUtil.getNum(detail.getGlbLianmengBXZhancheng());
      clubHeji += NumUtil.getNum(detail.getGlbClubHeji());
    }
    GlbInfo clubInfo = new GlbInfo();
    clubInfo.setGlbBaoxianChouqu(digit(baoxianChouqu));
    clubInfo.setGlbZhanjiChouqu(digit( zhanjiChouqu));
    clubInfo.setGlbChouquHeji(digit( chouquHeji));
    clubInfo.setGlbLianmengDaiShoushui(digit(daiShoushui));
    clubInfo.setGlbLianmengFanshui(digit(fanshui));
    clubInfo.setGlbLianmengBXJiaoshou(digit(baoxianJiaoshou));
    clubInfo.setGlbLianmengBXZhancheng(digit(baoxianZhancheng ));
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

  private String digit(double val){
    return NumUtil.digit(val);
  }

  private String digit(String val){
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
    stage.setWidth(650);
    stage.setHeight(400);

    Scene scene = new Scene(stackPane);
    stage.setScene(scene);
    stage.show();
  }

  private void setColumns(TableView table) {
    List<TableColumn<GameInfo, String>> cols = Arrays.asList(
        getTableColumn("俱乐部ID", "gameClubId", 1),
        getTableColumn("俱乐部名称", "gameClubName", 1),
        getTableColumn("玩家ID", "gamePlayerId", 1),
        getTableColumn("玩家名称", "gamePlayerName", 1),
        getTableColumn("牌局", "gamePaiju", 1),
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
    clubInfo.setGameClubHeji(NumUtil.digit2(sumLianmengFencheng + sumClubFencheng + ""));
    if (type == 1) {
      clubInfo.setDetailList(details);
      clubInfo.setGameClubId(details.get(0).getGameClubId());
      clubInfo.setGameClubName(details.get(0).getGameClubName());
    } else {
      clubInfo.setDetailList(null);
      clubInfo.setGameClubName("合计");
      clubInfo.setGameClubId("-");
    }
    return clubInfo;
  }



}
