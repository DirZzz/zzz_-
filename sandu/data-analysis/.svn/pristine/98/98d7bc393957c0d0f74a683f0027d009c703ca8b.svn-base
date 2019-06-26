package com.sandu.analysis.test.offline;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants.LOGBEAN_GETSTRING_TYPE_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.NEW_LOGBEAN_TYPE_ENUM;
import com.sandu.analysis.biz.page.model.LogBean;

import scala.Tuple2;

/**
 * 统计lastPage/timeonPage/pv/uv/AVGtimeOnPage
 * 
 * @author huangsongbo
 *
 */
public class PageAnalysisDemo {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		/*String logPrefix = "[PageAnalysis.main]: ";*/
		
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		// 123	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	curpage:pages/home/D	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> fileRDD = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfs/");
		JavaPairRDD<String, Iterable<String>> javaPairRDD = fileRDD.filter(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			if(logBean.getUserId() == null || !StringUtils.equals("pageview", logBean.getEventName())) {
				return false;
			} else {
				return true;
			}
		})
		// key = "123", value = "2019-04-09 17:18:49	pages/home/D"
		.mapToPair(str -> {
			LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.a);
			return new Tuple2<String, String>(logBean.getUserId(), logBean.getString(LOGBEAN_GETSTRING_TYPE_ENUM.a));
		})
		// key = "123", value = ["2019-04-09 17:18:49	pages/home/D", "2019-04-09 17:18:50		pages/home/A" ...]
		.groupByKey()
		.mapValues(strIterable -> {
			/*List<LogBean> logBeanList = Lists.newArrayList(strIterable.iterator()).stream().map((String i) -> new LogBean(i, NEW_LOGBEAN_TYPE_ENUM.b)).collect(Collectors.toList());*/
			@SuppressWarnings("unchecked")
			List<String> strList = IteratorUtils.toList(strIterable.iterator());
			List<LogBean> logBeanList = strList.stream().map(i -> new LogBean(i, NEW_LOGBEAN_TYPE_ENUM.b)).collect(Collectors.toList());
			
			LogBean logBeanStart = null;
			for(LogBean logBean : logBeanList) {
				if (logBeanStart == null) {
					logBeanStart = logBean;
				} else {
					logBean.setLastPage(logBeanStart.getCurrentPage());
					logBeanStart.setTimeOnPage(logBean.getCurrentTime() - logBeanStart.getCurrentTime());
					logBeanStart = logBean;
				}
			}
			
			strIterable = logBeanList.stream().map(i -> i.getString(LOGBEAN_GETSTRING_TYPE_ENUM.b)).collect(Collectors.toList());
			
			return strIterable;
		});
		
		JavaRDD<String> javaRDD = javaPairRDD.flatMap(r -> r._2.iterator())/*.cache()*/;
		
		System.out.println("------javaRDD spend: " + (System.currentTimeMillis() - startTime));
		
		// pv key = lastPage + currentPage
		JavaPairRDD<String, Integer> javaPairRDDPV = javaRDD
				.mapToPair(str -> {
					LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.c);
					return new Tuple2<String, Integer>(logBean.getLastPage() + "\t" + logBean.getCurrentPage(), 1);
				})
				.reduceByKey((i1, i2) -> i1 + i2)/*.cache()*/;
		
		// uv key = userId + lastPage + currentPage
		JavaPairRDD<String, Integer> javaPairRDDUV = javaRDD
				.map(str -> {
					LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.c);
					return logBean.getLastPage() + "\t" + logBean.getCurrentPage() + "\t" + logBean.getUserId();
				})
				.distinct()
				.mapToPair(str -> {
					LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.d);
					return new Tuple2<String, Integer>(logBean.getLastPage() + "\t" + logBean.getCurrentPage(), 1);
				})
				.reduceByKey((i1, i2) -> i1 + i2)/*.cache()*/;
		
		// averageTimeOnPage key = lastPage + currentPage
		JavaPairRDD<String, Long> javaPairRDDATOP = javaRDD
				.mapToPair(str -> {
					LogBean logBean = new LogBean(str, NEW_LOGBEAN_TYPE_ENUM.c);
					return new Tuple2<String, Long>(logBean.getLastPage() + "\t" + logBean.getCurrentPage(), logBean.getTimeOnPage());
				})
				.combineByKey(
						l -> new Tuple2<Long, Long>(l, 1L), 
						(t, l) -> new Tuple2<Long, Long>(t._1 + (l == null ? 0 : l), t._2 + (l == null ? 0 : 1)),
						(t1, t2) -> new Tuple2<Long, Long>(t1._1 + t2._1, t1._2 + t2._2)
						)
				.mapValues(t -> {
					return t._1/t._2;
				})/*.cache()*/;
		
		JavaPairRDD<String, String> javaPairRDDResult = javaPairRDDPV.join(javaPairRDDUV).mapValues(t -> t._1 + "\t" + t._2).join(javaPairRDDATOP).mapValues(t -> t._1 + "\t" + t._2);
		
		javaPairRDDResult.saveAsTextFile("C:/Users/Administrator/Desktop/result");
		System.out.println("------spend: " + (System.currentTimeMillis() - startTime));
	}
	
}
