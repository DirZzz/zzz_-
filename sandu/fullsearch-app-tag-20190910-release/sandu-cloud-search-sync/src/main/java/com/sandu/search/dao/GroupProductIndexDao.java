package com.sandu.search.dao;

import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类产品索引数据访问层
 *
 * @author xiaoxc
 * @date 2019-03-19
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
    List<GroupProductPo> queryGroupProductList(@Param("groupIdList") List<Integer> groupIdList, @Param("start") int start, @Param("limit") int limit);
}
