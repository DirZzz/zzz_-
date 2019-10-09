package com.sandu.cloud.notification.wechat.service;

import java.util.List;

import com.sandu.cloud.notification.wechat.dto.TemplateMsgReqParam;



public interface TemplateMsgService{
	
	/**
	 * 收集用户formId
	 * @param openId
	 * @param formIdList
	 */
	public void collectMiniUserFormId(String openId,List<String> formIdList);

	
	/**
	 * 发送模板消息
	 * @param param
	 */
	public void sendTemplateMsg(TemplateMsgReqParam param);
	
	
	/**
	 * 获取小程序access token
	 *
	 * @param appid
	 * @param secret
	 * @return
	 */
	String getAccessToken(String appid, String secret);

}
