package com.sandu.analysis.biz.otherEvent.dao;

import java.util.List;

import com.sandu.analysis.biz.otherEvent.model.OtherEventAnalysisResultDO;

public interface OtherEventAnalysisResultDao {

	void insertBeforeDelete(List<OtherEventAnalysisResultDO> list);

}
