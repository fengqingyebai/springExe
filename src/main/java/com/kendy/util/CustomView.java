package com.kendy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kendy.application.Main;
import com.kendy.constant.DataConstans;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CustomView {
  
  private  Logger logger = LoggerFactory.getLogger(getClass());
  
  public static CustomView instance = new CustomView();
  
  @Autowired
  DataConstans dataConstants;

  public void openBasedDialog(String path, String title, String windowName) {
    try {
      if (dataConstants.framesNameMap.get(windowName) == null) {
        // 打开新对话框
        String filePath = "/dialog/" + path;
        //Parent root = FXMLLoader.load(getClass().getResource(filePath));
        Parent root = (Parent) Main.loader.load(filePath);
        Stage addNewPlayerWindow = new Stage();
        Scene scene = new Scene(root);
        addNewPlayerWindow.setTitle(title);
        addNewPlayerWindow.setScene(scene);
        try {
          addNewPlayerWindow.getIcons()
              .add(new javafx.scene.image.Image("file:resource/images/icon.png"));
        } catch (Exception e) {
          logger.debug("找不到icon图标！");
          e.printStackTrace();
        }
        addNewPlayerWindow.show();
        // 缓存该对话框实例
        dataConstants.framesNameMap.put(windowName, addNewPlayerWindow);
        addNewPlayerWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
            dataConstants.framesNameMap.remove(windowName);
          }
        });

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
