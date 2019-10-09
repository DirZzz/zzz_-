package com.sandu.service.product.dao;

import com.sandu.api.product.model.BaseProductCountertops;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseProductCountertopsMapper {
	int deleteByPrimaryKey(Long id);

	int insert(BaseProductCountertops record);

	int insertSelective(BaseProductCountertops record);

	BaseProductCountertops selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(BaseProductCountertops record);

	int updateByPrimaryKey(BaseProductCountertops record);

	int deleteProCtops(@Param("proCtops") List<Integer> proCtops);

	List<BaseProductCountertops> listProCtopsByProductId(Long productId);
}