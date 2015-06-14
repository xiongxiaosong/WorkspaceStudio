package com.xxs.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.xxs.sdk.util.TransformUtil;

/**
 * 自定义的MyGallery控件通过继承Gallery对Gallery重写实现扇形旋转的效果
 * 
 * @author 熊小松
 * 
 */
@SuppressWarnings("deprecation")
public class XGallery extends Gallery {
	private int mMaxRotationAngle = 60;// 定义最大转动角
	private int mMaxZoom = -120;// 定义最大缩放值
	private int mCoveflowCenter;// 定义中心点坐标
	private Context context;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 */
	public XGallery(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);// 设置支持对子元素的静态转换
		this.context = context;
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param attrs
	 *            表征组
	 */
	public XGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);// 设置支持对子元素的静态转换
		this.context = context;
	}

	public XGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);// 设置支持对子元素的静态转换
		this.context = context;
	}

	// 下面六个方法是一个标准的javaBean文件，通过get和set方法来实现对成员变量的赋值和取值
	public int getmMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	public void setmMaxRotationAngle(int mMaxRotationAngle) {
		this.mMaxRotationAngle = mMaxRotationAngle;
	}

	public int getmMaxZoom() {
		return mMaxZoom;
	}

	public void setmMaxZoom(int mMaxZoom) {
		this.mMaxZoom = mMaxZoom;
	}

	public int getmCoveflowCenter() {
		return mCoveflowCenter;
	}

	public void setmCoveflowCenter(int mCoveflowCenter) {
		this.mCoveflowCenter = mCoveflowCenter;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	/**
	 * 计算图片中心点的坐标
	 * 
	 * @param view
	 *            当前View
	 * @return 计算得到的坐标
	 */
	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	/**
	 * 控制gallery中每个图片的旋转（重写gallery中的方法）
	 * 
	 * @param child
	 *            子View
	 * @param t
	 *            转换类型
	 */
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		final int childCenter = getCenterOfView(child);// 获取当前子View的中心点的坐标
		View view = getSelectedView();
		float roate = 0.0f;
		if (childCenter < mCoveflowCenter) {
			roate = 30;
		} else {
			roate = -30;
		}
		transformImageBitmap(child,roate, view);// 执行图片旋转

		return true;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void transformImageBitmap(View child, float rotationAngle, View view) {
		if (child == view) {
			ViewHelper.setRotation(child, 0);
			ObjectAnimator obj = ObjectAnimator.ofFloat(child, "translationY",
					0);
			obj.setDuration(0);
			obj.start();
		} else {
			ViewHelper.setRotation(child, rotationAngle);
			ObjectAnimator obj = ObjectAnimator.ofFloat(child, "translationY",
					-TransformUtil.dip2px(80));
			obj.setDuration(0);
			obj.start();
		}
	}
}
