/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.kendy.entity.JifenInfo;
import com.kendy.service.JifenService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JifenQueryController extends BaseController implements Initializable {

  // ============================================================================积分查询
  @FXML
  public TableView<JifenInfo> tableJifen;
  @FXML
  public TableColumn<JifenInfo, String> jfRank;
  @FXML
  public TableColumn<JifenInfo, String> jfPlayerName;
  @FXML
  public TableColumn<JifenInfo, String> jfValue;
  @FXML
  public DatePicker jfStartTime;
  @FXML
  public DatePicker jfEndTime;
  @FXML
  public ComboBox<String> jfTeamIDCombox;// 团队ID下拉框
  @FXML
  public TextField jifenInput;// 团队积分值
  @FXML
  public TextField jifenRankLimit;// 前50名
  @FXML
  public JFXCheckBox isCheckTeamProfitBox;// 勾选框：是否核算团队利润

  @Autowired
  JifenService jifenService;

  @Autowired
  MyController myController;


  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 绑定积查询表
    bindCellValueByTable(new JifenInfo(), tableJifen);

    // 积分查询
    jifenService.initjifenService(jfTeamIDCombox);



  }


  /**
   * 查询查询积分排名
   */
  @FXML
  public void jfQueryAciton(ActionEvent event) {
    String clubId = myController.getClubId();
    boolean isCheckTeamProfit = isCheckTeamProfitBox.isSelected();
    jifenService.jifenQuery(clubId, tableJifen, jfStartTime, jfEndTime, jifenInput, jifenRankLimit,
        jfTeamIDCombox, isCheckTeamProfit);
  }
}
