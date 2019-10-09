package com.sandu.cloud.pay.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInfo{
	
	/**
	 * 客户端id
	 */
	private String clientId;
	
	/**
	 * 客户端名称
	 */
	private String clientName;
	
	/**
	 *客户端密码
	 */
	private String clientSecret;
	
	
}
