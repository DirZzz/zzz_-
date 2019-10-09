package com.sandu.cloud.user.exception;

public enum UserBizExceptionCode {
	  
	//为了自定义异常处理,错误编写一定要以ERR开头
	SD_ERR_USER_NOT_LOGIN("SD_ERR_USER_NOT_LOGIN","请登录!"),
	SD_ERR_USER_NOT_EXIST("SD_ERR_USER_NOT_EXIST","用户不存在!"),
	SD_ERR_USER_PARAM_ERROR("SD_ERR_USER_PARAM_ERROR","非法参数!");
	
	private String code;
	private String message;
	
	private UserBizExceptionCode(String code,String message) {
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
