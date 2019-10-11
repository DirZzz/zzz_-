package com.sandu.analysis.test.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class LogBean implements Serializable {

	private static final long serialVersionUID = -5383155183367794576L;

	private String userId;
	
	private String eventProperty;
	
}
