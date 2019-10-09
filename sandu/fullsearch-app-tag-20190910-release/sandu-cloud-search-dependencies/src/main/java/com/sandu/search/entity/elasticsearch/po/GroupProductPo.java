package com.sandu.search.entity.elasticsearch.po;

import com.sandu.search.entity.elasticsearch.po.metadate.GroupPlatformRelPo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 组合产品实体类
 *
 * @author :  xiaoxc
 * @date : 20190319
 */
@Data
public class GroupProductPo implements Serializable {

    private static final long serialVersionUID = -2938959816564463424L;

    /**
     * 组合Id
     */
    private Integer id;
    /**
     * 组合Id
     */
    private String groupCode;
    /**
     * 组合Id
     */
    private String groupName;
    /**
     * 上架状态(0:未上架 1:上架 2:测试中 3:发布 4:下架)
     */
    private Integer state;
    /**
     * 组合类型
     */
    private Integer compositeType;
    /**
     * 品牌ID
     */
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 适用面积
     */
    private Integer spaceAreaValue;
    /**
     * 组合类型(0:普通组合;1:一件装修组合)
     */
    private Integer groupType;
    /**
     * 所属空间类型
     */
    private Integer houseType;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 总价格
     */
    private Double groupPrice;
    /**
     * 建议价格
     */
    private BigDecimal advicePrice;
    /**
     * 修改时间
     */
    private Date gmtModified;
    /**
     * 是否在商家后台显示(0为显示;1为不显示)
     */
    private Integer isDisplay;
    /**
     * 是否删除
     */
    private Integer dataIsDeleted;
    /**
     * 组合公开状态:0非公开,1公开
     */
    private Integer secrecyFlag;
    /**
     * 主产品Id
     */
    private Integer mainProductId;
    /**
     * 大分类value
     */
    private Integer productTypeValue;
    /**
     * 小分类value
     */
    private Integer productSmallTypeValue;
    /**
     * 封面图片路径
     */
    private String picPath;
    /**
     * 分类code
     */
    private String categoryCode;
    /**
     * 分类长code
     */
    private List<String> categoryLongCode;
    /**
     * 组合平台数据
     */
    private List<GroupPlatformRelPo> groupPlatformRel;

}
