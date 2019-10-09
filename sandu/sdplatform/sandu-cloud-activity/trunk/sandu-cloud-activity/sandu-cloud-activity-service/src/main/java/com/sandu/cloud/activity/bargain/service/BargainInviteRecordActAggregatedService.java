package com.sandu.cloud.activity.bargain.service;

import com.sandu.cloud.activity.bargain.model.BargainInviteRecordActAggregated;


public interface BargainInviteRecordActAggregatedService {

    /**
     * 插入
     *
     * @param wxactbargain
     * @return
     */
    void create(BargainInviteRecordActAggregated entity);

    /**
     * 更新
     *
     * @param wxactbargain
     * @return
     */
    int modifyById(BargainInviteRecordActAggregated entity);

    
    /**
     * 通过ID获取详情
     *
     * @return
     */
    BargainInviteRecordActAggregated get(String id);
    
    /**
     * 获取详情
     *
     * @return
     */
    BargainInviteRecordActAggregated get(String actId, String openId);

    /**
     * 是否已砍价
     * @param actId
     * @param openId
     * @return
     */
	boolean isCut(String actId, String openId);

	/**
	 * 被邀请砍价次数
	 * @param actId
	 * @param openId
	 * @return
	 */
	int getInviteCutCount(String actId, String openId);

	
	void increaseInviteCutCount(String id, String openId);

	

    
}
