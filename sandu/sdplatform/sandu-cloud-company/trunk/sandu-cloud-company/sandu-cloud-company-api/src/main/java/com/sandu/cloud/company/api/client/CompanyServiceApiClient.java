package com.sandu.cloud.company.api.client;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandu.cloud.company.model.Company;
import com.sandu.cloud.company.model.CompanyMiniProgramConfig;
import com.sandu.cloud.company.model.CompanyMiniProgramTemplateMsg;

@RequestMapping("/api/v1/company")
public interface CompanyServiceApiClient {
	
	@GetMapping(path = "/getCompanyMiniProConfig")
	CompanyMiniProgramConfig getCompanyMiniProConfig(String appId);

	@GetMapping(path = "/get")
	Company get(long id);

	@GetMapping(path = "/getCompanyMiniProgramConfigs")
	List<CompanyMiniProgramConfig> getCompanyMiniProgramConfigs(Long miniProgramCompanyId);
	
	@GetMapping(path = "/getMiniProgramTempateMsg")
	CompanyMiniProgramTemplateMsg getMiniProgramTempateMsg(@RequestParam(name="appId") String appId,@RequestParam(name="templateType") Integer templateType);
}
