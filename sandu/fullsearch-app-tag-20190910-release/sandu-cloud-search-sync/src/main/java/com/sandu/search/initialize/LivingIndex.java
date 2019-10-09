package com.sandu.search.initialize;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.index.LivingIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.LivingIndexException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.HouseIndexService;
import com.sandu.search.storage.house.HouseLivingMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LivingIndex {

    private final static String CLASS_LOG_PREFIX = "初始化搜索引擎小区索引:";

    @Autowired
    private ElasticSearchConfig elasticSearchConfig;

    @Autowired
    private HouseIndexService houseIndexService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private HouseLivingMetaDataStorage houseLivingMetaDataStorage;

    @PostConstruct
    public void initElasticSearchHouseIndex() {

        //开始时间
        long startTime = System.currentTimeMillis();


        if (!elasticSearchConfig.isInitLivingIndex()) {
            log.info(CLASS_LOG_PREFIX + "index living flag : {}", elasticSearchConfig.isInitLivingIndex());
            return;
        }

        log.info(CLASS_LOG_PREFIX + "开始索引小区数据........, index Name:{},", elasticSearchConfig.getLivingDataIndexName());

        //数据查询初始位
        int start = 0;
        //每次数据量
        int limit = IndexInfoQueryConfig.DEFAULT_QUERY_LIVINGPOINFO_LIMIT;

        //是否继续处理
        boolean isContinueHandler = true;
        //总数据量
        int totalProductCount = 0;
        //总索引量
        int totalIndexCount = 0;
        //异常数据
        int totalExceptionCount = 0;

        while (isContinueHandler) {

            List<Integer> livingIds;
            /********************************** 查询base_living中的数据,只取id,分两步做的原因是第二条sql执行速度太慢了 *********************************/
            try {
                livingIds = houseLivingMetaDataStorage.listLivingId(start, limit);
            } catch (LivingIndexException e) {
                log.error(CLASS_LOG_PREFIX + "查询小区信息失败:LivingIndexException:{}", e);
                return;
            }
            //无数据中断操作
            if (null == livingIds || 0 == livingIds.size()) {
                log.info(CLASS_LOG_PREFIX + "查询小区数据为空：start:{},limit:{}.", start, limit);
                return;
            }
            //数据不足指定数据量表示已查询出最后一条数据,下轮终止循环
            if (livingIds.size() < IndexInfoQueryConfig.DEFAULT_QUERY_LIVINGPOINFO_LIMIT) {
                isContinueHandler = false;
            }

            Map<String, Integer> handlerDataMap = indexLivingData(livingIds);

            if (handlerDataMap == null) continue;

            //递增start下标
            start = start + limit;

            //累加数据量
            totalProductCount += handlerDataMap.get("handlerListSize");
            totalIndexCount += handlerDataMap.get("bulkIndexListSize");
            totalExceptionCount += handlerDataMap.get("failCount");
        }

        log.info(CLASS_LOG_PREFIX + "索引所有小区数据完成!!!小区数据量:{}, 索引数据量:{},失败数:{},共耗时:{}ms", new String[]{
                totalProductCount + "",
                totalIndexCount + "",
                totalExceptionCount + "",
                (System.currentTimeMillis() - startTime) + ""
        });
    }

    public Map<String, Integer> indexLivingData(List<Integer> livingIds) {

        if (CollectionUtils.isEmpty(livingIds)) return null;

        //批量提交数据对象
        List<Object> bulkIndexList = new ArrayList<>(IndexInfoQueryConfig.DEFAULT_QUERY_LIVINGPOINFO_LIMIT);
        int end;
        int limit;
        if (livingIds.size() < IndexInfoQueryConfig.DEFAULT_QUERY_LIVINGPOINFO_LIMIT){
            end = 1;
            limit = livingIds.size();
        }else{
            end = 2;
            limit = 500;
        }
        log.info(CLASS_LOG_PREFIX + "livingIds的size:{}, end:{},limit:{}", new String[]{
                livingIds.size() + "",
                end + "",
                limit + ""
        });
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < end; i++) {
            List<Integer> handleList = livingIds.stream().skip(i * limit).limit(limit).collect(Collectors.toList());

            List<HouseLivingPo> housePos = houseIndexService.listHouseByLivingIds(handleList);

            if (!CollectionUtils.isEmpty(housePos)){
                for (HouseLivingPo po : housePos) {
                    if (po.getTotalHouse() == null || po.getTotalHouse() == 0) continue;
                    LivingIndexMappingData livingIndexMappingData = new LivingIndexMappingData();
                    livingIndexMappingData.setLivingId(po.getLivingId());
                    livingIndexMappingData.setAreaCode(po.getAreaCode());
                    livingIndexMappingData.setTotalHouse(po.getTotalHouse());
                    livingIndexMappingData.setLivingName(po.getLivingName());
                    livingIndexMappingData.setGmtCreate(po.getGmtCreate());
                    livingIndexMappingData.setCityCode(po.getCityCode());

                    /*
                     * 创建索引对象
                     * */
                    IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                            elasticSearchConfig.getLivingDataIndexName(),
                            TypeConstant.TYPE_LIVING,
                            livingIndexMappingData.getLivingId() + "",
                            JsonUtil.toJson(livingIndexMappingData)
                    );
                    //加入批量对象
                    bulkIndexList.add(indexRequestDTO);
                }
            }
        }
        log.info(CLASS_LOG_PREFIX + "处理循环耗时{}", new String[]{(System.currentTimeMillis() - startTime) + ""});
        //索引数据
        startTime = System.currentTimeMillis();
        BulkStatistics bulkStatistics = new BulkStatistics();
        int failCount = 0;
        try {
            if (!CollectionUtils.isEmpty(bulkIndexList))
                bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引户型数据异常:ElasticSearchException:{}", e);
            failCount++;
        }
        log.info(CLASS_LOG_PREFIX + "索引数据耗时{}", new String[]{(System.currentTimeMillis() - startTime) + ""});


        /********************************** 处理小区信息数据 *********************************/
        log.info(CLASS_LOG_PREFIX + "索引户型数据成功:成功索引数:{},无效索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                failCount + "",
                bulkStatistics.toString()
        });

        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("bulkIndexListSize", bulkIndexList.size());
        dataMap.put("failCount", failCount);
        dataMap.put("handlerListSize", bulkIndexList.size());

        return dataMap;
    }

}
