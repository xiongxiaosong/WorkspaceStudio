package com.xxs.sdk.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;

import com.xxs.sdk.R;
import com.xxs.sdk.helper.XSlidingActivityHelper;
import com.xxs.sdk.menu.XSlidingMenu;
import com.xxs.sdk.menu.XSlidingMenu.CanvasTransformer;
import com.xxs.sdk.myinterface.XSlidingCallBack;

/**
 * 自定义SlingActivity侧边滑动菜单类
 * 
 * @author xiongxs
 * @date 2014-10-12
 *       <ul>
 *       使用方法：通过外部继承本类实现，然后设置具体方法
 *       <li>setBehindContentView() 设置底部菜单布局文件</li>
 *       <li>setContentView() 设置顶部主视图布局文件</li>
 *       <li>XSlidingMenu sm = getSlidingMenu();得到菜单控件</li>
 *       <li>sm.setMode(XSlidingMenu.LEFT_RIGHT);
 *       设置滑动模式LEFT_RIGHT,RIGHT,LEFT三种可选</li>
 *       <li>sm.setSecondaryMenu();设置第二菜单布局,当滑动模式为LEFT_RIGHT时必须设置此项</li>
 *       <li>sm.setBehindScrollScale(0.0f);设置为0.0则菜单不会一起滑动,不设置则底部菜单和顶部视图会一起滑动</li>
 *       <li>sm.setShadowWidthRes(R.dimen.shadow_width);设置阴影边缘宽度</li>
 *       <li>sm.setShadowDrawable(R.drawable.shap_shadow);设置menu边缘阴影</li>
 *       <li>sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);设置打开后顶部布局留下的宽度</li>
 *       <li>sm.setFadeDegree(0.35f);设置menu（底部文件的褪色度数）,设置为零表示不褪色，其他则会呈现渐变的效果</li>
 *       <li>sm.setTouchModeAbove(XSlidingMenu.TOUCHMODE_FULLSCREEN);
 *       设置触发滑动模式TOUCHMODE_NONE
 *       (不可滑动),TOUCHMODE_MARGIN(边缘触发),TOUCHMODE_FULLSCREEN(全屏可触发)三种可选</li>
 *       <li>sm.setSecondaryShadowDrawable();设置第二菜单menu边缘阴影</li>
 *       <li>setQQanimation() 直接调用此方法，设置类似QQ侧滑动画效果</li>
 *       </ul>
 */
public class XSlidingFragmentActivity extends FragmentActivity implements
		XSlidingCallBack {
	private XSlidingActivityHelper mHelper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new XSlidingActivityHelper(this);
		mHelper.onCreate(savedInstanceState);
		getSlidingMenu().setSecondaryShadowDrawable(
				R.drawable.sliding_second_shadowright);
		getSlidingMenu().setShadowWidthRes(R.dimen.sliding_shadow_width);// 设置阴影边缘宽度
		getSlidingMenu().setShadowDrawable(R.drawable.sliding_shadow);// 设置menu边缘阴影
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_above_offset);// 设置打开后顶部布局留下的宽度
		getSlidingMenu().setFadeDegree(0.35f);// 设置menu（底部文件的褪色度数）,设置为零表示不褪色，其他则会呈现渐变的效果
	}

	/** 设置类似QQ侧边缩放动画的方法 */
	protected void setQQanimation() {
		getSlidingMenu().setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
				canvas.translate(
						-canvas.getWidth()
								* (1 - interp.getInterpolation(percentOpen)), 0);
			}
		});
		getSlidingMenu().setAboveCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 1);
				canvas.scale(2f - scale, 2f - scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
				canvas.translate(
						-getResources().getDimensionPixelOffset(R.dimen.slidingmenu_above_offset)
								* (interp.getInterpolation(percentOpen)), 0);
			}
		});
	}

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#findViewById(int)
	 */
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mHelper.findViewById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
	 * .Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mHelper.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	@Override
	public void setContentView(View v) {
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#setContentView(android.view.View,
	 * android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View v, LayoutParams params) {
		super.setContentView(v, params);
		mHelper.registerAboveContentView(v, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setBehindContentView(int)
	 */
	public void setBehindContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setBehindContentView(android.view.View)
	 */
	public void setBehindContentView(View v) {
		setBehindContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setBehindContentView(android.view.View,
	 * android.view.ViewGroup.LayoutParams)
	 */
	public void setBehindContentView(View v, LayoutParams params) {
		mHelper.setBehindContentView(v, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#getSlidingMenu
	 * ()
	 */
	public XSlidingMenu getSlidingMenu() {
		return mHelper.getSlidingMenu();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#toggle()
	 */
	public void toggle() {
		mHelper.toggle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showAbove()
	 */
	public void showContent() {
		mHelper.showContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showBehind()
	 */
	public void showMenu() {
		mHelper.showMenu();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showSecondaryMenu
	 * ()
	 */
	public void showSecondaryMenu() {
		mHelper.showSecondaryMenu();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setSlidingActionBarEnabled(boolean)
	 */
	@Override
	public void setSlidingActionBarEnabled(boolean b) {
		mHelper.setSlidingActionBarEnabled(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b)
			return b;
		return super.onKeyUp(keyCode, event);
	}

}
