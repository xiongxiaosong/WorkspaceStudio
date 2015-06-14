package com.xxs.sdk.myinterface;

import android.view.View;

/**
 * 自定义列表头文件状态回调接口
 * 
 * @author xiongxs
 * @date 2014-12-25
 */
public interface PandHeadeListrInterface {
	/** 隐藏 */
	public static final int PINNED_HEADER_GONE = 0;
	/** 保持可见 */
	public static final int PINNED_HEADER_VISIBLE = 1;
	/** 向上滑动 */
	public static final int PINNED_HEADER_PUSHED_UP = 2;

	/**
	 * 获取列表头文件的状态
	 * 
	 * @param position
	 *            序列编号
	 * @return <ul>
	 *         <li>0 PINNED_HEADER_GONE 隐藏</li>
	 *         <li>1 PINNED_HEADER_VISIBLE 保持可见</li>
	 *         <li>2 PINNED_HEADER_PUSHED_UP 向上滑动</li>
	 *         </ul>
	 */
	public int getPinnedHeaderState(int position);

	/**
	 * 设置头文件的方法
	 * 
	 * @param header
	 *            头文件View
	 * @param position
	 *            序列号
	 * @param alpha
	 *            透明值
	 */
	public void configurePinnedHeader(View header, int position, int alpha);
}
