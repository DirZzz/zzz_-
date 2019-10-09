package com.sandu.cloud.activity.bargain.service;

import java.util.Date;
import java.util.List;

import com.sandu.cloud.activity.bargain.model.Bargain;


public interface BargainService {

    /**
     * 插入
     *
     * @param wxactbargain
     * @return
     */
    void create(Bargain bargain);

    /**
     * 更新
     *
     * @param wxactbargain
     * @return
     */
    int modifyById(Bargain bargain);

    /**
     * 删除
     *
     * @param actIds
     * @return
     */
    int remove(String actId);

    /**
     * 通过ID获取详情
     *
     * @param actId
     * @return
     */
     Bargain get(String actId);
     
     /**
      * 通过ID和appid获取详情
      *
      * @param actId
      * @return
      */
      Bargain get(String actId,String appId);
     
     /**
      * 获取活动状态
      * @param wxActBargain
      * @return
      */
     Integer getBargainStatus(Bargain bargain);
     
     /**
      *  获取活动状态
      * @param actId
      * @return
      */
     Integer getBargainStatus(String actId,String appId);

     /**
      * 扣减库存
      * @param actId
      * @return
      */
	 boolean reduceProductInventory(String actId);

	 /**
	  * 增加参与人数
	  * @param actId
	  */
	 void increaseParticipants(String actId);

    List<Bargain> pageList(List<String> appids, Integer page, Integer limit);

    /**
     * 废弃不用了
     * 查询所有有效砍价活动
     * @return
     */
    List<Bargain> listAllEffectiveBargain(Date currentTime);
    
    
    List<Bargain> listWillExpireList();

    /**
    * 定时任务，扣除库存
    * @author : WangHaiLin
    * @date  2018/11/23 18:13
    * @return void  无返回
    *
    */
    void modifyVitualCount();


    int countByAppids(List<String> appids);

    Bargain getByActName(String actName);

}
