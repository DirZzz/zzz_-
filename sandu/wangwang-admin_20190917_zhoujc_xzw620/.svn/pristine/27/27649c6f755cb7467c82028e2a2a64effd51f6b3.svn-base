package com.sandu.api.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品扩展字段实体
 * @author xiaoxc
 * @data 2019/8/26 0026.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductExtension implements Serializable {

    public final static int PRODUCTS_HAVE_SPECIAL_EFFECTS = 1;
    public final static int PRODUCTS_NO_HAVE_SPECIAL_EFFECTS = 0;

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 产品Id
     */
    private Long productId;

    /**
     * 商家后台产品合并标识，1：合并产品
     */
    private Integer productMergeFlag;

    /**
     * 是否有特效(0:无,1:有)
     */
    private Integer isSpecialEffects;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date gmtModified;


}
