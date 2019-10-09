package com.sandu.search.exception;

/**
 * 组合产品搜索服务异常
 *
 * @author xiaoxc
 * @date 2019-03-22
 */
public class GroupProductSearchException extends Exception {

	private static final long serialVersionUID = -2262466116185107524L;

	public GroupProductSearchException() {
    }

    public GroupProductSearchException(String message) {
        super(message);
    }

    public GroupProductSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public GroupProductSearchException(Throwable cause) {
        super(cause);
    }

    public GroupProductSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
