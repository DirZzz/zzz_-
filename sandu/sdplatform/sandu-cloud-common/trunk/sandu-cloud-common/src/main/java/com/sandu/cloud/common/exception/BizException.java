package com.sandu.cloud.common.exception;


public class BizException extends RuntimeException {

	private static final long serialVersionUID = -5875371379845226068L;

	/**
	 * 具体异常码
	 */
	protected String code;
	
	/**
	 * 异常信息
	 */
	protected String message;

	
	public BizException() {
		super();
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}


	public BizException(String code, String message) {
		super();
		this.code = code;
		this.message = this.code+":"+message;
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(String message) {
		super(message);
	}
}
