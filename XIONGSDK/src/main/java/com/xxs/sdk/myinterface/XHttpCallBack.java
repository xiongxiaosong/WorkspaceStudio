package com.xxs.sdk.myinterface;

/**
 * 网络请求回调接口
 * 
 * @author xiongxs
 * @date 2014-10-08
 * @method <ul>
 *         <li>preExecuteHttp 准备请求网络的方法</li>
 *         <li>succedExecuteHttp 网络请求成功的方法</li>
 *         <li>failExecuteHttp 网络请求失败方法</li>
 *         </ul>
 */
public interface XHttpCallBack {
	/**
	 * 数据请求前调用
	 * 
	 * @param threadId
	 *            线程ID <br/>
	 *            注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void preExecuteHttp(String threadId);

	/**
	 * 数据请求成功
	 * 
	 * @param threadId
	 *            线程ID
	 * @param result
	 *            得到的网络请求返回值
	 *            <p/>
	 *            注意1：调用此方法的时候对返回inputStream要添加非空判断，如果为空则提示数据请求失败 <br/>
	 *            注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void succedExecuteHttp(String threadId, String result);

	/**
	 * 数据请求错误信息
	 * 
	 * @param threadId
	 *            线程ID
	 * @param e
	 *            具体错误信息 <br/>
	 *            注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void failExecuteHttp(String threadId, Exception e);
	/**
	 * 网络请求取消
	 * @param threadId 线程ID
	 */
	public void cancleExecuteHttp(String threadId);
}
