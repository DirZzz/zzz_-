package com.sandu.analysis.biz.page.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ButtonClickResultQuery {

	private Date startTime;
	
	private Date endTime;
	
}
