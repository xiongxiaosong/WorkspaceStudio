package com.xxs.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.xxs.sdk.R;
import com.xxs.sdk.util.ProveUtil;

/**
 * 自定义圆形View控件
 * 
 * @author xiongxs
 * @date 2014-10-27
 * @introduce 显现圆形View的颜色控制盒中心视图的添加
 */
public class XCircleView extends ViewGroup {
	private int layoutHeight, layoutWidth;
	private float centerX, centerY;
	private float circleR;
	private Paint paint;
	private Context mContext;
	private int viewvolor;

	public XCircleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
	}

	public XCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
	}

	public XCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
	}

	/** 初始化一些属性值的方法 */
	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.XCircleView);
		viewvolor = a.getColor(R.styleable.XCircleView_xcircleviewcolor, 0);
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeigth);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		layoutWidth = r - l;
		layoutHeight = b - t;
		centerX = layoutWidth / 2;
		centerY = layoutHeight / 2;
		circleR = layoutWidth > layoutHeight ? layoutHeight / 2 : layoutWidth / 2;
		float left = 0, top = 0;
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View mView = getChildAt(i);
			Object tag = mView.getTag();
			if (mView.getVisibility() == GONE || (tag == null || !ProveUtil.isNumeric(tag.toString()))) {
				continue;
			}
			int w = mView.getMeasuredWidth();// 获取View的宽度
			int h = mView.getMeasuredHeight();// 获取View的高度
			int index = Integer.valueOf(tag.toString());
			switch (index) {
			case 0:// 圆心
				left = centerX - w / 2;
				top = centerY - h / 2;
				break;
			default:
				break;
			}
			mView.layout((int) left, (int) top, (int) left + w, (int) top + h);
		}
		intMethod();
	}

	private void intMethod() {
		paint = new Paint();
		paint.setAntiAlias(true);// 设置抗锯齿
		if (viewvolor != 0)
			paint.setColor(viewvolor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawCircle(centerX, centerY, circleR, paint);

	}
}
