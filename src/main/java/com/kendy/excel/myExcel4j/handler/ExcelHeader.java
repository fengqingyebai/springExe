package com.kendy.excel.myExcel4j.handler;

import com.kendy.excel.myExcel4j.converter.ReadConvertible;
import com.kendy.excel.myExcel4j.converter.WriteConvertible;

/**
 * 功能说明: 用来存储Excel标题的对象，通过该对象可以获取标题和方法的对应关系
 */
public class ExcelHeader implements Comparable<ExcelHeader> {

  /**
   * excel的标题名称
   */
  private String title;

  /**
   * 每一个标题的顺序
   */
  private int order;

  /**
   * 写数据转换器
   */
  private WriteConvertible writeConverter;

  /**
   * 读数据转换器
   */
  private ReadConvertible readConverter;

  /**
   * 注解域
   */
  private String filed;

  /**
   * 属性类型
   */
  private Class<?> filedClazz;

  /**
   * 每一个标题的宽度(自定义)
   */
  private int colWidth;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public WriteConvertible getWriteConverter() {
    return writeConverter;
  }

  public void setWriteConverter(WriteConvertible writeConverter) {
    this.writeConverter = writeConverter;
  }

  public ReadConvertible getReadConverter() {
    return readConverter;
  }

  public void setReadConverter(ReadConvertible readConverter) {
    this.readConverter = readConverter;
  }

  public String getFiled() {
    return filed;
  }

  public void setFiled(String filed) {
    this.filed = filed;
  }

  public Class<?> getFiledClazz() {
    return filedClazz;
  }

  public void setFiledClazz(Class<?> filedClazz) {
    this.filedClazz = filedClazz;
  }


  public int getColWidth() {
    return colWidth;
  }

  public void setColWidth(int colWidth) {
    this.colWidth = colWidth;
  }

  @Override
  public int compareTo(ExcelHeader o) {
    return order - o.order;
  }

  public ExcelHeader() {
    super();
  }

  public ExcelHeader(String title, int order, WriteConvertible writeConverter,
      ReadConvertible readConverter, String filed, Class<?> filedClazz, int colWidth) {
    super();
    this.title = title;
    this.order = order;
    this.writeConverter = writeConverter;
    this.readConverter = readConverter;
    this.filed = filed;
    this.filedClazz = filedClazz;
    this.colWidth = colWidth;
  }
}
