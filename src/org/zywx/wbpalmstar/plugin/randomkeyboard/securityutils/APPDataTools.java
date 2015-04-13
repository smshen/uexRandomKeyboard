package org.zywx.wbpalmstar.plugin.randomkeyboard.securityutils;
/**
 * 加密算法接口
 * @author twf
 *
 */
public interface APPDataTools {
	
	/**
	 * 报文加密方法
	 * @param msgStr 源报文数据
	 * @return 加密后数据
	 */
	public String encryptMsgStr(String msgStr)throws Exception;
	
	/**
	 * 报文解密算法
	 * @param msgStr 源加密数据
	 * @return 解密后数据
	 */
	public String decryptionMsgStr(String msgStr)throws Exception;
}
