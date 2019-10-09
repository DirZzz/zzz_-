package com.sandu.search.service.index.impl;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.dao.GroupProductIndexDao;
import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.exception.GroupProductSearchException;
import com.sandu.search.exception.ProductIndexException;
import com.sandu.search.service.index.GroupProductIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分组产品索引服务
 *
 * @date 20171225
 * @auth zhengyoucai
 */
@Slf4j
@Service("groupProductIndexService")
public class GroupProductIndexServiceImpl implements GroupProductIndexService {

    private final static String CLASS_LOG_PREFIX = "分类产品索引服务:";

    private final GroupProductIndexDao groupProductIndexDao;

    @Autowired
    public GroupProductIndexServiceImpl(GroupProductIndexDao groupProductIndexDao) {
        this.groupProductIndexDao = groupProductIndexDao;
    }

    @Override
    public List<GroupProductPo> queryGroupProductList(List<Integer> groupIdList, int start, int limit) throws GroupProductSearchException {
        //初始化数据条数
        if (0 == limit) {
            limit = IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT;
        }
        //查询组合产品信息
        log.info(CLASS_LOG_PREFIX + "正在查询组合产品信息第{}-{}条.", start, (start + limit));
        List<GroupProductPo> groupProductPoList;
        try {
            groupProductPoList = groupProductIndexDao.queryGroupProductList(groupIdList, start, limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取组合产品数据第{}-{}条失败,Exception:" + e, start, (start + limit));
            throw new GroupProductSearchException(CLASS_LOG_PREFIX + "获取组合产品数据第" + start + "-" + (start + limit) + "条失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询组合产品信息完成,List<GroupProductPo>长度:{}.", groupProductPoList.size());

        return groupProductPoList;
    }
}
