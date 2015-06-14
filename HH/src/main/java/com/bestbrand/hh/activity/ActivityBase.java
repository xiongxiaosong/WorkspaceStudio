package com.bestbrand.hh.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.bestbrand.hh.dialog.NetworkCongestionDialog;
import com.xxs.sdk.manage.XActivityManager;
import com.xxs.sdk.myinterface.XHttpCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity基类
 *
 * @author xiongxs
 * @date 2015-03-31
 * @introduce 本工程中所有Activity的公共父类(除侧边菜单以外的所有, 都将集成此类)
 * @interface <ul>
 * <li>OnClickListener view点击事件监听</li>
 * <li>OnItemClickListener adapter item点击事件监听</li>
 * <li>XHttpCallBack 自定义网络请求回调接口 {@link com.xxs.sdk.myinterface.XHttpCallBack}</li>
 * <li>OnCheckedChangeListener 复选框选中状态改变监听事件 {@link com.xxs.sdk.myinterface.XHttpCallBack}</li>
 * </ul>
 */
public class ActivityBase extends FragmentActivity implements OnClickListener,
        OnItemClickListener, XHttpCallBack, OnCheckedChangeListener {
    /**
     * 网络阻塞弹框
     */
    protected NetworkCongestionDialog netdialog;
    /**
     * 上下文
     */
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        mContext = this;
        XActivityManager.addActivity(this);
        netdialog = new NetworkCongestionDialog(this);
    }

    /**
     * 获取公共请求参数的方法(例如:deviceId)
     *
     * @return 得到的请求参数
     * @introduce 公共请求参数统一放到这里, 调用接口是先获取公共参数然后再后面追加剩余参数, 下面公共参数根据项目自行添加
     */
    protected Map<String, String> getHttpBaseParamsMethod() {
        Map<String, String> parameters = new HashMap<String, String>();
        // JSONObject obj = new JSONObject();
        // try {
        // obj.put("deviceId", InfoUtil.getDeviceId());
        // obj.put("token", SharedUtil.readSharedMethod(
        // SharedStatic.UserInfos.SHARED_NAME,
        // SharedStatic.UserInfos.USER_TOKEN, ""));
        // parameters.put("result", obj.toString());
        // } catch (JSONException e) {
        // e.printStackTrace();
        // LogUtil.e(LOG_TAG, e);
        // }
        return parameters;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        XActivityManager.removeOneActivity(this);
    }

    /**
     * 设置错误提示的方法
     */
    protected void setErrorMethod(TextView view, String error) {
        view.setError(Html.fromHtml("<font color=#FFFFFF>" + error + "</font>"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void preExecuteHttp(String threadId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void succedExecuteHttp(String threadId, String result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void failExecuteHttp(String threadId, Exception e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancleExecuteHttp(String threadId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
