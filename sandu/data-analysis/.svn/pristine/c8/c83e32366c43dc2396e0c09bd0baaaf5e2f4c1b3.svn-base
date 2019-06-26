package com.sandu.analysis.biz.page.dao;

import java.util.List;

import com.sandu.analysis.biz.page.model.PageViewResultDO;

public interface PageViewResultDao {

	public void insert(List<PageViewResultDO> bigdataPageViewResultDOList);

	/**
	 * 首先检测有没有对应时间的数据, 如果有, 先删除
	 * @param bigdataPageViewResultDOList
	 */
	public void insertBeforeDelete(List<PageViewResultDO> bigdataPageViewResultDOList);
}
