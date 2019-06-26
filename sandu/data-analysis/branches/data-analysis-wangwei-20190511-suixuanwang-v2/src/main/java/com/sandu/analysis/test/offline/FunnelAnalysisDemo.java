package com.sandu.analysis.test.offline;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;

import com.sandu.analysis.biz.constant.AnalysisConstants;

import scala.Tuple2;

public class FunnelAnalysisDemo {

	public static void main(String[] args) {
		
		// 漏斗模型list
		List<String> pageList = Arrays.asList("pages/home/D", "pages/home/A", "pages/home/B");
		
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("FunnelAnalysis");
		sparkConf.setMaster("local");
		/*sparkConf.setMaster("yarn-cluster");*/
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		// 0		1								2								3				4									5			6	7		8					9					10		11			12
		// 123	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	curpage:pages/home/D	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> fileRDD = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfs/");
		JavaPairRDD<String, String> javaPairRDD = fileRDD.filter(str -> {
			if(StringUtils.isEmpty(str)) {
				return false;
			}
			
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if(strs.length != AnalysisConstants.LOG_SPLIT_LENGTH) {
				return false;
			}
			
			String eventName = strs[3];
			if(!StringUtils.equals("pageview", eventName)) {
				return false;
			}
			
			String eventProperty = strs[4];
			String currentPage = eventProperty.replace("curpage:", "");
			if(!pageList.contains(currentPage)) {
				return false;
			}
			
			return true;
		}).map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			String userId = strs[0];
			String eventProperty = strs[4];
			String currentPage = eventProperty.replace("curpage:", "");
			int pageFlag = pageList.indexOf(currentPage);
			return userId + "\t" + pageFlag;
		})
		/*.groupBy(str -> str.split("\t")[0]);*/
		.mapToPair(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return new Tuple2<String, String>(strs[0], strs[1]);
		})
		.groupByKey()
		.mapValues(iter -> {
			// eg: 漏斗路径是: pages/home/D, pages/home/A, pages/home/B
			// 只访问了pages/home/D, funnelDeepNum = 1
			// 访问了pages/home/D + pages/home/A, funnelDeepNum = 2
			// 访问了pages/home/D + pages/home/A + pages/home/B, funnelDeepNum = 3
			int funnelDeepNum = 0;
			int pageCheckIndex = 0;
			Iterator<String> Iterator = iter.iterator();
			while (Iterator.hasNext()) {
				String str = Iterator.next();
				if (StringUtils.equals(pageCheckIndex + "", str)) {
					funnelDeepNum ++;
					pageCheckIndex ++;
					if (pageCheckIndex >= pageList.size()) {
						break;
					}
				}
			}
			return funnelDeepNum + "";
		});
		
		Map<String, Long> map = javaPairRDD.values().countByValue();
		List<Long> resultList = Arrays.asList(0L, 0L, 0L);
		for(String key : map.keySet()) {
			long value = map.get(key);
			long index = Long.parseLong(key) - 1;
			for (long i = index; i >= 0; i--) {
				resultList.set((int) i, resultList.get((int) i) + value);
			}
		}
		
		System.out.println(resultList);
		/*javaPairRDD.saveAsTextFile("C:/Users/Administrator/Desktop/result");*/
	}
	
	@Test
	public void test() {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("1", 12L);
		map.put("2", 6L);
		map.put("3", 3L);
		List<Long> resultList = Arrays.asList(0L, 0L, 0L);
		for(String key : map.keySet()) {
			long value = map.get(key);
			long index = Long.parseLong(key) - 1;
			for (long i = index; i >= 0; i--) {
				resultList.set((int) i, resultList.get((int) i) + value);
			}
		}
		System.out.println(resultList);
	}
	
}
