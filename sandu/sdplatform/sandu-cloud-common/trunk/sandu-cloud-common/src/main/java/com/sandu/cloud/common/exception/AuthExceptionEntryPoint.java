package com.sandu.cloud.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws  ServletException {
    	String token = request.getHeader("Authorization");
    	log.warn("token已失效-->"+token);
    	
    	//api调用需要抛异常给feign
    	if(request.getRequestURI().startsWith("/api/")) {
    		throw new BizException("SD_BIZ_ERR_OAUTH_TOKEN_ERROR","无效token!");
    	}
    	
    	//其他调用controller直接返回给客户端
        Map<String,String> map = new HashMap<String,String>();
        map.put("code", "SD_BIZ_ERR_OAUTH_TOKEN_ERROR");
        map.put("message", "无效token!");
        response.setContentType("application/json");
     //   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), map);
            response.flushBuffer();
        } catch (Exception e) {
        	log.error("系统异常:",e);
            throw new ServletException();
        }
    }
}