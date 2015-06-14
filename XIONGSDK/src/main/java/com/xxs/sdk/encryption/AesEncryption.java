package com.xxs.sdk.encryption;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;

import com.xxs.sdk.util.Base64;
import com.xxs.sdk.util.LogUtil;


/**
 * 　
 * Aes加密
 * @author xxs
 * @date 2014-10-08
 * @introduce 密码学中的高级加密标准（Advanced Encryption Standard，AES），又称 高级加密标准Rijndael加密法， 是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。
 * 
 */
public class AesEncryption {

	private static String LOG_TAG = AesEncryption.class.getName();

	// 明文 !@#123&*(789
	public static final String	AES_KEY	= "tEZy+5kxPwR2dVEGy+64PQ==";

	/**
	 * 主要用于存储本地
	 * 
	 * @param password
	 *            明文密码
	 * @return 应用数据加密密码
	 */
	@SuppressLint("TrulyRandom")
	public final static String getKey(String password) {
		String key = "";
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes("UTF-8")));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			return Base64.encodeToString(enCodeFormat);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
		return key;
	}

	/**
	 * Aes加密byte[] to byte[]
	 * 
	 * @param bSrc
	 *            待加密数据
	 * @param sKey
	 *            16位密钥
	 * @return 成功返回加密后的数据，失败返回源数据
	 */
	public static byte[] Encrypt(byte[] bSrc, String sKey) {
		try {
			if (sKey == null) {
				LogUtil.d(LOG_TAG, "Key为null 或者 Key的长度不是16");
				return bSrc;
			}
			byte[] raw = Base64.decodeToBytes(sKey);
			if (raw.length != 16) {
				LogUtil.d(LOG_TAG, "Key为null 或者 Key的长度不是16");
				return bSrc;
			}
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] bIV = new byte[16];
			IvParameterSpec iv = new IvParameterSpec(bIV);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			return cipher.doFinal(bSrc);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
		return bSrc;
	}

	/**
	 * Aes加密String to byte[]
	 * 
	 * @param sSrc
	 *            待加密数据
	 * @param sKey 16位密钥
	 * @return 成功返回加密后的数据，失败返回sSrc.getBytes()
	 */
	public static byte[] Encrypt(String sSrc, String sKey) {
		try {
			return Encrypt(sSrc.getBytes("UTF-8"), sKey);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
			return sSrc == null ? null : sSrc.getBytes();
		}
	}

	/**
	 * 加密转换成base64字符串
	 * 
	 * @param sSrc
	 * @param sKey
	 * @return
	 */
	public static String Encrypt2Str(String sSrc, String sKey) {
		try {
			if (sSrc != null)
				return Base64.encodeToString(Encrypt(sSrc.getBytes("UTF-8"), sKey));
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
		return null;
	}

	/**
	 * 解密base64字符串
	 * 
	 * @param sSrc
	 * @param sKey
	 * @return
	 */
	public static String Decrypt2Str(String sSrc, String sKey) {
		try {
			if (sSrc != null)
				return DecryptToString(Base64.decodeToBytes(sSrc), sKey, -1);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
		return null;
	}

	/**
	 * AES解密
	 * 
	 * @param bSrc
	 * @param sKey
	 * @return
	 */
	public static byte[] Decrypt(byte[] bSrc, String sKey) {
		return Decrypt(bSrc, sKey, -1);
	}

	/**
	 * Aes解密数据 byte[] to byte[]
	 * 
	 * @param bSrc
	 *            待解密数据
	 * @param sKey
	 *            16位密钥
	 * @param oriLen
	 *            原始长度，-1，表示未知
	 * @return 成功返回解密后数据，失败返回源数据
	 */
	public static byte[] Decrypt(byte[] bSrc, String sKey, int oriLen) {

		try {
			if (sKey == null) {
				LogUtil.d(LOG_TAG, "Key为null 或者 Key的长度不是16");
				return bSrc;
			}
			byte[] raw = Base64.decodeToBytes(sKey);
			if (raw.length != 16) {
				LogUtil.d(LOG_TAG, "Key为null 或者 Key的长度不是16");
				return bSrc;
			}
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] bIV = new byte[16];
			IvParameterSpec iv = new IvParameterSpec(bIV);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] enData = cipher.doFinal(bSrc);

			// 暂时oriLen 为 -1
			if (enData.length > oriLen && oriLen > 0) {
				byte[] newData = new byte[oriLen];
				System.arraycopy(enData, 0, newData, 0, oriLen);
				enData = newData;
			}
			return enData;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
		return bSrc;
	}

	/**
	 * 把数据解密成字符串
	 * 
	 * @param bSrc
	 * @param sKey
	 * @return
	 */
	public static String DecryptToString(byte[] bSrc, String sKey) {
		return DecryptToString(bSrc, sKey, -1);
	}

	/**
	 * Aes解密数据 byte[] to String
	 * 
	 * @param bSrc
	 *            待解密数据
	 * @param sKey
	 *            16位密钥
	 * @param oriLen
	 *            原始长度，-1，表示未知
	 * @return 成功返回解密后字符串，失败返回空字符
	 */
	public static String DecryptToString(byte[] bSrc, String sKey, int oriLen) {
		try {
			byte[] deData = Decrypt(bSrc, sKey, oriLen);
			return new String(deData, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
		return null;
	}
}
