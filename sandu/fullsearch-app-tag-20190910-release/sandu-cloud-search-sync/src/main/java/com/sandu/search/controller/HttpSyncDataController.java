package com.sandu.search.controller;

import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.datasync.handler.GroupProductMessageHandler;
import com.sandu.search.datasync.handler.RecommendationMessageHandler;
import com.sandu.search.entity.SyncParamVo;
import com.sandu.search.entity.amqp.QueueMessage;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.service.index.DesignPlanIndexService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * http调用接口
 *
 * @date 2019/03/27
 * @auth xiaoxc
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/universal/data")
public class HttpSyncDataController {

    private final static String CLASS_LOG_PREFIX = "[数据同步]远程同步数据服务:";

    private final GroupProductMessageHandler groupProductMessageHandler;
    private final RecommendationMessageHandler recommendationMessageHandler;
    private final StorageComponent storageComponent;
    private final DesignPlanIndexService designPlanIndexService;

    @Autowired
    public HttpSyncDataController(RecommendationMessageHandler recommendationMessageHandler, StorageComponent storageComponent, GroupProductMessageHandler groupProductMessageHandler, DesignPlanIndexService designPlanIndexService) {
        this.groupProductMessageHandler = groupProductMessageHandler;
        this.storageComponent = storageComponent;
        this.designPlanIndexService = designPlanIndexService;
        this.recommendationMessageHandler = recommendationMessageHandler;
    }


    /**
     * 供远程调用同步数据
     *
     * @param syncParamVo
     * @return
     * @author xiaoxc
     */
    @PostMapping("/syncInfo")
    public String syncInfoByIds(@RequestBody SyncParamVo syncParamVo) {
        if (null == syncParamVo || StringUtils.isBlank(syncParamVo.getIds())) {
            log.error(CLASS_LOG_PREFIX + "参数为空！syncParamVo:{}", JsonUtil.toJson(syncParamVo));
            return "Param is empty!";
        }
        String ids = syncParamVo.getIds();
        //业务类型
        String businessType = syncParamVo.getBusinessType();
        //操作类型
        Integer actionType = syncParamVo.getAction();
        //模块类型
        Integer moduleType = syncParamVo.getModule();

        if (null == actionType || null == moduleType || 0 == actionType || 0 == moduleType) {
            return "Param is error!";
        }

        log.info(CLASS_LOG_PREFIX + businessType + ",module:" + moduleType + ",action:" + actionType + ",ids:" + ids);

        List<String> idsList = null;
        if (!StringUtils.isEmpty(ids)) {
            idsList = new ArrayList<>(Arrays.asList(ids.split(",")));
        }
        if (idsList == null || idsList.size() < 1) {
            return "ids is empty!";
        }
        List<Integer> idList = StringUtil.transformInteger(idsList);

        switch (moduleType) {
            //产品模块
            case QueueMessage.PRODUCT_MODULE:
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType || QueueMessage.UPDATE_ACTION == actionType) {
                    //新增|更新
                } else if (QueueMessage.DELETE_ACTION == actionType) {
                    //删除
                }
                break;
            //推荐方案
            case QueueMessage.RECOMMENDATION_MODULE:
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType
                        || QueueMessage.UPDATE_ACTION == actionType
                        || QueueMessage.DELETE_ACTION == actionType) {
                    //新增|更新|删除
                    recommendationMessageHandler.sycnRecommendationPlanData(idList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);
                }
                break;
            //组合产品
            case QueueMessage.GROUP_PRODUCT_MODULE:
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType || QueueMessage.UPDATE_ACTION == actionType) {
                    //更新
                    groupProductMessageHandler.sycnGroupProductData(idList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);
                } else if (QueueMessage.DELETE_ACTION == actionType) {
                    //删除
                    try {
                        groupProductMessageHandler.deleteByIds(idList);
                    } catch (ElasticSearchException e) {
                        log.error(CLASS_LOG_PREFIX + "删除组合产品失败，object：{},e:{}", JsonUtil.toJson(idList), e);
                    }
                }
                break;
            default:
                log.info(CLASS_LOG_PREFIX + "数据同步失败，同步数据模块类型错误,ModuleType:{}.", moduleType);
        }

        log.info(CLASS_LOG_PREFIX + "远程调用更新完成！");
        return "success";
    }

}
