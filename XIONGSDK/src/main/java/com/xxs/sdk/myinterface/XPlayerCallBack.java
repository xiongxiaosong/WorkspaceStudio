package com.xxs.sdk.myinterface;

/**
 * 自定义播放器回调接口
 * 
 * @author xiongxs
 * @date 2015-05-25
 */
public interface XPlayerCallBack {
	/**
	 * 加载失败回调函数
	 * 
	 * @param type
	 *            <ul>
	 *            <li>0 地址错误</li>
	 *            <li>1 加载失败，检查网络</li>
	 *            <li>2 连接超时，检查网络</li>
	 *            <li>3 视频格式不支持</li>
	 *            <li>4 未知错误</li>
	 *            <li>5 解码错误</li>
	 *            <ul>
	 */
	public void onLoadFalse(int type);

	/**
	 * 加载进度回调函数
	 * 
	 * @param percent
	 *            加载进度百分数(0-100)
	 */
	public void onBufferUpdatePercent(float percent);

	/**
	 * 播放进度回调函数
	 * 
	 * @param percent
	 *            播放进度百分数(0-100)
	 */
	public void onPlayPercent(float percent);

	/**
	 * 播放状态回调函数
	 * 
	 * @param type
	 *            <ul>
	 *            <li>0 开始播放</li>
	 *            <li>1 暂停</li>
	 *            <li>2 停止</li>
	 *            <li>3 播放完毕</li>
	 *            <li>4 加载中</li>
	 *            <ul>
	 */
	public void onPlayType(int type);
}
