package com.sandu.cloud.common.feign.config;

import java.io.IOException;
import java.util.Map;

import com.sandu.cloud.common.exception.BizException;
import com.sandu.cloud.common.util.JsonUtils;
import static feign.FeignException.errorStatus;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder{
	
	@Override
    public Exception decode(String methodKey, Response response) {
      try {
          if (response.body() != null) {
            String body = Util.toString(response.body().asReader());
            
            Map resultMap = JsonUtils.fromJson(body, Map.class);
            String exception = (String)resultMap.get("exception");
            //如果是自定义异常
            if(exception.startsWith("com.sandu.cloud") && exception.contains("BizException")) {
            	String messageWithCode = (String)resultMap.get("message");
            	int splitIndex = messageWithCode.indexOf(":");
            	String code = messageWithCode.substring(0,splitIndex);
            	String message = messageWithCode.substring(splitIndex+1);
            	return new BizException(code,message);
            }
            //如果是hibernate validator验证异常           
            if(exception.equals("javax.validation.ConstraintViolationException") 
            		|| exception.equals("org.springframework.validation.BindException")) {
            	String messageWithCode = (String)resultMap.get("message");
            	int splitIndex = messageWithCode.indexOf(":");
            	String message = messageWithCode.substring(splitIndex+1);
            	return new BizException("SD_BIZ_ERR_COMMON_PARAM_ERROR",message);
            }
            
          }
        } catch (IOException ignored) { // NOPMD
        }
      FeignException feignException = errorStatus(methodKey, response);
      return feignException;
    }
	
	
	
}