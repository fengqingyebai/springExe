package com.kendy.spider;

/**
 *  请求结果，只包含两个字段 {"result":1,"iErrCode":0}
 * @author 林泽涛
 * @time 2018年3月26日 下午3:36:52
 */
public class CMSResult {
	
	private Integer result;
	
	private Integer iErrCode;
	
	

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getiErrCode() {
		return iErrCode;
	}

	public void setiErrCode(Integer iErrCode) {
		this.iErrCode = iErrCode;
	}
	
	

}
