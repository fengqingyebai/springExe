package com.kendy.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.MaskerPane;

/**
 * StackPane遮罩层
 *
 * @author linzt
 * @date 2018-12-27
 */
public class MaskerPaneUtil {

  /**
   * 为StackPane添加遮罩层
   * @param stackPane
   */
  public static void addMaskerPane(StackPane stackPane) {
    if (stackPane != null) {
      ObservableList<Node> childrens = stackPane.getChildren();
      if (CollectUtil.isHaveValue(childrens)) {
        int lastIndex = childrens.size() - 1;
        Node node = childrens.get(lastIndex);
        boolean isMaskerPane = node instanceof MaskerPane;
        MaskerPane maskerPane = null;
        if (isMaskerPane) {
          maskerPane = (MaskerPane) node;
          maskerPane.setVisible(true);
          return;
        } else {
          maskerPane = new MaskerPane();
          maskerPane.setVisible(true);
          stackPane.getChildren().add(maskerPane);
        }
      }
    }
  }


  /**
   * 隐藏StackPane遮罩层
   *
   * @param stackPane
   */
  public static void hideMaskerPane(StackPane stackPane) {
    if (stackPane != null) {
      ObservableList<Node> childrens = stackPane.getChildren();
      if (CollectUtil.isHaveValue(childrens)) {
        int lastIndex = childrens.size() - 1;
        Node node = childrens.get(lastIndex);
        boolean isMaskerPane = node instanceof MaskerPane;
        MaskerPane maskerPane = null;
        if (isMaskerPane) {
          maskerPane = (MaskerPane) node;
        } else {
          maskerPane = new MaskerPane();
        }
        maskerPane.setVisible(false);
      }
    }
  }
}
