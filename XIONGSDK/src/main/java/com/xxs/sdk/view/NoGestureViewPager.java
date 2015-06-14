package com.xxs.sdk.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义不可滑动的ViewPager
 * 
 * @author xiongxs
 * @date 2015-05-13
 */
public class NoGestureViewPager extends ViewPager {
	/** 是否允许滑动 */
	private boolean allowScroll;

	public NoGestureViewPager(Context context) {
		super(context);
	}

	public NoGestureViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (allowScroll)
			return super.onTouchEvent(arg0);
		else
			return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (allowScroll)
			return super.onInterceptTouchEvent(arg0);
		else
			return false;
	}

	/** 获取是否可以滑动 */
	public boolean isAllowScroll() {
		return allowScroll;
	}

	/** 设置是否可以滑动 */
	public void setAllowScroll(boolean allowScroll) {
		this.allowScroll = allowScroll;
	}

}
