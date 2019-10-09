package com.sandu.cloud.common.exception;


public class SysException extends RuntimeException {

	private static final long serialVersionUID = -5875371379845226068L;

	/**
	 * 具体异常码
	 */
	protected String code;
	
	/**
	 * 异常信息
	 */
	protected String message;

	
	public SysException() {
		super();
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}


	public SysException(String code, String message) {
		super();
		this.code = code;
		this.message = this.code+":"+message;
	}

	public SysException(String message, Throwable cause) {
		super(message, cause);
	}

	public SysException(Throwable cause) {
		super(cause);
	}

	public SysException(String message) {
		super(message);
	}
}
