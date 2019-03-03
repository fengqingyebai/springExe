package com.kendy.util;

import com.kendy.application.Main;
import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Javafx Util
 */
public class FXUtil {

  public static Stage stage;

  // 成功信息
  public static ImageView okImg = new ImageView("images/ok.png");


  /**
   * 选择一个文件夹
   */
  public static File chooseDirFile() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setInitialDirectory(SystemUtil.getUserFile());
    return chooser.showDialog(stage);
  }


  /**
   * 选择一个Excel文件
   */
  public static File chooseExcelFile() {
    FileChooser fileChooser = new FileChooser();// 文件选择器
    fileChooser.setTitle("选择Excel文件");// 标题
    fileChooser.setInitialDirectory(SystemUtil.getUserFile());// 初始化根目标
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel", "*.xls?"));
    return fileChooser.showOpenDialog(stage);
  }

  public static Dialog<?> getBasicDialog(String title) {
    Dialog<?> dialog = new Dialog<>();
    dialog.setTitle(title);
    dialog.setHeaderText(null);
    ShowUtil.setIcon(dialog);
    return dialog;
  }

  public static Alert getBasicAlert(String content) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("提示");
    alert.setHeaderText(null);
    ShowUtil.setIcon(alert);
    if (StringUtil.isNotBlank(content)) {
      if (content.length() < 30) { // 换行
        content = System.lineSeparator() + content;
      }
      alert.setContentText(content);
    }
    return alert;
  }

  /**
   * 弹框确认
   */
  public static boolean confirm(String title, String content) {
    Alert alert = getBasicAlert(content);
    alert.setAlertType(AlertType.CONFIRMATION);
    if (StringUtil.isNotBlank(title)) {
      alert.setTitle(title);
    }
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  public static boolean confirm(String content) {
    return confirm(null, content);
  }


  /**
   * 成功信息
   */
  public static void info(String content) {
    Alert alert = getBasicAlert(content);
    alert.setTitle("成功");
    alert.setGraphic(okImg);
    alert.show();
  }


  /**
   * 切换页面
   * @param fxmlFile
   * @param title
   */
  public static void switchScene(String fxmlFile, String title) {

    try {
      Parent root = (Parent) Main.loader.load(fxmlFile);
      FadeTransition fadeTransition = new FadeTransition();
      fadeTransition.setDuration(Duration.millis(200));
      fadeTransition.setNode(Main.root);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      fadeTransition.play();
      fadeTransition.setOnFinished((e) -> {
        Main.primaryStage0.setTitle(title);
        Main.primaryStage0.setScene(new Scene(root));
        Main.primaryStage0.setResizable(!fxmlFile.contains("login"));
      });
    } catch (Exception e) {
      ErrorUtil.err("软件切换页面失败！", e);
    }

  }


}
