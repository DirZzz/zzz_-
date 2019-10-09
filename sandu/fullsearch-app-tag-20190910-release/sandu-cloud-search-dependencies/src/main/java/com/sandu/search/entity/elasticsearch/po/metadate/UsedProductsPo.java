package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品已使用记录统计持久化对象(只统计上个月的数据)
 *
 * @author xiaoxc
 * @date 2019-01-10
 */
@Data
public class UsedProductsPo implements Serializable {

    private static final long serialVersionUID = -4630924734953946259L;

    //产品ID
    private Integer productId;
    //上个月使用产品次数
    private int  lastMonthProductUsageCount;
}
