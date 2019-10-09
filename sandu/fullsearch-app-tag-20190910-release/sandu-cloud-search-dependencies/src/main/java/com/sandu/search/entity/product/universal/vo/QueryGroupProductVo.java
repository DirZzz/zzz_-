package com.sandu.search.entity.product.universal.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索组合产品信息参数
 *
 * @author xiaoxc
 * @data 2019/3/20 0020.
 */
@Data
public class QueryGroupProductVo implements Serializable {

    private static final long serialVersionUID = 5297283057169310791L;

    /**
     * 关键字（组合名称,组合编码,品牌名称）
     */
    private String searchKeyword;
    /**
     * 分类编码
     */
    private String categoryCode;
    /**
     * 产品大分类value
     */
    private Integer productTypeValue;
    /**
     * 空间面积
     */
    private Integer spaceAreas;
    /**
     * 组合Id
     */
    private Integer groupId;
    /**
     * 初始化产品Id
     */
    private Integer initProductId;
    /**
     * 搜索类型[1: 搜全部; (0/null): 搜本类
     */
    private Integer searchType;
    /**
     * 我的组合搜索则传用户Id
     */
    private Integer myUserId;
    /**
     * 品牌Id
     */
    private Integer brandId;
    /**
     * 起始页数
     */
    private Integer start;
    /**
     * 每页显示的数量
     */
    private Integer limit;

    /**************** 小程序条件 ***************/
    /**
     * 方案ID
     */
    private Integer planId;
    /**
     * 方案类型(2：推荐方案、3：效果图方案、默认草图方案 1)
     */
    private Integer designPlanType;


}
