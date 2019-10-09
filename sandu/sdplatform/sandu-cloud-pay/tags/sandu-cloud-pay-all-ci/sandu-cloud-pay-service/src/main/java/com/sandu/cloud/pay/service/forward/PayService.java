package com.sandu.cloud.pay.service.forward;

import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;

public interface PayService {

	/**
	 * 用户支付
	 * @param payParam
	 * @return
	 */
   String doPay(PayParamDto payParam);
   
   /**
    * 退款
    * @param refundParam
    * @return
    */
   String doRefund(RefundParamDto refundParam);
   
   /**
    * 企业付款
    * @param transfersParam
    * @return
    */
   String doTransfers(TransfersParamDto transfersParam);
}
