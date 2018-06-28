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
	

	
//	@FXML private Label currentTGCompanyLabel; //当前托管公司
	@FXML private Label currentTGTeamLabel; //当前托管团队
	
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
    //2111
    //Map<String, Long> groups = students.stream().collect(Collectors.groupingBy(Student::getSchool, Collectors.counting()));
    Map<String, Long> collect = totalBankFlowList.stream().collect( Collectors.groupingBy(BankFlowModel::getBankName , Collectors.counting()));
    
    log.debug("result:" + collect);
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
    
//  ObservableList<BankFlowInfo> obList= FXCollections.observableArrayList();
//  for(String gudongName : _gudongList) {
//
//      //设置数据
//      //{团队ID:List<Record}
//      Map<String,List<Record>> teamMap = gudongTeamMap.get(gudongName);
//      List<KaixiaoInfo> kaixiaoList = gudongKaixiaoMap.get(gudongName);
//      setDynamicTableData(table,teamMap, kaixiaoList, gudongName);
//      //往左边的股东表中添加记录
//      setDataToSumTable(table);
//      
//      contributionHBox.setSpacing(5);
//      contributionHBox.setPadding(new Insets(0, 0, 0, 0));
//      contributionHBox.getChildren().addAll(table);
//  }
    
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
      BankFlowInfo info = new BankFlowInfo();
      info.setYuEBao("100");
      obList.add(info);
      table.setItems(obList);
    }
  }
  
  
  /**
   * 动态生成表格
   */
  @SuppressWarnings("unchecked")
  private TableView<BankFlowInfo> dynamicGenerateGDTable() {
     TableColumn<BankFlowInfo,String> yuEBaoCol = getTableColumn("余额宝", "yuEBao"); 
     TableColumn<BankFlowInfo,String> huaXiaCol = getTableColumn("华夏", "huaXia"); 
     TableColumn<BankFlowInfo,String> pingAnCol = getTableColumn("平安", "pingAn"); 
     TableColumn<BankFlowInfo,String> zhaoShangCol = getTableColumn("招商", "zhaoShang"); 
     TableColumn<BankFlowInfo,String> zhiFuBaoCol = getTableColumn("支付宝", "zhiFuBao"); 
     TableColumn<BankFlowInfo,String> puFaCol = getTableColumn("浦发", "puFa"); 
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
	
    
	
}