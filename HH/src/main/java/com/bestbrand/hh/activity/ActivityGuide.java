package com.bestbrand.hh.activity;

import android.os.Bundle;

import com.bestbrand.hh.R;

/**
 * 引导页
 *
 * @author xiongxs
 * @date 2015-06-17
 * @introduce 每次新版本安装都会进入的引导页面
 */
public class ActivityGuide extends ActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }
}
