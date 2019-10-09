package com.sandu.search.service.living.impl;

import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import com.sandu.search.service.living.LivingSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service(value = "livingSearchService")
public class LivingSearchServiceImpl implements LivingSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchObjectResponse searchLiving(Integer curPage, Integer pageSize, String areaCode, String livingName, String cityCode) {

        //设置搜索condition
        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //排序对象
        List<SortOrderObject> sortOrderObjectList = new ArrayList<>();

        //build cityCode condition
        buildRegionCondition(areaCode,matchQueryList,livingName,cityCode);

       //execute search
        SearchResponse searchResponse =  executeSearch(matchQueryList,sortOrderObjectList,curPage,pageSize);

        log.info("search result =>{}" + searchResponse);
        List<Object> objectDataList = analysisResult(searchResponse);
        return new SearchObjectResponse(objectDataList, searchResponse.getHits().totalHits, searchResponse.getAggregations());
    }

    private List<Object> analysisResult(SearchResponse searchResponse) {
        List<Object> objectDataList = new ArrayList<>((int)searchResponse.getHits().totalHits);
        Iterator<SearchHit> iterator = searchResponse.getHits().iterator();
        while (iterator.hasNext()){
            objectDataList.add(JsonUtil.fromJson(iterator.next().getSourceAsString(), HouseLivingPo.class));
        }
        return objectDataList;
    }

    private SearchResponse executeSearch(List<QueryBuilder> matchQueryList, List<SortOrderObject> sortOrderObjectList, Integer curPage, Integer pageSize) {
        //多条件匹配
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(matchQueryList)){
            matchQueryList.forEach(boolQueryBuilder::must);
        }

        //搜索查询条件组建
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from((curPage - 1) * pageSize);
        sourceBuilder.size(pageSize);
        sourceBuilder.sort("gmtCreate.keyword",SortOrder.DESC);

        log.info("搜索数据DSL:{}", sourceBuilder.toString());

        SearchRequest searchRequest = new SearchRequest("living_info_aliases");

        searchRequest.source(sourceBuilder);
        SearchResponse search;
        try {
            search = restHighLevelClient.search(searchRequest);
        }catch (Exception e){
            log.error("系统异常",e);
            return null;
        }
        return search;
    }

    private void buildRegionCondition(String areaCode, List<QueryBuilder> matchQueryList, String livingName, String cityCode) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(cityCode)){
            boolQueryBuilder.must(QueryBuilders.termQuery("cityCode",cityCode));
        }
        if (!StringUtils.isEmpty(areaCode)){
            boolQueryBuilder.must(QueryBuilders.termQuery("areaCode",areaCode));
        }
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("livingName.keyword","*"+livingName+"*"));
        matchQueryList.add(boolQueryBuilder);
    }
}
