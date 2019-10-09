package com.sandu.cloud.company.service;

import java.util.List;

import com.sandu.cloud.company.model.Company;
import com.sandu.cloud.company.model.CompanyMiniProgramConfig;
import com.sandu.cloud.company.model.CompanyMiniProgramTemplateMsg;

public interface CompanyService {

	CompanyMiniProgramConfig getCompanyMiniProConfig(String appId);

	Company get(long id);

	List<CompanyMiniProgramConfig> getCompanyMiniProgramConfigs(Long miniProgramCompanyId);

	 /**
     * 获取小程序配置的模板消息
     * @param appId
     * @param templateType
     * @return
     */
	CompanyMiniProgramTemplateMsg getMiniProgramTempateMsg(String appId,Integer templateType);
}
