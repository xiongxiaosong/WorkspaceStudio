package com.xxs.sdk.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * 片段页面适配器基类
 * 
 * @author xiongxs
 * @date 2015-05-01
 * 
 */
public class AdapterFragmentPagerBase extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragmentsList;

	public AdapterFragmentPagerBase(FragmentManager fm) {
		this(fm, null);
	}

	/**
	 * 构造函数
	 * 
	 * @param fm
	 *            FragmentManager
	 * @param fragments
	 *            Fragment片段队列
	 */
	public AdapterFragmentPagerBase(FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fragmentsList = fragments;
	}

	@Override
	public int getCount() {
		return fragmentsList == null ? 0 : fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentsList.get(arg0);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}
}
