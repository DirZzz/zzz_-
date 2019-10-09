package com.sandu.cloud.activity.bargain.service;

import java.util.Date;
import java.util.List;

import com.sandu.cloud.activity.bargain.dto.BargainRegCutResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegDashBoardResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegShipmentInfoDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationAnalyseResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationQueryDto;
import com.sandu.cloud.activity.bargain.dto.RegistrationStatusDto;
import com.sandu.cloud.activity.bargain.model.BargainRegistration;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.common.vo.PageResultDto;


public interface BargainRegistrationService {

    /**
     * 插入
     *
     * @param registration
     * @return
     */
    void create(BargainRegistration bargainRegistration);

    /**
     * 更新
     *
     * @param registration
     * @return
     */
    int modifyById(BargainRegistration bargainRegistration);

    /**
     * 删除
     *
     * @param registrationIds
     * @return
     */
    int remove(String regId);

    /**
     * 通过ID获取详情
     *
     * @param registrationId
     * @return
     */
     BargainRegistration get(String regId);

     /**
      * 获取任务状态
      * @param actId
      * @param openId
      * @return
      */
     RegistrationStatusDto getBargainRegistrationStatus(String actId, LoginUser user);

     /**
      * 自己砍价
      * @param actId
      * @param user
      * @return
      */
     BargainRegCutResultDto cutPriceByMyself(String actId, LoginUser user);

     /**
      * 砍价(装进我家)
      * @param actId
      * @param user
      * @return
      */
     BargainRegCutResultDto cutPriceByDecorate(String actId, LoginUser user,Long houseId,String houseName);

	 /**
	  * 好友砍价状态
	  * @param actId
	  * @param openId
	  * @param registrationId
	  * @return
	  */
	 String getBargainInviteRecordCutStatus(String actId, String openId, String registrationId);

	 /**
	  * 邀请好友砍价接口
	  * @param registrationId
	  * @param user
	  * @return
	  */
	 BargainRegCutResultDto cutPriceByInvite(String actId,String registrationId, LoginUser user);

	 /**
	  * 未领奖状态更新成待领奖
	  * @param regId
	  * @return
	  */
	int refreshRegAwardStatusToWait(String regId);


	/**
	 * 报名任务分析列表
	 * @param query
	 * @return
	 */
	PageResultDto<BargainRegistrationAnalyseResultDto> getBargainRegAnalyseResultList(
			BargainRegistrationQueryDto query);

	/**
	 * 编辑运单号
	 * @param regId
	 * @param user
	 * @return
	 */
	int modifyShipmentNo(String regId, String carrier,String shipmentNo,LoginUser user);

	/**
	 * 图形统计数据列表
	 * @param regId
	 * @return
	 */
	BargainRegDashBoardResultDto getBargainRegDashBoardResultList(String actId,Date beginTime,Date endTime);
     

    List<BargainRegistration> list(List<String> actIds);
    
    List<BargainRegistration> list(String actId);

    
    BargainRegShipmentInfoDto getShipmentInfo(String regId);

    /**
     * 获取任务列表
     * @param regIdList
     * @return
     */
	List<BargainRegistration> listBargainReg(List<String> regIdList);
}
