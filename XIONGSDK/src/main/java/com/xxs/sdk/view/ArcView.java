package com.xxs.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.xxs.sdk.R;

/**
 * 自定义弧形进度的View
 * 
 * @author xiongxs
 * @date 2014-10-22
 */
public class ArcView extends View {
	/** 圆心 坐标 */
	private int centerX, centerY;
	/** 画笔 */
	private Paint paint;
	/** 圆弧线性渐变 */
	private Shader ArcShader;
	/** 弧形的大小 */
	private RectF recfArc;
	/** 视图的高度和宽度 */
	private int layoutheight, layoutweight;
	/** 圆弧的宽度 */
	private int arclinewidth;
	private Context mContext;
	/** 默认颜色 */
	private int defaultColor = 0xFF0000;
	/** 起始颜色 */
	private int startColor;
	/** 中间颜色 */
	private int mindleColor;
	/** 结束颜色 */
	private int endColor;
	/** 圆弧颜色，用于设置画笔颜色的 */
	private int arcviewcolor;
	/** 画笔模式 */
	private int arcviewstokenmode;
	/** 线条模式 */
	private static final int ARCVIEW_MODE_STOKEN = 0;
	/** 扇形模式 */
	private static final int ARCVIEW_MODE_FULL = 1;
	/** 是否包括圆心 */
	private boolean arcviewusecenter;
	/** 起始角度 */
	private float startangle;
	/** 延展弧度 */
	private float sweepangle;
	/** 当前弧度 */
	private float currentangle;
	/** 渐变模式 0 线性, 1圆周,2径向 */
	private int arcviewshadermode;
	/** 是否允许画弧形动画 */
	private boolean isallowanimation;

	public ArcView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
	}

	public ArcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
	}

	public ArcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
	}

	/** 初始化一些属性值的方法 */
	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.ArcView);
		arclinewidth = a.getDimensionPixelSize(
				R.styleable.ArcView_arcviewwidth, 0);
		startColor = a.getColor(R.styleable.ArcView_arcviewstartcolor, 0);
		mindleColor = a.getColor(R.styleable.ArcView_arcviewmindlecolor, 0);
		endColor = a.getColor(R.styleable.ArcView_arcviewendcolor, 0);
		defaultColor = getResources().getColor(R.color.hongse);
		arcviewcolor = a.getColor(R.styleable.ArcView_arcviewcolor,
				defaultColor);
		arcviewstokenmode = a.getInt(R.styleable.ArcView_arcviewstokenmode, 0);// 默认为线条模式
		arcviewusecenter = a.getBoolean(R.styleable.ArcView_arcviewusecenter,
				false);
		isallowanimation = a.getBoolean(
				R.styleable.ArcView_arcviewallowanimation, false);
		startangle = a.getFloat(R.styleable.ArcView_arcviewstartangle, 0f);
		sweepangle = a.getFloat(R.styleable.ArcView_arcviewsweepangle, 0f);
		arcviewshadermode = a.getInt(R.styleable.ArcView_arcviewshadermode, -1);
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
		layoutweight = r - l;
		layoutheight = b - t;
		int lay = Math.min(layoutweight, layoutheight);
		layoutweight = lay;
		layoutheight = lay;
		// TODO Auto-generated method stub
		centerX = layoutweight / 2;// 得到圆心坐标
		centerY = layoutheight / 2;
		intMethod();
	}

	/** 初始化的方法 */
	private void intMethod() {
		paint = new Paint();
		paint.setColor(arcviewcolor);
		paint.setAntiAlias(true);// 设置消除锯齿
		setShaderMethod();
	}

	/**
	 * 设置渐变器的方法
	 */
	private void setShaderMethod() {
		if (startColor != 0 && mindleColor != 0 && endColor != 0) {
			setShaderMethod(arcviewshadermode, new int[] { startColor,
					mindleColor, endColor });
		} else if (startColor != 0 && mindleColor != 0 && endColor == 0) {
			setShaderMethod(arcviewshadermode, new int[] { startColor,
					mindleColor });
		} else if (startColor != 0 && mindleColor == 0 && endColor != 0) {
			setShaderMethod(arcviewshadermode,
					new int[] { startColor, endColor });
			System.out.println();
		} else if (startColor != 0 && mindleColor == 0 && endColor == 0) {
			ArcShader = null;
		} else if (startColor == 0 && mindleColor != 0 && endColor != 0) {
			setShaderMethod(arcviewshadermode, new int[] { mindleColor,
					endColor });
		} else if (startColor == 0 && mindleColor != 0 && endColor == 0) {
			ArcShader = null;
		} else if (startColor == 0 && mindleColor == 0 && endColor != 0) {
			ArcShader = null;
		} else if (startColor == 0 && mindleColor == 0 && endColor == 0) {
			ArcShader = null;
		}
		recfArc = new RectF(arclinewidth / 2, arclinewidth / 2, layoutweight
				- arclinewidth / 2, layoutheight - arclinewidth / 2);
	}

	/**
	 * 设置渐变器的方法
	 * 
	 * @param modle
	 *            模式
	 * @param colors
	 *            颜色值
	 */
	private void setShaderMethod(int modle, int[] colors) {
		// int len = colors.length;
		// float[] positions = new float[len];
		// for (int i = 0; i < len; i++) {
		// positions[i] = i/(float)(len-1);
		// }
		switch (modle) {
		case 0:
			ArcShader = new LinearGradient(centerX, 0, centerX, layoutheight,
					colors, null, Shader.TileMode.CLAMP);
			break;
		case 1:
			ArcShader = new SweepGradient(centerX, centerY, colors, null);
			break;
		case 2:
			ArcShader = new RadialGradient(centerX, centerY, layoutheight / 2,
					colors, null, Shader.TileMode.CLAMP);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawArcMethod(canvas);
	}

	/***
	 * 绘制圆弧的方法
	 * 
	 * @param canvas
	 */
	public void drawArcMethod(Canvas canvas) {
		paint.setShader(ArcShader);
		switch (arcviewstokenmode) {
		case ARCVIEW_MODE_STOKEN:
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(arclinewidth);
			break;
		case ARCVIEW_MODE_FULL:
			paint.setStyle(Style.FILL);
			break;
		default:
			break;
		}
		if (isallowanimation) {
			if (sweepangle > currentangle) {
				currentangle += 1;
				canvas.drawArc(recfArc, startangle, currentangle,
						arcviewusecenter, paint);
				invalidate();
			} else if (sweepangle < currentangle) {
				currentangle -= 1;
				canvas.drawArc(recfArc, startangle, currentangle,
						arcviewusecenter, paint);
				invalidate();
			} else {
				canvas.drawArc(recfArc, startangle, currentangle,
						arcviewusecenter, paint);
			}
		} else {
			canvas.drawArc(recfArc, startangle, sweepangle, arcviewusecenter,
					paint);
		}
	}

	/**
	 * 设置跨越弧度的方法
	 * 
	 * @param sweepangle
	 *            度数
	 */
	public void setSweepAngle(float sweepangle) {
		this.sweepangle = Math.round(sweepangle);
		postInvalidate();
	}

	/**
	 * 设置起始角度的方法
	 * 
	 * @param startangle
	 *            起始角度
	 */
	public void setStartAngle(float startangle) {
		this.startangle = startangle;
		postInvalidate();
	}

	/** 设置起始颜色值 */
	public void setStartColor(int startColor) {
		this.startColor = startColor;
		setShaderMethod();
		postInvalidate();
	}

	/** 设置中间颜色值 */
	public void setMindleColor(int mindleColor) {
		this.mindleColor = mindleColor;
		setShaderMethod();
		postInvalidate();
	}

	/** 设置结束颜色值 */
	public void setEndColor(int endColor) {
		this.endColor = endColor;
		setShaderMethod();
		postInvalidate();
	}

	/** 设置画笔颜色值 */
	public void setArcviewcolor(int arcviewcolor) {
		this.arcviewcolor = arcviewcolor;
		setShaderMethod();
		postInvalidate();
	}

}
