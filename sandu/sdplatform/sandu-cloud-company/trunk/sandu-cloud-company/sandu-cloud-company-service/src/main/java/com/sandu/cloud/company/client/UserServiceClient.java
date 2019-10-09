package com.sandu.cloud.company.client;



import org.springframework.cloud.openfeign.FeignClient;

import com.sandu.cloud.user.api.client.UserServiceApiClient;


@FeignClient("sandu-cloud-user-service")
public interface UserServiceClient extends UserServiceApiClient {
	
}
