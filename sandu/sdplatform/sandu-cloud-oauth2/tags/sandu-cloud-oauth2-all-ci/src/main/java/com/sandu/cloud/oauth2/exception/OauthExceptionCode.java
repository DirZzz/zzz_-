package com.sandu.cloud.oauth2.exception;

public enum OauthExceptionCode {
	
	SD_ERR_OAUTH_CLIENT_INFO_PARAM_ERROR("SD_ERR_OAUTH_CLIENT_INFO_PARAM_ERROR","请求头没有配置客户端信息!"),
	SD_ERR_OAUTH_CLIENT_INFO_NOTFOUND("SD_ERR_OAUTH_CLIENT_INFO_NOTFOUND","请求头没有配置客户端信息!"),
	SD_ERR_OAUTH_TOKEN_ERROR("SD_ERR_OAUTH_TOKEN_ERROR","token解析异常!"),
	SD_ERR_OAUTH_CLIENT_INFO_PASSWORD_ERROR("SD_ERR_OAUTH_CLIENT_INFO_PASSWORD_ERROR","客户端配置密码不正确!");
	
	private String code;
	private String message;
	
	private OauthExceptionCode(String code,String message) {
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
