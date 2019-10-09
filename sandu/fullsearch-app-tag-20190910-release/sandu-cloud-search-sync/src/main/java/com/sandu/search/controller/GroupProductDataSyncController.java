package com.sandu.search.controller;

import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.datasync.handler.GroupProductMessageHandler;
import com.sandu.search.service.index.DesignPlanIndexService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 组合产品数据同步接口
 *
 * @date 2019/03/27
 * @auth xiaoxc
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/sync/groupProduct")
public class GroupProductDataSyncController {

    private final static String CLASS_LOG_PREFIX = "[数据同步]组合产品信息同步接口:";

    private final GroupProductMessageHandler groupProductMessageHandler;
    private final StorageComponent storageComponent;
    private final DesignPlanIndexService designPlanIndexService;

    @Autowired
    public GroupProductDataSyncController(StorageComponent storageComponent, GroupProductMessageHandler groupProductMessageHandler, DesignPlanIndexService designPlanIndexService) {
        this.groupProductMessageHandler = groupProductMessageHandler;
        this.storageComponent = storageComponent;
        this.designPlanIndexService = designPlanIndexService;
    }

    /**
     * 强制更新组合产品
     *
     * @param groupIdList
     * @return
     */
    @PostMapping("/syncGroupProductByIds")
    String syncGroupProductByIds(@RequestBody List<Integer> groupIdList) {

        if (null == groupIdList || 0 >= groupIdList.size()) {
            log.info(CLASS_LOG_PREFIX + "同步组合产品信息失败，组合产品ID为空!");
            return CLASS_LOG_PREFIX + "同步组合产品信息失败，组合产品ID为空!";
        }
        log.info(CLASS_LOG_PREFIX + "Rest服务收到待同步方案，组合产品ID:{}", JsonUtil.toJson(groupIdList));

        //增量同步数据
        groupProductMessageHandler.sycnGroupProductData(groupIdList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);

        return CLASS_LOG_PREFIX + "组合产品已同步,组合产品ID:" + JsonUtil.toJson(groupIdList);
    }

}
