package com.sandu.cloud.activity.bargain.client;

import org.springframework.cloud.openfeign.FeignClient;
import com.sandu.cloud.notification.wechat.api.client.TemplateMsgServiceApiClient;

@FeignClient("sandu-cloud-notification-service")
public interface TemplateMsgServiceClient extends TemplateMsgServiceApiClient {
	
}


