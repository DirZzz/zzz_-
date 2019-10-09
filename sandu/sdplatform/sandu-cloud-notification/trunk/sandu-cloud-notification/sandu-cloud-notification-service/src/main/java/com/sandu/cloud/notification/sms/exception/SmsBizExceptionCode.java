package com.sandu.cloud.notification.sms.exception;

public enum SmsBizExceptionCode {
	
	SD_ERR_NOTIFY_SMS_SEND_ERROR("SD_ERR_NOTIFY_SMS_SEND_ERROR","短信发送异常!"),
	
	
	SD_ERR_NOTIFY_SMS_TEST("SD_ERR_NOTIFY_SMS_TEST","非法参数!");
	
	private String code;
	private String message;
	
	private SmsBizExceptionCode(String code,String message) {
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
