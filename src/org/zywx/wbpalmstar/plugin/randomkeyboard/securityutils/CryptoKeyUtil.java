/**
 * @Author lewis(lgs@yitong.com.cn) 2014-3-21 上午10:53:19
 * @Class CryptoKeyUtil
 * Copyright (c) 2014 Shanghai P&C Information Technology Co.,Ltd. All rights reserved.
 */
package org.zywx.wbpalmstar.plugin.randomkeyboard.securityutils;

import java.security.Key;

public class CryptoKeyUtil {
	private static Key k;

	static {
		createKey();
	}

	/**
	 * 加密（键盘使用，单个字符加密）
	 * 
	 * @Description
	 * @param result
	 *            上一次结果
	 * @param input
	 *            点击字符串
	 * @return
	 * @Author lewis(lgs@yitong.com.cn) 2014-3-21 上午11:10:10
	 */
	public static StringBuffer encrypt(StringBuffer result, String input) {
		result.append(input);
		try {
			return new StringBuffer(BASE64Custom.encode(AESCoder.encrypt(result
					.toString().getBytes(), k)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @Description
	 * @param params
	 *            密文参数
	 * @return
	 * @Author lewis(lgs@yitong.com.cn) 2014-3-21 下午2:21:52
	 */
	public static StringBuffer delDecrypt(StringBuffer params) {
		StringBuffer result = new StringBuffer();
		try {
			String decryptStr = new String(AESCoder.decrypt(
					BASE64Custom.decode(params.toString()), k));
			// 删除最后一位数据
			result.append(decryptStr.substring(0, decryptStr.length() - 1));
			return result;
		} catch (Exception e) {
			// 解密异常
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密方法
	 * 
	 * @Description
	 * @param params
	 *            加密的参数
	 * @param length
	 *            实际长度
	 * @return
	 * @Author lewis(lgs@yitong.com.cn) 2014-3-21 下午2:06:30
	 */
	public static StringBuffer decrypt(StringBuffer params, int length) {
		StringBuffer result = new StringBuffer();
		String end = params.toString();
		for (int i = 0; i < length; i++) {
			String decryptStr;
			try {
				decryptStr = new String(AESCoder.decrypt(
						BASE64Custom.decode(end), k));
				if (null != decryptStr && decryptStr.length() > 0) {
					// 最后以为为实际解密数据
					result.append(decryptStr.substring(decryptStr.length() - 1));
					// 记录解密后去除真实数据的密文
					end = decryptStr.substring(0, decryptStr.length() - 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// 异常跳出循环
				break;
			}
		}
		// 反致数据
		return result.reverse();
	}

	/**
	 * 解密（该方法用于没有记录长度的解密）
	 * 
	 * @Description
	 * @param params
	 *            密文参数
	 * @return
	 * @Author lewis(lgs@yitong.com.cn) 2014-3-25 上午12:14:23
	 */
	public static StringBuffer decrypt(String params) {
		StringBuffer result = new StringBuffer();
		String end = params;
		do {
			try {
				// 执行解密
				String decryptStr = new String(AESCoder.decrypt(
						BASE64Custom.decode(end), k));
				// 解密后数据为空 直接跳出请求
				if ((decryptStr == null) || (decryptStr.length() == 0))
					break;
				// 解密后字段只有一位，代表已经解密到最后一位，直接结束解密
				if (decryptStr.length() == 1) {
					// 拼接最后一位数据
					result.append(decryptStr);
					break;
				}
				// 最后一位为实际解密数据
				result.append(decryptStr.substring(decryptStr.length() - 1));
				// 记录解密后去除真实数据的密文用于下次解密
				end = decryptStr.substring(0, decryptStr.length() - 1);
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				// 异常跳出循环
				break;
			}
		} while (true);

		// 反致数据
		return result.reverse();
	}

	/**
	 * 加密（用于整文本加密）
	 * 
	 * @Description
	 * @param str
	 *            待加密文本
	 * @return 加密后文本
	 * @Author lewis(lgs@yitong.com.cn) 2014-3-25 下午6:13:19
	 */
	public static String encryptAll(String str) {
		try {
			return BASE64Custom.encode(AESCoder.encrypt(str.getBytes(), k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 解密（解密整文本）
	 * 
	 * @Description
	 * @param params
	 * @return
	 * @Author lewis(lgs@yitong.com.cn) 2014-3-26 上午9:47:42
	 */
	public static String decryptAll(String params) {
		try {
			return new String(AESCoder.decrypt(BASE64Custom.decode(params), k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建加密key
	 * 
	 * @Description
	 * @Author lewis(lgs@yitong.com.cn) 2014-3-21 上午11:00:31
	 */
	private static void createKey() {
		if (AESCoder.IV.length() != 16) {
			// 默认KEY
			k = AESCoder.toKey("appcan.cn".getBytes());
		} else {
			k = AESCoder.toKey(AESCoder.IV.getBytes());
		}
	}

}
