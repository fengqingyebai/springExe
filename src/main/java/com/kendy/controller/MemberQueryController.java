/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.kendy.entity.MemberZJInfo;
import com.kendy.service.MemberService;
import com.kendy.util.StringUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberQueryController extends BaseController implements Initializable {

  // ===============================================================会员查询Tab
  @FXML
  public TableView<MemberZJInfo> tableMemberZJ;
  @FXML
  public TableColumn<MemberZJInfo, String> memberJu;
  @FXML
  public TableColumn<MemberZJInfo, String> memberZJ;
  @FXML
  public Label memberDateStr;// 会员当天战绩的时间
  @FXML
  public Label memberPlayerId;// 会员当天战绩的会员ID
  @FXML
  public Label memberPlayerName;// 会员当天战绩的会员名称
  @FXML
  public Label memberSumOfZJ;// 会员当天战绩的战绩总和
  @FXML
  public Label memberTotalZJ;// 会员历史战绩的战绩总和
  @FXML
  public TextField memberSearchName;// 会员名称
  @FXML
  public ListView<String> memberListView;// 模糊搜索的人名列表

  @Autowired
  MyController myController;

  @Autowired
  public MemberService memberService; // 配帐控制类

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // 绑定会员查询中的会员当天战绩表
    bindCellValueByTable(new MemberZJInfo(), tableMemberZJ);

    // 会员服务类
    memberService.initMemberQuery(memberListView, tableMemberZJ, memberDateStr, memberPlayerId,
        memberPlayerName, memberSumOfZJ, memberTotalZJ);
  }


  /**
   * 会员搜索(按钮)
   */
  public void memberSearchAction(ActionEvent event) {
    memberService.setResult2ListView(memberSearchName, memberListView);
  }

  /**
   * 会员搜索(回车)
   */
  public void searchMemberByEnterEvent(KeyEvent event) {
    String keyWord = memberSearchName.getText();
    if (KeyCode.ENTER == event.getCode() && !StringUtil.isBlank(keyWord)) {
      memberService.setResult2ListView(memberSearchName, memberListView);
    }
  }



}
