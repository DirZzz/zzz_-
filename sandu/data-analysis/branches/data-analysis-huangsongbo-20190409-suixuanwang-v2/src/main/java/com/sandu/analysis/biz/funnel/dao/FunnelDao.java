package com.sandu.analysis.biz.funnel.dao;

import java.util.List;

import com.sandu.analysis.biz.funnel.model.BigdataFunnelAnalysisResultDO;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelAnalysisResultQuery;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelDetailBO;

public interface FunnelDao {

	void insertBeforeDelete(List<BigdataFunnelAnalysisResultDO> list);

	List<Long> select(BigdataFunnelAnalysisResultQuery query);

	/**
	 * 
	 * 
	 * @author huangsongbo
	 * @return
	 */
	List<BigdataFunnelDetailBO> selectFromBigdataFunnelDetailBOwhereEffective();

}
