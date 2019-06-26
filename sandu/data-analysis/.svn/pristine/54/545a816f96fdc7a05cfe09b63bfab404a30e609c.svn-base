package com.sandu.analysis.biz.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	/**
	 * eg:
	 * str = refpage:shoes,curpage:pages/home/A
	 * 
	 * @author huangsongbo
	 * @param str
	 * @return
	 */
	public static Map<String, String> getMap(String str) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(StringUtils.isEmpty(str)) {
			return resultMap;
		}
		
		for(String strItem : str.split(",")) {
			String[] strItems = strItem.split(":");
			if(strItems.length != 2) {
				continue;
			}
			resultMap.put(strItems[0], strItems[1]);
		}
		
		return resultMap;
	}
	
	
}
