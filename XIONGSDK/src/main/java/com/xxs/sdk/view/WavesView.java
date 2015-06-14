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
 * 自定义首页波浪进度控件
 * 
 * @author xiongxs
 * @date 2015-04-01
 */
public class WavesView extends View {
	/** 默认颜色 */
	private static final int DEFULT_COLOR = 0xFF000000;
	/** 波浪背景颜色 */
	private int wavebackcolor;
	/** 波浪线条颜色 */
	private int wavelinecolor;
	/** 波浪进度百分比 */
	private float wavepercent;
	/** 是否允许波浪动画 */
	private boolean isallowanim;
	/** 上下文 */
	private Context mContext;
	/** 波浪背景画笔 */
	private Paint wavebackpaint;
	/** 波浪线条画笔 */
	private Paint wavelinepaint;
	/** 轨迹 */
	private Path wavepath;
	/** 视图宽度 */
	private int laywidth;
	/** 视图高度 */
	private int layheight;
	/** 振幅 */
	private int amplitude;
	/** 波长 */
	private int wavelength;
	/** 波浪线条宽度 */
	private int wavelinewidth;
	/** 角速度 */
	private float w = 0.5f;
	/** 波浪起始Y坐标 */
	private int startY = 0;
	/** 偏移角度 */
	private float angle = 0;
	/** 构造函数 */
	public WavesView(Context context) {
		this(context, null);
	}

	/** 构造函数 */
	public WavesView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/** 构造函数 */
	public WavesView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setWillNotDraw(false);
		mContext = context;
		setCustomAttributes(attrs);// 调用初始化属性的方法
		init();// 调用初始化的方法
	}

	/** 初始化一些属性值的方法 */
	private void setCustomAttributes(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = mContext.obtainStyledAttributes(attrs,
					R.styleable.waveview);
			int n = a.getIndexCount();
			for (int i = 0; i < n; i++) {
				int attr = a.getIndex(i);
				if (attr == R.styleable.waveview_wavebackcolor) {
					wavebackcolor = a.getColor(attr, DEFULT_COLOR);
				}  else if (attr == R.styleable.waveview_wavelinecolor) {
					wavelinecolor = a.getColor(attr, DEFULT_COLOR);
				} else if (attr == R.styleable.waveview_wavepercent) {
					wavepercent = a.getFloat(attr, 80f);
				} else if (attr == R.styleable.waveview_waveamplitude) {
					amplitude = a.getDimensionPixelSize(attr, 20);
				} else if (attr == R.styleable.waveview_wavelength) {
					wavelength = a.getDimensionPixelSize(attr, 400);
				} else if (attr == R.styleable.waveview_wavelinewidth) {
					wavelinewidth = a.getDimensionPixelSize(attr, 1);
				}
			}
			a.recycle();
		}
	}

	/** 初始化方法 */
	private void init() {
		// 初始化画笔和轨迹对象
		wavebackpaint = new Paint();
		wavelinepaint = new Paint();
		wavepath = new Path();
		// 设置抗锯齿
		wavebackpaint.setAntiAlias(true);
		wavelinepaint.setAntiAlias(true);
		wavelinepaint.setStyle(Style.STROKE);
		wavelinepaint.setStrokeWidth(wavelinewidth);
		// 设置画笔颜色
		wavebackpaint.setColor(wavebackcolor);
		wavelinepaint.setColor(wavelinecolor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		laywidth = getMeasuredWidth();
		layheight = getMeasuredHeight();
		startY = layheight;
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

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		setPath(canvas);// 调用绘制波浪的方法
	}



	/**
	 * 绘制波浪的方法
	 * 
	 * @param canvas
	 */
	private void setPath(Canvas canvas) {
		float mX = -10;
		float mY = startY;
		wavepath.reset();
		wavepath.moveTo(mX, startY);
		for (float i = -10; i < laywidth + 10; i++) {
			float y = startY
					- (int) ((Math
							.sin((i / wavelength + angle / 180) * Math.PI) * amplitude));
			wavepath.quadTo(mX, mY, (i + mX) / 2, (y + mY) / 2);
			mX = i;
			mY = y;
		}
		canvas.drawPath(wavepath, wavelinepaint);
		wavepath.lineTo(laywidth+10, layheight);
		wavepath.lineTo(-10, layheight);
		canvas.drawPath(wavepath, wavebackpaint);
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
				int nowY = (int) (layheight * (1 - wavepercent / 100) - amplitude);
				if (angle >= 360) {
					angle -= 360;
				}
				angle += w;
				if (startY > nowY) {
					startY--;
				} else {
					startY++;
				}
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

	/** 获取背景颜色 */
	public int getWavebackcolor() {
		return wavebackcolor;
	}

	/** 设置背景颜色 */
	public void setWavebackcolor(int wavebackcolor) {
		this.wavebackcolor = wavebackcolor;
		wavebackpaint.setColor(wavebackcolor);
		postInvalidate();
	}

	/** 获取波浪线条颜色 */
	public int getWavelinecolor() {
		return wavelinecolor;
	}

	/** 设置波浪线条颜色 */
	public void setWavelinecolor(int wavelinecolor) {
		this.wavelinecolor = wavelinecolor;
		wavelinepaint.setColor(wavelinecolor);
		postInvalidate();
	}

	/** 获取波浪进度百分比 */
	public float getWavepercent() {
		return wavepercent;
	}

	/** 设置波浪进度百分比 0-100 */
	public void setWavepercent(float wavepercent) {
		this.wavepercent = wavepercent;
		postInvalidate();
	}

	/** 获取是否允许动画 */
	public boolean isIsallowanim() {
		return isallowanim;
	}

	/** 设置是否允许动画 */
	public void setIsallowanim(boolean isallowanim) {
		this.isallowanim = isallowanim;
	}
}
