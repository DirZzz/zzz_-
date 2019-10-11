package com.sandu.analysis.test.offline;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;

import com.sandu.analysis.biz.constant.AnalysisConstants.NEW_LOGBEAN_TYPE_ENUM;
import com.sandu.analysis.biz.page.model.LogBean;

/**
 * 留存分析
 * 
 * @author huangsongbo
 *
 */
public class RetainedAnalysisDemo {

	/**
	 * 统计当天的新增用户list和活跃用户list
	 * 
	 * @param javaSparkContext
	 * @param filePath
	 * @param outputFilePath
	 */
	public static void newUserAndActiveUserAnalysis(JavaSparkContext javaSparkContext, String filePath,
			String outputFilePath) {

		JavaRDD<String> fileRDD = javaSparkContext.textFile(filePath).cache();

		// 新增用户统计
		JavaRDD<String> newUserIdRDD = fileRDD.filter(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			if (logBean.getEventName() != null && StringUtils.equals("btnclick", logBean.getEventName())
					&& logBean.getEventProperty() != null
					&& StringUtils.equals("btnid:pages/home/A_register", logBean.getEventProperty())) {
				return true;
			}
			return false;
		}).map(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			return "nu\t" + logBean.getUserId();
		}).distinct();
		/*JavaPairRDD<String, Iterable<String>> newUserIdRDD = fileRDD.filter(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			if (logBean.getEventName() != null && StringUtils.equals("btnclick", logBean.getEventName())
					&& logBean.getEventProperty() != null
					&& StringUtils.equals("btnid:pages/home/A_register", logBean.getEventProperty())) {
				return true;
			}
			return false;
		}).map(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			return logBean.getUserId();
		}).distinct().groupBy(str -> "newUserIdList");*/

		// 活跃用户统计
		JavaRDD<String> activeUserIdRDD = fileRDD.map(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			return "au\t" + logBean.getUserId();
		}).distinct();
		/*JavaPairRDD<String, Iterable<String>> activeUserIdRDD = fileRDD.map(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			return logBean.getUserId();
		}).distinct().groupBy(str -> "activeUserIdList");*/

		JavaRDD<String> resultRDD = newUserIdRDD.union(activeUserIdRDD);

		resultRDD.saveAsTextFile(outputFilePath);
		/*resultRDD.saveAsObjectFile(outputFilePath);*/
		/*resultRDD.saveAsHadoopFile(outputFilePath, Text.class, Iterable.class, SequenceFileOutputFormat.class);*/
		System.out.println("------done it");
	}

	/**
	 * 31天统计新增用户list+活跃用户list
	 * 
	 */
	@Test
	public void test001() {
		SparkConf conf = new SparkConf();
		conf.setMaster("local");
		conf.setAppName("newUserAndActiveUserAnalysis");
		JavaSparkContext javaSparkContext = new JavaSparkContext(conf);

		for (int index = 1; index <= 31; index++) {
			String filePath = "C:/Users/Administrator/Desktop/hdfsLocal/2019-03-" + (index < 10 ? "0" + index : index) + "/log";
			String outputFilePath = "C:/Users/Administrator/Desktop/hdfsLocal/2019-03-" + (index < 10 ? "0" + index : index) + "/userInfo";
			RetainedAnalysisDemo.newUserAndActiveUserAnalysis(javaSparkContext, filePath, outputFilePath);
		}
	}

	@Test
	public void test002() {
		SparkConf conf = new SparkConf();
		conf.setMaster("local");
		conf.setAppName("newUserAndActiveUserAnalysis");
		JavaSparkContext javaSparkContext = new JavaSparkContext(conf);

		String filePath = "C:/Users/Administrator/Desktop/hdfsLocal/2019-03-0" + 5 + "/log";
		String outputFilePath = "C:/Users/Administrator/Desktop/hdfsLocal/2019-03-0" + 5 + "/userInfo";
		newUserAndActiveUserAnalysis(javaSparkContext, filePath, outputFilePath);
	}
	
	/**
	 * 留存计算
	 */
	@Test
	public void test003() {
		LocalDate localDate = LocalDate.of(2019, 3, 1);
		// 次日
		// 3day
		// 5day
		// 7day
		// 30day
		
		SparkConf conf = new SparkConf();
		conf.setMaster("local");
		conf.setAppName("RetainedAnalysis");
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(conf);
		
		JavaRDD<String> fileRDD = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfsLocal/" + localDate.toString() + "/userInfo");
		List<String> newUserIdList = fileRDD.filter(str -> str.startsWith("nu")).map(str -> {
			String[] strs = str.split("\t");
			return strs[1];
		}).collect();
		
		JavaRDD<String> fileRDD1Day = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfsLocal/" + localDate.plusDays(1).toString() + "/userInfo");
		List<String> activeUserIdList1Day = fileRDD1Day.filter(str -> str.startsWith("au")).map(str -> {
			String[] strs = str.split("\t");
			return strs[1];
		}).collect();
		
		JavaRDD<String> fileRDD3Day = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfsLocal/" + localDate.plusDays(3).toString() + "/userInfo");
		List<String> activeUserIdList3Day = fileRDD3Day.filter(str -> str.startsWith("au")).map(str -> {
			String[] strs = str.split("\t");
			return strs[1];
		}).collect();
		
		JavaRDD<String> fileRDD7Day = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfsLocal/" + localDate.plusDays(7).toString() + "/userInfo");
		List<String> activeUserIdList7Day = fileRDD7Day.filter(str -> str.startsWith("au")).map(str -> {
			String[] strs = str.split("\t");
			return strs[1];
		}).collect();
		
		JavaRDD<String> fileRDD30Day = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfsLocal/" + localDate.plusDays(30).toString() + "/userInfo");
		List<String> activeUserIdList30Day = fileRDD30Day.filter(str -> str.startsWith("au")).map(str -> {
			String[] strs = str.split("\t");
			return strs[1];
		}).collect();
		
		List<String> retainedUserIdList1Day = new ArrayList<String>(activeUserIdList1Day);
		retainedUserIdList1Day.retainAll(newUserIdList);
		System.out.println(retainedUserIdList1Day);
		
		List<String> retainedUserIdList3Day = new ArrayList<String>(activeUserIdList3Day);
		retainedUserIdList3Day.retainAll(newUserIdList);
		System.out.println(retainedUserIdList3Day);
		
		List<String> retainedUserIdList7Day = new ArrayList<String>(activeUserIdList7Day);
		retainedUserIdList7Day.retainAll(newUserIdList);
		System.out.println(retainedUserIdList7Day);
		
		List<String> retainedUserIdList30Day = new ArrayList<String>(activeUserIdList30Day);
		retainedUserIdList30Day.retainAll(newUserIdList);
		System.out.println(retainedUserIdList30Day);
		
		System.out.println("留存数值: newUser = " + newUserIdList.size() + "人, "
				+ "1day = " + retainedUserIdList1Day.size() + "人, "
						+ "3day = " + retainedUserIdList3Day.size() + "人, "
								+ "7d = " + retainedUserIdList7Day.size() + "人, "
								+ "30day = " + retainedUserIdList30Day.size() + " 人");
		
	}
	
	@Test
	public void test004() {
		LocalDate localDate = LocalDate.of(2019, 3, 1);
		System.out.println(localDate.plusDays(1));
	}
	
}
