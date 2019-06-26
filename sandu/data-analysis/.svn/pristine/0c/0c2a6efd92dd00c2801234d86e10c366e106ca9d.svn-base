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
	 * app.properties: hdfs.domain
	 */
	public final static String HDFS_DOMAIN = ConfigurationManager.getProperty(ConfigConstants.HDFS_DOMAIN);
	
	/**
	 * app.properties: hdfs.events.dir
	 */
	public final static String HDFS_EVENTS_DIR = ConfigurationManager.getProperty(ConfigConstants.HDFS_EVENTS_DIR);
	
	/**
	 * app.properties: hdfs.userIdentity.dir
	 * 所有用户信息目录
	 */
	public final static String HDFS_USERIDENTITY_DIR = ConfigurationManager.getProperty(ConfigConstants.HDFS_USERIDENTITY_DIR);
	
	/**
	 * app.properties: hdfs.newUserIdentity.dir
	 * 新增用户信息目录
	 */
	public final static String HDFS_NEWUSERIDENTITY_DIR = ConfigurationManager.getProperty(ConfigConstants.HDFS_NEWUSERIDENTITY_DIR);
	
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
		btnid("btnid"),
		event("event")
		;
		
		@Getter
		private String name;
	}
	
	/**
	 * 如果页面停留时间大于#{TIME_OVERTIME}, 标记为从此页面退出
	 */
	public static long TIME_OVERTIME = 10 * 60 * 1000;
	
	/**
	 * 第#{n}天留存
	 */
	public static enum USERRETENTION_DAYS_ENUM {
		oneDay, threeDay, sevenDay, thirtyDay
	}
	
}
