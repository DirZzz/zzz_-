package com.sandu.api.goods.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GoodsVO implements Serializable
{
    /**
     * spuId
     */
    private Integer id;
    /**
     * spuCode
     */
    private String spuCode;
    /**
     * 图片地址
     */
    private String pic;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 商品内sku的最大价格
     */
    private BigDecimal maxPrice;
    /**
     * 商品内sku的最小价格
     */
    private BigDecimal minPrice;
    /**
     * 总库存
     */
    private Integer totalInventory;
    /**
     * 是否预售
     */
    private Integer isPresell;
    /**
     * 是否上架
     */
    private Integer isPutaway;
    /**
     * 是否特卖
     */
    private Integer isSpecialOffer;
    /**
     * 小类名称
     */
    private String smallType;
    /**
     * 大类名称
     */
    private String bigType;
    /**
     * 模型ID
     */
    private Integer modelId;
    /**
     * 贴图IDs
     */
    private String materialIds;
    /**
     * 有无模型/贴图
     */
    private String modelOrMaterial;

    private String productDesc;

    private Integer sort;

    private String companyName;
}
