package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.kendy.constant.Constants;
import com.kendy.customize.MyTable;
import com.kendy.db.DBService;
import com.kendy.entity.Club;
import com.kendy.entity.GameInfo;
import com.kendy.entity.GlbInfo;
import com.kendy.entity.lmbcontribute.GameContributeInfo;
import com.kendy.entity.lmbcontribute.GlbContributeInfo;
import com.kendy.model.lmbcontribute.lmb.ClubContributeInfomation;
import com.kendy.model.lmbcontribute.lmb.LmbContributeCache;
import com.kendy.util.DateTimeUtils;
import com.kendy.util.ErrorUtil;
import com.kendy.util.MaskerPaneUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 联盟币贡献值控制类
 */
@Component
public class LMBContributeController extends BaseController implements Initializable {

  @Autowired
  LMBController lmbController;

  @Autowired
  DBService dbService;

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
  private TableColumn<GlbContributeInfo, String> glbGudong;
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
  private TableColumn<GameContributeInfo, String> gameGudong;
  @FXML
  private TableColumn<GameContributeInfo, String> gameUserRate;
  @FXML
  private TableColumn<GameContributeInfo, String> gameContribute;

  //=================================
  @FXML
  private StackPane stackPane; // 遮罩层使用

  private static final String ZERO_PERCENT = "0%";
  private static final String LMB_CONTRIBUTE_NAME = "lmb_contribute";
  // 缓存数据
  private LmbContributeCache lmbCache;
  // 查看视图
  private Stage stage;

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

    // 加载缓存
    loadCache();

    // 表格赋值
    setTableData();
  }


  @FXML
  public void exportTableGlbAction() {
    new MyTable<GlbContributeInfo>(tableGlb,"俱乐部联盟币贡献值" + DateTimeUtils.formatBasic())
        .exportByTable();
  }


  @FXML
  public void exportTableGameAction() {
    new MyTable<GameContributeInfo>(tableGame, "小游戏联盟币贡献值" + DateTimeUtils.formatBasic())
        .exportByTable();
  }

  /**
   * 设置表格数据
   */
  private void setTableData() {
    logger.info("从联盟币贡献值板块刷新联盟币板块...");
    // 先刷新联盟币表
    lmbController.doRefresh();
    // 俱乐部数据源
    List<GlbContributeInfo> glbList = new ArrayList<>();
    for (GlbInfo item : lmbController.tableGlb.getItems()) {
      if (!StringUtils.equals(Constants.HE_JI, item.getGlbClubName())) {
        GlbContributeInfo info = new GlbContributeInfo();
        ClubContributeInfomation cache = getClubInmationFromCache(item.getGlbClubId());
        if (cache == null) {
          continue;
        }
        String heji = StringUtils.defaultString(item.getGlbChouquHeji(), Constants.ZERO);
        String userRate = cache.getGlbUserRate();
        info.setGlbClubId(item.getGlbClubId());
        info.setGlbClubName(item.getGlbClubName());
        info.setGlbBaoxianChouqu(item.getGlbBaoxianChouqu());
        info.setGlbZhanjiChouqu(item.getGlbZhanjiChouqu());
        info.setGlbChouquHeji(heji);
        info.setGlbGudong(cache.getGudong());
        info.setGlbUserRate(userRate);
        info.setGlbContribute(NumUtil.digit(NumUtil.getNumTimes(heji, userRate)));
        glbList.add(info);
      }
    }
    //小游戏数据源
    List<GameContributeInfo> gameList = new ArrayList<>();
    for (GameInfo item : lmbController.tableGame.getItems()) {
      if (!StringUtils.equals(Constants.HE_JI, item.getGameClubName())) {
        GameContributeInfo info = new GameContributeInfo();
        ClubContributeInfomation cache = getClubInmationFromCache(item.getGameClubId());
        if (cache == null) {
          continue;
        }
        String fencheng = item.getGameLianmengFencheng();
        String userRate = cache.getGameUserRate();
        info.setGameClubId(item.getGameClubId());
        info.setGameClubName(item.getGameClubName());
        info.setGameLianmengFencheng(fencheng);
        info.setGameGudong(cache.getGudong());
        info.setGameUserRate(userRate);
        info.setGameContribute(NumUtil.digit(NumUtil.getNumTimes(fencheng, userRate)));
        gameList.add(info);
      }
    }
    // 赋值两个表
    this.tableGlb.setItems(FXCollections.observableList(glbList));
    this.tableGame.setItems(FXCollections.observableList(gameList));
  }


  /**
   * 从缓存中获取单个俱乐部信息
   * <p>
   * Note: it will return null when club is not exist!
   * @see #saveIfNull
   * @return
   */
  private ClubContributeInfomation getClubInmationFromCache(String clubid) {
    for (ClubContributeInfomation clubInfomation : lmbCache.getClubContributeInfomations()) {
      if (StringUtils.equals(clubid, clubInfomation.getClubId())) {
        return clubInfomation;
      }
    }
    return saveIfNull(clubid);
  }


  private ClubContributeInfomation saveIfNull(String clubid) {
    Club club = dbService.getAllClub().get(clubid);
    if (club == null) {
      logger.error("俱乐部ID不存在：{}", clubid);
      return null;
    }
    ClubContributeInfomation info = new ClubContributeInfomation();
    info.setClubId(clubid);
    info.setClubName(club.getName());
    info.setGudong(club.getGudong());
    // 添加到缓存
    lmbCache.getClubContributeInfomations().add(info);
    return info;
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

          // 从联盟币板块获取数据
          setTableData();
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
   * 从数据库加载
   */
  private void loadCache() {
    LmbContributeCache _lmbCache = null;
    try {
      String cacheJson = dbService.getValueByKey(LMB_CONTRIBUTE_NAME);
      _lmbCache = JSON.parseObject(cacheJson, LmbContributeCache.class);
      if (_lmbCache != null) {
        lmbCache = _lmbCache; // 设置值
      }
    } catch (Exception e) {
      ErrorUtil.err("联盟币贡献值加载配置项失败");
    }
  }


  /**
   * 双击行弹框
   */
  private void openBaseView(boolean isTableGlb, String clubId, String clubName, String oldRate) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(clubName);
    dialog.setGraphic(null);
    dialog.setHeaderText(null);
    dialog.setContentText("俱乐部比例%:");
    ShowUtil.setIcon(dialog);
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      if (!StringUtils.contains(result.get(), "%")) {
        ShowUtil.show("请以百分号结尾!");
        return;
      }
      String newRate = StringUtils.defaultString(result.get(), ZERO_PERCENT);
      if (StringUtils.equals(newRate, oldRate)) {
        return;
      }
      // 更新缓存
      for (ClubContributeInfomation clubCahe : lmbCache.getClubContributeInfomations()) {
        if (StringUtils.equals(clubId, clubCahe.getClubId())) {
          if (isTableGlb) {
            clubCahe.setGlbUserRate(newRate);
          } else {
            clubCahe.setGameUserRate(newRate);
          }
        }
      }
      // 持久化缓存
      saveCache();
      // 刷新界面
      refreshAction();
      ShowUtil.show("设置成功", 2);
      logger.info("修改俱乐部{}用户比例：旧比例为{}, 新比例为{}", clubName, oldRate, newRate);
    }
  }

  /**
   * 持久化缓存
   */
  private void saveCache() {
    dbService.saveOrUpdateOthers(LMB_CONTRIBUTE_NAME, JSON.toJSONString(lmbCache));
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
    openBaseView(true, item.getGlbClubId(), item.getGlbClubName(), item.getGlbUserRate());
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
    openBaseView(false, item.getGameClubId(), item.getGameClubName(), item.getGameUserRate());
  }

  private void addGameSumLine(List<GameInfo> detailList, GameInfo item) {
    if (item != null) {
      if (detailList == null) {
        return;
      }
      boolean isContaisHeji = detailList.stream().map(GameInfo::getGameType)
          .anyMatch(e -> StringUtils.equals(Constants.HE_JI, e));
      if (isContaisHeji) {
        return; // 若已存在合计，则不再添加
      } else {
        // 计算总和
        GameInfo sumInfo = new GameInfo();
        BeanUtils.copyProperties(item, sumInfo);
        sumInfo.setGamePlayerId("-");
        sumInfo.setGamePlayerName("-");
        sumInfo.setGamePaiju("-");
        sumInfo.setGameType(Constants.HE_JI);
        detailList.add(sumInfo);
      }
    }
  }

}
