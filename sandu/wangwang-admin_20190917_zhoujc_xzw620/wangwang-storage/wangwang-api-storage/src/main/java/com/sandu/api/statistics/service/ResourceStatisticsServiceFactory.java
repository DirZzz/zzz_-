package com.sandu.api.statistics.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: chenqiang
 * @create: 2019-05-25 17:32
 */
@Component
public class ResourceStatisticsServiceFactory implements ApplicationContextAware {

    private static Map<ResourceStatisticsServiceEnum,ResourceStatisticsService> resourceStatisticsServiceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String,ResourceStatisticsService> serviceMap = applicationContext.getBeansOfType(ResourceStatisticsService.class);

        resourceStatisticsServiceMap = new HashMap<>();
        serviceMap.forEach((key,value) -> resourceStatisticsServiceMap.put(value.getServiceImplKey(),value));
    }

    /**
     * 根据枚举类型获取实现类
     * @param serviceEnum
     * @return
     */
    public ResourceStatisticsService getResourceStatisticsService(ResourceStatisticsServiceEnum serviceEnum) {
        return resourceStatisticsServiceMap.get(serviceEnum);
    }
}
