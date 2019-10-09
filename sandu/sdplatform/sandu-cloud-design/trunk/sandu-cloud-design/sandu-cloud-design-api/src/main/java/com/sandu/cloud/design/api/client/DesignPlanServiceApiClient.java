package com.sandu.cloud.design.api.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sandu.cloud.design.model.DesignPlan;

@RequestMapping("/api/v1/product")
public interface DesignPlanServiceApiClient {

	@GetMapping(path = "/get")
	DesignPlan get(long id);

}
