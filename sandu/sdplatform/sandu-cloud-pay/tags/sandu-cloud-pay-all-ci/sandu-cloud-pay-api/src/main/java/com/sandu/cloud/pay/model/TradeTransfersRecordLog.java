package com.sandu.cloud.pay.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name="pay2_trade_transfers_record_log")
@Data
public class TradeTransfersRecordLog{
	
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 交易id
	 */
	private Long tradeTransfersId;
	
	/**
	 * '外部请求信息(阿里,腾讯...)
	 */
	private String externalRequest;
	
	/**
	 * 外部请求返回信息(阿里,腾讯...)
	 */
	private String externalResponse;
	
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
