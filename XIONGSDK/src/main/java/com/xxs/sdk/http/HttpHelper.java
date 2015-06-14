package com.xxs.sdk.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpStatus;

import android.text.TextUtils;

import com.xxs.sdk.util.LogUtil;

/**
 * 网络请求工具类
 * 
 * @author xiongxs
 * @date 2014-10-08
 * @introduce 包含post请求get请求等方法
 */
public class HttpHelper {
	private static String LOG_TAG = HttpHelper.class.getName();
	private static final int TIMEOUT = 5000;
	private HttpURLConnection httpUrlConn;

	/**
	 * 发送get方式请求
	 * 
	 * @param path
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param isget
	 *            是否是get请求
	 * @throws java.io.UnsupportedEncodingException
	 *             不支持编码转换
	 * @throws java.net.MalformedURLException
	 *             url地址异常
	 * @throws java.io.IOException
	 *             IO异常
	 * @return 请求输入流InputStream
	 * @注意：调用此方法的时候对返回参数要添加非空判断，如果为空则提示数据请求失败
	 */
	public InputStream getHttpRequest(String path, Map<String, String> params,
			boolean isget) throws UnsupportedEncodingException,
			MalformedURLException, SocketTimeoutException, IOException {
		httpUrlConn = getHttpUrlConnection(path, params, isget);
		int httpcode = httpUrlConn.getResponseCode();
		LogUtil.e(LOG_TAG + "Http请求回调编码", "" + httpcode);
		if (httpUrlConn != null && httpcode == HttpStatus.SC_OK) {
			return httpUrlConn.getInputStream();
		}
		return null;
	}

	private HttpURLConnection getHttpUrlConnection(String path,
			Map<String, String> params, boolean isget)
			throws UnsupportedEncodingException, MalformedURLException,
			SocketTimeoutException, IOException {
		HttpURLConnection httpCon = null;
		StringBuilder sb = new StringBuilder();
		if (isget) {
			sb = new StringBuilder(path);
			sb.append('?');
		}
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey())
						.append('=')
						.append(URLEncoder.encode(entry.getValue().toString(),
								"UTF-8")).append('&');
			}
		}
		LogUtil.e(LOG_TAG + "请求地址:",path);
		if(!TextUtils.isEmpty(sb.toString()))
		sb.deleteCharAt(sb.length() - 1);
		LogUtil.e(LOG_TAG + "请求地址参数:",sb.toString());
		if (isget) {
			URL url = new URL(sb.toString());
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setRequestMethod("GET");
			httpCon.setDoOutput(false);
			httpCon.setDoInput(true);
			httpCon.setConnectTimeout(TIMEOUT);
		} else {
			byte[] entitydata = sb.toString().getBytes();
			URL url = new URL(path);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setRequestMethod("POST");
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setUseCaches(false);// Post 请求不能使用缓存
			httpCon.setConnectTimeout(TIMEOUT);
			httpCon.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");
			OutputStream outs = httpCon.getOutputStream();
			outs.write(entitydata);
			outs.flush();
			outs.close();
		}
		return httpCon;
	}
	/** 断开Http请求的方法 */
	public void disConnectHttp() throws Exception {
		if (httpUrlConn != null)
			httpUrlConn.disconnect();
	}
}
