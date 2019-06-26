package com.sandu.analysis.biz.page.dao;

import java.util.List;

import com.sandu.analysis.biz.page.model.ButtonClickResultDO;

public interface ButtonClickResultDao {

	/**
	 * 第一步: 先根据startTime + endTime检查有没有已存在的数据, 如果有, 先删除
	 * 第二步, insert #{buttonClickResultDOList}
	 * 
	 * @author huangsongbo
	 * @param buttonClickResultDOList
	 */
	void insertBeforeDelete(List<ButtonClickResultDO> buttonClickResultDOList);

}
