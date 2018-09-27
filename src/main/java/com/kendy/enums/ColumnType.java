package com.kendy.enums;

public enum ColumnType {

  COLUMN_RED(true),
  COLUMN_COMMON(false);

  boolean value ;

  ColumnType(boolean value) {
    this.value = value;
  }

  public boolean isValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }
}
