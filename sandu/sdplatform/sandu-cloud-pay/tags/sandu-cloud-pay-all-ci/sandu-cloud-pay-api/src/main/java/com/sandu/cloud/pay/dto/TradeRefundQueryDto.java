package com.sandu.cloud.pay.dto;

import lombok.Data;

@Data
public class TradeRefundQueryDto {

	/**
	 * 原支付系统生成的交易号
	 */
	private String originPayTradeNo;
	
	/**
	 * 原内部交易号,(订单,充值...)模块传过来的交易号,通常是订单号
	 */
	private String originInternalTradeNo;
	
	private String payRefundNo;
	
}
