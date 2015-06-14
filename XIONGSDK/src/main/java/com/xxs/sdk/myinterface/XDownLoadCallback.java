package com.xxs.sdk.myinterface;

/**
 * 自定义下载完成接口
 * 
 * @author xiongxs
 * 
 */
public interface XDownLoadCallback {
	/**
	 * 下载完成回调接口
	 * 
	 * @param downloadid
	 *            下载ID
	 */
	public void onXDownLoadFinish(String downloadid);

	/**
	 * 开始下载回调接口
	 * 
	 * @param downloadid
	 *            下载ID
	 */
	public void onXDownLoadStart(String downloadid);

	/**
	 * 下载中回调接口
	 * 
	 * @param downloadid
	 *            下载ID
	 * @param totlelengeth
	 *            总长度
	 * @param nowlength 已下载长度
	 */
	public void onXDownLoadLoad(String downloadid, long totlelengeth, long nowlength);

	/**
	 * 下载取消回调接口
	 * 
	 * @param downloadid
	 *            下载ID
	 */
	public void onXDownLoadCancle(String downloadid);
	/**
	 * 下载失败回调接口
	 * 
	 * @param downloadid
	 *            下载ID
	 * @param e
	 *            失败错误信息
	 */
	public void onXDownLoadFailed(String downloadid, Exception e);
}
