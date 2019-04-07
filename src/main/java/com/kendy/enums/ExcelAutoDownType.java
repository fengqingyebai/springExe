package com.kendy.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 自动下载类型
 * 2019-04-06 暂时不支持大菠萝
 */
public enum ExcelAutoDownType {
  PU_TONG("1", "普通保险局"),
  AO_MA_HA("2", "奥马哈保险局"),
  JIA_LE_BI("8", "加勒比海"),
  DE_ZHOU_NIU_ZAI("9", "德州牛仔")
  ;

  String name;
  String value;

  ExcelAutoDownType( String value, String name) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  /**
   * 根据自动下载类型值获取描述名称
   * @param val
   * @return 白名单的第一列类型名称
   */
  public static String getNambeByValue(String val){
    if (StringUtils.isNotBlank(val)) {
      val=val.trim();
      ExcelAutoDownType[] types = ExcelAutoDownType.values();
      for (ExcelAutoDownType type : types) {
        if (val.equals(type.getValue())) {
          // if two values equals, then return the excel type name
          return type.getName();
        }
      }
    }
    return "";
  }


}
