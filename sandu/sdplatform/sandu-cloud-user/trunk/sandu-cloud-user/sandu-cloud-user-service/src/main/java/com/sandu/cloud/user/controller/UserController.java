package com.sandu.cloud.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.pay.constants.PayMethodContants;
import com.sandu.cloud.pay.dto.PayParamDto;
import com.sandu.cloud.pay.dto.RefundParamDto;
import com.sandu.cloud.pay.dto.TransfersParamDto;
import com.sandu.cloud.user.client.PayServiceClient;
import com.sandu.cloud.user.exception.UserBizException;
import com.sandu.cloud.user.exception.UserBizExceptionCode;
import com.sandu.cloud.user.model.User;
import com.sandu.cloud.user.service.UserService;

@RestController
@RefreshScope
@RequestMapping("v1/user")
public class UserController {

	@Value("${custom.property.jdbc.url}")
	private String testProperty;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PayServiceClient payServiceClient;
	

	@GetMapping(path = "/test")
	public String getCurrentAccount() {
		return testProperty;
	}
	
	@RequestMapping(path = "/queryById", method = RequestMethod.GET)
	public User queryById() {
		return null;
	}
	
	@PostMapping("/testException")
    public void testException() {
		//int a = 1/0;
		throw new UserBizException(UserBizExceptionCode.SD_ERR_USER_NOT_EXIST);
    }
	
	@PostMapping("/testPay")
    public void testPay() {
		PayParamDto payParamDto = new PayParamDto();
		//本系统自己生成的交易号,可以通过此交易号去支付服务查询相关交易
		payParamDto.setIntenalTradeNo("123456");
		//交易业务描述
		payParamDto.setTradeDesc("度币充值");
		//支付金额,单位为分
		payParamDto.setTotalFee(1L);
		//支付方式: 微信扫码支付,h5支付,app支付,小程序支付,支付宝扫码支付,h5支付,app支付
		payParamDto.setPayMethod(PayMethodContants.WX_SCANCODE_PAY);
		//客户端ip
		payParamDto.setIp("127.0.0.1");
		//支付结果回调通知地址
		payParamDto.setNotifyUrl("http://127.0.0.1:8089/v1/pay/callback/test/notify");
		//客户端id:服务id或者应用id,需要在支付服务注册
		payParamDto.setClientId("001"); 
		//第三方收款应用id
		payParamDto.setAppId("2016092301955254");
		//需要保证上述参数设置完成后再生成签名
		payParamDto.setSign(payParamDto.createSign("123456"));
		//返回结果为json格式
		String result = payServiceClient.doPay(payParamDto);
		//本应用系统做业务处理
		System.out.println(result);
    }
	
	@PostMapping("/testRefund")
    public void testRefund() {
		RefundParamDto refundParamDto = new RefundParamDto();
		//付款时,支付系统返回的支付交易号
		refundParamDto.setOriginPayTradeNo("20190520163536993810774284495696");
		//本系统自己生成的退款交易号,可以通过此交易号去支付服务查询相关退款交易
		refundParamDto.setInternalRefundNo("654321");
		//原支付金额,以分为单位
		refundParamDto.setTotalFee(1L);
		//退款金额,以分为单位
		refundParamDto.setRefundFee(1L);
		//原支付方式
		refundParamDto.setPayMethod(PayMethodContants.WX_SCANCODE_PAY);
		//退款交易描述
		refundParamDto.setRefundDesc("取消订单退款");
		//客户端ip
		refundParamDto.setIp("127.0.0.1");
		//退款结果回调通知地址
		refundParamDto.setNotifyUrl("http://127.0.0.1:8089/v1/refund/callback/test/notify");
		//客户端id:服务id或者应用id,需要在支付服务注册
		refundParamDto.setClientId("001");
		//第三方收款应用id
		refundParamDto.setAppId("wxd4934d0dab14d276");
		//需要保证上述参数设置完成后再生成签名
		refundParamDto.setSign(refundParamDto.createSign("123456"));
		//返回退款结果为json格式
		String result = payServiceClient.doRefund(refundParamDto);
		//本应用系统做业务处理
		System.out.println(result);
    }
	
	
	@PostMapping("/testTransfers")
    public void testTransfers() {
		TransfersParamDto transfersParamDto = new TransfersParamDto();
		//应用系统自己生成的付款交易号,可以通过此交易号去支付服务查询相关付款交易
		transfersParamDto.setIntenalTradeNo("112233");
		//小程序用户openId
		transfersParamDto.setOpenId("o-D9N5SS_eyau3b8Q1qZMo_FWxB");
		//付款金额,最少0.3元
		transfersParamDto.setAmount(30L);
		//交易描述
		transfersParamDto.setTradeDesc("tradeDesc");
		//微信小程序支付
		transfersParamDto.setPayMethod(PayMethodContants.WX_MINI_PAY);
		//客户端ip
		transfersParamDto.setIp("127.0.0.1");
		//客户端id:服务id或者应用id,需要在支付服务注册
		transfersParamDto.setClientId("001");
		//第三方收付款应用id
		transfersParamDto.setAppId("wx42e6b214e6cdaed3");
		//需要保证上述参数设置完成后再生成签名
		transfersParamDto.setSign(transfersParamDto.createSign("123456"));
		//返回退款结果为json格式
		String result = payServiceClient.doTransfer(transfersParamDto);
		//本应用系统做业务处理
		System.out.println(result);
    }
	
	
}
