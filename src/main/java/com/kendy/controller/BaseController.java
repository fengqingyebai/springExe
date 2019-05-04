package com.kendy.controller;

import com.kendy.constant.DataConstans;
import com.kendy.enums.PermissionTabEnum;
import com.kendy.util.ColumnUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
public class BaseController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 绑定多个表格的列,由子实例完成后自动触发
   */
  public BaseController() {
  }


  /**
   * 绑定多个表格的列
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
      column.setCellFactory(ColumnUtil.getColorCellFactory(entity));
    }
  }


  /**
   * 获取表格的选中行
   *
   * @param table
   * @param <T>
   * @return
   */
  public <T> T getSelectedRow(TableView<T> table) {
    T selectedItem = null;
    if (TableUtil.isHasValue(table)) {
      selectedItem = table.getSelectionModel().getSelectedItem();
    }
    return selectedItem;
  }

  /**
   * 是否有行被选中
   *
   * @param table
   * @param <T>
   * @return
   */
  public <T> boolean selectedItem(TableView<T> table) {
    if (TableUtil.isNullOrEmpty(table)) {
      ShowUtil.show("表格无数据，请检查！");
      return Boolean.FALSE;
    }
    if (getSelectedRow(table) == null) {
      ShowUtil.show("大哥，请先选择记录！");
      return Boolean.FALSE;
    } else {
      return Boolean.TRUE;
    }
  }


  /**
   * 清空表格数据
   *
   * @param tables
   */
  public void clearData(TableView... tables) {
    if (tables != null) {
      for (TableView table : tables) {
        if (table != null) {
          table.getItems().clear();
        }
      }
    }
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
   *
   * @author linzt
   */
  public Class<?> getSubClass() {
    return null;
  }

  //
  // /**
  // * 父类获取子类的实例对象
  // * T是泛型，返回子类对应的类型
  // * @author linzt
  // */
  // public abstract <T> T getSubClassInstance();


}
