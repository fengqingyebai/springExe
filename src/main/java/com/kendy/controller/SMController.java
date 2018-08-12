package com.kendy.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jfoenix.controls.JFXChipView;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.db.DBUtil;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.ShangmaDetailInfo;
import com.kendy.entity.ShangmaInfo;
import com.kendy.entity.WaizhaiInfo;
import com.kendy.interfaces.Entity;
import com.kendy.service.ShangmaService;
import com.kendy.util.CollectUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * 上码控制器
 * 
 * @author 林泽涛
 * @time 2018年1月1日 下午10:55:48
 */
@Component
public class SMController extends BaseController implements Initializable {

  Logger logger = Logger.getLogger(SMController.class);
  
  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public MyController myController ;
  @Autowired
  public TeamProxyController teamProxyController; // 代理控制类
  @Autowired
  public ShangmaService shangmaService; // 上码控制类
  @Autowired
  public DataConstans dataConstants; // 数据控制类


  public SMController() {
    super();
    logger.info("执行SMController构造方法");
  }
  
  
  @PostConstruct
  public void inits() {
    logger.info("@PostConstruct SMController");

  }
  
  // 过滤团队
  public static List<String> filterTeams = FXCollections.observableArrayList();

  // ===============================================================上码查询Tab
  @FXML public TableView<ShangmaInfo> tableShangma;
  @FXML public TableColumn<ShangmaInfo, String> shangmaName;
  @FXML public TableColumn<ShangmaInfo, String> shangmaEdu;
  @FXML public TableColumn<ShangmaInfo, String> shangmaAvailableEdu;
  @FXML public TableColumn<ShangmaInfo, String> shangmaYCJF;// 已存积分=实时金额
  @FXML public TableColumn<ShangmaInfo, String> shangmaYiSM;// 已上码
  @FXML public TableColumn<ShangmaInfo, String> shangmaSumOfZJ;
  @FXML public TableColumn<ShangmaInfo, String> shangmaPlayerId;
  @FXML public TableColumn<ShangmaInfo, String> shangmaLianheEdu;
  @FXML public VBox shangmaVBox;// 用于初始化团队ID按钮
  @FXML public Label shangmaTeamId;// 显示栏上的团队ID
  @FXML public Label shangmaZSM;// 团队总上码
  @FXML public Label shangmaZZJ;// 团队总战绩
  @FXML public TextField shangmaSearch;// 上码的输入框

  @FXML public TextField teamYajin;// 团队押金
  @FXML public TextField teamEdu;// 团队额度
  @FXML public Label teamShangmaAvailable;// 团队可上码
  @FXML public CheckBox smTeamShangmaCheckBox;// 团队可上码CheckBox

  @FXML public TableView<ShangmaDetailInfo> tableShangmaDetail;
  @FXML public TableColumn<ShangmaDetailInfo, String> shangmaDetailName;
  @FXML public TableColumn<ShangmaDetailInfo, String> shangmaJu;
  @FXML public TableColumn<ShangmaDetailInfo, String> shangmaSM;
  @FXML public TableColumn<ShangmaDetailInfo, String> shangmaShishou;

  @FXML public TableView<ShangmaDetailInfo> tableShangmaNextDay;
  @FXML public TableColumn<ShangmaDetailInfo, String> shangmaNextDayName;
  @FXML public TableColumn<ShangmaDetailInfo, String> shangmaNextDayJu;
  @FXML public TableColumn<ShangmaDetailInfo, String> shangmaNextDaySM;

  // ===============================================================外债
  @FXML public TableView<WaizhaiInfo> tableWaizhai;
  @FXML public TableColumn<WaizhaiInfo, String> waizhaiType;
  @FXML public TableColumn<WaizhaiInfo, String> waizhaiMoney;
  @FXML public HBox waizhaiHBox;// 里面包含多个表


  /**
   * 节点加载完后需要进行的一些初始化操作 
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 绑定实时上码表
    bindCellValueByTable(new ShangmaInfo(), tableShangma);
    tableShangma.setRowFactory(new Callback<TableView<ShangmaInfo>, TableRow<ShangmaInfo>>() {
      @Override
      public TableRow<ShangmaInfo> call(TableView<ShangmaInfo> param) {
        return new TableRowControl(tableShangma);
      }
    });
    
    // 绑定上码个人信息表
    tableShangmaDetail.setEditable(true);
    bindCellValue(new ShangmaDetailInfo(), shangmaDetailName, shangmaJu, shangmaSM, shangmaShishou);
    shangmaDetailName.setCellFactory(ShangmaNameCellFactory);
    shangmaJu.setCellFactory(ShangmaNameCellFactory);
    shangmaSM.setCellFactory(ShangmaNameCellFactory);
    shangmaShishou.setCellFactory(ShangmaNameCellFactory);
    setTableShangmaSelect();

    // 绑定次日信息表
    shangmaNextDayName.setCellValueFactory(
        new PropertyValueFactory<ShangmaDetailInfo, String>("shangmaDetailName"));
    shangmaNextDayName.setCellFactory(ShangmaNameNextdayCellFactory);
    shangmaNextDayJu
        .setCellValueFactory(new PropertyValueFactory<ShangmaDetailInfo, String>("shangmaJu"));
    shangmaNextDayJu.setCellFactory(ShangmaNameNextdayCellFactory);
    shangmaNextDaySM
        .setCellValueFactory(new PropertyValueFactory<ShangmaDetailInfo, String>("shangmaSM"));
    shangmaNextDaySM.setCellFactory(ShangmaNameNextdayCellFactory);
    setColumnCenter(shangmaNextDayName, shangmaNextDayJu, shangmaNextDaySM);

    
    // 实时上马系统
    shangmaService.initShangma();

  }
  
  /**
   * 点击实时上码Tab时加载数据
   * 
   * @time 2018年7月22日
   */
  public void loadWhenClickTab() {
    // 刷新上码中的teamWanjiaIdMap
    shangmaService.refreshTeamIdAndPlayerId();

    // 获取最新的实时金额Map {玩家ID={}}
    Map<String, CurrentMoneyInfo> lastCMIMap = new HashMap<>();;
    ObservableList<CurrentMoneyInfo> obList = myController.tableCurrentMoneyInfo.getItems();
    if (obList != null) {
      String pId = "";
      for (CurrentMoneyInfo cmiInfo : obList) {
        pId = cmiInfo.getWanjiaId();
        if (StringUtil.isNotBlank(pId)) {
          lastCMIMap.put(pId, cmiInfo);
        }
      }
    }
    shangmaService.cmiMap = lastCMIMap;

    // 获取最新的上码表的个人详情
    dataConstants.refresh_SM_Detail_Map();

    // 加载数据
    String shangmaTeamIdValue = shangmaTeamId.getText();
    if (StringUtil.isNotBlank(shangmaTeamIdValue)) {
      // ShangmaService.loadShangmaTable(shangmaTeamIdValue,tableShangma);
    } else {
      if (dataConstants.huishuiMap.containsKey("公司")) {
        shangmaTeamIdValue = "公司";
        shangmaTeamId.setText("公司");
      } else {
        shangmaTeamIdValue = ((Button)shangmaVBox.getChildren().get(0)).getText();
      }
    }
    shangmaService.loadShangmaTable(shangmaTeamIdValue, tableShangma);
  }

  /**
   * 单击上码记录
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void setTableShangmaSelect() {
    tableShangma.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        ShangmaInfo smInfo = (ShangmaInfo) newValue;
        // 加载右边的个人详情
        if (smInfo != null) {
          String playerId = smInfo.getShangmaPlayerId();
          if (!StringUtil.isBlank(playerId)) {
            shangmaService.loadSMDetailTable(playerId);
            shangmaService.loadSMNextDayTable(playerId);
          }
        } else
          tableShangmaDetail.setItems(null);
      }
    });
  }


  /**
   * 单独设置列居中
   * 
   * @param colums
   */
  @SuppressWarnings("unchecked")
  public <T extends Entity> void setColumnCenter(TableColumn<T, ?>... colums) {
    for (TableColumn<T, ?> column : colums) {
      column.setStyle(Constants.CSS_CENTER);
    }
  }

  /**
   * 右下表：次日上码详情表添加双击名称事件
   */
  Callback<TableColumn<ShangmaDetailInfo, String>, TableCell<ShangmaDetailInfo, String>> ShangmaNameNextdayCellFactory =
      new Callback<TableColumn<ShangmaDetailInfo, String>, TableCell<ShangmaDetailInfo, String>>() {
        @Override
        public TableCell<ShangmaDetailInfo, String> call(
            TableColumn<ShangmaDetailInfo, String> param) {
          TextFieldTableCell<ShangmaDetailInfo, String> cell = new TextFieldTableCell<>();
          cell.setEditable(false);// 不让其可编辑
          cell.setOnMouseClicked((MouseEvent t) -> {
            // 鼠标双击事件
            if (t.getClickCount() == 2 && cell.getIndex() < tableShangmaNextDay.getItems().size()) {
              // 双击执行的代码
              ShangmaDetailInfo smDetail = tableShangmaNextDay.getItems().get(cell.getIndex());
              shangmaService.openAddNextdayShangSMDiag(smDetail);
            }
          });
          return cell;
        }
      };
      
  /**
   * 上码详情表添加双击名称事件
   */
  Callback<TableColumn<ShangmaDetailInfo, String>, TableCell<ShangmaDetailInfo, String>> ShangmaNameCellFactory =
      new Callback<TableColumn<ShangmaDetailInfo, String>, TableCell<ShangmaDetailInfo, String>>() {
        @Override
        public TableCell<ShangmaDetailInfo, String> call(
            TableColumn<ShangmaDetailInfo, String> param) {
          TextFieldTableCell<ShangmaDetailInfo, String> cell = new TextFieldTableCell<>();
          cell.setEditable(false);// 不让其可编辑
          cell.setOnMouseClicked((MouseEvent t) -> {
            // 鼠标双击事件
            if (t.getClickCount() == 2 && cell.getIndex() < tableShangmaDetail.getItems().size()) {
              // 双击执行的代码
              ShangmaDetailInfo smDetail = tableShangmaDetail.getItems().get(cell.getIndex());
              shangmaService.openAddShangSMDiag(smDetail);
            }
          });
          return cell;
        }
      };

  /**
   * 解决上码主表行双击事件
   * 
   */
  private class TableRowControl<T> extends TableRow<T> {
    public TableRowControl(TableView<T> tableView) {
      super();
      this.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          int index = TableRowControl.this.getIndex();
          if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2
              && index < tableView.getItems().size()) {
            // 双击执行的代码
            ShangmaInfo smInfo = tableShangma.getItems().get(index);
            shangmaService.openNewShangSMDiag(smInfo);
          }
        }
      });
    }
  }
  
  /**
   * 上码搜索(按钮)
   */
  public void shangmaSearchAction(ActionEvent event) {
    String keyWord = shangmaSearch.getText();
    if (!StringUtil.isBlank(keyWord)) {
      shangmaService.shangmaSearch(keyWord, shangmaTeamId);
    }
  }

  /**
   * 上码搜索(回车)
   */
  public void shangmaSearchByEnter(KeyEvent event) {
    String keyWord = shangmaSearch.getText();
    if (KeyCode.ENTER == event.getCode() && !StringUtil.isBlank(keyWord)) {
      shangmaService.shangmaSearch(keyWord, shangmaTeamId);
    }
  }

  /**
   * 实时上码导出为Excel
   */
  public void exportSMExcelAction(ActionEvent event) {
    shangmaService.exportShangmaExcel();
  }


  /**
   * 实时上码开始新的一天由用户自行点击加载次日的数据
   * 
   * @time 2018年2月4日
   * @param event
   */
  public void loadNextDayDataAction(ActionEvent event) {
    shangmaService.loadNextDayDataAction();
  }

  /**
   * 实时上码新增次日上码
   * 
   * @time 2018年2月4日
   * @param event
   */
  public void addNextDaySMDetailAction(ActionEvent event) {
    shangmaService.addNextDaySMDetailAction();
  }

  /**
   * 保存实时上码中的团队押金与团队额度修改
   * 
   * @param event
   */
  public void saveTeamYajinAndEduAction(ActionEvent event) {
    shangmaService.updateTeamYajinAndEdu();
  }
  
  /**
   * 过滤团队
   * 
   * @param event
   */
  @FXML
  public void setFilterTeamsAction(ActionEvent event) {
    
    JFXChipView<String> chipView = new JFXChipView<>();
    chipView.getChips().addAll(filterTeams);
    chipView.getSuggestions().addAll(dataConstants.huishuiMap.keySet());
    chipView.setStyle("-fx-background-color: WHITE;");
    chipView.clipProperty().addListener( e -> {
      System.out.println(e.toString());
    });

    StackPane pane = new StackPane();
    pane.getChildren().add(chipView);
    StackPane.setMargin(chipView, new Insets(20));
    pane.setStyle("-fx-background-color:white;");
//    pane.setStyle("-fx-background-color:url(http://pic.58pic.com/58pic/14/55/95/31858PICxyH_1024.jpg)");

    final Scene scene = new Scene(pane, 500, 500);
//    scene.getStylesheets().add(SMController.class.getResource("/css/myCss.css").toExternalForm());
    Stage stage = new Stage();
    stage.setTitle("过滤团队【请输入后按回车键】");
    ShowUtil.setIcon(stage);
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(e -> {
      filterTeams = chipView.getChips();
      //TODO 保存到数据库
      
      // 过滤界面
    });
  }

  
  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
