package com.kendy.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出托管Excel模型
 * 
 * @author 林泽涛
 * @time 2018年3月18日 下午2:04:49
 */
public class TGExcelModel {
	
	private String sheetName;
	
	private List<String> columnList;
	
	private List<Object[]> data;
	
	private List<String> columnSumList = new ArrayList<>();
	
	private List<Object[]> dataSum = new ArrayList<>();
	
	
	/*****************************************/
	private boolean isWaiZai = false;
	private Map<String,List<Object[]>> waizhaiMap = new HashMap<>();

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public List<Object[]> getData() {
		return data;
	}

	public void setData(List<Object[]> data) {
		this.data = data;
	}

	public List<String> getColumnSumList() {
		return columnSumList;
	}

	public void setColumnSumList(List<String> columnSumList) {
		this.columnSumList = columnSumList;
	}

	public List<Object[]> getDataSum() {
		return dataSum;
	}

	public void setDataSum(List<Object[]> dataSum) {
		this.dataSum = dataSum;
	}

	public boolean getIsWaiZai() {
		return isWaiZai;
	}

	public void setWaiZai(boolean isWaiZai) {
		this.isWaiZai = isWaiZai;
	}
	

	public Map<String, List<Object[]>> getWaizhaiMap() {
		return waizhaiMap;
	}

	public void setWaizhaiMap(Map<String, List<Object[]>> waizhaiMap) {
		this.waizhaiMap = waizhaiMap;
	}


	
	
	
	

}
