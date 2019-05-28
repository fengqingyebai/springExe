package com.kendy.customize;

import com.kendy.excel.myExcel4j.MyExcelUtils;
import com.kendy.exception.ExcelException;
import com.kendy.util.ErrorUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义TableView
 * <p>
 * 应用场景： 用于对一个表的便捷导出
 * </p>
 *
 * @author linzt
 * @date 2018-10-21
 */
public class MyTable<K> extends TableView<K> {

  Logger logger = LoggerFactory.getLogger(getClass());

  private TableView<K> outerTable = null;

  private String excelName;

  private Class<K> entityClass;

  public MyTable() {
    super();
    // TODO 此处要获取到泛型S的Class
    // getRealType();
    // System.out.println("==================================泛型实例："+clazz.getName());

  }

  public MyTable(TableView outerTable, String excelName){
    this.outerTable = outerTable;
    this.excelName = excelName;
  }

  private void relectEntity(){

    try{
      ParameterizedType parameterizedType =
          (ParameterizedType) getClass().getGenericSuperclass();
      Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
      System.out.println("========="+actualTypeArgument);
      /*for (Type actualTypeArgument : actualTypeArguments) {
        if (actualTypeArgument instanceof Class<?>) {
          Class<?> clazz = (Class<?>) actualTypeArgument;
          if (Entity.class.isAssignableFrom(clazz)) {
            // 获取实体类类型
            entityClass = (Class<S>) clazz;
          }
        }
      }*/
    }catch (Exception e){
      System.err.println("反射失败");
      throw e;
    }
  }

  private Class<K> clazz;

  // 使用反射技术得到T的真实类型
  public Class getRealType(){
    // 获取当前new的对象的泛型的父类类型
    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    // 获取第一个类型参数的真实类型
    this.clazz = (Class<K>) pt.getActualTypeArguments()[0];
    return clazz;
  }

  public final ObservableList<K> getMyItems(){
    if(outerTable != null){
      return outerTable.getItems();
    }
    return super.getItems();
  }

  /**
   * 根据EntityClass导出Excellent
   * @see #exportByTable()
   * @throws Exception
   */
  public void export() throws Exception{
    this.export0(true);
  }


  /**
   * 根据TableView导出Excel数据
   * @throws Exception
   */
  private void export0(boolean isExportByClass) throws Exception{
    String errDesc = "导出的配置信息不完整！";
    String excelOutputPath = getFinalOutputPath();
    Objects.requireNonNull(excelName, errDesc);
    if (isExportByClass) {
      Objects.requireNonNull(entityClass, errDesc);
      MyExcelUtils.getInstance().exportObjects2Excel(super.getItems(), entityClass, excelOutputPath);

    } else {
      Objects.requireNonNull(outerTable, errDesc);
      MyExcelUtils.getInstance().exportObjects2Excel(outerTable.getItems(), outerTable, excelOutputPath);

    }
    java.awt.Desktop.getDesktop().open(new File(excelOutputPath));
  }

  private String getFinalOutputPath(){
    if (StringUtils.isNotBlank(this.excelName)) {
      excelName = excelName.replace("/", "").replace(":", ":/").replace("?", "");
    }
    return "D:/" + excelName + ".xlsx";
  }

  /**
   * 根据TableView导出
   */
  public  void exportByTable() {
    if (TableUtil.isHasValue(outerTable)) {
      try {
        logger.info("正在导出{}...", this.getExcelName());
        this.export0(false);
        logger.info("导出完成{}", this.getExcelName());
      } catch (Exception ee) {
        ErrorUtil.err("导出失败", ee);
      }
    }
  }

  public String getExcelName() {
    if (StringUtils.isBlank(excelName)) {
      return outerTable == null ? "myTable" : "outerTable";
    }
    return excelName;
  }

  public void setExcelName(String excelName) {
    this.excelName = excelName;
  }

  public void setEntityClass(Class<K> entityClass) {
    this.entityClass = entityClass;
  }

  public TableView<K> getOuterTable() {
    return outerTable;
  }

  public void setOuterTable(TableView<K> outerTable) {
    this.outerTable = outerTable;
  }
}




