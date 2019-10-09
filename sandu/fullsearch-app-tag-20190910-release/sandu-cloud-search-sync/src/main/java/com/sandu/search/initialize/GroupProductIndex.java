package com.sandu.search.initialize;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.entity.elasticsearch.po.metadate.GroupPlatformRelPo;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.GroupProductSearchException;
import com.sandu.search.exception.ProductIndexException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.GroupProductIndexService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.product.GroupPlatformMetaDataStorage;
import com.sandu.search.storage.product.ProductCategoryMetaDataStorage;
import com.sandu.search.storage.product.ProductCategoryRelMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 搜索引擎组合产品索引初始化
 *
 * @author xiaoxc
 * @date 2019-03-19
 */
@Slf4j
@Component
public class GroupProductIndex {

    private final static String CLASS_LOG_PREFIX = "搜索引擎组合产品索引初始化:";

    private final ElasticSearchService elasticSearchService;
    private final GroupProductIndexService groupProductIndexService;
    private final BrandMetaDataStorage brandMetaDataStorage;
    private final ElasticSearchConfig elasticSearchConfig;
    private final GroupPlatformMetaDataStorage groupPlatformMetaDataStorage;
    private final ProductCategoryRelMetaDataStorage productCategoryRelMetaDataStorage;
    private final ProductCategoryMetaDataStorage productCategoryMetaDataStorage;


    @Autowired
    public GroupProductIndex(ProductCategoryMetaDataStorage productCategoryMetaDataStorage, ProductCategoryRelMetaDataStorage productCategoryRelMetaDataStorage, GroupPlatformMetaDataStorage groupPlatformMetaDataStorage, ElasticSearchConfig elasticSearchConfig,ElasticSearchService elasticSearchService, GroupProductIndexService groupProductIndexService, BrandMetaDataStorage brandMetaDataStorage) {
        this.elasticSearchService = elasticSearchService;
        this.groupProductIndexService = groupProductIndexService;
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.elasticSearchConfig = elasticSearchConfig;
        this.groupPlatformMetaDataStorage = groupPlatformMetaDataStorage;
        this.productCategoryRelMetaDataStorage = productCategoryRelMetaDataStorage;
        this.productCategoryMetaDataStorage = productCategoryMetaDataStorage;
    }


    //同步组合产品信息数据
    public void syncGroupProductInfoData() {
        //开始时间
        long startTime = System.currentTimeMillis();
        //数据查询初始位
        int start = 0;
        //每次数据量
        int limit = IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT;

        //是否继续处理
        boolean isContinueHandler = true;
        //总数据量
        int totalProductCount = 0;
        //总索引量
        int totalIndexCount = 0;
        //异常数据
        int totalExceptionCount = 0;

        while (isContinueHandler) {

            List<GroupProductPo> groupProductPoList;
            /********************************** 查询组合产品信息 *********************************/
            try {
                groupProductPoList = groupProductIndexService.queryGroupProductList(null, start, limit);
            } catch (GroupProductSearchException e) {
                log.error(CLASS_LOG_PREFIX + "查询组合产品信息失败:GroupProductIndexException:{}", e);
                return;
            }
            //无数据中断操作
            if (null == groupProductPoList || 0 == groupProductPoList.size()) {
                log.info(CLASS_LOG_PREFIX + "查询组合产品息数据为空：start:{},limit:{}.", start, limit);
                return;
            }
            //数据不足指定数据量表示已查询出最后一条数据,终止循环
            if (groupProductPoList.size() < IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT) {
                isContinueHandler = false;
            }

            /********************************** 索引组合产品数据 *********************************/
            int successIndexCount = indexGroupProdcutData(groupProductPoList, UserDefinedConstant.FULL_UPDATE_TYPE);

            //递增start下标
            start = start + limit;

            //累加数据量
            totalProductCount += groupProductPoList.size();
            totalIndexCount += groupProductPoList.size();
            totalExceptionCount += groupProductPoList.size() - successIndexCount;
        }

        log.info(CLASS_LOG_PREFIX + "索引所有组合产品数据完成!!!组合产品数据量:{}, 索引数据量:{},失败数:{},共耗时:{}ms", new String[]{
                totalProductCount + "",
                totalIndexCount + "",
                totalExceptionCount + "",
                (System.currentTimeMillis() - startTime) + ""
        });
    }

    //索引数据
    public int indexGroupProdcutData(List<GroupProductPo> groupProductPoList, String updateType) {

        if (Objects.equals(UserDefinedConstant.FULL_UPDATE_TYPE, updateType)) {
            log.info(CLASS_LOG_PREFIX + "开始全量更新组合产品数据...");
        } else if(Objects.equals(UserDefinedConstant.INCREMENT_UPDATE_TYPE, updateType)) {
            log.info(CLASS_LOG_PREFIX + "开始增量更新组合产品数据...");
        } else {
            log.error(CLASS_LOG_PREFIX + "非法入侵组合产品更新方法");
            return 0;
        }

        if (groupProductPoList == null || groupProductPoList.size() <= 0) {
            return 0;
        }

        //批量提交数据对象
        List<Object> bulkIndexList = new ArrayList<>(IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT);

        int failCount = 0;

        /********************************** 处理产组合产品基本信息数据 *********************************/
        // 组合产品索引数据对象
        for (int i = 0; i < groupProductPoList.size(); i++) {
            GroupProductPo groupProduct = groupProductPoList.get(i);
            //获取品牌名称
            if (null != groupProduct.getBrandId()) {
               groupProduct.setBrandName(brandMetaDataStorage.getBrandNameById(groupProduct.getBrandId()));
            }

            //主产品包含的所有分类长code
            List<Integer> allProductCategoryId = productCategoryRelMetaDataStorage.getCategoryListByProductId(groupProduct.getMainProductId());
            groupProduct.setCategoryLongCode(productCategoryMetaDataStorage.queryProductCategoryLongCodeByCategoryIdList(allProductCategoryId));

            //组合平台数据
            List<GroupPlatformRelPo> groupPlatformRelPoList = groupPlatformMetaDataStorage.getGroupPlatformById(groupProduct.getId());
            if (null != groupPlatformRelPoList && groupPlatformRelPoList.size() > 0) {
                groupProduct.setGroupPlatformRel(groupPlatformRelPoList);
            }

            IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                    elasticSearchConfig.getGroupProductDataIndexName(),
                    TypeConstant.TYPE_GROUP_PRODUCT,
                    groupProduct.getId().toString(),
                    JsonUtil.toJson(groupProduct)
            );
            //加入批量对象
            bulkIndexList.add(indexRequestDTO);

            if (0 == i % 100) {
                log.info(CLASS_LOG_PREFIX + "已解析{}条组合产品数据,剩余{}条", i, groupProductPoList.size() - i);
            }
        }

        //索引数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引组合产品数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引组合产品数据成功:成功索引数:{},无效索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                failCount + "",
                bulkStatistics.toString()
        });

        return groupProductPoList.size() - failCount;
    }

    public static void main(String[] args) {
        System.out.println(63095 / 1000);
    }
}
