package com.kendy.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
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
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.DataConstans;
import com.kendy.db.DBUtil;
import com.kendy.entity.Huishui;
import com.kendy.entity.Player;
import com.kendy.entity.SMAutoInfo;
import com.kendy.entity.ShangmaInfo;
import com.kendy.entity.ShangmaNextday;
import com.kendy.enums.KeyEnum;
import com.kendy.excel.ExportExcelTemplate;
import com.kendy.model.GameRoomModel;
import com.kendy.model.RespResult;
import com.kendy.model.SMResultModel;
import com.kendy.model.WanjiaApplyInfo;
import com.kendy.model.WanjiaListResult;
import com.kendy.service.AutoDownloadZJExcelService;
import com.kendy.service.HttpService;
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
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import com.kendy.util.TimeUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
 * 处理联盟配额的控制器
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
  public MyController myController ;
  @Autowired
  public CombineIDController combineIDController ;
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
  public TgWaizhaiService tgWaizhaiService; // 配帐控制类
  @Autowired
  public MoneyService moneyService; // 配帐控制类
  @Autowired
  public DataConstans dataConstants; // 数据控制类

  
  @Autowired
  HttpService httpService;

  @FXML public TextField smNextDayRangeFieldd; // 次日上码配置

  @FXML public TextArea tokenArea;// token值
  @FXML public Label tokenStatus; // token状态
  @FXML public ListView<String> logArea;
  @FXML public ListView<String> excelArea;
  @FXML public TextField sysCodeField;
  @FXML public TextField sperateTimeField;// 每隔多久去刷新
  @FXML public TextField filterPlayIdFields;// 上码过过滤人员（只给这些人上码）

  // =====================================================================自动上码日志记录表
  @FXML public TableView<SMAutoInfo> tableSMAuto;
  @FXML private TableColumn<SMAutoInfo, String> smAutoDate;
  @FXML private TableColumn<SMAutoInfo, String> smAutoPlayerId;
  @FXML private TableColumn<SMAutoInfo, String> smAutoPlayerName;
  @FXML private TableColumn<SMAutoInfo, String> smAutoPaiju;
  @FXML private TableColumn<SMAutoInfo, String> smAutoApplyAccount;
  @FXML private TableColumn<SMAutoInfo, String> smAutoIsTeamAvailabel;
  @FXML private TableColumn<SMAutoInfo, String> smAutoIsCurrentDay;
  @FXML private TableColumn<SMAutoInfo, String> smAutoIsNextDay;
  @FXML private TableColumn<SMAutoInfo, String> smAutoIsAgree;
  @FXML private TableColumn<SMAutoInfo, String> smAutoIsAgreeSuccess;
  @FXML private TableColumn<SMAutoInfo, String> smAutoAvailabel;
  @FXML private TableColumn<SMAutoInfo, String> smAutoTeamTotalAvailabel;

  @FXML public TextField downExcelPierodField;// 每隔多久去刷新


  private final String SM_AOTO_NEXT_DAY_DB_KEY =
      KeyEnum.SM_AOTO_NEXT_DAY_DB_KEY.getKeyName(); // 保存到数据库的key
  private final String SM_AOTO_TOKEN_DB_KEY = KeyEnum.SM_AOTO_TOKEN_DB_KEY.getKeyName(); // 保存到数据库的key

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
    bindCellValueByTable(new SMAutoInfo(), tableSMAuto); // 泽涛 自动上码待观察
    // MyController.bindCellValue(smAutoDate, smAutoPlayerId, smAutoPlayerName, smAutoPaiju,
    // smAutoApplyAccount,
    // smAutoIsTeamAvailabel, smAutoIsCurrentDay, smAutoIsNextDay, smAutoIsAgree,
    // smAutoIsAgreeSuccess,
    // smAutoAvailabel, smAutoTeamTotalAvailabel);
    // 初始化次日上码范围
    initNextDayRange();

    loadTokenAction(new ActionEvent());

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
   * @param event
   */
  public void loadNextDayAction(ActionEvent event) {
    initNextDayRange();
  }


  /**
   * 保存Token
   * 
   * @time 2018年3月31日
   * @param event
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
   * @param event
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
   * @throws Exception
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
    WanjiaListResult wanjiaListResult = httpService.getWanjiaListResult(token);
    if (wanjiaListResult == null) {
      setTokenStatusFail("接口无返回");
      return;
    }
    if (wanjiaListResult.getiErrCode() != 0) {
      setTokenStatusFail(wanjiaListResult.getiErrCode() + "");
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
   * @param description
   */
  private void logInfo(String description) {
    synchronized (lock) {
      ObservableList<String> items = logArea.getItems();
      if (items != null) {
        items.add(description);
      } else {
        items = FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList("ssss")));
      }
      logArea.refresh();
      log.info(description);
    }
  }

  /**
   * 清空右边日志框内容ListView
   * 
   * @time 2018年3月26日
   * @param event
   */
  public void removeLogAreaAction(ActionEvent event) {
    if (logArea.getItems() != null)
      logArea.getItems().clear();
  }

  /**
   * 获取token
   * 
   * @time 2018年3月26日
   * @return
   */
  public String getToken() {
    return StringUtil.nvl(tokenArea.getText(), "");
  }

  /**
   * 保存次日上码的配置
   * 
   * @time 2018年3月26日
   * @param evet
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
   * 判断是否今日上码
   * 
   * @time 2018年3月27日
   * @return
   */
  public boolean judgeIsTodaySM(String paijuString) {
    Integer paiju = Integer.valueOf(paijuString);
    String rangStr = smNextDayRangeFieldd.getText();
    if (StringUtil.isBlank(rangStr))
      return true;

    String[] list = rangStr.split("##");
    // List<String> list = Arrays.asList("01-90","8001-8090","9001-9090");
    for (String range : list) {
      String[] ranges = range.split("-");
      boolean isInRange =
          paiju >= Integer.valueOf(ranges[0]) && paiju <= Integer.valueOf(ranges[1]);
      if (isInRange) {
        return false; // 该牌局是次日上码
      }
    }
    return true; // 该牌局是今日上码
  }

  /**
   * 开始爬取后台数据
   * 
   * @time 2018年3月26日
   * @param evet
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
            reqAndHandleBuyinList();
          }
        });
        // logInfo("此处等待10秒...");
      }
    }, 1000, separateTime); // 定时器的延迟时间及间隔时间
  }

  public void reqAndHandleBuyinList() {
    logInfo("正在获取玩家信息..." + TimeUtil.getTimeString());
    try {
      // 调用接口
      List<WanjiaApplyInfo> buyinList = httpService.getBuyinList(getToken());
      // 处理数据
      if (buyinList == null) {
        logInfo("获取到玩家为空!!!");
      } else {
        logInfo("获取到玩家个数：" + buyinList.size());
        // 真正的处理逻辑
        handleAutoShangma(buyinList);
      }
    } catch (Exception e) {
      String errMesg = "获取玩家信息请求失败";
      logInfo(errMesg);
      log.error(errMesg);
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
   * 
   * @time 2018年3月26日
   * @param buyinList
   */
  public synchronized void handleAutoShangma(List<WanjiaApplyInfo> buyinList) {

    for (WanjiaApplyInfo wanjiaApplyInfo : buyinList) {

      /*
       * 玩家相关信息
       */
      String paijuStr = wanjiaApplyInfo.getGameRoomName();
      String paijuString =
          paijuStr.substring(wanjiaApplyInfo.getGameRoomName().lastIndexOf("-") + 1);// 本系统桌号
      String buyStack = wanjiaApplyInfo.getBuyStack().toString();// 购买数量
      String playerId = wanjiaApplyInfo.getShowId();
      String playerName = wanjiaApplyInfo.getStrNick();

      Player player = dataConstants.membersMap.get(playerId);
      if (player == null) {
        logInfo("玩家（" + playerName + ")未录入到系统中！！！");
        continue;
      }
      String teamId = player.getTeamName();
      Huishui huishui = dataConstants.huishuiMap.get(teamId);
      String selectTeamAvailabel = huishui.getTeamAvailabel(); // 是否勾选了团队上码：1是 0否

      logInfo(playerName + "正在模拟更新实时上码...");
      SMResultModel resultModel = shangmaService.getDataAfterloadShangmaTable(teamId, playerId);// 模拟更新实时上码
      logInfo(playerName + "模拟更新实时上码结束");
      ShangmaInfo selectedSMInfo = resultModel.getSelectedSMInfo();
      if (selectedSMInfo == null) {
        logInfo("玩家（" + playerName + ")在上码系统中不存在！！");
        continue;
      }

      String teamAvailabel = resultModel.getTeamTotalAvailabel(); // 获取团队可上码
      String calcAvailable = getAvailable(resultModel, selectTeamAvailabel, playerId, playerName); // 获取可上码
      boolean isTodaySM = judgeIsTodaySM(paijuString); // 是否为次日上码：
      boolean passCheck = checkInRange(selectTeamAvailabel, buyStack, teamAvailabel, calcAvailable); // 是否同意

      /****************************************/
      boolean addOK = false;
      logInfo(playerName + "是否合范围：" + passCheck);
      if (passCheck) {
        List<String> testList = new ArrayList<>();

        if (hasFilterPlayerIds())
          testList = Arrays.stream(filterPlayIdFields.getText().trim().split("##"))
              .collect(Collectors.toList());

        if (CollectUtil.isEmpty(testList) || testList.contains(playerId)) {
          // 添加上码到软件中，同时发送后台请求
          Long userUuid = wanjiaApplyInfo.getUuid();// 用户ID
          Long roomId = wanjiaApplyInfo.getGameRoomId(); // 房间号
          addOK = addShangma(resultModel, isTodaySM, playerId, playerName, paijuString, buyStack,
              userUuid, roomId);
        }
      } else {
        log.debug(player.getPlayerName() + "买入" + buyStack + "不在范围中[" + teamAvailabel + ","
            + calcAvailable + "]");
      }
      /****************************************/
      SMAutoInfo smAutoInfo = new SMAutoInfo(getTimeString(), playerId, playerName, paijuString,
          buyStack, teamAvailabel, // 团队可上码 (第一关)
          calcAvailable, // 计算可上码（第二关）
          "1".equals(selectTeamAvailabel) ? "是" : "否", isTodaySM ? "是" : "否", // smAutoIsCurrentDay
          isTodaySM ? "否" : "是", // smAutoIsNextDay
          passCheck ? "是" : "否", // smAutoIsAgree
          (passCheck) ? (addOK ? "成功" : "失败") : "-"// smAutoIsAgreeSuccess
      );
      logInfo(playerName + "开始记录入表。。。" + JSON.toJSONString(smAutoInfo) );
      addItem(smAutoInfo);
    }
  }

  /**
   * 本类核心 ：添加上码到软件中，同时发送后台请求
   * 
   * @time 2018年3月28日
   * @param isTodaySM 是否今日上码
   * @param passCheck 是否审核通过
   * @param paijuString 第几局
   * @param buyStack 上码值
   * @param userUuid 后台用户ID
   * @param roomId 房间号
   */
  public boolean addShangma(SMResultModel resultModel, boolean isTodaySM, String playerId,
      String playerName, String paijuString, String buyStack, Long userUuid, Long roomId) {
    boolean addOK = false;
    try {
      boolean acceptBuyOK = httpService.acceptBuy(userUuid, roomId, getToken()); // 后台申请买入
      if (acceptBuyOK) {
        if (isTodaySM) {
          shangmaService.addNewShangma2DetailTable_HT(resultModel,
              shangmaService.getShangmaPaiju(paijuString), buyStack);

        } else {
          ShangmaNextday nextday = new ShangmaNextday();
          nextday.setPlayerId(playerId);
          nextday.setPlayerName(playerName);
          nextday.setChangci(shangmaService.getShangmaPaiju(paijuString));
          nextday.setShangma(buyStack);
          nextday.setTime(TimeUtil.getDateTime2());

          // 新增玩家的次日数据
          shangmaService.addNewRecord_nextday_HT(resultModel, nextday);
        }
        addOK = true;
        log.info(String.format("%s[%s]第%s局买入%s,网络买入结果是[成功]，计入上码", playerName, playerId, paijuString,
            buyStack));
      } else {
        log.error(String.format("%s[%s]第%s局买入%s,网络买入结果是[失败]，没有计入上码", playerName, playerId,
            paijuString, buyStack));
      }
    } catch (Exception e) {
      String msg = String.format("%s[%s]第%s局买入%s,网络买入结果是[失败]，原因：网络异常[%s]", playerName, playerId,
          paijuString, buyStack, e.getMessage());
      log.error(msg, e);
      return Boolean.FALSE;
    }
    return addOK;
  }



  /**
   * 判断a 是否在区间范围[b,c]
   * 
   * @time 2018年3月28日
   * @param a 申请数量
   * @param b 团队可上码
   * @param c 个人可上码
   * @return
   */
  private boolean checkInRange(String selectedTeamAvailabel, String a, String b, String c) {
    Double A = NumUtil.getNum(a);
    Double B = NumUtil.getNum(b);
    Double C = NumUtil.getNum(c);
    if ("1".equals(selectedTeamAvailabel)) { // 勾选了团队可上码，则申请数量只跟团队可上码比较
      return A <= B;
    } else {
      return A <= B && A <= C;
    }
  }

  /**
   * 自动上码时获取玩家的可上码总接口 包括各种情况（如已勾选团队上码
   * 
   * @time 2018年3月27日
   * @return
   */
  private String getAvailable(SMResultModel resultModel, String selectTeamAvailabel,
      String playerId, String playerName) {
    List<ShangmaInfo> smList = resultModel.getSmList();
    String available = "0";
    // 勾选了团队上码
    if ("1".equals(selectTeamAvailabel)) {
      return "0";
    } else {
      // 以下是没有勾选团队上码的情况
      boolean personNode = not_supter_not_sub(playerId);
      if (personNode) {
        // 情况1：私人节点ID
        available = getOnePersonAvailabel(smList, playerId);
      } else {
        // 情况2：有联合ID
        available = getLianheAvailabel(smList, playerId);
      }

    }
    return available;
  }

  /**
   * 联合额度
   * 
   * @time 2018年3月27日
   * @param playerId
   * @return
   */
  public String getLianheAvailabel(List<ShangmaInfo> smList, String playerId) {
    String superId = dataConstants.Combine_Super_Id_Map.containsKey(playerId) ? playerId
        : dataConstants.Combine_Sub_Id_Map.get(playerId);

    if (StringUtil.isBlank(superId)) {
      log.error(playerId + "非父非子！");
      return "0";
    }

    String availaibel = smList.stream().filter(info -> superId.equals(info.getShangmaPlayerId()))
        .map(ShangmaInfo::getShangmaLianheEdu).findFirst().orElseGet(() -> "0");

    return availaibel;
  }

  /**
   * 私人可上码（非父非子ID）
   * 
   * @time 2018年3月27日
   * @param playerId
   * @return
   */
  public String getOnePersonAvailabel(List<ShangmaInfo> smList, String playerId) {
    String availaibel = smList.stream().filter(info -> playerId.equals(info.getShangmaPlayerId()))
        .map(ShangmaInfo::getShangmaAvailableEdu).findFirst().orElseGet(() -> "0");
    if (StringUtil.isBlank(availaibel)) {
      availaibel = "000000";
    }
    return availaibel;
  }

  /**
   * 往日志表添加记录
   * 
   * @time 2018年3月27日
   * @param smAutoInfo
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
   * @param evet
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
  }

  /**
   * 清空界面日志表
   * 
   * @time 2018年3月26日
   * @param evet
   */
  public void clearTableDataAction(ActionEvent evet) {
    TableUtil.clear(tableSMAuto);

  }

  /**
   * 判断是否私人节点（非父非子）
   * 
   * @time 2018年3月31日
   * @param playerId
   * @return
   */
  private boolean not_supter_not_sub(String playerId) {
    boolean isSuperId = dataConstants.Combine_Super_Id_Map.containsKey(playerId);
    boolean isSubId = dataConstants.Combine_Sub_Id_Map.containsKey(playerId);
    return !isSuperId && !isSubId;
  }

  /**
   * 是否有过滤节点
   * 
   * @time 2018年3月31日
   * @return
   */
  private boolean hasFilterPlayerIds() {
    return StringUtil.isNotBlank(filterPlayIdFields.getText());
  }

  /**
   * 导出有上码的记录
   * 
   * @time 2018年3月31日
   * @param event
   */
  public void exportSMAction(ActionEvent event) {
    List<SMAutoInfo> autoShangmas = getAutoShangmas(1);
    if (CollectUtil.isEmpty(autoShangmas)) {
      ShowUtil.show("没有可供导出的数据！");
      return;
    }
    String[] rowsName = new String[] {"爬取时间", "玩家ID", "玩家名称", "牌局", "申请数量", "团队可上码", "计算可上码",
        "勾选团队", "当天", "次日", "同意审核", "审核结果"};
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (SMAutoInfo info : autoShangmas) {
      objs = new Object[rowsName.length];
      objs[0] = info.getSmAutoDate();
      objs[1] = info.getSmAutoPlayerId();
      objs[2] = info.getSmAutoPlayerName();
      objs[3] = info.getSmAutoPaiju();
      objs[4] = info.getSmAutoApplyAccount();
      objs[5] = info.getSmAutoTeamTotalAvailabel();
      objs[6] = info.getSmAutoAvailabel();
      objs[7] = info.getSmAutoIsTeamAvailabel();
      objs[8] = info.getSmAutoIsCurrentDay();
      objs[9] = info.getSmAutoIsNextDay();
      objs[10] = info.getSmAutoIsAgree();
      objs[11] = info.getSmAutoIsAgreeSuccess();
      dataList.add(objs);
    }
    String title = "自动上码-" + TimeUtil.getDateTime();
    List<Integer> columnWidths =
        Arrays.asList(3500, 4000, 3000, 3000, 3000, 4000, 4000, 3000, 3000, 3000, 3000, 3000, 5000);
    ExportExcelTemplate ex = new ExportExcelTemplate(title, rowsName, columnWidths, dataList);
    try {
      ex.export();
      ShowUtil.show("导出完成", 1);
    } catch (Exception e) {
      ErrorUtil.err("导出自动记录失败", e);
      e.printStackTrace();
    }
  }


  /**
   * 获取相应的自动上码记录
   * 
   * @time 2018年3月31日
   * @param type 1:审核结果非“-” 2：所有记录
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SMAutoInfo> getAutoShangmas(int type) {
    if (TableUtil.isHasValue(tableSMAuto)) {
      ObservableList<SMAutoInfo> items = tableSMAuto.getItems();
      if (1 == type) {
        return items.stream().filter(info -> !"-".equals(info.getSmAutoIsAgreeSuccess()))
            .collect(Collectors.toList());
      } else {
        return items;
      }
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * 测试模式时加载六个人
   * 
   * @time 2018年3月31日
   * @param event
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
  @FXML private DatePicker datePicker; // dateLabel.setText(dataConstants.Date_Str);
  @FXML private TextField firstDayStartTimeField;
  @FXML private TextField secondDayEndTimeField;

  private final String EN_MH = ":";
  private final String CN_MH = "：";
  private final String PU_TONG = "1";
  private final String AO_MA_HA = "2";
  private final String DA_BO_LUO = "6";
  private final int DOWN_LIMIT = 6; // 每次下载5个，总共一次性下载 6 * 3 = 18

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
   * @return
   */
  private boolean checkTime() {
    LocalDate selectDate = datePicker.getValue();
    String starttime = firstDayStartTimeField.getText();
    String endtime = secondDayEndTimeField.getText();
    if (selectDate == null || isUnValidTime(starttime) || isUnValidTime(endtime)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 获取时间戳
   * 
   * @time 2018年5月4日
   * @param timeStr 有效的时：分， 如10：00，中文冒号和英文冒号都支持
   * @return
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


  @SuppressWarnings("unused")
  private class AutoDownExcelService extends Service<String> {

    @Override
    protected Task<String> createTask() {
      return new AutoDownExcelTask();
    }

  }


  private class AutoDownExcelTask extends Task<String> {

    @Override
    protected String call() throws Exception {
      autoDownExcels(PU_TONG);
      super.updateMessage("PU_TONG========");
      autoDownExcels(AO_MA_HA);
      super.updateMessage("AO_MA_HA========");
      autoDownExcels(DA_BO_LUO);
      return "一个周期执行完成";
    }

  }


  // 软件启动时先去加载桌面已经下载的Excel列表进缓存
  // 重复的跳过，不重复的则下载后更新缓存和数据
  public void autoDownExcels(String DownType) {

    String houtai = PU_TONG.equals(DownType) ? "普通后台"
        : AO_MA_HA.equals(DownType) ? "奥马哈后台" : DA_BO_LUO.equals(DownType) ? "大菠萝" : "其他后台";

    RespResult<GameRoomModel> parseObject = new RespResult<>();
    try {
      excelInfo("正在获取" + houtai + "房间列表..." + TimeUtil.getTimeString());
      Map<String, String> params = getParams(DownType);
      String respString = httpService.sendPost(
          "http://cms.pokermanager.club/cms-api/game/getHistoryGameList", params, getToken());
      if (StringUtil.isNotBlank(respString)) {
        if (log.isDebugEnabled()) {
          String paramsJson = JSON.toJSONString(params);
          log.info("req params : " + paramsJson);
          log.info("rsp json : " + respString);
        }
        parseObject = (RespResult<GameRoomModel>) JSON.parseObject(respString,
            new TypeReference<RespResult<GameRoomModel>>() {});
        excelInfo(houtai + "房间数量：" + parseObject.getResult().getTotal());
      }
    } catch (Exception e) {
      String errMsg = "获取房间列表：网络异常...";
      excelInfo(errMsg + e.getMessage());
      return;
    }

    if (parseObject.getiErrCode() > 0) {
      excelInfo("网络异常，后台返回码：" + parseObject.getiErrCode());
      return;
    }

    boolean hasRoomValue = parseObject.getResult().getList() != null;
    if (hasRoomValue) {
      List<GameRoomModel> roomList = parseObject.getResult().getList();
      List<GameRoomModel> updatedList = updatedList(roomList);
      boolean isAllDown = updatedList.isEmpty() ? true : false;
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
          log.error("自动下载异常: 连接超时，文件名：" + fileName);
          downloadCache.remove(fileName);
        } catch (IOException ioe) {
          String errMsg = ioe.getMessage();
          log.error("自动下载失败：文件名：" + fileName + (errMsg.contains("403") ? ",具体信息：403返回码！" : ""));
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
   * @param roomList
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<GameRoomModel> updatedList(final List<GameRoomModel> roomList) {
    if (CollectUtil.isEmpty(roomList))
      return Collections.EMPTY_LIST;

    List<GameRoomModel> updateList = roomList.stream().filter(info -> {
      String name = getDownLoadFilterName(info.getCreatetime(), info.getRoomname());
      return !downloadCache.containsKey(name);
    }).collect(Collectors.toList());
    return updateList;
  }

  private String getDownLoadFilterName(String finishedTime, String originalRoomName) {
    originalRoomName = originalRoomName.replace("/", "-").replace("%20", "-");
    originalRoomName = FilterUtf8mb4.filterUtf8mb4(originalRoomName);
    return "战绩导出-" + originalRoomName + ".xls";
  }

  /**
   * 获取自动下载战绩Excel的参数
   * 
   * @time 2018年4月15日
   * @param downType //1 普通 2奥马哈 3 4
   * @return
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
    return params;
  }

  public String[] getTimeRange() {
    String start = getFirstDayStartMillTime();
    String end = getSecondDayEndMillTime();

    String[] timeRange = new String[] {start, end};
    return timeRange;
  }

  /**
   * 清空战绩Excel下载记录
   * 
   * @time 2018年4月14日
   * @param event
   */
  public void removeExcelAreaAction(ActionEvent event) {
    if (excelArea.getItems() != null)
      excelArea.getItems().clear();
  }

  /**
   * 查看已下载的Excel记录
   * 
   * @time 2018年4月14日
   * @param event
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
   * @param event
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
   * @param evet
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

            // 自动下载当天普通房间Excel
            autoDownExcels(PU_TONG);

            // 自动下载当天奥马哈房间Excel
            autoDownExcels(AO_MA_HA);

            // 自动下载当天大菠萝Excel
            autoDownExcels(DA_BO_LUO);
          }
        });
        // Platform.runLater(new Runnable() {
        // @Override
        // public void run() {
        // Service<String> autoDownExcelService = new AutoDownExcelService();
        // autoDownExcelService.start();
        // }
        // });

      }
    }, 1000, separateTime); // 定时器的延迟时间及间隔时间

    // ExecutorService service = Executors.newScheduledThreadPool(1, new ThreadFactory() {
    //
    // @Override
    // public Thread newThread(Runnable r) {
    // Thread t = new Thread();
    // t.setDaemon(true);
    // return t;
    // }
    //
    // });
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
   * @param evet
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
