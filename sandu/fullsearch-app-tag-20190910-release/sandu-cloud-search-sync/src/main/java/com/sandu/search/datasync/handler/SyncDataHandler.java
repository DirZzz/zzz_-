package com.sandu.search.datasync.handler;

import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.entity.amqp.QueueMessage;
import com.sandu.search.exception.ElasticSearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息消费处理
 *
 * @date 20190425
 * @auth xiaoxc
 */
@Slf4j
@Component
public class SyncDataHandler {

    private final ProductMessageHandler productMessageHandler;
    private final RecommendationMessageHandler recommendationMessageHandler;
    private final GroupProductMessageHandler groupProductMessageHandler;

    private final static String CLASS_LOG_PREFIX = "同步消息消费处理:";

    @Autowired
    public SyncDataHandler(GroupProductMessageHandler groupProductMessageHandler,
                           ProductMessageHandler productMessageHandler,
                           RecommendationMessageHandler recommendationMessageHandler) {
        this.productMessageHandler = productMessageHandler;
        this.recommendationMessageHandler = recommendationMessageHandler;
        this.groupProductMessageHandler = groupProductMessageHandler;
    }

    public void syncDataByIds(List<Integer> idList, int moduleType, int actionType) throws ElasticSearchException {

        if (null == idList || idList.size() == 0) {
            log.error(CLASS_LOG_PREFIX + "更新对象为空！");
            return;
        }
        //消息分发
        switch (moduleType) {
            //产品模块
            case QueueMessage.PRODUCT_MODULE:
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType || QueueMessage.UPDATE_ACTION == actionType) {
                    //新增|更新
                } else if (QueueMessage.DELETE_ACTION == actionType) {
                    //删除
                    productMessageHandler.deleteByIds(idList);
                } else {
                    log.info(CLASS_LOG_PREFIX + "操作类型错误,actionType:{}", actionType);
                }
                break;
            //推荐方案
            case QueueMessage.RECOMMENDATION_MODULE:
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType
                        || QueueMessage.UPDATE_ACTION == actionType) {
                    //新增|更新
                    recommendationMessageHandler.sycnRecommendationPlanData(idList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);
                }else if (QueueMessage.DELETE_ACTION == actionType) {
                    //删除
                    recommendationMessageHandler.deleteByIds(idList);
                } else {
                    log.info(CLASS_LOG_PREFIX + "操作类型错误,actionType:{}", actionType);
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
                    groupProductMessageHandler.deleteByIds(idList);
                } else {
                    log.info(CLASS_LOG_PREFIX + "操作类型错误,actionType:{}", actionType);
                }
                break;
            default:
                log.info(CLASS_LOG_PREFIX + "数据同步失败，同步数据模块类型错误,ModuleType:{}.", moduleType);
        }

        log.info(CLASS_LOG_PREFIX + "消息消费完成，消息消费状态:{},消息Model:{}, Ids:" + idList, actionType, moduleType);
    }

}
