package com.bestbrand.hh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 封装互互界面二级列表信息的对象类
 * 
 * @author xiongxs
 * @date 2015-04-11
 */
public class BeanFragmentHhChild implements Parcelable {
	/** 名称 */
	private String name;
	/** 电话 */
	private String phone;
	/** 职业 */
	private String occupation;
	/** 头像地址 */
	private String photourl;

	public BeanFragmentHhChild() {

	}

	/** 获取名称 */
	public String getName() {
		return name;
	}

	/** 设置名称 */
	public void setName(String name) {
		this.name = name;
	}

	/** 获取电话号码 */
	public String getPhone() {
		return phone;
	}

	/** 设置电话号码 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/** 获取职称 */
	public String getOccupation() {
		return occupation;
	}

	/** 设置职称 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/** 获取头像地址 */
	public String getPhotourl() {
		return photourl;
	}

	/** 设置头像地址 */
	public void setPhotourl(String photourl) {
		this.photourl = photourl;
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
		out.writeString(name);
		out.writeString(phone);
		out.writeString(occupation);
		out.writeString(photourl);
	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<BeanFragmentHhChild> CREATOR = new Creator<BeanFragmentHhChild>() {
		@Override
		public BeanFragmentHhChild[] newArray(int size) {
			return new BeanFragmentHhChild[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public BeanFragmentHhChild createFromParcel(Parcel in) {
			return new BeanFragmentHhChild(in);
		}
	};

	public BeanFragmentHhChild(Parcel in) {
		name = in.readString();
		phone = in.readString();
		occupation = in.readString();
		photourl = in.readString();
	}

}
