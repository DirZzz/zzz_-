package com.sandu.cloud.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

    static {
    	objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

	private JsonUtils() {
		
	}
	public static String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("json转化异常:",e);
		}
		return null;
	}

	public static <T> T fromJson(String json, Class<T> cls){
		try {
			return objectMapper.readValue(json, cls);
		} catch (Exception e) {
			log.error("json加载异常:"+json,e);
		}
		return null;
	}

	public static <T> T fromJson(String json, TypeReference valueTypeRef){
		try {
			return objectMapper.readValue(json, valueTypeRef);
		} catch (Exception e) {
			log.error("json加载异常:"+json,e);
		}
		return null;
	}
}