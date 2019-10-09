package com.sandu.cloud.activity.bargain.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sandu.cloud.activity.bargain.model.Bargain;

import tk.mybatis.mapper.common.Mapper;



public interface BargainDao extends Mapper<Bargain> {
	
   // int insertWxActBargain(Bargain bargain);

    //int updateWxActBargainById(Bargain WxActBargain);

   // int deleteWxActBargainById(@Param("actId") String actId);

  //  Bargain selectWxActBargainById(@Param("actId") String actId);
    
  //  Bargain selectWxActBargainByIdAndAppId(@Param("actId") String actId,@Param("appId") String appId);

	@Update("update wx_act_bargain "+
        	"set product_remain_count = product_remain_count-1, "+
    		"product_display_count = product_display_count-1 "+
			"where id = #{actId} and product_remain_count>0 and is_deleted=0")
	int updateToReduceProductRemainCount(@Param("actId") String actId);

	@Update("update wx_act_bargain set registration_count = registration_count+1 "+
	        "where id = #{actId} and is_deleted=0")
	void updateToIncreaseRegistrationCount(@Param("actId") String actId);

	@Select("<script> "+
			"	SELECT * "+
			"	FROM "+
			"	  wx_act_bargain "+
			"	WHERE "+
			"	  is_deleted = 0 AND "+
			"	  app_id IN "+
			"	  <foreach collection='appids' open='(' separator=',' close=')' item='appid'> "+
			"		  #{appid} "+
			"	  </foreach> "+
			"	  ORDER BY "+
			"	  gmt_create DESC "+
			"	limit #{page},#{limit} "+
			"</script> ")
    List<Bargain> selectByAppids(@Param("appids") List<String> appids,@Param("page") Integer page, @Param("limit")Integer limit);
    /**
     * 查询所有有效砍价活动
     * @author : WangHaiLin
     * @param currentTime 当前时间
     * @date : 2018/11/22 19:27
     * @return java.util.List<com.sandu.api.act.model.WxActBargainAward>
     *
     */
	@Select("select * "+
	        "from wx_act_bargain "+
	        "where  is_deleted=0 "+
	        "AND product_remain_count>0 "+
	        "AND begain_time < #{currentTime} "+
	        "AND end_time 	> #{currentTime} "+
	        "AND sys_reduce_num !=0 "+
	        "AND is_enable=1) ")
    List<Bargain> selectAllEffectiveBargainAward(@Param("currentTime") Date currentTime);

	@Select("select * "+
	        "from wx_act_bargain "+
	       	"where product_remain_count>0 "+
			"	and is_enable=1 "+
			"	and end_time > now() "+
			"	and datediff(end_time,now()) < 2 "+
			"	and is_deleted=0")
	List<Bargain> selectWillExpireList();
	
    /**
     * 定时任务，扣库存
     */
	@Update("update wx_act_bargain set "+
			"        product_remain_count = "+
			"        CASE  "+
			"        WHEN (product_remain_count - sys_reduce_num) < 0 THEN 0 "+
			"        ELSE (product_remain_count - sys_reduce_num) "+
			"        END, "+
			"        product_display_count = "+
			"        CASE  "+
			"        WHEN (product_remain_count - sys_reduce_num) < 0 THEN 0 "+
			"        ELSE (product_display_count - sys_reduce_num) "+
			"        END, "+
			"        product_vitual_count= "+
			"        CASE  "+
			"        WHEN (product_remain_count - sys_reduce_num) < 0 THEN (product_vitual_count + product_remain_count) "+
			"        ELSE (product_vitual_count + sys_reduce_num) "+
			"        END "+
			"        WHERE is_deleted=0 "+
			"        AND begain_time  < NOW() "+
			"        AND end_time 	 > NOW() "+
			"        AND sys_reduce_num !=0 "+
			"        AND is_enable=1 "+
			"        AND product_remain_count>0 ")
    void updateVitualCount();


    int countByAppids(@Param("appids")List<String> appids);

    //Bargain selectByActName(String actName);
}
