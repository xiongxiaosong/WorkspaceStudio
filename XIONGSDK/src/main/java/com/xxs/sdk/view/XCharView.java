package com.xxs.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xxs.sdk.R;
import com.xxs.sdk.util.MathUtil;
import com.xxs.sdk.util.TransformUtil;

/**
 * 自定义图标视图
 * 
 * @author xiongxs
 * @date 2014-10-22
 */
/**
 * @author Administrator
 * 
 */
public class XCharView extends View {
	/** 分析图模式 */
	private int xcharmode;
	/** 纵向刻度单位 */
	private int xcharverticlunitmode;
	/** 折线图 */
	private static final int XCHAR_MODE_LINE = 0;
	/** 柱形图 */
	private static final int XCHAR_MODE_COLUM = 1;
	/** 饼状图 */
	private static final int XCHAR_MODE_PIE = 2;
	/** 数值模式 */
	private static final int VERTICL_UNITE_NUMBER = 0;
	/** 百分比模式 */
	private static final int VERTICL_UNITE_PERCENT = 1;
	/** 默认颜色 */
	private static final int DEFULT_COLOR = 0xff000000;
	/** 线条数 */
	private int linenumber;
	/** 柱形数 */
	private int columnnumber;
	/** 饼块数 */
	private int pienumber;
	/** 横向分割分数 */
	private int horizontalnumber;
	/** 纵向分割分数 */
	private int verticlnumber;
	/** 是否允许画出分割线 */
	private boolean allowdividingline;
	/** 是否允许在饼状图中心添加小圆 */
	private boolean allowcentercircle;
	/** 饼状图中心小圆显示的文字内容 */
	private String centercircletext;
	/** 上下文 */
	private Context mContext;
	/** 布局宽度 */
	private float layoutwidth;
	/** 布局高度 */
	private float layoutheight;
	/** 画笔 */
	private Paint paint;
	/** 圆心 坐标 */
	private float centerX, centerY;
	/** 圆弧半径 */
	private float recfArcRaid;
	/** 弧形的区域 */
	private RectF recfArc;
	/** 折线图数据 */
	private int[][] linenumberarray;
	/** 折线图点集的数组 */
	private float[][] linepointarray;
	/** 则线图第二名称数组 */
	private String[] linestringarray;
	/** 柱形图数据 */
	private int[][] columnumberarray;
	/** 饼状图数据 */
	private int[] pienumberarray;
	/** 颜色数据 折线图为各个线条颜色，柱形图为各个柱形颜色，饼状图为各个饼块颜色 */
	private int[] colorarray;
	/** 项目名称,代表各条线/各柱形/各饼块的名称 */
	private String[] names;
	/** 横向单位名称 例如周几，日期，时间等 */
	private String[] horizontalnames;
	/** 单位长宽 */
	private float onewidth, oneheight;
	/** 单位高度代表的值 */
	private int oneheightvalue;
	/** 图表名称 */
	private String xcharviewname;
	/** 默认最大值 */
	private int defultmaxvalue;
	/** 是否允许点击 */
	private boolean ifallowtouch;
	private Canvas mCanvas;
	/** 图标名称文字颜色 */
	private int xcharviewnamecolor;

	public XCharView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
	}

	public XCharView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
	}

	public XCharView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWillNotDraw(false);
		setCustomAttributes(attrs);
	}

	/** 初始化一些属性值的方法 */
	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.XcharView);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			if (attr == R.styleable.XcharView_xcharmode) {// 得到模式
				xcharmode = a.getInt(attr, -1);
			} else if (attr == R.styleable.XcharView_xcharlinenumber) {// 得到折线条数
				linenumber = a.getInt(attr, -1);
			} else if (attr == R.styleable.XcharView_xcharcolumnnumber) {// 得到柱形个数
				columnnumber = a.getInt(attr, -1);
			} else if (attr == R.styleable.XcharView_xcharpienumber) {// 得到饼状图分割块数
				pienumber = a.getInt(attr, -1);
			} else if (attr == R.styleable.XcharView_xcharhorizontalnumber) {// 得到横向分割列表数
				horizontalnumber = a.getInt(attr, -1);
			} else if (attr == R.styleable.XcharView_xcharverticlnumber) {// 得到纵向分割列表数
				verticlnumber = a.getInt(attr, -1);
			} else if (attr == R.styleable.XcharView_allowdividingline) {// 得到是否绘制表格线
				allowdividingline = a.getBoolean(attr, false);
			} else if (attr == R.styleable.XcharView_allowcentercircle) {// 得到是否允许绘制中心小圆
				allowcentercircle = a.getBoolean(attr, false);
			} else if (attr == R.styleable.XcharView_centercircletext) {// 得到中心小圆的文字
				centercircletext = a.getString(attr);
			} else if (attr == R.styleable.XcharView_xcharverticlunitmode) {// 得到纵向单位模式
				xcharverticlunitmode = a.getInt(attr, 0);
			} else if (attr == R.styleable.XcharView_xcharviewname) {// 得到图标名称
				xcharviewname = a.getString(attr);
			} else if (attr == R.styleable.XcharView_defultmaxvalue) {// 得到最大默认值
				defultmaxvalue = a.getInt(attr, -1);
			} else if (attr == R.styleable.XcharView_xcharviewnamecolor) {// 得到图表名称颜色
				xcharviewnamecolor = a.getColor(attr, DEFULT_COLOR);
			}
		}
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
		// TODO Auto-generated method stub
		layoutwidth = r - l;
		layoutheight = b - t;
		centerX = layoutwidth / 2;// 得到圆心坐标
		centerY = Math.min(layoutwidth, layoutheight) / 3;
		recfArcRaid = Math.min(layoutwidth, layoutheight) / 3;
		oneheight = (layoutheight - TransformUtil.dip2px(48)) / (verticlnumber - 1);
		switch (xcharverticlunitmode) {
		case VERTICL_UNITE_NUMBER:// 数值
			onewidth = (layoutwidth - TransformUtil.dip2px(48)) / (horizontalnumber - 1);
			break;
		case VERTICL_UNITE_PERCENT:// 百分比
			onewidth = (layoutwidth - TransformUtil.dip2px(60)) / (horizontalnumber - 1);
			break;
		default:
			break;
		}
		intMethod();
	}

	/** 初始化的方法 */
	private void intMethod() {
		paint = new Paint();
		paint.setStrokeWidth(TransformUtil.dip2px(1));
		paint.setTextSize(TransformUtil.dip2px(10));
		paint.setAntiAlias(true);// 设置消除锯齿
		recfArc = new RectF(centerX - recfArcRaid, centerY - recfArcRaid, centerX + recfArcRaid, centerY + recfArcRaid);
	}

	private float secondX, secondY;
	private int secondindex;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (ifallowtouch) {
				float x = event.getX();
				float y = event.getY();
				Paint painttost = new Paint();
				painttost.setColor(Color.BLACK);
				painttost.setTextSize(TransformUtil.dip2px(10));
				for (int i = 0; i < linepointarray.length; i++) {
					if ((x - TransformUtil.dip2px(10)) < linepointarray[i][0]
							&& (x + TransformUtil.dip2px(10)) > linepointarray[i][0]
							&& (y - TransformUtil.dip2px(10)) < linepointarray[i][1]
							&& (y + TransformUtil.dip2px(10)) > linepointarray[i][1]) {
						secondX = linepointarray[i][0];
						secondY = linepointarray[i][1];
						secondindex = i;
						postInvalidate();
						break;
					}
				}
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mCanvas = canvas;
		drawViewMethod(canvas);
		if (ifallowtouch)
			drawLinesecondText(canvas);
	}

	private void drawLinesecondText(Canvas canvas) {
		Paint painttost = new Paint();
		painttost.setColor(Color.BLACK);
		painttost.setTextSize(TransformUtil.dip2px(10));
		canvas.drawText(linestringarray[secondindex], secondX, secondY, painttost);
	}

	/** 真正绘制视图的方法 */
	private void drawViewMethod(Canvas canvas) {
		switch (xcharmode) {
		case XCHAR_MODE_LINE:// 折线图
			drawLineMethod(canvas);
			break;
		case XCHAR_MODE_COLUM:// 柱形图
			drawColumMethod(canvas);
			break;
		case XCHAR_MODE_PIE:// 饼状图
			drawPieMethod(canvas);
			break;
		default:
			break;
		}
	}

	/** 绘制折线图的方法 */
	private void drawLineMethod(Canvas canvas) {
		drawDivLineMethod(canvas);
	}

	/** 绘制柱形图的方法 */
	private void drawColumMethod(Canvas canvas) {
		// drawDivLineMethod(canvas);
	}

	/** 绘制折线图的方法的方法 */
	private void drawDivLineMethod(Canvas canvas) {
		paint.setShader(null);
		paint.setColor(getResources().getColor(R.color.huise));
		float startX = TransformUtil.dip2px(48);
		switch (xcharverticlunitmode) {
		case VERTICL_UNITE_NUMBER:// 数值
			startX = TransformUtil.dip2px(24);
			break;
		case VERTICL_UNITE_PERCENT:// 百分比
			startX = TransformUtil.dip2px(36);
			break;
		default:
			break;
		}
		float startY = layoutheight - TransformUtil.dip2px(24);
		float endX = layoutwidth - TransformUtil.dip2px(24);
		float endY = TransformUtil.dip2px(24);
		if (!TextUtils.isEmpty(xcharviewname)) {
			paint.setColor(xcharviewnamecolor);
			canvas.drawText(xcharviewname, startX, endY - TransformUtil.dip2px(10), paint);
		}
		paint.setColor(getResources().getColor(R.color.huise));
		// 下面是绘制边框以及刻度标尺
		canvas.drawLine(startX, startY, endX, startY, paint);
		canvas.drawLine(startX, startY, startX, endY, paint);
		for (int i = 1; i < horizontalnumber - 1; i++) {
			canvas.drawCircle(startX + onewidth * i, startY, TransformUtil.dip2px(2), paint);
		}
		for (int i = 1; i < verticlnumber - 1; i++) {
			canvas.drawCircle(startX, startY - oneheight * i, TransformUtil.dip2px(2), paint);
		}
		if (horizontalnames != null) {
			for (int i = 0; i < horizontalnames.length; i++) {
				if (i == horizontalnames.length - 1) {
					canvas.drawText(horizontalnames[i], startX + onewidth * i - paint.measureText(horizontalnames[i]),
							startY + TransformUtil.dip2px(12), paint);
				} else if (i == 0) {
					canvas.drawText(horizontalnames[i], startX + onewidth * i, startY + TransformUtil.dip2px(12), paint);
				} else {
					canvas.drawText(horizontalnames[i], startX + onewidth * i - paint.measureText(horizontalnames[i])
							/ 2, startY + TransformUtil.dip2px(12), paint);
				}
			}
		}
		if (allowdividingline) {// 绘制表格
			canvas.drawLine(startX, endY, endX, endY, paint);
			canvas.drawLine(endX, startY, endX, endY, paint);
			float pts[] = new float[(verticlnumber - 2) * 4 + (horizontalnumber - 2) * 4];
			for (int i = 1; i < horizontalnumber - 1; i++) {
				pts[i * 4 - 4] = startX + onewidth * i;
				pts[i * 4 - 3] = endY;
				pts[i * 4 - 2] = startX + onewidth * i;
				pts[i * 4 - 1] = startY;
			}
			for (int i = 1; i < verticlnumber - 1; i++) {
				pts[(horizontalnumber - 2) * 4 + i * 4 - 4] = endX;
				pts[(horizontalnumber - 2) * 4 + i * 4 - 3] = startY - oneheight * i;
				pts[(horizontalnumber - 2) * 4 + i * 4 - 2] = startX;
				pts[(horizontalnumber - 2) * 4 + i * 4 - 1] = startY - oneheight * i;
			}
			canvas.drawLines(pts, paint);
		}
		/** 渐变器 */
		LinearGradient charShader = new LinearGradient(centerX, 0, centerX, layoutheight, new int[] {
				getResources().getColor(R.color.charline_hongse), getResources().getColor(R.color.charline_chengse),
				getResources().getColor(R.color.charline_lvse) }, null, Shader.TileMode.CLAMP);
		paint.setShader(charShader);
		int maxnum = 0;// 最大数据值
		if (defultmaxvalue > 0) {
			maxnum = defultmaxvalue;
		} else {
			if (linenumberarray != null) {
				for (int i = 0; i < linenumberarray.length; i++) {
					for (int j = 0; j < linenumberarray[i].length; j++) {
						if (maxnum < linenumberarray[i][j]) {
							maxnum = linenumberarray[i][j];
						}
					}
				}
			}
			if (columnumberarray != null) {
				for (int i = 0; i < columnumberarray.length; i++) {
					for (int j = 0; j < columnumberarray[i].length; j++) {
						if (maxnum < columnumberarray[i][j]) {
							maxnum = columnumberarray[i][j];
						}
					}
				}
			}
		}
		if (maxnum <= 0) {
			maxnum = 10;
		}
		if (maxnum % (verticlnumber - 1) == 0) {// 计算单位高度的值
			oneheightvalue = maxnum / (verticlnumber - 1);
		} else {
			oneheightvalue = (maxnum / (verticlnumber - 1) * (verticlnumber - 1) + (verticlnumber - 1))
					/ (verticlnumber - 1);
		}
		switch (xcharverticlunitmode) {
		case VERTICL_UNITE_NUMBER:// 数值
			for (int i = 0; i < verticlnumber; i++) {// 绘制纵向刻度值
				canvas.drawText(oneheightvalue * i + "", startX - paint.measureText(oneheightvalue * i + "")
						- TransformUtil.dip2px(2), startY - oneheight * i + TransformUtil.dip2px(4), paint);
			}
			break;
		case VERTICL_UNITE_PERCENT:// 百分比
			for (int i = 0; i < verticlnumber; i++) {// 绘制纵向刻度值
				float zhi = 0f;
				if (oneheightvalue > 0) {
					if (maxnum > 100) {
						zhi = oneheightvalue * i * 100 / (oneheightvalue * (verticlnumber - 1));
					} else {
						zhi = oneheightvalue * i * maxnum / (oneheightvalue * (verticlnumber - 1));
					}
				} else {
					zhi = i * 100 / 100f;
				}
				canvas.drawText(Math.round(zhi) + "%", startX - paint.measureText(Math.round(zhi) + "%")
						- TransformUtil.dip2px(2), startY - oneheight * i + TransformUtil.dip2px(4), paint);
			}
			break;
		default:
			break;
		}

		float fendX = endX;
		// 绘制具体线条的值
		if (linenumberarray != null) {
			for (int i = 0; i < linenumber; i++) {
				float pts[] = new float[(linenumberarray[i].length) * 4];
				paint.setColor(colorarray[i]);
				pts[0] = startX;
				pts[1] = startY - linenumberarray[i][0] / (float) oneheightvalue * oneheight;
				for (int j = 0; j < linenumberarray[i].length; j++) {
					if (j < linenumberarray[i].length - 1) {
						pts[j * 4 + 2] = startX + onewidth * j;
						pts[j * 4 + 3] = startY - linenumberarray[i][j] / (float) oneheightvalue * oneheight;
						pts[j * 4 + 4] = startX + onewidth * j;
						pts[j * 4 + 5] = startY - linenumberarray[i][j] / (float) oneheightvalue * oneheight;
					} else {
						pts[j * 4 + 2] = startX + onewidth * j;
						pts[j * 4 + 3] = startY - linenumberarray[i][j] / (float) oneheightvalue * oneheight;
					}
				}
				canvas.drawLines(pts, paint);
				linepointarray = new float[linenumberarray[i].length][2];
				for (int j = 0; j < linenumberarray[i].length; j++) {
					paint.setShader(null);
					canvas.drawCircle(startX + onewidth * j, startY - linenumberarray[i][j] / (float) oneheightvalue
							* oneheight, TransformUtil.dip2px(3), paint);
					paint.setColor(Color.WHITE);
					float[] point = new float[2];
					point[0] = startX + onewidth * j;
					point[1] = startY - linenumberarray[i][j] / (float) oneheightvalue * oneheight;
					linepointarray[j] = point;
					canvas.drawCircle(startX + onewidth * j, startY - linenumberarray[i][j] / (float) oneheightvalue
							* oneheight, TransformUtil.dip2px(2), paint);
					paint.setColor(colorarray[i]);
				}
				// 绘制项目名称
				if (names != null && names.length > 0) {
					if (i % 2 == 0) {
						canvas.drawCircle(endX - paint.measureText(names[i]) - TransformUtil.dip2px(5), endY - i % 2
								* TransformUtil.dip2px(10) - TransformUtil.dip2px(5), TransformUtil.dip2px(3), paint);
						paint.setColor(Color.WHITE);
						canvas.drawCircle(endX - paint.measureText(names[i]) - TransformUtil.dip2px(5), endY - i % 2
								* TransformUtil.dip2px(10) - TransformUtil.dip2px(5), TransformUtil.dip2px(2), paint);
						paint.setColor(colorarray[i]);
						canvas.drawText(names[i], endX - paint.measureText(names[i]),
								endY - i % 2 * TransformUtil.dip2px(10) - TransformUtil.dip2px(1), paint);
						if (fendX > endX - paint.measureText(names[i]) - TransformUtil.dip2px(10)) {
							fendX = endX - paint.measureText(names[i]) - TransformUtil.dip2px(10);
						}
					} else {
						canvas.drawCircle(endX - paint.measureText(names[i]) - TransformUtil.dip2px(5), endY - i % 2
								* TransformUtil.dip2px(10) - TransformUtil.dip2px(5), TransformUtil.dip2px(3), paint);
						paint.setColor(Color.WHITE);
						canvas.drawCircle(endX - paint.measureText(names[i]) - TransformUtil.dip2px(5), endY - i % 2
								* TransformUtil.dip2px(10) - TransformUtil.dip2px(5), TransformUtil.dip2px(2), paint);
						paint.setColor(colorarray[i]);
						canvas.drawText(names[i], endX - paint.measureText(names[i]),
								endY - i % 2 * TransformUtil.dip2px(10) - TransformUtil.dip2px(1), paint);
						if (fendX > endX - paint.measureText(names[i]) - TransformUtil.dip2px(10)) {
							fendX = endX - paint.measureText(names[i]) - TransformUtil.dip2px(10);
						}
						endX = fendX;
					}
				}
			}
		}
	}

	/** 绘制饼状图的方法 */
	private void drawPieMethod(Canvas canvas) {
		if (!TextUtils.isEmpty(xcharviewname)) {
			paint.setColor(xcharviewnamecolor);
			canvas.drawText(xcharviewname, centerX - recfArcRaid - paint.measureText(xcharviewname), centerY
					- recfArcRaid + TransformUtil.dip2px(10), paint);
		}
		paint.setColor(getResources().getColor(R.color.huise));
		if (pienumberarray != null) {
			int sum = 0;
			for (int i = 0; i < pienumberarray.length; i++) {
				sum += pienumberarray[i];
			}
			float startangle = 0;
			float sweepAngle = 0;
			float minAngle = 0;
			for (int i = 0; i < pienumber; i++) {
				paint.setColor(colorarray[i]);
				sweepAngle = pienumberarray[i] * 360 / (float) sum;
				canvas.drawArc(recfArc, startangle, sweepAngle, true, paint);
				minAngle = startangle + sweepAngle / 2;
				startangle += sweepAngle;
			}
			startangle = 0;
			sweepAngle = 0;
			minAngle = 0;
			for (int i = 0; i < pienumber; i++) {
				sweepAngle = pienumberarray[i] * 360 / (float) sum;
				minAngle = startangle + sweepAngle / 2;
				startangle += sweepAngle;
				paint.setColor(getResources().getColor(R.color.baise));
				if (minAngle > 0 && minAngle <= 90) {
					if (minAngle > 0 && minAngle <= 45) {
						String text = MathUtil.saveFloatPointMethod(pienumberarray[i] * 100 / (float) sum, 1) + "%";
						canvas.drawText(
								text,
								centerX - paint.measureText(text)
										+ (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)), centerY
										+ (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI)), paint);
					} else {
						String text = MathUtil.saveFloatPointMethod(pienumberarray[i] * 100 / (float) sum, 1) + "%";
						canvas.drawText(
								text,
								centerX - paint.measureText(text) / 3 * 2
										+ (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)),
								centerY
										+ (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI) - TransformUtil
												.dip2px(5)), paint);
					}
				} else if (minAngle > 90 && minAngle <= 180) {
					if (minAngle > 90 && minAngle <= 135) {
						String text = MathUtil.saveFloatPointMethod(pienumberarray[i] * 100 / (float) sum, 1) + "%";
						canvas.drawText(
								text,
								centerX - paint.measureText(text) / 3
										+ (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)),
								centerY
										+ (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI) - TransformUtil
												.dip2px(5)), paint);
					} else {
						canvas.drawText(MathUtil.saveFloatPointMethod(pienumberarray[i] * 100 / (float) sum, 1) + "%",
								centerX + (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)), centerY
										+ (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI)), paint);
					}
				} else if (minAngle > 180 && minAngle <= 270) {
					if (minAngle > 180 && minAngle <= 225) {
						canvas.drawText(
								MathUtil.saveFloatPointMethod(pienumberarray[i] * 100 / (float) sum, 1) + "%",
								centerX + (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)),
								centerY + (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI))
										+ TransformUtil.dip2px(6), paint);
					} else {
						canvas.drawText(
								MathUtil.saveFloatPointMethod(pienumberarray[i] * 100 / (float) sum, 1) + "%",
								centerX + (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)),
								centerY + (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI))
										+ TransformUtil.dip2px(10), paint);
					}
				} else if (minAngle > 270 && minAngle <= 360) {
					String text = MathUtil.saveFloatPointMethod(pienumberarray[i] * 100 / (float) sum, 1) + "%";
					if (minAngle > 270 && minAngle <= 315) {
						canvas.drawText(
								text,
								centerX - paint.measureText(text)
										+ (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)),
								centerY + (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI))
										+ TransformUtil.dip2px(10), paint);
					} else {
						canvas.drawText(
								text,
								centerX - paint.measureText(text)
										+ (float) (recfArcRaid * Math.cos(minAngle / 180 * Math.PI)),
								centerY + (float) (recfArcRaid * Math.sin(minAngle / 180 * Math.PI))
										+ TransformUtil.dip2px(6), paint);
					}
				}

			}
		}
		// 绘制中心小圆
		if (allowcentercircle) {
			canvas.drawCircle(centerX, centerY, recfArcRaid / 2, paint);
			paint.setColor(getResources().getColor(R.color.huise));
			canvas.drawText(centercircletext, centerX - centercircletext.length() * TransformUtil.dip2px(10) / 2,
					centerY + TransformUtil.dip2px(10) / 2, paint);
		}
		// 绘制项目名称
		float startX = centerX - recfArcRaid
				- (TextUtils.isEmpty(xcharviewname) ? 0 : paint.measureText(xcharviewname));
		float startY = centerY + recfArcRaid;
		float fendX = startX;
		if (names != null && names.length > 0) {
			for (int i = 0; i < names.length; i++) {
				paint.setColor(colorarray[i]);
				if (i % 2 == 0) {
					canvas.drawCircle(startX, startY + i % 2 * TransformUtil.dip2px(10) + TransformUtil.dip2px(7),
							TransformUtil.dip2px(3), paint);
					canvas.drawText(names[i], startX + TransformUtil.dip2px(5),
							startY + i % 2 * TransformUtil.dip2px(10) + TransformUtil.dip2px(11), paint);
					if (fendX < startX + names[i].length() * TransformUtil.dip2px(10) + TransformUtil.dip2px(10)) {
						fendX = startX + names[i].length() * TransformUtil.dip2px(10) + TransformUtil.dip2px(10);
					}
				} else {
					canvas.drawCircle(startX, startY + i % 2 * TransformUtil.dip2px(10) + TransformUtil.dip2px(7),
							TransformUtil.dip2px(3), paint);
					canvas.drawText(names[i], startX + TransformUtil.dip2px(5),
							startY + i % 2 * TransformUtil.dip2px(10) + TransformUtil.dip2px(11), paint);
					if (fendX < startX + names[i].length() * TransformUtil.dip2px(10) + TransformUtil.dip2px(10)) {
						fendX = startX + names[i].length() * TransformUtil.dip2px(10) + TransformUtil.dip2px(10);
					}
					startX = fendX;
				}
			}
		}

	}

	/** 设置折线图数据 */
	public void setLinenumberarray(int[][] linenumberarray) {
		this.linenumberarray = linenumberarray;
	}

	/** 设置柱形图数据 */
	public void setColumnumberarray(int[][] columnumberarray) {
		this.columnumberarray = columnumberarray;
	}

	/** 设置饼状图数据 */
	public void setPienumberarray(int[] pienumberarray) {
		this.pienumberarray = pienumberarray;
	}

	/** 设置颜色数据 */
	public void setColorarray(int[] colorarray) {
		this.colorarray = colorarray;
	}

	/** 设置项目名称 代表各线条、柱形、饼块的含义 */
	public void setNames(String[] names) {
		this.names = names;
	}

	/** 设置横向单位名称 横向的刻度名称 */
	public void setHorizontalnames(String[] horizontalnames) {
		this.horizontalnames = horizontalnames;
	}

	/** 设置饼状图个数 */
	public void setPienumber(int pienumber) {
		this.pienumber = pienumber;
	}

	/** 设置第二名称 */
	public void setLinestringarray(String[] linestringarray) {
		this.linestringarray = linestringarray;
	}

	/** 设置是否允许点击事件 */
	public void setIfallowtouch(boolean ifallowtouch) {
		this.ifallowtouch = ifallowtouch;
	}

}
