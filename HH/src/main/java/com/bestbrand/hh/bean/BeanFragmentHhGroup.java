package com.bestbrand.hh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 封装互互界面一级分组信息的对象类
 * 
 * @author xiongxs
 * @date 2015-04-11
 */
public class BeanFragmentHhGroup implements Parcelable {
	/** 分组名称 */
	private String groupname;
	/** 总人数 */
	private int totlefrend;
	/** 在线人数 */
	private int onlinefrend;

	 public BeanFragmentHhGroup() {
	
	 }

	/** 获取分组名称 */
	public String getGroupname() {
		return groupname;
	}

	/** 设置分组名称 */
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	/** 获取总人数 */
	public int getTotlefrend() {
		return totlefrend;
	}

	/** 设置总人数 */
	public void setTotlefrend(int totlefrend) {
		this.totlefrend = totlefrend;
	}

	/** 获取在线人数 */
	public int getOnlinefrend() {
		return onlinefrend;
	}

	/** 设置在线人数 */
	public void setOnlinefrend(int onlinefrend) {
		this.onlinefrend = onlinefrend;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	// 实现Parcelable的方法writeToParcel，将ParcelableDate序列化为一个Parcel对象
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(groupname);
		out.writeInt(totlefrend);
		out.writeInt(onlinefrend);
	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<BeanFragmentHhGroup> CREATOR = new Creator<BeanFragmentHhGroup>() {
		@Override
		public BeanFragmentHhGroup[] newArray(int size) {
			return new BeanFragmentHhGroup[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public BeanFragmentHhGroup createFromParcel(Parcel in) {
			return new BeanFragmentHhGroup(in);
		}
	};

	public BeanFragmentHhGroup(Parcel in) {
		groupname = in.readString();
		totlefrend = in.readInt();
		onlinefrend = in.readInt();
	}
}
