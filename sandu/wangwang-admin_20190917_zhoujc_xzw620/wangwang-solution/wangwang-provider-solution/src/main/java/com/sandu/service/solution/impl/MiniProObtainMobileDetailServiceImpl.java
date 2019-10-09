package com.sandu.service.solution.impl;

import com.sandu.api.solution.model.MiniProObtainMobileDetail;
import com.sandu.api.solution.service.MiniProObtainMobileDetailService;
import com.sandu.service.solution.dao.MiniProObtainMobileDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("miniProObtainMobileDetailService")
public class MiniProObtainMobileDetailServiceImpl implements MiniProObtainMobileDetailService {

    @Autowired
    private MiniProObtainMobileDetailMapper miniProObtainMobileDetailMapper;

    @Override
    public List<MiniProObtainMobileDetail> listByAppId(String appid) {
        return miniProObtainMobileDetailMapper.listByAppId(appid);
    }

    @Override
    public void batchInsert(List<MiniProObtainMobileDetail> insertList) {
         miniProObtainMobileDetailMapper.batchInsert(insertList);
    }

    @Override
    public void updateBatch(List<MiniProObtainMobileDetail> updateList) {
         miniProObtainMobileDetailMapper.updateBatch(updateList);
    }

    @Override
    public List<MiniProObtainMobileDetail> findListLimit1() {
        return miniProObtainMobileDetailMapper.findListLimit1();
    }

    @Override
    public void updateCurRemainNumByIds(List<Long> ids) {
        miniProObtainMobileDetailMapper.updateCurRemainNumByIds(ids);
    }

    @Override
    public List<Long> findObtainDetailIdsByCompanyId(Integer companyId) {
        return miniProObtainMobileDetailMapper.findObtainDetailIdsByCompanyId(companyId);
    }
}
