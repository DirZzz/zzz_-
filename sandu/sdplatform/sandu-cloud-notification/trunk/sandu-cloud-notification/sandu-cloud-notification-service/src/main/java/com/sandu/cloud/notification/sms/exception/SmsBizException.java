package com.sandu.cloud.notification.sms.exception;

import com.sandu.cloud.common.exception.BizException;

public class SmsBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public SmsBizException() {
	}
	
	public SmsBizException(SmsBizExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}
}
