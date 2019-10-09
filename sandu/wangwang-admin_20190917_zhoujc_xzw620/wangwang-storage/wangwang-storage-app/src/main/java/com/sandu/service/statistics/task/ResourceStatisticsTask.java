package com.sandu.service.statistics.task;

import com.sandu.api.statistics.service.ResourceStatisticsServiceEnum;
import com.sandu.api.statistics.service.ResourceStatisticsServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: chenqiang
 * @create: 2019-05-23 15:25
 */
@Slf4j
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class ResourceStatisticsTask {

    @Autowired
    private ResourceStatisticsServiceFactory resourceStatisticsServiceFactory;

    @Async
//    @Scheduled(cron = "0 0 1 * * ?")
    public void userResourceStatistics () throws InterruptedException {

        try {

            // 调用用户实现类统计资源
            resourceStatisticsServiceFactory.getResourceStatisticsService(ResourceStatisticsServiceEnum.userResourceStatisticsServiceImpl).resourceStatistics();

        }catch (Exception e){

            log.error("调用用户实现类统计资源 异常",e);
            e.printStackTrace();
            System.out.println(e);
        }


    }

    @Async
//    @Scheduled(cron = "0 0 5 * * ?")
    public void companyResourceStatistics() {

        try {

            // 调用企业实现类统计资源
            resourceStatisticsServiceFactory.getResourceStatisticsService(ResourceStatisticsServiceEnum.companyResourceStatisticsServiceImpl).resourceStatistics();

        }catch (Exception e){

            log.error("调用企业实现类统计资源 异常",e);
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Async
//    @Scheduled(cron = "0 0 3 * * ?")
    public void shareResourceStatistics() {

        try {

            // 调用共享实现类统计资源
            resourceStatisticsServiceFactory.getResourceStatisticsService(ResourceStatisticsServiceEnum.shareResourceStatisticsServiceImpl).resourceStatistics();

        }catch (Exception e){

            log.error("调用共享实现类统计资源 异常",e);
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Async
//    @Scheduled(cron = "0 0 7 * * ?")
    public void otherResourceStatistics() {

        try {

            // 调用其他资源实现类统计资源
            resourceStatisticsServiceFactory.getResourceStatisticsService(ResourceStatisticsServiceEnum.otherResourceStatisticsServiceImpl).resourceStatistics();

        }catch (Exception e){

            log.error("调用其他资源实现类统计资源 异常",e);
            e.printStackTrace();
            System.out.println(e);
        }

    }

}
