package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 组合产品平台关联持久化对象
 *
 * @author xiaoxc
 * @date 2019-03-29
 */
@Data
public class GroupPlatformRelPo implements Serializable {

    private static final long serialVersionUID = 4166250950405187516L;

    /**
     * 组合ID
     */
    private int groupId;
    /**
     * 平台编码
     */
    private String platformCode;
    /**
     * 平台组合售卖价格
     */
    private Integer salePrice;
    /**
     * 平台组合建议价格
     */
    private Integer advicePrice;
    /**
     * 平台组合描述
     */
    private String description;
    /**
     * 平台组合发布状态
     */
    private int putawatStatus;
    /**
     * 平台组合状态
     */
    private int allotStatus;
    /**
     * 平台封面图片
     */
    private int coverPicId;
    /**
     * 平台组合图片列表
     */
    private String picIds;

}
