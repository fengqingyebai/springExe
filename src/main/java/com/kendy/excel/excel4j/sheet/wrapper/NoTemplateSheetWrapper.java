package com.kendy.excel.excel4j.sheet.wrapper;

import java.util.List;

/**
 * <p>无模板、基于注解导出的sheet包装类</p>
 * author : Crab2Died date : 2015/5/1  10:35
 */
public class NoTemplateSheetWrapper {

  /**
   * 待导出行数据
   */
  private List<?> data;

  /**
   * 基于注解的class
   */
  private Class clazz;

  /**
   * 是否写入表头
   */
  private boolean isWriteHeader;

  /**
   * sheet名
   */
  private String sheetName;

  public NoTemplateSheetWrapper(List<?> data, Class clazz) {
    this.data = data;
    this.clazz = clazz;
  }

  public NoTemplateSheetWrapper(List<?> data, Class clazz, boolean isWriteHeader) {
    this.data = data;
    this.clazz = clazz;
    this.isWriteHeader = isWriteHeader;
  }

  public NoTemplateSheetWrapper(List<?> data, Class clazz, boolean isWriteHeader,
      String sheetName) {
    this.data = data;
    this.clazz = clazz;
    this.isWriteHeader = isWriteHeader;
    this.sheetName = sheetName;
  }

  public List<?> getData() {
    return data;
  }

  public void setData(List<?> data) {
    this.data = data;
  }

  public Class getClazz() {
    return clazz;
  }

  public void setClazz(Class clazz) {
    this.clazz = clazz;
  }

  public boolean isWriteHeader() {
    return isWriteHeader;
  }

  public void setWriteHeader(boolean writeHeader) {
    isWriteHeader = writeHeader;
  }

  public String getSheetName() {
    return sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }
}
