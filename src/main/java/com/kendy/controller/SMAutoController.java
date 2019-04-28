package com.kendy.controller;

import com.kendy.db.entity.Player;
import com.kendy.db.service.CurrentMoneyService;
import com.kendy.db.service.PlayerService;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.enums.ExcelAutoDownType;
import com.kendy.model.CoinBuyerRusult;
import com.kendy.model.CoinBuyerRusult.CoinBuyer;
import com.kendy.model.RangeResult;
import com.kendy.model.RealBuyResult;
import com.kendy.model.Result;
import com.kendy.util.HttpUtils;
import com.kendy.util.HttpUtils.HttpResult;
import com.kendy.util.NumUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.DataConstans;
import com.kendy.db.DBUtil;
import com.kendy.entity.SMAutoInfo;
import com.kendy.entity.ShangmaInfo;
import com.kendy.enums.KeyEnum;
import com.kendy.excel.ExportExcelTemplate;
import com.kendy.model.GameRoomModel;
import com.kendy.model.RespResult;
import com.kendy.model.SMResultModel;
import com.kendy.service.AutoDownloadZJExcelService;
import com.kendy.service.JifenService;
import com.kendy.service.MemberService;
import com.kendy.service.MoneyService;
import com.kendy.service.ShangmaService;
import com.kendy.service.TGExportExcelService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.TgWaizhaiService;
import com.kendy.util.AlertUtil;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FilterUtf8mb4;
import com.kendy.util.MapUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.TimeUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * 自动上码控制器
 *
 * @author 林泽涛
 * @time 2017年11月24日 下午9:31:04
 */
@Component
public class SMAutoController extends BaseController implements Initializable {

  private Logger log = Logger.getLogger(SMAutoController.class);
  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public MyController myController;
  @Autowired
  public CombineIDController combineIDController;
  @Autowired
  public BaseController baseController; // 基本控制类
  @Autowired
  public JifenService jifenService; // 积分控制类
  @Autowired
  public MemberService memberService; // 配帐控制类
  @Autowired
  public ShangmaService shangmaService; // 上码控制类
  @Autowired
  public TeamProxyService teamProxyService; // 配帐控制类
  @Autowired
  public TGExportExcelService tGExportExcelService; // 配帐控制类
  @Autowired
  public AutoDownloadZJExcelService autoDownloadZJExcelService; // 配帐控制类
  @Autowired
  public TgWaizhaiService tgWaizhaiService; // 外债控制类
  @Autowired
  public MoneyService moneyService; // 配帐控制类
  @Autowired
  public DataConstans dataConstants; // 数据控制类

  @Autowired
  ChangciController changciController;

  @Resource
  CurrentMoneyService currentMoneyService;
  
  @Resource
  PlayerService playerService;

  @FXML
  public TextField smNextDayRangeFieldd; // 次日上码配置

  @FXML
  public TextArea tokenArea;// token值
  @FXML
  public Label tokenStatus; // token状态
  @FXML
  public ListView<String> logArea;
  @FXML
  public ListView<String> excelArea;
  @FXML
  public TextField sysCodeField;
  @FXML
  public TextField sperateTimeField;// 每隔多久去刷新
  @FXML
  public TextField filterPlayIdFields;// 上码过过滤人员（只给这些人上码）

  // =====================================================================自动上码日志记录表
  @FXML
  public TableView<SMAutoInfo> tableSMAuto;
  @FXML
  private TableColumn<SMAutoInfo, String> smAutoDate;
  @FXML
  private TableColumn<SMAutoInfo, String> smAutoTeamId;
  @FXML
  private TableColumn<SMAutoInfo, String> smAutoPlayerId;
  @FXML
  private TableColumn<SMAutoInfo, String> smAutoPlayerName;
  @FXML
  private TableColumn<SMAutoInfo, String> smAutoApplyAccount;
  @FXML
  private TableColumn<SMAutoInfo, String> smAutoIsAgree;
  @FXML
  private TableColumn<SMAutoInfo, String> smAutoIsAgreeSuccess;

  @FXML
  public TextField downExcelPierodField;// 每隔多久去刷新

  Charset charset = null;

  HttpUtils httpUtils = HttpUtils.getInstance();

  private final String SM_AOTO_NEXT_DAY_DB_KEY =
      KeyEnum.SM_AOTO_NEXT_DAY_DB_KEY.getKeyName(); // 保存到数据库的key
  private final String SM_AOTO_TOKEN_DB_KEY = KeyEnum.SM_AOTO_TOKEN_DB_KEY
      .getKeyName(); // 保存到数据库的key

  private final String CONNECT_FAIL = "连接失败,失败码：";

  private Object lock = new Object();

  public Map<String, Boolean> downloadCache = new ConcurrentHashMap<>();

  private Timer timer;

  private Timer excelTimer;

  {
    log.info("软件新开启：加载自动下载数据");
    loadDownLoadCache();
  }

  public void loadDownLoadCache() {
    File downLoadFileFolder =
        new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "\\"
            + LocalDate.now().toString());
    if (downLoadFileFolder.exists()) {
      for (File downLoadFile : downLoadFileFolder.listFiles()) {
        String fileName = downLoadFile.getName();
        if (fileName.endsWith(".xls")) {
          downloadCache.put(fileName, Boolean.TRUE);
        }
      }
      log.info("软件新开启：加载自动下载数据,个数：" + downLoadFileFolder.listFiles().length);
    }
  }

  /**
   * DOM加载完后的事件
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 换行
    tokenArea.setWrapText(true);
    // 绑定列值属性
    bindCellValueByTable(new SMAutoInfo(), tableSMAuto); // 自动上码待观察
    // MyController.bindCellValue(smAutoDate, smAutoPlayerId, smAutoPlayerName, smAutoPaiju,
    // smAutoApplyAccount,
    // smAutoIsTeamAvailabel, smAutoIsCurrentDay, smAutoIsNextDay, smAutoIsAgree,
    // smAutoIsAgreeSuccess,
    // smAutoAvailabel, smAutoTeamTotalAvailabel);
    // 初始化次日上码范围
    initNextDayRange();

    loadTokenAction(new ActionEvent());

    charset = Charset.forName(sysCodeField.getText());
  }

  /**
   * 初始化次日上码范围
   *
   * @time 2018年3月28日
   */
  private void initNextDayRange() {
    String range = dbUtil.getValueByKeyWithoutJson(SM_AOTO_NEXT_DAY_DB_KEY);
    if (StringUtil.isNotBlank(range)) {
      smNextDayRangeFieldd.setText(range);
    } else {
      smNextDayRangeFieldd.setText("01-90##8001-8090##9001-9090");
    }
  }

  /**
   * 加载次日上码范围
   *
   * @time 2018年3月31日
   */
  public void loadNextDayAction(ActionEvent event) {
    initNextDayRange();
  }


  /**
   * 保存Token
   *
   * @time 2018年3月31日
   */
  public void saveTokenAction(ActionEvent event) {
    String token = tokenArea.getText();
    if (StringUtil.isNotBlank(token)) {
      dbUtil.saveOrUpdateOthers(SM_AOTO_TOKEN_DB_KEY, token.trim());
      ShowUtil.show("保存OK", 1);
    } else {
      ShowUtil.show("别逗，token啥都没写，你就要保存?", 1);
    }
  }

  /**
   * 加载Token
   *
   * @time 2018年3月31日
   */
  public void loadTokenAction(ActionEvent event) {
    String token = dbUtil.getValueByKeyWithoutJson(SM_AOTO_TOKEN_DB_KEY);
    if (StringUtil.isNotBlank(token)) {
      tokenArea.setText(token);
      ShowUtil.show("加载完成", 1);
    }
  }

  /**
   * 测试token
   *
   * @time 2018年3月26日
   */
  public void tokenTestAction(ActionEvent event) {
    logInfo("正在检测token...");
    String token = getToken();
    if (StringUtil.isBlank(token)) {
      String message = "token值不能为空！！";
      ShowUtil.show(message);
      logInfo(message);
      return;
    }
    CoinBuyerRusult coinBuyerResult = getCoinBuyerResult();

    if (coinBuyerResult == null || coinBuyerResult.getResult() == null) {
      String msg = "接口没返回数据";
      if (coinBuyerResult != null) {
        msg += ", 返回码： " + coinBuyerResult.getiErrCode();
      }
      setTokenStatusFail(msg);
      return;
    }
    if (coinBuyerResult.getiErrCode() != 0) {
      setTokenStatusFail(coinBuyerResult.getiErrCode() + "");
      return;
    }
    setTokenStatusSuccess();
  }

  private void setTokenStatusFail(String description) {
    String message = CONNECT_FAIL + description;
    if ("1000".equals(description) || "1001".equals(description)) {
      message += ",请更新Token";
      ShowUtil.show("请更新Token", 1);
    }
    tokenStatus.setText(message);
    logInfo(message);
  }

  private void setTokenStatusSuccess() {
    String message = "token连接正常";
    tokenStatus.setText(message);
    logInfo(message);
  }

  /**
   * 记录日志
   *
   * @time 2018年3月26日
   */
  private void logInfo(String description) {
    synchronized (lock) {
      ObservableList<String> items = logArea.getItems();
      if (items != null) {
        items.add(description);
      } else {
        items = FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList(" ")));
      }
      logArea.refresh();
      log.info(description);
    }
  }

  /**
   * 清空右边日志框内容ListView
   *
   * @time 2018年3月26日
   */
  public void removeLogAreaAction(ActionEvent event) {
    if (logArea.getItems() != null) {
      logArea.getItems().clear();
    }
  }

  /**
   * 获取token
   *
   * @time 2018年3月26日
   */
  public String getToken() {
    return StringUtil.nvl(tokenArea.getText(), "");
  }

  /**
   * 保存次日上码的配置
   *
   * @time 2018年3月26日
   */
  public void saveNextDayConfigAction(ActionEvent evet) {
    String smNextDayRange = smNextDayRangeFieldd.getText();
    if (StringUtil.isNotBlank(smNextDayRange)) {
      dbUtil.saveOrUpdateOthers(SM_AOTO_NEXT_DAY_DB_KEY, smNextDayRange);
      ShowUtil.show("保存OK", 1);
    } else {
      ShowUtil.show("别逗，啥都没写，你就要保存?", 1);
    }
  }


  /**
   * 开始爬取后台数据
   *
   * @time 2018年3月26日
   */
  public void startSpiderAction(ActionEvent evet) {
    if (this.timer != null) {
      ShowUtil.show("后台程序正在运行！", 1);
      return;
    }
    Integer separateTime = getRefreshSeparateTime();
    tokenStatus.setText("正在爬取数据...");
    logInfo("每隔" + separateTime / 1000 + "秒刷新一次！");
    logInfo("开始爬取程序...");
    this.timer = new Timer();
    timer.schedule(new TimerTask() {
      public void run() {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            // 自动上码
            if (changciController.isEditingView()) {
              reqAndHandleBuyinList();
            } else {
              logInfo("用户正在查看历史数据，程序不进行爬取，正在等待下一个任务...");
            }
          }
        });
        // logInfo("此处等待10秒...");
      }
    }, 1000, separateTime); // 定时器的延迟时间及间隔时间
    if (!changciController.spiderNode.isSelected()) {
      changciController.spiderNode.setSelected(true);
    }
  }


  private CoinBuyerRusult getCoinBuyerResult() {
    charset = Charset.forName(sysCodeField.getText());
    Map<String, String> params = new HashMap<>();
    Map<String, String> header = getHeader();
    String url = "http://cms.pokermanager.club/cms-api/leaguecredit/getLeagueCoinApplyList";
    CoinBuyerRusult coinBuyerRusult = null;
    try {
      HttpResult result = httpUtils.formPost(url, charset, params, header);
      if (result.isOK()) {
        String content = result.getContent();
        logger.info("获取联盟币玩家原始数据 = " + content);
        try {
          coinBuyerRusult = JSON.parseObject(content, CoinBuyerRusult.class);
        } catch (Exception e) {
          logInfo("JSON转换失败: " + e.getMessage() + ",原始返回：" + content);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      String errMesg = "自动获取联盟币玩家请求失败,异常：" + e.getMessage();
      logInfo(errMesg);
      log.error(errMesg, e);
    }
    return coinBuyerRusult;
  }

  public void reqAndHandleBuyinList() {
    logInfo("正在获取玩家信息..." + TimeUtil.getTimeString());
    CoinBuyerRusult coinBuyerRusult = getCoinBuyerResult();
    if (coinBuyerRusult == null || coinBuyerRusult.getiErrCode() != 0) {
      String errMsg = "获取联盟币玩家列表返回错误码：" + coinBuyerRusult.getiErrCode();
      logInfo(errMsg);
      logger.error(errMsg);
      return;
    }
    List<CoinBuyer> coinBuyers = coinBuyerRusult.getResult();
    if (coinBuyers != null) {
      // 只记录发放，过滤回收  mstType:1是发送， 2是回收
      coinBuyers = coinBuyers.stream().filter(e->1==e.getMsgType()).collect(Collectors.toList());
      logInfo("获取联盟币玩家个数（发放）：" + coinBuyers.size());

      // 真正的处理逻辑
      if (CollectionUtils.isNotEmpty(coinBuyers)) {
        handleAutoShangma(coinBuyers);
      }
    }

    logInfo("");
  }


  private Integer getRefreshSeparateTime() {

    String text = sperateTimeField.getText();
    Integer time = 20;
    try {
      time = Integer.valueOf(text.trim());
    } catch (Exception e) {
      time = 20;
    }
    return time * 1000;
  }

  /**
   * 处理自动上码的逻辑（核心代码） 备注：申请数量过两道关之后程序会去实时上码Tab中自动上码
   * <p>
   * { nickName : 堪睇系唔使靓仔 strCover : http://info.pokermate.net/data/2018/5/26/775587_small_1527328738875.png
   * showId : 1871535391 grantNums : 500.0 remarks : msgType : 1 uniqueId : 11912 }
   * </p>
   */
  public synchronized void handleAutoShangma(List<CoinBuyer> coinBuyers) {

    for (CoinBuyer coinBuyer : coinBuyers) {
      try {
        // 玩家相关信息
        String playerName = coinBuyer.getNickName();
        String playerId = coinBuyer.getShowId();
        String buyStack = coinBuyer.getGrantNums(); // 购买数量

        // 联盟币购买条件判断
        RangeResult checkRangeResult = checkInRange(coinBuyer);
        boolean passCheck = checkRangeResult.isInRange(); // 是否合范围
        logInfo(playerName + "是否合范围：" + (passCheck ? "是" : "否"));
        if (!passCheck) {
          addBuyResultRecord(checkRangeResult);
          continue;
        }

        // 联盟币购买结果
        boolean buyLmbOk = false;
        int buyLmbCode = -1;
        List<String> testList = new ArrayList<>();

        if (hasFilterPlayerIds()) {
          testList = Arrays.stream(filterPlayIdFields.getText().trim().split("##"))
              .collect(Collectors.toList());
        }

        if (CollectUtil.isEmpty(testList) || testList.contains(playerId)) {
          // 向后台申请购买， 成功后修改实时金额表
          buyLmbCode = buyLmbCoin(coinBuyer);
          checkRangeResult.setBuyCode(buyLmbCode);
          buyLmbOk = (buyLmbCode == 0);
          if (buyLmbOk) {
            // 金额修改
            checkRangeResult.setBuyOK(true);
            logInfo(playerName + "购买成功，正在模拟更新实时金额...");
            CurrentMoneyInfo cmi = checkRangeResult.getCmi();
            // 设置实时金额
            String finalSSJE = NumUtil
                .digit0(NumUtil.getNum(cmi.getShishiJine()) - NumUtil.getNum(buyStack));
            cmi.setShishiJine(finalSSJE);
            // 设置联盟币
            String finalLmb = NumUtil.digit2(NumUtil.getNum(cmi.getCmiLmb()) + NumUtil.getNum(buyStack) + "");
            cmi.setCmiLmb(finalLmb);

            // 保存到数据库
            moneyService.saveOrUpdate2DB(cmi);

            // 保存到实时金额表
            changciController.tableCurrentMoneyInfo.refresh();
            logInfo(playerName + "将实时金额" + cmi.getShishiJine() + "修改为" + finalSSJE);

          } else {
            checkRangeResult.setBuyOK(false);
          }
        }
        // 记录
        addBuyResultRecord(checkRangeResult);

      } catch (Exception e) {
        logInfo("玩家【" + coinBuyer.getNickName() + "】自动购买联盟币报错，请看日志！！");
        logger.error("处理玩家【{}】自动购买联盟币报错，原因：{}", coinBuyer.getNickName(), e);
      }
    }
  }

  private void addBuyResultRecord(RangeResult rangeRsult) {
    SMAutoInfo smAutoInfo = new SMAutoInfo(getTimeString(),
        rangeRsult.getTeamId(),
        rangeRsult.getPlayerId(),
        rangeRsult.getPlayerName(),
        rangeRsult.getBuyStack(),
        rangeRsult.getCurrentMoney(),
        rangeRsult.getAvailableEdu(),
        rangeRsult.isInRange() ? "是" : "否",
        (rangeRsult.isInRange()) ? (rangeRsult.isBuyOK() ? "成功" : "失败") : "-",
        getFailDescription(rangeRsult, rangeRsult.getBuyCode())
    );
    addItem(smAutoInfo);
  }

  private String getFailDescription(Result checkRangeResult, int buyLmbCode) {
    if (!checkRangeResult.isInRange()) {
      return "不足额度";
    }
    if (!checkRangeResult.isBuyOK()) {
      if (buyLmbCode == 3003) {
        return "后台联盟币不足";
      }
    }
    return buyLmbCode + "";
  }

  /**
   * 向后台申请购买联盟币
   */
  public int buyLmbCoin(CoinBuyer coinBuyer) {
    charset = Charset.forName(sysCodeField.getText());
    String playerName = coinBuyer.getNickName();
    String playerId = coinBuyer.getShowId();
    String buyStack = coinBuyer.getGrantNums() + "";
    String uniqueId = coinBuyer.getUniqueId() + "";
    int msgType = coinBuyer.getMsgType();
    String buyType = msgType == 1 ? "买入" : "回购";
    String ACCEPT_BUY_URL = "http://cms.pokermanager.club/cms-api/leaguecredit/acceptLeagueCoinApply";

    Map<String, String> params = new HashMap<>();
    params.put("uniqueId", uniqueId);
    params.put("userShowId", playerId);
    Map<String, String> header = getHeader();
    String result = "";
    try {
      HttpResult httpResult = httpUtils.formPost(ACCEPT_BUY_URL, charset, params, header);
      result = httpResult.getContent();
      log.info("");
      if (httpResult.isOK()) {
        if (StringUtil.isNotBlank(result)) {
          RealBuyResult realBuyResult = JSON.parseObject(result, RealBuyResult.class);

          boolean buyLmbOk = StringUtils.equals("0", realBuyResult.getIErrCode()) &&
              StringUtils.equals("0", realBuyResult.getResult());

          String coinBuyStr = (buyLmbOk ? "成功" : "失败");
          logInfo(String.format("%s[%s]申请%s数量%s,联盟币购买结果是[%s], 后台返回：%s",
              playerName, playerId, buyType, buyStack, coinBuyStr, result));

          return Integer.valueOf(realBuyResult.getIErrCode());
        }
      }
    } catch (Exception e) {
      String errMsg = String.format("玩家【%s】,玩家ID【%s】,购买联盟币%s网络异常%s, 后台返回：%s",
          playerName, playerId, buyStack, e.getMessage(), result);
      logInfo(errMsg);
      logger.error(errMsg, e);
    }
    return -1;
  }


  private void throwException(String playerId, String buyStack, Throwable e) {
    logger.error("玩家ID【%s】购买联盟币%s网络异常", playerId, buyStack, e);
    throw new RuntimeException(e.getMessage());
  }

  private Map<String, String> getHeader() {
    Map<String, String> header = new HashMap<>();
    header.put("token", getToken());
    header.put("Accept", "application/json;charset=UTF-8");
    header.put("X-Requested-With", "XMLHttpRequest");
    header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    return header;
  }

  /**
   * 判断a 是否在区间范围[b,c]
   */
  private RangeResult checkInRange(CoinBuyer coinBuyer) throws Exception {
    // 玩家相关信息
    String playerName = coinBuyer.getNickName();
    String playerId = coinBuyer.getShowId();
    String buyStack = coinBuyer.getGrantNums(); // 购买数量
    Player player = dataConstants.membersMap.get(playerId);
    if (player == null) {
      return new RangeResult("玩家不存在", playerId,
          playerName, "-", buyStack, "-", "-", null);
    }
    if (changciController.isLockedView()) {
      return new RangeResult("锁定页面", playerId,
          playerName, "-", buyStack, "-", "-", null);
    }
    CurrentMoneyInfo cmi = moneyService.getInfoById(playerId);
    if (cmi == null) {
      return new RangeResult("金额表不存在", playerId,
          playerName, "-", buyStack, "-", "-", null);
    }
    String parentId = combineIDController.hasCombineIdRelation(playerId);
    if (StringUtils.isBlank(parentId)) {
      // 没有合并关系
      return checkInRangeWithNonbineRalation(player, cmi, buyStack);

    } else {
      // 有合并关系
      return checkInRangeWithConbineRalation(parentId, player, cmi,  buyStack);
    }
  }

  /**
   * 校验是否同意购买（无合并关系）
   * @param player
   * @param cmi
   * @param buyStack
   * @return
   */
  private RangeResult checkInRangeWithNonbineRalation(Player player, CurrentMoneyInfo cmi,
      String buyStack) {
    String ssje = cmi.getShishiJine();
    String edu = cmi.getCmiEdu();
    if (checkPass(ssje, edu, buyStack)) {
      return new RangeResult(true, "", cmi.getWanjiaId(),
          player.getPlayername(), player.getTeamid(), buyStack, ssje, edu, cmi);
    }
    return new RangeResult(false,  "拦截不足币", cmi.getWanjiaId(),
        player.getPlayername(), player.getTeamid(), buyStack, ssje, edu, null);
  }


  /**
   * 校验是否同意购买（有合并关系）
   * @param player
   * @param buyStack
   * @return
   */
  private RangeResult checkInRangeWithConbineRalation(String parentId, Player player, CurrentMoneyInfo currentCmi, String buyStack) {
    CurrentMoneyInfo parentCmi = moneyService.getInfoById(parentId);
    if (parentCmi == null) {
      return new RangeResult(false, "父ID不存在", player.getPlayerid(),
          player.getPlayername(), "-", buyStack, "-", "-", null);
    }
    List<CurrentMoneyInfo> cmiList = new ArrayList<>();
    cmiList.add(parentCmi);
    for (String subId : dataConstants.Combine_Super_Id_Map.get(parentId)) {
      CurrentMoneyInfo _cmi = moneyService.getInfoById(subId);
      if (_cmi != null) {
        cmiList.add(_cmi);
      }
    }
    double ssje = 0;
    double edu = 0;
    for (CurrentMoneyInfo cmi : cmiList) {
      ssje += NumUtil.getNum(cmi.getShishiJine());
      if (edu < NumUtil.getNum(cmi.getCmiEdu())) {
        edu = NumUtil.getNum(cmi.getCmiEdu());
      }
    }
    if (checkPass(ssje + "", edu + "", buyStack)) {
      return new RangeResult(true, "", player.getPlayerid(),
          player.getPlayername(), player.getTeamid(), buyStack, NumUtil.digit0(ssje), NumUtil.digit0(edu), currentCmi);
    }
    return new RangeResult(false, "拦截不足币", player.getPlayerid(),
        player.getPlayername(), player.getTeamid(), buyStack, NumUtil.digit0(ssje), NumUtil.digit0(edu), null);

  }

  private boolean checkPass(String ssje, String edu, String buyStack) {
    return (NumUtil.getNum(ssje) + NumUtil.getNum(edu) - NumUtil.getNum(buyStack) > 0);
  }


  /**
   * 自动上码时获取玩家的可上码总接口 包括各种情况（如已勾选团队上码
   *
   * @time 2018年3月27日
   */
//  private String getAvailable(SMResultModel resultModel, String selectTeamAvailabel,
//      String playerId, String playerName) {
//    List<ShangmaInfo> smList = resultModel.getSmList();
//    String available = "0";
//    // 勾选了团队上码
//    if ("1".equals(selectTeamAvailabel)) {
//      return "0";
//    } else {
//      // 以下是没有勾选团队上码的情况
//      boolean personNode = not_supter_not_sub(playerId);
//      if (personNode) {
//        // 情况1：私人节点ID
//        available = getOnePersonAvailabel(smList, playerId);
//      } else {
//        // 情况2：有联合ID
//        available = getLianheAvailabel(smList, playerId);
//      }
//
//    }
//    return available;
//  }

  /**
   * 联合额度
   *
   * @time 2018年3月27日
   */
//  public String getLianheAvailabel(List<ShangmaInfo> smList, String playerId) {
//    String superId = dataConstants.Combine_Super_Id_Map.containsKey(playerId) ? playerId
//        : dataConstants.Combine_Sub_Id_Map.get(playerId);
//
//    if (StringUtil.isBlank(superId)) {
//      log.error(playerId + "非父非子！");
//      return "0";
//    }
//
//    String availaibel = smList.stream().filter(info -> superId.equals(info.getShangmaPlayerId()))
//        .map(ShangmaInfo::getShangmaLianheEdu).findFirst().orElseGet(() -> "0");
//
//    return availaibel;
//  }

  /**
   * 私人可上码（非父非子ID）
   *
   * @time 2018年3月27日
   */
//  public String getOnePersonAvailabel(List<ShangmaInfo> smList, String playerId) {
//    String availaibel = smList.stream().filter(info -> playerId.equals(info.getShangmaPlayerId()))
//        .map(ShangmaInfo::getShangmaAvailableEdu).findFirst().orElseGet(() -> "0");
//    if (StringUtil.isBlank(availaibel)) {
//      availaibel = "000000";
//    }
//    return availaibel;
//  }

  /**
   * 往日志表添加记录
   *
   * @time 2018年3月27日
   */
  public void addItem(SMAutoInfo smAutoInfo) {
    if (TableUtil.isNullOrEmpty(tableSMAuto)) {
      tableSMAuto.setItems(FXCollections.observableArrayList());
    }
    tableSMAuto.getItems().add(smAutoInfo);

    tableSMAuto.refresh();
  }

  /**
   * 停止爬取后台数据
   *
   * @time 2018年3月26日
   */
  public void stopSpiderAction(ActionEvent evet) {
    if (this.timer == null) {
      ShowUtil.show("后台程序没有启动", 1);
      return;
    }
    this.timer.cancel();
    this.timer = null;
    logInfo("停止爬取后台数据!!!");
    tokenStatus.setText("暂停爬取数据...");
    if (changciController.spiderNode.isSelected()) {
      changciController.spiderNode.setSelected(false);
    }
  }

  /**
   * 清空界面日志表
   *
   * @time 2018年3月26日
   */
  public void clearTableDataAction(ActionEvent evet) {
    TableUtil.clear(tableSMAuto);

  }

//  /**
//   * 判断是否私人节点（非父非子）
//   *
//   * @time 2018年3月31日
//   */
//  private boolean not_supter_not_sub(String playerId) {
//    boolean isSuperId = dataConstants.Combine_Super_Id_Map.containsKey(playerId);
//    boolean isSubId = dataConstants.Combine_Sub_Id_Map.containsKey(playerId);
//    return !isSuperId && !isSubId;
//  }

  /**
   * 是否有过滤节点
   *
   * @time 2018年3月31日
   */
  private boolean hasFilterPlayerIds() {
    return StringUtil.isNotBlank(filterPlayIdFields.getText());
  }

  /**
   * 导出有上码的记录 TODO 封装一个可以通过SimpleStringProperty导出的
   *
   * @time 2018年3月31日
   */
  public void exportSMAction(ActionEvent event) {
    List<SMAutoInfo> autoShangmas = getAutoShangmas();
    if (CollectUtil.isEmpty(autoShangmas)) {
      ShowUtil.show("没有可供导出的数据！");
      return;
    }
    String[] rowsName = new String[]{"爬取时间", "团队ID", "玩家ID", "玩家名称", "申请数量", "实时金额", "可用额度",
        "同意购买", "购买结果", "备注"};
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (SMAutoInfo info : autoShangmas) {
      objs = new Object[rowsName.length];
      objs[0] = info.getSmAutoDate();
      objs[1] = info.getSmAutoTeamId();
      objs[2] = info.getSmAutoPlayerId();
      objs[3] = info.getSmAutoPlayerName();
      objs[4] = info.getSmAutoApplyAccount();
      objs[5] = info.getSmAutoCurrentMoney();
      objs[6] = info.getSmAutoAvailabelEdu();
      objs[7] = info.getSmAutoIsAgree();
      objs[8] = info.getSmAutoIsAgreeSuccess();
      objs[9] = info.getSmAutoFailDescription();
      dataList.add(objs);
    }
    String title = "自动购买联盟币-" + TimeUtil.getDateTime();
    List<Integer> columnWidths =
        Arrays.asList(3500, 3500, 4000, 5000, 3000, 3000, 4000, 4000, 3000, 3000, 3000, 3000, 3000,
            5000, 5000);
    ExportExcelTemplate ex = new ExportExcelTemplate(title, rowsName, columnWidths, dataList);
    try {
      ex.export();
      ShowUtil.show("导出完成", 1);
    } catch (Exception e) {
      ErrorUtil.err("导出自动购买联盟币记录失败", e);
      e.printStackTrace();
    }
  }


  /**
   * 获取相应的自动上码记录
   *
   * @time 2018年3月31日
   */
  @SuppressWarnings("unchecked")
  public List<SMAutoInfo> getAutoShangmas() {
    if (TableUtil.isHasValue(tableSMAuto)) {
      ObservableList<SMAutoInfo> items = tableSMAuto.getItems();
      return items.stream().filter(info -> "成功".equals(info.getSmAutoIsAgreeSuccess()))
          .collect(Collectors.toList());
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * 测试模式时加载六个人
   *
   * @time 2018年3月31日
   */
  public void load6PlayerIdsAction(ActionEvent event) {
    filterPlayIdFields
        .setText("2162968366##2162813955##2162971465##2162864256##2162932597##2162892097");
  }

  public String getTimeString() {
    SimpleDateFormat format = new SimpleDateFormat("HH : mm : ss");
    String timeStr = format.format(new Date());
    return timeStr;
  }


  /************************************************************************************************
   *
   * 自动下载区域<a href=> http://cms.pokermanager.club/cms-api/game/exportGame?roomId=28739668&token=...
   *
   *************************************************************************************************/
  @FXML
  private DatePicker datePicker; // dateLabel.setText(dataConstants.Date_Str);
  @FXML
  private TextField firstDayStartTimeField;
  @FXML
  private TextField secondDayEndTimeField;

  private final String EN_MH = ":";
  private final String CN_MH = "：";
  private final int DOWN_LIMIT = 10; // 每次下载5个，总共一次性下载 6 * 3 = 18

  public LocalDate getSelectedDate() {
    return datePicker.getValue();
  }

  /**
   * 获取第一天的时间戳
   */
  public String getFirstDayStartMillTime() {
    String timeStr = firstDayStartTimeField.getText();
    return getFinalTimeStr(timeStr, 1); // 1表示start, 表示第一天
  }

  /**
   * 获取第二天的时间戳(加一天)
   */
  public String getSecondDayEndMillTime() {
    String timeStr = secondDayEndTimeField.getText();
    return getFinalTimeStr(timeStr, 2); // 2表示end, 表示第二天
  }

  private boolean isUnValidTime(String timeStr) {
    boolean isUnValid = true;
    if (StringUtil.isBlank(timeStr) && timeStr.length() != 5) {
      return isUnValid;
    }
    if (timeStr.contains(EN_MH) || timeStr.contains(CN_MH)) {
      isUnValid = false;
    }
    return isUnValid;
  }

  /**
   * 检查时间有效性 包括选择日期，开始时间和结束时间
   *
   * @time 2018年5月4日
   */
  private boolean checkTime() {
    LocalDate selectDate = datePicker.getValue();
    String starttime = firstDayStartTimeField.getText();
    String endtime = secondDayEndTimeField.getText();
    return selectDate != null && !isUnValidTime(starttime) && !isUnValidTime(endtime);
  }

  /**
   * 获取时间戳
   *
   * @param timeStr 有效的时：分， 如10：00，中文冒号和英文冒号都支持
   * @time 2018年5月4日
   */
  private String getFinalTimeStr(String timeStr, int type) {
    timeStr = timeStr.replace(CN_MH, EN_MH);
    if (type == 1) {
      timeStr = getSelectedDate().toString() + " " + timeStr + ":00";
    } else {
      timeStr = getSelectedDate().plusDays(1).toString().substring(0, 10) + " " + timeStr + ":00";
    }
    LocalDateTime dateTime = LocalDateTime.parse(timeStr, TimeUtil.sdf);
    long epochMilli = dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    return epochMilli + "";
  }


  // 软件启动时先去加载桌面已经下载的Excel列表进缓存
  // 重复的跳过，不重复的则下载后更新缓存和数据
  public void autoDownExcels(String downType) {

    String houtai = ExcelAutoDownType.getNambeByValue(downType);

    RespResult<GameRoomModel> parseObject = new RespResult<>();
    try {
      excelInfo("正在获取" + houtai + "房间列表..." + TimeUtil.getTimeString());
      Map<String, String> params = getParams(downType);

      try {
        HttpResult httpResult = httpUtils.formPost(
            "http://cms.pokermanager.club/cms-api/game/getHistoryGameList", charset,
            params, getHeader());
        if (httpResult.isOK()) {
          String content = httpResult.getContent();
          if (StringUtil.isNotBlank(content)) {
            parseObject = JSON.parseObject(content,
                new TypeReference<RespResult<GameRoomModel>>() {
                });
            excelInfo(houtai + "房间数量：" + parseObject.getResult().getTotal());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      String errMsg = "获取房间列表：联盟币异常...";
      excelInfo(errMsg + e.getMessage());
      return;
    }

    if (parseObject.getiErrCode() > 0) {
      excelInfo("联盟币异常，后台返回码：" + parseObject.getiErrCode());
      return;
    }

    boolean hasRoomValue = parseObject.getResult().getList() != null;
    if (hasRoomValue) {
      List<GameRoomModel> roomList = parseObject.getResult().getList();
      List<GameRoomModel> updatedList = updatedList(roomList);
      boolean isAllDown = updatedList.isEmpty();
      if (isAllDown) {
        excelInfo(houtai + "无更新");
        return;
      } else {
        excelInfo(houtai + "更新了" + updatedList.size() + "个白名单");
      }
      // 排序， 前6个
      updatedList = updatedList.stream().limit(DOWN_LIMIT)
          .sorted(
              (m, n) -> Long.valueOf(m.getCreatetime()).compareTo(Long.valueOf(n.getCreatetime())))
          .collect(Collectors.toList());
      String token = getToken();
      for (GameRoomModel gameRoomModel : updatedList) {
        String fileName =
            getDownLoadFilterName(gameRoomModel.getCreatetime(), gameRoomModel.getRoomname());
        if (downloadCache.containsKey(fileName)) {
          if (log.isDebugEnabled()) {
            log.info("跳过：" + fileName);
          }
          continue;
        }

        String roomId = gameRoomModel.getRoomid();
        try {

          long start = System.currentTimeMillis();

          autoDownloadZJExcelService.autoDown(fileName, roomId, token);

          long end = System.currentTimeMillis();
          String message = "已下载" + fileName + ",耗时：" + (end - start) + "毫秒";
          excelInfo(message);
          log.info(message);
          downloadCache.put(fileName, Boolean.TRUE);

        } catch (UnknownHostException ue) {
          log.error("自动下载异常：UnknownHostException，文件名：" + fileName);
          downloadCache.remove(fileName);
        } catch (FileNotFoundException notfoundE) {
          log.error("自动下载异常：FileNotFoundException，原因，已经下载过，且正被使用中，或者含有'/'" + fileName + ",房间ID:"
              + roomId);
          downloadCache.remove(fileName);
        } catch (SocketTimeoutException timeOutE) {
          log.error("自动下载异常SocketTimeoutException: 连接超时，文件名：" + fileName);
          downloadCache.remove(fileName);
        } catch (IOException ioe) {
          String errMsg = ioe.getMessage();
          log.error("自动下载失败IOException：文件名：" + fileName + (errMsg.contains("403") ? ",具体信息：403返回码！"
              : ""));
          downloadCache.remove(fileName);
        } catch (Exception e) {
          log.error("自动下载异常：未捕获的其他异常，文件名：" + fileName + ",具体信息：" + e.getMessage(), e);
          downloadCache.remove(fileName);
        }

      }
    }

  }

  /**
   * 获取房间列表后，检查与上次的对比
   *
   * @time 2018年4月15日
   */
  @SuppressWarnings("unchecked")
  private List<GameRoomModel> updatedList(final List<GameRoomModel> roomList) {
    if (CollectUtil.isEmpty(roomList)) {
      return Collections.EMPTY_LIST;
    }

    List<GameRoomModel> updateList = roomList.stream().filter(info -> {
      String name = getDownLoadFilterName(info.getCreatetime(), info.getRoomname());
      return !downloadCache.containsKey(name);
    }).collect(Collectors.toList());
    return updateList;
  }

  private String getDownLoadFilterName(String finishedTime, String originalRoomName) {
    originalRoomName = originalRoomName.replace("/", "-").replace("%20", "-");
    originalRoomName = FilterUtf8mb4.filterUtf8mb4(originalRoomName);
    return finishedTime + "#战绩导出-" + originalRoomName + ".xls";
  }

  /**
   * 获取自动下载战绩Excel的参数
   *
   * @param downType //1 普通 2奥马哈 3 4
   * @time 2018年4月15日
   */
  private Map<String, String> getParams(String downType) {
    String clubId = myController.currentClubId.getText();
    // long[] timeRange = TimeUtil.getTimeRange();
    String[] timeRange = getTimeRange();
    String startTime = timeRange[0];
    String endTime = timeRange[1];
    Map<String, String> params = new HashMap<>();
    params.put("clubId", clubId);
    params.put("startTime", startTime);
    params.put("endTime", endTime);
    params.put("keyword", "");
    params.put("order", "-1");
    params.put("gameType", downType);
    params.put("pageSize", "250");
    params.put("isCredit", "1");
    return params;
  }

  public String[] getTimeRange() {
    String start = getFirstDayStartMillTime();
    String end = getSecondDayEndMillTime();

    String[] timeRange = new String[]{start, end};
    return timeRange;
  }

  /**
   * 清空战绩Excel下载记录
   *
   * @time 2018年4月14日
   */
  public void removeExcelAreaAction(ActionEvent event) {
    if (excelArea.getItems() != null) {
      excelArea.getItems().clear();
    }
  }

  /**
   * 查看已下载的Excel记录
   *
   * @time 2018年4月14日
   */
  @SuppressWarnings("rawtypes")
  public void seeHasDownExcelListCacheAction(ActionEvent event) {
    if (MapUtil.isNullOrEmpty(downloadCache)) {
      ShowUtil.show("无下载记录，拜拜！", 1);
      return;
    }

    Dialog dialog = new Dialog();
    dialog.setResizable(true);
    dialog.setHeight(600.0); // TODO 设置大小没效果？？？
    dialog.setWidth(400.0);
    dialog.setTitle("已下载的Excel");
    dialog.setHeaderText(null);

    ButtonType loginButtonType = new ButtonType("确定", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    VBox cacheContent = new VBox();
    int index = 0;
    String indexString = "";
    for (String downedExcelName : downloadCache.keySet()) {
      indexString = String.format("%04d", (++index));
      cacheContent.getChildren().add(new Label(indexString + " :   " + downedExcelName));
    }
    scrollPane.setContent(cacheContent);
    dialog.getDialogPane().setContent(scrollPane);

    dialog.show();
  }

  private void excelInfo(String description) {
    ObservableList<String> items = excelArea.getItems();
    if (items != null) {
      items.add(description);
      log.info(description);
    } else {
      items = FXCollections.observableArrayList();
    }
    excelArea.refresh();
  }

  /**
   * 删除本地文件并重新下载
   *
   * @time 2018年4月15日
   */
  public void reAutoDownAction(ActionEvent event) {
    String content = "你即将删除本地文件【" + downloadCache.size() + "个】并重新下载 , 确定?";
    if (AlertUtil.confirm(content)) {
      if (this.timer != null) {
        ShowUtil.show("请先暂停爬取!");
        return;
      } else {
        try {
          // 删除本地文件夹内容
          File downLoadFileFolder =
              new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()
                  + "\\" + getSelectedDate().toString());
          if (downLoadFileFolder.exists()) {
            FileUtils.forceDelete(downLoadFileFolder);
            ShowUtil.show("已删除本地文件夹！", 1);
          } else {
            ShowUtil.show("本地不存在文件夹：" + downLoadFileFolder.getAbsolutePath());
          }

        } catch (IOException e) {
          ErrorUtil.err("删除本地文件夹失败", e);
        } catch (NullPointerException e) {
          ShowUtil.show("请输入时间再删除本地文件夹");

        } finally {
          // 清空缓存
          downloadCache.clear();
        }
      }

    }
  }


  /**
   * 开始下载Excel按钮
   *
   * @time 2018年4月29日
   */
  public void startDownloadTimerAction(ActionEvent evet) {
    if (!checkTime()) {
      ShowUtil.show("请先设置好时间！", 1);
      return;
    }
    if (this.excelTimer != null) {
      ShowUtil.show("下载Excel的后台程序正在运行！", 1);
      return;
    }
    Integer separateTime = getDownExcelPeriod();
    excelInfo("每隔" + separateTime / 1000 + "秒刷新一次！");
    excelInfo("开始程序爬取和下载...");
    setTimeRange();
    this.excelTimer = new Timer();
    excelTimer.schedule(new TimerTask() {
      public void run() {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {

            for (ExcelAutoDownType type : ExcelAutoDownType.values()) {
              String excelDownType = type.getValue();
              // 自动下载当天Excel
              autoDownExcels(excelDownType);
            }
          }
        });

      }
    }, 1000, separateTime); // 定时器的延迟时间及间隔时间

  }

  /**
   * 下载时记录下载范围
   *
   * @time 2018年5月5日
   */
  private void setTimeRange() {
    String start = firstDayStartTimeField.getText().replace(CN_MH, EN_MH);
    start = getSelectedDate().toString() + " " + start;

    String end = secondDayEndTimeField.getText().replace(CN_MH, EN_MH);
    end = getSelectedDate().plusDays(1).toString() + " " + end;

    String msg = String.format("下载范围：%s 到 %s", start, end);
    excelInfo(msg);
  }

  private Integer getDownExcelPeriod() {

    String text = downExcelPierodField.getText();
    Integer time = 60;
    try {
      time = Integer.valueOf(text.trim());
    } catch (Exception e) {
      time = 60;
    }
    return time * 1000;
  }

  /**
   * 停止下载Excel
   *
   * @time 2018年4月29日
   */
  public void stopAutoDownExcelAction(ActionEvent evet) {
    if (this.excelTimer == null) {
      ShowUtil.show("后台程序没有启动", 1);
      return;
    }
    this.excelTimer.cancel();
    this.excelTimer = null;
    excelInfo("停止下载后台Excel数据!!!");
  }

  @Override
  public Class<?> getSubClass() {
    return getClass();
  }

}
