package com.sandu.cloud.pay.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name="pay2_trade_transfers_record")
@Data
public class TradeTransfersRecord{

	public static final Integer STATUS_BEGIN = 10;
	public static final Integer STATUS_SUCCESS = 30;
	public static final Integer STATUS_FAILURE = 40;
	
	public static final Integer PAY_METHOD_WX_MINIPRO = 1; //微信小程序付款
	
	
	
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 支付系统生成的交易号
	 */
	private String payTradeNo;
	
	/**
	 * 内部交易号,(订单,充值...)模块传过来的交易号,通常是订单号
	 */
	private String intenalTradeNo;
	
	/**
	 * 外部交易号(阿里,腾讯的交易号...)
	 */
	private String extenalTradeNo;
	
	/**
	 * 付款方式:1.微信小程序付款
	 */
	private Integer payMethod;
	
	/**
	 * 付款金额
	 */
	private Long amount;
	
	/**
	  * 交易时间
	  */
	private Date  tradeDate;
	
	/**
	 * 付款描述
	 */
	private String tradeDesc;
	
	/**
	 * 收款人微信openid
	 */
	private String openId;
	
	/**
	 * 签名:使用MD5
	 */
	private String sign; 
	
	
	/**
	 * 交易状态:10.开始,30.成功,40.失败
	 */
	private Integer status;
	
	/**
	 * 付款成功时间
	 */
	private String paymentTime;
	
 
	/**
	 * ip
	 */
	private String ip;
	
	
	private String clientId;
	
	/**
	 * appId
	 */
	private String appId; 
	
	/**
	 * 创建时间
	 */
	private Date gmtCreate; 
	
	/**
	 * 修改时间
	 */
	private Date gmtModified;
	/**
	 * 是否删除
	 */
	private Integer isDeleted; 
	/**
	 * 备注
	 */
	private String remark;
	
}
