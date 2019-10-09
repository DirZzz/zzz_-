package com.sandu.cloud.common.vo;

import java.io.Serializable;

public class ResponseEnvelope<T> implements Serializable {
	
	
	private static final long serialVersionUID = -1L;
	// private String msgId;
	private String code;
	private String message;
	private T data;

	public ResponseEnvelope() {

	}

	public ResponseEnvelope(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
		
	}

	public ResponseEnvelope(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static <T> ResponseEnvelope<T> ok() {
		return new ResponseEnvelope<T>("SUCCESS", "操作成功!");
	}
	
	public static <T> ResponseEnvelope<T> ok(String message,T data) {
		return new ResponseEnvelope<T>("SUCCESS", message, data);
	}

	public static <T> ResponseEnvelope<T> ok(T data) {
		return new ResponseEnvelope<T>("SUCCESS", "操作成功!", data);
	}
	
	public static <T> ResponseEnvelope <T> error(String message) {
		return new ResponseEnvelope<T>("ERR_BIZ_ERROR", message);
	}

	public static <T> ResponseEnvelope <T> error(String code, String message) {
		return new ResponseEnvelope<T>(code, message);
	}

	public static <T> ResponseEnvelope<T> error() {
		return new ResponseEnvelope<T>("ERR_SYS_ERROR", "ERR_SYS_ERROR:系统异常!");
	}

	public String getcode() {
		return code;
	}

	public void setcode(String code) {
		this.code = code;
	}

	public String getmessage() {
		return message;
	}

	public void setmessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
