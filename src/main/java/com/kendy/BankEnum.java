package com.kendy;

/**
 * 银行类型枚举类
 * 
 * @author 林泽涛
 * @time 2018年6月28日
 */
public  enum BankEnum {
	YuEBao("余额宝", "yuEBao"), 
	HuaXia("华夏", "huaXia"), 
	XingYe("兴业", "xingYe"),
	PingAn("平安", "pingAn"), 
	ZhaoShang("招商", "zhaoShang"), 
	ZhiFuBao("支付宝", "zhiFuBao"), 
	PuFa("浦发", "puFa")
	;

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