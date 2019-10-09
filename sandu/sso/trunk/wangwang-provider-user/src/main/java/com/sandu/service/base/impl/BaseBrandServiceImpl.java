package com.sandu.service.base.impl;

import com.sandu.api.brand.model.BaseBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sandu.api.base.service.BaseBrandService;
import com.sandu.service.base.dao.BaseBrandDao;

import java.util.List;

@Service("baseBrandService")
@Transactional
public class BaseBrandServiceImpl implements BaseBrandService {
	
	@Autowired
	private BaseBrandDao baseBrandDao;
	
	@Override
	public int getIdByBrandName(String brandName) {
		int id = baseBrandDao.getIdByBrandName(brandName);
		return id;
	}

	@Override
	public List<BaseBrand> findCompanyBrandList(Long companyId) {
		return baseBrandDao.selectCompanyBrandList(companyId);
	}
}
