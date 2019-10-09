package com.sandu.cloud.activity.bargain.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.sandu.cloud.notification.sms.api.client.SmsServiceApiClient;

@FeignClient("sandu-cloud-notification-service")
public interface SmsServiceClient extends SmsServiceApiClient {
	
}


