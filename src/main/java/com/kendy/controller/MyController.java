package com.kendy.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.application.Main;
import com.kendy.application.SpringFxmlLoader;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.controller.tgController.TGController;
import com.kendy.db.DBConnection;
import com.kendy.db.DBUtil;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.DangjuInfo;
import com.kendy.entity.DangtianHuizongInfo;
import com.kendy.entity.Huishui;
import com.kendy.entity.JiaoshouInfo;
import com.kendy.entity.JifenInfo;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.MemberZJInfo;
import com.kendy.entity.PingzhangInfo;
import com.kendy.entity.Player;
import com.kendy.entity.ProfitInfo;
import com.kendy.entity.TeamInfo;
import com.kendy.entity.TotalInfo;
import com.kendy.entity.WaizhaiInfo;
import com.kendy.entity.WanjiaInfo;
import com.kendy.entity.ZijinInfo;
import com.kendy.entity.ZonghuiInfo;
import com.kendy.entity.ZonghuiKaixiaoInfo;
import com.kendy.enums.KeyEnum;
import com.kendy.excel.ExcelReaderUtil;
import com.kendy.interfaces.Entity;
import com.kendy.model.CombineID;
import com.kendy.model.GameRecord;
import com.kendy.other.Wrap;
import com.kendy.service.JifenService;
import com.kendy.service.MemberService;
import com.kendy.service.MoneyService;
import com.kendy.service.ShangmaService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.WaizhaiService;
import com.kendy.service.ZonghuiService;
import com.kendy.util.AlertUtil;
import com.kendy.util.ClipBoardUtil;
import com.kendy.util.CollectUtil;
import com.kendy.util.DialogUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.FileUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.PathUtil;
import com.kendy.util.RandomUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.SystemUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.Text2ImageUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * 财务软件主界面的组件控制器
 * 
 * @author 林泽涛
 * @time 2018年1月1日 下午10:55:48
 */
@Component
public class MyController extends BaseController implements Initializable {

  Logger logger = Logger.getLogger(MyController.class);
  
  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public CombineIDController combineIDController ;
  @Autowired
  public SMAutoController smAutoController; // 上码控制类
  @Autowired
  public TeamProxyController teamProxyController; // 代理控制类
  @Autowired
  public TGController tgController; // 托管控制类
  @Autowired
  public BankFlowController bankFlowController; // 银行流水控制类
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
  public WaizhaiService waizhaiService; // 配帐控制类
  @Autowired
  public ZonghuiService zonghuiService; // 配帐控制类
  @Autowired
  public MoneyService moneyService; // 配帐控制类
  @Autowired
  public DataConstans dataConstants; // 数据控制类
  @Autowired
  public ExcelReaderUtil excelReaderUtil; // excel读取类


  private final String ZERO = "0";
  private final String INDEX_ZERO = "第0局";

  // 自动导入下一场战绩Excel的缓存队列
  private ArrayBlockingQueue<File> excelQueue = new ArrayBlockingQueue<>(2500);
  
  public MyController() {
    super();
    logger.info("执行MyController构造方法");
  }
  
  
  @PostConstruct
  public void inits() {
    logger.info("@PostConstruct MyController");

  }

  @FXML public TextField sysCode; // 系统编码
  @FXML public TextField membersDir; // 人员名单Excel路径
  @FXML public TextField huishuiDir; // 回水Excel路径
  @FXML public TextField preDataDir; // 回水Excel路径
  @FXML public TextField excelDir; // excel文件夹路径
  @FXML public Label currentClubId; // 当前俱乐部ID
  @FXML public TextField teamIdField; // 新增团队ID
  @FXML public TextField teamNameField; // 新增团队名称
  @FXML public TextField huishui; // 新增团队回水
  @FXML public TextField insuranceRate; // 新增保险比例
  @FXML public TextField gudongInput; // 新增股东名称
  @FXML public ListView<String> gudongListView; // 所有股东名称
  @FXML public Label indexLabel;// 第几局
  @FXML public Label dbConnectionState;// 数据库连接状态
  @FXML public TextField searchText;//
  @FXML public Button importHuishuiBtn;
  @FXML public Button importMembersBtn;
  @FXML public Button importPreDataBtn;
  @FXML public Hyperlink delCurrentMoneyBtn;
  @FXML public TextField combineIdDir;
  @FXML public RadioButton whiteVersionOld;
  @FXML public RadioButton whiteVersionNew;
  @FXML public RadioButton radio_autoTest_yes;
  @FXML public RadioButton radio_autoTest_no;
  @FXML public RadioButton radio_rate_0975;
  @FXML public RadioButton radio_rate_095;
  @FXML public HBox hbox_autoTestMode;


  // =================================================TabPane
  @FXML public TabPane tabs;

  // =================================================第一个tableView
  @FXML public TableView<TotalInfo> tableTotalInfo;

  @FXML public TableColumn<TotalInfo, String> tuan;// 团
  @FXML public TableColumn<TotalInfo, String> wanjiaId;// ID
  @FXML public TableColumn<TotalInfo, String> wanjia;// 玩家
  @FXML public TableColumn<TotalInfo, String> jifen;// 计分
  @FXML public TableColumn<TotalInfo, String> shishou;// 实收
  @FXML public TableColumn<TotalInfo, String> baoxian;// 保险
  @FXML public TableColumn<TotalInfo, String> chuHuishui;// 出回水
  @FXML public TableColumn<TotalInfo, String> baohui;// 保回

  @FXML public TableColumn<TotalInfo, String> shuihouxian;// 水后险
  @FXML public TableColumn<TotalInfo, String> shouHuishui;// 收回水
  @FXML public TableColumn<TotalInfo, String> heLirun;// 合利润

  // =================================================实时金额表tableView
  @FXML public TableView<CurrentMoneyInfo> tableCurrentMoneyInfo;

  @FXML public TableColumn<CurrentMoneyInfo, String> cmSuperIdSum;// 总和
  @FXML public TableColumn<CurrentMoneyInfo, String> mingzi;// 名字
  @FXML public TableColumn<CurrentMoneyInfo, String> shishiJine;// 实时金额
  @FXML public TableColumn<CurrentMoneyInfo, String> cmiEdu;// 实时金额

  // =================================================资金表tableView
  @FXML public TableView<ZijinInfo> tableZijin;

  @FXML public TableColumn<ZijinInfo, String> zijinType;
  @FXML public TableColumn<ZijinInfo, String> zijinAccount;
  // =================================================利润表tableView
  @FXML public TableView<ProfitInfo> tableProfit;

  @FXML public TableColumn<ProfitInfo, String> profitType;
  @FXML public TableColumn<ProfitInfo, String> profitAccount;
  // =================================================开销表tableView
  @FXML public TableView<KaixiaoInfo> tableKaixiao;

  @FXML public TableColumn<KaixiaoInfo, String> kaixiaoType;
  @FXML public TableColumn<KaixiaoInfo, String> kaixiaoMoney;
  // =================================================当局表tableView
  @FXML public TableView<DangjuInfo> tableDangju;

  @FXML public TableColumn<DangjuInfo, String> type;
  @FXML public TableColumn<DangjuInfo, String> money;
  // =================================================交收表tableView
  @FXML public TableView<JiaoshouInfo> tableJiaoshou;

  @FXML public TableColumn<JiaoshouInfo, String> jiaoshouType;
  @FXML public TableColumn<JiaoshouInfo, String> jiaoshouMoney;
  // =================================================交收表tableView
  @FXML public TableView<PingzhangInfo> tablePingzhang;

  @FXML public TableColumn<PingzhangInfo, String> pingzhangType;
  @FXML public TableColumn<PingzhangInfo, String> pingzhangMoney;

  // =================================================牌局表tableView
  @FXML public TableView<WanjiaInfo> tablePaiju;

  @FXML public TableColumn<WanjiaInfo, String> paiju;// 名字
  @FXML public TableColumn<WanjiaInfo, String> wanjiaName;// 实时金额
  @FXML public TableColumn<WanjiaInfo, String> zhangji;// 实时金额
  @FXML public TableColumn<WanjiaInfo, String> yicunJifen;// 实时金额
  @FXML public TableColumn<WanjiaInfo, String> heji;// 实时金额
  @FXML public TableColumn<WanjiaInfo, String> pay;// 支付
  @FXML public TableColumn<WanjiaInfo, String> copy;// 复制

  // =================================================牌局表tableView
  @FXML public TableView<TeamInfo> tableTeam;

  @FXML public TableColumn<TeamInfo, String> teamID;// 团ID
  @FXML public TableColumn<TeamInfo, String> teamZJ;// 团战绩
  @FXML public TableColumn<TeamInfo, String> teamHS;// 团回水
  @FXML public TableColumn<TeamInfo, String> teamBS;// 团保险
  @FXML public TableColumn<TeamInfo, String> teamSum;// 行总和
  @FXML public TableColumn<TeamInfo, String> teamJiesuan;// 结算按钮


  // ===========================================联盟对帐
  @FXML public Button lianmengBtn;
  @FXML public Label LMLabel;

  // ===========================================分页控件
  @FXML public TextField pageInput;

  @FXML public Button refreshBtn;
  @FXML public Button lockDangjuBtn;
  @FXML public Button openKaixiaoDialogBtn;
  @FXML public Button importZJBtn;
  @FXML public Button delKaixiaoBtn;
  @FXML public HBox importZJHBox;

  @FXML public Hyperlink addCurrentMoneyLink;
  @FXML public Hyperlink delCurrentMoneyLink;
  @FXML public Label lockedLabel;
  @FXML public Label softDateLabel; //软件时间
  // ===============================================================汇总Tab
  @FXML public TableView<ZonghuiInfo> tableZonghui;
  @FXML public TableColumn<ZonghuiInfo, String> zonghuiTabelId;
  @FXML public TableColumn<ZonghuiInfo, String> zonghuiFuwufei;
  @FXML public TableColumn<ZonghuiInfo, String> zonghuiBaoxian;
  @FXML public TableColumn<ZonghuiInfo, String> zonghuiHuishui;
  @FXML public TableColumn<ZonghuiInfo, String> zonghuiHuiBao;
  @FXML public ListView<String> juTypeListView;// 局类型ListView

  @FXML public TableView<DangtianHuizongInfo> tableDangtianHuizong;
  @FXML public TableColumn<DangtianHuizongInfo, String> huizongType;
  @FXML public TableColumn<DangtianHuizongInfo, String> huizongMoney;

  @FXML public TableView<ZonghuiKaixiaoInfo> tableZonghuiKaixiao;
  @FXML public TableColumn<ZonghuiKaixiaoInfo, String> zonghuiKaixiaoType;
  @FXML public TableColumn<ZonghuiKaixiaoInfo, String> zonghuiKaixiaoMoney;
  // ===============================================================会员查询Tab
  @FXML public TableView<MemberZJInfo> tableMemberZJ;
  @FXML public TableColumn<MemberZJInfo, String> memberJu;
  @FXML public TableColumn<MemberZJInfo, String> memberZJ;
  @FXML public Label memberDateStr;// 会员当天战绩的时间
  @FXML public Label memberPlayerId;// 会员当天战绩的会员ID
  @FXML public Label memberPlayerName;// 会员当天战绩的会员名称
  @FXML public Label memberSumOfZJ;// 会员当天战绩的战绩总和
  @FXML public Label memberTotalZJ;// 会员历史战绩的战绩总和
  @FXML public TextField memberSearchName;// 会员名称
  @FXML public ListView<String> memberListView;// 模糊搜索的人名列表

  // ===============================================================外债
  @FXML public TableView<WaizhaiInfo> tableWaizhai;
  @FXML public TableColumn<WaizhaiInfo, String> waizhaiType;
  @FXML public TableColumn<WaizhaiInfo, String> waizhaiMoney;
  @FXML public HBox waizhaiHBox;// 里面包含多个表
  // ============================================================================积分查询
  @FXML public TableView<JifenInfo> tableJifen;
  @FXML public TableColumn<JifenInfo, String> jfRank;
  @FXML public TableColumn<JifenInfo, String> jfPlayerName;
  @FXML public TableColumn<JifenInfo, String> jfValue;
  @FXML public DatePicker jfStartTime;
  @FXML public DatePicker jfEndTime;
  @FXML public ComboBox<String> jfTeamIDCombox;// 团队ID下拉框
  @FXML public TextField jifenInput;// 团队积分值
  @FXML public TextField jifenRankLimit;// 前50名

  // 保存到other数据表的key
  private final String KEY_GU_DONG = KeyEnum.GU_DONG.getKeyName();
  private final String KEY_CLUB_ID = KeyEnum.CLUB_ID.getKeyName();

  /*
   * 每点击结算按钮就往这个静态变更累加（只针对当局） 撤销时清空为0 锁定时清空为0 平帐时与上场的总团队服务费相加
   */
  public Double current_Jiesuaned_team_fwf_sum = 0d;


  /**
   * 节点加载完后需要进行的一些初始化操作 
   * Initializes the controller class. 
   * This method is automatically called after
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

    // 绑定玩家信息表
    bindCellValueByTable(new TotalInfo(), tableTotalInfo);

    // 绑定牌局表
    bindCellValueByTable(new WanjiaInfo(), tablePaiju);
    pay.setCellFactory(cellFactory);// 支付按钮：单独出来
    copy.setCellFactory(cellFactoryCopy);// 复制按钮：单独出来
    //setColumnCenter(pay, copy);

    // 绑定实时金额表
    tableCurrentMoneyInfo.setEditable(true);
    bindCellValueByTable(new CurrentMoneyInfo(), tableCurrentMoneyInfo);
    cmSuperIdSum.setStyle(Constants.CSS_CENTER_BOLD);
    shishiJine.setCellFactory(TextFieldTableCell.forTableColumn());
    setSSJEEditOnCommit();

    // 绑定资金表
    tableZijin.setEditable(true);
    bindCellValueByTable(new ZijinInfo(), tableZijin);
    zijinType.setCellFactory(zijinCellFactory);

    // 绑定利润表
    bindCellValueByTable(new ProfitInfo(), tableProfit);
    // 绑定实时开销表
    bindCellValueByTable(new KaixiaoInfo(), tableKaixiao);
    // 绑定实时当局表
    bindCellValueByTable(new DangjuInfo(), tableDangju);
    // 绑定交收表
    bindCellValueByTable(new JiaoshouInfo(), tableJiaoshou);
    // 绑定平帐表
    bindCellValueByTable(new PingzhangInfo(), tablePingzhang);

    // 绑定团队表
    bindCellValueByTable(new TeamInfo(), tableTeam);
    teamJiesuan.setCellFactory(cellFactoryJiesuan);
    teamJiesuan.setStyle(Constants.CSS_CENTER);

    // 绑定汇总信息表（当天每一局的团队汇总查询）
    bindCellValueByTable(new ZonghuiInfo(), tableZonghui);
    // 绑定汇总查询中的当天汇总表
    bindCellValueByTable(new DangtianHuizongInfo(), tableDangtianHuizong);
    // 绑定汇总查询中的开销表表
    bindCellValueByTable(new ZonghuiKaixiaoInfo(), tableZonghuiKaixiao);
    // 绑定会员查询中的会员当天战绩表
    bindCellValueByTable(new MemberZJInfo(), tableMemberZJ);

    // 绑定外债信息表
    bindCellValueByTable(new WaizhaiInfo(), tableWaizhai);

    // 绑定积查询表
    bindCellValueByTable(new JifenInfo(), tableJifen);

    // 初始化实时金额表
    moneyService.iniitMoneyInfo(tableCurrentMoneyInfo);

    // 总汇表中的初始化
    juTypeListView.getItems().add("合局");

    LMLabel.setTextFill(Color.web("#CD3700"));
    indexLabel.setTextFill(Color.web("#0076a3"));// 设置Label 的文本颜色。
    indexLabel.setFont(new Font("Arial", 30));

    // 会员服务类
    memberService.initMemberQuery(memberListView, tableMemberZJ, memberDateStr, memberPlayerId,
        memberPlayerName, memberSumOfZJ, memberTotalZJ);

    tabsAction();

    // 合并ID
    combineIDController.initCombineIdController(tableCurrentMoneyInfo);

    // 积分查询
    jifenService.initjifenService(jfTeamIDCombox);

    // 是否启动测试模式
    initAutoTestMode();

    // 选择导入白名单的版本
    initWhiteVersion();

    // 初始化全局比例
    initHSRate();

    // 加载各个tab页面，备注：此方法应放在tabsAction之前
    loadSubTabs();
    
    if( SpringFxmlLoader.getContext() == null ) {
      logger.error("SpringFxmlLoader.getContext() is null");
    }else {
      logger.info("SpringFxmlLoader.getContext() is not null >>>>>");
    String[] beanDefinitionNames = SpringFxmlLoader.getContext().getBeanDefinitionNames();
    for( String bean : beanDefinitionNames) {
      logger.info(bean);
    }
    logger.info("以上为spring容器中加载的bean\n\n");
    }

  }

  /**
   * 实时金额修改
   */
  private void setSSJEEditOnCommit() {
    shishiJine.setOnEditCommit(new EventHandler<CellEditEvent<CurrentMoneyInfo, String>>() {
      @Override
      public void handle(CellEditEvent<CurrentMoneyInfo, String> t) {
        // 修改原值
        CurrentMoneyInfo cmInfo =
            (CurrentMoneyInfo) t.getTableView().getItems().get(t.getTablePosition().getRow());

        if (cmInfo != null && !StringUtil.isBlank(cmInfo.getMingzi())) {
          // 更新到已存积分
          boolean isChangedOK =
              moneyService.changeYicunJifen(tablePaiju, cmInfo.getMingzi(), t.getNewValue());
          if (isChangedOK) {
            cmInfo.setShishiJine(t.getNewValue());
          } else {
            cmInfo.setShishiJine(t.getOldValue());
            tableCurrentMoneyInfo.refresh();
          }
          moneyService.flush_SSJE_table();// 最后刷新实时金额表
        } else if (cmInfo != null) {
          cmInfo.setShishiJine(null);
          ShowUtil.show("空行不能输入", 1);
          tableCurrentMoneyInfo.refresh();
        }
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
   * 加载各个tab页面
   */
  private void loadSubTabs() {
    ApplicationContext context = SpringFxmlLoader.getContext();
    logger.info("before: context is " + (context != null ? " not null" : "null" ));
    addSubTab("代理查询", "team_proxy_tab_frame.fxml");
    addSubTab("实时上码系统", "shangma_tab_frame.fxml");
    addSubTab("联盟对账", "LM_Tab_Fram.fxml");
    addSubTab("联盟配账", "Quota_Tab_Fram.fxml");
    addSubTab("股东贡献值", "gudong_contribution.fxml");
    addSubTab("托管工具", "TG_toolaa.fxml");
    addSubTab("自动上码配置", "SM_Autos.fxml");
    addSubTab("银行流水", "bank_flow_frame.fxml");
  }

  /**
   * 加载子单个Tab
   * 
   * @time 2018年7月5日
   * @param tabName
   * @param frameName
   */
  private void addSubTab(String tabName, String frameName) {
    try {
      String path = "/dialog/" + frameName;
      Parent root = (Parent) Main.loader.load(path);
      Tab subTab = new Tab();
      subTab.setText(tabName);
      subTab.setClosable(false);
      subTab.setContent(root);
      tabs.getTabs().add(subTab);
    } catch (Exception e) {
      ErrorUtil.err(tabName + "tab加载失败", e);
    }
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
   * @return
   */
  private int getVersionType() {
    return whiteVersionOld.isSelected() ? 0 : 1;
  }

  /**
   * 启动测试模式
   * 
   * @time 2017年11月11日
   */
  private void initAutoTestMode() {
    hbox_autoTestMode.setVisible(false);
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
          hbox_autoTestMode.setVisible(true);
        } else {
          hbox_autoTestMode.setVisible(false);
        }
      }
    });
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
        logger.info(" newTab:" + tab.getText());
        if ("场次信息".equals(tab.getText())) {
          moneyService.flush_SSJE_table();
          moneyService.update_Table_CMI_Map();// 更新{玩家ID=CurrentMoneyInfo},感觉没什么用
        }
        if ("代理查询".equals(tab.getText())) {
          teamProxyController.loadWhenClickTab();
        }
        if ("实时上码系统".equals(tab.getText())) {
          smController.loadWhenClickTab();
        }
        if ("积分查询".equals(tab.getText())) {

        }
        if ("联盟对帐".equals(tab.getText())) {
          lmController.refreshClubList();
        }
        if ("联盟配账".equals(tab.getText())) {
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
        }
        if ("托管工具".equals(tab.getText())) {
          tgController.loadDataLastest();
        }
        if ("自动上码配置".equals(tab.getText().trim())) {
          shangmaService.refreshTeamIdAndPlayerId();
        }
        if ("银行流水".equals(tab.getText().trim())) {
          bankFlowController.refresh();
        }

      }
    });
  }
  
  /**
   * 新增银行
   */
  public void addBankAction(ActionEvent event) {
    moneyService.addBank();
  }
  /**
   * 减少银行
   */
  public void delBankAction(ActionEvent event) {
    moneyService.delBank();
  }


  /**
   * kendy:绑定数据域
   * 
   * @param colums TableColumn 可变参数
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void bindCellValue(TableColumn<? extends Entity, String>... colums) {
    try {
      for (TableColumn column : colums) {
        String fxId = column.getId();
        column.setCellValueFactory(new PropertyValueFactory<Entity, String>(fxId));
        column.setStyle("-fx-alignment: CENTER;");
        column.setSortable(false);// 禁止排序
      }
    } catch (Exception e) {
      throw new RuntimeException("小林：绑定列值失败");
    }
  }

  // ==================================== 打开文件选择 Excel  ====================================
  /**
   * 导入excel文件选择器
   * 
   * @param textField
   * @param title
   */
  public File openBasicExcelDialog(TextField textField, String title) {
    String rootPathName = !StringUtil.isBlank(dataConstants.Root_Dir) ? dataConstants.Root_Dir
        : System.getProperty("user.home");
    FileChooser fileChooser = new FileChooser();// 文件选择器
    fileChooser.setTitle(Constants.TITLE + ": " + title);// 标题
    fileChooser.setInitialDirectory(new File(rootPathName));// 初始化根目标
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel", "*.xls?"));
    File file=null;
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
   * 打开战绩excel
   */
  public void openZJExcelAction(ActionEvent event) {
    File file = openBasicExcelDialog(excelDir, "请选择战绩Excel");
    if(file != null) {
     String tableId = FileUtil.getTableId(file.getAbsolutePath());
     indexLabel.setText(tableId);
    }
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
        dbUtil.insertMembers(allPlayers);

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
        // 积分查询初始化团队ID
        jifenService.init_Jifen_TeamIdCombox();
        // 上码系统中的团队ID按钮
        shangmaService.initShangmaButton();
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
   * 导入战绩文件
   */
  public void importZJExcelAction(ActionEvent even) {
    String excelFilePath = getExcelPath();
    String userClubId = getClubId();
    String tableId = FileUtil.getTableId(excelFilePath);
    if (StringUtil.isNotBlank(excelFilePath)) {
      File file = new File(excelFilePath);
      if(!file.exists()) {
        ShowUtil.show("检测到该Excel文件不存在，可能已经锁定后被转移了（桌号"+tableId+")");
        return;
      }
      if (StringUtil.isAnyBlank(dataConstants.Date_Str, getSoftDate())) {
        ShowUtil.show("检测到您当前未设置软件时间！请开始新的一天");
        return;
      }
      if (!NumUtil.isNumeric(tableId.replace("第", "").replaceAll("局", ""))) {
        ErrorUtil.err(excelFilePath + "不是一个合法的Excel文件名称，请检查！");
        return;
      }
      if (hbox_autoTestMode.isVisible()) {
        final_selected_LM_type = "联盟1";
        selected_LM_type = "联盟1";
      } else {
        selectLM();
        if (StringUtil.isBlank(selected_LM_type)) {
          String msg = "导入战绩时没有选择对应的联盟，场次：" + tableId + " Excel不准导入！";
          ShowUtil.show(msg);
          return;
        }
      }
      if (StringUtil.isBlank(userClubId)
          || dataConstants.Index_Table_Id_Map.containsValue(tableId)) {
        ErrorUtil.err("该战绩表(" + tableId + "场次)已经导过");
        return;
      }

      try {
        // 将人员名单文件缓存起来
        List<GameRecord> gameRecords = excelReaderUtil.readZJRecord(excelFilePath, userClubId,
            selected_LM_type, getVersionType());
        indexLabel.setText(tableId);
        importExcelData(tableId, gameRecords);

        importZJBtn.setDisable(true); // 导入按钮设置为不可用
        ShowUtil.show("导入战绩文件成功", 2);

      } catch (Exception e) {
        ErrorUtil.err("战绩导入失败", e);
      }
    }
  }

  private void importExcelData(String tableId, List<GameRecord> gameRecords) {
    // 1 填充总信息表
    moneyService.fillTablerAfterImportZJ(tableTotalInfo, tablePaiju, tableDangju, tableJiaoshou,
        tableTeam, gameRecords, tableId);
    // 2填充当局表和交收表和团队表的总和
    moneyService.setTotalNumOnTable(tableDangju, dataConstants.SumMap.get("当局"));
    moneyService.setTotalNumOnTable(tableJiaoshou, dataConstants.SumMap.get("交收"));
    tableTeam.getColumns().get(4)
        .setText(moneyService.digit0(dataConstants.SumMap.get("团队回水及保险总和")));
  }

  /**
   * 导入战绩后选择联盟
   */
  private String selected_LM_type = "";// 选择后会被清空
  private String final_selected_LM_type = "";// 选择后不会被清空，用于检测额度是否超出

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void selectLM() {
    Dialog dialog = FXUtil.getBasicDialog("请选择联盟:"); 
    // 添加联盟按钮
    GridPane grid = new GridPane();
    grid.setPrefHeight(150);
    grid.setPrefWidth(200);
    grid.setHgap(10);
    grid.setVgap(20);
    grid.setPadding(new Insets(20, 15, 10, 10));
    for (int i = 0; i < 3; i++) {
      Button btn = new Button("联盟" + (i + 1));
      btn.setPrefWidth(200);
      btn.setOnAction(event -> {
        selected_LM_type = btn.getText();
        dialog.setTitle(selected_LM_type);
        logger.info(selected_LM_type);
      });
      grid.add(btn, 0, i);
    }
    // 添加取消按钮
    HBox hbox = new HBox();
    hbox.setPadding(new Insets(0, 0, 0, 70));
    hbox.setSpacing(10);
    hbox.setStyle("-fx-background-color:#FFFFFF;");

    Hyperlink cancleLink = new Hyperlink("取消");
    cancleLink.setPrefWidth(100);
    cancleLink.setOnAction(event -> {
      selected_LM_type = "";
      dialog.close();
    });
    hbox.getChildren().addAll(cancleLink);
    grid.add(hbox, 0, 3);

    // 添加确定按钮
    ButtonType loginButtonType = new ButtonType("确定", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

    dialog.setOnCloseRequest(event -> {
      final_selected_LM_type = StringUtil.nvl(selected_LM_type, "联盟1");
      if ("".equals(selected_LM_type)) {
        return;
      }
      if (AlertUtil.confirm("==== " + selected_LM_type + " ===, 确定??")) {
        logger.info("最终选择:" + selected_LM_type);
      } else {
        selected_LM_type = "";
        logger.info("selected_LM_type:" + selected_LM_type);
      }
    });

    dialog.getDialogPane().setContent(grid);
    dialog.showAndWait();
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
      for(CombineID combineId : importedCombinIds) {
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
   * 检查excel是否重复
   */
  public void checkExcelAction(ActionEvent event) {
    String filePath = excelDir.getText();
    if (!StringUtil.isBlank(filePath)) {
      String tableId = filePath.substring(filePath.lastIndexOf("-") + 1, filePath.lastIndexOf("."));
      if (dataConstants.Index_Table_Id_Map.containsValue(tableId)) {
        ShowUtil.show("场次：" + tableId + "已经导入过，请勿重复操作！");
      } else {
        ShowUtil.show("校验通过，即将导入场次" + tableId + "的战。", 2);
      }
    } else {
      ShowUtil.show("先选择需要导入的Excel", 2);
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
    String selectedGudongName = (String) gudongListView.getFocusModel().getFocusedItem();
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
   * @return
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
   * 打开增加实时开销对话框
   */
  public void openKaixiaoDialogAction(ActionEvent event) {
    openBasedDialog("add_kaixiao_fram.fxml", "新增开销", Constants.ADD_KAIXIAO_FRAME);
  }
  /**
   * 打开删除人员对话框
   */
  public void openDelMemberDialogAction(ActionEvent event) {
    openBasedDialog("del_member_framesss.fxml", "人员删除或修改", Constants.DEL_MEMBER_FRAME);
  }
  /**
   * 打开新增实时金额对话框
   */
  public void openAddCurrentMoneyAction(ActionEvent event) {
    openBasedDialog("add_current_money_frame4.fxml", "新增实时金额", Constants.ADD_CURRENT_MONEY_FRAME);
  }
  /**
   * 打开新增合并ID对话框
   */
  public void opentCombineIDDialogAction(ActionEvent event) {
    openBasedDialog("combine_player_id_framess.fxml", "合并ID", Constants.COMBINE_ID_FRAME);
  }
  /**
   * 牌局表列中添加支付按钮
   */
  Callback<TableColumn<WanjiaInfo, String>, TableCell<WanjiaInfo, String>> cellFactory = //
  new Callback<TableColumn<WanjiaInfo, String>, TableCell<WanjiaInfo, String>>() {
    @SuppressWarnings("rawtypes")
    @Override
    public TableCell call(final TableColumn<WanjiaInfo, String> param) {
      final TableCell<WanjiaInfo, String> cell = new TableCell<WanjiaInfo, String>() {

        final Button btn = new Button("支付");

        @Override
        public void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setGraphic(null);
            setText(null);
          } else {
            btn.setOnAction(event -> {
              WanjiaInfo wanjiaInfo = getTableView().getItems().get(getIndex());
              if ("1".equals(wanjiaInfo.getHasPayed())) {
                ShowUtil.show("抱歉，已支付过！！");
                return;
              }
              if (getTableRow() != null) {
                try {
                  // //支付时修改玩家实时金额 或 添加 实时金额
                  moneyService.updateOrAdd_SSJE_after_Pay(wanjiaInfo);
                  btn.setText("已支付");
                  wanjiaInfo.setHasPayed("1");
                  ShowUtil.show(
                      "已将" + wanjiaInfo.getWanjiaName() + "的实时金额修改为   " + wanjiaInfo.getHeji(),
                      2);
                  // moneyInfo.setShishiJine(wj.getHeji());//设置新的实时金额的值
                  moneyService.flush_SSJE_table();// 刷新实时金额表

                  // 点击支付时更改SM_Detail_Map中的支付状态
                  shangmaService.update_SM_Detail_Map_byPlayerIdAndPaiju(
                      wanjiaInfo.getWanjiaId(), indexLabel.getText());
                } catch (Exception e) {
                  ErrorUtil.err("支付失败", e);
                }
              }
            });
            WanjiaInfo wj = getTableView().getItems().get(getIndex());
            // 解决不时本应支付确显示成已支付的bug
            if (!"1".equals(wj.getHasPayed())) {
              btn.setText("支付");
            } else {
              btn.setText("已支付");
            }
            setGraphic(btn);// 小林：这一行解决了支付按钮消失的问题
            // 在此处增加是否要显示该按钮(如果玩家从属于某个非空或非公司的团队，则无需显示按钮)
            String tempTeamId = wj.getHasPayed();// 这个tempTeamId是hasPayed的内容,这里没有公司的人
            if (!StringUtil.isBlank(tempTeamId) && !"0".equals(tempTeamId)) {
              // 获取团队信息
              Huishui hs = dataConstants.huishuiMap.get(tempTeamId);
              // 情况一：有从属团队的玩家，再分两种情况
              if (hs != null) {
                // A:若团队战绩要管理，需要显示支付按钮
                if ("是".equals(hs.getZjManaged())) {
                  // log.debug("====teamId为不为空，要显示，要战绩管理：是");
                  setGraphic(btn);
                  // B:若团队战绩不要管理，无须显示支付按钮
                } else {
                  // log.debug("hsPayed:====================hs为空："+hs.getZjManaged());
                  setGraphic(null);
                }
              }
            } else {
              // log.debug("====teamId为空或为0，要显示+"+dataConstants.membersMap.get(wj.getWanjiaId()).getTeamName());
              // 情况二：对于没有从从属的团队的玩家或者团队是公司的玩家，一定需要需要显示支付按钮
              setGraphic(btn);
            }
            // setGraphic(btn);
            setText(null);
          }
        }
      };
      return cell;
    }
  };
  
  /**
   * 在列中添加按钮
   */
  Callback<TableColumn<WanjiaInfo, String>, TableCell<WanjiaInfo, String>> cellFactoryCopy = //
      new Callback<TableColumn<WanjiaInfo, String>, TableCell<WanjiaInfo, String>>() {
        @SuppressWarnings("rawtypes")
        @Override
        public TableCell call(final TableColumn<WanjiaInfo, String> param) {
          final TableCell<WanjiaInfo, String> cell = new TableCell<WanjiaInfo, String>() {

            final Button btn = new Button("copy");

            @Override
            public void updateItem(String item, boolean empty) {
              super.updateItem(item, empty);
              if (empty) {
                setGraphic(null);
                setText(null);
              } else {
                btn.setOnAction(event -> {
                  WanjiaInfo wanjiaInfo = getTableView().getItems().get(getIndex());
                  try {
                    clip2QQ(wanjiaInfo);
                    logger.debug("已经复制到剪切板");
                  } catch (Exception e) {
                    ShowUtil.show("复制失败", 1);
                    e.printStackTrace();
                  }
                });
                setGraphic(btn);
                setText(null);
              }
            }
          };
          return cell;
        }
      };


  /**
   * 结算按钮
   */
  Callback<TableColumn<TeamInfo, String>, TableCell<TeamInfo, String>> cellFactoryJiesuan = //
      new Callback<TableColumn<TeamInfo, String>, TableCell<TeamInfo, String>>() {
        @SuppressWarnings("rawtypes")
        @Override
        public TableCell call(final TableColumn<TeamInfo, String> param) {
          final TableCell<TeamInfo, String> cell = new TableCell<TeamInfo, String>() {

            final Button btn = new Button("结算");

            @Override
            public void updateItem(String item, boolean empty) {
              super.updateItem(item, empty);
              if (empty) {
                setGraphic(null);
                setText(null);
              } else {
                btn.setOnAction(event -> {
                  //
                  if (!importZJBtn.isDisabled()) {
                    ShowUtil.show("只能在导入战绩后才能结算！");
                    return;
                  }
                  if (!AlertUtil.confirm("只能在当天最后一场才能结算！你确定了吗?")) {
                    return;
                  }
                  TeamInfo teamInfo = getTableView().getItems().get(getIndex());
                  if (getTableRow() != null && teamInfo != null
                      && "0".equals(teamInfo.getHasJiesuaned())) {
                    TeamInfo tempTeamInfo = copyTeamInfo(teamInfo);
                    btn.setText("已结算");
                    String rowTeamSum = teamInfo.getTeamSum();
                    teamInfo.setHasJiesuaned("1");
                    teamInfo.setTeamZJ(ZERO);
                    teamInfo.setTeamBS(ZERO);
                    teamInfo.setTeamHS(ZERO);
                    teamInfo.setTeamSum(ZERO);
                    // 结算后自动删除总和中部分数字
                    String key = "团队回水及保险总和";
                    if (dataConstants.SumMap.get(key) != null) {
                      double totalSum = dataConstants.SumMap.get(key);
                      dataConstants.SumMap.put(key, Double
                          .valueOf(moneyService.digit0(totalSum - NumUtil.getNum(rowTeamSum))));
                    } ;
                    // 缓存中清空之前所加的团队回水，以便下次团队累计重新从0开始
                    dataConstants.Team_Huishui_Map.remove(teamInfo.getTeamID());
                    dataConstants.Team_Info_Pre_Map.remove(teamInfo.getTeamID());

                    // 2018-01-04 在实时金额栏中新增该团队减去团队服务费的记录
                    add2SSJE(tempTeamInfo);
                  }
                });
                // 解决不时本应支付确显示成已支付的bug
                TeamInfo teamInfo = getTableView().getItems().get(getIndex());
                if (!"1".equals(teamInfo.getHasJiesuaned())) {
                  btn.setText("结算");
                }
                setGraphic(btn);
                setText(null);
              }
            }
          };
          return cell;
        }
      };

  public TeamInfo copyTeamInfo(TeamInfo info) {
    TeamInfo temp = new TeamInfo(info.getTeamID(), info.getTeamZJ(), info.getTeamHS(),
        info.getTeamBS(), info.getTeamSum());
    return temp;
  }

  /**
   * 点击结算按钮后往实时金额新增一条团队记录（减去该团队服务费）
   * 
   * @time 2018年1月4日
   * @param teamInfo
   */
  public void add2SSJE(TeamInfo teamInfo) {
    Platform.runLater(new Runnable() { // 更新JavaFX的主线程的代码放在此处
      @Override
      public void run() {
        String teamID = teamInfo.getTeamID();
        String teamName = "团队#" + teamID;// 自定义添加到金额表中的名称
        String teamSum = teamInfo.getTeamSum();
        Double _teamSum = NumUtil.getNum(teamSum);
        if (_teamSum == 0) {
          // 这里是否直接返回？？......
          // return;
        }
        // 获取代理查询中该团队的服务费
        String fwfString = teamProxyService.get_TeamFWF_byTeamId(teamID);
        Double teamFWF = NumUtil.getNum(fwfString);// 团队服务费 : 待累加到总团队服务费中
        Double _tempSSJE = _teamSum - teamFWF;// 此处已减去该 团队服务费
        String tempSSJE = NumUtil.digit0(_tempSSJE);// 此处已减去该 团队服务费
        current_Jiesuaned_team_fwf_sum += teamFWF;
        // 获取新记录
        CurrentMoneyInfo cmiInfo = moneyService.getInfoByName(teamName);
        if (cmiInfo == null) {// cmiInfo为null表示该团队不存在于实时金额表中
          cmiInfo = new CurrentMoneyInfo(teamName, tempSSJE, "", "");// 玩家ID和额度为空
          moneyService.addInfo(cmiInfo);
          logger.info(String.format("点击结算按钮:新增一条团队记录进金额表,团队ID=%s,团队服务费=%s,金额=%s", teamID, fwfString,
              tempSSJE));
        } else {
          // 如果在实时金额中已经存在该团队记录，则更新该条实时金额
          String oldTeamSSJE = cmiInfo.getShishiJine();
          Double _newTeamSSJE = NumUtil.getNum(oldTeamSSJE) + _tempSSJE;// 此处已减去该 团队服务费
          String newTeamSSJE = NumUtil.digit0(_newTeamSSJE);
          cmiInfo.setShishiJine(newTeamSSJE);
          logger.info(String.format("点击结算按钮:修改金额表原团队记录,团队ID=%s,团队服务费=%s,金额=%s", teamID, fwfString,
              newTeamSSJE));
        }
        // 并刷新表
        moneyService.refreshRecord();
        // 自动平帐按钮
        refreshBtn.fire();// 这个是为了让当局的团队总服务费能累加到利润表中的总团队服务费中
      }
    });
  }


  /**
   * 利润表修改总团队服务费(累积该团队的服务费)
   * 
   * @time 2018年1月5日
   * @param teamFWF
   */
  public void add2AllTeamFWF_from_tableProfit(TableView<ProfitInfo> table, Double teamFWF) {
    try {
      ProfitInfo profitInfo =
          TableUtil.getItem(table).filtered(info -> "总团队服务费".equals(info.getProfitType())).get(0);
      String allTeamFWF = NumUtil.digit0(NumUtil.getNum(profitInfo.getProfitAccount()) + teamFWF);
      profitInfo.setProfitAccount(allTeamFWF);
      table.refresh();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }




  /**
   * 资金表添加双击名称事件
   */
  Callback<TableColumn<ZijinInfo, String>, TableCell<ZijinInfo, String>> zijinCellFactory =
      new Callback<TableColumn<ZijinInfo, String>, TableCell<ZijinInfo, String>>() {
        @Override
        public TableCell<ZijinInfo, String> call(TableColumn<ZijinInfo, String> param) {

          TextFieldTableCell<ZijinInfo, String> cell = new TextFieldTableCell<>();
          cell.setEditable(false);// 不让其可编辑
          cell.setOnMouseClicked((MouseEvent t) -> {
            // 鼠标双击事件
            if (t.getClickCount() == 2) {
              if (lockedLabel.isVisible()) {
                return;
              }
              // 双击执行的代码
              ZijinInfo info = tableZijin.getItems().get(cell.getIndex());
              moneyService.openAddZijinDiag(tableZijin, info);
            }
          });
          return cell;
        }
      };


  /**
   * 复制玩家信息到剪切板，并转成图片发到QQ
   * 
   * @param wj 玩家信息
   * @throws Exception
   */
  private void clip2QQ(WanjiaInfo wj) throws Exception {
    String code = sysCode.getText();
    if (StringUtil.isBlank(code)) {
      code = "GBK";
    }
    String html = Text2ImageUtil.getHtml(wj);
    BufferedImage img = Text2ImageUtil.toImage(html, code);
    ClipBoardUtil.setClipboardImage((Image) img);
  }

  /**
   * 更新开销表
   * 
   * @param data
   */
  public void updateKaixiaoTable(KaixiaoInfo info) {
    // 获取ObserableList
    ObservableList<KaixiaoInfo> list = tableKaixiao.getItems();
    list.add(info);
    tableKaixiao.setItems(list);
  }

  /**
   * 更新实时金额表
   * 
   * @param data
   */
  public void updateCurrentMoneyTable(Player player, String SSJE) {
    // 获取ObserableList
    ObservableList<CurrentMoneyInfo> list = tableCurrentMoneyInfo.getItems();
    list.add(
        new CurrentMoneyInfo(player.getPlayerName(), SSJE, player.getgameId(), player.getEdu()));
    tableCurrentMoneyInfo.setItems(list);
  }

  /**
   * 刷新同步,即平帐按钮
   * 
   * @param data
   */
  public void refreshAction(ActionEvent event) {
    moneyService.refreshSumPane(tableTeam, tableZijin, tableKaixiao, tableProfit,
        tableCurrentMoneyInfo, tablePingzhang, LMLabel);
  }

  /**
   * 搜索按钮
   */
  public void searchRowAction(ActionEvent event) {
    String keyWord = searchText.getText();
    moneyService.searchRowAction(tableCurrentMoneyInfo, keyWord);
  }

  public void searchRowByEnterEvent(KeyEvent event) {
    String keyWord = searchText.getText();
    if (KeyCode.ENTER == event.getCode() && !StringUtil.isBlank(keyWord)) {
      moneyService.searchRowAction(tableCurrentMoneyInfo, keyWord);
    }
  }

  /**
   * 锁定当局按钮
   */
  public void lockDangjuAction(ActionEvent event) {
    String JS = "交收";
    String PZ = "平帐";
    if (dataConstants.SumMap.get(JS) != null && dataConstants.SumMap.get(PZ) != null) {
      double sumOfJS = dataConstants.SumMap.get(JS);
      double sumOfPZ = dataConstants.SumMap.get(PZ);
      double diff = Math.abs(sumOfJS + sumOfPZ);
      if (diff < 10) {
        // 若交收与平帐大体相等，则更新联盟对帐
        /*
         * 联盟对帐 = 上一局的联盟对帐 + 本局的联盟对帐 当清空上一局的联盟对帐时，联盟对帐设置为0.00
         */
        if (!"0.00".equals(LMLabel.getText())) {
          LMLabel.setText(getNewLMVal(sumOfJS * (-1) + ""));
        } else {
          LMLabel.setText(NumUtil.digit0(sumOfJS * (-1)) + "");
        }
        // 锁定并缓存十个表数据
        Map<String, String> mapOf10Tables;
        try {
          mapOf10Tables = moneyService.lock10Tables(tableTotalInfo, tablePaiju, tableTeam,
              tableCurrentMoneyInfo, tableZijin, tableKaixiao, tableProfit, tableDangju,
              tableJiaoshou, tablePingzhang, LMLabel);
        } catch (Exception e) {
          ErrorUtil.err("锁定失败\r\n相关表数据不能为空");
          return;
        }

        // 汇总信息保存到数据库
        int paijuIndex = dataConstants.Paiju_Index.getAndIncrement();
        dataConstants.Index_Table_Id_Map.put(paijuIndex + "", indexLabel.getText());// 记录局与场次映射

        // 缓存到总团队回水中(不会从中减少)
        setDangjuTeamInfo();

        Map<String, String> totalMap = new HashMap<>();
        totalMap.putAll(mapOf10Tables);
        dataConstants.All_Locked_Data_Map.put(paijuIndex + "", totalMap);// 总缓存数据

        // 清空表数据
        clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou,
            tablePingzhang);
        // 清空相关缓存
        Double teamData = dataConstants.SumMap.get("团队回水及保险总和");// 这个值必须保留
        Double shangchangKaixiao = dataConstants.SumMap.get("上场开销");// 这个值必须保留
        dataConstants.SumMap = new HashMap<String, Double>();
        dataConstants.SumMap.put("团队回水及保险总和", teamData);
        dataConstants.SumMap.put("上场开销", shangchangKaixiao);
        // 控制分页
        pagePaneOpration();

        indexLabel.setText(INDEX_ZERO);
        importZJBtn.setDisable(false);

        // 锁定当局备份上码表的个人详情
        dataConstants.lock_SM_Detail_Map();

        dataConstants.Dangju_Team_Huishui_List = new LinkedList<>();

        // 保存所有缓存数据进数据库(与上面的数据顺序不要变换），不然会中途加载时想查看以前的数据会报请撤销或锁定数据
        Platform.runLater(() -> {
          Map<String, String> lastLockedDataMap = dataConstants.getLockedDataMap();// 这里没有场次信息的数据了
          String json_all_locked_data = JSON.toJSONString(lastLockedDataMap);
          int ju_size1 = dataConstants.Index_Table_Id_Map.size();
          
          int ju_size2 = dataConstants.Paiju_Index.get() - 1;
          Map<String, String> lastDataDetailMap = dataConstants.All_Locked_Data_Map.get(ju_size2 + "");
          String lastDataDetailJson = JSON.toJSONString(lastDataDetailMap);
          
          dbUtil.saveLastLockedData(ju_size1, json_all_locked_data, ju_size2, lastDataDetailJson);// IO耗时长
        });

        // 保存当前Excel记录到数据库
        try {
          dbUtil.addGameRecordList(lmController.currentRecordList);
        } catch (Exception e) {
          ErrorUtil.err(e.getMessage(), e);
        }
        lmController.refreshClubList();
        lmController.checkOverEdu(final_selected_LM_type);// 检查俱乐部额度


        // 当局已结算的团队服务费之和 要置为0
        current_Jiesuaned_team_fwf_sum = 0d;

        // 转移Excel
        moveExcel();

        ShowUtil.show("锁定成功！", 2);
      } else {
        ShowUtil.show("平帐与交收的差值大于10，不能锁定！！！");
      }
    } else {
      ShowUtil.show("平帐失败，不能锁定！！！", 2);
    }
  }
  
  /**
   * 获取软件时间
   */
  public String getSoftDate() {
    return softDateLabel.getText();
  }
  
  /**
   * 获取Excel路径
   */
  public String getExcelPath() {
    return excelDir.getText();
  }
  
  /**
   * 获取当前俱乐部
   */
  public String getClubId() {
    return currentClubId.getText();
  }


  /**
   * 转移Excel
   * 
   * @time 2018年4月21日
   */
  private void moveExcel() {
    //判断日期
    if (StringUtil.isBlank(getSoftDate())) {
      ShowUtil.show("软件时间未确定，导致当前Excel未被转移!");
      return;
    }
    //白名单路径或白名单文件为空时不进行转移
    String resourceFilePath = getExcelPath();
    File srcFile = new File(resourceFilePath);
    if(StringUtil.isBlank(getExcelPath()) || !srcFile.exists()) {
      logger.info("白名单路径 为空或者不存在，锁定时不进行Excel转移！");
      return;
    }
    try {
      String fileName = FileUtil.getFileName(resourceFilePath);
      String targetFilePath =
          PathUtil.getUserDeskPath() + getSoftDate() + "已锁定" + File.separator +  fileName;
      FileUtils.moveFile(srcFile, new File(targetFilePath));
    } catch (Exception e) {
      ErrorUtil.err("转移Excel失败", e);
    }
  }
  

  // 缓存到总团队回水中(不会从中减少)
  public void setDangjuTeamInfo() {
    dataConstants.Dangju_Team_Huishui_List.forEach(info -> {
      String teamId = info.getTeamId();
      List<GameRecord> teamHuishuiList = dataConstants.Total_Team_Huishui_Map.get(teamId);
      if (teamHuishuiList == null) {
        teamHuishuiList = new ArrayList<>();
      }
      teamHuishuiList.add(info);
      dataConstants.Total_Team_Huishui_Map.put(teamId, teamHuishuiList);
    });
  }

  // 获取上一个联盟对帐与本局联盟对帐的总值
  public String getNewLMVal(String dangjuLMVal) {
    int preJuIndex = dataConstants.Index_Table_Id_Map.size();
    if (dataConstants.All_Locked_Data_Map.get(preJuIndex + "") != null) {
      String preJuLMVal = dataConstants.All_Locked_Data_Map.get(preJuIndex + "").get("联盟对帐");
      return NumUtil.digit0(NumUtil.getNum(dangjuLMVal) + NumUtil.getNum(preJuLMVal));
    }
    return moneyService.digit0(dangjuLMVal);
  }

  // 获取最新的利润总和
  public String getChangciTotalProfit() {
    String totalProfit = "0";
    int paijuIndex = dataConstants.Index_Table_Id_Map.size();
    if (paijuIndex == 0)
      return totalProfit;
    Map<String, String> lastLockedMap = dataConstants.All_Locked_Data_Map.get(paijuIndex + "");
    if (lastLockedMap != null) {
      totalProfit = lastLockedMap.get("利润总和");
    }
    return totalProfit;
  }


  public void cleardataConstantsCache() {
    // dataConstants.SumMap.clear();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void clearData(TableView tableTotalInfo, TableView tablePaiju, TableView tableTeam,
      TableView tableDangju, TableView tableJiaoshou, TableView tablePingzhang) {
    // 清空相关界面表数据
    tableTotalInfo.setItems(null);
    tablePaiju.setItems(null);
    tableDangju.setItems(null);
    tableJiaoshou.setItems(null);
    tablePingzhang.setItems(null);
    // 清空相关界面表总数据
    moneyService.setTotalNumOnTable(tableDangju, 0d);
    moneyService.setTotalNumOnTable(tableJiaoshou, 0d);
    moneyService.setTotalNumOnTable(tablePingzhang, 0d);


    // 清空缓存中的数据
    cleardataConstantsCache();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void clear10Tables(TableView tableTotalInfo, TableView tablePaiju, TableView tableTeam,
      TableView tableDangju, TableView tableJiaoshou, TableView tablePingzhang) {
    // 清空相关界面表数据
    tableTotalInfo.setItems(null);
    tablePaiju.setItems(null);
    tableDangju.setItems(null);
    tableJiaoshou.setItems(null);
    tablePingzhang.setItems(null);
    // 清空相关界面表总数据
    moneyService.setTotalNumOnTable(tableDangju, 0d);
    moneyService.setTotalNumOnTable(tableJiaoshou, 0d);
    moneyService.setTotalNumOnTable(tablePingzhang, 0d);

    if ("2017-01-01".equals(dbUtil.Load_Date)) {
      tableTeam.setItems(null);
      moneyService.setTotalNumOnTable(tableTeam, 0d, 4);
    }
    // 清空缓存中的数据
    cleardataConstantsCache();
  }


  /********************************************************** 自定义 分页控件代码 开始 *********/
  // 每一场锁定时添加一个页
  public void pagePaneOpration() {
    int index = dataConstants.Paiju_Index.get();
    pageInput.setText(index + "");
  }

  // 第一页按钮
  public void pageFirstAction(ActionEvent event) {
    if (isHalfImport())
      return;

    pageInput.setText("1");
    // 调用
    getResultByPage(1);
  }

  // 前一页按钮
  public void pagePreAction(ActionEvent event) {
    if (isHalfImport())
      return;

    String page = pageInput.getText();
    int pageIndex = getPageIndex(page, false);
    pageInput.setText(pageIndex + "");
    // 调用
    if (dataConstants.Index_Table_Id_Map.size() > 0 && pageIndex >= 1) {
      getResultByPage(pageIndex);
    }
  }

  // 下一页按钮
  public void pageNextAction(ActionEvent event) {
    if (isHalfImport())
      return;

    String page = pageInput.getText();
    int pageIndex = getPageIndex(page, true);
    pageInput.setText(pageIndex + "");

    // 调用
    if (dataConstants.Index_Table_Id_Map.size() > 0 && pageIndex >= 1) {
      getResultByPage(pageIndex);
    }
  }

  // 最后一页页按钮
  public void pageLastAction(ActionEvent event) {
    if (isHalfImport())
      return;

    pageInput.setText(dataConstants.Paiju_Index.get() + "");
    // 调用
    getResultByPage(dataConstants.Paiju_Index.get());
  }

  // 输入页码
  public void turn2PageEnterAction(KeyEvent event) {
    String pageText = pageInput.getText();
    int pageMax = dataConstants.Paiju_Index.get();
    if (KeyCode.ENTER == event.getCode() && !StringUtil.isBlank(pageText)) {
      try {
        int page = Integer.valueOf(pageText.trim());
        if (page > pageMax || page < 0) {
          ShowUtil.show("值必须界于1到" + pageMax + "之间", 1);
          return;
        }
        // 调用
        getResultByPage(page);

      } catch (Exception e) {
        ShowUtil.show("您输入的不是一个数值", 1);
        pageInput.setText("");
      }
    }
  }

  /**
   * 判断是否只导到一半就撤销（通过页码）
   * 
   * @time 2017年10月17日
   * @return
   */
  private boolean isHalfImport() {
    if (dataConstants.Dangju_Team_Huishui_List.size() > 0) {
      ShowUtil.show("请把本局撤销或锁定再去查看锁定信息！");
      return true;
    }
    return false;
  }

  // 根据用户输入的页码或不断点击前后页时获取可用的页数
  public int getPageIndex(String oldPage, boolean addOrDel) {
    int newPage = 1;
    int currentMaxPage = dataConstants.Paiju_Index.get();
    try {
      if (!StringUtil.isBlank(oldPage)) {
        if (addOrDel) {// 加
          newPage = Integer.valueOf(oldPage) + 1;
          if (newPage > currentMaxPage) {
            newPage--;
          }
        } else {// 减
          newPage = Integer.valueOf(oldPage) - 1;
          if (newPage < 1) {
            newPage++;
          }
        }
      } else {
        newPage = currentMaxPage - 1;
      }
    } catch (Exception e) {// 出现异常默认跳到最后一页
      newPage = currentMaxPage - 1;
    }
    return newPage;
  }

  /**
   * 根据页码查询
   * 
   * @time 2017年10月17日
   * @param pageIndex
   */
  public void getResultByPage(int pageIndex) {
    try {
      // add 2017-11-11 若是开始新的一天则最后一场和第一场不做重新加载
      if (dataConstants.Index_Table_Id_Map.size() == 0) {
        return;
      }

      // 对于已缓存的数据的查询
      int cacheMaxPage = dataConstants.Paiju_Index.get();
      if (pageIndex > 0 && pageIndex < cacheMaxPage) {
        // 恢复十个表数据
        reCovery10TablesByPage(pageIndex);
        // 获取对应的场次
        // 锁定的数据不能再被修改
        fobiddenChangeData();
        indexLabel.setVisible(true);
        logger.debug("dataConstants.Index_Table_Id_Map.get(pageIndex):"
            + dataConstants.Index_Table_Id_Map.get(pageIndex + ""));
        indexLabel.setText(dataConstants.Index_Table_Id_Map.get(pageIndex + ""));

      } else {// 对于缓存之外的数据查询（属于新增）
        // 恢复锁定的数据
        changableData();
        // 加载前一场数据
        pageIndex -= 1;
        reCovery10TablesByPage(pageIndex);// 恢复十个表数据

        // 清空相关表数据（保留类似昨日留底的表数据）
        clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou,
            tablePingzhang);
        // tableCurrentMoneyInfo,tableZijin,tableKaixiao,tableProfit
        indexLabel.setText(INDEX_ZERO);
      }
      // 如果尾页有改动数据，需要同步缓存
      // reCoveryRelatedCache();

    } catch (Exception e) {
      ShowUtil.show("查询失败！！", 1);
      e.printStackTrace();
    }
  }

  public void clear10Tables() {
    // 清空相关表数据（保留类似昨日留底的表数据）
    clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou, tablePingzhang);
    // tableCurrentMoneyInfo,tableZijin,tableKaixiao,tableProfit
    indexLabel.setText(INDEX_ZERO);
  }

  // 恢复十个表数据
  public void reCovery10TablesByPage(int pageIndex) throws Exception {

    moneyService.reCovery10TablesByPage(tableTotalInfo, tablePaiju, tableTeam,
        tableCurrentMoneyInfo, tableZijin, tableKaixiao, tableProfit, tableDangju, tableJiaoshou,
        tablePingzhang, LMLabel, pageIndex);
  }

  /********************************************************** 分页控件代码 结束 *********/
  // 对于锁定的数据禁止被修改
  public void fobiddenChangeData() {
    lockedLabel.setVisible(true);
    lianmengBtn.setDisable(true);
    refreshBtn.setVisible(false);
    lockDangjuBtn.setVisible(false);
    delKaixiaoBtn.setVisible(false);
    delCurrentMoneyBtn.setVisible(false);
    openKaixiaoDialogBtn.setVisible(false);
    addCurrentMoneyLink.setVisible(false);
    importZJHBox.setVisible(false);
    tableCurrentMoneyInfo.setEditable(false);
    tableTeam.getColumns().get(5).setVisible(false);
    tableTeam.refresh();
    tablePaiju.getColumns().get(6).setVisible(false);
    tablePaiju.refresh();
    tableZijin.setEditable(false);

  }

  // 恢复数据（可见，可修改）
  public void changableData() {
    lockedLabel.setVisible(false);
    lianmengBtn.setDisable(false);
    refreshBtn.setVisible(true);
    lockDangjuBtn.setVisible(true);
    delKaixiaoBtn.setVisible(true);
    delCurrentMoneyBtn.setVisible(true);
    openKaixiaoDialogBtn.setVisible(true);
    addCurrentMoneyLink.setVisible(true);
    importZJHBox.setVisible(true);
    tableCurrentMoneyInfo.setEditable(true);
    tableTeam.getColumns().get(5).setVisible(true);
    tableTeam.refresh();
    tablePaiju.getColumns().get(6).setVisible(true);
    tablePaiju.refresh();
    tableZijin.setEditable(true);
  }

  public void dbConnectAction(ActionEvent event) {
    if (DBConnection.getConnection() != null) {
      dbConnectionState.setText("已成功连接！");
      dbConnectionState.setTextFill(Color.web("#0076a3"));
    }
  }

  // 第一次获取获取联盟对账（原始数据）
  private String getLMValFirstTime() {
    String lm = dataConstants.preDataMap.get("联盟对帐");
    try {
      Double.valueOf(lm);
      return lm;
    } catch (Exception e) {
      Map<String, String> map = JSON.parseObject(lm, new TypeReference<Map<String, String>>() {});
      return moneyService.nvl(map.get("联盟对帐"), "0");
    }
  }

  /**
   * 撤销当局信息
   */
  public void openCancelAlertAction(ActionEvent event) {
    if (AlertUtil.confirm("即将加载关闭之前的最后锁定数据，确认要从中途加载吗？")) {
      importZJBtn.setDisable(false);
      logger.debug("确定撤销本局所有操作");
      // 情况一：第一场还没锁定就撤销
      // 直接从昨日留底中加载数据
      // 备份到01场次
      try {
        // 如果以下三个有没有数据则可判断还没导入最新的Excel
        selected_LM_type = "";
        if (tableTotalInfo.getItems() == null || tablePaiju.getItems() == null
            || tableTotalInfo.getItems().size() == 0 || tablePaiju.getItems().size() == 0) {
          ShowUtil.show("您还没有导入数据", 1);
          return;
        }
        // 当局已结算的团队服务费之和 要置为0
        current_Jiesuaned_team_fwf_sum = 0d;

        if (dataConstants.Index_Table_Id_Map.size() == 0) {
          LMLabel.setText(getLMValFirstTime());
          // moneyService.fillTableCurrentMoneyInfo(tableCurrentMoneyInfo, tableZijin,
          // tableProfit,tableKaixiao,LMLabel);
          fillTables(tableCurrentMoneyInfo, tableZijin, tableProfit, tableKaixiao, LMLabel);
          // 清空相关表数据（保留类似昨日留底的表数据）
          dataConstants.SumMap = new LinkedHashMap<>();// 这里不缓存团队回水了，直接清空
          dataConstants.Team_Huishui_Map = new LinkedHashMap<>();
          dataConstants.Total_Team_Huishui_Map = new LinkedHashMap<>();
          dataConstants.Dangju_Team_Huishui_List = new LinkedList<>();

          clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou,
              tablePingzhang);
          // 获取第一次加载的上码表的个人详情{玩家ID=List<ShangmaDetailInfo>}
          dataConstants.refresh_SM_Detail_Map();
          return;
        }

        // 撤销的代码
        cancelDangju();

        logger.debug("=====================缓存中的数据恢复成功！");
        ShowUtil.show("撤销成功", 1);
      } catch (Exception e) {
        ShowUtil.show("撤销失败！！！", 1);
        e.printStackTrace();
      }
    } else {
      logger.debug("取消撤销本局所有操作");
    }
  }

  /**
   * 清空联盟对帐的信息
   */
  public void openLMDialogAction(ActionEvent event) {
    if (AlertUtil.confirm("你确定要清空联盟对称信息么?")) {
      LMLabel.setText("0.00");
      logger.info("确定清空联盟对帐信息");

    } else {
      logger.info("取消清空联盟对帐信息");
    }
  }

  /**
   * 抽取出撤销的共同代码
   * 
   * @time 2017年10月17日
   * @throws Exception
   */
  public void cancelDangju() throws Exception {
    // 加载前一场数据
    int pageIndex = dataConstants.Index_Table_Id_Map.size();
    // 恢复十个表数据
    reCovery10TablesByPage(pageIndex);
    // 清空相关表数据（保留类似昨日留底的表数据）
    clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou, tablePingzhang);

    // 恢复缓存中的数据
    reCoveryRelatedCache();

  }

  /**
   * 恢复缓存中的数据
   */
  public void reCoveryRelatedCache() {
    String tableId = indexLabel.getText();// 如第08局，是多少就多少，这个IndexLabel的值必须正确

    if (dataConstants.Dangju_Team_Huishui_List.size() > 0) {// 说明需要恢复到前一场次的缓存状态
      if (dataConstants.Index_Table_Id_Map.containsValue(tableId)) {
        // 说明用户还没有导战绩进来，但是可能修改了其他地方
        // 最好是保证场次是新的tableId
      }
      // 恢复玩家战绩信息
      dataConstants.zjMap.remove(tableId);

      Map<String, String> maps = dbUtil.getLastLockedData();
      if (maps != null && maps.size() > 0) {
        dataConstants.Team_Huishui_Map = JSON.parseObject(maps.get("Team_Huishui_Map"),
            new TypeReference<Map<String, List<GameRecord>>>() {});
        dataConstants.Total_Team_Huishui_Map = JSON.parseObject(maps.get("Total_Team_Huishui_Map"),
            new TypeReference<Map<String, List<GameRecord>>>() {});
      }
      // 初始化当局回水
      dataConstants.Dangju_Team_Huishui_List = new LinkedList<>();

      // 恢复上一场的团队累计 getLockedInfo
      int currentPage = Integer.parseInt(pageInput.getText());
      int size = dataConstants.Index_Table_Id_Map.size();
      if (currentPage - size == 1) {
        // 此情况下要从上一场加载==团队回水总和
        dataConstants.SumMap = new HashMap<String, Double>();
        String sumOfTeam = moneyService.getLockedInfo(size + "", "团队回水总和");
        String shangchangKaixiao = moneyService.getLockedInfo(size + "", "实时开销总和");
        if ("".equals(sumOfTeam)) {
          logger.error("从上一场加载==团队回水总和失败！！！！");
        } else {
          dataConstants.SumMap.put("团队回水及保险总和", Double.valueOf(sumOfTeam));
          dataConstants.SumMap.put("上场开销", Double.valueOf(shangchangKaixiao));// add 9-1
        }
      }
    }
    // 上码表恢复数据
    dataConstants.recovery_SM_Detail_Map();
  }

  public int getCurrentPage() {
    return Integer.parseInt(pageInput.getText());
  }


  /**
   * 总汇刷新按钮
   */
  public void zonghuiRefreshAction(ActionEvent event) {
    zonghuiService.refreHuizongTable(tableZonghui, tableDangtianHuizong, tableZonghuiKaixiao,
        tableProfit);
  }

  /**
   * 外债刷新按钮
   */
  public void waizhaiRefreshAction(ActionEvent event) {
    waizhaiService.generateWaizhaiTables(tableWaizhai, waizhaiHBox, tableCurrentMoneyInfo,
        tableTeam);
  }

  /**
   * 会员搜索(按钮)
   */
  public void memberSearchAction(ActionEvent event) {
    memberService.setResult2ListView(memberSearchName, memberListView);
  }

  /**
   * 会员搜索(回车)
   */
  public void searchMemberByEnterEvent(KeyEvent event) {
    String keyWord = memberSearchName.getText();
    if (KeyCode.ENTER == event.getCode() && !StringUtil.isBlank(keyWord)) {
      memberService.setResult2ListView(memberSearchName, memberListView);
    }
  }

  /**
   * 实时上码导出为Excel
   */
  public void exportSMExcelAction(ActionEvent event) {
    shangmaService.exportShangmaExcel();
  }

  /**
   * 删除实时开销按钮
   */
  public void delKaixiaoAction(ActionEvent event) {
    int kaixiaoIndex = tableKaixiao.getSelectionModel().getFocusedIndex();
    KaixiaoInfo info = tableKaixiao.getSelectionModel().getSelectedItem();
    if (info != null) {
      String content = "实时开销名称：" + info.getKaixiaoType() + " 金额：" + info.getKaixiaoMoney()
      + "\r\n你确定要删除所选中的开销记录吗?";
      if (AlertUtil.confirm(content)) {
        tableKaixiao.getItems().remove(kaixiaoIndex);
        tableKaixiao.refresh();
        // 删除数据库中的开销数据
        String kaixiaoID = info.getKaixiaoID();
        String kaixiaoGudong = info.getKaixiaoGudong();
        if (!StringUtil.isAnyBlank(kaixiaoID, kaixiaoGudong)) {
          dbUtil.del_gudong_kaixiao_by_id(kaixiaoID);
        }
      }
    } else {
      ShowUtil.show("请选中要删除的实时开销记录!");
      return;
    }
  }

  /**
   * 删除实时金额
   */
  public void delCurrentMoneyAction(ActionEvent event) {
    int index = tableCurrentMoneyInfo.getSelectionModel().getFocusedIndex();
    CurrentMoneyInfo info = tableCurrentMoneyInfo.getSelectionModel().getSelectedItem();
    if (info != null) {
      String content =
          "实时金额名称：" + info.getMingzi() + " 金额：" + info.getShishiJine() + "\r\n你确定要删除所选中的实时金额吗?";
      if (AlertUtil.confirm(content)) {
        tableCurrentMoneyInfo.getItems().remove(index);
        tableCurrentMoneyInfo.refresh();
        moneyService.flush_SSJE_table();
      }
    } else {
      ShowUtil.show("请选中要删除的实时金额记录!");
      return;
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
      softDateLabel.setText(dataConstants.Date_Str);
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
      
      dataConstants.Date_Str = getSoftDate();// 此行代码不能删，因为上行代码已将其时间删除
      // 加载必要的原始数据（人员和回水）
      dataConstants.initMetaData();
      // 加载昨日数据
      dataConstants.loadPreData();

      // 渲染表格数据
      LMLabel.setText(getLMValFirstTime());
      fillTables(tableCurrentMoneyInfo, tableZijin, tableProfit, tableKaixiao, LMLabel);
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
   * 把渲染表格的数据加载出来，要判断是否2017-01-01，分成两步
   * 
   * @param table
   * @param tableZijin
   * @param tableProfit
   * @param tableKaixiao
   * @param LMLabel
   */
  public void fillTables(TableView<CurrentMoneyInfo> table, TableView<ZijinInfo> tableZijin,
      TableView<ProfitInfo> tableProfit, TableView<KaixiaoInfo> tableKaixiao, Label LMLabel) {
    // 清空十个表数据
    clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou, tablePingzhang);
    indexLabel.setText(INDEX_ZERO);

    if (dbUtil.isPreData2017VeryFirst()) {
      moneyService.fillTableCurrentMoneyInfo(tableCurrentMoneyInfo, tableZijin, tableProfit,
          tableKaixiao, LMLabel);
    } else {
      moneyService.fillTableCurrentMoneyInfo2(tableTeam, tableCurrentMoneyInfo, tableZijin,
          tableProfit, tableKaixiao, LMLabel);
      // 缓存战绩文件夹中多份excel中的数据 {团队ID=List<GameRecord>...}这个可能会被修改，用在展示每场的tableTeam信息
      Map<String, String> map = dbUtil.getLastLockedData();
      if (map != null && map.size() > 0) {
        dataConstants.Team_Huishui_Map = JSON.parseObject(map.get("Team_Huishui_Map"),
            new TypeReference<Map<String, List<GameRecord>>>() {});
      }
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

      // 恢复所有缓存数据
      dataConstants.recoveryAllCache();
      dataConstants.initMetaData();
      // add 2017-10-21 代理类初始化团队ID
      teamProxyService.initTeamSelectAndZjManage(teamProxyController.teamIDCombox);
      // add 2017-10-21 代理类初始化团队ID
      jifenService.init_Jifen_TeamIdCombox();

      // 加载十个表格数据
      int pageIndex = dataConstants.Paiju_Index.get();
      pageInput.setText(pageIndex + "");
      try {
        softDateLabel.setText(dataConstants.Date_Str);
        // 恢复锁定的数据
        changableData();
        // 加载前一场数据
        pageIndex -= 1;
        reCovery10TablesByPage(pageIndex);// 恢复十个表数据
        moneyService.flush_SSJE_table();

        // 清空相关表数据（保留类似昨日留底的表数据）
        clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou,
            tablePingzhang);
        indexLabel.setText(INDEX_ZERO);
      } catch (Exception e) {
        ShowUtil.show("中途继续失败：原因：" + e.getMessage());
        e.printStackTrace();
      }
      ShowUtil.show("中途继续成功，请继续操作", 2);
      // 转到场次信息页面
      tabs.getSelectionModel().select(1);
    }
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
   * @param event
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
   * 查询会员积分排名
   * 
   * @param event
   */
  public void jfQueryAciton(ActionEvent event) {
    jifenService.jifenQuery(tableJifen, jfStartTime, jfEndTime, jifenInput, jifenRankLimit,
        jfTeamIDCombox);
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
   * 导出实时金额表
   * 
   * @time 2017年10月28日
   * @param event
   */
  public void exportSSJEAction(ActionEvent event) {
    try {
      moneyService.exportSSJEAction(tableCurrentMoneyInfo);
      ShowUtil.show("导出实时金额表Excel成功", 2);
    } catch (Exception e) {
      ShowUtil.show("导出实时金额表Excel失败，原因：" + e.getMessage());
    }
  }

  /**
   * 刷新下一场的缓存按钮
   * 
   * @time 2017年11月10日
   * @param event
   */
  public void refreshNextExcelAction(ActionEvent event) {
    
    if (AlertUtil.confirm("你确定要强制刷新自动导入下一场的缓存操作吗?")) {

      // 获取导入的值
      String zjFilePath = excelDir.getText();
      if (StringUtil.isBlank(zjFilePath)) {
        ShowUtil.show("战绩栏要有初始Excel地址！");
        return;
      }
      // 获取父级目录下的所有Excel文件
      File rootFile = new File(zjFilePath.substring(0, zjFilePath.lastIndexOf("\\")));
      File[] excelList = rootFile.listFiles();
      // 清空并重置待导入队列
      excelQueue.clear();
      for (File file : excelList) {
        try {
          excelQueue.put(file);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 导入下一场（测试使用）
   * 
   * @time 2017年11月10日
   * @param event
   */
  public void importNextZJExcelAction(ActionEvent event) {
    if (excelQueue.size() > 0) {
      try {
        File excelFile = excelQueue.poll();
        excelDir.setText(excelFile.getAbsolutePath());
        importZJBtn.fire();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      ShowUtil.show("没有下一场的数据了", 2);
    }
  }



  /**
   * 删除团队ID 备注：此方法的界面按钮已经删除，但后台代码仍保留着（以备后期要重新开发）
   * 
   * @time 2017年11月14日
   * @param event
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
        dbUtil.delMembers_after_delTeam(teamId);
        synchronized (dataConstants.membersMap) {
          Iterator<Map.Entry<String, Player>> ite = dataConstants.membersMap.entrySet().iterator();
          while (ite.hasNext()) {
            Map.Entry<String, Player> entry = ite.next();
            Player player = entry.getValue();
            if (player.getTeamName().trim().toUpperCase().equals(teamId)) {
              ite.remove();// 删除
            }
          }
        }

        // 以下清除代码
        // A 清空代理查询中的团队下拉框
        String selected_team = teamProxyController.teamIDCombox.getSelectionModel().getSelectedItem();
        teamProxyController.teamIDCombox.getItems().remove(teamId);
        if (teamId.equals(selected_team) && teamProxyController.teamIDCombox.getItems().size() > 0) {
          teamProxyController.teamIDCombox.getSelectionModel().select(0);
        }
        // B 积分查询中的团队下拉框
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
   * @param event
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
   * @param event
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
   * 
   * @param event
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
   * @param even
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
   * 导入空白表
   * 
   * @time 2018年2月25日
   * @param event
   */
  public void importBlankExcelAction(ActionEvent event) {
    String tableId = RandomUtil.getRandomNumber(10000, 20000) + "";// 随机生成ID
    List<GameRecord> blankDataList = new ArrayList<GameRecord>();
    // 存储数据 {场次=infoList...}
    dataConstants.zjMap.put(tableId, blankDataList);
    final_selected_LM_type = "联盟1";
    selected_LM_type = "联盟1";
    lmController.currentRecordList = new ArrayList<>();

    indexLabel.setText("第" + tableId + "局");
    importExcelData(tableId, blankDataList);

    importZJBtn.setDisable(true);// 导入不可用
    ShowUtil.show("导入空白战绩文件成功", 2);
  }


  /*******************************************************************************************
   * 
   * 0.95与0.975切换版本
   * 
   ********************************************************************************************/

  /**
   * 根据原始战绩获取回水 1：出回水 2：收回水, 此时teamId非必要参数 备注：0.95版本：如果原始战绩为正数，则出回水和收回水都是0
   * 
   * @time 2018年5月19日
   * @param yszj
   * @return
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
   * @param yszjz
   * @param teamId
   * @param type
   * @return
   */
  private String getByYSZJ(String yszj, String teamId, int type) {
    Double zhanji = Double.valueOf(yszj);
    if (type == 1) {
      return NumUtil.digit1(moneyService.getChuhuishui(yszj, teamId));
    } else {
      return NumUtil
          .digit1(Math.abs(Double.valueOf(zhanji)) * (1 - Constants.CURRENT_HS_RATE) + "");
    }
  }
  
  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
