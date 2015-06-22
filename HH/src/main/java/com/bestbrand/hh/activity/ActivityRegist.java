package com.bestbrand.hh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bestbrand.hh.R;
import com.bestbrand.hh.http.HttpClintClass;
import com.bestbrand.hh.view.TitleBar;
import com.xxs.sdk.manage.XActivityManager;

/**
 * 用户注册
 *
 * @author xiongxs
 * @date 2015-05-10
 */
public class ActivityRegist extends ActivityBase {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewMethod();// 调用实例化控件的方法
    }

    /**
     * 实例化控件的方法
     */
    private void findViewMethod() {
        // 实例化控件
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        editTelephone = (EditText) findViewById(R.id.activity_register_telephone);
        editPassword = (EditText) findViewById(R.id.activity_register_password);
        editProvenumber = (EditText) findViewById(R.id.activity_register_provenumber);
        buttonRegister = (Button) findViewById(R.id.activity_register_register);
        // 绑定监听事件
        buttonRegister.setOnClickListener(this);
        mTitleBar.setleftClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:// 返回
                turnToLogin();//调用跳转到登录页面的方法
                break;
            case R.id.activity_register_register:// 注册
                registerMethod();// 调用注册方法
                break;

            default:
                break;
        }
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
}
