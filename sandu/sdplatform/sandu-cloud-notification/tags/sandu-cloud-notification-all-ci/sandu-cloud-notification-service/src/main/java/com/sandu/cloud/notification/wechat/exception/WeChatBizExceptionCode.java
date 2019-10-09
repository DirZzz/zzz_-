package com.sandu.cloud.notification.wechat.exception;

public enum WeChatBizExceptionCode {
	
	SD_ERR_NOTIFY_WECHAT_PARAM_MUST_NOT_BE_NULL("SD_ERR_NOTIFY_WECHAT_PARAM_MUST_NOT_BE_NULL","发送模板消息参数不能为空"),
	SD_ERR_NOTIFY_WECHAT_APPID_MUST_NOT_BE_NULL("SD_ERR_NOTIFY_WECHAT_APPID_MUST_NOT_BE_NULL","appid不能为空"),
	SD_ERR_NOTIFY_WECHAT_FORM_ID_NOT_FOUND("SD_ERR_NOTIFY_WECHAT_FORM_ID_NOT_FOUND","formId不存在!"),

	
	SD_ERR_NOTIFY_WECHAT_NOT_LOGIN2("SD_ERR_PAY_PARAM_ERROR","非法参数!");
	
	private String code;
	private String message;
	
	private WeChatBizExceptionCode(String code,String message) {
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
