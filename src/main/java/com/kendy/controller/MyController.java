package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.kendy.application.Main;
import com.kendy.application.SpringFxmlLoader;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.controller.tgController.TGController;
import com.kendy.db.DBConnection;
import com.kendy.db.DBUtil;
import com.kendy.db.entity.Player;
import com.kendy.db.service.PlayerService;
import com.kendy.entity.Huishui;
import com.kendy.enums.KeyEnum;
import com.kendy.excel.ExcelReaderUtil;
import com.kendy.model.CombineID;
import com.kendy.other.Wrap;
import com.kendy.service.JifenService;
import com.kendy.service.MemberService;
import com.kendy.service.MoneyService;
import com.kendy.service.ShangmaService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.ZonghuiService;
import com.kendy.util.AlertUtil;
import com.kendy.util.CollectUtil;
import com.kendy.util.DialogUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.MaskerPaneUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.SystemUtil;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 财务软件主界面的组件控制器
 *
 * @author 林泽涛
 * @time 2018年1月1日 下午10:55:48
 */
@Component
public class MyController extends BaseController implements Initializable {

  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public CombineIDController combineIDController;
  @Autowired
  public SMAutoController smAutoController; // 上码控制类
  @Autowired
  public TeamProxyController teamProxyController; // 代理控制类
  @Autowired
  public TGController tgController; // 托管控制类
  @Autowired
  public BankFlowController bankFlowController; // 银行流水控制类
  @Autowired
  public StaticController staticController; // 银行流水控制类
  @Autowired
  public GDController gdController; // 股东控制类
  @Autowired
  public LMController lmController; // 联盟控制类
  @Autowired
  public QuotaController quotaController; // 配帐控制类
  @Autowired
  public JifenService jifenService; // 积分控制类
  @Autowired
  public MemberService memberService; // 配帐控制类
  @Autowired
  public SMController smController; // 上码控制类
  @Autowired
  public ShangmaService shangmaService; // 上码控制类
  @Autowired
  public TeamProxyService teamProxyService; // 配帐控制类
  @Autowired
  public ZonghuiService zonghuiService; // 配帐控制类
  @Autowired
  public MoneyService moneyService; // 配帐控制类
  @Autowired
  public DataConstans dataConstants; // 数据控制类
  @Autowired
  public ExcelReaderUtil excelReaderUtil; // excel读取类
  @Autowired
  public ZjStaticController zjStaticController; // 战绩统计控制类
  @Autowired
  public WaizhaiController waizhaiController; // 外债控制类
  @Autowired
  public JifenQueryController jifenQueryController; // 积分控制类
  @Autowired
  public ChangciController changciController;

  @Resource
  PlayerService playerService;


  private static Tooltip tooltip = null;

  static {
    // 设置Tooltip
    tooltip = new Tooltip("您无权限打开此页面！");
    tooltip.setStyle("-fx-font: normal bold 25 Langdon; "
        + "-fx-base: #AE3522; "
        + "-fx-text-fill: orange;");
  }



  public MyController() {
    super();
    logger.info("执行MyController构造方法");
  }


  @PostConstruct
  public void inits() {
    logger.info("@PostConstruct MyController");

  }

  @FXML
  public TextField sysCode; // 系统编码
  @FXML
  public TextField membersDir; // 人员名单Excel路径
  @FXML
  public TextField huishuiDir; // 回水Excel路径
  @FXML
  public TextField preDataDir; // 回水Excel路径
  @FXML
  public Label currentClubId; // 当前俱乐部ID
  @FXML
  public TextField teamIdField; // 新增团队ID
  @FXML
  public TextField teamNameField; // 新增团队名称
  @FXML
  public TextField huishui; // 新增团队回水
  @FXML
  public TextField insuranceRate; // 新增保险比例
  @FXML
  public TextField gudongInput; // 新增股东名称
  @FXML
  public ListView<String> gudongListView; // 所有股东名称

  @FXML
  public Label dbConnectionState;// 数据库连接状态

  @FXML
  public Button importHuishuiBtn;
  @FXML
  public Button importMembersBtn;
  @FXML
  public Button importPreDataBtn;

  @FXML
  public TextField combineIdDir;
  @FXML
  public RadioButton whiteVersionOld;
  @FXML
  public RadioButton whiteVersionNew;

  @FXML
  public RadioButton radio_rate_0975;
  @FXML
  public RadioButton radio_rate_095;



  // =================================================TabPane
  @FXML
  public TabPane tabs;



  // ===================================================================
  @FXML
  public StackPane basicInfoStackPane;

  @FXML
  public RadioButton radio_autoTest_yes;
  @FXML
  public RadioButton radio_autoTest_no;

  // 保存到other数据表的key
  private final String KEY_GU_DONG = KeyEnum.GU_DONG.getKeyName();
  private final String KEY_CLUB_ID = KeyEnum.CLUB_ID.getKeyName();




  /**
   * 节点加载完后需要进行的一些初始化操作 Initializes the controller class. This method is automatically called after
   * the fxml file has been loaded.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 第一次打开主窗口时设置当前俱乐部ID值
    String clubIdValue = dbUtil.getValueByKeyWithoutJson(KEY_CLUB_ID);
    currentClubId.setText(clubIdValue);

    // 第一次打开主窗口时显示所有股东
    String gudongs = dbUtil.getValueByKeyWithoutJson(KEY_GU_DONG);
    dataConstants.gudongList = new ArrayList<>();
    if (!StringUtil.isBlank(gudongs)) {
      for (String gudong : gudongs.split(",")) {
        dataConstants.gudongList.add(gudong);
      }
    }
    for (String gd : dataConstants.gudongList) {
      gudongListView.getItems().add(gd);
    }



    tabsAction();

    // 合并ID
    combineIDController.initCombineIdController(changciController.tableCurrentMoneyInfo);



    // 选择导入白名单的版本
    initWhiteVersion();

    // 初始化全局比例
    initHSRate();

    // 加载各个tab页面，备注：此方法应放在tabsAction之前
    loadSubTabs();

    if (SpringFxmlLoader.getContext() == null) {
      logger.error("SpringFxmlLoader.getContext() is null");
    } else {
      logger.info("SpringFxmlLoader.getContext() is not null >>>>>");
      String[] beanDefinitionNames = SpringFxmlLoader.getContext().getBeanDefinitionNames();
      for (String bean : beanDefinitionNames) {
        logger.info(bean);
      }
      logger.info("以上为spring容器中加载的bean\n\n");
    }

    // 是否启动测试模式
    initAutoTestMode();

  }




  /**
   * 加载各个tab页面
   */
  private void loadSubTabs() {
    addSubTab("场次信息", "changci_tab_frame.fxml");
    addSubTab("总汇信息", "zonghui_tab_frame.fxml");
    addSubTab("外债信息", "waizhai_tab_frame.fxml");
    addSubTab("会员查询", "member_query_tab_frame.fxml");
    addSubTab("积分查询", "jifen_query_tab_frame.fxml");
    addSubTab("代理查询", "team_proxy_tab_frame.fxml");
    addSubTab("实时上码系统", "shangma_tab_frame.fxml");
    addSubTab("联盟对账", "LM_Tab_Fram.fxml");
    addSubTab("联盟配账", "Quota_Tab_Fram.fxml");
    addSubTab("股东贡献值", "gudong_contribution.fxml");
    addSubTab("托管工具", "TG_toolaa.fxml");
    addSubTab("自动上码配置", "SM_Autos.fxml");
    addSubTab("银行流水", "bank_flow_frame.fxml");
    addSubTab("历史统计", "history_static_tab_frame.fxml");
    addSubTab("战绩统计", "zj_static_tab_frame.fxml");
  }

  /**
   * 加载子单个Tab
   *
   * @time 2018年7月5日
   */
  private Tab addSubTab(String tabName, String frameName) {
    try {
      String path = "/dialog/" + frameName;
      Parent root = (Parent) Main.loader.load(path);
      Tab subTab = new Tab();
      subTab.setText(tabName);
      subTab.setClosable(false);
      subTab.setContent(root);
      tabs.getTabs().add(subTab);
      return subTab;
    } catch (Exception e) {
      ErrorUtil.err(tabName + "tab加载失败", e);
    }
    return null;
  }

  /**
   * 初始化白名单版本
   *
   * @time 2018年1月4日
   */
  private void initWhiteVersion() {
    ToggleGroup group = new ToggleGroup();
    whiteVersionOld.setToggleGroup(group);
    whiteVersionNew.setToggleGroup(group);
    whiteVersionOld.setUserData(0);// "旧名单"
    whiteVersionNew.setUserData(1);// "新名单"
    whiteVersionNew.setSelected(true);
    logger.info("默认导入版本：新名单");
    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
        Integer version = (Integer) group.getSelectedToggle().getUserData();
        if (version == 1) {
          logger.info("导入版本：新名单");
        } else {
          logger.info("导入版本：旧名单");
        }
      }
    });
  }

  /**
   * 获取版本类型 0：旧版本 1：新版本
   *
   * @time 2018年1月5日
   */
  public int getVersionType() {
    return whiteVersionOld.isSelected() ? 0 : 1;
  }



  /**
   * 界面回水比例
   *
   * @time 2018年5月18日
   */
  private void initHSRate() {
    ToggleGroup group = new ToggleGroup();
    radio_rate_0975.setToggleGroup(group);
    radio_rate_095.setToggleGroup(group);
    String rate0975 = "0.975";
    String rate095 = "0.95";
    radio_rate_0975.setUserData(rate0975);
    radio_rate_095.setUserData(rate095);
    radio_rate_095.setSelected(true);
    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
        String rate = (String) group.getSelectedToggle().getUserData();
        if ("0.975".equals(rate)) {
          Constants.CURRENT_HS_RATE = 0.975;
          logger.info("当前全局比例修改为0.975");
        } else {
          Constants.CURRENT_HS_RATE = 0.95;
          logger.info("当前全局比例修改为0.95");
        }
      }
    });
  }

  /**
   * tabs切换事件
   *
   * @time 2017年11月12日
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void tabsAction() {
    tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        Tab tab = (Tab) newValue;
        if (tab == null) {
          return;
        }
        String tabName = tab.getText();
        logger.info(" newTab:" + tabName);
        switch (tabName) {
          case "场次信息": {
            moneyService.flush_SSJE_table();
            moneyService.update_Table_CMI_Map();// 更新{玩家ID=CurrentMoneyInfo},感觉没什么用
            break;
          }
          case "代理查询": {
            teamProxyController.loadWhenClickTab();
            break;
          }
          case "实时上码系统": {
            smController.loadWhenClickTab();
            break;
          }
          case "联盟对账": {
            lmController.refreshClubList();
            break;
          }
          case "联盟配账": {
            lmController.refreshClubList();
            // 导入每场战绩时的所有俱乐部记录
            quotaController.currentRecordList = lmController.currentRecordList;
            // {俱乐部ID : 俱乐部信息}
            quotaController.allClubMap = lmController.allClubMap;
            // {俱乐部ID : 俱乐部每一场信息}
            quotaController.eachClubList = lmController.eachClubList;
            // 缓存三个联盟的信息
            quotaController.LMTotalList = lmController.LMTotalList;
            // 自动加载联盟1的数据
            quotaController.autoSelectLM1();
            break;
          }
          case "托管工具": {
            tgController.loadDataLastest();
            break;
          }
          case "自动上码配置": {
            shangmaService.refreshTeamIdAndPlayerId();
            break;
          }
          case "银行流水": {
            bankFlowController.refresh();
            break;
          }
          case "历史统计": {
            //staticController.refresh();
            //break;
          }
        }
      }
    });
  }





  // ==================================== 打开文件选择 Excel  ====================================

  /**
   * 导入excel文件选择器
   */
  public File openBasicExcelDialog(TextField textField, String title) {
    String rootPathName = !StringUtil.isBlank(dataConstants.Root_Dir) ? dataConstants.Root_Dir
        : System.getProperty("user.home");
    FileChooser fileChooser = new FileChooser();// 文件选择器
    fileChooser.setTitle(Constants.TITLE + ": " + title);// 标题
    fileChooser.setInitialDirectory(new File(rootPathName));// 初始化根目标
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel", "*.xls?"));
    File file = null;
    try {
      file = fileChooser.showOpenDialog(Main.primaryStage0);
    } catch (Exception e) {
      fileChooser.setInitialDirectory(SystemUtil.getUserFile());// 初始化根目标
      file = fileChooser.showOpenDialog(Main.primaryStage0);
    }
    if (file != null) {
      String filePath = file.getAbsolutePath();
      textField.setText(filePath);
      dataConstants.Root_Dir = filePath.substring(0, filePath.lastIndexOf("\\"));
    }
    return file;
  }

  /**
   * 打开人员名单excel
   */
  public void openMembersExcelAction(ActionEvent event) {
    openBasicExcelDialog(membersDir, "选择人员名单Excel");
  }



  /**
   * 打开回水比例excel
   */
  public void openHuishuiExcelAction(ActionEvent event) {
    openBasicExcelDialog(huishuiDir, "请选择回水比例Excel");
  }

  /**
   * 打开昨日留底excel
   */
  public void openPreDataExcelAction(ActionEvent event) {
    openBasicExcelDialog(preDataDir, "请选择回水比例Excel");
  }

  /**
   * 打开合并ID的模板xcel
   */
  public void opentBatchCombineIDDialogAction(ActionEvent event) {
    openBasicExcelDialog(combineIdDir, "请选择合并ID的模板xcel");
  }

  // ======================= 导入 Excel =============================

  /**
   * 导入人员名单文件
   */
  public void importMembersExcelAction(ActionEvent even) {
    String membersFilePath = membersDir.getText();
    if (StringUtil.isNotBlank(membersFilePath)) {
      // 将人员名单文件缓存起来
      try {
        Map<String, Player> allPlayers =
            excelReaderUtil.readMembersRecord(new File(membersFilePath));
        dataConstants.membersMap.putAll(allPlayers);// 求并集,key相同的会被替换掉
        // 插入到数据库
        List<Player> players = (List<Player>) allPlayers.values();
        playerService.save(players);

        ShowUtil.show("导入人员名单成功", 2);
        // 刷新相关缓存
        dataConstants.refresh_SM_Detail_Map();// 加载测试数据
      } catch (Exception e) {
        ErrorUtil.err("导入人员名单失败", e);
      }
    } else {
      ShowUtil.warn("您未选择需要导入的Excel!");
    }
  }

  /**
   * 导入回水比例文件
   */
  @SuppressWarnings("unchecked")
  public void importHuishuiExcelAction(ActionEvent even) {
    String huishuiFilePath = huishuiDir.getText();
    if (!StringUtil.isBlank(huishuiFilePath)) {
      // 将人员名单文件缓存起来
      Wrap wrap = excelReaderUtil.readHuishuiRecord(new File(huishuiFilePath));
      if (wrap.resultSuccess) {
        dataConstants.huishuiMap.putAll((Map<String, Huishui>) wrap.obj);
        dbUtil.insertTeamHS((Map<String, Huishui>) wrap.obj);
        ShowUtil.show("导入回水比例成功", 2);
        // 代理查询初始化团队ID
        teamProxyService.initTeamSelectAndZjManage(teamProxyController.teamIDCombox);
//        if (teamProxyService != null) {
//          teamProxyService.initTeamSelectAndZjManage(teamProxyController.teamIDCombox);
//        }
        // 积分查询初始化团队ID
        jifenService.init_Jifen_TeamIdCombox();
//        if (jifenService != null) {
//          jifenService.init_Jifen_TeamIdCombox();
//        }
        // 上码系统中的团队ID按钮
        shangmaService.initShangmaButton();
//        if (shangmaService != null) {
//          shangmaService.initShangmaButton();
//        }
      } else {
        ShowUtil.show("导入回水比例失败", 2);
      }
    }
  }

  /**
   * 导入昨日留底文件 备注：后期要添加不要删除所有信息！！！！
   */
  public void importPreDataExcelAction(ActionEvent even) {
    String preDataFilePath = preDataDir.getText();
    if (!StringUtil.isBlank(preDataFilePath)) {
      // 将人员名单文件缓存起来
      try {
        dataConstants.preDataMap = excelReaderUtil.readPreDataRecord(new File(preDataFilePath));
        if (dataConstants.preDataMap.size() == 0) {
          ShowUtil.show("导入昨日留底失败!");
        } else {
          ShowUtil.show("导入昨日留底成功", 2);

          // 保存到数据库、
          String dataTime = "2017-01-01";// 第一次导入时是没有时间的，故到时可以改,注意这里没有匹配人员ID
          String json_preData = JSON.toJSONString(dataConstants.preDataMap);
          dbUtil.insertPreData(dataTime, json_preData);
        }
      } catch (Exception e) {
        ShowUtil.show("导入昨日留底出错,原因：" + e.getMessage());
        e.printStackTrace();
      }
    }
  }




  /**
   * 检查共享额度
   *
   * @deprecated
   */
  private void checkOverShareEdu() {
    String overShareEduResult = lmController.getOverShareEduResult(false);
    if (StringUtils.isNotBlank(overShareEduResult)) {
      ShowUtil.show("超出共享额度", false, overShareEduResult);
    }
  }






  /**
   * 导入合并ID模板 备注：后期要判断是否在父子ID是否在同一个团队里面
   */
  public void importBatchCombineIdExcelAction(ActionEvent event) {

    String combineIdPath = combineIdDir.getText();
    if (StringUtil.isBlank(combineIdPath)) {
      ShowUtil.show("哥，你还没选择要导入合并ID的Excel文件!!");
      return;
    }
    try {
      // 读取模板数据
      long start = System.currentTimeMillis();
      final List<CombineID> importedCombinIds =
          excelReaderUtil.readCombineIdRecord(new File(combineIdPath));

      // 与现有的合并ID缓存进行求并集（更新缓存）
      for (CombineID combineId : importedCombinIds) {
        String _parentId = combineId.getParentId();
        Set<String> _subIdSet = combineId.getSubIds();

        Set<String> subIdSet = dataConstants.Combine_Super_Id_Map.get(_parentId);
        if (subIdSet == null) {// 缓存中没有此父节点，可直接入库
          // 更新缓存
          dataConstants.Combine_Super_Id_Map.put(_parentId, _subIdSet);// 更新父节点
          _subIdSet.forEach(_subID -> {
            dataConstants.Combine_Sub_Id_Map.put(_subID, _parentId);// 更新子节点
          });
          // 入库
          dbUtil.saveOrUpdateCombineId(_parentId, _subIdSet);
        } else {// 缓存中存在此父节点，需要进行比较
          boolean isNeedSave = false;
          for (String _subId : _subIdSet) {
            if (!subIdSet.contains(_subId)) {
              isNeedSave = true;
              subIdSet.add(_subId);// 更新父节点
              dataConstants.Combine_Sub_Id_Map.put(_subId, _parentId);// 更新子节点
            }
          }
          if (isNeedSave) {
            // 入库
            dbUtil.saveOrUpdateCombineId(_parentId, subIdSet);
          }
        }
      }
      long end = System.currentTimeMillis();

      FXUtil.info("导入合并ID模板成功！耗时：" + (end - start) + "毫秒");
    } catch (Exception e) {
      ShowUtil.show("导入合并ID模板失败！原因：" + e.getMessage());
      e.printStackTrace();
    }
  }




  /**
   * 修改当前俱乐部ID
   */
  public void configCurrentClueIdAction(ActionEvent event) {
    new DialogUtil("修改", "新俱乐部ID:").getTextResult().ifPresent(newClubId -> {
      String _currentClubId = dbUtil.getValueByKeyWithoutJson(KEY_CLUB_ID);
      if (!StringUtils.equals(newClubId, _currentClubId) && StringUtil.isNotBlank(newClubId)) {
        dbUtil.saveOrUpdateOthers(KEY_CLUB_ID, newClubId);
        ShowUtil.show("软件的当前俱乐部为" + newClubId, 2);
        currentClubId.setText(newClubId);
      }
    });

  }

  /**
   * 增加股东
   */
  public void addGudongAction(ActionEvent event) {
    String newGudongName = gudongInput.getText();
    if (StringUtil.isNotBlank(newGudongName)) {
      if (!gudongListView.getItems().contains(newGudongName)) {
        gudongListView.getItems().add(newGudongName);
        gudongInput.setText("");
        // 修改缓存
        dataConstants.gudongList.add(newGudongName);

        // 更新数据库
        String gudongs = dataConstants.gudongList.stream().collect(Collectors.joining(","));
        dbUtil.saveOrUpdateOthers(KEY_GU_DONG, gudongs);
      }
    }
  }

  /**
   * 删除股东
   */
  public void delGudongAction(ActionEvent event) {
    String selectedGudongName = gudongListView.getFocusModel().getFocusedItem();
    if (StringUtil.isNotBlank(selectedGudongName)) {
      gudongListView.getItems().remove(selectedGudongName);
      List<String> cacheGudongs = dataConstants.gudongList;
      cacheGudongs.remove(selectedGudongName);

      // 持久化
      String gudongs = cacheGudongs.stream().collect(Collectors.joining(","));
      dbUtil.saveOrUpdateOthers(KEY_GU_DONG, gudongs);
    }
  }

  /**
   * 获取股东列表
   *
   * @time 2018年2月21日
   */
  public ObservableList<String> getGudongList() {
    return FXCollections.observableArrayList(dataConstants.gudongList);
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
      if (dataConstants.framesNameMap.get(windowName) == null) {
        // 打开新对话框
        String filePath = "/dialog/" + path;
        //Parent root = FXMLLoader.load(getClass().getResource(filePath));
        Parent root = (Parent) Main.loader.load(filePath);
        Stage addNewPlayerWindow = new Stage();
        Scene scene = new Scene(root);
        addNewPlayerWindow.setTitle(title);
        addNewPlayerWindow.setScene(scene);
        ShowUtil.setIcon(addNewPlayerWindow);
        addNewPlayerWindow.show();
        // 缓存该对话框实例
        dataConstants.framesNameMap.put(windowName, addNewPlayerWindow);
        addNewPlayerWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
            dataConstants.framesNameMap.remove(windowName);
          }
        });

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 打开增加人员名单对话框
   */
  public void openNewPlayerDialogAction(ActionEvent event) {
    openBasedDialog("add_new_player_frame.fxml", "新增人员名单", Constants.ADD_NEWPALYER_FRAME);
  }

  /**
   * 打开增加团队回水对话框
   */
  public void addHuishuiAction(ActionEvent event) {
    openBasedDialog("add_huishui_frame.fxml", "新增团队回水", Constants.ADD_TEAM_HUISHUI_FRAME);
  }



  /**
   * 打开删除人员对话框
   */
  public void openDelMemberDialogAction(ActionEvent event) {
    openBasedDialog("del_member_framesss.fxml", "人员删除或修改", Constants.DEL_MEMBER_FRAME);
  }



  /**
   * 打开新增合并ID对话框
   */
  public void opentCombineIDDialogAction(ActionEvent event) {
    openBasedDialog("combine_player_id_framess.fxml", "合并ID", Constants.COMBINE_ID_FRAME);
  }






  public void dbConnectAction(ActionEvent event) {
    if (DBConnection.getConnection() != null) {
      dbConnectionState.setText("已成功连接！");
      dbConnectionState.setTextFill(Color.web("#0076a3"));
    }
  }








  /**
   * 用户输入新的一天
   */
  private boolean handleNewDayTimeOK() {
    String maxGameRecordTimeStr = dbUtil.getMaxGameRecordTime();
    String newDay =
        StringUtil.isBlank(maxGameRecordTimeStr) ? LocalDate.now().toString() :
            LocalDate.parse(maxGameRecordTimeStr).plusDays(1).toString();
    TextInputDialog textDialog = new TextInputDialog(newDay);
    textDialog.setTitle("提示-昨天的数据库最后时间为：" + StringUtil.nvl(maxGameRecordTimeStr, "无"));
    textDialog.setHeaderText(null);
    textDialog.setContentText("请输入新一天时间:");
    ShowUtil.setIcon(textDialog);
    Optional<String> timeOpt = textDialog.showAndWait();
    timeOpt.ifPresent(userTime -> {
      try {
        userTime = LocalDate.parse(StringUtil.nvl(userTime, "")).toString();
        // 判断时间范围
        if (StringUtil.isNotBlank(maxGameRecordTimeStr)
            && userTime.compareTo(maxGameRecordTimeStr) <= 0) {
          ErrorUtil.err("输入的时间不能小于" + maxGameRecordTimeStr);
          return;
        }

        dataConstants.Date_Str = userTime;
      } catch (Exception e) {
        logger.error("输入新的一天时间格式错误:" + userTime, e);
      }
    });
    if (StringUtil.isBlank(dataConstants.Date_Str)) {
      ShowUtil.show("时间不准确！！");
      return Boolean.FALSE;
    } else {
      changciController.softDateLabel.setText(dataConstants.Date_Str);
      logger.info("客户输入新一天的时间是：" + dataConstants.Date_Str);
    }
    return Boolean.TRUE;
  }

  /**
   * 开始新一天的统计
   */
  public void openTimeInputAction(ActionEvent event) {
    if (AlertUtil.confirm("即将初始化所有配置项，点击确定开始操作新一天报表!")) {

      // 从数据库中判断是否符合从中途继续条件
      if (2 == dbUtil.newStaticOrContinue()) {
        ShowUtil.show("开始新一天的统计条件不满足，请点击中途继续按钮！");
        return;
      }
      // 处理输入时间
      if (!handleNewDayTimeOK()) {
        return;
      }

      // excelDir.setText("C:\\Users\\kendy\\Desktop\\2018-07-26已锁定\\已锁定-1532541147231-07月26号-战绩导出-24-09.xls");

      // 清空所有缓存数据
      dataConstants.clearAllData();

      dataConstants.Date_Str = changciController.getSoftDate();// 此行代码不能删，因为上行代码已将其时间删除
      // 加载必要的原始数据（人员和回水）
      dataConstants.initMetaData();
      // 加载昨日数据
      dataConstants.loadPreData();

      // 渲染表格数据
      changciController.LMLabel.setText(changciController.getLMValFirstTime());
      changciController.fillTables(changciController.tableCurrentMoneyInfo,
          changciController.tableZijin, changciController.tableProfit,
          changciController.tableKaixiao, changciController.LMLabel);
      ShowUtil.show("加载数据成功", 2);
      // 转到场次信息页面
      tabs.getSelectionModel().select(1);

      // 代理查询：
      List<Huishui> list = dbUtil.getAllTeamHS();
      list.forEach(hs -> {
        dataConstants.huishuiMap.put(hs.getTeamId(), hs);
      });
      teamProxyService.initTeamSelectAndZjManage(teamProxyController.teamIDCombox);

      // 积分查询初始化团队ID
      jifenService.init_Jifen_TeamIdCombox();

      // 实时上码表：清空所有SM_Detail
      dataConstants.SM_Detail_Map = new HashMap<>();// 新一天统计时应该清空昨日数据

      // 初始化股东列表
      dataConstants.initGudong();
    }
  }




  /**
   * 中途继续按钮
   */
  public void middle2ContinueAction(ActionEvent event) {
    String content = "即将加载关闭之前的最后锁定数据，确认要从中途加载吗？";
    if (AlertUtil.confirm(content)) {
      // 从数据库中判断是否符合从中途继续条件
      if (1 == dbUtil.newStaticOrContinue()) {
        ShowUtil.show("中途继续条件不满足，请点击开始新一天的统计！");
        return;
      }
      MaskerPaneUtil.addMaskerPane(basicInfoStackPane);
      Task task = new Task<Void>() {
        @Override
        public Void call() throws Exception {
          Thread.sleep(1500);
          Platform.runLater(() -> {
            // 中途继续具体逻辑
            doMiddleConintue();
          });
          return null;
        }

        @Override
        protected void succeeded() {
          super.succeeded();
          MaskerPaneUtil.hideMaskerPane(basicInfoStackPane);
        }
      };
      new Thread(task).start();

    }
  }

  public void doMiddleConintue() {
    // 恢复所有缓存数据
    dataConstants.recoveryAllCache();
    dataConstants.initMetaData();
    // add 2017-10-21 代理类初始化团队ID
    teamProxyService.initTeamSelectAndZjManage(teamProxyController.teamIDCombox);
    // add 2017-10-21 代理类初始化团队ID
    jifenService.init_Jifen_TeamIdCombox();

    // 加载十个表格数据
    int pageIndex = dataConstants.Paiju_Index.get();
    changciController.pageInput.setText(pageIndex + "");
    try {
      changciController.softDateLabel.setText(dataConstants.Date_Str);
      // 恢复锁定的数据
      changciController.changableData();
      // 加载前一场数据
      pageIndex -= 1;
      changciController.reCovery10TablesByPage(pageIndex);// 恢复十个表数据
      moneyService.flush_SSJE_table();

      // 清空相关表数据（保留类似昨日留底的表数据）
      clearData(changciController.tableTotalInfo,
          changciController.tablePaiju,
          changciController.tableTeam,
          changciController.tableDangju,
          changciController.tableJiaoshou,
          changciController.tablePingzhang);
      changciController.indexLabel.setText(changciController.INDEX_ZERO);
    } catch (Exception e) {
      ShowUtil.show("中途继续失败：原因：" + e.getMessage());
      e.printStackTrace();
    }
    ShowUtil.show("中途继续成功，请继续操作", 2);
    // 转到场次信息页面
    tabs.getSelectionModel().select(1);
  }

  /**
   * 结束今天统计按钮
   */
  public void endStaticAction(ActionEvent event) {
    if (AlertUtil.confirm("即将结束今天所有操作并将数据保存到数据库，确定？")) {

      if (dataConstants.All_Locked_Data_Map.isEmpty()) {
        ShowUtil.show("今日无锁定数据，不能归档！请检查");
        return;
      }
      // 将最后一场的锁定数据保存到数据库，只保留实时金额和昨日利润等
      endOneDayStaticAndSave();
      // 处理锁定数据
      dbUtil.handle_last_locked_data();
      dbUtil.del_all_locked_data_details();// 删除今日的锁定数据

      dbUtil.reset_clubZhuofei_to_0();// 重置联盟中俱乐部的桌费和已结算为0

      tgController.deleteKaixiaoAndComment();// 删除托管中的开销和玩家备注信息

      ShowUtil.show("已更新到数据库，即将关闭此软件。", 2);
      Main.primaryStage0.close();
      System.exit(0);
    }
  }

  /**
   * 结束一天的统计并进行归档
   *
   * @time 2018年7月7日
   */
  public void endOneDayStaticAndSave() {
    final Map<String, String> lockedMap = getLastLockedData();
    List<String> list = Arrays.asList("团队回水总和", "团队回水", "实时金额", "实时金额总和", "资金", "资金总和", "利润",
        "利润总和", "实时开销", "实时开销总和", "联盟对帐");
    Map<String, String> map = new HashMap<>();
    for (String key : list) {
      map.put(key, lockedMap.get(key));
    }
    // 锁定最后一场时默认实时开销只保留在利润中的总开销，具体记录清空
    map.put("实时开销", JSON.toJSONString(Arrays.asList()));// new KaixiaoInfo()
    map.put("实时开销总和", JSON.toJSONString(0));// new KaixiaoInfo()
    String time = dataConstants.Date_Str;
    dbUtil.insertPreData(time, JSON.toJSONString(map));
  }

  /**
   * 获取最新的锁定数据
   */
  public Map<String, String> getLastLockedData() {
    int paijuIndex = dataConstants.Index_Table_Id_Map.size();
    return dataConstants.All_Locked_Data_Map.get(paijuIndex + "");// 总缓存数据
    // Map<String,String> map = new HashMap<>();
    // map.put("战绩", JSON.toJSONString(TotalInfoList));
    // map.put("玩家", JSON.toJSONString(WanjiaInfoList));
    // map.put("团队回水", JSON.toJSONString(TeamInfoList));
    // map.put("团队回水总和", sumOfTeam);
    // map.put("实时金额", JSON.toJSONString(list));
    // map.put("实时金额总和", sumOfCurrentMoney);
    // map.put("资金", JSON.toJSONString(listZijinInfo));
    // map.put("资金总和", sumOfZijin);
    // map.put("利润", JSON.toJSONString(listProfitInfo));
    // map.put("利润总和", sumOfProfit);
    // map.put("实时开销", JSON.toJSONString(listKaixiaoInfo));
    // map.put("实时开销总和", sumOfKaixiao);
    // map.put("当局", JSON.toJSONString(listDangjuInfo));
    // map.put("当局总和", sumOfDangjuInfo);
    // map.put("交收", JSON.toJSONString(listJiaoshouInfo));
    // map.put("交收总和", sumOfJiaoshouInfo);
    // map.put("平帐", JSON.toJSONString(listPingzhangInfo));
    // map.put("平帐总和", sumOfPingzhangInfo);
    // map.put("联盟对帐", getLMLabelText(LMLabel));
  }


  /**
   * 清空数据库
   *
   * @time 2017年10月17日
   */
  public void clearAllDataAction(ActionEvent event) {
    if (AlertUtil.confirm("即将清空桌费、已结算、已锁定的历史数据，是否继续?")) {
      if (dbUtil.clearAllData()) {
        ShowUtil.show("清空成功。", 1);
      } else {
        ShowUtil.show("清空失败");
      }
    }
  }


  /**
   * 导出人员表
   */
  public void exportMembersExcelAction(ActionEvent event) {
    moneyService.exportMemberExcel();
  }

  /**
   * 导出回水表
   */
  public void exportHuishuiExcelAction(ActionEvent event) {
    moneyService.exportTeamhsExcel();
  }

  /**
   * 导出合并ID表
   */
  public void exportCombineIDAction(ActionEvent event) {
    moneyService.exportCombineIDAction();
  }





  /**
   * 删除团队ID 备注：此方法的界面按钮已经删除，但后台代码仍保留着（以备后期要重新开发）
   *
   * @time 2017年11月14日
   */
  public void delTeamAction(ActionEvent event) {

    // 注册界面
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("注册");
    dialog.setHeaderText(null);
    dialog.setContentText("删除团队ID:");
    Optional<String> result = dialog.showAndWait();

    result.ifPresent(_teamId -> {
      try {
        String teamId = _teamId.trim().toUpperCase();
        // 判断是否有此团队
        Huishui hs = dataConstants.huishuiMap.get(teamId);
        if (hs == null) {
          ShowUtil.show("哥，不存在此团队ID,请检查！");
          return;
        }

        // 清空数据库与缓存中回水表中的团队记录
        dbUtil.delHuishui(teamId);
        dataConstants.huishuiMap.remove(teamId);

        // 清空数据库与缓存中属于该团队的人员记录
        playerService.deleteByTeamId(teamId);
        synchronized (dataConstants.membersMap) {
          Iterator<Map.Entry<String, Player>> ite = dataConstants.membersMap.entrySet().iterator();
          while (ite.hasNext()) {
            Map.Entry<String, Player> entry = ite.next();
            Player player = entry.getValue();
            if (player.getTeamid().trim().toUpperCase().equals(teamId)) {
              ite.remove();// 删除
            }
          }
        }

        // 以下清除代码
        // A 清空代理查询中的团队下拉框
        String selected_team = teamProxyController.teamIDCombox.getSelectionModel()
            .getSelectedItem();
        teamProxyController.teamIDCombox.getItems().remove(teamId);
        if (teamId.equals(selected_team)
            && teamProxyController.teamIDCombox.getItems().size() > 0) {
          teamProxyController.teamIDCombox.getSelectionModel().select(0);
        }
        // B 积分查询中的团队下拉框
        ComboBox<String> jfTeamIDCombox = jifenQueryController.jfTeamIDCombox;
        String selected_jifen = jfTeamIDCombox.getSelectionModel().getSelectedItem();
        jfTeamIDCombox.getItems().remove(teamId);
        if (teamId.equals(selected_jifen) && jfTeamIDCombox.getItems().size() > 0) {
          jfTeamIDCombox.getSelectionModel().select(0);
        }
        // C 实时上码中的团队下拉框
        shangmaService.initShangmaButton();
        ((Button) smController.shangmaVBox.getChildren().get(0)).fire();

        ShowUtil.show("删除成功！", 2);
      } catch (Exception e) {
        logger.error("删除团队ID失败", e);
      }
    });
  }

  /**
   * 修改团队回水
   *
   * @time 2017年11月17日
   */
  public void updateTeamHsRateAction(ActionEvent event) {

    DialogUtil inputDlg = new DialogUtil("修改", "待修改的团队ID:", "团队新回水比例：");
    Optional<Pair<String, String>> result = inputDlg.getResult();
    result.ifPresent(teamId_and_hsRate -> {

      // 获取到原始的有效的团队ID及团队回水率
      String teamID = teamId_and_hsRate.getKey();
      String teamHsRate = teamId_and_hsRate.getValue();
      if (StringUtil.isBlank(teamID) || StringUtil.isBlank(teamHsRate)) {
        ShowUtil.show("修改失败！原因：团队ID或团队回水不能为空！！");
        return;
      }
      teamID = teamID.trim().toUpperCase();
      teamHsRate = teamHsRate.trim();
      Huishui hs = dataConstants.huishuiMap.get(teamID);// 判断是否有此团队
      if (hs == null) {
        ShowUtil.show("修改失败！原因：不存在此团队ID,请检查！");
        return;
      }
      if (!teamHsRate.endsWith("%")) {
        ShowUtil.show("修改失败！原因：团队回水必须中有百分号！");
        return;
      }
      // 开始修改
      // 1修改数据库
      Double _hsRate = NumUtil.getNumByPercent(teamHsRate);
      int size = (_hsRate + "").length();
      if (size > 6) {
        _hsRate = NumUtil.getNum(NumUtil.digit4(_hsRate.toString()));
      }
      if (!dbUtil.updateTeamHsRate(teamID, _hsRate + "")) {
        return;
      }

      // 2修改缓存
      hs.setHuishuiRate(NumUtil.getNumByPercent(teamHsRate) + "");// 地址引用，会修改值

      ShowUtil.show("修改成功！", 2);
    });
  }


  /**
   * 修改团队保险比例
   *
   * @time 2018年2月8日
   */
  public void updateTeamHsBaoxianRateAction(ActionEvent event) {

    DialogUtil inputDlg = new DialogUtil("修改团队保险比例", "待修改的团队ID:", "团队新保险比例：");
    Optional<Pair<String, String>> result = inputDlg.getResult();
    result.ifPresent(teamId_and_hsRate -> {

      // 获取到原始的有效的团队ID及团队回水率
      String teamID = teamId_and_hsRate.getKey();
      String teamHsRate = teamId_and_hsRate.getValue();
      if (StringUtil.isBlank(teamID) || StringUtil.isBlank(teamHsRate)) {
        ShowUtil.show("修改失败！原因：团队ID或团队回水不能为空！！");
        return;
      }
      teamID = teamID.trim().toUpperCase();
      teamHsRate = teamHsRate.trim();
      Huishui hs = dataConstants.huishuiMap.get(teamID);// 判断是否有此团队
      if (hs == null) {
        ShowUtil.show("修改失败！原因：不存在此团队ID,请检查！");
        return;
      }
      if (!teamHsRate.endsWith("%")) {
        ShowUtil.show("修改失败！原因：团队保险必须中有百分号！");
        return;
      }
      // 开始修改
      // 1修改数据库
      Double _hsRate = NumUtil.getNumByPercent(teamHsRate);
      int size = (_hsRate + "").length();
      if (size > 6) {
        _hsRate = NumUtil.getNum(NumUtil.digit4(_hsRate.toString()));
      }
      if (!dbUtil.updateTeamHsBaoxianRate(teamID, _hsRate + "")) {
        return;
      }

      // 2修改缓存
      hs.setInsuranceRate(NumUtil.getNumByPercent(teamHsRate) + "");// 地址引用，会修改值

      ShowUtil.show("修改成功！", 2);
    });
  }


  /**
   * 修改团队股东
   */
  public void updateTeamHsGudongAction(ActionEvent event) {

    DialogUtil inputDlg = new DialogUtil("修改团队股东", "待修改的团队ID:", "团队新股东：");
    Optional<Pair<String, String>> result = inputDlg.getResult();
    result.ifPresent(teamId_and_teamGD -> {

      // 获取到原始的有效的团队ID及团队回水率
      String teamID = teamId_and_teamGD.getKey();
      String teamGD = teamId_and_teamGD.getValue();
      if (StringUtil.isBlank(teamID) || StringUtil.isBlank(teamGD)) {
        ShowUtil.show("修改失败！原因：团队ID或团队股东不能为空！！");
        return;
      }
      teamID = teamID.trim().toUpperCase();
      teamGD = teamGD.trim();
      Huishui hs = dataConstants.huishuiMap.get(teamID);// 判断是否有此团队
      if (hs == null) {
        ShowUtil.show("修改失败！原因：不存在此团队ID,请检查！");
        return;
      }
      // 开始修改
      // 1修改数据库
      if (!dbUtil.updateTeamHsGudong(teamID, teamGD)) {
        ShowUtil.show("修改失败");
        return;
      }

      // 2修改缓存
      hs.setGudong(teamGD);// 地址引用，会修改值

      ShowUtil.show("修改成功！", 2);
    });
  }

  /**
   * 检测合并ID
   *
   * @time 2017年12月2日
   */
  public void checkCombineIdAction(ActionEvent even) {
    List<String> resList = combineIDController.checkCombineId();
    if (CollectUtil.isEmpty(resList)) {
      ShowUtil.show("检测通过", 2);
      return;
    }
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for (String msg : resList) {
      sb.append(++i).append("、").append(msg).append("\r\n\r\n");
    }
    Dialog<?> dialog = new Dialog<>();
    dialog.setTitle("检测结果");
    dialog.setHeaderText(null);
    ShowUtil.setIcon(dialog);

    ButtonType loginButtonType = new ButtonType("我知道了", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 15, 5, 10));

    TextArea textArea = new TextArea();
    textArea.setText(sb.toString());
    grid.add(textArea, 0, 0);

    dialog.getDialogPane().setContent(grid);

    dialog.show();

  }


  /**
   * 实时上码开始新的一天由用户自行点击加载次日的数据
   *
   * @time 2018年2月4日
   */
  public void loadNextDayDataAction(ActionEvent event) {
    shangmaService.loadNextDayDataAction();
  }

  /**
   * 实时上码新增次日上码
   *
   * @time 2018年2月4日
   */
  public void addNextDaySMDetailAction(ActionEvent event) {
    shangmaService.addNextDaySMDetailAction();
  }

  /**
   * 保存实时上码中的团队押金与团队额度修改
   */
  @FXML
  public void saveTeamYajinAndEduAction(ActionEvent event) {
    shangmaService.updateTeamYajinAndEdu();
  }



  /*******************************************************************************************
   *
   * 0.95与0.975切换版本
   *
   ********************************************************************************************/

  /**
   * 根据原始战绩获取回水
   * <p>
   * 1：出回水 2：收回水, 此时teamId非必要参数
   * <p>
   * 备注：0.95版本：如果原始战绩为正数，则出回水和收回水都是0
   *
   * @time 2018年5月19日
   */
  public String getHuishuiByYSZJ(String yszj, String teamId, int type) {
    try {
      if (Constants.FINAL_HS_RATE_0975 == Constants.CURRENT_HS_RATE) {
        // 0.975版本
        return getByYSZJ(yszj, teamId, type);

      } else {
        // 0.95版本
        Double zhanji = Double.valueOf(yszj);
        if (zhanji.compareTo(0d) >= 0) {
          return "0";
        } else {
          return getByYSZJ(yszj, teamId, type);
        }
      }
    } catch (Exception e) {
      logger.error("根据原始战绩获取出回水出错，原因:" + e.getMessage());
      return "0";
    }
  }

  /**
   * 根据原始战绩获取回水 (共用)
   *
   * @time 2018年5月19日
   */
  private String getByYSZJ(String yszj, String teamId, int type) {
    double zhanji = Double.parseDouble(yszj);
    if (type == 1) {
      return NumUtil.digit1(moneyService.getChuhuishui(yszj, teamId));
    } else {
      return NumUtil
          .digit1(Math.abs(zhanji) * (1 - Constants.CURRENT_HS_RATE) + "");
    }
  }

  /**
   * 启动测试模式
   *
   * @time 2017年11月11日
   */
  private void initAutoTestMode() {
    changciController.hbox_autoTestMode.setVisible(false);
    ToggleGroup group = new ToggleGroup();
    radio_autoTest_yes.setToggleGroup(group);
    radio_autoTest_no.setToggleGroup(group);
    radio_autoTest_yes.setUserData("是");
    radio_autoTest_no.setUserData("否");
    radio_autoTest_no.setSelected(true);
    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
        String autoTestMode = (String) group.getSelectedToggle().getUserData();
        if ("是".equals(autoTestMode)) {
          changciController.hbox_autoTestMode.setVisible(true);
        } else {
          changciController.hbox_autoTestMode.setVisible(false);
        }
      }
    });
  }

  /**
   * 获取当前俱乐部
   */
  public String getClubId() {
    return currentClubId.getText();
  }

  @Override
  public Class<?> getSubClass() {
    return getClass();
  }

}
