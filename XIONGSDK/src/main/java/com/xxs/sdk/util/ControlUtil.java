package com.xxs.sdk.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.view.inputmethod.InputMethodManager;

import com.xxs.sdk.app.AppContext;

/**
 * 控制工具类
 * 
 * @author xiongxs
 * @date 2014-10-08
 * @introduce 包含一些状态控制的方法例如wifi开关
 */
public class ControlUtil {
	private static String LOG_TAG = ControlUtil.class.getName();
	/**
	 * GPS开关
	 */
	public static final void openGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent
					.getBroadcast(AppContext.mMainContext, 0, gpsIntent, 0)
					.send();
		} catch (CanceledException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
	}

	/**
	 * WIFI网络开关
	 * 
	 * @param enabled
	 *            true 打开wifi false 关闭wifi
	 */
	public static final void toggleWiFi(boolean enabled) {
		WifiManager wm = (WifiManager) AppContext.mMainContext
				.getSystemService(Context.WIFI_SERVICE);
		wm.setWifiEnabled(enabled);
	}

	/**
	 * 移动网络开关
	 * 
	 * @param enabled
	 *            true 打开移动网络 false 关闭移动网络
	 */
	public static final void toggleMobileData(boolean enabled) {
		ConnectivityManager conMgr = (ConnectivityManager) AppContext.mMainContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Class<?> conMgrClass = null; // ConnectivityManager类
		Field iConMgrField = null; // ConnectivityManager类中的字段
		Object iConMgr = null; // IConnectivityManager类的引用
		Class<?> iConMgrClass = null; // IConnectivityManager类
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法

		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		} catch (SecurityException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param mContext
	 *            上下文
	 */
	public static final void hideSoftInput(Context mContext) {
		InputMethodManager imm = (InputMethodManager) AppContext.mMainContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mContext != null && mContext instanceof Activity
				&& ((Activity) mContext).getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	/**
	 * 打开软键盘
	 * 
	 * @param mContext
	 *            上下文
	 */
	public static final void showSoftInput(Context mContext) {
		InputMethodManager imm = (InputMethodManager) AppContext.mMainContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mContext != null && mContext instanceof Activity
				&& ((Activity) mContext).getCurrentFocus() != null) {
			imm.showSoftInput(((Activity) mContext).getCurrentFocus(),
					InputMethodManager.SHOW_FORCED);
		}
	}
}
