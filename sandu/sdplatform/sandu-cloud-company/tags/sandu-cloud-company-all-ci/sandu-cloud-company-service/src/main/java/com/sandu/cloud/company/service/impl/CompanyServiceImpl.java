package com.sandu.cloud.company.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.company.dao.CompanyMapper;
import com.sandu.cloud.company.dao.CompanyMiniProgramConfigMapper;
import com.sandu.cloud.company.dao.CompanyMiniProgramTemplateMsgMapper;
import com.sandu.cloud.company.model.Company;
import com.sandu.cloud.company.model.CompanyMiniProgramConfig;
import com.sandu.cloud.company.model.CompanyMiniProgramTemplateMsg;
import com.sandu.cloud.company.service.CompanyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyMiniProgramConfigMapper companyMiniProgramConfigMapper;
    
    @Autowired
    private CompanyMiniProgramTemplateMsgMapper companyMiniProgramTemplateMsgMapper;

    @Override
    public Company get(long id) {
        return companyMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public CompanyMiniProgramConfig getCompanyMiniProConfig(String appId) {
    	CompanyMiniProgramConfig config = new CompanyMiniProgramConfig();
    	config.setAppId(appId);
    	return companyMiniProgramConfigMapper.selectOne(config);
    }

    @Override
    public List<CompanyMiniProgramConfig> getCompanyMiniProgramConfigs(Long miniProgramCompanyId) {
    	CompanyMiniProgramConfig config = new CompanyMiniProgramConfig();
    	config.setCompanyId(miniProgramCompanyId);
    	return companyMiniProgramConfigMapper.select(config);
    }

	@Override
	public CompanyMiniProgramTemplateMsg getMiniProgramTempateMsg(String appId, Integer templateType) {
		CompanyMiniProgramTemplateMsg msg = new CompanyMiniProgramTemplateMsg();
		msg.setAppid(appId);
		msg.setTemplateType(templateType);
		return companyMiniProgramTemplateMsgMapper.selectOne(msg);
	}
    
}
