package com.kendy.controller;

import com.jfoenix.controls.JFXButton;
import com.kendy.constant.Constants;
import com.kendy.customize.MyTable;
import com.kendy.db.DBUtil;
import com.kendy.entity.ClubStaticInfo;
import com.kendy.entity.TeamStaticInfo;
import com.kendy.entity.TotalInfo2;
import com.kendy.entity.ZjClubStaticDetailInfo;
import com.kendy.entity.ZjClubStaticInfo;
import com.kendy.entity.ZjTeamStaticDetailInfo;
import com.kendy.entity.ZjTeamStaticInfo;
import com.kendy.enums.ColumnColorType;
import com.kendy.excel.myExcel4j.MyExcelUtils;
import com.kendy.util.ColumnUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.MaskerPaneUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.TimeUtil;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.controlsfx.control.MaskerPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 战绩汇总控制类
 */
@Component
public class ZjStaticController extends BaseController implements Initializable {

  @Autowired
  private DBUtil dbUtil;

  @Autowired
  private MyController myController;


  @FXML
  private TableView<ZjTeamStaticInfo> tableTeamStatic; // 团队统计表

  @FXML
  private TableColumn<ZjTeamStaticInfo, String> teamClubId; // 俱乐部ID

  @FXML
  private TableColumn<ZjTeamStaticInfo, String> teamId; // 团队ID

  @FXML
  private TableColumn<ZjTeamStaticInfo, String> teamBeginStaticTime; // 开始统计时间

  @FXML
  private TableColumn<ZjTeamStaticInfo, String> teamPersonCount; // 人次


  // ====================== 俱乐部表
  @FXML
  private TableView<ZjClubStaticInfo> tableClubStatic; // 俱乐部表

  @FXML
  private TableColumn<ZjClubStaticInfo, String> clubName;

  @FXML
  private TableColumn<ZjClubStaticInfo, String> clubId; // 俱乐部ID

  @FXML
  private TableColumn<ZjClubStaticInfo, String> clubBeginStaticTime; // 俱乐部开始统计时间

  @FXML
  private TableColumn<ZjClubStaticInfo, String> clubPersonCount;

  //====================================================================
  @FXML
  private Label timeLabel;

  @FXML
  private StackPane stackPane;



  public static final String BEGIN = "开始";


  /**
   * DOM加载完后的事件
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.bindCellValueByTable(new ZjTeamStaticInfo(), tableTeamStatic);
    tableTeamStatic.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        ZjTeamStaticInfo item = getSelectedRow(tableTeamStatic);
        if (item != null && StringUtil.isAllNotBlank(item.getTeamId())) {
          viewTeamStatic();
        }
      }
    });

    super.bindCellValueByTable(new ZjClubStaticInfo(), tableClubStatic);  //
    tableClubStatic.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        ZjClubStaticInfo item = getSelectedRow(tableClubStatic);
        if (item != null && StringUtil.isAllNotBlank(item.getClubName())) {
          viewClubEverydayStatic();
        }
      }
    });


  }

  /**
   * 刷新数据
   */
  public void refresh() {
    System.out.println("ZjStaticController.refresh start....");
    // 更新时间
    timeLabel.setText(TimeUtil.getTimeString());

    // 清空界面数据
    clearData(tableTeamStatic);

    // 加载团队统计表
    loadTeamStaticView();

    // 加载俱乐部汇总
    loadClubStaticView();

    System.out.println("ZjStaticController.refresh finishes...");

  }

  /**
   * 加载团队统计表
   */
  private void loadTeamStaticView() {
    clearData(tableTeamStatic);
    // 加载原始数据
    String clubId = myController.getClubId();
    dbUtil.insertZjStaticData(clubId);
    // 从数据库加载统计数据
    List<ZjTeamStaticInfo> staticRecords = dbUtil.getZjTeamStaticsRecordsByClub(clubId);

    // 渲染到页面
    tableTeamStatic.getItems().addAll(staticRecords);
    tableTeamStatic.refresh();
  }

  /**
   * 加载所有俱乐部统计表
   */
  private void loadClubStaticView() {
    clearData(tableClubStatic);
    // 从数据库加载统计数据
    List<ZjClubStaticInfo> staticRecords = dbUtil.getZjClubTotalStatic();

    // 渲染到页面
    tableClubStatic.getItems().addAll(staticRecords);
    tableClubStatic.refresh();
  }

  /**
   * 查看团队的视图【左边统计】
   */
  private Stage stage;

  private void viewTeamStatic() {
    if (selectedItem(tableTeamStatic)) {
      ZjTeamStaticInfo item = getSelectedRow(tableTeamStatic);
      String clubId = myController.getClubId();
      String teamId = item.getTeamId();
      String TITLE = teamId + "团队的战绩统计";
      MyTable<ZjTeamStaticDetailInfo> table = new MyTable<>();

      setColumns(table);

      table.setEditable(false);

      // 获取值
      List<ZjTeamStaticDetailInfo> list = dbUtil.getZjTeamStaticsByTeamId(clubId, teamId);
      table.getItems().addAll(list);
      table.getSelectionModel().clearSelection();

      // 导出按钮
      table.setEntityClass(ZjTeamStaticDetailInfo.class);
      table.setExcelName(TITLE + TimeUtil.getDateTime());
      JFXButton exportBtn = getDownloadButn(table);

      StackPane stackPane = new StackPane();
      stackPane.getChildren().addAll(table, exportBtn);
      stackPane.setAlignment(Pos.BOTTOM_CENTER);

      if (stage == null) { // 共享一个舞台
        stage = new Stage();
      }
      // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
      ShowUtil.setIcon(stage);
      stage.setTitle(TITLE);
      stage.setWidth(550);
      stage.setHeight(400);

      Scene scene = new Scene(stackPane);
      stage.setScene(scene);
      stage.show();
    }
  }

  private void setColumns(TableView table) {
    List<TableColumn<ZjTeamStaticDetailInfo, String>> cols = Arrays.asList(
        getTableColumn("俱乐部ID", "detailClubId", 1),
        getTableColumn("团队ID", "detailTeamId", 1),
        getTableColumn("玩家ID", "detailPlayerId", 1),
        getTableColumn("玩家名称", "detailPlayerName", 1),
        getTableColumn("累计战绩", "detailPersonSumYszj", 1),
        getTableColumn("总人次", "detailPersonCount", 1)
    );
    table.getColumns().addAll(cols);
  }

  private void setClubColumns(TableView table) {
    List<TableColumn<ZjClubStaticDetailInfo, String>> cols = Arrays.asList(
        getTableColumn("序号", "detailClubIndex", 2),
        getTableColumn("俱乐部名称", "detailClubName", 2),
        getTableColumn("俱乐部ID", "detailClubId", 2),
        getTableColumn("玩家名称", "detailClubPlayerName", 2),
        getTableColumn("玩家ID", "detailClubPlayerId", 2),
        getTableColumn("开始统计时间", "detailClubBeginStaticTime", 2),
        getTableColumn("累计战绩", "detailClubTotalZJ", 2),
        getTableColumn("人次", "detailClubPersonCount", 2)
    );
    table.getColumns().addAll(cols);
  }

  private JFXButton getDownloadButn(MyTable<?> table) {
    JFXButton exportBtn = new JFXButton("导出");
    exportBtn.setStyle("-fx-background-color: #DAF2E3; -fx-font-size: 20");
    exportBtn.setPrefWidth(120);
    exportBtn.setPrefHeight(40);
    exportBtn.setId("exportBtn");
    exportBtn.setOnAction(e -> {
      if (TableUtil.isHasValue(table)) {
        try {
          logger.info("正在导出{}...", table.getExcelName());
          table.export();
          logger.info("导出完成{}" + table.getExcelName());
        } catch (Exception ee) {
          ErrorUtil.err("导出失败", ee);
        }
      }
    });
    return exportBtn;
  }



  //===============================================右边俱乐部双击时显示拥有的团队合计【开始】

  private Stage clubStage;

  private void viewClubEverydayStatic() {
    if (selectedItem(tableClubStatic)) {
      ZjClubStaticInfo item = getSelectedRow(tableClubStatic);
      String clubId = item.getClubId();
      String TITLE = item.getClubName().replaceAll("[?|/]", "") + "的战绩统计";
      MyTable<ZjClubStaticDetailInfo> table = new MyTable<>();

      setClubColumns(table);

      table.setEditable(false);

      // 获取值
      List<ZjClubStaticDetailInfo> list = dbUtil.getZjClubStaticDetail(clubId);
      table.getItems().addAll(list);
      table.getSelectionModel().clearSelection();

      // 导出按钮
      table.setEntityClass(ZjClubStaticDetailInfo.class);
      table.setExcelName(TITLE + TimeUtil.getDateTime());
      JFXButton exportBtn = getDownloadButn(table);

      StackPane stackPane = new StackPane();
      stackPane.getChildren().addAll(table, exportBtn);
      stackPane.setAlignment(Pos.BOTTOM_CENTER);

      // 新开一个舞台
      if (clubStage == null) {
        clubStage = new Stage();
      }
      // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
      ShowUtil.setIcon(clubStage);
      clubStage.setTitle(TITLE);
      clubStage.setWidth(740);
      clubStage.setHeight(400);

      Scene scene = new Scene(stackPane);
      clubStage.setScene(scene);
      clubStage.show();
    }
  }


  /**
   * 获取动态数据表的列
   *
   * @param colName
   * @param colVal
   * @param type
   * @param <T>
   * @return
   */
  private <T> TableColumn<T, String> getTableColumn(String colName,
      String colVal, int type) {
    T t = (T)(type == 1 ?  new ZjTeamStaticDetailInfo() :  new ZjClubStaticDetailInfo());
    return ColumnUtil.getTableRedColumn(colName, colVal, t);
  }


  @FXML
  public void exportTeamExcelAction() {
    final TableView table = tableTeamStatic;
    String excelName = myController.getClubId()+"的团队战绩统计" + TimeUtil.getDateTime();
    try {
      MyExcelUtils.getInstance().exportExcel(table, ZjTeamStaticInfo.class, excelName);
      logger.info(excelName+"导出成功");
    } catch (Exception e) {
      ErrorUtil.err(excelName+"导出失败", e);
    }
  }


  @FXML
  public void refreshAction(){
    System.out.println("====================战绩统计刷新按钮");

    MaskerPaneUtil.addMaskerPane(stackPane);
    Task task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        Thread.sleep(1000);
        Platform.runLater(()->{
          // 处理业务逻辑
          refresh();
        });
        return null;
      }
      @Override
      protected void succeeded(){
        super.succeeded();
        MaskerPaneUtil.hideMaskerPane(stackPane);
      }
    };
    new Thread(task).start();

  }



  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
