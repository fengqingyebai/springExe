package com.kendy.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import com.kendy.application.Main;
import com.kendy.db.DBUtil;
import com.kendy.entity.BankFlowInfo;
import com.kendy.enums.BankEnum;
import com.kendy.model.BankFlowModel;
import com.kendy.util.CollectUtil;
import com.kendy.util.MapUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 银行流水控制类
 * 
 * @author linzt
 * @time 2018年6月27日
 */
@Component
public class BankFlowController extends BaseController implements Initializable {

  private static Logger log = Logger.getLogger(BankFlowController.class);
  
  @Autowired
  public DBUtil dbUtil;

  @FXML public ScrollPane scrollDates; // 放所有动态表的pane

  @FXML public StackPane stackPane; // 放条形图的pane

  private static final String CENTER_CSS = "-fx-alignment: CENTER;";
  private static final int COL_WIDTH = 98;

  VBox bankFlowVBox = new VBox(); // 放所有动态表

  List<BankFlowModel> totalBankFlowList;

  // {每一天 ：{银行类型 ： 上码列表}}
  Map<String, Map<String, List<BankFlowModel>>> bankFlowMap;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 初始化数据
    initData();
    if (CollectUtil.isEmpty(totalBankFlowList))
      return;

    // 动态生成表
    generateAllTables();

  }


  /**
   * 动态生成表
   * 
   * @time 2018年7月1日
   */
  public void generateAllTables() {
    // 最新日期
    String latestDay = bankFlowMap.keySet().stream().max((x, y) -> x.compareTo(y)).orElse("");
    // 制表
    long start = System.currentTimeMillis();
    bankFlowMap.forEach((dayKey, dayDataMap) -> {
      Label dateLabel = new Label(dayKey);
      dateLabel.setStyle("-fx-font-size: 1.3em; -fx-text-fill: #5B5B5B");
      TableView<BankFlowInfo> table = dynamicGenerateGDTable();
      int tableHeight = 200;
      if (latestDay.equals(dayKey)) {
        tableHeight = 620;
      }
      table.setMinHeight(tableHeight);
      setTableData(dayKey, table, dayDataMap);

      // 添加内容
      bankFlowVBox.getChildren().add(dateLabel);
      addDetailStaticInfo(dayDataMap); // 添加详细信息
      bankFlowVBox.getChildren().add(table);
      bankFlowVBox.getChildren().add(new Label());
    });
    long end = System.currentTimeMillis();
    log.info(String.format("加载%d条流水进系统耗时：%d毫秒", totalBankFlowList.size(), end - start));

    scrollDates.setPadding(new Insets(10));
    scrollDates.setFitToHeight(true);
    scrollDates.setFitToWidth(true);
    scrollDates.setContent(bankFlowVBox);
  }

  /**
   * 初始化数据
   * 
   * @time 2018年7月1日
   */
  private void initData() {
    totalBankFlowList = dbUtil.getAllHistoryBankMoney();
    if (CollectUtil.isEmpty(totalBankFlowList)) {
      return;
    }

    // {每一天 ：{银行类型 ： 上码列表}}
    bankFlowMap =
        totalBankFlowList.stream().sorted((x, y) -> x.getUpdateTime().compareTo(y.getUpdateTime()))
            .collect(Collectors.groupingBy(BankFlowModel::getSoftTime, // TreeMap::new // 先按每一天分类
                Collectors.groupingBy(BankFlowModel::getBankName))); // 再按银行类型分类
    // 按时间排序（必须！）
    Map<String, Map<String, List<BankFlowModel>>> _bankFlowMap =
        new TreeMap<>((x, y) -> y.compareTo(x));
    _bankFlowMap.putAll(bankFlowMap);
    bankFlowMap = _bankFlowMap;
  }

  /**
   * 添加详情统计信息
   * 
   * @time 2018年6月30日
   * @param todayMap
   */
  private void addDetailStaticInfo(final Map<String, List<BankFlowModel>> todayMap) {
    if (todayMap != null) {
      String bankName, msg;
      int todayCount;
      Long payCount, incomeCount, todaySumPay, todaySumIncome, todaySumFlow = 0L;
      List<Label> labelList = new ArrayList<>();
      // 遍历所有银行
      for (BankEnum bankEnum : BankEnum.values()) {
        bankName = bankEnum.getName();
        List<BankFlowModel> todayBankList = todayMap.get(bankName);
        if (CollectUtil.isEmpty(todayBankList)) {
          msg = getBankFlowMsg(bankName, 0, 0L, 0L, 0L, 0L, 0L);
        } else {
          todayCount = todayBankList.size();
          payCount = todayBankList.stream().filter(e -> e.getMoney() < 0).count();
          incomeCount = todayBankList.stream().filter(e -> e.getMoney() >= 0).count();
          todaySumPay = todayBankList.stream().filter(e -> e.getMoney() < 0)
              .mapToLong(BankFlowModel::getMoney).sum();
          todaySumIncome = todayBankList.stream().filter(e -> e.getMoney() >= 0)
              .mapToLong(BankFlowModel::getMoney).sum();
          todaySumFlow = todaySumPay + todaySumIncome;
          msg = getBankFlowMsg(bankName, todayCount, payCount, incomeCount, todaySumPay,
              todaySumIncome, todaySumFlow);
        }
        Label label = new Label(msg.toString());
        if (todaySumFlow.longValue() < -1) {
          label.setStyle("-fx-text-fill: red");
        }
        labelList.add(label);
      }
      labelList.add(new Label("详情  :"));
      bankFlowVBox.getChildren().addAll(labelList);
    }
  }

  /**
   * 获取详情统计信息
   * 
   * @time 2018年6月30日
   * @param bankName
   * @param todayCount // 当天总笔数
   * @param todaySumPay // 当天总支出
   * @param todaySumIncome // 当天总收入
   * @param todaySumFlow // 当天总流水
   * @return
   */
  private String getBankFlowMsg(String bankName, int todayCount, Long payCount, Long incomeCount,
      Long todaySumPay, Long todaySumIncome, Long todaySumFlow) {
    StringBuffer msg = new StringBuffer();
    String pattern = "%-8d";
    msg.append(String.format("%-4s", bankName.replace("额", "").replace("付", ""))).append(": ")
        .append(" 当天总笔数 ").append(String.format(pattern, todayCount)).append(" 当天总支出笔数 ")
        .append(String.format(pattern, payCount)).append(" 当天总收入笔数 ")
        .append(String.format(pattern, incomeCount)).append(" 当天总支出￥")
        .append(String.format(pattern, todaySumPay)).append(" 当天总收入￥")
        .append(String.format(pattern, todaySumIncome)).append(" 当天总利润￥")
        .append(String.format(pattern, todaySumFlow));
    return msg.toString();
  }

  /**
   * 设置动态表的数据
   * 
   * @param table
   * @param todayMap {银行类型 ： 上码列表}
   */
  private void setTableData(String dateString, TableView<BankFlowInfo> table,
      Map<String, List<BankFlowModel>> todayMap) {
    if (MapUtil.isNullOrEmpty(todayMap))
      return;
    int loopTimes = todayMap.values().stream().mapToInt(Collection::size).max().orElseGet(() -> 0);
    if (loopTimes > 0) {
      ObservableList<BankFlowInfo> obList = FXCollections.observableArrayList();
      for (int i = 0; i < loopTimes; i++) {
        BankFlowInfo info = new BankFlowInfo();
        info.setIndex(i + 1 + "");
        info.setDateString(dateString);
        info.setYuEBao(getBankFlowValue(i, BankEnum.YuEBao, todayMap));
        info.setHuaXia(getBankFlowValue(i, BankEnum.HuaXia, todayMap));
        info.setPingAn(getBankFlowValue(i, BankEnum.PingAn, todayMap));
        info.setZhaoShang(getBankFlowValue(i, BankEnum.ZhaoShang, todayMap));
        info.setZhiFuBao(getBankFlowValue(i, BankEnum.ZhiFuBao, todayMap));
        info.setPuFa(getBankFlowValue(i, BankEnum.PuFa, todayMap));
        info.setXingYe(getBankFlowValue(i, BankEnum.XingYe, todayMap));
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
   * @time 2018年6月30日
   * @param index 序号，与当天银行的对应列表进行比较，便于取流水
   * @param bankEnum 银行类型
   * @param todayMap 当天所有银行的流水
   * @return
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
    TableColumn<BankFlowInfo, String> dateCol = getTableColumnCommon("日期", "dateString");
    table.getColumns().add(indexCol);
    table.getColumns().add(dateCol);
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
    col.setStyle(CENTER_CSS);
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
    col.setStyle(CENTER_CSS);
    col.setPrefWidth(COL_WIDTH);
    col.setCellValueFactory(new PropertyValueFactory<BankFlowInfo, String>(colVal));
    col.setSortable(false);
    return col;
  }

  /**
   * 查看历史统计信息条形图
   * 
   * @time 2018年7月1日
   * @param event
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @FXML
  public void seeHistoryStaticAction(ActionEvent event) {
    if (CollectUtil.isEmpty(totalBankFlowList)) {
      ShowUtil.show("数据库中没有银行流水记录");
      return;
    }
    Dialog dialog = new Dialog<>();
    dialog.setTitle("银行流水汇总");
    dialog.setHeaderText(null);
    dialog.setHeight(500.0);
    dialog.setWidth(1000.0);
    dialog.setResizable(true);
    ButtonType loginButtonType = new ButtonType("关闭", ButtonData.APPLY);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);
    //dialog.initOwner(Main.primaryStage0); // 用于设置图标和使大小有效
    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    ShowUtil.setIcon(stage);

    StackPane stackPane = new StackPane();

    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final StackedBarChart<String, Number> sbc = new StackedBarChart<>(xAxis, yAxis);
    List<String> dayList =
        bankFlowMap.keySet().stream().map(e -> getSimpleDateString(e)).collect(Collectors.toList());
    xAxis.setCategories(FXCollections.<String>observableArrayList(dayList));
    sbc.setTitle(getTitleInfo());

    Platform.runLater(() -> {
      for (BankEnum bankEnum : BankEnum.values()) {
        String bankName = bankEnum.getName();
        final XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(bankName);
        bankFlowMap.forEach((day, dayMap) -> {
          List<BankFlowModel> bankFlowList = dayMap.get(bankName);
          int data = 0;
          if (CollectUtil.isHaveValue(bankFlowList)) {
            data = bankFlowList.stream().mapToInt(BankFlowModel::getMoney).sum();
          }
          series.getData().add(new XYChart.Data<>(getSimpleDateString(day), data));
        });
        sbc.getData().addAll(series);
      }
    });
    stackPane.getChildren().add(sbc);
    dialog.getDialogPane().setContent(stackPane);

    dialog.show();
  }

  /**
   * 获取统计的信息头
   * 
   * @time 2018年7月1日
   * @return
   */
  private String getTitleInfo() {
    int sumIncome = totalBankFlowList.stream().filter(e -> e.getMoney() > 0)
        .mapToInt(BankFlowModel::getMoney).sum();
    int sum = totalBankFlowList.stream().mapToInt(e -> Math.abs(e.getMoney())).sum();
    double devideUnit = 10000d;
    String titleInfo = String.format("总流水%s万    总收入%s万    总支出%s万",
        NumUtil.getNumDivide2(Double.valueOf(sum + ""), devideUnit),
        NumUtil.getNumDivide2(Double.valueOf(sumIncome + ""), devideUnit),
        NumUtil.getNumDivide2(Double.valueOf(sum - sumIncome + ""), devideUnit));
    return titleInfo;
  }

  /**
   * 获取月份和日， 如2018-06-01 ==> 6-1
   * 
   * @time 2018年7月1日
   * @param dateString
   * @return
   */
  private final String getSimpleDateString(String dateString) {
    if (StringUtil.isNotBlank(dateString) && dateString.length() > 6) {
      LocalDate date = LocalDate.parse(dateString);
      int month = date.getMonthValue();
      int day = date.getDayOfMonth();
      return month + "-" + day;
    }
    return dateString;
  }
  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
