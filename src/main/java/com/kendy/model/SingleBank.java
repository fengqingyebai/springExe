package com.kendy.model;

import java.util.List;

public class SingleBank {

  private String bankName;
  private String bankPinyin;
  private List<BankFlowModel> bankFlowList;

  public SingleBank(String bankName, String bankPinyin, List<BankFlowModel> bankFlowList) {
    super();
    this.bankName = bankName;
    this.bankPinyin = bankPinyin;
    this.bankFlowList = bankFlowList;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankPinyin() {
    return bankPinyin;
  }

  public void setBankPinyin(String bankPinyin) {
    this.bankPinyin = bankPinyin;
  }

  public List<BankFlowModel> getBankFlowList() {
    return bankFlowList;
  }

  public void setBankFlowList(List<BankFlowModel> bankFlowList) {
    this.bankFlowList = bankFlowList;
  }


}
