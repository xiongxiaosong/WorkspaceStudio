package com.xxs.sdk.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class AppContext extends Application {
	/** 公用Handler */
	public static final Handler mMainHandler = new Handler(
			Looper.getMainLooper());

	/** 全局上下文 */
	public static Context mMainContext = null;
	/** 当前Activity的上下文 */
	public static Context mCurrentContext = null;// 当前Activity的上下文
	/** 网络时间 */
	public static long netTime;
	/** 本地时间 */
	public static long localTime;

	/**
	 * 初始化上下文的方法
	 * 
	 * @param MainContext
	 * @intrduce 
	 *           外部工程通过自定义Application在onCreate方法中调用AppContext.initContext(this)完成初始化
	 * @注意：自定义的Application需要到AndroidManifest.xml完成注册，否则无法实现上下文初始化功能
	 */
	public static void initContext(Context MainContext) {
		mMainContext = MainContext.getApplicationContext();
	}

	/**
	 * 初始化时间同步的方法
	 * 
	 * @param nTime
	 *            网络标准时间
	 * @param lTime SystemClock.elapsedRealtime()
	 *            本地标准时间
	 */
	public static void initTimeMethod(long nTime, long lTime) {
		netTime = nTime > 0 ? nTime : System.currentTimeMillis();
		localTime = lTime;
	}
	//
	// /**
	// * 清除所有通知栏信息
	// */
	// public static final void clearNotificaction() {
	// NotificationManager manager = (NotificationManager) mMainContext
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// manager.cancelAll();
	// }
	//
	// /**
	// * 清除通知栏指定信息
	// *
	// * @param id
	// * 指定通知信息的ID
	// */
	// public static final void clearNotificaction(int id) {
	// NotificationManager manager = (NotificationManager) mMainContext
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// manager.cancel(id);
	// }
}
