package com.kendy.enums;


import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

/**
 * tab名称枚举
 */
public enum PermissionTabEnum {

  JBXX("基本信息","JBXX",""),

  CCXX("场次信息","CCXX",""),

  ZHXX("总汇信息","ZHXX",""),

  WZXX("外债信息","WZXX","waizhai_tab_frame.fxml"),

  HYCX("会员查询","HYCX","member_query_tab_frame.fxml"),

  JFCX("积分查询","JFCX","jifen_query_tab_frame.fxml"),

  DLCX("代理查询","DLCX","team_proxy_tab_frame.fxml"),

  SSSMST("实时上码系统","SSSMST","shangma_tab_frame.fxml"),

  LMDZ("联盟对帐","LMDZ","LM_Tab_Fram.fxml"),

  LMPZ("联盟配帐","LMPZ","Quota_Tab_Fram.fxml"),

  GDGXZ("股东贡献值","GDGXZ","gudong_contribution.fxml"),

  TGGJ("托管工具","TGGJ","TG_toolaa.fxml"),

  ZDSMPZ("自动上码配置","ZDSMPZ","SM_Autos.fxml"),

  YHLS("银行流水","YHLS","bank_flow_frame.fxml"),

  LSTJ("历史统计","LSTJ","history_static_tab_frame.fxml"),

  ZJTJ("战绩统计","ZJTJ","zj_static_tab_frame.fxml");

  private String tabName;
  private String tabShortName;
  private String fxmlFileName;

  PermissionTabEnum(String tabName, String tabShortName, String fxmlFileName) {
    this.tabName = tabName;
    this.tabShortName = tabShortName;
    this.fxmlFileName = fxmlFileName;
  }

  public String getTabName() {
    return tabName;
  }

  public void setTabName(String tabName) {
    this.tabName = tabName;
  }

  public String getTabShortName() {
    return tabShortName;
  }

  public void setTabShortName(String tabShortName) {
    this.tabShortName = tabShortName;
  }

  public String getFxmlFileName() {
    return fxmlFileName;
  }

  public void setFxmlFileName(String fxmlFileName) {
    this.fxmlFileName = fxmlFileName;
  }


  public static boolean hasPermission(String tabName) {
    for (PermissionTabEnum Tab : PermissionTabEnum.values()) {
      if (StringUtils.equalsAny(tabName, Tab.getTabName(), Tab.getTabShortName())) {
        return Boolean.TRUE;
      }
    }
    return Boolean.FALSE;
  }

  public static void main(String[] args) {
    Arrays.stream(PermissionTabEnum.values()).forEachOrdered(e-> System.out.print("'"+e.getTabName()+"',"));
  }


}
