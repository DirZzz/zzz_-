package com.sandu.cloud.pay.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.pay.dao.TradeRecordDao;
import com.sandu.cloud.pay.dto.TradeQueryDto;
import com.sandu.cloud.pay.model.TradeRecord;
import com.sandu.cloud.pay.service.TradeRecordService;


@Service("payTradeRecordService")
public class TradeRecordServiceImpl implements TradeRecordService {

	@Autowired
	private TradeRecordDao tradeRecordDao;
   
	@Override
	public Long addPayTradeRecord(TradeRecord tradeRecord) {
		tradeRecordDao.insertSelective(tradeRecord);
		return tradeRecord.getId();
	}

	@Override
	public TradeRecord getTradeRecord(String payTradeNo) {
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setPayTradeNo(payTradeNo);
		tradeRecord.setIsDeleted(0);
		return tradeRecordDao.selectOne(tradeRecord);
	}

	@Override
	public int modifyExtenalTradeNoAndStatus(Long id, String extenalTradeNo,Integer status) {
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setId(id);
		tradeRecord.setExtenalTradeNo(extenalTradeNo);
		tradeRecord.setStatus(status);
		tradeRecord.setGmtModified(new Date());
		return tradeRecordDao.updateByPrimaryKeySelective(tradeRecord);
	}

	@Override
	public void modifyInternalNotifyResult(Long id, Integer notifyResult) {
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setId(id);
		tradeRecord.setNotifyResult(notifyResult);
		tradeRecord.setGmtModified(new Date());
		tradeRecordDao.updateByPrimaryKeySelective(tradeRecord);
	}

	@Override
	public int changeToProcessStatus(String payTradeNo) {
		return tradeRecordDao.updateToProcessStatus(payTradeNo);
	}

	@Override
	public List<TradeRecord> getList(TradeQueryDto queryVo) {
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setPayTradeNo(queryVo.getPayTradeNo());
		tradeRecord.setIntenalTradeNo(queryVo.getIntenalTradeNo());
		return tradeRecordDao.select(tradeRecord);
	}

	@Override
	public String getTradeRecordByIntenalTradeNo(String internalTradeNo) {
		if(StringUtils.isBlank(internalTradeNo)) {
			return null;
		}
		List<TradeRecord> list = this.queryPayTradeRecord("internalTradeNo",internalTradeNo);
		if (list != null && list.size() > 0) {
			return list.get(0).getPayTradeNo();
		}
		return null;
	}

	@Override
	public String getTradeRecordByPayTradeNo(String payTradeNo) {
		if(StringUtils.isBlank(payTradeNo)) {
			return null;
		}
		List<TradeRecord> list = this.queryPayTradeRecord("payTradeNo",payTradeNo);
		if (list != null && list.size() > 0) {
			return list.get(0).getExtenalTradeNo();
		}
		return null;
	}

	private List<TradeRecord> queryPayTradeRecord(String queryFlagByTradeNo, String payTradeNo) {
		TradeQueryDto queryVo = new TradeQueryDto();
		if (Objects.equals("payTradeNo",queryFlagByTradeNo)){
			queryVo.setPayTradeNo(payTradeNo);
		}else if(Objects.equals("internalTradeNo",queryFlagByTradeNo)){
			queryVo.setIntenalTradeNo(payTradeNo);
		}
		return getList(queryVo);
	}
}
