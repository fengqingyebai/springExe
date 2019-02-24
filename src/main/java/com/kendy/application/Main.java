package com.kendy.application;

import com.kendy.constant.Constants;
import com.kendy.controller.LoginController;
import com.kendy.util.ErrorUtil;
import com.kendy.util.FXUtil;
import com.kendy.util.IPUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.sun.xml.internal.bind.v2.TODO;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 程序主窗口
 *
 * @author 林泽涛
 * @time 2017年10月21日 下午10:01:39
 */
public class Main extends Application {

  public static final SpringFxmlLoader loader = new SpringFxmlLoader();

  private Logger log = LoggerFactory.getLogger(Main.class);

  @Autowired
  LoginController loginController;

  public static Parent root;
  // 共用窗口
  public static Stage primaryStage0;

  @Override
  public void start(Stage primaryStage) {


    try {
      log.info("进入Main.start方法......");
      root = (Parent) loader.load("/dialog/login.fxml");
      // root = (Parent) loader.load("/dialog/MainStageees2.fxml");
      primaryStage.getIcons().add(Constants.icon);
      primaryStage.setTitle("登录");
      //primaryStage.setTitle(Constants.TITLE + Constants.VERSION);
      Scene scene = new Scene(root);
      scene.getStylesheets().add("css/myCss.css");
      primaryStage.setScene(scene);
      primaryStage.show();
      primaryStage0 = primaryStage;
      FXUtil.stage = primaryStage;
      primaryStage.setOnCloseRequest(e -> {
        exit();
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    launch(args);
  }

  public void exit() {
    log.info("====================即将关闭所有程序！");
    System.exit(0);
  }


}
