package com.sandu.analysis.test.offline;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.SparkSession;

import com.sandu.analysis.test.model.LogBean;

/**
 * 
 * @author huangsongbo
 *
 */
public class DataSetTest {

	/**
	 * 失败, dataset的api都是类似于sql的api, 我决定还是用rdd实现业务
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SparkSession spark = SparkSession.builder().appName("Simple Application").master("local").getOrCreate();
		Dataset<String> dataset = spark.read().textFile("C:/Users/Administrator/Desktop/hdfs/");
		Dataset<LogBean> resultDataSet = dataset.map(str -> {
			String[] strs = str.split("\t");
			
			LogBean logBean = new LogBean();
			logBean.setUserId(strs[0]);
			logBean.setEventProperty(strs[3]);
			return logBean;
		}, Encoders.bean(LogBean.class));
		
		resultDataSet.show();
		
		/*RelationalGroupedDataset relationalGroupedDataset = resultDataSet.groupBy("eventProperty");*/
		/*relationalGroupedDataset.*/
	}
	
}
