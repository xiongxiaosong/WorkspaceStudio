package com.xxs.sdk.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.xxs.sdk.myinterface.XExpandableHeaderInterface;
import com.xxs.sdk.view.XExpandableView;

/**
 * 自定义二级展开列表适配器
 * 
 * @author xiongxs
 * @date 2014-12-24
 * @introduce 
 *            外部继承此适配器使用需要重写getChildView、getGroupView、setTreeHeaderState三个方法，根据具体需求完成实际操作
 */
public class XExpandableViewAdapter extends BaseExpandableListAdapter implements
		XExpandableHeaderInterface {
	/** 自定义的二级展开列表接口 */
	protected XExpandableView xExpandableView;
	/** 分组头文件队列 */
	protected HashMap<Integer, Integer> groupStatusMap;
	/** 一级分组内容 */
	protected ArrayList<Object> groups;
	/** 二级列表内容 */
	protected ArrayList<ArrayList<Object>> children;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param groups
	 *            一级分组内容
	 * @param children
	 *            二级列表内容
	 * @param xExpandableView
	 *            自定义的二级展开列表控件
	 */
	public XExpandableViewAdapter(Context context, ArrayList<Object> groups,
			ArrayList<ArrayList<Object>> children,
			XExpandableView xExpandableView) {
		this.xExpandableView = xExpandableView;
		this.children = children == null ? new ArrayList<ArrayList<Object>>()
				: children;
		this.groups = groups;
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return children == null ? null : children.get(groupPosition).get(
				childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return children == null || children.size() == 0 ? 0 : children.get(
				groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return groups == null ? null : groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups == null || groups.size() == 0 ? 0 : groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	/**
	 * 继承者重写此方法实现对二级展开列表里面子视图显示内容的控制
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return null;
	}

	/**
	 * 继承者重写此方法实现对二级展开列表里面分组视图显示内容的控制
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return XExpandableHeaderInterface.PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !xExpandableView.isGroupExpanded(groupPosition)) {
			return XExpandableHeaderInterface.PINNED_HEADER_GONE;
		} else {
			return XExpandableHeaderInterface.PINNED_HEADER_VISIBLE;
		}
	}

	/**
	 * 继承者重写此方法实现对二级展开列表里面头文件视图显示内容的控制
	 */
	@Override
	public void setTreeHeaderState(View header, int groupPosition,
			int childPosition, int alpha) {

	}

	@Override
	public void setHeadViewClickStatus(int groupPosition, int status) {
		// TODO Auto-generated method stub
		if (groupStatusMap.containsKey(groupPosition)) {
			groupStatusMap.remove(groupPosition);
		}
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

}