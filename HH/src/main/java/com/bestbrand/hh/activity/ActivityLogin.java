package com.bestbrand.hh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bestbrand.hh.R;
import com.bestbrand.hh.entity.EntityLoginResult;
import com.bestbrand.hh.http.HttpClintClass;
import com.bestbrand.hh.util.UtilHttpToast;
import com.xxs.sdk.util.UiUtil;

/**
 * 用户登录
 *
 * @author xiongxs
 * @date 2015-05-10
 */
public class ActivityLogin extends ActivityBase {
    private static String LOG_TAG = ActivityLogin.class.getName();
    /**
     * 从注册页面返回序号
     */
    private static final int QERUESTCODE_FORGEOTPASSWORD = 1;
    /**
     * 从忘记密码页面返回序号
     */
    private static final int QERUESTCODE_REGISTER = 0;
    /**
     * 用户名输入框
     */
    private EditText editUsername;
    /**
     * 密码输入框
     */
    private EditText editPassword;
    /**
     * 注册入口
     */
    private TextView textRegist;
    /**
     * 忘记密码入口
     */
    private TextView textForgotpassword;
    /**
     * 登录按钮
     */
    private Button buttonLogin;
    /**
     * 登录线程ID
     */
    private String LOGINID = LOG_TAG + "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewMethod();// 调用实例化控件的方法
    }

    /**
     * 实例化控件的方法
     */
    private void findViewMethod() {
        // 实例化控件
        editUsername = (EditText) findViewById(R.id.activity_login_username);
        editPassword = (EditText) findViewById(R.id.activity_login_password);
        buttonLogin = (Button) findViewById(R.id.activity_login_login);
        textRegist = (TextView) findViewById(R.id.activity_login_regist);
        textForgotpassword = (TextView) findViewById(R.id.activity_login_frogotpassword);
        // 绑定监听事件
        buttonLogin.setOnClickListener(this);
        textRegist.setOnClickListener(this);
        textForgotpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_login_login:// 登录
                if (inputIsRight()) {//调用判断输入信息是否正确的方法
                    HttpClintClass.getMethod().LoginMethod(editUsername.getText().toString().trim(), editPassword.getText().toString().trim(), LOGINID, this, false);
                }
                break;
            case R.id.activity_login_regist:// 注册
                Intent intent02 = new Intent(this, ActivityRegist.class);
                startActivityForResult(intent02, QERUESTCODE_REGISTER);
                break;
            case R.id.activity_login_frogotpassword:
                Intent intent03 = new Intent(this, ActivityForgotPassword.class);
                startActivityForResult(intent03, QERUESTCODE_FORGEOTPASSWORD);
                break;

            default:
                break;
        }
    }

    /**
     * 判断用户名密码等信息输入格式是否正确的方法
     */
    private boolean inputIsRight() {
        if (TextUtils.isEmpty(editUsername.getText().toString().trim())) {
            UiUtil.setErrorMethod(editUsername, getString(R.string.error_username));
            return false;
        }
        if (TextUtils.isEmpty(editUsername.getText().toString().trim())) {
            UiUtil.setErrorMethod(editUsername, getString(R.string.error_password));
            return false;
        }
        return true;
    }

    @Override
    public void preExecuteHttp(String threadId) {
        super.preExecuteHttp(threadId);
        netdialog.showDialog(threadId);
    }

    @Override
    public void succedExecuteHttp(String threadId, String result) {
        super.succedExecuteHttp(threadId, result);
        EntityLoginResult eResult = EntityLoginResult.paramsJson(result);
        netdialog.hidDialog();
        if (eResult.getCode() == 200) {
            Intent intent01 = new Intent(this, ActivityMain.class);
            startActivity(intent01);
            finish();
        } else {
            UtilHttpToast.getMethod().showToast(this, eResult.getCode());
        }
    }

    @Override
    public void failExecuteHttp(String threadId,int code, Exception e) {
        super.failExecuteHttp(threadId,code, e);
        netdialog.hidDialog();
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
