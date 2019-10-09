package com.sandu.api.goods.model.po;

import lombok.Data;
import java.io.Serializable;

@Data
public class GoodsListQueryPO implements Serializable
{
    private String typeCode;

    private String childTypeCode;

    private Integer isPutaway;

    private Integer isPresell;

    private String spuName;

    private String spuCode;

    private Integer companyId;

    private Integer start = 0;

    private Integer limit = 20;

    private String productModelNumber;

    private Integer hasModelOrMaterial;

    private String companyName;
    
    /**
     * bc.company_name like #{% + companyNameLike + %}
     * 
     * 备注: companyName字段, 在mapper.xml的操作是: bc.company_name like concat('%',#{companyName})
     * 不知道为什么只有前面加%, 咱也不敢问, 所以我另起了一个字段, 避免改了别人的代码出问题
     * 
     * add by huangsongbo
     */
    private String companyNameLike;
    
    /**
     * 查询模式
     * selectMode = 1: 加入查询条件: 商品对应的厂商下必须有品牌馆(select * from company_shop where company_id = 1003 and is_deleted = 0 and business_type = 1;
     */
    private Integer selectMode;
    
}
