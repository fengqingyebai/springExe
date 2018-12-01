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
import com.kendy.enums.ColumnType;
import com.kendy.excel.ExportExcelTemplate;
import com.kendy.excel.myExcel4j.MyExcelUtils;
import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import com.kendy.util.AlertUtil;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.TimeUtil;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
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
    // 清空界面数据
    clearData(tableTeamStatic);

    // 加载团队统计表
    loadTeamStaticView();

    // 加载俱乐部汇总
    loadClubStaticView();

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




  private void setDoubleClick(TableView<TeamStaticInfo> table) {
    table.setOnMouseClicked(e -> {
      // 行双击
//      if (e.getClickCount() == 2) {
//        TeamStaticInfo item = getSelectedRow(table);
//        if (item != null && StringUtil.isAllNotBlank(item.getTeamId())) {
//          String teamId = item.getTeamId();
//          String staticTime = item.getStaticTime();
//          logger.info("查询历史汇总中的当天记录" + staticTime + "...");
//          viewDetailGameRecord(teamId, staticTime);
//        }
//      }
    });
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

  private void setColumns(TableView table){
    List<TableColumn<ZjTeamStaticDetailInfo, String>> cols = Arrays.asList(
        getTableColumn("俱乐部ID", "detailClubId"),
        getTableColumn("团队ID", "detailTeamId"),
        getTableColumn("玩家ID", "detailPlayerId"),
        getTableColumn("玩家名称", "detailPlayerName"),
        getTableColumn("累计战绩", "detailPersonSumYszj"),
        getTableColumn("总人次", "detailPersonCount")
    );
    table.getColumns().addAll(cols);
  }

  private void setClubColumns(TableView table){
    List<TableColumn<ZjClubStaticDetailInfo, String>> cols = Arrays.asList(
        getClubTableColumn("序号", "detailClubIndex"),
        getClubTableColumn("俱乐部名称", "detailClubName"),
        getClubTableColumn("俱乐部ID", "detailClubId"),
        getClubTableColumn("玩家名称", "detailPlayerName"),
        getClubTableColumn("玩家ID", "detailClubPlayerId"),
        getClubTableColumn("开始统计时间", "detailClubBeginStaticTime"),
        getClubTableColumn("累计战绩", "detailClubTotalZJ"),
        getClubTableColumn("人次", "detailClubPersonCount")
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


  /**
   * 显示团队某天的具体记录
   */
  private void viewDetailGameRecord(String teamId, String softTime) {
    MyTable<TotalInfo2> table = new MyTable<>();
    for (TableColumn column : myController.tableTotalInfo.getColumns()) {
      table.getColumns().add(getTotalInfo2Column(
          column.getText(), column.getId(), ColumnType.COLUMN_RED));
    }
    // 手动添加桌号一列
    TableColumn<TotalInfo2, String> talbeIdColumn = new TableColumn<>();
    talbeIdColumn.setId("tableId");
    talbeIdColumn.setText("桌号");
    table.getColumns().add(getTotalInfo2Column(
        talbeIdColumn.getText(), talbeIdColumn.getId(), ColumnType.COLUMN_RED));

    table.setEditable(false);

    // 获取值
    List<TotalInfo2> list = dbUtil
        .getStaticDetailRecords(myController.getClubId(), teamId, softTime);
    table.getItems().addAll(list);
    table.getSelectionModel().clearSelection();

    // 导出按钮
    table.setEntityClass(TotalInfo2.class);
    table.setExcelName(teamId + "团队" + softTime + "-" + TimeUtil.getDateTime());
    JFXButton exportBtn = getDownloadButn(table);

    StackPane stackPane = new StackPane();
    stackPane.getChildren().addAll(table, exportBtn);
    stackPane.setAlignment(Pos.BOTTOM_CENTER);

    /*if (stage == null) { // 共享一个舞台
      stage = new Stage();
    }*/
    Stage detailStage = new Stage();
    ShowUtil.setIcon(detailStage);
    detailStage.setTitle(teamId + "团队" + softTime);
    detailStage.setWidth(1055);
    detailStage.setHeight(500);

    Scene scene = new Scene(stackPane);
    detailStage.setScene(scene);
    detailStage.show();
  }

  //===============================================右边俱乐部双击时显示拥有的团队合计【开始】

  private Stage clubStage;
  private void viewClubEverydayStatic() {
    if (selectedItem(tableClubStatic)) {
      ZjClubStaticInfo item = getSelectedRow(tableClubStatic);
      String clubId = item.getClubId();
      String TITLE = item.getClubName() + "的战绩统计";
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
      if(clubStage == null){
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
  //===============================================右边俱乐部双击时显示拥有的团队合计【结束】

  // =============================================导出俱乐部汇总【开始】
  /**
   * 动态生成列
   *
   * @param columnType 红色和非红色
   */
  private TableColumn<ZjTeamStaticDetailInfo, String> getTableColumn(String colName, String colVal,
      ColumnType columnType) {
    TableColumn<ZjTeamStaticDetailInfo, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(85);
    col.setCellValueFactory(new PropertyValueFactory<ZjTeamStaticDetailInfo, String>(colVal));
    if (columnType == ColumnType.COLUMN_RED) {
      col.setCellFactory(myController.getColorCellFactory(new ZjTeamStaticDetailInfo()));
    }
    col.setSortable(false);
    return col;
  }

  private TableColumn<ZjClubStaticDetailInfo, String> getClubTableColumn(String colName, String colVal,
      ColumnType columnType) {
    TableColumn<ZjClubStaticDetailInfo, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(85);
    col.setCellValueFactory(new PropertyValueFactory<ZjClubStaticDetailInfo, String>(colVal));
    if (columnType == ColumnType.COLUMN_RED) {
      col.setCellFactory(myController.getColorCellFactory(new ZjClubStaticDetailInfo()));
    }
    col.setSortable(false);
    return col;
  }

  private TableColumn<ZjTeamStaticDetailInfo, String> getTableColumn(String colName, String colVal) {
    return getTableColumn(colName, colVal, ColumnType.COLUMN_RED);
  }

  private TableColumn<ZjClubStaticDetailInfo, String> getClubTableColumn(String colName, String colVal) {
    return getClubTableColumn(colName, colVal, ColumnType.COLUMN_RED);
  }



  /**
   * 动态生成列
   *
   * @param columnType 红色和非红色
   */
  private TableColumn<TeamStaticInfo, String> getTableColumnCommon(String colName, String colVal,
      ColumnType columnType) {
    TableColumn<TeamStaticInfo, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(85);
    col.setCellValueFactory(new PropertyValueFactory<TeamStaticInfo, String>(colVal));
    if (columnType == ColumnType.COLUMN_RED) {
      col.setCellFactory(myController.getColorCellFactory(new TeamStaticInfo()));
    }
    col.setSortable(false);
    return col;
  }

  private TableColumn<ClubStaticInfo, String> getTableClubColumn(String colName, String colVal,
      ColumnType columnType) {
    TableColumn<ClubStaticInfo, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(85);
    col.setCellValueFactory(new PropertyValueFactory<ClubStaticInfo, String>(colVal));
    if (columnType == ColumnType.COLUMN_RED) {
      col.setCellFactory(myController.getColorCellFactory(new ClubStaticInfo()));
    }
    col.setSortable(false);
    return col;
  }

  private TableColumn<TotalInfo2, String> getTotalInfo2Column(String colName, String colVal,
      ColumnType columnType) {
    TableColumn<TotalInfo2, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(85);
    col.setCellValueFactory(new PropertyValueFactory<TotalInfo2, String>(colVal));
    if (columnType == ColumnType.COLUMN_RED) {
      col.setCellFactory(myController.getColorCellFactory(new TotalInfo2()));
    }
    col.setSortable(false);
    return col;
  }

  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
