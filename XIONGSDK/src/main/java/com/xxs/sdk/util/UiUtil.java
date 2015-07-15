package com.xxs.sdk.util;

import android.os.Build;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by xiongxs on 2015/7/15.
 *
 * @introduce 提供一些对UI操作的常用方法
 */
public class UiUtil {
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
}
