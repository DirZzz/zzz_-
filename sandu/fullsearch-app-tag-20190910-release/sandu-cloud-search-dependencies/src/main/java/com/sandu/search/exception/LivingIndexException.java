package com.sandu.search.exception;

/**
 * 小区服务异常
 *
 * @date 20180109
 * @auth pengxuangang
 */
public class LivingIndexException extends Exception {
    public LivingIndexException() {
    }

    public LivingIndexException(String message) {
        super(message);
    }

    public LivingIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public LivingIndexException(Throwable cause) {
        super(cause);
    }

    public LivingIndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
