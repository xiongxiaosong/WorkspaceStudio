package com.bestbrand.hh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bestbrand.hh.R;

/**
 * 侧边菜单片段
 * 
 * @author xiongxs
 * @date 2015-03-31
 * @introduce 侧边菜单展示页，提供多个菜单选项
 */

public class FragmentLeftMenu extends FragmentBase {
	/** 个人信息 */
	private RelativeLayout ownmessage;
	/** 我的业务 */
	private RelativeLayout mybusiness;
	/** 套餐明细 */
	private RelativeLayout packagedetails;
	/** 流量提醒 */
	private RelativeLayout fluxremind;
	/** 消息中心 */
	private RelativeLayout messagecenter;
	/** 流量记录 */
	private RelativeLayout fluxrecord;
	/** 版本更新 */
	private RelativeLayout checklevel;
	/** 使用反馈 */
	private RelativeLayout feedback;
	/** 注销 */
	private TextView cancellation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_leftmenu, container, false);
		viewGroup.addView(view);
		return viewGroup;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		findViewMethod(view);// 调用实例化控件的方法
	}

	/** 实例化控件的方法 */
	private void findViewMethod(View view) {
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}
	}
}
