package com.sandu.search.entity.product.universal.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SXWProductVo implements Serializable {
    private static final long serialVersionUID = 4716935822951211685L;
    // 商品ID
    private Integer spuId;
    // 商品名称
    private String spuName;
    // 商品图片
    private String picPath;
    // 商品价格
    private BigDecimal price;

    private BigDecimal salePrice;
    // 购买人数
    private Integer sellNumber;

    private Integer productId;

    private ActivityVO activity;
    //装修报价
    private BigDecimal decorationPrice;
}
