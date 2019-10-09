package com.sandu.search.entity.product.universal;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SXWProductBaseConditionVo implements Serializable {
    private static final long serialVersionUID = -6735916575266543708L;
    //搜索关键字
    private String searchKeyword;
    // 产品分类五级LongCode集合
    private List<String> productFiveCategoryLongCodes;
    // 产品分类四级LongCode集合
    private List<String> productThreeCategoryLongCodes;

    /**
     * 无结果时，是否展示推荐商品（true ：展示，false:不展示）
     */
    private Boolean recommendFlag = true;
    /**
     * 品牌ID
     */
    private Integer brandId;
    private List<Integer> brandIds;

    /**
     * 品牌管过滤
     */
    private Boolean filterWithBrandShop;

    /**
     * 店铺ID
     */
    private Integer shopId;

    /**
     * 子节点
     */
    private List<Integer> childCategoryIds;

    /**
     * 1 / 2/ 3 级节点
     */
    private Integer parentCategoryId;

    //产品风格ID
    private Integer styleId;

    private int companyId; //公司ID


}
