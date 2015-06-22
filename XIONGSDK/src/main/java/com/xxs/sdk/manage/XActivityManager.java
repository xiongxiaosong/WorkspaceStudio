package com.xxs.sdk.manage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import com.xxs.sdk.util.LogUtil;

/**
 * Activity管理工具类
 *
 * @author xxs
 * @version 1.0
 * @dete 2014-09-30
 * @introduce 提供Activity队列添加，移除，以及全部结束等方法
 */
public class XActivityManager {
    private static String LOG_TAG = XActivityManager.class.getName();
    /**
     * Activity集合
     */
    private static List<Activity> mList = new LinkedList<Activity>();

    /**
     * 添加Activity
     *
     * @param mActivity 待添加的Activity
     */
    public static final void addActivity(Activity mActivity) {
        if (!mList.contains(mActivity))
            mList.add(mActivity);
    }

    /**
     * 移除Activity
     *
     * @param mActivity 待移除的Activity
     */
    public static final void removeOneActivity(Activity mActivity) {
        if (mList.contains(mActivity))
            mList.remove(mActivity);
    }

    /**
     * 结束一个队列中所有和指定类名相同的Activity的方法
     *
     * @param mclass 指定类
     */
    public static final void finishOneActivity(Class mclass) {
        ArrayList<Activity> array = new ArrayList<Activity>();
        for (Activity activity : mList) {
            if (activity != null
                    && (mclass.getName().equals(activity
                    .getClass().getName())))
                array.add(activity);
            activity.finish();
        }
        mList.removeAll(array);
    }

    /**
     * 移除指定Activity以外的其他Activity
     *
     * @param mActivity 指定的Activity
     */
    public static final void finishOtherActity(Activity mActivity) {
        for (Activity activity : mList) {
            if (activity != null
                    && !(mActivity.getClass().getName().equals(activity
                    .getClass().getName())))
                activity.finish();
        }
        Activity activity = getOneActivity(mActivity);
        mList.clear();
        if (null != activity)
            addActivity(activity);
    }

    /**
     * 得到队列与指定Activity类名相同的Activity
     *
     * @param mActivity
     * @return 队列中的Activity
     */
    public static final Activity getOneActivity(Activity mActivity) {
        for (Activity activity : mList) {
            if (activity != null
                    && (mActivity.getClass().getName().equals(activity
                    .getClass().getName())))
                return activity;
        }
        return null;
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

    /**
     * 判断队列中是否含有与指定类相同类型的Activity
     *
     * @param mclass 指定类
     * @return 包含 true 不包含 false
     */
    public static final boolean ifContainActivity(Class mclass) {
        for (Activity activity : mList) {
            if (activity != null
                    && (mclass.getName().equals(activity
                    .getClass().getName())))
                return true;
        }
        return false;
    }
}
