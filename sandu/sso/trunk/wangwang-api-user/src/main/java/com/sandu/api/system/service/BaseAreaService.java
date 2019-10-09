package com.sandu.api.system.service;

import com.sandu.api.user.model.MiniProgramUserWxLocaltion;

public interface BaseAreaService {
    String getAreaName(Integer areaId);

    String selectByAreaCode(String provinceCode);

    String findAreaCodeLike(String provinceName);

    int saveMiniProgramUserWxLocaltion(MiniProgramUserWxLocaltion localtion);

    int updateMiniProgramUserWxLocaltion(MiniProgramUserWxLocaltion localtion);

    MiniProgramUserWxLocaltion getMiniProWxLocaltionInfoByUserId(Long id);
}
