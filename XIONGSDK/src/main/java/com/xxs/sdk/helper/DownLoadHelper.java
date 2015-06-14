package com.xxs.sdk.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * 下载工具辅助类
 * 
 * @author xiongxs
 * @date 2015-04-20
 */
public class DownLoadHelper {
	/** 请求超时 */
	private static final int TIMEOUT = 30000;
	/** 连接超时 */
	private static final int TIMEOUT_SOCKET = 30000;
	private HttpURLConnection httpUrlConn;

	/**
	 * 获取下载数据流的方法
	 * 
	 * @param path
	 *            下载路径
	 * @return 得到的下载数据流
	 * @throws java.net.MalformedURLException
	 *             下载地址错误
	 * @throws java.net.SocketTimeoutException
	 *             连接超时
	 * @throws java.net.SocketException
	 *             网络连接错误
	 * @throws java.io.IOException
	 *             数据异常
	 */
	public InputStream getDownLoadRequest(String path)
			throws MalformedURLException, SocketTimeoutException,
			SocketException, IOException {
		URL url = new URL(path);
		httpUrlConn = (HttpURLConnection) url.openConnection();
		httpUrlConn.setConnectTimeout(TIMEOUT);// 设置5秒连接超时
		httpUrlConn.setReadTimeout(TIMEOUT_SOCKET);// 设置5秒读取响应超时
		httpUrlConn.connect();
		return httpUrlConn.getInputStream();
	}

	/** 断开下载连接请求的方法 */
	public void disConnectHttp() throws Exception {
		if (httpUrlConn != null)
			httpUrlConn.disconnect();
	}
}
