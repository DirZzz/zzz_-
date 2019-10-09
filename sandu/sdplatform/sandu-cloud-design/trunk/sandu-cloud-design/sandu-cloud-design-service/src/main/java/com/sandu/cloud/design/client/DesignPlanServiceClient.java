package com.sandu.cloud.design.client;



import org.springframework.cloud.openfeign.FeignClient;

import com.sandu.cloud.user.api.client.UserServiceApiClient;


@FeignClient("sandu-cloud-user-service")
public interface DesignPlanServiceClient extends UserServiceApiClient {
	
}
