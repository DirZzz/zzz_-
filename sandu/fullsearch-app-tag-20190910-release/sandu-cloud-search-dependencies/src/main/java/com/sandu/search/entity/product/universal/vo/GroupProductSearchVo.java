package com.sandu.search.entity.product.universal.vo;

import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 组合搜索条件VO
 *
 * @author xiaoxc
 * @data 2019/3/20 0020.
 */
@Data
public class GroupProductSearchVo implements Serializable {

    private static final long serialVersionUID = -2095202831016307317L;

    /**
     * 关键字（组合名称,组合编码,品牌名称）
     */
    private String searchKeyword;
    /**
     * 分类编码
     */
    private String categoryCode;
    /**
     * 产品大分类value
     */
    private Integer productTypeValue;
    /**
     * 产品小分类集合
     */
    private List<Integer> productSmallTypeList;
    /**
     * 空间面积
     */
    private Integer spaceAreas;
    /**
     * 搜索类型[1: 搜全部; (0/null): 搜本类
     */
    private Integer searchType;
    /**
     * 我的组合搜索则传用户Id
     */
    private Integer myUserId;
    /**
     * 上架状态(0:未上架 1:上架 2:测试中 3:发布 4:下架)
     */
    private List<Integer> stateList;
    /**
     * 组合类型
     */
    private Integer compositeType;
    /**
     * 组合类型(0:普通组合;1:一件装修组合)
     */
    private Integer groupType;
    /**
     * 品牌Ids
     */
    private List<Integer> brandIdList;
    /**
     * 起始页数
     */
    private Integer start;
    /**
     * 每页显示的数量
     */
    private Integer limit;

    /**
     * 单值匹配多字段--MultiMatchField.matchFieldList--List中第一个字段优先级最高
     */
    private List<MultiMatchField> multiMatchFieldList;

    /**
     * 搜索分类长编码
     */
    private String categoryLongCode;

    /**
     * 公司产品可见范围小类ID列表
     */
    private List<Integer> companyProductVisibilityRangeIdList;

    /**
     * 公司产品可见范围大类 + 小类ID集合
     */
    private Map<Integer, List<Integer>> CompanyAliveTypeMap;

}
