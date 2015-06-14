package com.xxs.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

import com.xxs.sdk.myinterface.XListViewAcyionDownPosition;

/**
 * 自定义ListView 使用请注意与ListView的Item的布局配合，
 * 该效果的实现是基于在Item的布局中通过设置MarginLeft和MaiginRight来隐藏左右菜单的，
 * 所以使用此ListView时，请务必在布局Item时使用MarginLeft和MaiginRight；
 * 或者自己改写此ListView，已达到想要的实现方式
 * 
 * @author xiongxs
 * @date 2014-10-10
 * @introduce 实现ListView左右滑动删除的方法
 */
public class XListView2 extends ListView implements OnScrollListener {
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
	/** 上下文 */
	private Context context;
	/** 手指按下时的X坐标 */
	private float startX;
	/** 手指按下时的Y坐标 */
	private float startY;
	/** 手指按下时对应的Item想的序列号 */
	private int startPosition;
	/** ListView的item */
	private View itemView;
	/** 左侧菜单的长度 */
	private int leftLength = 0;
	/** 右侧菜单的长度 */
	private int rightLength = 0;
	/** 是否正在横向滑动 */
	private boolean ishscrolling = false;
	/** 是否完成了横向滑动 */
	private boolean ishscrolled = false;
	/** 滑动类 */
	private Scroller scroller;
	/** 用于屏蔽横向滑动式Item的点击事件 */
	private MotionEvent cancelEvent;
	/** 按下时回调接口 */
	private XListViewAcyionDownPosition actiondowncallback;

	public XListView2(Context context) {
		super(context);
		this.context = context;
		IntView();// 调用初始化视图的方法
	}

	public XListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		IntView();// 调用初始化视图的方法
	}

	public XListView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		IntView();// 调用初始化视图的方法
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

	/** 初始化视图的方法 */
	private void IntView() {
		scroller = new Scroller(context);
		miniMoveLength = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
		setOnScrollListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		float lastX = ev.getX();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/* 当前模式不允许滑动，则直接返回，交给ListView自身去处理 */
			if (hScrollMode == HSCROLL_NOT_ALLOW) {
				return super.onTouchEvent(ev);
			}
			if (ishscrolled) {
				scrollBack();
				return false;
			}
			startX = ev.getX();
			startY = ev.getY();
			startPosition = pointToPosition((int) startX, (int) startY);
			// 无效的position, 不做任何处理
			// if (startPosition == AdapterView.INVALID_POSITION) {
			// return super.onTouchEvent(ev);
			// }
			// 获取我们点击的item view
			if (actiondowncallback != null && startPosition >= 0) {
				actiondowncallback.onXListViewAcyionDownPosition(startPosition);
			}
			itemView = getChildAt(startPosition - getFirstVisiblePosition());
			/* 此处根据设置的滑动模式，自动获取左侧或右侧菜单的长度 */
			// if (hScrollMode == HSCROLL_BOTH_ALLOW) {
			// leftLength = 200;
			// rightLength = 200;
			// // leftLength = -itemView.getPaddingLeft();
			// // rightLength = -itemView.getPaddingRight();
			// } else if (hScrollMode == HSCROLL_LEFT_ALLOW) {
			// leftLength = 200;
			// leftLength = -itemView.getPaddingLeft();
			// } else if (hScrollMode == HSCROLL_RIGHT_ALLOW) {
			// rightLength = 200;
			// // rightLength = -itemView.getPaddingRight();
			// System.out.println("--距离：----：" + rightLength);
			// }
			break;
		case MotionEvent.ACTION_MOVE:
			if (!ishscrolling
					&& startPosition != AdapterView.INVALID_POSITION
					&& (Math.abs(ev.getX() - startX) > miniMoveLength && Math
							.abs(ev.getY() - startY) < miniMoveLength)) {
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
	public void computeScroll() {// 调用startScroll的时候scroller.computeScrollOffset()返回true，
		if (scroller.computeScrollOffset()) {
			// 让ListView item根据当前的滚动偏移量进行滚动
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	/** 设置从左滑出长度 */
	public void setLeftLength(int leftLength) {
		this.leftLength = leftLength;
	}

	/** 设置从右滑出长度 */
	public void setRightLength(int rightLength) {
		this.rightLength = rightLength;
	}

	/** 设置按下时回调接口 */
	public void setXListViewAcyionDownPosition(
			XListViewAcyionDownPosition actiondowncallback) {
		this.actiondowncallback = actiondowncallback;
	}
}
