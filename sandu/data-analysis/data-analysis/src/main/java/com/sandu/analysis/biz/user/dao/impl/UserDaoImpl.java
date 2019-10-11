package com.sandu.analysis.biz.user.dao.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.sandu.analysis.biz.user.dao.UserDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.util.JdbcUtils;

public class UserDaoImpl implements UserDao {

	String className = this.getClass().getName();
	
	@SuppressWarnings("deprecation")
	@Override
	public List<String> selectUuid(LocalDateTime startTime, LocalDateTime endTime) {
		String functionName = className + ".selectUuid";
		
		if(startTime == null || endTime == null) {
			System.out.println("warn, function = " + functionName + ", message = (startTime == null || endTime == null) = true");
			return null;
		}
		
		String sql = "select uuid from app_online_30.sys_user where is_deleted = 0 and gmt_create >= ? and gmt_create < ?";
		Object[] params = new Object[] {
				startTime.format(CommonConstants.DATE_TIME_FORMATTER_DEFAULT), endTime.format(CommonConstants.DATE_TIME_FORMATTER_DEFAULT)
		};
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		
		List<String> uuidList = null;
		try {
			uuidList = qr.query(sql, params, new ColumnListHandler<String>("uuid"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return uuidList;
	}

}
