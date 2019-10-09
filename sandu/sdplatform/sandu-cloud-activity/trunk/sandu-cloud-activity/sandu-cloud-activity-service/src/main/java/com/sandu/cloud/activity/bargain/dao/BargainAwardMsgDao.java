package com.sandu.cloud.activity.bargain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgWebDto;
import com.sandu.cloud.activity.bargain.model.BargainAwardMsg;

import tk.mybatis.mapper.common.Mapper;


public interface BargainAwardMsgDao extends Mapper<BargainAwardMsg> {
	
   // void insertWxActBargainAwardMsg(BargainAwardMsg awardMsg);

  //  int updateWxActBargainAwardMsgById(BargainAwardMsg awardMsg);

  //  int deleteWxActBargainAwardMsgById(@Param("awardMsgId") String awardMsgId, @Param("modifier")String modifier);

  // BargainAwardMsg selectWxActBargainAwardMsgById(@Param("awardMsgId") String awardMsgId);

  //  List<WxActBargainAwardMsgVO> selectListByActId(WxActBargainAwardMsgQuery query);

   // int selectCountByActId(WxActBargainAwardMsgQuery query);

  @Select(" SELECT " + 
  		"        id      AS  awardmsgId," + 
  		"        act_id  AS  actId," + 
  		"        message AS  message" + 
  		"        FROM wx_act_bargain_award_msg" + 
  		"        WHERE is_deleted=0" + 
  		"        AND act_id = #{actId}" + 
  		"        ORDER BY RAND() LIMIT 20")
   List<BargainAwardMsgWebDto> selectMsgRandomList(@Param("actId") String actId);

}
