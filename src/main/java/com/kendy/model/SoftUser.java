package com.kendy.model;


import java.io.Serializable;

/**
 * @author linzt
 * @date 2019-02-12
 *
 */
public class SoftUser implements Serializable {

  private String id;

  private String creaTeime;

  private String deadTime;

  private String zhName;

  private String code;

  private String enName;

  private String status;

  private String permission;

  public SoftUser() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreaTeime() {
    return creaTeime;
  }

  public void setCreaTeime(String creaTeime) {
    this.creaTeime = creaTeime;
  }

  public String getDeadTime() {
    return deadTime;
  }

  public void setDeadTime(String deadTime) {
    this.deadTime = deadTime;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }
}

