package com.xxs.sdk.manage;

import java.util.LinkedList;
import java.util.List;

import android.support.v4.app.Fragment;

import com.xxs.sdk.util.LogUtil;

/**
 * Fragment管理工具类
 * 
 * @version 1.0
 * @author xxs
 * @dete 2014-09-30
 * @introduce 提供Fragment队列添加，移除，以及全部结束等方法
 */
public class XFragmentManager {
	private static String LOG_TAG = XFragmentManager.class.getName();
	/** Fragment集合 */
	private static List<Fragment> mList = new LinkedList<Fragment>();

	/**
	 * 添加Fragment
	 * 
	 * @param mFragment
	 *            待添加的Fragment
	 */
	public static final void addFragment(Fragment mFragment) {
		if (!mList.contains(mFragment))
			mList.add(mFragment);
	}

	/**
	 * 移除Fragment
	 * 
	 * @param mFragment
	 *            待移除的Fragment
	 */
	public static final void removeOneFragment(Fragment mFragment) {
		if (mList.contains(mFragment))
			mList.remove(mFragment);
	}

	/**
	 * 移除指定Fragment以外的其他Fragment
	 * 
	 * @param mFragment
	 *            指定的Fragment
	 */
	public static final void finishOtherActity(Fragment mFragment) {
		List<Fragment> list = new LinkedList<Fragment>();
		for (Fragment fragment : mList) {
			if (fragment != null
					&& !(mFragment.getClass().getName().equals(fragment
							.getClass().getName())))
				list.add(fragment);
		}
		mList.removeAll(list);
	}

	/**
	 * 移除Fragment队列中所有Fragment
	 */
	public static final void removeAllFragment() {
		LogUtil.d(LOG_TAG, "结束了堆栈中所有的Fragment");
		mList.clear();
	}

	/**
	 * 获取队列中指定的Fragment
	 * 
	 * @param mFragment
	 *            指定的Fragment
	 * @return 队列中的Fragment
	 */
	public static final Fragment getFragment(Fragment mFragment) {
		Fragment gFragment = null;
		for (int i = 0; i < mList.size(); i++) {
			Fragment fragment = mList.get(i);
			if (fragment != null
					&& (mFragment.getClass().getName().equals(fragment
							.getClass().getName()))) {
				gFragment = fragment;
				break;
			}
		}
		return gFragment;
	}
}
