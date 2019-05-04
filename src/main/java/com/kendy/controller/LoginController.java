/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.kendy.constant.Constants;
import com.kendy.exception.LoginException;
import com.kendy.model.SoftUser;
import com.kendy.util.DateTimeUtils;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.HttpUtils;
import com.kendy.util.HttpUtils.HttpResult;
import com.kendy.util.MaskerPaneUtil;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginController extends BaseController implements Initializable {

  @FXML
  private TextField enNameField;

  @FXML
  private TextField codeField;

  @FXML
  private StackPane loginStackPane;

  @Autowired
  private MyController myController;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }


  @FXML
  private void loginAction(ActionEvent event) throws LoginException {
    MaskerPaneUtil.addMaskerPane(loginStackPane);
    Task task = new Task<Void>() {
      @Override
      public Void call() throws Exception {
        Thread.sleep(1500);
        Platform.runLater(() -> {
          // 具体逻辑
          String fxmlPath = "/dialog/MainStageees2.fxml";
          String title = Constants.TITLE + Constants.VERSION;
          try {
            doLogin(fxmlPath, title);
          } catch (LoginException e) {
          }
        });
        return null;
      }

      @Override
      protected void succeeded() {
        super.succeeded();
        MaskerPaneUtil.hideMaskerPane(loginStackPane);
      }
    };
    new Thread(task).start();
  }

  private Charset charset = Charset.forName("UTF-8");

  /**
   * 处理登陆的真正逻辑
   * @param fxmlFilePath
   * @param title
   * @throws LoginException
   */
  private void doLogin(String fxmlFilePath, String title) throws LoginException{
    // T请求远程登陆并返回权限数据
    HttpResult httpResult = null;
    try {
      if (enNameField == null || codeField == null) {
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
      header.put("Content-Type", "application/json;charset=utf-8");
      header.put("Accept", "application/json");
      httpResult = HttpUtils.getInstance().post("http://193.112.224.86:8009/bpItf/getInfo",
          charset, bodyJson, header, 20000);
      if (httpResult.isOK()) {
        if (StringUtils.isNotBlank(httpResult.getContent())) {
          logger.info("获取登陆信息：" + httpResult.getContent());
        }
      } else {
        throw new LoginException("httpResult not OK");
      }
    } catch (Exception e) {
      ErrorUtil.err("登陆失败", e);
      return;
    }

    String content = httpResult.getContent();

    SoftUser softUser = JSON.parseObject(content, SoftUser.class);

    if (softUser == null) {
      throwLoginException("登陆失败，账号密码错误!");
      return;
    }
    String status = softUser.getStatus();
    if (StringUtils.equals("停用", status)) {
      throwLoginException("该帐号已经停用，请联系管理员");
      return;
    }
    // 检验是否过期
    String deadTime = softUser.getDeadTime();
    if (StringUtils.isNotBlank(deadTime)) {
      try {
        LocalDate localDate = DateTimeUtils.parseLocalDate(deadTime, "yyyy-MM-dd");
        LocalDate finalDeadDate = localDate.plusDays(1);
        if (!finalDeadDate.isAfter(LocalDate.now())) {
          ErrorUtil.err("该帐号已经超过有效期" + deadTime);
          return;
        }
      } catch (Exception e) {
        ErrorUtil.err("检验帐号是否过期失败", e);
        return;
      }

    }
    String permissionStr = softUser.getPermission();
    if (StringUtils.isBlank(permissionStr)) {
      ErrorUtil.err("该帐号没有配置权限");
      return;
    }

    // 切换页面
    FXUtil.switchScene(fxmlFilePath, title);
  }



  private void throwLoginException(String msg) throws LoginException {
    err(msg);
    throw new LoginException(msg);
  }


  private void err(String msg) {
    Platform.runLater(() -> {
      Notifications.create()
          .title("错误提示")
          .darkStyle()
          .text(msg)
          .position(Pos.TOP_CENTER)
          .showError();
    });
  }

}
