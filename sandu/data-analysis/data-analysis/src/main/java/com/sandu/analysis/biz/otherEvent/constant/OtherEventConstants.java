package com.sandu.analysis.biz.otherEvent.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class OtherEventConstants {

	@Getter
	@AllArgsConstructor
	public static enum OTHER_EVENT_FLAG_ENUM {
		/**
		 * 产品替换渲染事件(随选网)
		 */
		PRODUCT_REPLACE("serviceEvent\treplaceProduct\twx42e6b214e6cdaed3", "product_replace"),
		/**
		 * 一键装修事件(随选网)
		 */
		ONEKEY("serviceEvent\tonekey\twx42e6b214e6cdaed3", "onekey"),
		/**
		 * 720页面-打开
		 */
		BEGIN_720("pageevent\t720begin\twx42e6b214e6cdaed3", "720_begin"),
		/**
		 * 720页面-加载完成
		 */
		FINISH_720("pageevent\t720finish\twx42e6b214e6cdaed3", "720_finish")
		;
		
		private String eventFlag;
		private String eventCode;
	}
	
	/**
	 * 需要统计的事件标识
	 * 格式为: 事件类型 + \t + 事件code + \t + 平台 + \t + 事件field(log中事件字段)
	 * eg: serviceEvent	replaceProduct	wx42e6b214e6cdaed3sd
	 */
	public final static List<String> OTHER_EVENT_FLAG_LIST = getOTHER_EVENT_FLAG_LIST();

	private static List<String> getOTHER_EVENT_FLAG_LIST() {
		List<String> list = new ArrayList<String>();
		
		// ==========添加元素(事件标识) ->start
		for(OTHER_EVENT_FLAG_ENUM enumItem : OTHER_EVENT_FLAG_ENUM.values()) {
			list.add(enumItem.getEventFlag());
		}
		// ==========添加元素(事件标识) ->end
		
		return list;
	}
	
	/**
	 * key = eventFlag, value = eventCode
	 */
	public final static Map<String, String> OTHER_EVENT_FLAG_MAP = getOTHER_EVENT_FLAG_MAP();

	private static Map<String, String> getOTHER_EVENT_FLAG_MAP() {
		Map<String, String> map = new HashMap<String, String>();
		
		for(OTHER_EVENT_FLAG_ENUM enumItem : OTHER_EVENT_FLAG_ENUM.values()) {
			map.put(enumItem.getEventFlag(), enumItem.getEventCode());
		}
		
		return map;
	}
	
}
