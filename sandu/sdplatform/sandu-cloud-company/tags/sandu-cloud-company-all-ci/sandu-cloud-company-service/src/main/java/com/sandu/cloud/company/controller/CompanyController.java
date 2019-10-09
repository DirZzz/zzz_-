package com.sandu.cloud.company.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/v1/company")
public class CompanyController {

	@Value("${custom.property.jdbc.url}")
	private String testProperty;

	
}
