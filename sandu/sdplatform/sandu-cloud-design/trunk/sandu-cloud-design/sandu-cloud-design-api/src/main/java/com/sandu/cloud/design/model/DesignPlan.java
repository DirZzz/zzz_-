package com.sandu.cloud.design.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

//@Table(name="base_design_plan")
@Data
public class DesignPlan{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    private String productCode;

    private String productName;

    private Double salePrice;

}