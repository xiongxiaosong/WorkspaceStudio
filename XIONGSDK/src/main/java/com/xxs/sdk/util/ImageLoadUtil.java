package com.xxs.sdk.util;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xxs.sdk.R;
import com.xxs.sdk.app.AppContext;

/**
 * 通过Imageloader加载图片的工具类
 * 
 * @author xiongxs
 * 
 */
public class ImageLoadUtil {
	/**图片加载器*/
	private ImageLoader imageLoader;
	/**单利对象*/
	public static ImageLoadUtil iLoadUtil;
	/**单例模式获取唯一对象*/
	public static ImageLoadUtil getMethod(){
		if(iLoadUtil == null){
			iLoadUtil = new ImageLoadUtil();
		}
		return iLoadUtil;
	}
	/**构造函数*/
	public ImageLoadUtil(){
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(AppContext.mMainContext));
	}
	/**
	 * 加载网络图片的方法
	 * 
	 * @param imageView
	 *            显示图片的ImageView
	 * @param imageuri
	 *            图片地址
	 * @param imgEmptyid
	 *            显示图片为空或错误时显示的图片
	 * @param imgStubid
	 *            加载中显示的图片
	 * @param imgFailid
	 *            解码错误显示的图片
	 */
	public void loadImageMethod(ImageView imageView, String imageuri, Integer imgEmptyid,
			Integer imgStubid, Integer imgFailid) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);// 设置图片实现的编码方式
		builder.cacheInMemory(true);// 设置图片是否缓存在内存中
		builder.cacheOnDisc(true);// 设置图片是否缓存在SD卡中
		builder.showImageForEmptyUri(R.drawable.empty_image);// 设置图片为空或错误时显示的图片
		if(imgEmptyid!=null)
		builder.showImageForEmptyUri(imgEmptyid);// 设置图片为空或错误时显示的图片
		if(imgStubid!=null)
		builder.showStubImage(imgStubid);// 设置图片在下载期间显示的图片
		if(imgFailid!=null)
		builder.showImageOnFail(imgFailid);// 设置图片加载或解码过程中出现错误时显示的图片
		DisplayImageOptions mOptions = builder.build();
//		imageLoader.init(ImageLoaderConfiguration
//				.createDefault(AppContext.mMainContext));
		this.imageLoader.displayImage(imageuri, imageView, mOptions);
	}
}
