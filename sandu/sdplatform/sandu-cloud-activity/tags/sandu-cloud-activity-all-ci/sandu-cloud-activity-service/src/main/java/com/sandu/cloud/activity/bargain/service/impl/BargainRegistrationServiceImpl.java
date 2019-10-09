package com.sandu.cloud.activity.bargain.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.cloud.activity.bargain.dao.BargainRegistrationDao;
import com.sandu.cloud.activity.bargain.dto.BargainRegCutResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegDashBoardResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegShipmentInfoDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationAnalyseResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationQueryDto;
import com.sandu.cloud.activity.bargain.dto.RegistrationStatusDto;
import com.sandu.cloud.activity.bargain.exception.BargainBizException;
import com.sandu.cloud.activity.bargain.exception.BargainBizExceptionCode;
import com.sandu.cloud.activity.bargain.model.Bargain;
import com.sandu.cloud.activity.bargain.model.BargainDecorateRecord;
import com.sandu.cloud.activity.bargain.model.BargainInviteRecord;
import com.sandu.cloud.activity.bargain.model.BargainInviteRecordActAggregated;
import com.sandu.cloud.activity.bargain.model.BargainInviteRecordActDayAggregated;
import com.sandu.cloud.activity.bargain.model.BargainRegistration;
import com.sandu.cloud.activity.bargain.service.BargainDecorateRecordService;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordActAggregatedService;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordActDayAggregatedService;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordService;
import com.sandu.cloud.activity.bargain.service.BargainRegistrationService;
import com.sandu.cloud.activity.bargain.service.BargainService;
import com.sandu.cloud.common.util.UUIDUtil;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.common.vo.PageResultDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BargainRegistrationServiceImpl implements BargainRegistrationService {


	@Autowired
    private BargainRegistrationDao bargainRegistrationDao;

    @Autowired
    private BargainService bargainService;
    
    @Autowired
    private BargainInviteRecordService bargainInviteRecordService;
    
    @Autowired
    private BargainDecorateRecordService bargainDecorateRecordService;
    
    @Autowired
    private BargainInviteRecordActAggregatedService inviteRecordActAggregatedService;
    
    @Autowired
    private BargainInviteRecordActDayAggregatedService inviteRecordActDayAggregatedService;
    
    
    @Override
    public void create(BargainRegistration registration) {
        bargainRegistrationDao.insert(registration);
    }

    @Override
    public int modifyById(BargainRegistration registration) {
        return bargainRegistrationDao.updateByPrimaryKeySelective(registration);
    }

    @Override
    public int remove(String regId) {
    	BargainRegistration registration = new BargainRegistration();
    	registration.setIsDeleted(0);
    	registration.setId(regId);
        return bargainRegistrationDao.updateByPrimaryKeySelective(registration);
    }

    @Override
    public BargainRegistration get(String regId) {
        return bargainRegistrationDao.selectByPrimaryKey(regId);
    }

    @Override
    public int refreshRegAwardStatusToWait(String regId) {
    	return bargainRegistrationDao.updateRegAwardStatusToWait(regId);
    }


	@Override
	public RegistrationStatusDto getBargainRegistrationStatus(String actId, LoginUser user) {
		BargainRegistration regEntity = this.getWxActBargainRegistration(actId,user.getOpenId());
		if(regEntity!=null) {
			//如果任务已完成并且没出现异常,但是还没领奖,则前端显示领奖按钮,不考虑活动是否已结束
			if(regEntity.getCompleteStatus()==BargainRegistration.COMPLETE_STATUS_FINISH
					&& regEntity.getAwardsStatus()==BargainRegistration.AWARDS_STATUS_UNAWRD
					&& regEntity.getExceptionStatus()==BargainRegistration.EXCEPTION_STATUS_OK) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_UNAWARD,regEntity.getId());
			}
			//如果任务已完成并且没出现异常,但是已领奖,则前端显示领奖成功按钮,不考虑活动是否已结束
			if(regEntity.getCompleteStatus()==BargainRegistration.COMPLETE_STATUS_FINISH
					&&regEntity.getAwardsStatus() == BargainRegistration.AWARDS_STATUS_WAIT_AWARD
					&&regEntity.getExceptionStatus() == BargainRegistration.EXCEPTION_STATUS_OK) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_AWARDED,regEntity.getId());
			}
			
			//除上面情况,其他情况如果活动结束,则前端显示活动结束按钮
			Integer actStatus = bargainService.getBargainStatus(actId,user.getAppId());
			if(actStatus==Bargain.STATUS_UNBEGIN) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_ACT_UNBEGIN,regEntity.getId());
			}else if(actStatus==Bargain.STATUS_ENDED) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_ACT_ENDED,regEntity.getId());
			}
			
			//活动正常进行,则返回邀请状态,没有邀请记录前端显示"极速砍价",有邀请记录前端显示"邀请好友"
			if(regEntity.getInviteStatus()==BargainRegistration.INVITE_STATUS_UNINVITE) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_UNINVITE,regEntity.getId());
			}else if(regEntity.getInviteStatus()==BargainRegistration.INVITE_STATUS_INVITED) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_INVITING,regEntity.getId());
			}
			log.error("活动状态异常!actId:"+actId+"openId:"+user.getOpenId());
			throw new RuntimeException("活动状态异常!actId:"+actId+"openId:"+user.getOpenId());
		}else {
			//除上面情况,其他情况如果活动结束,则前端显示活动结束按钮
			Integer actStatus = bargainService.getBargainStatus(actId,user.getAppId());
			if(actStatus==Bargain.STATUS_UNBEGIN) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_ACT_UNBEGIN,null);
			}else if(actStatus==Bargain.STATUS_ENDED) {
				return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_ACT_ENDED,null);
			}
			return new RegistrationStatusDto(BargainRegistration.STATUS_CODE_UNINVITE,null);
		}
		
	}
	
	
	private BargainRegistration getWxActBargainRegistration(String actId, String openId) {
		BargainRegistration obj = new BargainRegistration();
		obj.setActId(actId);
		obj.setOpenId(openId);
		obj.setIsDeleted(0);
		return bargainRegistrationDao.selectOne(obj);
	}
	
	

	@Override
	@Transactional
	public BargainRegCutResultDto cutPriceByMyself(String actId, LoginUser user) {
		Bargain actEntity = bargainService.get(actId,user.getAppId());
		checkActStatus(actEntity);
		BargainRegistration regEntity = this.getWxActBargainRegistration(actId, user.getOpenId());
		if(regEntity==null) {
			regEntity = this.buildRegEntityByMyselfCut(actEntity, user);
			this.create(regEntity);
		}else {
			setRegistrationInviteStatusToInvited(regEntity.getId());
		}
		
		if(bargainInviteRecordService.isCut(regEntity.getId(),user.getOpenId())) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_REPEAT_CUT);
		}
		
		//计算随机金额
		Double cutPrice = this.calRandomCutPrice(actEntity.getMyCutPriceMax(),actEntity.getMyCutPriceMin());
		BargainInviteRecord inviteRecord = this.buildInviteRecord(regEntity, user, cutPrice);
		
		//先增加砍价记录
		bargainInviteRecordService.create(inviteRecord);
		//累计参与人数
		bargainService.increaseParticipants(actEntity.getId());
		
		//执行砍价
		executeCutPrice(regEntity.getId(),cutPrice);
		
		//更新任务状态与扣减库存
		boolean isComplete = refreshRegCompleteStatusAndReduceProductInventory(actEntity.getId(),regEntity.getId());
		
		return new BargainRegCutResultDto(regEntity.getOpenId(),regEntity.getId(),regEntity.getProductName(),cutPrice,isComplete);
	}
	
	/**
	 * 更新任务状态
	 * @param actId
	 * @param regId
	 * @return 是否已完成任务
	 */
	private boolean refreshRegCompleteStatusAndReduceProductInventory(String actId,String regId) {

		BargainRegistration newRegEntity = this.get(regId);
		//成功完成任务
		if(newRegEntity.getProductRemainPrice()<=0) {
			//更新报名参加的活动状态
			int updateCount = this.refreshRegCompleteStatusToFinish(newRegEntity.getId());
			//更新活动商品库存
			if(updateCount>0) {
				boolean isSuccess = bargainService.reduceProductInventory(actId);
				//如果扣减失败
				if(!isSuccess) {
					log.warn("没库存了:"+actId);
					this.recordNoProductInventoryException(regId);
				}else {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private void recordNoProductInventoryException(String regId) {
		BargainRegistration updateRegEntity = new BargainRegistration();
		updateRegEntity.setId(regId);
		updateRegEntity.setExceptionStatus(BargainRegistration.EXCEPTION_STATUS_NO_STOCK);
		bargainRegistrationDao.updateByPrimaryKeySelective(updateRegEntity);
	}

	/**
	 * 更新任务状态为已完成
	 * @param regId
	 * @return
	 */
	private int refreshRegCompleteStatusToFinish(String regId) {
		return bargainRegistrationDao.updateRegCompleteStatusToFinish(regId);
	}

	/**
	 * 执行砍价
	 * @param regId
	 * @param cutPrice
	 */
	private void executeCutPrice(String regId, Double cutPrice) {
		bargainRegistrationDao.updateToReduceRegProductRemainPriceById(regId,cutPrice);
	}

	/**
	 * 计算随机砍掉金额
	 * @return
	 */
	private Double calRandomCutPrice(Double max,Double min) {
		if(max.doubleValue()==min.doubleValue()) {
			return max;
		}
		max = max*100;
		min = min*100;
		Random random = new Random();
		Double cutPrice = Double.valueOf(random.nextInt(max.intValue() - min.intValue())+ min.intValue())/100;
		return cutPrice;
	}
	
	
	
	private BargainInviteRecord buildInviteRecord(BargainRegistration regEntity,LoginUser user,Double cutPrice) {
		BargainInviteRecord inviteRecord = new BargainInviteRecord();
		inviteRecord.setId(UUIDUtil.getUUID());
		inviteRecord.setRegistrationId(regEntity.getId());
		inviteRecord.setOpenId(user.getOpenId());
		inviteRecord.setNickname(user.getNickName());
		inviteRecord.setHeadPic(user.getHeadPic());
		inviteRecord.setCutPrice(cutPrice);
		inviteRecord.setRemainPrice(regEntity.getProductRemainPrice()-cutPrice);
		inviteRecord.setAppId(user.getAppId());
		inviteRecord.setGmtCreate(new Date());
		inviteRecord.setIsDeleted(0);
		return inviteRecord;
	}
	
	/**
	 * 将任务邀请状态设置为已邀请
	 * @param regId
	 */
	private void setRegistrationInviteStatusToInvited(String regId){
		BargainRegistration updateRegEntity = new BargainRegistration();
		updateRegEntity.setId(regId);
		updateRegEntity.setInviteStatus(BargainRegistration.INVITE_STATUS_INVITED);
		bargainRegistrationDao.updateByPrimaryKeySelective(updateRegEntity);
	}
	
	

	
	private BargainRegistration buildRegEntityByMyselfCut(Bargain actEntity,LoginUser user) {
		BargainRegistration regEntity = initRegEntity(actEntity,user);
		regEntity.setInviteStatus(BargainRegistration.INVITE_STATUS_INVITED);
		return regEntity;
	}
	
	private BargainRegistration buildRegEntityByDecorate(Bargain actEntity,LoginUser user) {
		BargainRegistration regEntity = initRegEntity(actEntity,user);
		regEntity.setDecorateStatus(BargainRegistration.DECORATE_STATUS_DECORATED);
		return regEntity;
		
	}
	/**
	 * 构建任务对象
	 * @param actEntity
	 * @param user
	 * @return
	 */
	private BargainRegistration initRegEntity(Bargain actEntity,LoginUser user) {
		Date now = new Date();
		BargainRegistration regEntity = new BargainRegistration();
		regEntity.setId(UUIDUtil.getUUID());
		regEntity.setActId(actEntity.getId());
		regEntity.setOpenId(user.getOpenId());
		regEntity.setNickname(user.getNickName());
//		regEntity.setHeadPic(user.getHeadPic());
		regEntity.setProductName(actEntity.getProductName());
		regEntity.setProductPrice(actEntity.getProductDiscountPrice());
		regEntity.setProductMinPrice(actEntity.getProductMinPrice());
		//优惠价-底价=需要砍掉的金额
		regEntity.setProductRemainPrice(actEntity.getProductDiscountPrice()-actEntity.getProductMinPrice());
		regEntity.setCutMethodPriceMin(actEntity.getCutMethodPriceMin());
		regEntity.setCutMethodPriceMax(actEntity.getCutMethodPriceMax());
		regEntity.setInviteStatus(BargainRegistration.INVITE_STATUS_UNINVITE);
		regEntity.setDecorateStatus(BargainRegistration.DECORATE_STATUS_UNDECORATE);
		regEntity.setAwardsStatus(BargainRegistration.AWARDS_STATUS_UNAWRD);
		regEntity.setExceptionStatus(BargainRegistration.EXCEPTION_STATUS_OK);
		regEntity.setCompleteStatus(BargainRegistration.COMPLETE_STATUS_UNFINISH);
		regEntity.setShipmentStatus(BargainRegistration.SHIPMENT_STATUS_UNDELIVERED);
		regEntity.setInviteCutPriceSum(0);
		regEntity.setInviteCutRecordCount(0);
		regEntity.setAppId(user.getAppId());
		regEntity.setCreator(user.getNickName());
		regEntity.setGmtCreate(now);
		regEntity.setModifier(user.getNickName());
		regEntity.setGmtModified(now);
		regEntity.setIsDeleted(0);
		return regEntity;
	}

	
	@Override
	public BargainRegCutResultDto cutPriceByDecorate(String actId, LoginUser user,Long houseId,String houseName) {
		Bargain actEntity = bargainService.get(actId,user.getAppId());
		checkActStatus(actEntity);
		BargainRegistration regEntity = this.getWxActBargainRegistration(actId, user.getOpenId());
		if(regEntity==null) {
			regEntity = this.buildRegEntityByDecorate(actEntity, user);
			this.create(regEntity);
		}else {
			setRegistrationDecorateStatusToDecorated(regEntity.getId());
		}
		
		//判断之前是否已经装修了
		if(isDecorateBefore(regEntity.getId())) {
			return new BargainRegCutResultDto(regEntity.getOpenId(),regEntity.getId(),regEntity.getProductName(),new Double(0),false);
		}else {
			//计算随机金额
			Double cutPrice = new Double(50);
			BargainDecorateRecord decorateRecord = this.buildDecorateRecord(regEntity, user, cutPrice,houseId,houseName);
			
			//先增加体验装进我家记录
			bargainDecorateRecordService.create(decorateRecord);
					
			//执行砍价
			executeCutPrice(regEntity.getId(),cutPrice);
			
			//更新任务状态及库存
			boolean isComplete = refreshRegCompleteStatusAndReduceProductInventory(actEntity.getId(),regEntity.getId());
			
			return new BargainRegCutResultDto(regEntity.getOpenId(),regEntity.getId(),regEntity.getProductName(),cutPrice,isComplete);
		}
	}
	
	/**
	 * 检查活动状态
	 */
	private void checkActStatus(Bargain actEntity) {
		Integer actStatus = bargainService.getBargainStatus(actEntity);
		if(actStatus==Bargain.STATUS_UNBEGIN) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_UNBEGIN);
		}else if(actStatus==Bargain.STATUS_ENDED) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_ENDED);
		}
	}

	/**
	 * 是否已经进行了装进我家砍价
	 * @param regId
	 * @param openId
	 * @return
	 */
	private boolean isDecorateBefore(String regId) {
		BargainDecorateRecord record =bargainDecorateRecordService.getByRegId(regId);
		if(record!=null) {
			return true;
		}
		return false;
	}

	private BargainDecorateRecord buildDecorateRecord(BargainRegistration regEntity, LoginUser user,
			Double cutPrice,Long houseId,String houseName) {
		BargainDecorateRecord decorateRecord = new BargainDecorateRecord();
		decorateRecord.setId(UUIDUtil.getUUID());
		decorateRecord.setRegistrationId(regEntity.getId());
		decorateRecord.setOpenId(user.getOpenId());
//		decorateRecord.setHeadPic(user.getHeadPic());
		decorateRecord.setHouseId(houseId);
		decorateRecord.setHouseName(houseName);
		decorateRecord.setCutPrice(cutPrice);
		decorateRecord.setRemainPrice(regEntity.getProductRemainPrice()-cutPrice);
		decorateRecord.setAppId(user.getAppId());
		decorateRecord.setCreator(user.getNickName());
		decorateRecord.setGmtCreate(new Date());
		decorateRecord.setIsDeleted(0);
		return decorateRecord;
	}

	/**
	 * 将任务邀请状态设置为已装修
	 * @param regId
	 */
	private void setRegistrationDecorateStatusToDecorated(String regId){
		BargainRegistration updateRegEntity = new BargainRegistration();
		updateRegEntity.setId(regId);
		updateRegEntity.setDecorateStatus(BargainRegistration.DECORATE_STATUS_DECORATED);
		bargainRegistrationDao.updateByPrimaryKeySelective(updateRegEntity);
	}

	@Override
	public String getBargainInviteRecordCutStatus(String actId, String openId, String registrationId) {
		Bargain actEntity = bargainService.get(actId);
		Integer actStatus = bargainService.getBargainStatus(actEntity);
		BargainInviteRecord inviteRecord = bargainInviteRecordService.get(registrationId,openId);
		if(actStatus==Bargain.STATUS_ENDED && inviteRecord==null) {
			return "ACT_ENDED_UN_CUT";
		}else if(actStatus==Bargain.STATUS_ENDED && inviteRecord!=null) {
			return "ACT_ENDED_CUT";
		}else if(actStatus==Bargain.STATUS_UNBEGIN) {
			return "ACT_UNBEGIN";
		}
		BargainRegistration regEntity = bargainRegistrationDao.selectByPrimaryKey(registrationId);
		if(regEntity==null) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_TASK_NOT_EXIST);
		}
		if(regEntity.getCompleteStatus()==BargainRegistration.COMPLETE_STATUS_FINISH) {
			return "REG_COMPLETE";
		}
		
		
		
		if(inviteRecord==null) {
			return "UN_CUT";
		}else{
			return "CUT";
		}
	}

	@Override
	public BargainRegCutResultDto cutPriceByInvite(String actId,String registrationId, LoginUser user) {
		Bargain actEntity = bargainService.get(actId,user.getAppId());
		checkActStatus(actEntity);
		BargainRegistration regEntity = this.get(registrationId);
		if(regEntity==null) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_TASK_NOT_EXIST);
		}
		
		if(regEntity.getCompleteStatus()==BargainRegistration.COMPLETE_STATUS_FINISH) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_REG_COMPLETE);
		}
		
		checkUser(actEntity,registrationId,user);
		
		//计算随机金额
		Double cutPrice = this.calRandomCutPrice(regEntity.getCutMethodPriceMax(),regEntity.getCutMethodPriceMin());
		BargainInviteRecord inviteRecord = this.buildInviteRecord(regEntity, user, cutPrice);
		
		//记得更新邀请砍价信息
		inviteCutInfoRecord(inviteRecord,actEntity,user);
		
		//执行砍价
		executeCutPrice(regEntity.getId(),cutPrice);
		
		//更新任务状态与扣减库存
		boolean isComplete = refreshRegCompleteStatusAndReduceProductInventory(actEntity.getId(),regEntity.getId());
		
		return new BargainRegCutResultDto(regEntity.getOpenId(),regEntity.getId(),regEntity.getProductName(),cutPrice,isComplete);
	}
	
	private void inviteCutInfoRecord(BargainInviteRecord inviteRecord,Bargain actEntity,LoginUser user) {		
		// 先增加砍价记录
		bargainInviteRecordService.create(inviteRecord);

		// 更新用户-活动砍价人次数
		if(inviteRecordActAggregatedService.isCut(actEntity.getId(),user.getOpenId())) {
			inviteRecordActAggregatedService.increaseInviteCutCount(actEntity.getId(),user.getOpenId());
		}else {
			BargainInviteRecordActAggregated entity = buildInviteRecordActAggregated(actEntity,user);
			inviteRecordActAggregatedService.create(entity);
		}
		// 更新用户-每天-活动砍价次数
		if(inviteRecordActDayAggregatedService.isCutCurrentDay(actEntity.getId(),user.getOpenId())) {
			inviteRecordActDayAggregatedService.increaseCurrentDayInviteCutCount(actEntity.getId(),user.getOpenId());
		}else {
			BargainInviteRecordActDayAggregated entity = buildInviteRecordActDayAggregated(actEntity,user);
			inviteRecordActDayAggregatedService.create(entity);
		}
		
	}

	private BargainInviteRecordActDayAggregated buildInviteRecordActDayAggregated(Bargain actEntity,LoginUser user) {
		BargainInviteRecordActDayAggregated entity = new BargainInviteRecordActDayAggregated();
		entity.setId(UUIDUtil.getUUID());
		entity.setOpenId(user.getOpenId());
		entity.setInviteDate(new Date());
		entity.setActId(actEntity.getId());
		entity.setInviteCutCount(1);
		entity.setAppId(user.getAppId());
		return entity;
	}

	private BargainInviteRecordActAggregated buildInviteRecordActAggregated(Bargain actEntity,LoginUser user) {
		BargainInviteRecordActAggregated entity = new BargainInviteRecordActAggregated();
		entity.setId(UUIDUtil.getUUID());
		entity.setOpenId(user.getOpenId());
		entity.setActId(actEntity.getId());
		entity.setInviteCutCount(1);
		entity.setAppId(user.getAppId());
		return entity;
	}

	private void checkUser(Bargain actEntity,String registrationId,LoginUser user) {
		//已帮好友砍价.
		if(bargainInviteRecordService.isCut(registrationId,user.getOpenId())) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_REPEAT_CUT);
		}
		
		//只允许新用户参加
		if(actEntity.getOnlyAllowNew()==1) {
			long sec = (new Date().getTime()-user.getGmtCreate().getTime())/ 1000; //用户创建时间距离当前时间(秒数)
			//一分钟内算新用户
			if(sec>=60) {				
				throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_REPEAT_ONLY_NEW_USER);
			}
			//是否已为一个好友砍过价
			if(inviteRecordActAggregatedService.isCut(actEntity.getId(),user.getOpenId())) {
				throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_REPEAT_ONLY_NEW_USER);
			}
		}
		else {
			//配置每天帮砍好友数
			if(actEntity.getHelpCutPerDay()!=null && actEntity.getHelpCutPerDay()>0) {
				int inviteCutCount = inviteRecordActDayAggregatedService.getCurrentDateInviteCutCount(actEntity.getId(),user.getOpenId())+1;  //if ==null return 0 else cutCount
				if(inviteCutCount>actEntity.getHelpCutPerDay().intValue()) {
					throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NO_CHANCE_TODAY);
				}
			}
			//配置此次活动帮砍好友数
			else if(actEntity.getHelpCutPerAct()!=null && actEntity.getHelpCutPerAct()>0) {
				int inviteCutCount = inviteRecordActAggregatedService.getInviteCutCount(actEntity.getId(),user.getOpenId())+1;  //if ==null return 0 else cutCount
				if(inviteCutCount>actEntity.getHelpCutPerAct().intValue()) {
					throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NO_CHANCE);
				}
			}else {
				throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NO_CONFIG_CUT_FRIENDS);
			}
			
		}
		
	}

	@Override
	public PageResultDto<BargainRegistrationAnalyseResultDto> getBargainRegAnalyseResultList(
			BargainRegistrationQueryDto query) {
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		List<BargainRegistrationAnalyseResultDto> list = bargainRegistrationDao.selectWxActBargainRegAnalyseResult(query);
		PageInfo<BargainRegistrationAnalyseResultDto> pageInfo = new PageInfo<>(list);
		return new PageResultDto<BargainRegistrationAnalyseResultDto>(pageInfo.getList(),pageInfo.getTotal());
	}

	@Override
	public int modifyShipmentNo(String regId, String carrier,String shipmentNo, LoginUser user) {
		// TODO Auto-generated method stub
		BargainRegistration wxActBargainRegistration = new BargainRegistration();
		wxActBargainRegistration.setCarrier(carrier);
		wxActBargainRegistration.setShipmentNo(shipmentNo);
		wxActBargainRegistration.setShipmentStatus(BargainRegistration.SHIPMENT_STATUS_DELIVERED);
		wxActBargainRegistration.setId(regId);
		wxActBargainRegistration.setModifier(user.getUsername());
		wxActBargainRegistration.setGmtModified(new Date());
		return bargainRegistrationDao.updateByPrimaryKeySelective(wxActBargainRegistration);
	}

	@Override
	public BargainRegDashBoardResultDto getBargainRegDashBoardResultList(String actId,Date beginTime,Date endTime) {
		BargainRegDashBoardResultDto retVo = new BargainRegDashBoardResultDto();
		retVo.setRegList(bargainRegistrationDao.selectRegCount(actId,beginTime,endTime));
		retVo.setRegSuccessList(bargainRegistrationDao.selectRegSuccessCount(actId,beginTime,endTime));
		retVo.setCutList(bargainRegistrationDao.selectCutCount(actId,beginTime,endTime));
		return retVo;
	}
	

    @Override
    public List<BargainRegistration> list(List<String> ids) {
        return bargainRegistrationDao.getBargainRegistrationsByActIds(ids);
    }
    
    @Override
	public List<BargainRegistration> list(String id) {
    	if(StringUtils.isBlank(id)) {
    		return null;
    	}
		List<String> ids = new ArrayList<String>();
		ids.add(id);
    	return bargainRegistrationDao.getBargainRegistrationsByActIds(ids);
    	
	}

	@Override
	public BargainRegShipmentInfoDto getShipmentInfo(String regId) {
		// TODO Auto-generated method stub
		BargainRegistration reg = bargainRegistrationDao.selectByPrimaryKey(regId);
		if(reg!=null) {
			BargainRegShipmentInfoDto vo = new BargainRegShipmentInfoDto();
			vo.setCarrier(reg.getCarrier());
			vo.setShipmentNo(reg.getShipmentNo());
			return vo;
		}
		return null;
	}

	@Override
	public List<BargainRegistration> listBargainReg(List<String> regIdList) {
		return bargainRegistrationDao.selectWxActBargainRegistrationByIdList(regIdList);
	}
}
