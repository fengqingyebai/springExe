package com.kendy.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * 封装Alert组件
 * 
 * @author 林泽涛
 * @time 2018年7月22日 下午12:44:50
 */
public class AlertUtil {
  

  public static boolean confirm(String title, String content) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("提示");
    alert.setHeaderText(null);
    ShowUtil.setIcon(alert);
    if (StringUtil.isNotBlank(title)) {
      alert.setTitle(title);
    }
    if (StringUtil.isNotBlank(content)) {
      if (content.length() < 30) { // 换行
        content = System.lineSeparator() + content;
      }
      alert.setContentText(content);
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
  
  
  
  
}
