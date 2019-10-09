package com.sandu.cloud.activity.bargain.service;

import java.util.List;

import com.sandu.cloud.activity.bargain.model.BargainInviteRecord;
import com.sandu.cloud.common.vo.PageResultDto;


public interface BargainInviteRecordService {

    /**
     * 插入
     *
     * @param inviterecord
     * @return
     */
    void create(BargainInviteRecord inviteRecord);

    /**
     * 更新
     *
     * @param inviterecord
     * @return
     */
    int modifyById(BargainInviteRecord inviteRecord);

    /**
     * 删除
     *
     * @param inviterecordIds
     * @return
     */
    int remove(String inviteRecordId);

    /**
     * 获取详情
     *
     * @param inviterecordId
     * @return
     */
     BargainInviteRecord get(String inviteRecordId);
     
     /**
      * 获取详情
      * @param registrationId
      * @param openId
      * @return
      */
     BargainInviteRecord get(String registrationId, String openId);
     
     
     
     /**
      * 查询列表
      * @param queryEntity
      * @return
      */
     List<BargainInviteRecord> list(BargainInviteRecord queryEntity);

     /**
      * 是否已帮好友砍过价
      * @param registrationId
      * @param openId
      * @return
      */
	boolean isCut(String registrationId, String openId);
	

	/**
	 * 分页查询邀请记录
	 * @param regId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageResultDto<BargainInviteRecord> pageList(String regId, Integer pageNum, Integer pageSize);

	/**
	 * 获取好友列表,以逗号分隔
	 * @param regId
	 * @return
	 */
	String getCutFriends(String regId);

	 

}
