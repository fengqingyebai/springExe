package com.kendy.entity;

import com.kendy.interfaces.Entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * 银行类
 * 
 * @author linzt
 * @time 2018年6月28日 
 */
public class BankFlowInfo implements Entity{
	
	
	private SimpleStringProperty index = new SimpleStringProperty(""); // 序号
	private SimpleStringProperty dateString = new SimpleStringProperty(""); // 时间

	private SimpleStringProperty yuEBao = new SimpleStringProperty(""); // 余额宝
	private SimpleStringProperty huaXia = new SimpleStringProperty(""); // 华夏
	private SimpleStringProperty pingAn = new SimpleStringProperty(""); // 平安
	private SimpleStringProperty zhaoShang = new SimpleStringProperty(""); // 招商
	private SimpleStringProperty zhiFuBao = new SimpleStringProperty(""); // 支付宝
	private SimpleStringProperty puFa = new SimpleStringProperty(""); // 浦发
	private SimpleStringProperty xingYe = new SimpleStringProperty(""); // 兴业
	
	
	public BankFlowInfo() {
		super();
	}

	public BankFlowInfo(String yuEBao, String huaXia,
			String pingAn, String zhaoShang, String zhiFuBao, String puFa) {
		super();
		this.yuEBao = new SimpleStringProperty(yuEBao);
		this.huaXia = new SimpleStringProperty(huaXia);
		this.pingAn = new SimpleStringProperty(pingAn);
		this.zhaoShang = new SimpleStringProperty(zhaoShang);
		this.zhiFuBao = new SimpleStringProperty(zhiFuBao);
		this.puFa = new SimpleStringProperty(puFa);
	}
	
	//==========================================
	public SimpleStringProperty indexProperty() {
		return this.index;
	}
	
	public String getIndex() {
		return this.indexProperty().get();
	}
	
	public void setIndex(final String index) {
		this.indexProperty().set(index);
	}
	//==========================================
	public SimpleStringProperty dateStringProperty() {
		return this.dateString;
	}
	
	public String getDateString() {
		return this.dateStringProperty().get();
	}
	
	public void setDateString(final String index) {
		this.dateStringProperty().set(index);
	}
	

    //=======================
	public SimpleStringProperty yuEBaoProperty() {
		return this.yuEBao;
	}
	public String getYuEBao() {
		return this.yuEBaoProperty().get();
	}
	public void setYuEBao(final String yuEBao) {
		this.yuEBaoProperty().set(yuEBao);
	}
	
	//=======================
	public SimpleStringProperty zhiFuBaoProperty() {
		return this.zhiFuBao;
	}
	public String getZhiFuBao() {
		return this.zhiFuBaoProperty().get();
	}
	public void setZhiFuBao(final String zhiFuBao) {
		this.zhiFuBaoProperty().set(zhiFuBao);
	}
	
	//=======================
	public SimpleStringProperty zhaoShangProperty() {
		return this.zhaoShang;
	}
	public String getZhaoShang() {
		return this.zhaoShangProperty().get();
	}
	public void setZhaoShang(final String zhaoShang) {
		this.zhaoShangProperty().set(zhaoShang);
	}
	
	
	//=======================
	public SimpleStringProperty huaXiaProperty() {
		return this.huaXia;
	}

	public String getHuaXia() {
		return this.huaXiaProperty().get();
	}

	public void setHuaXia(final String huaXia) {
		this.huaXiaProperty().set(huaXia);
	}
	

	//=======================
	public SimpleStringProperty pingAnProperty() {
		return this.pingAn;
	}

	public String getPingAn() {
		return this.pingAnProperty().get();
	}

	public void setPingAn(final String pingAn) {
		this.pingAnProperty().set(pingAn);
	}
	//=======================
    public SimpleStringProperty puFaProperty() {
        return this.puFa;
    }
    public String getPuFa() {
        return this.puFaProperty().get();
    }
    public void setPuFa(final String puFa) {
        this.puFaProperty().set(puFa);
    }
    //=======================
	public SimpleStringProperty xingYeProperty() {
		return this.xingYe;
	}
	public String getXingYe() {
		return this.xingYeProperty().get();
	}
	public void setXingYe(final String xingYe) {
		this.xingYeProperty().set(xingYe);
	}
	

	@Override
	public String toString() {
		return "KaixiaoInfo [huaXia=" + huaXia.get() + ", pingAn=" + pingAn.get() + "]";
	}
	
	
	
	
}
