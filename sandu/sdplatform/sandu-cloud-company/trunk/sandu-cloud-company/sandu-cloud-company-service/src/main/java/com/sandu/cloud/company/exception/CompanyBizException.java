package com.sandu.cloud.company.exception;

import com.sandu.cloud.common.exception.BizException;

public class CompanyBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public CompanyBizException() {
	}
	
	public CompanyBizException(CompanyExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
