package com.sandu.service.company.dao;


import com.sandu.api.company.input.CompanyShopQuery;
import com.sandu.api.company.model.bo.CompanyShopListBO;
import com.sandu.api.shop.model.CompanyShop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Sandu
 */
@Repository
public interface CompanyShopDao {

	List<CompanyShopListBO> listCompanyShop(CompanyShopQuery query);

	CompanyShop get(@Param("id") int id);

	List<Map<String, String>> shopId2CompanyName(@Param("shopIds") List<Integer> shopIds);

	List<CompanyShop> listCompanyShopByPlanId(@Param("planId") Integer planId,@Param("planType") Integer planType);

    List<CompanyShopListBO> selectAllList();

	int countShopPlan(Integer shopId);

	int countProjectCase(Integer shopId);

	int totalShopArticle(Integer id);

	void updateBatch(@Param("updateList") List<CompanyShopListBO> updateList);
}
