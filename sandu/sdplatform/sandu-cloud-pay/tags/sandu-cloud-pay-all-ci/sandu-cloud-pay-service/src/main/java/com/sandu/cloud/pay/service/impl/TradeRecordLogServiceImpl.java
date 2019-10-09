package com.sandu.cloud.pay.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.pay.dao.TradeRecordLogDao;
import com.sandu.cloud.pay.model.TradeRecordLog;
import com.sandu.cloud.pay.service.TradeRecordLogService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


@Service("payTradeRecordLogService")
public class TradeRecordLogServiceImpl implements TradeRecordLogService {

    
    @Autowired
    private TradeRecordLogDao tradeRecordLogDao;

    @Override
    public Long addPayTradeRecordLog(TradeRecordLog tradeRecordLog) {
        tradeRecordLogDao.insertSelective(tradeRecordLog);
        return tradeRecordLog.getId();
    }

	@Override
	public void saveExternalNotifyBody(Long tradeId, String jsonNotifyBody) {
		TradeRecordLog tradeRecordLog = new TradeRecordLog();
		tradeRecordLog.setExternalNotifyBody(jsonNotifyBody);
		tradeRecordLog.setGmtModified(new Date());
		Example example = new Example(TradeRecordLog.class);
		Criteria  criteria = example.createCriteria();
		criteria.andEqualTo("tradeId", tradeId);
		tradeRecordLogDao.updateByExampleSelective(tradeRecordLog, example);
	}

	@Override
	public void saveInternalSystemNotifyLog(Long tradeId, String request, String response) {
		TradeRecordLog tradeRecordLog = new TradeRecordLog();
		tradeRecordLog.setInternalNotifyRequest(request);
		tradeRecordLog.setInternalNotifyResponse(response);
		tradeRecordLog.setGmtModified(new Date());
		Example example = new Example(TradeRecordLog.class);
		Criteria  criteria = example.createCriteria();
		criteria.andEqualTo("tradeId", tradeId);
		tradeRecordLogDao.updateByExampleSelective(tradeRecordLog, example);
	}

   
}
