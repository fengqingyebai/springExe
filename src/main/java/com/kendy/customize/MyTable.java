package com.kendy.customize;

import com.kendy.excel.excel4j.ExcelUtils;
import com.kendy.exception.ExcelException;
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.apache.commons.lang3.StringUtils;

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

  private String excelName;

  private Class<K> entityClass;

  public MyTable() {
    super();
    // TODO 此处要获取到泛型S的Class
    // getRealType();
    // System.out.println("==================================泛型实例："+clazz.getName());

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


  public void export() throws Exception{
    final ObservableList<K> items = getItems();

    if(StringUtils.isBlank(excelName) || entityClass == null){
      throw new ExcelException("导出的配置信息不完整！");
    }
    String excelOutputPath = "D:/" + excelName + ".xlsx";
    ExcelUtils.getInstance().exportObjects2Excel(items, entityClass, excelOutputPath);
    java.awt.Desktop.getDesktop().open(new File(excelOutputPath));
  }

  public String getExcelName() {
    return excelName;
  }

  public void setExcelName(String excelName) {
    this.excelName = excelName;
  }

  public void setEntityClass(Class<K> entityClass) {
    this.entityClass = entityClass;
  }

}




