package com.kendy.model;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 合并ID对象
 *
 * @author kendy
 */
public class CombineID {

  private String parentId;

  private Set<String> subIds;


  public CombineID() {
    super();
  }

  public CombineID(String parentId, Set<String> subIds) {
    super();
    this.parentId = parentId;
    this.subIds = subIds;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public Set<String> getSubIds() {
    return subIds;
  }

  public void setSubIds(Set<String> subIds) {
    this.subIds = subIds;
  }

}
