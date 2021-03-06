package com.bestbrand.hh.util;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

/**
 * 网络请求错误提示管理
 * 
 * @author xiongxs
 * @date 2015-05-10
 */
public class UtilHttpToast {
	/** 单例对象 */
	public static UtilHttpToast utilHttpToast;

	/** 单例模式获取唯一对象 */
	public static UtilHttpToast getMethod() {
		if (null == utilHttpToast) {
			utilHttpToast = new UtilHttpToast();
		}
		return utilHttpToast;
	}

	/**
	 * 根据返回Json数据进行对应提示的方法
	 * 
	 * @param context
	 *            上下文
	 * @param code
	 *            错误代码编号
	 */
	public void showToast(Context context, int code) {
		switch (code) {
		case 400:
			
			break;

		default:
			break;
		}
	}
}
