package com.sandu.service.base.dao;

import com.sandu.api.brand.model.BaseBrand;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface BaseBrandDao {
   
	/**
	 * 通过品牌名称查出id
	 * @param brandName
	 * @return
	 */
	int getIdByBrandName(String brandName);

	List<BaseBrand> selectCompanyBrandList(Long companyId);
}
