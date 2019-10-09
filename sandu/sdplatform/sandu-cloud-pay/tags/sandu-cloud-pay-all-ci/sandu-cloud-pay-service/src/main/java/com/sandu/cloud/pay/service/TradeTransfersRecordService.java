package com.sandu.cloud.pay.service;

import com.sandu.cloud.pay.model.TradeTransfersRecord;


public interface TradeTransfersRecordService {

	/**
	 * 增加交易流水
	 * @param payTradeRecord
	 * @return
	 */
	Long add(TradeTransfersRecord tradeTransfersRecord);


	int modify(TradeTransfersRecord tradeTransfersRecord);
	

   
}
