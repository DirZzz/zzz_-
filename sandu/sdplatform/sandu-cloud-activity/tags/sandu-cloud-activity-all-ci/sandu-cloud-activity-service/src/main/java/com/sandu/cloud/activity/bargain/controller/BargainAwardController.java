package com.sandu.cloud.activity.bargain.controller;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.activity.bargain.client.SmsServiceClient;
import com.sandu.cloud.activity.bargain.dto.BargainAwardAddDto;
import com.sandu.cloud.activity.bargain.exception.BargainBizException;
import com.sandu.cloud.activity.bargain.exception.BargainBizExceptionCode;
import com.sandu.cloud.activity.bargain.service.BargainAwardService;
import com.sandu.cloud.common.login.LoginContext;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.common.vo.ResponseEnvelope;
import com.sandu.cloud.notification.sms.api.client.TestVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "WxActBargainAwardMsg", description = "领奖消息")
@RestController
@RequestMapping("/v1/act/bargain/award")
@RefreshScope
@Slf4j
@Validated
public class BargainAwardController {
	
	private static final String REDIS_SMS_CODE_PREFIX="sms:validation_code:";
	
    @Autowired
    private BargainAwardService bargainAwardService;
    
    @Autowired
    private SmsServiceClient smsServiceClient;
    
    @Autowired
	private StringRedisTemplate stringRedisTemplate;
    
    @ApiOperation(value = "砍价成功领奖品", response = ResponseEnvelope.class)
    @PostMapping("/addAwardRecord")
    public ResponseEnvelope add(@Valid BargainAwardAddDto awardAdd) {
    	boolean isOk = checkValidationCode(awardAdd.getMobile(), awardAdd.getValidationCode());
        if (isOk) {
        	LoginUser user = LoginContext.getLoginUser();
        	bargainAwardService.addAwardRecord(awardAdd,user);
		}
		return ResponseEnvelope.ok("领取成功");
    }
    
    private boolean checkValidationCode(String mobile,String validationCode) {
    	String code = stringRedisTemplate.opsForValue().get(REDIS_SMS_CODE_PREFIX+mobile);
    	if(StringUtils.isBlank(code)) {
    		log.warn("验证码不存在:"+mobile);
    		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_SMS_CODE_IS_NOT_EXIST); 
    	}
    	if(!code.equals(validationCode)) {
    		log.warn("验证码不正确:"+mobile);
    		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_SMS_CODE_ERROR); 
    	}
    	return true;
    }	
    

	@RequestMapping(value = "/getSms")
    @ApiOperation(value = "手机发送验证码接口..")
	public ResponseEnvelope sendSms(
			@NotNull(message = "手机号不能为空!")
			@Pattern(regexp = "^((17[0-9])|(2[0-9][0-9])|(13[0-9])|(15[012356789])|(18[0-9])|(14[57])|(16[0-9])|(19[0-9]))[0-9]{8}$", message = "手机号码不正确！")
			String mobile) {
		TestVo vo = new TestVo();
		vo.setAge(1);
		vo.setName("aaa");
		smsServiceClient.send2(vo);
		
		boolean isExist = stringRedisTemplate.hasKey(REDIS_SMS_CODE_PREFIX+mobile); 
		if(isExist) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_SMS_CODE_REPEAT_ERROR); 
		}
		
		String code = RandomStringUtils.randomNumeric(6);
        String message = "短信验证码"+code+"（3分钟内有效），切勿告知他人。如有疑问请致电0755-23895307咨询。";
        log.info(message);
		smsServiceClient.send(mobile, message);
		
		stringRedisTemplate.opsForValue().set(REDIS_SMS_CODE_PREFIX+mobile, code, 3,TimeUnit.MINUTES);
        
        return ResponseEnvelope.ok();
    }
	

}
