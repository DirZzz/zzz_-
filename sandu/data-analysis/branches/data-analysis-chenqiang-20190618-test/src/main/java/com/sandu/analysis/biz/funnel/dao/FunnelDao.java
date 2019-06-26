package com.sandu.analysis.biz.funnel.dao;

import java.util.List;

import com.sandu.analysis.biz.funnel.model.BigdataFunnalAnalysisResultDO;
import com.sandu.analysis.biz.funnel.model.BigdataFunnalAnalysisResultQuery;
import com.sandu.analysis.biz.funnel.model.BigdataFunnalDetailBO;

public interface FunnelDao {

	void insertBeforeDelete(List<BigdataFunnalAnalysisResultDO> list);

	List<Long> select(BigdataFunnalAnalysisResultQuery query);

	/**
	 * 
	 * 
	 * @author huangsongbo
	 * @return
	 */
	List<BigdataFunnalDetailBO> selectFromBigdataFunnalDetailBOwhereEffective();

}
