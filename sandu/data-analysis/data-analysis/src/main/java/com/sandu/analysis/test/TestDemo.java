package com.sandu.analysis.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTNAME_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.funnel.dao.FunnelDao;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelAnalysisResultQuery;
import com.sandu.analysis.biz.funnel.offline.FunnelAnalysis;
import com.sandu.analysis.biz.util.Utils;

import scala.Tuple2;

public class TestDemo {

	@Test
	public void test001() {
		Date date = new Date();
		Set<BigdataFunnelAnalysisResultQuery> set = new HashSet<BigdataFunnelAnalysisResultQuery>();
		for (int index = 0; index < 5; index++) {
			BigdataFunnelAnalysisResultQuery bigdataFunnelAnalyzeResultQuery = new BigdataFunnelAnalysisResultQuery();
			bigdataFunnelAnalyzeResultQuery.setFunnelId(1L);
			bigdataFunnelAnalyzeResultQuery.setEndTime(date);
			bigdataFunnelAnalyzeResultQuery.setStartTime(date);
			set.add(bigdataFunnelAnalyzeResultQuery);
		}
		
		System.out.println(set.size());
		
	}
	
	@Test
	public void test002() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = simpleDateFormat.parse("2019-04-19 00:00:00");
		Date date2 = simpleDateFormat.parse("2019-04-20 00:00:00");
		
		BigdataFunnelAnalysisResultQuery bigdataFunnelAnalyzeResultQuery = new BigdataFunnelAnalysisResultQuery();
		bigdataFunnelAnalyzeResultQuery.setFunnelId(0L);
		bigdataFunnelAnalyzeResultQuery.setEndTime(date2);
		bigdataFunnelAnalyzeResultQuery.setStartTime(date);
		
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		List<Long> idList = funnelDao.select(bigdataFunnelAnalyzeResultQuery);
		System.out.println(idList);
	}

	@Test
	public void test003() {
		String[] args = new String[] {"20190422"};
		System.out.println(FunnelAnalysis.getDataDirInfo(args));
	}
	
	@Test
	public void test004() {
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		System.out.println(funnelDao.selectFromBigdataFunnelDetailBOwhereEffective());
	}
	
	@Test
	public void test005() {
		Integer[] ints = new Integer[6];
		Arrays.fill(ints, 0);
		List<Integer> list = Arrays.asList(ints);
		System.out.println(list);
	}
	
	@Test
	public void test006() {
		LocalDate localDate = Utils.getLocalDate(null);
		System.out.println(localDate);
		Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
	}
	
	@Test
	public void test007() throws URISyntaxException {
		URI uri = new URI("hdfs://192.168.1.240:8020/test/home");
		System.out.println(uri.getPath());
		System.out.println(uri.getHost());
		System.out.println(uri.toString());
	}
	
	@Test
	public void test008() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(Arrays.asList(1, 2, 4, 5, 7, 2), 2);
		System.out.println(javaRDD.glom().collect());
	}
	
	@Test
	public void test009() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		/*JavaRDD<String> javaRDD = */
		/*Map<String, Long> map = */
		JavaPairRDD<String, Integer> rdd = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfs")		/*System.out.println(javaRDD.collect())*/
		.flatMap(str -> {
			return Arrays.asList(str.split(","));
		}).mapToPair(str -> new Tuple2<>(str, 1))
		.reduceByKey((i1, i2) -> i1 + i2);
		/*.countByValue();*/
		System.out.println(rdd.collect());
		
		
	}
	
	@Test
	public void test010() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		/*JavaRDD<Integer> javaRDD = */
		/*JavaRDD<Integer> rdd =*/
		JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(Arrays.asList(1, 3, 5, 6, 4, 5), 3);
		System.out.println(javaRDD.glom().collect());
		/*.filter(integer -> integer <5);
		System.out.println(rdd.collect());*/
		/*System.out.println(javaRDD.map(i -> i + 1).collect());*/
		/*System.out.println(javaRDD.collect());*/
	}
	
	/*public void test*/
	
	@Test
	public void test011() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		// 0		1								2								3				4															5			6	7		8					9					10		11			12
		// uuid	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	refpage:shoes,curpage:pages/home/A	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> rdd = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfs")
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			// log 格式过滤
			if(AnalysisConstants.LOG_SPLIT_LENGTH != strs.length) {
				return false;
			}
			// 只要pageview类型的事件
			if(!StringUtils.equals(strs[3], EVENTNAME_ENUM.pageview.getName())) {
				return false;
			}
			return true;
		})
		// 0		1
		// uuid, page
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			Map<String, String> eventMap = Utils.getMap(strs[4]);
			String event = eventMap.get(EVENTPROPERTY_ENUM.curpage.getName());
			return strs[0] + AnalysisConstants.SPLIT_REGEX + event;
		}).cache()
		;
		
		rdd.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1];
		}).countByValue();
		
		Map<String, Long> map = rdd.distinct().map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1];
		}).countByValue();
		
		System.out.println(map);
	}
	
	@Test
	public void test012() throws IOException {
		List<String> strList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/log.txt"), Charset.forName("utf-8"));
		List<String> list1 = new ArrayList<String>();
		// 0		1								2								3				4															5			6	7		8					9					10		11			12
		// uuid	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	refpage:shoes,curpage:pages/home/A	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		for (String str : strList) {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (strs[4].equals("curpage:pages/home/home")) {
				list1.add(strs[0]);
			}
		}
		System.out.println(list1.size());
		System.out.println(new HashSet<String>(list1).size());
	}
	
	/**
	 * 测试用户新增/活跃统计
	 * @throws IOException 
	 */
	@Test
	public void test013() throws IOException {
		List<String> strList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/log.txt"), Charset.forName("utf-8"));
		List<String> list1 = new ArrayList<String>();
		for (String str : strList) {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			list1.add(strs[0]);
		}
		Set<String> set = new HashSet<String>(list1);
		System.out.println(set.size());
		List<String> userUuidList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/newUser.txt"), Charset.forName("utf-8"));
		Set<String> set2 = new HashSet<String>();
		for (String str : userUuidList) {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			set2.add(strs[0]);
		}
		set.retainAll(set2);
		System.out.println(set.size());
		
		int i = 0;
		for (String str : strList) {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (strs[3].equals("pageview") && set.contains(strs[0])) {
				i ++;
			}
		}
		
		/*System.out.println(i);*/
		System.out.println(i / set.size());
	}
	
	@Test
	public void test014() throws IOException {
		List<String> userUuidList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/newUser.txt"), Charset.forName("utf-8"));
		List<String> strList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/log.txt"), Charset.forName("utf-8"));
		Set<String> set = new HashSet<String>();
		for (String str : strList) {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if (userUuidList.contains(strs[0])) {
				set.add(strs[0]);
			}
		}
		System.out.println(set.size());
	}
	
	@Test
	public void test0015() throws IOException {
		List<String> strList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/log.txt"), Charset.forName("utf-8"));
		List<String> uuidList = new ArrayList<String>(Arrays.asList("c4fa6857bb3f11e8b1d5f403434d7108"));
		strList.forEach(item -> {
			String[] strs = item.split(AnalysisConstants.SPLIT_REGEX);
			if(uuidList.contains(strs[0])) {
				System.out.println(item);
			}
		});
	}
	
	@Test
	public void test0016 () {
		LocalDate nowDate = LocalDate.parse("2019-05-31");
		System.out.println(nowDate);
	}
	
	@Test
	public void test0017 () throws IOException {
		List<String> strList = Files.readLines(new File("C:/Users/Administrator/Desktop/hdfs/log.txt"), Charset.forName("utf-8"));
		
		Set<String> uuidList = new HashSet<String>();
		
		Set<String> uuidList2 = new HashSet<String>();
		
		int count = 0;
		
		for (String str : strList) {
			if (str.contains("wxf1113bc672fe7112")) {
				uuidList.add(str.split(AnalysisConstants.SPLIT_REGEX)[0]);
				count ++;
				
				if (str.contains("pageview	curpage:pages/index/index")) {
					uuidList2.add(str.split(AnalysisConstants.SPLIT_REGEX)[0]);
				}
				
			}
		}
		
		System.out.println(uuidList.size());
		
		System.out.println(uuidList2.size());
		
		uuidList.removeAll(uuidList2);
		
		for (String str : uuidList) {
			System.out.println(str);
		}
		
	}
	
}
