package com.sandu.api.statistics.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: chenqiang
 * @create: 2019-05-25 16:27
 */
@Component
public interface ResourceStatisticsService {

//    ExecutorService pool = Executors.newFixedThreadPool(10);
    ExecutorService pool = Executors.newCachedThreadPool();

    void resourceStatistics() throws Exception;

    ResourceStatisticsServiceEnum getServiceImplKey();


}
