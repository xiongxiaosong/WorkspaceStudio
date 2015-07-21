package com.xxs.sdk.manage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.xxs.sdk.R;
import com.xxs.sdk.app.AppContext;
import com.xxs.sdk.myinterface.XDownLoadCallback;
import com.xxs.sdk.myinterface.XHttpCallBack;
import com.xxs.sdk.thread.XDownLoadThread;
import com.xxs.sdk.thread.XHttpThread;
import com.xxs.sdk.util.LogUtil;

/**
 * 管理线程的类
 * 
 * @author xiongxs
 * @date 2014-10-08
 * @introduce 管理线程的启动，取消，停止，堆栈，出栈等操作
 */
public class ThreadManage {
	private static String LOG_TAG = ThreadManage.class.getName();
	public static ThreadManage threadManage;
	/**
	 * 网络请求队列
	 * <ul>
	 * <li>key:线程ID,通过程序控制生成唯一的ID序列</li>
	 * <li>value:具体的线程对象</li>
	 * </ul>
	 * */
	private final Map<String, Thread> httpThreadQueens;
	private final Map<String, Thread> downloadQueens;

	/** 单利模式获取唯一实体对象 */
	public static ThreadManage getMethod() {
		if (threadManage == null) {
			threadManage = new ThreadManage();
		}
		return threadManage;
	}

	/** 构造函数,可以进行一些初始化的操作 */
	public ThreadManage() {
		httpThreadQueens = Collections
				.synchronizedMap(new HashMap<String, Thread>());
		downloadQueens = Collections
				.synchronizedMap(new HashMap<String, Thread>());
	}

	/**
	 * 执行网络请求的方法
	 * 
	 * @param threadId
	 *            线程ID,通过程序控制生成唯一的ID序列
	 * @param httpCallBack
	 *            网络请求回调函数
	 * @param path
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param isget
	 *            是否是get方法请求
	 */
	public void doHttpRequest(String threadId, XHttpCallBack httpCallBack,
			String path, Map<String, String> params, boolean isget) {
		if (httpCallBack == null) {
			LogUtil.d(LOG_TAG, httpCallBack + "is null");
		}
		if (!httpThreadQueens.containsKey(threadId)) {
			Thread thread = new XHttpThread(threadId, httpCallBack, path,
					params, isget);
			thread.start();
			httpThreadQueens.put(threadId, thread);
		} else {
			if (httpCallBack != null)
				httpCallBack
						.failExecuteHttp(
								threadId,9,
								new Exception(
										AppContext.mMainContext
												.getResources()
												.getString(
														R.string.the_currentjob_isexecution)));
		}
	}

	/**
	 * 移除网络请求队列中指定的线程
	 * 
	 * @param threadId
	 *            线程ID
	 */
	public void removeHttpThread(String threadId) {
		if (httpThreadQueens.containsKey(threadId)) {
			try {
				((XHttpThread) httpThreadQueens.get(threadId))
						.disConnectHttpMethod();
			} catch (Exception e) {
				LogUtil.e(LOG_TAG, e);
			}
			httpThreadQueens.get(threadId).interrupt();
			httpThreadQueens.remove(threadId);
		}
	}

	/**
	 * 取消网络请求的方法
	 * 
	 * @param threadId
	 *            线程ID
	 */
	public void cancleHttpThread(String threadId) {

		if (httpThreadQueens.containsKey(threadId)) {
			try {
				((XHttpThread) httpThreadQueens.get(threadId))
						.cancleHttpMethod();
			} catch (Exception e) {
				LogUtil.e(LOG_TAG, e);
			}
			httpThreadQueens.get(threadId).interrupt();
			httpThreadQueens.remove(threadId);
		}
	}

	/** 移除所有线程队列的所有网络请求 */
	public void removeAllHttpThread() {
		for (String key : httpThreadQueens.keySet()) {
			try {
				((XHttpThread) httpThreadQueens.get(key))
						.disConnectHttpMethod();
			} catch (Exception e) {
				LogUtil.e(LOG_TAG, e);
			}
			httpThreadQueens.get(key).interrupt();
		}
		httpThreadQueens.clear();
	}

	/**
	 * 执行下载请求的方法
	 * 
	 * @param threadId
	 *            线程ID,通过程序控制生成唯一的ID序列
	 * @param downloadcallback
	 *            下载请求回调函数
	 * @param path
	 *            请求地址
	 * @param filename
	 *            保存文件名字
	 */
	public void doDownloadRequest(String threadId,
			XDownLoadCallback downloadcallback, String path, String filename) {

		if (downloadcallback == null) {
			LogUtil.d(LOG_TAG, downloadcallback + "is null");
		}
		if (!downloadQueens.containsKey(threadId)) {
			Thread thread = new XDownLoadThread(threadId, downloadcallback,
					path, filename);
			thread.start();
			downloadQueens.put(threadId, thread);
		} else {
			if (downloadcallback != null)
				downloadcallback
						.onXDownLoadFailed(
								threadId,
								new Exception(
										AppContext.mMainContext
												.getResources()
												.getString(
														R.string.the_currentjob_isexecution)));
		}

	}

	/**
	 * 移除下载请求队列中指定的线程
	 * 
	 * @param threadId
	 *            线程ID
	 */
	public void removeDownloadThread(String threadId) {
		if (downloadQueens.containsKey(threadId)) {
			try {
				((XDownLoadThread) downloadQueens.get(threadId))
						.disConnectDownloadMethod();
			} catch (Exception e) {
				LogUtil.e(LOG_TAG, e);
			}
			downloadQueens.get(threadId).interrupt();
			downloadQueens.remove(threadId);
		}
	}

	/**
	 * 取消下载请求的方法
	 * 
	 * @param threadId
	 *            线程ID
	 */
	public void cancleDownloadThread(String threadId) {

		if (downloadQueens.containsKey(threadId)) {
			try {
				((XDownLoadThread) downloadQueens.get(threadId))
						.cancleDownloadMethod();
			} catch (Exception e) {
				LogUtil.e(LOG_TAG, e);
			}
			downloadQueens.get(threadId).interrupt();
			downloadQueens.remove(threadId);
		}
	}

	/** 移除所有线程队列的所有网络请求 */
	public void removeAllDownloadThread() {
		for (String key : downloadQueens.keySet()) {
			try {
				((XDownLoadThread) downloadQueens.get(key))
						.disConnectDownloadMethod();
			} catch (Exception e) {
				LogUtil.e(LOG_TAG, e);
			}
			downloadQueens.get(key).interrupt();
		}
		downloadQueens.clear();
	}

	/** 移除所有线程队列的所有请求 */
	public void removeAllThread() {
		removeAllHttpThread();
		removeAllDownloadThread();
	}
}
