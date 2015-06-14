package com.xxs.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.xxs.sdk.R;

/**
 * 自定义波浪球球控件
 * 
 * @author xiongxs
 * @date 2015-04-04
 * @introduce 实现球形水纹进度
 */
public class WavesBallView extends View {
	/** 默认颜色 */
	private static final int DEFULT_COLOR = 0xFF000000;
	/** 布局宽度 */
	private int laywidth;
	/** 布局高度 */
	private int layheight;
	/** 布局背景颜色 */
	private int laybackcolor;
	/** 水波背景颜色 */
	private int wavesbackcolor;
	/** 波浪线条颜色 */
	private int wavelinecolor;
	/** 布局背景画笔 */
	private Paint laybackpaint;
	/** 波浪背景画笔 */
	private Paint wavebackpaint;
	/** 波浪线条画笔 */
	private Paint wavelinepaint;
	/** 波浪背景轨迹 */
	private Path wavebackpath;
	/** 百分比 */
	private float wavepercent;
	/** 上下文 */
	private Context mContext;
	/** 振幅 */
	private float amplitude;
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
	/** 截图使用轨迹 */
	private Path mPath;
	/** 是否允许动画 */
	private boolean isallowanim;

	/** 构造函数 */
	public WavesBallView(Context context) {
		this(context, null);
	}

	/** 构造函数 */
	public WavesBallView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/** 构造函数 */
	public WavesBallView(Context context, AttributeSet attrs, int defStyleAttr) {
		// this(context, attrs, defStyleAttr, 0);
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
					R.styleable.waveballview);
			int n = a.getIndexCount();
			for (int i = 0; i < n; i++) {
				int attr = a.getIndex(i);
				if (attr == R.styleable.waveballview_waveballbackcolor) {
					wavesbackcolor = a.getColor(attr, DEFULT_COLOR);
				} else if (attr == R.styleable.waveballview_waveballamplitude) {
					amplitude = a.getDimensionPixelSize(attr, 20);
				} else if (attr == R.styleable.waveballview_waveballlaybackcolor) {
					laybackcolor = a.getColor(attr, DEFULT_COLOR);
				}  else if (attr == R.styleable.waveballview_waveballlength) {
					wavelength = a.getDimensionPixelSize(attr, 400);
				} else if (attr == R.styleable.waveballview_waveballlinecolor) {
					wavelinecolor = a.getColor(attr, DEFULT_COLOR);
				} else if (attr == R.styleable.waveballview_waveballlinewidth) {
					wavelinewidth = a.getDimensionPixelSize(attr, 1);
				} else if (attr == R.styleable.waveballview_waveballpercent) {
					wavepercent = a.getFloat(attr, 80f);
				}
			}
			a.recycle();
		}
	}

	/** 初始化方法 */
	private void init() {
		// 初始化画笔和轨迹对象
		laybackpaint = new Paint();
		wavebackpaint = new Paint();
		wavelinepaint = new Paint();
		wavebackpath = new Path();
		mPath = new Path();
		wavelinepaint.setStyle(Style.STROKE);
		// 设置抗锯齿
		laybackpaint.setAntiAlias(true);
		wavebackpaint.setAntiAlias(true);
		wavelinepaint.setAntiAlias(true);
		// 设置画笔颜色
		laybackpaint.setColor(laybackcolor);
		wavebackpaint.setColor(wavesbackcolor);
		wavelinepaint.setColor(wavelinecolor);
		// 设置水波线条宽度
		wavelinepaint.setStrokeWidth(wavelinewidth);
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
		canvas.drawCircle((float) laywidth / 2, (float) laywidth / 2,
				(float) laywidth / 2, laybackpaint);
		// 下面几句是设置截取轨迹的方法
		canvas.save();
		mPath.reset();
		canvas.clipPath(mPath); // makes the clip empty
		mPath.addCircle(laywidth / 2, layheight / 2, laywidth / 2,
				Path.Direction.CCW);
		canvas.clipPath(mPath, Region.Op.REPLACE);
		setPath(canvas);// 调用绘制水纹波浪的方法
	}


	/**
	 * 绘制水纹波浪球的方法
	 * 
	 * @param canvas
	 */
	private void setPath(Canvas canvas) {
		float mX = 0;
		float mY = startY;
		wavebackpath.reset();
		wavebackpath.moveTo(mX, startY);
		for (float i = 0; i <= laywidth; i++) {
			float y = startY
					- (float) (Math.sin((i / wavelength + angle / 180)
							* Math.PI) * amplitude);
			wavebackpath.quadTo(mX, mY, (i + mX) / 2, (y + mY) / 2);
			mX = i;
			mY = y;
		}
		wavebackpath.lineTo(laywidth, layheight);
		wavebackpath.lineTo(0, layheight);
		canvas.drawPath(wavebackpath, wavebackpaint);
		canvas.drawPath(wavebackpath, wavelinepaint);
		canvas.restore();
	}

	/**
	 * 执行波浪动画的线程
	 * 
	 * @author xiongxs
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

	/** 获取振幅 */
	public float getAmplitude() {
		return amplitude;
	}

	/** 设置振幅 */
	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
		postInvalidate();
	}

	/** 获取布局背景颜色 */
	public int getLaybackcolor() {
		return laybackcolor;
	}

	/** 设置布局背景颜色 */
	public void setLaybackcolor(int laybackcolor) {
		this.laybackcolor = laybackcolor;
		laybackpaint.setColor(laybackcolor);
		postInvalidate();
	}

	/** 获取水波背景颜色 */
	public int getWavesbackcolor() {
		return wavesbackcolor;
	}

	/** 设置水波背景颜色 */
	public void setWavesbackcolor(int wavesbackcolor) {
		this.wavesbackcolor = wavesbackcolor;
		wavebackpaint.setColor(wavesbackcolor);
		postInvalidate();
	}

	/** 获取水波线条颜色 */
	public int getWavelinecolor() {
		return wavelinecolor;
	}

	/** 设置水波线条颜色 */
	public void setWavelinecolor(int wavelinecolor) {
		this.wavelinecolor = wavelinecolor;
		wavelinepaint.setColor(wavelinecolor);
		postInvalidate();
	}

	/** 获取波长 */
	public int getWavelength() {
		return wavelength;
	}

	/** 设置波长 */
	public void setWavelength(int wavelength) {
		this.wavelength = wavelength;
		postInvalidate();
	}

	/** 获取水波线条宽度 */
	public int getWavelinewidth() {
		return wavelinewidth;
	}

	/** 设置水波线条宽度 */
	public void setWavelinewidth(int wavelinewidth) {
		this.wavelinewidth = wavelinewidth;
		postInvalidate();
	}

	/** 获取百分比 */
	public float getWavepercent() {
		return wavepercent;
	}

	/** 设置百分比 */
	public void setWavepercent(float wavepercent) {
		this.wavepercent = wavepercent;
		postInvalidate();
	}

}
