package com.sandu.search.entity.product.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 组合产品收藏PO
 *
 * @author xiaoxc
 * @data 2019/3/22 0022.
 */
@Data
public class GroupProductCollectPo implements Serializable {

    private static final long serialVersionUID = -6680529371210657388L;

    /**
     * 组合ID
     */
    private Integer groupId;

    /**
     * 收藏标志
     */
    private Integer collectState;
}
