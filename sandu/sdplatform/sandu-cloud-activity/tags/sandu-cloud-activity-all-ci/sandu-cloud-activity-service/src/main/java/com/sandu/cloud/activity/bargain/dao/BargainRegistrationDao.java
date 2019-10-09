package com.sandu.cloud.activity.bargain.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sandu.cloud.activity.bargain.dto.BargainRegCountResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationAnalyseResultDto;
import com.sandu.cloud.activity.bargain.dto.BargainRegistrationQueryDto;
import com.sandu.cloud.activity.bargain.model.BargainRegistration;

import tk.mybatis.mapper.common.Mapper;


public interface BargainRegistrationDao extends Mapper<BargainRegistration> {
	
    //int insertWxActBargainRegistration(BargainRegistration wxActBargainRegistration);

   // int updateWxActBargainRegistrationById(BargainRegistration wxActBargainRegistration);

  //  int deleteWxActBargainRegistrationById(@Param("regId") String regId);

  //  BargainRegistration selectWxActBargainRegistrationById(@Param("regId") String regId);

	//BargainRegistration selectWxActBargainRegistrationByActIdAndOpenId(@Param("actId")String actId, @Param("openId")String openId);

	@Update("update wx_act_bargain_registration "+
			"       	set  "+
			"       		product_remain_price = product_remain_price - #{cutPrice}, "+
			"       		invite_cut_price_sum = invite_cut_price_sum + #{cutPrice}, "+
			"       		invite_cut_record_count = invite_cut_record_count + 1, "+
			"       		gmt_modified = now() "+
			"       where id = #{regId} ")
	void updateToReduceRegProductRemainPriceById(@Param("regId")String regId, @Param("cutPrice")Double cutPrice);

	@Update("update wx_act_bargain_registration "+
			"        	set  "+
			"        		complete_status = 10, "+
			"        		gmt_modified = now() "+
			"        where id = #{regId} and complete_status=0 and is_deleted=0 ")
	int updateRegCompleteStatusToFinish(@Param("regId") String regId);

	
	@Update("update wx_act_bargain_registration "+
			"       	set  "+
			"       		awards_status = 10, "+
			"       		gmt_modified = now() "+
			"       where id = #{regId} and awards_status=0 and is_deleted=0 ")
	int updateRegAwardStatusToWait(@Param("regId") String regId);


	@Select("<script> "+
			"	select  "+
			"		reg.id regId, "+
			"		reg.head_pic headPic, "+
			"		reg.open_id openId, "+
			"		reg.nickname, "+
			"		reg.gmt_create gmtCreate, "+
			"		reg.invite_cut_record_count inviteCutCount, "+
			"		reg.invite_cut_price_sum inviteCutPriceSum, "+
			"		reg.decorate_status decorateStatus,  "+
			"		case  "+
			"			when (reg.exception_status=0 and reg.complete_status=10 and reg.shipment_status=0) "+
			"			then 10 "+
			"			when (reg.exception_status=0 and reg.complete_status=10 and reg.shipment_status=10) "+
			"			then 20 "+
			"			else null "+
			"			end status, "+
			"		reg.shipment_status shipmentStatus, "+
			"		award.receiver, "+
			"		award.mobile, "+
			"		award.address "+
			"	from wx_act_bargain_registration reg "+
			"	left join wx_act_bargain_award award "+
			"	on reg.id = award.registration_id "+
			"	where reg.is_deleted=0 "+
			"	<if test='actId != null'>  and reg.act_id = #{actId} </if> "+
			"	<if test='openId!= null'>  and reg.open_id = #{openId} </if> "+
			"	<if test='nickname != null'>  and reg.nickname = #{nickname} </if> "+
			"	<if test='status != null and status == 10'> "+
			"		and reg.exception_status=0 and reg.complete_status=10 and reg.shipment_status=0 "+
			"	</if> "+
			"	<if test='status != null and status == 20'> "+
			"		and reg.shipment_status=10 "+
			"	</if> "+
			"	<if test='decorateStatus != null'>  and reg.decorate_status = #{decorateStatus} </if> "+
			"	order by reg.gmt_create desc "+
			"</script>  ")
	List<BargainRegistrationAnalyseResultDto> selectWxActBargainRegAnalyseResult(
			BargainRegistrationQueryDto query);
	
	/**
	 * 参与人数
	 * @param regId
	 * @return
	 */
	@Select("select "+
			"	DATE_FORMAT(gmt_create,'%Y-%m-%d %H') time,"+
			"	count(*) num "+
			"from wx_act_bargain_registration "+
			"where is_deleted=0 "+
			"and act_id = #{actId} "+
			"and gmt_create >= #{beginTime} "+
			"and gmt_create <= #{endTime} "+
			"group by DATE_FORMAT(gmt_create,'%Y-%m-%d %H') "+
			"order by gmt_create")
	List<BargainRegCountResultDto> selectRegCount(@Param("actId") String actId,@Param("beginTime")Date beginTime,@Param("endTime")Date endTime);
	
	/**
	 * 砍价成功人数
	 * @param regId
	 * @return
	 */
	@Select("select "+
			"	DATE_FORMAT(gmt_create,'%Y-%m-%d %H') time, "+
			"	count(*) num "+
			"from wx_act_bargain_registration "+
			"where complete_status=10 and exception_status=0 and is_deleted=0 "+
			"and act_id = #{actId} "+
			"and gmt_create >= #{beginTime} "+
			"and gmt_create <= #{endTime} "+
			"group by DATE_FORMAT(gmt_create,'%Y-%m-%d %H') ")
	List<BargainRegCountResultDto> selectRegSuccessCount(@Param("actId") String actId,@Param("beginTime")Date beginTime,@Param("endTime")Date endTime);
	
	/**
	 * 所有参与砍价的人数(包括自己,好友,装进我家)
	 * @param regId
	 * @return
	 */
	@Select("select "+
			"	DATE_FORMAT(gmt_create,'%Y-%m-%d %H') time,"+
			"	sum(invite_cut_record_count) num "+
			"from wx_act_bargain_registration "+
			"where is_deleted=0 "+
			"and act_id = #{actId} "+
			"and gmt_create >= #{beginTime} "+
			"and gmt_create <= #{endTime} "+
			"group by DATE_FORMAT(gmt_create,'%Y-%m-%d %H') ")
	List<BargainRegCountResultDto> selectCutCount(@Param("actId") String actId,@Param("beginTime")Date beginTime,@Param("endTime")Date endTime);
	


	@Select("<script> "+
			"	select * "+
			"	from "+
			"	   wx_act_bargain_registration "+
			"	where "+
			"	  is_deleted=0 and "+
			"	  act_id IN "+
			"	  <foreach collection='ids' open='(' separator=',' close=')' item='id'> "+
			"		  #{id} "+
			"	  </foreach> "+
			"</script> ")
    List<BargainRegistration> getBargainRegistrationsByActIds(@Param("ids") List<String> ids);

    @Select("<script> "+
    		"	select * "+
    		"	from "+
    		"	   wx_act_bargain_registration "+
    		"	where "+
    		"	  is_deleted=0 and "+
    		"	  id IN "+
    		"	  <foreach collection='regIdList' open='(' separator=',' close=')' item='id'> "+
    		"		  #{id} "+
    		"	  </foreach> "+
    		"</script> ")
	List<BargainRegistration> selectWxActBargainRegistrationByIdList(@Param("regIdList") List<String> regIdList);

}
