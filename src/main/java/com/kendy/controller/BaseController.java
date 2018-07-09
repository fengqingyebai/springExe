package com.kendy.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kendy.annotation.Mycolumn;
import com.kendy.excel.excel4j.handler.ExcelHeader;
import com.kendy.exception.BindColumnException;
import com.kendy.interfaces.Entity;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * 基础控制类
 * 
 * @author linzt
 * @time 2018年7月6日 
 */
public abstract class BaseController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  
  //提示：子类应该在自己的构造方法中自动去实现各个表格的初始化，而不是显示调用
  
  /**
   * 绑定多个表格的列
   * @param tables
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> void bindCellValueByTables2(TableView<T>... tables) {
    Class<?> clz = getSubClass();
    List<ExcelHeader> headers = new ArrayList<>();
    List<Field> fields = new ArrayList<>();
    for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
    }
    for (Field field : fields) {
        // 是否使用自定义注解
        if (field.isAnnotationPresent(Mycolumn.class)) {
            Mycolumn myColumn = field.getAnnotation(Mycolumn.class);
            try {
              boolean noNeedRedColumn = myColumn.noNeedRedColumn();
            } catch (Exception e) {
                //throw new BindColumnException();
            }
        }
    }
    try {
      for(TableView<T> table : tables) {
        ObservableList<TableColumn<T, ?>> columns =  table.getColumns();
        for(TableColumn<T, ?> column : columns) {
          String fxId = column.getId();
          column.setCellValueFactory( new PropertyValueFactory(fxId));
          column.setStyle("-fx-alignment: CENTER;");
          column.setSortable(false);//禁止排序
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("小林：绑定列值失败");
    }
  }
  
  
  
  /**
   * 绑定多个表格的列
   * @param tables
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> void bindCellValueByTables(TableView<T>... tables) {
    try {
      for(TableView<T> table : tables) {
        ObservableList<TableColumn<T, ?>> columns =  table.getColumns();
        for(TableColumn<T, ?> column : columns) {
          String fxId = column.getId();
          column.setCellValueFactory( new PropertyValueFactory(fxId));
          column.setStyle("-fx-alignment: CENTER;");
          column.setSortable(false);//禁止排序
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("小林：绑定列值失败");
    }
  }
  
  /**
   * 绑定数据域
   * @param colums TableColumn 可变参数
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public  void bindCellValue(TableColumn<? extends Entity,String>... colums){
      try {
          for(TableColumn column : colums){
              String fxId = column.getId();
              column.setCellValueFactory(
                      new PropertyValueFactory<Entity,String>(fxId)
                      );
              column.setStyle("-fx-alignment: CENTER;");
              column.setSortable(false);//禁止排序
          }
      } catch (Exception e) {
          throw new RuntimeException("小林：绑定列值失败");
      }
  }
  
  /**
   * 父类获取子类
   * 
   * @return
   */
  abstract Class<?> getSubClass();
  
  
  
  
  
}
