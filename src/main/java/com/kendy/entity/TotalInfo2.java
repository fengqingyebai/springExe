package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author linzt
 * @date
 */
public class TotalInfo2 extends TotalInfo {

  @MyExcelField(title = "桌号")
  private SimpleStringProperty tableId = new SimpleStringProperty();

  public String getTableId() {
    return tableId.get();
  }

  public SimpleStringProperty tableIdProperty() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId.set(tableId);
  }
}
