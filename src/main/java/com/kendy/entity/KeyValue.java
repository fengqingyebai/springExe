package com.kendy.entity;

import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

public class KeyValue implements Entity {

  public SimpleStringProperty key = new SimpleStringProperty();//资金类型
  public SimpleStringProperty value = new SimpleStringProperty();//金额


  public KeyValue() {
    super();
  }

  public KeyValue(String key, String value) {
    super();
    this.key = new SimpleStringProperty(key);
    this.value = new SimpleStringProperty(value);
  }

  public SimpleStringProperty keyProperty() {
    return this.key;
  }

  public String getKey() {
    return this.keyProperty().get();
  }

  public void setKey(final String key) {
    this.keyProperty().set(key);
  }

  public SimpleStringProperty valueProperty() {
    return this.value;
  }

  public String getValue() {
    return this.valueProperty().get();
  }

  public void setValue(final String value) {
    this.valueProperty().set(value);
  }
}

