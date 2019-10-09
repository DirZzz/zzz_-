package com.sandu.cloud.activity.bargain.service;

import java.util.List;

import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgQueryDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgWebDto;
import com.sandu.cloud.activity.bargain.model.BargainAwardMsg;
import com.sandu.cloud.common.vo.PageResultDto;


public interface BargainAwardMsgService {

    /**
     * 插入
     *
     * @param wxActBargainAwardMsg
     * @return
     */
    void create(BargainAwardMsg bargainAwardMsg);

    /**
     * 更新
     *
     * @param wxActBargainAwardMsg
     * @return
     */
    int modifyById(BargainAwardMsg bargainAwardMsg);

    /**
     * 删除
     *
     * @param awardMsgId
     * @return
     */
    int remove(String  awardMsgId,String modifier);

    /**
     * 通过ID获取详情
     *
     * @param awardMsgId
     * @return
     */
    BargainAwardMsg get(String awardMsgId);

    /**
    * 查询领取消息列表
    * @author : WangHaiLin
    * @date : 2018/11/22 10:07
    * @param query 查询入参
    * @return java.util.List<com.sandu.api.act.output.WxActBargainAwardMsgVO>
    *
    */
    List<BargainAwardMsg> getListByActId(BargainAwardMsgQueryDto query);

    /**
     * 查询领取消息数量
     * @author : WangHaiLin
     * @date : 2018/11/22 10:07
     * @param query 查询入参
     * @return int 领取消息数量
     */
    int getCountByActId(BargainAwardMsgQueryDto query);

    
    PageResultDto<BargainAwardMsgDto> pageList(BargainAwardMsgQueryDto query);
    
    
    
    /**
    * 随机获取领取消息实体
    * @author : WangHaiLin
    * @date  2018/11/22 14:08
    * @param actId  活动Id
    * @return java.util.List<com.sandu.api.act.output.WxActBargainAwardMsgWebVO>
    *
    */
    List<BargainAwardMsgWebDto> getMsgRandomList(String actId);



}
