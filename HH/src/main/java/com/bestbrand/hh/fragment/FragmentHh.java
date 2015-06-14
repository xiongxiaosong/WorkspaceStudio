package com.bestbrand.hh.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.bestbrand.hh.R;
import com.bestbrand.hh.adapter.AdapterFragmentHh;
import com.bestbrand.hh.bean.BeanFragmentHhChild;
import com.bestbrand.hh.bean.BeanFragmentHhGroup;
import com.xxs.sdk.view.XExpandableView;

/**
 * 互互片段
 * 
 * @author xiongxs
 * @date 2015-04-11
 * @introduce 展示互友列表
 */
public class FragmentHh extends FragmentBase implements OnGroupExpandListener,
		OnGroupCollapseListener, OnItemLongClickListener, OnChildClickListener {
	/** 单利对象 */
	public static FragmentHh mfragment;
	/** 二级展开列表 */
	private XExpandableView xExpandableView;
	/** 一级列表数据队列 */
	private ArrayList<Object> arraygroup = new ArrayList<Object>();
	/** 二级列表数据 队列 */
	private ArrayList<ArrayList<Object>> children = new ArrayList<ArrayList<Object>>();
	/** 自定义二级列表适配器 */
	private AdapterFragmentHh adapter;

	/** 单利模式获取唯一对象 */
	public static FragmentHh getInstense() {
		if (mfragment == null) {
			mfragment = new FragmentHh();
		}
		return mfragment;
	}

	@Override
	public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_hh, null);
		viewGroup.addView(view);
		return viewGroup;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		findViewMethod(view);// 调用实例化控件的方法
	}

	/** 实例化控件的方法 */
	private void findViewMethod(View view) {
		xExpandableView = (XExpandableView) view
				.findViewById(R.id.fragment_hh_frendlist);
		xExpandableView.setHeaderView(LayoutInflater.from(mActivity).inflate(
				R.layout.item_fragment_hh_header, xExpandableView,false));
		xExpandableView.setOnChildClickListener(this);
		xExpandableView.setOnGroupExpandListener(this);
		xExpandableView.setOnGroupCollapseListener(this);
		xExpandableView.setGroupIndicator(null);
		initUserData();
	}

	/**
	 * 初始化二级列表数据
	 */
	private void initUserData() {
		// TODO Auto-generated method stub
		adapter = new AdapterFragmentHh(mActivity, arraygroup, children,
				xExpandableView);
		xExpandableView.setAdapter(adapter);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}

	private void init() {
		for (int i = 0; i < 12; i++) {
			BeanFragmentHhGroup bGroup = new BeanFragmentHhGroup();
			bGroup.setGroupname("分组            "+i);
			bGroup.setTotlefrend(20);
			bGroup.setOnlinefrend(i);
			arraygroup.add(bGroup);
			ArrayList<Object> array = new ArrayList<Object>();
			for (int j = 0; j < 20; j++) {
				BeanFragmentHhChild bChild = new BeanFragmentHhChild();
				bChild.setPhotourl("http://www.zhiyin.cn/uploadfile/2010/1124/20101124102847438.jpg");
				bChild.setName("刘亦菲");
				bChild.setOccupation("演员");
				bChild.setPhone("12345678909");
				array.add(bChild);
			}
			children.add(array);
		}
		for(int i = 0; i < adapter.getGroupCount(); i++){
			xExpandableView.collapseGroup(i);
			adapter.setHeadViewClickStatus(i, 0);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 长按监听
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 分组关闭
	 * 
	 * @param groupPosition
	 *            分组序号
	 */
	@Override
	public void onGroupCollapse(int groupPosition) {
		// TODO Auto-generated method stub

	}

	/**
	 * 分组打开
	 * 
	 * @param groupPosition
	 *            分组序号
	 */
	@Override
	public void onGroupExpand(int groupPosition) {
		// TODO Auto-generated method stub
	}

	/**
	 * 子项点击监听
	 * 
	 * @param parent
	 *            父容器控件
	 * @param view
	 *            点击的view对象
	 * @param groupPosition
	 *            分组序号
	 * @param childPosition
	 *            对应分组下的子序号
	 * @param id
	 *            序列号
	 * @return
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View view,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		return false;
	}
}
