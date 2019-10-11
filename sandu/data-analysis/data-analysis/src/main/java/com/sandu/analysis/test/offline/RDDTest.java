package com.sandu.analysis.test.offline;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;

import com.sandu.analysis.biz.constant.AnalysisConstants;

import scala.Tuple2;

public class RDDTest {

	@Test
	public void test() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("FunnelAnalysis");
		sparkConf.setMaster("local");
		/*sparkConf.setMaster("yarn-cluster");*/
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		List<Integer> data = Arrays.asList(5, 1, 1, 4, 4, 2, 2);
		JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(data,5);
		JavaRDD<String> javaRDD1 = javaRDD.map(integer -> integer + "");
		/*String result = javaRDD1.treeReduce(new Function2<String, String, String>() {    
		    @Override    
		    public String call(String v1, String v2) throws Exception {        
		      System.out.println(v1 + "=" + v2);        
		      return v1 + "=" + v2;    
		  }
		});*/
		String result = javaRDD1.treeReduce((a, b) -> a + "=" + b);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + result);
		
		/*List<String> list = Arrays.asList("a", "b", "c", "a", "b", "a");
		JavaRDD<String> javaRDD = javaSparkContext.parallelize(list);
		
		Map<String, Long> result =  javaRDD.countByValue();
		System.out.println(result);
		JavaPairRDD<String, Integer> javaPairRDD = javaRDD.mapToPair(str -> new Tuple2<String, Integer>(str, 1));
		System.out.println(javaPairRDD.lookup("a"));*/
		
		/*JavaPairRDD<String, Integer> pairRDD2 = javaPairRDD.mapValues(integer -> integer + 1);
		System.out.println(pairRDD2.collect());*/
		
		/*JavaPairRDD<String, Iterable<Integer>> pairRdd = javaPairRDD.groupByKey();
		JavaRDD<Integer> rdd2 = pairRdd.flatMap(f -> f._2.iterator());
		System.out.println(rdd2.collect());*/
		
		/*JavaPairRDD<String, Integer> pairRDD2 = javaPairRDD.reduceByKey((a, b) -> a + b);
		System.out.println(pairRDD2.collect());*/
		
		/*List<Tuple2<String, Integer>> list = Arrays.asList(new Tuple2<String, Integer>("a", 54), new Tuple2<String, Integer>("a", 34), new Tuple2<String, Integer>("b", 43), new Tuple2<String, Integer>("b", 64), new Tuple2<String, Integer>("b", 23));
		JavaPairRDD<String, Integer> pairRDD = javaSparkContext.parallelizePairs(list);
		JavaPairRDD<String, Tuple2<Integer, Integer>> pairRDD2 = pairRDD.combineByKey(
				integer -> new Tuple2<Integer, Integer>(integer, 1),
				(t, integer) -> new Tuple2<Integer, Integer>(t._1 + integer, t._2 + 1),
				(t1, t2) -> new Tuple2<Integer, Integer>(t1._1 + t2._1, t1._2 + t2._2)
				);
		System.out.println(pairRDD2.collect());*/
		
		/*List<Tuple2<String, Integer>> list1= Arrays.asList(new Tuple2<String, Integer>("a", 1), new Tuple2<String, Integer>("a", 1));
		JavaPairRDD<String, Integer> pairRDD1 = javaSparkContext.parallelizePairs(list1);
		List<Tuple2<String, Integer>> list2= Arrays.asList(new Tuple2<String, Integer>("b", 1), new Tuple2<String, Integer>("a", 1));
		JavaPairRDD<String, Integer> pairRDD2 = javaSparkContext.parallelizePairs(list2);
		JavaPairRDD<String, Tuple2<Integer, Integer>> result = pairRDD1.join(pairRDD2);
		JavaPairRDD<String, Tuple2<Integer, Integer>> result = pairRDD1.union(pairRDD2);
		System.out.println(result.collect());*/
		
		/*List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		JavaRDD<Integer> rdd = javaSparkContext.parallelize(list, 2);
		List<Integer> result = rdd.collect();*/
		/*Integer result = rdd.aggregate(0, (a, b) -> a + b, (a, b) -> a + b);
		System.out.println(result);*/
		
		/*List<Integer> data = Arrays.asList(1, 2, 4, 3, 5, 6, 7);
		JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(data);
		JavaPairRDD<Integer,Integer> cartesianRDD = javaRDD.cartesian(javaRDD);
		System.out.println(cartesianRDD.collect());*/
	}
	
	@Test
	public void test002() {
		// ==========spark上下文初始化 ->start
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
		System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		// ==========spark上下文初始化 ->end
		
		Map<String, Long> map = javaSparkContext.textFile("C:/Users/Administrator/Desktop/results/newUserIdentity/201905/08/")
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2];
		}).countByValue();
		
		System.out.println(map);
	}
	
	@Test
	public void test003() {
		// ==========spark上下文初始化 ->start
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("PageAnalysis");
		sparkConf.setMaster("local");
		System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		// ==========spark上下文初始化 ->end
		
		List<String> strList = javaSparkContext.textFile("C:/Users/Administrator/Desktop/results/newUserIdentity/")
		.distinct().collect();
		
		System.out.println(strList);
	}
	
}
