package com.kendy.model;

import java.util.ArrayList;
import java.util.List;

import com.kendy.entity.ShangmaInfo;

/**
 * 银行流水模型
 * 
 * @author linzt
 * @time 2018年6月28日
 */
public class BankFlowModel {
	
  private String bankName;
  private int money;
  private String updateTime;
  private String softTime;
  
  public BankFlowModel() {
    super();
  }

  public BankFlowModel(String bankName, int money, String updateTime, String softTime) {
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

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getSoftTime() {
    return softTime;
  }

  public void setSoftTime(String softTime) {
    this.softTime = softTime;
  }

  @Override
  public String toString() {
    return "BankFlowModel [bankName=" + bankName + ", money=" + money + ", updateTime=" + updateTime
        + ", softTime=" + softTime + "]";
  }
  
}
