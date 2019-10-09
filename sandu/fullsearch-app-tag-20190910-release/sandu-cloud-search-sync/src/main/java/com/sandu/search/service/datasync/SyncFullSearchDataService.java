package com.sandu.search.service.datasync;

import com.sandu.search.entity.SyncParamVo;

/**
 * 同步数据到ES
 * @author xiaoxc
 * @data 2019/4/24 0024.
 */
public interface SyncFullSearchDataService {

    boolean syncFullSearchData(SyncParamVo syncParamVo);
}
