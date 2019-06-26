package com.sandu.analysis.biz.user.dao;

import com.sandu.analysis.biz.user.model.UserAnalysisResultDto;

import java.util.List;

public interface UserAnalysisResultDao {

    public void insert(List<UserAnalysisResultDto> dtoList);

    /**
     * 首先检测有没有对应时间&&类型的数据, 如果有, 先逻辑删除
     * @param userAnalysisResultDtoList
     */
    public void insertBeforeDelete(List<UserAnalysisResultDto> userAnalysisResultDtoList);
}
