package com.xxs.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.xxs.sdk.R;
import com.xxs.sdk.myinterface.XListViewAcyionDownPosition;
import com.xxs.sdk.myinterface.XListViewPullCallBack;
import com.xxs.sdk.util.DateUtil;
import com.xxs.sdk.util.SharedUtil;

/**
 * 自定义ListView 使用请注意与ListView的Item的布局配合，
 * 该效果的实现是基于在Item的布局中通过设置MarginLeft和MaiginRight来隐藏左右菜单的，
 * 所以使用此ListView时，请务必在布局Item时使用MarginLeft和MaiginRight；
 * 或者自己改写此ListView，已达到想要的实现方式
 * 
 * 如果要是用下拉刷新或者上拉更多请配合XListViewPullCallBack来使用
 * 
 * @author xiongxs
 * @date 2014-10-10
 * @introduce 实现上下拉拽功能以及Item横滑功能
 */
public class XListView extends ListView implements OnScrollListener {
	/** 禁止横滑模式 */
	public static final int HSCROLL_NOT_ALLOW = 0;
	/** 左右均可滑出 */
	public static final int HSCROLL_BOTH_ALLOW = 1;
	/** 只允许从右滑出 */
	public static final int HSCROLL_RIGHT_ALLOW = 2;
	/** 只允许从左滑出 */
	public static final int HSCROLL_LEFT_ALLOW = 3;
	/** 禁用下拉刷新和上拉更多 */
	public static final int PULL_NOT_ALLOW = 0;
	/** 同时允许下拉刷新和上拉更多 */
	public static final int PULL_BOTH_ALLOW = 1;
	/** 只允许下拉刷新 */
	public static final int PULL_TOP_ALLOW = 2;
	/** 只允许上拉更多 */
	public static final int PULL_BUTTOM_ALLOW = 3;
	/** 当前横滑模式 */
	private int hScrollMode = HSCROLL_NOT_ALLOW;
	/** 当前上下拉拽模式 */
	private int pullMode = PULL_NOT_ALLOW;
	/** 最小滑动触发距离 */
	private int miniMoveLength;
	/** 上下文 */
	private Context context;
	/** ListView头文件 */
	private LinearLayout headview;
	/** ListView脚文件 */
	private LinearLayout footview;
	/** 头文件高度 */
	private int headviewheight;
	/** 脚文件高度 */
	private int footviewheight;
	/** 头文件下拉刷新箭头图片 */
	private ImageView head_image_arrow;
	/** 头文件刷新时等待提示转圈 */
	private ProgressBar head_progress;
	/** 头文件刷新文字提示 */
	private TextView head_textview_tishi;
	/** 头文件最近一次刷新时间 */
	private TextView head_textview_leasttime;
	/** 脚文件刷新时等待提示转圈 */
	private ProgressBar foot_progress;
	/** 脚文件刷新文字提示 */
	private TextView foot_textview_tishi;
	/** 向上翻转动画 */
	private Animation uproate;
	/** 回退动画 */
	private Animation downroate;
	/** 当前视图能看见的第一项索引 */
	private int mfirstVisibleItem = -1;
	/** 当前可见的Item项目数 */
	private int mvisibleItemCount = -1;
	/** 当前列表的总Item项目数 */
	private int mtotalItemCount = -1;
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
	/** 是否正在拉拽 */
	private boolean ispulling = false;
	/** 是否正在横向滑动 */
	private boolean ishscrolling = false;
	/** 是否完成了横向滑动 */
	private boolean ishscrolled = false;
	/** 滑动类 */
	private Scroller scroller;
	/** 用于保存动画是否已经执行的状态机 */
	private boolean head_image_arrow_isuped;
	/** 纵向滑动偏移量 */
	private float deltaY;
	/** 自定义ListView回调接口 */
	private XListViewPullCallBack xListViewPullCallBack;
	/** 是否正在下拉刷新 */
	private boolean ispulldown = false;
	/** 是否正在上拉更多 */
	private boolean ispullup = false;
	/** 用于屏蔽横向滑动式Item的点击事件 */
	private MotionEvent cancelEvent;
	/** 存储下拉刷新时间的文件名 */
	private String sharedname = "xlistview";
	/** 按下时回调接口 */
	private XListViewAcyionDownPosition actiondowncallback;

	public XListView(Context context) {
		super(context);
		this.context = context;
		IntView();// 调用初始化视图的方法
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		IntView();// 调用初始化视图的方法
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		IntView();// 调用初始化视图的方法
	}

	/**
	 * 设置回调接口监听
	 * 
	 * @param xListViewPullCallBack
	 */
	public void setXlistViewCallBack(XListViewPullCallBack xListViewPullCallBack) {
		this.xListViewPullCallBack = xListViewPullCallBack;
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

	/**
	 * 设置上下拉拽模式
	 * 
	 * @param pullMode
	 *            模式编号通过XListView只掉调出选择
	 */
	public void setPullMode(int pullMode) {
		this.pullMode = pullMode;
	}

	/** 初始化视图的方法 */
	private void IntView() {
		scroller = new Scroller(context);
		miniMoveLength = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
		headview = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_top_layout, null);
		footview = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_buttom_layout, null);
		headview.measure(0, 0);// 调用一次measure之后才能获取到高度
		footview.measure(0, 0);
		headviewheight = headview.getMeasuredHeight();// 得到视图的高度
		footviewheight = footview.getMeasuredHeight();
		setOnScrollListener(this);
		addHeaderView(headview, null, false);
		addFooterView(footview, null, false);
		head_image_arrow = (ImageView) headview
				.findViewById(R.id.pull_top_image);
		head_progress = (ProgressBar) headview
				.findViewById(R.id.pull_top_progress);
		head_textview_tishi = (TextView) headview
				.findViewById(R.id.pull_top_tishi);
		head_textview_leasttime = (TextView) headview
				.findViewById(R.id.pull_top_least_time);
		foot_progress = (ProgressBar) footview
				.findViewById(R.id.pull_buttom_progress);
		foot_textview_tishi = (TextView) footview
				.findViewById(R.id.pull_buttom_tishi);
		resetFoot();
		resetHead();
		// 初始化加载动画
		uproate = AnimationUtils.loadAnimation(context, R.anim.roate_up);
		downroate = AnimationUtils.loadAnimation(context, R.anim.roate_down);
		// 设置动画停止在结束时候的位置
		uproate.setFillAfter(true);
		downroate.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		float lastX = ev.getX();
		float lastY = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/* 当前模式不允许滑动，则直接返回，交给ListView自身去处理 */
			if (hScrollMode == HSCROLL_NOT_ALLOW && pullMode == PULL_NOT_ALLOW) {
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
					&& !ispulldown
					&& !ispullup
					&& !ispulling
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
			if (ishscrolling && !ispulling && !ispulldown && !ispullup) {
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
			if (!ishscrolling
					&& !ispulling
					&& (Math.abs(ev.getX() - startX) < miniMoveLength && Math
							.abs(ev.getY() - startY) > miniMoveLength)) {
				float offsetY = startY - lastY;
				if (offsetY > 0
						&& startPosition != AdapterView.INVALID_POSITION
						&& (this.pullMode == PULL_BOTH_ALLOW || this.pullMode == PULL_BUTTOM_ALLOW)
						&& mfirstVisibleItem + mvisibleItemCount == mtotalItemCount) {
					/* 向上滑动 */
					ispulling = true;
				} else if (offsetY < 0
						&& (this.pullMode == HSCROLL_BOTH_ALLOW || this.pullMode == PULL_TOP_ALLOW)
						&& mfirstVisibleItem == 0) {
					/* 向下滑动 */
					ispulling = true;
				} else {
					ispulling = false;
				}
			}
			if (!ishscrolling && ispulling && !ispulldown && !ispullup) {
				deltaY = (startY - lastY) / 2;// 纵向偏移量等于实际滑动量的一半，增强用户体验
				if (deltaY > 0) {// 向上滑动
					if (mfirstVisibleItem + mvisibleItemCount == mtotalItemCount) {
						foot_progress.setVisibility(View.GONE);
						footview.setPadding(0, (int) (deltaY - footviewheight),
								0, 0);
						if (Math.abs(deltaY) >= footviewheight) {
							foot_textview_tishi.setText("松开立即加载");
						} else {
							foot_textview_tishi.setText("上拉加载更多");
						}
					}
				} else if (deltaY < 0) {// 向下滑动
					if (mfirstVisibleItem == 0) {
						head_image_arrow.setVisibility(View.VISIBLE);
						head_progress.setVisibility(View.GONE);
						headview.setPadding(0,
								(int) (-deltaY - headviewheight), 0, 0);
						head_textview_leasttime.setText("上次刷新:"
								+ SharedUtil.readSharedMethod(sharedname,
										"leasttime", ""));
						if (Math.abs(deltaY) >= headviewheight
								&& !head_image_arrow_isuped) {
							head_image_arrow.startAnimation(uproate);
							head_textview_tishi.setText("松开刷新");
							head_image_arrow_isuped = true;
						} else if (Math.abs(deltaY) < headviewheight) {
							head_image_arrow.clearAnimation();
							head_textview_tishi.setText("下拉刷新");
							head_image_arrow_isuped = false;
						}
					}
				} else {

				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (cancelEvent != null) {
				cancelEvent.recycle();
				cancelEvent = null;
			}
			if (ispulling) {
				setPullState();
				ispulling = false;
			} else if (ishscrolling) {
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

	/** 设置正在刷新或加载的状态显示 */
	private void setPullState() {
		if (deltaY > 0) {// 向上滑
			if (deltaY >= footviewheight && !ispullup) {
				footview.setPadding(0, 0, 0, 0);
				foot_textview_tishi.setText("正在加载...");
				foot_progress.setVisibility(View.VISIBLE);
				xListViewPullCallBack.onStratPullUpBack();
				ispullup = true;
			} else {
				footview.setPadding(0, -1 * footviewheight, 0, 0);
			}
		} else if (deltaY < 0) {// 向下滑
			if (Math.abs(deltaY) >= headviewheight && !ispulldown) {
				head_image_arrow.clearAnimation();
				head_image_arrow.setVisibility(View.GONE);
				head_progress.setVisibility(View.VISIBLE);
				head_textview_tishi.setText("正在刷新...");
				headview.setPadding(0, 0, 0, 0);
				xListViewPullCallBack.onStartPullDownBack();
				ispulldown = true;
			} else {
				headview.setPadding(0, -1 * headviewheight, 0, 0);
			}
		}
	}

	/**
	 * 重置头文件的方法
	 * 
	 */
	public void resetHead() {
		SharedUtil.writeSharedMethod(sharedname, "leasttime",
				DateUtil.getDateAndTime());
		headview.setPadding(0, -1 * headviewheight, 0, 0);
		headview.invalidate();
		ispulldown = false;
	}

	/**
	 * 重置脚文件的方法
	 * 
	 */
	public void resetFoot() {
		footview.setPadding(0, -1 * footviewheight, 0, 0);
		footview.invalidate();
		ispullup = false;
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
		mfirstVisibleItem = firstVisibleItem;
		mvisibleItemCount = visibleItemCount;
		mtotalItemCount = totalItemCount;
	}

	/** 设置从左滑出长度 */
	public void setLeftLength(int leftLength) {
		this.leftLength = leftLength;
	}

	/** 设置从右滑出长度 */
	public void setRightLength(int rightLength) {
		this.rightLength = rightLength;
	}

	/** 设置存储下拉刷新时间文件名的方法 */
	public void setSharedname(String sharedname) {
		this.sharedname = sharedname;
	}

	/** 设置按下时回调接口 */
	public void setXListViewAcyionDownPosition(
			XListViewAcyionDownPosition actiondowncallback) {
		this.actiondowncallback = actiondowncallback;
	}
}
