package com.sandu.cloud.common.feign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfiguration {
    @Bean
    public ErrorDecoder errorDecoder(){
        return new CustomErrorDecoder();
    }
    
    @Bean
    public FeignBasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new FeignBasicAuthRequestInterceptor();
    }
}