package com.kendy.util;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXUtil {
  
  /**
   * 选择一个文件夹
   * 
   * @param title
   * @param stage
   * @return
   */
  public File chooseDirFile(String title, Stage stage) {
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
  public File chooseExcelFile(String title, Stage stage) {
    FileChooser fileChooser = new FileChooser();// 文件选择器
    fileChooser.setTitle(title);// 标题
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));// 初始化根目标
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel", "*.xls?"));
    return  fileChooser.showOpenDialog(stage);
  }

}
