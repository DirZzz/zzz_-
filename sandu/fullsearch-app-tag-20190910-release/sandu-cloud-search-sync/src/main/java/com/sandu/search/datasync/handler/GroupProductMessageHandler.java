package com.sandu.search.datasync.handler;

import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.common.tools.EntityUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.DeleteRequestDTO;
import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.exception.*;
import com.sandu.search.initialize.GroupProductIndex;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.GroupProductIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组合产品消息处理
 *
 * @author xiaoxc
 * @date 2019/03/25
 */
@Slf4j
@Component
public class GroupProductMessageHandler {

    private final static String CLASS_LOG_PREFIX = "Rabbit组合产品消息处理:";

    private final GroupProductIndexService groupProductIndexService;
    private final GroupProductIndex groupProductIndex;
    private final ElasticSearchConfig elasticSearchConfig;
    private final ElasticSearchService elasticSearchService;

    @Autowired
    public GroupProductMessageHandler(
            GroupProductIndexService groupProductIndexService,
            GroupProductIndex groupProductIndex,
            ElasticSearchConfig elasticSearchConfig,
            ElasticSearchService elasticSearchService) {
        this.elasticSearchConfig = elasticSearchConfig;
        this.elasticSearchService = elasticSearchService;
        this.groupProductIndexService = groupProductIndexService;
        this.groupProductIndex = groupProductIndex;
    }

    //待更新组合产品列表
    private static volatile List<Integer> waitUpdateRecommendationPlanIdList = new ArrayList<>();

    /**
     * 新增/更新组合产品消息处理
     *
     * @param groupProductPoList 组合产品索引对象
     * @return
     */
    @SuppressWarnings("all")
    public boolean addAndUpdate(List<GroupProductPo> groupProductPoList) {

        if (null == groupProductPoList || 0 >= groupProductPoList.size()) {
            log.info(CLASS_LOG_PREFIX + "消费消息失败，消息对象为空。");
            return false;
        }

        //组合增量更新
        List<GroupProductPo> groupList = new ArrayList<>(groupProductPoList.size());
        //根据组合产品ID更新
        List<Integer> groupIdList = new ArrayList<>(groupProductPoList.size());

        groupProductPoList.forEach(groupProductPo -> {
            //判断更新类型
            List<String> fieldList = null;
            try {
                fieldList = EntityUtil.queryHasValueExcludeNameIsIdInField(groupProductPo);
            } catch (IllegalAccessException e) {
                log.warn(CLASS_LOG_PREFIX + "检查对象失败,IllegalAccessException:{}.", e);
            }

            if (null != fieldList && 0 < fieldList.size()) {
                //属性增量更新
                groupList.add(groupProductPo);
            } else {
                if (groupProductPo.getId() != null) {
                    groupIdList.add(groupProductPo.getId());
                }
            }
        });

        if (null != groupIdList && 0 < groupIdList.size()) {
            sycnGroupProductData(groupIdList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);
            log.info(CLASS_LOG_PREFIX + "消费消息成功，组合产品已更新!groupIdList:{}", JsonUtil.toJson(groupIdList));
        }

        //属性增量更新
        /*if (null != groupList && 0 < groupList.size()) {
            //加入属性增量更新列表
            updateIncrGroupProductInfo(groupList);
            log.info(CLASS_LOG_PREFIX + "消费消息成功，组合产品已加入属性增量更新列表!groupList:{},现有待更新组合产品数据条数:{}.", JsonUtil.toJsonExcludeEmpty(groupList), groupList.size());
        }*/

        return true;
    }




    /**
     * 删除组合产品消息处理
     *
     * @param groupProductPoList 组合产品索引对象
     * @return
     */
    public boolean delete(List<GroupProductPo> groupProductPoList) throws ElasticSearchException {

        if (null == groupProductPoList || 0 >= groupProductPoList.size()) {
            log.info(CLASS_LOG_PREFIX + "消费消息失败，消息对象为空。");
            return false;
        }
        String indexName = StringUtils.isEmpty(elasticSearchConfig.getGroupProductDataIndexName())?IndexConstant.GROUP_PRODUCT_ALIASES:elasticSearchConfig.getGroupProductDataIndexName();
        for (GroupProductPo groupProductPo : groupProductPoList) {
            if (null != groupProductPo.getId()) {
                DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO(
                        indexName,
                        TypeConstant.TYPE_GROUP_PRODUCT,
                        groupProductPo.getId().toString());
                boolean falg = elasticSearchService.delete(deleteRequestDTO);
                if (!falg) {
                    log.error(CLASS_LOG_PREFIX + "删除组合产品失败,groupId:{}", groupProductPo.getId());
                }
            }
        }

        return true;
    }

    /**
     * 同步删除组合产品处理
     *
     * @param idList 组合产品索引对象
     * @return
     */
    public boolean deleteByIds(List<Integer> idList) throws ElasticSearchException {

        if (null == idList || 0 >= idList.size()) {
            log.info(CLASS_LOG_PREFIX + "同步删除组合产品失败，同步对象为空。");
            return false;
        }
        String indexName = StringUtils.isEmpty(elasticSearchConfig.getGroupProductDataIndexName())?IndexConstant.GROUP_PRODUCT_ALIASES:elasticSearchConfig.getGroupProductDataIndexName();
        for (Integer id : idList) {
            DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO(
                    indexName,
                    TypeConstant.TYPE_GROUP_PRODUCT,
                    id.toString());
            boolean falg = elasticSearchService.delete(deleteRequestDTO);
            if (!falg) {
                log.error(CLASS_LOG_PREFIX + "同步删除组合产品失败,id:{}", id);
            }
        }

        return true;
    }

    /**
     * 增量同步组合产品
     * @param groupIdList
     */
    public void sycnGroupProductData(List<Integer> groupIdList, String type) {

        //去空
        groupIdList = groupIdList.stream().filter(waitUpdateProductId -> null != waitUpdateProductId && 0 != waitUpdateProductId).collect(Collectors.toList());
        //去重
        groupIdList = groupIdList.stream().distinct().collect(Collectors.toList());

        if (null == groupIdList || groupIdList.size() == 0) {
            log.error(CLASS_LOG_PREFIX + "同步数据为空！GroupIdList:{}", JsonUtil.toJson(groupIdList));
            return;
        }

        List<GroupProductPo> groupProductPoList;
        /********************************** 查询组合产品信息 *********************************/
        //更新增量数据
        try {
            groupProductPoList = groupProductIndexService.queryGroupProductList(groupIdList,0, groupIdList.size());
        } catch (GroupProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "查询组合产品信息失败:GroupProductSearchException:{}", e);
            return;
        }

        if (null == groupProductPoList || 0 >= groupProductPoList.size()) {
            log.error(CLASS_LOG_PREFIX + "组合产品同步数据失败！");
            return;
        }

        //更新数据
        log.info(CLASS_LOG_PREFIX + "开始更新数据，数据总条数:{}.", groupProductPoList.size());
        int indexSuccessCount = groupProductIndex.indexGroupProdcutData(groupProductPoList, type);
        log.info(CLASS_LOG_PREFIX + "更新数据完成，成功{}条,失败{}条.", indexSuccessCount, groupProductPoList.size() - indexSuccessCount);
    }

}
