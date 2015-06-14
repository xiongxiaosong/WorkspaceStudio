package com.xxs.sdk.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager
 * 
 * @author xiongxs
 * @date 2014-09-10
 * @introduce 控制ViewPager滑动模式
 */
public class XViewPager extends ViewPager {
	private boolean enabled;

	public XViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public XViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	// 触摸没有反应就可以了
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 设置是否可滑动
	 * 
	 * @param enabled
	 *            true 可滑动 false 不可滑动
	 */
	public void setEnable(boolean enabled) {
		this.enabled = enabled;
	}
}
