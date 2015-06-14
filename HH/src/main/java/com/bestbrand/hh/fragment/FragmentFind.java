package com.bestbrand.hh.fragment;

import com.bestbrand.hh.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 发现片段
 * 
 * @author xiongxs
 * @date 2015-04-11
 * @introduce 实现发起活动、项目讨论等功能
 */
public class FragmentFind extends FragmentBase {
	/** 单利对象 */
	public static FragmentFind mfragment;

	/** 单利模式获取唯一对象 */
	public static FragmentFind getInstense() {
		if (mfragment == null) {
			mfragment = new FragmentFind();
		}
		return mfragment;
	}

	@Override
	public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup viewGroup = super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		viewGroup.addView(view);
		return viewGroup;
	}

}
