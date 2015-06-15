package com.bestbrand.hh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bestbrand.hh.R;

/**
 * 程序启动之前的欢迎页
 *
 * @author xiongxs
 * @date 2014-05-09
 */
public class ActivityWelecom extends ActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welecom);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ActivityWelecom.this,
                        ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

}
