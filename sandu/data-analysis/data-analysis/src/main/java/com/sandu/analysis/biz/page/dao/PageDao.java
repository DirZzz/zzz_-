package com.sandu.analysis.biz.page.dao;

import java.util.List;

import com.sandu.analysis.biz.page.model.PageViewResultDO;
import com.sandu.analysis.biz.page.model.Page;

public interface PageDao {

	public void insert(Page user);
	
	public Page selectById(Long id);

}
