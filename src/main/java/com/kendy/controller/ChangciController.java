/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.controller.tgController.TGController;
import com.kendy.db.DBUtil;
import com.kendy.db.entity.CurrentMoney;
import com.kendy.db.entity.Player;
import com.kendy.db.entity.pk.CurrentMoneyPK;
import com.kendy.db.service.CurrentMoneyService;
import com.kendy.db.service.GameRecordService;
import com.kendy.db.service.PlayerService;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.DangjuInfo;
import com.kendy.entity.Huishui;
import com.kendy.entity.JiaoshouInfo;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.PersonalInfo;
import com.kendy.entity.PingzhangInfo;
import com.kendy.entity.ProfitInfo;
import com.kendy.entity.TeamInfo;
import com.kendy.entity.TotalInfo;
import com.kendy.entity.WanjiaInfo;
import com.kendy.entity.ZijinInfo;
import com.kendy.enums.MoneyCreatorEnum;
import com.kendy.excel.ExcelReaderUtil;
import com.kendy.interfaces.Entity;
import com.kendy.model.GameRecordModel;
import com.kendy.service.JifenService;
import com.kendy.service.LittleGameService;
import com.kendy.service.PersonalService;
import com.kendy.service.MemberService;
import com.kendy.service.MoneyService;
import com.kendy.service.ShangmaService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.ZonghuiService;
import com.kendy.util.AlertUtil;
import com.kendy.util.ClipBoardUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.FileUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.PathUtil;
import com.kendy.util.RandomUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.Text2ImageUtil;
import com.kendy.util.TimeUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangciController extends BaseController implements Initializable {

  // ===============================================================总汇主表
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
  public MyController myController;
  @Resource
  private GameRecordService gameRecordService;
  @Resource
  private CurrentMoneyService currentMoneyService;
  @Resource
  PlayerService playerService;
  @Autowired
  PersonalService personalService;
  @Autowired
  LittleGameService littleGameService;

  // =================================================第一个tableView
  @FXML
  public TableView<TotalInfo> tableTotalInfo;

  @FXML
  public TableColumn<TotalInfo, String> tuan;// 团
  @FXML
  public TableColumn<TotalInfo, String> wanjiaId;// ID
  @FXML
  public TableColumn<TotalInfo, String> wanjia;// 玩家
  @FXML
  public TableColumn<TotalInfo, String> jifen;// 计分
  @FXML
  public TableColumn<TotalInfo, String> shishou;// 实收
  @FXML
  public TableColumn<TotalInfo, String> baoxian;// 保险
  @FXML
  public TableColumn<TotalInfo, String> chuHuishui;// 出回水
  @FXML
  public TableColumn<TotalInfo, String> baohui;// 保回

  @FXML
  public TableColumn<TotalInfo, String> shuihouxian;// 水后险
  @FXML
  public TableColumn<TotalInfo, String> shouHuishui;// 收回水
  @FXML
  public TableColumn<TotalInfo, String> heLirun;// 合利润

  // =================================================实时金额表tableView
  @FXML
  public TableView<CurrentMoneyInfo> tableCurrentMoneyInfo;

  @FXML
  public TableColumn<CurrentMoneyInfo, String> cmSuperIdSum;// 总和
  @FXML
  public TableColumn<CurrentMoneyInfo, String> mingzi;// 名字
  @FXML
  public TableColumn<CurrentMoneyInfo, String> shishiJine;// 实时金额
  @FXML
  public TableColumn<CurrentMoneyInfo, String> cmiEdu;// 实时金额
  @FXML
  public TableColumn<CurrentMoneyInfo, String> cmiLmb;// 用户剩余联盟币

  // =================================================资金表tableView
  @FXML
  public TableView<ZijinInfo> tableZijin;

  @FXML
  public TableColumn<ZijinInfo, String> zijinType;
  @FXML
  public TableColumn<ZijinInfo, String> zijinAccount;
  // =================================================利润表tableView
  @FXML
  public TableView<ProfitInfo> tableProfit;

  @FXML
  public TableColumn<ProfitInfo, String> profitType;
  @FXML
  public TableColumn<ProfitInfo, String> profitAccount;
  // =================================================开销表tableView
  @FXML
  public TableView<KaixiaoInfo> tableKaixiao;

  @FXML
  public TableColumn<KaixiaoInfo, String> kaixiaoType;
  @FXML
  public TableColumn<KaixiaoInfo, String> kaixiaoMoney;
  // =================================================当局表tableView
  @FXML
  public TableView<DangjuInfo> tableDangju;

  @FXML
  public TableColumn<DangjuInfo, String> type;
  @FXML
  public TableColumn<DangjuInfo, String> money;
  // =================================================交收表tableView
  @FXML
  public TableView<JiaoshouInfo> tableJiaoshou;

  @FXML
  public TableColumn<JiaoshouInfo, String> jiaoshouType;
  @FXML
  public TableColumn<JiaoshouInfo, String> jiaoshouMoney;
  // =================================================交收表tableView
  @FXML
  public TableView<PingzhangInfo> tablePingzhang;

  @FXML
  public TableColumn<PingzhangInfo, String> pingzhangType;
  @FXML
  public TableColumn<PingzhangInfo, String> pingzhangMoney;

  // =================================================牌局表tableView
  @FXML
  public TableView<WanjiaInfo> tablePaiju;

  @FXML
  public TableColumn<WanjiaInfo, String> paiju;// 名字
  @FXML
  public TableColumn<WanjiaInfo, String> wanjiaName;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> zhangji;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> yicunJifen;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> heji;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> pay;// 支付
  @FXML
  public TableColumn<WanjiaInfo, String> copy;// 复制

  // =================================================团队累计表tableView
  @FXML
  public TableView<TeamInfo> tableTeam;

  @FXML
  public TableColumn<TeamInfo, String> teamID;// 团ID
  @FXML
  public TableColumn<TeamInfo, String> teamZJ;// 团战绩
  @FXML
  public TableColumn<TeamInfo, String> teamHS;// 团回水
  @FXML
  public TableColumn<TeamInfo, String> teamBS;// 团保险
  @FXML
  public TableColumn<TeamInfo, String> teamSum;// 行总和
  @FXML
  public TableColumn<TeamInfo, String> teamJiesuan;// 结算按钮

  // =================================================个人累计表
  @FXML
  public TableView<PersonalInfo> tablePersonal;

  @FXML
  public TableColumn<PersonalInfo, String> personalPlayerId;// 团ID
  @FXML
  public TableColumn<PersonalInfo, String> personalPlayerName;// 团战绩
  @FXML
  public TableColumn<PersonalInfo, String> personalSumZJ;// 团回水
  @FXML
  public TableColumn<PersonalInfo, String> personalSumHS;// 团保险
  @FXML
  public TableColumn<PersonalInfo, String> personalSumHB;// 行总和
  @FXML
  public TableColumn<PersonalInfo, String> personalPay;// 结算按钮


  // ===========================================联盟对帐
  @FXML
  public Button lianmengBtn;
  @FXML
  public Label LMLabel;

  // ===========================================分页控件
  @FXML
  public TextField pageInput;

  @FXML
  public Button refreshBtn;
  @FXML
  public Button lockDangjuBtn;
  @FXML
  public Button openKaixiaoDialogBtn;
  @FXML
  public Button importZJBtn;
  @FXML
  public Button delKaixiaoBtn;
  @FXML
  public HBox importZJHBox;

  @FXML
  public Hyperlink addCurrentMoneyLink;
  @FXML
  public Hyperlink delCurrentMoneyLink;
  @FXML
  public Label lockedLabel;
  @FXML
  public Label softDateLabel; //软件时间

  @FXML
  public Label indexLabel;// 第几局

  @FXML
  public TextField searchText;//

  @FXML
  public Hyperlink delCurrentMoneyBtn;

  @FXML
  public TextField excelDir; // excel文件夹路径

  @FXML
  public HBox hbox_autoTestMode;

  @FXML
  public JFXToggleButton spiderNode; // 关闭和开启自动购买联盟币按钮

  @FXML
  public StackPane stackPane;


  private final String ZERO = "0";
  public final String INDEX_ZERO = "第0局";
  public static final Object ssjeMonitor = new Object();

  // 实时金额监视锁
  private Object tableCurrentMoneyInfoLock = new Object();

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    initSpiderToggleAction();

    // 绑定玩家信息表
    bindCellValueByTable(new TotalInfo(), tableTotalInfo);
    setTableTotalInfoDialog();

    // 绑定牌局表
    bindCellValueByTable(new WanjiaInfo(), tablePaiju);
    pay.setCellFactory(cellFactory);// 支付按钮：单独出来
    copy.setCellFactory(cellFactoryCopy);// 复制按钮：单独出来
    //setColumnCenter(pay, copy);

    // 绑定实时金额表
    tableCurrentMoneyInfo.setEditable(true);
    bindCellValueByTable(new CurrentMoneyInfo(), tableCurrentMoneyInfo);
    cmSuperIdSum.setStyle(Constants.CSS_CENTER_BOLD);
    cmSuperIdSum.setCellFactory(sumMoneyCellFactory);
    shishiJine.setCellFactory(TextFieldTableCell.forTableColumn());
    cmiLmb.setCellFactory(cmiLmbCellFactory);
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
    // 绑定个人表
    bindCellValueByTable(new PersonalInfo(), tablePersonal);
    personalPay.setCellFactory(personalCellFactory);
    tablePersonal.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        PersonalInfo item = getSelectedRow(tablePersonal);
        if (item != null && item.getPersonalPlayerId() != null) {
          personalService.openPersonalInfoDetailailView(item.getPersonalPlayerName(), item.getPersonalPlayerId());
        }
      }
    });

    // 初始化实时金额表
    moneyService.iniitMoneyInfo(tableCurrentMoneyInfo);

    LMLabel.setTextFill(Color.web("#CD3700"));
    indexLabel.setTextFill(Color.web("#0076a3"));// 设置Label 的文本颜色。
    indexLabel.setFont(new Font("Arial", 30));
  }



  /**
   * 双击第一个表格是展示相应的玩家信息
   */
  private void setTableTotalInfoDialog() {
    tableTotalInfo.setOnMouseClicked(e -> {
      // 行双击
      if (e.getClickCount() == 2) {
        TotalInfo item = TableUtil.getSelectedRow(tableTotalInfo);
        if (item != null && StringUtils.isNotBlank(item.getWanjiaId())) {
          // 具体业务逻辑
          showModalDialog(stackPane, "玩家信息", getPlayerInfo(item.getWanjiaId()).toString());
        }
      }
    });
  }

  /**
   * 获取玩家信息
   */
  private StringBuilder getPlayerInfo(String playerId) {
    StringBuilder sb = new StringBuilder();
    String NEW_LINE = System.lineSeparator();
    Player player = playerService.get(playerId);
    if (player != null) {
      sb.append("玩家名称：").append(player.getPlayername()).append(NEW_LINE);
      sb.append("玩家ID：").append(player.getPlayerid()).append(NEW_LINE);
      sb.append("所属团队：").append(player.getTeamid()).append(NEW_LINE);
      sb.append("所属股东：").append(player.getGudong()).append(NEW_LINE);
      sb.append("额度：").append(player.getEdu()).append(NEW_LINE);
      sb.append("是否父ID：").append(combineIDController.isSuperId(player.getPlayerid()) ? "是" : "否")
          .append(NEW_LINE);
      sb.append("是否子ID：").append(combineIDController.isSubId(player.getPlayerid()) ? "是" : "否")
          .append(NEW_LINE);
      sb.append("个人回水：").append(player.getHuishui()).append(NEW_LINE);
      sb.append("个人回保：").append(player.getHuibao()).append(NEW_LINE);
    }
    return sb;
  }

  /**
   * 弹框提示
   */
  private void showModalDialog(StackPane stackPane, String header, String content) {
    JFXDialogLayout dialogContent = new JFXDialogLayout();
    dialogContent.setHeading(new Text(header));
    Text text = new Text(content);
    text.setLineSpacing(8);
    dialogContent.setBody(text);
    JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.CENTER);
    dialog.show();
  }


  /*
   * 每点击结算按钮就往这个静态变更累加（只针对当局） 撤销时清空为0 锁定时清空为0 平帐时与上场的总团队服务费相加
   */
  public Double current_Jiesuaned_team_fwf_sum = 0d;

  // 自动导入下一场战绩Excel的缓存队列
  public ArrayBlockingQueue<File> excelQueue = new ArrayBlockingQueue<>(2500);

  /**
   * 实时金额修改
   */
  private void setSSJEEditOnCommit() {
    shishiJine.setOnEditCommit(t -> {
      String oldVal = t.getOldValue();
      String newVal = t.getNewValue();
      // 若值相等，则不做处理
      if (StringUtils.equals(oldVal, newVal)) {
        return;
      }

      // 修改原值
      CurrentMoneyInfo cmInfo =
          t.getTableView().getItems().get(t.getTablePosition().getRow());

      if (cmInfo != null && StringUtils.isNotBlank(cmInfo.getMingzi())) {
        // 更新到已存积分
        boolean isChangedOK =
            moneyService.changeYicunJifen(tablePaiju, cmInfo.getMingzi(), newVal);
        if (isChangedOK) {
          cmInfo.setShishiJine(newVal);
          //同步到数据库
          moneyService.saveOrUpdate2DB(cmInfo);
        } else {
          cmInfo.setShishiJine(oldVal);
          tableCurrentMoneyInfo.refresh();
        }
        moneyService.flush_SSJE_table();// 最后刷新实时金额表
        // 记录修改的日志
        logger.info("手动修改实时金额数据记录：玩家名称：{}，ID是：{}, 旧金额：{}, 新金额：{}, 修改时间：{}"
            , cmInfo.getMingzi(), StringUtils.defaultString(cmInfo.getWanjiaId(), "空"), oldVal,
            newVal, TimeUtil.getDateTime2());

      } else if (cmInfo != null) {
        cmInfo.setShishiJine(null);
        ShowUtil.show("空行不能输入", 1);
        tableCurrentMoneyInfo.refresh();
      } else {

      }
    });
  }

  /**
   * 单独设置列居中
   */
  @SuppressWarnings("unchecked")
  public <T extends Entity> void setColumnCenter(TableColumn<T, ?>... colums) {
    for (TableColumn<T, ?> column : colums) {
      column.setStyle(Constants.CSS_CENTER);
    }
  }

  public static final List<String> NO_NEED_LOAD_TABS = Arrays.asList("基本信息", "场次信息", "总汇信息");


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

  private void importExcelData(String tableId, List<GameRecordModel> gameRecordModels) {
    // 1 填充总信息表
    moneyService.fillTablerAfterImportZJ(tableTotalInfo, tablePaiju, tableDangju, tableJiaoshou,
        tableTeam, tablePersonal, gameRecordModels, tableId);
    // 2填充当局表和交收表和团队表的总和
    moneyService.setTotalNumOnTable(tableDangju, dataConstants.SumMap.get("当局"));
    moneyService.setTotalNumOnTable(tableJiaoshou, dataConstants.SumMap.get("交收"));
    tableTeam.getColumns().get(4)
        .setText(moneyService.digit0(dataConstants.SumMap.get("团队回水及保险总和")));
  }


  /**
   * 牌局表列中添加支付按钮
   */
  Callback<TableColumn<PersonalInfo, String>, TableCell<PersonalInfo, String>> personalCellFactory = //
      new Callback<TableColumn<PersonalInfo, String>, TableCell<PersonalInfo, String>>() {
        @SuppressWarnings("rawtypes")
        @Override
        public TableCell call(final TableColumn<PersonalInfo, String> param) {
          final TableCell<PersonalInfo, String> cell = new TableCell<PersonalInfo, String>() {

            final Button btn = new Button("支付");

            @Override
            public void updateItem(String item, boolean empty) {
              super.updateItem(item, empty);
              if (empty) {
                setGraphic(null);
                setText(null);
              } else {
                PersonalInfo personalInfo = getTableView().getItems().get(getIndex());
                btn.setOnAction(event -> {
                  if (StringUtils
                      .equals(Constants.PERSONAL_OF_JIE_SUANED, personalInfo.getHasJiesuaned())) {
                    ShowUtil.show("抱歉，已支付过！！");
                    return;
                  }
                  if (getTableRow() != null) {
                    try {
                      // //支付时修改玩家实时金额 或 添加 实时金额
                      moneyService.updateOrAdd_SSJE_after_personal_Pay(personalInfo);
                      // 更新
                      gameRecordService.updatePersonalJieSuan(personalInfo.getPersonalPlayerId());

                      personalInfo.setHasJiesuaned("1");
                      personalInfo.setPersonalSumHB("0");
                      personalInfo.setPersonalSumHS("0");
                      tablePersonal.refresh();

                      btn.setText("已支付");
                      personalInfo.setHasJiesuaned(Constants.PERSONAL_OF_JIE_SUANED);
                      CurrentMoneyInfo cmi = moneyService
                          .get_CMI_byId(personalInfo.getPersonalPlayerId());
                      ShowUtil.show("已将" + personalInfo.getPersonalPlayerName() + "的实时金额修改为   "
                          + cmi.getShishiJine(), 2);
                      moneyService.flush_SSJE_table();// 刷新实时金额表

                    } catch (Exception e) {
                      ErrorUtil.err("支付失败", e);
                    }
                  }
                });
                //PersonalInfo wj = getTableView().getItems().get(getIndex());
                // 解决不时本应支付确显示成已支付的bug
                if (StringUtils
                    .equals(Constants.PERSONAL_OF_UN_JIE_SUAN, personalInfo.getHasJiesuaned())) {
                  btn.setText("支付");
                } else {
                  btn.setText("已支付");
                }
                setGraphic(btn);// 小林：这一行解决了支付按钮消失的问题
                // 在此处增加是否要显示该按钮(如果玩家从属于某个非空或非公司的团队，则无需显示按钮)
//                String tempTeamId = wj.getHasPayed();// 这个tempTeamId是hasPayed的内容,这里没有公司的人
//                if (!StringUtil.isBlank(tempTeamId) && !"0".equals(tempTeamId)) {
//                  // 获取团队信息
//                  Huishui hs = dataConstants.huishuiMap.get(tempTeamId);
//                  // 情况一：有从属团队的玩家，再分两种情况
//                  if (hs != null) {
//                    // A:若团队战绩要管理，需要显示支付按钮
//                    if ("是".equals(hs.getZjManaged())) {
//                      // log.debug("====teamId为不为空，要显示，要战绩管理：是");
//                      setGraphic(btn);
//                      // B:若团队战绩不要管理，无须显示支付按钮
//                    } else {
//                      // log.debug("hsPayed:====================hs为空："+hs.getZjManaged());
//                      setGraphic(null);
//                    }
//                  }
//                } else {
//                  // log.debug("====teamId为空或为0，要显示+"+dataConstants.membersMap.get(wj.getWanjiaId()).getTeamName());
//                  // 情况二：对于没有从从属的团队的玩家或者团队是公司的玩家，一定需要需要显示支付按钮
//                  setGraphic(btn);
//                }
                // setGraphic(btn);
                setText(null);
              }
            }
          };
          return cell;
        }
      };

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
                    }
                    // 缓存中清空之前所加的团队回水，以便下次团队累计重新从0开始
                    String teamID = teamInfo.getTeamID();
                    //dataConstants.Team_Huishui_Map.remove(teamID);
                    //结算时设置相关gameRecord记录为已结算
                    setIsJiesuaned(teamID);
                    System.out.println("==================结算删除：" + teamID + ", dataConstant :"
                        + dataConstants.Team_Huishui_Map.get(teamID));
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

  /**
   * 结算时设置相关gameRecord记录为已结算
   * <P>
   * 解决中途继续后不能平帐的问题
   */
  public void setIsJiesuaned(String teamId) {
    dataConstants.Team_Huishui_Map.remove(teamId);
    String clubId = myController.getClubId();
    lmController.currentRecordList.stream().forEach(e -> {
      if (StringUtils.equals(e.getClubid(), clubId)
          && StringUtils.equals(e.getTeamId(), teamId)) {
        e.setIsjiesuaned("1");
      }
    });

    dbUtil.updateRecordJiesuan(getSoftDate(), clubId, teamId);
  }

  public TeamInfo copyTeamInfo(TeamInfo info) {
    TeamInfo temp = new TeamInfo(info.getTeamID(), info.getTeamZJ(), info.getTeamHS(),
        info.getTeamBS(), info.getTeamSum());
    return temp;
  }

  /**
   * 点击结算按钮后往实时金额新增一条团队记录（减去该团队服务费）
   *
   * @time 2018年1月4日
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
          cmiInfo = new CurrentMoneyInfo(teamName, tempSSJE, "", "",
              MoneyCreatorEnum.DEFAULT.getCreatorName(), "");// 玩家ID和额度为空
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
              if (isLockedView()) {
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
   * 实时金额表总和双击
   */
  Callback<TableColumn<CurrentMoneyInfo, String>, TableCell<CurrentMoneyInfo, String>> sumMoneyCellFactory =
      new Callback<TableColumn<CurrentMoneyInfo, String>, TableCell<CurrentMoneyInfo, String>>() {
        @Override
        public TableCell<CurrentMoneyInfo, String> call(
            TableColumn<CurrentMoneyInfo, String> param) {

          TextFieldTableCell<CurrentMoneyInfo, String> cell = new TextFieldTableCell<>();
          cell.setEditable(false);// 不让其可编辑
          cell.setOnMouseClicked((MouseEvent t) -> {
            // 鼠标双击事件
            if (t.getClickCount() == 2) {
              CurrentMoneyInfo item = tableCurrentMoneyInfo.getItems().get(cell.getIndex());
              boolean isBlankRow = StringUtils
                  .isAllBlank(item.getMingzi(), item.getShishiJine(), item.getWanjiaId());
              boolean notSuperId = !dataConstants.Combine_Super_Id_Map
                  .containsKey(item.getWanjiaId());

              if (notSuperId) {
                return;
              }
              // 双击执行的代码
              moneyService.showSumPersonSSJE(tableCurrentMoneyInfo, cell.getIndex());
            }
          });
          return cell;
        }
      };

  /**
   * 实时金额表中联盟币双击事件
   */
  Callback<TableColumn<CurrentMoneyInfo, String>, TableCell<CurrentMoneyInfo, String>> cmiLmbCellFactory =
      new Callback<TableColumn<CurrentMoneyInfo, String>, TableCell<CurrentMoneyInfo, String>>() {
        @Override
        public TableCell<CurrentMoneyInfo, String> call(
            TableColumn<CurrentMoneyInfo, String> param) {
          TextFieldTableCell<CurrentMoneyInfo, String> cell = new TextFieldTableCell<>();
          cell.setEditable(false);// 不让其可编辑
          cell.setOnMouseClicked((MouseEvent t) -> {
            // 鼠标双击事件
            if (t.getClickCount() == 2 && isEditingView() && cell.getIndex() < tableCurrentMoneyInfo
                .getItems().size()) {
              CurrentMoneyInfo cmi = tableCurrentMoneyInfo.getItems().get(cell.getIndex());
              if (cmi != null && StringUtil.isAllNotBlank(cmi.getWanjiaId(), cmi.getMingzi())) {
                // 双击执行的代码
                openAddLmbDialog(cmi);
              }
            }
          });
          return cell;
        }
      };

  // 右表：名称鼠标双击事件：打开对话框增加上码值
  private void openAddLmbDialog(CurrentMoneyInfo cmi) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("联盟币");
    dialog.setGraphic(new ImageView(new Image("/images/coin.png")));
    dialog.setHeaderText(null);
    dialog.setContentText("续增联盟币:");
    ShowUtil.setIcon(dialog);
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      String addMoney = result.get();
      String oldLmb = StringUtils.defaultString(cmi.getCmiLmb(), "0");
      addMoney = StringUtil.nvl(addMoney);
      cmi.setCmiLmb(NumUtil.digit2(NumUtil.getNum(oldLmb) + NumUtil.getNum(addMoney) + ""));
      // 保存到数据库
      moneyService.saveOrUpdate2DB(cmi);
      tableCurrentMoneyInfo.refresh();
    }
  }

  /**
   * 复制玩家信息到剪切板，并转成图片发到QQ
   *
   * @param wj 玩家信息
   */
  private void clip2QQ(WanjiaInfo wj) throws Exception {
    String code = myController.sysCode.getText();
    if (StringUtil.isBlank(code)) {
      code = "GBK";
    }
    String html = Text2ImageUtil.getHtml(wj);
    BufferedImage img = Text2ImageUtil.toImage(html, code, 410, 100);

    //通知功能
    try {
      ClipBoardUtil.setClipboardImage(img);
      Platform.runLater(() -> {
        Notifications
            .create().title("截图成功").darkStyle()
            .text(wj.getWanjiaName() + System.lineSeparator() + wj.getPaiju())
            .position(Pos.BOTTOM_LEFT)
            .showInformation();
      });
    } catch (Exception e) {
      Platform.runLater(() -> {
        Notifications.create().title("截图失败").text(e.getMessage()).position(Pos.BOTTOM_RIGHT)
            .showError();
      });
      e.printStackTrace();
    }
  }

  /**
   * 更新开销表
   */
  public void updateKaixiaoTable(KaixiaoInfo info) {
    // 获取ObserableList
    ObservableList<KaixiaoInfo> list = tableKaixiao.getItems();
    list.add(info);
    tableKaixiao.setItems(list);
  }

  /**
   * 更新实时金额表
   */
  public void updateCurrentMoneyTable(Player player, String SSJE) {
    // 获取ObserableList
    ObservableList<CurrentMoneyInfo> list = tableCurrentMoneyInfo.getItems();
    list.add(
        new CurrentMoneyInfo(
            player.getPlayername(), SSJE, player.getPlayerid(), player.getEdu(),
            MoneyCreatorEnum.DEFAULT.getCreatorName(), ""));
    tableCurrentMoneyInfo.setItems(list);
  }

  /**
   * 平帐按钮
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
//          if (littleGameService.isLittleGame(ZhanjiType.getInstance().getGameName())) {
//            // 若是小游戏，则不对联盟对账进行处理
//
//          } else {
//          }
          LMLabel.setText(NumUtil.digit0(sumOfJS * (-1)) + "");
        }
        // 锁定并缓存十个表数据
        Map<String, String> mapOf10Tables;
        try {
          mapOf10Tables = moneyService.lock10Tables(tableTotalInfo, tablePaiju, tableTeam,
              tableCurrentMoneyInfo, tableZijin, tableKaixiao, tableProfit, tableDangju,
              tableJiaoshou, tablePersonal, tablePingzhang, LMLabel);
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
          Map<String, String> lastDataDetailMap = dataConstants.All_Locked_Data_Map
              .get(ju_size2 + "");
          String lastDataDetailJson = JSON.toJSONString(lastDataDetailMap);

          dbUtil.saveLastLockedData(ju_size1, json_all_locked_data, ju_size2,
              lastDataDetailJson);// IO耗时长
        });

        // 保存当前Excel记录到数据库
        try {
          dbUtil.addGameRecordList(lmController.currentRecordList);
        } catch (Exception e) {
          ErrorUtil.err(e.getMessage(), e);
        }
        lmController.refreshClubList();
        // lmController.checkOverEdu(currentLMName);// 检查俱乐部额度
        // 检查共享额度
        //this.checkOverShareEdu();
        lmController.checkOverSharedEdu2(false);

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
    if (StringUtil.isBlank(getExcelPath()) || !srcFile.exists()) {
      logger.info("白名单路径 为空或者不存在，锁定时不进行Excel转移！");
      return;
    }
    try {
      String fileName = FileUtil.getFileName(resourceFilePath);
      String targetFilePath =
          PathUtil.getUserDeskPath() + getSoftDate() + "已锁定" + File.separator + fileName;
      FileUtils.moveFile(srcFile, new File(targetFilePath));
    } catch (Exception e) {
      ErrorUtil.err("转移Excel失败", e);
    }
  }


  // 缓存到总团队回水中(不会从中减少)
  public void setDangjuTeamInfo() {
    dataConstants.Dangju_Team_Huishui_List.forEach(info -> {
      String teamId = info.getTeamId();
      List<GameRecordModel> teamHuishuiList = dataConstants.Total_Team_Huishui_Map.get(teamId);
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
    if (paijuIndex == 0) {
      return totalProfit;
    }
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
    if (isHalfImport()) {
      return;
    }

    pageInput.setText("1");
    // 调用
    getResultByPage(1);
  }

  // 前一页按钮
  public void pagePreAction(ActionEvent event) {
    if (isHalfImport()) {
      return;
    }

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
    if (isHalfImport()) {
      return;
    }

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
    if (isHalfImport()) {
      return;
    }

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
    clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou, tablePingzhang,
        tablePersonal);
    // tableCurrentMoneyInfo,tableZijin,tableKaixiao,tableProfit
    indexLabel.setText(INDEX_ZERO);
  }

  // 恢复十个表数据
  public void reCovery10TablesByPage(int pageIndex) throws Exception {

    moneyService.reCovery10TablesByPage(tableTotalInfo, tablePaiju, tableTeam,
        tableCurrentMoneyInfo, tableZijin, tableKaixiao, tableProfit, tableDangju, tableJiaoshou,
        tablePingzhang, tablePersonal, LMLabel, pageIndex);
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
    spiderNode.setVisible(false);
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
    spiderNode.setVisible(true);
    tableCurrentMoneyInfo.setEditable(true);
    tableTeam.getColumns().get(5).setVisible(true);
    tableTeam.refresh();
    tablePaiju.getColumns().get(6).setVisible(true);
    tablePaiju.refresh();
    tableZijin.setEditable(true);
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
            new TypeReference<Map<String, List<GameRecordModel>>>() {
            });
        dataConstants.Total_Team_Huishui_Map = JSON.parseObject(maps.get("Total_Team_Huishui_Map"),
            new TypeReference<Map<String, List<GameRecordModel>>>() {
            });
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
        // 先删除数据库
        try {
          currentMoneyService.remove(new CurrentMoneyPK(info.getWanjiaId(), info.getMingzi()));
        } catch (Exception e) {
          e.printStackTrace();
        }
        // 删除实时金额表
        tableCurrentMoneyInfo.getItems().remove(index);
        tableCurrentMoneyInfo.refresh();
        moneyService.flush_SSJE_table();
        logger.info("手动修改实时金额数据记录(删除记录)：名称：{}, ID:{}, 实时金额：{}"
            , info.getMingzi(), StringUtils.defaultString(info.getWanjiaId(), "空"),
            StringUtils.defaultString(info.getShishiJine(), "空"));
      }
    } else {
      ShowUtil.show("请选中要删除的实时金额记录!");
      return;
    }
  }

  /**
   * 把渲染表格的数据加载出来，要判断是否2017-01-01，分成两步
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
            new TypeReference<Map<String, List<GameRecordModel>>>() {
            });
      }
    }
  }

  /**
   * 导出实时金额表
   *
   * @time 2017年10月28日
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
   * 导入空白表
   *
   * @time 2018年2月25日
   */
  @FXML
  public void importBlankExcelAction(ActionEvent event) {
    String tableId = RandomUtil.getRandomNumber(10000, 20000) + "";// 随机生成ID
    List<GameRecordModel> blankDataList = new ArrayList<>();
    // 存储数据 {场次=infoList...}
    dataConstants.zjMap.put(tableId, blankDataList);
    currentLMName = "联盟1";
    lmController.currentRecordList = new ArrayList<>();

    indexLabel.setText("第" + tableId + "局");
    importExcelData(tableId, blankDataList);

    importZJBtn.setDisable(true);// 导入不可用

    // 记录当局是否为小游戏
    ZhanjiType.getInstance().setBlankImport(indexLabel.getText());
    ShowUtil.show("导入空白战绩文件成功", 2);
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
   * 打开战绩excel
   */
  public void openZJExcelAction(ActionEvent event) {
    File file = myController.openBasicExcelDialog(excelDir, "请选择战绩Excel");
    if (file != null) {
      String tableId = FileUtil.getTableId(file.getAbsolutePath());
      indexLabel.setText(tableId);
    }
  }

  /**
   * 导入战绩后选择联盟
   */
  private String currentLMName = "";// 选择后不会被清空，用于检测额度是否超出


  /**
   * 导入战绩文件
   */
  public void importZJExcelAction(ActionEvent even) {
    String excelFilePath = getExcelPath();
    String LMName = "";
    if (StringUtil.isBlank(excelFilePath)) {
      ShowUtil.show("亲，你还未导入白名单呢！");
      return;
    }
    String userClubId = myController.getClubId();
    String tableId = FileUtil.getTableId(excelFilePath);
    if (StringUtil.isNotBlank(excelFilePath)) {
      File file = new File(excelFilePath);
      if (!file.exists()) {
        ShowUtil.show("检测到该Excel文件不存在，可能已经锁定后被转移了（桌号" + tableId + ")");
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
        currentLMName = "联盟1";
      } else {
        try {
          currentLMName = lmController.getLMByTableId(FileUtil.getPureTableId(excelFilePath));
          logger.info("根据桌号{}获取到所属联盟：{}", tableId, currentLMName);
        } catch (Exception e) {
          ShowUtil.show(tableId + "不在联盟范围内，请到联盟对帐页面修改！");
          return;
        }
//        selectLM();
//        if (StringUtil.isBlank(selected_LM_type)) {
//          String msg = "导入战绩时没有选择对应的联盟，场次：" + tableId + " Excel不准导入！";
//          ShowUtil.show(msg);
//          return;
//        }
      }
      if (StringUtil.isBlank(userClubId)
          || dataConstants.Index_Table_Id_Map.containsValue(tableId)) {
        ErrorUtil.err("该战绩表(" + tableId + "场次)已经导过");
        return;
      }

      try {
        // 将人员名单文件缓存起来
        List<GameRecordModel> gameRecordModels = excelReaderUtil
            .readZJRecord(excelFilePath, userClubId,
                currentLMName, myController.getVersionType());
        // 记录是否为小游戏
        recordZhanjiType(gameRecordModels);
        indexLabel.setText(tableId);
        // 填充数据
        importExcelData(tableId, gameRecordModels);

        importZJBtn.setDisable(true); // 导入按钮设置为不可用
        ShowUtil.show("导入战绩文件成功", 2);

      } catch (Exception e) {
        ErrorUtil.err("战绩导入失败", e);
      }
    }
  }

  private void recordZhanjiType(List<GameRecordModel> gameRecordModels) {
    if (CollectionUtils.isNotEmpty(gameRecordModels)) {
      GameRecordModel recordModel = gameRecordModels.get(0);
      boolean isGame = false;
      if (littleGameService.isLittleGame(recordModel)) {
        isGame = true;
      }
      ZhanjiType.getInstance().setZhanjiImport(isGame, recordModel.getTableid(), recordModel.getJutype());
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
//        selected_LM_type = "";
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

  // 第一次获取获取联盟对账（原始数据）
  public String getLMValFirstTime() {
    String lm = dataConstants.preDataMap.get("联盟对帐");
    if (StringUtils.isBlank(lm)) {
      return "0";
    }
    try {
      Double.valueOf(lm);
      return lm;
    } catch (Exception e) {
      Map<String, String> map = JSON.parseObject(lm, new TypeReference<Map<String, String>>() {
      });
      return moneyService.nvl(map.get("联盟对帐"), "0");
    }
  }

  public void initSpiderToggleAction() {
    spiderNode.selectedProperty().addListener(e -> {
      if (spiderNode.isSelected()) {
        smAutoController.startSpiderAction(new ActionEvent());
      } else {
        smAutoController.stopSpiderAction(new ActionEvent());
      }
    });
  }

  /**当
   * 打开新增实时金额对话框
   */
  public void openAddCurrentMoneyAction(ActionEvent event) {
    myController.openBasedDialog("add_current_money_frame4.fxml", "新增实时金额",
        Constants.ADD_CURRENT_MONEY_FRAME);
  }

  /**
   * 打开增加实时开销对话框
   */
  public void openKaixiaoDialogAction(ActionEvent event) {
    myController.openBasedDialog("add_kaixiao_fram.fxml", "新增开销", Constants.ADD_KAIXIAO_FRAME);
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
   * 当前是否为锁定页面 若为锁定页面，则联盟币购买时不进行修改实时金额表，避免修改了历史数据
   */
  public boolean isLockedView() {
    return lockedLabel.isVisible();
  }

  public boolean isEditingView() {
    return !isLockedView();
  }


  /**
   * 保存实时金额表到数据库
   */
  @FXML
  public void saveAllMoney2DBAction(ActionEvent event) {
    if (TableUtil.isNullOrEmpty(tableCurrentMoneyInfo)) {
      ShowUtil.show("实时金额表无数据，无须同步！");
      return;
    }
    if (FXUtil.confirm("同步金额", "同步所有金额到数据库，请先关闭自动爬取, 您是否要现在同步数据？")) {
      // TODO 添加遮罩层
      // 获取待覆盖到数据库中的数据
      List<CurrentMoney> datas =
          tableCurrentMoneyInfo.getItems().stream()
              .filter(e -> !StringUtils.isAllBlank(e.getWanjiaId(), e.getMingzi()))
              .map(e -> moneyService.change2CurrentMoney(e))
              .collect(Collectors.toList());
      //覆盖
      try {
        currentMoneyService.removeAll();
        currentMoneyService.save(datas);
        ShowUtil.show("同步成功！", 2);
      } catch (Exception e) {
        ErrorUtil.err("同步失败", e);
      }
    }
  }

  /**
   * 强制从数据库覆盖本地实时金额表
   */
  @FXML
  public void forceReplaceMoneyFromDBAction(ActionEvent event) {
    if (isLockedView()) {
      return;
    }
    if (FXUtil.confirm("强制修改", "请先关闭自动爬取, 即将从数据库强制覆盖所有金额到财务软件，您是否要现在要覆盖数据？")) {
      List<CurrentMoney> moneyDBList = currentMoneyService.getAll();
      if (CollectionUtils.isEmpty(moneyDBList)) {
        if (!FXUtil.confirm("数据库无数据，是否仍要覆盖？")) {
          return;
        }
      }
      List<CurrentMoneyInfo> items = new ArrayList<>();
      for (CurrentMoney entity : moneyDBList) {
        CurrentMoneyInfo item = new CurrentMoneyInfo();
        item.setCreator(MoneyCreatorEnum.DEFAULT.getCreatorName());
        item.setMingzi(entity.getName());
        item.setShishiJine(entity.getMoney());
        item.setWanjiaId(entity.getId());
        item.setCmiEdu(entity.getEdu());
        item.setColor("");
        item.setCmSuperIdSum(entity.getSum());
        item.setCmiLmb(entity.getLmb());
        items.add(item);
      }
      tableCurrentMoneyInfo.setItems(FXCollections.observableArrayList(items));
      tableCurrentMoneyInfo.refresh();
      // 刷新
      moneyService.flush_SSJE_table();
      ShowUtil.show("从数据库强制覆盖本地实时金额表完成", 2);
    }
  }


  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


  /**
   * 战绩类型
   */
  public static class ZhanjiType{

    Logger logger = LoggerFactory.getLogger(ZhanjiType.class);

    private  boolean isLittleGame = false;
    private  String tableId ;
    private  String gameName;

    private static ZhanjiType instance = new ZhanjiType();

    public static ZhanjiType getInstance(){
      return instance;
    }

    private ZhanjiType() {
    }

    public void setBlankImport(String tableId){
      this.isLittleGame = false;
      this.tableId = tableId;
      this.gameName = "";
    }

    public void setZhanjiImport(boolean isLittleGame, String tableId, String gameName){
      this.isLittleGame = isLittleGame;
      this.tableId = tableId;
      this.gameName = gameName;
      if (logger.isInfoEnabled()) {
        logger.info("{}是{}, 类型是：{}", tableId, isLittleGame? "小游戏" : "普通局" , gameName );
      }
    }

    public boolean isLittleGame() {
      return isLittleGame;
    }

    public void setLittleGame(boolean littleGame) {
      isLittleGame = littleGame;
    }

    public String getTableId() {
      return tableId;
    }

    public void setTableId(String tableId) {
      this.tableId = tableId;
    }

    public String getGameName() {
      return gameName;
    }

    public void setGameName(String gameName) {
      this.gameName = gameName;
    }
  }


}
