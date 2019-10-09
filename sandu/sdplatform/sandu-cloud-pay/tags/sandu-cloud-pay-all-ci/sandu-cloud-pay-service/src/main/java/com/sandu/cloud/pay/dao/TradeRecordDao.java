package com.sandu.cloud.pay.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.sandu.cloud.pay.model.TradeRecord;

import tk.mybatis.mapper.common.Mapper;

public interface TradeRecordDao extends Mapper<TradeRecord> {
	/**
	 * 通过交易号将 状态从'开始'修改成'回调处理中'
	 * @param payTradeNo
	 */
	@Update("update "+
			"	pay2_trade_record "+
			"set "+
			"	status=20, "+
			"	gmt_modified = now() "+
			"where "+
			"	pay_trade_no = #{payTradeNo} "+
			"	and status = 10 "+
			"	and is_deleted = 0 ")
	int updateToProcessStatus(@Param("payTradeNo")String payTradeNo);
}
