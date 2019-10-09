package com.sandu.search.service.product;

import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.product.universal.vo.GroupProductSearchVo;
import com.sandu.search.entity.product.universal.vo.GroupProductVo;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.GroupProductSearchException;

import java.util.List;

/**
 * 组合产品搜索服务
 *
 * @author xiaoxc
 * @data 2019/3/22 0022.
 */
public interface GroupProductSearchService {


    /**
     * 通过条件搜索组合
     * @param searchVo
     * @return
     */
    SearchObjectResponse searchGroupProduct(GroupProductSearchVo searchVo) throws GroupProductSearchException;

    /**
     * 通过IdList搜索组合信息
     * @param groupIdList
     * @return
     */
    SearchObjectResponse searchGroupProductByIds(List<Integer> groupIdList) throws GroupProductSearchException;

    /**
     *  组合产品索引数据转化Vo
     * @param groupProductPoList  组合信息集合
     * @param functType           功能类型
     * @return
     */
    List<GroupProductVo> conversionGroupProductVo(List<GroupProductPo> groupProductPoList, LoginUser loginUser, String platformCode, String functType);

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

}
