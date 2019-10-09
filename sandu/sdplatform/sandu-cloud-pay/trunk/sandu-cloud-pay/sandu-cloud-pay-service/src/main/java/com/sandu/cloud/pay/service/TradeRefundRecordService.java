package com.sandu.cloud.pay.service;

import java.util.List;

import com.sandu.cloud.pay.dto.TradeRefundQueryDto;
import com.sandu.cloud.pay.model.TradeRefundRecord;


public interface TradeRefundRecordService {

	/**
	 * 增加交易流水
	 * @param payTradeRecord
	 * @return
	 */
	Long add(TradeRefundRecord tradeRefundRecord);


	int modify(TradeRefundRecord tradeRefundRecord);
	
	List<TradeRefundRecord> getList(TradeRefundQueryDto queryVo);


	int changeToProcessStatus(String payRefundNo);


	//Object getAliRefundApiConfig();
}
