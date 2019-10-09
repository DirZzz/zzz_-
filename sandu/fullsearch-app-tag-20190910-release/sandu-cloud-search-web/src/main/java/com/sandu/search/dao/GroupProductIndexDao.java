package com.sandu.search.dao;

import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.entity.product.po.GroupProductCollectPo;
import com.sandu.search.entity.product.universal.vo.GroupProductSearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 组合产品数据访问层
 *
 * @date 2019-03-19
 * @auth xiaoxc
 */
@Repository
public interface GroupProductIndexDao {

    /**
     * 获取产品数据
     *
     * @param start 起始数
     * @param limit 最大数
     * @return
     */
    List<GroupProductPo> queryGroupProductList(@Param("start") int start, @Param("limit") int limit);

    /**
     * 获取组合收藏信息
     * @param userId
     * @return
     */
    List<GroupProductCollectPo> findGroupCollectInfo(@Param("userId") Integer userId, @Param("groupIds") List<Integer> groupIds);

    /**
     * 获取用户收藏组合数量
     * @param groupProductSearchVo
     * @return
     */
    int getGroupCollectCount(GroupProductSearchVo groupProductSearchVo);

    /**
     * 获取用户收藏组合ID集合
     * @param groupProductSearchVo
     * @return
     */
    List<Integer> getGroupCollectList(GroupProductSearchVo groupProductSearchVo);

    /**
     * 获取组合总价
     * @param groupId
     * @param platformCode
     * @return
     */
    BigDecimal getGroupPrice(@Param("groupId") Integer groupId, @Param("platformCode") String platformCode);
}
