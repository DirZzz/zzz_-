package com.sandu.cloud.pay.service;

import com.sandu.cloud.pay.model.TradeRecordLog;



public interface TradeRecordLogService {

    
   /**
    * 记录交易日志
    * @param payTradeRecordLog
    * @return
    */
    Long addPayTradeRecordLog(TradeRecordLog tradeRecordLog);

    /**
     * 记录外部通知信息
     * @param tradeId
     * @param jsonNotifyBody
     */
    void saveExternalNotifyBody(Long tradeId, String jsonNotifyBody);

    /**
     * 保存内部系统通知报文
     * @param tradeId
     * @param request
     * @param response
     */
	void saveInternalSystemNotifyLog(Long tradeId, String request, String response);

    
}
