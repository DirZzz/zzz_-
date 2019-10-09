package com.sandu.service.statistics.dao;

import com.sandu.api.statistics.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: chenqiang
 * @create: 2019-05-23 15:48
 */
@Repository
public interface ResourceStatisticsDao {

    /*************************** 用户资源统计SQL ***************************/

    List<UserDto> listUser(ResourceStatisticsDto dto);

    Long selectUserPlanConfigSource(ResourceStatisticsDto dto);

    Long selectUserScenePlanConfigSource(ResourceStatisticsDto dto);

    Long selectUserPlanRenderSource(ResourceStatisticsDto dto);

    Long selectUserScenePlanRenderSource(ResourceStatisticsDto dto);

    Long selectUserTextureSource(ResourceStatisticsDto dto);

    Long selectShopPicSource(ResourceStatisticsDto dto);

    Long selectShopIntroducedSource(ResourceStatisticsDto dto);

    /**************************** 用户资源统计辅助SQL ***************************/

    /** 查询店铺封面、logo、介绍 资源id集合 */
    List<ShopResourceDto> selectShop(ResourceStatisticsDto dto);

    /** 查询店铺博文封id集合 */
    List<Integer> selectShopArticleCover(ResourceStatisticsDto dto);

    /** 查询店铺工程案例 封面、logo、介绍 资源id 集合 */
    List<ShopResourceDto> selectShopCase(ResourceStatisticsDto dto);


    /*************************** 企业资源统计SQL ***************************/
    List<CompanyDto> listCompany(ResourceStatisticsDto dto);

    List<Integer> listBrand(@Param("companyId") Integer companyId);

    List<TextureDto> listCompanyTextrue(ResourceStatisticsDto dto);

    List<TextureDto> listCompanyModelTextrue(ResourceStatisticsDto dto);

    Long selectCompanyPlanConfigSource(ResourceStatisticsDto dto);

    Long selectCompanyPlanRenderSource(ResourceStatisticsDto dto);

    Long selectCompanySharePlanConfigSource(ResourceStatisticsDto dto);

    Long selectCompanySharePlanRenderSource(ResourceStatisticsDto dto);

    Long selectCompanyTextrueSource(ResourceStatisticsDto dto);

    Long selectCompanyModelSource(ResourceStatisticsDto dto);

    List<ProductDto> listProductPic(ResourceStatisticsDto dto);

    List<Integer> listCompanyUserList(ResourceStatisticsDto dto);

    Long selectCompanySource(ResourceStatisticsDto dto);

    List<ShopResourceDto> listCompanyOffline(ResourceStatisticsDto dto);

    /**------查询图片资源--------*/
    // 主图
    List<PicDto> listPidPic(ResourceStatisticsDto dto);

    // 图片资源
    Long selectPicSource(ResourceStatisticsDto dto);

    // 模型资源
    Long selectModelSource(ResourceStatisticsDto dto);

    /** 户型资源 */
    List<HouseDto> listHouse(ResourceStatisticsDto dto);

    List<HouseDto> listHouseSpace(ResourceStatisticsDto dto);

    List<HouseDto> listHouseSpaceTemplet(ResourceStatisticsDto dto);

    Long selectHouseFileSource(ResourceStatisticsDto dto);

    Long selectSupplyDemandSource(ResourceStatisticsDto dto);

    Long selectZoneSource(ResourceStatisticsDto dto);

    List<PicDto> listArticleSourceOne(ResourceStatisticsDto dto);
}
