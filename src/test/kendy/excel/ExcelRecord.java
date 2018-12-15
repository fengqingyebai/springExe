package kendy.excel;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;

/**
 * @author linzt
 * @date
 */
public class ExcelRecord {

  @MyExcelField(title="玩家ID")
  private String playerId;

  @MyExcelField(title="保险买入#彩池买入")
  private String insuranceBuy;

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getInsuranceBuy() {
    return insuranceBuy;
  }

  public void setInsuranceBuy(String insuranceBuy) {
    this.insuranceBuy = insuranceBuy;
  }
}