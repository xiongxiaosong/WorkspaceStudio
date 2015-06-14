package com.xxs.sdk.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.xxs.sdk.R;
import com.xxs.sdk.app.AppContext;

/**
 * 用于TextView显示带图片的富文本
 * 
 * @author zhoupeng
 * 
 */
public class HtmlImageGetterUtil implements ImageGetter, ImageLoadingListener {

	private TextView mTextView;
	private Map<String, Drawable> mDrawableMap; // 保存加载好的图片
	private ImageLoader mImageLoader;
	private String mContent; // 文本内容
	private Drawable defaultDrawable;
	public static HtmlImageGetterUtil util;

	public static HtmlImageGetterUtil getMethod() {
		if (util == null) {
			util = new HtmlImageGetterUtil();
		}
		return util;
	}

	public HtmlImageGetterUtil() {
		mDrawableMap = new HashMap<String, Drawable>();
		this.mImageLoader = ImageLoader.getInstance();
		defaultDrawable = AppContext.mMainContext.getResources().getDrawable(
				R.drawable.empty_image);
		// 设置图片边界
		defaultDrawable.setBounds(0, 0, defaultDrawable.getIntrinsicWidth(),
				defaultDrawable.getIntrinsicHeight());
	}

	public void loadHtml(TextView textView, String content) {
		this.mContent = content;
		this.mTextView = textView;
		mTextView.setText(Html.fromHtml(mContent, this, null));
	}

	@Override
	public Drawable getDrawable(String source) {
		if (mDrawableMap.containsKey(source)) {
			return mDrawableMap.get(source);
		} else {
			mImageLoader.loadImage(source, this);
		}
		return defaultDrawable;
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {

	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		mDrawableMap.put(imageUri, defaultDrawable);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		Log.v("imageLoader", "success load:" + imageUri);
		Drawable drawable = new BitmapDrawable(
				AppContext.mMainContext.getResources(), loadedImage);
		// 设置图片边界
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		mDrawableMap.put(imageUri, drawable);
		mTextView.setText(Html.fromHtml(mContent, this, null));
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {

	}

	public void clearMap() {
		mDrawableMap.clear();
	}
}
