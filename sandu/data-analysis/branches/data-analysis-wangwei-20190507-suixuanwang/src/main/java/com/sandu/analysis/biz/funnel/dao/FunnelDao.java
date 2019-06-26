package com.sandu.analysis.biz.funnel.dao;

import java.util.List;

import com.sandu.analysis.biz.funnel.model.BigdataFunnalAnalyzeResultDO;
import com.sandu.analysis.biz.funnel.model.BigdataFunnalAnalyzeResultQuery;
import com.sandu.analysis.biz.funnel.model.BigdataFunnalDetailBO;

public interface FunnelDao {

	void insertBeforeDelete(List<BigdataFunnalAnalyzeResultDO> list);

	List<Long> select(BigdataFunnalAnalyzeResultQuery query);

	/**
	 * 
	 * 
	 * @author huangsongbo
	 * @return
	 */
	List<BigdataFunnalDetailBO> selectFromBigdataFunnalDetailBOwhereEffective();

}
