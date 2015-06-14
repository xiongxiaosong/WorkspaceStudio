package com.xxs.sdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Scroller;

import com.xxs.sdk.myinterface.XExpandableHeaderInterface;
import com.xxs.sdk.myinterface.XExpandableViewAcyionView;

/**
 * 自定义二级展开列表控件
 * 
 * @author xiongxs
 * @date 2014-12-24
 * @introduce 使用此控件需要配合XexpandableViewAdapter使用
 */
public class XExpandableView extends ExpandableListView implements
		OnScrollListener, OnGroupClickListener {
	/** 禁止横滑模式 */
	public static final int HSCROLL_NOT_ALLOW = 0;
	/** 左右均可滑出 */
	public static final int HSCROLL_BOTH_ALLOW = 1;
	/** 只允许从右滑出 */
	public static final int HSCROLL_RIGHT_ALLOW = 2;
	/** 只允许从左滑出 */
	public static final int HSCROLL_LEFT_ALLOW = 3;
	/** 当前横滑模式 */
	private int hScrollMode = HSCROLL_NOT_ALLOW;
	/** 最小滑动触发距离 */
	private int miniMoveLength;
	/** 手指按下时对应的Item想的序列号 */
	private int startPosition;
	/** ListView的item */
	private View itemView;
	/** 左侧菜单的长度 */
	private int leftLength = 0;
	/** 右侧菜单的长度 */
	private int rightLength;
	/** 是否正在横向滑动 */
	private boolean ishscrolling = false;
	/** 是否完成了横向滑动 */
	private boolean ishscrolled = false;
	/** 滑动类 */
	private Scroller scroller;
	/** 用于屏蔽横向滑动式Item的点击事件 */
	private MotionEvent cancelEvent;
	/** 手指按下时的X坐标 */
	private float startX;
	/** 手指按下时的Y坐标 */
	private float startY;
	/** 最大透明值 */
	private static final int MAX_ALPHA = 255;
	/** 自定义的二级展开列表头部视图回调接口 */
	private XExpandableHeaderInterface mInterface;
	/** 用于在列表头显示的 View,mHeaderViewVisible 为 true 才可见 */
	private View mHeaderView;
	/** 列表头是否可见 */
	private boolean mHeaderViewVisible;
	/** 头部视图文件宽度 */
	private int mHeaderViewWidth;
	/** 头部视图文件高度 */
	private int mHeaderViewHeight;
	// /** 手指按下时的X坐标 */
	// private float mDownX;
	// /** 手指按下时的Y坐标 */
	// private float mDownY;
	/** 头文件离职状态 */
	private int mOldState = -1;
	private XExpandableViewAcyionView actiondowncallback;

	/** 设置监听事件的方法 */
	private void registerListener() {
		setOnScrollListener(this);
		setOnGroupClickListener(this);
	}

	public XExpandableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		miniMoveLength = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
		scroller = new Scroller(context);
		registerListener();// 调用设置监听事件的方法
	}

	public XExpandableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		miniMoveLength = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
		scroller = new Scroller(context);
		registerListener();// 调用设置监听事件的方法
	}

	public XExpandableView(Context context) {
		super(context);
		miniMoveLength = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
		scroller = new Scroller(context);
		registerListener();// 调用设置监听事件的方法
	}

	/**
	 * 设置头部视图展示的View
	 * 
	 * @param view
	 *            加载显示的头文件视图
	 */
	public void setHeaderView(View view) {
		mHeaderView = view;
		LayoutParams lp = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	/**
	 * 点击 HeaderView 触发的事件
	 */
	private void headerViewClick() {
		long packedPosition = getExpandableListPosition(this
				.getFirstVisiblePosition());
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(packedPosition);

		if (mInterface.getHeadViewClickStatus(groupPosition) == 1) {
			this.collapseGroup(groupPosition);
			mInterface.setHeadViewClickStatus(groupPosition, 0);
		} else {
			this.expandGroup(groupPosition);
			mInterface.setHeadViewClickStatus(groupPosition, 1);
		}
		this.setSelectedGroup(groupPosition);
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// mDownX = ev.getX();
	// mDownY = ev.getY();
	// if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
	// return true;
	// }
	// break;
	// case MotionEvent.ACTION_UP:
	// float x = ev.getX();
	// float y = ev.getY();
	// float offsetX = Math.abs(x - mDownX);
	// float offsetY = Math.abs(y - mDownY);
	// // 如果 HeaderView 是可见的 , 点击在 HeaderView 内 , 那么触发 headerClick()
	// if (x <= mHeaderViewWidth && y <= mHeaderViewHeight && offsetX <=
	// mHeaderViewWidth
	// && offsetY <= mHeaderViewHeight) {
	// if (mHeaderView != null) {
	// headerViewClick();
	// }
	// return true;
	// }
	// break;
	// default:
	// break;
	// }
	// return super.onInterceptTouchEvent(ev);
	// }
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 如果 HeaderView 是可见的 , 此函数用于判断是否点击了 HeaderView, 并对做相应的处理 , 因为 HeaderView
	 * 是画上去的 , 所以设置事件监听是无效的 , 只有自行控制 .
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mHeaderViewVisible) {
			float lastX = ev.getX();
			float lastY = ev.getY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// if (hScrollMode == HSCROLL_NOT_ALLOW ) {
				// return super.onTouchEvent(ev);
				// }
				if (ishscrolled) {
					scrollBack();
					return false;
				}
				startX = ev.getX();
				startY = ev.getY();
				// if (startX <= mHeaderViewWidth && startY <=
				// mHeaderViewHeight) {
				// return true;
				// }
				startPosition = pointToPosition((int) startX, (int) startY);
				if (startPosition != getFirstVisiblePosition()) {
					itemView = getChildAt(startPosition
							- getFirstVisiblePosition());
					setHscrollMode(HSCROLL_RIGHT_ALLOW);
				} else {
					itemView = mHeaderView;
					setHscrollMode(HSCROLL_NOT_ALLOW);
				}
				if (actiondowncallback != null && null != itemView
						&& null != itemView.getTag()) {
					actiondowncallback.onXExpandableViewAcyionView(itemView);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (!ishscrolling
						&& startPosition != AdapterView.INVALID_POSITION
						&& (Math.abs(ev.getX() - startX) > miniMoveLength)
						&& Math.abs(ev.getY() - startY) < miniMoveLength) {
					float offsetX = startX - lastX;
					if (offsetX > 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_RIGHT_ALLOW)) {
						/* 从右向左滑 */
						ishscrolling = true;
					} else if (offsetX < 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_LEFT_ALLOW)) {
						/* 从左向右滑 */
						ishscrolling = true;
					} else {
						ishscrolling = false;
					}
					/* 此段代码是为了避免我们在侧向滑动时同时出发ListView的OnItemClickListener时间 */
					cancelEvent = MotionEvent.obtain(ev);
					cancelEvent
							.setAction(MotionEvent.ACTION_CANCEL
									| (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
					onTouchEvent(cancelEvent);
				}
				if (ishscrolling) {
					/* 设置此属性，可以在侧向滑动时，保持ListView不会上下滚动 */
					requestDisallowInterceptTouchEvent(true);
					// 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
					int deltaX = (int) (startX - lastX);
					if (deltaX < 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_LEFT_ALLOW)) {
						/* 向右滑 */
						itemView.scrollTo(-deltaX > leftLength ? -leftLength
								: deltaX, 0);
					} else if (deltaX > 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_RIGHT_ALLOW)) {
						/* 向左滑 */
						itemView.scrollTo(deltaX < rightLength ? deltaX
								: rightLength, 0);
					} else {
						itemView.scrollTo(0, 0);
					}
					return true; // 拖动的时候ListView不滚动
				}
				break;
			case MotionEvent.ACTION_UP:
				if (cancelEvent != null) {
					cancelEvent.recycle();
					cancelEvent = null;
				}
				if (ishscrolling) {
					scrollByDistanceX();
					ishscrolling = false;
				}
				float x = ev.getX();
				float y = ev.getY();
				float offsetX = Math.abs(x - startX);
				float offsetY = Math.abs(y - startY);
				// 如果 HeaderView 是可见的 , 点击在 HeaderView 内 , 那么触发 headerClick()
				if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
						&& offsetX <= mHeaderViewWidth
						&& offsetY <= mHeaderViewHeight && !ishscrolling
						&& !ishscrolled) {
					if (mHeaderView != null) {
						headerViewClick();
					}
					return true;
				}
				break;
			default:
				break;
			}
		} else {
			// TODO Auto-generated method stub
			float lastX = ev.getX();
			float lastY = ev.getY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				/* 当前模式不允许滑动，则直接返回，交给ListView自身去处理 */
				// if (hScrollMode == HSCROLL_NOT_ALLOW ) {
				// return super.onTouchEvent(ev);
				// }
				if (ishscrolled) {
					scrollBack();
					return false;
				}
				startX = ev.getX();
				startY = ev.getY();
				startPosition = pointToPosition((int) startX, (int) startY);
				itemView = getChildAt(startPosition - getFirstVisiblePosition());
				if (actiondowncallback != null && null != itemView
						&& null != itemView.getTag()) {
					actiondowncallback.onXExpandableViewAcyionView(itemView);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (!ishscrolling
						&& startPosition != AdapterView.INVALID_POSITION
						&& (Math.abs(ev.getX() - startX) > miniMoveLength)
						&& Math.abs(ev.getY() - startY) < miniMoveLength) {
					float offsetX = startX - lastX;
					if (offsetX > 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_RIGHT_ALLOW)) {
						/* 从右向左滑 */
						ishscrolling = true;
					} else if (offsetX < 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_LEFT_ALLOW)) {
						/* 从左向右滑 */
						ishscrolling = true;
					} else {
						ishscrolling = false;
					}
					/* 此段代码是为了避免我们在侧向滑动时同时出发ListView的OnItemClickListener时间 */
					cancelEvent = MotionEvent.obtain(ev);
					cancelEvent
							.setAction(MotionEvent.ACTION_CANCEL
									| (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
					onTouchEvent(cancelEvent);
				}
				if (ishscrolling) {
					/* 设置此属性，可以在侧向滑动时，保持ListView不会上下滚动 */
					requestDisallowInterceptTouchEvent(true);
					// 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
					int deltaX = (int) (startX - lastX);
					if (deltaX < 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_LEFT_ALLOW)) {
						/* 向右滑 */
						itemView.scrollTo(-deltaX > leftLength ? -leftLength
								: deltaX, 0);
					} else if (deltaX > 0
							&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_RIGHT_ALLOW)) {
						/* 向左滑 */
						itemView.scrollTo(deltaX < rightLength ? deltaX
								: rightLength, 0);
					} else {
						itemView.scrollTo(0, 0);
					}
					return true; // 拖动的时候ListView不滚动
				}
				break;
			case MotionEvent.ACTION_UP:
				if (cancelEvent != null) {
					cancelEvent.recycle();
					cancelEvent = null;
				}
				if (ishscrolling) {
					scrollByDistanceX();
					ishscrolling = false;
				}
				break;

			default:
				break;
			}
			return super.onTouchEvent(ev);
		}
		return super.onTouchEvent(ev);

	}

	/**
	 * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
	 */
	private void scrollByDistanceX() {
		/* 当前模式不允许滑动，则直接返回 */
		if (this.hScrollMode == HSCROLL_NOT_ALLOW) {
			return;
		}
		if (itemView.getScrollX() > 0
				&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_RIGHT_ALLOW)) {
			/* 从右向左滑 */
			if (itemView.getScrollX() >= rightLength / 2) {
				scrollLeft();
			} else {
				// 滚回到原始位置
				scrollBack();
			}
		} else if (itemView.getScrollX() < 0
				&& (this.hScrollMode == HSCROLL_BOTH_ALLOW || this.hScrollMode == HSCROLL_LEFT_ALLOW)) {
			/* 从左向右滑 */
			if (itemView.getScrollX() <= -leftLength / 2) {
				scrollRight();
			} else {
				// 滚回到原始位置
				scrollBack();
			}
		} else {
			// 滚回到原始位置
			scrollBack();
		}

	}

	/**
	 * 往右滑动，getScrollX()返回的是左边缘的距离，就是以View左边缘为原点到开始滑动的距离，所以向右边滑动为负值
	 */
	private void scrollRight() {
		final int delta = (leftLength + itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate(); // 刷新itemView
		ishscrolled = true;
	}

	/**
	 * 向左滑动，根据上面我们知道向左滑动为正值
	 */
	private void scrollLeft() {
		final int delta = (rightLength - itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,
				Math.abs(delta));
		postInvalidate(); // 刷新itemView
		ishscrolled = true;
	}

	/** 回退横向滑动状态的方法 */
	public void scrollBack() {
		ishscrolled = false;
		if (itemView != null) {
			scroller.startScroll(itemView.getScrollX(), 0,
					-itemView.getScrollX(), 0, Math.abs(itemView.getScrollX()));
		}
		postInvalidate(); // 刷新itemView
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (scroller.computeScrollOffset()) {
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mInterface = (XExpandableHeaderInterface) adapter;
	}

	/**
	 * 
	 * 点击了 Group 触发的事件 , 要根据根据当前点击 Group 的状态来
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		if (mInterface.getHeadViewClickStatus(groupPosition) == 0) {
			mInterface.setHeadViewClickStatus(groupPosition, 1);
			parent.expandGroup(groupPosition);
			// parent.setSelectedGroup(groupPosition);

		} else if (mInterface.getHeadViewClickStatus(groupPosition) == 1) {
			mInterface.setHeadViewClickStatus(groupPosition, 0);
			parent.collapseGroup(groupPosition);
		}
		// 返回 true 才可以弹回第一行 , 不知道为什么
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
		final int groupPos = ExpandableListView
				.getPackedPositionGroup(flatPostion);
		final int childPos = ExpandableListView
				.getPackedPositionChild(flatPostion);
		int state = mInterface.getTreeHeaderState(groupPos, childPos);
		if (mHeaderView != null && mInterface != null && state != mOldState) {
			mOldState = state;
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
		}
		configureHeaderView(groupPos, childPos);
	}

	public void configureHeaderView(int groupPosition, int childPosition) {
		if (mHeaderView == null || mInterface == null
				|| ((ExpandableListAdapter) mInterface).getGroupCount() == 0) {
			return;
		}

		int state = mInterface.getTreeHeaderState(groupPosition, childPosition);

		switch (state) {
		case XExpandableHeaderInterface.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case XExpandableHeaderInterface.PINNED_HEADER_VISIBLE: {
			mInterface.setTreeHeaderState(mHeaderView, groupPosition,
					childPosition, MAX_ALPHA);

			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}

			mHeaderViewVisible = true;

			break;
		}

		case XExpandableHeaderInterface.PINNED_HEADER_PUSHED_UP: {
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
			mInterface.setTreeHeaderState(mHeaderView, groupPosition,
					childPosition, alpha);
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
						+ y);
			}
			mHeaderViewVisible = true;
			break;
		}
		}
	}

	/**
	 * 列表界面更新时调用该方法(如滚动时)
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			// 分组栏是直接绘制到界面中，而不是加入到ViewGroup中
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	/**
	 * 设置HeaderView是否可见
	 * 
	 * @param mHeaderViewVisible
	 */
	public void setmHeaderViewVisible(boolean mHeaderViewVisible) {
		this.mHeaderViewVisible = mHeaderViewVisible;
		postInvalidate();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		final long flatPos = getExpandableListPosition(firstVisibleItem);
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
		int childPosition = ExpandableListView.getPackedPositionChild(flatPos);
		configureHeaderView(groupPosition, childPosition);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	/** 设置从左滑出长度 */
	public void setLeftLength(int leftLength) {
		this.leftLength = leftLength;
	}

	/** 设置从右滑出长度 */
	public void setRightLength(int rightLength) {
		this.rightLength = rightLength;
	}

	/**
	 * 设置横滑模式
	 * 
	 * @param hScrollMode
	 *            模式编号通过XListView只掉调出选择
	 */
	public void setHscrollMode(int hScrollMode) {
		this.hScrollMode = hScrollMode;
	}

	/** 设置按下时回调监听 */
	public void setXExpandableViewAcyionView(
			XExpandableViewAcyionView actiondowncallback) {
		this.actiondowncallback = actiondowncallback;
	}
}
