package com.xxs.sdk.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * 封装手势图标点集对象
 * 
 * @author xiongxs
 * @date 2015-06-15
 */
public class XGuestLockInfo implements Parcelable {
	/** 正常状态 */
	public static final int IMAGE_NORMAL = 0x01;
	/** 选中状态 */
	public static final int IMAGE_SELECTED = 0x02;
	/** 错误状态 */
	public static final int IMAGE_ERROR = 0x03;
	/** 左边X的值 */
	private float leftX;
	/** 右边X的值 */
	private float rightX;
	/** 上边Y的值 */
	private float topY;
	/** 下边Y的值 */
	private float bottomY;
	/** 中心X的值 */
	private float centerX;
	/** 中心Y的值 */
	private float centerY;
	/** 当前序号 */
	private int position;
	/** 当前状态 */
	private int state;
	private ImageView imageView;

	public XGuestLockInfo() {

	}

	/** 获取左边X的值 */
	public float getLeftX() {
		return leftX;
	}

	/** 设置左边X的值 */
	public void setLeftX(float leftX) {
		this.leftX = leftX;
	}

	/** 获取右边X的值 */
	public float getRightX() {
		return rightX;
	}

	/** 设置右边X的值 */
	public void setRightX(float rightX) {
		this.rightX = rightX;
	}

	/** 获取上边Y的值 */
	public float getTopY() {
		return topY;
	}

	/** 设置上边Y的值 */
	public void setTopY(float topY) {
		this.topY = topY;
	}

	/** 获取下边Y的值 */
	public float getBottomY() {
		return bottomY;
	}

	/** 设置下班Y的值 */
	public void setBottomY(float bottomY) {
		this.bottomY = bottomY;
	}

	/** 获取中心X的值 */
	public float getCenterX() {
		return centerX;
	}

	/** 设置中心X的值 */
	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	/** 获取中心Y的值 */
	public float getCenterY() {
		return centerY;
	}

	/** 设置中心Y的值 */
	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}

	/** 获取当前序号 */
	public int getPosition() {
		return position;
	}

	/** 设置当前序号 */
	public void setPosition(int position) {
		this.position = position;
	}

	/** 获取当前状态 */
	public int getState() {
		return state;
	}

	/** 设置当前状态 */
	public void setState(int state) {
		this.state = state;
	}

	/** 获取ImageView对象 */
	public ImageView getImageView() {
		return imageView;
	}

	/** 设置ImageView对象 */
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeFloat(leftX);
		out.writeFloat(rightX);
		out.writeFloat(topY);
		out.writeFloat(bottomY);
		out.writeFloat(centerX);
		out.writeFloat(centerY);
		out.writeInt(position);
		out.writeInt(state);
		out.writeValue(imageView);
	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<XGuestLockInfo> CREATOR = new Creator<XGuestLockInfo>() {
		@Override
		public XGuestLockInfo[] newArray(int size) {
			return new XGuestLockInfo[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public XGuestLockInfo createFromParcel(Parcel in) {
			return new XGuestLockInfo(in);
		}
	};

	public XGuestLockInfo(Parcel in) {
		leftX = in.readFloat();
		rightX = in.readFloat();
		topY = in.readFloat();
		bottomY = in.readFloat();
		centerX = in.readFloat();
		centerY = in.readFloat();
		position = in.readInt();
		state = in.readInt();
		imageView = (ImageView) in.readValue(XGuestLockInfo.class
				.getClassLoader());
	}
}
