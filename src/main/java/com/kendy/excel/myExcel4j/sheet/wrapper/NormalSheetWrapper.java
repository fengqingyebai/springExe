package com.kendy.excel.myExcel4j.sheet.wrapper;

import java.util.List;
import java.util.Map;

/**
 * <p>基于模板、注解的通用sheet包装类</p>
 * author : Crab2Died date : 2015/5/1  10:35
 */
public class NormalSheetWrapper {

  /**
   * sheet的序号
   */
  private int sheetIndex;

  /**
   * 表格行数据
   */
  private List<?> data;

  /**
   * 扩张数据
   */
  private Map<String, String> extendMap;

  /**
   * 基于注解的class
   */
  private Class clazz;

  /**
   * 是否写入表头
   */
  private boolean isWriteHeader;

  public NormalSheetWrapper(int sheetIndex, List<?> data, Class clazz) {
    this.sheetIndex = sheetIndex;
    this.data = data;
    this.clazz = clazz;
  }

  public NormalSheetWrapper(List<?> data, Class clazz) {
    this.data = data;
    this.clazz = clazz;
  }

  public NormalSheetWrapper(int sheetIndex, List<?> data, Map<String, String> extendMap,
      Class clazz,
      boolean isWriteHeader) {
    this.sheetIndex = sheetIndex;
    this.data = data;
    this.extendMap = extendMap;
    this.clazz = clazz;
    this.isWriteHeader = isWriteHeader;
  }

  public int getSheetIndex() {
    return sheetIndex;
  }

  public void setSheetIndex(int sheetIndex) {
    this.sheetIndex = sheetIndex;
  }

  public List<?> getData() {
    return data;
  }

  public void setData(List<?> data) {
    this.data = data;
  }

  public Map<String, String> getExtendMap() {
    return extendMap;
  }

  public void setExtendMap(Map<String, String> extendMap) {
    this.extendMap = extendMap;
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
}
