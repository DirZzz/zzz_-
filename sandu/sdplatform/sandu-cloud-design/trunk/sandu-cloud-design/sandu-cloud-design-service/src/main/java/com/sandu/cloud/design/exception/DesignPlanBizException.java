package com.sandu.cloud.design.exception;

import com.sandu.cloud.common.exception.BizException;

public class DesignPlanBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public DesignPlanBizException() {
	}
	
	public DesignPlanBizException(DesignPlanExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
