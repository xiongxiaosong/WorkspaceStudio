package com.xxs.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.xxs.sdk.R;

/**
 * 正弦线控件
 * 
 * @author xiongxs
 *
 */
public class SineView extends View {
	/** 默认颜色 */
	private static final int DEFULT_COLOR = 0xFF000000;
	/** 振幅 */
	private int sineamplitude;
	/** 波长 */
	private int sinelength;
	/** 正弦线线条宽度 */
	private int sinelinewidth;
	/** 正弦线线条颜色 */
	private int sinelinecolor;
	/** 上下文 */
	private Context mContext;
	/** 画笔 */
	private Paint paint;
	/** 轨迹 */
	private Path path;
	/** 视图宽度 */
	private int laywidth;
	/** 视图高度 */
	private int layheight;
	/** 角速度 */
	private float w = 0.5f;
	/** 波浪起始Y坐标 */
	private int startY = 0;
	/** 偏移角度 */
	private float angle = 0;
	/** 是否允许波浪动画 */
	private boolean isallowanim;

	public SineView(Context context) {
		this(context, null);
	}

	public SineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);// 调用初始化属性的方法
		init();// 调用初始化的方法
	}

	/**
	 * 创建视图之前调用的方法
	 */
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		isallowanim = true;
		new WaveAnimThread().start();
	}

	/**
	 * 销毁视图之前调用的方法
	 */
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		isallowanim = false;
	}

	/** 初始化一些属性值的方法 */
	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.sineview);
		sinelinecolor = a.getColor(R.styleable.sineview_sinelinecolor,
				DEFULT_COLOR);
		sinelength = a.getDimensionPixelSize(R.styleable.sineview_sinelength,
				200);
		sinelinewidth = a.getDimensionPixelSize(
				R.styleable.sineview_sinelinewidth, 1);
		sineamplitude = a.getDimensionPixelSize(
				R.styleable.sineview_sineamplitude, 20);
		a.recycle();
	}

	/** 初始化方法 */
	private void init() {
		// 初始化画笔和轨迹对象
		paint = new Paint();
		path = new Path();
		// 设置抗锯齿
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(sinelinewidth);
		// 设置画笔颜色
		paint.setColor(sinelinecolor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		laywidth = getMeasuredWidth();
		layheight = getMeasuredHeight();
		startY = layheight / 2;
	}

	/**
	 * 执行波浪动画的线程
	 * 
	 * @author 熊小松
	 *
	 */
	private class WaveAnimThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (isallowanim) {
				int nowY = (int) (layheight / 2 - sineamplitude / 2);
				if (angle >= 360) {
					angle -= 360;
				}
				angle += w;
				startY = nowY;
				// if (startY > nowY) {
				// startY--;
				// } else {
				// startY++;
				// }
				refreshHandler.sendEmptyMessage(1);
				try {
					Thread.sleep(4);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 执行刷新视图工作的Handler
	 */
	private Handler refreshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				postInvalidate();
			}
		}
	};

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setPath(canvas);
	}

	/**
	 * 绘制波浪的方法
	 * 
	 * @param canvas
	 */
	private void setPath(Canvas canvas) {
		float mX = -10;
		float mY = startY;
		path.reset();
		path.moveTo(mX, startY);
		for (float i = -10; i < laywidth + 10; i++) {
			float y = startY
					- (int) ((Math
							.sin((i / sinelength + angle / 180) * Math.PI) * sineamplitude));
			path.quadTo(mX, mY, (i + mX) / 2, (y + mY) / 2);
			mX = i;
			mY = y;
		}
		canvas.drawPath(path, paint);
	}
}
