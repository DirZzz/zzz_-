package com.sandu.analysis.test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.junit.Test;

import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.factory.DaoFactory;
import com.sandu.analysis.biz.page.dao.PageDao;
import com.sandu.analysis.biz.page.model.Page;
import com.sandu.analysis.util.JdbcUtils;

public class TestJDBC {

	public static void main(String[] args) {
		Page page = new Page();
		page.setName("aa");
		PageDao pageDao = DaoFactory.getPageDao();
		pageDao.insert(page);
	}
	
	@Test
	public void test001() {

		/*String sql = "select uuid from app_online_30.sys_user where is_deleted = 0 and id in (?)";*/
		String sql = "select uuid from app_online_30.sys_user where is_deleted = 0 and id in (234532, 234526)";
		Object[] params = new Object[] {
				"()"
		};
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		
		List<String> uuidList = null;
		try {
			uuidList = qr.query(sql, params, new ColumnListHandler<String>("uuid"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(uuidList);
	}
	
}
