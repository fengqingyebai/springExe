package com.kendy.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.kendy.BankEnum;
import com.kendy.db.DBUtil;
import com.kendy.entity.BankFlowInfo;
import com.kendy.model.BankFlowModel;
import com.kendy.util.CollectUtil;
import com.kendy.util.MapUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;

import application.MyController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 银行流水控制类
 * 
 * @author linzt
 * @time 2018年6月27日
 */
public class BankFlowController implements Initializable{
	
	
	private static Logger log = Logger.getLogger(BankFlowController.class);

	//=====================================================================
	@FXML public ScrollPane scrollDates; // 
	@FXML public StackPane stackPane; // 
	
    private static final String CENTER_CSS = "-fx-alignment: CENTER;";
    private static final int COL_WIDTH = 98;
	
    VBox bankFlowVBox = new VBox();
    
    List<BankFlowModel> totalBankFlowList;
    
    // {每一天 ：{银行类型 ： 上码列表}}
    Map<String, Map<String, List<BankFlowModel>>> BankFlowMap;

	@Override
	public void initialize(URL location, ResourceBundle resources) {


		totalBankFlowList = DBUtil.getAllHistoryBankMoney();
		if (CollectUtil.isNullOrEmpty(totalBankFlowList)) {
			log.debug("数据库中没有银行流水记录");
			return;
		}
		// {每一天 ：{银行类型 ： 上码列表}}
		BankFlowMap = totalBankFlowList.stream()
				.collect(Collectors.groupingBy(BankFlowModel::getSoftTime, // 先按每一天分类
						Collectors.groupingBy(BankFlowModel::getBankName))); // 再按银行类型分类

		// TODO 每一天进行排序
		String latestDay = BankFlowMap.keySet().stream().max((x,y)-> x.compareTo(y)).orElse("");

		// 动态生成表
		long start = System.currentTimeMillis();
		BankFlowMap.forEach((dayKey, dayDataMap) -> {
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
   * 添加详情统计信息
   * @time 2018年6月30日
   * @param todayMap
   */
  private void addDetailStaticInfo(final Map<String, List<BankFlowModel>>  todayMap) {
	  if(todayMap != null ) {
		  String bankName, msg;
		  int todayCount;
		  Long payCount, incomeCount,todaySumPay, todaySumIncome, todaySumFlow = 0L;
		  List<Label> labelList = new ArrayList<>();
		  //遍历所有银行
		  for(BankEnum bankEnum : BankEnum.values()) {
			  bankName = bankEnum.getName();
			  List<BankFlowModel> todayBankList = todayMap.get(bankName);
			  if(CollectUtil.isNullOrEmpty(todayBankList)) {
				  msg = getBankFlowMsg(bankName, 0, 0L, 0L,0L, 0L, 0L);
			  }else {
				  todayCount = todayBankList.size();
				  payCount = todayBankList.stream().filter(e->e.getMoney() < 0).count();
				  incomeCount = todayBankList.stream().filter(e->e.getMoney() >= 0).count();
				  todaySumPay = todayBankList.stream().filter(e->e.getMoney() < 0).mapToLong(BankFlowModel::getMoney).sum();
				  todaySumIncome = todayBankList.stream().filter(e->e.getMoney() >= 0).mapToLong(BankFlowModel::getMoney).sum();
				  todaySumFlow = todaySumPay + todaySumIncome;
				  msg = getBankFlowMsg(bankName, todayCount, payCount, incomeCount, todaySumPay, todaySumIncome, todaySumFlow);
			  }
			  Label label = new Label(msg.toString());
			  if(todaySumFlow.longValue() < -1) {
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
   private String getBankFlowMsg(String bankName, int todayCount, Long payCount, Long incomeCount, Long todaySumPay, Long todaySumIncome, Long todaySumFlow) {
		StringBuffer msg  = new StringBuffer();
		String pattern = "%-8d";
		msg.append(String.format("%-4s",bankName.replace("额", "").replace("付", ""))).append(": ")
			.append(" 当天总笔数 ").append(String.format(pattern, todayCount))
			.append(" 当天总支出笔数 ").append(String.format(pattern, payCount))
			.append(" 当天总收入笔数 ").append(String.format(pattern, incomeCount))
			.append(" 当天总支出￥").append(String.format(pattern, todaySumPay))
			.append(" 当天总收入￥").append(String.format(pattern, todaySumIncome))
			.append(" 当天总流水￥").append(String.format(pattern, todaySumFlow));
		return msg.toString();
	}
  
  /**
   * 设置动态表的数据
   * @param table
   * @param todayMap {银行类型 ： 上码列表}
   */
  private void setTableData(String dateString, TableView<BankFlowInfo> table, Map<String, List<BankFlowModel>> todayMap) {
    if(MapUtil.isNullOrEmpty(todayMap)) 
      return;
    int loopTimes = todayMap.values().stream().mapToInt(Collection::size).max().orElseGet(()-> 0);
    if(loopTimes > 0) {
      ObservableList<BankFlowInfo> obList= FXCollections.observableArrayList();
      for(int i=0; i< loopTimes ; i++) {
    	  BankFlowInfo info = new BankFlowInfo();
    	  info.setIndex(i+1+"");
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
   * <p>此方法会调用很多次</p>
   * 
   * @time 2018年6月30日
   * @param index 序号，与当天银行的对应列表进行比较，便于取流水
   * @param bankEnum 银行类型
   * @param todayMap 当天所有银行的流水
   * @return
   */
  private String getBankFlowValue(int index, BankEnum bankEnum, Map<String, List<BankFlowModel>> todayMap) {
	  if(MapUtil.isHavaValue(todayMap)) {
		  List<BankFlowModel> bankFlowList = todayMap.get(bankEnum.getName());
		  if(CollectUtil.isHaveValue(bankFlowList) && bankFlowList.size() > index) {
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
		col.setCellFactory(MyController.getColorCellFactory(new BankFlowInfo()));
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
	
	@FXML
	public void seeHistoryStaticAction(ActionEvent event) {
		Dialog dialog = new Dialog<>();
		dialog.setTitle("银行流水每日汇总图");
		dialog.setHeaderText(null);
		dialog.setHeight(600.0);
		dialog.setWidth(900.0);
    	ButtonType loginButtonType = new ButtonType("关闭", ButtonData.APPLY);
    	dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 15, 10, 10));
    	//==============================================================
		totalBankFlowList = DBUtil.getAllHistoryBankMoney();
		if (CollectUtil.isNullOrEmpty(totalBankFlowList)) {
			ShowUtil.show("数据库中没有银行流水记录");
			return;
		}
    	final CategoryAxis xAxis = new CategoryAxis();
    	final NumberAxis yAxis = new NumberAxis();
    	final StackedBarChart<String, Number> sbc = new StackedBarChart<>(xAxis, yAxis);
    	List<String> dayList = BankFlowMap.keySet().stream().collect(Collectors.toList());
    	xAxis.setCategories(FXCollections.<String>observableArrayList( dayList));
    	
    	double sum = NumUtil.getNumDivide(totalBankFlowList.stream().mapToDouble(BankFlowModel::getMoney).sum(), 10000d);
    	sbc.setTitle(String.format("历史总流水：￥%f万", sum));

    	Platform.runLater(() -> {
        	for(BankEnum bankEnum : BankEnum.values()) {
        		String bankName = bankEnum.getName();
        		final XYChart.Series<String, Number> series = new XYChart.Series<>();
        		series.setName(bankName);
        		BankFlowMap.forEach((day, dayMap) -> {
        			List<BankFlowModel> bankFlowList = dayMap.get(bankName);
        			int data = 0;
        			if(CollectUtil.isHaveValue(bankFlowList)) {
        				data = bankFlowList.stream().mapToInt(BankFlowModel::getMoney).sum();
        			}
        			series.getData().add(new XYChart.Data<>(day, data));
        		});
        		sbc.getData().addAll(series);
        	}
    	});
    	//==============================================================
		//grid.add(new Label("sdialog.getDialogPane().getButtonTypes().addAll(loginButtonType);"), 0, 0);
		grid.add(sbc,0, 1);
    	dialog.getDialogPane().setContent(grid);

    	dialog.show();

	}
  
  

}
