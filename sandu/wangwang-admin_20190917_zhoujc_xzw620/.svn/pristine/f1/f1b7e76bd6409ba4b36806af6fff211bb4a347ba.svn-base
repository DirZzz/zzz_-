package com.sandu.service.solution.dao;

import com.sandu.api.solution.model.MiniProObtainMobileDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiniProObtainMobileDetailMapper {
    List<MiniProObtainMobileDetail> listByAppId(String appid);

    void batchInsert(@Param("insertList") List<MiniProObtainMobileDetail> insertList);

    void updateBatch(@Param("updateList")List<MiniProObtainMobileDetail> updateList);

    List<MiniProObtainMobileDetail> findListLimit1();

    void updateCurRemainNumByIds(@Param("ids") List<Long> ids);

    List<Long> findObtainDetailIdsByCompanyId(Integer companyId);
}
