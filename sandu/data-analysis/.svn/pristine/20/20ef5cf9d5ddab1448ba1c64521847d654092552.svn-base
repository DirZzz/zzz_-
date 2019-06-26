package com.sandu.analysis.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * 生成随机日志以供调试
 * 
 * @author huangsongbo
 *
 */
public class CreateLogs {

	public static void main(String[] args) {
		// 50    2019-04-08 19:31:56    2019-04-08 19:32:27    pageview    curpage:index    a0001    js    1.0.0    bigdatadev1    192.168.3.97    中国    广东省    深圳市
		// 50    2019-04-08 19:32:20    2019-04-08 19:32:51    btnclick    btnid:index_changePassword    a0001    js    1.0.0    bigdatadev1    192.168.3.97    中国    广东省    深圳市
		List<String> userIdList = Arrays.asList("50", "100", "123", "323", "345", "423", "675");
		List<String> eventNameList = Arrays.asList("pageview", "btnclick");
		List<String> pageList = Arrays.asList("curpage:pages/home/A", "curpage:pages/home/B", "curpage:pages/home/C", "curpage:pages/home/D");
		List<String> buttonList = Arrays.asList("btnid:pages/home/A_a", "btnid:pages/home/B_b", "btnid:pages/home/C_c", "btnid:pages/home/D_d");
		
		Date now = new Date();
		long nowTime = now.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for (int index = 0; index < 100000; index++) {
			
			String serviceTime = simpleDateFormat.format(new Date(nowTime += (int) (Math.random() * 1000)));
			String eventName = CreateLogs.getRandomElement(eventNameList);
			
			System.out.println(
					CreateLogs.getRandomElement(userIdList) + "\t"
					+ serviceTime + "\t"
					+ serviceTime + "\t"
					+ eventName + "\t"
					+ (StringUtils.equals("pageview", eventName) ? CreateLogs.getRandomElement(pageList) : CreateLogs.getRandomElement(buttonList)) + "\t"
					+ "a0001\tjs\t1.0.0\tbigdatadev1\t192.168.3.97\t中国\t广东省\t深圳市"
					);
		}
	}
	
	private static <T> T getRandomElement(List<T> list) {
		int index = (int) (Math.random() * list.size());
		return list.get(index);
	}
	
	/**
	 * 生成随机数list
	 */
	@Test
	public void test001() {
		List<Integer> list = new ArrayList<Integer>();
		for (int index = 1; index <= 300; index++) {
			list.add(index);
		}
		System.out.println(list);
	}
	
	/**
	 * 生成31天的日志, 用来调试计算留存程序
	 * @throws IOException
	 */
	@Test
	public void test002() throws IOException {
		// 日志基础信息
		List<String> eventNameList = Arrays.asList("pageview", "btnclick");
		List<String> pageList = Arrays.asList("curpage:pages/home/A", "curpage:pages/home/B", "curpage:pages/home/C", "curpage:pages/home/D");
		List<String> buttonList = Arrays.asList("btnid:pages/home/A_a", "btnid:pages/home/B_b", "btnid:pages/home/C_c", "btnid:pages/home/D_d");
		
		// 基础用户
		List<Integer> oldUserIdList = new ArrayList<Integer>();
		for (int index = 1; index <= 500; index++) {
			oldUserIdList.add(index);
		}
		// 新用户idList, key = 第?天, value = newUserIdList
		Map<Integer, List<Integer>> newUserIdMap = new HashMap<Integer, List<Integer>>();
		Integer start = 301;
		
		// 每天有多少个活跃用户
		int activeUserCount = 100;
		
		// 生成31天的新用户id
		for (int index = 1; index <= 31; index++) {
			// 每天20-40个新用户
			int newUserCount = (int) (Math.random() * 20) + 20;
			List<Integer> newUserIdList = new ArrayList<Integer>();
			for (int index2 = 0; index2 < newUserCount; index2++) {
				newUserIdList.add(start ++);
			}
			newUserIdMap.put(index, newUserIdList);
		}
		/*System.out.println(newUserIdMap);*/
		
		// 准备生成31天的日志
		for (int index = 1; index <= 31; index++) {
			System.out.println("filePath = " + "C:/Users/Administrator/Desktop/hdfsLocal/2019-03-" + (index < 10 ? "0" + index : index) + "/log/log.txt");
			File file = new File("C:/Users/Administrator/Desktop/hdfsLocal/2019-03-" + (index < 10 ? "0" + index : index) + "/log/log.txt");
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(file.exists()) {
				file.createNewFile();
			}
			/*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));*/
			FileWriter fw = new FileWriter(file,false);
            BufferedWriter bw = new BufferedWriter(fw);
			
			List<Integer> newUserIdList = newUserIdMap.get(index);

			Date now = new Date();
			long nowTime = now.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// 先生成新用户的注册日志
			for(Integer newUserId : newUserIdList) {
				String serviceTime = simpleDateFormat.format(new Date(nowTime += (int) (Math.random() * 1000)));
				bw.write(
						newUserId + "\t"
						+ serviceTime + "\t"
						+ serviceTime + "\t"
						+ "btnclick" + "\t"
						+ "btnid:pages/home/A_register" + "\t"
						+ "a0001\tjs\t1.0.0\tbigdatadev1\t192.168.3.97\t中国\t广东省\t深圳市"
						);
				bw.newLine();
			}
			
			// 确定今天的活跃用户有哪些, 从#{oldUserIdList}中随机取#{activeUserCount}个用户
			List<Integer> acitveUserIdList = getRandomList(oldUserIdList, activeUserCount);
			
			// 生成活跃用户日志
			for (int index2 = 0; index2 < 100000; index2++) {
				String serviceTime = simpleDateFormat.format(new Date(nowTime += (int) (Math.random() * 1000)));
				String eventName = CreateLogs.getRandomElement(eventNameList);
				
				bw.write(
						CreateLogs.getRandomElement(acitveUserIdList) + "\t"
						+ serviceTime + "\t"
						+ serviceTime + "\t"
						+ eventName + "\t"
						+ (StringUtils.equals("pageview", eventName) ? CreateLogs.getRandomElement(pageList) : CreateLogs.getRandomElement(buttonList)) + "\t"
						+ "a0001\tjs\t1.0.0\tbigdatadev1\t192.168.3.97\t中国\t广东省\t深圳市"
						);
				bw.newLine();
			}
			
			// 活跃用户添加今天的新增用户
			oldUserIdList.addAll(newUserIdList);
			
			// 每天活跃用户增加一些(2-5个)
			activeUserCount += ((int) (Math.random() * 3) + 2);
			
			bw.close();
			fw.close();
		}
	}
	
	private List<Integer> getRandomList(List<Integer> acitveUserIdList, int activeUserCount) {
		Collections.shuffle(acitveUserIdList);
		return acitveUserIdList.subList(0, activeUserCount);
	}

	@Test
	public void test003() throws IOException {
		File file = new File("C:/Users/Administrator/Desktop/log/2019-04-12/log.txt");
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(!file.exists()) {
			file.createNewFile();
		}
	}
	
}
