package com.kendy.controller;

import com.kendy.constant.Constants;
import com.kendy.db.DBUtil;
import com.kendy.entity.ClubStaticInfo;
import com.kendy.entity.TeamStaticInfo;
import com.kendy.entity.TotalInfo;
import com.kendy.enums.ColumnType;
import com.kendy.excel.ExportExcelTemplate;
import com.kendy.util.AlertUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.TimeUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    List<TeamStaticInfo> staticRecords = dbUtil.getStaticRecordsByClub(myController.getClubId(), null);

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
    for(Node node : radios){
      RadioButton radio = (RadioButton) node;
      String text = radio.getText();
      radio.setToggleGroup(lmGroup);
      radio.setUserData(text);
      if(Constants.LM1.equals(text)){
        radio.setSelected(true);
      }
    }
    lmGroup.selectedToggleProperty().addListener(e->{
        String radioLmType = (String) lmGroup.getSelectedToggle().getUserData();
        if (Constants.LM3.equals(radioLmType)) {
          // 清空
          clearData(tableTeamStatic, tableClubStatic);

        } else{
          refresh();// 刷新
        }
    });
  }

  /** 获取当前选择的联盟**/
  private String getSelectedLM(){
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
      TableView<TeamStaticInfo> table = new TableView<>();
      for (TableColumn column : tableTeamStatic.getColumns()) {
        table.getColumns().add(getTableColumnCommon(
            column.getText(), column.getId(), ColumnType.COLUMN_RED));
      }

      table.setEditable(false);
      setDoubleClick(table);

      // 获取值
      List<TeamStaticInfo> list = dbUtil.getStaticRecordsByTeam(clubId, teamId);
      table.getItems().addAll(list);
      table.getSelectionModel().clearSelection();

      // 设置总金额
      final VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.setPadding(new Insets(2, 0, 0, 0));
      vbox.getChildren().addAll(table);

      if (stage == null) { // 共享一个舞台
        stage = new Stage();
      }
      // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
      ShowUtil.setIcon(stage);
      stage.setTitle(teamId + "的每天汇总");
      stage.setWidth(700);
      stage.setHeight(450);

      Scene scene = new Scene(vbox);
      stage.setScene(scene);
      stage.show();
    }
  }



  /**
   * 显示团队某天的具体记录
   * @param teamId
   * @param softTime
   */
  private void viewDetailGameRecord(String teamId, String softTime) {
    TableView<TotalInfo> table = new TableView<>();
    for (TableColumn column : myController.tableTotalInfo.getColumns()) {
      table.getColumns().add(getTotalInfoColumn(
          column.getText(), column.getId(), ColumnType.COLUMN_RED));
    }

    table.setEditable(false);

    // 获取值
    List<TotalInfo> list = dbUtil
        .getStaticDetailRecords(myController.getClubId(), teamId, softTime);
    table.getItems().addAll(list);
    table.getSelectionModel().clearSelection();

    // 设置总金额
    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(2, 0, 0, 0));
    vbox.getChildren().addAll(table);

    /*if (stage == null) { // 共享一个舞台
      stage = new Stage();
    }*/
    Stage detailStage = new Stage();
    ShowUtil.setIcon(detailStage);
    detailStage.setTitle(teamId + "团队" + softTime);
    detailStage.setWidth(970);
    detailStage.setHeight(450);

    Scene scene = new Scene(vbox);
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
      TableView<ClubStaticInfo> table = new TableView<>();
      for (TableColumn column : tableClubStatic.getColumns()) {
        String colName = column.getText();
        String replaceStr = "开始";
        if(StringUtils.contains(colName, replaceStr)){
          colName = colName.replace(replaceStr, "");
        }
        table.getColumns().add(getTableClubColumn(
            colName, column.getId(), ColumnType.COLUMN_RED));
      }

      table.setEditable(false);
      //setDoubleClick(table);

      // 获取值
      List<ClubStaticInfo> list = dbUtil.getClubEveryDayStatic(getSelectedLM(), clubId);
      table.getItems().addAll(list);
      table.getSelectionModel().clearSelection();

      // 设置总金额
      final VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.setPadding(new Insets(2, 0, 0, 0));
      vbox.getChildren().addAll(table);

      if (subTeamStage == null) { // 共享一个舞台
        subTeamStage = new Stage();
      }
      // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
      ShowUtil.setIcon(subTeamStage);
      subTeamStage.setTitle(clubName);
      subTeamStage.setWidth(700);
      subTeamStage.setHeight(450);

      Scene scene = new Scene(vbox);
      subTeamStage.setScene(scene);
      subTeamStage.show();
    }
  }
  //===============================================右边俱乐部双击时显示拥有的团队合计【结束】


  @FXML
  void exportTeamStaticAction(ActionEvent event) {
    if (TableUtil.isHasValue(tableTeamStatic)) {
      ObservableList<TeamStaticInfo> list = tableTeamStatic.getItems();
      String name = "团队汇总" + TimeUtil.getDateTime();
      try {
        String[] rowsName = new String[]{"联盟名称", "团家ID", "开始统计时间", "总战绩", "总回水", "总回保", "总人数",
            "总输赢"};
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
          objs[7] = info.getSumProfit();
          dataList.add(objs);
        }
        List<Integer> columnWidths =
            Arrays.asList(4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000);
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

  // ===================================================

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

  private TableColumn<TotalInfo, String> getTotalInfoColumn(String colName, String colVal,
      ColumnType columnType) {
    TableColumn<TotalInfo, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(85);
    col.setCellValueFactory(new PropertyValueFactory<TotalInfo, String>(colVal));
    if (columnType == ColumnType.COLUMN_RED) {
      col.setCellFactory(myController.getColorCellFactory(new TotalInfo()));
    }
    col.setSortable(false);
    return col;
  }

  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
