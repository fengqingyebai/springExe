package com.kendy.test;


import java.net.URL;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Component
public class SearchController implements Initializable {

  private Logger loger = LoggerFactory.getLogger(SearchController.class);

  @Autowired
  private OrderService orderService;

  public SearchController() {
    loger.info(" 执行SearchController 构造方法");
    if (orderService != null) {
      loger.info("orderService is not null");
    } else {
      loger.info("orderService is  null....");
    }
  }


  @PostConstruct
  public void inits() {
    loger.info("正在初始化SearchController构造方法后的初始化");
    if (orderService != null) {
      loger.info("@PostConstruct加载后orderService is not null");
    } else {
      loger.info("@PostConstructxml加载后orderService is  null....");
    }
  }


  @FXML
  private Button search;
  @FXML
  private TableView<Order> table;
  @FXML
  private TextField productName;
  @FXML
  private TextField minPrice;
  @FXML
  private TextField maxPrice;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    if (orderService != null) {
      loger.info("xml加载后orderService is not null !");
    } else {
      loger.info("xml加载后orderService is  null !");
    }
//  if(tgController !=null) {
//    loger.info("tgController is not null !");
//  }else {
//    loger.info("tgController is  null !");
//  }
  }

  public void search() {
//  OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
//  orderSearchCriteria.setProductName(productName.getText());
//  orderSearchCriteria
//    .setMaxPrice(StringUtils.isEmpty(minPrice.getText()) ? null:new BigDecimal(minPrice.getText()));
//  orderSearchCriteria
//    .setMinPrice(StringUtils.isEmpty(minPrice.getText()) ? null: new BigDecimal(minPrice.getText()));
//  ObservableList<Order> rows = FXCollections.observableArrayList();
//  rows.addAll(orderService.findOrders(orderSearchCriteria));
//  table.setItems(rows);
  }

  public void clear() {
    table.setItems(null);
    productName.setText("");
    minPrice.setText("");
    maxPrice.setText("");
  }
}