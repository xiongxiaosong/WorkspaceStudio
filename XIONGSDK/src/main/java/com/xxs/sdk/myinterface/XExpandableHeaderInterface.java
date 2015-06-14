package com.xxs.sdk.myinterface;

import android.view.View;

/**
 * 自定义二级展开列表头文件视图回调接口
 * 
 * @author xiongxs
 * @date 2014-12-24
 */
public interface XExpandableHeaderInterface {
	public static final int PINNED_HEADER_GONE = 0;
	public static final int PINNED_HEADER_VISIBLE = 1;
	public static final int PINNED_HEADER_PUSHED_UP = 2;

	/**
	 * 获取 Header 的状态
	 * 
	 * @param groupPosition
	 *            分组号
	 * @param childPosition
	 *            所在分组子序号
	 * @return <ul>
	 *         <li>0 PINNED_HEADER_GONE 隐藏</li>
	 *         <li>1 PINNED_HEADER_VISIBLE 保持可见</li>
	 *         <li>1 PINNED_HEADER_PUSHED_UP 向上滑动</li>
	 *         </ul>
	 */
	public int getTreeHeaderState(int groupPosition, int childPosition);

	/**
	 * 配置 Header, 让 Header 知道显示的内容
	 * 
	 * @param header
	 *            Header视图文件
	 * @param groupPosition
	 *            分组号
	 * @param childPosition
	 *            所在分组子序号
	 * @param alpha
	 *            透明值
	 */
	public void setTreeHeaderState(View header, int groupPosition,
                                   int childPosition, int alpha);

	/**
	 * 设置组按下的状态
	 * 
	 * @param groupPosition
	 *            分组号
	 * @param status
	 *            <ul>
	 *            <li>0 PINNED_HEADER_GONE 隐藏</li>
	 *            <li>1 PINNED_HEADER_VISIBLE 保持可见</li>
	 *            <li>1 PINNED_HEADER_PUSHED_UP 向上滑动</li>
	 *            </ul>
	 */
	public void setHeadViewClickStatus(int groupPosition, int status);

	/**
	 * 获取组按下的状态
	 * 
	 * @param groupPosition
	 *            分组号
	 * @return <ul>
	 *         <li>0 PINNED_HEADER_GONE 隐藏</li>
	 *         <li>1 PINNED_HEADER_VISIBLE 保持可见</li>
	 *         <li>1 PINNED_HEADER_PUSHED_UP 向上滑动</li>
	 *         </ul>
	 */
	public int getHeadViewClickStatus(int groupPosition);

}
