package com.xxs.sdk.manage;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import com.xxs.sdk.util.LogUtil;

/**
 * Activity管理工具类
 * 
 * @version 1.0
 * @author xxs
 * @dete 2014-09-30
 * @introduce 提供Activity队列添加，移除，以及全部结束等方法
 */
public class XActivityManager {
	private static String LOG_TAG = XActivityManager.class.getName();
	/** Activity集合 */
	private static List<Activity> mList = new LinkedList<Activity>();

	/**
	 * 添加Activity
	 * 
	 * @param mActivity
	 *            待添加的Activity
	 */
	public static final void addActivity(Activity mActivity) {
		if (!mList.contains(mActivity))
			mList.add(mActivity);
	}

	/**
	 * 移除Activity
	 * 
	 * @param mActivity
	 *            待移除的Activity
	 */
	public static final void removeOneActivity(Activity mActivity) {
		if (mList.contains(mActivity))
		mList.remove(mActivity);
	}

	/**
	 * 结束一个Activity的方法
	 * 
	 * @param mActivity
	 *            待结束的Activity
	 */
	public static final void finishOneActivity(Activity mActivity) {
		mActivity.finish();
		removeOneActivity(mActivity);
	}

	/**
	 * 移除指定Activity以外的其他Activity
	 * 
	 * @param mActivity
	 *            指定的Activity
	 */
	public static final void finishOtherActity(Activity mActivity) {
		for (Activity activity : mList) {
			if (activity != null
					&& !(mActivity.getClass().getName().equals(activity
							.getClass().getName())))
				activity.finish();
		}
		mList.clear();
		addActivity(mActivity);
	}

	/**
	 * 结束Activity队列中所有Activity
	 * 
	 * @introduce 一键退出多个Activity或者类似返回主界面功能是可使用
	 */
	public static final void finishAllActity() {
		for (Activity activity : mList) {
			if (activity != null) {
				activity.finish();
			}
		}
		LogUtil.d(LOG_TAG, "结束了堆栈中所有的Activity");
		mList.clear();
	}
}
