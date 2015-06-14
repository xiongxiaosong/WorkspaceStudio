package com.bestbrand.hh.activity;

import com.bestbrand.hh.R;
import com.xxs.sdk.util.ProveUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 用户登录
 * 
 * @author xiongxs
 * @date 2015-05-10
 */
public class ActivityLogin extends ActivityBase {
	private static final int QERUESTCODE_REGISTER = 0;
	private static final int QERUESTCODE_FORGEOTPASSWORD = 1;
	/** 用户名输入框 */
	private EditText editUsername;
	/** 密码输入框 */
	private EditText editPassword;
	/** 注册入口 */
	private TextView textRegist;
	/** 忘记密码入口 */
	private TextView textForgotpassword;
	/** 登录按钮 */
	private Button buttonLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewMethod();// 调用实例化控件的方法
	}

	/** 实例化控件的方法 */
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
			Intent intent01 = new Intent(this, ActivityMain.class);
			startActivity(intent01);
			finish();
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

	/** 判断用户名密码等信息输入格式是否正确的方法 */
	private boolean inputIsRight() {
		boolean isright = false;
		return isright;
	}
}
