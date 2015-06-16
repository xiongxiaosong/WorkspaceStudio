package com.xxs.sdk.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.xxs.sdk.R;
import com.xxs.sdk.myinterface.XGuestureLockCallback;
import com.xxs.sdk.entity.XGuestLockInfo;

/**
 * 自定义手势密码锁控件
 * 
 * @author xiongxs
 * @date 2015-06-11
 */
public class XGuestureLock extends ViewGroup {
	/** 上下文 */
	private Context mContext;
	/** 正常图片 */
	private Drawable drawableNormal;
	/** 选中图片 */
	private Drawable drawableSelected;
	/** 错误图片 */
	private Drawable drawableError;
	/** 正常手势轨迹线条颜色 */
	private int lineCorlorNormal;
	/** 错误轨迹线条颜色 */
	private int lineCorlorError;
	/** 默认颜色 */
	private int defaultColor = 0xFF0000;
	/** 布局宽度 */
	private int laywidth;
	/** 布局高度 */
	private int layheight;
	/** 图片队列 */
	private ArrayList<XGuestLockInfo> arrayGuestInfo;
	/** 绘制轨迹路线的画笔 */
	private Paint linePaint;
	/** 是否是校验密码 */
	private boolean isVerify;
	/** 手势轨迹线条宽度 */
	private int lineWidth;
	/** 以选中点集合 */
	private ArrayList<XGuestLockInfo> arrayChoosed;
	/** 当前手指触摸位置的X坐标 */
	private float nowX;
	/** 当前手指触摸位置的Y坐标 */
	private float nowY;
	/** 手指是否抬起 */
	private boolean isTouchup;
	/** 是否允许绘制轨迹线 */
	private boolean isallowDrawLine = true;
	/** 是否校验错误 */
	private boolean isError;
	/** 记录手势密码缓存 */
	private StringBuilder haschoosed;
	/** 手势密码回调接口 */
	private XGuestureLockCallback xCallback;

	public XGuestureLock(Context context) {
		this(context, null);
	}

	public XGuestureLock(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XGuestureLock(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);// 调用初始化
		initMethod();// 调用初始化的方法
		addChildMethod();// 调用添加子View的方法
		arrayGuestInfo = new ArrayList<XGuestLockInfo>();
		arrayChoosed = new ArrayList<XGuestLockInfo>();
	}

	/** 初始化的方法 */
	private void initMethod() {
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStyle(Style.STROKE);// 设置非填充
		linePaint.setStrokeWidth(lineWidth);
	}

	/** 初始化一些属性值的方法 */
	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.GestureLock);
		drawableNormal = a.getDrawable(R.styleable.GestureLock_drawablenormal);
		drawableSelected = a
				.getDrawable(R.styleable.GestureLock_drawableselected);
		drawableError = a.getDrawable(R.styleable.GestureLock_drawableeerror);
		lineCorlorNormal = a.getColor(R.styleable.GestureLock_linecolornormal,
				defaultColor);
		lineCorlorError = a.getColor(R.styleable.GestureLock_linecolorerror,
				defaultColor);
		lineWidth = a.getDimensionPixelSize(R.styleable.GestureLock_linewidth,
				5);
	}

	/** 添加子View的方法 */
	private void addChildMethod() {
		for (int i = 0; i < 9; i++) {
			ImageView image = new ImageView(mContext);
			image.setImageDrawable(drawableNormal);
			image.setScaleType(ScaleType.FIT_CENTER);
			this.addView(image);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeigth);
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			int widthSpec = 0;
			int heightSpec = 0;
			LayoutParams params = v.getLayoutParams();
			if (params.width > 0) {
				widthSpec = MeasureSpec.makeMeasureSpec(params.width,
						MeasureSpec.EXACTLY);
			} else if (params.width == -1) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.EXACTLY);
			} else if (params.width == -2) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.AT_MOST);
			}
			if (params.height > 0) {
				heightSpec = MeasureSpec.makeMeasureSpec(params.height,
						MeasureSpec.EXACTLY);
			} else if (params.height == -1) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.EXACTLY);
			} else if (params.height == -2) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.AT_MOST);
			}
			v.measure(widthSpec, heightSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		laywidth = r - l;
		layheight = b - t;
		float onewidth = laywidth / 3;
		float oneheight = layheight / 3;
		for (int i = 0; i < getChildCount(); i++) {
			ImageView mView = (ImageView) getChildAt(i);
			float w = mView.getMeasuredWidth();// 获取View的宽度
			float h = mView.getMeasuredHeight();// 获取View的高度
			int cum = i % 3;
			int row = i / 3;
			float centerX = onewidth * cum + onewidth / 2;
			float centerY = oneheight * row + oneheight / 2;
			float left = centerX - w / 2;
			float top = centerY - h / 2;
			mView.layout((int) left, (int) top, (int) (left + w),
					(int) (top + h));
			XGuestLockInfo info = new XGuestLockInfo();
			info.setLeftX(left);
			info.setRightX(left + w);
			info.setTopY(top);
			info.setBottomY(top + h);
			info.setCenterX(centerX);
			info.setCenterY(centerY);
			info.setPosition(i);
			info.setImageView(mView);
			info.setState(XGuestLockInfo.IMAGE_NORMAL);
			arrayGuestInfo.add(info);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (isallowDrawLine) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				clearMethod();
				isTouchup = false;
				nowX = event.getX();
				nowY = event.getY();
				XGuestLockInfo info = getPointMethod(nowX, nowY);
				if (info != null) {
					info.getImageView().setImageDrawable(drawableSelected);
					arrayChoosed.add(info);
				}
				postInvalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				nowX = event.getX();
				nowY = event.getY();
				XGuestLockInfo info2 = getPointMethod(nowX, nowY);
				if (info2 != null && !isPointChooseMethod(info2)) {
					info2.getImageView().setImageDrawable(drawableSelected);
					arrayChoosed.add(info2);
				}
				postInvalidate();
				break;
			case MotionEvent.ACTION_UP:
				isTouchup = true;
				postInvalidate();
				break;
			default:
				break;
			}
		}
		return true;
	}

	/**
	 * 获取当前选中点的方法
	 * 
	 * @param pressX
	 * @param pressY
	 * @return 得到的封装点击信息的对象
	 */
	private XGuestLockInfo getPointMethod(float pressX, float pressY) {
		for (XGuestLockInfo info : arrayGuestInfo) {
			if (!(info.getLeftX() <= pressX && pressX <= info.getRightX())) {
				continue;
			}
			if (!(info.getTopY() <= pressY && pressY <= info.getBottomY())) {
				continue;
			}
			return info;
		}
		return null;
	}

	/**
	 * 获取当前点是否已经选择过的方法
	 * 
	 * @param getinfo
	 *            手势图标点集对象
	 * @return 已经选择过 true 没有选择过 false
	 */
	private boolean isPointChooseMethod(XGuestLockInfo getinfo) {
		for (XGuestLockInfo info : arrayChoosed) {
			if (info.getCenterX() == getinfo.getCenterX()
					&& info.getCenterY() == getinfo.getCenterY()) {
				return true;
			}
		}
		return false;
	}

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				isallowDrawLine = true;
				clearMethod();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isVerify && isError) {// 绘制错误提示轨迹
			drawaLineMethod(canvas, null, true);
			for (XGuestLockInfo info : arrayChoosed) {
				info.getImageView().setImageDrawable(drawableError);
			}
			isallowDrawLine = false;
			myHandler.sendEmptyMessageDelayed(0, 1000);
		} else {
			if (isTouchup) {
				if (arrayChoosed.size() < 4) {
					if(arrayChoosed.size()>0){
						if (xCallback != null)
							xCallback
							.onXGuestureLockCallback(XGuestureLockCallback.POINT_LENGTH_SHORT);
					}
					clearMethod();
				} else {
					if (!isVerify) {
						haschoosed = new StringBuilder();
						for (XGuestLockInfo xinfo : arrayChoosed) {
							haschoosed.append(xinfo.getPosition());
							isVerify = true;
						}
						clearMethod();
						if (xCallback != null)
							xCallback
									.onXGuestureLockCallback(XGuestureLockCallback.FIRST_LINE_OVER);
					} else {
						StringBuilder verifychoosed = new StringBuilder();
						for (XGuestLockInfo xinfo : arrayChoosed) {
							verifychoosed.append(xinfo.getPosition());
						}
						if (haschoosed.toString().equals(
								verifychoosed.toString())) {
							if (xCallback != null)
								xCallback
										.onXGuestureLockCallback(XGuestureLockCallback.TWICE_LINE_SAME);
						} else {
							if (xCallback != null)
								xCallback
										.onXGuestureLockCallback(XGuestureLockCallback.TWICE_NOT_SAME);
							isError = true;
							postInvalidate();
						}
					}
					drawaLineMethod(canvas, null, false);
				}
			} else {
				drawaLineMethod(canvas, new float[] { nowX, nowY }, false);
			}
		}
	}

	/**
	 * 绘制轨迹线条的方法
	 * 
	 * @param canvas
	 *            画布
	 * @param nowpts
	 *            手指当前按下位置的坐标
	 */
	private void drawaLineMethod(Canvas canvas, float[] nowpts, boolean iserror) {
		if (iserror) {
			linePaint.setColor(lineCorlorError);
		} else {
			linePaint.setColor(lineCorlorNormal);
		}
		if (arrayChoosed != null && arrayChoosed.size() > 0) {
			float[] pts = new float[nowpts != null ? (arrayChoosed.size() + 1) * 4
					: arrayChoosed.size() * 4];
			pts[0] = arrayChoosed.get(0).getCenterX();
			pts[1] = arrayChoosed.get(0).getCenterY();
			for (int i = 0; i < arrayChoosed.size(); i++) {
				if (i < arrayChoosed.size() - 1) {
					pts[i * 4 + 2] = arrayChoosed.get(i).getCenterX();
					pts[i * 4 + 3] = arrayChoosed.get(i).getCenterY();
					pts[i * 4 + 4] = arrayChoosed.get(i).getCenterX();
					pts[i * 4 + 5] = arrayChoosed.get(i).getCenterY();
				} else {
					pts[i * 4 + 2] = arrayChoosed.get(i).getCenterX();
					pts[i * 4 + 3] = arrayChoosed.get(i).getCenterY();
					if (nowpts != null) {
						pts[i * 4 + 4] = arrayChoosed.get(i).getCenterX();
						pts[i * 4 + 5] = arrayChoosed.get(i).getCenterY();
					}
				}
			}
			if (nowpts != null) {
				pts[pts.length - 2] = nowpts[0];
				pts[pts.length - 1] = nowpts[1];
			}
			canvas.drawLines(pts, linePaint);
		}
	}

	/** 清空绘制状态的方法 */
	private void clearMethod() {
		isError = false;
		arrayChoosed.clear();
		for (XGuestLockInfo info : arrayGuestInfo) {
			info.getImageView().setImageDrawable(drawableNormal);
		}
	}

	/** 获取是否是手势密码验证 */
	public boolean isVerify() {
		return isVerify;
	}

	/** 设置是否是手势密码验证 */
	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}

	/** 获取手势密码缓存 */
	public StringBuilder getHaschoosed() {
		return haschoosed;
	}

	/** 设置手势密码缓存 */
	public void setHaschoosed(StringBuilder haschoosed) {
		this.haschoosed = haschoosed;
	}

	/** 获取手势密码回调接口 */
	public XGuestureLockCallback getxCallback() {
		return xCallback;
	}

	/** 设置手势密码回调接口 */
	public void setxCallback(XGuestureLockCallback xCallback) {
		this.xCallback = xCallback;
	}

}
