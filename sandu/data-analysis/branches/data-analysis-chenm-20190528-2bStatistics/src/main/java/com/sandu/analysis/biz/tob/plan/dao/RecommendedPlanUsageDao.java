package com.sandu.analysis.biz.tob.plan.dao;

import com.sandu.analysis.biz.tob.plan.model.RecommendedPlanUsageDto;
import com.sandu.analysis.biz.tob.plan.model.SpaceCommonTypeDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-14 17:01
 * Desc:
 */
public interface RecommendedPlanUsageDao {


    /**
     * 根据方案ID获取方案其他信息
     * @param ids 方案IDs
     * @return RecommendedPlanUsageDto
     */
    List<RecommendedPlanUsageDto> selectPlanInfoByIds(List<Integer> ids);

    /**
     * 根据空间ID获取空间类型
     * @param ids 空间IDs
     * @return SpaceCommonTypeDto
     */
    List<SpaceCommonTypeDto> selectSpaceTypeByIds(List<Integer> ids);

    /**
     * 删除后插入数据库
     * @param dtos
     * @param localDate
     */
    void insertAfterDelete(List<RecommendedPlanUsageDto> dtos, LocalDate localDate);

}
