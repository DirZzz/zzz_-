package com.sandu.cloud.common.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sandu.cloud.common.vo.ResponseEnvelope;

import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice(
		   {"com.sandu.cloud.user.controller",
			"com.sandu.cloud.pay.controller",
			"com.sandu.cloud.design.controller",
			"com.sandu.cloud.activity.bargain.controller"})
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BizException.class)
    public ResponseEnvelope<BizException> bizExceptionHandler(BizException bizException) {
		log.warn(bizException.getMessage());
        return new ResponseEnvelope<BizException>(bizException.getCode(),bizException.getMessage());
    }
	
	/**
	 * 方法上的复杂参数验证处理
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEnvelope<BizException> handle(BindException exception) {
        BindingResult validResult = exception.getBindingResult();
        StringBuilder errorInfo = new StringBuilder();
        validResult.getAllErrors().forEach(e -> {
        	errorInfo.append(e.getDefaultMessage());
        	errorInfo.append(";");
        });
        return new ResponseEnvelope<BizException>("SD_BIZ_ERR_COMMON_PARAM_ERROR",errorInfo.toString());
    }
	
	/**
	 * 方法上的简单参数验证处理
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEnvelope<BizException>  handle(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuffer errorInfo = new StringBuffer();
        for (ConstraintViolation<?> item : violations) {
            errorInfo.append(item.getMessage());
            errorInfo.append(";");
        }
        return new ResponseEnvelope<BizException>("SD_BIZ_ERR_COMMON_PARAM_ERROR",errorInfo.toString());
    }


	@ExceptionHandler(Exception.class)
	@NewSpan
    public ResponseEnvelope sysExceptionHandler(@SpanTag("error") Exception e) {
		if(e.getClass().getName().equals("org.springframework.security.access.AccessDeniedException")) {
			log.warn(e.getMessage());
			return new ResponseEnvelope<BizException>("SD_BIZ_ERR_OAUTH_ACCESS_DENY","SD_SYS_BIZ_OAUTH_ACCESS_DENY:没有访问权限!");
		}
		log.error("系统异常",e);
        return new ResponseEnvelope<SysException>("SD_SYS_ERR_ERROR","SYS_ERR_ERROR:系统异常!");
    }

}