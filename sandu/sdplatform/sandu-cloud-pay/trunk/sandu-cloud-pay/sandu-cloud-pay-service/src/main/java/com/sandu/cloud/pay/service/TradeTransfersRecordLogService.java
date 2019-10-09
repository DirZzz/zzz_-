package com.sandu.cloud.pay.service;

import com.sandu.cloud.pay.model.TradeTransfersRecordLog;



public interface TradeTransfersRecordLogService {

    
   /**
    * 记录交易日志
    * @param payTradeRecordLog
    * @return
    */
    Long add(TradeTransfersRecordLog tradeTransfersRecordLog);

    
}
