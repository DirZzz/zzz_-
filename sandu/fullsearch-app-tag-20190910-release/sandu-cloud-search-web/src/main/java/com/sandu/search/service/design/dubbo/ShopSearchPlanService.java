package com.sandu.search.service.design.dubbo;

import com.sandu.search.entity.designplan.po.ShopPlanInfoPo;
import com.sandu.search.exception.ElasticSearchException;

import java.util.List;

/**
 * @desc 店铺列表展示方案信息
 *
 * @auth xiaoxc
 * @data 20181030
 */
public interface ShopSearchPlanService {

    /**
     * 获取店铺方案信息
     * @param shopId
     * @return
     */
    ShopPlanInfoPo getShopPlanInfo(Integer shopId) throws ElasticSearchException;

    /**
     * 获取店铺方案信息(全屋)
     * @param shopId
     * @param planType
     * @return
     * @throws ElasticSearchException
     */
    ShopPlanInfoPo getShopPlanInfo(Integer shopId, Integer planType) throws ElasticSearchException;

    List<ShopPlanInfoPo> shopId2ShopPlanInfo(List<Integer> shopIds, Integer planType) throws ElasticSearchException;
}
