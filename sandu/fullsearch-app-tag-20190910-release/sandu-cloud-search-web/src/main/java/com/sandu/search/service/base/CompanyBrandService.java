package com.sandu.search.service.base;

import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.user.LoginUser;

import java.util.List;

/**
 * 企业服务
 *
 * @author xiaoxc
 * @data 2019/3/28 0028.
 */
public interface CompanyBrandService {

    /**
     * 获取企业信息
     * @param platformCode
     * @param loginUser
     * @return
     */
    CompanyPo getCompanyInfo(String platformCode, LoginUser loginUser);

    /**
     * 获取企业品牌集合
     * @param companyPo
     * @param platformCode
     * @return
     */
    List<Integer> getBrandIdList(CompanyPo companyPo, String platformCode);
}
