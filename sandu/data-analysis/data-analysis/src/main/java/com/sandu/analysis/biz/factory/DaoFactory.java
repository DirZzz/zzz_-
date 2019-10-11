package com.sandu.analysis.biz.factory;

import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanStatisticsHourDao;
import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanUsageDao;
import com.sandu.analysis.biz.tob.plan.dao.impl.RecommendedPlanStatisticsHourDaoImpl;
import com.sandu.analysis.biz.tob.plan.dao.impl.RecommendedPlanUsageDaoImpl;
import com.sandu.analysis.biz.tob.user.dao.AreaStatisticsDao;
import com.sandu.analysis.biz.tob.house.dao.HouseStatisticsDao;
import com.sandu.analysis.biz.tob.house.dao.impl.HouseStatisticsDaoImpl;
import com.sandu.analysis.biz.tob.user.dao.CompanyStatisticsDao;
import com.sandu.analysis.biz.tob.user.dao.UserStatisticsDao;
import com.sandu.analysis.biz.tob.user.dao.impl.AreaStatisticsDaoImpl;
import com.sandu.analysis.biz.tob.user.dao.impl.CompanyStatisticsDaoImpl;
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
import com.sandu.analysis.biz.tob.plan.dao.PlanAnalysisResultDao;
import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanDao;
import com.sandu.analysis.biz.tob.plan.dao.impl.PlanAnalysisResultDaoImpl;
import com.sandu.analysis.biz.tob.plan.dao.impl.RecommendedPlanDaoImpl;
import com.sandu.analysis.biz.user.dao.UserAnalysisResultDao;
import com.sandu.analysis.biz.user.dao.UserDao;
import com.sandu.analysis.biz.user.dao.impl.UserAnalysisResultImpl;
import com.sandu.analysis.biz.user.dao.impl.UserDaoImpl;
import com.sandu.analysis.biz.tob.user.dao.impl.UserStatisticsDaoImpl;
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

	public static PlanAnalysisResultDao getPlanAnalysisResultDao() {
		return new PlanAnalysisResultDaoImpl();
	}

	public static RecommendedPlanDao getRecommendedPlanDao() {
		return new RecommendedPlanDaoImpl();
	}


	public static UserStatisticsDao getUserStatisticsDao(){
		return new UserStatisticsDaoImpl();
	}

	public static CompanyStatisticsDao getCompanyStatisticsDao(){
		return new CompanyStatisticsDaoImpl();
	}

	public static RecommendedPlanUsageDao getRecommendedPlanUsageDao(){
		return new RecommendedPlanUsageDaoImpl();
	}

	public static AreaStatisticsDao getAreaStatisticsDao(){
		return new AreaStatisticsDaoImpl();
	}

	public static HouseStatisticsDao getHouseStatisticsDao() {
		return new HouseStatisticsDaoImpl();
	}

	public static RecommendedPlanStatisticsHourDao getRecommendedPlanStatisticsHourDao(){
		return new RecommendedPlanStatisticsHourDaoImpl();
	}
}
