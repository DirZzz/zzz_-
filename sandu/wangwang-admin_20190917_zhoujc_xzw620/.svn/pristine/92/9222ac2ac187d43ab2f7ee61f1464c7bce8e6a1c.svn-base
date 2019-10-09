package com.sandu.service.solution.dao;


import com.sandu.api.solution.input.CompanyShopDesignPlanAdd;
import com.sandu.api.solution.input.FullHouseDesignPlanQuery;
import com.sandu.api.solution.model.CompanyShopDesignPlan;
import com.sandu.api.solution.model.FullHouseDesignPlan;
import com.sandu.api.solution.model.bo.DesignPlanBO;
import com.sandu.api.solution.model.bo.FullHouseDesignPlanBO;
import com.sandu.api.solution.output.DesignerUserKVDto;
import com.sandu.api.solution.output.FullHouseDesignPlanCoverPicInfoDO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: YuXingchi
 * @Description:
 * @Date: Created in 15:18 2018/8/22
 */

@Repository
public interface FullHouseDesignPlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FullHouseDesignPlan record);

    int insertSelective(FullHouseDesignPlan record);

    FullHouseDesignPlan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FullHouseDesignPlan record);

    int updateByPrimaryKey(FullHouseDesignPlan record);

    List<FullHouseDesignPlanBO> selectListSelective(FullHouseDesignPlanQuery query);

    int batchUpdatePlanSecrecy(@Param("planIds")List<Integer> updateIds, @Param("secrecyFlag")Integer secrecyFlag);

    List<FullHouseDesignPlanBO> listShareDesignPlan(FullHouseDesignPlanQuery query);

    /**
     * 查看详情
     * @param planId
     * @return
     */
    List<FullHouseDesignPlanBO> getBaseInfo(@Param("planId") Integer planId);

    /**
     * 根据店铺查询全屋方案
     * @param shopId
     * @return
     */
    List<CompanyShopDesignPlan> storeFullHouseByShopId(@Param("shopId") Integer shopId);

    /**
     * 取消发布
     * @param shopId
     * @param planIds
     * @return
     */
    Integer cancelPublish(@Param("shopId") Integer shopId, @Param("planIds") List<Integer> planIds);

    /**
     * 发布
     * @param add
     * @return
     */
    Integer publish(CompanyShopDesignPlanAdd add);

    int updateSalePrice(@Param("id") Long id, @Param("salePrice")Double salePrice, @Param("salePriceChargeType")Integer salePriceChargeType);

    int updatePlanPrice(@Param("id")Long id, @Param("planPrice")Double planPrice, @Param("chargeType")Integer chargeType);

    List<DesignPlanBO> selectManagerSXWFullHouseDesignPlan(FullHouseDesignPlanQuery query);
    List<FullHouseDesignPlanBO> getPushStateByPlanIds(@Param("planIds") List<Integer> lists);

    int updatePutawayTime(@Param("id")Long id);

    List<DesignerUserKVDto> queryDesignerListByCompanyId(@Param("companyId")Integer companyId);

    List<FullHouseDesignPlanBO> selectListByPlands(@Param("planIds") List<Integer> planIds);

    @Select("\t\tselect superior.id from\n" +
            "\t\tdesign_plan_recommended_superior superior\n" +
            "\t\tinner join full_house_design_plan plan on plan.id = superior.design_plan_recommended_id\n" +
            "\t\twhere\n" +
            "\t\t superior.space_type =13\n" +
            "\t\t  and superior.is_deleted = 0\n" +
            "\t\tand plan.is_deleted != superior.is_deleted;")
    List<Integer> hasInvalidPlanRecommended();

    void deleteSuperiorsByIds(@Param("superiors") List<Integer> superiors);

    void deleteByPrimaryKeys(@Param("fullPlanIds") List<Integer> fullPlanIds);

    @Update("update full_house_design_plan set is_show_cover_pic_ids = #{coverPicIds} where id = #{id}")
    int updateIsShowCoverPicIdsById(@Param("id") Integer fullHousePlanId, @Param("coverPicIds") String coverPicIds);

	List<FullHouseDesignPlanCoverPicInfoDO> selectFullHouseDesignPlanCoverPicInfoDOById(@Param("id") Long id);

	@Select("select is_show_cover_pic_ids from full_house_design_plan where id = #{id}")
	String selectIsShowCoverPicIdsById(@Param("id") Long id);
	
}