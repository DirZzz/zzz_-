package com.sandu.cloud.pay.service;

import java.util.List;

import com.sandu.cloud.pay.dto.TradeQueryDto;
import com.sandu.cloud.pay.model.TradeRecord;


public interface TradeRecordService {

	/**
	 * 增加交易流水
	 * @param payTradeRecord
	 * @return
	 */
	Long addPayTradeRecord(TradeRecord tradeRecord);

	/**
	 * 获取交易
	 * @param payTradeNo
	 * @return
	 */
	TradeRecord getTradeRecord(String payTradeNo);

	/**
	 * 修改外部交易号及交易状态
	 * @param id
	 * @param extenalTradeNo
	 * @param status
	 * @return
	 */
	int modifyExtenalTradeNoAndStatus(Long id, String extenalTradeNo,Integer status);

	/**
	 * 修改内部系统通知结果
	 * @param id
	 * @param notifyResult
	 */
	void modifyInternalNotifyResult(Long id, Integer notifyResult);

	/**
	 * 开始状态修改成回调处理中
	 * @param payTradeNo
	 */
	int changeToProcessStatus(String payTradeNo);
	
	
	List<TradeRecord> getList(TradeQueryDto queryVo);

	String getTradeRecordByIntenalTradeNo(String internalTradeNo);

	String getTradeRecordByPayTradeNo(String payTradeNo);
}
