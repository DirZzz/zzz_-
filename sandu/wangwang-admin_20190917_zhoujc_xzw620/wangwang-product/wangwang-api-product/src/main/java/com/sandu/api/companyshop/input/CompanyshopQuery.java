package com.sandu.api.companyshop.input;

import com.sandu.common.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * store_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-10-22 16:51
 */
@Data
public class CompanyshopQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "店铺分类")
    /*private Integer businessType;*/
    private String businessType;

    @ApiModelProperty(value="查询参数")
    private String queryParam;


    @ApiModelProperty(value = "isRelease ： 1是已发布， 0是未发布。")
    private Integer isRelease;

    private Integer userType;


    private String provinceCode;

    private String cityCode;

    private String areaCode;

    @ApiModelProperty(value = "店铺类型支持多选")
    private String shopTypes;
    
    /**
     * business_type in #{businessTypeList}
     */
    private List<Integer> businessTypeList;
    
}
