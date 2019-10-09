package com.sandu.search.service.product.impl;

import com.sandu.common.util.collections.Lists;
import com.sandu.search.common.constant.*;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import com.sandu.search.entity.elasticsearch.dco.ValueOffset;
import com.sandu.search.entity.elasticsearch.dco.ValueRange;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.ShouldMatchSearch;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchAggregationVo;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchMatchVo;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.ProductSearchException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.product.ProductSearchService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.system.SysUserMetaDataStorage;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 产品搜索服务
 *
 * @date 20180103
 * @auth pengxuangang
 */
@Slf4j
@EnableAutoConfiguration
@Service("productSearchService")
public class ProductSearchServiceImpl implements ProductSearchService {

    private final static String CLASS_LOG_PREFIX = "产品搜索服务:";

    /*@Value("${productSearchOrder.beforeLength}")
    private Map<String, String> productTypeMap = new HashMap<>();*/

    private final ElasticSearchService elasticSearchService;
    private final SysUserMetaDataStorage sysUserMetaDataStorage;
    private final MetaDataService metaDataService;
    private final RedisService redisService;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;

    @Autowired
    public ProductSearchServiceImpl(ElasticSearchService elasticSearchService, SysUserMetaDataStorage sysUserMetaDataStorage, MetaDataService metaDataService, RedisService redisService, SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage) {
        this.elasticSearchService = elasticSearchService;
        this.sysUserMetaDataStorage = sysUserMetaDataStorage;
        this.metaDataService = metaDataService;
        this.redisService = redisService;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
    }

    @Override
    public SearchObjectResponse searchProduct(
    		ProductSearchMatchVo productSearchMatchVo, 
    		List<ProductSearchAggregationVo> productSearchAggregationVoList, 
    		PageVo pageVo
    		) throws ProductSearchException {

        if (null == productSearchMatchVo || null == pageVo) {
            log.warn(CLASS_LOG_PREFIX + "通过条件搜索产品失败，必需参数为空.");
            throw new ProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索产品失败，必需参数为空.");
        }

        /********************************* Step 1: 组装搜索条件 *************************************************/

        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //非匹配条件List
        List<QueryBuilder> noMatchQueryList = new ArrayList<>(1);
        //或匹配条件List
        List<ShouldMatchSearch> shouldMatchSearchList = new ArrayList<>();
        //聚合条件
        List<AggregationBuilder> aggregationBuilderList = new ArrayList<>(null == productSearchAggregationVoList ? 0 : productSearchAggregationVoList.size());

        //组合查询条件--产品名
        if (!StringUtils.isEmpty(productSearchMatchVo.getProductName())) {
            matchQueryList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_NAME, productSearchMatchVo.getProductName()));
        }
        //组合查询条件--产品大类
        Integer productTypeValue = productSearchMatchVo.getProductTypeValue();
        //组合业务产品小类筛选(只有同城联盟和小程序产品替换搜索用) update by xiaoxc-20190227注释(由于增加了不同大类互换搜索)
        if(null != productSearchMatchVo.getBizProductTypeSmallValue() && 0!= productSearchMatchVo.getBizProductTypeSmallValue()){
            matchQueryList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, productSearchMatchVo.getBizProductTypeSmallValue()));
        }

        //组合查询条件--天花随机拼花标识
        if (ProductPo.HAVE_CEILING_RANDOM_PATCH_FLOWER == productSearchMatchVo.getCeilingRandomPatchFlowerFlag()) {
            matchQueryList.add(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CEILING_PATCHFLOWER_FLAG, ProductPo.HAVE_CEILING_RANDOM_PATCH_FLOWER));
        }

        //组合查询条件--白模全铺长度
        RangeQueryBuilder fullPaveLengthRangeQueryBuilder = null;
        ValueOffset valueOffset = productSearchMatchVo.getFullPaveLengthValueOffset();
        
        // add by huangsongbo 2018.9.15 应对背景墙缩放需求(缩放之后,要按缩放后的长度来搜索背景墙) ->start
        if(productSearchMatchVo.isBeijing()&& null !=valueOffset) {
        	if(productSearchMatchVo.getFullPaveLength() != null) {
            	valueOffset.setFullPaveLength(productSearchMatchVo.getFullPaveLength());
            } else if(productSearchMatchVo.getProductLength() != null) {
            	valueOffset.setFullPaveLength(productSearchMatchVo.getProductLength());
            }
        }
        // add by huangsongbo 2018.9.15 应对背景墙缩放需求(缩放之后,要按缩放后的长度来搜索背景墙) ->start
        
        if (null != valueOffset) {
            //长度左界限
            int leftOffset = 0;
            //长度右界限
            int rightOffset = 0;
            //白膜全铺长度值
            int fullPaveLength = valueOffset.getFullPaveLength();
            //偏移量
            int fullPaveOffsetValue = valueOffset.getFullPaveOffsetValue();
            switch (valueOffset.getFullPaveOffsetType()) {
                //正常偏移(左右偏移)
                case ValueOffset.NORMAL_FULL_PAVE_OFFSET_TYPE:
                    leftOffset = fullPaveLength - fullPaveOffsetValue;
                    rightOffset = fullPaveLength + fullPaveOffsetValue;
                    break;
                //左偏移
                case ValueOffset.LEFT_FULL_PAVE_OFFSET_TYPE:
                    leftOffset = fullPaveLength - fullPaveOffsetValue;
                    rightOffset = fullPaveLength;
                    break;
                //右偏移
                case ValueOffset.RIGHT_FULL_PAVE_OFFSET_TYPE:
                    rightOffset = fullPaveLength + fullPaveOffsetValue;
                    leftOffset = fullPaveLength;
                    break;
                //不偏移(指代一个具体值,如:5<=x<=5)
                case ValueOffset.NO_FULL_PAVE_OFFSET_TYPE:
                    rightOffset = fullPaveLength;
                    leftOffset = fullPaveLength;
                    break;
            }

            //组装条件
            fullPaveLengthRangeQueryBuilder = QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH);
            //值匹配类型
            switch (valueOffset.getValueEqualType()) {
                case ValueOffset.ONLY_INCLUDE_LOWER :
                    //仅包含小值
                    fullPaveLengthRangeQueryBuilder.gte(leftOffset);
                    fullPaveLengthRangeQueryBuilder.lt(rightOffset);
                    break;
                case ValueOffset.ONLY_INCLUDE_UPPER :
                    //仅包含大值
                    fullPaveLengthRangeQueryBuilder.gt(leftOffset);
                    fullPaveLengthRangeQueryBuilder.lte(rightOffset);
                    break;
                case ValueOffset.INCLUDE_ALL :
                    //包含所有值(默认)
                    fullPaveLengthRangeQueryBuilder.gte(leftOffset);
                    fullPaveLengthRangeQueryBuilder.lte(rightOffset);
                    break;
                case ValueOffset.NOT_INCLUDE :
                    //都不包含
                    fullPaveLengthRangeQueryBuilder.gt(leftOffset);
                    fullPaveLengthRangeQueryBuilder.lt(rightOffset);
                    break;
                default:
                    //默认包含所有值
                    fullPaveLengthRangeQueryBuilder.gte(leftOffset);
                    fullPaveLengthRangeQueryBuilder.lte(rightOffset);
                    log.info(CLASS_LOG_PREFIX + "值匹配类型默认包含所有值.leftOffset:{}, rightOffset:{}.", leftOffset, rightOffset);
            }

        }
        //组合查询条件--产品当前小类优先
        Integer nowProductTypeSmallValue = productSearchMatchVo.getNowProductTypeSmallValue();

        //组合查询条件--公司产品可见范围小类ID列表和产品小类
        /**************************** 公司产品可见范围小类ID列表和产品小类 start ***************************/
        //结构图
        /**
         * bool(productSmallTypeBoolQueryBuilder)
         *   |
         *   |---should
         *   |      |
         *   |      |---bool(companyBoolQueryBuilder)
         *   |             |---should
         *   |             |      |
         *   |             |      |---bool(fullpaveBoolQueryBuilder)
         *   |             |            |
         *   |             |            |---terms(brand)
         *   |             |            |---terms(product small type)
         *   |             |            |---terms(full pave length)
         *   |             |            |---isAuth|terms(platform code)
         *   |             |            |---isAuth|terms(platform putaway status)
         *   |             |
         *   |             |---should
         *   |                    |
         *   |                    |---bool(brandBoolQueryBuilder)
         *   |                         |
         *   |                         |---terms(brand)
         *   |                         |---terms(product small type)
         *   |                         |---isAuth|terms(platform code)
         *   |                         |---isAuth|terms(platform putaway status)
         *   |---should
         *          |
         *          |---bool(normalBoolQueryBuilder)
         *                 |---should
         *                 |      |
         *                 |      |---bool(fullpaveBoolQueryBuilder)
         *                 |           |---terms(full pave length)
         *                 |           |---terms(product small type)
         *                 |           |---term(secrecy flag)
         *                 |           |---term(platform code)
         *                 |           |---term(platform putaway status)
         *                 |
         *                 |---should
         *                        |
         *                        |---bool(product small type)
         *                             |---terms(product small type)
         *                             |---term(secrecy flag)
         *                             |---term(platform code)
         *                             |---term(platform putaway status)
         */

        //小类
        List<Integer> productSmallTypeValueList = productSearchMatchVo.getProductTypeSmallValueList();
        //产品小类bool查询
        BoolQueryBuilder productSmallTypeBoolQueryBuilder = QueryBuilders.boolQuery();

        /********************** 1.公司可见产品小类品牌过滤 **********************/
        //产品可见范围小类ID列表
        List<Integer> companyProductVisibilityRangeIdList = productSearchMatchVo.getCompanyProductVisibilityRangeIdList();
        //公司可见范围的小类(因为可见范围的小类要单独处理-经过品牌过滤)
        List<Integer> productSmallTypeVisibilityRangeList = (null == companyProductVisibilityRangeIdList || 0 >= companyProductVisibilityRangeIdList.size()) ?
                null : productSmallTypeValueList.stream().filter(companyProductVisibilityRangeIdList::contains).collect(toList());
        //除公司可见范围之外的小类(因为可见范围的小类要单独处理-经过品牌过滤)
        List<Integer> nowProductSmallTypeList = (null == productSmallTypeVisibilityRangeList || 0 >= productSmallTypeVisibilityRangeList.size()) ?
                productSmallTypeValueList : productSmallTypeValueList.stream().filter(item -> !productSmallTypeVisibilityRangeList.contains(item)).collect(toList());

        //公司可见范围bool查询
        BoolQueryBuilder companyBoolQueryBuilder = QueryBuilders.boolQuery();
        //普通bool查询
        BoolQueryBuilder normalBoolQueryBuilder = QueryBuilders.boolQuery();

        //内部用户标识
        boolean innerUser = productSearchMatchVo.isInnerUser();
        //是否有产品库权限(不需要平台过滤)
        boolean productLibraryAuth = productSearchMatchVo.isProductLibraryAuth();

        //产品平台+产品平台上架状态
        BoolQueryBuilder boolQueryBuildersOfPlatformCode = getBoolQueryBuildersOfPlatformCode(productSearchMatchVo.getPlatformCode(), innerUser, productLibraryAuth);

        //公司可见范围内小类
        //公司品牌列表
        List<Integer> companyBrandIdList = productSearchMatchVo.getCompanyBrandIdList();
        if (null != productTypeValue && null != productSmallTypeVisibilityRangeList && 0 < productSmallTypeVisibilityRangeList.size()) {
            if (null == companyBrandIdList || 0 >= companyBrandIdList.size()) {
                //将可见范围内的小类交回范围外(不处理)
                nowProductSmallTypeList.addAll(productSmallTypeVisibilityRangeList);
            } else {
                //品牌
                TermsQueryBuilder brandTermsQueryBuilder = QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_ID, companyBrandIdList);

                //品牌+小类bool查询
                BoolQueryBuilder brandBoolQueryBuilder = QueryBuilders.boolQuery();

                //定制衣柜-定制浴室柜-定制镜小类需要增加白膜全铺长度过滤，其他的不用
                //包含特殊处理白膜的小类ID列表
                List<Integer> includeProductSmallFullPraveIdList = productSmallTypeVisibilityRangeList.stream().filter(productSmallTypeValue ->
                        (ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET == productTypeValue && ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET == productSmallTypeValue)
                                || (ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM == productTypeValue && ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM == productSmallTypeValue)
                                || (ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM == productTypeValue && ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM == productSmallTypeValue)
                ).collect(Collectors.toList());
                //排除特殊处理白膜的小类ID列表(将大小类组合成:'大类-小类'的形式再来比较就可以仅对比一次出结果)
                List<Integer> excludeProductSmallFullPraveIdList = productSmallTypeVisibilityRangeList.stream().filter(productSmallTypeValue ->
                        !(ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET).equals(productTypeValue + "-" + productSmallTypeValue)
                                &&!(ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM).equals(productTypeValue + "-" + productSmallTypeValue)
                                &&!(ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM).equals(productTypeValue + "-" + productSmallTypeValue)
                ).collect(Collectors.toList());

                //包含全铺长度的小类
                if (null != fullPaveLengthRangeQueryBuilder && null != includeProductSmallFullPraveIdList && 0 < includeProductSmallFullPraveIdList.size()) {
                    //全铺长度+品牌+小类+产品平台+产品平台上架状态bool查询
                    BoolQueryBuilder fullpaveBoolQueryBuilder = QueryBuilders.boolQuery();
                    //品牌
                    fullpaveBoolQueryBuilder.must(brandTermsQueryBuilder);
                    //大小类
                    fullpaveBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue));
                    fullpaveBoolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, includeProductSmallFullPraveIdList));
                    //全铺长度
                    
                    // update by huangsongbo 2018.8.7 ->start
                    // 长度过滤条件,不适用于多模型产品
                    /*fullpaveBoolQueryBuilder.must(fullPaveLengthRangeQueryBuilder);*/
                    this.dealWithfullPaveLengthRangeQueryBuilder(fullpaveBoolQueryBuilder, fullPaveLengthRangeQueryBuilder);
                    // update by huangsongbo 2018.8.7 ->end
                    
                    //产品平台+产品平台上架状态
                    /*if (null != boolQueryBuildersOfPlatformCode) {
                        fullpaveBoolQueryBuilder.must(boolQueryBuildersOfPlatformCode);
                    }*/

                    //加入公司bool查询条件
                    if (fullpaveBoolQueryBuilder.must().size() > 0) {
                        companyBoolQueryBuilder.should(fullpaveBoolQueryBuilder);
                    }
                }

                //不包含全铺长度的小类
                if (null != excludeProductSmallFullPraveIdList && 0 < excludeProductSmallFullPraveIdList.size()) {
                    //品牌
                    brandBoolQueryBuilder.must(brandTermsQueryBuilder);
                    //大小类
                    brandBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue));
                    brandBoolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, excludeProductSmallFullPraveIdList));
                    //产品平台+产品平台上架状态
                    /*if (null != boolQueryBuildersOfPlatformCode) {
                        brandBoolQueryBuilder.must(boolQueryBuildersOfPlatformCode);
                    }*/

                    //加入公司bool查询条件
                    if (brandBoolQueryBuilder.must().size() > 0) {
                        companyBoolQueryBuilder.should(brandBoolQueryBuilder);
                    }
                }
            }
        }
        //合并所有小类
        List<Integer> allProductSmallType = new ArrayList<>(16);
        //公司可见范围外小类
        if (null != nowProductSmallTypeList && 0 < nowProductSmallTypeList.size()) {
            //定制衣柜-定制浴室柜-定制镜小类需要增加白膜全铺长度过滤，其他的不用
            //包含特殊处理白膜的小类ID列表
            List<Integer> includeProductSmallFullPraveIdList = nowProductSmallTypeList.stream().filter(productSmallTypeValue ->
                    (ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET == productTypeValue && ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET == productSmallTypeValue)
                            || (ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM == productTypeValue && ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM == productSmallTypeValue)
                            || (ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM == productTypeValue && ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM == productSmallTypeValue)
            ).collect(Collectors.toList());
            //排除特殊处理白膜的小类ID列表(将大小类组合成:'大类-小类'的形式再来比较就可以仅对比一次出结果)
            List<Integer> excludeProductSmallFullPraveIdList = nowProductSmallTypeList.stream().filter(productSmallTypeValue ->
                    !(ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET).equals(productTypeValue + "-" + productSmallTypeValue)
                            &&!(ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM).equals(productTypeValue + "-" + productSmallTypeValue)
                            &&!(ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM).equals(productTypeValue + "-" + productSmallTypeValue)
            ).collect(Collectors.toList());


            //有白膜条件需要分开处理
            if (null != fullPaveLengthRangeQueryBuilder) {
                //有需要特殊处理的小类
                if (null != includeProductSmallFullPraveIdList && 0 < includeProductSmallFullPraveIdList.size()) {
                    //全铺长度+小类查询+未保密+(产品平台+产品平台上架状态)bool
                    BoolQueryBuilder fullpaveBoolQueryBuilder = QueryBuilders.boolQuery();
                    //大小类
                    fullpaveBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue));
                    fullpaveBoolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, includeProductSmallFullPraveIdList));
                    //全铺长度

                    // update by huangsongbo 2018.8.7 ->start
                    // 长度过滤条件,不适用于多模型产品
	                 /*fullpaveBoolQueryBuilder.must(fullPaveLengthRangeQueryBuilder);*/
                    this.dealWithfullPaveLengthRangeQueryBuilder(fullpaveBoolQueryBuilder, fullPaveLengthRangeQueryBuilder);
                    // update by huangsongbo 2018.8.7 ->end

                    //本公司可见公开未公开产品 + 非本公司公开产品
                    if (productSearchMatchVo.isSecrecyFlag()) {
                        fullpaveBoolQueryBuilder.must(getOpenProductConditions(productSearchMatchVo));
                    }

                    //加入普通bool查询条件
                    normalBoolQueryBuilder.should(fullpaveBoolQueryBuilder);

                    //bool
                    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                    //大小类
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue));
                    boolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, excludeProductSmallFullPraveIdList));


                    //加入普通bool查询条件
                    normalBoolQueryBuilder.should(boolQueryBuilder);
                } else {
                    //没有需要特殊处理的小类
                    //全铺长度+小类查询+未保密+(产品平台+产品平台上架状态)bool
                    BoolQueryBuilder fullpaveBoolQueryBuilder = QueryBuilders.boolQuery();
                    //大小类
                    fullpaveBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue));
                    fullpaveBoolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, excludeProductSmallFullPraveIdList));
                    //全铺长度

                    // update by huangsongbo 2018.8.7 ->start
                    // 长度过滤条件,不适用于多模型产品
	                 /*fullpaveBoolQueryBuilder.must(fullPaveLengthRangeQueryBuilder);*/
                    this.dealWithfullPaveLengthRangeQueryBuilder(fullpaveBoolQueryBuilder, fullPaveLengthRangeQueryBuilder);
                    // update by huangsongbo 2018.8.7 ->end

                    //本公司可见公开未公开产品 + 非本公司公开产品
                    if (productSearchMatchVo.isSecrecyFlag()) {
                        fullpaveBoolQueryBuilder.must(getOpenProductConditions(productSearchMatchVo));
                    }


                    //加入普通bool查询条件
                    normalBoolQueryBuilder.should(fullpaveBoolQueryBuilder);
                }
            } else {
                //没有白膜条件直接全部一起处理
                allProductSmallType.addAll(excludeProductSmallFullPraveIdList);
                allProductSmallType.addAll(includeProductSmallFullPraveIdList);

                //bool
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

                //本公司可见公开未公开产品 + 非本公司公开产品
                if (productSearchMatchVo.isSecrecyFlag()) {
                    boolQueryBuilder.must(getOpenProductConditions(productSearchMatchVo));
                }


                //地面和墙面可以与瓷砖、大理石、木地板互换
                if (null != productTypeValue) {
                    if (ProductTypeValue.PRODUCT_TYPE_VALUE_GROUND == productTypeValue
                            || ProductTypeValue.PRODUCT_TYPE_VALUE_WALL == productTypeValue
                            && (ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SPACE_WALL == nowProductTypeSmallValue
                            || ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_BRICK_WALL == nowProductTypeSmallValue
                            || ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_STONE_SPACE_WALL == nowProductTypeSmallValue
                            || ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_COATING_WALL == nowProductTypeSmallValue
                            || ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_WALLBOARD_WALL == nowProductTypeSmallValue
                            || ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_ORIGIN_SPACE_WALL == nowProductTypeSmallValue)) {
                        BoolQueryBuilder productTypeShouldBool = QueryBuilders.boolQuery();
                        //当前产品(白膜)小类bool
                        BoolQueryBuilder currentQueryBuilder = QueryBuilders.boolQuery();
                        currentQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue));
                        //小类
                        if (null != allProductSmallType && allProductSmallType.size() > 0) {
                            currentQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, allProductSmallType));
                        }
                        productTypeShouldBool.should(currentQueryBuilder);

                        //查询瓷砖、大理石、木地板分类产品
                        BoolQueryBuilder otherQueryBuilder = QueryBuilders.boolQuery();
                        List<Integer> otherProductTypeList = Arrays.asList(ProductTypeValue.PRODUCT_TYPE_VALUE_CZ, ProductTypeValue.PRODUCT_TYPE_VALUE_DALS, ProductTypeValue.PRODUCT_TYPE_VALUE_DB);
                        otherQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, otherProductTypeList));
                        productTypeShouldBool.should(otherQueryBuilder);

                        boolQueryBuilder.must(productTypeShouldBool);
                        normalBoolQueryBuilder.should(boolQueryBuilder);
                    } else {
                        if (!StringUtils.isEmpty(productTypeValue)) {
                            QueryBuilder matchProductTypeValueQueryBuilder = QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, productTypeValue);
                            boolQueryBuilder.must(matchProductTypeValueQueryBuilder);
                        }
                        //小类
                         if (null != allProductSmallType && allProductSmallType.size() > 0) {
                            boolQueryBuilder.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, allProductSmallType));
                        }
                        //加入普通bool查询条件
                        if (boolQueryBuilder.must().size() > 0) {
                            normalBoolQueryBuilder.should(boolQueryBuilder);
                        }
                    }
                }
            }
        }

        //产品小类条件聚合
        if (companyBoolQueryBuilder.should().size() > 0) {
            productSmallTypeBoolQueryBuilder.should(companyBoolQueryBuilder);
        }
        if (normalBoolQueryBuilder.should().size() > 0) {
            productSmallTypeBoolQueryBuilder.should(normalBoolQueryBuilder);
        }

        //搜全部时公司用户可见分类过滤
        Map<Integer, List<Integer>> companyAliveTypeMap = productSearchMatchVo.getCompanyAliveTypeMap();

        //产品分类条件聚合
        if (productSmallTypeBoolQueryBuilder.should().size() > 0) {
            matchQueryList.add(productSmallTypeBoolQueryBuilder);
        } else if (null == companyAliveTypeMap || companyAliveTypeMap.size() == 0) {
            //本公司可见公开未公开产品 + 非本公司公开产品
            if (productSearchMatchVo.isSecrecyFlag()) {
                matchQueryList.add(getOpenProductConditions(productSearchMatchVo));
            }
        }

        /**************************** 公司产品可见范围小类ID列表和产品小类 end ***************************/

        //没有大小类，没有编码，但有厂商品牌过滤则进行大小类排除过滤(对应U3D：搜全部按钮---此操作性能消耗稍大)
        if ((null == productTypeValue || 0 == productTypeValue)
                && (null == productSmallTypeValueList || 0 >= productSmallTypeValueList.size())
                && (null != companyAliveTypeMap && 0 < companyAliveTypeMap.size())) {
            /**
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
                nowBool.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, nowProductSmallTypeValueList));
                //品牌
                if (null != companyBrandIdList && 0 < companyBrandIdList.size()) {
                    nowBool.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_ID, companyBrandIdList));
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
                nowBool.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, nowProductSmallTypeValueList));
                //本公司可见公开未公开产品 + 非本公司公开产品
                if (productSearchMatchVo.isSecrecyFlag()) {
                    companyAliveBool.must(getOpenProductConditions(productSearchMatchVo));
                }
                //合并条件
                companyAliveBool.should(nowBool);
            }

            //合并数据
            matchQueryList.add(companyAliveBool);
        }


        //组合查询条件--产品分类ID列表
        List<Integer> productCategoryIdList = productSearchMatchVo.getProductCategoryIdList();
        if (null != productCategoryIdList && 0 < productCategoryIdList.size()) {
            TermsQueryBuilder termsProductCategoryCodeQueryBuilder = QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ALLCATEGORYID, productCategoryIdList);
            matchQueryList.add(termsProductCategoryCodeQueryBuilder);
        }

        //组合查询条件--二级分类ID
        int secondLevelCategoryId = productSearchMatchVo.getSecondLevelCategoryId();
        if (0 != secondLevelCategoryId) {
            QueryBuilder matchProductCategoryQueryBuilder = QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SECONDLEVEL_CATEGORID, secondLevelCategoryId);
            matchQueryList.add(matchProductCategoryQueryBuilder);
        }

        //组合查询条件--三级分类ID
        int thirdLevelCategoryId = productSearchMatchVo.getThirdLevelCategoryId();
        if (0 != thirdLevelCategoryId) {
            QueryBuilder matchProductCategoryQueryBuilder = QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_THIRDLEVEL_CATEGORYID, thirdLevelCategoryId);
            matchQueryList.add(matchProductCategoryQueryBuilder);
        }

        //组合查询条件--产品品牌ID列表
        List<Integer> brandIdList = productSearchMatchVo.getBrandIdList();
        if (null != brandIdList && 0 < brandIdList.size()) {
            matchQueryList.add(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_ID, brandIdList));
        }

        //组合查询条件--产品发布状态
        matchQueryList.add(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PUTAWAY_STATUS, productSearchMatchVo.getPutawayStatusList()));
        //(产品平台+产品平台上架状态)
        if (null != boolQueryBuildersOfPlatformCode) {
            matchQueryList.add(boolQueryBuildersOfPlatformCode);
        }

        //组合查询条件--产品分类长编码
        List<String> productCategoryLongCodeList = productSearchMatchVo.getProductCategoryLongCodeList();
        if (null != productCategoryLongCodeList && 0 < productCategoryLongCodeList.size()) {
            //是否是五级分类查询
            boolean isFiveLevelCategoryList = true;
            for (String productCategoryLongCode : productCategoryLongCodeList) {
                //长编码列表中均为五级分类才是五级分类查询
                //五级分类长编码中点会出现7次
                if (7 != StringUtil.appearNumber(productCategoryLongCode, ".")) {
                    isFiveLevelCategoryList = false;
                    break;
                }
            }

            //五级分类中所有条件为MUST
            if (isFiveLevelCategoryList) {
                //组装bool查询(结构图)
                /**
                 * bool
                 *   |
                 *   |---must---terms(productCategoryLongCode)
                 *   |
                 *   |---must---terms(productCategoryLongCode)
                 *   .
                 */
                BoolQueryBuilder categoryBoolQueryBuilder = QueryBuilders.boolQuery();
                productCategoryLongCodeList.forEach(productCategoryLongCode -> categoryBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CATEGORY_LONG_CODE_LIST, productCategoryLongCode)));
                matchQueryList.add(categoryBoolQueryBuilder);
            } else {
                //其余查询条件为SHOULD
                TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CATEGORY_LONG_CODE_LIST, productCategoryLongCodeList);
                matchQueryList.add(termsQueryBuilder);
            }
        }

        //组合查询条件--单值匹配多字段--List中第一个字段优先级最高
        List<MultiMatchField> multiMatchFieldList = productSearchMatchVo.getMultiMatchFieldList();
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
                    //产品编码由于是强匹配,防止其他分值大于产品编码.故上调为5倍权重
                    if (QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CODE.equals(filedName)) {
                        boost = boost * 10;
                    }
                    multiMatchQueryBuilder.field(filedName, boost);
                }
                multiMatchQueryBuilder.type(Type.MOST_FIELDS);
                //加入查询
                matchQueryList.add(multiMatchQueryBuilder);
            });
        }

        //组合查询条件--高度
        ValueRange productHeightValueRange = productSearchMatchVo.getProductHeightValueRange();
        if (null != productHeightValueRange) {
            //范围查询条件
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT);
            //范围类型
            switch (productHeightValueRange.getRangeType()) {
                //前匹配
                case ValueRange.RANGE_TYPE_ONLY_START_EQUAL:
                    rangeQueryBuilder.gte(productHeightValueRange.getStartValue());
                    rangeQueryBuilder.lt(productHeightValueRange.getEndValue());
                    break;
                //后匹配
                case ValueRange.RANGE_TYPE_ONLY_END_EQUAL:
                    rangeQueryBuilder.gt(productHeightValueRange.getStartValue());
                    rangeQueryBuilder.lte(productHeightValueRange.getEndValue());
                    break;
                //都不匹配
                case ValueRange.RANGE_TYPE_START_AND_END_NO_EQUAL:
                    rangeQueryBuilder.gt(productHeightValueRange.getStartValue());
                    rangeQueryBuilder.lt(productHeightValueRange.getEndValue());
                    break;
                //都匹配
                case ValueRange.RANGE_TYPE_ALL_EQUAL:
                    rangeQueryBuilder.gte(productHeightValueRange.getStartValue());
                    rangeQueryBuilder.lte(productHeightValueRange.getEndValue());
                    break;
                default:
                    log.warn(CLASS_LOG_PREFIX + "组合查询条件--高度---不支持的范围类型:{}.", productHeightValueRange.getRangeType());
            }

            //多模型产品不进行高度过滤
            BoolQueryBuilder productHeightBoolQueryBuilder = new BoolQueryBuilder();
            productHeightBoolQueryBuilder.should(rangeQueryBuilder);
            productHeightBoolQueryBuilder.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PRODUCT_BATCH_TYPE, 2));

            matchQueryList.add(productHeightBoolQueryBuilder);
        }

        //组合查询条件--产品属性
        Map<String, String> productFilterAttributeMap = productSearchMatchVo.getProductFilterAttributeMap();
        //天花区域属性
        int templateZoneStatus = productSearchMatchVo.getTemplateZoneStatus();
        if (ProductSearchMatchVo.NO_TEMPLATE_ZONE != templateZoneStatus) {
            productFilterAttributeMap = null == productFilterAttributeMap ? new HashMap<>() : productFilterAttributeMap;
            //沿墙面生成天花
            if (ProductSearchMatchVo.ONLY_WALL_FACE_ZONE == templateZoneStatus) {
                productFilterAttributeMap.put(ProductAttribute.CEILING_ZONE_TYPE_NAME, ProductAttribute.WALL_FACE_CEILING_TYPE_VALUE);
            } else if (ProductSearchMatchVo.ALL_TEMPLATE_ZONE == templateZoneStatus) {
                //沿区域生成天花
                productFilterAttributeMap.put(ProductAttribute.CEILING_ZONE_TYPE_NAME, ProductAttribute.ZONE_CEILING_TYPE_VALUE);
            }
        }
        if (null != productFilterAttributeMap && 0 < productFilterAttributeMap.size()) {
            for (Map.Entry<String, String> entries : productFilterAttributeMap.entrySet()) {
                matchQueryList.add(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ATTRIBUTE_TYPE_FILTER_MAP + "." + entries.getKey(), entries.getValue()));
            }
        }

        //组合查询条件--产品属性--当前前一个月的产品使用数--产品使用数排序
        /*Integer userId = productSearchMatchVo.getUserId();
        if (null != userId && 0 < userId) {
            //自定义计算分值
            FieldValueFactorFunctionBuilder fieldValueFactorFunctionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_USAGE_COUNT + "." + userId);
            fieldValueFactorFunctionBuilder.modifier(FieldValueFactorFunction.Modifier.LOG1P);
            fieldValueFactorFunctionBuilder.factor(1.1f);
            fieldValueFactorFunctionBuilder.missing(0);
            //分值计算查询
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(fieldValueFactorFunctionBuilder);
            //加入查询
            shouldMatchSearchList.add(new ShouldMatchSearch(functionScoreQueryBuilder,0));
        }

        //组合查询条件--产品属性--排序
        Map<String, String> productOrderAttributeMap = productSearchMatchVo.getProductOrderAttributeMap();
        if (null != productOrderAttributeMap && 0 < productOrderAttributeMap.size()) {
            for (Map.Entry<String, String> entries : productOrderAttributeMap.entrySet()) {
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ATTRIBUTE_TYPE_ORDER_MAP + "." + entries.getKey(), entries.getValue());
                //最小匹配数为0-表示不强制匹配
                shouldMatchSearchList.add(new ShouldMatchSearch(termQueryBuilder, 0));
            }
        }

        //组合查询条件--产品当前大小类优先
        Integer nowProductTypeValue = productSearchMatchVo.getNowProductTypeValue();
        if (null != nowProductTypeValue && 0 < nowProductTypeValue) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE, nowProductTypeValue);
            //增加权重
            termQueryBuilder.boost(3.0f);
            shouldMatchSearchList.add(new ShouldMatchSearch(termQueryBuilder, 0));
        }

        //组合查询条件--产品当前小类优先
        if (null != nowProductTypeSmallValue && 0 < nowProductTypeSmallValue) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE, nowProductTypeSmallValue);
            //增加权重
            termQueryBuilder.boost(3.0f);
            shouldMatchSearchList.add(new ShouldMatchSearch(termQueryBuilder, 0));
        }

        //组合查询条件--产品品牌优先级最高
        Integer productBrandId = productSearchMatchVo.getProductBrandId();
        if(null != productBrandId && 0<productBrandId){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_ID, productBrandId);
            //增加权重
            termQueryBuilder.boost(4.0f);
            shouldMatchSearchList.add(new ShouldMatchSearch(termQueryBuilder, 0));
        }*/

        //排除查询条件--是否包含白膜产品
        if (!productSearchMatchVo.isIncludeWhiteMembraneProduct()) {
            noMatchQueryList.add(QueryBuilders.prefixQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CODE, "baimo_"));
        }

        //排序字段
        List<SortOrderObject> sortOrderObjectList = productSearchMatchVo.getSortOrderObjectList();
        if(sortOrderObjectList == null) {
        	sortOrderObjectList = new ArrayList<SortOrderObject>();
        }
        //组合查询条件:产品品牌优先级最高-->产品属性-->当前前一个月的产品使用数-->产品使用数排序-->产品发布时间
        this.getSearchProductSortObj(sortOrderObjectList, productSearchMatchVo);

        //产品必须未被删除
        matchQueryList.add(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_IS_DELETE, "0"));

        //一石多面过滤
        Integer isComplexParquet = productSearchMatchVo.getIsComplexParquet();
        if (null != isComplexParquet) {
            matchQueryList.add(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CEILING_PATCHFLOWER_FLAG, isComplexParquet));
        }
        // add by zhangwj 不可替换产品过滤（无模型并且无贴图，只要有其一即表示可以替换）
        if (ProductTypeValue.PRODUCT_TYPE_VALUE_CEILING != (productTypeValue==null?-1:productTypeValue) ) {
            BoolQueryBuilder unReplaceProductBool = QueryBuilders.boolQuery();
            RangeQueryBuilder productModelIdRangeQuery = QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_MODEL_ID);
            productModelIdRangeQuery.gt(0);
            unReplaceProductBool.should(productModelIdRangeQuery);
            unReplaceProductBool.should(QueryBuilders.boolQuery().must(QueryBuilders.existsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_MATERIAL_LIST)));
            matchQueryList.add(unReplaceProductBool);
        }

        
        // 组合产品的主产品或者单产品 add by huangsongbo 2018.8.7
        matchQueryList.add(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PRODUCT_BATCH_TYPE, Arrays.asList(new Integer[] {1, 2})));

        /********************************* Step 2: 组装聚合条件 *************************************************/
        if (null != productSearchAggregationVoList && 0 < productSearchAggregationVoList.size()) {
            productSearchAggregationVoList.forEach(productSearchAggregation -> {
                if (null != productSearchAggregation) {
                    //字段聚合
                    if (ProductSearchAggregationVo.FIELD_AGGREGATION_TYPE == productSearchAggregation.getAggregationType()) {
                        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(productSearchAggregation.getAggregationName());
                        termsAggregationBuilder.field(productSearchAggregation.getAggregationFieldName());
                        termsAggregationBuilder.size(productSearchAggregation.getAggregationSize());
                        aggregationBuilderList.add(termsAggregationBuilder);
                        //最小值聚合
                    } else if (ProductSearchAggregationVo.MIN_VALUE_AGGREGATION_TYPE == productSearchAggregation.getAggregationType()) {
                        MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min(productSearchAggregation.getAggregationName());
                        minAggregationBuilder.field(productSearchAggregation.getAggregationFieldName());
                        aggregationBuilderList.add(minAggregationBuilder);
                        //最大值聚合
                    } else if (ProductSearchAggregationVo.MAX_VALUE_AGGREGATION_TYPE == productSearchAggregation.getAggregationType()) {
                        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max(productSearchAggregation.getAggregationName());
                        maxAggregationBuilder.field(productSearchAggregation.getAggregationFieldName());
                        aggregationBuilderList.add(maxAggregationBuilder);
                    }
                }
            });
        }

        /********************************* Step 3: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;

        log.info(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表:ProductSearchMatchVo:{},ProductSearchAggregationVoList:{}", productSearchMatchVo.toString(), JsonUtil.toJson(productSearchAggregationVoList));
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, noMatchQueryList, shouldMatchSearchList, aggregationBuilderList, sortOrderObjectList, pageVo.getStart(), pageVo.getPageSize(), IndexConstant.INDEX_PRODUCT_INFO_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败! ElasticSearchException:{}.", e);
            throw new ProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败,查询结果为空。ProductSearchMatchVo:{}.", productSearchMatchVo.toString());
            throw new ProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败,查询结果为空。ProductSearchMatchVo:" + productSearchMatchVo.toString());
        }

        return searchObjectResponse;
    }


    /**
     * 组合查询条件:产品品牌优先级最高-->产品小分类-->产品属性-->当前前一个月的产品使用数-->产品使用数排序-->产品发布时间
     * @author xiaoxc
     * @data 2019-01-11
      */
    private void getSearchProductSortObj(List<SortOrderObject> sortOrderObjectList, ProductSearchMatchVo productSearchMatchVo) {

        if (null == productSearchMatchVo) {
            return;
        }

        // 按发布时间排序(通用版按钮) DESC
        if (productSearchMatchVo.getSortType() != null && productSearchMatchVo.getSortType().intValue() == 1) {
            sortOrderObjectList.add(new SortOrderObject(
                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PUTAWAY_DATE,
                    SortOrder.DESC,
                    SortMode.MEDIAN,
                    SortOrderObject.DEFAULT_SORT
            ));
        }

        // 产品品牌优先级最高
        Integer productBrandId = productSearchMatchVo.getProductBrandId();
        if (null != productBrandId && 0 < productBrandId) {
            Script scriptSort = new Script("(doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_ID+"'].value == " + productBrandId + ") ? 2 : (doc['productBrandId'].value == 178) ? 1 : 0");
            sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.DESC, SortOrderObject.SCRIPT_SORT));
        }

        //当前大类
        Integer nowProductTypeValue = productSearchMatchVo.getNowProductTypeValue();
        if (null != nowProductTypeValue && 0 < nowProductTypeValue) {
            Script scriptSort = new Script("(doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TYPE_VALUE+"'].value == " + nowProductTypeValue + ") ? 1 : 0");
            sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.DESC, SortOrderObject.SCRIPT_SORT));
        }

        // 产品当前小类优先
        Integer nowProductTypeSmallValue = productSearchMatchVo.getNowProductTypeSmallValue();
        if (null != nowProductTypeSmallValue && 0 < nowProductTypeSmallValue) {
            Script scriptSort = new Script("(doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SMALL_TYPE_VALUE+"'].value == " + nowProductTypeSmallValue + ") ? 1 : 0");
            sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.DESC, SortOrderObject.SCRIPT_SORT));
        }

        // 产品属性
        Map<String, String> productOrderAttributeMap = productSearchMatchVo.getProductOrderAttributeMap();
        if (null != productOrderAttributeMap && 0 < productOrderAttributeMap.size()) {
            //先判断属性成品是否存在
            List<String> attributeKey = redisService.getList(RedisConstant.PRODUCT_ATTRIBUT_KEY_LIST);
            for (Map.Entry<String, String> entries : productOrderAttributeMap.entrySet()) {
                if (null != attributeKey && attributeKey.size() > 0 && attributeKey.indexOf(entries.getKey()) != -1) {
                    Script scriptSort = new Script("(doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ATTRIBUTE_TYPE_ORDER_MAP+"." + entries.getKey() + ".keyword'].value == '" + entries.getValue() + "') ? 1 : 0");
                    sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.DESC, SortOrderObject.SCRIPT_SORT));
                }
            }
        }

        // 窗帘按高度就近排序
        Integer fullPaveHeight = productSearchMatchVo.getFullPaveHeight();
        if (null != fullPaveHeight && fullPaveHeight != 0) {
            if (null != nowProductTypeValue && ProductTypeValue.PRODUCT_TYPE_VALUE_HOME_TEXTILES == nowProductTypeValue) {
                if(null != nowProductTypeSmallValue  && (ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_CURTAIN_HOME_TEXTILES == nowProductTypeSmallValue
                        || ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CURTAIN_HOME_TEXTILES == nowProductTypeSmallValue)) {
                    Script scriptSort = new Script("Math.abs(doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT+"'].value - " + fullPaveHeight + ")");
                    sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.ASC, SortOrderObject.SCRIPT_SORT));
                }
            }
        }

        // 当前时间前一个月的产品使用次数 DESC
        sortOrderObjectList.add(new SortOrderObject(
                QueryConditionField.QUERY_CONDITION_FIELD_LASTMONTH_USEDPRODUCT_COUNT,
                SortOrder.DESC,
                SortMode.MAX,
                SortOrderObject.DEFAULT_SORT
        ));

        // 用户个人使用产品次数 DESC
        Integer userId = productSearchMatchVo.getUserId();
        if (null != userId && 0 < userId) {
            // 没有使用过产品不需要用此排序
            Integer userUsageProductCount = sysUserMetaDataStorage.getUserUsageProductCount(userId);
            if (null != userUsageProductCount && userUsageProductCount > 0) {
                sortOrderObjectList.add(new SortOrderObject(
                        QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_USAGE_COUNT + "." + userId,
                        SortOrder.DESC,
                        SortMode.MAX,
                        SortOrderObject.DEFAULT_SORT
                ));
            }
        }

        // 默认按发布时间倒序
        if (productSearchMatchVo.getSortType() == null) {
            sortOrderObjectList.add(new SortOrderObject(
                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PUTAWAY_DATE,
                    SortOrder.DESC,
                    SortMode.MEDIAN,
                    SortOrderObject.DEFAULT_SORT
            ));
        }
    }

    /**
     * 特殊分类需要长度、高度排序（就近原则，先长后高）
     * @author：xiaoxc
     * @date: 2019-06-19
     */
    /*private void getSpecialTypeOrder(ProductSearchMatchVo productSearchMatchVo, List<SortOrderObject> sortOrderObjectList) {
        //当前大类
        Integer productTypeValue = productSearchMatchVo.getNowProductTypeValue();
        Integer productSmallTypeValue = productSearchMatchVo.getNowProductTypeSmallValue();
        //获取大分类key
        String bigValueKey = systemDictionaryMetaDataStorage.getSystemDictionaryValueKeyByTypeAndValue("productType", productTypeValue);
        if (StringUtils.isEmpty(bigValueKey)) {
            return;
        }
        //判断大分类key是否在配置中
        String mapValue = productTypeMap.get(bigValueKey);
        if (StringUtils.isEmpty(mapValue)) {
            return;
        }
        //获取小分类key
        String smallValueKey = systemDictionaryMetaDataStorage.getSystemDictionaryValueKeyByTypeAndValue(bigValueKey, productSmallTypeValue);
        if (StringUtils.isEmpty(smallValueKey)) {
            return;
        }
        //判断小分类key是否在配置中
        List<String> valueList = new ArrayList<>(Arrays.asList(mapValue.split(",")));
        if (!valueList.contains(smallValueKey)) {
            return;
        }
        //长度排序
        Integer fullPaveLength = productSearchMatchVo.getFullPaveLength();
        if (null != fullPaveLength && fullPaveLength != 0) {
            Script scriptSort = new Script(ScriptType.INLINE,"painless","doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH+"'].value == " + fullPaveLength + "?0:(" + fullPaveLength + " - doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH+"'].value < 0 ? (doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH+"'].value - " + fullPaveLength + "):(" + fullPaveLength + " - doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_LENGTH+"'].value))", new HashMap<>());
            sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.ASC, SortOrderObject.SCRIPT_SORT));
        }
        //高度排序
        Integer fullPaveHeight = productSearchMatchVo.getFullPaveHeight();
        if (null != fullPaveHeight && fullPaveHeight != 0) {
            Script scriptSort = new Script(ScriptType.INLINE,"painless","doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT+"'].value == " + fullPaveHeight + "?0:(" + fullPaveHeight + " - doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT+"'].value < 0 ? (doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT+"'].value - " + fullPaveHeight + "):(" + fullPaveHeight + " - doc['"+QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_HEIGHT+"'].value))", new HashMap<>());
            sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.ASC, SortOrderObject.SCRIPT_SORT));
        }
    }*/



    /**
     * 设置长度过滤条件,并且如果是多模型产品,则无视该过滤条件
     * 多模型产品不考虑长度过滤
     * 
     * @author huangsongbo
     * @param fullpaveBoolQueryBuilder 
     * @param fullPaveLengthRangeQueryBuilder
     */
    private void dealWithfullPaveLengthRangeQueryBuilder(BoolQueryBuilder fullpaveBoolQueryBuilder, RangeQueryBuilder fullPaveLengthRangeQueryBuilder) {
		// 参数验证 ->start
    	// 略
    	// 参数验证 ->end
    	
    	/*{
    		"bool": {
    			"should": [{
    					"range": {
    						"productLength": {
    							"from": 540,
    							"to": 550,
    							"include_lower": false,
    							"include_upper": true,
    							"boost": 1.0
    						}
    					}
    				},
    				{
    					"term": {
    						"productBatchType": 2
    					}
    				}
    			]
    		}
    	}*/
    	
    	/*fullpaveBoolQueryBuilder.must(fullPaveLengthRangeQueryBuilder);*/
    	BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(fullPaveLengthRangeQueryBuilder);
        boolQueryBuilder.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PRODUCT_BATCH_TYPE, 2));
        fullpaveBoolQueryBuilder.must(boolQueryBuilder);
	}

	@Override
    public ProductIndexMappingData searchProductById(Integer productId) throws ProductSearchException {

        if (null == productId || 0 > productId) {
            return null;
        }

        //构造查询体
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ID, productId);

        //查询数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(Collections.singletonList(matchQueryBuilder), null, null, null, null, IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START, 1, IndexConstant.INDEX_PRODUCT_INFO_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过产品ID查询产品失败，QueryBuilder:{}, ElasticSearchException:{}.", matchQueryBuilder.toString(), e);
            throw new ProductSearchException(CLASS_LOG_PREFIX + "通过产品ID查询产品失败，QueryBuilder:" + matchQueryBuilder.toString() + ", ElasticSearchException:" + e);
        }

        if (null != searchObjectResponse) {
            List<ProductIndexMappingData> productIndexMappingDataList = (List<ProductIndexMappingData>) searchObjectResponse.getHitResult();
            if (null != productIndexMappingDataList && 0 < productIndexMappingDataList.size()) {
                return productIndexMappingDataList.get(0);
            }
        }

        return null;
    }

    /**
     * 获取平台编码布尔查询
     *
     * @param platformCode 平台编码
     * @param isInnerUser 是否是内部用户
     * @param productLibraryAuth 产品审核权限
     * @return
     */
    private BoolQueryBuilder getBoolQueryBuildersOfPlatformCode(String platformCode, boolean isInnerUser, boolean productLibraryAuth) {

        //内部用户且分配产品审核权限不需要平台过滤
        if (isInnerUser && productLibraryAuth) {
            return null;
        }
        BoolQueryBuilder boolQueryBuilder = null;

        if (!StringUtils.isEmpty(platformCode)) {
            //初始化查询
            boolQueryBuilder = new BoolQueryBuilder();
            //新增条件
            boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_CODE_LIST, platformCode));

            //需要检查产品平台状态必须为已发布
            switch (platformCode) {
                //2B-移动端
                case PlatformConstant.PLATFORM_CODE_TOB_MOBILE:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_TOB_MOBILE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    break;
                //2B-PC
                case PlatformConstant.PLATFORM_CODE_TOB_PC:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_TOB_PC_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    break;
                //品牌2C-网站
                case PlatformConstant.PLATFORM_CODE_TOC_SITE:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_TOC_SITE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    break;
                //2C-移动端
                case PlatformConstant.PLATFORM_CODE_TOC_MOBILE:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_TOC_MOBILE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    break;
                //Mini-Program
                case PlatformConstant.PLATFORM_CODE_MINI_PROGRAM:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_MINI_PROGRAM_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    break;
                //随选网
                case PlatformConstant.PLATFORM_CODE_SELECT_DECORATION:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_SELECT_DECORATION_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    break;
            }
        }

        return boolQueryBuilder;
    }

    /**
     * COMMON-1893：公开与不公开产品逻辑优化
     * @author xiaoxc
     * @param productSearchMatchVo
     * @date 2019-05-30
     * @return
     */
    private BoolQueryBuilder getOpenProductConditions(ProductSearchMatchVo productSearchMatchVo) {
        /**
         * 本企业品牌  or 非本公司公开
         * bool
         *   |
         *   |---should
         *   |      |
         *   |      |---bool(brandBool)
         *   |             |terms(companyBrandIdList)
         *   |             |
         *   |      |---bool(secrecyFlagBool)
         *   |             |---terms(secrecyFlag)
         *
         */
        // 自己公司的产品不需要公开就可见，通过品牌过滤
        List<Integer> companyBrandIdList = productSearchMatchVo.getCompanyBrandIdList();
        BoolQueryBuilder secrecyFlagShouldBool = QueryBuilders.boolQuery();
        if (!Lists.isEmpty(companyBrandIdList)) {
            //品牌
            BoolQueryBuilder brandBool = QueryBuilders.boolQuery();
            brandBool.must(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_ID, companyBrandIdList));
            secrecyFlagShouldBool.should(brandBool);
        }

        //其他公司需要公开才可见
        BoolQueryBuilder secrecyFlagBool = QueryBuilders.boolQuery();
        secrecyFlagBool.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SECRECY_FLAG, ProductPo.NO_SECRECY));
        secrecyFlagShouldBool.should(secrecyFlagBool);

        return secrecyFlagShouldBool;
    }

}
