package com.sandu.cloud.gateway.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sandu.cloud.common.util.JsonUtils;
import com.sandu.cloud.common.vo.ResponseEnvelope;


@Component
public class ReplaceHeaderTokenFilter extends ZuulFilter {

	private static final String REDIS_SSO_TOKEN_PREFIX="sso:access_token:";
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
    
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

   
    /**
     * 网关不验证任何登录,因为不同应用url是否需要登录才能访问都有自己的要求,如果配置在网关会导致频繁修改和重启网关.
     */
    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
       
        //1.从header取出sso token
        String header = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(header)) {
        	//2.从redis 取出sso token对应的oauth2 token
            String oath2Token = stringRedisTemplate.opsForValue().get(REDIS_SSO_TOKEN_PREFIX+header);
            if(StringUtils.isNotBlank(oath2Token)) {
            	//3.替换成oauth2 token
                requestContext.addZuulRequestHeader("Authorization","Bearer "+oath2Token);
            }
        }
      
        return null;
  
    }


    private void accessDenied(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(200);
        ResponseEnvelope responseResult = new ResponseEnvelope("SD_ERR_GATEWAY_ACCESS_DENY","未登录");
        //转成json
        String jsonString = JsonUtils.toJson(responseResult);
        requestContext.setResponseBody(jsonString);
        //转成json，设置contentType
        response.setContentType("application/json;charset=utf-8");
    }


}
