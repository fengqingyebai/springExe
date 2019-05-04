package com.kendy.model;

import java.util.List;

/**
 * 购买联盟币结果
 * @author linzt
 * @see
 */
public class CoinBuyerRusult {

  /**
   * result : [{"nickName":"堪睇系唔使靓仔","strCover":"http://info.pokermate.net/data/2018/5/26/775587_small_1527328738875.png","showId":"1871535391","grantNums":500,"remarks":"","msgType":1,"uniqueId":11912},{"nickName":"十下十下","strCover":"http://info.pokermate.net/data/2018/3/25/257001_small_1521958159170.png","showId":"604071921","grantNums":500,"remarks":"","msgType":1,"uniqueId":11880}]
   * iErrCode : 0
   */
  private int iErrCode;
  private List<CoinBuyer> result;


  public int getiErrCode() {
    return iErrCode;
  }

  public void setiErrCode(int iErrCode) {
    this.iErrCode = iErrCode;
  }

  public List<CoinBuyer> getResult() {
    return result;
  }

  public void setResult(List<CoinBuyer> result) {
    this.result = result;
  }

  public static class CoinBuyer {

    /**
     * nickName : 堪睇系唔使靓仔
     * strCover : http://info.pokermate.net/data/2018/5/26/775587_small_1527328738875.png
     * showId : 1871535391
     * grantNums : 500.0
     * remarks :
     * msgType : 1
     * uniqueId : 11912
     */
    private String nickName;
    private String strCover;
    private String showId;
    private String grantNums;
    private String remarks;
    private int msgType;
    private int uniqueId;

    public String getNickName() {
      return nickName;
    }

    public void setNickName(String nickName) {
      this.nickName = nickName;
    }

    public String getStrCover() {
      return strCover;
    }

    public void setStrCover(String strCover) {
      this.strCover = strCover;
    }

    public String getShowId() {
      return showId;
    }

    public void setShowId(String showId) {
      this.showId = showId;
    }

    public String getGrantNums() {
      return grantNums;
    }

    public void setGrantNums(String grantNums) {
      this.grantNums = grantNums;
    }

    public String getRemarks() {
      return remarks;
    }

    public void setRemarks(String remarks) {
      this.remarks = remarks;
    }

    public int getMsgType() {
      return msgType;
    }

    public void setMsgType(int msgType) {
      this.msgType = msgType;
    }

    public int getUniqueId() {
      return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
      this.uniqueId = uniqueId;
    }
  }

}
