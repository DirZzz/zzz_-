package com.sandu.service.system.dao;

import com.sandu.api.user.model.MiniProgramUserWxLocaltion;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseAreaDao {
    String selectAreaName(Integer areaId);

    String selectByAreaCode(String provinceCode);

    String selectAreaCodeByAreaNameLike(String areaName);

    int saveMiniProgramUserWxLocaltion(MiniProgramUserWxLocaltion localtion);

    int updateMiniProgramUserWxLocaltion(MiniProgramUserWxLocaltion localtion);

    MiniProgramUserWxLocaltion getMiniProWxLocaltionInfoByUserId(Long userId);
}
