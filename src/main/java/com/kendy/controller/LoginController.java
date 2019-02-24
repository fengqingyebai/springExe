/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.alibaba.fastjson.JSONObject;
import com.kendy.application.Main;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.exception.LoginException;
import com.kendy.util.ErrorUtil;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

@Component
public class LoginController extends BaseController implements Initializable {


  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  //    addSubTab("代理查询", "team_proxy_tab_frame.fxml");
//    addSubTab("实时上码系统", "shangma_tab_frame.fxml");
//    addSubTab("联盟对账", "LM_Tab_Fram.fxml");
//    addSubTab("联盟配账", "Quota_Tab_Fram.fxml");
//    addSubTab("股东贡献值", "gudong_contribution.fxml");
//    addSubTab("托管工具", "TG_toolaa.fxml");
//    addSubTab("自动上码配置", "SM_Autos.fxml");
//    addSubTab("银行流水", "bank_flow_frame.fxml");
//    addSubTab("历史统计", "history_static_tab_frame.fxml");
//    addSubTab("战绩统计", "zj_static_tab_frame.fxml");

  @FXML
  private void loginAction(ActionEvent event) throws LoginException {
    // TODO 请求远程登陆并返回权限数据
    JSONObject obj = JSONObject.parseObject(
        "{ \"info\":{ \"list\": [{  \"历史统计\":\"history_static_tab_frame\" }]}}");
    if (obj == null) {
      throwLoginException("帐号密码不存在！");
    }
    JSONObject info = obj.getJSONObject("info");
    if (info == null) {
      throwLoginException("该帐号无权限信息");
    }
    Object list = info.get("list");
    if (list == null) {
      throwLoginException("该帐号无权限信息");
    }
    List<Map<String, String>> pList = null;
    try {
      pList = (List<Map<String, String>>) list;
    } catch (Exception e) {
      throwLoginException("软件错误，请联系技术人员");
    }
    Map<String, String> permissions = pList.get(0);
    if (permissions != null) {
      DataConstans.permissions = permissions;
      logger.info("获取登陆权限：" + permissions);
    }
    try {
      Thread.sleep(3000);
      System.out.println("先睡三秒...");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    switchScene("/dialog/MainStageees2.fxml");
  }

  public void switchScene(String fxmlFile) {

    try {
      Parent root = (Parent) Main.loader.load(fxmlFile);
      FadeTransition fadeTransition = new FadeTransition();
      fadeTransition.setDuration(Duration.millis(200));
      fadeTransition.setNode(Main.root);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      fadeTransition.play();
      fadeTransition.setOnFinished((e) -> {
        Main.primaryStage0.setTitle(Constants.TITLE + Constants.VERSION);
        Main.primaryStage0.setScene(new Scene(root));
      });
    } catch (Exception e) {
      ErrorUtil.err("软件切换页面失败！");
    }

  }


  private void throwLoginException(String msg) throws LoginException {
    err(msg);
    throw new LoginException(msg);
  }


  private void err(String msg) {
    Platform.runLater(() -> {
      Notifications.create()
          .title("登陆")
          .darkStyle()
          .text(msg)
          .position(Pos.TOP_CENTER)
          .showError();
    });
  }

}
