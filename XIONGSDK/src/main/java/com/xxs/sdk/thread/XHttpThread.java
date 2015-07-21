package com.xxs.sdk.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.xxs.sdk.R;
import com.xxs.sdk.app.AppContext;
import com.xxs.sdk.http.HttpHelper;
import com.xxs.sdk.manage.ThreadManage;
import com.xxs.sdk.myinterface.XHttpCallBack;
import com.xxs.sdk.util.LogUtil;
import com.xxs.sdk.util.ProveUtil;
import com.xxs.sdk.util.TransformUtil;

/**
 * 网络请求线程
 * 
 * @author xiongxs
 * @date 2014-10-08
 * @introduce 实现网络求数据请求的线程，得到请求结果并且发送回调消息
 */
public class XHttpThread extends Thread {
	private static String LOG_TAG = XHttpThread.class.getName();
	private XHttpCallBack httpCallBack;
	private String path;
	private Map<String, String> params;
	private boolean isget;
	private String threadId;
	private HttpHelper helper;
	/** 是否取消网络请求 */
	private boolean iscancled;

	/** 构造函数 */
	public XHttpThread() {

	}

	/**
	 * 构造函数
	 * 
	 * @param threadId
	 *            线程ID,通过程序控制生成唯一的ID序列
	 * @param httpCallBack
	 *            网络请求回调接口
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param isget
	 *            是否是get请求
	 */
	public XHttpThread(String threadId, XHttpCallBack httpCallBack,
			String path, Map<String, String> params, boolean isget) {
		this.httpCallBack = httpCallBack;
		this.path = path;
		this.params = params;
		this.isget = isget;
		this.threadId = threadId;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
			handler.sendEmptyMessage(5);
			Message msg = new Message();
			try {
				if (ProveUtil.IfHasNet()) {
					helper = new HttpHelper();
					InputStream inputStream = helper.getHttpRequest(path,
							params, isget);
					if (inputStream != null) {
						String result = TransformUtil
								.inputstream2String(inputStream);
						LogUtil.e(LOG_TAG + "Http请求返回数据", "" + result);
						msg.what = 4;
						msg.obj = result;
						handler.sendMessage(msg);
					} else {
						msg.what = 7;
						handler.sendMessage(msg);
					}
				} else {
					msg.what = 0;
					handler.sendMessage(msg);
				}
			} catch (UnsupportedEncodingException e) {
				msg.what = 1;
				handler.sendMessage(msg);
				LogUtil.e(LOG_TAG, e);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				msg.what = 2;
				handler.sendMessage(msg);
				LogUtil.e(LOG_TAG, e);
			} catch (SocketTimeoutException e) {
				msg.what = 6;
				handler.sendMessage(msg);
				LogUtil.e(LOG_TAG, e);
			} catch (SocketException e) {
				msg.what = 8;
				handler.sendMessage(msg);
				LogUtil.e(LOG_TAG, e);
			} catch (IOException e) {
				msg.what = 3;
				handler.sendMessage(msg);
				LogUtil.e(LOG_TAG, e);
			}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (!iscancled) {
				if (msg.what != 5) {
					ThreadManage.getMethod().removeHttpThread(threadId);
				}
				switch (msg.what) {
				case 0:// 无网络
					httpCallBack
							.failExecuteHttp(
									threadId,0,
									new Exception(
											AppContext.mMainContext
													.getResources()
													.getString(
															R.string.nonetwork_please_checknet)));
					break;
				case 1:// 不支持编码
					httpCallBack
							.failExecuteHttp(
									threadId,1,
									new Exception(
											AppContext.mMainContext
													.getResources()
													.getString(
															R.string.notsurport_encodtransform)));
					break;
				case 2:// 请求地址错误
					httpCallBack.failExecuteHttp(
							threadId,2,
							new Exception(AppContext.mMainContext
									.getResources().getString(
											R.string.net_adress_error)));
					break;
				case 3:// 数据请求失败
					httpCallBack
							.failExecuteHttp(
									threadId,3,
									new Exception(
											AppContext.mMainContext
													.getResources()
													.getString(
															R.string.falsegetdata_please_checknet)));
					break;
				case 4:// 请求成功
					String result = (String) msg.obj;
					LogUtil.e(LOG_TAG + "成功:", result);
					httpCallBack.succedExecuteHttp(threadId, result);
					break;
				case 5:// 开始请求网络
					httpCallBack.preExecuteHttp(threadId);
					break;
				case 6:// 请求超时
					httpCallBack.failExecuteHttp(
							threadId,6,
							new Exception(AppContext.mMainContext
									.getResources().getString(
											R.string.netsocket_timeout)));
					break;
				case 7:// 服务器异常
					httpCallBack.failExecuteHttp(
							threadId,7,
							new Exception(AppContext.mMainContext
									.getResources().getString(
											R.string.service_error)));
					break;
				case 8:// 数据连接中断
					if (httpCallBack != null)
						httpCallBack.failExecuteHttp(threadId,8, new Exception(
								AppContext.mMainContext.getResources()
										.getString(R.string.data_way_cutdown)));
					break;
				default:
					break;
				}
			}
		}
	};

	/** 断开Http连接的方法 */
	public void disConnectHttpMethod() throws Exception {
		if (helper != null)
            iscancled = true;
			helper.disConnectHttp();
	}

	/** 取消Http连接的方法 */
	public void cancleHttpMethod(){
		iscancled = true;
		httpCallBack.cancleExecuteHttp(threadId);
	}
}
