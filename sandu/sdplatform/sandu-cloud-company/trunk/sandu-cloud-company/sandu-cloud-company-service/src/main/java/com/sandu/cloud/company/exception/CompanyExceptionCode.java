package com.sandu.cloud.company.exception;

public enum CompanyExceptionCode {
	
	SD_ERR_PAY_NOT_LOGIN("SD_ERR_COMPANY_NOT_LOGIN","请登录!"),
	SD_ERR_PAY_PARAM_ERROR("SD_ERR_COMPANY_PARAM_ERROR","非法参数!");
	
	private String code;
	private String message;
	
	private CompanyExceptionCode(String code,String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
