package com.sandu.cloud.notification.wechat.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TemplateMsgReqParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message="openid不能为空")
	private String openId;
	
	@NotEmpty(message="appid不能为空")
	private String appId;
	
	@NotEmpty(message="appSecret不能为空")
	private String appSecret;
	
	/**
	 * 模板消息类型
	 */
	@NotEmpty(message="模板消息类型不能为空")
	private Integer msgType;
	/**
	 * 模板消息id
	 */
	@NotEmpty(message="模板消息id不能为空")
	private String templateId;
	/**
	 * 模板数据
	 */
	@NotNull(message="模板数据不能为空")
	private Map templateData;
	/**
	 * 跳转页
	 */
	private String page;
    
}
