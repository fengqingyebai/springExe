package com.kendy.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jfoenix.controls.JFXButton;
import com.kendy.constant.Constants;
import com.kendy.db.DBUtil;
import com.kendy.entity.BankFlowInfo;
import com.kendy.enums.BankEnum;
import com.kendy.model.BankFlowModel;
import com.kendy.util.CollectUtil;
import com.kendy.util.MapUtil;
import com.kendy.util.StringUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 银行流水控制类
 *
 * @author linzt
 * @time 2018年6月27日
 */
@Component
public class BankFlowController extends BaseController implements Initializable {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  public DBUtil dbUtil;

  @Autowired
  public MyController myController;

  @FXML
  public JFXButton bankFlowTitle;

  @FXML
  public ScrollPane scrollDates; // 放所有动态表的pane

  private static final String TITLE = "今日银行流水";

  private static final int COL_WIDTH = 84;

  // 放所有动态表
  VBox bankFlowVBox = new VBox(10);

  // 所有的银行流水记录
  public List<BankFlowModel> totalBankFlowList = new ArrayList<>();

  // {银行 ： 该银行的流水列表}
  Map<String, List<BankFlowModel>> todayBankFlowMap = new HashMap<>();

  private static boolean firstTime = true;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refresh();
  }

  public void refresh() {

    // 初始化数据
    initData();
    if (CollectUtil.isEmpty(totalBankFlowList)) {
      return;
    }

    // 动态生成表
    generateAllTables();
  }

  /**
   * 设置银行流水日期
   */
  public void setBankFlowTitle(String softTime) {
    bankFlowTitle.setText(TITLE + softTime);
  }


  /**
   * 动态生成表
   *
   * @time 2018年7月1日
   */
  public void generateAllTables() {
    // 最新日期
    String latestDay = getCurrentSoftTime();
    // 制表
    long start = System.currentTimeMillis();
    TableView<BankFlowInfo> table = dynamicGenerateGDTable();
    int tableHeight = 620;
    table.setMinHeight(tableHeight);
    setTableData(latestDay, table, todayBankFlowMap);

    // 添加内容
    bankFlowVBox.getChildren().add(table);
    addDetailStaticInfo(todayBankFlowMap); // 添加详细信息
    addHistoryStaticInfo(); // 添加历史汇总信息
    bankFlowVBox.getChildren().add(new Label());
    long end = System.currentTimeMillis();
    log.info(String.format("加载%d条流水进系统耗时：%d毫秒", totalBankFlowList.size(), end - start));

    scrollDates.setPadding(new Insets(10));
    scrollDates.setFitToHeight(true);
    scrollDates.setFitToWidth(true);
    scrollDates.setContent(bankFlowVBox);
  }

  private String getCurrentSoftTime() {
    String latestDay = myController.getSoftDate();
    return StringUtil.isNotBlank(latestDay) ? latestDay
        : StringUtil.nvl(dbUtil.getMaxBankFlowTime(), "");
  }

  /**
   * 初始化数据
   *
   * @time 2018年7月1日
   */
  private void initData() {
    bankFlowVBox.getChildren().clear();
    logger.info("清空银行流水界面数据");
    if (firstTime) {
      logger.info("从数据库加载所有银行流水数据");
      totalBankFlowList = dbUtil.getAllHistoryBankMoney();
      firstTime = false;
    }

    if (CollectUtil.isEmpty(totalBankFlowList)) {
      return;
    }

    String softDate = getCurrentSoftTime();
    // {每一天 ：{银行类型 ： 上码列表}}
    todayBankFlowMap = totalBankFlowList.stream().filter(e -> e.getSoftTime().equals(softDate))
        .sorted((x, y) -> x.getUpdateTime().compareTo(y.getUpdateTime()))
        .collect(Collectors.groupingBy(BankFlowModel::getBankName)); // 再按银行类型分类
  }

  private void addStaticTitle(String title) {
    Label label = new Label(title);
    label.setStyle("-fx-font-size: 1.3em; -fx-text-fill: #9c8617");
    bankFlowVBox.getChildren().addAll(label);
  }


  /**
   * 添加统计信息：今日汇总 & 历史汇总
   *
   * @param isToday 是否为今日汇总
   * @param dataMap {银行：该银行相应的流水记录}
   */
  private void addStaticInfo(final boolean isToday,
      final Map<String, List<BankFlowModel>> dataMap) {
    if (dataMap != null) {
      int todayCount;
      Long payCount, incomeCount, todaySumPay, todaySumIncome, todaySumFlow = 0L;
      String titleString = isToday ? "今日汇总：" : "历史汇总";
      addStaticTitle(titleString);
      // 遍历所有银行
      for (BankEnum bankEnum : BankEnum.values()) {
        HBox hbox = new HBox();
        String bankName = bankEnum.getName();
        List<BankFlowModel> todayBankList = dataMap.get(bankName);
        if (CollectUtil.isEmpty(todayBankList)) {
          hbox = getBankFlowHBoxMsg(isToday, bankName, 0, 0L, 0L, 0L, 0L, 0L);
        } else {
          todayCount = todayBankList.size();
          payCount = todayBankList.stream().filter(e -> e.getMoney() < 0).count();
          incomeCount = todayBankList.stream().filter(e -> e.getMoney() >= 0).count();
          todaySumPay = todayBankList.stream().filter(e -> e.getMoney() < 0)
              .mapToLong(BankFlowModel::getMoney).sum();
          todaySumIncome = todayBankList.stream().filter(e -> e.getMoney() >= 0)
              .mapToLong(BankFlowModel::getMoney).sum();
          todaySumFlow = todaySumPay + todaySumIncome;
          hbox = getBankFlowHBoxMsg(isToday, bankName, todayCount, payCount, incomeCount,
              todaySumPay, todaySumIncome, todaySumFlow);
        }
        bankFlowVBox.getChildren().addAll(hbox);
      }
    }
  }

  /**
   * 添加详情统计信息
   *
   * @time 2018年6月30日
   */
  private void addDetailStaticInfo(final Map<String, List<BankFlowModel>> todayMap) {
    addStaticInfo(true, todayMap);
  }


  /**
   * 历史汇总信息
   */
  private void addHistoryStaticInfo() {
    final Map<String, List<BankFlowModel>> historyMap =
        totalBankFlowList.stream().collect(Collectors.groupingBy(BankFlowModel::getBankName));
    addStaticInfo(false, historyMap);
  }


  /**
   * 添加一条统计信息
   */
  private HBox getBankFlowHBoxMsg(boolean isToday, String bankName, int todayCount, Long payCount,
      Long incomeCount, Long totalSumPay, Long totalSumIncome, Long totalSumFlow) {
    String prefix = isToday ? "当天" : "历史";
    String first = "\t" + bankName.replace("额", "").replace("付", "") + " : " + prefix + "总笔数  ";
    HBox hbox = new HBox();
    hbox.getChildren().add(getHbox(first, todayCount));
    hbox.getChildren().add(getHbox(prefix + "总支出笔数", payCount));
    hbox.getChildren().add(getHbox(prefix + "总收入笔数", incomeCount));
    hbox.getChildren().add(getHbox(prefix + "总支出￥", totalSumPay));
    hbox.getChildren().add(getHbox(prefix + "总收入￥", totalSumIncome));
    return hbox;
  }

  private HBox getHbox(String key, long value) {
    HBox hbox = new HBox();
    Label keyLabel = new Label(" " + key + " ");
    Label valueLabel = new Label(value + "");
    if (value < -1) {
      valueLabel.setStyle("-fx-text-fill: red");
    }
    valueLabel.setPrefWidth(100D);
    hbox.getChildren().addAll(keyLabel, valueLabel);
    return hbox;
  }


  /**
   * 设置动态表的数据
   *
   * @param todayMap {银行类型 ： 上码列表}
   */
  private void setTableData(String dateString, TableView<BankFlowInfo> table,
      Map<String, List<BankFlowModel>> todayMap) {
    if (MapUtil.isNullOrEmpty(todayMap)) {
      return;
    }
    int loopTimes = todayMap.values().stream().mapToInt(Collection::size).max().orElseGet(() -> 0);
    if (loopTimes > 0) {
      ObservableList<BankFlowInfo> obList = FXCollections.observableArrayList();
      for (int i = 0; i < loopTimes; i++) {
        BankFlowInfo info = new BankFlowInfo();
        info.setIndex(i + 1 + "");
        info.setDateString(dateString.replace("2018-", ""));
        info.setYuEBao(getBankFlowValue(i, BankEnum.YuEBao, todayMap));
        info.setHuaXia(getBankFlowValue(i, BankEnum.HuaXia, todayMap));
        info.setPingAn(getBankFlowValue(i, BankEnum.PingAn, todayMap));
        info.setZhaoShang(getBankFlowValue(i, BankEnum.ZhaoShang, todayMap));
        info.setZhiFuBao(getBankFlowValue(i, BankEnum.ZhiFuBao, todayMap));
        info.setPuFa(getBankFlowValue(i, BankEnum.PuFa, todayMap));
        info.setXingYe(getBankFlowValue(i, BankEnum.XingYe, todayMap));
        //// 中国，中信，民生，光大，建设，工商
        info.setZhongGuo(getBankFlowValue(i, BankEnum.ZhongGuo, todayMap));
        info.setZhongXin(getBankFlowValue(i, BankEnum.ZhongXin, todayMap));
        info.setMinSheng(getBankFlowValue(i, BankEnum.MinSheng, todayMap));
        info.setGuangDa(getBankFlowValue(i, BankEnum.GuangDa, todayMap));
        info.setJianShe(getBankFlowValue(i, BankEnum.JianShe, todayMap));
        info.setGongShang(getBankFlowValue(i, BankEnum.GongShang, todayMap));

        obList.add(info);
      }
      table.setItems(obList);
    }
  }

  /**
   * 获取当天的银行对应的流水
   * <p>
   * 此方法会调用很多次
   * </p>
   *
   * @param index 序号，与当天银行的对应列表进行比较，便于取流水
   * @param bankEnum 银行类型
   * @param todayMap 当天所有银行的流水
   * @time 2018年6月30日
   */
  private String getBankFlowValue(int index, BankEnum bankEnum,
      Map<String, List<BankFlowModel>> todayMap) {
    if (MapUtil.isHavaValue(todayMap)) {
      List<BankFlowModel> bankFlowList = todayMap.get(bankEnum.getName());
      if (CollectUtil.isHaveValue(bankFlowList) && bankFlowList.size() > index) {
        return bankFlowList.get(index).getMoney() + "";
      }
    }
    return "";
  }


  /**
   * 动态生成表格
   */
  private TableView<BankFlowInfo> dynamicGenerateGDTable() {
    TableView<BankFlowInfo> table = new TableView<BankFlowInfo>();
    TableColumn<BankFlowInfo, String> indexCol = getTableColumnCommon("序号", "index");
    // TableColumn<BankFlowInfo, String> dateCol = getTableColumnCommon("日期", "dateString");
    table.getColumns().add(indexCol);
    // table.getColumns().add(dateCol);
    for (BankEnum bankEnum : BankEnum.values()) {
      table.getColumns().add(getTableColumn(bankEnum.getName(), bankEnum.getValue()));
    }
    return table;
  }

  /**
   * 动态生成列
   */
  private TableColumn<BankFlowInfo, String> getTableColumn(String colName, String colVal) {
    TableColumn<BankFlowInfo, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(COL_WIDTH);
    col.setCellValueFactory(new PropertyValueFactory<BankFlowInfo, String>(colVal));
    col.setCellFactory(getColorCellFactory(new BankFlowInfo()));
    col.setSortable(false);
    return col;
  }

  /**
   * 动态生成列(不用红色字段的列)
   */
  private TableColumn<BankFlowInfo, String> getTableColumnCommon(String colName, String colVal) {
    TableColumn<BankFlowInfo, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(COL_WIDTH);
    col.setCellValueFactory(new PropertyValueFactory<BankFlowInfo, String>(colVal));
    col.setSortable(false);
    return col;
  }

  /**
   * 下拉
   *
   * @time 2018年7月1日
   */
  @FXML
  public void seeHistoryStaticAction(ActionEvent event) {
    scrollDates.setVvalue(scrollDates.getVmax());
  }

  /**
   * 返回
   */
  @FXML
  public void seeDetailStaticAction(ActionEvent event) {
    scrollDates.setVvalue(scrollDates.getVmin());
  }


  /**
   * 获取月份和日， 如2018-06-01 ==> 6-1
   *
   * @time 2018年7月1日
   */
  // private final String getSimpleDateString(String dateString) {
  // if (StringUtil.isNotBlank(dateString) && dateString.length() > 6) {
  // LocalDate date = LocalDate.parse(dateString);
  // int month = date.getMonthValue();
  // int day = date.getDayOfMonth();
  // return month + "-" + day;
  // }
  // return dateString;
  // }
  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
