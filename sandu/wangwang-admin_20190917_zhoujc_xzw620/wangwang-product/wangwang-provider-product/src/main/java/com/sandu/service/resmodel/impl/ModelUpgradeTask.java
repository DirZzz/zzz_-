package com.sandu.service.resmodel.impl;

import com.sandu.service.resmodel.dao.ModelUpdateTask2019Dao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 模型定时任务
 * @author: chenqiang
 * @create: 2019-09-04 14:37
 */
@Slf4j
@Component
@EnableScheduling
public class ModelUpgradeTask {

    @Autowired
    private ModelUpdateTask2019Dao modelUpdateTask2019Dao;

    @Scheduled(cron = "0 0/5 * * * ?")
    private void configureTasks() {

        log.info("开始执行模型升级任务时间=" + LocalDateTime.now() + "-开始");

        // 查询预备任务表中 已关联产品的新增 or 修改的模型
        List<Integer> modelIdList = modelUpdateTask2019Dao.selectModelUpgradeId();

        if (null != modelIdList && modelIdList.size() > 0) {

            // 新增模型任务数据
            modelUpdateTask2019Dao.insertModelUpgradeTask(modelIdList);

            // 修改预备任务表模型状态
            modelUpdateTask2019Dao.updateModelState(modelIdList);
        }

        log.info("开始执行模型升级任务时间=" + LocalDateTime.now() + "-结束");
    }
}
