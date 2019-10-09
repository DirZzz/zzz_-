package com.sandu.cloud.activity.bargain.client;

import org.springframework.cloud.openfeign.FeignClient;
import com.sandu.cloud.company.api.client.CompanyServiceApiClient;

@FeignClient("sandu-cloud-company-service")
public interface CompanyServiceClient extends CompanyServiceApiClient {
	
}


