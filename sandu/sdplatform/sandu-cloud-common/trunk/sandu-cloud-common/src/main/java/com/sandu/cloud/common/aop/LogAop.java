package com.sandu.cloud.common.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import com.sandu.cloud.common.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * aop实现日志
 */
@Aspect
@Slf4j
@Component
public class LogAop {
	
    
	@Around("(execution(* com.sandu.cloud..controller.*.*(..)) || execution(* com.sandu.cloud..api.*.*(..)))")
	public Object logSave(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        try {
        	Map<String, Object> params = getParamsNameAndValue(pjp);
        	log.info("Invoke method:"+className+"."+methodName+" --> Params:"+JsonUtils.toJson(params));
            Object object = pjp.proceed();// 执行原方法
            return object;
        } catch (Exception e) {
            throw e;
        }
    }
    
    Map<String, Object> getParamsNameAndValue(ProceedingJoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
        if(paramNames!=null) {
	        for (int i = 0; i < paramNames.length; i++) {
	        	if(!"request".equals(paramNames[i]) && !"response".equals(paramNames[i])) {
	        		param.put(paramNames[i], paramValues[i]);
	        	}
	        }
        }
        return param;
    }

 
}
