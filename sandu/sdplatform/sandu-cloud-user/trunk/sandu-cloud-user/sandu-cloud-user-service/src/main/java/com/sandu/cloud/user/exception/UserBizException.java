package com.sandu.cloud.user.exception;

import com.sandu.cloud.common.exception.BizException;

public class UserBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public UserBizException() {
	}
	
	public UserBizException(UserBizExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
