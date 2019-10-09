package com.sandu.search.service.index;

import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.exception.GroupProductSearchException;
import com.sandu.search.exception.ProductIndexException;

import java.util.List;

/**
 * 分组产品索引服务
 *
 * @author xiaoxc
 * @date 2019-03-19
 */
public interface GroupProductIndexService {

    /**
     * 获取产品分组数据
     *
     * @param groupIdList 组合Id集合
     * @param start       起始数
     * @param limit       最大数
     * @return
     */
    List<GroupProductPo> queryGroupProductList(List<Integer> groupIdList, int start, int limit) throws GroupProductSearchException;
}
