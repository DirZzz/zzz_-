package com.sandu.cloud.pay.exception;

import com.sandu.cloud.common.exception.BizException;

public class PayBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public PayBizException() {
	}
	
	public PayBizException(PayExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
