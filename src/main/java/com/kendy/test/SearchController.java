package com.kendy.test;


import java.net.URL;
import java.util.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import com.kendy.controller.tgController.TGController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SearchController implements Initializable {
  
  public SearchController(){
    System.out.println(" 执行SearchController 构造方法");
  }

 @Autowired
 private OrderService orderService;
 
 @Autowired
 private TGController tgController;
 
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
  if(orderService !=null) {
    System.out.println("orderService is not null !");
  }
  if(tgController !=null) {
    System.out.println("tgController is not null !");
  }
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