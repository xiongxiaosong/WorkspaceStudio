package com.bestbrand.hh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bestbrand.hh.R;
import com.bestbrand.hh.http.HttpClintClass;
import com.bestbrand.hh.view.TitleBar;
import com.xxs.sdk.manage.XActivityManager;
import com.xxs.sdk.myinterface.MyTimerInterCallback;
import com.xxs.sdk.util.MyTimerUtil;
import com.xxs.sdk.util.ProveUtil;
import com.xxs.sdk.util.UiUtil;

/**
 * 用户注册
 *
 * @author xiongxs
 * @date 2015-05-10
 */
public class ActivityRegist extends ActivityBase implements MyTimerInterCallback {
    private static final String LOG_TAG = ActivityRegist.class.getName();
    private static final String ID_REGISTER = LOG_TAG + "register";
    /**
     * 用户名/手机号输入框
     */
    private EditText editTelephone;
    /**
     * 密码输入框
     */
    private EditText editPassword;
    /**
     * 密码确认输入框
     */
    private EditText editPasswordEnsure;
    /**
     * 验证码输入框
     */
    private EditText editProvenumber;
    /**
     * 登录按钮
     */
    private Button buttonRegister;
    /**
     * 标题栏控件
     */
    private TitleBar mTitleBar;
    /**
     * 获取验证码的按钮
     */
    private FrameLayout getprovenum;
    /**
     * 倒计时进度条
     */
    private ProgressBar progressBar;
    /**
     * 获取验证码时的提示文字
     */
    private TextView progressmessage;
    /**
     * 计时器
     */
    private MyTimerUtil myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewMethod();// 调用实例化控件的方法
        initMethod();//调用初始化的方法
    }

    /**
     * 实例化控件的方法
     */
    private void findViewMethod() {
        // 实例化控件
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        editTelephone = (EditText) findViewById(R.id.activity_register_username);
        editPassword = (EditText) findViewById(R.id.activity_register_password);
        editPasswordEnsure = (EditText) findViewById(R.id.activity_register_password_ensure);
        editProvenumber = (EditText) findViewById(R.id.activity_register_provenumber);
        buttonRegister = (Button) findViewById(R.id.activity_register_getnow);
        getprovenum = (FrameLayout) findViewById(R.id.activity_register_getprove);
        progressBar = (ProgressBar) findViewById(R.id.activity_register_progress);
        progressmessage = (TextView) findViewById(R.id.activity_register_progressmessage);
        // 绑定监听事件
        buttonRegister.setOnClickListener(this);
        mTitleBar.setleftClickListener(this);
        getprovenum.setOnClickListener(this);
    }

    /**
     * 进行一些初始化的方法
     */
    private void initMethod() {
        myTimer = new MyTimerUtil(60 * 1000, 1000, this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:// 返回
                turnToLogin();//调用跳转到登录页面的方法
                break;
            case R.id.activity_register_getnow:// 注册
                if (proveInputTypeMethod()) {
                    registerMethod();// 调用注册方法
                }
                break;
            case R.id.activity_register_getprove://获取验证码
                if (!myTimer.isstarted()) {
                    myTimer.onStart();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证输入信息格式是否正确的方法
     */
    private boolean proveInputTypeMethod() {
        if (TextUtils.isEmpty(editTelephone.getText().toString().trim())) {
            UiUtil.setErrorMethod(editTelephone, getString(R.string.please_input_telephone));
            return false;
        }
        if (TextUtils.isEmpty(editPassword.getText().toString().trim())) {
            UiUtil.setErrorMethod(editPassword, getString(R.string.please_input_password));
            return false;
        }
        if (TextUtils.isEmpty(editPasswordEnsure.getText().toString().trim())) {
            UiUtil.setErrorMethod(editPasswordEnsure, getString(R.string.please_input_password_ensure));
            return false;
        }
        if (TextUtils.isEmpty(editProvenumber.getText().toString().trim())) {
            UiUtil.setErrorMethod(editProvenumber, getString(R.string.please_input_provenumber));
            return false;
        }
        if (!ProveUtil.isMobile(editTelephone.getText().toString().trim())) {
            UiUtil.setErrorMethod(editProvenumber, getString(R.string.error_telephone_type));
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                turnToLogin();//调用跳转到登录页面的方法
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 跳转到登录界面的方法
     */
    private void turnToLogin() {
        if (XActivityManager.ifContainActivity(ActivityLogin.class)) {
            finish();
        } else {
            Intent intent = new Intent(this, ActivityLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            finish();
        }
    }

    /**
     * 用户注册的方法
     */
    private void registerMethod() {
        HttpClintClass.getMethod().RegisterMethod(
                editTelephone.getText().toString().trim(),
                editPassword.getText().toString().trim(),
                editTelephone.getText().toString().trim(), ID_REGISTER, this,
                false);
    }

    @Override
    public void preExecuteHttp(String threadId) {
        // TODO Auto-generated method stub
        super.preExecuteHttp(threadId);
    }

    @Override
    public void succedExecuteHttp(String threadId, String result) {
        // TODO Auto-generated method stub
        super.succedExecuteHttp(threadId, result);
    }

    @Override
    public void cancleExecuteHttp(String threadId) {
        // TODO Auto-generated method stub
        super.cancleExecuteHttp(threadId);
    }

    @Override
    public void failExecuteHttp(String threadId, Exception e) {
        // TODO Auto-generated method stub
        super.failExecuteHttp(threadId, e);
    }

    @Override
    public void timerLeft(long lefttime) {
        progressmessage.setText(lefttime + "s");
        progressBar.setProgress((int) (lefttime*100/60));
    }

    @Override
    public void timerFinish() {
        myTimer.onCancle();
        progressmessage.setText(getString(R.string.string_register_provenumber_getagain));
        progressBar.setProgress(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTimer.onCancle();
    }
}
