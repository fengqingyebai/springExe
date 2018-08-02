package com.kendy.util;

import java.io.File;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXUtil {
  
  public static Stage stage;
  
  // 成功信息
  public static ImageView okImg = new ImageView("images/ok.png");
  
  /**
   * 选择一个文件夹
   * 
   * @return
   */
  public static File chooseDirFile() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    return chooser.showDialog(stage);
  }
  
  
  /**
   * 选择一个Excel文件
   * 
   * @param title
   * @param stage
   * @return
   */
  public static File chooseExcelFile() {
    FileChooser fileChooser = new FileChooser();// 文件选择器
    fileChooser.setTitle("选择Excel文件");// 标题
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));// 初始化根目标
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel", "*.xls?"));
    return  fileChooser.showOpenDialog(stage);
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
   * @param title
   * @param content
   * @return
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
   * @param content
   */
  public static void info(String content) {
    Alert alert = getBasicAlert(content);
    alert.setTitle("成功");
    alert.setGraphic(okImg);
    alert.show();
  }
  
  

}
