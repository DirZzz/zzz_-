package com.sandu.cloud.activity.bargain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class BargainRegCutResultDto {
	
	private String regOwnerOpenId;
	
	private String regId;
	
    private String productName;
     
    private Double cutPrice;
    
    private boolean complete;
    
}

