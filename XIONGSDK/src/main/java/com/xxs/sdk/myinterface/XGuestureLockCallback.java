package com.xxs.sdk.myinterface;

/**
 * 自定义手势密码锁回调接口
 * 
 * @author xiongxs
 * @date 2015-06-16
 */
public interface XGuestureLockCallback {
	/** 选取手势密码点长度不足 */
	public static final int POINT_LENGTH_SHORT = 0x01;
	/** 两次输入手势密码不一致 */
	public static final int TWICE_NOT_SAME = 0x02;
	/** 两次输入手势密一致 */
	public static final int TWICE_LINE_SAME = 0x03;
	/** 第一条手势密码设置完成 */
	public static final int FIRST_LINE_OVER = 0x04;

	/**
	 * 手势密码回调函数
	 * 
	 * @param type
	 *            <ul>
	 *            <li>1 {@link XGuestureLockCallback.POINT_LENGTH_SHORT}
	 *            绘制长度不足（提示最少选择四个点）</li>
	 *            <li>2 {@link XGuestureLockCallback.TWICE_NOT_SAME} 密码确认错误
	 *            （提示语上次输入密码不符）</li>
	 *            <li>3 {@link XGuestureLockCallback.TWICE_LINE_SAME}
	 *            两次输入手势密一致（提示手势密码设置成功）</li>
	 *            <li>4 {@link XGuestureLockCallback.FIRST_LINE_OVER}
	 *            第一条手势密码设置完成（进入手势密码验证）</li>
	 *            </ul>
	 */
	public void onXGuestureLockCallback(int type);
}
