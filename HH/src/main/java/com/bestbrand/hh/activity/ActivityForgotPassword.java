package com.bestbrand.hh.activity;

import android.os.Bundle;
import android.view.View;

import com.bestbrand.hh.R;
import com.bestbrand.hh.view.TitleBar;

/**
 * 忘记密码
 *
 * @author xiongxs
 * @date 2015-05-10
 */
public class ActivityForgotPassword extends ActivityBase {
    /**
     * 自定义标题栏控件
     */
    private TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        findViewMethod();//调用实例化控件的方法
    }

    /**
     * 实例化控件的方法
     */
    private void findViewMethod() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setleftClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:// 返回
                finish();
                break;
            default:
                break;
        }
    }
}
