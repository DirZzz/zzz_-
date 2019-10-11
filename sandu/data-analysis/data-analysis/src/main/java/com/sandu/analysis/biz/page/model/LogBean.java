package com.sandu.analysis.biz.page.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.constant.AnalysisConstants.LOGBEAN_GETSTRING_TYPE_ENUM;
import com.sandu.analysis.biz.constant.AnalysisConstants.NEW_LOGBEAN_TYPE_ENUM;

import lombok.Getter;
import lombok.Setter;

/**
 * 123	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	curpage:pages/home/D	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
 * 
 * @author huangsongbo
 *
 */
@Getter
@Setter
public class LogBean {

	private String userId;
	
	private String serviceTime;
	
	private String clientTime;
	
	private String eventName;
	
	private String eventProperty;
	
	private String appId;
	
	private String sdk;
	
	private String sdkVersion;
	
	private String hostName;
	
	private String ip;
	
	private String country;
	
	private String province;
	
	private String city;
	
	// ------额外的字段 ->start
	
	private String currentPage;
	
	private Long timeOnPage;
	
	private String lastPage;
	
	private Long currentTime;
	
	// ------额外的字段 ->end
	
	public LogBean () {
		
	}

	public LogBean(String str, NEW_LOGBEAN_TYPE_ENUM typeEnum) {
		if(typeEnum == null) {
			return;
		}
		
		String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
		if(!typeEnum.getLength().equals(new Integer(strs.length))) {
			return;
		}
		
		switch (typeEnum) {
		case a:
			
			// 123	2019-04-09 17:18:49	2019-04-09 17:18:49	pageview	curpage:pages/home/D	a0001	js	1.0.0	bigdatadev1	192.168.3.97	中国	广东省	深圳市
			this.userId = strs[0];
			this.serviceTime = strs[1];
			this.clientTime = strs[2];
			this.eventName = strs[3];
			this.eventProperty = strs[4];
			this.appId = strs[5];
			this.sdk = strs[6];
			this.sdkVersion = strs[7];
			this.hostName = strs[8];
			this.ip = strs[9];
			this.country = strs[10];
			this.province = strs[11];
			this.city = strs[12];
			break;
			
		case b:
			this.currentTime = Long.valueOf(strs[0]);
			this.currentPage = strs[1];
			this.userId = strs[2];
			break;
			
		case c:
			this.userId = strs[0];
			this.lastPage = strs[1];
			this.currentPage = strs[2];
			this.timeOnPage = StringUtils.equals("null", strs[3]) ? null : Long.valueOf(strs[3]);
			break;
			
		case d:
			this.lastPage = strs[0];
			this.currentPage = strs[1];
			this.userId = strs[2];
			break;
		default:
			
		}
		
	}
	
	public String getString(LOGBEAN_GETSTRING_TYPE_ENUM typeEnum) {
		if(typeEnum == null) {
			return "";
		}
		
		switch (typeEnum) {
		case a:
			String currentPage = eventProperty == null ? null : eventProperty.split(":")[1];
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Long currentTime = null;
			try {
				currentTime = serviceTime == null ? null : simpleDateFormat.parse(serviceTime).getTime();
			} catch (ParseException e) {
				
			}
			return currentTime + "\t" + currentPage + "\t" + this.userId;
		case b:
			return this.userId + "\t" + this.lastPage + "\t" + this.currentPage + "\t" + this.timeOnPage;
		default:
			return "";
		}
		
	}
	
}
