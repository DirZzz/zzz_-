package com.sandu.search.service.product.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sandu.common.util.Utils;
import com.sandu.common.util.collections.Lists;
import com.sandu.search.common.constant.GroupProductConstant;
import com.sandu.search.common.constant.QueryConditionField;
import com.sandu.search.common.tools.EntityCopyUtils;
import com.sandu.search.dao.GroupProductIndexDao;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import com.sandu.search.entity.product.po.GroupProductCollectPo;
import com.sandu.search.entity.product.universal.vo.GroupProductSearchVo;
import com.sandu.search.entity.product.universal.vo.GroupProductVo;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.GroupProductSearchException;
import com.sandu.search.service.base.CompanyBrandService;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.product.GroupProductSearchService;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 组合搜索服务实现
 *
 * @author xiaoxc
 * @data 2019/3/22 0022.
 */
@Slf4j
@Service("groupProductSearchService")
public class GroupProductSearchServiceImpl implements GroupProductSearchService {

    private static final String CLASS_LOG_PREFIX = "组合产品搜索服务:";

    private final ElasticSearchService elasticSearchService;
    private final GroupProductIndexDao groupProductIndexDao;
    private final CompanyBrandService companyBrandService;

    @Autowired
    public GroupProductSearchServiceImpl(ElasticSearchService elasticSearchService, GroupProductIndexDao groupProductIndexDao, CompanyBrandService companyBrandService) {
        this.elasticSearchService = elasticSearchService;
        this.groupProductIndexDao = groupProductIndexDao;
        this.companyBrandService = companyBrandService;
    }


    @Override
    public SearchObjectResponse searchGroupProduct(GroupProductSearchVo searchVo) throws GroupProductSearchException {

        if (null == searchVo) {
            log.error(CLASS_LOG_PREFIX + "搜索组合产品失败,必传参数为空！");
            throw new GroupProductSearchException("搜索组合产品失败,必传参数为空！");
        }

        /********************************* Step 1: 组装搜索条件 *************************************************/
        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //排序对象
        List<SortOrderObject> sortOrderObjectList = new ArrayList<>();
        //初始化
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //企业品牌列表
        List<Integer> brandIdList = searchVo.getBrandIdList();
        //主产品大分类
        Integer productTypeValue = searchVo.getProductTypeValue();

        //TODO 搜索本类条件
        Integer searchType = searchVo.getSearchType();
        if (null == searchType || GroupProductConstant.GROUP_TYPE_SEARCH_THIS == searchType) {
            //主产品分类编码
            String categoryLongCode = searchVo.getCategoryLongCode();
            if (!StringUtils.isBlank(categoryLongCode)) {
                boolQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CATEGORY_LONG_CODE, categoryLongCode));
            }

            if (null != productTypeValue && productTypeValue > 0) {
                boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue));

                //小类
                List<Integer> productSmallTypeValueList = searchVo.getProductSmallTypeList();
                //公司可见范围小类
                List<Integer> companyProductVisibilityRangeIdList = searchVo.getCompanyProductVisibilityRangeIdList();
                //公司可见范围的小类(因为可见范围的小类要单独处理-经过品牌过滤)
                List<Integer> productSmallTypeVisibilityRangeList = (null == companyProductVisibilityRangeIdList || 0 >= companyProductVisibilityRangeIdList.size()) ?
                        null : productSmallTypeValueList.stream().filter(companyProductVisibilityRangeIdList::contains).collect(toList());
                //除公司可见范围之外的小类(因为可见范围的小类要单独处理-经过品牌过滤)
                List<Integer> nowProductSmallTypeList = (null == productSmallTypeVisibilityRangeList || 0 >= productSmallTypeVisibilityRangeList.size()) ?
                        productSmallTypeValueList : productSmallTypeValueList.stream().filter(item -> !productSmallTypeVisibilityRangeList.contains(item)).collect(toList());

                //可见范围组装dsl语句，如果没有可见范围值直接大类过滤了
                if (CollectionUtils.isNotEmpty(productSmallTypeVisibilityRangeList)) {
                    BoolQueryBuilder visibilityRangeBuilder = QueryBuilders.boolQuery();
                    BoolQueryBuilder brandBuilder = QueryBuilders.boolQuery();
                    brandBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_PRODUCT_SMALL_TYPE_VALUE, productSmallTypeVisibilityRangeList));

                    //可见分类需要品牌过滤
                    if (null != brandIdList && brandIdList.size() > 0) {
                        brandBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_BRAND_ID, brandIdList));
                    }
                    visibilityRangeBuilder.should(brandBuilder);

                    if (CollectionUtils.isNotEmpty(nowProductSmallTypeList)) {
                        visibilityRangeBuilder.should(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_PRODUCT_SMALL_TYPE_VALUE, nowProductSmallTypeList));
                    }
                    boolQueryBuilder.filter(visibilityRangeBuilder);
                }
            }

            //组合分类 eg:沙发组合、餐桌组合
            Integer compositeType = searchVo.getCompositeType();
            if (null != compositeType && compositeType > 0) {
                boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_COMPOSITE_TYPE, compositeType));
            }
        } else {
            //搜索全部组合数据过滤可见分类品牌
            Map<Integer, List<Integer>> companyAliveTypeMap = searchVo.getCompanyAliveTypeMap();
            if ((null != companyAliveTypeMap && companyAliveTypeMap.size() > 0)
                    && (CollectionUtils.isNotEmpty(brandIdList))) {
                disposeFullSearchGroup(companyAliveTypeMap, brandIdList, boolQueryBuilder);
            }
        }

        //组合状态根据用户类型判断获取
        List<Integer> stateList = searchVo.getStateList();
        if (null != stateList && stateList.size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_STATE, stateList));
        }


        //一值多字段匹配
        this.getMultiMatchQueryBuilder(searchVo.getMultiMatchFieldList(), boolQueryBuilder);

        //组合面积 查询小于等于空间面积的组合
        Integer spaceAreas = searchVo.getSpaceAreas();
        if (null != spaceAreas && spaceAreas > 0) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_SPACE_AREAS);
            rangeQueryBuilder.lte(spaceAreas);
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        //我的组合搜索标识
        Integer myUserId = searchVo.getMyUserId();
        if (null != myUserId && myUserId > 0) {
            //查询用户创建的组合
            boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_MY_GROUP_USER_ID, myUserId));
        } else {
            //组合类型(0:普通组合;1:一件装修组合)
            Integer groupType = searchVo.getGroupType();
            if (null != groupType) {
                boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_TYPE, groupType));
            }
        }
        //逻辑删除过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_PRODUCT_IS_DELETEd, 0));

        matchQueryList.add(boolQueryBuilder);

        //时间排序DESC
        sortOrderObjectList.add(new SortOrderObject(
                QueryConditionField.QUERY_CONDITION_FIELD_GROUP_GMT_MODIFIED,
                SortOrder.DESC,
                SortMode.MEDIAN,
                SortOrderObject.DEFAULT_SORT
        ));

        /********************************* Step 2: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, null, null, null, sortOrderObjectList, searchVo.getStart(), searchVo.getLimit(), IndexConstant.GROUP_PRODUCT_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过条件搜索组合产品失败! ElasticSearchException:{}.", e);
            throw new GroupProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索组合产品失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过条件搜索组合产品列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过条件搜索组合产品失败,查询结果为空。GroupProductSearchVo:{}.", searchVo.toString());
            throw new GroupProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索组合产品列表失败,查询结果为空。GroupProductSearchVo:" + searchVo.toString());
        }

        return searchObjectResponse;
    }


    @Override
    public SearchObjectResponse searchGroupProductByIds(List<Integer> groupIdList) throws GroupProductSearchException {

        if (Lists.isEmpty(groupIdList)) {
            log.error(CLASS_LOG_PREFIX + "搜索组合产品失败,必传参数为空！");
            throw new GroupProductSearchException("搜索组合产品失败,必传参数为空！");
        }

        /********************************* Step 1: 组装搜索条件 *************************************************/
        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //排序对象
        List<SortOrderObject> sortOrderObjectList = new ArrayList<>();
        //初始化
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //根据组合ID过滤
        boolQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_ID, groupIdList));

        matchQueryList.add(boolQueryBuilder);

        /********************************* Step 2: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, null, null, null, sortOrderObjectList, 0, groupIdList.size(), IndexConstant.GROUP_PRODUCT_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过groupIdList搜索组合产品失败! ElasticSearchException:{}.", e);
            throw new GroupProductSearchException(CLASS_LOG_PREFIX + "通过groupIdList搜索组合产品失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过groupIdList搜索组合产品列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过groupIdList搜索组合产品失败,查询结果为空。groupIdList:{}.", groupIdList);
            throw new GroupProductSearchException(CLASS_LOG_PREFIX + "通过groupIdList搜索组合产品列表失败,查询结果为空。groupIdList:" + groupIdList);
        }

        return searchObjectResponse;

    }

    /**
     * 一值多字段匹配
     * @param multiMatchFieldList
     * @param boolQueryBuilder
     */
    private void getMultiMatchQueryBuilder(List<MultiMatchField> multiMatchFieldList, BoolQueryBuilder boolQueryBuilder) {
        if (null != multiMatchFieldList && 0 < multiMatchFieldList.size()) {
            //遍历条件
            multiMatchFieldList.forEach(multiMatchField -> {
                //匹配字段
                List<String> matchFieldList = multiMatchField.getMatchFieldList();

                //设置查询条件
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(multiMatchField.getSearchKeyword(), matchFieldList.toArray(new String[matchFieldList.size()]));
                //设置字段优先级
                for (int i = 0; i < matchFieldList.size(); i++) {
                    //字段名
                    String filedName = matchFieldList.get(i);
                    //权重
                    float boost = (matchFieldList.size() - i);
                    //组合名称由于是强匹配,防止其他分值大于方案名称.故上调为5倍权重
                    if (QueryConditionField.QUERY_CONDITION_FIELD_GROUP_NAME.equals(filedName)) {
                        boost = boost * 10;
                    }
                    multiMatchQueryBuilder.field(filedName, boost);
                }
                //加入查询
                boolQueryBuilder.filter(multiMatchQueryBuilder);
            });
        }
    }

    @Override
    public List<GroupProductVo> conversionGroupProductVo(List<GroupProductPo> groupProductPoList, LoginUser loginUser, String platformCode, String functType) {
        if (null == groupProductPoList || groupProductPoList.size() == 0 || null == loginUser) {
            return null;
        }
        List<GroupProductVo> groupProductVoList = new ArrayList<>(groupProductPoList.size());
        Map<Integer, Integer> groupCollectMap = new HashMap<>(groupProductPoList.size());
        boolean collectFlag = true;

        if (!Objects.equals(GroupProductConstant.FUNCT_TYPE_COLLECT_GROUP_LIST, functType)) {
            //获取ALL组合收藏状态
            List<Integer> groupIds = new ArrayList<>(groupProductPoList.size());
            groupProductPoList.stream().forEach(groupProductPo -> groupIds.add(groupProductPo.getId()));
            if (null != groupIds && groupIds.size() > 0) {
                List<GroupProductCollectPo> groupCollectPoList = groupProductIndexDao.findGroupCollectInfo(loginUser.getId(), groupIds);
                if (null != groupCollectPoList && groupCollectPoList.size() > 0) {
                    groupCollectPoList.stream().forEach(collectPo -> groupCollectMap.put(collectPo.getGroupId(), collectPo.getCollectState()));
                }
            }
            if (null == groupCollectMap || groupCollectMap.size() == 0) {
                collectFlag = false;
            }
        }

        //获取企业信息
        CompanyPo companyPo = companyBrandService.getCompanyInfo(platformCode, loginUser);
        //获取企业品牌信息
        List<Integer> brandIdList = companyBrandService.getBrandIdList(companyPo, platformCode);

        GroupProductVo groupProductVo = null;
        for (GroupProductPo groupProductPo : groupProductPoList) {
            groupProductVo = new GroupProductVo();
            EntityCopyUtils.copyData(groupProductPo, groupProductVo);
            Integer groupId = groupProductPo.getId();
            groupProductVo.setGroupId(groupId);
            //组合收藏状态获取
            if (!Objects.equals(GroupProductConstant.FUNCT_TYPE_COLLECT_GROUP_LIST, functType)) {
                if (collectFlag && groupCollectMap.containsKey(groupId) && GroupProductConstant.GROUP_COLLECT_NOT_STATE < groupCollectMap.get(groupId)) {
                    groupProductVo.setCollectState(GroupProductConstant.GROUP_COLLECT_HAS_STATE);
                } else {
                    groupProductVo.setCollectState(GroupProductConstant.GROUP_COLLECT_NOT_STATE);
                }
            } else {
                groupProductVo.setCollectState(GroupProductConstant.GROUP_COLLECT_HAS_STATE);
            }
            //非本品牌公司不让看到组合价格
            if (null != brandIdList && !brandIdList.contains(groupProductPo.getBrandId())) {
                groupProductVo.setGroupPrice(-1.0);
            } else {
                if (null == groupProductPo.getGroupPrice() || groupProductPo.getGroupPrice() == 0) {
                    BigDecimal groupPrice = groupProductIndexDao.getGroupPrice(groupId, platformCode);
                    groupProductVo.setGroupPrice(groupPrice == null ? 0 : groupPrice.doubleValue());
                }
            }
            groupProductVoList.add(groupProductVo);
        }

        return groupProductVoList;
    }


    @Override
    public int getGroupCollectCount(GroupProductSearchVo groupProductSearchVo) {
        return groupProductIndexDao.getGroupCollectCount(groupProductSearchVo);
    }

    @Override
    public List<Integer> getGroupCollectList(GroupProductSearchVo groupProductSearchVo) {
        return groupProductIndexDao.getGroupCollectList(groupProductSearchVo);
    }

    /**
     * 处理搜索全部组合数据 需要过滤可见分类品牌
     * bool(companyAliveBool)
     *    should
     *          bool(nowbool)
     *              terms(productType)
     *              terms(productSmallType)
     *              terms(brand)
     *          bool(nowbool)
     *              terms(productType)
     *              terms(productSmallType)
     *
     */
    private void disposeFullSearchGroup(Map<Integer, List<Integer>> companyAliveTypeMap, List<Integer> brandIdList, BoolQueryBuilder boolQueryBuilder) {
        // 查询所有组合过滤可见分类产品组合

        /**************** 分离成2部分数据(1.被品牌过滤的大小类,2.不被品牌过滤的大小类) ***************/
        Map<Integer, List<Integer>> normalTypeMap = new HashMap<>();

        //2.不被品牌过滤的大小类(遍历数据，假定大小类值范围为60)
        for (int i = 1; i <= 60; i++) {
            //正常产品小类列表(初始化60个小类)
            List<Integer> normalProductSmallList = new ArrayList<>(60);
            for (int j = 1; j <= 60; j++) {
                normalProductSmallList.add(j);
            }

            //去掉小类列表中要被过滤的
            if (companyAliveTypeMap.containsKey(i)) {
                //要做过滤的大类
                List<Integer> companyProductSmallValueList = companyAliveTypeMap.get(i);
                //去重
                normalProductSmallList = normalProductSmallList.stream().filter(item -> !companyProductSmallValueList.contains(item)).collect(toList());
            }

            normalTypeMap.put(i, normalProductSmallList);
        }

        //公司有效分类bool
        BoolQueryBuilder companyAliveBool = QueryBuilders.boolQuery();
        /**************** 第一部分:要过滤品牌的 *******************/
        for (Map.Entry<Integer, List<Integer>> entries : companyAliveTypeMap.entrySet()) {
            //当前bool
            BoolQueryBuilder nowBool = QueryBuilders.boolQuery();

            //产品大类
            Integer nowProductTypeValue = entries.getKey();
            nowBool.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, nowProductTypeValue));

            //产品小类
            List<Integer> nowProductSmallTypeValueList = entries.getValue();
            nowBool.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_PRODUCT_SMALL_TYPE_VALUE, nowProductSmallTypeValueList));
            //品牌
            if (null != brandIdList && 0 < brandIdList.size()) {
                nowBool.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_BRAND_ID, brandIdList));
            }

            //合并条件
            companyAliveBool.should(nowBool);
        }

        /**************** 第二部分:不过滤的 *******************/
        for (Map.Entry<Integer, List<Integer>> entries : normalTypeMap.entrySet()) {
            //当前bool
            BoolQueryBuilder nowBool = QueryBuilders.boolQuery();

            //产品大类
            Integer nowProductTypeValue = entries.getKey();
            nowBool.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, nowProductTypeValue));

            //产品小类
            List<Integer> nowProductSmallTypeValueList = entries.getValue();
            nowBool.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_GROUP_PRODUCT_SMALL_TYPE_VALUE, nowProductSmallTypeValueList));

            //合并条件
            companyAliveBool.should(nowBool);
        }

        boolQueryBuilder.filter(companyAliveBool);
    }

}
