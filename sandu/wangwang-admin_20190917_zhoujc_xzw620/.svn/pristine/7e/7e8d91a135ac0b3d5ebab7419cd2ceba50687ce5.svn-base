package com.sandu.service.solution.dao;


import com.sandu.api.solution.model.DesignPlanRecommendedSuperior;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DesignPlanRecommendedSuperiorMapper {
	int deleteByPrimaryKey(Long id);

	int insert(DesignPlanRecommendedSuperior record);

	int insertSelective(DesignPlanRecommendedSuperior record);

	DesignPlanRecommendedSuperior selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(DesignPlanRecommendedSuperior record);

	int updateByPrimaryKey(DesignPlanRecommendedSuperior record);

	List<DesignPlanRecommendedSuperior> ListByPlanIdAndSpaceType(@Param("planId") Integer planId);

	void deleteByPlanId(@Param("planId") Integer planId);

	List<DesignPlanRecommendedSuperior> findPutFullHousePlan();
}