package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 方案报价
 * @author Sandu
 * @ClassName PlanQuotedPricePO
 * @date 2019/5/22-10:47$
 */
@Data
public class PlanQuotedPricePO implements Serializable {
	private String cityCode;

	private Double price;

	private Integer priceMode;
	
	private Integer planId;

}
