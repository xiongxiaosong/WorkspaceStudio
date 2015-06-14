package com.xxs.sdk.encryption;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.xxs.sdk.util.LogUtil;

/**
 * 获取MD5码的类及MD5加密实现
 * 
 * @author xxs
 * @time 2014-03-06
 * @introduce MD5即Message-Digest Algorithm 5（信息-摘要算法 5），用于确保信息传输完整一致。是计算机广泛使用的杂凑算法之一（又译摘要算法、哈希算法），主流编程语言普遍已有MD5实现。
 */
public class Md5Encryption {
	private static String LOG_TAG =Md5Encryption.class.getName();
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 计算字节数组的MD5码
	 * @param b
	 *            字节数组
	 * @return 得到的MD5码
	 */
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 计算字符串的MD5码
	 * 
	 * @param s
	 *            待转换的字符串
	 * @return 得到的MD5码
	 */
	public static String StringToMd5(String s) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(s.getBytes("UTF-8"));
			return toHexString(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
		return s;
	}

	/**
	 * 计算文件的MD5码
	 * 
	 * @param filename
	 *            文件路径
	 * @return 得到的MD5码
	 */
	public static String FileToMd5(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
			return null;
		}
	}
}
