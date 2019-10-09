package com.sandu.cloud.pay.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 微信支付的统一下单工具类
 */
public class PaymentKit {
	

	/**
	 * 组装签名的字段
	 * 
	 * @param params
	 *            参数
	 * @param urlEncoder
	 *            是否urlEncoder
	 * @return {String}
	 */
	public static String packageSign(Map<String, String> params, boolean urlEncoder) {
		// 先将参数以其参数名的字典序升序进行排序
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> param : sortedParams.entrySet()) {
			String value = param.getValue();
			if (StringUtils.isBlank(value)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(param.getKey()).append("=");
			if (urlEncoder) {
				try {
					value = urlEncode(value);
				} catch (UnsupportedEncodingException e) {
				}
			}
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * urlEncode
	 * 
	 * @param src
	 *            微信参数
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             编码错误
	 */
	public static String urlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, Charset.forName("UTF-8").name()).replace("+", "%20");
	}
	

	/**
	 * 生成签名
	 * 
	 * @param params
	 *            参数
	 * @param partnerKey
	 *            支付密钥
	 * @return {String}
	 * 
	 */
	public static String createSign(Map<String, String> params, String partnerKey) {
		// 生成签名前先去除sign
		params.remove("sign");
		String stringA = packageSign(params, false);
		String stringSignTemp = stringA + "&key=" + partnerKey;
		return DigestUtils.md5Hex(stringSignTemp).toUpperCase();
	}

	/**
	 * 支付异步通知时校验sign
	 * 
	 * @param params
	 *            参数
	 * @param paternerKey
	 *            支付密钥
	 * @return {boolean}
	 */
	public static boolean verifyNotify(Map<String, String> params, String paternerKey) {
		String sign = params.get("sign");
		String localSign = PaymentKit.createSign(params, paternerKey);
		return sign.equals(localSign);
	}
	
	/**
	 * 替换url中的参数
	 * @param str
	 * @param regex
	 * @param args
	 * @return {String}
	 */
	public static String replace(String str,String regex,String... args){
		int length = args.length;
		for (int i = 0; i < length; i++) {
			str=str.replaceFirst(regex, args[i]);
		}
		return str;
	}

}