package com.sandu.cloud.design.exception;

import com.sandu.cloud.common.exception.SysException;

public class DesignPlanSysException extends SysException {

	private static final long serialVersionUID = 1L;

	public DesignPlanSysException() {
	}
	
	public DesignPlanSysException(DesignPlanExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
