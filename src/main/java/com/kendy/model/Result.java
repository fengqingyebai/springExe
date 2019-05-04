package com.kendy.model;

/**
 * @author linzt
 * @date
 */
public class Result {

  boolean isInRange = false;
  boolean isBuyOK = false;
  int buyCode = -1;
  String description = "";

  public Result() {
  }

  public Result(boolean isInRange, boolean isBuyOK, int buyCode, String description) {
    this.isInRange = isInRange;
    this.isBuyOK = isBuyOK;
    this.buyCode = buyCode;
    this.description = description;
  }

  public boolean isInRange() {
    return isInRange;
  }

  public void setInRange(boolean inRange) {
    isInRange = inRange;
  }

  public boolean isBuyOK() {
    return isBuyOK;
  }

  public void setBuyOK(boolean buyOK) {
    isBuyOK = buyOK;
  }

  public int getBuyCode() {
    return buyCode;
  }

  public void setBuyCode(int buyCode) {
    this.buyCode = buyCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public  boolean isAllOK(){
    return isInRange && isBuyOK && (buyCode == 0);
  }
}
