package com.sandu.analysis.biz.usersRetention.offline;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTNAME_ENUM;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.user.dao.UserDao;
import com.sandu.analysis.biz.util.DBUtils;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;

import scala.Tuple2;

public class UserAnalysis {
	
	private static String className = "UserAnalysis";
	
	/**
	 * 步骤一: 统计出当天的新用户list + 所有用户list
	 * 步骤二: 存放到对应的hdfs目录
	 * 
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		// ==========spark上下文初始化 ->start
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("UserAnalysis");
		sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
		System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		// ==========spark上下文初始化 ->end
		
		// ==========获取与要处理的hdfs文件路径 ->start
		LocalDate localDate = Utils.getLocalDate(args);
		String dataDirInfo = localDate.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
		
		String hdfsFileDir = null;
		String hdfsUserIdentityDir = null;
		String hdfsNewUserIdentityDir = null;
		if(ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV)) {
			hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_EVENTS_DIR;
			hdfsUserIdentityDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_USERIDENTITY_DIR;
			hdfsNewUserIdentityDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR;
		} else {
			hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_EVENTS_DIR + dataDirInfo;
			// 检测hdfs中有没有这个目录, 没有代表今天没日志, 直接return
			boolean isExist = HadoopUtils.getIsExist(hdfsFileDir);
			if(!isExist) {
				System.out.println("warn, function = PageAnalysis.main, message = hdfs中对应目录不存在, dir = " + hdfsFileDir);
				return;
			}
			
			// 检测输出类目录存不存在, 存在则删除(视为至少已经计算过一次数据)
			hdfsUserIdentityDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_USERIDENTITY_DIR + dataDirInfo;
			hdfsNewUserIdentityDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR + dataDirInfo;
			
			HadoopUtils.deleteHdfsDir(hdfsUserIdentityDir);
			HadoopUtils.deleteHdfsDir(hdfsNewUserIdentityDir);
		}
		System.out.println("==========hdfsFileDir = " + hdfsFileDir);
		System.out.println("==========hdfsUserIdentityDir = " + hdfsUserIdentityDir);
		System.out.println("==========hdfsUserIdentityDir = " + hdfsUserIdentityDir);
		// ==========获取与要处理的hdfs文件路径 ->end
		
		// ==========获取用户渠道信息 ->start
		// key = uuid, value = channel
		Map<String, String> channelInfoMap = DBUtils.getChannelInfoMap();
		// ==========获取用户渠道信息 ->end
		
		// ==========从db查询新增用户信息(sysUser) ->start
		// sys_user.uuid list
		UserDao userDao = DaoFactory.getUserDao();
		List<String> newUserUuidListTemp = userDao.selectUuid(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());
		if (newUserUuidListTemp == null) {
			newUserUuidListTemp = new ArrayList<String>();
		}
		List<String> newUserUuidList = newUserUuidListTemp;
		System.out.println("==========检测到当天的注册人数为: " + (newUserUuidList == null ? 0 : newUserUuidList.size()));
		// ==========从db查询新增用户信息(sysUser) ->end
		
		// ==========第一层简单数据处理 ->start
		// 0		1								2		3				4															5			6	7		8					9					10		11			12
		// uuid	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	refpage:shoes,curpage:pages/home/A	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> javaRDD = javaSparkContext.textFile(hdfsFileDir)
		.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			// log 格式过滤
			if(AnalysisConstants.LOG_SPLIT_LENGTH != strs.length) {
				return false;
			}
			return true;
		})
		// 0		1		2		3
		// uuid	appId	channel		pageview
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			String channel = channelInfoMap.containsKey(strs[0]) ? channelInfoMap.get(strs[0]) : CommonConstants.DEFAULT_CHANNEL;
			return strs[0] + AnalysisConstants.SPLIT_REGEX + strs[5] 
					+ AnalysisConstants.SPLIT_REGEX + channel 
					+ AnalysisConstants.SPLIT_REGEX + strs[3] +  AnalysisConstants.SPLIT_REGEX +  strs[1] +  AnalysisConstants.SPLIT_REGEX + strs[2] ;
		})
		.cache();
		// ==========第一层简单数据处理 ->end
		
		// ==========活跃用户 RDD ->start
		// 0		1			2				3				4
		// uuid	appId	channel		pageview	curpage
		JavaRDD<String> userIdentityRDD = javaRDD
		// 0		1			2
		// uuid	appId	channel
		.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[0] + AnalysisConstants.SPLIT_REGEX + strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2]
					+ AnalysisConstants.SPLIT_REGEX +  strs[4] +  AnalysisConstants.SPLIT_REGEX + strs[5];
		})
		.distinct()
		.cache();
		// ==========活跃用户 RDD ->end
		
		// ==========保存活跃用户 RDD ->start
		userIdentityRDD.saveAsTextFile(hdfsUserIdentityDir);
		System.out.println("==========活跃用户保存完毕, 人数 = " + userIdentityRDD.count() + ", 保存后的路径 = " + hdfsUserIdentityDir);
		// ==========保存活跃用户 RDD ->end
		
		// ==========新增用户RDD ->start
		// 0		1			2
		// uuid	appId	channel
		JavaRDD<String> newUserIdentityRDD = userIdentityRDD.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			if(newUserUuidList.contains(strs[0])) {
				return true;
			}
			return false;
		}).cache();
		// ==========新增用户RDD ->end
		
		// ==========保存新增用户RDD ->start
		newUserIdentityRDD.saveAsTextFile(hdfsNewUserIdentityDir);
		System.out.println("==========新增用户保存完毕, 人数 = " + newUserIdentityRDD.count() + ", 保存后的路径 = " + hdfsNewUserIdentityDir);
		// ==========保存新增用户RDD ->end
		
		// ==========计算平均打开页面次数 ->start
		// 0		1			2				3
		// uuid	appId	channel		pageview
		JavaPairRDD<String, Integer> avgPvRDD = javaRDD.filter(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			// 只要pageview
			if(!StringUtils.equals(strs[3], EVENTNAME_ENUM.pageview.getName())) {
				return false;
			}
			// 只要新用户的日志
			if(!newUserUuidList.contains(strs[0])) {
				return false;
			}
			return true;
		})
		
		// key = uuid	appId	channel, value = 1
		.mapToPair(str -> new Tuple2<String, Integer>(str, 1))
		// key = uuid	appId	channel, value = sum
		.reduceByKey((i1, i2) -> i1 + i2)
		// key = appId	channel, value = sum
		.mapToPair(t -> {
			String[] strs = t._1.split(AnalysisConstants.SPLIT_REGEX);
			return new Tuple2<String, Integer>(strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2], t._2);
		})
		// key = appId	channel, value = sum:1
		.mapValues(integer -> new Tuple2<>(integer, 1))
		// key = appId	channel, value = sum(sum):sum(1)
		.reduceByKey((t1, t2) -> new Tuple2<>(t1._1 + t2._1, t1._2 + t2._2))
		// key = appId	channel, value = avg
		.mapValues(t -> t._1/t._2);
		
		Map<String, Integer> avgPvMap = avgPvRDD.collectAsMap();
		/*System.out.println(result.collect());*/
		// ==========计算平均打开页面次数 ->end
		
		// ==========获取key = appId	channel, value = newUserSize 的map ->start
		// 0	1	2
		// uuid	appId	channel
		Map<String, Long> newUserMap = newUserIdentityRDD.map(str -> {
			String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
			return strs[1] + AnalysisConstants.SPLIT_REGEX + strs[2];
		}).countByValue();
		// ==========获取key = appId	channel, value = newUserSize 的map ->start
		
		// ==========insert bigdata_user_retention_result(留存统计结果表) ->start
		System.out.println("==========avgPvMap = " + avgPvMap);
		UsersRetentionAnaysis.insertIntoDB(avgPvMap, newUserMap, localDate);
		// ==========insert bigdata_user_retention_result(留存统计结果表) ->end
	}
	
}
