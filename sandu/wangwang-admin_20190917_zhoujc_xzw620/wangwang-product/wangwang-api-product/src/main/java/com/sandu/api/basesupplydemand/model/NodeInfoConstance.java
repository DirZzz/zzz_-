package com.sandu.api.basesupplydemand.model;

import java.io.Serializable;

/**
 * @author Sandu
 * @ClassName NodeInfoConstance
 * @date 2018/11/6
 */
public class NodeInfoConstance implements Serializable {
	//	收藏
	public static int DETAIL_TYPE_FAVORITE = 1;
	//;"点赞	"
	public static int DETAIL_TYPE_LIKE = 2;
	//浏览
	public static int DETAIL_TYPE_VIEW = 3;

	//评论
	public static int DETAIL_TYPE_COMMENT = 4;
	//虚拟收藏数s
	public static int DETAIL_TYPE_VIRTUAL_FAVORITE = 5;
	//虚拟点赞数
	public static int DETAIL_TYPE_VIRTUAL_LIKE = 6;
	//虚拟浏览数
	public static int DETAIL_TYPE_VIRTUAL_VIEW = 7;
	//推荐方案
	public static int NODE_TYPE_DESIGN_PLAN_RECOMMENDED = 1;
	//全屋方案
	public static int NODE_TYPE_FULL_HOUSE_DESIGN_PLAN = 2;
	//户型
	public static int NODE_TYPE_HOUSE = 3;
	//供求信息
	public static int NODE_TYPE_SUPPLY_DEMAND = 4;
	//用户评论
	public static int NODE_TYPE_USER_REVIEWS = 5;
	//店铺
	public static int NODE_TYPE_SHOP = 6;


}
