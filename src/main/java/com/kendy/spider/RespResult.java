package com.kendy.spider;

import java.util.List;

/**
 * Experimental : 封装返回数据
 * 
 * @author 林泽涛
 * @time 2018年4月8日 下午9:42:31
 * @param <T>
 */
public class RespResult<T> {

  private Result<T> result;

  private Integer iErrCode;

  public RespResult() {
    result = new Result<T>();
    iErrCode = -1;
  }


  public Result<T> getResult() {
    return result;
  }

  public void setResult(Result<T> result) {
    this.result = result;
  }

  public Integer getiErrCode() {
    return iErrCode;
  }

  public void setiErrCode(Integer iErrCode) {
    this.iErrCode = iErrCode;
  }


  public static class Result<T> {
    private String total;
    private List<T> list;

    public String getTotal() {
      return total;
    }

    public void setTotal(String total) {
      this.total = total;
    }

    public List<T> getList() {
      return list;
    }

    public void setList(List<T> list) {
      this.list = list;
    }

    @Override
    public String toString() {
      return "Result [total=" + total + ", list=" + list + "]";
    }
  }


  @Override
  public String toString() {
    return "RespResult [result=" + result + ", iErrCode=" + iErrCode + "]";
  }


}
