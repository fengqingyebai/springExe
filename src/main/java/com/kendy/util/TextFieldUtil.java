package com.kendy.util;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

/**
 * @author linzt
 * @date
 */
public class TextFieldUtil {
  /**
   * 从输入框中获取列表获取列表
   */
  public static List<String> defaultSplit(TextField textField) {
    List<String> _types = new ArrayList<>();
    String typeText = textField.getText();
    if (StringUtils.isNotBlank(typeText)) {
      for (String type : typeText.trim().split("##")) {
        _types.add(type);
      }
    }
    return _types;
  }

  /**
   * 使用##进行切割
   * @param textField
   * @param list
   */
  public static void setDefaultContent(TextField textField, List<String> list) {
    if (textField != null && list != null) {
      String content = String.join("##", list);
      textField.setText(content);
    }
  }

}
