package com.kendy.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kendy.annotation.Mycolumn;
import com.kendy.application.SpringFxmlLoader;
import com.kendy.interfaces.Entity;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * 基础控制类
 * 
 * @author linzt
 * @time 2018年7月6日
 */
@Component
public class BaseController{

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  
  public void setInitialize(Object... baseControllers) {
    for(Object controller : baseControllers) {
      controller = SpringFxmlLoader.getContext().getBean(controller.getClass());
    }
  }
  
  
  
  // // T指代表实体
  // private T entity;

  // 提示：子类应该在自己的构造方法中自动去实现各个表格的初始化，而不是显示调用

  /**
   * 绑定多个表格的列,由子实例完成后自动触发
   */
   public BaseController() {
     logger.info("正在初始化父类bindTableColumnValue方法....");
     Class<?> clz = getSubClass();
     if(clz != null) {
       logger.info("子类:"+clz.getName());
       List<Field> fields = new ArrayList<>();
       for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
         fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
       }
       logger.info("开始>>>------------------------------------------------");
       logger.info("正在获取子类{}属性....", clz.getName());
       for (Field field : fields) {
         // 是否使用自定义注解
         if (field.isAnnotationPresent(Autowired.class)) {
           Class<?> fieldType = field.getType();
           logger.info("检测:"+fieldType.getName());
           if(fieldType == TableView.class) {
           String name = field.getName();
           logger.info("检测到表格"+name);
           }
         }
       }
       logger.info("------------------------------------------------<<<结束");
     }
   }



//  private static int count = 1;
//  /**
//   * 
//   */
//  public BaseController() {
//    super();
//    logger.info(" BaseController构造方法。。。"+ count++);
//  }

  /**
   * 绑定多个表格的列
   * 
   * @param tables
   */
  public <T> void bindCellValueByTable(T entity, TableView<T> table) {
    try {
      List<TableColumn<T, ?>> columns = table.getColumns();
      bindCellValues(entity, columns);
    } catch (Exception e) {
      throw new RuntimeException("小林：绑定列值失败");
    }
  }

  /**
   * 绑定数据域
   * 
   * @param colums TableColumn 可变参数
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T extends Entity> void bindCellValue(T entity, TableColumn<T, ?>... colums) {
    for (TableColumn column : colums) {
      bindSingleCellValues(entity, column);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> void bindCellValues(T entity, List<TableColumn<T, ?>> colums) {
    for (TableColumn column : colums) {
      bindSingleCellValues(entity, column);
    }
  }

  public <T> void bindSingleCellValues(T entity, TableColumn<T, String> column) {
    String fxId = column.getId();
    column.setCellValueFactory(new PropertyValueFactory<T, String>(fxId));
    column.setStyle("-fx-alignment: CENTER;");
    column.setSortable(false);// 禁止排序
    if (entity != null) {
      column.setCellFactory(getColorCellFactory(entity));
    }
  }


  public <T> Callback<TableColumn<T, String>, TableCell<T, String>> getColorCellFactory(
      T t) {
    return new Callback<TableColumn<T, String>, TableCell<T, String>>() {
      public TableCell<T, String> call(TableColumn<T, String> param) {
        TableCell<T, String> cell = new TableCell<T, String>() {
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            this.setTextFill(null);
            if (!isEmpty() && item != null) {
              if (item.contains("-")) {
                this.setTextFill(Color.RED);
              } else {
                this.setTextFill(Color.BLACK);
              }
              setText(item);
            }
          }
        };
        cell.setEditable(false);// 不让其可编辑
        return cell;
      }
    };
  }



  //
  // public T getEntity() {
  // return entity;
  // }
  //
  // public void setEntity(T entity) {
  // this.entity = entity;
  // }

   /**
   * 父类获取子类Class
   * @author linzt
   */
   public Class<?> getSubClass(){ return null;};
  //
  // /**
  // * 父类获取子类的实例对象
  // * T是泛型，返回子类对应的类型
  // * @author linzt
  // */
  // public abstract <T> T getSubClassInstance();



}
