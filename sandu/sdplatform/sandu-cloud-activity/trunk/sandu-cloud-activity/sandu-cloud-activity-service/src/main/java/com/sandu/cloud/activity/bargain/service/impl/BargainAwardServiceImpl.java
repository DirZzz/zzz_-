package com.sandu.cloud.activity.bargain.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.activity.bargain.dao.BargainAwardDao;
import com.sandu.cloud.activity.bargain.dto.BargainAwardAddDto;
import com.sandu.cloud.activity.bargain.exception.BargainBizException;
import com.sandu.cloud.activity.bargain.exception.BargainBizExceptionCode;
import com.sandu.cloud.activity.bargain.model.BargainAward;
import com.sandu.cloud.activity.bargain.model.BargainAwardMsg;
import com.sandu.cloud.activity.bargain.model.BargainRegistration;
import com.sandu.cloud.activity.bargain.service.BargainAwardMsgService;
import com.sandu.cloud.activity.bargain.service.BargainAwardService;
import com.sandu.cloud.activity.bargain.service.BargainRegistrationService;
import com.sandu.cloud.common.util.UUIDUtil;
import com.sandu.cloud.common.vo.LoginUser;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BargainAwardServiceImpl implements BargainAwardService {

    @Autowired
    private BargainAwardDao bargainAwardDao;

    
    @Autowired
    private BargainAwardMsgService bargainAwardMsgService;


    @Autowired
    private BargainRegistrationService bargainRegistrationService;


	@Override
	public void create(BargainAward bargainAward) {
		bargainAwardDao.insert(bargainAward);
	}

	@Override
	public int modifyById(BargainAward bargainAward) {
		return bargainAwardDao.updateByPrimaryKeySelective(bargainAward);
	}

	@Override
	public int remove(String awardId) {
		return bargainAwardDao.deleteByPrimaryKey(awardId);
	}

	@Override
	public BargainAward get(String awardId) {
		return bargainAwardDao.selectByPrimaryKey(awardId);
	}


	@Override
	public void addAwardRecord(BargainAwardAddDto wxActBargainAwardAdd, LoginUser user) {
		//验证当前用户是否是获奖用户(防止恶意领奖)
		BargainRegistration regEntity = bargainRegistrationService.get(wxActBargainAwardAdd.getRegId());
		if(regEntity==null) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_TASK_NOT_EXIST);
		}
		if(!regEntity.getOpenId().equals(user.getOpenId())) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_BAD_USER);
		}
		//需要保证任务已完成,未领奖并且没异常
		if(regEntity.getCompleteStatus()==BargainRegistration.COMPLETE_STATUS_FINISH
				&&regEntity.getExceptionStatus()==BargainRegistration.EXCEPTION_STATUS_OK
				&&regEntity.getAwardsStatus()==BargainRegistration.AWARDS_STATUS_UNAWRD) {
			int updateCount = bargainRegistrationService.refreshRegAwardStatusToWait(regEntity.getId());
			if(updateCount <= 0) {
				throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_AWARD_REPEAT);
			}
			//保存领奖记录
			BargainAward awardEntity = buildWxActBargainAward(wxActBargainAwardAdd,regEntity,user);
			bargainAwardDao.insert(awardEntity);
			//生成领奖消息
			BargainAwardMsg awardMsgEntity = buildWxActBargainAwardMsg(wxActBargainAwardAdd,regEntity,user);
			bargainAwardMsgService.create(awardMsgEntity);
		}else {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_BAD_OPT);
		}

	}
	private BargainAward buildWxActBargainAward(BargainAwardAddDto wxActBargainAwardAdd,BargainRegistration regEntity,LoginUser user) {
		BargainAward awardEntity = new BargainAward();
		awardEntity.setId(UUIDUtil.getUUID());
		awardEntity.setActId(regEntity.getActId());
		awardEntity.setRegistrationId(regEntity.getId());
		awardEntity.setOpenId(user.getOpenId());
		awardEntity.setNickname(user.getNickName());
		awardEntity.setReceiver(wxActBargainAwardAdd.getReceiver());
		awardEntity.setMobile(wxActBargainAwardAdd.getMobile());
		awardEntity.setAddress(wxActBargainAwardAdd.getAddress());
		awardEntity.setAppId(user.getAppId());
		awardEntity.setGmtCreate(new Date());
		awardEntity.setIsDeleted(0);
		return awardEntity;
	}

	private BargainAwardMsg buildWxActBargainAwardMsg(BargainAwardAddDto wxActBargainAwardAdd,BargainRegistration regEntity,LoginUser user) {
		BargainAwardMsg awardMsgEntity = new BargainAwardMsg();
		Date now = new Date();
		awardMsgEntity.setId(UUIDUtil.getUUID());
		awardMsgEntity.setActId(regEntity.getActId());
		awardMsgEntity.setRegistrationId(regEntity.getId());
		awardMsgEntity.setOpenId(user.getOpenId());
		//如果是整数,则去掉小数点
		if(regEntity.getProductMinPrice().doubleValue()==regEntity.getProductMinPrice().intValue()) {
			awardMsgEntity.setMessage(user.getNickName()+"已"+regEntity.getProductMinPrice().intValue()+"元领取"+regEntity.getProductName());
		}else {
			awardMsgEntity.setMessage(user.getNickName()+"已"+regEntity.getProductMinPrice()+"元领取"+regEntity.getProductName());
		}
		awardMsgEntity.setAppId(user.getAppId());
		awardMsgEntity.setCreator(user.getNickName());
		awardMsgEntity.setGmtCreate(now);
		awardMsgEntity.setModifier(user.getNickName());
		awardMsgEntity.setGmtModified(now);
		awardMsgEntity.setIsDeleted(0);
		return awardMsgEntity;
	}

}
