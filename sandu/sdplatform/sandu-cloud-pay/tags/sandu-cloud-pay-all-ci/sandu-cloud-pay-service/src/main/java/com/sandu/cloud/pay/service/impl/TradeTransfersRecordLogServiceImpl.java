package com.sandu.cloud.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.pay.dao.TradeTransfersRecordLogDao;
import com.sandu.cloud.pay.model.TradeTransfersRecordLog;
import com.sandu.cloud.pay.service.TradeTransfersRecordLogService;


@Service("payTradeTransfersRecordLogService")
public class TradeTransfersRecordLogServiceImpl implements TradeTransfersRecordLogService {
    
    @Autowired
    private TradeTransfersRecordLogDao tradeTransfersRecordLogDao;

	@Override
	public Long add(TradeTransfersRecordLog payTradeTransfersRecordLog) {
		tradeTransfersRecordLogDao.insertSelective(payTradeTransfersRecordLog);
		return payTradeTransfersRecordLog.getId();
	}
   
}
