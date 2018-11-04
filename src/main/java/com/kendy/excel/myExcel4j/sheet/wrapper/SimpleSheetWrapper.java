package com.kendy.excel.myExcel4j.sheet.wrapper;

import java.util.List;

/**
 * <p>无模板，无注解的简单sheet包装类</p>
 * author : Crab2Died date : 2015/5/1  10:35
 */
public class SimpleSheetWrapper {

  /**
   * 每个sheet的列表数据
   */
  private List<?> data;

  /**
   * 每个sheet的表头
   */
  private List<String> header;

  /**
   * 每个sheet的名字
   */
  private String sheetName;

  public SimpleSheetWrapper(List<?> data, List<String> header, String sheetName) {
    this.data = data;
    this.header = header;
    this.sheetName = sheetName;
  }

  public SimpleSheetWrapper(List<?> data, List<String> header) {
    this.data = data;
    this.header = header;
  }

  public SimpleSheetWrapper(List<?> data, String sheetName) {
    this.data = data;
    this.sheetName = sheetName;
  }

  public SimpleSheetWrapper(List<?> data) {
    this.data = data;
  }

  public List<?> getData() {
    return data;
  }

  public void setData(List<?> data) {
    this.data = data;
  }

  public List<String> getHeader() {
    return header;
  }

  public void setHeader(List<String> header) {
    this.header = header;
  }

  public String getSheetName() {
    return sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }
}
