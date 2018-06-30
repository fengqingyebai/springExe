package com.kendy.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.kendy.db.DBUtil;
import com.kendy.entity.BankFlowInfo;
import com.kendy.model.BankFlowModel;
import com.kendy.util.CollectUtil;
import com.kendy.util.MapUtil;

import application.MyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

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
	
	
	private static final String DATE_LABEL_CSS = "dateLabel";
    private static final String FX_TEXT_FILL_WHITE = "-fx-text-fill:BLACK";
    private static final String ANIMATED_OPTION_BUTTON = "animated-option-button";
    private static final String ANIMATED_OPTION_SUB_BUTTON = "animated-option-sub-button";
    private static final String ANIMATED_OPTION_SUB_BUTTON2 = "animated-option-sub-button2";
    private static final String CENTER_CSS = "-fx-alignment: CENTER;";
    
	

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    LocalDate localDate = LocalDate.now();
    VBox cacheContent = new VBox();

    cacheContent.setPrefHeight(2000);
    
    List<BankFlowModel> totalBankFlowList = DBUtil.getAllHistoryBankMoney();
    if(CollectUtil.isNullOrEmpty(totalBankFlowList)) {
      log.debug("数据库中没有银行流水记录");
      return;
    }
    //{每一天 ：{银行类型 ： 上码列表}}
    Map<String, Map<String, List<BankFlowModel>>> BankFlowMap = totalBankFlowList.stream().collect(
        Collectors.groupingBy(BankFlowModel::getSoftTime, // 先按每一天分类
            Collectors.groupingBy(BankFlowModel::getBankName))); // 再按银行类型分类 
    
    //TODO 每一天进行排序
    
    //动态生成表
    long start = System.currentTimeMillis();
    BankFlowMap.forEach((dayKey, dayDataMap) -> {
      Label dateLabel = new Label(dayKey);
      dateLabel.setStyle("-fx-font-size: 1.3em; -fx-text-fill:	#5B5B5B");
      TableView<BankFlowInfo> table = dynamicGenerateGDTable();
      int tableHeight = 200;
      if("2018-06-29".equals(dayKey)) {
    	  tableHeight = 620;
      }
      table.setMinHeight(tableHeight);
      
      setTableData(table, dayDataMap);
      cacheContent.getChildren().add(dateLabel);
      cacheContent.getChildren().add(table);
      cacheContent.getChildren().add(new Label());
    });
    long end = System.currentTimeMillis();
    log.info(String.format("加载%d条流水进系统耗时：%d毫秒", totalBankFlowList.size(), end - start));
  
    scrollDates.setPadding(new Insets(10));
    scrollDates.setFitToHeight(true);
    scrollDates.setFitToWidth(true);

   // cacheContent.getChildren().addAll(nodesList);
    scrollDates.setContent(cacheContent);//.add(nodesList);
    
  }
  
  /**
   * 设置动态表的数据
   * @param table
   * @param todayMap {银行类型 ： 上码列表}
   */
  private void setTableData(TableView<BankFlowInfo> table, Map<String, List<BankFlowModel>> todayMap) {
    if(MapUtil.isNullOrEmpty(todayMap)) 
      return;
    int loopTimes = todayMap.values().stream().mapToInt(Collection::size).max().orElseGet(()-> 0);
    if(loopTimes > 0) {
      ObservableList<BankFlowInfo> obList= FXCollections.observableArrayList();
      
      for(int i=0; i< loopTimes ; i++) {
    	  BankFlowInfo info = new BankFlowInfo();
    	  info.setIndex(i+1+"");
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
		TableColumn<BankFlowInfo, String> indexCol = getTableColumn("序号", "index");
		table.getColumns().add(indexCol);
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
		col.setPrefWidth(82);
		col.setCellValueFactory(new PropertyValueFactory<BankFlowInfo, String>(colVal));
		col.setCellFactory(MyController.getColorCellFactory(new BankFlowInfo()));
		col.setSortable(false);
		return col;
	}
  
  
	/**
	 * 银行类型枚举类
	 * 
	 * @author 林泽涛
	 * @time 2018年6月28日
	 */
	private static enum BankEnum {
		YuEBao("余额宝", "yuEBao"), 
		HuaXia("华夏", "huaXia"), 
		PingAn("平安", "pingAn"), 
		ZhaoShang("招商", "zhaoShang"), 
		ZhiFuBao("支付宝", "zhiFuBao"), 
		PuFa("浦发", "puFa"), 
		XingYe("兴业", "xingYe");

		String name;
		String value;
		BankEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
	}
}
