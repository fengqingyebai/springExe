package com.kendy.db.entity.pk;

import java.io.Serializable;

/**
 * @author linzt
 * @date
 */
public class CurrentMoneyPK implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  public CurrentMoneyPK() {
    super();
  }

  public CurrentMoneyPK(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
