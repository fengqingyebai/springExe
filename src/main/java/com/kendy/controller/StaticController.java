package com.kendy.controller;

import com.jfoenix.controls.JFXButton;
import com.kendy.constant.Constants;
import com.kendy.customize.MyTable;
import com.kendy.db.DBUtil;
import com.kendy.entity.ClubStaticInfo;
import com.kendy.entity.TeamStaticInfo;
import com.kendy.entity.TotalInfo2;
import com.kendy.excel.ExportExcelTemplate;
import com.kendy.excel.myExcel4j.MyExcelUtils;
import com.kendy.util.AlertUtil;
import com.kendy.util.CollectUtil;
import com.kendy.util.ColumnUtil;
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
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 历史汇总控制类
 */
@Component
public class StaticController extends BaseController implements Initializable {

  @Autowired
  private DBUtil dbUtil;

  @Autowired
  private MyController myController;


  @FXML
  private TableView<TeamStaticInfo> tableTeamStatic; // 团队统计表

  @FXML
  private TableColumn<TeamStaticInfo, String> teamClubId; // 俱乐部ID

  @FXML
  private TableColumn<TeamStaticInfo, String> teamId; // 团队ID

  @FXML
  private TableColumn<TeamStaticInfo, String> sumZJ; // 总战绩

  @FXML
  private TableColumn<TeamStaticInfo, String> sumProfit; // 总输赢

  @FXML
  private TableColumn<TeamStaticInfo, String> staticTime; // 开始统计时间

  @FXML
  private TableColumn<TeamStaticInfo, String> sumPerson; // 总人数

  @FXML
  private TableColumn<TeamStaticInfo, String> sumChuhuishui; // 总回水

  @FXML
  private TableColumn<TeamStaticInfo, String> sumHuibao; // 总回保

  @FXML
  private TableColumn<TeamStaticInfo, String> teamFWF; // 团队服务费

  @FXML
  private TableColumn<TeamStaticInfo, String> teamProxyHBRate; // 团队代理回保比例

  @FXML
  private TableColumn<TeamStaticInfo, String> teamProxyHSRate; // 团队代理回水比例

  // ====================== 俱乐部表
  @FXML
  private TableView<ClubStaticInfo> tableClubStatic; // 俱乐部表

  @FXML
  private TableColumn<ClubStaticInfo, String> clubLmType;

  @FXML
  private TableColumn<ClubStaticInfo, String> clubName;

  @FXML
  private TableColumn<ClubStaticInfo, String> clubId; // 俱乐部ID

  @FXML
  private TableColumn<ClubStaticInfo, String> clubStaticTime; // 俱乐部ID

  @FXML
  private TableColumn<ClubStaticInfo, String> clubSumZJ; // 俱乐部总战绩

  @FXML
  private TableColumn<ClubStaticInfo, String> clubSumPerson; // 俱乐部总人数

  @FXML
  private TableColumn<ClubStaticInfo, String> clubSumProfit; // 俱乐部总输赢

  @FXML
  private TableColumn<ClubStaticInfo, String> clubSumBaoxian; // 俱乐部总保险

  // ==================================联盟选择框
  @FXML
  private HBox LmBox; // 联盟包装器

  ToggleGroup lmGroup = new ToggleGroup(); // 联盟选择

  public static final String BEGIN = "开始";


  /**
   * DOM加载完后的事件
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.bindCellValueByTable(new TeamStaticInfo(), tableTeamStatic);
    tableTeamStatic.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        TeamStaticInfo item = getSelectedRow(tableTeamStatic);
        if (item != null && StringUtil.isAllNotBlank(item.getTeamId())) {
          viewTeamStatic();
        }
      }
    });

    super.bindCellValueByTable(new ClubStaticInfo(), tableClubStatic);  //
    tableClubStatic.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        ClubStaticInfo item = getSelectedRow(tableClubStatic);
        if (item != null && StringUtil.isAllNotBlank(item.getClubName())) {
          viewClubEverydayStatic();
        }
      }
    });

    initLMRadios(); // 初始化联盟选项

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
    // 从数据库加载统计数据
    List<TeamStaticInfo> staticRecords = dbUtil
        .getStaticRecordsByClub(myController.getClubId(), null);

    // 渲染到页面
    tableTeamStatic.getItems().addAll(staticRecords);
    tableTeamStatic.refresh();
  }

  /**
   * 加载团队统计表
   */
  private void loadClubStaticView() {
    clearData(tableClubStatic);
    // 从数据库加载统计数据
    List<ClubStaticInfo> staticRecords = dbUtil.getClubTotalStatic(getSelectedLM());

    // 渲染到页面
    tableClubStatic.getItems().addAll(staticRecords);
    tableClubStatic.refresh();
  }


  /**
   * 初始化联盟点击框
   */
  private void initLMRadios() {

    ObservableList<Node> radios = LmBox.getChildren();
    for (Node node : radios) {
      RadioButton radio = (RadioButton) node;
      String text = radio.getText();
      radio.setToggleGroup(lmGroup);
      radio.setUserData(text);
      if (Constants.LM1.equals(text)) {
        radio.setSelected(true);
      }
    }
    lmGroup.selectedToggleProperty().addListener(e -> {
      String radioLmType = (String) lmGroup.getSelectedToggle().getUserData();
//      if (Constants.LM3.equals(radioLmType)) {
//        // 清空
//        clearData(tableTeamStatic, tableClubStatic);
//
//      } else {
//        refresh();// 刷新
//      }
      refresh();// 刷新
    });
  }

  /**
   * 获取当前选择的联盟
   **/
  private String getSelectedLM() {
    return (String) lmGroup.getSelectedToggle().getUserData();
  }


  @FXML
  void clearTeamStaticAction(ActionEvent event) {
    if (selectedItem(tableTeamStatic)) {
      TeamStaticInfo item = getSelectedRow(tableTeamStatic);
      if (AlertUtil.confirm("清空", "兄弟，确定要清空" + item.getTeamId() + "的历史记录吗？")) {
        // 清空数据
        int updateRows = dbUtil.clearTeamGameRecord(myController.getClubId(), item.getTeamId());

        // 刷新界面
        FXUtil.info("操作成功! 已清空了" + updateRows + "条记录！");
        refresh();
      }
    }
  }

  @FXML
  void clearClubStaticAction(ActionEvent event) {
    if (selectedItem(tableClubStatic)) {
      ClubStaticInfo item = getSelectedRow(tableClubStatic);
      String clubName = item.getClubName();
      String clubId = item.getClubId();
      if (AlertUtil.confirm(clubName, "兄弟，确定要清空" + clubName + "的历史记录吗？")) {
        // 清空数据
        int updateRows = dbUtil.clearClubGameRecord(getSelectedLM(), clubId);

        // 刷新界面
        FXUtil.info("操作成功! 已清空了" + updateRows + "条记录！");
        refresh();
      }
    }
  }


  private void setDoubleClick(TableView<TeamStaticInfo> table) {
    table.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        TeamStaticInfo item = getSelectedRow(table);
        if (item != null && StringUtil.isAllNotBlank(item.getTeamId())) {
          String teamId = item.getTeamId();
          String staticTime = item.getStaticTime();
          logger.info("查询历史汇总中的当天记录" + staticTime + "...");
          viewDetailGameRecord(teamId, staticTime);
        }
      }
    });
  }


  /**
   * 查看团队的视图【左边统计】
   */
  private Stage stage;

  private void viewTeamStatic() {
    if (selectedItem(tableTeamStatic)) {
      TeamStaticInfo item = getSelectedRow(tableTeamStatic);
      String clubId = myController.getClubId();
      String teamId = item.getTeamId();
      String TITLE = teamId + "的每天汇总";
      MyTable<TeamStaticInfo> table = new MyTable<>();
      for (TableColumn column : tableTeamStatic.getColumns()) {
        String colName = column.getText();
        if (StringUtils.contains(colName, BEGIN)) {
          colName = StringUtils.replace(colName, BEGIN, "");
        }
        table.getColumns().add(getTableColumn(
            colName, column.getId(), 1));
      }

      table.setEditable(false);
      setDoubleClick(table);

      // 获取值
      List<TeamStaticInfo> list = dbUtil.getStaticRecordsByTeam(clubId, teamId);
      table.getItems().addAll(list);
      table.getSelectionModel().clearSelection();

      // 导出按钮
      table.setEntityClass(TeamStaticInfo.class);
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
      stage.setWidth(960);
      stage.setHeight(300);

      Scene scene = new Scene(stackPane);
      stage.setScene(scene);
      stage.show();
    }
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
      table.getColumns().add(getTableColumn(
          column.getText(), column.getId(), 3));
    }
    // 手动添加桌号一列
    TableColumn<TotalInfo2, String> talbeIdColumn = new TableColumn<>();
    talbeIdColumn.setId("tableId");
    talbeIdColumn.setText("桌号");
    table.getColumns().add(getTableColumn(
        talbeIdColumn.getText(), talbeIdColumn.getId(), 3));

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
  private Stage subTeamStage;

  private void viewClubEverydayStatic() {
    if (selectedItem(tableClubStatic)) {
      ClubStaticInfo item = getSelectedRow(tableClubStatic);
      String clubName = item.getClubName();
      String clubId = item.getClubId();
      MyTable<ClubStaticInfo> table = new MyTable<>();
      for (TableColumn column : tableClubStatic.getColumns()) {
        String colName = StringUtils.defaultString(column.getText());
        colName = colName.replace(BEGIN, "").replace("总", "");

        table.getColumns().add(getTableColumn(colName, column.getId(), 2));
      }

      table.setEditable(false);
      //setDoubleClick(table);

      // 获取值
      List<ClubStaticInfo> list = dbUtil.getClubEveryDayStatic(getSelectedLM(), clubId);
      table.getItems().addAll(list);
      table.getSelectionModel().clearSelection();

      // 导出按钮
      table.setEntityClass(ClubStaticInfo.class);
      table.setExcelName(clubName.replaceAll("[?|/]","") + "每日汇总" + TimeUtil.getDateTime());
      JFXButton exportBtn = getDownloadButn(table);

      StackPane stackPane = new StackPane();
      stackPane.getChildren().addAll(table, exportBtn);
      stackPane.setAlignment(Pos.BOTTOM_CENTER);

      if (subTeamStage == null) { // 共享一个舞台
        subTeamStage = new Stage();
      }
      // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
      ShowUtil.setIcon(subTeamStage);
      subTeamStage.setTitle(clubName);
      subTeamStage.setWidth(700);
      subTeamStage.setHeight(450);

      Scene scene = new Scene(stackPane);
      subTeamStage.setScene(scene);
      subTeamStage.show();
    }
  }
  //===============================================右边俱乐部双击时显示拥有的团队合计【结束】

  // =============================================导出俱乐部汇总【开始】

  /**
   * 导出俱乐部汇总
   */
  @FXML
  public void exportAllClubExcelStaticAction(ActionEvent event) {
    // 获取数据
    String lmType = getSelectedLM();
    List<ClubStaticInfo> clubsExcelStatic = dbUtil.getClubsExcelStatic(lmType);
    if (CollectUtil.isEmpty(clubsExcelStatic)) {
      ShowUtil.show("当前无数据！！");
      return;
    }

    // 转成Map参数
    Map<String, String> excelParamsMap = getExcelParamsMap(clubsExcelStatic);

    // 基于模板导出Excel, 往Excel模板中填写数据
    String msg = "导出俱乐部模板Excel";
    String outputPath = new StringBuilder("D:/").append(lmType).append("-俱乐部导出")
        .append(TimeUtil.getDateTime()).append(".xlsx").toString();
    try {
      logger.info("开始" + msg + "...");
      MyExcelUtils.getInstance().exportObjects2Excel(TEMPLE_EXCEL_PATH, excelParamsMap, outputPath);
      java.awt.Desktop.getDesktop().open(new File(outputPath));
      logger.info(msg + "成功");
    } catch (Exception err) {
      ErrorUtil.err(msg + "失败", err);
    }

  }

  private Map<String, String> getExcelParamsMap(List<ClubStaticInfo> clubsExcelStatic) {
    //Map<String, String> map = getDefaultParamMap();
    Map<String, String> map = new HashMap<>();
        AtomicInteger count = new AtomicInteger(0);
    Map<String, List<ClubStaticInfo>> dataMap = clubsExcelStatic.stream()
        .collect(Collectors.groupingBy(e -> e.getClubName()));

    dataMap.forEach((lmName, clubList) -> {
      int index = count.getAndAdd(1);
      double sumZJ = clubList.stream().mapToDouble(e -> NumUtil.getNum(e.getClubSumZJ())).sum();
      double sumBaoxian = clubList.stream().mapToDouble(e -> NumUtil.getNum(e.getClubSumBaoxian()))
          .sum();
      double sumPerson = clubList.stream().mapToDouble(e -> NumUtil.getNum(e.getClubSumPerson()))
          .sum();
      double sumProfit = clubList.stream().mapToDouble(e -> NumUtil.getNum(e.getClubSumProfit()))
          .sum();
      map.put("lm_name_" + index + "_0", lmName);
      map.put("sum_jiaoshou_" + index + "_0", NumUtil.digit0(sumProfit));
      map.put("sum_baoxian_" + index + "_0", NumUtil.digit0(sumBaoxian));
      map.put("sum_renci_" + index + "_0", NumUtil.digit0(sumPerson));
      map.put("sum_zj_" + index + "_0", NumUtil.digit0(sumZJ));

      Collections.sort(clubList, Comparator.comparing(ClubStaticInfo::getClubStaticTime));
      int size = clubList.size();
      for (int i = 0; i < size; i++) {
        ClubStaticInfo info = clubList.get(i);
        map.put("static_time_" + index + "_" + i, info.getClubStaticTime());
        map.put("jiaoshou_" + index + "_" + i, info.getClubSumProfit());
        map.put("baoxian_" + index + "_" + i, info.getClubSumBaoxian());
        map.put("renci_" + index + "_" + i, info.getClubSumPerson());
        map.put("zj_" + index + "_" + i, info.getClubSumZJ());
      }
    });
    return map;
  }


  StringBuilder sb = new StringBuilder();
  private static final String BOTTOM_LINE = "_";
  private static final String TEMPLE_EXCEL_PATH = "/excel/俱乐部导出模板.xlsx";

  private String getKey(String key, int clubIndex, int rowIndex) {
    sb.setLength(0);
    return sb.append(key).append(BOTTOM_LINE).append(clubIndex).append(BOTTOM_LINE).append(rowIndex)
        .toString();
  }

  // =============================================导出俱乐部汇总【结束】


  @FXML
  public void exportTeamStaticAction(ActionEvent event) {
    if (TableUtil.isHasValue(tableTeamStatic)) {
      ObservableList<TeamStaticInfo> list = tableTeamStatic.getItems();
      String name = "团队汇总" + TimeUtil.getDateTime();
      try {
        String[] rowsName =
            new String[]{"联盟名称", "团家ID", "开始统计时间", "总战绩", "总回水", "总回保", "总人数", "服务费",
                "总输赢", "团保比例", "团水比例"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
          TeamStaticInfo info = list.get(i);
          objs = new Object[rowsName.length];
          objs[0] = info.getTeamClubId();
          objs[1] = info.getTeamId();
          objs[2] = info.getStaticTime();
          objs[3] = info.getSumZJ();
          objs[4] = info.getSumChuhuishui();
          objs[5] = info.getSumHuibao();
          objs[6] = info.getSumPerson();
          objs[7] = info.getTeamFWF();
          objs[8] = info.getSumProfit();
          objs[9] = info.getTeamProxyHBRate();
          objs[10] = info.getTeamProxyHSRate();
          dataList.add(objs);
        }
        List<Integer> columnWidths =
            Arrays.asList(4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000);
        ExportExcelTemplate ex = new ExportExcelTemplate(name, rowsName, columnWidths, dataList);
        ex.export();
        System.out.println("finises..");
      } catch (Exception e) {
        ErrorUtil.err("导出团队汇总Excecl失败", e);
      }
    } else {
      ShowUtil.show("数据表无数据，请检查！");
    }
  }

  /**
   * 获取动态列
   */
  private <T> TableColumn<T, String> getTableColumn(String colName,
      String colVal, int type) {
    T t = (T)(type == 1 ?  new TeamStaticInfo() : (type == 2 ? new ClubStaticInfo() : new TotalInfo2()));
    return ColumnUtil.getTableRedColumn(colName, colVal, t);
  }


  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
