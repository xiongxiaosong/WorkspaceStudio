package com.xxs.sdk.util;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.Html;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xxs.sdk.app.AppContext;

/**
 * Created by xiongxs on 2015/7/15.
 *
 * @introduce 提供一些对UI操作的常用方法
 */
public class UiUtil {
    public static UiUtil uiUtil;
    /*statusbar view*/
    private ViewGroup view;
    /*沉浸颜色*/
    private TextView textView;
    /**
     * 设置错误提示的方法
     */
    public static void setErrorMethod(TextView view, String error) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            view.setError(Html.fromHtml("<font color=#FFFFFF>" + error
                    + "</font>"));
        else
            view.setError(Html.fromHtml("<font color=#000000>" + error
                    + "</font>"));
    }
    /**
     * 沉浸模式状态栏初始化
     * @param activity 上下文
     * @param colorResource 沉浸颜色资源ID
     * @return
     */
    @SuppressLint("NewApi")
    public void initStatusbar(Activity activity, int colorResource) {
        //4.4版本及以上可用
        if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 状态栏沉浸效果
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //decorview实际上就是activity的外层容器，是一层framlayout
            view = (ViewGroup) activity.getWindow().getDecorView();
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, getStatusBarHeight());
            //textview是实际添加的状态栏view，颜色可以设置成纯色，也可以加上shape，添加gradient属性设置渐变色
            if(textView != null){
                removeStatus();
            }
            textView = new TextView(activity);
            textView.setBackgroundResource(colorResource);
            textView.setLayoutParams(lParams);
            view.addView(textView);
        }
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = AppContext.mMainContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 如果项目中用到了slidingmenu,根据slidingmenu滑动百分比设置statusbar颜色：渐变色效果
     * @param alpha
     */
    public void changeStatusBarColor(float alpha) {
        //textview是slidingmenu关闭时显示的颜色
        //textview2是slidingmenu打开时显示的颜色
        if(textView!= null){
            textView.setAlpha(1 - alpha);
        }
    }
    private void removeStatus(){
        view.removeView(textView);
    }
}
