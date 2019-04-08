/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.kendy.entity.DangtianHuizongInfo;
import com.kendy.entity.WaizhaiInfo;
import com.kendy.entity.ZonghuiInfo;
import com.kendy.entity.ZonghuiKaixiaoInfo;
import com.kendy.service.WaizhaiService;
import com.kendy.service.ZonghuiService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZonghuiController extends BaseController implements Initializable {

  // ===============================================================总汇主表
  @FXML
  public TableView<ZonghuiInfo> tableZonghui;
  @FXML
  public TableColumn<ZonghuiInfo, String> zonghuiTabelId;
  @FXML
  public TableColumn<ZonghuiInfo, String> zonghuiFuwufei;
  @FXML
  public TableColumn<ZonghuiInfo, String> zonghuiBaoxian;
  @FXML
  public TableColumn<ZonghuiInfo, String> zonghuiHuishui;
  @FXML
  public TableColumn<ZonghuiInfo, String> zonghuiHuiBao;
  @FXML
  public ListView<String> juTypeListView;// 局类型ListView


  //======================================================总汇当天
  @FXML
  public TableView<DangtianHuizongInfo> tableDangtianHuizong;
  @FXML
  public TableColumn<DangtianHuizongInfo, String> huizongType;
  @FXML
  public TableColumn<DangtianHuizongInfo, String> huizongMoney;


  //======================================================总汇开销
  @FXML
  public TableView<ZonghuiKaixiaoInfo> tableZonghuiKaixiao;
  @FXML
  public TableColumn<ZonghuiKaixiaoInfo, String> zonghuiKaixiaoType;
  @FXML
  public TableColumn<ZonghuiKaixiaoInfo, String> zonghuiKaixiaoMoney;

  @Autowired
  MyController myController;

  @Autowired
  ChangciController changciController;

  @Autowired
  ZonghuiService zonghuiService;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // 绑定外债信息表
    // 绑定汇总信息表（当天每一局的团队汇总查询）
    bindCellValueByTable(new ZonghuiInfo(), tableZonghui);
    // 绑定汇总查询中的当天汇总表
    bindCellValueByTable(new DangtianHuizongInfo(), tableDangtianHuizong);
    // 绑定汇总查询中的开销表表
    bindCellValueByTable(new ZonghuiKaixiaoInfo(), tableZonghuiKaixiao);

    // 总汇表中的初始化
    juTypeListView.getItems().add("合局");
  }

  /**
   * 总汇刷新按钮
   */
  public void zonghuiRefreshAction(ActionEvent event) {
    zonghuiService.refreHuizongTable(tableZonghui, tableDangtianHuizong, tableZonghuiKaixiao,
        changciController.tableProfit);
  }



}
