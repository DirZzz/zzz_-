package com.sandu.search.config;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sandu.search.initialize.LivingIndex;
import com.sandu.search.service.index.HouseIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.CollectionUtils;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulingConfig implements SchedulingConfigurer {

	@Autowired
	private LivingIndex livingIndex;

	@Autowired
	private HouseIndexService houseIndexService;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(10);
	}


	@Scheduled(cron = "1 * * * * ? ")
	public void syncUpdateElasticSearchData(){
		log.info("################################定时同步更改户型数据的小区########################################");
		//获取一分钟前有更新的户型数据
		List<Integer> houseLivingIds =  houseIndexService.queryHouseChangeBeforeOneMin();

		if (CollectionUtils.isEmpty(houseLivingIds)) return;

		long start = System.currentTimeMillis();
		log.info("定时同步数据到es开始,livingIds:{}" + houseLivingIds);
		livingIndex.indexLivingData(houseLivingIds);
		log.info("定时同步数据到es结束,耗时:{}",System.currentTimeMillis() - start,"ms");
	}
}
