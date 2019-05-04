package com.kendy.util;

import com.kendy.constant.Constants;
import com.kendy.enums.ColumnColorType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * 数据列的工具类
 *
 * @author linzt
 * @date 2018-12-15
 */
public class ColumnUtil {

  public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> getColorCellFactory(
      T t) {
    return new Callback<TableColumn<T, String>, TableCell<T, String>>() {
      public TableCell<T, String> call(TableColumn<T, String> param) {
        TableCell<T, String> cell = new TableCell<T, String>() {
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            this.setTextFill(null);
            if (!isEmpty() && item != null) {
              if (StringUtil.isNegativeNumber(item)) {
                this.setTextFill(Color.RED);
              } else {
                this.setTextFill(Color.BLACK);
              }
              setText(item);
            }
          }
        };
        cell.setEditable(false);// 不让其可
        return cell;
      }
    };
  }

  public static <T> TableColumn<T, String> getTableBlackColumn(String colName, String colVal, T t) {
    return getTableColumn(colName, colVal, ColumnColorType.COLUMN_COMMON, 85.0, t);
  }

  public static <T> TableColumn<T, String> getTableRedColumn(String colName, String colVal, T t) {
    return getTableColumn(colName, colVal, ColumnColorType.COLUMN_RED, 85.0, t);
  }

  public static <T> TableColumn<T, String> getTableRedColumn(String colName, String colVal, double width, T t) {
    return getTableColumn(colName, colVal, ColumnColorType.COLUMN_RED, width, t);
  }

  public static <T> TableColumn<T, String> getTableColumn(String colName, String colVal,
      ColumnColorType columnColorType, double width, T t) {
    TableColumn<T, String> col = new TableColumn<>(colName);
    col.setStyle(Constants.CSS_CENTER);
    col.setPrefWidth(width);
    col.setCellValueFactory(new PropertyValueFactory<T, String>(colVal));
    if (columnColorType == ColumnColorType.COLUMN_RED) {
      col.setCellFactory(getColorCellFactory(t));
    }
    col.setSortable(false);
    return col;
  }

}
