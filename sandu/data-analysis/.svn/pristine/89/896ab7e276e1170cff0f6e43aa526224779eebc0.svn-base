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
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTNAME_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.EVENTPROPERTY_ENUM;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.funnel.dao.FunnelDao;
import com.sandu.analysis.biz.funnel.model.BigdataFunnalAnalyzeResultDO;
import com.sandu.analysis.biz.funnel.model.BigdataFunnalDetailBO;
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
		
		List<BigdataFunnalDetailBO> bigdataFunnalDetailBOList = getBigdataFunnalDetailBOListFromDB();
		// 根据漏斗id分组: 相同漏斗id的漏斗明细分一组
		Map<Long, List<BigdataFunnalDetailBO>> bigdataFunnalDetailBOListMap = getbigdataFunnalDetailBOListMap(bigdataFunnalDetailBOList);
		
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("FunnelAnalysis");
		sparkConf.setMaster(AnalysisConstants.SPARK_MASTER);
		System.out.println("------master = " + AnalysisConstants.SPARK_MASTER);
		@SuppressWarnings("resource")
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		
		/*DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");*/
		
		String hdfsFileDir = null;
		if(ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV)) {
			hdfsFileDir = "C:/Users/Administrator/Desktop/hdfs/";
		} else {
			hdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_EVENTS_DIR + dataDirInfo;
			// 检测hdfs中有没有这个目录, 如果没有则创建目录
			boolean isExist = HadoopUtils.getIsExist(hdfsFileDir);
			if(!isExist) {
				System.out.println("warn, function = FunnelAnnlysis.main, message = hdfs中对应目录不存在, dir = " + hdfsFileDir);
				insertToMysql(localDate, bigdataFunnalDetailBOList, null, null);
				return;
			}
		}
		System.out.println("------hdfsFileDir = " + hdfsFileDir);
		
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
		
		for(Long key : bigdataFunnalDetailBOListMap.keySet()) {
			List<BigdataFunnalDetailBO> bigdataFunnalDetailBOListItem = bigdataFunnalDetailBOListMap.get(key);
			System.out.println("------bigdataFunnalDetailBOListItem = " + bigdataFunnalDetailBOListItem);
			
			if(bigdataFunnalDetailBOListItem == null || bigdataFunnalDetailBOListItem.size() == 0) {
				System.out.println("warn, function = FunnelAnnlysis.main, message = (bigdataFunnalDetailBOListItem == null || bigdataFunnalDetailBOListItem.size() == 0) = true");
				continue;
			}
			
			// appId
			String appId = bigdataFunnalDetailBOListItem.get(0).getAppId();
			if(StringUtils.isEmpty(appId)) {
				System.out.println("warn, function = FunnelAnnlysis.main, message = StringUtils.isEmpty(appId) = true");
				continue;
			}
			
			List<String> funnelPageList = getFunnelPageList(bigdataFunnalDetailBOListItem);
			if(funnelPageList == null) {
				System.out.println("warn, function = FunnelAnnlysis.main message = bigdata_funnal.id = " + key + ", 没查到节点数据(bigdata_funnal_detail)");
				continue;
			}
			
			JavaRDD<List<Integer>> javaRDD = fileRDD
			// 转化为
			// 0		1	2		3
			// 123	0	time	appId
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
			// 过滤和漏斗无关的事件
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
			// key = 123(userId), value = [0 time, ....]
			.groupByKey()
			// 统计漏斗模型转化率
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
					if(new Integer(0).equals(intItem)) {
						indexForLast = 0;
						sorceList.set(0, sorceList.get(0) + 1);
						continue;
					}
					if(new Integer(indexForLast + 1).equals(intItem)) {
						indexForLast ++;
						sorceList.set(indexForLast, sorceList.get(indexForLast) + 1);
						continue;
					}
				}
				
				return sorceList;
			})
			.values()
			.cache();
			
			if(javaRDD.isEmpty()) {
				System.out.println("warn, function = main, message = (javaRDD.isEmpty()) = true");
				insertToMysql(localDate, bigdataFunnalDetailBOListItem, null, null);
				return;
			}
			
			List<Integer> funnelNodeNum = javaRDD.reduce((list1, list2) -> {
				List<Integer> resultList = new ArrayList<>();
				for (int index = 0; index < list1.size(); index++) {
					resultList.add(list1.get(index) + list2.get(index));
				}
				return resultList;
			});
			
			System.out.println("------funnelNodeNum = " + funnelNodeNum);
			
			List<Integer> initList = new ArrayList<Integer>();
			for (int index = 0; index < funnelPageList.size(); index++) {
				initList.add(0);
			}
			
			List<Integer> funnelNodeUV = javaRDD.aggregate(initList, (list1, list2) -> {
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
			
			System.out.println("------funnelNodeUV = " + funnelNodeUV);
			/*.saveAsTextFile("C:/Users/Administrator/Desktop/result");*/
			
			insertToMysql(localDate, bigdataFunnalDetailBOListItem, funnelNodeNum, funnelNodeUV);
		}
		
	}

	private static List<BigdataFunnalDetailBO> getBigdataFunnalDetailBOListFromDB() {
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		return funnelDao.selectFromBigdataFunnalDetailBOwhereEffective();
	}

	private static Map<Long, List<BigdataFunnalDetailBO>> getbigdataFunnalDetailBOListMap(
			List<BigdataFunnalDetailBO> bigdataFunnalDetailBOList) {
		Map<Long, List<BigdataFunnalDetailBO>> resultMap = new HashMap<Long, List<BigdataFunnalDetailBO>>();
		if (bigdataFunnalDetailBOList == null || bigdataFunnalDetailBOList.size() == 0) {
			return resultMap;
		}
		
		for(BigdataFunnalDetailBO bigdataFunnalDetailBO : bigdataFunnalDetailBOList) {
			Long funnelId =  bigdataFunnalDetailBO.getFunnelId();
			if (resultMap.containsKey(funnelId)) {
				resultMap.get(funnelId).add(bigdataFunnalDetailBO);
			} else {
				List<BigdataFunnalDetailBO> list = new ArrayList<BigdataFunnalDetailBO>();
				list.add(bigdataFunnalDetailBO);
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
	 * @param bigdataFunnalDetailBOList
	 * @return eg: ["pageview;pages/home/D", "pageview;pages/home/A", "pageview;pages/home/B"]
	 */
	private static List<String> getFunnelPageList(List<BigdataFunnalDetailBO> bigdataFunnalDetailBOList) {
		if(bigdataFunnalDetailBOList == null || bigdataFunnalDetailBOList.size() == 0) {
			return null;
		}
		
		return bigdataFunnalDetailBOList.stream().map(bo -> bo.getNodeEventType() + ";" + bo.getNodeEventProperty()).collect(Collectors.toList());
	}
	
	/**
	 * 获取需要统计的某个漏斗
	 * 2019.04.19
	 * 数据暂时定时, 后期改造成从db中查漏斗模型
	 * 
	 * @author huangsongbo
	 * @return
	 */
	private static List<BigdataFunnalDetailBO> getBigdataFunnalDetailBOList() {
		List<BigdataFunnalDetailBO> bigdataFunnalDetailBOList = new ArrayList<BigdataFunnalDetailBO>();
		
		// step 1
		BigdataFunnalDetailBO bigdataFunnalDetailBONode1 = new BigdataFunnalDetailBO();
		bigdataFunnalDetailBONode1.setId(1L);
		bigdataFunnalDetailBONode1.setNodeEventProperty("pages/home/home");
		bigdataFunnalDetailBONode1.setNodeEventType("pageview");
		bigdataFunnalDetailBONode1.setNodeName("首页");
		bigdataFunnalDetailBONode1.setNodeSeq(1);
		bigdataFunnalDetailBOList.add(bigdataFunnalDetailBONode1);
		
		// step 2
		BigdataFunnalDetailBO bigdataFunnalDetailBONode2 = new BigdataFunnalDetailBO();
		bigdataFunnalDetailBONode2.setId(2L);
		bigdataFunnalDetailBONode2.setNodeEventProperty("pages/designMyhome/designMyhome");
		bigdataFunnalDetailBONode2.setNodeEventType("pageview");
		bigdataFunnalDetailBONode2.setNodeName("设计我家页面");
		bigdataFunnalDetailBONode2.setNodeSeq(2);
		bigdataFunnalDetailBOList.add(bigdataFunnalDetailBONode2);
		
		// step 3
		BigdataFunnalDetailBO bigdataFunnalDetailBONode3 = new BigdataFunnalDetailBO();
		bigdataFunnalDetailBONode3.setId(3L);
		bigdataFunnalDetailBONode3.setNodeEventProperty("pages/designMyhome/designMyhome;designMyHome");
		bigdataFunnalDetailBONode3.setNodeEventType("btnclick");
		bigdataFunnalDetailBONode3.setNodeName("点击开始设计按钮");
		bigdataFunnalDetailBONode3.setNodeSeq(3);
		bigdataFunnalDetailBOList.add(bigdataFunnalDetailBONode3);
		
		// step 4
		BigdataFunnalDetailBO bigdataFunnalDetailBONode4 = new BigdataFunnalDetailBO();
		bigdataFunnalDetailBONode4.setId(4L);
		bigdataFunnalDetailBONode4.setNodeEventProperty("pages/three-house/three-house");
		bigdataFunnalDetailBONode4.setNodeEventType("pageview");
		bigdataFunnalDetailBONode4.setNodeName("进入3d户型页面");
		bigdataFunnalDetailBONode4.setNodeSeq(4);
		bigdataFunnalDetailBOList.add(bigdataFunnalDetailBONode4);
		
		/*// step 5
		BigdataFunnalDetailBO bigdataFunnalDetailBONode5 = new BigdataFunnalDetailBO();
		bigdataFunnalDetailBONode5.setId(5L);
		bigdataFunnalDetailBONode5.setNodeEventProperty("baseProduct/product_houseType");
		bigdataFunnalDetailBONode5.setNodeEventType("pageview");
		bigdataFunnalDetailBONode5.setNodeName("户型类型编辑页面");
		bigdataFunnalDetailBONode5.setNodeSeq(5);
		bigdataFunnalDetailBOList.add(bigdataFunnalDetailBONode5);
		
		// step 6
		BigdataFunnalDetailBO bigdataFunnalDetailBONode6 = new BigdataFunnalDetailBO();
		bigdataFunnalDetailBONode6.setId(6L);
		bigdataFunnalDetailBONode6.setNodeEventProperty("baseProduct/product_houseType_save");
		bigdataFunnalDetailBONode6.setNodeEventType("btnclick");
		bigdataFunnalDetailBONode6.setNodeName("点击户型类型保存按钮");
		bigdataFunnalDetailBONode6.setNodeSeq(6);
		bigdataFunnalDetailBOList.add(bigdataFunnalDetailBONode6);*/
		
		return bigdataFunnalDetailBOList;
	}

	private static void insertToMysql(LocalDate localDate, List<BigdataFunnalDetailBO> bigdataFunnalDetailBOList, List<Integer> funnelNodePV, List<Integer> funnelNodeUV) {
		if(bigdataFunnalDetailBOList == null || bigdataFunnalDetailBOList.size() == 0) {
			System.out.println("error, function = insertToMysql, message = (bigdataFunnalDetailBOList == null || bigdataFunnalDetailBOList.size() == 0) = true");
			return;
		}
		if(funnelNodePV == null || funnelNodePV.size() == 0) {
			System.out.println("warn, function = insertToMysql, message = (funnelNodePV == null || funnelNodePV.size() == 0) = true");
			funnelNodePV = new ArrayList<Integer>(bigdataFunnalDetailBOList.size());
			for (int index = 0; index < bigdataFunnalDetailBOList.size(); index++) {
				funnelNodePV.add(0);
			}
			/*return;*/
		}
		if(funnelNodeUV == null || funnelNodeUV.size() == 0) {
			System.out.println("warn, function = insertToMysql, message = (funnelNodeUV == null || funnelNodeUV.size() == 0) = true");
			funnelNodeUV = new ArrayList<Integer>(bigdataFunnalDetailBOList.size());
			for (int index = 0; index < bigdataFunnalDetailBOList.size(); index++) {
				funnelNodeUV.add(0);
			}
			/*return;*/
		}
		if(bigdataFunnalDetailBOList.size() != funnelNodePV.size() || funnelNodePV.size() != funnelNodeUV.size()) {
			System.out.println("error, function = insertToMysql, message = 统计程序有问题, bigdataFunnalDetailBOList, funnelNodePV, funnelNodeUV 三个list的size必须相等");
			return;
		}
		
		Date now = new Date();
		
		List<BigdataFunnalAnalyzeResultDO> list = new ArrayList<BigdataFunnalAnalyzeResultDO>();
		/*for(BigdataFunnalDetailBO bigdataFunnalDetailBO : bigdataFunnalDetailBOList) {*/
		for (int index = 0; index < bigdataFunnalDetailBOList.size(); index++) {
			BigdataFunnalDetailBO bigdataFunnalDetailBO = bigdataFunnalDetailBOList.get(index);
			
			BigdataFunnalAnalyzeResultDO bigdataFunnalAnalyzeResultDO = new BigdataFunnalAnalyzeResultDO();
			bigdataFunnalAnalyzeResultDO.setCreator(CommonConstants.CREATOR_SYSTEM);
			bigdataFunnalAnalyzeResultDO.setEndTime(Date.from(localDate.plusDays(1L).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
			bigdataFunnalAnalyzeResultDO.setFunnelId(bigdataFunnalDetailBO.getFunnelId());
			bigdataFunnalAnalyzeResultDO.setGmtCreate(now);
			bigdataFunnalAnalyzeResultDO.setGmtModified(now);
			bigdataFunnalAnalyzeResultDO.setIsDeleted(CommonConstants.ISDELETED_DEFAULT);
			bigdataFunnalAnalyzeResultDO.setModifier(CommonConstants.CREATOR_SYSTEM);
			bigdataFunnalAnalyzeResultDO.setNodeId(bigdataFunnalDetailBO.getId());
			bigdataFunnalAnalyzeResultDO.setNodeName(bigdataFunnalDetailBO.getNodeName());
			bigdataFunnalAnalyzeResultDO.setNodePv(funnelNodePV.get(index));
			bigdataFunnalAnalyzeResultDO.setNodeSeq(bigdataFunnalDetailBO.getNodeSeq());
			bigdataFunnalAnalyzeResultDO.setNodeUv(funnelNodeUV.get(index));
			bigdataFunnalAnalyzeResultDO.setRemark("");
			bigdataFunnalAnalyzeResultDO.setStartTime(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
			list.add(bigdataFunnalAnalyzeResultDO);
		}
		
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		funnelDao.insertBeforeDelete(list);
	}
	
}
