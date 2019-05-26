package com.kendy.util;

import com.kendy.constant.Constants;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 展示工具类
 *
 * @author 林泽涛
 * @time 2017年11月18日 下午11:16:28
 */
public class ShowUtil {

  private static Logger log = LoggerFactory.getLogger(ShowUtil.class);

  /**
   * 带有自动关闭功能的提示框
   *
   * @time 2017年11月18日
   */
  public static void show(String message, int time) {
    int sleepTime = time * 1000;
    JOptionPane op =
        new JOptionPane(message + "\r\n" + time + "秒后自动关闭...", JOptionPane.INFORMATION_MESSAGE);
    final JDialog dialog = op.createDialog("提示");

    // 创建一个新计时器
    Timer timer = new Timer();

    // n秒 后执行该任务
    timer.schedule(new TimerTask() {
      public void run() {
        dialog.setVisible(false);
        dialog.dispose();
      }
    }, sleepTime);

    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setAlwaysOnTop(true);
    dialog.setModal(false);
    dialog.setVisible(true);

  }

  /**
   * 错误信息提示框
   *
   */
  public static void showInfo(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setGraphic(null);
    alert.setTitle("提示");
    alert.setHeaderText(null);
    alert.setContentText("\r\n" + message);
    setIcon(alert);
    alert.showAndWait();

  }


  /**
   * 错误信息提示框
   *
   * @time 2017年11月18日
   */
  public static void show(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("提示");
    alert.setHeaderText(null);
    alert.setContentText("\r\n" + message);
    setIcon(alert);
    alert.showAndWait();

  }

  public static void show(String title, boolean needNextLine, String message){
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title != null ? title : "提示");
    alert.setHeaderText(null);
    alert.setContentText(needNextLine ? "\r\n" + message : message);
    setIcon(alert);
    alert.showAndWait();
  }

  /**
   * 警告信息
   *
   * @time 2017年11月19日
   */
  public static void warn(String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("提示");
    alert.setHeaderText(null);
    alert.setContentText("\r\n" + message);
    setIcon(alert);
    alert.showAndWait();

  }


  /**
   * 设置弹框图标
   *
   * @time 2018年7月22日
   */
  public static void setIcon(Stage stage) {
    try {
      stage.getIcons().add(Constants.icon);
    } catch (Exception e) {
    }
  }

  /**
   * 针对Dialog类型的弹框图标设置
   *
   * @time 2018年7月22日
   */
  public static void setIcon(Dialog<?> dialog) {
    if (dialog instanceof TextInputDialog || dialog instanceof Alert) {
      dialog.initOwner(FXUtil.stage);
    } else {
      Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
      setIcon(stage);
    }
  }


}
