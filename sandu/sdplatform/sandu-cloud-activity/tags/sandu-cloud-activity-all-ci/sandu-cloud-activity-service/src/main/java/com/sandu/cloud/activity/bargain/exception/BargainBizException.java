package com.sandu.cloud.activity.bargain.exception;

import com.sandu.cloud.common.exception.BizException;

public class BargainBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public BargainBizException() {
	}
	
	public BargainBizException(BargainBizExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
