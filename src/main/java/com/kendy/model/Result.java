package com.kendy.model;

/**
 * @author linzt
 * @date
 */
public class Result {

  boolean ok = false;
  int code = -1;
  String description = "";


  public Result(boolean ok, String description) {
    this.ok = ok;
    this.description = description;
  }

  public Result(boolean ok, int code, String description) {
    this.ok = ok;
    this.code = code;
    this.description = description;
  }

  public boolean isOk() {
    return ok;
  }

  public void setOk(boolean ok) {
    this.ok = ok;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public static Result SUCCESS = new Result(Boolean.TRUE, "");
}
