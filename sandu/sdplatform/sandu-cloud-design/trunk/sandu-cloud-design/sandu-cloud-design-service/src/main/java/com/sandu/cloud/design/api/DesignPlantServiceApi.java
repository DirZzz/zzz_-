package com.sandu.cloud.design.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sandu.cloud.design.model.DesignPlan;
import com.sandu.cloud.design.service.DesignPlanService;

@RestController
@RequestMapping("/api/v1/product")
public class DesignPlantServiceApi {
	
	@Autowired
	private DesignPlanService designPlanService;
	

	@GetMapping(path = "/get")
	public DesignPlan get(long id) {
		return designPlanService.get(id);
	}

	
}
