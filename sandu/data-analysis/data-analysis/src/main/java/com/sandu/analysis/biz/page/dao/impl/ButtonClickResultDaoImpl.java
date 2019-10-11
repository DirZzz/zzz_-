package com.sandu.analysis.biz.page.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.sandu.analysis.biz.page.dao.ButtonClickResultDao;
import com.sandu.analysis.biz.page.model.ButtonClickResultDO;
import com.sandu.analysis.biz.page.model.ButtonClickResultQuery;
import com.sandu.analysis.util.JdbcUtils;

public class ButtonClickResultDaoImpl implements ButtonClickResultDao {
	
	private String className = this.getClass().getName();

	@Override
	public void insertBeforeDelete(List<ButtonClickResultDO> buttonClickResultDOList) {
		String functionName = className + ".insertBeforeDelete";
		
		if(buttonClickResultDOList == null || buttonClickResultDOList.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (buttonClickResultDOList == null || buttonClickResultDOList.size() == 0) = true");
			return;
		}
		
		Set<ButtonClickResultQuery> buttonClickResultQuerieSet = new HashSet<ButtonClickResultQuery>();
		for(ButtonClickResultDO item : buttonClickResultDOList) {
			ButtonClickResultQuery buttonClickResultQuery = new ButtonClickResultQuery();
			buttonClickResultQuery.setEndTime(item.getEndTime());
			buttonClickResultQuery.setStartTime(item.getStartTime());
			buttonClickResultQuerieSet.add(buttonClickResultQuery);
		}
		
		// 逻辑删除已存在的数据(startTime相等 && endTime相等 && funnel_id相等)
		this.delete(buttonClickResultQuerieSet);
		
		this.insert(buttonClickResultDOList);
	}

	private void insert(List<ButtonClickResultDO> buttonClickResultDOList) {
		System.out.println("==========准备insert统计结果");
		
		String functionName = className + ".insert";
		
		if(buttonClickResultDOList == null || buttonClickResultDOList.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (buttonClickResultDOList == null || buttonClickResultDOList.size() == 0) = true");
			return;
		}
		
		System.out.println("==========buttonClickResultDOList.size = " + buttonClickResultDOList.size());
		
		String sql = "insert into bigdata_button_click_result "
				+ "(current_page_property, button_property, pv, uv, start_time, end_time, app_id, channel, creator, gmt_create, modifier, gmt_modified, is_deleted, remark, npv, nuv) "
				+ "values "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// ==========设置参数 ->start
		Object[][] params = new Object[buttonClickResultDOList.size()][];
		for (int index = 0; index < buttonClickResultDOList.size(); index++) {
			ButtonClickResultDO buttonClickResultDO = buttonClickResultDOList.get(index);
			params[index] = new Object[] {
				buttonClickResultDO.getCurrentPageProperty(), buttonClickResultDO.getButtonProperty(), buttonClickResultDO.getPv(),
				buttonClickResultDO.getUv(), simpleDateFormat.format(buttonClickResultDO.getStartTime()), simpleDateFormat.format(buttonClickResultDO.getEndTime()),
				buttonClickResultDO.getAppId(), buttonClickResultDO.getChannel(), buttonClickResultDO.getCreator(),
				simpleDateFormat.format(buttonClickResultDO.getGmtCreate()), buttonClickResultDO.getModifier(), simpleDateFormat.format(buttonClickResultDO.getGmtModified()),
				buttonClickResultDO.getIsDeleted(), buttonClickResultDO.getRemark(), buttonClickResultDO.getNpv(), buttonClickResultDO.getNuv()
			};
		}
		// ==========设置参数 ->end
		
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		try {
			qr.batch(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("==========insert统计结果完成");
	}

	private void delete(Set<ButtonClickResultQuery> querySet) {
		System.out.println("==========准备删除旧数据");
		
		String functionName = className + ".delete";
		
		if(querySet == null || querySet.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (querySet == null || querySet.size() == 0) = true");
			return;
		}
		querySet.forEach(item -> this.delete(item));
		System.out.println("==========删除旧数据完成");
	}

	private void delete(ButtonClickResultQuery query) {
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
			String sql = "update bigdata_button_click_result set is_deleted = 1 where id = ?";
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
	private List<Long> select(ButtonClickResultQuery query) {
		String functionName = className + ".select";
		
		if(query == null || query.getEndTime() == null || query.getStartTime() == null) {
			System.out.println("warn, function = " + functionName + ", (query == null || query.getEndTime() == null || query.getStartTime() == null) = true");
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<Long> list = null;
		String sql = "select id from bigdata_button_click_result where start_time = ? and end_time = ?";
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
