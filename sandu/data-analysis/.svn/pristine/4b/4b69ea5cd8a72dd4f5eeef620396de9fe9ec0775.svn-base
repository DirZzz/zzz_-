package com.sandu.analysis.test;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;

import com.sandu.analysis.biz.constant.AnalysisConstants;

import scala.Tuple2;

public class TestReadKeyValueRDDFile {

	@Test
	public void test() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("test1");
		sparkConf.setMaster("local");
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		javaSparkContext.textFile("C:/Users/Administrator/Desktop/hdfs/")
		.mapToPair(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return new Tuple2<String, String>(strs[5] + AnalysisConstants.SPLIT_REGEX + "other", strs[0]);
		})
		.groupByKey()
		.saveAsTextFile("C:/Users/Administrator/Desktop/results/");
	}
	
	@Test
	public void test02() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("test1");
		sparkConf.setMaster("local");
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		JavaPairRDD<String, Iterable> javaPairRDD = javaSparkContext.sequenceFile("C:/Users/Administrator/Desktop/results/", String.class, Iterable.class);
		System.out.println(javaPairRDD.collect());

	}
	
}
