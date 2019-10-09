package com.sandu.cloud.pay.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.pay.dao.TradeRefundRecordLogDao;
import com.sandu.cloud.pay.model.TradeRefundRecordLog;
import com.sandu.cloud.pay.service.TradeRefundRecordLogService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


@Service("payTradeRefundRecordLogService")
public class TradeRefundRecordLogServiceImpl implements TradeRefundRecordLogService {

    
    @Autowired
    private TradeRefundRecordLogDao tradeRefundRecordLogDao;

	@Override
	public Long add(TradeRefundRecordLog payTradeRefundRecordLog) {
		tradeRefundRecordLogDao.insertSelective(payTradeRefundRecordLog);
		return payTradeRefundRecordLog.getId();
	}

	@Override
	public void saveExternalNotifyBody(Long tradeRefundId, String jsonNotifyBody) {
		TradeRefundRecordLog tradeRefundRecordLog = new TradeRefundRecordLog();
		tradeRefundRecordLog.setExternalNotifyBody(jsonNotifyBody);
		tradeRefundRecordLog.setGmtModified(new Date());
		
		Example example = new Example(TradeRefundRecordLog.class);
		Criteria  criteria = example.createCriteria();
		criteria.andEqualTo("tradeRefundId", tradeRefundId)
				.andEqualTo("isDeleted", 0);
		tradeRefundRecordLogDao.updateByExampleSelective(tradeRefundRecordLog, example);
	}

	@Override
	public void saveInternalSystemNotifyLog(Long tradeRefundId, String request, String response) {
		TradeRefundRecordLog tradeRefundRecordLog = new TradeRefundRecordLog();
		tradeRefundRecordLog.setInternalNotifyRequest(request);
		tradeRefundRecordLog.setInternalNotifyResponse(response);
		tradeRefundRecordLog.setGmtModified(new Date());
		
		Example example = new Example(TradeRefundRecordLog.class);
		Criteria  criteria = example.createCriteria();
		criteria.andEqualTo("tradeRefundId", tradeRefundId)
				.andEqualTo("isDeleted", 0);
		tradeRefundRecordLogDao.updateByExampleSelective(tradeRefundRecordLog, example);
		
	}

   

   
}
