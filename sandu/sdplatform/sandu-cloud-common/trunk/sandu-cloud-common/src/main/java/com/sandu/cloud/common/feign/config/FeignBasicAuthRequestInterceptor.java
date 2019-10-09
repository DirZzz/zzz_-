package com.sandu.cloud.common.feign.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignBasicAuthRequestInterceptor  implements RequestInterceptor {

    public FeignBasicAuthRequestInterceptor() {

    }

    @Override
    public void apply(RequestTemplate template) {
    	ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //添加token
        template.header("Authorization", request.getHeader("Authorization"));
    }
}