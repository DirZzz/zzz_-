package com.sandu.analysis.test.offline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTNAME_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.util.Utils;

/**
 * 
 * @author huangsongbo 2019.07.09
 *
 */
public class EventAnalysisTest {

	/**
	 * 指定事件分析, 分析pv/uv, 当天新增用户对应pv/uv
	 * 
	 * @author huangsongbo 2019.07.09
	 * @throws IOException 
	 */
	@Test
	public void test001() throws IOException {
		List<String> logList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/log.txt"), Charsets.UTF_8);
		List<String> newUserUuidList = Files.readLines(new File("C:/Users/Administrator/Desktop/新建文本文档 (2).txt"), Charsets.UTF_8);
		/*List<String> eventList = new ArrayList<String>(Arrays.asList("pages/plan/case-house-type/case-house-type;clickSearchHouse", "pages/three-house/three-house;startOffer"));*/
		List<String> eventList = new ArrayList<String>(Arrays.asList(
				"	curpage:pages/home/home	", 
				"	curpage:pages/designMyhome/designMyhome	",
				"	btnid:clickSearchHouse,curpage:pages/plan/case-house-type/case-house-type	",
				"btnid:startOffer,curpage:pages/three-house/three-house"));
		Map<String, List<Object>> result = new HashMap<String, List<Object>>();
		
		int count = 0;
		
		for (String log : logList) {
			// 0		1								2								3				4															5			6	7		8					9					10		11			12
			// 123	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	refpage:shoes,curpage:pages/home/A	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
			String[] strs = log.split(AnalysisConstants.SPLIT_REGEX);
			Map<String, String> eventMap = Utils.getMap(strs[4]);
			/*String key =  StringUtils.equals(EVENTNAME_ENUM.pageview.getName(), strs[3]) ? 
							eventMap.get(EVENTPROPERTY_ENUM.curpage.getName())
							: eventMap.get(EVENTPROPERTY_ENUM.curpage.getName()) + ";" + eventMap.get(EVENTPROPERTY_ENUM.btnid.getName());*/
			if (
					/*strs[5].equals("wx42e6b214e6cdaed3")*/
					log.indexOf("wx42e6b214e6cdaed3") != -1
					/*&& eventList.contains(key)*/
					&& (
							/*key.equals("pages/plan/case-house-type/case-house-type;clickSearchHouse")*/
							log.indexOf("	curpage:pages/home/home	") != -1
							|| log.indexOf("	curpage:pages/designMyhome/designMyhome	") != -1
							|| log.indexOf("	btnid:clickSearchHouse,curpage:pages/plan/case-house-type/case-house-type	") != -1
							|| log.indexOf("btnid:startOffer,curpage:pages/three-house/three-house	") != -1
							)
					) {
				String key = null;
				if (log.indexOf("	curpage:pages/home/home	") != -1) {
					key = "	curpage:pages/home/home	";
				} else if (log.indexOf("	curpage:pages/designMyhome/designMyhome	") != -1) {
					key = "	curpage:pages/designMyhome/designMyhome	";
				} else if (log.indexOf("	btnid:clickSearchHouse,curpage:pages/plan/case-house-type/case-house-type	") != -1) {
					key = "	btnid:clickSearchHouse,curpage:pages/plan/case-house-type/case-house-type	";
				} else if (log.indexOf("btnid:startOffer,curpage:pages/three-house/three-house") != -1) {
					key = "btnid:startOffer,curpage:pages/three-house/three-house";
				}
				
				/*System.out.println(key);*/
				
				List<Object> itemValue = null;
				if (result.containsKey(key)) {
					itemValue = result.get(key);
				} else {
					// 1 = pv, 2 = user set, 3 = new user pv, 4 = new user set
					itemValue = new ArrayList<Object>(Arrays.asList(0, new HashSet<String>(), 0, new HashSet<String>()));
					result.put(key, itemValue);
				}
				
				/*if (log.indexOf("btnid:startOffer,curpage:pages/three-house/three-house") != -1) {
					System.out.println(log);
					count ++;
				}*/
				
				itemValue.set(0, (Integer) itemValue.get(0) + 1);
				@SuppressWarnings("unchecked")
				Set<String> set = (Set<String>) itemValue.get(1);
				set.add(strs[0]);
				
				if (newUserUuidList.contains(strs[0])) {
					itemValue.set(2, (Integer) itemValue.get(2) + 1);
					@SuppressWarnings("unchecked")
					Set<String> set2 = (Set<String>) itemValue.get(3);
					set2.add(strs[0]);
				}
				
			}
		}
		
		System.out.println(result);
		
		for (List<Object> objList : result.values()) {
			@SuppressWarnings("unchecked")
			Set<String> set = (Set<String>) objList.get(1);
			objList.set(1, set.size());
			@SuppressWarnings("unchecked")
			Set<String> set2 = (Set<String>) objList.get(3);
			objList.set(3, set2.size());
		}
		
		System.out.println(result);
		
		System.out.println(count);
		
	}
	
	@Test
	public void test002() throws IOException {
		List<String> logList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/log.txt"), Charsets.UTF_8);
		int count = 0;
		for (String log : logList) {
			if (log.indexOf("btnid:startOffer,curpage:pages/three-house/three-house") != -1
					&& log.indexOf("wx42e6b214e6cdaed3") != -1) {
				count ++;
			}
		}
		
		System.out.println(count);
		
	}
	
}
