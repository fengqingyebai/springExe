package com.kendy.util;

import com.jfoenix.controls.JFXButton;
import com.kendy.customize.MyTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author linzt
 * @date
 */
public class ButtonUtil {

  final static Logger logger = LoggerFactory.getLogger(ButtonUtil.class);

  public static JFXButton getDownloadButn(MyTable<?> table) {
    JFXButton exportBtn = new JFXButton("导出");
    exportBtn.setStyle("-fx-background-color: #DAF2E3; -fx-font-size: 20");
    exportBtn.setPrefWidth(120);
    exportBtn.setPrefHeight(40);
    exportBtn.setId("exportBtn");
    exportBtn.setOnAction(e -> {
      if (TableUtil.isHasValue(table)) {
        try {
          logger.info("正在导出{}...", table.getExcelName());
          table.export();
          logger.info("导出完成{}" + table.getExcelName());
        } catch (Exception ee) {
          ErrorUtil.err("导出失败", ee);
        }
      }
    });
    return exportBtn;
  }
}
