package com.sandu.search.common.constant;

/**
 * 组合产品状态常量
 *
 * @author xiaoxc
 * @data 2019/3/20 0020.
 */
public class GroupProductConstant {

    /**************** 组合状态 ****************/

    //组合状态--上架
    public static final int GROUP_STATE_PUTAWAY = 1;
    //组合状态--测试中
    public static final int GROUP_STATE_TEST = 2;
    //组合状态--发布
    public static final int GROUP_STATE_RELEASE = 3;

    /**************** 组合类型 *****************/

    //普通组合
    public static final int GROUP_TYPE_COMMON = 0;
    //一建装修组合
    public static final int GROUP_TYPE_INTELLIGENCE = 1;

    //搜索组合类型 1: 搜全部;(0/null): 搜本类
    public static final int GROUP_TYPE_SEARCH_FULL = 1;

    public static final int GROUP_TYPE_SEARCH_THIS = 0;

    /**************** 组合收藏状态 *****************/

    //组合已收藏1
    public static final int GROUP_COLLECT_HAS_STATE = 1;
    //组合未收藏0
    public static final int GROUP_COLLECT_NOT_STATE = 0;

    /**************** 组合模块功能类型 *****************/

    //搜索组合列表
    public static final String FUNCT_TYPE_SEARCH_GROUP_LIST = "search";
    //收藏组合列表
    public static final String FUNCT_TYPE_COLLECT_GROUP_LIST = "collect";



}
