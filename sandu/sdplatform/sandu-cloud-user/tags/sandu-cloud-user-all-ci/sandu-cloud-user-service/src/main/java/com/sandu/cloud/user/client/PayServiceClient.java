package com.sandu.cloud.user.client;



import org.springframework.cloud.openfeign.FeignClient;

import com.sandu.cloud.pay.api.client.PayServiceApiClient;

/**
 * @author WTD
 * @createDate 2019-03-08 20:16
 */
@FeignClient("sandu-cloud-pay-service")
public interface PayServiceClient extends PayServiceApiClient {
	
}
