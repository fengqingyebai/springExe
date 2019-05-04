package com.kendy.model;

import java.util.List;

public class WanjiaListResult {

  private List<WanjiaApplyInfo> result;

  private Integer iErrCode;

  public List<WanjiaApplyInfo> getResult() {
    return result;
  }

  public void setResult(List<WanjiaApplyInfo> result) {
    this.result = result;
  }

  public Integer getiErrCode() {
    return iErrCode;
  }

  public void setiErrCode(Integer iErrCode) {
    this.iErrCode = iErrCode;
  }


}
