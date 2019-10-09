package com.sandu.cloud.common.zipkin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.ErrorParser;




public class ZipkinConfiguration {
	

	ErrorParser errorParser() {
		return new ErrorParser();
	}
}