package com.xxs.sdk.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.xxs.sdk.myinterface.XViewPagerClickListener;

/**
 * Pager切换适配器基类
 * 
 * @author xiongxs
 * @date 2014-11-18
 */
public class AdapterPagerBase extends PagerAdapter {
	private ArrayList<View> arrayview;
	/** 自定义ViewPager页面点击回调接口 */
	private XViewPagerClickListener onXViewPagerClickListener;

	/**
	 * 构造函数
	 * 
	 * @param arrayview
	 *            view识图队列
	 */
	public AdapterPagerBase(ArrayList<View> arrayview) {
		// TODO Auto-generated constructor stub
		this.arrayview = arrayview;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayview == null ? 0 : arrayview.size() == 1 ? 1
				: Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		position = arrayview.size()>0?position % arrayview.size():0;
		View view = arrayview.get(position);
		container.addView(view);
		view.setOnClickListener(new myOnclickListener(position));
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);
		position = arrayview.size()>0?position % arrayview.size():0;
		container.removeView(arrayview.get(position));
	}
	/**
	 * 自定义响应点击事件的类
	 * 
	 * @author xiongxs
	 * 
	 */
	private class myOnclickListener implements OnClickListener {
		private int position;

		/**
		 * 构造函数
		 * 
		 * @param position
		 *            点击的Item序号
		 */
		public myOnclickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if(null !=onXViewPagerClickListener)
				onXViewPagerClickListener.onViewpagerItemClick(position);
		}

	}
	/** 获取自定义ViewPage页面点击回调接口 */
	public XViewPagerClickListener getOnXViewPagerClickListener() {
		return onXViewPagerClickListener;
	}

	/** 设置自定义ViewPage页面点击回调接口 */
	public void setOnXViewPagerClickListener(
			XViewPagerClickListener onXViewPagerClickListener) {
		this.onXViewPagerClickListener = onXViewPagerClickListener;
	}

}
