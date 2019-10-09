package com.sandu.api.solution.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典相关常量
 * 
 * @author huangsongbo
 *
 */
public class SysDictionaryConstants {
	
	/**
	 * <p>eg:
	 * <p>key = 3, value = "客餐厅"
	 * <p>key = 4, value = "卧室"
	 */
	public final static Map<Integer, String> SPACE_TYPE_MAP = getSPACE_TYPE_MAP();

	private static Map<Integer, String> getSPACE_TYPE_MAP() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(10, "阳台");
		map.put(11, "入户花园");
		map.put(12, "天井");
		map.put(13, "全屋");
		map.put(1, "客厅");
		map.put(2, "餐厅");
		map.put(3, "客餐厅");
		map.put(4, "卧室");
		map.put(5, "厨房");
		map.put(6, "卫生间");
		map.put(7, "书房");
		map.put(8, "衣帽间");
		map.put(9, "其他");
		return map;
	}
	
}
