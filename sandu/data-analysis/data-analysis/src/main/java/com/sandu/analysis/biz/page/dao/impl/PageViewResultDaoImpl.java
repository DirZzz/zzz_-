package com.sandu.analysis.biz.page.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.sandu.analysis.biz.page.dao.PageViewResultDao;
import com.sandu.analysis.biz.page.model.PageViewResultDO;
import com.sandu.analysis.biz.page.model.PageViewResultQuery;
import com.sandu.analysis.util.JdbcUtils;

public class PageViewResultDaoImpl implements PageViewResultDao {

	private String className = this.getClass().getName();
	
	@Override
	public void insert(List<PageViewResultDO> bigdataPageViewResultDOList) {
		System.out.println("==========准备insert统计结果");
		String functionName = className + ".insert";
		
		if(bigdataPageViewResultDOList == null || bigdataPageViewResultDOList.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (bigdataPageViewResultDOList == null || bigdataPageViewResultDOList.size() == 0) = true");
			return;
		}
		
		System.out.println("==========bigdataPageViewResultDOList.size() = " + bigdataPageViewResultDOList.size());
		
		String sql = "insert into bigdata_page_view_result "
				+ "(page_property, pv, uv, time_on_page, start_time, end_time, app_id, channel, creator, gmt_create, modifier, gmt_modified, is_deleted, remark, npv, nuv, "
				+ "bounce_pv, bounce_uv, bounce_npv, bounce_nuv) "
				+ "values "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// ==========设置参数 ->start
		Object[][] params = new Object[bigdataPageViewResultDOList.size()][];
		for (int index = 0; index < bigdataPageViewResultDOList.size(); index++) {
			PageViewResultDO bigdataPageViewResultDO = bigdataPageViewResultDOList.get(index);
			params[index] = new Object[] {
				bigdataPageViewResultDO.getPageProperty(), bigdataPageViewResultDO.getPv(), bigdataPageViewResultDO.getUv(), bigdataPageViewResultDO.getTimeOnPage(),
				simpleDateFormat.format(bigdataPageViewResultDO.getStartTime()), simpleDateFormat.format(bigdataPageViewResultDO.getEndTime()), bigdataPageViewResultDO.getAppId(), bigdataPageViewResultDO.getChannel(),
				bigdataPageViewResultDO.getCreator(), simpleDateFormat.format(bigdataPageViewResultDO.getGmtCreate()), bigdataPageViewResultDO.getModifier(), simpleDateFormat.format(bigdataPageViewResultDO.getGmtModified()),
				bigdataPageViewResultDO.getIsDeleted(), bigdataPageViewResultDO.getRemark(), bigdataPageViewResultDO.getNpv(), bigdataPageViewResultDO.getNuv(), 
				bigdataPageViewResultDO.getBouncePv(), bigdataPageViewResultDO.getBounceUv(), bigdataPageViewResultDO.getBounceNpv(), bigdataPageViewResultDO.getBounceNuv()
			};
		}
		// ==========设置参数 ->end
		
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		try {
			qr.batch(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("==========" + e.getMessage());
		}
		System.out.println("==========insert统计结果完成");
	}

	@Override
	public void insertBeforeDelete(List<PageViewResultDO> pageViewResultDOList) {
		String functionName = className + ".insertBeforeDelete";
		
		if(pageViewResultDOList == null || pageViewResultDOList.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (bigdataPageViewResultDOList == null || bigdataPageViewResultDOList.size() == 0) = true");
			return;
		}
		
		Set<PageViewResultQuery> pageViewResultQuerieList = new HashSet<PageViewResultQuery>();
		for(PageViewResultDO item : pageViewResultDOList) {
			PageViewResultQuery pageViewResultQuery = new PageViewResultQuery();
			pageViewResultQuery.setEndTime(item.getEndTime());
			pageViewResultQuery.setStartTime(item.getStartTime());
			pageViewResultQuerieList.add(pageViewResultQuery);
		}
		
		// 逻辑删除已存在的数据(startTime相等 && endTime相等 && funnel_id相等)
		this.delete(pageViewResultQuerieList);
		
		this.insert(pageViewResultDOList);
		
	}

	private void delete(Set<PageViewResultQuery> querySet) {
		System.out.println("==========准备删除旧数据");
		String functionName = className + ".delete";
		
		if(querySet == null || querySet.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (querySet == null || querySet.size() == 0) = true");
			return;
		}
		querySet.forEach(item -> this.delete(item));
		System.out.println("==========旧数据删除完成");
	}

	private void delete(PageViewResultQuery query) {
		String functionName = className + ".delete";
		
		if(query == null || query.getEndTime() == null || query.getStartTime() == null) {
			System.out.println("warn, function = " + functionName + ", message = (query == null || query.getEndTime() == null || query.getStartTime() == null) = true");
			System.out.println("info, function = " + functionName + ", message = query = " + query);
			System.out.println("info, function = " + functionName + ", message = query.getEndTime() = " + query.getEndTime());
			System.out.println("info, function = " + functionName + ", message = query.getStartTime() = " + query.getStartTime());
			return;
		}
		
		List<Long> idList = this.select(query);
		
		this.delete(idList);
	}

	private void delete(List<Long> idList) {
		String functionName = className + ".delete";
		
		if(idList == null || idList.size() == 0) {
			System.out.println("warn, function = " + functionName + ", (idList == null || idList.size() == 0) = true");
			return;
		}
		
		try {
			QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "update bigdata_page_view_result set is_deleted = 1 where id = ?";
			Object params[][] = new Object[idList.size()][];
			for (int i = 0; i < idList.size(); i++) {
				Long id = idList.get(i);
				params[i] = new Object[] {id};
			}
			qr.batch(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private List<Long> select(PageViewResultQuery query) {
		String functionName = className + ".select";
		
		if(query == null || query.getEndTime() == null || query.getStartTime() == null) {
			System.out.println("warn, function = " + functionName + ", (query == null || query.getEndTime() == null || query.getStartTime() == null) = true");
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<Long> list = null;
		String sql = "select id from bigdata_page_view_result where start_time = ? and end_time = ?";
		Object[] params = new Object[] {simpleDateFormat.format(query.getStartTime()), simpleDateFormat.format(query.getEndTime())};
		System.out.println("sql = " + sql);
		System.out.println("params = " + params);
		try {
			QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
			list = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
}
