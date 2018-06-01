package com.kendy.entity;

import java.time.LocalDateTime;
import com.kendy.util.TimeUtil;

/**
 * 银行流水记录
 * 
 * @author linzt
 * @time 2018年6月1日 下午3:17:10
 */
public class HistoryBankMoney {
  
  private String bankName;
  private int money;
  private String updateTime;
  private String softTime; //作用1、区分软件时间   2、区分是否为当天
  
  
  
  public HistoryBankMoney(String bankName, int money, String softTime) {
    super();
    this.bankName = bankName;
    this.money = money;
    this.softTime = softTime;
    this.updateTime = TimeUtil.getDateTime2();
  }
  
  


  public HistoryBankMoney(String bankName, int money, String updateTime, String softTime) {
    super();
    this.bankName = bankName;
    this.money = money;
    this.updateTime = updateTime;
    this.softTime = softTime;
  }




  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public int getMoney() {
    return money;
  }

  public void setMoney(int money) {
    this.money = money;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void String(String updateTime) {
    this.updateTime = updateTime;
  }


  public String getSoftTime() {
    return softTime;
  }

  public void setSoftTime(String softTime) {
    this.softTime = softTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String toString() {
    return "HistoryBankMoney [bankName=" + bankName + ", money=" + money + ", updateTime="
        + updateTime +  ", softTime=" + softTime + "]";
  }

}
