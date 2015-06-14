package com.xxs.sdk.adapter;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.xxs.sdk.myinterface.PandHeadeListrInterface;
import com.xxs.sdk.view.PanddHeaderListView;

/**
 * 自定义的PandHeaderList适配器
 * 
 * @author xiongxs
 * @date 2014-12-26
 * @introduce 外部继承此适配器需要重写getView、configurePinnedHeader、两个方法 ，根据具体需求实现逻辑
 * @注意 ①如果配合SideBar使用需要重写getPositionForsideSwction方法 ②最好将数据序列根据英文字母进行排序
 */
public class PandHeaderListAdapter extends BaseAdapter implements
		SectionIndexer, PandHeadeListrInterface, OnScrollListener {
	private int mLocationPosition = -1;
	/** 数据对象集合 */
	private ArrayList<?> mDatas;
	/** 首字母集合 */
	private ArrayList<?> mFriendsSections;
	/** 首字母所在序号集合 */
	private ArrayList<Integer> mFriendsPositions;

	public PandHeaderListAdapter(Context context, ArrayList<?> datas,
			ArrayList<?> friendsSections, ArrayList<Integer> friendsPositions) {
		// TODO Auto-generated constructor stub
		mDatas = datas;
		mFriendsSections = friendsSections;
		mFriendsPositions = friendsPositions;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PanddHeaderListView) {
			((PanddHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {

	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mFriendsSections.toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= mFriendsSections.size()) {
			return -1;
		}
		return mFriendsPositions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mFriendsPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}

	/**
	 * 根据侧边点击事件获取选中项的方法
	 * 
	 * @param section
	 */
	public int getPositionForsideSwction(int section) {
		return 0;
	}
}
