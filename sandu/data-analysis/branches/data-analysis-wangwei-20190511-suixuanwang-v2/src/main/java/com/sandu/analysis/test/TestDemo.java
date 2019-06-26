package com.sandu.analysis.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.funnel.dao.FunnelDao;
import com.sandu.analysis.biz.funnel.model.BigdataFunnalAnalyzeResultQuery;
import com.sandu.analysis.biz.funnel.offline.FunnelAnalysis;
import com.sandu.analysis.biz.util.Utils;

public class TestDemo {

	@Test
	public void test001() {
		Date date = new Date();
		Set<BigdataFunnalAnalyzeResultQuery> set = new HashSet<BigdataFunnalAnalyzeResultQuery>();
		for (int index = 0; index < 5; index++) {
			BigdataFunnalAnalyzeResultQuery bigdataFunnalAnalyzeResultQuery = new BigdataFunnalAnalyzeResultQuery();
			bigdataFunnalAnalyzeResultQuery.setFunnelId(1L);
			bigdataFunnalAnalyzeResultQuery.setEndTime(date);
			bigdataFunnalAnalyzeResultQuery.setStartTime(date);
			set.add(bigdataFunnalAnalyzeResultQuery);
		}
		
		System.out.println(set.size());
		
	}
	
	@Test
	public void test002() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = simpleDateFormat.parse("2019-04-19 00:00:00");
		Date date2 = simpleDateFormat.parse("2019-04-20 00:00:00");
		
		BigdataFunnalAnalyzeResultQuery bigdataFunnalAnalyzeResultQuery = new BigdataFunnalAnalyzeResultQuery();
		bigdataFunnalAnalyzeResultQuery.setFunnelId(0L);
		bigdataFunnalAnalyzeResultQuery.setEndTime(date2);
		bigdataFunnalAnalyzeResultQuery.setStartTime(date);
		
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		List<Long> idList = funnelDao.select(bigdataFunnalAnalyzeResultQuery);
		System.out.println(idList);
	}

	@Test
	public void test003() {
		String[] args = new String[] {"20190422"};
		System.out.println(FunnelAnalysis.getDataDirInfo(args));
	}
	
	@Test
	public void test004() {
		FunnelDao funnelDao = DaoFactory.getFunnelDao();
		System.out.println(funnelDao.selectFromBigdataFunnalDetailBOwhereEffective());
	}
	
	@Test
	public void test005() {
		Integer[] ints = new Integer[6];
		Arrays.fill(ints, 0);
		List<Integer> list = Arrays.asList(ints);
		System.out.println(list);
	}
	
	@Test
	public void test006() {
		LocalDate localDate = Utils.getLocalDate(null);
		System.out.println(localDate);
		Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
	}
	
	@Test
	public void test007() throws URISyntaxException {
		URI uri = new URI("hdfs://192.168.1.240:8020/test/home");
		System.out.println(uri.getPath());
		System.out.println(uri.getHost());
		System.out.println(uri.toString());
	}
	
}
 