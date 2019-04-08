/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.kendy.entity.WaizhaiInfo;
import com.kendy.exception.LoginException;
import com.kendy.service.WaizhaiService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaizhaiController extends BaseController implements Initializable {

  // ===============================================================外债
  @FXML
  public TableView<WaizhaiInfo> tableWaizhai;
  @FXML
  public TableColumn<WaizhaiInfo, String> waizhaiType;
  @FXML
  public TableColumn<WaizhaiInfo, String> waizhaiMoney;
  @FXML
  public HBox waizhaiHBox;// 里面包含多个表

  @Autowired
  WaizhaiService waizhaiService;

  @Autowired
  MyController myController;

  @Autowired
  ChangciController changciController;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // 绑定外债信息表
    bindCellValueByTable(new WaizhaiInfo(), tableWaizhai);
  }



  /**
   * 外债刷新按钮
   */
  public void waizhaiRefreshAction(ActionEvent event) {
    waizhaiService.generateWaizhaiTables(tableWaizhai, waizhaiHBox, changciController.tableCurrentMoneyInfo,
        changciController.tableTeam);
  }

}
