package com.xxs.sdk.util;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xxs.sdk.app.AppContext;

/**
 * 格式转换的工具类
 * 
 * @author xiongxs
 * @date 2014-10-08
 */
public class TransformUtil {
	private static String LOG_TAG = TransformUtil.class.getName();
	/**
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";

	/**
	 * 将字符串编码成16进制编码,适用于所有字符（包括中文）
	 * 
	 * @param string
	 *            待转换的字符串
	 * @return 转换后得到的16进制编码
	 */
	public static String encodeStringToHex(String string) {
		if(TextUtils.isEmpty(string)){
			return null;
		}
		// 根据默认编码获取字节数组
		byte[] bytes = string.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 将16进制编码解码成字符串,适用于所有字符（包括中文）
	 * 
	 * @param string
	 *            待转换的16进制编码字符串
	 * @return 转换后得到的字符串
	 */
	public static String decodeHexToString(String string) {
		if(TextUtils.isEmpty(string)){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				string.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < string.length(); i += 2)
			baos.write((hexString.indexOf(string.charAt(i)) << 4 | hexString
					.indexOf(string.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	/**
	 * 去除html标签的方法
	 * 
	 * @param htmlStr
	 *            取出前的字符串
	 * @return 去除后的字符串
	 */
	public static String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符 v
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern
				.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
		htmlStr.replaceAll("&nbsp;", "");
		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 半角转换为全角
	 * 
	 * @param string
	 *            待转换的字符串
	 * @return 转换后得到的全角字符串
	 */
	public static String toDBC(String string) {
		char[] c = string.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127 && c[i] > 32)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/**
	 * 将dp转换为实际的像素值px
	 * 
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(float dpValue) {
		float scale = AppContext.mMainContext.getResources()
				.getDisplayMetrics().density;
		return (int) (dip2px(dpValue, scale) + 0.5f);
	}

	/**
	 * 将像素值px转换为dp
	 * 
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(float pxValue) {
		float scale = AppContext.mMainContext.getResources()
				.getDisplayMetrics().density;
		return (int) (px2dip(pxValue, scale) + 0.5f);
	}

	/**
	 * 将px值转换为sp值
	 * 
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(float pxValue) {
		float scale = AppContext.mMainContext.getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (px2sp(pxValue, scale) + 0.5f);
	}

	/**
	 * 将sp值转换为px值
	 * 
	 * @param spValue
	 * @return
	 */
	public static int sp2px(float spValue) {
		float scale = AppContext.mMainContext.getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (sp2px(spValue, scale) + 0.5f);
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	private static float px2dip(float pxValue, float scale) {
		return pxValue / scale;
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	private static float dip2px(float dipValue, float scale) {
		return dipValue * scale;
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	private static float px2sp(float pxValue, float fontScale) {
		return pxValue / fontScale;
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	private static float sp2px(float spValue, float fontScale) {
		return spValue * fontScale;
	}

	/**
	 * 将InputStream转换为String的方法
	 * 
	 * @param inputStream
	 *            输入流
	 * @return 转换后的String
	 * @throws java.io.IOException
	 *             IO异常
	 */
	public static String inputstream2String(InputStream inputStream)
			throws SocketException, IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inputStream.close();
		String result = new String(outStream.toByteArray());
		return result;
	}
}
