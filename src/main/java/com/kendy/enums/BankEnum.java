package com.kendy.enums;

/**
 * 银行类型枚举类
 *
 * @author 林泽涛
 * @time 2018年6月28日
 */
public enum BankEnum {
  YuEBao("余额宝", "yuEBao"),
  HuaXia("华夏", "huaXia"),
  XingYe("兴业", "xingYe"),
  PingAn("平安", "pingAn"),
  ZhaoShang("招商", "zhaoShang"),
  ZhiFuBao("支付宝", "zhiFuBao"),
  PuFa("浦发", "puFa"),
  ZhongGuo("中国", "zhongGuo"),
  ZhongXin("中信", "zhongXin"),
  MinSheng("民生", "minSheng"),
  GuangDa("光大", "guangDa"),
  JianShe("建设", "jianShe"),
  GongShang("工商", "gongShang"),
  ;

  //中国，中信，民生，光大，建设，工商
  String name;
  String value;

  BankEnum(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }
}