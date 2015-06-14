package com.bestbrand.hh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 封装登录返回Data数据的JAVABEAN对象
 * 
 * @author xiongxs
 * @date 2015-05-14
 */
public class BeanLoginData implements Parcelable {
	public BeanLoginData() {

	}

	/** 用户名 */
	private String username;
	/** 用户昵称 */
	private String nickname;
	/** 用户真实姓名 */
	private String realname;
	/** 头像图片地址 */
	private String photo;

	/** 获取用户名称 */
	public String getUsername() {
		return username;
	}

	/** 设置用户名称 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** 获取用户昵称 */
	public String getNickname() {
		return nickname;
	}

	/** 设置用户昵称 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/** 获取用户真实姓名 */
	public String getRealname() {
		return realname;
	}

	/** 设置用户真实姓名 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/** 获取用户头像地址URL */
	public String getPhoto() {
		return photo;
	}

	/** 设置用户头像地址URL */
	public void setPhoto(String photo) {
		this.photo = photo;
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
		out.writeString(username);
		out.writeString(nickname);
		out.writeString(realname);
		out.writeString(photo);
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

	public BeanLoginData(Parcel in) {
		username = in.readString();
		nickname = in.readString();
		realname = in.readString();
		photo = in.readString();
	}

}
