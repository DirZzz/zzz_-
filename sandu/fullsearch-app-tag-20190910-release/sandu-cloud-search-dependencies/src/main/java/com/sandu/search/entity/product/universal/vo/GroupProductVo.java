package com.sandu.search.entity.product.universal.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 组合产品页面展示信息类
 *
 * @author xiaoxc
 * @data 2019/3/22 0022.
 */
@Data
public class GroupProductVo implements Serializable {

    private static final long serialVersionUID = 7456426283396224406L;

    /**
     * 组合Id
     */
    private Integer groupId;
    /**
     * 组合封面图路径
     */
    private String picPath;
    /**
     * 收藏状态
     */
    private Integer collectState;
    /**
     * 组合名称
     */
    private String groupName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 组合价格
     */
    private Double groupPrice;



}
