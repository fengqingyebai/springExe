package com.kendy.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import com.kendy.db.DBUtil;
import com.kendy.entity.BankFlowInfo;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.Record;
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

    cacheContent.setPrefHeight(1000);
    
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
    int size = BankFlowMap.size();
    BankFlowMap.forEach((dayKey, dayDataMap) -> {
      Label dateLabel = new Label(dayKey);
      dateLabel.setStyle("-fx-font-size: 1.75em; -fx-text-fill:blue");
      TableView<BankFlowInfo> table = dynamicGenerateGDTable();
      table.setMinHeight(200);
      
      setTableData(table, dayDataMap);
      cacheContent.getChildren().add(dateLabel);
      cacheContent.getChildren().add(table);
      cacheContent.getChildren().add(new Label());
   });
  
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
    	  try { info.setYuEBao(todayMap.get(BankEnum.YuEBao.getName()).get(i).getMoney()+"");} catch (Exception e) { }
    	  try { info.setHuaXia(todayMap.get(BankEnum.HuaXia.getName()).get(i).getMoney()+"");} catch (Exception e) { }
    	  try { info.setPingAn(todayMap.get(BankEnum.PingAn.getName()).get(i).getMoney()+"");} catch (Exception e) { }
    	  try { info.setZhaoShang(todayMap.get(BankEnum.ZhaoShang.getName()).get(i).getMoney()+"");} catch (Exception e) { }
    	  try { info.setZhiFuBao(todayMap.get(BankEnum.ZhiFuBao.getName()).get(i).getMoney()+"");} catch (Exception e) { }
    	  try { info.setPuFa(todayMap.get(BankEnum.PuFa.getName()).get(i).getMoney()+"");} catch (Exception e) { }
    	  obList.add(info);
      }
      table.setItems(obList);
    }
  }
  
  
  /**
   * 动态生成表格
   */
  @SuppressWarnings("unchecked")
  private TableView<BankFlowInfo> dynamicGenerateGDTable() {
     TableColumn<BankFlowInfo,String> yuEBaoCol = getTableColumn(BankEnum.YuEBao.getName(), BankEnum.YuEBao.getValue()); 
     TableColumn<BankFlowInfo,String> huaXiaCol = getTableColumn(BankEnum.HuaXia.getName(), BankEnum.HuaXia.getValue()); 
     TableColumn<BankFlowInfo,String> pingAnCol = getTableColumn(BankEnum.PingAn.getName(), BankEnum.PingAn.getValue()); 
     TableColumn<BankFlowInfo,String> zhaoShangCol = getTableColumn(BankEnum.ZhaoShang.getName(), BankEnum.ZhaoShang.getValue()); 
     TableColumn<BankFlowInfo,String> zhiFuBaoCol = getTableColumn(BankEnum.ZhiFuBao.getName(), BankEnum.ZhiFuBao.getValue()); 
     TableColumn<BankFlowInfo,String> puFaCol = getTableColumn(BankEnum.PuFa.getName(), BankEnum.PuFa.getValue()); 
     TableView<BankFlowInfo> table = new TableView<BankFlowInfo>();
     table.setPrefWidth(210);
     table.getColumns().addAll(yuEBaoCol, huaXiaCol, pingAnCol, zhaoShangCol, zhiFuBaoCol, puFaCol);
     return table;
  }
  
  /**
   * 动态生成列
   */
  private TableColumn<BankFlowInfo,String> getTableColumn(String colName, String colVal) {
    TableColumn<BankFlowInfo, String> col = new TableColumn<>(colName);
    col.setStyle(CENTER_CSS);
    col.setPrefWidth(90);
    col.setCellValueFactory(new PropertyValueFactory<BankFlowInfo, String>(colVal));
    col.setCellFactory(MyController.getColorCellFactory(new BankFlowInfo()));
    return col;
  }
  
  
  /**
   * 银行类型枚举类
   * @author 林泽涛
   * @time 2018年6月28日 下午11:34:00
   */
  private static enum BankEnum {
	  YuEBao("余额宝", "yuEBao"),
	  HuaXia("华夏", "huaXia"),
	  PingAn("平安", "pingAn"),
	  ZhaoShang("招商", "zhaoShang"),
	  ZhiFuBao("支付宝", "zhiFuBao"),
	  PuFa("浦发", "puFa")
	  ;
	  
	  String  name;
	  String value;
	  BankEnum(String name, String value){
		  this.name = name;
		  this.value = value;
	  }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	  
  }
	
    
	
}
