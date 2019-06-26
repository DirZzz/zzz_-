package com.sandu.analysis.biz.factory;

import com.sandu.analysis.biz.funnel.dao.FunnelDao;
import com.sandu.analysis.biz.funnel.dao.impl.FunnelDaoImpl;
import com.sandu.analysis.biz.otherEvent.dao.OtherEventAnalysisResultDao;
import com.sandu.analysis.biz.otherEvent.dao.impl.OtherEventAnalysisResultDaoImpl;
import com.sandu.analysis.biz.page.dao.ButtonClickResultDao;
import com.sandu.analysis.biz.page.dao.PageDao;
import com.sandu.analysis.biz.page.dao.PageViewResultDao;
import com.sandu.analysis.biz.page.dao.TestPagePVDao;
import com.sandu.analysis.biz.page.dao.impl.ButtonClickResultDaoImpl;
import com.sandu.analysis.biz.page.dao.impl.PageDaoImpl;
import com.sandu.analysis.biz.page.dao.impl.PageViewResultDaoImpl;
import com.sandu.analysis.biz.page.dao.impl.TestPagePVDaoImpl;
import com.sandu.analysis.biz.user.dao.UserAnalysisResultDao;
import com.sandu.analysis.biz.user.dao.UserDao;
import com.sandu.analysis.biz.user.dao.impl.UserAnalysisResultImpl;
import com.sandu.analysis.biz.user.dao.impl.UserDaoImpl;
import com.sandu.analysis.biz.usersRetention.dao.UserRetentionResultDao;
import com.sandu.analysis.biz.usersRetention.dao.impl.UserRetentionResultDaoImpl;

public class DaoFactory {

	public static PageDao getPageDao() {
		return new PageDaoImpl();
	}
	
	public static TestPagePVDao getTestPagePVDao() {
		return new TestPagePVDaoImpl();
	}
	
	public static FunnelDao getFunnelDao() {
		return new FunnelDaoImpl();
	}
	
	public static PageViewResultDao getPageViewResultDao() {
		return new PageViewResultDaoImpl();
	}
	
	public static ButtonClickResultDao getButtonClickResultDao() {
		return new ButtonClickResultDaoImpl();
	}
	
	public static UserDao getUserDao() {
		return new UserDaoImpl();
	}
	
	public static UserRetentionResultDao getUserRetentionResultDao() {
		return new UserRetentionResultDaoImpl();
	}

	public static UserAnalysisResultDao getUserAnalysisResultDao() {
		return new UserAnalysisResultImpl();
	}
	
	public static OtherEventAnalysisResultDao getOtherEventAnalysisResultDao() {
		return new OtherEventAnalysisResultDaoImpl();
	}
	
}
