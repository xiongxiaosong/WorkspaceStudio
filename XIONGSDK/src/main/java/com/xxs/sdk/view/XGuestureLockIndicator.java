package com.xxs.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.xxs.sdk.R;

/**
 * 手势密码图案提示
 *
 * @author xiongxs
 * @date 2015-06-11
 */
public class XGuestureLockIndicator extends ViewGroup {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 正常图片
     */
    private Drawable drawableNormal;
    /**
     * 选中图片
     */
    private Drawable drawableSelected;
    /**
     * 布局宽度
     */
    private int laywidth;
    /**
     * 布局高度
     */
    private int layheight;

    public XGuestureLockIndicator(Context context) {
        this(context, null);
    }

    public XGuestureLockIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XGuestureLockIndicator(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setWillNotDraw(false);
        setCustomAttributes(attrs);// 调用初始化
        addChildMethod();// 调用添加子View的方法
    }

    /**
     * 初始化一些属性值的方法
     */
    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs,
                R.styleable.GestureLock);
        drawableNormal = a.getDrawable(R.styleable.GestureLock_drawablenormal);
        drawableSelected = a
                .getDrawable(R.styleable.GestureLock_drawableselected);
    }

    /**
     * 添加子View的方法
     */
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
        }
    }

    /**
     * 清空提示状态的方法
     */
    public void clearMethod() {
        for (int i = 0; i < getChildCount(); i++) {
            ImageView mView = (ImageView) getChildAt(i);
            mView.setImageDrawable(drawableNormal);
            mView.setScaleType(ScaleType.FIT_CENTER);
        }
    }

    /**
     * 设置选中图案提示的方法
     *
     * @param locknum 封装选中项的字符串
     */
    public void setSelectedMethod(String locknum) {
        for (int i = 0; i < locknum.length(); i++) {
            int position = Integer.valueOf(locknum.substring(i, i + 1));
            ((ImageView) getChildAt(position))
                    .setImageDrawable(drawableSelected);
        }
    }
}
