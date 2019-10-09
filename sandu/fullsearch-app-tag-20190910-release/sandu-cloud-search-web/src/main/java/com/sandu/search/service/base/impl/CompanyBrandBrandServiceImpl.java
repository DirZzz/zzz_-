package com.sandu.search.service.base.impl;

import com.sandu.search.common.constant.CompanyTypeEnum;
import com.sandu.search.common.constant.PlatformConstant;
import com.sandu.search.common.constant.UserTypeConstant;
import com.sandu.search.common.tools.DomainUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.base.CompanyBrandService;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.system.SysUserMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 企业实现服务
 *
 * @author xiaoxc
 * @data 2019/3/28 0028.
 */
@Slf4j
@Service("baseCompanyService")
public class CompanyBrandBrandServiceImpl implements CompanyBrandService {

    private static final String CLASS_LOG_PREFIX = "企业实现服务:";

    private final HttpServletRequest request;
    private final DomainUtil domainUtil;
    private final SysUserMetaDataStorage sysUserMetaDataStorage;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final MetaDataService metaDataService;
    private final BrandMetaDataStorage brandMetaDataStorage;

    private CompanyBrandBrandServiceImpl(HttpServletRequest request, DomainUtil domainUtil, SysUserMetaDataStorage sysUserMetaDataStorage, CompanyMetaDataStorage companyMetaDataStorage, MetaDataService metaDataService, BrandMetaDataStorage brandMetaDataStorage) {
        this.request = request;
        this.domainUtil = domainUtil;
        this.sysUserMetaDataStorage = sysUserMetaDataStorage;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.metaDataService = metaDataService;
        this.brandMetaDataStorage = brandMetaDataStorage;
    }

    @Override
    public CompanyPo getCompanyInfo(String platformCode, LoginUser loginUser) {
        /*********************************** 公司信息 ******************************/
        Integer companyId = 0;
        if (PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformCode)) {
            //品牌网站HOST解析
            String domainName = DomainUtil.getDomainNameByHost(request);
            log.info(CLASS_LOG_PREFIX + "公司域名-domainName:{}", domainName);

            if (StringUtils.isEmpty(domainName)) {
                log.info(CLASS_LOG_PREFIX + "品牌网站HOST解析失败 domainname is null!");
                return null;
            }
            //获取为空则直接获取公司--(小程序有公司但没有公司域名)
            companyId = companyMetaDataStorage.getCompanyIdByDomainName(domainName);
            log.info(CLASS_LOG_PREFIX + "Brand2C公司ID获取完成! DomainName:{}, CompanyId:{}.", domainName, companyId);

        } else if (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode) ||
                PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
            //小程序获取公司ID
            companyId = domainUtil.getCompanyIdInMiniProgramByRequest(request);
            log.info(CLASS_LOG_PREFIX + "小程序获取公司ID为:{}.", companyId);

        } else if (PlatformConstant.PLATFORM_CODE_TOB_PC.equals(platformCode) ||
                PlatformConstant.PLATFORM_CODE_TOB_MOBILE.equals(platformCode)) {
            //获取公司ID
            int businessAdministrationId = loginUser.getBusinessAdministrationId();
            if (0 == businessAdministrationId) {
                SysUserPo userPo = sysUserMetaDataStorage.getUserPoByUserId(loginUser.getId());
                if (userPo != null) {
                    Integer franchiser = userPo.getBusinessAdministrationId();
                    if (null == franchiser || franchiser == 0) {
                        companyId = userPo.getCompanyId() == null ? 0 : userPo.getCompanyId();
                    } else {
                        companyId = franchiser;
                    }
                }
            } else {
                companyId = businessAdministrationId;
            }
        }
        CompanyPo companyPo = null;
        if (null != companyId && 0 < companyId) {
            //缓存获取经销商获取厂商Id
            companyPo = companyMetaDataStorage.getCompanyPoByCompanyId(new Integer(companyId));
            //缓存为空从数据库获取
            if (null == companyPo || 0 == companyPo.getCompanyId()) {
                try {
                    companyPo = metaDataService.queryCompanyById(companyId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "获取企业异常~ e:{}" + e);
                    return null;
                }
            }
        }
        return companyPo;
    }


    @Override
    public List<Integer> getBrandIdList(CompanyPo companyPo, String platformCode) {
        if (null == companyPo) {
            return null;
        }
        List<Integer> brandIdList = new ArrayList<>();
        Integer companyId = companyPo.getCompanyId();
        //TODO 厂商、独立经销商、经销商用户品牌
        if (CompanyTypeEnum.MANUFACTURER.getValue().toString().equals(companyPo.getBusinessType())
                || CompanyTypeEnum.INDEPENDENT_DEALERS.getValue().toString().equals(companyPo.getBusinessType())) {
            //获取厂商、独立经销商
            brandIdList = brandMetaDataStorage.queryBrandIdListByCompanyIdList(new ArrayList<>(Arrays.asList(companyId)));
        } else if (CompanyTypeEnum.FRANCHISER.getValue().toString().equals(companyPo.getBusinessType())) {
            //获取经销商品牌
            if (!com.alibaba.dubbo.common.utils.StringUtils.isBlank(companyPo.getDealerBrands())) {
                brandIdList = StringUtil.transformInteger(Arrays.asList(companyPo.getDealerBrands().split(",")));
            }
        }

        //小程序可用品牌过滤
        if (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode)
                || PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
            String appId = domainUtil.getAppIdFromMiniProgramByRequest(request);
            brandIdList = brandMetaDataStorage.getEnableBrandIdsByAppId(appId);
            if (null == brandIdList || 0 == brandIdList.size()) {
                brandIdList = brandMetaDataStorage.queryBrandIdListByCompanyIdList(Arrays.asList(companyId));
            }
        }
        if (null != brandIdList && 0 < brandIdList.size()) {
            //可以查看无品牌数据
            brandIdList.add(-1);
        }

        return brandIdList;
    }


}
