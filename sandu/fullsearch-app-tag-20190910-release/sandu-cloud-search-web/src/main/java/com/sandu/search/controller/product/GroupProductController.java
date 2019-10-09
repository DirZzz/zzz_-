package com.sandu.search.controller.product;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sandu.common.util.collections.Lists;
import com.sandu.search.common.constant.*;
import com.sandu.search.common.tools.EntityCopyUtils;
import com.sandu.search.common.tools.MessageUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import com.sandu.search.entity.elasticsearch.po.GroupProductPo;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.product.universal.vo.GroupProductSearchVo;
import com.sandu.search.entity.product.universal.vo.GroupProductVo;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.common.LoginContext;
import com.sandu.search.entity.product.universal.vo.QueryGroupProductVo;
import com.sandu.search.entity.response.universal.UniversalSearchResultResponse;
import com.sandu.search.exception.GroupProductSearchException;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.base.CompanyBrandService;
import com.sandu.search.service.design.RecommendationPlanSearchService;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.product.GroupProductSearchService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.product.ProductCategoryMetaDataStorage;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 组合产品搜索
 *
 * @author xiaoxc
 * @data 2019/3/20 0020.
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/universal/groupProduct/search")
public class GroupProductController {

    private final static String CLASS_LOG_PREFIX = "组合产品搜索:";

    private final HttpServletRequest request;
    private final CompanyBrandService companyBrandService;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final MetaDataService metaDataService;
    private final BrandMetaDataStorage brandMetaDataStorage;
    private final GroupProductSearchService groupProductSearchService;
    private final ProductCategoryMetaDataStorage productCategoryMetaDataStorage;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;

    @Autowired
    public GroupProductController(GroupProductSearchService groupProductSearchService,
                                  BrandMetaDataStorage brandMetaDataStorage,
                                  MetaDataService metaDataService,
                                  CompanyMetaDataStorage companyMetaDataStorage,
                                  HttpServletRequest request,
                                  CompanyBrandService companyBrandService,
                                  ProductCategoryMetaDataStorage productCategoryMetaDataStorage,
                                  SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage) {
        this.request = request;
        this.companyBrandService = companyBrandService;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.metaDataService = metaDataService;
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.groupProductSearchService = groupProductSearchService;
        this.productCategoryMetaDataStorage = productCategoryMetaDataStorage;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
    }


    @RequestMapping("list")
    UniversalSearchResultResponse queryGroupProductListByCondition(@ModelAttribute("queryGroupProductVo") QueryGroupProductVo queryGroupProductVo, Integer msgId) {

        //参数验证、登录信息获取
        MessageUtil paramVerifyMessage = this.paramVerify(queryGroupProductVo);
        if (!paramVerifyMessage.isStauts()) {
            return new UniversalSearchResultResponse(false, paramVerifyMessage.getMessage(), msgId);
        }
        //接口平台限制
        String platformCode = request.getHeader(HeaderConstant.PLATFORM_CODE);
        if (StringUtils.isBlank(platformCode)) {
            return new UniversalSearchResultResponse(false, "平台编码为空！", msgId);
        }

        GroupProductSearchVo searchVo = new GroupProductSearchVo();
        //初始化参数
        EntityCopyUtils.copyData(queryGroupProductVo, searchVo);

        //非PC端需要传方案Id,用于获取面积
        Integer planId = queryGroupProductVo.getPlanId();
        if (!Objects.equals(PlatformConstant.PLATFORM_CODE_TOB_PC, platformCode)) {
            if (null == planId || planId == 0) {
                return new UniversalSearchResultResponse(false, "Param is empty!");
            }
            Integer spaceAreas = metaDataService.getSpaceAreasByPlanId(planId, queryGroupProductVo.getDesignPlanType());
            searchVo.setSpaceAreas(spaceAreas);
        }

        //设置长编码
        if (!StringUtils.isBlank(queryGroupProductVo.getCategoryCode())) {
            searchVo.setCategoryLongCode(queryGroupProductVo.getCategoryCode());
        }
        LoginUser loginUser = (LoginUser) paramVerifyMessage.getObj();
        //通过内部用户和非内部用户判断状态和组合类型过滤
        List<Integer> stateList = this.getStateList(loginUser);
        if (!Lists.isEmpty(stateList)) {
            searchVo.setStateList(stateList);
        }
        //非内部用户只能查看普通组合
        if (UserTypeConstant.USER_ROLE_TYPE_INNER != loginUser.getUserType()) {
            searchVo.setGroupType(GroupProductConstant.GROUP_TYPE_COMMON);
        }

        //设置组合类型
        Integer groupId = queryGroupProductVo.getGroupId();
        if (null != groupId && groupId > 0) {
            GroupProductPo groupProductPo = metaDataService.getGroupProductById(groupId);
            if (null == groupProductPo) {
                log.error(CLASS_LOG_PREFIX + "找不到该组合数据,groupId:{}", groupId);
                return new UniversalSearchResultResponse(false, "找不到该组合数据！", msgId);
            } else {
                searchVo.setCompositeType(groupProductPo.getCompositeType());
            }
        }

        //获取企业信息
        CompanyPo companyPo = companyBrandService.getCompanyInfo(platformCode, loginUser);
        if (null == companyPo && companyPo.getCompanyId() == 0) {
            log.error(CLASS_LOG_PREFIX + "企业数据错误~ userId:{}", loginUser.getId());
            return new UniversalSearchResultResponse(false, "数据错误!");
        }

        Integer productTypeValue = searchVo.getProductTypeValue();
        if (null != productTypeValue && productTypeValue > 0) {
            //获取公司产品可见范围
            String companyProductVisibilityRange = companyMetaDataStorage.getCompanyProductVisibilityRangeByCompanyId(companyPo.getCompanyId());
            if (!StringUtils.isEmpty(companyProductVisibilityRange)) {
                //公司产品可见范围ID列表--产品分类表ID
                String[] companyProductVisibilityRangeIds = companyProductVisibilityRange.split(",");
                //分类编码列表
                List<String> categoryCodeList = productCategoryMetaDataStorage.queryCategoryCodeByCategoryIds(companyProductVisibilityRangeIds);
                //小类列表--排除非同产品大类的小类ID列表(并将字典valuekey转换为字典值)
                List<Integer> productSmallTypeList = systemDictionaryMetaDataStorage.excludeProductSmallTypeByKeyList(categoryCodeList, searchVo.getProductTypeValue());
                searchVo.setCompanyProductVisibilityRangeIdList(productSmallTypeList);

                //公司可见范围大小类详情
                Map<Integer, List<Integer>> companyAliveTypeMap = systemDictionaryMetaDataStorage.queryProductTypeByKeyList(categoryCodeList);
                if (null != companyAliveTypeMap && 0 < companyAliveTypeMap.size()) {
                    searchVo.setCompanyAliveTypeMap(companyAliveTypeMap);
                }
            }
        }

        String categoryCode = searchVo.getCategoryCode();
        //直接点右上角的"建材家居"搜索或选择产品在渲“建材家居”,通过分类编码查询出大小类(品牌过滤会通过小类来区分)---会重装入大类小类
        if (!StringUtils.isEmpty(categoryCode)) {
            //大小类集合
            List[] productTypeListResult = productCategoryMetaDataStorage.getProductTypeByProductCode(categoryCode);
            if (null != productTypeListResult && 2 == productTypeListResult.length) {
                //大类
                searchVo.setProductTypeValue((Integer) productTypeListResult[0].get(0));
                //小类
                searchVo.setProductSmallTypeList(productTypeListResult[1]);
            }
        }

        //获取企业品牌信息
        List<Integer> brandIdList = companyBrandService.getBrandIdList(companyPo, platformCode);
        if (null != brandIdList && brandIdList.size() > 0) {
            searchVo.setBrandIdList(brandIdList);
        }

        //TODO 单值多字段匹配
        List<MultiMatchField> multiMatchFieldList = new ArrayList<>();

        //构造方案搜索对象--搜索关键字(组合名称>组合编码>品牌名称)
        String searchKeyword = queryGroupProductVo.getSearchKeyword();
        if (!StringUtils.isEmpty(searchKeyword)) {
            searchKeyword = searchKeyword.toLowerCase();
            List<String> fidleNameList = Arrays.asList(
                    QueryConditionField.QUERY_CONDITION_FIELD_GROUP_NAME,
                    QueryConditionField.QUERY_CONDITION_FIELD_GROUP_CODE,
                    QueryConditionField.QUERY_CONDITION_FIELD_BRAND_NAME);
            multiMatchFieldList.add(new MultiMatchField(searchKeyword, fidleNameList));
            //装回对象
            searchVo.setMultiMatchFieldList(multiMatchFieldList);
        }

        //分页参数
        if (null == searchVo.getStart()) {
            searchVo.setStart(0);
        }
        if (null == searchVo.getLimit()) {
            searchVo.setLimit(30);
        }

        SearchObjectResponse searchObjectResponse = null;
        try {
            searchObjectResponse = groupProductSearchService.searchGroupProduct(searchVo);
        } catch (GroupProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "查询组合产品数据异常！Exception:{}", e);
            return new UniversalSearchResultResponse(true, "数据错误!");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....GroupProductSearchVo:{}", searchVo.toString());
            return new UniversalSearchResultResponse(true, "ok", msgId);
        }

        List<GroupProductPo> groupProductIndexData = (List<GroupProductPo>) searchObjectResponse.getHitResult();
        //转换对象
        List<GroupProductVo> groupProductVoList = groupProductSearchService.conversionGroupProductVo(groupProductIndexData, loginUser, platformCode, GroupProductConstant.FUNCT_TYPE_SEARCH_GROUP_LIST);

        return new UniversalSearchResultResponse(true, "ok", msgId, searchObjectResponse.getHitTotal(), groupProductVoList);
    }


    /**
     * 参数验证、获取登录信息
     * @param vo
     * @return
     */
    private MessageUtil paramVerify(QueryGroupProductVo vo) {

        if (null == vo) {
            return new MessageUtil(false, "Param is empty!");
        }

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null == loginUser) {
            return new MessageUtil(false, "请登录!");
        }

        if (null != vo.getMyUserId() && !Objects.equals(loginUser.getId(), vo.getMyUserId())) {
            return new MessageUtil(false, "请求用户非当前登录用户,请登录!");
        }

        if (null == vo.getMyUserId() && StringUtils.isBlank(vo.getCategoryCode())) {
            return new MessageUtil(false, "Param is empty!!");
        }

        return new MessageUtil(true, loginUser);
    }

    /**
     * 获取用户类型状态
     * @param loginUser
     * @return
     */
    private List<Integer> getStateList(LoginUser loginUser) {
        //通过内部用户和非内部用户判断状态和组合类型过滤
        if (UserTypeConstant.USER_ROLE_TYPE_INNER == loginUser.getUserType()) {
            return Arrays.asList(
                    GroupProductConstant.GROUP_STATE_PUTAWAY,
                    GroupProductConstant.GROUP_STATE_TEST,
                    GroupProductConstant.GROUP_STATE_RELEASE
            );
        } else {
            return Arrays.asList(
                    GroupProductConstant.GROUP_STATE_RELEASE
            );
        }
    }


    @RequestMapping("collectList")
    UniversalSearchResultResponse queryCollectGroupList(Integer start, Integer limit) {

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null == loginUser) {
            return new UniversalSearchResultResponse(false, "请登录!");
        }
        String platformCode = request.getHeader(HeaderConstant.PLATFORM_CODE);
        if (StringUtils.isBlank(platformCode)) {
            return new UniversalSearchResultResponse(false, "平台编码为空！");
        }
        GroupProductSearchVo searchVo = new GroupProductSearchVo();
        searchVo.setMyUserId(loginUser.getId());
        //通过内部用户和非内部用户判断状态和组合类型过滤
        List<Integer> stateList = this.getStateList(loginUser);
        if (!Lists.isEmpty(stateList)) {
            searchVo.setStateList(stateList);
        }
        //非内部用户只能查看普通组合
        if (UserTypeConstant.USER_ROLE_TYPE_INNER != loginUser.getUserType()) {
            searchVo.setGroupType(GroupProductConstant.GROUP_TYPE_COMMON);
        }
        //分页参数
        if (null == start || null == limit) {
            searchVo.setStart(0);
            searchVo.setLimit(24);
        } else {
            searchVo.setStart(start);
            searchVo.setLimit(limit);
        }

        int collectGroupCount = groupProductSearchService.getGroupCollectCount(searchVo);
        SearchObjectResponse searchObjectResponse = null;
        if (collectGroupCount > 0) {
            List<Integer> collectGroupIdList = groupProductSearchService.getGroupCollectList(searchVo);
            try {
                searchObjectResponse = groupProductSearchService.searchGroupProductByIds(collectGroupIdList);
            } catch (GroupProductSearchException e) {
                log.error(CLASS_LOG_PREFIX + "根据GroupIdList查询组合产品数据异常！Exception:{}", e);
                return new UniversalSearchResultResponse(true, "数据错误!");
            }
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....GroupProductSearchVo:{}", searchVo.toString());
            return new UniversalSearchResultResponse(true, "ok");
        }

        List<GroupProductPo> groupProductIndexData = (List<GroupProductPo>) searchObjectResponse.getHitResult();
        //转换对象
        List<GroupProductVo> groupProductVoList = groupProductSearchService.conversionGroupProductVo(groupProductIndexData, loginUser, platformCode, GroupProductConstant.FUNCT_TYPE_COLLECT_GROUP_LIST);

        return new UniversalSearchResultResponse(true, "ok", collectGroupCount, groupProductVoList);

    }

    @RequestMapping("brandGroupList")
    UniversalSearchResultResponse queryBrandGroupList(@ModelAttribute("queryGroupProductVo") QueryGroupProductVo queryGroupProductVo) {

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null == loginUser) {
            return new UniversalSearchResultResponse(false, "请登录!");
        }
        String platformCode = request.getHeader(HeaderConstant.PLATFORM_CODE);
        if (StringUtils.isBlank(platformCode)) {
            return new UniversalSearchResultResponse(false, "平台编码为空！");
        }
        if (null == queryGroupProductVo || null == queryGroupProductVo.getBrandId() || queryGroupProductVo.getBrandId() <= 0) {
            return new UniversalSearchResultResponse(false, "Param is empty！");
        }
        GroupProductSearchVo searchVo = new GroupProductSearchVo();
        //初始化参数
        EntityCopyUtils.copyData(queryGroupProductVo, searchVo);

        //通过内部用户和非内部用户判断状态和组合类型过滤
        List<Integer> stateList = this.getStateList(loginUser);
        if (!Lists.isEmpty(stateList)) {
            searchVo.setStateList(stateList);
        }
        //非内部用户只能查看普通组合
        if (UserTypeConstant.USER_ROLE_TYPE_INNER != loginUser.getUserType()) {
            searchVo.setGroupType(GroupProductConstant.GROUP_TYPE_COMMON);
        }
        //筛选品牌过滤
        searchVo.setBrandIdList(Arrays.asList(queryGroupProductVo.getBrandId()));

        //分页参数
        if (null == searchVo.getStart()) {
            searchVo.setStart(0);
        }
        if (null == searchVo.getLimit()) {
            searchVo.setLimit(24);
        }
        SearchObjectResponse searchObjectResponse = null;
        try {
            searchObjectResponse = groupProductSearchService.searchGroupProduct(searchVo);
        } catch (GroupProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "根据条件查询组合产品数据异常！Exception:{}", e);
            return new UniversalSearchResultResponse(true, "数据错误!");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....searchVo:{}", searchVo.toString());
            return new UniversalSearchResultResponse(true, "ok");
        }

        List<GroupProductPo> groupProductIndexData = (List<GroupProductPo>) searchObjectResponse.getHitResult();
        //转换对象
        List<GroupProductVo> groupProductVoList = groupProductSearchService.conversionGroupProductVo(groupProductIndexData, loginUser, platformCode, GroupProductConstant.FUNCT_TYPE_SEARCH_GROUP_LIST);

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), groupProductVoList);

    }

    /*@RequestMapping("categoryGroupList")
    UniversalSearchResultResponse categoryGroupList(@ModelAttribute("queryGroupProductVo") QueryGroupProductVo queryGroupProductVo) {

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null == loginUser) {
            return new UniversalSearchResultResponse(false, "请登录!");
        }
        String platformCode = request.getHeader(HeaderConstant.PLATFORM_CODE);
        if (StringUtils.isBlank(platformCode)) {
            return new UniversalSearchResultResponse(false, "平台编码为空！");
        }
        if (null == queryGroupProductVo || StringUtils.isBlank(queryGroupProductVo.getCategoryCode())) {
            return new UniversalSearchResultResponse(false, "Param is empty！");
        }
        GroupProductSearchVo searchVo = new GroupProductSearchVo();
        //初始化参数
        EntityCopyUtils.copyData(queryGroupProductVo, searchVo);

        //通过内部用户和非内部用户判断状态和组合类型过滤
        List<Integer> stateList = this.getStateList(loginUser);
        if (!Lists.isEmpty(stateList)) {
            searchVo.setStateList(stateList);
        }
        //非内部用户只能查看普通组合
        if (UserTypeConstant.USER_ROLE_TYPE_INNER != loginUser.getUserType()) {
            searchVo.setGroupType(GroupProductConstant.GROUP_TYPE_COMMON);
        }
        //筛选品牌过滤
        searchVo.setCategoryLongCode(queryGroupProductVo.getCategoryCode());

        //分页参数
        if (null == searchVo.getStart()) {
            searchVo.setStart(0);
        }
        if (null == searchVo.getLimit()) {
            searchVo.setLimit(24);
        }
        SearchObjectResponse searchObjectResponse = null;
        try {
            searchObjectResponse = groupProductSearchService.searchGroupProduct(searchVo);
        } catch (GroupProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "根据条件查询组合产品数据异常！Exception:{}", e);
            return new UniversalSearchResultResponse(true, "数据错误!");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....searchVo:{}", searchVo.toString());
            return new UniversalSearchResultResponse(true, "ok");
        }

        List<GroupProductPo> groupProductIndexData = (List<GroupProductPo>) searchObjectResponse.getHitResult();
        //转换对象
        List<GroupProductVo> groupProductVoList = groupProductSearchService.conversionGroupProductVo(groupProductIndexData, loginUser, platformCode, GroupProductConstant.FUNCT_TYPE_SEARCH_GROUP_LIST);

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), groupProductVoList);

    }*/

    @RequestMapping("getGroupInfo")
    UniversalSearchResultResponse getGroupInfoByIds(String groupIds) {

        if (StringUtils.isBlank(groupIds)) {
            return new UniversalSearchResultResponse(false, "Param is null!");
        }
        List<String> idsList = new ArrayList<>(Arrays.asList(groupIds.split(",")));

        List<Integer> idList = StringUtil.transformInteger(idsList);
        if (null == idList || idList.size() == 0) {
            return new UniversalSearchResultResponse(false, "Param is error!");
        }

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null == loginUser) {
            return new UniversalSearchResultResponse(false, "请登录!");
        }
        String platformCode = request.getHeader(HeaderConstant.PLATFORM_CODE);
        if (StringUtils.isBlank(platformCode)) {
            return new UniversalSearchResultResponse(false, "平台编码为空！");
        }

        SearchObjectResponse searchObjectResponse = null;
        try {
            searchObjectResponse = groupProductSearchService.searchGroupProductByIds(idList);
        } catch (GroupProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "根据GroupIdList查询组合产品数据异常！Exception:{}", e);
            return new UniversalSearchResultResponse(false, "数据错误!");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....GroupIdList:{}", idList);
            return new UniversalSearchResultResponse(true, "ok");
        }

        List<GroupProductPo> groupProductIndexData = (List<GroupProductPo>) searchObjectResponse.getHitResult();
        //转换对象
        List<GroupProductVo> groupProductVoList = groupProductSearchService.conversionGroupProductVo(groupProductIndexData, loginUser, platformCode, GroupProductConstant.FUNCT_TYPE_SEARCH_GROUP_LIST);

        return new UniversalSearchResultResponse(true, "ok", groupProductVoList.size(), groupProductVoList);

    }

}
