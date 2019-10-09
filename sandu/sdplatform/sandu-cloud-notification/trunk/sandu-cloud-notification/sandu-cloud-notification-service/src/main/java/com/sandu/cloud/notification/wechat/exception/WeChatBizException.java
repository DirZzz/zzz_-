package com.sandu.cloud.notification.wechat.exception;

import com.sandu.cloud.common.exception.BizException;

public class WeChatBizException extends BizException {

	private static final long serialVersionUID = 1L;

	public WeChatBizException() {
	}
	
	public WeChatBizException(WeChatBizExceptionCode exceptionCode) {
		super(exceptionCode.getCode(), exceptionCode.getMessage());
	}


}
