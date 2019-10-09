package com.sandu.cloud.activity.bargain.service;

import com.sandu.cloud.activity.bargain.dto.BargainAwardAddDto;
import com.sandu.cloud.activity.bargain.model.BargainAward;
import com.sandu.cloud.common.vo.LoginUser;


public interface BargainAwardService {

    /**
     * 插入
     *
     * @param wxactbargainaward
     * @return
     */
    void create(BargainAward bargainAward);

    /**
     * 更新
     *
     * @param wxactbargainaward
     * @return
     */
    int modifyById(BargainAward bargainAward);

    /**
     * 删除
     *
     * @param wxactbargainawardIds
     * @return
     */
    int remove(String awardId);

    /**
     * 通过ID获取详情
     *
     * @param wxactbargainawardId
     * @return
     */
    BargainAward get(String awardId);

    /**
     *
     * 领奖
     */
	void addAwardRecord(BargainAwardAddDto bargainAwardAdd,LoginUser user);

}
