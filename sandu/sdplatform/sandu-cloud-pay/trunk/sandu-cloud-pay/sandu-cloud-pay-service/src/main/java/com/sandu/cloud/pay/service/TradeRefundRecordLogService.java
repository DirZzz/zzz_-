package com.sandu.cloud.pay.service;

import com.sandu.cloud.pay.model.TradeRefundRecordLog;



public interface TradeRefundRecordLogService {

    
   /**
    * 记录交易日志
    * @param payTradeRecordLog
    * @return
    */
    Long add(TradeRefundRecordLog tradeRefundRecordLog);

    /**
     * 记录外部通知信息
     * @param tradeId
     * @param jsonNotifyBody
     */
    void saveExternalNotifyBody(Long tradeRefundId, String jsonNotifyBody);

    /**
     * 保存内部系统通知报文
     * @param tradeId
     * @param request
     * @param response
     */
	void saveInternalSystemNotifyLog(Long tradeRefundId, String request, String response);

    
}
