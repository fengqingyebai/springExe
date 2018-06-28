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

	private SimpleStringProperty yuEBao = new SimpleStringProperty(); // 余额宝
	private SimpleStringProperty huaXia = new SimpleStringProperty(); // 华夏
	private SimpleStringProperty pingAn = new SimpleStringProperty(); // 平安
	private SimpleStringProperty zhaoShang = new SimpleStringProperty(); // 招商
	private SimpleStringProperty zhiFuBao = new SimpleStringProperty(); // 支付宝
	private SimpleStringProperty puFa = new SimpleStringProperty(); // 浦发
	
	
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
	

	@Override
	public String toString() {
		return "KaixiaoInfo [huaXia=" + huaXia.get() + ", pingAn=" + pingAn.get() + "]";
	}
	
	
	
	
}
