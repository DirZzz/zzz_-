package com.sandu.analysis.biz.funnel.offline;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTNAME_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.funnel.dao.FunnelDao;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelAnalysisResultDO;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelDetailBO;
import com.sandu.analysis.biz.util.DBUtils;
import com.sandu.analysis.biz.util.HadoopUtils;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.constant.ConfigConstants;

import scala.Tuple2;

/**
 * 漏斗模型分析
 * 
 * @author huangsongbo
 *
 */
public class FunnelAnalysis {
	
	public static void main(String[] args) {
		
		// 获取时间: 分析那一天的log
		// 返回eg: 201904/20/
		LocalDate localDate = Utils.getLocalDate(args);
		String dataDirInfo = localDate.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
		
		List<BigdataFunnelDetailBO> bigdataFunnelDetailBOList = getBigdataFunnelDetailBOListFromDB();
		// 根据漏斗id分组: 相同漏斗id的漏斗明细分一组
		Map<Long, List<BigdataFunnelDetailBO>> bigdataFunnelDetailBOListMap = getbigdataFunnelDetailBOListMap(bigdataFunnelDetailBOList);
		
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("FunnelAnalysis");
		sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
		System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		String hdfsFileDir = null;
		if(ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV)) {
			hdfsFileDir = "C:/Users/Administrator/Desktop/hdfs/";
		} else {
			hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_EVENTS_DIR + dataDirInfo;
			// 检测hdfs中有没有这个目录, 如果没有则创建目录
			boolean isExist = HadoopUtils.getIsExist(hdfsFileDir);
			if(!isExist) {
				System.out.println("warn, function = FunnelAnnlysis.main, message = hdfs中对应目录不存在, dir = " + hdfsFileDir);
				insertToMysql(localDate, bigdataFunnelDetailBOList, null, null);
				return;
			}
		}
		System.out.println("------hdfsFileDir = " + hdfsFileDir);
		
		// ==========获取用户渠道信息 ->start
		// key = uuid, value = channel
		Map<String, String> channelInfoMap = DBUtils.getChannelInfoMap();
		// ==========获取用户渠道信息 ->end
		
		// 0		1								2								3				4															5			6	7		8					9					10		11			12
		// 123	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	refpage:shoes,curpage:pages/home/A	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
		JavaRDD<String> fileRDD = javaSparkContext.textFile(hdfsFileDir)
		.filter(str -> {
			if(AnalysisConstants.LOG_SPLIT_LENGTH != str.split(AnalysisConstants.SPLIT_REGEX).length) {
				return false;
			}
			return true;
		})
		.cache();
		
		for(Long key : bigdataFunnelDetailBOListMap.keySet()) {
			List<BigdataFunnelDetailBO> bigdataFunnelDetailBOListItem = bigdataFunnelDetailBOListMap.get(key);
			System.out.println("------bigdataFunnelDetailBOListItem = " + bigdataFunnelDetailBOListItem);
			
			if(bigdataFunnelDetailBOListItem == null || bigdataFunnelDetailBOListItem.size() == 0) {
				System.out.println("warn, function = FunnelAnnlysis.main, message = (bigdataFunnelDetailBOListItem == null || bigdataFunnelDetailBOListItem.size() == 0) = true");
				continue;
			}
			
			// appId
			String appId = bigdataFunnelDetailBOListItem.get(0).getAppId();
			if(StringUtils.isEmpty(appId)) {
				System.out.println("warn, function = FunnelAnnlysis.main, message = StringUtils.isEmpty(appId) = true");
				continue;
			}
			
			List<String> funnelPageList = getFunnelPageList(bigdataFunnelDetailBOListItem);
			if(funnelPageList == null) {
				System.out.println("warn, function = FunnelAnnlysis.main message = bigdata_funnel.id = " + key + ", 没查到节点数据(bigdata_funnel_detail)");
				continue;
			}
			
			/*JavaRDD<List<Integer>> javaRDD = */
			JavaPairRDD<String, List<Integer>> javaPairRDD = fileRDD
			// 转化为
			// 0		1			2		3
			// uuid	index		time	appId
			// index 为当前页面再漏斗模型list中的index
			.map(str -> {
				String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
				Map<String, String> eventMap = Utils.getMap(strs[4]);
				String event =  strs[3] + ";" + (
						StringUtils.equals(EVENTNAME_ENUM.pageview.getName(), strs[3]) ? 
								eventMap.get(EVENTPROPERTY_ENUM.curpage.getName())
								: eventMap.get(EVENTPROPERTY_ENUM.curpage.getName()) + ";" + eventMap.get(EVENTPROPERTY_ENUM.btnid.getName())
						);
				Long time = LocalDateTime.parse(strs[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toInstant(ZoneOffset.of("+8")).toEpochMilli();
				return strs[0] + AnalysisConstants.SPLIT_REGEX + (funnelPageList.indexOf(event)) + AnalysisConstants.SPLIT_REGEX + time + AnalysisConstants.SPLIT_REGEX + strs[5];
			})
			// 过滤和漏斗无关的事件/只要对应平台的数据
			.filter(str -> {
				String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
				return StringUtils.equals(appId, strs[3]) && (!"-1".equals(strs[1]));
			})
			// 转化为
			// key = 123(userId), value = 0	time
			.mapToPair(str -> {
				String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
				return new Tuple2<String, String>(strs[0], Integer.valueOf(strs[1]) + AnalysisConstants.SPLIT_REGEX + strs[2]);
			})
			// 按key(user标识)分组
			// key = uuid, value = [0 time, ....]
			.groupByKey()
			// 统计漏斗模型转化率
			// key = uuid, value = sorceList(eg: 5, 3, 0)
			.mapValues(intIterable -> {
				// 初始化分数
				// 如果漏斗模型是: A -> B -> C
				// 则分数初始化的时候是: [0, 0, 0]
				// 一个流程中, 走一步, 对应index元素 +1
				// 根据统计之后效果可能是: [5, 3, 0]
				// 从此看出漏斗效果
				List<Integer> sorceList = new ArrayList<Integer>();
				for (int index = 0; index < funnelPageList.size(); index++) {
					sorceList.add(0);
				}
				// 记录此用户在本次流程中, 最终走到了哪个步骤
				int indexForLast = -1;
				// 记录此用户在本次流程中, 当前在第几步(用于支持用户"回到上一页")
				int indexForCurrent = -1;
				
				// 用户行为轨迹按时间排序
				@SuppressWarnings("unchecked")
				List<String> list = IteratorUtils.toList(intIterable.iterator());
				list.sort((o1, o2) -> {
					long time1 = Long.parseLong(o1.split(AnalysisConstants.SPLIT_REGEX)[1]);
					long time2 = Long.parseLong(o2.split(AnalysisConstants.SPLIT_REGEX)[1]);
					return (int) (time1 - time2);
				});
				
				/*System.out.println("----list = " + list);*/
				
				for(String item : list) {
					Integer intItem = Integer.valueOf(item.split(AnalysisConstants.SPLIT_REGEX)[0]);
					// 如果回到了起始位置
					if(new Integer(0).equals(intItem)) {
						indexForLast = 0;
						indexForCurrent = 0;
						sorceList.set(0, sorceList.get(0) + 1);
						continue;
					}
					// 如果是下一步并且是最深漏斗节点
					if(new Integer(indexForLast + 1).equals(intItem) && indexForLast == indexForCurrent) {
						indexForLast ++;
						indexForCurrent ++;
						sorceList.set(indexForLast, sorceList.get(indexForLast) + 1);
						continue;
					}
					// 如果是下一步, 但不是最深漏斗节点(说明回退过)
					// 不会加分, 只会步骤前进
					if(new Integer(indexForCurrent + 1).equals(intItem)) {
						indexForCurrent ++;
						continue;
					}
					// 如果是回退
					if(intItem < indexForCurrent) {
						indexForCurrent = intItem;
						continue;
					}
				}
				
				return sorceList;
			})
			// key = channel, value = sorceList
			.mapToPair(t -> {
				String channel = channelInfoMap.containsKey(t._1) ? channelInfoMap.get(t._1) : CommonConstants.DEFAULT_CHANNEL;
				return new Tuple2<String, List<Integer>>(channel, t._2);
			})
			.cache();
			/*.values()
			.cache();*/
			
			/*if(javaRDD.isEmpty()) {
				System.out.println("warn, function = main, message = (javaRDD.isEmpty()) = true");
				insertToMysql(localDate, bigdataFunnelDetailBOListItem, null, null);
				return;
			}*/
			if(javaPairRDD.isEmpty()) {
				System.out.println("warn, function = main, message = (javaRDD.isEmpty()) = true");
				insertToMysql(localDate, bigdataFunnelDetailBOListItem, null, null);
				return;
			}
			
			// ==========漏斗节点类pv统计 ->start
			/*List<Integer> funnelNodeNum = javaRDD.reduce((list1, list2) -> {
				List<Integer> resultList = new ArrayList<>();
				for (int index = 0; index < list1.size(); index++) {
					resultList.add(list1.get(index) + list2.get(index));
				}
				return resultList;
			});
			
			System.out.println("------funnelNodeNum = " + funnelNodeNum);*/
			
			// key = channel, value = sorceList
			Map<String, List<Integer>> nodeNumMap = javaPairRDD.reduceByKeyLocally((list1, list2) -> {
				List<Integer> resultList = new ArrayList<>();
				for (int index = 0; index < list1.size(); index++) {
					resultList.add(list1.get(index) + list2.get(index));
				}
				return resultList;
			});
			
			System.out.println("------nodeNumMap = " + nodeNumMap);
			// ==========漏斗节点类pv统计 ->end
			
			// ==========漏斗节点uv统计 ->start
			List<Integer> initList = new ArrayList<Integer>();
			for (int index = 0; index < funnelPageList.size(); index++) {
				initList.add(0);
			}
			
			// 集群环境运行的时候, 总是数据少了, 所以才改成这个样子, 感觉和数据分区有关
			Map<String, List<Integer>> nodeUvMap = javaPairRDD.mapValues(l -> {
				List<Integer> returnList = new ArrayList<Integer>();
				for (Integer integer : l) {
					if (integer > 0) {
						returnList.add(1);
					} else {
						returnList.add(0);
					}
				}
				return returnList;
			}).reduceByKeyLocally((list1, list2) -> {
				List<Integer> resultList = new ArrayList<>();
				for (int index = 0; index < list1.size(); index++) {
					resultList.add(list1.get(index) + list2.get(index));
				}
				return resultList;
			});
			// 改之前的函数
			/*javaPairRDD.foldByKey(initList, (list1, list2) -> {
				List<Integer> resultList = new ArrayList<>();
				for (int index = 0; index < list1.size(); index++) {
					resultList.add(list1.get(index) + (list2.get(index) > 0 ? 1 : 0));
				}
				return resultList;
			})
			.collectAsMap();*/
			
			System.out.println("------nodeUvMap = " + nodeUvMap);
			/*List<Integer> funnelNodeUV = javaRDD.aggregate(initList, (list1, list2) -> {
				List<Integer> resultList = new ArrayList<>();
				for (int index = 0; index < list1.size(); index++) {
					resultList.add(list1.get(index) + (list2.get(index) > 0 ? 1 : 0));
				}
				return resultList;
			}, (list1, list2) -> {
				List<Integer> resultList = new ArrayList<>();
				for (int index = 0; index < list1.size(); index++) {
					resultList.add(list1.get(index) + list2.get(index));
				}
				return resultList;
			});
			
			System.out.println("------funnelNodeUV = " + funnelNodeUV);*/
			/*.saveAsTextFile("C:/Users/Administrator/Desktop/result");*/
			// ==========漏斗节点uv统计 ->end
			
			/*insertToMysql(localDate, bigdataFunnelDetailBOListItem, funnelNodeNum, funnelNodeUV);*/
			insertToMysql(localDate, bigdataFunnelDetailBOListItem, nodeNumMap, nodeUvMap);
		}
		
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param localDate 当前时间
	 * @param bigdataFunnelDetailBOListItem 漏斗模型
	 * @param nodeNumMap 漏斗节点次数map(key = channel)
	 * @param nodeUvMap 漏斗节点uv map(key = channel)
	 */
	private static void insertToMysql(LocalDate localDate, List<BigdataFunnelDetailBO> bigdataFunnelDetailBOList,
			Map<String, List<Integer>> nodeNumMap, Map<String, List<Integer>> nodeUvMap) {
		for(String key : nodeNumMap.keySet()) {
			List<Integer> funnelNodePV = nodeNumMap.get(key);
			List<Integer> funnelNodeUV = nodeUvMap.get(key);
			insertToMysql(localDate, bigdataFunnelDetailBOList, funnelNodePV, funnelNodeUV, key);
		}
	}

	private static List<BigdataFunnelDetailBO> getBigdataFunnelDetailBOListFromDB() {
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		return funnelDao.selectFromBigdataFunnelDetailBOwhereEffective();
	}

	private static Map<Long, List<BigdataFunnelDetailBO>> getbigdataFunnelDetailBOListMap(
			List<BigdataFunnelDetailBO> bigdataFunnelDetailBOList) {
		Map<Long, List<BigdataFunnelDetailBO>> resultMap = new HashMap<Long, List<BigdataFunnelDetailBO>>();
		if (bigdataFunnelDetailBOList == null || bigdataFunnelDetailBOList.size() == 0) {
			return resultMap;
		}
		
		for(BigdataFunnelDetailBO bigdataFunnelDetailBO : bigdataFunnelDetailBOList) {
			Long funnelId =  bigdataFunnelDetailBO.getFunnelId();
			if (resultMap.containsKey(funnelId)) {
				resultMap.get(funnelId).add(bigdataFunnelDetailBO);
			} else {
				List<BigdataFunnelDetailBO> list = new ArrayList<BigdataFunnelDetailBO>();
				list.add(bigdataFunnelDetailBO);
				resultMap.put(funnelId, list);
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * @author huangsongbo
	 * @param args eg: 20190420
	 * @return eg: 201904/20/
	 */
	public static String getDataDirInfo(String[] args) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM/dd/");
		String dataDirInfo = null;
		if(args != null && args.length > 0) {
			// args[0] 格式: 20190422
			LocalDate now = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyyMMdd"));
			dataDirInfo = now.format(dateTimeFormatter);
		} else {
			// 默认统计昨天的数据
			LocalDate now = LocalDate.now();
			dataDirInfo = now.plusDays(-1).format(dateTimeFormatter);
		}
		
		return dataDirInfo;
	}

	/**
	 * 
	 * 
	 * @author huangsongbo
	 * @param bigdataFunnelDetailBOList
	 * @return eg: ["pageview;pages/home/D", "pageview;pages/home/A", "pageview;pages/home/B"]
	 */
	private static List<String> getFunnelPageList(List<BigdataFunnelDetailBO> bigdataFunnelDetailBOList) {
		if(bigdataFunnelDetailBOList == null || bigdataFunnelDetailBOList.size() == 0) {
			return null;
		}
		
		return bigdataFunnelDetailBOList.stream().map(bo -> bo.getNodeEventType() + ";" + bo.getNodeEventProperty()).collect(Collectors.toList());
	}
	
	/**
	 * 获取需要统计的某个漏斗
	 * 用于调试
	 * 
	 * 2019.04.19
	 * 数据暂时定时, 后期改造成从db中查漏斗模型
	 * 
	 * @author huangsongbo
	 * @return
	 */
	private static List<BigdataFunnelDetailBO> getBigdataFunnelDetailBOList() {
		List<BigdataFunnelDetailBO> bigdataFunnelDetailBOList = new ArrayList<BigdataFunnelDetailBO>();
		
		// step 1
		BigdataFunnelDetailBO bigdataFunnelDetailBONode1 = new BigdataFunnelDetailBO();
		bigdataFunnelDetailBONode1.setId(1L);
		bigdataFunnelDetailBONode1.setNodeEventProperty("pages/home/home");
		bigdataFunnelDetailBONode1.setNodeEventType("pageview");
		bigdataFunnelDetailBONode1.setNodeName("首页");
		bigdataFunnelDetailBONode1.setNodeSeq(1);
		bigdataFunnelDetailBOList.add(bigdataFunnelDetailBONode1);
		
		// step 2
		BigdataFunnelDetailBO bigdataFunnelDetailBONode2 = new BigdataFunnelDetailBO();
		bigdataFunnelDetailBONode2.setId(2L);
		bigdataFunnelDetailBONode2.setNodeEventProperty("pages/designMyhome/designMyhome");
		bigdataFunnelDetailBONode2.setNodeEventType("pageview");
		bigdataFunnelDetailBONode2.setNodeName("设计我家页面");
		bigdataFunnelDetailBONode2.setNodeSeq(2);
		bigdataFunnelDetailBOList.add(bigdataFunnelDetailBONode2);
		
		// step 3
		BigdataFunnelDetailBO bigdataFunnelDetailBONode3 = new BigdataFunnelDetailBO();
		bigdataFunnelDetailBONode3.setId(3L);
		bigdataFunnelDetailBONode3.setNodeEventProperty("pages/designMyhome/designMyhome;designMyHome");
		bigdataFunnelDetailBONode3.setNodeEventType("btnclick");
		bigdataFunnelDetailBONode3.setNodeName("点击开始设计按钮");
		bigdataFunnelDetailBONode3.setNodeSeq(3);
		bigdataFunnelDetailBOList.add(bigdataFunnelDetailBONode3);
		
		// step 4
		BigdataFunnelDetailBO bigdataFunnelDetailBONode4 = new BigdataFunnelDetailBO();
		bigdataFunnelDetailBONode4.setId(4L);
		bigdataFunnelDetailBONode4.setNodeEventProperty("pages/three-house/three-house");
		bigdataFunnelDetailBONode4.setNodeEventType("pageview");
		bigdataFunnelDetailBONode4.setNodeName("进入3d户型页面");
		bigdataFunnelDetailBONode4.setNodeSeq(4);
		bigdataFunnelDetailBOList.add(bigdataFunnelDetailBONode4);
		
		/*// step 5
		BigdataFunnelDetailBO bigdataFunnelDetailBONode5 = new BigdataFunnelDetailBO();
		bigdataFunnelDetailBONode5.setId(5L);
		bigdataFunnelDetailBONode5.setNodeEventProperty("baseProduct/product_houseType");
		bigdataFunnelDetailBONode5.setNodeEventType("pageview");
		bigdataFunnelDetailBONode5.setNodeName("户型类型编辑页面");
		bigdataFunnelDetailBONode5.setNodeSeq(5);
		bigdataFunnelDetailBOList.add(bigdataFunnelDetailBONode5);
		
		// step 6
		BigdataFunnelDetailBO bigdataFunnelDetailBONode6 = new BigdataFunnelDetailBO();
		bigdataFunnelDetailBONode6.setId(6L);
		bigdataFunnelDetailBONode6.setNodeEventProperty("baseProduct/product_houseType_save");
		bigdataFunnelDetailBONode6.setNodeEventType("btnclick");
		bigdataFunnelDetailBONode6.setNodeName("点击户型类型保存按钮");
		bigdataFunnelDetailBONode6.setNodeSeq(6);
		bigdataFunnelDetailBOList.add(bigdataFunnelDetailBONode6);*/
		
		return bigdataFunnelDetailBOList;
	}

	private static void insertToMysql(LocalDate localDate, List<BigdataFunnelDetailBO> bigdataFunnelDetailBOList, List<Integer> funnelNodePV, List<Integer> funnelNodeUV, String channel) {
		if(bigdataFunnelDetailBOList == null || bigdataFunnelDetailBOList.size() == 0) {
			System.out.println("error, function = insertToMysql, message = (bigdataFunnelDetailBOList == null || bigdataFunnelDetailBOList.size() == 0) = true");
			return;
		}
		if(funnelNodePV == null || funnelNodePV.size() == 0) {
			System.out.println("warn, function = insertToMysql, message = (funnelNodePV == null || funnelNodePV.size() == 0) = true");
			funnelNodePV = new ArrayList<Integer>(bigdataFunnelDetailBOList.size());
			for (int index = 0; index < bigdataFunnelDetailBOList.size(); index++) {
				funnelNodePV.add(0);
			}
			/*return;*/
		}
		if(funnelNodeUV == null || funnelNodeUV.size() == 0) {
			System.out.println("warn, function = insertToMysql, message = (funnelNodeUV == null || funnelNodeUV.size() == 0) = true");
			funnelNodeUV = new ArrayList<Integer>(bigdataFunnelDetailBOList.size());
			for (int index = 0; index < bigdataFunnelDetailBOList.size(); index++) {
				funnelNodeUV.add(0);
			}
			/*return;*/
		}
		if(bigdataFunnelDetailBOList.size() != funnelNodePV.size() || funnelNodePV.size() != funnelNodeUV.size()) {
			System.out.println("error, function = insertToMysql, message = 统计程序有问题, bigdataFunnelDetailBOList, funnelNodePV, funnelNodeUV 三个list的size必须相等");
			return;
		}
		
		Date now = new Date();
		
		List<BigdataFunnelAnalysisResultDO> list = new ArrayList<BigdataFunnelAnalysisResultDO>();
		/*for(BigdataFunnelDetailBO bigdataFunnelDetailBO : bigdataFunnelDetailBOList) {*/
		for (int index = 0; index < bigdataFunnelDetailBOList.size(); index++) {
			BigdataFunnelDetailBO bigdataFunnelDetailBO = bigdataFunnelDetailBOList.get(index);
			
			BigdataFunnelAnalysisResultDO bigdataFunnelAnalyzeResultDO = new BigdataFunnelAnalysisResultDO();
			bigdataFunnelAnalyzeResultDO.setCreator(CommonConstants.CREATOR_SYSTEM);
			bigdataFunnelAnalyzeResultDO.setEndTime(Date.from(localDate.plusDays(1L).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
			bigdataFunnelAnalyzeResultDO.setFunnelId(bigdataFunnelDetailBO.getFunnelId());
			bigdataFunnelAnalyzeResultDO.setChannel(channel);
			bigdataFunnelAnalyzeResultDO.setGmtCreate(now);
			bigdataFunnelAnalyzeResultDO.setGmtModified(now);
			bigdataFunnelAnalyzeResultDO.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
			bigdataFunnelAnalyzeResultDO.setModifier(CommonConstants.CREATOR_SYSTEM);
			bigdataFunnelAnalyzeResultDO.setNodeId(bigdataFunnelDetailBO.getId());
			bigdataFunnelAnalyzeResultDO.setNodeName(bigdataFunnelDetailBO.getNodeName());
			bigdataFunnelAnalyzeResultDO.setNodePv(funnelNodePV.get(index));
			bigdataFunnelAnalyzeResultDO.setNodeSeq(bigdataFunnelDetailBO.getNodeSeq());
			bigdataFunnelAnalyzeResultDO.setNodeUv(funnelNodeUV.get(index));
			bigdataFunnelAnalyzeResultDO.setRemark("");
			bigdataFunnelAnalyzeResultDO.setStartTime(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
			list.add(bigdataFunnelAnalyzeResultDO);
		}
		
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		funnelDao.insertBeforeDelete(list);
	}
	
}
