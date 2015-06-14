package com.xxs.sdk.myinterface;

import android.graphics.Bitmap;

/**
 * 自定义图片加载接口
 * 
 * @author xiongxs
 * @date 2014-10-21
 */
public interface XImageLoderCallback {
	/**
	 * 准备加载图片的回调方法
	 * 
	 * @param threadId
	 *            线程ID <br/>
	 *            注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void preLoadImage(String threadId);

	/**
	 * 图片加载成功的回调方法
	 * 
	 * @param threadId
	 *            线程ID
	 * @param bitmap
	 *            得到的Bitmap对象 得到的网络请求输入流
	 *            <p/>
	 *            注意1：调用此方法的时候对返回Bitmap要添加非空判断，如果为空则提示数据请求失败 <br/>
	 *            注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void succedLoadImage(String threadId, Bitmap bitmap);

	/**
	 * 图片加载错误的回调方法
	 * 
	 * @param threadId
	 *            线程ID
	 * @param e
	 *            具体错误信息 <br/>
	 *            注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void failLoadImage(String threadId, Exception e);
}
