package com.sandu.search.datasync.task;

import com.sandu.search.datasync.handler.RecommendationMessageHandler;
import com.sandu.search.initialize.GroupProductIndex;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步组合产品数据任务
 *
 * @author xiaoxc
 * @date 2019-03-20
 */
@Slf4j
@Component
public class GroupProductTask {

    private final static String CLASS_LOG_PREFIX = "同步组合产品数据任务:";

    private final StorageComponent storageComponent;
    private final GroupProductIndex groupProductIndex;
    private final RecommendationMessageHandler recommendationMessageHandler;

    //全量任务执行次数
    private static int fullTaskExcuteCount = 0;
    //增量任务执行次数
    private static int incrementTaskExcuteCount = 0;
    // 是否正在执行全量更新(作用:1.避免同时执行两个全量更新;2.避免执行全量更新的同时执行增量更新)
    public static boolean isRuningFullTask = false;


    @Autowired
    public GroupProductTask(RecommendationMessageHandler recommendationMessageHandler, StorageComponent storageComponent, GroupProductIndex groupProductIndex) {
        this.storageComponent = storageComponent;
        this.groupProductIndex = groupProductIndex;
        this.recommendationMessageHandler = recommendationMessageHandler;
    }

    /**
     * 组合产品数据全量同步
     *
     * @date 2019/03/20
     * @auth xiaoxc
     * @strategy 每天凌晨4点执行
     */
    @Scheduled(cron = "${task.groupproduct.all.cron}")
    public void groupProductDataSyncTask() {
        //任务开始时间
        long startTime = System.currentTimeMillis();

        log.info(CLASS_LOG_PREFIX + "准备执行组合产品全量更新...");

        //第一次任务跳过
        if (0 == fullTaskExcuteCount) {
            ++fullTaskExcuteCount;
            log.info(CLASS_LOG_PREFIX + "组合产品数据全量同步,第一次任务跳过...");
            return;
        }

        //组合产品全量更新标识
        GroupProductTask.isRuningFullTask = true;
        //索引全屋方案到组合产品表
        log.info(CLASS_LOG_PREFIX + "开始同步组合产品数据...");
        groupProductIndex.syncGroupProductInfoData();
        log.info(CLASS_LOG_PREFIX + "开始全量索引组合产品数据完成...耗时:{}ms", (System.currentTimeMillis() - startTime));
    }

}
