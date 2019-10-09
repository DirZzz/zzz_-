package com.sandu.cloud.pay.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sandu.cloud.pay.dao.TradeRefundRecordDao;
import com.sandu.cloud.pay.dto.TradeRefundQueryDto;
import com.sandu.cloud.pay.model.TradeRefundRecord;
import com.sandu.cloud.pay.service.TradeRefundRecordService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


@Service("payTradeRefundRecordService")
public class TradeRefundRecordServiceImpl implements TradeRefundRecordService {

	@Autowired
	private TradeRefundRecordDao tradeRefundRecordDao;
   
	@Override
	public Long add(TradeRefundRecord payTradeRefundRecord) {
		tradeRefundRecordDao.insertSelective(payTradeRefundRecord);
		return payTradeRefundRecord.getId();
	}

	@Override
	public int modify(TradeRefundRecord tradeRefundRecord) {
		return tradeRefundRecordDao.updateByPrimaryKeySelective(tradeRefundRecord);
	}
	
	@Override
	public List<TradeRefundRecord> getList(TradeRefundQueryDto queryVo) {
		TradeRefundRecord tradeRefundRecord = new TradeRefundRecord();
		BeanUtils.copyProperties(queryVo, tradeRefundRecord);
		return tradeRefundRecordDao.select(tradeRefundRecord);
	}

	@Override
	public int changeToProcessStatus(String payRefundNo) {
		TradeRefundRecord tradeRefundRecord = new TradeRefundRecord();
		tradeRefundRecord.setStatus(20);
		tradeRefundRecord.setGmtModified(new Date());
		
		Example example = new Example(TradeRefundRecord.class);
		Criteria  criteria = example.createCriteria();
		criteria.andEqualTo("payRefundNo", payRefundNo)
				.andEqualTo("status", 10)
				.andEqualTo("isDeleted", 0);
		return tradeRefundRecordDao.updateByExampleSelective(tradeRefundRecord, example);
	}

	/*@Override
	public Object getAliRefundApiConfig() {
		AliPayApiConfig aliPayApiConfig = AliPayApiConfig.New()
				.setAppId(PayConfig.ALI_APP_ID)
				.setAlipayPublicKey(PayConfig.ALI_PUBLIC_KEY)
				.setCharset("UTF-8")
				.setPrivateKey(PayConfig.ALI_PRIVATE_KEY)
				.setServiceUrl(PayConfig.ALI_SERVICE_URL)
				.setSignType("RSA2")
				.build();
		AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
		AliPayApiConfigKit.setThreadLocalAppId(PayConfig.ALI_APP_ID);
		return aliPayApiConfig;
	}*/
}
