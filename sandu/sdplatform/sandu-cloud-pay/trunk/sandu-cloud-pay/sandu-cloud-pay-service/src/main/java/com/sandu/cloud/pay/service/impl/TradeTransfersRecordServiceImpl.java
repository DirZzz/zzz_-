package com.sandu.cloud.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.pay.dao.TradeTransfersRecordDao;
import com.sandu.cloud.pay.model.TradeTransfersRecord;
import com.sandu.cloud.pay.service.TradeTransfersRecordService;


@Service("payTradeTransfersRecordService")
public class TradeTransfersRecordServiceImpl implements TradeTransfersRecordService {

	@Autowired
	private TradeTransfersRecordDao tradeTransfersRecordDao;
   
	@Override
	public Long add(TradeTransfersRecord payTradeTransfersRecord) {
		tradeTransfersRecordDao.insertSelective(payTradeTransfersRecord);
		return payTradeTransfersRecord.getId();
	}

	@Override
	public int modify(TradeTransfersRecord payTradeTransfersRecord) {
		return tradeTransfersRecordDao.updateByPrimaryKeySelective(payTradeTransfersRecord);
	}
	
	   
}
