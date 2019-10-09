package com.sandu.cloud.notification.wechat.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.notification.wechat.dto.FormIdInfoBo;
import com.sandu.cloud.notification.wechat.dto.TemplateMsgReqParam;
import com.sandu.cloud.notification.wechat.exception.WeChatBizException;
import com.sandu.cloud.notification.wechat.exception.WeChatBizExceptionCode;
import com.sandu.cloud.notification.wechat.service.TemplateMsgService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TemplateMsgServiceImpl  implements TemplateMsgService{

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String REDIS_ACCESS_TOKEN_KEY_PREFIX="template_message:access_token:";
    private static final String REDIS_MINI_USER_FORM_ID_PREFIX="template_message:user_form_id:";

    @Override
	public void collectMiniUserFormId(String openId, List<String> formIdList) {
    	List<String> formIdInfolist = new ArrayList<String>();
    	//微信formid有效期是7天,这里保存6天23小时
    	Date now = new Date();
    	Calendar expireDate = Calendar.getInstance();
        expireDate.setTime(now);
        expireDate.add(Calendar.HOUR_OF_DAY, 23); 
        expireDate.add(Calendar.DAY_OF_YEAR,6);
        
    	for(String formId:formIdList) {
    		//因为我们是在开发者工具中测试，所以得到的formId值为the formId is a mock one
    		//简单数据验证
    		if(formId!=null && !formId.contains("formId") && formId.length()>10) {
    			String formIdInfoJson = JsonUtils.toJson(new FormIdInfoBo(formId,dateFormat.format(expireDate.getTime())));
        		formIdInfolist.add(formIdInfoJson);
    		}
    		
    	}
    	redisTemplate.opsForList().rightPushAll(REDIS_MINI_USER_FORM_ID_PREFIX+openId, formIdInfolist);
	}
    


    /**
     * 获取有效的formId
     * @param openId
     * @return
     */
    private String getValidFormId(String openId) {
    	List<String> formIdList = redisTemplate.opsForList().range(REDIS_MINI_USER_FORM_ID_PREFIX+openId, 0, -1);
    	String validFormId = "";
    	int trimStart = 0;
    	Date now = new Date();
    	int size;
    	for (int i = 0; i < (size = formIdList.size()); i++) {
    		try {
	    		FormIdInfoBo formIdInfoBo = JsonUtils.fromJson(formIdList.get(i), FormIdInfoBo.class);
	    		if(dateFormat.parse(formIdInfoBo.getExpire()).compareTo(now)>0) {
	    			validFormId = formIdInfoBo.getFormId();
	    			trimStart = i + 1;
	    			break;
	    		}
    		}catch(Exception ex) {
    			log.error("getValidFormId error:",ex);
    		}
    	}
    	// 移除本次使用的和已过期的
    	redisTemplate.opsForList().trim(REDIS_MINI_USER_FORM_ID_PREFIX + openId, trimStart == 0 ? size : trimStart, -1);
    	
    	if(StringUtils.isBlank(validFormId)) {
    		throw new WeChatBizException(WeChatBizExceptionCode.SD_ERR_NOTIFY_WECHAT_FORM_ID_NOT_FOUND);
    	}
    	
    	return validFormId;
    }
    
    @Override
    public void sendTemplateMsg(TemplateMsgReqParam param) {
		log.info("----------------进入发送消息的方法----------------");
        //1.获取accessToken
        String accessToken = this.getAccessToken(param.getAppId(), param.getAppSecret());
        log.info("openid:"+param.getOpenId()+",accessToken:"+accessToken);
        if(StringUtils.isBlank(accessToken)) {
        	 throw new RuntimeException("获取accessToken失败");
        }
        
        //2.发送模板消息   
        String reqUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token={1}";
        String formId = this.getValidFormId(param.getOpenId());
        Map<String, String> paramsMap = this.getTemplateMsgParam(param.getOpenId(), param.getTemplateId(), param.getPage(), formId, param.getTemplateData()); 
        log.info("发送模板消息参数:"+JsonUtils.toJson(paramsMap));
        String resultStr = restTemplate.postForObject(reqUrl, paramsMap, String.class, accessToken);
        
        //3.解析返回结果
        Map<String, Object> map = JsonUtils.fromJson(resultStr, Map.class);
        if (map != null) {
        	Double errcode = (Double)map.get("errcode");
            if (errcode==null || errcode.intValue()!=0) {
                log.error("发送模板消息失败param:{},result:{}" ,param.toString(), resultStr);
                throw new RuntimeException("发送模板消息失败");
            }
        }
	}
    
    
    
    /**
	 * 获取调用微信发送模板消息方法参数
	 * @param openid
	 * @param templateId
	 * @param page
	 * @param formId
	 * @param data
	 * @return
	 */
	private Map<String, String> getTemplateMsgParam(String openid,String templateId,String page,String formId,Map data){
		 Map paramsMap = new HashMap();
		 paramsMap.put("touser", openid);
		 paramsMap.put("template_id", templateId);
		 if(StringUtils.isNotBlank(page)) {
			 paramsMap.put("page", page);
		 }
		 paramsMap.put("form_id", formId);
		 paramsMap.put("data", data);
		 return paramsMap;
	}

    
   
    
    /**
	 * 获取模板数据,替换模板里面的配置参数
	 * @return
	 */
	private Map getRenderTempalteData(String designPlanName,String renderResult) {
		Map<String, Map> keywordsMap = new LinkedHashMap<String, Map>();
		Map<String, String> keywordTempMap = null;
		
		keywordTempMap = new HashMap<String, String>();
		keywordTempMap.put("value", designPlanName);
		keywordsMap.put("keyword1", keywordTempMap);
		
		keywordTempMap = new HashMap<String, String>();
		keywordTempMap.put("value", renderResult);
		keywordsMap.put("keyword2", keywordTempMap);
		
		return keywordsMap;
	}
	
	
	
	
	/**
	 * 获取accessToken:先从缓存拿,拿不到再去请求微信接口.获取到的token有效期是2个小时
	 * @param appId
	 * @param secret
	 * @return
	 */
	@Override
	public String getAccessToken(String appid, String secret) {
		Object obj = redisTemplate.opsForValue().get(REDIS_ACCESS_TOKEN_KEY_PREFIX+appid);
		if(obj==null) {
			return callToGetAccessToken(appid,secret);
		}else {
			//验证token是否有效
			String accessToken = obj.toString();
			String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + accessToken;
			String resultStr = restTemplate.getForObject(url, String.class);
			if (resultStr!=null && resultStr.contains("ip_list")) {
				return accessToken;
			}else {
				log.info("无效token,重新取token:"+appid);
				return callToGetAccessToken(appid,secret);
			}
		}
	}
	
	private String callToGetAccessToken(String appId,String secret) {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={1}&secret={2}";
		String resultStr = restTemplate.getForObject(url, String.class, appId,secret);
		Map<String, Object> map = JsonUtils.fromJson(resultStr, Map.class);
		if (map != null) {
            String accessToken = (String) map.get("access_token");
            if (StringUtils.isBlank(accessToken)) {
                log.error("获取accessToken返回:" + resultStr);
                return null;
            }
            redisTemplate.opsForValue().set(REDIS_ACCESS_TOKEN_KEY_PREFIX+appId,accessToken,Long.valueOf(1800));//官方描述2个小时有效,实际却没有.这里改成半小时.
            return accessToken;
        }
		return null;
	}
	
	public static void main(String[] args) { 
		//String page = String.format("/page/aaa?aa=%1$s&bb=%2$s", 12,"b"); ///page/aaa?aa=%1$s&bb=%2$s
		System.out.println(String.format("pages/cutprice/cutprice?actId=%1$s&regId=%2$s", "",2));
		//System.out.println(page);
        
		Map<String, Map> dataMap = new HashMap<String, Map>();
		Map<String, Map> keywordsMap = new LinkedHashMap<String, Map>();
		Map<String, String> keywordTempMap = null;
		
		dataMap.put("data", keywordsMap);
		keywordTempMap = new HashMap<String, String>();
		keywordTempMap.put("value", "aaa");
		keywordsMap.put("keyword1", keywordTempMap);
		
		keywordTempMap = new HashMap<String, String>();
		keywordTempMap.put("value", "bbb");
		keywordsMap.put("keyword2", keywordTempMap);
		
	 
		 Map paramsMap = new HashMap();
		 paramsMap.put("touser", "o-D9N5UU24UYBRmLdxvoTntHalJQ");
		 paramsMap.put("template_id", "abc"); 
		 paramsMap.put("page", "/pages/my-tasks/my-tasks");
		 
		 paramsMap.put("form_id", "1532670076365");
		 paramsMap.put("data", dataMap);
		
		 System.out.println(JsonUtils.toJson(dataMap));
	     
	}
	
	

	


	
}
