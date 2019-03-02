/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kendy.application.Main;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.exception.LoginException;
import com.kendy.model.SoftUser;
import com.kendy.util.ErrorUtil;
import com.kendy.util.HttpUtils;
import com.kendy.util.HttpUtils.HttpResult;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
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
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

@Component
public class LoginController extends BaseController implements Initializable {

  @FXML
  private TextField enNameField;

  @FXML
  private TextField codeField;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }


  @FXML
  private void loginAction(ActionEvent event) throws LoginException {
    // TODO 请求远程登陆并返回权限数据
//    JSONObject obj = JSONObject.parseObject(
//        "{ \"info\":{ \"list\": [{  \"历史统计\":\"history_static_tab_frame\" }]}}");
//    if (obj == null) {
//      throwLoginException("帐号密码不存在！");
//    }
    HttpResult httpResult = null;
    try {
      if (enNameField == null || codeField == null){
        ErrorUtil.err("哥，账号密码不能为空！");
        return;
      }
      String enName = enNameField.getText();
      String code = codeField.getText();
      if (enNameField == null || StringUtils.isBlank(enName)) {
        ErrorUtil.err("登陆账号不能为空！");
        return;
      }
      if (StringUtils.isBlank(code)) {
        ErrorUtil.err("登陆密码不能为空！");
        return;
      }
      Map<String, String> param = new HashMap<>();
      param.put("enName", enName);
      param.put("code", code);
      String bodyJson = JSON.toJSONString(param);
      Map<String, String> header = new HashMap<>();
      header.put("content-type", "application/json;charset:utf-8");
      httpResult = HttpUtils.getInstance().post("http://localhost:8009/bpItf/getInfo",
          Charset.defaultCharset(),
          bodyJson, header, 20000);
      if (httpResult.isOK()) {
        logger.info("获取登陆信息：", JSON.toJSONString(httpResult));

      } else {
        throw new LoginException("httpResult not OK");
      }
    } catch (Exception e) {
      ErrorUtil.err("登陆失败", e);
      return;
    }

    String content = httpResult.getContent();

    SoftUser softUser = JSON.parseObject(content, SoftUser.class);

    JSONObject obj = JSON.parseObject(content);


    JSONObject info = obj.getJSONObject("info");
    if (info == null) {
      throwLoginException("该帐号无权限信息");
      return;
    }
    Object list = info.get("list");
    if (list == null) {
      throwLoginException("该帐号无权限信息");
      return;
    }
    List<Map<String, String>> pList = null;
    try {
      pList = (List<Map<String, String>>) list;
    } catch (Exception e) {
      throwLoginException("软件错误，请联系技术人员");
      return;
    }
    Map<String, String> permissions = pList.get(0);
    if (permissions != null) {
      DataConstans.permissions = permissions;
      logger.info("获取登陆权限：" + permissions);
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
