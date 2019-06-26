package com.sandu.analysis.biz.tob.house.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseEventConstants {

	public final static String HOUSE_DAY_TYPE = "day";
	public final static String HOUSE_HOUR_TYPE = "hour";

	@Getter
	@AllArgsConstructor
	public enum HouseEventFlagEnum {
		/**
		 * 户型使用---自己装修/一键装修事件(pc2b)
		 */
		SELF_DECORATE("serviceEvent\thouseUse\tpc2b", "pcHouseUse"),
		/**
		 * 户型使用---装修我家/一键装修事件(移动端)
		 */
		ONEKEY_DECORATE("serviceEvent\thouseUse\tmobile2b", "mobileHouseUse")
		;
		
		private String eventFlag;
		private String eventCode;
	}
	
	/**
	 * 需要统计的事件标识
	 * 格式为: 事件类型 + \t + 事件code + \t + 平台
	 * eg: serviceEvent	selfDecorate	pc2b
	 */
	public final static List<String> HOUSE_EVENT_FLAG_LIST = getHouseEventFlagList();

	private static List<String> getHouseEventFlagList() {
		List<String> list = new ArrayList<String>();
		
		// ==========添加元素(事件标识) ->start
		for(HouseEventFlagEnum enumItem : HouseEventFlagEnum.values()) {
			list.add(enumItem.getEventFlag());
		}
		// ==========添加元素(事件标识) ->end
		
		return list;
	}
	
	/**
	 * key = eventFlag, value = eventCode
	 */
	public final static Map<String, String> HOUSE_EVENT_FLAG_MAP = getOTHER_EVENT_FLAG_MAP();

	private static Map<String, String> getOTHER_EVENT_FLAG_MAP() {
		Map<String, String> map = new HashMap<String, String>();
		
		for(HouseEventFlagEnum enumItem : HouseEventFlagEnum.values()) {
			map.put(enumItem.getEventFlag(), enumItem.getEventCode());
		}
		
		return map;
	}
	
}
