package com.sandu.cloud.design.exception;

public enum DesignPlanExceptionCode {
	
	/**
	 * sys error指的系统相关异常,需要通知开发人员去修复的异常.
	 */
	SD_SYS_ERR_PAY_NOT_LOGIN("SD_SYS_ERR_PAY_NOT_LOGIN","请登录!"),
	SD_SYS_ERR_PAY_PARAM_ERROR("SD_SYS_ERR_PAY_PARAM_ERROR","非法参数!"),
	
	
	/**
	 * biz error指的是业务异常,需要提醒用户的,属于用户操作错误,不需要做系统修复.如:参数为空之类的...
	 */
	SD_BIZ_ERR_PAY_NOT_LOGIN("SD_BIZ_ERR_PAY_NOT_LOGIN","请登录!"),
	SD_BIZ_ERR_PAY_PARAM_ERROR("SD_BIZ_ERR_PAY_PARAM_ERROR","非法参数!");
	
	private String code;
	private String message;
	
	private DesignPlanExceptionCode(String code,String message) {
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
