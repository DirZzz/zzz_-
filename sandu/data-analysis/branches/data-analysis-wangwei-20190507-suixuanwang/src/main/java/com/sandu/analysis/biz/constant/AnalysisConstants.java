package com.sandu.analysis.biz.constant;

import com.sandu.analysis.conf.ConfigurationManager;
import com.sandu.analysis.constant.ConfigConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AnalysisConstants {

	// ------spark程序运行的一些配置 ->start
	/**
	 * app.properties: spark.master
	 */
	public final static String SPARK_MASTER = ConfigurationManager.getProperty(ConfigConstants.SPARK_MASTER);
	
	/**
	 * app.properties: hdfs.dir
	 */
	public final static String HDFS_DIR = ConfigurationManager.getProperty(ConfigConstants.HDFS_DIR);
	
	/**
	 * app.properties: run.env
	 */
	public final static String RUN_ENV = ConfigurationManager.getProperty(ConfigConstants.RUN_ENV);
	// ------spark程序运行的一些配置 ->start
	
	/**
	 * hdfs初始内容切分符号
	 */
	public final static String SPLIT_REGEX = "\t";
	
	/**
	 * hdfs文件初始内容切分后的length
	 */
	public final static int LOG_SPLIT_LENGTH = 13;
	
	@AllArgsConstructor
	public static enum NEW_LOGBEAN_TYPE_ENUM {
		a(13), b(3), c(4), d(3);
		
		@Getter
		private Integer length;
		
	}
	
	public static enum LOGBEAN_GETSTRING_TYPE_ENUM {
		a, b
	}
	
	/**
	 * log 中 event name种类
	 * 
	 * @author huangsongbo
	 *
	 */
	@AllArgsConstructor
	public static enum EVENTNAME_ENUM {
		/**
		 * 页面访问事件
		 */
		pageview("pageview"), 
		/**
		 * 按钮点击事件
		 */
		btnclick("btnclick")
		;
		
		@Getter
		private String name;
	}
	
	@AllArgsConstructor
	public static enum EVENTPROPERTY_ENUM {
		/**
		 * 当前页面标识
		 */
		curpage("curpage"),
		/**
		 * 点击按钮标识
		 */
		btnid("btnid")
		;
		
		@Getter
		private String name;
	}
	
}
