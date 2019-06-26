package com.sandu.analysis.biz.usersRetention.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.sandu.analysis.biz.usersRetention.dao.UserRetentionResultDao;
import com.sandu.analysis.biz.usersRetention.model.UserRetentionResultDO;
import com.sandu.analysis.biz.usersRetention.model.UserRetentionResultForUpdateDO;
import com.sandu.analysis.biz.usersRetention.model.UserRetentionResultQuery;
import com.sandu.analysis.util.JdbcUtils;

public class UserRetentionResultDaoImpl implements UserRetentionResultDao {

	private String className = this.getClass().getName();
	
	@Override
	public void insertBeforeDelete(UserRetentionResultDO userRetentionResultDO) {
		String functionName = className + ".insertBeforeDelete";
		
		if(userRetentionResultDO == null) {
			System.out.println("warn, function = " + functionName + ", message = (userRetentionResultDO == null) = true");
			return;
		}
		
		UserRetentionResultQuery userRetentionResultQuery = new UserRetentionResultQuery();
		userRetentionResultQuery.setEndTime(userRetentionResultDO.getEndTime());
		userRetentionResultQuery.setStartTime(userRetentionResultDO.getStartTime());
		
		// 逻辑删除已存在的数据(startTime相等 && endTime相等 && funnel_id相等)
		this.delete(userRetentionResultQuery);
		
		this.insert(Arrays.asList(userRetentionResultDO));
	}

	private void insert(List<UserRetentionResultDO> list) {
		String functionName = className + ".insert";
		
		if(list == null || list.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (list == null || list.size() == 0) = true");
			return;
		}
		String sql = "insert into bigdata_user_retention_result "
				+ "(start_time, end_time, new_user_count, average_pv, one_day_retention_count, three_day_retention_count, "
				+ "seven_day_retention_count, thirty_day_retention_count, app_id, channel, creator, gmt_create, modifier, gmt_modified, is_deleted, remark)"
				+ " values "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// ==========设置参数 ->start
		Object[][] params = new Object[list.size()][];
		for (int index = 0; index < list.size(); index++) {
			UserRetentionResultDO userRetentionResultDO = list.get(index);
			params[index] = new Object[] {
					simpleDateFormat.format(userRetentionResultDO.getStartTime()), simpleDateFormat.format(userRetentionResultDO.getEndTime()), userRetentionResultDO.getNewUserCount(),
					userRetentionResultDO.getAveragePv(), userRetentionResultDO.getOneDayRetentionCount(), userRetentionResultDO.getThreeDayRetentionCount(),
					userRetentionResultDO.getSevenDayRetentionCount(), userRetentionResultDO.getThirtyDayRetentionCount(), userRetentionResultDO.getAppId(),
					userRetentionResultDO.getChannel(), userRetentionResultDO.getCreator(), simpleDateFormat.format(userRetentionResultDO.getGmtCreate()),
					userRetentionResultDO.getModifier(), simpleDateFormat.format(userRetentionResultDO.getGmtModified()), userRetentionResultDO.getIsDeleted(),
					userRetentionResultDO.getRemark()
			};
		}
		// ==========设置参数 ->end
		
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		try {
			qr.batch(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void delete(UserRetentionResultQuery query) {
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
			String sql = "update bigdata_user_retention_result set is_deleted = 1 where id = ?";
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
	private List<Long> select(UserRetentionResultQuery query) {
		String functionName = className + ".select";
		
		if(query == null || query.getEndTime() == null || query.getStartTime() == null) {
			System.out.println("warn, function = " + functionName + ", (query == null || query.getEndTime() == null || query.getStartTime() == null) = true");
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<Long> list = null;
		String sql = "select id from bigdata_user_retention_result where start_time = ? and end_time = ?";
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

	@SuppressWarnings("deprecation")
	@Override
	public List<UserRetentionResultDO> select(LocalDateTime startTime, LocalDateTime endTime) {
		String functionName = className + "select";
		
		if(startTime == null || endTime == null) {
			System.out.println("warn, function = " + functionName + ", message = (startTime == null || endTime == null) = true");
			return null;
		}
		String sql = "select "
				+ "id, start_time, end_time, new_user_count, average_pv, one_day_retention_count, three_day_retention_count, "
				+ "seven_day_retention_count, thirty_day_retention_count, app_id, channel, creator, gmt_create, modifier, "
				+ "gmt_modified, is_deleted, remark from bigdata_user_retention_result "
				+ "where "
				+ "start_time = ? and end_time = ? and is_deleted = 0";
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		BeanProcessor bean = new GenerousBeanProcessor();
		RowProcessor processor = new BasicRowProcessor(bean);
		Object[] params = new Object[] {
			startTime.toString(), endTime.toString()
		};
		
		List<UserRetentionResultDO> list = null;
		try {
			list = qr.query(sql, params, new BeanListHandler<UserRetentionResultDO>(UserRetentionResultDO.class, processor));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public void update(UserRetentionResultForUpdateDO userRetentionResultForUpdateDO) {
		String functionName = className + "update";
		
		if(userRetentionResultForUpdateDO == null) {
			System.out.println("warn, function = " + functionName + ", message = (userRetentionResultForUpdateDO == null) = true");
			return;
		}
		if(userRetentionResultForUpdateDO.getId() == null) {
			System.out.println("warn, function = " + functionName + ", message = (userRetentionResultForUpdateDO.getId() == null) = true");
			return;
		}
		if(
				userRetentionResultForUpdateDO.getOneDayRetentionCount() == null 
				&& userRetentionResultForUpdateDO.getThreeDayRetentionCount() == null
				&& userRetentionResultForUpdateDO.getSevenDayRetentionCount() == null
				&& userRetentionResultForUpdateDO.getThirtyDayRetentionCount() == null
				) {
			System.out.println("warn, function = " + functionName 
					+ ", message = (userRetentionResultForUpdateDO.getOneDayRetentionCount() == null "
					+ "&& userRetentionResultForUpdateDO.getThreeDayRetentionCount() == null "
					+ "&& userRetentionResultForUpdateDO.getSevenDayRetentionCount() == null "
					+ "&& userRetentionResultForUpdateDO.getThirtyDayRetentionCount() == null) = true");
			return;
		}
		
		// ==========生成sql ->start
		String sql1 = "update bigdata_user_retention_result set ";
		String sql2 = (
					((userRetentionResultForUpdateDO.getOneDayRetentionCount() != null) ? ", one_day_retention_count = " + userRetentionResultForUpdateDO.getOneDayRetentionCount() : "")
					+ ((userRetentionResultForUpdateDO.getThreeDayRetentionCount() != null) ? ", three_day_retention_count = " + userRetentionResultForUpdateDO.getThreeDayRetentionCount() : "")
					+ ((userRetentionResultForUpdateDO.getSevenDayRetentionCount() != null) ? ", seven_day_retention_count = " + userRetentionResultForUpdateDO.getSevenDayRetentionCount() : "")
					+ ((userRetentionResultForUpdateDO.getThirtyDayRetentionCount() != null) ? ", thirty_day_retention_count = " + userRetentionResultForUpdateDO.getThirtyDayRetentionCount() : "")
				)
				.substring(1);
		String sql3 = " where id = " + userRetentionResultForUpdateDO.getId();
		String sql = sql1 + sql2 + sql3;
		// ==========生成sql ->end
		
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		
		try {
			qr.update(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
