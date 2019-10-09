package com.sandu.api.solution.service;


import com.github.pagehelper.PageInfo;
import com.sandu.api.product.model.BaseProductStyle;
import com.sandu.api.solution.input.*;
import com.sandu.api.solution.model.FullHouseDesignPlan;
import com.sandu.api.solution.model.ResRenderPic;
import com.sandu.api.solution.model.bo.DesignPlanBO;
import com.sandu.api.solution.model.bo.FullHouseDesignPlanBO;
import com.sandu.api.solution.model.po.CopyShareDesignPlanPO;
import com.sandu.api.solution.model.po.DesignPlanDeliveryPO;
import com.sandu.api.solution.output.DesignerUserKVDto;
import com.sandu.api.solution.output.FullHouseDesignPlanCoverPicInfoDO;
import com.sandu.api.solution.output.FullHouseDesignPlanCoverPicInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: YuXingchi
 * @Description:
 * @Date: Created in 17:40 2018/8/22
 */
public interface FullHouseDesignPlanService {

    /**
     * 查询全屋方案列表
     *
     * @param query
     * @return
     */
    PageInfo<FullHouseDesignPlanBO> selectListSelective(FullHouseDesignPlanQuery query);

    /**
     * 根据ids查询相关方案信息
     * @param planIds
     * @return
     */
    PageInfo<FullHouseDesignPlanBO> getFullDesignPlanlistByIds(List<Integer> planIds);

    /**
     * 全屋方案上下架
     *
     * @param planId
     * @param platformIds
     * @return
     */
    Map<String, Object> putFullHouseDesignPlan(String planId, String platformIds);

    /**
     * 根据方案，公司查询全屋方案
     *
     * @param planId
     * @param companyId
     * @return
     */
    FullHouseDesignPlanBO getBaseInfo(Integer planId, Integer companyId);

    /**
     * 修改全屋方案详情
     *
     * @param designPlanUpdate
     * @return
     */
    int updateFullHouseDetail(FullHouseDesignPlanUpdate designPlanUpdate);

    /**
     * 根据id删除方案
     *
     * @param planId
     */
    int deletePlanById(Long planId);

    /**
     * 根据id查询全屋方案
     *
     * @param planId
     * @return
     */
    FullHouseDesignPlan getFullHousePlanById(Integer planId);

    /**
     * 批量交付全屋方案
     *
     * @param designPlanDeliveryPO
     * @param isBatch
     * @return
     */
    List<Integer> deliver(DesignPlanDeliveryPO designPlanDeliveryPO, boolean isBatch);

    /**
     * 公开全屋方案
     *
     * @param planIds
     * @param secrecy
     * @return
     */
    boolean batchChangePlanSecrecy(List<Integer> planIds, Integer secrecy);

    /**
     * 分享方案库
     *
     * @param query
     * @return
     */
    PageInfo<FullHouseDesignPlanBO> shareDesignPlan(FullHouseDesignPlanQuery query);

    Integer copyDesignPlanToCompany2(CopyShareDesignPlanPO copyShareDesignPlanPO);

    /**
     * 使用分享方案
     *
     * @param copyShareDesignPlanPO
     */
    void copyDesignPlanToCompany(CopyShareDesignPlanPO copyShareDesignPlanPO);

    List<BaseProductStyle> styleList();

    /**
     * 设置方案设计描述
     *
     * @param config
     */
    void configDesignPlan(DesignPlanConfig config);

    /**
     * 获取方案设计描述
     *
     * @param id
     * @return content
     */
    String showDesignPlanConfig(Integer id);

    /**
     * 获取推荐方案详情
     *
     * @param planId
     * @param companyId
     * @return
     */
    List<DesignPlanBO> getDetailDesignPlan(Integer planId, Integer companyId);

    /**
     * 查询店铺管理全屋方案
     * @param query
     * @return
     */
    PageInfo<FullHouseDesignPlanBO> selectStoreFullHousePlan(FullHouseDesignPlanQuery query);

    /**
     * 取消发布
     * @param lists
     * @return
     */
    Integer cancelPublish(List<CompanyShopDesignPlanAdd> lists);

    /**
     * 发布
     * @param companyShopDesignPlanAddList
     * @return
     */
    Integer publish(List<CompanyShopDesignPlanAdd> companyShopDesignPlanAddList);

    int editSalePrice(Long id, Double salePrice, Integer salePriceChargeType);

    int editPlanPrice(Long id, Double planPrice, Integer chargeType);

    PageInfo<DesignPlanBO> findManagerSXWFullHouseDesign(FullHouseDesignPlanQuery query);

    boolean modifyPlanForManager(ManagerSXWPlanUpdate update);

    int save(FullHouseDesignPlan fullHouseDesignPlan);

    List<DesignerUserKVDto> queryDesignerListByCompanyId(Integer companyId);

    void deletePlanByIds(List<Integer> fullPlanIds);

    List<ResRenderPic> fetchSubPlanPhotoLevelCoverPics(Integer fullHousePlanId);

    int modifyFullHousePlanCoverPics(Integer fullHousePlanId, String coverPicIds);

    /**
     * 获取该全屋推荐方案下所有推荐方案的封面图信息
     * 
     * @author huangsongbo 2019.08.30
     * @param id
     * @return
     */
	List<FullHouseDesignPlanCoverPicInfoDTO> getFullHouseDesignPlanCoverPicInfoDTO(Long id);
	
}