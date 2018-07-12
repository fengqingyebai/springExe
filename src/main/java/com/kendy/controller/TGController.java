package com.kendy.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.application.Main;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.db.DBUtil;
import com.kendy.entity.ProxyTeamInfo;
import com.kendy.entity.TGCommentInfo;
import com.kendy.entity.TGCompanyModel;
import com.kendy.entity.TGFwfinfo;
import com.kendy.entity.TGKaixiaoInfo;
import com.kendy.entity.TGLirunInfo;
import com.kendy.entity.TGTeamInfo;
import com.kendy.entity.TGTeamModel;
import com.kendy.entity.TypeValueInfo;
import com.kendy.excel.ExportExcelTemplate;
import com.kendy.interfaces.Entity;
import com.kendy.service.TGExportExcelService;
import com.kendy.service.TGFwfService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.TgWaizhaiService;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.InputDialog;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

/**
 * 处理联盟配额的控制器
 * 
 * @author 林泽涛
 * @time 2017年11月24日 下午9:31:04
 */
public class TGController extends BaseController implements Initializable {


  private static Logger log = Logger.getLogger(TGController.class);

  // //=====================================================================
  @FXML
  public VBox TG_Company_VBox; // 托管公司（按钮）

  @FXML
  public VBox TG_Team_VBox; // 托管公司的内部托管团队

  @FXML
  private Label currentTGCompanyLabel; // 当前托管公司
  @FXML
  private Label currentTGTeamLabel; // 当前托管团队

  @FXML
  public TextField tgTeamHSRate; // 托管团队回水比例
  @FXML
  private TextField tgTeamHBRate; // 托管团队回保比例
  @FXML
  public TextField tgTeamFwfRate; // 托管团队服务费比例

  @FXML
  public TextField tgCompanyYajin; // 托管公司押金
  @FXML
  public TextField tgCompanyEdu; // 托管公司额度

  @FXML
  public TextField tgYifenhong; // 托管公司已分红
  @FXML
  public Label totalWaizhai; // 总外债
  @FXML
  public Label tgTotalProfit; // 总利润
  @FXML
  public Label tgAvailable; // 总可分配
  @FXML
  private Label tgTeamFwfLabel; // 团队服务费

  @FXML
  public CheckBox teamProxyCheckBox;// 是否托管团队代理


  // =====================================================================
  @FXML
  public TabPane tabs;

  // =====================================================================团队战绩表
  @FXML
  public TableView<TGTeamInfo> tableTGZhanji;
  @FXML
  private TableColumn<TGTeamInfo, String> tgPlayerId;
  @FXML
  private TableColumn<TGTeamInfo, String> tgPlayerName;
  @FXML
  private TableColumn<TGTeamInfo, String> tgYSZJ;
  @FXML
  private TableColumn<TGTeamInfo, String> tgZJ25;
  @FXML
  private TableColumn<TGTeamInfo, String> tgZJUnknow;
  @FXML
  private TableColumn<TGTeamInfo, String> tgProfit;
  @FXML
  private TableColumn<TGTeamInfo, String> tgHuiBao;
  @FXML
  private TableColumn<TGTeamInfo, String> tgBaoxian;
  @FXML
  private TableColumn<TGTeamInfo, String> tgChangci;

  // =====================================================================托管团队战绩总和表
  @FXML
  public TableView<TypeValueInfo> tableZJSum;
  @FXML
  private TableColumn<TypeValueInfo, String> tgZJSumType;
  @FXML
  private TableColumn<TypeValueInfo, String> tgZJSumValue;

  // =====================================================================托管团队映射表
  @FXML
  public TableView<TypeValueInfo> tableTGTeamRate;
  @FXML
  private TableColumn<TypeValueInfo, String> tgTeamId;
  @FXML
  private TableColumn<TypeValueInfo, String> tgTeamRate;

  // =====================================================================托管开销表
  @FXML
  public TableView<TGKaixiaoInfo> tableTGKaixiao;
  @FXML
  private TableColumn<TGKaixiaoInfo, String> tgKaixiaoDate;
  @FXML
  private TableColumn<TGKaixiaoInfo, String> tgKaixiaoPlayerName;
  @FXML
  private TableColumn<TGKaixiaoInfo, String> tgKaixiaoPayItem;
  @FXML
  private TableColumn<TGKaixiaoInfo, String> tgKaixiaoMoney;
  @FXML
  private TableColumn<TGKaixiaoInfo, String> tgKaixiaoCompany;
  @FXML
  public ListView<String> tgKaixiaoSumView; // 开销合计
  // =====================================================================托管玩家备注表
  @FXML
  public TableView<TGCommentInfo> tableTGComment;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentDate;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentPlayerId;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentPlayerName;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentType;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentId;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentName;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentBeizhu;
  @FXML
  private TableColumn<TGCommentInfo, String> tgCommentCompany;
  @FXML
  public ListView<String> tgCommentSumView; // 玩家备注合计

  // =====================================================================托管团队外债表
  @FXML
  public TableView<TypeValueInfo> tgWZTeam;
  @FXML
  private TableColumn<TypeValueInfo, String> tgWZTeamId;
  @FXML
  private TableColumn<TypeValueInfo, String> tgWZTeamValue;

  @FXML
  public HBox tgWZTeamHBox; // 存储动态的团队外债数据表

  // =====================================================================托管服务费表
  @FXML
  public TableView<TGFwfinfo> tableTGFwf;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfCompany;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfTeamId;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfHuishui;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfHuiBao;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfProfit;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfFanshui;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfFanbao;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfQuanshui;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfQuanbao;
  @FXML
  private TableColumn<TGFwfinfo, String> tgFwfHeji;


  // =====================================================================托管服务费总和表
  @FXML
  public TableView<TypeValueInfo> tableTGFwfSum;
  @FXML
  private TableColumn<TypeValueInfo, String> tgFwfType;
  @FXML
  private TableColumn<TypeValueInfo, String> tgFwfValue;

  // =====================================================================托管利润表表
  @FXML
  public TableView<TGLirunInfo> tableTGLirun;
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunDate;
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunTotalProfit;
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunTotalKaixiao;
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunATMCompany;
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunTGCompany;
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunTeamProfit;
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunRestHeji;// 合计
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunHeji;// 托管合计
  @FXML
  private TableColumn<TGLirunInfo, String> tgLirunCompanyName;// 托管公司


  private static final String TG_TEAM_RATE_DB_KEY = "tg_team_rate"; // 保存到数据库的key

  /**
   * DOM加载完后的事件
   */
  @SuppressWarnings("unchecked")
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 绑定列值属性
    MyController.bindCellValue(tgKaixiaoDate, tgKaixiaoPlayerName, tgKaixiaoPayItem, tgKaixiaoMoney,
        tgKaixiaoCompany);
    MyController.bindCellValue(tgCommentDate, tgCommentPlayerId, tgCommentPlayerName, tgCommentType,
        tgCommentId, tgCommentName, tgCommentBeizhu, tgCommentCompany);
    MyController.bindCellValue(tgFwfCompany, tgFwfTeamId, tgFwfHuishui, tgFwfHuiBao, tgFwfProfit,
        tgFwfFanshui, tgFwfFanbao, tgFwfQuanshui, tgFwfQuanbao, tgFwfHeji);
    MyController.bindCellValue(tgLirunDate, tgLirunTotalProfit, tgLirunTotalKaixiao,
        tgLirunATMCompany, tgLirunTGCompany, tgLirunTeamProfit, tgLirunRestHeji, tgLirunHeji,
        tgLirunCompanyName);
    binCellValueDiff(tgTeamId, "type");
    binCellValueDiff(tgTeamRate, "value");
    binCellValueDiff(tgZJSumType, "type");
    binCellValueDiff(tgZJSumValue, "value");
    binCellValueDiff(tgWZTeamId, "type");
    binCellValueDiff(tgWZTeamValue, "value");
    binCellValueDiff(tgFwfType, "type");
    binCellValueDiff(tgFwfValue, "value");
    MyController.bindCellValue(tgPlayerId, tgPlayerName, tgYSZJ, tgZJ25, tgZJUnknow, tgProfit,
        tgHuiBao, tgBaoxian, tgChangci);
    bindColorColumns(new TGTeamInfo(), tgYSZJ, tgZJ25, tgZJUnknow, tgProfit, tgHuiBao, tgBaoxian);
    bindColorColumns(new TGFwfinfo(), tgFwfHuishui, tgFwfHuiBao, tgFwfProfit, tgFwfFanshui,
        tgFwfFanbao, tgFwfQuanshui, tgFwfQuanbao, tgFwfHeji);
    bindColorColumns(new TGLirunInfo(), tgLirunTotalProfit, tgLirunTotalKaixiao, tgLirunATMCompany,
        tgLirunTGCompany, tgLirunTeamProfit, tgLirunRestHeji, tgLirunHeji);
    // tabs切换事件
    tabsAction();

    // 加载托管团队比例数据
    refreshTableTGTeam();

    // 加载托管公司数据
    loadDataLastest();

    // 点击代理CheckBox时的事件
    // initTeamProxyAction();
  }

  /**
   * 点击代理CheckBox时的事件 主要是修改当前托管团队的代理
   * 
   * @time 2018年3月21日
   */
  // private void initTeamProxyAction() {
  // teamProxyCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
  // public void changed(ObservableValue<? extends Boolean> ov,
  // Boolean old_val, Boolean new_val) {
  // ShowUtil.show(new_val + "", 1);
  // }
  // });
  // }

  private <T> void binCellValueDiff(TableColumn<T, String> column, String bindName) {
    try {
      column.setStyle("-fx-alignment: CENTER;");
      column.setCellValueFactory(new PropertyValueFactory<T, String>(bindName));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 通用红色列
   * 
   * @time 2018年3月17日
   * @param entity
   * @param columns
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void bindColorColumns(Entity entity, TableColumn<? extends Entity, String>... columns) {
    for (TableColumn column : columns)
      column.setCellFactory(getColorCellFactory(entity));
  }


  /**
   * tabs切换事件
   * 
   */
  private void tabsAction() {
    tabs.getSelectionModel().selectedItemProperty().addListener(info -> {
      // Tab tab = (Tab)info;
      refreshSmallTabData();
    });
  }

  /**
   * 选择托管公司按钮 或 手动点击小tab时自动刷新界面数据
   * 
   * @time 2018年3月16日
   */
  private void refreshSmallTabData() {
    String selectedTab = tabs.getSelectionModel().getSelectedItem().getText().trim();
    log.info(" newTab:" + selectedTab);
    if ("开销".equals(selectedTab)) {
      refreshTableTGKaixiao();// 刷新
    }
    if ("玩家备注".equals(selectedTab)) {
      refreshTableTGComment();// 刷新
    }
    if ("托管外债".equals(selectedTab)) {
      refreshTabTGWaizhai();// 刷新
    }
    if ("服务费明细".equals(selectedTab)) {
      refreshFwfTab();
    }
    if ("月利润".equals(selectedTab)) {
      refreshProfitTab();
    }
  }

  private void refreshFwfTab() {
    TGFwfService tgFwfService = new TGFwfService();
    tgFwfService.setFwfDetail(StringUtil.nvl(currentTGCompanyLabel.getText(), ""), tableTGFwf,
        tableTGFwfSum);
  }

  /**
   * 获取当前托管公司的值
   * 
   * @time 2018年3月4日
   * 
   * @return
   */
  public String getCurrentTGCompany() {
    return currentTGCompanyLabel.getText();
  }

  /**
   * 打开对话框
   * 
   * @param path fxml名称
   * @param title 对话框标题
   * @param windowName 对话框关闭时的名称
   */
  public void openBasedDialog(String path, String title, String windowName) {
    try {
      if (DataConstans.framesNameMap.get(windowName) == null) {
        // 打开新对话框
        String filePath = "/dialog/" + path;
        Parent root = FXMLLoader.load(getClass().getResource(filePath));
        Stage addNewPlayerWindow = new Stage();
        Scene scene = new Scene(root);
        addNewPlayerWindow.setTitle(title);
        addNewPlayerWindow.setScene(scene);
        try {
          addNewPlayerWindow.getIcons()
              .add(new javafx.scene.image.Image("file:resource/images/icon.png"));
        } catch (Exception e) {
          log.debug("找不到icon图标！");
          e.printStackTrace();
        }
        addNewPlayerWindow.show();
        // 缓存该对话框实例
        DataConstans.framesNameMap.put(windowName, addNewPlayerWindow);
        addNewPlayerWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
            DataConstans.framesNameMap.remove(windowName);
          }
        });

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * 新增托管公司
   * 
   * @time 2018年3月2日
   * @param event
   */
  public void addNewTGCompanyAction(ActionEvent event) {
    openBasedDialog("TG_add_company_frame.fxml",
        "新增托管公司(当前俱乐部" + MyController.currentClubId.getText() + ")", Constants.ADD_COMPANY_FRAME);
  }



  /**
   * 新增托管开销
   * 
   * @time 2018年3月3日
   * @param event
   */
  public void addKaixiaoAction(ActionEvent event) {
    openBasedDialog("TG_add_kaixiao_frame.fxml", "新增托管开销", Constants.ADD_TG_KAIXIAAO_FRAME);
  }

  /**
   * 新增托管开销
   */
  public void addPlayerCommentAction(ActionEvent event) {
    openBasedDialog("TG_add_player_comment_frame.fxml", "新增玩家备注", Constants.ADD_TG_KAIXIAAO_FRAME);
  }

  /**
   * 删除开销记录
   */
  public void delTGKaixiaoAction(ActionEvent event) {
    TGKaixiaoInfo selectedItem = tableTGKaixiao.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      ShowUtil.show("先选择要删除的托管开销记录！");
    } else {
      // 同步到数据库
      DBUtil.del_tg_kaixiao_by_id(selectedItem.getTgKaixiaoEntityId());
      refreshTableTGKaixiao();
      ShowUtil.show("操作完成 ", 1);
    }
  }


  /**
   * 删除玩家备注记录
   */
  public void delTGCommentAction(ActionEvent event) {
    TGCommentInfo selectedItem = tableTGComment.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      ShowUtil.show("先选择要删除的托管玩家备注记录！");
    } else {
      // 同步到数据库
      DBUtil.del_tg_comment_by_id(selectedItem.getTgCommentEntityId());
      refreshTableTGComment();
      ShowUtil.show("操作完成 ", 1);
    }
  }

  /**
   * 刷新托管开销表
   */
  public void refreshTableTGKaixiao() {

    // 从数据库获取最新数据
    List<TGKaixiaoInfo> tgKaixiaoList = DBUtil.get_all_tg_kaixiao();
    String company = currentTGCompanyLabel.getText();
    if (StringUtil.isAnyBlank(company)) {
      ShowUtil.show("请选择托管公司！");
    } else if (CollectUtil.isHaveValue(tgKaixiaoList)) {
      tgKaixiaoList = tgKaixiaoList.stream()
          .filter(info -> company.equals(info.getTgKaixiaoCompany())).collect(Collectors.toList());
    }

    // 过滤某个托管公司 TODO

    // 赋值
    ObservableList<TGKaixiaoInfo> obList;
    ObservableList<String> sumObList;
    if (CollectUtil.isNullOrEmpty(tgKaixiaoList)) {
      obList = FXCollections.observableArrayList();
      sumObList = FXCollections.observableArrayList();
    } else {
      obList = FXCollections.observableArrayList(tgKaixiaoList);
      /**** 计算合计 ****/
      // 1 按类别划分 （金币、打牌奖励、推荐奖励...）
      Map<String, List<TGKaixiaoInfo>> typeMap =
          tgKaixiaoList.stream().collect(Collectors.groupingBy(TGKaixiaoInfo::getTgKaixiaoPayItem));
      List<String> sumList = new ArrayList<>();
      int sum = tgKaixiaoList.stream()
          .mapToInt(info -> NumUtil.getNum(info.getTgKaixiaoMoney()).intValue()).sum();
      sumList.add("开销合计:" + sum);

      typeMap.forEach((name, list) -> {
        sumList.add(name + ": " + NumUtil.digit0(list.stream().map(TGKaixiaoInfo::getTgKaixiaoMoney)
            .mapToDouble(NumUtil::getNum).sum()));
      });
      sumList.add("开销次数:" + tgKaixiaoList.size());

      // 添加为0的情况
      List<String> payItems = TGAddKaixiaoController.payItems;
      if (CollectUtil.isHaveValue(payItems)) {
        payItems.stream().filter(item -> typeMap.get(item) == null)
            .forEach(item -> sumList.add(item + ": 0"));
      }
      // 2 计算服务费
      sumObList = FXCollections.observableArrayList(sumList);
    }
    tableTGKaixiao.setItems(obList);
    tableTGKaixiao.refresh();
    tgKaixiaoSumView.setItems(sumObList);
    tgKaixiaoSumView.refresh();

  }


  /**
   * 刷新托管玩家备注表
   */
  public void refreshTableTGComment() {
    // 从数据库获取最新数据
    List<TGCommentInfo> tgCommentList = DBUtil.get_all_tg_comment();
    // 过滤某个托管公司 TODO
    String company = currentTGCompanyLabel.getText();
    if (StringUtil.isAnyBlank(company)) {
      ShowUtil.show("请选择托管公司！");
    } else if (CollectUtil.isHaveValue(tgCommentList)) {
      tgCommentList = tgCommentList.stream()
          .filter(info -> company.equals(info.getTgCommentCompany())).collect(Collectors.toList());
    }

    // 赋值
    ObservableList<TGCommentInfo> obList;
    ObservableList<String> sumObList;
    if (CollectUtil.isNullOrEmpty(tgCommentList)) {
      obList = FXCollections.observableArrayList();
      sumObList = FXCollections.observableArrayList();
    } else {
      obList = FXCollections.observableArrayList(tgCommentList);
      /**** 计算合计 ****/
      // 按类别划分 （改号、更换帐号、推荐玩家...）
      Map<String, List<TGCommentInfo>> typeMap =
          tgCommentList.stream().collect(Collectors.groupingBy(TGCommentInfo::getTgCommentType));
      List<String> sumList = new ArrayList<>();
      typeMap.forEach((name, list) -> {
        sumList.add(name + ": " + list.size());
      });
      // 添加为0的情况
      List<String> items = TGAddCommentController.typeItems;
      if (CollectUtil.isHaveValue(items)) {
        items.stream().filter(item -> typeMap.get(item) == null)
            .forEach(item -> sumList.add(item + ": 0"));
      }

      sumObList = FXCollections.observableArrayList(sumList);
    }
    tableTGComment.setItems(obList);
    tableTGComment.refresh();
    tgCommentSumView.setItems(sumObList);
    tgCommentSumView.refresh();

  }


  /**
   * 加载最新的数据
   */
  public void loadDataLastest() {
    // 清空数据

    // 获取数据
    List<TGCompanyModel> tgCompanys = DBUtil.get_all_tg_company();

    if (CollectUtil.isHaveValue(tgCompanys)) {
      // 获取特定结构 {托管公司 ：｛ 团队名称 ： 团队数据列表 ｝｝ TODO

      ObservableList<Node> companyButtons = TG_Company_VBox.getChildren();
      TG_Company_VBox.setPrefWidth(120);
      if (CollectUtil.isHaveValue(companyButtons)) {
        companyButtons.clear();
      }
      tgCompanys = tgCompanys.stream()
          .filter(info -> MyController.currentClubId.getText().equals(info.getClubId()))
          .collect(Collectors.toList());
      tgCompanys.forEach(company -> {
        Button companyBtn = getCompanyButton(company);
        companyButtons.add(companyBtn);
      });
    }
  }

  /**
   * 获取一个动态的公司按钮
   * 
   * @time 2018年3月7日
   * @param companyEntity
   * @return
   */
  private Button getCompanyButton(TGCompanyModel companyEntity) {
    String company = companyEntity.getTgCompanyName();
    List<String> teamList = companyEntity.getTgTeamList();
    Button companyBtn = new Button(company);
    companyBtn.setPrefWidth(110);
    companyBtn.setOnAction(event -> {
      // 改前景颜色
      // TG_Company_VBox.getStylesheets().add("-fx-background-color:red");
      // 修改当前托管公司名称
      currentTGCompanyLabel.setText(company);
      currentTGTeamLabel.setText("");


      // 清空表数据
      clearTableTGTeamDataAndSum();
      clearWhenChangeCompanyBtn();

      // 加载托管公司名下的团队按钮数据 {托管公司 ：｛ 团队名称 ： 团队数据列表 ｝｝
      if (CollectUtil.isHaveValue(teamList)) {
        loadTeamBtnView(teamList);
      }
      // 设置托管公司的相关信息
      setTGCompanyInfo(company);
      // 设置下面那个小Tab的当前Tab数据
      refreshSmallTabData();
    });

    return companyBtn;
  }

  /**
   * 加载动态的按钮列表图
   * 
   * @time 2018年3月7日
   * @param teamList
   */
  private void loadTeamBtnView(List<String> teamList) {
    ObservableList<Node> teamBtns = TG_Team_VBox.getChildren();
    TG_Team_VBox.setPrefWidth(100);
    if (CollectUtil.isHaveValue(teamBtns)) {
      teamBtns.clear();
    }
    // 排序
    try {
      if (teamList != null && teamList.size() > 1) {
        teamList = teamList.stream().sorted((x, y) -> {
          String a = x.replaceFirst("[a-zA-Z]+", "");
          String b = y.replaceFirst("[a-zA-Z]+", "");
          if (StringUtil.isAnyBlank(a, b)) {
            return 1;
          } else {
            return Integer.valueOf(a).compareTo(Integer.valueOf(b));
          }
        }).collect(Collectors.toList());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    teamList.forEach(teamId -> {
      Button teamBtn = getTeamButton(teamId);
      teamBtns.add(teamBtn);
    });
  }

  /**
   * 获取动态的团队按钮
   * 
   * @time 2018年3月7日
   * @param teamId
   * @return
   */
  private Button getTeamButton(String teamId) {
    Button teamBtn = new Button(teamId);
    teamBtn.setPrefWidth(90);
    teamBtn.setOnAction(event -> {
      // 清空数据
      clearWhenChangeTeamBtn();

      currentTGTeamLabel.setText(teamId);
      // 获取代理查询的团队数据
      final List<ProxyTeamInfo> proxyTeamInfoList = getProxyTeamInfoList(teamId);
      // 转化为托管公司的团队数据
      List<TGTeamInfo> tgTeamList = convert2TGTeamInfo(teamId, proxyTeamInfoList);
      tableTGZhanji.setItems(FXCollections.observableArrayList(tgTeamList));
      // 设置团队合计
      refreshTableTGTeamSum();
      // 设置团队的托管回水比例、回保比例、服务费
      setTGTeamRateInfo(teamId);
    });

    return teamBtn;
  }


  /**
   * 点击托管公司按钮后先清空相应值
   */
  private void clearWhenChangeCompanyBtn() {
    clearWhenChangeTeamBtn();
    tgCompanyYajin.setText("");
    tgCompanyEdu.setText("");
    tgYifenhong.setText("0");
  }

  /**
   * 点击托管团队按钮后先清空相应值
   * 
   * @time 2018年3月11日
   */
  private void clearWhenChangeTeamBtn() {
    tgTeamHSRate.setText("");
    tgTeamHBRate.setText("");
    tgTeamFwfRate.setText("");
    tgTeamFwfLabel.setText("0.0");
  }

  /**
   * 设置公司的托管押金、额度。其他？
   * 
   * @time 2018年3月11日
   * @param teamId
   */
  private void setTGCompanyInfo(String tgCompany) {
    TGCompanyModel tgCompanyModel = DBUtil.get_tg_company_by_id(tgCompany);
    if (tgCompanyModel == null) {
      return;
    }
    // 设置
    tgCompanyYajin.setText(tgCompanyModel.getYajin());
    tgCompanyEdu.setText(tgCompanyModel.getEdu());
    tgYifenhong.setText(tgCompanyModel.getYifenhong());
    // 其他更新

  }

  /**
   * 设置团队的托管回水比例、回保比例、服务费
   * 
   * @time 2018年3月11日
   * @param teamId
   */
  private void setTGTeamRateInfo(String teamId) {
    TGTeamModel tgTeamModel = DBUtil.get_tg_team_by_id(teamId);
    if (tgTeamModel == null) {
      // 设置
      tgTeamHSRate.setText("0%");
      tgTeamHBRate.setText("0%");
      tgTeamFwfRate.setText("0%");
      tgTeamFwfLabel.setText("0");
      teamProxyCheckBox.setSelected(false);
      // saveTGTeamAction();
      return;
    }
    // 设置
    tgTeamHSRate.setText(tgTeamModel.getTgHuishui());
    tgTeamHBRate.setText(tgTeamModel.getTgHuiBao());
    tgTeamFwfRate.setText(tgTeamModel.getTgFwfRate());
    // TODO
    tgTeamFwfLabel.setText(getTGTeamFwf());
    boolean isTeamProxy = "1".equals(tgTeamModel.getTgTeamProxy());
    teamProxyCheckBox.setSelected(isTeamProxy);
    // 其他更新

  }

  /**
   * 获取团队服务费 = (战绩2.5合计 + 保险合计) * 服务费比例
   * 
   * @time 2018年3月21日
   * @return
   */
  private String getTGTeamFwf() {

    if (TableUtil.isHasValue(tableZJSum)) {
      ObservableList<TypeValueInfo> items = tableZJSum.getItems();
      // 战绩2.5合计
      Optional<String> zhaiji25HejiOpt =
          items.stream().filter(info -> "战绩2.5%合计".equals(info.getType()))
              .map(TypeValueInfo::getValue).findFirst();
      // 保险合计
      Optional<String> baoxiaoHejiOpt = items.stream().filter(info -> "保险合计".equals(info.getType()))
          .map(TypeValueInfo::getValue).findFirst();
      if (zhaiji25HejiOpt.isPresent() && baoxiaoHejiOpt.isPresent()) {
        String sumOfFwf = NumUtil.getSum(zhaiji25HejiOpt.get(), baoxiaoHejiOpt.get());
        // 求该团队的服务比例
        String fwfRate = tgTeamFwfRate.getText();
        fwfRate = StringUtil.isBlank(fwfRate) ? "0%" : fwfRate.trim();
        double rate = NumUtil.getNumByPercent(fwfRate);
        if (rate == 0 || rate == 0.0) {
          return "0";
        } else {
          Double fwf = NumUtil.getNumTimes(sumOfFwf, fwfRate);
          return NumUtil.digit2(fwf + "");
        }
      } else {
        return "0";
      }
    } else {
      return "0";
    }
  }

  /**
   * 代理查询中的数据转成托管中的团队信息数据
   * 
   * @time 2018年3月7日
   * @param teamId
   * @param proxyTeamInfoList
   * @return
   */
  private List<TGTeamInfo> convert2TGTeamInfo(String teamId,
      List<ProxyTeamInfo> proxyTeamInfoList) {
    List<TGTeamInfo> list = new ArrayList<>();
    TGController tgController = MyController.tgController;
    Map<String, TGTeamModel> tgTeamRateMap = tgController.getTgTeamModelMap();

    TGTeamModel tgTeamModel = tgTeamRateMap.get(teamId);
    String teamUnknowValue = tgTeamModel == null ? "0%" : tgTeamModel.getTgHuishui();

    String teamHuibaoRateValue = tgTeamModel == null ? "0.0" : tgTeamModel.getTgHuiBao();

    // 更改列名称
    changeColumnName_TeamUnknowRate(teamUnknowValue);

    if (CollectUtil.isHaveValue(proxyTeamInfoList)) {
      list = proxyTeamInfoList.stream().map(info -> {
        String yszj = info.getProxyYSZJ();
        TGTeamInfo tgTeam = new TGTeamInfo();
        tgTeam.setTgPlayerId(info.getProxyPlayerId());
        tgTeam.setTgPlayerName(info.getProxyPlayerName());
        tgTeam.setTgYSZJ(info.getProxyYSZJ());
        tgTeam.setTgBaoxian(info.getProxyBaoxian());
        tgTeam.setTgChangci(info.getProxyTableId());
        // 设置战绩2.5% ,即满水
        String percent25Str = getLirunByYSZJ_TG(yszj, teamUnknowValue, null, 1);
        tgTeam.setTgZJ25(percent25Str);
        // 设置战绩未知%
        String teamUnknowStr = getLirunByYSZJ_TG(yszj, teamUnknowValue, null, 2);
        tgTeam.setTgZJUnknow(teamUnknowStr);
        // 设置回保
        String teamHuibaoRateStr = NumUtil.digit2((-1) * Constants.CURRENT_HS_RATE
            * NumUtil.getNumTimes(tgTeam.getTgBaoxian(), teamHuibaoRateValue) + "");
        if (tgTeam.getTgBaoxian().equals("0")) {
          teamHuibaoRateStr = "0";
        }
        tgTeam.setTgHuiBao(teamHuibaoRateStr);

        // 设置利润
        String profit = getLirunByYSZJ_TG(yszj, teamUnknowValue, tgTeam, 3);
        tgTeam.setTgProfit(profit);

        return tgTeam;
      }).collect(Collectors.toList());
    }
    return list;
  }


  /**
   * 获取每一行的利润 公式 = 原始战绩 * （2.5% - unknow%） + 保险 * （-Constants.HS_RATE） - 回保
   * 
   * @time 2018年3月7日
   * @param info
   * @return
   */
  public String getRecordProfit(TGTeamInfo info) {
    String teamRate25 = info.getTgZJ25();
    String teamRateUnknow = info.getTgZJUnknow();
    String baoxian = info.getTgBaoxian();
    String huibao = info.getTgHuiBao();
    Double recordProfit = NumUtil.getNum(teamRate25) - NumUtil.getNum(teamRateUnknow)
        + (NumUtil.getNum(baoxian) * (-Constants.CURRENT_HS_RATE) - NumUtil.getNum(huibao));
    return NumUtil.digit2(recordProfit + "");
  }

  /**
   * 模拟获取代理查询中的团队数据
   * 
   * @time 2018年3月6日
   * @param teamId
   * @return
   */
  public List<ProxyTeamInfo> getProxyTeamInfoList(String teamId) {

    List<ProxyTeamInfo> proxyTeamList = new ArrayList<>();
    ObservableList<String> obList = TeamProxyService.teamIDCombox.getItems();
    if (!CollectUtil.isHaveValue(obList)) {
      ShowUtil.show("小林提示：代理查询团队下拉框没有数据！", 2);
      return proxyTeamList;
    } else {
      if (StringUtil.isNotBlank(teamId) && obList.contains(teamId.toUpperCase())) {
        for (String _teamId : obList) {
          if (_teamId.equals(teamId.toUpperCase())) {
            TeamProxyService.teamIDCombox.getSelectionModel().select(_teamId);
            log.info("模拟获取代理查询中的团队数据: " + teamId);
            ObservableList<ProxyTeamInfo> teamList = TeamProxyService.tableProxyTeam.getItems();
            if (CollectUtil.isHaveValue(teamList)) {
              for (ProxyTeamInfo info : teamList) {
                proxyTeamList.add(info);
              }
            }
          }
        }
      }
    }
    return proxyTeamList;
  }


  /**
   * 获取十六进制的颜色代码.例如 "#6E36B4" , For HTML ,
   * 
   * @return String
   */
  public static String getRandColorCode() {
    String r, g, b;
    Random random = new Random();
    r = Integer.toHexString(random.nextInt(256)).toUpperCase();
    g = Integer.toHexString(random.nextInt(256)).toUpperCase();
    b = Integer.toHexString(random.nextInt(256)).toUpperCase();

    r = r.length() == 1 ? "0" + r : r;
    g = g.length() == 1 ? "0" + g : g;
    b = b.length() == 1 ? "0" + b : b;

    return r + g + b;
  }


  /**
   * 获取托管团队的托管回水比例和回保比例
   * 
   * @time 2018年3月17日
   * @return
   */
  public Map<String, TGTeamModel> getTgTeamModelMap() {
    List<TGTeamModel> tableTGTeams = DBUtil.get_all_tg_team();
    if (CollectUtil.isNullOrEmpty(tableTGTeams)) {
      tableTGTeams = new ArrayList<>();
    }
    // toMap方法，当key相同时会报错
    Map<String, TGTeamModel> map = tableTGTeams.stream().distinct()
        .collect(Collectors.toMap(TGTeamModel::getTgTeamId, Function.identity()));
    return map == null ? new HashMap<>() : map;
  }

  private void changeColumnName_TeamUnknowRate(String teamUnknowRate) {
    tableTGZhanji.getColumns().get(5).setText("战绩" + teamUnknowRate);
  }


  /**
   * 获取托管团队表内容
   * 
   * @time 2018年3月6日
   * @return
   */
  public List<TypeValueInfo> getTableTGTeams() {
    ObservableList<TypeValueInfo> tgTeamRates = tableTGTeamRate.getItems();
    return CollectUtil.isNullOrEmpty(tgTeamRates) ? FXCollections.observableArrayList()
        : tgTeamRates;
  }

  /**
   * 刷新托管团队表
   * 
   * @time 2018年3月3日
   */
  private void refreshTableTGTeam() {
    List<TypeValueInfo> list;
    String teamsJson = DBUtil.getValueByKey(TG_TEAM_RATE_DB_KEY);
    if (StringUtil.isNotBlank(teamsJson) && !"{}".equals(teamsJson)) {
      list = JSON.parseObject(teamsJson, new TypeReference<List<TypeValueInfo>>() {});
    } else {
      // list = new ArrayList<>();
      list = getMoniTGTeamRate();
      String teamJson = JSON.toJSONString(list);
      DBUtil.saveOrUpdateOthers(TG_TEAM_RATE_DB_KEY, teamJson);
    }
    tableTGTeamRate.setItems(FXCollections.observableArrayList(list));
  }


  private List<TypeValueInfo> getMoniTGTeamRate() {
    // DBUtil.delValueByKey(TG_TEAM_RATE_DB_KEY);
    List<TypeValueInfo> list = new ArrayList<>();
    Random random = new Random();
    for (int i = 1; i <= 40; i++) {
      double nextDouble = random.nextDouble();
      list.add(new TypeValueInfo("S" + i, NumUtil.digit1(1 + nextDouble + "") + "%"));
    }
    return list;
  }


  /**
   * 添加托管团队
   */
  public void AddTGTeamRateBtnAction(ActionEvent event) {
    InputDialog dialog = new InputDialog("添加托管团队", "托管团队", "团队比例");

    Optional<Pair<String, String>> result = dialog.getResult();
    if (result.isPresent()) {
      try {
        Pair<String, String> map = result.get();
        String teamId = map.getKey().trim();
        String teamRate = map.getValue().trim();
        // 是否重复
        List<TypeValueInfo> tableTGTeams = getTableTGTeams();
        boolean repeatTeamId =
            tableTGTeams.stream().anyMatch(info -> teamId.equals(info.getType()));
        if (repeatTeamId) {
          ShowUtil.show(teamId + "团队已经存在！");
          return;
        }
        if (!teamRate.endsWith("%")) {
          ShowUtil.show("比例必须包含百分比符号%");
          return;
        }
        // 添加
        tableTGTeams.add(new TypeValueInfo(teamId, teamRate));
        String teamsJson = JSON.toJSONString(tableTGTeams);
        DBUtil.saveOrUpdateOthers(TG_TEAM_RATE_DB_KEY, teamsJson);
        // 刷新当前表(战绩) TODO
        refreshTableTGTeam();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 删除托管团队比例
   */
  public void delTGTeamRateBtnAction(ActionEvent event) {
    TypeValueInfo selectedItem = tableTGTeamRate.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      ShowUtil.show("请先选择记录!");
      return;
    }
    tableTGTeamRate.getItems().remove(selectedItem);
    ObservableList<TypeValueInfo> items = tableTGTeamRate.getItems();
    String teamsJson = JSON.toJSONString(items);
    DBUtil.saveOrUpdateOthers(TG_TEAM_RATE_DB_KEY, teamsJson);
    // 刷新当前表(战绩) TODO
    refreshTableTGTeam();

  }

  /**
   * 刷新托管团队总和数据（合计）
   */
  public void refreshTableTGTeamSum() {
    ObservableList<TGTeamInfo> items = tableTGZhanji.getItems();
    // 1 战绩2.5%：
    double zjRate25Sum = items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgZJ25())).sum();

    // 2 战绩未知
    String columnName = tableTGZhanji.getColumns().get(5).getText();
    double zjRateUnknowSum = tableTGZhanji.getItems().stream()
        .mapToDouble(info -> NumUtil.getNum(info.getTgZJUnknow())).sum();

    // 3 保险
    double zjBaoxianSum =
        items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgBaoxian())).sum()
            * (-Constants.CURRENT_HS_RATE);

    // 4 回保
    double zjHuibaoSum =
        items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgHuiBao())).sum();

    // 5 总和
    double zjProfitSum = zjRate25Sum - zjRateUnknowSum + zjBaoxianSum - zjHuibaoSum;
    // 6 总人次
    int size = 0;
    if (items != null) {
      size = items.size();
    }

    List<TypeValueInfo> list = new ArrayList<TypeValueInfo>();
    list.add(new TypeValueInfo("战绩2.5%合计", NumUtil.digit2(zjRate25Sum + "")));
    list.add(new TypeValueInfo(columnName + "合计", NumUtil.digit2(zjRateUnknowSum + "")));
    list.add(new TypeValueInfo("保险合计", NumUtil.digit2(zjBaoxianSum + "")));
    list.add(new TypeValueInfo("回保合计", NumUtil.digit2(zjHuibaoSum + "")));
    list.add(new TypeValueInfo("总利润合计", NumUtil.digit2(zjProfitSum + "")));
    list.add(new TypeValueInfo("总人次", size + ""));

    ObservableList<TypeValueInfo> obList = FXCollections.observableArrayList(list);
    tableZJSum.setItems(obList);

  }

  /**
   * 刷新托管团队战绩以及总和数据（合计）
   */
  private void clearTableTGTeamDataAndSum() {
    tableTGZhanji.getColumns().get(5).setText("战绩0%");
    if (TableUtil.isHasValue(tableTGZhanji)) {
      tableTGZhanji.getItems().clear();
    }
    if (TableUtil.isHasValue(tableZJSum)) {
      tableZJSum.getItems().clear();
    }
  }


  /**
   * 刷新外债Tab
   * 
   * @time 2018年3月8日
   */
  public void refreshTabTGWaizhai() {
    TableUtil.clear(tgWZTeam);
    MyController myController = Main.myController;
    TgWaizhaiService.generateWaizhaiTables(tgWZTeam, tgWZTeamHBox,
        myController.tableCurrentMoneyInfo, myController.tableTeam);
  }

  /**
   * 保存托管团的各个比例
   * 
   * @time 2018年3月11日
   */
  public void saveTGTeamAction() {
    String tgTeamId = currentTGTeamLabel.getText();
    if (StringUtil.isBlank(tgTeamId)) {
      ShowUtil.show("请先选择托管团队！");
      return;
    }
    // 托管团队回水比例
    String teamHSRate = StringUtil.nvl(tgTeamHSRate.getText(), "0%");
    // 托管团队回保比例
    String teamHBRate = StringUtil.nvl(tgTeamHBRate.getText(), "0%");
    // 托管团队服务费
    String teamFWFRate = StringUtil.nvl(tgTeamFwfRate.getText(), "0%");
    // 托管团队是否代理 并 保存
    TGTeamModel team =
        new TGTeamModel(tgTeamId, teamHSRate, teamHBRate, teamFWFRate, getTeamProxyChecked());

    DBUtil.saveOrUpdate_tg_team(team);
    ShowUtil.show("保存成功", 2);
  }

  /**
   * 获取该托管团队是否代理
   * 
   * @time 2018年3月21日
   * @return
   */
  public String getTeamProxyChecked() {
    return teamProxyCheckBox.isSelected() ? "1" : "0";
  }

  /**
   * 保存或修改公司信息（押金和额度）
   * 
   * @time 2018年3月11日
   * @param event
   */
  public void saveCompanyInfoAction(ActionEvent event) {
    String tgCompany = currentTGCompanyLabel.getText();
    if (StringUtil.isBlank(tgCompany)) {
      ShowUtil.show("请先选择托管公司！");
      return;
    }
    TGCompanyModel db_company = DBUtil.get_tg_company_by_id(tgCompany);
    if (db_company == null) {
      ShowUtil.show("数据库中没有存储此托管公司！");
      return;
    }
    // 托管公司押金
    String companyYajin = StringUtil.nvl(tgCompanyYajin.getText(), "0");
    // 托管公司额度
    String companyEdu = StringUtil.nvl(tgCompanyEdu.getText(), "0");
    // 托管公司已分红
    String yifenhong = StringUtil.nvl(tgYifenhong.getText(), "0");

    db_company.setYajin(companyYajin);
    db_company.setEdu(companyEdu);
    db_company.setYifenhong(yifenhong);

    DBUtil.saveOrUpdate_tg_company(db_company);
    ShowUtil.show("保存成功", 2);
  }

  /**
   * 刷新月利润表
   * 
   * @time 2018年3月17日
   */
  public void refreshProfitTab() {
    String tgCompany = currentTGCompanyLabel.getText();
    if (StringUtil.isBlank(tgCompany)) {
      ShowUtil.show("请先选择托管公司！");
      return;
    }

    log.info("===========================刷新月利润表");
    List<TGLirunInfo> list = new ArrayList<>();
    TGLirunInfo lirun = new TGLirunInfo();

    // 获取数据库的历史日利润表 TODO
    List<TGLirunInfo> liruns = DBUtil.get_all_tg_lirun(tgCompany);
    if (CollectUtil.isHaveValue(liruns)) {
      String today = getDateString();
      boolean isTodayContains =
          liruns.stream().anyMatch(info -> today.equals(info.getTgLirunDate()));
      if (isTodayContains) {
        liruns = liruns.stream().filter(info -> !today.equals(info.getTgLirunDate()))
            .collect(Collectors.toList());
        log.info("刷新月利润表时已经存在了今天" + today + "的数据了！所以去除数据库的今天记录并重新计算");
      }
      list.addAll(liruns);
    }
    refreshFwfTab();// 刷新服务费



    // 获取日期（日期与当前托管公司为主键）
    String dateString = getDateString();
    lirun.setTgLirunDate(dateString);
    lirun.setTgLirunCompanyName(tgCompany);
    // 获取总利润（即服务费明细中的总利润）
    ObservableList<TypeValueInfo> fwfList = tableTGFwfSum.getItems();
    String tgLirunTotalProfit = fwfList.stream().filter(info -> "总利润".equals(info.getType()))
        .map(TypeValueInfo::getValue).findFirst().orElse("0");
    lirun.setTgLirunTotalProfit(tgLirunTotalProfit);
    // 获取总开销
    List<TGKaixiaoInfo> allTGKaixiaos = DBUtil.get_all_tg_kaixiao();
    double sumOfKaixiao =
        allTGKaixiaos.stream().filter(info -> tgCompany.equals(info.getTgKaixiaoCompany()))
            .map(TGKaixiaoInfo::getTgKaixiaoMoney).mapToDouble(NumUtil::getNum).sum();
    lirun.setTgLirunTotalKaixiao(NumUtil.digit0(sumOfKaixiao + ""));
    // 设置Rest合计
    lirun.setTgLirunRestHeji(NumUtil.digit2(NumUtil.getSum(tgLirunTotalProfit, sumOfKaixiao + "")));
    // 设置公司占股
    TGCompanyModel companyModel = DBUtil.get_tg_company_by_id(tgCompany);
    String companyRate = companyModel.getCompanyRate();
    companyRate = companyRate.endsWith("%") ? NumUtil.getNumByPercent(companyRate) + ""
        : NumUtil.getNum(companyRate) / 100.0 + "";
    Double atmCompanyProfit = NumUtil.getNumTimes(lirun.getTgLirunRestHeji(), companyRate);
    lirun.setTgLirunATMCompany(NumUtil.digit2(atmCompanyProfit + ""));

    // 设置托管公司占股
    String tgCompanyRate = StringUtil.nvl(companyModel.getTgCompanyRate(), "0%");
    tgCompanyRate = tgCompanyRate.endsWith("%") ? NumUtil.getNumByPercent(tgCompanyRate) + ""
        : NumUtil.getNum(tgCompanyRate) / 100.0 + "";
    Double tgCompanyProfit = NumUtil.getNumTimes(lirun.getTgLirunRestHeji(), tgCompanyRate);
    lirun.setTgLirunTGCompany(NumUtil.digit2(tgCompanyProfit + ""));

    // 设置团队服务费 TODO
    Double tgCompanyProxy = 0.0d;
    List<TGTeamModel> tgTeamInfos = DBUtil.get_all_tg_team();
    Set<String> proxyTeamSet =
        tgTeamInfos.stream().filter(info -> "1".equals(info.getTgTeamProxy()))
            .map(TGTeamModel::getTgTeamId).collect(Collectors.toSet());
    tgCompanyProxy = tableTGFwf.getItems().stream()
        .filter(info -> proxyTeamSet.contains(info.getTgFwfTeamId()))
        .mapToDouble(
            info -> NumUtil.getNum(info.getTgFwfFanshui()) + NumUtil.getNum(info.getTgFwfFanbao()))
        .sum();
    lirun.setTgLirunTeamProfit(NumUtil.digit2(tgCompanyProxy + ""));

    // 设置托管公司合计 = 托管公司占股 + 托管公司代理
    Double tgCompanyHeji = tgCompanyProfit + tgCompanyProxy;
    lirun.setTgLirunHeji(NumUtil.digit2(tgCompanyHeji + ""));

    list.add(lirun);
    tableTGLirun.setItems(FXCollections.observableArrayList(list));

  }


  /*
   * 刷新第一行（总外债、总利润、可分配...）
   */
  public void refreshFirstRowAction() {
    refreshTabTGWaizhai();// 刷新外债
    // 总外债
    totalWaizhai.setText(getTotalTeamWaizhai());
    // 总利润
    tgTotalProfit.setText(getTotalTGProfit());
    // 可分配
    tgAvailable.setText(getTotalAvailable());
  }

  /**
   * 获取总外债
   * 
   * @time 2018年3月17日
   * @return
   */
  private String getTotalTeamWaizhai() {
    String totalWaizhai = tgWZTeamValue.getText();
    try {
      Double.valueOf(totalWaizhai);
    } catch (NumberFormatException e) {
      totalWaizhai = "0";
    }
    return totalWaizhai;
  }

  /**
   * 获取总利润（就是月利润中的总利润）
   * 
   * @time 2018年3月17日
   * @return
   */
  private String getTotalTGProfit() {
    double sum = tableTGLirun.getItems().stream().map(TGLirunInfo::getTgLirunHeji)
        .mapToDouble(NumUtil::getNum).sum();
    return sum + "";
  }

  /**
   * 获取总利润（就是月利润中的总利润）
   * 
   * @time 2018年3月17日
   * @return
   */
  private String getTotalAvailable() {
    // 总外债
    String zongWaizhai = totalWaizhai.getText();
    // 总利润
    String zongLirun = tgTotalProfit.getText();
    // 已分红
    String yiFenhong = StringUtil.nvl(tgYifenhong.getText(), "0");
    // 押金
    String yajing = StringUtil.nvl(tgCompanyYajin.getText(), "0");
    Double availabel = NumUtil.getNum(zongLirun) + NumUtil.getNum(yajing)
        + NumUtil.getNum(zongWaizhai) - NumUtil.getNum(yiFenhong);
    return NumUtil.digit2(availabel + "");
  }

  /**
   * 导出当前托管公司的所有数据
   * 
   * @time 2018年3月2日
   * @param event
   */
  public void exportTGExcelAction(ActionEvent event) {
    TGExportExcelService exportExcelService = new TGExportExcelService(MyController.tgController);
    exportExcelService.exportExcel();
  }

  /**
   * 结束今天统计时删除托管中的开销和玩家备注
   * 
   * @time 2018年3月19日
   */
  public void deleteKaixiaoAndComment() {
    DBUtil.del_all_tg_kaixiao();
    DBUtil.del_all_tg_comment();
    log.info("结束今天统计时删除托管中的开销和玩家备注...");
  }

  /**
   * 保存当前托管公司的日利润数据
   * 
   * @time 2018年3月19日
   * @param event
   */
  public void saveCurrentTGCompanyLirnAction(ActionEvent event) {
    String tgCompany = currentTGCompanyLabel.getText();
    if (StringUtil.isBlank(tgCompany)) {
      ShowUtil.show("请选择托管公司！", 2);
      return;
    }

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("提示");
    alert.setHeaderText(null);
    alert.setContentText("确定这是最后一场了吗，将保存本场最后一条记录进数据库，确定数据正确了吗？");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      int size = tableTGLirun.getItems().size();
      if (size > 0) {
        TGLirunInfo tgLirunInfo = tableTGLirun.getItems().get(size - 1);
        DBUtil.saveOrUpdate_tg_lirun(tgLirunInfo);
        ShowUtil.show("保存成功", 2);
      } else {
        ShowUtil.show("无可保存的数据！", 2);
      }
    }
  }

  /**
   * 手动清空利润表
   */
  public void clearLirunAction() {
    boolean delOK = DBUtil.del_all_tg_lirun();
    if (delOK) {
      if (TableUtil.isHasValue(tableTGLirun)) {
        tableTGLirun.getItems().clear();
      }
      ShowUtil.show("删除成功", 2);
    }
  }


  /**
   * 获取团队的回保比例
   */
  public double getTgTeamHuibaoRate(String teamId) {
    List<TGTeamModel> tgTeams = DBUtil.get_all_tg_team();
    if (CollectUtil.isHaveValue(tgTeams)) {
      Optional<TGTeamModel> teamInfoOpt =
          tgTeams.stream().filter(info -> teamId.equals(info.getTgTeamId())).findFirst();
      if (teamInfoOpt.isPresent()) {
        TGTeamModel teamInfo = teamInfoOpt.get();
        String fwfRate = teamInfo.getTgHuiBao();
        if (StringUtil.isBlank(fwfRate) || "0%".equals(fwfRate)) {
          return 1.0;
        } else if ("1".equals(teamInfo.getTgTeamProxy())) { //
          return 1 - NumUtil.getNumByPercent(fwfRate);
        }
      }
    }
    return 1.0;
  }

  /**
   * 获取团队的服务费比例
   */
  public double getTgTeamFwfRate(String teamId) {
    List<TGTeamModel> tgTeams = DBUtil.get_all_tg_team();
    if (CollectUtil.isHaveValue(tgTeams)) {
      Optional<TGTeamModel> teamInfoOpt =
          tgTeams.stream().filter(info -> teamId.equals(info.getTgTeamId())).findFirst();
      if (teamInfoOpt.isPresent()) {
        TGTeamModel teamInfo = teamInfoOpt.get();
        String fwfRate = teamInfo.getTgFwfRate();
        if (StringUtil.isBlank(fwfRate) || "0%".equals(fwfRate)) {
          return 0.0;
        } else { // if("1".equals(teamInfo.getTgTeamProxy()))
          return NumUtil.getNumByPercent(fwfRate);
        }
      }
    }
    return 0.0;
  }


  public static String getDateString() {
    String date_Str = DataConstans.Date_Str;
    if (StringUtil.isBlank(date_Str)) {
      ShowUtil.show("当前没有确定的系统时间！将以1971-01-01代替！");
      return "1971-01-01";
    }
    return date_Str;
  }

  /**
   * 导出当前月利润
   */
  public void exportLirunAction(ActionEvent event) {
    if (TableUtil.isNullOrEmpty(tableTGLirun)) {
      ShowUtil.show("左边栏当前无月利润！");
      return;
    }
    ObservableList<TGLirunInfo> monthLiruns = tableTGLirun.getItems();
    String[] rowsName =
        new String[] {"日期", "托管公司", "总利润", "总开销", "合计", "公司占股", "托管公司占股", "团队利润", "托管合计"};
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (TGLirunInfo info : monthLiruns) {
      objs = new Object[rowsName.length];
      objs[0] = info.getTgLirunDate();
      objs[1] = info.getTgLirunCompanyName();
      objs[2] = info.getTgLirunTotalProfit();
      objs[3] = info.getTgLirunTotalKaixiao();
      objs[4] = info.getTgLirunRestHeji();
      objs[5] = info.getTgLirunATMCompany();
      objs[6] = info.getTgLirunTGCompany();
      objs[7] = info.getTgLirunTeamProfit();
      objs[8] = info.getTgLirunHeji();
      dataList.add(objs);
    }
    String title = LocalDate.now() + "月利润";
    List<Integer> columnWidths =
        Arrays.asList(3500, 4000, 3000, 3000, 3000, 4000, 4000, 3000, 3000, 3000, 3000, 3000, 3000);
    ExportExcelTemplate ex = new ExportExcelTemplate(title, rowsName, columnWidths, dataList);
    try {
      ex.export();
      ShowUtil.show("月利润导出完成", 1);
    } catch (Exception e) {
      ErrorUtil.err("月利润导出自动记录失败", e);
      e.printStackTrace();
    }

  }

  /*******************************************************************************************
   * 
   * 0.95与0.975切换版本
   * 
   ********************************************************************************************/

  /**
   * 根据原始战绩获取利润
   * 
   */
  public String getLirunByYSZJ_TG(String yszj, String teamUnknowValue, TGTeamInfo tgTeam,
      int type) {
    try {
      if (Constants.FINAL_HS_RATE_0975 == Constants.CURRENT_HS_RATE) {
        // 0.975版本
        return getByYSZJ_TG(yszj, teamUnknowValue, tgTeam, type);

      } else {
        // 0.95版本
        Double zhanji = Double.valueOf(yszj);
        if (zhanji.compareTo(0d) >= 0) {
          return "0";
        } else {
          return getByYSZJ_TG(yszj, teamUnknowValue, tgTeam, type);
        }
      }
    } catch (Exception e) {
      log.error("根据原始战绩获取出利润出错，原因:" + e.getMessage());
      return "0";
    }
  }

  /**
   * 根据原始战绩获取利润 (共用)
   * 
   * @param yszj 原始战绩
   * @param teamUnknowValue 团队未知，用于获取(战绩0%)的值
   * @param tgTeam 指TGTeamInfo
   * @param type 1、战绩2.5%，即满水 2、战绩0% 3、一行的利润
   * @return
   */
  private String getByYSZJ_TG(String yszj, String teamUnknowValue, TGTeamInfo tgTeam, int type) {
    if (type == 1) {
      return NumUtil.digit2(Math.abs(NumUtil.getNum(yszj)) * (1 - Constants.CURRENT_HS_RATE) + "");
    } else if (type == 2) {
      return NumUtil.digit2(Math.abs(NumUtil.getNumTimes(yszj, teamUnknowValue)) + "");
    } else if (type == 3) {
      return getRecordProfit(tgTeam);
    }
    return "0";
  }



}
