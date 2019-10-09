package com.sandu.cloud.company.api;


import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.company.model.Company;
import com.sandu.cloud.company.model.CompanyMiniProgramConfig;
import com.sandu.cloud.company.model.CompanyMiniProgramTemplateMsg;
import com.sandu.cloud.company.service.CompanyService;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyServiceApi {
	
	@Autowired
	private CompanyService companyService;
	
	@GetMapping(path = "/getCompanyMiniProConfig")
	public CompanyMiniProgramConfig getCompanyMiniProConfig(@NotEmpty(message="appId不能为空") String appId) {
		return companyService.getCompanyMiniProConfig(appId);
	}

	@GetMapping(path = "/get")
	public Company get(long id) {
		return companyService.get(id);
	}

	@GetMapping(path = "/getCompanyMiniProgramConfigs")
	public List<CompanyMiniProgramConfig> getCompanyMiniProgramConfigs(@NotEmpty(message="companyId不能为空") Long companyId){
		return companyService.getCompanyMiniProgramConfigs(companyId);
	}
	
	@GetMapping(path = "/getMiniProgramTempateMsg")
	public CompanyMiniProgramTemplateMsg getMiniProgramTempateMsg(String appId,Integer templateType) {
		return companyService.getMiniProgramTempateMsg(appId, templateType);
	}
}
