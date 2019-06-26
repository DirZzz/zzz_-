package com.sandu.analysis.test;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class WordCount {

	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster("local");
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		// 123	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	curpage:pages/home/D	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> fileRDD = javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfs/");
		JavaRDD<String> javaRDD = fileRDD.flatMap(str -> Arrays.asList(str.split(",")).iterator());
		System.out.println(javaRDD.countByValue());
	}

	
}
