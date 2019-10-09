package com.sandu.cloud.activity.bargain.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.activity.bargain.client.CompanyServiceClient;
import com.sandu.cloud.activity.bargain.client.TemplateMsgServiceClient;
import com.sandu.cloud.activity.bargain.dto.BargainRegCutResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegDashBoardResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegShipmentInfoDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationAnalyseResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationQueryDto;
import com.sandu.cloud.activity.bargain.dto.RegistrationStatusDto;
import com.sandu.cloud.activity.bargain.exception.BargainBizException;
import com.sandu.cloud.activity.bargain.exception.BargainBizExceptionCode;
import com.sandu.cloud.activity.bargain.model.BargainRegistration;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordService;
import com.sandu.cloud.activity.bargain.service.BargainRegistrationService;
import com.sandu.cloud.common.login.LoginContext;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.common.vo.PageResultDto;
import com.sandu.cloud.common.vo.ResponseEnvelope;
import com.sandu.cloud.company.model.CompanyMiniProgramConfig;
import com.sandu.cloud.company.model.CompanyMiniProgramTemplateMsg;
import com.sandu.cloud.notification.wechat.dto.TemplateMsgReqParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "ActBargain", description = "砍价活动")
@RestController
@RequestMapping("/v1/act/bargain/reg")
@Validated
@Slf4j
public class BargainRegistrationController {
	
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private BargainRegistrationService bargainRegistrationService;
    
    @Autowired
    private BargainInviteRecordService bargainInviteRecordService;

    @Autowired
    private TemplateMsgServiceClient templateMsgServiceClient;
    
    @Autowired
    private CompanyServiceClient companyServiceClient;
    
    
    @ApiOperation(value = "获取报名参加的活动状态", response = ResponseEnvelope.class)
    @GetMapping("/getRegStatus")
    public ResponseEnvelope getRegStatus(@NotEmpty(message="活动id不能为空!") String actId) {
		LoginUser loginUser = LoginContext.getLoginUser();
		RegistrationStatusDto retVo = bargainRegistrationService.getBargainRegistrationStatus(actId, loginUser);
		return ResponseEnvelope.ok(retVo);
    }
    
    
    
    @ApiOperation(value = "砍价接口(自己砍价)", response = ResponseEnvelope.class)
    @PostMapping("/cutPriceByMyself")
    public ResponseEnvelope cutPriceByMyself(@NotEmpty(message="活动id不能为空!") String actId) {
		//返回砍价金额
		LoginUser loginUser = LoginContext.getLoginUser();
		BargainRegCutResultDto retVo = bargainRegistrationService.cutPriceByMyself(actId,loginUser);
		if(retVo.isComplete()) {
			String cutFriends = bargainInviteRecordService.getCutFriends(retVo.getRegId());
			log.info("砍价任务完成(自己砍价):openId:{},productName:{},cutFriends:{}",loginUser.getOpenId(), retVo.getProductName(), cutFriends);
			this.sendActBarginRegCompleteTemplateMsg(loginUser, retVo.getProductName(), cutFriends,actId,retVo.getRegId());
		}
		return ResponseEnvelope.ok(retVo.getCutPrice());
    }
    
    private void sendActBarginRegCompleteTemplateMsg(LoginUser user, String productName,String cutFriends,Object...pageParams) {
    	//获取模析消息数据
		Map tempalteData = this.buildActBarginRegCompleteTempalteData(productName,cutFriends);
		TemplateMsgReqParam reqParam = buildTemplateMsgReqParam(user,tempalteData,pageParams);
		templateMsgServiceClient.send(reqParam);
	}

    private TemplateMsgReqParam buildTemplateMsgReqParam(LoginUser user,Map tempalteData,Object...pageParams) {
    	String openId = user.getOpenId();
		String appId = user.getAppId();
		String appSecret = this.getMiniProgramConfig(appId).getAppSecret();
		CompanyMiniProgramTemplateMsg templateMsg = this.getMiniProgramTemplateMsg(appId, CompanyMiniProgramTemplateMsg.TEMPLATE_TYPE_ACT_BARGAIN_REG_COMPLETE);
		String page = String.format(templateMsg.getPage()==null?"":templateMsg.getPage(), pageParams); /// page/aaa?aa=%1$s&bb=%2$s
		return new TemplateMsgReqParam(openId, appId, appSecret, 
				CompanyMiniProgramTemplateMsg.TEMPLATE_TYPE_ACT_BARGAIN_REG_COMPLETE, templateMsg.getTemplateId(), tempalteData, 
				page);
    }
    
    private CompanyMiniProgramConfig getMiniProgramConfig(String appId) {
		CompanyMiniProgramConfig config = companyServiceClient.getCompanyMiniProConfig(appId);
        if (config == null || StringUtils.isBlank(config.getAppSecret())) {
            log.error("appid错误或者服务器未配置secret:" + appId);
            throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_MINI_CONFIG_NOT_FOUND);
        }
        return config;
	}
    
    private CompanyMiniProgramTemplateMsg getMiniProgramTemplateMsg(String appId, Integer msgType) {
    	CompanyMiniProgramTemplateMsg templateMsg = companyServiceClient.getMiniProgramTempateMsg(appId,msgType);
		if (templateMsg == null || StringUtils.isBlank(templateMsg.getTemplateId())) {
			log.error("未配置渲染模板消息id" + appId);
			 throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_MINI_TEMPLATE_MSG_NOT_FOUND);
		}
		return templateMsg;
	}
    
	/**
	 * 获取模板数据,替换模板里面的配置参数
	 * @return
	 */
	private Map buildActBarginRegCompleteTempalteData(String productName,String cutFriends) {
		Map<String, Map> keywordsMap = new LinkedHashMap<String, Map>();
		Map<String, String> keywordTempMap = null;
		
		keywordTempMap = new HashMap<String, String>();
		keywordTempMap.put("value", productName);
		keywordsMap.put("keyword1", keywordTempMap);
		
		keywordTempMap = new HashMap<String, String>();
		keywordTempMap.put("value", cutFriends);
		keywordsMap.put("keyword2", keywordTempMap);
		
		return keywordsMap;
	}
    
    
    @ApiOperation(value = "砍价接口(装进我家)", response = ResponseEnvelope.class)
    @PostMapping("/cutPriceByDecorate")
    public ResponseEnvelope cutPriceByDecorate(@NotEmpty(message="活动id不能为空!") String actId,Long houseId,String houseName,String formId, String forwardPage) {
		LoginUser loginUser = LoginContext.getLoginUser();
		BargainRegCutResultDto retVo = bargainRegistrationService.cutPriceByDecorate(actId,loginUser,houseId,houseName);
		if(retVo.isComplete()) {
			String cutFriends = bargainInviteRecordService.getCutFriends(retVo.getRegId());
			log.info("砍价任务完成(装进我家):openId:{},productName:{},cutFriends:{}",loginUser.getOpenId(), retVo.getProductName(), cutFriends);
			this.sendActBarginRegCompleteTemplateMsg(loginUser, retVo.getProductName(), cutFriends,actId,retVo.getRegId());
		}
		return ResponseEnvelope.ok(retVo.getCutPrice());
    	
    }
    
    
    @ApiOperation(value = "是否已帮好友砍价", response = ResponseEnvelope.class)
    @GetMapping("/getInviteCutStatus")
    public ResponseEnvelope getInviteCutStatus(@NotEmpty(message="活动id不能为空!") String actId,@NotEmpty(message="任务id不能为空!")String regId) {
		//返回砍价金额
		LoginUser loginUser = LoginContext.getLoginUser();
		String statusCode = bargainRegistrationService.getBargainInviteRecordCutStatus(actId,loginUser.getOpenId(),regId);
		return ResponseEnvelope.ok(statusCode);
    }
    
    @ApiOperation(value = "砍价接口(帮好友砍价)", response = ResponseEnvelope.class)
    @PostMapping("/cutPriceByInvite")
    public ResponseEnvelope cutPriceByInvite(@NotEmpty(message="活动id不能为空!") String actId,@NotEmpty(message="任务id不能为空!") String regId) {
		//返回砍价金额
		LoginUser loginUser = LoginContext.getLoginUser();
		BargainRegCutResultDto retVo = bargainRegistrationService.cutPriceByInvite(actId,regId,loginUser);
		if(retVo.isComplete()) {
			String cutFriends = bargainInviteRecordService.getCutFriends(retVo.getRegId());
		}
		return ResponseEnvelope.ok(retVo.getCutPrice());
    }
    
    
    @ApiOperation(value = "活动报名统计列表", response = ResponseEnvelope.class)
    @GetMapping("/getRegAnalyseResultList")
    public ResponseEnvelope getRegAnalyseResultList(BargainRegistrationQueryDto query) {
    	PageResultDto<BargainRegistrationAnalyseResultDto> pageResult = bargainRegistrationService.getBargainRegAnalyseResultList(query);
		return ResponseEnvelope.ok(pageResult);
    }
    
    
    @ApiOperation(value = "获取物流信息", response = ResponseEnvelope.class)
    @GetMapping("/getShipmentInfo")
    public ResponseEnvelope getShipmentInfo(@NotEmpty(message="任务id不能为空!") String regId) {
		BargainRegShipmentInfoDto retVo = bargainRegistrationService.getShipmentInfo(regId);
		return ResponseEnvelope.ok(retVo);
    }
    
    
    @ApiOperation(value = "编辑运单号", response = ResponseEnvelope.class)
    @PostMapping("/modifyShipmentNo")
    public ResponseEnvelope modifyShipmentNo(@NotEmpty(message="任务id不能为空!") String regId,
    		@NotEmpty(message="快递公司不能为空!") String carrier, 
    		@NotEmpty(message="运单号不能为空!") String shipmentNo) {
		//返回砍价金额
		LoginUser loginUser = LoginContext.getLoginUser();
		BargainRegistration regEntity = bargainRegistrationService.get(regId);
		if(regEntity==null) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_TASK_NOT_EXIST);
		}
		if(regEntity.getCompleteStatus()!=BargainRegistration.COMPLETE_STATUS_FINISH) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_TASK_NOT_FINISH);
		}
		
		if(regEntity.getExceptionStatus()==BargainRegistration.EXCEPTION_STATUS_NO_STOCK) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NO_STOCK);
		}
		bargainRegistrationService.modifyShipmentNo(regId,carrier,shipmentNo,loginUser);
		return ResponseEnvelope.ok();
    	
    }
    
    
    @ApiOperation(value = "图形统计数据列表", response = ResponseEnvelope.class)
    @GetMapping("/getRegDashBoardResultList")
    public ResponseEnvelope getRegDashBoardResultList(
    		@NotEmpty(message="任务id不能为空!") String actId,
    		@NotEmpty(message="开始时间不能为空!") String beginTime,
    		@NotEmpty(message="结束时间不能为空!") String endTime) {
    	
    		Date beginTime2 = null;
    		Date endTime2 = null;
    		try {
    			beginTime2 = dateFormat.parse(beginTime);
    		}catch(Exception ex) {
    			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_BEGIN_DATE_FORMAT_ERROR);
    		}
    		try {
    			endTime2 = dateFormat.parse(endTime);
    		}catch(Exception ex) {
    			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_BEGIN_DATE_FORMAT_ERROR);
    		}
    		if(beginTime2.compareTo(endTime2)>0) {
    			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_END_DATE_MUST_AFTER_BEGIN_DATE);
    		}
    		BargainRegDashBoardResultDto resultVo = bargainRegistrationService.getBargainRegDashBoardResultList(actId,beginTime2,endTime2);
			return ResponseEnvelope.ok(resultVo);
    }
    
  
    

}
