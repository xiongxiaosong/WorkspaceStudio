package com.bestbrand.hh.http;

import java.util.HashMap;
import java.util.Map;

import com.bestbrand.hh.util.UtilServer;
import com.xxs.sdk.manage.ThreadManage;
import com.xxs.sdk.myinterface.XHttpCallBack;

/**
 * 网络请求类
 * 
 * @author xiongxs
 * @date 2015-04-16
 * @introduce 本项目中左右网络请求的方法都将在这里找到
 */
public class HttpClintClass {
	/** 单例对象 */
	public static HttpClintClass httpClintClass;

	/** 单例模式获取唯一对象 */
	public static HttpClintClass getMethod() {
		if (httpClintClass == null) {
			httpClintClass = new HttpClintClass();
		}
		return httpClintClass;
	}

	/**
	 * 获取公共请求参数的方法(例如:deviceId)
	 * 
	 * @return 得到的请求参数
	 * @introduce 公共请求参数统一放到这里,调用接口是先获取公共参数然后再后面追加剩余参数,下面公共参数根据项目自行添加
	 */
	private Map<String, String> getHttpBaseParamsMethod() {
		Map<String, String> parameters = new HashMap<String, String>();
		return parameters;
	}

	/**
	 * 获取注册请求参数的方法
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param nickname
	 *            昵称
	 * @return 得到的请求参数
	 */
	private Map<String, String> getRegisterParamsMethod(String username,
			String password, String nickname) {
		Map<String, String> params = getHttpBaseParamsMethod();
		params.put("username", username);// 用户名
		params.put("password", password);// 密码
		params.put("nickname", nickname);// 昵称
		return params;
	}

	/**
	 * 注册
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param nickname
	 *            昵称
	 * @param threadId
	 *            网络请求线程ID
	 * @param callBack
	 *            网络请求回调接口
	 * @param isget
	 *            是否是get请求
	 */
	public void RegisterMethod(String username, String password,
			String nickname, String threadId, XHttpCallBack callBack,
			boolean isget) {
		Map<String, String> params = getRegisterParamsMethod(username,
				password, nickname);
		ThreadManage.getMethod().doHttpRequest(threadId, callBack,
				UtilServer.REGISTERUSER, params, isget);
	}

	/**
	 * 获取登录请求参数的方法
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 得到的请求参数
	 */
	private Map<String, String> getLoginParamsMethod(String username,
			String password) {
		Map<String, String> params = getHttpBaseParamsMethod();
		params.put("username", username);// 用户名
		params.put("password", password);// 密码
		return params;
	}

	/**
	 * 登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param threadId
	 *            网络请求线程ID
	 * @param callBack
	 *            网络请求回调接口
	 * @param isget
	 *            是否是get请求
	 */
	public void LoginMethod(String username, String password, String threadId,
			XHttpCallBack callBack, boolean isget) {
		Map<String, String> params = getLoginParamsMethod(username, password);
		ThreadManage.getMethod().doHttpRequest(threadId, callBack,
				UtilServer.LOGINUSER, params, isget);
	}
}
