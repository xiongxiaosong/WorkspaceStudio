package com.bestbrand.hh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.bestbrand.hh.R;
import com.bestbrand.hh.adapter.AdapterGadePager;
import com.easemob.chat.core.p;

import java.util.ArrayList;

/**
 * 引导页
 *
 * @author xiongxs
 * @date 2015-06-17
 * @introduce 每次新版本安装都会进入的引导页面
 */
public class ActivityGuide extends ActivityBase implements OnPageChangeListener {
    /**
     * 引导页小圆点视图容器
     */
    private RadioGroup gadepoints;
    /**
     * 装在引导页视图的容器
     */
    private ViewPager viewPager;
    /**
     * 装载引导页面的视图队列
     */
    private ArrayList<View> arrayViews;
    /**
     * 填充引导页面内容的适配器
     */
    private AdapterGadePager adapterViewPager;
    /**
     * 注册按钮
     */
    private FrameLayout registerButton;
    /**
     * 登录按钮
     */
    private FrameLayout loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViewMethod();//调用实例化控件的方法
        initViewMethod();
    }

    /**
     * 实例化控件的方法
     */
    private void findViewMethod() {
        //实例化控件
        gadepoints = (RadioGroup) findViewById(R.id.activity_guide_points);
        viewPager = (ViewPager) findViewById(R.id.activity_guid_viewpager);
        registerButton = (FrameLayout) findViewById(R.id.activity_guide_register);
        loginButton = (FrameLayout) findViewById(R.id.activity_guide_login);
        //设置监听事件
        viewPager.setOnPageChangeListener(this);
        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    private void initViewMethod() {
        arrayViews = new ArrayList<View>();
        View gade01 = LayoutInflater.from(this).inflate(R.layout.view_gade01, null);
        View gade02 = LayoutInflater.from(this).inflate(R.layout.view_gade02, null);
        View gade03 = LayoutInflater.from(this).inflate(R.layout.view_gade03, null);
        View gade04 = LayoutInflater.from(this).inflate(R.layout.view_gade04, null);
        View gade05 = LayoutInflater.from(this).inflate(R.layout.view_gade05, null);
        View gade06 = LayoutInflater.from(this).inflate(R.layout.view_gade06, null);
        arrayViews.add(gade01);
        arrayViews.add(gade02);
        arrayViews.add(gade03);
        arrayViews.add(gade04);
        arrayViews.add(gade05);
        arrayViews.add(gade06);
        adapterViewPager = new AdapterGadePager(arrayViews);
        viewPager.setAdapter(adapterViewPager);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_guide_register://注册
                Intent intent01 = new Intent(this, ActivityRegist.class);
                this.startActivity(intent01);
                finish();
                break;
            case R.id.activity_guide_login://登录
                Intent intent02 = new Intent(this, ActivityLogin.class);
                this.startActivity(intent02);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        gadepoints.getChildAt(position).performClick();//设置指定小圆点选中
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
