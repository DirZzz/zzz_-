package com.sandu.service.resmodel.dao;

import com.sandu.api.resmodel.model.ModelUpdateTask2019;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelUpdateTask2019Dao {

    /**
     * 新增模型资源
     */
    int insertSelective(ModelUpdateTask2019 record);

    int insertSelectiveTaskWait(ModelUpdateTask2019 record);

    List<Integer> selectModelUpgradeId();

    int updateModelState(@Param("idList") List<Integer> idList);

    int insertModelUpgradeTask(@Param("idList") List<Integer> idList);
}