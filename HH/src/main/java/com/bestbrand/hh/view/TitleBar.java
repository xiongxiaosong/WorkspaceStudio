package com.bestbrand.hh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bestbrand.hh.R;

/**
 * common title bar
 * 
 * @author tangqiang
 * 
 */
public class TitleBar extends RelativeLayout {

	public static final int HIDE_ALL = 0x1;
	public static final int SHOW_LEFT = 0x2;
	public static final int SHOW_RIGHT = 0x3;
	public static final int SHOW_ALL = 0x4;

	private String mTitle;
	private int mShowFlag;
	private Drawable mLeftDrawable;
	private Drawable mRightDrawable;
	private String mRightText;
	private int mRightTextColor;

	private TextView mTitleText;
	public RelativeLayout mLeftBtn;
	public RelativeLayout mRightBtn;
	private ImageView mLeftImage;
	private ImageView mRightImage;
	private TextView mRightBtnText;
	private View view_pop;
	private Context mContext;

	public TitleBar(Context context) {
		this(context, null);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.titlebar);
			mTitle = a.getString(R.styleable.titlebar_titleText);
			mShowFlag = a.getInt(R.styleable.titlebar_showtype, SHOW_LEFT);
			mLeftDrawable = a.getDrawable(R.styleable.titlebar_leftDrawable);
			mRightDrawable = a.getDrawable(R.styleable.titlebar_rightDrawable);
			mRightText = a.getString(R.styleable.titlebar_rightText);
			mRightTextColor = a.getColor(R.styleable.titlebar_rightTextColor,
					0xFF000000);
			a.recycle();
		}
		initialize(mContext);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	public void initialize(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_title_bar, this);
		mTitleText = (TextView) view.findViewById(R.id.title_name_tv);
		mTitleText.setText(mTitle);
		mLeftBtn = (RelativeLayout) view.findViewById(R.id.title_left_btn);
		mRightBtn = (RelativeLayout) view.findViewById(R.id.title_right_btn);
		mLeftImage = (ImageView) view.findViewById(R.id.titlebar_left_image);
		mRightImage = (ImageView) view.findViewById(R.id.titlebar_right_image);
		mRightBtnText = (TextView) view.findViewById(R.id.title_right_text);
		mRightBtnText.setTextColor(mRightTextColor);
		controlShow(mShowFlag);
	}

	public void controlShow(int showFlag) {
		switch (showFlag) {
		case SHOW_LEFT:
			mLeftBtn.setVisibility(View.VISIBLE);
			mRightBtn.setVisibility(View.INVISIBLE);
			if (mLeftDrawable != null) {
				mLeftImage.setImageDrawable(mLeftDrawable);
			}
			break;
		case SHOW_RIGHT:
			mRightBtn.setVisibility(View.VISIBLE);
			mLeftBtn.setVisibility(View.INVISIBLE);
			if (mRightDrawable != null) {
				mRightImage.setVisibility(View.VISIBLE);
				mRightImage.setImageDrawable(mRightDrawable);
				mRightBtnText.setVisibility(View.GONE);
			} else if (!TextUtils.isEmpty(mRightText)) {
				mRightBtnText.setVisibility(View.VISIBLE);
				mRightBtnText.setText(mRightText);
				mRightImage.setVisibility(View.GONE);
			}
			break;

		case HIDE_ALL:
			mLeftBtn.setVisibility(View.INVISIBLE);
			mRightBtn.setVisibility(View.INVISIBLE);
			break;

		case SHOW_ALL:
			mLeftBtn.setVisibility(View.VISIBLE);
			mRightBtn.setVisibility(View.VISIBLE);
			if (mLeftDrawable != null) {
				mLeftImage.setImageDrawable(mLeftDrawable);
			}
			if (mRightDrawable != null) {
				mRightImage.setVisibility(View.VISIBLE);
				mRightImage.setImageDrawable(mRightDrawable);
				mRightBtnText.setVisibility(View.GONE);
			} else if (!TextUtils.isEmpty(mRightText)) {
				mRightBtnText.setVisibility(View.VISIBLE);
				mRightBtnText.setText(mRightText);
				mRightImage.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	public void setTitle(CharSequence title) {
		mTitleText.setText(title);
	}

	public void setleftClickListener(OnClickListener l) {
		mLeftBtn.setOnClickListener(l);
	}

	public void setRightClickListener(OnClickListener l) {
		mRightBtn.setOnClickListener(l);
	}

	public void setRightButtonText(String text) {
		mRightBtnText.setText(text);
	}

	public View getPopView() {
		return view_pop;
	}

	/**
	 * 设置TitleBar样式的方法
	 * 
	 * @param leftdDrawable
	 *            左边图片
	 * @param rightDrawable
	 *            右边图片
	 * @param title
	 *            标题文字
	 * @param right
	 *            右边文字
	 * @param type
	 *            显示类型
	 *            <ul>
	 *            <li>1 不显示左右两边按钮</li>
	 *            <li>2 显示左边按钮</li>
	 *            <li>3 显示右边按钮</li>
	 *            <li>4 同时显示左右两边按钮</li>
	 *            </ul>
	 */
	public void setTitleStyleMethod(Drawable leftdDrawable,
			Drawable rightDrawable, String title, String right, int type) {
		this.mLeftDrawable = leftdDrawable;
		this.mRightDrawable = rightDrawable;
		this.mTitle = title;
		this.mRightText = right;
		this.mShowFlag = type;
		// initialize(mContext);
		controlShow(mShowFlag);
		// postInvalidate();
		if (mLeftDrawable != null) {
			mLeftImage.setImageDrawable(mLeftDrawable);
		}
		if (mRightDrawable != null) {
			mLeftImage.setImageDrawable(mRightDrawable);
		}
		if (mRightText != null) {
			setRightButtonText(right);
		}
		if (title != null) {
			setTitle(title);
		}
	}

	/**
	 * 判断左边布局是否为空
	 * 
	 * @return 为空返回true 否则返回false
	 */
	public boolean isLeftNull() {
		return mLeftDrawable == null;
	}

	/**
	 * 判断右边布局是否为空
	 * 
	 * @return 为空返回true 否则返回false
	 */
	public boolean isRightNull() {
		if (mRightDrawable == null && TextUtils.isEmpty(mRightText)) {
			return true;
		}
		return false;
	}

	/** 设置右边按钮文字颜色 */
	public void setmRightTextColor(int mRightTextColor) {
		this.mRightTextColor = mRightTextColor;
		mRightBtnText.setTextColor(mRightTextColor);
	}

	/**
	 * 设置右边按钮的背景颜色
	 * 
	 * @param mRightTextBackColor
	 */
	public void setMRightTextBackColor(int mRightTextBackColor) {
		this.mRightBtnText.setBackgroundColor(mRightTextBackColor);
	}

	/**
	 * 设置右边按钮的背景
	 * 
	 * @param mRightTextBackdrawble
	 */
	public void setMRightTextBackDrawble(int mRightTextBackdrawble) {
		this.mRightBtnText.setBackgroundResource(mRightTextBackdrawble);
	}

	/**
	 * 设置右边文字的大小
	 * 
	 * @param mRightTextSize
	 */
	public void setmRightTextSize(float mRightTextSize) {
		mRightBtnText.setTextSize(mRightTextSize);
	}

	/**
	 * 设置标题文字的大小
	 * 
	 * @param mTitleTextSize
	 */
	public void setmTitleTextSize(float mTitleTextSize) {
		mTitleText.setTextSize(mTitleTextSize);
	}

	/**
	 * 隐藏右边按钮
	 */
	public void hideTitleRight() {
		this.mRightBtnText.setVisibility(View.GONE);
	}
}