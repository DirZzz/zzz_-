package com.sandu.search.service.datasync.impl;

import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.datasync.handler.SyncDataHandler;
import com.sandu.search.entity.SyncParamVo;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.service.datasync.SyncFullSearchDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 同步数据到ES服务
 * @author xiaoxc
 * @data 2019/4/24 0024.
 */
@Slf4j
@Service("syncFullSearchDataService")
public class SyncFullSearchDataServiceImpl implements SyncFullSearchDataService {
    private static final String CLASS_LOG_PREFIX = "同步数据服务：";

    @Autowired
    private SyncDataHandler syncDataHandler;

    @Override
    public boolean syncFullSearchData(SyncParamVo syncParamVo) {

        if (null == syncParamVo || StringUtils.isBlank(syncParamVo.getIds())) {
            log.error(CLASS_LOG_PREFIX + "参数为空！syncParamVo:{}", JsonUtil.toJson(syncParamVo));
            return false;
        }
        String ids = syncParamVo.getIds();
        //业务类型
        String businessType = syncParamVo.getBusinessType();
        //操作类型
        Integer actionType = syncParamVo.getAction();
        //模块类型
        Integer moduleType = syncParamVo.getModule();

        if (StringUtils.isBlank(ids) || null == actionType || null == moduleType || 0 == actionType || 0 == moduleType) {
            log.error(CLASS_LOG_PREFIX + "ids:{},actionType:{},moduleType:" + moduleType , ids, actionType);
            return false;
        }

        log.info(CLASS_LOG_PREFIX + businessType + ",module:" + moduleType + ",action:" + actionType + ",ids:" + ids);

        List<String> idsList = null;
        if (!StringUtils.isEmpty(ids)) {
            idsList = new ArrayList<>(Arrays.asList(ids.split(",")));
        }
        if (idsList == null || idsList.size() < 1) {
            log.error(CLASS_LOG_PREFIX + "idsList is empty!idsList:{}", idsList);
            return false;
        }
        List<Integer> idList = StringUtil.transformInteger(idsList);

        try {
            syncDataHandler.syncDataByIds(idList, moduleType, actionType);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "更新ES数据异常, e:{}", e);
            return false;
        }

        return true;
    }
}
