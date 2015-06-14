package com.xxs.sdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xxs.sdk.myinterface.PandHeadeListrInterface;

/***
 * 自定义表头固定悬浮列表控件
 * 
 * @author xiongxs
 * @date 2014-12-26
 * @introduce 使用此控件需要配合{@link PandHeaderListAdapter}适配器使用
 * @注意 childItem必须包含有头文件相同布局
 */
public class PanddHeaderListView extends ListView {
	/** 最大透明值 */
	private static final int MAX_ALPHA = 255;
	/** 自定义的表头视图状态回调接口 */
	private PandHeadeListrInterface mInterface;
	/** 表头文件视图对象 mHeaderViewVisible为true是可见 */
	private View mHeaderView;
	/** 表头文件视图是否可见 */
	private boolean mHeaderViewVisible;
	/** 表头文件视图宽度 */
	private int mHeaderViewWidth;
	/** 表头文件视图高度 */
	private int mHeaderViewHeight;

	public PanddHeaderListView(Context context) {
		super(context);
	}

	public PanddHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PanddHeaderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(getFirstVisiblePosition());
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	/**
	 * 设置表头文件视图对象的方法
	 * 
	 * @param view
	 *            待设置的表头文件视图对象
	 */
	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mInterface = (PandHeadeListrInterface) adapter;
	}

	public void configureHeaderView(int position) {
		if (mHeaderView == null) {
			return;
		}
		int state = mInterface.getPinnedHeaderState(position);
		switch (state) {
		case PandHeadeListrInterface.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case PandHeadeListrInterface.PINNED_HEADER_VISIBLE: {
			mInterface.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
			mHeaderViewVisible = true;
			break;
		}

		case PandHeadeListrInterface.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			int y;
			int alpha;
			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}
			mInterface.configurePinnedHeader(mHeaderView, position, alpha);
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
						+ y);
			}
			mHeaderViewVisible = true;
			break;
		}
		}
	}

	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}
}
