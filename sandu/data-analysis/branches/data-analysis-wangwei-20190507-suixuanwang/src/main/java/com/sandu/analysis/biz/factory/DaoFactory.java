package com.sandu.analysis.biz.factory;

import com.sandu.analysis.biz.funnel.dao.FunnelDao;
import com.sandu.analysis.biz.funnel.dao.impl.FunnelDaoImpl;
import com.sandu.analysis.biz.page.dao.PageDao;
import com.sandu.analysis.biz.page.dao.TestPagePVDao;
import com.sandu.analysis.biz.page.dao.impl.PageDaoImpl;
import com.sandu.analysis.biz.page.dao.impl.TestPagePVDaoImpl;

public class DaoFactory {

	public static PageDao getUserDao() {
		return new PageDaoImpl();
	}
	
	public static TestPagePVDao getTestPagePVDao() {
		return new TestPagePVDaoImpl();
	}
	
	public static FunnelDao getFunnelDao() {
		return new FunnelDaoImpl();
	}
	
}
