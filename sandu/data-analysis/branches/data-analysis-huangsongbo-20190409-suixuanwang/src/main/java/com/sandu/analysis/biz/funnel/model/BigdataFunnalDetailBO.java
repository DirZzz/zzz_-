package com.sandu.analysis.biz.funnel.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class BigdataFunnalDetailBO implements Serializable {
	
	private static final long serialVersionUID = -5176817396917714355L;

	private Long id;
	
	private String nodeName;
	
	private Integer nodeSeq;
	
	private String nodeEventType;
	
	private String nodeEventProperty;
	
	private Long funnelId;

	private String appId;
	
}
