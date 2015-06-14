package com.xxs.sdk.linkviewpager;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * PagerContainer: A layout that displays a ViewPager with its children that are outside
 * the typical pager bounds.
 */
public class XPagerContainer extends FrameLayout implements XViewPager.OnPageChangeListener {
 
    private XViewPager mPager;
    boolean mNeedsRedraw = false;
 
    public XPagerContainer(Context context) {
        super(context);
        init();
    }
 
    public XPagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
 
    public XPagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
 
//    @SuppressLint("NewApi")
	private void init() {
        //Disable clipping of children so non-selected pages are visible
//        setClipChildren(false);
 
        //Child clipping doesn't work with hardware acceleration in Android 3.x/4.x
        //You need to set this value here if using hardware acceleration in an
        // application targeted at these releases.
//        setLayerType(FrameLayout.LAYER_TYPE_SOFTWARE, null);
    }
 
    @Override
    protected void onFinishInflate() {
        try {
            mPager = (XViewPager) getChildAt(0);
            mPager.setOnPageChangeListener(this);
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }
 
    public XViewPager getViewPager() {
        return mPager;
    }
 
    private Point mCenter = new Point();
    private Point mInitialTouch = new Point();
 
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenter.x = w / 2;
        mCenter.y = h / 2;
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //We capture any touches not already handled by the ViewPager
        // to implement scrolling from a touch outside the pager bounds.
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialTouch.x = (int)ev.getX();
                mInitialTouch.y = (int)ev.getY();
            default:
                ev.offsetLocation(mCenter.x - mInitialTouch.x, mCenter.y - mInitialTouch.y);
                break;
        }
 
        return mPager.dispatchTouchEvent(ev);
    }
 
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Force the container to redraw on scrolling.
        //Without this the outer pages render initially and then stay static
        if (mNeedsRedraw) invalidate();
    }
 
    @Override
    public void onPageSelected(int position) { }
 
    @Override
    public void onPageScrollStateChanged(int state) {
        mNeedsRedraw = (state != XViewPager.SCROLL_STATE_IDLE);
    }
}
