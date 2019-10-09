package com.sandu.api.solution.service;

import com.sandu.api.solution.model.MiniProObtainMobileDetail;

import java.util.List;

public interface MiniProObtainMobileDetailService {
    List<MiniProObtainMobileDetail> listByAppId(String appid);

    void batchInsert(List<MiniProObtainMobileDetail> insertList);

    void updateBatch(List<MiniProObtainMobileDetail> updateList);

    List<MiniProObtainMobileDetail> findListLimit1();

    void updateCurRemainNumByIds(List<Long> ids);

    List<Long> findObtainDetailIdsByCompanyId(Integer companyId);
}
