package com.sandu.cloud.oauth2.exception;

import com.sandu.cloud.common.exception.BizException;

public class OauthBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public OauthBizException() {
	}
	
	public OauthBizException(OauthExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
