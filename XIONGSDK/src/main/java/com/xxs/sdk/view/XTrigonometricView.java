package com.xxs.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.xxs.sdk.R;
import com.xxs.sdk.util.TransformUtil;

/**
 * 自定义绘制三角函数的控件
 * 
 * @author xiongxs
 * @date 2014-12-02
 */
public class XTrigonometricView extends View {
	/** 默认不绘制曲线 */
	private static final int DEFULT_MODLE = -1;
	/** 默认文字大小10像素 */
	private static final int DEFULT_TEXTSIZE = 10;
	/** 默认横向分割数 */
	private static final int DEFULT_HRONUMBER = 7;
	/** 默认纵向分割数 */
	private static final int DEFULT_VERNUMBER = 5;
	/** 默认不允许绘制表格线 */
	private static final boolean DEFULT_ALLOWTAB = false;
	/** 不进行曲线绘制 */
	public static final int MODLE_NONE = -1;
	/** 正弦 */
	public static final int MODLE_SIN = 0;
	/** 余弦 */
	public static final int MODLE_COS = 1;
	/** 正切 */
	public static final int MODLE_TAN = 2;
	/** 余切 */
	public static final int MODLE_COT = 3;
	/** 反正弦 */
	public static final int MODLE_ARCSIN = 4;
	/** 反余弦 */
	public static final int MODLE_ARCCOS = 5;
	/** 反正切 */
	public static final int MODLE_ARCTAN = 6;
	/** 反余切 */
	public static final int MODLE_ARCCOT = 7;
	/** 默认绘制第一象限 */
	private static final int DEFULT_XIANGXIAN = 0;
	/** 第一象限 */
	public static final int XIANGXIAN_ONE = 0;
	/** 第二象限 */
	public static final int XIANGXIAN_TWO = 1;
	/** 第三象限 */
	public static final int XIANGXIAN_THREE = 2;
	/** 第四象限 */
	public static final int XIANGXIAN_FOUR = 3;
	/** 第一二象限 */
	public static final int XIANGXIAN_ONEANDTWO = 4;
	/** 第三四象限 */
	public static final int XIANGXIAN_THREEANDFOUR = 5;
	/** 第一四象限 */
	public static final int XIANGXIAN_ONEANDFOUR = 6;
	/** 第二三象限 */
	public static final int XIANGXIAN_TWOANDTHREE = 7;
	/** 第一二三四象限 */
	public static final int XIANGXIAN_ONETWOTHREEFOUR = 8;
	/** 默认线条宽度 1px */
	private static final int DEFULT_LINEWIDTH = 1;
	/** 默认边缘距离5像素 */
	private static final int DEFULT_PAIDING = 5;
	/** 默认曲线颜色 */
	private static final int DEFULT_LINECOLOR = 0xff000000;
	/** 默认坐标轴颜色 */
	private static final int DEFULT_XYCOLOR = 0xff000000;
	/** 默认View名称文字颜色 */
	private static final int DEFULT_VIEWNAMECOLOR = 0xff000000;
	/** 默认系数 */
	private static final int DEFULT_COEFFICIENT = 1;
	/** 默认常量 */
	private static final int DEFULT_CONSTANT = 0;
	/** 默认偏移量 */
	private static final int DEFULT_OFFSET = 0;
	/** 第一条三角函数模型 0正弦函数,1与弦函数,2正切函数,3余切函数,4反正弦函数,5反余弦函数,6反正切函数,7反余切函数 */
	private int firstmodle;
	/** 第二条三角函数模型 0正弦函数,1与弦函数,2正切函数,3余切函数,4反正弦函数,5反余弦函数,6反正切函数,7反余切函数 */
	private int secondmodle;
	/** 绘制象限 0第一象限,1第二象限,2第三象限,3第四象限,4第一二象限,5第三四象限,6第一四象限,7第二三象限,8第一二三四象限 */
	private int xiangxian;
	/** 上下文 */
	private Context mContext;
	/** 第一条曲线颜色 */
	private int firstlinecolor;
	/** 第二条曲线颜色 */
	private int secondlinecolor;
	/** 基准线颜色 */
	private int jizhunlinecolor;
	/** 曲线线条宽度 */
	private int linewidth;
	/** xy坐标轴线条宽度 */
	private int xywidth;
	/** 坐标轴颜色 */
	private int xycolor;
	/** 布局宽度 */
	private float layoutwidth;
	/** 布局高度 */
	private float layoutheight;
	/** 中心点X坐标 */
	private float centerX;
	/** 中心点Y坐标 */
	private float centerY;
	/** 1/4Y */
	private float onefourY;
	/** 3/4Y */
	private float threefourY;
	/** 绘制XY坐标轴的画笔 */
	private Paint paintxy;
	/** 绘制第一条曲线图的画笔 */
	private Paint paintfirstline;
	/** 绘制第二条曲线图的画笔 */
	private Paint paintsecondline;
	/** 基准线图画笔 */
	private Paint paintjizhunline;
	/** 基准线图画笔 */
	private Paint viewnamepaint;
	/** 边缘距离 */
	private int laypaiding;
	/** 绘制虚线的工具 */
	private DashPathEffect effects;
	/** 第一条曲线系数 */
	private float firstcoefficient;
	/** 第一条曲线常量 */
	private float firstconstant;
	/** 第一条曲线偏移量 */
	private float firstoffset;
	/** 第二条曲线系数 */
	private float secondcoefficient;
	/** 第二条曲线常量 */
	private float secondconstant;
	/** 第二条曲线偏移量 */
	private float secondoffset;
	/** 第一条曲线名称 */
	private String firstname;
	/** 第二条曲线名称 */
	private String secondname;
	/** 整个View的名称 */
	private String viewname;
	/** 绘制文字的大小 */
	private int textsize;
	/** 是否允许绘制表格线 */
	private boolean allowtab;
	/** 横向分割数 */
	private int heronumber;
	/** 纵向分个数 */
	private int vernumber;
	/** View名称文字颜色 */
	private int viewnamecolor;
	/** 起始数值 */
	private int verstartnumber = 0;
	/** 结束数值 */
	private int verendnumber = 100;

	public XTrigonometricView(Context context) {
		super(context);
		this.mContext = context;
		setWillNotDraw(false);
		intMethod();
	}

	public XTrigonometricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
		intMethod();
	}

	public XTrigonometricView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
		intMethod();
	}

	/** 初始化一些属性值的方法 */
	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.XTrigonometricView);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			if(attr == R.styleable.XTrigonometricView_XTrigonometricView_firstmodle){
				firstmodle = a.getInt(attr, DEFULT_MODLE);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_secondmodle){
				secondmodle = a.getInt(attr, DEFULT_MODLE);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_xiangxian){
				xiangxian = a.getInt(attr, DEFULT_XIANGXIAN);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_firstlinecolor){
				firstlinecolor = a.getColor(attr, DEFULT_LINECOLOR);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_jizhunlinecolor){
				jizhunlinecolor = a.getColor(attr, DEFULT_LINECOLOR);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_secondlinecolor){
				secondlinecolor = a.getColor(attr, DEFULT_LINECOLOR);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_xylinecolor){
				xycolor = a.getColor(attr, DEFULT_XYCOLOR);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_linewidth){
				linewidth = a.getDimensionPixelSize(attr, DEFULT_LINEWIDTH);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_xywidth){
				xywidth = a.getDimensionPixelSize(attr, DEFULT_LINEWIDTH);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_pading){
				laypaiding = a.getDimensionPixelSize(attr, DEFULT_PAIDING);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_textsize){
				textsize = a.getDimensionPixelSize(attr, DEFULT_TEXTSIZE);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_firstcoefficient){
				firstcoefficient = a.getFloat(attr, DEFULT_COEFFICIENT);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_firstconstant){
				firstconstant = a.getFloat(attr, DEFULT_CONSTANT);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_firstoffset){
				firstoffset = a.getFloat(attr, DEFULT_OFFSET);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_secondcoefficient){
				secondcoefficient = a.getFloat(attr, DEFULT_COEFFICIENT);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_secondconstant){
				secondconstant = a.getFloat(attr, DEFULT_CONSTANT);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_secondoffset){
				secondoffset = a.getFloat(attr, DEFULT_OFFSET);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_firstname){
				firstname = a.getString(attr);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_secondname){
				secondname = a.getString(attr);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_viewname){
				viewname = a.getString(attr);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_allowtab){
				allowtab = a.getBoolean(attr, DEFULT_ALLOWTAB);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_heronumber){
				heronumber = a.getInt(attr, DEFULT_HRONUMBER);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_vernumber){
				vernumber = a.getInt(attr, DEFULT_VERNUMBER);
			}else if(attr == R.styleable.XTrigonometricView_XTrigonometricView_viewnamecolor){
				viewnamecolor = a.getColor(attr, DEFULT_VIEWNAMECOLOR);
			}
		}
		a.recycle();
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeigth);
	};

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		layoutwidth = right - left;
		layoutheight = bottom - top;
		centerX = layoutwidth / 2;
		centerY = layoutheight / 2;
		onefourY = (layoutheight - laypaiding * 2) / 4 + laypaiding;
		threefourY = (layoutheight - laypaiding * 2) / 4 * 3 + laypaiding;
	}

	/** 初始化的方法 */
	private void intMethod() {
		// 初始化画笔
		paintfirstline = new Paint();
		paintsecondline = new Paint();
		paintjizhunline = new Paint();
		paintxy = new Paint();
		viewnamepaint = new Paint();
		// 设置画笔文字大小
		paintfirstline.setTextSize(textsize);
		paintsecondline.setTextSize(textsize);
		paintjizhunline.setTextSize(textsize);
		paintxy.setTextSize(textsize);
		viewnamepaint.setTextSize(textsize);
		// 设置消除锯齿
		paintfirstline.setAntiAlias(true);
		paintsecondline.setAntiAlias(true);
		paintjizhunline.setAntiAlias(true);
		paintxy.setAntiAlias(true);
		viewnamepaint.setAntiAlias(true);
		// 设置画笔线条宽度
		paintfirstline.setStrokeWidth(linewidth);
		paintsecondline.setStrokeWidth(linewidth);
		paintjizhunline.setStrokeWidth(linewidth);
		paintxy.setStrokeWidth(xywidth);
		viewnamepaint.setStrokeWidth(xywidth);
		// 设置画笔颜色
		paintfirstline.setColor(firstlinecolor);
		paintsecondline.setColor(secondlinecolor);
		paintxy.setColor(xycolor);
		paintjizhunline.setColor(jizhunlinecolor);
		viewnamepaint.setColor(viewnamecolor);
		// 设置画虚线的属性
		effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paintfirstline.setStrokeJoin(Paint.Join.ROUND);
		paintfirstline.setStrokeCap(Paint.Cap.ROUND);
		paintsecondline.setStrokeJoin(Paint.Join.ROUND);
		paintsecondline.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawXYMethod(canvas);
		drawFirstLineMethod(canvas);
		drawSecondLineMethod(canvas);
	}

	/**
	 * 绘制虚线的方法
	 * 
	 * @param canvas
	 * @param y
	 *            Y坐标
	 */
	public void drawEffectLine(Canvas canvas, float y) {
		// 画竖直虚线
		paintjizhunline.setPathEffect(effects);
		paintjizhunline.setStyle(Paint.Style.STROKE);// 空心
		Path path1 = new Path();
		path1.moveTo(laypaiding, y);
		path1.lineTo(layoutwidth - laypaiding, y);
		canvas.drawPath(path1, paintjizhunline);
	}

	/** 绘制XY坐标轴线条的方法 */
	private void drawXYMethod(Canvas canvas) {
		paintfirstline.setStyle(Paint.Style.FILL);
		paintsecondline.setStyle(Paint.Style.FILL);
		viewnamepaint.setStyle(Paint.Style.FILL);
		switch (xiangxian) {
		case 0:// 第一象限
			if (!TextUtils.isEmpty(firstname) && firstmodle >= 0) {
				float namewidth = paintfirstline.measureText(firstname);
				canvas.drawText(firstname,
						layoutwidth - namewidth - laypaiding, textsize,
						paintfirstline);
				canvas.drawCircle(layoutwidth - namewidth - laypaiding
						- TransformUtil.dip2px(5),
						textsize / 2 + TransformUtil.dip2px(5) / 2,
						TransformUtil.dip2px(3), paintfirstline);
			}
			if (!TextUtils.isEmpty(secondname) && secondmodle >= 0) {
				float namewidth = paintsecondline.measureText(secondname);
				canvas.drawText(secondname, layoutwidth - namewidth
						- laypaiding, textsize * 2, paintsecondline);
				canvas.drawCircle(layoutwidth - namewidth - laypaiding
						- TransformUtil.dip2px(5), textsize / 2 * 3
						+ TransformUtil.dip2px(5) / 2, TransformUtil.dip2px(3),
						paintsecondline);
			}
			canvas.drawLine(laypaiding, layoutheight - laypaiding, layoutwidth
					- laypaiding, layoutheight - laypaiding, paintxy);
			canvas.drawLine(laypaiding, laypaiding, laypaiding, layoutheight
					- laypaiding, paintxy);
			canvas.drawPoint(laypaiding, layoutheight - laypaiding, paintxy);
			if (allowtab) {// 绘制表格线以及标签文字
				float onewidth = (layoutwidth - laypaiding * 2)
						/ (heronumber - 1);
				float oneheight = (layoutheight - laypaiding * 2)
						/ (vernumber - 1);
				for (int i = 0; i < heronumber; i++) {
					canvas.drawLine(laypaiding + onewidth * i, laypaiding,
							laypaiding + onewidth * i, layoutheight
									- laypaiding, paintxy);
					float textwidth = paintxy.measureText(i + 1 + "");
					canvas.drawText(i + 1 + "", laypaiding + onewidth * i
							- textwidth / 2, layoutheight - laypaiding
							+ textsize, paintxy);
				}
				for (int i = 0; i < vernumber; i++) {
					canvas.drawLine(laypaiding, laypaiding + oneheight * i,
							layoutwidth - laypaiding, laypaiding + oneheight
									* i, paintxy);
				}
			}
			float startnumwidth = paintxy.measureText(verstartnumber+"");
			float endnumwidth = paintxy.measureText(verendnumber+"");
			canvas.drawText(verstartnumber+"", laypaiding-startnumwidth-textsize/4, layoutheight-laypaiding+textsize/2, paintxy);
			canvas.drawText(verendnumber+"", laypaiding-endnumwidth-textsize/4, laypaiding+textsize/2, paintxy);
			if (!TextUtils.isEmpty(viewname)) {
				canvas.drawText(viewname, 0, textsize, viewnamepaint);
			}
			// drawEffectLine(canvas, centerY);
			break;
		case 1:// 第二象限
			canvas.drawLine(laypaiding, laypaiding, layoutwidth - laypaiding,
					laypaiding, paintxy);
			canvas.drawLine(laypaiding, laypaiding, laypaiding, layoutheight
					- laypaiding, paintxy);
			canvas.drawPoint(laypaiding, laypaiding, paintxy);
			// drawEffectLine(canvas, centerY);
			break;
		case 2:// 第三象限
			canvas.drawLine(laypaiding, laypaiding, layoutwidth - laypaiding,
					laypaiding, paintxy);
			canvas.drawLine(layoutwidth - laypaiding, laypaiding, layoutwidth
					- laypaiding, layoutheight - laypaiding, paintxy);
			canvas.drawPoint(layoutwidth - laypaiding, laypaiding, paintxy);
			// drawEffectLine(canvas, centerY);
			break;
		case 3:// 第四象限
			canvas.drawLine(laypaiding, layoutheight - laypaiding, layoutwidth
					- laypaiding, layoutheight - laypaiding, paintxy);
			canvas.drawLine(layoutwidth - laypaiding, laypaiding, layoutwidth
					- laypaiding, layoutheight - laypaiding, paintxy);
			canvas.drawPoint(layoutwidth - laypaiding, layoutheight
					- laypaiding, paintxy);
			// drawEffectLine(canvas, centerY);
			break;
		case 4:// 第一二象限
			canvas.drawLine(laypaiding, centerY, layoutwidth - laypaiding,
					centerY, paintxy);
			canvas.drawLine(laypaiding, laypaiding, laypaiding, layoutheight
					- laypaiding, paintxy);
			canvas.drawPoint(centerX, centerY, paintxy);
			// drawEffectLine(canvas, onefourY);
			// drawEffectLine(canvas, threefourY);
			break;
		case 5:// 第三四象限
			canvas.drawLine(laypaiding, centerY, layoutwidth - laypaiding,
					centerY, paintxy);
			canvas.drawLine(layoutwidth - laypaiding, laypaiding, layoutwidth
					- laypaiding, layoutheight - laypaiding, paintxy);
			canvas.drawPoint(centerX, centerY, paintxy);
			// drawEffectLine(canvas, onefourY);
			// drawEffectLine(canvas, threefourY);
			break;
		case 6:// 第一四象限
			canvas.drawLine(laypaiding, layoutheight - laypaiding, layoutwidth
					- laypaiding, layoutheight - laypaiding, paintxy);
			canvas.drawLine(centerX, laypaiding, centerX, layoutheight
					- laypaiding, paintxy);
			canvas.drawPoint(centerX, centerY, paintxy);
			// drawEffectLine(canvas, centerY);
			break;
		case 7:// 第二三象限
			canvas.drawLine(laypaiding, laypaiding, layoutwidth - laypaiding,
					laypaiding, paintxy);
			canvas.drawLine(centerX, laypaiding, centerX, layoutheight
					- laypaiding, paintxy);
			canvas.drawPoint(centerX, centerY, paintxy);
			// drawEffectLine(canvas, centerY);
			break;
		case 8:// 第一二三四象限
			canvas.drawLine(laypaiding, centerY, layoutwidth - laypaiding,
					centerY, paintxy);
			canvas.drawLine(centerX, laypaiding, centerX, layoutheight
					- laypaiding, paintxy);
			canvas.drawPoint(centerX, centerY, paintxy);
			// drawEffectLine(canvas, onefourY);
			// drawEffectLine(canvas, threefourY);
			break;
		default:
			break;
		}
	}

	/** 绘制第一条曲线的方法 */
	private void drawFirstLineMethod(Canvas canvas) {
		paintfirstline.setStyle(Paint.Style.STROKE);
		switch (firstmodle) {
		case MODLE_SIN:// 正弦
			drawSinLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		case MODLE_COS:// 余弦
			drawCosLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		case MODLE_TAN:// 正切
			drawTanLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		case MODLE_COT:// 余切
			drawCotLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		case MODLE_ARCSIN:// 反正弦
			drawArcsinLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		case MODLE_ARCCOS:// 反余弦
			drawArccosLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		case MODLE_ARCTAN:// 反正切
			drawArctanLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		case MODLE_ARCCOT:// 反余切
			drawArccotLineMethod(canvas, paintfirstline, firstcoefficient,
					firstconstant, firstoffset);
			break;
		default:
			break;
		}
	}

	/** 绘制第二条曲线的方法 */
	private void drawSecondLineMethod(Canvas canvas) {
		paintsecondline.setStyle(Paint.Style.STROKE);
		switch (secondmodle) {
		case MODLE_SIN:// 正弦
			drawSinLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		case MODLE_COS:// 余弦
			drawCosLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		case MODLE_TAN:// 正切
			drawTanLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		case MODLE_COT:// 余切
			drawCotLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		case MODLE_ARCSIN:// 反正弦
			drawArcsinLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		case MODLE_ARCCOS:// 反余弦
			drawArccosLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		case MODLE_ARCTAN:// 反正切
			drawArctanLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		case MODLE_ARCCOT:// 反余切
			drawArccotLineMethod(canvas, paintsecondline, secondcoefficient,
					secondconstant, secondoffset);
			break;
		default:
			break;
		}
	}

	/**
	 * 绘制正弦曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawSinLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		/** 横向倍数 */
		float widthbei = layoutwidth / 10;
		/** 纵向倍数 */
		float heightbei = (layoutheight - laypaiding * 2) / 4 / coefficient;
		/** 起始点X坐标 */
		float mX;
		/** 起始点Y坐标 */
		float mY;
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限
			mX = laypaiding;
			mY = threefourY;
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = threefourY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_TWO:// 第二象限
			mX = laypaiding;
			mY = onefourY;
			path.moveTo(mX, mY);
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = onefourY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_THREE:// 第三象限
			mX = laypaiding;
			mY = onefourY;
			path.moveTo(mX, mY);
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = onefourY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_FOUR:// 第四象限
			mX = laypaiding;
			mY = threefourY;
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = threefourY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限
			mX = laypaiding;
			mY = centerY;
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (int i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = centerY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限
			mX = laypaiding;
			mY = centerY;
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (int i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = centerY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限
			mX = laypaiding;
			mY = threefourY;
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = threefourY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限
			mX = laypaiding;
			mY = onefourY;
			path.moveTo(mX, mY);
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = onefourY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限
			mX = laypaiding;
			mY = centerY;
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (int i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = centerY
						- heightbei
						* (float) Math.sin(Math.PI / 2
								* (i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		default:
			break;
		}
		canvas.drawPath(path, paint);// 画手滑过的路径
	}

	/**
	 * 绘制余弦曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawCosLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限

			break;
		case XIANGXIAN_TWO:// 第二象限

			break;
		case XIANGXIAN_THREE:// 第三象限

			break;
		case XIANGXIAN_FOUR:// 第四象限

			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限

			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限

			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限

			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限

			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限

			break;
		default:
			break;
		}
	}

	/**
	 * 绘制正切曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawTanLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限

			break;
		case XIANGXIAN_TWO:// 第二象限

			break;
		case XIANGXIAN_THREE:// 第三象限

			break;
		case XIANGXIAN_FOUR:// 第四象限

			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限

			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限

			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限

			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限

			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限

			break;
		default:
			break;
		}
	}

	/**
	 * 绘制余切曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawCotLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限

			break;
		case XIANGXIAN_TWO:// 第二象限

			break;
		case XIANGXIAN_THREE:// 第三象限

			break;
		case XIANGXIAN_FOUR:// 第四象限

			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限

			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限

			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限

			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限

			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限

			break;
		default:
			break;
		}
	}

	/**
	 * 绘制反正弦曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawArcsinLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限

			break;
		case XIANGXIAN_TWO:// 第二象限

			break;
		case XIANGXIAN_THREE:// 第三象限

			break;
		case XIANGXIAN_FOUR:// 第四象限

			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限

			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限

			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限

			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限

			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限

			break;
		default:
			break;
		}
	}

	/**
	 * 绘制反余弦曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawArccosLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限

			break;
		case XIANGXIAN_TWO:// 第二象限

			break;
		case XIANGXIAN_THREE:// 第三象限

			break;
		case XIANGXIAN_FOUR:// 第四象限

			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限

			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限

			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限

			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限

			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限

			break;
		default:
			break;
		}
	}

	/**
	 * 绘制反正切曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawArctanLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		/** 横向倍数 */
		float widthbei = layoutwidth / 10;
		/** 纵向倍数 */
		float heightbei = (layoutheight - laypaiding * 2) / 4;
		/** 起始点X坐标 */
		float mX;
		/** 起始点Y坐标 */
		float mY;
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限
			mX = laypaiding;
			mY = threefourY - heightbei
					* (float) Math.atan((0 / widthbei + offset)) + constant;
			System.out.println("起始点坐标:"+(float) Math.atan((0 / widthbei + offset)) + constant);
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = threefourY - heightbei
						* (float) Math.atan((i / widthbei + offset)) + constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_TWO:// 第二象限

			break;
		case XIANGXIAN_THREE:// 第三象限

			break;
		case XIANGXIAN_FOUR:// 第四象限

			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限

			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限

			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限

			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限

			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限

			break;
		default:
			break;
		}
		canvas.drawPath(path, paint);// 画手滑过的路径
	}

	/**
	 * 绘制反余切曲线的方法
	 * 
	 * @param coefficient
	 *            系数
	 * @param constant
	 *            常量
	 * @param offset
	 *            偏移量
	 */
	private void drawArccotLineMethod(Canvas canvas, Paint paint,
			float coefficient, float constant, float offset) {
		Path path = new Path();
		/** 横向倍数 */
		float widthbei = layoutwidth / 10;
		/** 纵向倍数 */
		float heightbei = (layoutheight - laypaiding * 2) / 4 / coefficient;
		/** 起始点X坐标 */
		float mX;
		/** 起始点Y坐标 */
		float mY;
		switch (xiangxian) {
		case XIANGXIAN_ONE:// 第一象限
			mX = laypaiding;
			mY = threefourY;
			path.moveTo(mX, mY);
			/** 纵向倍数 */
			for (float i = 0; i < layoutwidth - laypaiding * 2; i++) {
				float x = laypaiding + i;
				float y = threefourY - heightbei
						* (float) Math.atan(1 / (i / widthbei + offset))
						+ constant;
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			break;
		case XIANGXIAN_TWO:// 第二象限

			break;
		case XIANGXIAN_THREE:// 第三象限

			break;
		case XIANGXIAN_FOUR:// 第四象限

			break;
		case XIANGXIAN_ONEANDTWO:// 第一二象限

			break;
		case XIANGXIAN_THREEANDFOUR:// 第三四象限

			break;
		case XIANGXIAN_ONEANDFOUR:// 第一四象限

			break;
		case XIANGXIAN_TWOANDTHREE:// 第二三象限

			break;
		case XIANGXIAN_ONETWOTHREEFOUR:// 第一二三四象限

			break;
		default:
			break;
		}
		canvas.drawPath(path, paint);// 画手滑过的路径
	}

	/** 获取第一条线的模式 0正弦函数,1与弦函数,2正切函数,3余切函数,4反正弦函数,5反余弦函数,6反正切函数,7反余切函数 */
	public int getFirstmodle() {
		return firstmodle;
	}

	/** 设置第一条曲线的模式 0正弦函数,1与弦函数,2正切函数,3余切函数,4反正弦函数,5反余弦函数,6反正切函数,7反余切函数 */
	public void setFirstmodle(int firstmodle) {
		this.firstmodle = firstmodle;
		postInvalidate();
	}

	/** 获取第二条线的模式 0正弦函数,1与弦函数,2正切函数,3余切函数,4反正弦函数,5反余弦函数,6反正切函数,7反余切函数 */
	public int getSecondmodle() {
		return secondmodle;
	}

	/** 设置第二条曲线的模式 0正弦函数,1与弦函数,2正切函数,3余切函数,4反正弦函数,5反余弦函数,6反正切函数,7反余切函数 */
	public void setSecondmodle(int secondmodle) {
		this.secondmodle = secondmodle;
		postInvalidate();
	}

	/** 获取象限 0第一象限,1第二象限,2第三象限,3第四象限,4第一二象限,5第三四象限,6第一四象限,7第二三象限,8第一二三四象限 */
	public int getXiangxian() {
		return xiangxian;
	}

	/** 设置象限 0第一象限,1第二象限,2第三象限,3第四象限,4第一二象限,5第三四象限,6第一四象限,7第二三象限,8第一二三四象限 */

	public void setXiangxian(int xiangxian) {
		this.xiangxian = xiangxian;
		postInvalidate();
	}

	/** 获取第一条曲线的颜色 */
	public int getFirstlinecolor() {
		return firstlinecolor;
	}

	/** 设置第一条曲线的颜色 */
	public void setFirstlinecolor(int firstlinecolor) {
		this.firstlinecolor = firstlinecolor;
		postInvalidate();
	}

	/** 获取第二条曲线的颜色 */
	public int getSecondlinecolor() {
		return secondlinecolor;
	}

	/** 设置第二条曲线的颜色 */
	public void setSecondlinecolor(int secondlinecolor) {
		this.secondlinecolor = secondlinecolor;
		postInvalidate();
	}

	/** 获取基准线的颜色 */
	public int getJizhunlinecolor() {
		return jizhunlinecolor;
	}

	/** 设置基准线的颜色 */
	public void setJizhunlinecolor(int jizhunlinecolor) {
		this.jizhunlinecolor = jizhunlinecolor;
		postInvalidate();
	}

	/** 获取曲线的宽度 */
	public int getLinewidth() {
		return linewidth;
	}

	/** 设置曲线的宽度 */
	public void setLinewidth(int linewidth) {
		this.linewidth = linewidth;
		postInvalidate();
	}

	/** 获取XY坐标线的宽度 */
	public int getXywidth() {
		return xywidth;
	}

	/** 设置XY坐标线的宽度 */
	public void setXywidth(int xywidth) {
		this.xywidth = xywidth;
		postInvalidate();
	}

	/** 获取XY坐标线的颜色 */
	public int getXycolor() {
		return xycolor;
	}

	/** 设置XY坐标线的颜色 */
	public void setXycolor(int xycolor) {
		this.xycolor = xycolor;
		postInvalidate();
	}

	/** 获取布局宽度 */
	public float getLayoutwidth() {
		return layoutwidth;
	}

	/** 设置布局宽度 */
	public void setLayoutwidth(float layoutwidth) {
		this.layoutwidth = layoutwidth;
		postInvalidate();
	}

	/** 获取布局高度 */
	public float getLayoutheight() {
		return layoutheight;
	}

	/** 设置布局高度 */
	public void setLayoutheight(float layoutheight) {
		this.layoutheight = layoutheight;
		postInvalidate();
	}

	/** 获取视图边距 */
	public int getLaypaiding() {
		return laypaiding;
	}

	/** 设置视图边距 */
	public void setLaypaiding(int laypaiding) {
		this.laypaiding = laypaiding;
		postInvalidate();
	}

	/** 获取第一条曲线的名称 */
	public String getFirstname() {
		return firstname;
	}

	/** 设置第一条曲线的名称 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
		postInvalidate();
	}

	/** 获取第二条曲线的名称 */
	public String getSecondname() {
		return secondname;
	}

	/** 设置第二条曲线的名称 */
	public void setSecondname(String secondname) {
		this.secondname = secondname;
		postInvalidate();
	}

	/** 获取整个View视图的名称 */
	public String getViewname() {
		return viewname;
	}

	/** 设置整个View视图的名称 */
	public void setViewname(String viewname) {
		this.viewname = viewname;
		postInvalidate();
	}

	/** 获取是否允许绘制表格线 */
	public boolean isAllowtab() {
		return allowtab;
	}

	/** 设置是否允许绘制表格线 */
	public void setAllowtab(boolean allowtab) {
		this.allowtab = allowtab;
		postInvalidate();
	}

	/** 获取横向分割数 */
	public int getHeronumber() {
		return heronumber;
	}

	/** 设置横向分割数 */
	public void setHeronumber(int heronumber) {
		this.heronumber = heronumber;
		postInvalidate();
	}

	/** 获取纵向分割数 */
	public int getVernumber() {
		return vernumber;
	}

	/** 设置纵向分割数 */
	public void setVernumber(int vernumber) {
		this.vernumber = vernumber;
		postInvalidate();
	}

	/** 获取起始数值 */
	public int getVerstartnumber() {
		return verstartnumber;
	}

	/** 设置起始数值 */
	public void setVerstartnumber(int verstartnumber) {
		this.verstartnumber = verstartnumber;
		postInvalidate();
	}

	/** 获取结束数值 */
	public int getVerendnumber() {
		return verendnumber;
	}

	/** 设置结束数值 */
	public void setVerendnumber(int verendnumber) {
		this.verendnumber = verendnumber;
		postInvalidate();
	}

	/**
	 * 设置第一条曲线参数的方法
	 * 
	 * @param firstcoefficient
	 *            系数
	 * @param firstconstant
	 *            常量
	 * @param firstoffset
	 *            偏移量
	 */
	public void setFirstParameters(float firstcoefficient, float firstconstant,
			float firstoffset) {
		this.firstcoefficient = firstcoefficient;
		this.firstconstant = firstconstant;
		this.firstoffset = firstoffset;
		postInvalidate();
	}

	/**
	 * 设置第二条曲线参数的方法
	 * 
	 * @param secondcoefficient
	 *            系数
	 * @param secondconstant
	 *            常量
	 * @param secondoffset
	 *            偏移量
	 */
	public void setSecondParameters(float secondcoefficient,
			float secondconstant, float secondoffset) {
		this.secondcoefficient = secondcoefficient;
		this.secondconstant = secondconstant;
		this.secondoffset = secondoffset;
		postInvalidate();
	}
}
