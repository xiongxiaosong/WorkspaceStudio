package com.bestbrand.hh.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestbrand.hh.R;
import com.bestbrand.hh.bean.BeanFragmentHhChild;
import com.bestbrand.hh.bean.BeanFragmentHhGroup;
import com.xxs.sdk.adapter.XExpandableViewAdapter;
import com.xxs.sdk.util.ImageLoadUtil;
import com.xxs.sdk.view.XExpandableView;

/**
 * 自定义互互好友列表展示界面适配器
 * 
 * @author xiongxs
 * @date 2015-04-11
 */
public class AdapterFragmentHh extends XExpandableViewAdapter {
	/** 上下文 */
	private Context mContext;

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
	public AdapterFragmentHh(Context context, ArrayList<Object> groups,
			ArrayList<ArrayList<Object>> children,
			XExpandableView xExpandableView) {
		super(context, groups, children, xExpandableView);
		this.mContext = context;
	}

	/**
	 * 设置分组内容的方法
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderGroup viewHolderGroup;
		if (convertView == null) {
			viewHolderGroup = new ViewHolderGroup();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_fragment_hh_group, parent, false);
			viewHolderGroup.grouptitle = (TextView) convertView
					.findViewById(R.id.item_fragment_hh_group_title);
			viewHolderGroup.frendnumber = (TextView) convertView
					.findViewById(R.id.item_fragment_hh_group_number);
			viewHolderGroup.arrow = (ImageView) convertView
					.findViewById(R.id.item_fragment_hh_group_arrow);
			convertView.setTag(viewHolderGroup);
		} else {
			viewHolderGroup = (ViewHolderGroup) convertView.getTag();
		}
		if (getGroup(groupPosition) != null) {
			BeanFragmentHhGroup bean = (BeanFragmentHhGroup) getGroup(groupPosition);
			viewHolderGroup.frendnumber.setText(bean.getOnlinefrend() + "/"
					+ bean.getTotlefrend());
			viewHolderGroup.grouptitle.setText(bean.getGroupname());
			if (isExpanded) {
				viewHolderGroup.arrow.setImageResource(R.drawable.frendgroup_expanded);
			} else {
				viewHolderGroup.arrow.setImageResource(R.drawable.frendgroup_unexpanded);
			}
		}
		return convertView;
	}

	/**
	 * 设置二级列表内容的方法
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderChild viewHolderChild;
		if (convertView == null) {
			viewHolderChild = new ViewHolderChild();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_fragment_hh_child, parent, false);
			viewHolderChild.name = (TextView) convertView
					.findViewById(R.id.item_fragment_hh_child_name);
			viewHolderChild.phone = (TextView) convertView
					.findViewById(R.id.item_fragment_hh_child_phone);
			viewHolderChild.occupation = (TextView) convertView
					.findViewById(R.id.item_fragment_hh_child_occupation);
			viewHolderChild.photo = (ImageView) convertView
					.findViewById(R.id.item_fragment_hh_child_image);
			convertView.setTag(viewHolderChild);
		} else {
			viewHolderChild = (ViewHolderChild) convertView.getTag();
		}
		if (getChild(groupPosition, childPosition) != null) {
			BeanFragmentHhChild bean = (BeanFragmentHhChild) getChild(
					groupPosition, childPosition);
			ImageLoadUtil.getMethod().loadImageMethod(viewHolderChild.photo, bean.getPhotourl(),
					R.drawable.ic_launcher, R.drawable.empty_image,
					R.drawable.empty_image);
			viewHolderChild.name.setText(bean.getName());
			viewHolderChild.phone.setText(bean.getPhone());
			viewHolderChild.occupation.setText(bean.getOccupation());
		}
		return convertView;
	}

	/**
	 * 设置header的方法
	 */
	@Override
	public void setTreeHeaderState(View header, int groupPosition,
			int childPosition, int alpha) {
		// TODO Auto-generated method stub
//		super.setTreeHeaderState(header, groupPosition, childPosition, alpha);
		TextView grouptitle = (TextView) header
				.findViewById(R.id.item_fragment_hh_header_title);
		TextView frendnumber = (TextView) header
				.findViewById(R.id.item_fragment_hh_header_number);
		BeanFragmentHhGroup bean = (BeanFragmentHhGroup) getGroup(groupPosition);
		grouptitle.setText(bean.getGroupname());
		frendnumber.setText(bean.getOnlinefrend() + "/"
				+ bean.getTotlefrend());
	}

	public static class ViewHolderGroup {
		/** 分组人数 */
		private TextView grouptitle;
		/** 在线/总人数 */
		private TextView frendnumber;
		/** 方向指示图片 */
		private ImageView arrow;
	}

	public static class ViewHolderChild {
		/** 名称 */
		private TextView name;
		/** 电话 */
		private TextView phone;
		/** 职称 */
		private TextView occupation;
		/** 头像 */
		private ImageView photo;
	}
}
